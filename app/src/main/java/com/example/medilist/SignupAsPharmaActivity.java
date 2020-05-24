package com.example.medilist;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.medilist.doctor.BasicActivity;
import com.example.medilist.doctor.DocterUser;
import com.example.medilist.pharmacist.BasicPharmacistActivity;
import com.example.medilist.pharmacist.PharmacistUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignupAsPharmaActivity extends AppCompatActivity {
    TextView login;
    EditText nameEt,phnoEt,strNameEt,strAddEt,emailEt,passwordEt,conpasswordEt;
    Button signup;
    CircleImageView cvProfileImage;
    RadioGroup radioGenGroup;
    RadioButton radioGenButton;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    String ID;
    FirebaseAuth auth;
    DatabaseReference dbr;
    ProgressDialog progressDialog;
    Uri resultUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_as_pharma);
        auth = FirebaseAuth.getInstance();
        dbr= FirebaseDatabase.getInstance().getReference("Pharmacist");
        compoAssign();
        clickSignup();
        clickLogin();
        choosepic();
    }

    public void compoAssign(){
        nameEt = (EditText)findViewById(R.id.ptPharmaName);
        phnoEt= (EditText)findViewById(R.id.ptPharmaPhNo);
        strNameEt= (EditText)findViewById(R.id.ptPharmaStoreName);
        strAddEt= (EditText)findViewById(R.id.ptPharmaStoreAdd);
        emailEt = (EditText)findViewById(R.id.ptPharmaEmail);
        passwordEt = (EditText)findViewById(R.id.ptPharmaPass);
        conpasswordEt = (EditText)findViewById(R.id.ptPharmaCnfPass);
        signup = (Button)findViewById(R.id.btnPharmaSignup);
        progressDialog = new ProgressDialog(SignupAsPharmaActivity.this);
        login = (TextView)findViewById(R.id.tvLogin);
        cvProfileImage = (CircleImageView) findViewById(R.id.btnPharmaProfilePic);
        radioGenGroup = (RadioGroup) findViewById(R.id.rgrpGender);
        auth = FirebaseAuth.getInstance();
        resultUri = Uri.EMPTY;
    }

    public void choosepic(){
        cvProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(SignupAsPharmaActivity.this);
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
                cvProfileImage.setImageURI(null);
                cvProfileImage.setImageURI(resultUri);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    private boolean validate(){
        boolean result = false;
        String emailS = emailEt.getText().toString();
        String passwordS= passwordEt.getText().toString();
        String conpasswordS = conpasswordEt.getText().toString();
        if(resultUri.toString().isEmpty()) {
            Toast.makeText(this, "Upload Your Profile Photo!", Toast.LENGTH_SHORT).show();
            cvProfileImage.requestFocus();
        }else if(emailS.equals("") || !emailS.matches(emailPattern)){
            emailEt.setError("Enter valid email");
            emailEt.requestFocus();
        }else if(passwordS.equals("")|| passwordS.length()<6){
            passwordEt.setError("Password must be >6 letter");
            passwordEt.requestFocus();
        }else if(conpasswordS.equals("") || passwordS.length()<6){
            conpasswordEt.setError("Password must be >6 letter");
            conpasswordEt.requestFocus();
        }else if(!conpasswordS.equals(passwordS)){
            conpasswordEt.setError("Password doesn't match");
            conpasswordEt.requestFocus();
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
                startActivity(new Intent(SignupAsPharmaActivity.this,LoginActivity.class));
            }
        });
    }

    public void clickSignup() {
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()){
                    showProgDialog();
                    String user_email = emailEt.getText().toString().trim();
                    String user_password = passwordEt.getText().toString().trim();
                    auth.createUserWithEmailAndPassword(user_email,user_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(SignupAsPharmaActivity.this, "Registration Complete", Toast.LENGTH_SHORT).show();
                                adduser();
                                startActivity(new Intent(SignupAsPharmaActivity.this, BasicPharmacistActivity.class));
                                progressDialog.dismiss();
                                finish();
                            }
                            else{
                                String e ="Failed to create user:"+task.getException().getMessage();
                                Toast.makeText(SignupAsPharmaActivity.this, e , Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        }
                    });
                }
            }
        });
    }

    public void adduser() {

        radioGenButton = (RadioButton) findViewById(radioGenGroup.getCheckedRadioButtonId());
        String Name = nameEt.getText().toString();
        String Email = emailEt.getText().toString().trim();
        String Gender = radioGenButton.getText().toString();
        String StrName = strNameEt.getText().toString();
        String StrAdd = strAddEt.getText().toString();
        String PhNo = phnoEt.getText().toString();
        ID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        /*PharmacistUser pharmacistUser = new PharmacistUser(Name,Email,Gender,StrName,StrAdd,PhNo,ID);
        dbr.child(ID).setValue(pharmacistUser);*/

        dbr = dbr.child(ID);
        dbr.child("Name").setValue(Name);
        dbr.child("Email").setValue(Email);
        dbr.child("Gender").setValue(Gender);
        dbr.child("StrName").setValue(StrName);
        dbr.child("StrAdd").setValue(StrAdd);
        dbr.child("PhNo").setValue(PhNo);
        dbr.child("ID").setValue(ID);
        upload(resultUri);
    }

    void upload(Uri uri){

        StorageReference storageReference= FirebaseStorage.getInstance().getReference().child("PharmacistImages").child("ProfilePic");
        final StorageReference ref = storageReference.child(Objects.requireNonNull(uri.getLastPathSegment()));
        ref.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        dbr.child("ProfilePhoto").setValue(String.valueOf(uri));
                    }
                });
            }
        });
    }

    private void showProgDialog() {
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
}
