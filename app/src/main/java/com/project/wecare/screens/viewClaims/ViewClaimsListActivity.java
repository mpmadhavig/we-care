package com.project.wecare.screens.viewClaims;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.project.wecare.R;
import com.project.wecare.database.vehicles.VehiclesManager;
import com.project.wecare.helpers.ClaimRecViewAdapter;
import com.project.wecare.models.Claim;
import com.project.wecare.models.Vehicle;
import com.project.wecare.screens.newClaimForm.ClaimActivity;
import com.project.wecare.screens.viewVehicles.VehiclesActivity;

import java.util.ArrayList;
import java.util.Objects;

public class ViewClaimsListActivity extends AppCompatActivity {

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
            case R.id.action_settings:
                Toast.makeText(this, "action_settings", Toast.LENGTH_SHORT).show();
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