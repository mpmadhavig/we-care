package com.project.wecare.screens.viewVehicles;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.project.wecare.R;
import com.project.wecare.screens.login.LoginActivity;
import com.project.wecare.screens.viewClaims.ViewClaimsListActivity;
import com.project.wecare.database.claims.ClaimManager;
import com.project.wecare.database.users.UserManager;
import com.project.wecare.database.vehicles.VehiclesDatabaseManager;
import com.project.wecare.database.vehicles.VehiclesManager;
import com.project.wecare.helpers.VehicleRecViewAdapter;
import com.project.wecare.interfaces.ItemClickListener;
import com.project.wecare.models.Vehicle;
import com.project.wecare.services.GPSTracker;

import java.util.ArrayList;

public class VehiclesActivity extends AppCompatActivity implements ItemClickListener {
    Context mContext;
    GPSTracker gps;

    private RecyclerView vehicleRecView;
    private VehicleRecViewAdapter adapter;

    private final String TAG = "WeCare";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicles);
        setTitle("Welcome to We Care");

        Log.d(TAG, "Vehicles activity arrived: success");
        vehicleRecView = findViewById(R.id.vehicleRecView);

        ArrayList<String> regNumbers = UserManager.getInstance().getCurrentUser().getVehiclesRegNumber();
        Log.d(TAG, "Vehicles reg numbers"+ regNumbers.toString());
        setUserVehicles(regNumbers);
        initiateGPSTracker();

        //todo: Alert dialogue for logout


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
        Intent intent = new Intent(VehiclesActivity.this, ViewClaimsListActivity.class );
        intent.putExtra("regNumber" , vehicle.getRegNumber());
        startActivity(intent);
    }

    public void initiateGPSTracker() {
        mContext = this;

        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(VehiclesActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        } else {
            Log.d(TAG, "initiateGPSTracker: You need have granted permission");
            gps = new GPSTracker(mContext, VehiclesActivity.this);

            // Check if GPS enabled
            if (gps.canGetLocation()) {
                double latitude = gps.getLatitude();
                double longitude = gps.getLongitude();

                ClaimManager.getInstance().setGps(gps);
                Log.d(TAG, "initiateGPSTracker: Your Location is - \nLat: " + latitude + "\nLong: " + longitude);

            } else {
                // Ask user to enable GPS/network in settings.
                gps.showSettingsAlert();
            }
        }
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:

                //Prompt an alert dialogue to user for verification
                new AlertDialog.Builder(this)
                        .setTitle("Confirmation logout")
                        .setMessage("Are you sure you want to logout?")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //Update locally stored user authentication info
                                UserManager.getInstance().logoutUser(VehiclesActivity.this);

                                //Update the firebase user info
                                FirebaseAuth.getInstance().signOut();
                                Intent intent = new Intent(VehiclesActivity.this, LoginActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_lock_power_off)
                        .show();


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
