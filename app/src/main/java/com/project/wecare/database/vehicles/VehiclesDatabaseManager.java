package com.project.wecare.database.vehicles;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class VehiclesDatabaseManager {

    private final FirebaseFirestore db;

    private String COLLECTION_VEHICLES = "vehicles";

    // Singleton
    private static VehiclesDatabaseManager instance;

    private VehiclesDatabaseManager() {
        db = FirebaseFirestore.getInstance();
    }

    public static VehiclesDatabaseManager getInstance() {
        if (instance == null) instance = new VehiclesDatabaseManager();
        return instance;
    }

    public void getVehicle(String regNumber, OnCompleteListener<DocumentSnapshot> listenerObj){
        db.collection(COLLECTION_VEHICLES).document(regNumber)
                .get()
                .addOnCompleteListener( listenerObj);
    }

    public void getVehicles(ArrayList<String> regNumbers, OnCompleteListener<QuerySnapshot> listenerObj){
        db.collection(COLLECTION_VEHICLES)
                .whereIn(
                        "regNumber",
                        regNumbers)
                .get()
                .addOnCompleteListener( listenerObj);
    }
}