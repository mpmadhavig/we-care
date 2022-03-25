package com.project.wecare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.Objects;

public class ClaimActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_claim);

        ActionBar actionBar = getSupportActionBar();
        Objects.requireNonNull(actionBar).setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        startActivity(new Intent(this,WelcomeActivity.class));
        return super.onOptionsItemSelected(item);
    }

    public void getEvidence(View view){
        Toast.makeText(this, "Next Evidence", Toast.LENGTH_SHORT).show();
    }

    public void accidentDetails(View view){
        // Todo: next activity
        Intent intent = new Intent(this, WelcomeActivity.class);
        startActivity(intent);
    }
}