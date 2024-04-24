package com.gisfy.ntfp.VSS.RequestForm;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TableLayout;

import com.gisfy.ntfp.R;
import com.gisfy.ntfp.RFO.Models.DFONTFPModel;
import com.gisfy.ntfp.RFO.Models.TransitNTFPModel;
import com.gisfy.ntfp.RFO.Status.ToDFO;
import com.gisfy.ntfp.Utils.StaticChecks;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class ChartAlert extends Dialog {

    private final Activity activity;
    private final List<List<String>> list;
    private final String[] titles;
    public ChartAlert(Activity a,String[] titles,List<List<String>> list) {
        super(a);
        this.activity = a;
        this.list=list;
        this.titles=titles;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_table_layout);
        TableLayout tableLayout=findViewById(R.id.tableLayout);
        tableLayout.setStretchAllColumns(true);
        new StaticChecks(activity).setTableLayout(tableLayout, titles, list);
        findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}