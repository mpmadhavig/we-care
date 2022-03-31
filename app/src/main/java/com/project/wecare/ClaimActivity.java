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
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class ClaimActivity extends AppCompatActivity {

    private TextInputEditText driver_name;
    private TextInputEditText driver_nic;
    private TextInputEditText driver_driving_license_no;
    private TextInputEditText driver_license_expiration_date;
    private TextInputEditText driver_address;
    private TextInputEditText driver_contact_no;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_claim);

        ActionBar actionBar = getSupportActionBar();
        Objects.requireNonNull(actionBar).setDisplayHomeAsUpEnabled(true);

        driver_name = findViewById(R.id.driver_name);
        driver_nic = findViewById(R.id.driver_nic);
        driver_driving_license_no = findViewById(R.id.driver_driving_license_no);
        driver_license_expiration_date = findViewById(R.id.driver_license_expiration_date);
        driver_address = findViewById(R.id.driver_address);
        driver_contact_no = findViewById(R.id.driver_contact_no);

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putCharSequence("driver_name", driver_name.getText());
        outState.putCharSequence("driver_nic", driver_nic.getText());
        outState.putCharSequence("driver_driving_license_no", driver_driving_license_no.getText());
        outState.putCharSequence("driver_license_expiration_date", driver_license_expiration_date.getText());
        outState.putCharSequence("driver_address", driver_address.getText());
        outState.putCharSequence("driver_contact_no", driver_contact_no.getText());

    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        driver_name.setText(savedInstanceState.getCharSequence("driver_name"));
        driver_nic.setText(savedInstanceState.getCharSequence("driver_nic"));
        driver_driving_license_no.setText(savedInstanceState.getCharSequence("driver_driving_license_no"));
        driver_license_expiration_date.setText(savedInstanceState.getCharSequence("driver_license_expiration_date"));
        driver_address.setText(savedInstanceState.getCharSequence("driver_address"));
        driver_contact_no.setText(savedInstanceState.getCharSequence("driver_contact_no"));

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        startActivity(new Intent(this, WelcomeActivity.class));
        return super.onOptionsItemSelected(item);
    }

    public void getEvidence(View view){
        Toast.makeText(this, "Next Evidence", Toast.LENGTH_SHORT).show();
    }

    public void accidentDetails(View view){
        // Todo: next activity
        Intent intent = new Intent(this, RecordActivity.class);
        startActivity(intent);
    }
}