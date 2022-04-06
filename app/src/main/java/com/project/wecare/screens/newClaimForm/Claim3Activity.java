package com.project.wecare.screens.newClaimForm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.project.wecare.R;

public class Claim3Activity extends AppCompatActivity {

    private CheckBox cb_pledgeChecked ;
    private Button btn_submitClaim ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_claim3);
        setTitle("New Claim : Step 5");

        cb_pledgeChecked = findViewById(R.id.cbCheckPledge);
        btn_submitClaim = findViewById(R.id.btnSubmitClaim);

        btn_submitClaim.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (cb_pledgeChecked.isChecked()){
                    Intent intent = new Intent(Claim3Activity.this , Claim4Activity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }else{
                    Toast.makeText(Claim3Activity.this, "Please check the pledge to submit the form.",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

    }
    private void submitClaim(){

    }
}