package com.gisfy.ntfp.RFO.Status;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;

import com.gisfy.ntfp.API.RetrofitClient;
import com.gisfy.ntfp.Login.Models.RFOUser;
import com.gisfy.ntfp.R;
import com.gisfy.ntfp.RFO.Adapters.DFOAdapter;
import com.gisfy.ntfp.RFO.Adapters.PositionClickListener;
import com.gisfy.ntfp.RFO.Models.DFOACKModel;
import com.gisfy.ntfp.RFO.Models.DFONTFPModel;
import com.gisfy.ntfp.RFO.Models.StatusModel;
import com.gisfy.ntfp.RFO.Models.TransitNTFPModel;
import com.gisfy.ntfp.RFO.Models.TransitPassModel;
import com.gisfy.ntfp.RFO.Models.VSSACKModel;
import com.gisfy.ntfp.RFO.Models.VSSNTFPModel;
import com.gisfy.ntfp.Utils.FCMNotifications;
import com.gisfy.ntfp.Utils.SharedPref;
import com.gisfy.ntfp.Utils.SnackBarUtils;
import com.gisfy.ntfp.Utils.StaticChecks;
import com.gisfy.ntfp.VSS.PCtoVSS.PCTOVSSActivity;
import com.gisfy.ntfp.VSS.RequestForm.ChartAlert;
import com.gisfy.ntfp.VSS.Shipment.edit_shipment;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.gisfy.ntfp.RFO.Status.ToVSS.setDate;
import static com.gisfy.ntfp.Utils.StaticChecks.isDateInRange;

public class ToDFO extends AppCompatActivity {


