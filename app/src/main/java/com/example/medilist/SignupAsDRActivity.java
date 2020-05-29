package com.example.medilist;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.medilist.doctor.BasicActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignupAsDRActivity extends AppCompatActivity {
     TextView login;
     EditText nameEt,degreeEt,phnoEt,hptNameEt,hptAddEt,emailEt,passwordEt,conpasswordEt;
     RadioGroup radioGenGroup;
     RadioButton radioGenButton;
     Button signup;
     CircleImageView ProfileImage;
     String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
     String ID;
     FirebaseAuth auth;
     DatabaseReference dbr;
     StorageReference storageReference;
     ProgressDialog progressDialog;
     Uri resultUri;
     String dremail;
     int testupload=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_as_dr);
        auth = FirebaseAuth.getInstance();
        dbr = FirebaseDatabase.getInstance().getReference("Doctor");
        compoAssign();
        clickLogin();
        clickSignup();
        choosepic();

    }
    public void compoAssign(){
        nameEt = (EditText)findViewById(R.id.ptDrName);
        phnoEt= (EditText)findViewById(R.id.ptDrPhNo);
        hptNameEt= (EditText)findViewById(R.id.ptDrHspName);
        hptAddEt= (EditText)findViewById(R.id.ptDrHspAdd);
        emailEt = (EditText)findViewById(R.id.ptDrEmail);
        passwordEt = (EditText)findViewById(R.id.ptDrPass);
        conpasswordEt = (EditText)findViewById(R.id.ptDrCnfPass);
        signup = (Button)findViewById(R.id.btnDrSignup);
        login = (TextView)findViewById(R.id.tvLogin);
        degreeEt = (EditText)findViewById(R.id.ptDrDegree);
        ProfileImage = (CircleImageView) findViewById(R.id.btnDrProfilePic);
        radioGenGroup = (RadioGroup) findViewById(R.id.rgrpGender);
        progressDialog = new ProgressDialog(SignupAsDRActivity.this);
        resultUri = Uri.parse("android.resource://"+this.getPackageName()+"/drawable/profilephoto");
        Glide.with(getApplicationContext()).load(resultUri).into(ProfileImage);
    }
    public void choosepic(){
        ProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(SignupAsDRActivity.this);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                testupload++;
                resultUri = result.getUri();
                upload(resultUri);

            }else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }
    }

    private boolean validate(){

        boolean result = false;
        String emailS = emailEt.getText().toString();
        String passwordS= passwordEt.getText().toString();
        String conpasswordS = conpasswordEt.getText().toString();
        if(TextUtils.isEmpty(nameEt.getText())){
            nameEt.setError("Enter Your Name");
            nameEt.requestFocus();
        }else if(TextUtils.isEmpty(degreeEt.getText())){
            degreeEt.setError("Enter Your Degree");
            degreeEt.requestFocus();
        }else if(TextUtils.isEmpty(phnoEt.getText())){
            phnoEt.setError("Enter Your Phone No.");
            phnoEt.requestFocus();
        }else if(phnoEt.length()<10 || phnoEt.length()>10){
            phnoEt.setError("Enter Valid Phone No.");
            phnoEt.requestFocus();
        }else if(TextUtils.isEmpty(hptNameEt.getText())){
            hptNameEt.setError("Enter Your Clinic Name");
            hptNameEt.requestFocus();
        }else if(TextUtils.isEmpty(hptAddEt.getText())){
            hptAddEt.setError("Enter Your Clinic Address");
            hptAddEt.requestFocus();
        }else if(TextUtils.isEmpty(emailEt.getText()) || !emailS.matches(emailPattern)){
            emailEt.setError("Enter valid email");
            emailEt.requestFocus();
        }else if(TextUtils.isEmpty(passwordEt.getText())|| passwordS.length()<6){
            passwordEt.setError("Password must be >6 letter");
            passwordEt.requestFocus();
        }else if(TextUtils.isEmpty(conpasswordEt.getText()) || passwordS.length()<6){
            conpasswordEt.setError("Password must be >6 letter");
            conpasswordEt.requestFocus();
        }else if(!conpasswordS.equals(passwordS)){
            conpasswordEt.setError("Password doesn't match");
            conpasswordEt.requestFocus();
        } else if(testupload == 0){
            Toast.makeText(this, "Please Upload Your Profile Photo", Toast.LENGTH_SHORT).show();
            ProfileImage.requestFocus();
        }
        else{
            result = true;
        }
        return result;
    }

    public void clickLogin(){
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupAsDRActivity.this,LoginActivity.class));
            }
        });
    }

    public void clickSignup() {
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validate()) {
                    showProgDialog();
                    final String user_email = emailEt.getText().toString().trim();
                    final String user_password = passwordEt.getText().toString().trim();
                    auth.createUserWithEmailAndPassword(user_email, user_password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NotNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(SignupAsDRActivity.this, "Registration Complete", Toast.LENGTH_SHORT).show();
                                adduser();
                                startActivity(new Intent(SignupAsDRActivity.this, BasicActivity.class));
                                finish();
                            } else {
                                String e = "Failed to create user:" + task.getException().getMessage();
                                Toast.makeText(SignupAsDRActivity.this, e, Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(SignupAsDRActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });

    }

    private void showProgDialog() {
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

    }

    public void adduser() {
        radioGenButton = (RadioButton) findViewById(radioGenGroup.getCheckedRadioButtonId());
        String Name = nameEt.getText().toString();
        String Degree = degreeEt.getText().toString();
        String PhNo = phnoEt.getText().toString();
        String HptName = hptNameEt.getText().toString();
        String HptAdd = hptAddEt.getText().toString();
        String Email = emailEt.getText().toString().trim();
        dremail = Email;
        String Gender = radioGenButton.getText().toString();
        ID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        dbr = dbr.child(ID);
        dbr.child("Name").setValue("Dr ".concat(Name));
        dbr.child("Email").setValue(Email);
        dbr.child("Gender").setValue(Gender);
        dbr.child("HptName").setValue(HptName);
        dbr.child("HptAdd").setValue(HptAdd);
        dbr.child("PhNo").setValue(PhNo);
        dbr.child("Degree").setValue(Degree);
        dbr.child("ID").setValue(ID);

        addProfileToDB();
    }

    private void addProfileToDB() {
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                dbr.child("ProfilePhoto").setValue(String.valueOf(uri));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignupAsDRActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    void upload(Uri resultUri){
        showProgDialog();
        long randomNumber = (long) (Math.random()*Math.pow(10,10));
        String strrno = Long.toString(randomNumber);
        storageReference= FirebaseStorage.getInstance().getReference().child("DoctorImages");
        storageReference = storageReference.child("ProfilePic").child(strrno.concat(resultUri.getLastPathSegment()));
        storageReference.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                progressDialog.dismiss();
                Glide.with(getApplicationContext()).load(resultUri).into(ProfileImage);
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(SignupAsDRActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
