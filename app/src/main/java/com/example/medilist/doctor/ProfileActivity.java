package com.example.medilist.doctor;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.medilist.LoginActivity;
import com.example.medilist.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity { EditText pass,cnfpass;
TextView chngpass;
ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
       getSupportActionBar().setTitle("Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        progressDialog = new ProgressDialog(ProfileActivity.this);
        chngpass=findViewById(R.id.tvchngpass);
        chngpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mb = new AlertDialog.Builder(ProfileActivity.this);
                View v = getLayoutInflater().inflate(R.layout.activity_forget_password,null);

                pass = (EditText) v.findViewById(R.id.etchngpass);
                cnfpass = (EditText) v.findViewById(R.id.etchngcnfpass);

                mb.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        showProgDialog();
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            String passwordS = pass.getText().toString().trim();
                            String conpasswordS = cnfpass.getText().toString();
                        while (true) {
                            if(TextUtils.isEmpty(pass.getText())|| passwordS.length()<6){
                                pass.setError("Password must be >6 letter");
                                pass.requestFocus();
                            }else if(TextUtils.isEmpty(cnfpass.getText()) || passwordS.length()<6){
                                cnfpass.setError("Password must be >6 letter");
                                cnfpass.requestFocus();
                            }else if(!conpasswordS.equals(passwordS)){
                                cnfpass.setError("Password doesn't match");
                                cnfpass.requestFocus();
                            }
                            else
                                break;
                        }
                        user.updatePassword(passwordS)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(ProfileActivity.this, "Password is changed.", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
                                            }
                                            else{
                                                String e = "Failed to create user:" + task.getException().getMessage();
                                                Toast.makeText(ProfileActivity.this, e, Toast.LENGTH_SHORT).show();
                                                progressDialog.dismiss();
                                            }
                                        }
                                    });

                    }
                });
                mb.setView(v);
                AlertDialog dialog = mb.create();
                dialog.show();
            }
        });
    }
    private void showProgDialog() {
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

    }
}
