package com.project.wecare;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.project.wecare.models.Claim;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.ArrayList;
import java.util.Objects;

public class ClaimActivity extends AppCompatActivity {

    private EditText et_driverName, et_driverNIC, et_driverLicense, et_driverLicenseExp,
            et_driverAddress, et_driverContactNo ;
    private CheckBox cb_damage1, cb_damage2, cb_damage3, cb_damage4, cb_damage5,cb_damage6,
            cb_damage7,cb_damage8;
    private RadioButton rb_roadDry ,rb_roadWet,  rb_roadUphill, rb_roadDownhill, rb_roadFlat,
        rb_roadSmooth, rb_roadRough, rb_visGood, rb_visModerate, rb_visPoor;
    private FloatingActionButton btn_next;

    private String name,nic, licencesNo, licenseExp, address, contactNo;
    private ArrayList<String> damagedRegions;
    private ArrayList<String> roadStatus;

    private final Calendar myCalendar= Calendar.getInstance();

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
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                updateLabel();
            }
        };
        et_driverLicenseExp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(ClaimActivity.this,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

    }


    private void updateLabel(){
        String myFormat="MM/dd/yy";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        et_driverLicenseExp.setText(dateFormat.format(myCalendar.getTime()));
        et_driverLicenseExp.setError(null);
    }

    public void next(){
        initialize();
        if (!validate()) {
            Toast.makeText(this, "Invalid information provided", Toast.LENGTH_SHORT).show();
        }else{
            Intent intent = new Intent(ClaimActivity.this, Claim2Activity.class);
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

        if (licenseExp.isEmpty()){
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
        licenseExp = et_driverLicenseExp.getText().toString().trim();
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

    //    @Override
//    public void onSaveInstanceState(Bundle savedInstanceState) {
//        super.onSaveInstanceState(savedInstanceState);
//        // Save UI state changes to the savedInstanceState.
//        // This bundle will be passed to onCreate if the process is
//        // killed and restarted.
//        savedInstanceState.putString("name", name);
//        savedInstanceState.putString("nic", nic);
//        savedInstanceState.putString("licencesNo", licencesNo);
//        savedInstanceState.putString("licenseExp", licenseExp);
//        savedInstanceState.putString("address", address);
//        savedInstanceState.putString("contactNo", contactNo);
//        savedInstanceState.putBoolean("isDamaged1", cb_damage1.isChecked());
//        savedInstanceState.putBoolean("isDamaged2", cb_damage2.isChecked());
//        savedInstanceState.putBoolean("isDamaged3", cb_damage3.isChecked());
//        savedInstanceState.putBoolean("isDamaged4", cb_damage4.isChecked());
//        savedInstanceState.putBoolean("isDamaged5", cb_damage5.isChecked());
//        savedInstanceState.putBoolean("isDamaged6", cb_damage6.isChecked());
//        savedInstanceState.putBoolean("isDamaged7", cb_damage7.isChecked());
//        savedInstanceState.putBoolean("isDamaged8", cb_damage8.isChecked());
//        savedInstanceState.putBoolean("roadDry", rb_roadDry.isChecked());
//        savedInstanceState.putBoolean("roadWet", rb_roadWet.isChecked());
//        savedInstanceState.putBoolean("roadUphill", rb_roadUphill.isChecked());
//        savedInstanceState.putBoolean("roadDownhill", rb_roadDownhill.isChecked());
//        savedInstanceState.putBoolean("roadFlat", rb_roadFlat.isChecked());
//        savedInstanceState.putBoolean("roadSmooth", rb_roadSmooth.isChecked());
//        savedInstanceState.putBoolean("roadRough", rb_roadRough.isChecked());
//        savedInstanceState.putBoolean("visGood", rb_visGood.isChecked());
//        savedInstanceState.putBoolean("visPoor", rb_visPoor.isChecked());
//        savedInstanceState.putBoolean("visModerate", rb_visModerate.isChecked());

//    }

//    @Override
//    public void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//        // Restore UI state from the savedInstanceState.
//        // This bundle has also been passed to onCreate.
//        et_driverName.setText(savedInstanceState.getString("name"));
//        et_driverNIC.setText(savedInstanceState.getString("nic"));
//        et_driverLicense.setText(String.valueOf(savedInstanceState.getString("licencesNo")));
//        et_driverLicenseExp.setText(savedInstanceState.getString("licenseExp"));
//        et_driverAddress.setText(savedInstanceState.getString("address"));
//        et_driverContactNo.setText(String.valueOf(savedInstanceState.getString("contactNo")));
//        cb_damage1.setChecked(savedInstanceState.getBoolean("isDamaged1"));
//        cb_damage2.setChecked(savedInstanceState.getBoolean("isDamaged2"));
//        cb_damage3.setChecked(savedInstanceState.getBoolean("isDamaged3"));
//        cb_damage4.setChecked(savedInstanceState.getBoolean("isDamaged3"));
//        cb_damage5.setChecked(savedInstanceState.getBoolean("isDamaged4"));
//        cb_damage6.setChecked(savedInstanceState.getBoolean("isDamaged5"));
//        cb_damage7.setChecked(savedInstanceState.getBoolean("isDamaged6"));
//        cb_damage8.setChecked(savedInstanceState.getBoolean("isDamaged7"));
//        rb_roadDry.setChecked(savedInstanceState.getBoolean("roadDry"));
//        rb_roadWet.setChecked(savedInstanceState.getBoolean("roadWet"));
//        rb_roadSmooth.setChecked(savedInstanceState.getBoolean("roadSmooth"));
//        rb_roadUphill.setChecked(savedInstanceState.getBoolean("roadUphill"));
//        rb_roadDownhill.setChecked(savedInstanceState.getBoolean("roadDownhill"));
//        rb_roadFlat.setChecked(savedInstanceState.getBoolean("roadFlat"));
//        rb_roadRough.setChecked(savedInstanceState.getBoolean("roadRough"));
//        rb_visGood.setChecked(savedInstanceState.getBoolean("visGood"));
//        rb_visModerate.setChecked(savedInstanceState.getBoolean("visModerate"));
//        rb_visPoor.setChecked(savedInstanceState.getBoolean("visPoor"));
//    }
    
}

