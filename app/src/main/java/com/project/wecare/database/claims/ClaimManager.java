package com.project.wecare.database.claims;

import android.content.Context;
import android.util.Log;

//import com.example.govimithuruapp.accountManagement.AuthController;

import com.project.wecare.database.users.UserManager;
import com.project.wecare.models.Claim;
import com.project.wecare.services.GPSTracker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ClaimManager {

    private GPSTracker gps;
    private ArrayList<Claim> queue; // claims to be submitted
    private Claim currentClaim;
    private boolean accidentDetails;
    private boolean accidentEvidence;
    private boolean thirdPartDetails;
    private boolean thirdPartyEvidence;

    // Singleton
    private static ClaimManager instance;

    private ClaimManager() {
        queue = new ArrayList<>();
        accidentDetails = false;
        accidentEvidence = false;
        thirdPartDetails = false;
        thirdPartyEvidence = false;
    }

    public static ClaimManager getInstance() {
        if (instance == null) {
            Log.d("MAD", "getInstance: ClaimManager");
            instance = new ClaimManager();
        }
        return instance;
    }

    public Claim createNewClaim() {
        String userNIC = UserManager.getInstance().getCurrentUser().getNic();
        String currentTimestamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        currentTimestamp = currentTimestamp.replace(".","");
        String claimId = userNIC + currentTimestamp;

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

    public boolean isAccidentDetails() {
        return accidentDetails;
    }

    public void setAccidentDetails(boolean accidentDetails) {
        this.accidentDetails = accidentDetails;
    }

    public boolean isAccidentEvidence() {
        return accidentEvidence;
    }

    public void setAccidentEvidence(boolean accidentEvidence) {
        this.accidentEvidence = accidentEvidence;
    }

    public boolean isThirdPartDetails() {
        return thirdPartDetails;
    }

    public void setThirdPartDetails(boolean thirdPartDetails) {
        this.thirdPartDetails = thirdPartDetails;
    }

    public boolean isThirdPartyEvidence() {
        return thirdPartyEvidence;
    }

    public void setThirdPartyEvidence(boolean thirdPartyEvidence) {
        this.thirdPartyEvidence = thirdPartyEvidence;
    }

}
