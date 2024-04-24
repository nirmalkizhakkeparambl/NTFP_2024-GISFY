package com.gisfy.ntfp.Collectors;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.gisfy.ntfp.R;
import com.gisfy.ntfp.Utils.StaticChecks;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PDFList extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PDFAdapter adapter;
    public List<PDFModel> list=new ArrayList<>();
    public ImageView sync,filter;
    private TextView title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_collectors);
        intiViews();
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        final DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        new StaticChecks(PDFList.this).showSnackBar("Filtered...");
                    }
                }, mYear, mMonth, mDay);
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show();
            }
        });
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void intiViews() {
        title=findViewById(R.id.titlebar_title);
        title.setText(R.string.trainingmaterials);
        recyclerView=findViewById(R.id.recyclerView);
        sync=findViewById(R.id.synchronise);
        filter=findViewById(R.id.back);
        list.add(new PDFModel(R.drawable.vector_pdf,"document.pdf",getResources().getString(R.string.sustainableharvesting),""));
        adapter=new PDFAdapter(list,PDFList.this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(PDFList.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        findViewById(R.id.synchronise).setVisibility(View.GONE);
    }
}