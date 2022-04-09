package com.project.wecare.models;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.HashSet;
import java.util.Set;

public class SharedPreferenceModel {
    private static final String CLAIM_ID_KEY = "claim-ids-";

    private static SharedPreferenceModel instance;
    private final SharedPreferences sharedPref;

    static Gson gson = new Gson();

    private SharedPreferenceModel(Activity activity) {
        this.sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
    }

    public static SharedPreferenceModel getInstance(Activity activity){
        if (instance==null) {
            instance = new SharedPreferenceModel(activity);
        }
        return instance;
    }

    public void storeClaim(String key, Claim value) {
        // convert to json string
        String jsonValue = gson.toJson(value);

        // edit and store
        SharedPreferences.Editor editor = instance.sharedPref.edit();
        editor.putString(key, jsonValue);
        editor.apply();
    }

    public Claim getClaim(String key) {
        // convert to object
        String jsonString = instance.sharedPref.getString(key, "defaultValue");

        return gson.fromJson(jsonString, Claim.class);
    }

    @SuppressLint("MutatingSharedPrefs")
    public void storeClaimId(String newClaimId, String userId) {
        // add new claim id
        Set<String> claimIds = instance.sharedPref.getStringSet(CLAIM_ID_KEY+userId, new HashSet<>());
        claimIds.add(newClaimId);

        // edit and store
        SharedPreferences.Editor editor = instance.sharedPref.edit();
        editor.putStringSet(CLAIM_ID_KEY+userId, claimIds);
        editor.apply();
    }

    public Set<String> getClaimIds(String userId) {
        return instance.sharedPref.getStringSet(CLAIM_ID_KEY+userId, new HashSet<>());
    }
}
