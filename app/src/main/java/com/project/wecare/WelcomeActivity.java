package com.project.wecare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
    }

    public void openNewClaim(View view) {
        Toast.makeText(this, "open new clain", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, ClaimActivity.class);
        startActivity(intent);
    }

    public void refresh(View view){
        Toast.makeText(this, "refresh", Toast.LENGTH_SHORT).show();
    }

    public void switchLang(View view){
        Toast.makeText(this, "switchLang", Toast.LENGTH_SHORT).show();
    }

    public void logout(View view){
        Toast.makeText(this, "logout", Toast.LENGTH_SHORT).show();
    }
}