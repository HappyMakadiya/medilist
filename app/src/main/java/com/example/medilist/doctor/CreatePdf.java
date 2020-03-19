package com.example.medilist.doctor;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.cete.dynamicpdf.Document;
import com.cete.dynamicpdf.Font;
import com.cete.dynamicpdf.LineStyle;
import com.cete.dynamicpdf.Page;
import com.cete.dynamicpdf.PageOrientation;
import com.cete.dynamicpdf.PageSize;
import com.cete.dynamicpdf.RgbColor;
import com.cete.dynamicpdf.TextAlign;
import com.cete.dynamicpdf.pageelements.Label;
import com.cete.dynamicpdf.pageelements.Line;
import com.example.medilist.R;
import com.github.barteksc.pdfviewer.PDFView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;

public class CreatePdf extends AppCompatActivity {
    String spDrugType,spUnit,spDirec,spFreq,etDrugName,etDose,etQuant;
    String drname,drdegree,drphno,hptname,hptadd;
    PDFView pv;
    Bundle bundle;
    DatabaseReference dbreff;
    ProgressDialog progressDialog;
    private static String FILE = Environment.getExternalStorageDirectory() + "/prescription.pdf";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_pdf_viewer);
        progressDialog = new ProgressDialog(CreatePdf.this);
        showProgDialog();
        bundle = getIntent().getExtras();

        setBundle();
        dbreff = FirebaseDatabase.getInstance().getReference().child("Doctor").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        System.out.println(FirebaseAuth.getInstance().getCurrentUser().getUid());
        dbreff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                drname = dataSnapshot.child("Name").getValue().toString();
                drdegree = dataSnapshot.child("Degree").getValue().toString();
                drphno = dataSnapshot.child("PhNo").getValue().toString();
                hptname = dataSnapshot.child("HptName").getValue().toString();
                hptadd = dataSnapshot.child("HptAdd").getValue().toString();
                createPDF();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private void showProgDialog() {
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

    }
    private void createPDF() {
        Document objDocument = new Document();
        Page objPage = new Page(PageSize.A4, PageOrientation.PORTRAIT,54.0f);

        Label objLabel = new Label(hptname, 0, 0, 504, 70, Font.getHelvetica(), 40, TextAlign.CENTER);
        objLabel.setTextColor(RgbColor.getDarkGreen());
        objPage.getElements().add(objLabel);

        objLabel = new Label("Dr. "+ drname ,0, 70, 504, 21, Font.getHelvetica(), 18, TextAlign.LEFT);
        objPage.getElements().add(objLabel);

        objLabel = new Label("("+ drdegree+")" ,0, 91, 504, 21, Font.getHelvetica(), 16, TextAlign.LEFT);
        objPage.getElements().add(objLabel);

        objLabel = new Label(hptadd ,0, 112, 504, 21, Font.getHelvetica(), 16, TextAlign.LEFT);
        objPage.getElements().add(objLabel);

        objLabel = new Label(drphno ,0, 133, 504, 21, Font.getHelvetica(), 16, TextAlign.LEFT);
        objPage.getElements().add(objLabel);

        Line ml = new  Line(0, 166, 504, 166, 5, RgbColor.getDarkGreen(), LineStyle.getSolid());
        objPage.getElements().add(ml);

        objLabel = new Label("Date: " ,0, 180, 504, 21, Font.getHelvetica(), 16, TextAlign.LEFT);
        objPage.getElements().add(objLabel);
        objLabel = new Label("Patient Name: " ,0, 201, 504, 21, Font.getHelvetica(), 16, TextAlign.LEFT);
        objPage.getElements().add(objLabel);


        objDocument.getPages().add(objPage);
        try {
            // Outputs the document to file
            progressDialog.dismiss();
            objDocument.draw(FILE);
            Toast.makeText(this, "File has been written to :" + FILE,
                    Toast.LENGTH_LONG).show();
            File file = new File(FILE);
            pv = (PDFView)findViewById(R.id.pdfv1);
            pv.fromFile(file).load();
        } catch (Exception e) {
            Toast.makeText(this,
                    "Error, unable to write to file\n" + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }

    }

    private void setBundle() {
        spDrugType = bundle.getString("Drug_Type");
        spUnit = bundle.getString("Unit");
        spDirec = bundle.getString("Direction");
        spFreq = bundle.getString("Frequency");
        etDrugName = bundle.getString("Drug_Name");
        etDose = bundle.getString("Drug_Dose");
        etQuant = bundle.getString("Drug_Quantity");
    }
}