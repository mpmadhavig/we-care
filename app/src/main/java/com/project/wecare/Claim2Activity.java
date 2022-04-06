package com.project.wecare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
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

    private Claim currentClaim;
    private ClaimManager claimManager;

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
        setTitle("New Claim : Step 3");

        // action bar on top of the screen
        ActionBar actionBar = getSupportActionBar();
        Objects.requireNonNull(actionBar).setDisplayHomeAsUpEnabled(true);

        // Access the currently working out claim
        claimManager = ClaimManager.getInstance();
        currentClaim = claimManager.getCurrentClaim();

        this.initializeViewElements();
        this.getDataFromClaimManager();

        FloatingActionButton nextButton = findViewById(R.id.goToRecordActivity2);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isValidate()) {
                    Toast.makeText(Claim2Activity.this, "Invalid information provided", Toast.LENGTH_SHORT).show();

                } else {
                    Intent intent = new Intent(Claim2Activity.this, Record2Activity.class);
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
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (isValidate()) {
            startActivity(new Intent(Claim2Activity.this, RecordActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean isValidate(){
        boolean valid = true;

        if (isOtherVehicleDamagedYes.isChecked()) {

            if (  otherVehicleRegNumber.getText().toString().equals("")
//                    | !otherVehicleRegNumber.getText().toString().matches("^(KA|DL)(10|0[1-9])([A-Z]{1,2})([1-9][0-9]{3})$")
            ) {
                otherVehicleRegNumber.setError("Please enter a valid Registration Number");
                valid = false;
            }

            if (  otherPartyDriverName.getText().toString().equals("")
            ) {
                otherPartyDriverName.setError("Please enter a valid name");
                valid = false;
            }

            if (  otherPartyDriverNumber.getText().toString().equals("")
                    | (otherPartyDriverNumber.getText().toString().length()>10)
            ) {
                otherPartyDriverNumber.setError("Please enter a valid contact Number");
                valid = false;
            }

            if ( !(checkBox_2_1.isChecked() |
                checkBox_2_2.isChecked() |
                checkBox_2_3.isChecked() |
                checkBox_2_4.isChecked() |
                checkBox_2_5.isChecked() |
                checkBox_2_6.isChecked() |
                checkBox_2_7.isChecked() |
                checkBox_2_8.isChecked())
            ) {
                txtDamagedArea.setError("Please select one or more area");
                valid = false;
            }

            if (  otherPartyAccNumber.getText().toString().equals("") ) {
                otherPartyAccNumber.setError("Please enter a valid account number");
                valid = false;
            }

            if (  otherPartyBankName.getText().toString().equals("")
            ) {
                otherPartyBankName.setError("Please enter a valid name");
                valid = false;
            }

            if (  otherPartyBankBranch.getText().toString().equals("")
            ) {
                otherPartyBankBranch.setError("Please enter a valid name");
                valid = false;
            }
        }

        if (isPropertyDamageYes.isChecked()) {

            if (  propertyContactPersonName.getText().toString().equals("")
                    | !propertyContactPersonName.getText().toString().matches("[a-zA-Z]+(\\s+[a-zA-Z]+)*")
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
                    | (propertyContactPersonNumber.getText().toString().length()>10)
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
            ) {
                propertyContactPersonBankName.setError("Please enter a valid name");
                valid = false;
            }

            if (  propertyContactPersonBankBranch.getText().toString().equals("")
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

        if (valid) {
            claimManager.setThirdPartDetails(true);
            sendDataToClaimManager();
        }
        return valid;
    }

    private void sendDataToClaimManager() {
        currentClaim.setOtherVehicleDamaged(isOtherVehicleDamagedYes.isChecked());
        currentClaim.setIsPropertyDamage(isPropertyDamageYes.isChecked());

        if (isOtherVehicleDamagedYes.isChecked()) {
            currentClaim.setOtherVehicleRegNumber(otherVehicleRegNumber.getText().toString());
            currentClaim.setOtherPartyDriverName(otherPartyDriverName.getText().toString());
            currentClaim.setOtherPartyDriverNumber(otherPartyDriverNumber.getText().toString());

            ArrayList<String> damagedRegions = new ArrayList<>();
            if (checkBox_2_1.isChecked()) damagedRegions.add("1");
            if (checkBox_2_2.isChecked()) damagedRegions.add("2");
            if (checkBox_2_3.isChecked()) damagedRegions.add("3");
            if (checkBox_2_4.isChecked()) damagedRegions.add("4");
            if (checkBox_2_5.isChecked()) damagedRegions.add("5");
            if (checkBox_2_6.isChecked()) damagedRegions.add("6");
            if (checkBox_2_7.isChecked()) damagedRegions.add("7");
            if (checkBox_2_8.isChecked()) damagedRegions.add("8");
            currentClaim.setOtherVehicleDamagedRegions(damagedRegions);

            currentClaim.setOtherPartyAccNumber(Integer.parseInt(otherPartyAccNumber.getText().toString()));
            currentClaim.setOtherPartyBankName(otherPartyBankName.getText().toString());
            currentClaim.setOtherPartyBankBranch(otherPartyBankBranch.getText().toString());
        }

        if (isPropertyDamageYes.isChecked()) {
            currentClaim.setPropertyContactPersonName(propertyContactPersonName.getText().toString());
            currentClaim.setPropertyContactPersonAddress(propertyContactPersonAddress.getText().toString());
            currentClaim.setPropertyContactPersonNumber(propertyContactPersonNumber.getText().toString());
            currentClaim.setPropertyDamage(propertyDamage.getText().toString());

            currentClaim.setPropertyContactPersonAccNumber(Integer.parseInt(propertyContactPersonAccNumber.getText().toString()));
            currentClaim.setPropertyContactPersonBankName(propertyContactPersonBankName.getText().toString());
            currentClaim.setPropertyContactPersonBankBranch(propertyContactPersonBankBranch.getText().toString());
        }

        ClaimManager.getInstance().setCurrentClaim(currentClaim);

    }

    private void getDataFromClaimManager() {

        if (claimManager.isThirdPartDetails()) {
            if (!currentClaim.isPropertyDamage()) {
                isPropertyDamageYes.setChecked(currentClaim.isPropertyDamage());
                isPropertyDamageNo.setChecked(!currentClaim.isPropertyDamage());

            } else {
                isPropertyDamageYes.setChecked(currentClaim.isPropertyDamage());
                isPropertyDamageNo.setChecked(!currentClaim.isPropertyDamage());

                propertyContactPersonName.setText(currentClaim.getPropertyContactPersonName());
                propertyContactPersonAddress.setText(currentClaim.getPropertyContactPersonAddress());
                propertyContactPersonNumber.setText((currentClaim.getPropertyContactPersonNumber()));
                propertyDamage.setText(currentClaim.getPropertyDamage());

                propertyContactPersonAccNumber.setText(String.valueOf(currentClaim.getPropertyContactPersonAccNumber()));
                propertyContactPersonBankName.setText(currentClaim.getPropertyContactPersonBankName());
                propertyContactPersonBankBranch.setText(currentClaim.getPropertyContactPersonBankBranch());
            }

            if (!currentClaim.getOtherVehicleDamaged()) {
                isOtherVehicleDamagedYes.setChecked(currentClaim.getOtherVehicleDamaged());
                isOtherVehicleDamagedNo.setChecked(!currentClaim.getOtherVehicleDamaged());

            } else {
                isOtherVehicleDamagedYes.setChecked(currentClaim.getOtherVehicleDamaged());
                isOtherVehicleDamagedNo.setChecked(!currentClaim.getOtherVehicleDamaged());

                otherVehicleRegNumber.setText(currentClaim.getOtherVehicleRegNumber());
                otherPartyDriverName.setText(currentClaim.getOtherPartyDriverName());
                otherPartyDriverNumber.setText(currentClaim.getOtherPartyDriverNumber());

                checkBox_2_1.setChecked(currentClaim.getOtherVehicleDamagedRegions().contains("1"));
                checkBox_2_2.setChecked(currentClaim.getOtherVehicleDamagedRegions().contains("2"));
                checkBox_2_3.setChecked(currentClaim.getOtherVehicleDamagedRegions().contains("3"));
                checkBox_2_4.setChecked(currentClaim.getOtherVehicleDamagedRegions().contains("4"));
                checkBox_2_5.setChecked(currentClaim.getOtherVehicleDamagedRegions().contains("5"));
                checkBox_2_6.setChecked(currentClaim.getOtherVehicleDamagedRegions().contains("6"));
                checkBox_2_7.setChecked(currentClaim.getOtherVehicleDamagedRegions().contains("7"));
                checkBox_2_8.setChecked(currentClaim.getOtherVehicleDamagedRegions().contains("8"));

                otherPartyAccNumber.setText(String.valueOf(currentClaim.getOtherPartyAccNumber()));
                otherPartyBankName.setText(currentClaim.getOtherPartyBankName());
                otherPartyBankBranch.setText(currentClaim.getOtherPartyBankBranch());
            }
        }
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
        txtDamagedArea.setError(null);
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