package com.gisfy.ntfp.Collectors;

import android.os.Bundle;

import com.gisfy.ntfp.R;
import com.pdfview.PDFView;

import androidx.appcompat.app.AppCompatActivity;


public class PDFViewer extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_viewer);
//        Uri path = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.document);
        PDFView pdfView=findViewById(R.id.activity_main_pdf_view);
        pdfView.fromAsset("document.pdf");
        pdfView.show();
    }
}