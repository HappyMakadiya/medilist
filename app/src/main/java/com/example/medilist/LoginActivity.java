package com.example.medilist;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.medilist.doctor.BasicActivity;
import com.example.medilist.patient.BasicPatientActivity;
import com.example.medilist.pharmacist.BasicPharmacistActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    EditText emailEt,passwordEt,conpasswordEt;
    Button login;
    TextView signup;
    FirebaseAuth auth;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser()!=null){
            startActivity(new Intent(LoginActivity.this,BasicActivity.class));
        }

        setContentView(R.layout.activity_login);
        compoAssign();
        clickLogin();
        clickSignup();
    }
    public void compoAssign(){
        emailEt = (EditText)findViewById(R.id.ptEmailId);
        passwordEt = (EditText)findViewById(R.id.ptPassword);
        login = (Button)findViewById(R.id.btnLogin);
        signup = (TextView)findViewById(R.id.tvDntAccSignup);
        progressDialog = new ProgressDialog(LoginActivity.this);

    }
    public void clickSignup(){
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,SignupActivity.class));

            }
        });
    }
    public boolean validate(){
        boolean result = false;
        String emailS = emailEt.getText().toString();
        String passwordS= passwordEt.getText().toString();
        if(emailS.equals("") || !emailS.matches(emailPattern)){
            emailEt.setError("Enter valid email");
            emailEt.requestFocus();
        }else if(passwordS.equals("")|| passwordS.length()<6){
            passwordEt.setError("Password must be >6 letter");
            passwordEt.requestFocus();
        }
        else{
            result = true;
        }
        return result;

    }
    public void clickLogin(){
        auth = FirebaseAuth.getInstance();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    showProgDialog();
                    String email = emailEt.getText().toString();
                    final String password = passwordEt.getText().toString();

                    auth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        startActivity(new Intent(LoginActivity.this,BasicActivity.class));
                                        finish();
                                    } else {
                                        String e ="Failed to login:"+task.getException().getMessage();
                                        Toast.makeText(LoginActivity.this,e, Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                    }

                                    // ...
                                }
                            });
                }

            }
        });
    }

    private void showProgDialog() {
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }
}
