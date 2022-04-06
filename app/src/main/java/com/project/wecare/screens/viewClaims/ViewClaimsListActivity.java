package com.project.wecare.screens.viewClaims;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.project.wecare.R;
import com.project.wecare.database.users.UserManager;
import com.project.wecare.database.vehicles.VehiclesManager;
import com.project.wecare.helpers.ClaimRecViewAdapter;
import com.project.wecare.models.Claim;
import com.project.wecare.models.Vehicle;
import com.project.wecare.screens.login.LoginActivity;
import com.project.wecare.screens.newClaimForm.ClaimActivity;
import com.project.wecare.screens.newClaimForm.RecordActivity;

import java.util.ArrayList;

public class ViewClaimsListActivity extends AppCompatActivity {

    private RecyclerView claimRecView;
    private TextView tv_vehicleTitle, tv_model, tv_year, tv_insuranceType, tv_insuredDate;
    private String regNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_claims_list);
        Intent intent = getIntent();
        regNumber = intent.getStringExtra("regNumber");

        claimRecView = findViewById(R.id.claimRecView);
        tv_vehicleTitle = findViewById(R.id.titleVehicle);
        tv_model = findViewById(R.id.txt_vehicleModel);
        tv_year = findViewById(R.id.txt_vehicleYear);
        tv_insuranceType = findViewById(R.id.txt_insuranceType);
        tv_insuredDate = findViewById(R.id.txt_insuranceDate);

        tv_vehicleTitle.setText("Vehicle : "+ regNumber);

        setvehicleDetails(regNumber);

        ArrayList<Claim> claims = new ArrayList<>();
        claims.add(new Claim("2021/2/9 Claim1"));
        claims.add(new Claim("2021/2/9 Claim2"));
        claims.add(new Claim("2021/2/9 Claim3"));
        claims.add(new Claim("2021/2/9 Claim4"));
        claims.add(new Claim("2021/2/9 Claim5"));
        claims.add(new Claim("2021/2/9 Claim6"));
        claims.add(new Claim("2021/2/9 Claim7"));

        ClaimRecViewAdapter adapter = new ClaimRecViewAdapter();
        adapter.setClaims(claims);

        claimRecView.setAdapter(adapter);
        claimRecView.setLayoutManager(new GridLayoutManager(this, 1));
    }

    private void setvehicleDetails(String regNumber){
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
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    public void openNewClaim(View view) {
        Intent intent = new Intent(this, ClaimActivity.class);
        startActivity(intent);
    }

    public void getEvidence(View view){
        Intent intent = new Intent(this, RecordActivity.class);
        startActivity(intent);
    }

    public void refresh(View view){
        Toast.makeText(this, "refresh", Toast.LENGTH_SHORT).show();
    }

    public void switchLang(View view){
        Toast.makeText(this, "switchLang", Toast.LENGTH_SHORT).show();
    }


    public void logout(View view){
        Intent intent = new Intent(this, ClaimActivity.class);
        startActivity(intent);
    }
}