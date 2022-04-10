package com.project.wecare.database.claims;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

//import com.example.govimithuruapp.accountManagement.AuthController;

import com.project.wecare.database.users.UserManager;
import com.project.wecare.models.Claim;
import com.project.wecare.models.SharedPreferenceModel;
import com.project.wecare.services.GPSTracker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;

public class ClaimManager {

    private GPSTracker gps;
    private ArrayList<Claim> queue; // all the claims
    private Claim currentClaim;
    private boolean accidentDetails;
    private boolean accidentEvidence;
    private boolean thirdPartDetails;
    private boolean thirdPartyEvidence;
    private SharedPreferenceModel sharedPref;

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

    public Claim createNewClaim(String regNumber) {
        String userNIC = UserManager.getInstance().getCurrentUser().getNic();
        @SuppressLint("SimpleDateFormat") String currentTimestamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        currentTimestamp = currentTimestamp.replace(".","");
        String claimId = userNIC + currentTimestamp;

        Claim claim =  new Claim(claimId);
        claim.setOwnVehicleRegNumber(regNumber);

        return claim;
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

    public ArrayList<Claim> initializeQueue(Activity activity) {
        // get stored claims
        this.queue = new ArrayList<>();
        Set<String> claims = sharedPref.getClaimIds(UserManager.getInstance().getCurrentUser().getNic());
        Toast.makeText(activity, "ids: " + claims.size(), Toast.LENGTH_SHORT).show();
        for (String claimId: claims ) {
            this.queue.add(sharedPref.getClaim(claimId));
        }
        return this.queue;
    }

    public ArrayList<Claim> getQueue() {
        return this.queue;
    }

    private void setQueue(ArrayList<Claim> queue) {
        this.queue = queue;
    }

    public Claim getClaimByRegNumber(String claimNumber){
        for (int i = 0; i < queue.size(); i++){
            Claim claim = queue.get(i);
            if (claim.getClaimId().equals(claimNumber)){
                return claim;
            }
        }
        return null;
    }

    public SharedPreferenceModel getSharedPref() {
        return this.sharedPref;
    }

    public void setSharedPref(Activity activity) {
        this.sharedPref = SharedPreferenceModel.getInstance(activity);
    }
}


