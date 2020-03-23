package com.example.medilist.patient;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.medilist.LoginActivity;
import com.example.medilist.R;
import com.example.medilist.doctor.AboutUsActivity;
import com.example.medilist.doctor.BasicActivity;
import com.example.medilist.doctor.ProfileActivity;
import com.google.firebase.auth.FirebaseAuth;

public class BasicPatientActivity extends AppCompatActivity {
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_patient);
        getSupportActionBar().setTitle("Patient Dashboard");
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
            startActivity(new Intent(BasicPatientActivity.this, ProfilePatientActivity.class));
        }else  if(item.getItemId()==R.id.aboutus){
            startActivity(new Intent(BasicPatientActivity.this, AboutUsPatientActivity.class));
        }else  if(item.getItemId()==R.id.signout){
            auth.signOut();
            finish();
            startActivity(new Intent(BasicPatientActivity.this, LoginActivity.class));
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
