package com.project.wecare.database.claims;

import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.project.wecare.models.Claim;
import com.project.wecare.models.Evidence;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ClaimDatabaseManager {

    private final FirebaseFirestore db;

    private String COLLECTION_CLAIMS = "claims";

    // Singleton
    private static ClaimDatabaseManager instance;

    private ClaimDatabaseManager() {
        db = FirebaseFirestore.getInstance();
    }

    public static ClaimDatabaseManager getInstance() {
        if (instance == null) instance = new ClaimDatabaseManager();
        return instance;
    }

    public void addClaim(Claim claim, OnSuccessListener<Void> successListenerObj, OnFailureListener failureListenerObj){

        Map<String, Object> newClaim = new HashMap<>();

        newClaim.put("claimId", claim.getClaimId());

        //driverdetails
        newClaim.put("driverName",claim.getDriverName());
        newClaim.put("driverNic",claim.getDriverNic());
        newClaim.put("driverAddress",claim.getDriverAddress());
        newClaim.put("driverLicencesNo",claim.getDriverLicencesNo());
        newClaim.put("driverContactNo",claim.getDriverContactNo());
        newClaim.put("driverLicenseExp", (claim.getDriverLicenseExp()));

        //accident details
        newClaim.put("ownVehicleDamagedRegions", claim.getOwnVehicleDamagedRegions() );
        newClaim.put("roadStatus",claim.getRoadStatus());
        newClaim.put("roadVisibility",claim.getRoadVisibility());
        newClaim.put("date" , claim.getDate());

        //own vehicle details
        newClaim.put("ownVehicleRegNumber",claim.getOwnVehicleRegNumber());
//        private ArrayList<Evidence> ownVehicleDamageEvidences;

        //3rd party vehicle damage details
        newClaim.put("isOtherVehicleDamaged",claim.getOtherVehicleDamaged());
        if (claim.getOtherVehicleDamaged()){
            newClaim.put("otherVehicleRegNumber", claim.getOtherVehicleRegNumber());
            newClaim.put("otherPartyDriverName", claim.getOtherPartyDriverName());
            newClaim.put("otherPartyDriverNumber", claim.getOtherPartyDriverNumber());
            newClaim.put("otherVehicleDamagedRegions", claim.getOtherVehicleDamagedRegions());
            newClaim.put("otherPartyAccNumber", claim.getOtherPartyAccNumber());
            newClaim.put("otherPartyBankName", claim.getOtherPartyBankName());
            newClaim.put("otherPartyBankBranch", claim.getOtherPartyBankBranch());
//            private ArrayList<Evidence> otherVehicleDamageEvidences;
        }


        // 3rd party property damage
        newClaim.put("isPropertyDamage",claim.isPropertyDamage());
        if(claim.isPropertyDamage()){
            newClaim.put("propertyContactPersonName",claim.getPropertyContactPersonName());
            newClaim.put("propertyContactPersonAddress",claim.getPropertyContactPersonAddress());
            newClaim.put("propertyContactPersonNumber",claim.getPropertyContactPersonNumber());
            newClaim.put("propertyDamage",claim.getPropertyDamage());
            newClaim.put("propertyContactPersonAccNumber",claim.getPropertyContactPersonAccNumber());
            newClaim.put("propertyContactPersonBankName",claim.getPropertyContactPersonBankName());
            newClaim.put("propertyContactPersonBankBranch",claim.getPropertyContactPersonBankBranch());
//            private ArrayList<Evidence> propertyDamageEvidences;
        }

        Log.d("Wecare", "Claim initialization for firebase success");

        // Add claims general informations
        db.collection(COLLECTION_CLAIMS)
                .document(claim.getClaimId())
                .set(newClaim)
                .addOnSuccessListener( successListenerObj)
                .addOnFailureListener(failureListenerObj);
    }


    public void addEvidence(String ClaimId, String evidenceType, Evidence evidence, OnSuccessListener<Void> successListenerObj, OnFailureListener failureListenerObj) {

        Map<String, Object> newEvidence = new HashMap<>();

        newEvidence.put("evidenceId", evidence.getEvidenceID());
        newEvidence.put("date", evidence.getDate());
        newEvidence.put("localUri", evidence.getImagePath());
        newEvidence.put("remoteUri", evidence.getRemoteUri());
        newEvidence.put("latitude", evidence.getLatitude());
        newEvidence.put("longitude", evidence.getLongitude());

        db.collection(COLLECTION_CLAIMS)
                .document(ClaimId)
                .collection(evidenceType)
                .document(evidence.getEvidenceID())
                .set(newEvidence)
                .addOnSuccessListener( successListenerObj)
                .addOnFailureListener(failureListenerObj);
    }

}
