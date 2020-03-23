package com.example.medilist.pharmacist;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.medilist.R;

public class ProfilePharmacistActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_pharmacist);
        getSupportActionBar().setTitle("Profile");
    }
}
