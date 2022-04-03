package com.project.wecare;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.project.wecare.models.Claim;

import java.util.ArrayList;
import java.util.Objects;

public class Claim2Activity extends AppCompatActivity {

    private Claim claim;

    // View elements
    private TextView isPropertyDamageTxt, isOtherVehicleDamagedTxt;
    private RadioGroup isPropertyDamage, isOtherVehicleDamaged;
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
    private TextView txtDamagedArea;
    private CheckBox
            checkBox_2_1, checkBox_2_2, checkBox_2_3, checkBox_2_4,
            checkBox_2_5, checkBox_2_6, checkBox_2_7, checkBox_2_8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_claim2);

        // action bar on top of the screen
        ActionBar actionBar = getSupportActionBar();
        Objects.requireNonNull(actionBar).setDisplayHomeAsUpEnabled(true);

        initializeViewElements();

        FloatingActionButton nextButton = findViewById(R.id.goToRecordActivity2);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isValidate()) {
                    Toast.makeText(Claim2Activity.this, "Invalid information provided", Toast.LENGTH_SHORT).show();

                } else {
                    Intent intent = new Intent(Claim2Activity.this, Record2Activity.class);
                    sendDataToClaimManager();
                    startActivity(intent);
                }
            }
        });

        isPropertyDamage.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                boolean enable = i==R.id.isPropertyDamageYes;
                clearPropertyErrorMessage();
                changeEnablePropertyForProperty(enable);
            }
        });

        isOtherVehicleDamaged.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                boolean enable = i==R.id.isOtherVehicleDamagedYes;
                clearVehicleErrorMessage();
                changeEnablePropertyForVehicle(enable);
            }
        });

        // Access the currently working out claim
        ClaimManager claimManager = ClaimManager.getInstance();
        claimManager.setCurrentClaim(claimManager.createNewClaim());
        claim = claimManager.getCurrentClaim();

        // Todo: set empty values if no value exists

        // set if default values exists
        //this.getDataFromClaimManager();
    }


    private boolean isValidate(){
        boolean valid = true;

        if (isOtherVehicleDamagedYes.isChecked()) {

            if (  otherVehicleRegNumber.getText().toString().equals("")
                    | otherVehicleRegNumber.getText().toString().matches("^(KA|DL)(10|0[1-9])([A-Z]{1,2})([1-9][0-9]{3})$")
            ) {
                otherVehicleRegNumber.setError("Please enter a valid Registration Number");
                valid = false;
            }

            if (  otherPartyDriverName.getText().toString().equals("")
                    | otherPartyDriverName.getText().toString().matches("[a-zA-Z]+(\\s+[a-zA-Z]+)*")
                    | !(otherPartyDriverName.getText().toString().length()>3)
            ) {
                otherPartyDriverName.setError("Please enter a valid name");
                valid = false;
            }

            if (  otherPartyDriverNumber.getText().toString().equals("")
                    | !(otherPartyDriverNumber.getText().toString().length()==10)
            ) {
                otherPartyDriverNumber.setError("Please enter a valid contact Number");
                valid = false;
            }

            if ( checkBox_2_1.isChecked() |
                checkBox_2_2.isChecked() |
                checkBox_2_3.isChecked() |
                checkBox_2_4.isChecked() |
                checkBox_2_5.isChecked() |
                checkBox_2_6.isChecked() |
                checkBox_2_7.isChecked() |
                checkBox_2_8.isChecked()
            ) {
                txtDamagedArea.setError("Please select one or more area");
            }

            if (  otherPartyAccNumber.getText().toString().equals("") ) {
                otherPartyAccNumber.setError("Please enter a valid account number");
                valid = false;
            }

            if (  otherPartyBankName.getText().toString().equals("")
                    | otherPartyBankName.getText().toString().matches("[a-zA-Z]+(\\s+[a-zA-Z]+)*")
                    | !(otherPartyBankName.getText().toString().length()>3)
            ) {
                otherPartyBankName.setError("Please enter a valid name");
                valid = false;
            }

            if (  otherPartyBankBranch.getText().toString().equals("")
                    | otherPartyBankBranch.getText().toString().matches("[a-zA-Z]+(\\s+[a-zA-Z]+)*")
                    | !(otherPartyBankBranch.getText().toString().length()>3)
            ) {
                otherPartyBankBranch.setError("Please enter a valid name");
                valid = false;
            }
        }

        if (isPropertyDamageYes.isChecked()) {

            if (  propertyContactPersonName.getText().toString().equals("")
                    | propertyContactPersonName.getText().toString().matches("[a-zA-Z]+(\\s+[a-zA-Z]+)*")
                    | !(propertyContactPersonName.getText().toString().length()>3)
            ) {
                propertyContactPersonName.setError("Please enter a valid name");
                valid = false;
            }

            if (  propertyContactPersonAddress.getText().toString().equals("")
            ) {
                propertyContactPersonName.setError("Please enter a valid address");
                valid = false;
            }

            if (  propertyContactPersonNumber.getText().toString().equals("")
                    | !(propertyContactPersonNumber.getText().toString().length()==10)
            ) {
                propertyContactPersonNumber.setError("Please enter a valid contact Number");
                valid = false;
            }

            if (  propertyDamage.getText().toString().equals("")
            ) {
                propertyDamage.setError("Please enter a valid name");
                valid = false;
            }

            if (  propertyContactPersonAccNumber.getText().toString().equals("") ) {
                propertyContactPersonAccNumber.setError("Please enter a valid account number");
                valid = false;
            }

            if (  propertyContactPersonBankName.getText().toString().equals("")
                    | propertyContactPersonBankName.getText().toString().matches("[a-zA-Z]+(\\s+[a-zA-Z]+)*")
                    | !(propertyContactPersonBankName.getText().toString().length()>3)
            ) {
                propertyContactPersonBankName.setError("Please enter a valid name");
                valid = false;
            }

            if (  propertyContactPersonBankBranch.getText().toString().equals("")
                    | propertyContactPersonBankBranch.getText().toString().matches("[a-zA-Z]+(\\s+[a-zA-Z]+)*")
                    | !(propertyContactPersonBankBranch.getText().toString().length()>3)
            ) {
                propertyContactPersonBankBranch.setError("Please enter a valid name");
                valid = false;
            }

        }

        if (!(isPropertyDamageYes.isChecked() | isPropertyDamageNo.isChecked())) {
            isPropertyDamageTxt.setError("Please mark your choice");
            valid = false;
        }

        if (!(isOtherVehicleDamagedYes.isChecked() | isOtherVehicleDamagedNo.isChecked())) {
            isOtherVehicleDamagedTxt.setError("Please mark your choice");
            valid = false;
        }

        return valid;
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



    private void clearPropertyErrorMessage() {
        propertyContactPersonName.setError(null);
        propertyContactPersonAddress.setError(null);
        propertyContactPersonNumber.setError(null);
        propertyDamage.setError(null);
        propertyContactPersonAccNumber.setError(null);
        propertyContactPersonBankName.setError(null);
        propertyContactPersonBankBranch.setError(null);
        isPropertyDamageTxt.setError(null);
    }

    private void clearVehicleErrorMessage() {
        otherVehicleRegNumber.setError(null);
        otherPartyDriverName.setError(null);
        otherPartyDriverNumber.setError(null);
        otherPartyAccNumber.setError(null);
        otherPartyBankName.setError(null);
        otherPartyBankBranch.setError(null);
        checkBox_2_1.setError(null);
        checkBox_2_2.setError(null);
        checkBox_2_3.setError(null);
        checkBox_2_4.setError(null);
        checkBox_2_5.setError(null);
        checkBox_2_6.setError(null);
        checkBox_2_7.setError(null);
        checkBox_2_8.setError(null);
        isOtherVehicleDamagedTxt.setError(null);
    }

    private void changeEnablePropertyForProperty(boolean enable) {
        propertyContactPersonName.setEnabled(enable);
        propertyContactPersonAddress.setEnabled(enable);
        propertyContactPersonNumber.setEnabled(enable);
        propertyDamage.setEnabled(enable);
        propertyContactPersonAccNumber.setEnabled(enable);
        propertyContactPersonBankName.setEnabled(enable);
        propertyContactPersonBankBranch.setEnabled(enable);
    }

    private void changeEnablePropertyForVehicle(boolean enable) {
        otherVehicleRegNumber.setEnabled(enable);
        otherPartyDriverName.setEnabled(enable);
        otherPartyDriverNumber.setEnabled(enable);
        otherPartyAccNumber.setEnabled(enable);
        otherPartyBankName.setEnabled(enable);
        otherPartyBankBranch.setEnabled(enable);
        checkBox_2_1.setEnabled(enable);
        checkBox_2_2.setEnabled(enable);
        checkBox_2_3.setEnabled(enable);
        checkBox_2_4.setEnabled(enable);
        checkBox_2_5.setEnabled(enable);
        checkBox_2_6.setEnabled(enable);
        checkBox_2_7.setEnabled(enable);
        checkBox_2_8.setEnabled(enable);
    }

    public void initializeViewElements() {
        isPropertyDamage = findViewById(R.id.isPropertyDamage);
        isOtherVehicleDamaged = findViewById(R.id.isOtherVehicleDamaged);

        isPropertyDamageTxt = findViewById(R.id.isOtherPropertyDamaged);
        isOtherVehicleDamagedTxt = findViewById(R.id.isOtherVehicleDamagedTitle);

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

        txtDamagedArea = findViewById(R.id.txtDamagedArea);

        checkBox_2_1 = findViewById(R.id.checkBox_2_1);
        checkBox_2_2 = findViewById(R.id.checkBox_2_2);
        checkBox_2_3 = findViewById(R.id.checkBox_2_3);
        checkBox_2_4 = findViewById(R.id.checkBox_2_4);
        checkBox_2_5 = findViewById(R.id.checkBox_2_5);
        checkBox_2_6 = findViewById(R.id.checkBox_2_6);
        checkBox_2_7 = findViewById(R.id.checkBox_2_7);
        checkBox_2_8 = findViewById(R.id.checkBox_2_8);
    }
}