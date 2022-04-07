package com.project.wecare.screens.viewClaims;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RadioButton;

import com.project.wecare.R;
import com.project.wecare.database.claims.ClaimManager;
import com.project.wecare.helpers.ImageViewAdapter;
import com.project.wecare.models.Claim;
import com.project.wecare.models.Evidence;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ViewClaimActivity extends AppCompatActivity {

    // ClaimActivity
    private EditText et_driverName, et_driverNIC, et_driverLicense, et_driverLicenseExp,
            et_driverAddress, et_driverContactNo ;
    private CheckBox cb_damage1, cb_damage2, cb_damage3, cb_damage4, cb_damage5,cb_damage6,
            cb_damage7,cb_damage8;
    private RadioButton rb_roadDry ,rb_roadWet,  rb_roadUphill, rb_roadDownhill, rb_roadFlat,
            rb_roadSmooth, rb_roadRough, rb_visGood, rb_visModerate, rb_visPoor;

    private GridView evidenceGridView;

    // Claim2Activity
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

    private GridView gatherPropertyEvidence;
    private GridView gatherVehicleEvidence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_claim);

        Intent intent = getIntent();
        String claimNumber = intent.getStringExtra("claimNumber");

        initializeViewElements();
        setClaimDetails(claimNumber);
        setEnableOrDisable();
    }

    private void setClaimDetails(String claimNumber){
        Claim currentClaim = ClaimManager.getInstance().getClaimByRegNumber(claimNumber);

        // ClaimActivity
        et_driverName.setText(currentClaim.getDriverName());
        et_driverNIC.setText(currentClaim.getDriverNic());
        et_driverLicense.setText(currentClaim.getDriverLicencesNo());
        et_driverLicenseExp.setText(new SimpleDateFormat("MM/dd/yy", Locale.US).format(currentClaim.getDriverLicenseExp()));
        et_driverAddress.setText(currentClaim.getDriverAddress());
        et_driverContactNo.setText(currentClaim.getDriverContactNo());

        ArrayList<String> damagedRegions = currentClaim.getOwnVehicleDamagedRegions();
        if(damagedRegions.contains("1")){cb_damage1.setChecked(true);}
        if(damagedRegions.contains("2")){cb_damage2.setChecked(true);}
        if(damagedRegions.contains("3")){cb_damage3.setChecked(true);}
        if(damagedRegions.contains("4")){cb_damage4.setChecked(true);}
        if(damagedRegions.contains("5")){cb_damage5.setChecked(true);}
        if(damagedRegions.contains("6")){cb_damage6.setChecked(true);}
        if(damagedRegions.contains("7")){cb_damage7.setChecked(true);}
        if(damagedRegions.contains("8")){cb_damage8.setChecked(true);}

        ArrayList<String> roadStatus = currentClaim.getRoadStatus();
        if(roadStatus.contains("Dry")){rb_roadDry.setChecked(true);}
        if(roadStatus.contains("Wet")){rb_roadWet.setChecked(true);}
        if(roadStatus.contains("Uphill")){rb_roadUphill.setChecked(true);}
        if(roadStatus.contains("Downhill")){rb_roadDownhill.setChecked(true);}
        if(roadStatus.contains("Flat")){rb_roadFlat.setChecked(true);}
        if(roadStatus.contains("Smooth")){rb_roadSmooth.setChecked(true);}
        if(roadStatus.contains("Rough")){rb_roadRough.setChecked(true);}

        if(currentClaim.getRoadVisibility().equals("Good")){rb_visGood.setChecked(true);}
        if(currentClaim.getRoadVisibility().equals("Moderate")){rb_visModerate.setChecked(true);}
        if(currentClaim.getRoadVisibility().equals("Poor")){rb_visPoor.setChecked(true);}

        // record
        ImageViewAdapter adapter = new ImageViewAdapter(this, R.layout.image_grid_item, currentClaim.getOwnVehicleDamageEvidences());
        evidenceGridView.setAdapter(adapter);

        // Claim2Activity
        if (ClaimManager.getInstance().isThirdPartDetails()) {
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

                ImageViewAdapter adapter2 = new ImageViewAdapter(this, R.layout.image_grid_item, currentClaim.getPropertyDamageEvidences());
                gatherPropertyEvidence.setAdapter(adapter2);
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

                ImageViewAdapter adapter3 = new ImageViewAdapter(this, R.layout.image_grid_item, currentClaim.getOtherVehicleDamageEvidences());
                gatherVehicleEvidence.setAdapter(adapter3);
            }
        }
    }

    private void initializeViewElements() {
        // ClaimActivity
        et_driverName = (EditText) findViewById(R.id.txtDriverName);
        et_driverNIC = (EditText) findViewById(R.id.txtDriverNIC);
        et_driverLicense = (EditText) findViewById(R.id.txtDriverLicense);
        et_driverLicenseExp = (EditText) findViewById(R.id.txtDriverLicenseExpiration);
        et_driverAddress = (EditText) findViewById(R.id.txtDriverAddress);
        et_driverContactNo = (EditText) findViewById(R.id.txtDriverContactNo);
        cb_damage1 = (CheckBox) findViewById(R.id.checkBox1);
        cb_damage2 = (CheckBox) findViewById(R.id.checkBox2);
        cb_damage3 = (CheckBox) findViewById(R.id.checkBox3);
        cb_damage4 = (CheckBox) findViewById(R.id.checkBox4);
        cb_damage5 = (CheckBox) findViewById(R.id.checkBox5);
        cb_damage6 = (CheckBox) findViewById(R.id.checkBox6);
        cb_damage7 = (CheckBox) findViewById(R.id.checkBox7);
        cb_damage8 = (CheckBox) findViewById(R.id.checkBox8);
        rb_roadDry = (RadioButton) findViewById(R.id.radioBtnDry);
        rb_roadWet = (RadioButton) findViewById(R.id.radioBtnWet);
        rb_roadSmooth = (RadioButton) findViewById(R.id.radioBtnSmooth);
        rb_roadUphill = (RadioButton) findViewById(R.id.radioBtnUphill);
        rb_roadDownhill = (RadioButton) findViewById(R.id.radioBtnDownhill);
        rb_roadFlat = (RadioButton) findViewById(R.id.radioBtnFlat);
        rb_roadSmooth = (RadioButton) findViewById(R.id.radioBtnSmooth);
        rb_roadRough = (RadioButton) findViewById(R.id.radioBtnRough);
        rb_visGood = (RadioButton) findViewById(R.id.radioBtnGood);
        rb_visModerate = (RadioButton) findViewById(R.id.radioBtnModerate);
        rb_visPoor = (RadioButton) findViewById(R.id.radioBtnPoor);

        evidenceGridView = (GridView) findViewById(R.id.image_capture_grid_view);


        // Claim2Activity
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

        gatherPropertyEvidence = findViewById(R.id.gatherPropertyEvidence);
        gatherVehicleEvidence = findViewById(R.id.gatherVehicleEvidence);

    }

    private void setEnableOrDisable() {
        // ClaimActivity
        et_driverName.setEnabled(false);
        et_driverNIC.setEnabled(false);
        et_driverLicense.setEnabled(false);
        et_driverLicenseExp.setEnabled(false);
        et_driverAddress.setEnabled(false);
        et_driverContactNo.setEnabled(false);
        cb_damage1.setEnabled(false);
        cb_damage2.setEnabled(false);
        cb_damage3.setEnabled(false);
        cb_damage4.setEnabled(false);
        cb_damage5.setEnabled(false);
        cb_damage6.setEnabled(false);
        cb_damage7.setEnabled(false);
        cb_damage8.setEnabled(false);
        rb_roadDry.setEnabled(false);
        rb_roadWet.setEnabled(false);
        rb_roadSmooth.setEnabled(false);
        rb_roadUphill.setEnabled(false);
        rb_roadDownhill.setEnabled(false);
        rb_roadFlat.setEnabled(false);
        rb_roadSmooth.setEnabled(false);
        rb_roadRough.setEnabled(false);
        rb_visGood.setEnabled(false);
        rb_visModerate.setEnabled(false);
        rb_visPoor.setEnabled(false);

        evidenceGridView.setEnabled(false);

        // Claim2Activity
        propertyContactPersonName.setEnabled(false);
        propertyContactPersonAddress.setEnabled(false);
        propertyContactPersonNumber.setEnabled(false);
        propertyDamage.setEnabled(false);
        propertyContactPersonAccNumber.setEnabled(false);
        propertyContactPersonBankName.setEnabled(false);
        propertyContactPersonBankBranch.setEnabled(false);

        otherVehicleRegNumber.setEnabled(false);
        otherPartyDriverName.setEnabled(false);
        otherPartyDriverNumber.setEnabled(false);
        otherPartyAccNumber.setEnabled(false);
        otherPartyBankName.setEnabled(false);
        otherPartyBankBranch.setEnabled(false);
        checkBox_2_1.setEnabled(false);
        checkBox_2_2.setEnabled(false);
        checkBox_2_3.setEnabled(false);
        checkBox_2_4.setEnabled(false);
        checkBox_2_5.setEnabled(false);
        checkBox_2_6.setEnabled(false);
        checkBox_2_7.setEnabled(false);
        checkBox_2_8.setEnabled(false);

        gatherVehicleEvidence.setEnabled(false);
        gatherPropertyEvidence.setEnabled(false);
    }
}