package com.project.wecare.screens.newClaimForm;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.project.wecare.R;
import com.project.wecare.database.claims.ClaimDatabaseManager;
import com.project.wecare.database.claims.ClaimManager;
import com.project.wecare.database.users.UserManager;
import com.project.wecare.models.Claim;
import com.project.wecare.models.Evidence;
import com.project.wecare.screens.viewClaims.ViewClaimsListActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

public class Claim3Activity extends AppCompatActivity {

    ClaimManager claimManager;

    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    ProgressDialog progressDialog;

    private CheckBox cb_pledgeChecked ;
    private Button btn_submitClaim ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_claim3);
        setTitle("New Claim : Step 5");

        cb_pledgeChecked = findViewById(R.id.cbCheckPledge);
        btn_submitClaim = findViewById(R.id.btnSubmitClaim);

        claimManager = ClaimManager.getInstance();
        Claim claim = claimManager.getCurrentClaim();
        claim.setDate(new Date());

        btn_submitClaim.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (cb_pledgeChecked.isChecked()){
//                    Intent intent = new Intent(Claim3Activity.this , Claim4Activity.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(intent);
                    if(isConnected()){
                        submitClaimToDatabase(claim);
                    }else{
                        // save claim in local storage
                        claimManager.getSharedPref().storeClaim(claim.getClaimId(), claim);
                        claimManager.getSharedPref().storeClaimId(claim.getClaimId(), UserManager.getInstance().getCurrentUser().getNic());

                        Toast.makeText(Claim3Activity.this, "No internet connection. Claim stored locally.",
                                Toast.LENGTH_LONG).show();

                        //Define intents to redirect
                        Intent intent=new Intent(Claim3Activity.this, ViewClaimsListActivity.class);
                        intent.putExtra("regNumber",claim.getOwnVehicleRegNumber());
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        //Update the claim manager
                        ClaimManager claimManager=ClaimManager.getInstance();
                        claimManager.setCurrentClaim(null);
                        claimManager.setAccidentDetails(false);
                        claimManager.setAccidentEvidence(false);
                        claimManager.setThirdPartDetails(false);
                        claimManager.setThirdPartyEvidence(false);

                        //redirect
                        startActivity(intent);
                    }
                }else{
                    Toast.makeText(Claim3Activity.this, "Please check the pledge to submit the form.",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public boolean isConnected(){
        try{
            String command = "ping -c 1 google.com";
            return Runtime.getRuntime().exec(command).waitFor() == 0;
        }catch(Exception e){
            Log.d("Wecare", e.toString());
            return false;
        }
    }

    public void submitClaimToDatabase(Claim claim){

        progressDialog = new ProgressDialog(Claim3Activity.this);
        progressDialog.setMessage("uploading..."); // Setting Message
        progressDialog.setTitle("Claim Submission"); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog

        ClaimDatabaseManager.getInstance().addClaim(claim,
                new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Wecare", "Successfully submitted the claim general information.");
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
                        Toast.makeText(Claim3Activity.this, "Submission failed. Stored locally ", Toast.LENGTH_LONG).show();
                    }
                });


    }


    public void uploadPhotos ( Claim claim, int evidenceType){
        //regNumber/claimId/ownVehicleEvidence

        String regNumber = claim.getOwnVehicleRegNumber();
        String claimId = claim.getClaimId();

        ArrayList<Evidence> evidences;
        String evidenceName ;

        if(evidenceType == 0){
            //own vehicle damage
            evidences = claim.getOwnVehicleDamageEvidences();
            evidenceName = "ownVehicleDamageEvidences";
        }else if(evidenceType == 1){
            //other vehicle damage
            evidences  = claim.getOtherVehicleDamageEvidences();
            evidenceName = "otherVehicleDamageEvidences";
        }else{
            //property damage
            evidences  = claim.getPropertyDamageEvidences();
            evidenceName = "propertyDamageEvidences";
        }


        for (int i =0 ; i< evidences.size(); i++){

            Evidence e = evidences.get(i);
            Uri uri = Uri.fromFile(new File(e.getImagePath()));

            StorageReference  imageRef = storageReference.child(regNumber).child(claimId).child(evidenceName).child(uri.getLastPathSegment());
            UploadTask uploadTask = imageRef.putFile(uri);

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("Wecare", "Error : "+ e.toString());
                    Log.d("Wecare", "File Upload Failed");
                    progressDialog.dismiss();

                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>(){
                        @Override
                        public void onSuccess(Uri uri){
                            Uri downloadUri=uri;
                            String remoteUri=downloadUri.toString();
                            e.setRemoteUri(remoteUri);
                            Log.d("Wecare","Remote uri : "+remoteUri);


                            if(evidenceType==0){
                                //If own vehicle damage, increment the count of uploaded images
                                claim.incOwnVehicleEvidenceUploadedCount();

                            }else if(evidenceType==1){
                                //If other vehicle damage, increment the count of uploaded images
                                claim.incOtherVehicleEvidenceUploadedCount();
                            }else{
                                //If property damage, increment the count of uploaded images
                                claim.incPropertyEvidenceUploadedCount();
                            }

                            ClaimDatabaseManager.getInstance().addEvidence(claim.getClaimId(),evidenceName, e,
                                    new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            if(claim.isAllEvidencesSubmitted()){

                                                Log.d("Wecare","All evidence files Uploaded Successfully");

                                                claim.setState(1);
                                                // save claim in local storage
                                                claimManager.getSharedPref().storeClaim(claim.getClaimId(), claim);
                                                claimManager.getSharedPref().storeClaimId(claim.getClaimId(), UserManager.getInstance().getCurrentUser().getNic());

                                                //Update the claim manager
                                                ClaimManager claimManager=ClaimManager.getInstance();
                                                claimManager.setCurrentClaim(null);
                                                claimManager.setAccidentDetails(false);
                                                claimManager.setAccidentEvidence(false);
                                                claimManager.setThirdPartDetails(false);
                                                claimManager.setThirdPartyEvidence(false);

                                                progressDialog.dismiss();

                                                //Redirect
                                                Intent intent=new Intent(Claim3Activity.this,ViewClaimsListActivity.class);
                                                intent.putExtra("regNumber",claim.getOwnVehicleRegNumber());
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(intent);
                                            }
                                        }
                                    },
                                    new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            claim.setState(0);
                                            progressDialog.dismiss();
                                        }
                                    });

                        }
                    });
                }
            });
        }
    }
}