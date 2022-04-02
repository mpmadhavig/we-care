package com.project.wecare;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.project.wecare.models.Claim;
import com.project.wecare.models.Evidence;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

public class Claim2Activity extends AppCompatActivity {

    private Claim claim;
    private ClaimManager claimManager;

    // View elements
    private RadioButton isPropertyDamageYes, isPropertyDamageNo,
            isOtherVehicleDamagedYes, isOtherVehicleDamagedNo;
    private EditText
            propertyContactPersonName,
            propertyContactPersonAddress,
            propertyContactPersonNumber,
            propertyDamage,
            propertyContactPersonAccNumber, propertyContactPersonBankName, propertyContactPersonBankBranch,

            otherVehicleRegNumber,
            otherPartyDriverName,
            otherPartyDriverNumber,
            otherPartyAccNumber, otherPartyBankName, otherPartyBankBranch;
    private CheckBox
            checkBox_2_1, checkBox_2_2, checkBox_2_3, checkBox_2_4,
            checkBox_2_5, checkBox_2_6, checkBox_2_7, checkBox_2_8;
    private FloatingActionButton nextButton;

    // Variable fields
    private boolean isPropertyDamage, isOtherVehicleDamaged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_claim2);

        // action bar on top of the screen
        ActionBar actionBar = getSupportActionBar();
        Objects.requireNonNull(actionBar).setDisplayHomeAsUpEnabled(true);

        isPropertyDamageYes = findViewById(R.id.isPropertyDamageYes);
        isPropertyDamageNo = findViewById(R.id.isPropertyDamageNo);
        isOtherVehicleDamagedYes = findViewById(R.id.isOtherVehicleDamagedYes);
        isOtherVehicleDamagedNo = findViewById(R.id.isOtherVehicleDamagedNo);

        propertyContactPersonName = findViewById(R.id.propertyContactPersonName);
        propertyContactPersonAddress = findViewById(R.id.propertyContactPersonAddress);
        propertyContactPersonNumber = findViewById(R.id.propertyContactPersonNumber);
        propertyDamage = findViewById(R.id.otherDamagedPropertyDescription);

        propertyContactPersonAccNumber = findViewById(R.id.propertyContactPersonAccNumber);
        propertyContactPersonBankName = findViewById(R.id.propertyContactPersonBankName);
        propertyContactPersonBankBranch = findViewById(R.id.propertyContactPersonBankBranch);

        otherVehicleRegNumber = findViewById(R.id.otherVehicleRegNumber);
        otherPartyDriverName = findViewById(R.id.otherPartyDriverName);
        otherPartyDriverNumber = findViewById(R.id.otherPartyDriverNumber);

        otherPartyAccNumber = findViewById(R.id.otherPartyAccNumber);
        otherPartyBankName = findViewById(R.id.otherPartyBankName);
        otherPartyBankBranch = findViewById(R.id.otherPartyBankBranch);

        checkBox_2_1 = findViewById(R.id.checkBox_2_1);
        checkBox_2_2 = findViewById(R.id.checkBox_2_2);
        checkBox_2_3 = findViewById(R.id.checkBox_2_3);
        checkBox_2_4 = findViewById(R.id.checkBox_2_4);
        checkBox_2_5 = findViewById(R.id.checkBox_2_5);
        checkBox_2_6 = findViewById(R.id.checkBox_2_6);
        checkBox_2_7 = findViewById(R.id.checkBox_2_7);
        checkBox_2_8 = findViewById(R.id.checkBox_2_8);

        nextButton = findViewById(R.id.goToRecordActivity2);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isValidate()) {
                    Toast.makeText(Claim2Activity.this, "Invalid information provided", Toast.LENGTH_SHORT).show();

                } else {
                    Intent intent = new Intent(Claim2Activity.this, RecordActivity.class);
                    sendDataToClaimManager();
                    startActivity(intent);
                }
            }
        });

        // Access the currently working out claim
        claimManager = ClaimManager.getInstance();
        claimManager.setCurrentClaim(claimManager.createNewClaim());
        claim = claimManager.getCurrentClaim();

        // Todo: set empty values if no value exists

        // set if default values exists
        // this.getDataFromClaimManager();
    }

    private boolean isValidate(){
        // Todo
        return true;
    }

    private void sendDataToClaimManager() {

        if (isOtherVehicleDamagedYes.isChecked()) {
            claim.setOtherVehicleDamaged(isOtherVehicleDamagedYes.isChecked());

            claim.setOtherVehicleRegNumber(otherVehicleRegNumber.getText().toString());
            claim.setOtherPartyDriverName(otherPartyDriverName.getText().toString());
            claim.setOtherPartyDriverNumber(otherPartyDriverNumber.getText().toString());

            ArrayList<String> damagedRegions = new ArrayList<>();
            if (checkBox_2_1.isChecked()) damagedRegions.add("1");
            if (checkBox_2_2.isChecked()) damagedRegions.add("2");
            if (checkBox_2_3.isChecked()) damagedRegions.add("3");
            if (checkBox_2_4.isChecked()) damagedRegions.add("4");
            if (checkBox_2_5.isChecked()) damagedRegions.add("5");
            if (checkBox_2_6.isChecked()) damagedRegions.add("6");
            if (checkBox_2_7.isChecked()) damagedRegions.add("7");
            if (checkBox_2_8.isChecked()) damagedRegions.add("8");
            claim.setOtherVehicleDamagedRegions(damagedRegions);

            claim.setOtherPartyAccNumber(Integer.parseInt(otherPartyAccNumber.getText().toString()));
            claim.setOtherPartyBankName(otherPartyBankName.getText().toString());
            claim.setOtherPartyBankBranch(otherPartyBankBranch.getText().toString());
        }

        if (isPropertyDamageYes.isChecked()) {
            claim.setIsPropertyDamage(isPropertyDamageYes.isChecked());

            claim.setPropertyContactPersonName(propertyContactPersonName.getText().toString());
            claim.setPropertyContactPersonAddress(propertyContactPersonAddress.getText().toString());
            claim.setPropertyContactPersonNumber(propertyContactPersonNumber.getText().toString());
            claim.setPropertyDamage(propertyDamage.getText().toString());

            claim.setPropertyContactPersonAccNumber(Integer.parseInt(propertyContactPersonAccNumber.getText().toString()));
            claim.setPropertyContactPersonBankName(propertyContactPersonBankName.getText().toString());
            claim.setPropertyContactPersonBankBranch(propertyContactPersonBankBranch.getText().toString());
        }

        ClaimManager.getInstance().setCurrentClaim(claim);

    }

    private void getDataFromClaimManager(){
        isOtherVehicleDamagedYes.setChecked(claim.getOtherVehicleDamaged());
        isOtherVehicleDamagedNo.setChecked(!claim.getOtherVehicleDamaged());

        otherVehicleRegNumber.setText(claim.getOtherVehicleRegNumber());
        otherPartyDriverName.setText(claim.getOtherPartyDriverName());
        otherPartyDriverNumber.setText(claim.getOtherPartyDriverNumber());

        checkBox_2_1.setChecked(claim.getOtherVehicleDamagedRegions().contains("1"));
        checkBox_2_2.setChecked(claim.getOtherVehicleDamagedRegions().contains("2"));
        checkBox_2_3.setChecked(claim.getOtherVehicleDamagedRegions().contains("3"));
        checkBox_2_4.setChecked(claim.getOtherVehicleDamagedRegions().contains("4"));
        checkBox_2_5.setChecked(claim.getOtherVehicleDamagedRegions().contains("5"));
        checkBox_2_6.setChecked(claim.getOtherVehicleDamagedRegions().contains("6"));
        checkBox_2_7.setChecked(claim.getOtherVehicleDamagedRegions().contains("7"));
        checkBox_2_8.setChecked(claim.getOtherVehicleDamagedRegions().contains("8"));

        otherPartyAccNumber.setText(claim.getOtherPartyAccNumber());
        otherPartyBankName.setText(claim.getOtherPartyBankName());
        otherPartyBankBranch.setText(claim.getOtherPartyBankBranch());



        isPropertyDamageYes.setChecked(claim.isPropertyDamage());
        isPropertyDamageNo.setChecked(!claim.isPropertyDamage());

        propertyContactPersonName.setText(claim.getPropertyContactPersonName());
        propertyContactPersonAddress.setText(claim.getPropertyContactPersonAddress());
        propertyContactPersonNumber.setText(claim.getPropertyContactPersonNumber());
        propertyDamage.setText(claim.getPropertyDamage());

        propertyContactPersonAccNumber.setText(claim.getPropertyContactPersonAccNumber());
        propertyContactPersonBankName.setText(claim.getPropertyContactPersonBankName());
        propertyContactPersonBankBranch.setText(claim.getPropertyContactPersonBankBranch());
    }

}