package com.project.wecare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.project.wecare.database.users.UserManager;
import com.project.wecare.database.vehicles.VehiclesDatabaseManager;
import com.project.wecare.database.vehicles.VehiclesManager;
import com.project.wecare.helpers.VehicleRecViewAdapter;
import com.project.wecare.interfaces.ItemClickListener;
import com.project.wecare.models.User;
import com.project.wecare.models.Vehicle;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class VehiclesActivity extends AppCompatActivity implements ItemClickListener {

    private RecyclerView vehicleRecView;
    private VehicleRecViewAdapter adapter;

    private final String TAG = "WeCare";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicles);

        Log.d(TAG, "Vehicles activity arrived: success");
        vehicleRecView = findViewById(R.id.vehicleRecView);

        ArrayList<String> regNumbers = UserManager.getInstance().getCurrentUser().getVehiclesRegNumber();
        Log.d(TAG, "Vehicles reg numbers"+ regNumbers.toString());
        setUserVehicles(regNumbers);
    }


    public void setUserVehicles(ArrayList<String> regNumbers){
        // get vehicles information of the user from firestore database
        VehiclesDatabaseManager.getInstance().getVehicles(regNumbers,
                new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        Log.d(TAG, "Vehicles retrieved  : Success");
                        if (task.isSuccessful()) {
                            ArrayList<Vehicle> vehicles = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, "Vehicle : "+ document.toString());
                                Vehicle v = new Vehicle(
                                        document.getString("regNumber"),
                                        document.getString("model"),
                                        ((Long) document.get("year")).intValue());
                                v.setInsuranceType(document.getString("insuranceType"));
//                                    v.setInsuredDate((Date) document.getData("insuredDate")); //Todo: add this info
                                vehicles.add(v);
                                Log.d(TAG, "Vehicle with "+ v.getRegNumber()+" added : Success");
                            }
                            VehiclesManager.getInstance().setVehicles(vehicles);
                            Log.d(TAG, "Vehicles in the manager : "+ VehiclesManager.getInstance().getVehicles().toArray().length );

                            // UI update
                            adapter = new VehicleRecViewAdapter();
                            adapter.setVehicles(vehicles);
                            Log.d(TAG, "SUCCESS");
                            adapter.setClickListener((ItemClickListener) VehiclesActivity.this);
                            Log.d(TAG, "set listerener : SUCCESS");
                            vehicleRecView.setAdapter(adapter);
                            vehicleRecView.setLayoutManager(new GridLayoutManager(VehiclesActivity.this, 1));

                        } else {
                            Log.w(TAG, "DocumentsRetrieve : error", task.getException());
                        }
                    }
                });
    }

    @Override
    public void onClick(View view, int position) {
        // The onClick implementation of the RecyclerView item click
        Vehicle vehicle = VehiclesManager.getInstance().getVehicles().get(position);
        Intent intent = new Intent(VehiclesActivity.this, WelcomeActivity.class );
        intent.putExtra("regNumber" , vehicle.getRegNumber());
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