    private TabLayout tabLayout;
    private SharedPref pref;
    public List<DFOACKModel> list=new ArrayList<>();
    private DFOAdapter adapter;
    public ImageView cancel;
    public TableLayout tableLayout;
    public TextInputEditText remarks;
    public TextInputLayout remarksLayout;
    public Button accept,reject, filter;
    public Spinner reportSpinner,vssSpinner;
    public SpinKitView spinKitView;
    public ConstraintLayout bottomSheet;
    private final List<String> vssList=new ArrayList<>();
    private ArrayAdapter<String> vssAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transit);
        pref=new SharedPref(this);
        tabLayout=(TabLayout)findViewById(R.id.tabLayout);
        initViews();
        reject = findViewById(R.id.reject);
        reject.setVisibility(View.GONE);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.pending)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.approvedrejected)));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter=new DFOAdapter(list, ToDFO.this, new PositionClickListener() {
            @Override
            public void itemClicked(TransitPassModel position) {

            }
            @Override
            public void itemClicked(DFOACKModel position) {
                modelClick(position);
            }

            @Override
            public void itemClicked(VSSACKModel position) {

            }
        });
        recyclerView.setAdapter(adapter);
        RFOUser user=pref.getRFO();
        final HashMap<String,String> json=new HashMap<>();
        json.put("DivisionId",user.getDivisionId()+"");
        json.put("RangeId", user.getRangeId()+"");
        Log.i("DFOCheck",json.toString());
        getData(json);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                try {
                    updateTab(tab.getPosition());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
    private void initViews() {
        TextView tv=findViewById(R.id.titlebar_title);
        tv.setText(getResources().getString(R.string.todfo));
        remarksLayout=findViewById(R.id.remarks_layout);
        remarks=findViewById(R.id.remarks);
        accept=findViewById(R.id.accept);
        cancel=findViewById(R.id.cancel_button);
        tableLayout=findViewById(R.id.tableLayout);
        reportSpinner=findViewById(R.id.spinner);
        spinKitView=findViewById(R.id.spin_kit);
        bottomSheet=findViewById(R.id.bottomSheet_Layout);
        bottomSheet.bringToFront();
        findViewById(R.id.durationLayout).setVisibility(View.GONE);
        reportSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (reportSpinner.getSelectedItem().toString().equals("Other")) {
                    findViewById(R.id.remarks_layout).setVisibility(View.VISIBLE);
                }else {
                    findViewById(R.id.remarks_layout).setVisibility(View.GONE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        ArrayAdapter<String> pcAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item,getResources().getStringArray(R.array.dfo_report));
        reportSpinner.setAdapter(pcAdapter);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.bottomSheet_Layout).setVisibility(View.GONE);
            }
        });
        findViewById(R.id.backpayment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        initFilterViews();

    }

    private void initFilterViews() {
        filter = (Button) findViewById(R.id.save);
        Button cancel = (Button) findViewById(R.id.cancel);
        vssSpinner = (Spinner) findViewById(R.id.title_List);
        EditText from = (EditText) findViewById(R.id.fromDate);
        EditText to = (EditText) findViewById(R.id.toDate);
        Spinner statusSpinner = (Spinner) findViewById(R.id.statusSpinner);

        findViewById(R.id.filter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slide();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slide();
            }
        });


        vssList.add("Select any option");
        Log.i("vssList209",vssList.size()+"");
        vssAdapter = new ArrayAdapter<String>(ToDFO.this,
                android.R.layout.simple_spinner_dropdown_item,vssList);
        vssSpinner.setAdapter(vssAdapter);


        ArrayAdapter<String> statusadap = new ArrayAdapter<String>(ToDFO.this,
                android.R.layout.simple_spinner_dropdown_item,getResources().getStringArray(R.array.status_spinner_approved));
        statusSpinner.setAdapter(statusadap);
        setDate(ToDFO.this,from);
        setDate(ToDFO.this,to);
        if (tabLayout.getSelectedTabPosition()==0){
            statusSpinner.setVisibility(View.GONE);
            findViewById(R.id.statustv).setVisibility(View.GONE);
        }
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RFOUser user=pref.getRFO();
                final HashMap<String,String> json=new HashMap<>();
                json.put("DivisionId",user.getDivisionId()+"");
                json.put("RangeId", user.getRangeId()+"");

                if (from.getText().length()>0)
                    json.put("FromDate",from.getText().toString());
                if (to.getText().length()>0)
                    json.put("ToDate",to.getText().toString());
                if (vssSpinner.getSelectedItemPosition()!=0 )
                    json.put("VSS",vssSpinner.getSelectedItem().toString());
                if (statusSpinner.getSelectedItemPosition()!=0)
                    json.put("Status",statusSpinner.getSelectedItem().toString());

                getData(json);
                slide();
            }
        });
    }

    public void slide(){
        if(findViewById(R.id.filterLayout).getVisibility()==View.GONE) {
            if (tabLayout.getSelectedTabPosition()==0){
                findViewById(R.id.statustv).setVisibility(View.GONE);
                findViewById(R.id.statusSpinner).setVisibility(View.GONE);
            }else{
                findViewById(R.id.statustv).setVisibility(View.VISIBLE);
                findViewById(R.id.statusSpinner).setVisibility(View.VISIBLE);
            }
            Animation bottomUp = AnimationUtils.loadAnimation(this,
                    R.anim.slide_up);
            ViewGroup hiddenPanel = (ViewGroup) findViewById(R.id.filterLayout);
            hiddenPanel.startAnimation(bottomUp);
            hiddenPanel.setVisibility(View.VISIBLE);
            hiddenPanel.bringToFront();
        }else{
            Animation bottomUp = AnimationUtils.loadAnimation(this,
                    R.anim.slide_down);
            ViewGroup hiddenPanel = (ViewGroup)findViewById(R.id.filterLayout);
            hiddenPanel.startAnimation(bottomUp);
            hiddenPanel.setVisibility(View.GONE);
        }
    }

    private void getData(HashMap<String,String> json){
        Log.i("json",json.toString());
        list.clear();
        findViewById(R.id.spin_kit).setVisibility(View.VISIBLE);
        Call<List<DFOACKModel>> call = RetrofitClient.getInstance().getMyApi().getDFOACK(json);
        call.enqueue(new Callback<List<DFOACKModel>>() {
            @Override
            public void onResponse(Call<List<DFOACKModel>> call, Response<List<DFOACKModel>> response) {
                if (response.isSuccessful()){
                    list=response.body();
                    try {
                        updateTab(tabLayout.getSelectedTabPosition());
                    } catch (Exception e) {
                        e.printStackTrace();
                        SnackBarUtils.ErrorSnack(ToDFO.this,getString(R.string.nodata));
                    }
                }else{
                    SnackBarUtils.ErrorSnack(ToDFO.this,getString(R.string.unabletofetch));
                }
                findViewById(R.id.spin_kit).setVisibility(View.GONE);
            }
            @Override
            public void onFailure(Call<List<DFOACKModel>> call, Throwable t) {
                findViewById(R.id.spin_kit).setVisibility(View.GONE);
//                SnackBarUtils.ErrorSnack(ToDFO.this,getString(R.string.servernotresponding));
            }
        });
    }
    private void updateTab(int position){
        vssList.clear();
        vssList.add("Select Vss");
        bottomSheet.setVisibility(View.GONE);
        if (position==0){
            List<DFOACKModel> pending=new ArrayList<>();
            for (DFOACKModel model:list) {
                if (!model.getStatusByRFO().equals("Approved") && !model.getStatusByRFO().equals("Rejected")) {
                    pending.add(model);
                    if (!vssList.contains(model.getvSS()!=null?model.getvSS():"No Name"))
                        vssList.add(model.getvSS()!=null?model.getvSS():"No Name");
                }
            }
            adapter.updateData(pending);
        }else{
            List<DFOACKModel> other=new ArrayList<>();
            for (DFOACKModel model:list){
                if (model.getStatusByRFO().equals("Approved")||model.getStatusByRFO().equals("Rejected")){
                    other.add(model);
                }
                if (!vssList.contains(model.getvSS()!=null?model.getvSS():"No Name"))
                    vssList.add(model.getvSS()!=null?model.getvSS():"No Name");
            }
            adapter.updateData(other);
        }
        vssAdapter = new ArrayAdapter<String>(ToDFO.this,
                android.R.layout.simple_spinner_dropdown_item,vssList);
        vssSpinner.setAdapter(vssAdapter);

        Log.i("vssListsize325ToDFO",vssList.size()+"");

        adapter.notifyDataSetChanged();
        vssAdapter.notifyDataSetChanged();
        if (findViewById(R.id.filterLayout).getVisibility()==View.VISIBLE){
            Animation bottomUp = AnimationUtils.loadAnimation(this,
                    R.anim.slide_down);
            ViewGroup hiddenPanel = (ViewGroup)findViewById(R.id.filterLayout);
            hiddenPanel.startAnimation(bottomUp);
            hiddenPanel.setVisibility(View.GONE);
        }
    }
    private void modelClick(DFOACKModel model) {
        if (!model.getStatusByRFO().equals("Approved") && !model.getStatusByRFO().equals("Rejected")) {
//            reject.setVisibility(View.VISIBLE);
            accept.setText(getString(R.string.confirm));
            reportSpinner.setVisibility(View.GONE);
            remarksLayout.setVisibility(View.GONE);
            String[] titles = getResources().getStringArray(R.array.dfo_titles);
            List<List<String>> rows = new ArrayList<>();
            for (DFONTFPModel ntfpModel : model.getnTFP()) {
                List<String> row = new ArrayList<>();
                String grade1 = ntfpModel.getGrade1Qty()+ntfpModel.getUnit()+" - "+ntfpModel.getGrade1Cost()+" Rs.";
                String grade2 = ntfpModel.getGrade2Qty()+ntfpModel.getUnit()+" - "+ntfpModel.getGrade2Cost()+" Rs.";
                String grade3 = ntfpModel.getGrade3Qty()+ntfpModel.getUnit()+" - "+ntfpModel.getGrade3Cost()+" Rs.";

                row.add(ntfpModel.getnTFP());
                row.add(grade1);
                row.add(grade2);
                row.add(grade3);
                row.add(ntfpModel.getTotalCost());
                rows.add(row);
            }
            new StaticChecks(ToDFO.this).setTableLayout(tableLayout, titles, rows);
            bottomSheet.setVisibility(View.VISIBLE);
            StaticChecks.slideInFromBottom(ToDFO.this,bottomSheet);

            accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!model.getStatusByRFO().equals("Approved")&&!model.getStatusByRFO().equals("Rejected")){
                        if (reportSpinner.getVisibility() == View.VISIBLE) {
                            if (reportSpinner.getSelectedItemPosition()!=0){
                                if (!reportSpinner.getSelectedItem().toString().equals("Other")){
                                    setACK( model.getPurchaseOrderNumber(), "Rejected",reportSpinner.getSelectedItem().toString(),model);
                                }else{
                                    setACK(model.getPurchaseOrderNumber(), "Rejected",remarks.getText().toString(),model);
                                }
                            }else{
                                SnackBarUtils.ErrorSnack(ToDFO.this,"Select any reason");
                            }
                        } else {
                            setACK(model.getPurchaseOrderNumber(), "Approved",remarks.getText().toString(),model);
                        }
                    }
                }
            });
