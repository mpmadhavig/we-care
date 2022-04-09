package com.project.wecare.helpers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.HashSet;
import java.util.Set;

public class Helper {
    static Gson gson = new Gson();
    private static String CLAIM_ID_KEY = "claim-ids-";

    public static void storeData(Activity activity, String key, Object value) {
        // save in shared pref
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);

        // convert to json string
        String jsonValue = gson.toJson(value);

        // edit and store
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, jsonValue);
        editor.apply();
    }

    public static Object getData(Activity activity, String key) {
        // get from shared pref
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        // convert to object
        String jsonString = sharedPref.getString(key, "defaultValue");

        return gson.fromJson(jsonString, Object.class);
    }

    @SuppressLint("MutatingSharedPrefs")
    public static void storeClaimId(Activity activity, String newClaimId, String userId) {
        // save in shared pref
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);

        // add new claim id
        Set<String> claimIds = sharedPref.getStringSet(CLAIM_ID_KEY+userId, new HashSet<>());
        claimIds.add(newClaimId);

        // edit and store
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putStringSet(CLAIM_ID_KEY+userId, claimIds);
        editor.apply();
    }

    public static Set<String> getClaimIds(Activity activity, String userId) {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        return sharedPref.getStringSet(CLAIM_ID_KEY+userId, new HashSet<>());
    }
}
