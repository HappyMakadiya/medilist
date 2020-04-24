package com.example.medilist.doctor;
import android.app.Activity;
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

import com.example.medilist.R;
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
    Spinner spDrugType,spDirec,spFreq;
    EditText etdisname,etpatid,etDrugName,etQuant,etDisName;
    Button btnadd,btnShowPdf,btnSendpdf,btnsearch;

    RecyclerView recyclerView;
    ArrayList <DrugList> lvarrayList;
    DrugListAdapter lvadapter;

    Intent intent;
    DatabaseReference dbrRx;
    String IDPatient,Patname,DrName;
    private static String FILE = Environment.getExternalStorageDirectory() + "/prescription.pdf";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_px);
        getSupportActionBar().setTitle("Create Prescription");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        addcompo();
        btnsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkandgetUser();
            }
        });
        onAddDrug();
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

    private void checkandgetUser() {
        DatabaseReference dbrPat = FirebaseDatabase.getInstance().getReference("Patient");
        Query q1 = dbrPat.orderByChild("Email").equalTo(etpatid.getText().toString());
        q1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot ss : dataSnapshot.getChildren()){
                        Toast.makeText(NewPxActivity.this, "Email Found", Toast.LENGTH_SHORT).show();
                        IDPatient= ss.getValue(PatientUser.class).getID();
                        Patname = ss.getValue(PatientUser.class).getName();
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

    private void addcompo() {
        etpatid = (EditText)findViewById(R.id.etPatientID);
        btnadd = (Button)findViewById(R.id.btnAddDrug);
        btnShowPdf = (Button)findViewById(R.id.btnShowPdf);
        btnSendpdf = (Button)findViewById(R.id.btnSendPdf);
        etDisName = (EditText)findViewById(R.id.etDis);
        btnsearch = (Button)findViewById(R.id.btnsearch);
        recyclerView=findViewById(R.id.rvdruglist);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        lvarrayList = new ArrayList<>();
        lvadapter = new DrugListAdapter(NewPxActivity.this,lvarrayList);
        dbrRx = FirebaseDatabase.getInstance().getReference("Patient");
    }

    private void onShowPdf() {

        Bundle bundle = new Bundle();
        intent = new Intent(NewPxActivity.this, CreatePdf.class);
        bundle.putSerializable("Druglist",(Serializable)lvarrayList);
        intent.putExtra("BundleDrugList",bundle);
        startActivity(intent);


    }

    private void onSendPdf() {
        if(TextUtils.isEmpty(etpatid.getText())){
            etpatid.setError("Enter Patient Email");
            etpatid.requestFocus();
        }else {
            DatabaseReference dbrPat = FirebaseDatabase.getInstance().getReference("Patient");
            Query q1 = dbrPat.orderByChild("Email").equalTo(etpatid.getText().toString());
            q1.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        for(DataSnapshot ss : dataSnapshot.getChildren()){
                            Toast.makeText(NewPxActivity.this, "Email Found", Toast.LENGTH_SHORT).show();
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

                            Calendar calendar = Calendar.getInstance();
                            SimpleDateFormat date = new SimpleDateFormat("dd-MMM-yyyy");
                            SimpleDateFormat day = new SimpleDateFormat("dd");
                            SimpleDateFormat month = new SimpleDateFormat("MMM");
                            SimpleDateFormat year = new SimpleDateFormat("yyyy");
                            String strDate = date.format(calendar.getTime());
                            String strDay = day.format(calendar.getTime());
                            String strMonth = month.format(calendar.getTime());
                            String strYear = year.format(calendar.getTime());


                            Uri file = Uri.fromFile(new File(FILE));
                            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Rx");
                            final StorageReference ref = storageReference.child(etpatid.getText().toString()).child(Objects.requireNonNull(file.getLastPathSegment()));
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
                            System.out.println(etDisName.getText().toString()+"///////////////////////////");
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


    /*private void setSpinnerAdapter(Spinner sp,String[] st){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(NewPxActivity.this,android.R.layout.simple_list_item_1,st);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(adapter);
    }*/

}
