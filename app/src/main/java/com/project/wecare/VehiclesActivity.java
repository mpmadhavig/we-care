package com.project.wecare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.project.wecare.helpers.VehicleRecViewAdapter;
import com.project.wecare.models.Vehicle;
import com.project.wecare.services.GPSTracker;

import java.util.ArrayList;

public class VehiclesActivity extends AppCompatActivity {

    Context mContext;
    GPSTracker gps;

    private RecyclerView vehicleRecView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicles);

        vehicleRecView = findViewById(R.id.vehicleRecView);

        ArrayList<Vehicle> vehicles = new ArrayList<>();
        vehicles.add(new Vehicle("QL-7856", "Mitsubishi Lancer" , 2013, R.drawable.car_1));
        vehicles.add(new Vehicle("AB-9654", "Maruti", 2015, R.drawable.car_2));
        vehicles.add(new Vehicle("AC-9654", "Suzuki", 2016, R.drawable.car_3));
        vehicles.add(new Vehicle("QL-7856", "Mitsubishi Lancer" , 2013, R.drawable.car_4));
        vehicles.add(new Vehicle("AB-9654", "Maruti", 2015, R.drawable.car_5));
        vehicles.add(new Vehicle("AC-9654", "Suzuki", 2016, R.drawable.car_1));
        vehicles.add(new Vehicle("QL-7856", "Mitsubishi Lancer" , 2013, R.drawable.car_2));
        vehicles.add(new Vehicle("AB-9654", "Maruti", 2015, R.drawable.car_3));
        vehicles.add(new Vehicle("AC-9654", "Suzuki", 2016, R.drawable.car_4));

        VehicleRecViewAdapter adapter = new VehicleRecViewAdapter();
        adapter.setVehicles(vehicles);

        vehicleRecView.setAdapter(adapter);
        vehicleRecView.setLayoutManager(new GridLayoutManager(this, 1));

        initiateGPSTracker();
    }

    public void initiateGPSTracker() {
        mContext = this;

        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(VehiclesActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        } else {
            Toast.makeText(mContext,"You need have granted permission",Toast.LENGTH_SHORT).show();
            gps = new GPSTracker(mContext, VehiclesActivity.this);

            // Check if GPS enabled
            if (gps.canGetLocation()) {

                double latitude = gps.getLatitude();
                double longitude = gps.getLongitude();

                ClaimManager.getInstance().setGps(gps);

                Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
            } else {
                // Can't get location.
                // GPS or network is not enabled.
                // Ask user to enable GPS/network in settings.
                gps.showSettingsAlert();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Toast.makeText(this, "action_settings", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.action_new_claim2:
                Intent intent = new Intent(VehiclesActivity.this, ClaimActivity.class);
                intent.putExtra("ACCESS_CURRENT_CLAIM", false); // Todo : Later should be removed
                startActivity(intent);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the

                    // contacts-related task you need to do.

                    gps = new GPSTracker(mContext, VehiclesActivity.this);

                    // Check if GPS enabled
                    if (gps.canGetLocation()) {

                        double latitude = gps.getLatitude();
                        double longitude = gps.getLongitude();

                        // \n is for new line
                        Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                    } else {
                        // Can't get location.
                        // GPS or network is not enabled.
                        // Ask user to enable GPS/network in settings.
                        gps.showSettingsAlert();
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                    Toast.makeText(mContext, "You need to grant permission", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }
}
