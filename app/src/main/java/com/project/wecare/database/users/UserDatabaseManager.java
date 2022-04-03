package com.project.wecare.database.users;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class UserDatabaseManager {

    private final FirebaseFirestore db;

    private String COLLECTION_NIC_EMAIL_MAPPING = "nicEmailMapping";
    private String COLLECTION_USERS = "users";

    // Singleton
    private static UserDatabaseManager instance;

    private UserDatabaseManager() {
        db = FirebaseFirestore.getInstance();
    }

    public static UserDatabaseManager getInstance() {
        if (instance == null) instance = new UserDatabaseManager();
        return instance;
    }

    public void getNicMappingEmail(String nic, OnCompleteListener<QuerySnapshot> listenerObj){
        db.collection(COLLECTION_NIC_EMAIL_MAPPING)
                .get()
                .addOnCompleteListener( listenerObj);
    }

    public void getUser(String nic, OnCompleteListener<QuerySnapshot> listenerObj){
        db.collection(COLLECTION_USERS)
                .get()
                .addOnCompleteListener( listenerObj);
    }


}
