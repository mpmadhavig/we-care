package com.project.wecare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Claim2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_claim2);
    }

    public void onClickBtn(View v) {
        Intent intent = new Intent(this, ClaimActivity.class);
        intent.putExtra("ACCESS_CURRENT_CLAIM", true);
        startActivity(intent);
    }
}