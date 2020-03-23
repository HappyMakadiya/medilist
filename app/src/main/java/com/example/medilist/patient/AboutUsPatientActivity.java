package com.example.medilist.patient;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.medilist.R;

public class AboutUsPatientActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us_patient);
        getSupportActionBar().setTitle("About Us");
    }
}
