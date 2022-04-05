package com.project.wecare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

public class Claim3Activity extends AppCompatActivity {

    private CheckBox cb_pledgeChecked ;
    private Button btn_submitClaim ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_claim3);

        cb_pledgeChecked = findViewById(R.id.cbCheckPledge);
        btn_submitClaim = findViewById(R.id.btnSubmitClaim);

        btn_submitClaim.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (cb_pledgeChecked.isChecked()){
                    Intent intent = new Intent(Claim3Activity.this , Claim4Activity.class);
                    startActivity(intent);
                    finish();
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