package com.project.wecare.screens.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.project.wecare.R;
import com.project.wecare.screens.viewVehicles.VehiclesActivity;
import com.project.wecare.database.users.UserDatabaseManager;
import com.project.wecare.database.users.UserManager;
import com.project.wecare.models.User;

import java.util.ArrayList;


public class LoginActivity extends AppCompatActivity {
    
    private EditText et_nic, et_password;
    private String nic, password, email;
    private Button btn_login;

    private String TAG = "WeCare";
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);

        et_nic = (EditText) findViewById(R.id.txtLoginNIC);
        et_password = (EditText) findViewById(R.id.txtLoginPassword);
        btn_login = (Button) findViewById(R.id.btnLogin);

        btn_login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                login();
            }
        });
    }

    public void login(){
        //Todo : check the validity of the credentials and create a user via usermanger

        nic = et_nic.getText().toString().trim();
        password = et_password.getText().toString().trim();

        if (validate()){

            UserDatabaseManager.getInstance().getNicMappingEmail(nic,
                    new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            Boolean isNicValid = false;
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    if (nic.equals(document.getId().toString().trim())) {
                                        Log.d(TAG, "NicMappingEmailRetrieve : success");
                                        email = document.getData().get("email").toString();
                                        isNicValid = true;
                                        authenticate(email, password, nic);
                                        break;
                                    }
                                }
                                if(!isNicValid){
                                    et_nic.setError("Please enter a valid NIC Number");
                                }
                            } else {
                                Log.w(TAG, "DocumentsRetrieve : error", task.getException());
                            }
                        }
            });


        }

    }

    public void authenticate(String email, String password, String nic){

        mAuth = FirebaseAuth.getInstance();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "signInWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser(); // info locally cached
//
                                    //Create new user with the personal information
                                    setNewUser(nic);

                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                                    Toast.makeText(LoginActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    et_password.setError("Incorrect password");
//                            updateUI(null);
                                }
                            }
                        });
    }

    public void setNewUser(String nic){
        //Get authenticated user informations
        UserDatabaseManager.getInstance().getUser(nic,
                new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (nic.equals(document.getId().toString().trim())) {
                                    User user = new User(
                                            document.getData().get("name").toString(),
                                            document.getData().get("nic").toString(),
                                            document.getData().get("licenseNo").toString(),
                                            document.getData().get("contactNo").toString(),
                                            document.getData().get("address").toString(),
                                            document.getData().get("occupation").toString(),
                                            "EN"
                                    );
                                    //Todo : Add licenseExp info
//                                    Timestamp licenseExpTimestamp = (Timestamp)document.getData().get("licenseExp");
//                                    user.setLicenseExp(new Date(licenseExpTimestamp.getTime()));
                                    
                                    user.setAuthenticated(true);
                                    user.setVehiclesRegNumber( (ArrayList<String>) document.get("vehicles"));
                                    UserManager.getInstance().setCurrentUser(LoginActivity.this, user);

                                    // Move to the screen that displays the vehicles own by the user
                                    Intent intent = new Intent(LoginActivity.this, VehiclesActivity.class);
                                    startActivity(intent);
                                    finish();

                                    break;
                                }
                            }

                        } else {
                            Log.w(TAG, "DocumentsRetrieve : error", task.getException());
                        }
                    }
                });

    }
    public Boolean validate(){
        Boolean valid = true;

        if (nic.isEmpty()){
            et_nic.setError("Please enter the NIC number");
            valid = false;
        }

        if (password.isEmpty()){
            et_password.setError("Please enter the password");
            valid = false;
        }

        return valid;
    }

}