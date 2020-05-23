package com.example.medilist.doctor;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Environment;

import androidx.appcompat.app.AppCompatActivity;

import com.example.medilist.R;
import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;


public class ShowPdfViewer extends AppCompatActivity {
    PDFView pv;

    private static String FILE = Environment.getExternalStorageDirectory() + "/prescription.pdf";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_pdf_viewer);
        showPdf();
    }
    private void showPdf() {
        File file = new File(FILE);
        pv = (PDFView)findViewById(R.id.pdfv1);
        pv.fromFile(file).load();
    }

}
