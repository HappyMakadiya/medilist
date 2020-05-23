package com.example.medilist.doctor;

import android.app.ProgressDialog;
import android.content.Context;
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
import com.example.medilist.LoginActivity;
import com.example.medilist.R;
import com.github.barteksc.pdfviewer.PDFView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.util.ArrayList;

public class CreatePdf {
    Context context;
    String drname,drdegree,drphno,hptname,hptadd;
    DatabaseReference dbreff;
    ArrayList<DrugList> drugLists;
    String patname;
    private static String FILE = Environment.getExternalStorageDirectory() + "/prescription.pdf";
    public CreatePdf(ArrayList<DrugList> d , Context c) {
        context = c;
        drugLists = d;
        dbreff = FirebaseDatabase.getInstance().getReference().child("Doctor").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        dbreff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                drname = dataSnapshot.child("Name").getValue().toString();
                drdegree = dataSnapshot.child("Degree").getValue().toString();
                drphno = dataSnapshot.child("PhNo").getValue().toString();
                hptname = dataSnapshot.child("HptName").getValue().toString();
                hptadd = dataSnapshot.child("HptAdd").getValue().toString();
                generatePDF();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void generatePDF() {
        Document objDocument = new Document();
        Page objPage = new Page(PageSize.A4, PageOrientation.PORTRAIT,54.0f);

        Label objLabel = new Label(hptname, 0, 0, 504, 70, Font.getHelvetica(), 40, TextAlign.CENTER);
        objLabel.setTextColor(RgbColor.getDarkGreen());
        objPage.getElements().add(objLabel);

        objLabel = new Label(drname ,0, 70, 504, 21, Font.getHelvetica(), 18, TextAlign.LEFT);
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
            objDocument.draw(FILE);
            Toast.makeText(context, "Pdf has created", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(context, "Pdf has not created", Toast.LENGTH_SHORT).show();
        }
    }
}