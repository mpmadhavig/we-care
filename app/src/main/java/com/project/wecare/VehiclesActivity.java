package com.project.wecare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.project.wecare.helpers.VehicleRecViewAdapter;
import com.project.wecare.models.Vehicle;

import java.util.ArrayList;

public class VehiclesActivity extends AppCompatActivity {

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


            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }


    public void refresh(View view){
        Toast.makeText(this, "refresh", Toast.LENGTH_SHORT).show();
    }

    public void switchLang(View view){
        Toast.makeText(this, "switchLang", Toast.LENGTH_SHORT).show();
    }

    public void logout(View view){
//        Intent intent = new Intent(this, ClaimActivity.class);
//        startActivity(intent);
    }
}
