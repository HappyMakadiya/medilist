package com.example.medilist.pharmacist;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.medilist.R;

public class AboutUsPharmaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us_pharma);
        getSupportActionBar().setTitle("About Us");
    }
}
