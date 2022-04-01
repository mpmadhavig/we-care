package com.project.wecare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.Toast;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.project.wecare.models.Claim;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.ArrayList;
import java.util.Objects;

public class ClaimActivity extends AppCompatActivity {

    private Claim claim;

    private EditText et_driverName, et_driverNIC, et_driverLicense, et_driverLicenseExp,
            et_driverAddress, et_driverContactNo ;
    private CheckBox cb_damage1, cb_damage2, cb_damage3, cb_damage4, cb_damage5,cb_damage6,
            cb_damage7,cb_damage8;
    private RadioButton rb_roadDry ,rb_roadWet,  rb_roadUphill, rb_roadDownhill, rb_roadFlat,
        rb_roadSmooth, rb_roadRough, rb_visGood, rb_visModerate, rb_visPoor;
    private FloatingActionButton btn_next;

    private String name,nic, licencesNo, address, contactNo, roadVisibility;
    private Date licenseExp;
    private ArrayList<String> damagedRegions;
    private ArrayList<String> roadStatus;

    private final Calendar LicenseExpCalendar= Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_claim);

        ActionBar actionBar = getSupportActionBar();
        Objects.requireNonNull(actionBar).setDisplayHomeAsUpEnabled(true);

        // Initialize buttons and edit texts for form
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
        btn_next = (FloatingActionButton) findViewById(R.id.nextPage_claimForm1);

        btn_next.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                next();
            }
        });

        DatePickerDialog.OnDateSetListener date =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                LicenseExpCalendar.set(Calendar.YEAR, year);
                LicenseExpCalendar.set(Calendar.MONTH,month);
                LicenseExpCalendar.set(Calendar.DAY_OF_MONTH,day);
                updateLabel();
            }
        };
        et_driverLicenseExp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(ClaimActivity.this,date,LicenseExpCalendar.get(Calendar.YEAR),LicenseExpCalendar.get(Calendar.MONTH),LicenseExpCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        // Check whether to create a new claim or to view the previous
//        Intent intent = getIntent();
//        String possibleID = intent.getStringExtra(VIEW_CLAIM_ID);
//        if (possibleID.length() > 0) {
//            claim = AuthController.getInstance().getCurrentUser().getClaim(possibleID);
//            adjustForViewingClaim();
//        } else {
            claim = ClaimManager.getInstance().createNewClaim();
            adjustForNewClaim();
//        }

    }


    private void updateLabel(){
        String myFormat="MM/dd/yy";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        et_driverLicenseExp.setText(dateFormat.format(LicenseExpCalendar.getTime()));
        et_driverLicenseExp.setError(null);
    }

    public void next(){

        initialize();

        if (!validate()) {
//            Toast.makeText(this, "Invalid information provided", Toast.LENGTH_SHORT).show();
//        } else{
            Intent intent = new Intent(ClaimActivity.this, RecordActivity.class);
            extractFirstFormData();
            ClaimManager.getInstance().setCurrentClaim(claim);
            startActivity(intent);
        }
    }


    public boolean validate(){
        boolean valid = true;

        if (name.isEmpty() | ! name.matches("[a-zA-Z]+") | !(name.length()>3)){
            et_driverName.setError("Please enter a valid name");
            valid = false;
        }

        if (nic.isEmpty()){
            et_driverNIC.setError("Please enter nic");
            valid = false;
        }

        if (licencesNo.isEmpty()){
            et_driverLicense.setError("Please enter license Number");
            valid = false;
        }

        if (et_driverLicenseExp.getText().toString().isEmpty()){
            et_driverLicenseExp.setError("Please enter license expiry date");
            valid = false;
        }

        if (contactNo.isEmpty()){
            et_driverContactNo.setError("Please enter contact Number");
            valid = false;
        }

        return valid;
    }

    public void initialize(){
        name = et_driverName.getText().toString().trim();
        nic = et_driverNIC.getText().toString().trim();
        licencesNo = et_driverLicense.getText().toString().trim();
        licenseExp = LicenseExpCalendar.getTime();
        address = et_driverAddress.getText().toString().trim();
        contactNo = et_driverContactNo.getText().toString().trim();

        damagedRegions = new ArrayList<String>();
        if(cb_damage1.isChecked()){ damagedRegions.add("1"); }
        if(cb_damage2.isChecked()){ damagedRegions.add("2"); }
        if(cb_damage3.isChecked()){ damagedRegions.add("3"); }
        if(cb_damage4.isChecked()){ damagedRegions.add("4"); }
        if(cb_damage5.isChecked()){ damagedRegions.add("5"); }
        if(cb_damage6.isChecked()){ damagedRegions.add("6"); }
        if(cb_damage7.isChecked()){ damagedRegions.add("7"); }
        if(cb_damage8.isChecked()){ damagedRegions.add("8"); }

        roadStatus = new ArrayList<String>();
        if(rb_roadDry.isChecked()){ roadStatus.add("Dry");}
        if(rb_roadWet.isChecked()){ roadStatus.add("Wet");}
        if(rb_roadUphill.isChecked()){ roadStatus.add("Uphill");}
        if(rb_roadDownhill.isChecked()){ roadStatus.add("Downhill");}
        if(rb_roadFlat.isChecked()){ roadStatus.add("Flat");}
        if(rb_roadSmooth.isChecked()){ roadStatus.add("Smooth");}
        if(rb_roadRough.isChecked()){ roadStatus.add("Rough");}

        if(rb_visGood.isChecked()){ roadVisibility = "Good";}
        if(rb_visModerate.isChecked()){ roadVisibility = "Moderate";}
        if(rb_visPoor.isChecked()){ roadVisibility = "Poor";}

    }

    //set the details of the claim in claim object
    public void extractFirstFormData(){
        claim.setDriverName(name);
        claim.setDriverNic(nic);
        claim.setDriverLicencesNo(licencesNo);
        claim.setDriverLicenseExp(licenseExp); // Todo : Should pass in the date format
        claim.setDriverAddress(address);
        claim.setDriverContactNo(contactNo);

        claim.setOwnVehicleDamagedRegions(damagedRegions);
        claim.setRoadStatus(roadStatus);
        claim.setRoadVisibility(roadVisibility);

    }

    public void adjustForNewClaim(){
        // Todo : auto fill the form data for driver details
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        startActivity(new Intent(this,WelcomeActivity.class));
        return super.onOptionsItemSelected(item);
    }

    public void getEvidence(View view){
        Toast.makeText(this, "Next Evidence", Toast.LENGTH_SHORT).show();
    }

    public void accidentDetails(View view){
        // Todo: next activity
        Intent intent = new Intent(this, WelcomeActivity.class);
        startActivity(intent);
    }
}

