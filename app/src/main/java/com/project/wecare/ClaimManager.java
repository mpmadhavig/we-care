package com.project.wecare;

import android.content.Context;
import android.location.Location;

//import com.example.govimithuruapp.accountManagement.AuthController;

import com.project.wecare.models.Claim;
import com.project.wecare.services.GPSTracker;

import java.util.ArrayList;
import java.util.PriorityQueue;

public class ClaimManager {

    private GPSTracker gps;
    private ArrayList<Claim> queue; // claims to be submitted
    private Claim currentClaim;

    // Singleton
    private static ClaimManager instance;

    private ClaimManager() {
        queue = new ArrayList<>();
    }

    public static ClaimManager getInstance() {
        if (instance == null) instance = new ClaimManager();
        return instance;
    }

    public Claim createNewClaim() {
        String claimId = "testclaim"; // Todo: Add a unique ID
        return new Claim(claimId);
    }

    public Claim getCurrentClaim() {
        return currentClaim;
    }

    public void setCurrentClaim(Claim currentClaim) {
        this.currentClaim = currentClaim;
    }

    public void submitClaim(Claim claim, Context context) {
        queue.add(claim);
//        claim.postClaimToBackend(context);
//        EvidenceManager.getInstance().uploadEvidences(claim, context);
    }

    public GPSTracker getGps() {
        return gps;
    }

    public void setGps(GPSTracker gps) {
        this.gps = gps;
    }
}
