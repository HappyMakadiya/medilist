package com.example.medilist.doctor;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.medilist.LoginActivity;
import com.example.medilist.R;
import com.google.firebase.auth.FirebaseAuth;

public class BasicActivity extends AppCompatActivity {
    FirebaseAuth auth;
    Button btncreate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic);
        getSupportActionBar().setTitle("Doctor Dashboard");

        btncreate = (Button)findViewById(R.id.btnCreate);
        btncreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(BasicActivity.this, NewPxActivity.class));
            }
        });
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
            startActivity(new Intent(BasicActivity.this, ProfileActivity.class));
        }else  if(item.getItemId()==R.id.aboutus){
            startActivity(new Intent(BasicActivity.this, AboutUsActivity.class));
        }else  if(item.getItemId()==R.id.signout){
            auth.signOut();
            finish();
            startActivity(new Intent(BasicActivity.this, LoginActivity.class));
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
