package com.example.medilist.doctor;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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
import com.example.medilist.R;
import com.example.medilist.patient.PatientUser;
import com.google.android.gms.tasks.OnSuccessListener;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;
public class NewPxActivity extends AppCompatActivity {
    Spinner spDrugType,spDirec,spFreq;
    EditText etpatid,etDrugName,etQuant,etDisName;
    Button btnadd,btnShowPdf,btnSendpdf;
    ListView lv;
    ArrayList <DrugList> lvarrayList;
    DrugListAdapter lvadapter;
    Intent intent;
    TextView checkuser;
    boolean re;
    DatabaseReference dbrRx;
    String IDPatient;
    private static String FILE = Environment.getExternalStorageDirectory() + "/prescription.pdf";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_px);
        getSupportActionBar().setTitle("Create Prescription");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        addcompo();
        lv.setAdapter(lvadapter);
        onAddDrug();
        onShowPdf();
        onSendPdf();
    }


    private void onShowPdf() {
        Bundle bundle = new Bundle();
        bundle.putString("Disease_Name", String.valueOf(etDisName));
        bundle.putString("Drug_Type", String.valueOf(spDrugType));
        bundle.putString("Direction", String.valueOf(spDirec));
        bundle.putString("Frequency", String.valueOf(spFreq));
        bundle.putString("Drug_Name", String.valueOf(etDrugName));
        bundle.putString("Drug_Quantity", String.valueOf(etQuant));
        intent = new Intent(NewPxActivity.this, CreatePdf.class);
        intent.putExtras(bundle);
        btnShowPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent);
            }
        });

    }

    private void onSendPdf() {
        btnSendpdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkUserEmail();
            }
        });
    }
    private void checkUserEmail() {
        DatabaseReference dbr = FirebaseDatabase.getInstance().getReference("Patient");
        Query q1 = dbr.orderByChild("Email").equalTo(etpatid.getText().toString());
        q1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot ss : dataSnapshot.getChildren()){
                        IDPatient= ss.getValue(PatientUser.class).getID();


                        Calendar calendar = Calendar.getInstance();
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy");
                        String str = simpleDateFormat.format(calendar.getTime());

                        Uri file = Uri.fromFile(new File(FILE));
                        StorageReference storageReference= FirebaseStorage.getInstance().getReference().child("Rx");
                        final StorageReference ref = storageReference.child(etpatid.getText().toString()).child(Objects.requireNonNull(file.getLastPathSegment()));
                        ref.putFile(file).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        dbrRx.child(IDPatient).child("Rx").child(str).setValue(String.valueOf(uri));
                                    }
                                });
                            }
                        });
                        Toast.makeText(NewPxActivity.this, "Rx has been sent..", Toast.LENGTH_SHORT).show();
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
        lv = (ListView)findViewById(R.id.lstvDrug);
        checkuser =(TextView)findViewById(R.id.tvcheckuser);
        etDisName = (EditText)findViewById(R.id.etDis);
        lvarrayList = new ArrayList<>();
        lvadapter = new DrugListAdapter(NewPxActivity.this,R.layout.adapter_drug_layout,lvarrayList);
        dbrRx = FirebaseDatabase.getInstance().getReference("Patient");
    }

    private void onAddDrug() {

        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mb = new AlertDialog.Builder(NewPxActivity.this);
                View v = getLayoutInflater().inflate(R.layout.layout_px_dialog,null);
                mb.setTitle("Add Drug");


                spDrugType = (Spinner) v.findViewById(R.id.spDrugType);
                setSpinnerAdapter(spDrugType,getResources().getStringArray(R.array.drugType_array));

                spDirec = (Spinner) v.findViewById(R.id.spDirec);
                setSpinnerAdapter(spDirec,getResources().getStringArray(R.array.direc_array));

                spFreq = (Spinner) v.findViewById(R.id.spFreq);
                setSpinnerAdapter(spFreq,getResources().getStringArray(R.array.freq_array));

                etDrugName = (EditText) v.findViewById(R.id.etDrugName);
                etQuant = (EditText) v.findViewById(R.id.etQuant);
                etDisName = (EditText) v.findViewById(R.id.etDis);
                mb.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        DrugList lv2c = new DrugList(etDrugName.getText().toString(),etQuant.getText().toString());
                        lvarrayList.add(lv2c);
                        lvadapter.notifyDataSetChanged();
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


    private void setSpinnerAdapter(Spinner sp,String[] st){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(NewPxActivity.this,android.R.layout.simple_list_item_1,st);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(adapter);
    }

}