//            reject.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    reject.setVisibility(View.GONE);
//                    reportSpinner.setVisibility(View.VISIBLE);
//                    accept.setText("Submit");
//                }
//            });
        }
        else{
            String[] titles = getResources().getStringArray(R.array.dfo_titles);
            List<List<String>> rows = new ArrayList<>();
            for (DFONTFPModel ntfpModel : model.getnTFP()) {
                List<String> row = new ArrayList<>();
                String grade1 = ntfpModel.getGrade1Qty()+ntfpModel.getUnit()+" - "+ntfpModel.getGrade1Cost()+" Rs.";
                String grade2 = ntfpModel.getGrade2Qty()+ntfpModel.getUnit()+" - "+ntfpModel.getGrade2Cost()+" Rs.";
                String grade3 = ntfpModel.getGrade3Qty()+ntfpModel.getUnit()+" - "+ntfpModel.getGrade3Cost()+" Rs.";

                row.add(ntfpModel.getnTFP());
                row.add(grade1);
                row.add(grade2);
                row.add(grade3);
                row.add(ntfpModel.getTotalCost());
                rows.add(row);
            }
            new ChartAlert(ToDFO.this,titles,rows).show();
        }
    }
    private void setACK( final String id, final String status, final String remarks, DFOACKModel model){
        spinKitView.setVisibility(View.VISIBLE);
        final HashMap<String,String> json=new HashMap<>();
        json.put("PurchaseOrderNumber",id);
        json.put("StatusByRFO", status);
        json.put("Remarks", remarks);
        Log.i("json",json.toString());
        Call<List<StatusModel>> call = RetrofitClient.getInstance().getMyApi().setDFOACK(json);
        call.enqueue(new Callback<List<StatusModel>>() {
            @Override
            public void onResponse(Call<List<StatusModel>> call, retrofit2.Response<List<StatusModel>> response) {
                if (response.isSuccessful()){
                    Log.i("body",response.body().get(0).getStatus());
                    if (response.body().get(0).getStatus().equals("Success")){
                        SnackBarUtils.SuccessSnack(ToDFO.this,getString(R.string.sucess));
                        RFOUser user=pref.getRFO();
                        final HashMap<String,String> json=new HashMap<>();
                        json.put("DivisionId",user.getDivisionId()+"");
                        json.put("RangeId", user.getRangeId()+"");
                        getData(json);

                    }else{
                        SnackBarUtils.ErrorSnack(ToDFO.this,getString(R.string.unabletofetch));
                    }
                }
                spinKitView.setVisibility(View.GONE);
                bottomSheet.setVisibility(View.GONE);
            }
            @Override
            public void onFailure(Call<List<StatusModel>> call, Throwable t) {
                spinKitView.setVisibility(View.GONE);
                bottomSheet.setVisibility(View.GONE);
                SnackBarUtils.ErrorSnack(ToDFO.this,getString(R.string.servernotresponding));
            }
        });
    }
}