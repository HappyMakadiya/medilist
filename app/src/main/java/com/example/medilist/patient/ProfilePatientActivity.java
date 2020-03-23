package com.example.medilist.patient;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.medilist.R;


public class ProfilePatientActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_patient);
        getSupportActionBar().setTitle("Profile");
    }
}
