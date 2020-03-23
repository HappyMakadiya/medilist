package com.example.medilist.pharmacist;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.medilist.LoginActivity;
import com.example.medilist.R;
import com.example.medilist.patient.AboutUsPatientActivity;
import com.example.medilist.patient.BasicPatientActivity;
import com.example.medilist.patient.ProfilePatientActivity;
import com.google.firebase.auth.FirebaseAuth;

public class BasicPharmacistActivity extends AppCompatActivity {
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_pharmacist);
        getSupportActionBar().setTitle("Pharmacist Dashboard");
        auth = FirebaseAuth.getInstance();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_list,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.profile){
            startActivity(new Intent(BasicPharmacistActivity.this, ProfilePharmacistActivity.class));
        }else  if(item.getItemId()==R.id.aboutus){
            startActivity(new Intent(BasicPharmacistActivity.this, AboutUsPharmaActivity.class));
        }else  if(item.getItemId()==R.id.signout){
            auth.signOut();
            finish();
            startActivity(new Intent(BasicPharmacistActivity.this, LoginActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent a= new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }
}
