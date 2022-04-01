package com.project.wecare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.project.wecare.helpers.ClaimRecViewAdapter;
import com.project.wecare.models.Claim;

import java.util.ArrayList;

public class WelcomeActivity extends AppCompatActivity {

    private RecyclerView claimRecView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        claimRecView = findViewById(R.id.claimRecView);

        ArrayList<Claim> claims = new ArrayList<>();
        claims.add(new Claim("2021/2/9 Susuki Maruti", R.drawable.car_1));
        claims.add(new Claim("2021/2/9 Susuki Maruti", R.drawable.car_2));
        claims.add(new Claim("2021/2/9 Susuki Maruti", R.drawable.car_3));
        claims.add(new Claim("2021/2/9 Susuki Maruti", R.drawable.car_4));
        claims.add(new Claim("2021/2/9 Susuki Maruti", R.drawable.car_5));
        claims.add(new Claim("2021/2/9 Susuki Maruti", R.drawable.car_1));
        claims.add(new Claim("2021/2/9 Susuki Maruti", R.drawable.car_2));
        claims.add(new Claim("2021/2/9 Susuki Maruti", R.drawable.car_3));
        claims.add(new Claim("2021/2/9 Susuki Maruti", R.drawable.car_4));
        claims.add(new Claim("2021/2/9 Susuki Maruti", R.drawable.car_5));
        claims.add(new Claim("2021/2/9 Susuki Maruti", R.drawable.car_1));
        claims.add(new Claim("2021/2/9 Susuki Maruti", R.drawable.car_2));
        claims.add(new Claim("2021/2/9 Susuki Maruti", R.drawable.car_3));
        claims.add(new Claim("2021/2/9 Susuki Maruti", R.drawable.car_4));
        claims.add(new Claim("2021/2/9 Susuki Maruti", R.drawable.car_5));

        ClaimRecViewAdapter adapter = new ClaimRecViewAdapter();
        adapter.setClaims(claims);

        claimRecView.setAdapter(adapter);
        claimRecView.setLayoutManager(new GridLayoutManager(this, 1));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Toast.makeText(this, "action_settings", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.action_new_claim:
                Intent intent = new Intent(this, ClaimActivity.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    public void openNewClaim(View view) {
        Intent intent = new Intent(this, ClaimActivity.class);
        startActivity(intent);
    }

    public void getEvidence(View view){
        Intent intent = new Intent(this, RecordActivity.class);
        startActivity(intent);
    }

    public void refresh(View view){
        Toast.makeText(this, "refresh", Toast.LENGTH_SHORT).show();
    }

    public void switchLang(View view){
        Toast.makeText(this, "switchLang", Toast.LENGTH_SHORT).show();
    }

    public void logout(View view){
        Intent intent = new Intent(this, ClaimActivity.class);
        startActivity(intent);
    }
}