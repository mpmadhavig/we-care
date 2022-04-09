package com.project.wecare.screens.newClaimForm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

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

public class Claim4Activity extends AppCompatActivity {

    ClaimManager claimManager;

    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private Boolean isInternetAvailable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_claim4);

        claimManager = ClaimManager.getInstance();

        Claim claim = claimManager.getCurrentClaim();
        claim.setDate(new Date());

        isInternetAvailable = isConnected();

        Log.d("Wecare", isInternetAvailable.toString());

        if(isInternetAvailable){
            submitClaimToDatabase(claim);
        }else{
            // save claim in local storage
            claimManager.getSharedPref().storeClaim(claim.getClaimId(), claim);
            claimManager.getSharedPref().storeClaimId(claim.getClaimId(), UserManager.getInstance().getCurrentUser().getNic());

            //Define intents to redirect
            Intent intent=new Intent(Claim4Activity.this,ViewClaimsListActivity.class);
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

        ClaimDatabaseManager.getInstance().addClaim(claim,
                new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(Claim4Activity.this, "Successfully submitted the claim general information.", Toast.LENGTH_LONG).show();
                        // Upload photos to firebase storage and store the remote uri
                        uploadPhotos(claim, 0);
                        uploadPhotos(claim, 1);
                        uploadPhotos(claim, 2);
                    }
                },
                new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Claim4Activity.this, "Submission failed. Stored locally ", Toast.LENGTH_LONG).show();
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
                    Toast.makeText(Claim4Activity.this, "File Upload Failed", Toast.LENGTH_SHORT).show();
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
                                                Toast.makeText(Claim4Activity.this,"All evidence files Uploaded Successfully",Toast.LENGTH_SHORT).show();
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

                                                //Redirect
                                                Intent intent=new Intent(Claim4Activity.this,ViewClaimsListActivity.class);
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
                                        }
                                    });

                        }
                    });
                }
            });
        }
    }
}