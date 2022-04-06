package com.project.wecare.screens.newClaimForm;

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
import com.project.wecare.R;
import com.project.wecare.screens.viewClaims.ViewClaimsListActivity;
import com.project.wecare.database.claims.ClaimManager;
import com.project.wecare.models.Claim;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.ArrayList;
import java.util.Objects;

public class ClaimActivity extends AppCompatActivity {

    private Claim currentClaim;
    private ClaimManager claimManager;

    private EditText et_driverName, et_driverNIC, et_driverLicense, et_driverLicenseExp,
            et_driverAddress, et_driverContactNo ;
    private CheckBox cb_damage1, cb_damage2, cb_damage3, cb_damage4, cb_damage5,cb_damage6,
            cb_damage7,cb_damage8;
    private RadioButton rb_roadDry ,rb_roadWet,  rb_roadUphill, rb_roadDownhill, rb_roadFlat,
        rb_roadSmooth, rb_roadRough, rb_visGood, rb_visModerate, rb_visPoor;

    private String name,nic, licencesNo, address, contactNo, roadVisibility;
    private Date licenseExp;
    private ArrayList<String> damagedRegions;
    private ArrayList<String> roadStatus;

    private final Calendar LicenseExpCalendar= Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_claim);
        setTitle("New Claim : Step 1");

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
        FloatingActionButton btn_next = (FloatingActionButton) findViewById(R.id.nextPage_claimForm1);

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

        // Check whether to create a new claim or to access the currently working out claim
        Intent intent = getIntent();
        claimManager = ClaimManager.getInstance();
        boolean accessCurrentClaim = claimManager.isAccidentDetails();
        if (accessCurrentClaim) {
            currentClaim = claimManager.getCurrentClaim();
            adjustForViewingClaim();
        } else {
            String regNumber = intent.getStringExtra("regNumber");
            currentClaim = claimManager.createNewClaim(regNumber);
            adjustForNewClaim();
        }

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
            Toast.makeText(this, "Invalid information provided", Toast.LENGTH_SHORT).show();
        } else{
            Intent intent = new Intent(ClaimActivity.this, RecordActivity.class);
            extractFirstFormData();
            claimManager.setCurrentClaim(currentClaim);
            claimManager.setAccidentDetails(true);
            startActivity(intent);
        }
    }


    public boolean validate(){
        boolean valid = true;

        if (name.isEmpty() | ! name.matches("[a-zA-Z]+(\\s+[a-zA-Z]+)*") | !(name.length()>3)){
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

        if (address.isEmpty()){
            et_driverAddress.setError("Please enter your address");
            valid = false;
        }

        if (contactNo.isEmpty() | !(contactNo.length()==10)){
            et_driverContactNo.setError("Please enter a valid contact Number");
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
        currentClaim.setDriverName(name);
        currentClaim.setDriverNic(nic);
        currentClaim.setDriverLicencesNo(licencesNo);
        currentClaim.setDriverLicenseExp(licenseExp);
        currentClaim.setDriverAddress(address);
        currentClaim.setDriverContactNo(contactNo);

        currentClaim.setOwnVehicleDamagedRegions(damagedRegions);
        currentClaim.setRoadStatus(roadStatus);
        currentClaim.setRoadVisibility(roadVisibility);

    }

    public void adjustForNewClaim(){
        // Todo : auto fill the form data for driver details
    }

    public void adjustForViewingClaim(){
        name = currentClaim.getDriverName();
        nic = currentClaim.getDriverNic();
        licencesNo = currentClaim.getDriverLicencesNo();
        licenseExp = currentClaim.getDriverLicenseExp();
        address = currentClaim.getDriverAddress();
        contactNo = currentClaim.getDriverContactNo();

        damagedRegions = currentClaim.getOwnVehicleDamagedRegions();
        roadStatus = currentClaim.getRoadStatus();
        roadVisibility = currentClaim.getRoadVisibility();

        et_driverName.setText(name);
        et_driverNIC.setText(nic);
        et_driverLicense.setText(licencesNo);
        et_driverLicenseExp.setText(new SimpleDateFormat("MM/dd/yy", Locale.US).format(licenseExp).toString());
        et_driverAddress.setText(address);
        et_driverContactNo.setText(contactNo);

        if(damagedRegions.contains("1")){cb_damage1.setChecked(true);}
        if(damagedRegions.contains("2")){cb_damage2.setChecked(true);}
        if(damagedRegions.contains("3")){cb_damage3.setChecked(true);}
        if(damagedRegions.contains("4")){cb_damage4.setChecked(true);}
        if(damagedRegions.contains("5")){cb_damage5.setChecked(true);}
        if(damagedRegions.contains("6")){cb_damage6.setChecked(true);}
        if(damagedRegions.contains("7")){cb_damage7.setChecked(true);}
        if(damagedRegions.contains("8")){cb_damage8.setChecked(true);}

        if(roadStatus.contains("Dry")){rb_roadDry.setChecked(true);}
        if(roadStatus.contains("Wet")){rb_roadWet.setChecked(true);}
        if(roadStatus.contains("Uphill")){rb_roadUphill.setChecked(true);}
        if(roadStatus.contains("Downhill")){rb_roadDownhill.setChecked(true);}
        if(roadStatus.contains("Flat")){rb_roadFlat.setChecked(true);}
        if(roadStatus.contains("Smooth")){rb_roadSmooth.setChecked(true);}
        if(roadStatus.contains("Rough")){rb_roadRough.setChecked(true);}

        if(roadVisibility == "Good"){rb_visGood.setChecked(true);}
        if(roadVisibility == "Moderate"){rb_visModerate.setChecked(true);}
        if(roadVisibility == "Poor"){rb_visPoor.setChecked(true);}

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        startActivity(new Intent(this, ViewClaimsListActivity.class));
        return super.onOptionsItemSelected(item);
    }

    public void getEvidence(View view){
        Toast.makeText(this, "Next Evidence", Toast.LENGTH_SHORT).show();
    }

    public void accidentDetails(View view){
        // Todo: next activity
        Intent intent = new Intent(this, ViewClaimsListActivity.class);
        startActivity(intent);
    }
}

