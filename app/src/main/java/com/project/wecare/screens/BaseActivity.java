package com.project.wecare.screens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.project.wecare.R;
import com.project.wecare.helpers.LocaleHelper;

import java.util.Locale;


public class BaseActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    protected Resources resources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // this variable set by concrete activity
        getMenuInflater().inflate(R.menu.menu, menu);

        String[] items = new String[]{"සිංහල", "English"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);

        MenuItem item = menu.findItem(R.id.action_change_language);
        Spinner spinner = (Spinner) item.getActionView();
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        String lang = LocaleHelper.retrieve(getApplicationContext());
        int position = lang.equals("en") ? 1 : 0;
        spinner.setSelection(position);

        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Context context;
        switch (position) {
            case 0:
                context = LocaleHelper.setLocale(BaseActivity.this, "si");
                resources = context.getResources();
                onConfigurationChanged(resources.getConfiguration());
                break;
            case 1:
                context = LocaleHelper.setLocale(BaseActivity.this, "en");
                resources = context.getResources();
                onConfigurationChanged(resources.getConfiguration());
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}