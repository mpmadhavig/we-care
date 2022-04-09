package com.project.wecare.screens.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.project.wecare.R;
import com.project.wecare.models.SharedPreferenceModel;
import com.project.wecare.screens.viewVehicles.VehiclesActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.project.wecare.database.users.UserManager;
import com.project.wecare.models.User;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            reload();
        }else{
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void reload(){
        User existingUser =  UserManager.getInstance().getSavedUser(MainActivity.this);
        Intent intent = new Intent(this, VehiclesActivity.class);
        startActivity(intent);
        finish();
    }
}