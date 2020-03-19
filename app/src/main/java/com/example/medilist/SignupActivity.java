package com.example.medilist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class SignupActivity extends AppCompatActivity {
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser()!=null){
            finish();
        }
        Button signupDr=findViewById(R.id.btnSignupDr);
        signupDr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this,SignupAsDRActivity.class));
                finish();
            }
        });

        Button signupPat=findViewById(R.id.btnSignupPatient);
        signupPat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this,SignupAsPatientActivity.class));
                finish();
            }
        });

        Button signupPharma=findViewById(R.id.btnSignupPharma);
        signupPharma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this,SignupAsPharmaActivity.class));
                finish();
            }
        });

        TextView login=(TextView) findViewById(R.id.tvLogin);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this,LoginActivity.class));
                finish();
            }
        });
    }
}
