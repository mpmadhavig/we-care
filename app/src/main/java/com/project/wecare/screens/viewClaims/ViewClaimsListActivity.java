package com.project.wecare.screens.viewClaims;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.project.wecare.R;
import com.project.wecare.database.claims.ClaimManager;
import com.project.wecare.database.users.UserManager;
import com.project.wecare.database.vehicles.VehiclesManager;
import com.project.wecare.helpers.ClaimRecViewAdapter;
import com.project.wecare.interfaces.ItemClickListener;
import com.project.wecare.models.Claim;
import com.project.wecare.models.Vehicle;
import com.project.wecare.screens.login.LoginActivity;
import com.project.wecare.screens.newClaimForm.ClaimActivity;
import com.project.wecare.screens.viewVehicles.VehiclesActivity;

import java.util.ArrayList;
import java.util.Objects;

public class ViewClaimsListActivity extends AppCompatActivity implements ItemClickListener {

    private ClaimManager claimManager;
    private ArrayList<Claim> claims;

    private TextView tv_model;
    private TextView tv_year;
    private TextView tv_insuranceType;
    private TextView tv_insuredDate;
    private String regNumber;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_claims_list);

        // action bar initialize
        ActionBar actionBar = getSupportActionBar();
        Objects.requireNonNull(actionBar).setDisplayHomeAsUpEnabled(true);

        claimManager = ClaimManager.getInstance();
        claimManager.setSharedPref(ViewClaimsListActivity.this);

        Intent intent = getIntent();
        regNumber = intent.getStringExtra("regNumber");

        RecyclerView claimRecView = findViewById(R.id.claimRecView);
        TextView tv_vehicleTitle = findViewById(R.id.titleVehicle);
        tv_model = findViewById(R.id.txt_vehicleModel);
        tv_year = findViewById(R.id.txt_vehicleYear);
        tv_insuranceType = findViewById(R.id.txt_insuranceType);
        tv_insuredDate = findViewById(R.id.txt_insuranceDate);

        tv_vehicleTitle.setText("Vehicle : "+ regNumber);

        setVehicleDetails(regNumber);

        claims = claimManager.initializeQueue(this);

        ArrayList<String> regNumbers = UserManager.getInstance().getCurrentUser().getVehiclesRegNumber();
        Log.d("Claim", "claim id numbers"+ regNumbers.toString());

        ClaimRecViewAdapter adapter = new ClaimRecViewAdapter();
        adapter.setClaims(claims);
        adapter.setClickListener((ItemClickListener) ViewClaimsListActivity.this);

        claimRecView.setAdapter(adapter);
        claimRecView.setLayoutManager(new GridLayoutManager(this, 1));
    }

    @Override
    public void onClick(View view, int position) {
        // The onClick implementation of the RecyclerView item click
        Claim claim = ClaimManager.getInstance().getQueue().get(position);

        Intent intent = new Intent(ViewClaimsListActivity.this, ViewClaimActivity.class );
        intent.putExtra("claimNumber" , claim.getClaimId());
        intent.putExtra("regNumber" , regNumber);
        startActivity(intent);
    }

    @Override
    public void onButtonClick(View view, int position) {
        Toast.makeText(this, "Resubmit", Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("SetTextI18n")
    private void setVehicleDetails(String regNumber){
        Vehicle v = VehiclesManager.getInstance().getVehicleByRegNumber(regNumber);
        tv_model.setText("Model : " + v.getModel().toString());
        tv_year.setText("Year : " + v.getYear().toString());
        tv_insuranceType.setText("Insurance type : " + v.getInsuranceType().toString());
        tv_insuredDate.setText("Insured date : "+"05/10/2020"); // Todo: Add the real date
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
                                UserManager.getInstance().logoutUser(ViewClaimsListActivity.this);

                                //Update the firebase user info
                                FirebaseAuth.getInstance().signOut();
                                Intent intent = new Intent(ViewClaimsListActivity.this, LoginActivity.class);
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

            case R.id.action_new_claim2:
                Intent intent = new Intent(ViewClaimsListActivity.this, ClaimActivity.class);
                intent.putExtra("regNumber",regNumber);
                startActivity(intent);
                return true;

            default:
                startActivity(new Intent(ViewClaimsListActivity.this, VehiclesActivity.class));
                return super.onOptionsItemSelected(item);

        }
    }
}