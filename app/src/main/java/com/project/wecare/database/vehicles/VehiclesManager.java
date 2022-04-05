package com.project.wecare.database.vehicles;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.project.wecare.database.users.UserManager;
import com.project.wecare.models.Vehicle;

import java.util.ArrayList;
import java.util.HashMap;

public class VehiclesManager {

    private ArrayList<Vehicle> vehicles;

    private final String TAG = "VehiclesManager";

    // Singleton
    private static VehiclesManager instance;

    private  VehiclesManager() {
        this.vehicles = new ArrayList<>();
    }


    public static VehiclesManager getInstance() {
        if (instance == null) instance = new VehiclesManager();
        return instance;
    }

    public void setVehicles(ArrayList<Vehicle> vehicles){ this.vehicles = vehicles; }

    public ArrayList<Vehicle> getVehicles() {
        return vehicles;
    }
}
