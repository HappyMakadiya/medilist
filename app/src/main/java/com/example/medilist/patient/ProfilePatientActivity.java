package com.example.medilist.patient;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.medilist.LoginActivity;
import com.example.medilist.R;
import com.example.medilist.doctor.ProfileActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.Calendar;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfilePatientActivity extends AppCompatActivity {
    EditText pass,cnfpass;
    TextView chngpass;
    EditText nameEt,phnoEt,emailEt;
    ProgressDialog progressDialog;
    String strname,strphno,gender,struri,stremail,strdob;
    TextView tvdob;
    DatabaseReference dbr;
    RadioGroup radioGenGroup;
    RadioButton radiocheckedButton;
    Calendar c;
    DatePickerDialog dpd;
    Button btnaskupdate,btnupdate,btnDOB;
    CircleImageView ProfileImage;
    Uri resultUri;
    StorageReference storageReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_patient);

        addcompo();
        showProgDialog();
        getSupportActionBar().setTitle("Patient Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dbr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                strname = dataSnapshot.child("Name").getValue().toString();
                stremail = dataSnapshot.child("Email").getValue().toString();
                strphno = dataSnapshot.child("PhNo").getValue().toString();
                gender = dataSnapshot.child("Gender").getValue().toString();
                strdob = dataSnapshot.child("DOB").getValue().toString();
                struri = dataSnapshot.child("ProfilePhoto").getValue().toString();

                nameEt.setText(strname);
                emailEt.setText(stremail);
                phnoEt.setText(strphno);
                tvdob.setText(strdob);
                if(gender.equals("Male")){
                    radioGenGroup.check(R.id.rbtnMale);
                }
                else{
                    radioGenGroup.check(R.id.rbtnFemale);
                }
                Glide.with(getApplicationContext()).load(struri).into(ProfileImage);
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        setValue();
        btnaskupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatedata();
            }
        });

        chngpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changepassfunc();
            }
        });


    }

    private void addcompo() {
        progressDialog = new ProgressDialog(ProfilePatientActivity.this);
        dbr = FirebaseDatabase.getInstance().getReference().child("Patient").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        nameEt = findViewById(R.id.ptPatName);
        emailEt= findViewById(R.id.ptPatEmail);
        phnoEt = findViewById(R.id.ptPatPhNo);
        radioGenGroup = (RadioGroup) findViewById(R.id.rgrpGender);
        chngpass=findViewById(R.id.tvchngpass);
        btnaskupdate=findViewById(R.id.btnaskupdate);
        btnupdate=findViewById(R.id.btnupdate);
        btnDOB=findViewById(R.id.btnPatDob);
        tvdob=findViewById(R.id.tvDOBView);
        ProfileImage = (CircleImageView) findViewById(R.id.btnPatProfilePic);
    }

    private void setValue() {
        setcustomtv(nameEt,false);
        setcustomtv(emailEt,false);
        setcustomtv(phnoEt,false);
        ProfileImage.setClickable(false);
        btnDOB.setClickable(false);
        for(int i=0;i< radioGenGroup.getChildCount();i++){
            radioGenGroup.getChildAt(i).setEnabled(false);
        }
        btnupdate.setVisibility(View.INVISIBLE);
        btnupdate.setClickable(false);

        btnaskupdate.setVisibility(View.VISIBLE);
        btnaskupdate.setClickable(true);

    }

    private void setcustomtv(EditText et,boolean bl) {
        int i;
        if(bl){
            i=1;
        }
        else{
            i=0;
        }
        et.setInputType(i);
    }

    private void updatedata() {
        btnaskupdate.setVisibility(View.INVISIBLE);
        btnaskupdate.setClickable(false);
        btnupdate.setVisibility(View.VISIBLE);
        btnupdate.setClickable(true);

        setcustomtv(nameEt,true);
        setcustomtv(phnoEt,true);
        btnDOB.setClickable(true);

        for(int i=0;i< radioGenGroup.getChildCount();i++){
            radioGenGroup.getChildAt(i).setEnabled(true);
        }

        btnupdate.setClickable(true);

        btnDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatedob();
            }
        });
        ProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(ProfilePatientActivity.this);
            }
        });
        btnupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatedatabase();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                resultUri = result.getUri();
                upload(resultUri);
            }else if(resultCode == RESULT_CANCELED){
                Glide.with(getApplicationContext()).load(struri).into(ProfileImage);
            }else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void updatedob() {
        c=Calendar.getInstance();
        int day=c.get(Calendar.DAY_OF_MONTH);
        final int month=c.get(Calendar.MONTH);
        int year=c.get(Calendar.YEAR);

        dpd= new DatePickerDialog(ProfilePatientActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int fyear, int fmonth, int fdayOfMonth) {
                fmonth=fmonth+1;
                tvdob.setText(fdayOfMonth + "/" + fmonth + "/" + fyear);
            }
        },year,month,day);
        dpd.show();
    }

    private void updatedatabase() {
        if (validate()) {
            adduser();
            Toast.makeText(this, "Data Updated", Toast.LENGTH_SHORT).show();
            setValue();
        }
    }

    private boolean validate(){
        boolean result = false;
        if(TextUtils.isEmpty(nameEt.getText())){
            nameEt.setError("Enter Your Name");
            nameEt.requestFocus();
        }else if(TextUtils.isEmpty(phnoEt.getText())){
            phnoEt.setError("Enter Your Phone No.");
            phnoEt.requestFocus();
        }else if(phnoEt.length()<10 || phnoEt.length()>10){
            phnoEt.setError("Enter Valid Phone No.");
            phnoEt.requestFocus();
        }
        else{
            result = true;
        }
        return result;
    }

    private void adduser() {
        strname = nameEt.getText().toString();
        strphno = phnoEt.getText().toString();
        strdob = tvdob.getText().toString();

        radiocheckedButton = (RadioButton) findViewById(radioGenGroup.getCheckedRadioButtonId());
        gender = radiocheckedButton.getText().toString();
        dbr.child("Name").setValue(strname);
        dbr.child("Gender").setValue(gender);
        dbr.child("PhNo").setValue(strphno);
        dbr.child("DOB").setValue(strdob);
        addProfileToDB();
    }

    private void addProfileToDB() {
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                dbr.child("ProfilePhoto").setValue(String.valueOf(uri));
            }
        });
    }

    void upload(Uri resultUri){
        showProgDialog();
        Toast.makeText(this, "Into Upload", Toast.LENGTH_SHORT).show();
        long randomNumber = (long) (Math.random()*Math.pow(10,10));
        String strrno = Long.toString(randomNumber);
        storageReference= FirebaseStorage.getInstance().getReference().child("PatientImages");
        storageReference = storageReference.child("ProfilePic").child(strrno.concat(resultUri.getLastPathSegment()));
        storageReference.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                progressDialog.dismiss();
                Glide.with(getApplicationContext()).load(resultUri).into(ProfileImage);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(ProfilePatientActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void changepassfunc(){
        AlertDialog.Builder mb = new AlertDialog.Builder(ProfilePatientActivity.this);
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
                                    Toast.makeText(ProfilePatientActivity.this, "Password is changed.", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(ProfilePatientActivity.this, LoginActivity.class));
                                }
                                else{
                                    String e = "Failed to create user:" + task.getException().getMessage();
                                    Toast.makeText(ProfilePatientActivity.this, e, Toast.LENGTH_SHORT).show();
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

    private void showProgDialog() {
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

    }
}
