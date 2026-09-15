package com.example.smartpantrymanager;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);

        TextView tvAppName = findViewById(R.id.tvSettingsAppName);
        TextView tvVersion = findViewById(R.id.tvSettingsVersion);
        Button btnBack = findViewById(R.id.btnBackSettings);

        tvAppName.setText("Smart Pantry Manager");
        tvVersion.setText("Version 1.0");

        btnBack.setOnClickListener(v -> finish());
    }
}