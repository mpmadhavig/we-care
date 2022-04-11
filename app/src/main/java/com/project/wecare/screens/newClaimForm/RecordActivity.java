package com.project.wecare.screens.newClaimForm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.core.content.FileProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import androidx.exifinterface.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.project.wecare.R;
import com.project.wecare.database.claims.ClaimManager;
import com.project.wecare.helpers.ImageViewAdapter;
import com.project.wecare.models.Claim;
import com.project.wecare.models.Evidence;
import com.project.wecare.screens.Base2Activity;
import com.project.wecare.services.GPSTracker;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class RecordActivity extends Base2Activity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int NO_OF_GRIDS = 8;

    private String currentPhotoPath;
    private Integer currentPosition;

    private GridView gridView;
    private TextView evidenceTitle;
    private ArrayList<Evidence> evidenceArray;

    private GPSTracker gps;
    private ClaimManager claimManager;
    private Claim currentClaim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        setTitle("New Claim : Step 2");

        // action bar initialize
        ActionBar actionBar = getSupportActionBar();
        Objects.requireNonNull(actionBar).setDisplayHomeAsUpEnabled(true);

        // initialize local evidence array
        evidenceArray = new ArrayList<>();
        evidenceArray.add( new Evidence("", new Date(), 0.0, 0.0, ""));

        // get the currently processing claim
        claimManager = ClaimManager.getInstance();
        currentClaim = claimManager.getCurrentClaim();
        gps = claimManager.getGps();

        // set currentClaim
        this.getDataFromClaimManager();

        // initialize image grid view recycler view
        ImageViewAdapter adapter = new ImageViewAdapter(this, R.layout.image_grid_item, evidenceArray);
        gridView = findViewById(R.id.image_capture_grid_view);
        evidenceTitle = findViewById(R.id.evidenceTitle);

        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                dispatchTakePictureIntent(view, position);
            }
        });

        FloatingActionButton nextButton = findViewById(R.id.goToClaim2Activity);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValidate()) {
                    Intent intent = new Intent(RecordActivity.this, Claim2Activity.class);
                    startActivity(intent);

                } else {
                    Toast.makeText(RecordActivity.this, "Please add one or more captures as evidence", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void getDataFromClaimManager() {
        this.evidenceArray = currentClaim.getOwnVehicleDamageEvidences();
        if (claimManager.isAccidentEvidence()) {
            if (this.evidenceArray.size() < NO_OF_GRIDS) {
                this.evidenceArray.add(new Evidence("", new Date(), 0.0, 0.0, ""));
            }

        } else {
            this.evidenceArray.add(new Evidence("", new Date(), 0.0, 0.0, ""));
        }
    }

    // Todo: 0 -> 1
    private boolean isValidate() {
        boolean validate = true;
        if (evidenceArray.size() == 1) {
            if (evidenceArray.get(0).getImagePath().equals("")) {
                validate = false;
                evidenceTitle.setError("Add one or more evidences");
            }
        }

        if (validate) {
            claimManager.setAccidentEvidence(true);
            if (evidenceArray.get(evidenceArray.size() - 1).getImagePath().equals("")) {
                evidenceArray.remove(evidenceArray.size() - 1);
            }
        }

        return validate;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        
        if (isValidate())
            startActivity(new Intent(RecordActivity.this, ClaimActivity.class));
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            File imgFile = new  File(currentPhotoPath);
            if(imgFile.exists())
            {
                ImageView image = this.gridView.getChildAt(this.currentPosition).findViewById(R.id.image_id);
                image.setImageURI(Uri.fromFile(imgFile));
                Evidence currentEvidence = evidenceArray.get(this.currentPosition);
                currentEvidence.setEvidenceID(UUID.randomUUID().toString());
                currentEvidence.setDate(new Date());
                currentEvidence.setImagePath(currentPhotoPath);

                try {
                    final ExifInterface exifInterface = new ExifInterface(currentPhotoPath);
                    double[] latlng = exifInterface.getLatLong();
                    if (latlng != null) {
                        currentEvidence.setLatitude(latlng[0]);
                        currentEvidence.setLongitude(latlng[1]);
                    }
                    else {
                        currentEvidence.setLatitude(gps.getLatitude());
                        currentEvidence.setLongitude(gps.getLongitude());
                    }
                } catch (IOException e) {
                    Toast.makeText(gps, "Couldn't read exif info: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }

                if (evidenceArray.size() < NO_OF_GRIDS) {
                    evidenceArray.add(new Evidence("", new Date(), 0.0, 0.0, ""));
                }
            }
        }
    }

    @SuppressLint("QueryPermissionsNeeded")
    public void dispatchTakePictureIntent(View view, Integer position) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        currentPosition = position;

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.project.wecare",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }


}