package com.project.wecare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.exifinterface.media.ExifInterface;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import com.project.wecare.helpers.ImageViewAdapter;
import com.project.wecare.models.Claim;
import com.project.wecare.models.Evidence;
import com.project.wecare.services.GPSTracker;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class Record2Activity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int NO_OF_GRIDS = 2;

    private String currentPhotoPath;
    private Integer currentPosition;
    private boolean isVehicleEvidenceView = false;

    private GridView gridViewProperty;
    private GridView gridViewVehicle;
    private TextView propertyEvidenceTitle;
    private TextView vehicleEvidenceTitle;
    private ArrayList<Evidence> propertyDamageEvidences;
    private ArrayList<Evidence> otherVehicleDamageEvidences;

    private ClaimManager claimManager;
    private GPSTracker gps;
    private Claim currentClaim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record2);
        setTitle("Evidence: Third Party Property Damage");

        // action bar initialize
        ActionBar actionBar = getSupportActionBar();
        Objects.requireNonNull(actionBar).setDisplayHomeAsUpEnabled(true);

        // get the currently processing claim
        claimManager = ClaimManager.getInstance();
        currentClaim = claimManager.getCurrentClaim();
        gps = claimManager.getGps();

        // set currentClaim
        this.getDataFromClaimManager();

        // initialize image grid view recycler view
        ImageViewAdapter adapterProperty = new ImageViewAdapter(this, R.layout.image_grid_item, propertyDamageEvidences);
        gridViewProperty = findViewById(R.id.gatherPropertyEvidence);
        propertyEvidenceTitle = findViewById(R.id.propertyEvidenceTitle);

        gridViewProperty.setAdapter(adapterProperty);
        gridViewProperty.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                isVehicleEvidenceView = false;
                if (!currentClaim.isPropertyDamage())
                    view.setEnabled(false);
                else
                    dispatchTakePictureIntent(view, position);
            }
        });

        // initialize image grid view recycler view
        ImageViewAdapter adapterVehicle = new ImageViewAdapter(this, R.layout.image_grid_item, otherVehicleDamageEvidences);
        gridViewVehicle = findViewById(R.id.gatherVehicleEvidence);
        vehicleEvidenceTitle = findViewById(R.id.vehicleEvidenceTitle);

        gridViewVehicle.setAdapter(adapterVehicle);
        gridViewVehicle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                isVehicleEvidenceView = true;
                if (!currentClaim.getOtherVehicleDamaged())
                    view.setEnabled(false);
                else
                    dispatchTakePictureIntent(view, position);
            }
        });

        FloatingActionButton nextButton = findViewById(R.id.goToSubmitClaim);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValidate()) {
                    Intent intent = new Intent(Record2Activity.this, VehiclesActivity.class);
                    startActivity(intent);

                } else {
                    Toast.makeText(Record2Activity.this, "Please add one or more captures as evidence", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getDataFromClaimManager() {
        this.propertyDamageEvidences = currentClaim.getPropertyDamageEvidences();
        this.otherVehicleDamageEvidences = currentClaim.getOtherVehicleDamageEvidences();

        if (claimManager.isThirdPartyEvidence()) {
            if (this.propertyDamageEvidences.size() < NO_OF_GRIDS) {
                this.propertyDamageEvidences.add( new Evidence("", new Date(), 0.0, 0.0, ""));
            }

            if (this.otherVehicleDamageEvidences.size() < NO_OF_GRIDS) {
                this.otherVehicleDamageEvidences.add( new Evidence("", new Date(), 0.0, 0.0, ""));
            }

        } else {
            this.propertyDamageEvidences.add( new Evidence("", new Date(), 0.0, 0.0, ""));
            this.otherVehicleDamageEvidences.add( new Evidence("", new Date(), 0.0, 0.0, ""));
        }
    }

    private boolean isValidate() {
        boolean validate = true;
        if (currentClaim.isPropertyDamage()) {
            if (propertyDamageEvidences.size() == 1) {
                if (propertyDamageEvidences.get(0).getImagePath().equals("")) {
                    validate = false;
                    propertyEvidenceTitle.setError("Please add one or more evidences");
                }
            }
        }
        if (currentClaim.getOtherVehicleDamaged()) {
            if (otherVehicleDamageEvidences.size() == 1) {
                if (otherVehicleDamageEvidences.get(0).getImagePath().equals("")) {
                    vehicleEvidenceTitle.setError("Add one or more evidences");
                    validate = false;
                }
            }
        }
        if (validate) {
            claimManager.setThirdPartyEvidence(true);
            if (propertyDamageEvidences.get(propertyDamageEvidences.size() - 1).getImagePath().equals("")) {
                propertyDamageEvidences.remove(propertyDamageEvidences.size() - 1);
            }
            if (otherVehicleDamageEvidences.get(otherVehicleDamageEvidences.size() - 1).getImagePath().equals("")) {
                otherVehicleDamageEvidences.remove(otherVehicleDamageEvidences.size() - 1);
            }
        }

        return validate;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (isValidate())
            startActivity(new Intent(Record2Activity.this,Claim2Activity.class));
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            File imgFile = new  File(currentPhotoPath);
            if(imgFile.exists())
            {
                ImageView image;
                Evidence currentEvidence;
                if (isVehicleEvidenceView) {
                    image = this.gridViewVehicle.getChildAt(this.currentPosition).findViewById(R.id.image_id);
                    currentEvidence = otherVehicleDamageEvidences.get(this.currentPosition);

                } else {
                    image = this.gridViewProperty.getChildAt(this.currentPosition).findViewById(R.id.image_id);
                    currentEvidence = propertyDamageEvidences.get(this.currentPosition);
                }
                image.setImageURI(Uri.fromFile(imgFile));

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

                if (isVehicleEvidenceView) {
                    if (otherVehicleDamageEvidences.size() < NO_OF_GRIDS) {
                        otherVehicleDamageEvidences.add(new Evidence("", new Date(), 0.0, 0.0, ""));
                    }
                } else {
                    if (propertyDamageEvidences.size() < NO_OF_GRIDS) {
                        propertyDamageEvidences.add(new Evidence("", new Date(), 0.0, 0.0, ""));
                    }
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