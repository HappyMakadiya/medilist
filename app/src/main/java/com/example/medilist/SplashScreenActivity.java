package com.example.medilist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;

public class SplashScreenActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 800;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser()!=null){
            startActivity(new Intent(SplashScreenActivity.this,LoginActivity.class));
        }

        else {
            setContentView(R.layout.activity_splash_screen);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent homeintent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                    startActivity(homeintent);
                    finish();
                }
            }, SPLASH_TIME_OUT);
        }
    }
}
