package com.project.wecare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this, ClaimActivity.class);
        intent.putExtra("ACCESS_CURRENT_CLAIM", false); // Todo : Later should be removed
        startActivity(intent);
        finish();
    }

}