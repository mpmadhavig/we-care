package com.project.wecare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.project.wecare.database.users.UserDatabaseManager;
import com.project.wecare.database.users.UserManager;


public class LoginActivity extends AppCompatActivity {
    
    private EditText et_nic, et_password;
    private String nic, password, email;
    private Button btn_login;

    private String TAG = "AppLogin";
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                                        authenticate(email, password);
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

    public void authenticate(String email, String password){

        mAuth = FirebaseAuth.getInstance();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "signInWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
//                            updateUI(user);
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