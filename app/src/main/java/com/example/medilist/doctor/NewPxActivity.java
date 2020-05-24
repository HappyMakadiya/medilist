package com.example.medilist.doctor;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medilist.LoginActivity;
import com.example.medilist.R;
import com.example.medilist.SignupAsDRActivity;
import com.example.medilist.patient.PatientUser;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;
public class NewPxActivity extends AppCompatActivity {
    Boolean statusCreate = false,statususer=false;
    Spinner spDrugType,spDirec,spFreq;
    EditText etdisname,etpatid,etDrugName,etQuant,etDisName;
    Button btnadd,btnShowPdf,btnSendpdf,btnsearch,btnCreatePdf;
    ProgressDialog progressDialog;
    RecyclerView recyclerView;
    ArrayList <DrugList> lvarrayList;
    DrugListAdapter lvadapter;
    Intent intent;
    DatabaseReference dbrRx;
    String IDPatient,Patname,DrName,strDate,strDay,strMonth,strYear;
    Calendar calendar;
    SimpleDateFormat date,day,month,year;
    TextView tvsearchpatname;

    private static String FILE = Environment.getExternalStorageDirectory() + "/prescription.pdf";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_px);
        getSupportActionBar().setTitle("Create Prescription");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        addcompo();
        setDate();
        btnsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkandgetUser();
            }
        });
        onAddDrug();
        btnCreatePdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCreatePdf();
            }
        });
        btnShowPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onShowPdf();
            }
        });
        btnSendpdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSendPdf();
            }
        });


    }

    private void setDate() {
        calendar = Calendar.getInstance();
        date = new SimpleDateFormat("dd-MMM-yyyy");
        day = new SimpleDateFormat("dd");
        month = new SimpleDateFormat("MMM");
        year = new SimpleDateFormat("yyyy");
        strDate = date.format(calendar.getTime());
        strDay = day.format(calendar.getTime());
        strMonth = month.format(calendar.getTime());
        strYear = year.format(calendar.getTime());
    }

    private void addcompo() {
        progressDialog = new ProgressDialog(NewPxActivity.this);
        etpatid = (EditText)findViewById(R.id.etPatientID);
        btnadd = (Button)findViewById(R.id.btnAddDrug);
        btnShowPdf = (Button)findViewById(R.id.btnShowPdf);
        btnSendpdf = (Button)findViewById(R.id.btnSendPdf);
        btnCreatePdf = (Button)findViewById(R.id.btnCreatePdf);
        etDisName = (EditText)findViewById(R.id.etDis);
        tvsearchpatname=(TextView)findViewById(R.id.tvSearchPatName);
        btnsearch = (Button)findViewById(R.id.btnsearch);
        progressDialog = new ProgressDialog(NewPxActivity.this);
        recyclerView=findViewById(R.id.rvdruglist);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        lvarrayList = new ArrayList<>();
        lvadapter = new DrugListAdapter(NewPxActivity.this,lvarrayList);
        dbrRx = FirebaseDatabase.getInstance().getReference("Patient");
    }

    private  void onCreatePdf(){
        if(statususer){
                new CreatePdf(lvarrayList,getApplicationContext(),tvsearchpatname.getText().toString(),strDate);
                statusCreate = true;
        }
        else{
            Toast.makeText(this, "Verify Patient ID By Clicking Search Button!", Toast.LENGTH_SHORT).show();
        }

    }

    private void checkandgetUser() {

        DatabaseReference dbrPat = FirebaseDatabase.getInstance().getReference("Patient");
        Query q1 = dbrPat.orderByChild("Email").equalTo(etpatid.getText().toString());
        q1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot ss : dataSnapshot.getChildren()){
                        statususer = true;
                        Toast.makeText(NewPxActivity.this, "Email Found", Toast.LENGTH_SHORT).show();
                        IDPatient= ss.getValue(PatientUser.class).getID();
                        Patname = ss.getValue(PatientUser.class).getName();
                        tvsearchpatname.setText(Patname);

                    }
                }
                else{
                    Toast.makeText(NewPxActivity.this, "Email Not Found", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void onShowPdf() {
        if(statususer){
            if(statusCreate){
                intent = new Intent(NewPxActivity.this, ShowPdfViewer.class);
                startActivity(intent);
            }
            else {
                Toast.makeText(this, "First You have to create PDF!!", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(this, "Verify Patient ID By Clicking Search Button!", Toast.LENGTH_SHORT).show();
        }


    }

    private void onSendPdf() {
        if(TextUtils.isEmpty(etpatid.getText()) || !statususer){
            etpatid.setError("Enter Patient Email and Verify Patient ID By Clicking Search Button!");
            etpatid.requestFocus();
        }
//        if(!statususer){
//            Toast.makeText(this, "Verify Patient ID By Clicking Search Button!", Toast.LENGTH_SHORT).show();
//        }
       else {
           showProgDialog();
            DatabaseReference dbrPat = FirebaseDatabase.getInstance().getReference("Patient");
            Query q1 = dbrPat.orderByChild("Email").equalTo(etpatid.getText().toString());
            q1.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        for(DataSnapshot ss : dataSnapshot.getChildren()){
                            Toast.makeText(NewPxActivity.this, "Email Found", Toast.LENGTH_SHORT).show();
                            tvsearchpatname.setText(Patname);
                            DatabaseReference dbrDr = FirebaseDatabase.getInstance().getReference("Doctor").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                            dbrDr.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    DrName = dataSnapshot.getValue(DocterUser.class).getName();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                            Uri file = Uri.fromFile(new File(FILE));
                            long randomNumber = (long) (Math.random()*Math.pow(10,10));
                            String strrno = Long.toString(randomNumber);
                            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Rx");
                            final StorageReference ref = storageReference.child(etpatid.getText().toString()).child(Objects.requireNonNull(strrno.concat(Objects.requireNonNull(file.getLastPathSegment()))));
                            ref.putFile(file).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            dbrRx = dbrRx.child(IDPatient).child("Rx").push();
                                            dbrRx.child("Date").setValue(strDate);
                                            dbrRx.child("Day").setValue(strDay);
                                            dbrRx.child("Month").setValue(strMonth);
                                            dbrRx.child("Year").setValue(strYear);
                                            dbrRx.child("DrName").setValue(DrName);
                                            dbrRx.child("RxURI").setValue(String.valueOf(uri));

                                        }
                                    });
                                }
                            });
                            progressDialog.dismiss();
                            Toast.makeText(NewPxActivity.this, "Rx has been sent..", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                    else{
                        Toast.makeText(NewPxActivity.this, "Email Not Found", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }
    }

    private void onAddDrug() {

        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mb = new AlertDialog.Builder(NewPxActivity.this);
                View v = getLayoutInflater().inflate(R.layout.layout_px_dialog,null);
                mb.setTitle("Add Drug");

                etdisname = (EditText) v.findViewById(R.id.etDis);
                spDrugType = (Spinner) v.findViewById(R.id.spDrugType);
                etDrugName = (EditText) v.findViewById(R.id.etDrugName);
                spDirec = (Spinner) v.findViewById(R.id.spDirec);
                spFreq = (Spinner) v.findViewById(R.id.spFreq);
                etQuant = (EditText) v.findViewById(R.id.etQuant);

                mb.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            DrugList lv2c = new DrugList(etdisname.getText().toString(),etDrugName.getText().toString(),spDrugType.getSelectedItem().toString(),etQuant.getText().toString(),spDirec.getSelectedItem().toString(),spFreq.getSelectedItem().toString());
                            lvarrayList.add(lv2c);
                            lvadapter.notifyDataSetChanged();
                            recyclerView.setAdapter(lvadapter);

                        }
                    });

                mb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
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
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

}
