package com.project.wecare.screens.viewClaims;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.project.wecare.R;
import com.project.wecare.database.claims.ClaimDatabaseManager;
import com.project.wecare.database.claims.ClaimManager;
import com.project.wecare.database.users.UserManager;
import com.project.wecare.database.vehicles.VehiclesManager;
import com.project.wecare.helpers.ClaimRecViewAdapter;
import com.project.wecare.interfaces.ItemClickListener;
import com.project.wecare.models.Claim;
import com.project.wecare.models.Evidence;
import com.project.wecare.models.Vehicle;
import com.project.wecare.screens.BaseActivity;
import com.project.wecare.screens.login.LoginActivity;
import com.project.wecare.screens.newClaimForm.ClaimActivity;
import com.project.wecare.screens.viewVehicles.VehiclesActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public class ViewClaimsListActivity extends BaseActivity implements ItemClickListener {

    private TextView titleInfo;
    private TextView claimsInfo;
    private TextView tv_vehicleTitle;
    private final StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    //Firebase submission progress
    ProgressDialog progressDialog;

    private ClaimManager claimManager;

    private TextView tv_model;
    private TextView tv_year;
    private TextView tv_insuranceType;
    private TextView tv_insuredDate;
    private String regNumber;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_claims_list);

        // action bar initialize
        ActionBar actionBar = getSupportActionBar();
        Objects.requireNonNull(actionBar).setDisplayHomeAsUpEnabled(true);

        claimManager = ClaimManager.getInstance();
        claimManager.setSharedPref(ViewClaimsListActivity.this);

        Intent intent = getIntent();
        regNumber = intent.getStringExtra("regNumber");

        RecyclerView claimRecView = findViewById(R.id.claimRecView);
        titleInfo = findViewById(R.id.titleInfo);
        claimsInfo = findViewById(R.id.claimsInfo);
        tv_vehicleTitle = findViewById(R.id.titleVehicle);
        tv_model = findViewById(R.id.txt_vehicleModel);
        tv_year = findViewById(R.id.txt_vehicleYear);
        tv_insuranceType = findViewById(R.id.txt_insuranceType);
        tv_insuredDate = findViewById(R.id.txt_insuranceDate);

        tv_vehicleTitle.setText("Vehicle : " + regNumber);

        setVehicleDetails(regNumber);

        ArrayList<Claim> claims = claimManager.initializeQueue(this);

        ArrayList<String> regNumbers = UserManager.getInstance().getCurrentUser().getVehiclesRegNumber();
        Log.d("Claim", "claim id numbers" + regNumbers.toString());

        ClaimRecViewAdapter adapter = new ClaimRecViewAdapter();
        adapter.setClaims(claims);
        adapter.setClickListener((ItemClickListener) ViewClaimsListActivity.this);

        claimRecView.setAdapter(adapter);
        claimRecView.setLayoutManager(new GridLayoutManager(this, 1));
    }

    @Override
    public void onClick(View view, int position) {
        // The onClick implementation of the RecyclerView item click
        Claim claim = ClaimManager.getInstance().getQueue().get(position);

        Intent intent = new Intent(ViewClaimsListActivity.this, ViewClaimActivity.class);
        intent.putExtra("claimNumber", claim.getClaimId());
        intent.putExtra("regNumber", regNumber);
        startActivity(intent);
    }

    @Override
    public void onButtonClick(View view, int position) {
//        Toast.makeText(this, "Resubmit", Toast.LENGTH_SHORT).show();
        if (isConnected()) {
            Claim claim = ClaimManager.getInstance().getQueue().get(position);
            submitClaimToDatabase(claim);
        }else{
            //Prompt an alert dialogue to user for verification
            new AlertDialog.Builder(this)
                    .setTitle("No Internet")
                    .setMessage("Please check your internet connection and try again.")

                    // Specifying a listener allows you to take an action before dismissing the dialog.
                    // A null listener allows the button to dismiss the dialog and take no further action.
                    .setNegativeButton("OK", null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }


    @SuppressLint("SetTextI18n")
    private void setVehicleDetails(String regNumber) {
        Vehicle v = VehiclesManager.getInstance().getVehicleByRegNumber(regNumber);
        tv_model.setText("Model : " + v.getModel().toString());
        tv_year.setText("Year : " + v.getYear().toString());
        tv_insuranceType.setText("Insurance type : " + v.getInsuranceType().toString());
        tv_insuredDate.setText("Insured date : " + "05/10/2020"); // Todo: Add the real date
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setTitle(resources.getString(R.string.app_name));
        tv_vehicleTitle.setText(resources.getString(R.string.txt_vehicles) + " " + regNumber);
        titleInfo.setText(resources.getString(R.string.txt_basicDetails));
        claimsInfo.setText(resources.getString(R.string.txt_Claims));

        Vehicle v = VehiclesManager.getInstance().getVehicleByRegNumber(regNumber);
        tv_model.setText(resources.getString(R.string.txt_model) + " : " + v.getModel().toString());
        tv_year.setText(resources.getString(R.string.yearTxt) + " : " + v.getYear().toString());
        tv_insuranceType.setText(resources.getString(R.string.insurance_type) + " : " + v.getInsuranceType().toString());
        tv_insuredDate.setText(resources.getString(R.string.insurance_date) + " : "+"05/10/2020"); // Todo: Add the real date
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:

                //Prompt an alert dialogue to user for verification
                new AlertDialog.Builder(this)
                        .setTitle("Confirmation logout")
                        .setMessage("Are you sure you want to logout?")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //Update locally stored user authentication info
                                UserManager.getInstance().logoutUser(ViewClaimsListActivity.this);

                                //Update the firebase user info
                                FirebaseAuth.getInstance().signOut();
                                Intent intent = new Intent(ViewClaimsListActivity.this, LoginActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_lock_power_off)
                        .show();

                return true;

            case R.id.action_new_claim:
                Intent intent = new Intent(ViewClaimsListActivity.this, ClaimActivity.class);
                intent.putExtra("regNumber", regNumber);
                startActivity(intent);
                return true;

            default:
                startActivity(new Intent(ViewClaimsListActivity.this, VehiclesActivity.class));
                return super.onOptionsItemSelected(item);

        }
    }

    public boolean isConnected() {
        try {
            String command = "ping -c 1 google.com";
            return Runtime.getRuntime().exec(command).waitFor() == 0;
        } catch (Exception e) {
            Log.d("Wecare", e.toString());
            return false;
        }
    }

    public void submitClaimToDatabase(Claim claim) {

        progressDialog = new ProgressDialog(ViewClaimsListActivity.this);
        progressDialog.setMessage("uploading..."); // Setting Message
        progressDialog.setTitle("Claim Submission"); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog

        ClaimDatabaseManager.getInstance().addClaim(claim,
                new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Upload photos to firebase storage and store the remote uri
                        uploadPhotos(claim, 0);
                        uploadPhotos(claim, 1);
                        uploadPhotos(claim, 2);
                    }
                },
                new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(ViewClaimsListActivity.this, "Submission failed.Try again later", Toast.LENGTH_LONG).show();
                    }
                });


    }


    public void uploadPhotos(Claim claim, int evidenceType) {
        //regNumber/claimId/ownVehicleEvidence

        String regNumber = claim.getOwnVehicleRegNumber();
        String claimId = claim.getClaimId();

        ArrayList<Evidence> evidences;
        String evidenceName;

        if (evidenceType == 0) {
            //own vehicle damage
            evidences = claim.getOwnVehicleDamageEvidences();
            evidenceName = "ownVehicleDamageEvidences";
        } else if (evidenceType == 1) {
            //other vehicle damage
            evidences = claim.getOtherVehicleDamageEvidences();
            evidenceName = "otherVehicleDamageEvidences";
        } else {
            //property damage
            evidences = claim.getPropertyDamageEvidences();
            evidenceName = "propertyDamageEvidences";
        }


        for (int i = 0; i < evidences.size(); i++) {

            Evidence e = evidences.get(i);
            Uri uri = Uri.fromFile(new File(e.getImagePath()));

            StorageReference imageRef = storageReference.child(regNumber).child(claimId).child(evidenceName).child(uri.getLastPathSegment());
            UploadTask uploadTask = imageRef.putFile(uri);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("Wecare", "Error : " + e.toString());
                    progressDialog.dismiss();
                    Toast.makeText(ViewClaimsListActivity.this, "File Upload Failed. Try again later", Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Uri downloadUri = uri;
                            String remoteUri = downloadUri.toString();
                            e.setRemoteUri(remoteUri);
                            Log.d("Wecare", "Remote uri : " + remoteUri);


                            if (evidenceType == 0) {
                                //If own vehicle damage, increment the count of uploaded images
                                claim.incOwnVehicleEvidenceUploadedCount();

                            } else if (evidenceType == 1) {
                                //If other vehicle damage, increment the count of uploaded images
                                claim.incOtherVehicleEvidenceUploadedCount();
                            } else {
                                //If property damage, increment the count of uploaded images
                                claim.incPropertyEvidenceUploadedCount();
                            }

                            ClaimDatabaseManager.getInstance().addEvidence(claim.getClaimId(), evidenceName, e,
                                    new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            if (claim.isAllEvidencesSubmitted()) {
                                                Toast.makeText(ViewClaimsListActivity.this, "Claim successfully uploaded", Toast.LENGTH_SHORT).show();
                                                claim.setState(1);
                                                // save claim in local storage
                                                claimManager.getSharedPref().storeClaim(claim.getClaimId(), claim);
                                                claimManager.getSharedPref().storeClaimId(claim.getClaimId(), UserManager.getInstance().getCurrentUser().getNic());

                                                progressDialog.dismiss();

                                                //Redirect
                                                Intent intent = new Intent(ViewClaimsListActivity.this, ViewClaimsListActivity.class);
                                                intent.putExtra("regNumber", claim.getOwnVehicleRegNumber());
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(intent);
                                            }
                                        }
                                    },
                                    new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            progressDialog.dismiss();
                                            Toast.makeText(ViewClaimsListActivity.this, "Submission failed.Try again later", Toast.LENGTH_LONG).show();
                                        }
                                    });

                        }
                    });
                }
            });
        }
    }
}