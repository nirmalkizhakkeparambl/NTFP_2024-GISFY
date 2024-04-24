package com.gisfy.ntfp.VSS.PCtoVSS;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;

import com.gisfy.ntfp.API.RetrofitClient;
import com.gisfy.ntfp.Collectors.CollectorStockModel;
import com.gisfy.ntfp.Login.Models.RFOUser;
import com.gisfy.ntfp.Login.Models.VSSUser;
import com.gisfy.ntfp.R;
import com.gisfy.ntfp.RFO.Adapters.PositionClickListener;
import com.gisfy.ntfp.RFO.Adapters.VSSAdapter;
import com.gisfy.ntfp.RFO.Models.DFOACKModel;
import com.gisfy.ntfp.RFO.Models.DFONTFPModel;
import com.gisfy.ntfp.RFO.Models.StatusModel;
import com.gisfy.ntfp.RFO.Models.TransitNTFPModel;
import com.gisfy.ntfp.RFO.Models.TransitPassModel;
import com.gisfy.ntfp.RFO.Models.VSSACKModel;
import com.gisfy.ntfp.RFO.Models.VSSNTFPModel;
import com.gisfy.ntfp.RFO.Status.ToVSS;
import com.gisfy.ntfp.RFO.Status.TransitActivity;
import com.gisfy.ntfp.Utils.FCMNotifications;
import com.gisfy.ntfp.Utils.SharedPref;
import com.gisfy.ntfp.Utils.SnackBarUtils;
import com.gisfy.ntfp.Utils.StaticChecks;
import com.gisfy.ntfp.VSS.RequestForm.ChartAlert;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.gisfy.ntfp.RFO.Status.ToVSS.setDate;

public class PCTOVSSActivity extends AppCompatActivity {


    private TabLayout tabLayout;
    public ImageView filter;
    public List<VSSACKModel> list=new ArrayList<>();

    private VSSAdapter adapter;
    private SharedPref pref;

    public ImageView cancel;
    public TableLayout tableLayout;
    public TextInputEditText remarks;
    public TextInputLayout remarksLayout;
    public Button reject,accept;
    public Spinner reportSpinner;
    public SpinKitView spinKitView;
    public ConstraintLayout bottomSheet;
    private VSSUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transit);
        initViews();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.pending)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.ack)));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter=new VSSAdapter(list, PCTOVSSActivity.this, new PositionClickListener() {
            @Override
            public void itemClicked(TransitPassModel position) {

            }
            @Override
            public void itemClicked(DFOACKModel position) {
            }

            @Override
            public void itemClicked(VSSACKModel position) {
                modelClick(position);

            }
        });
        recyclerView.setAdapter(adapter);
        user=pref.getVSS();
        final HashMap<String,String> json=new HashMap<>();
        json.put("DivisionId",user.getDivisionId()+"");
        json.put("RangeId", user.getRangeId()+"");
        json.put("VSSId", user.getVid()+"");
Log.i("PCCheck",json.toString());
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
        tv.setText(getResources().getString(R.string.tovss));
        remarksLayout=findViewById(R.id.remarks_layout);
        remarks=findViewById(R.id.remarks);
        accept=findViewById(R.id.accept);
        reject=findViewById(R.id.reject);
        cancel=findViewById(R.id.cancel_button);
        tableLayout=findViewById(R.id.tableLayout);
        reportSpinner=findViewById(R.id.spinner);
        spinKitView=findViewById(R.id.spin_kit);
        bottomSheet=findViewById(R.id.bottomSheet_Layout);
        bottomSheet.bringToFront();
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
        tabLayout=(TabLayout)findViewById(R.id.tabLayout);
        pref=new SharedPref(this);
        ArrayAdapter<String> pcAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item,getResources().getStringArray(R.array.vss_report));
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
        Button filter = (Button) findViewById(R.id.save);
        Button cancel = (Button) findViewById(R.id.cancel);
        Spinner vssSpinner = (Spinner) findViewById(R.id.title_List);
        EditText from = (EditText) findViewById(R.id.fromDate);
        EditText to = (EditText) findViewById(R.id.toDate);
        Spinner statusSpinner = (Spinner) findViewById(R.id.statusSpinner);
        findViewById(R.id.titletv).setVisibility(View.GONE);
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

       vssSpinner.setVisibility(View.GONE);

        ArrayAdapter<String> statusadap = new ArrayAdapter<String>(PCTOVSSActivity.this,
                android.R.layout.simple_spinner_dropdown_item,getResources().getStringArray(R.array.status_spinner_approved));
        statusSpinner.setAdapter(statusadap);
        setDate(PCTOVSSActivity.this,from);
        setDate(PCTOVSSActivity.this,to);

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final HashMap<String,String> json=new HashMap<>();
                json.put("DivisionId",user.getDivisionId()+"");
                json.put("RangeId", user.getRangeId()+"");

                if (from.getText().length()>0)
                    json.put("FromDate",from.getText().toString());
                if (to.getText().length()>0)
                    json.put("ToDate",to.getText().toString());
                if (statusSpinner.getSelectedItemPosition()!=0)
                    json.put("Status",statusSpinner.getSelectedItem().toString());
                Log.i("kishore",json.toString());
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

    public void getData(HashMap<String,String> json){
        list.clear();
        Log.i("DUBGGING",json.toString());
        findViewById(R.id.spin_kit).setVisibility(View.VISIBLE);
        Call<List<VSSACKModel>> call = RetrofitClient.getInstance().getMyApi().getVSSACK(json);
        call.enqueue(new Callback<List<VSSACKModel>>() {
            @Override
            public void onResponse(Call<List<VSSACKModel>> call, Response<List<VSSACKModel>> response) {
                if (response.isSuccessful()){
                    list =response.body();
                    try {
                        updateTab(tabLayout.getSelectedTabPosition());
                    } catch (Exception e) {
                        e.printStackTrace();
                        SnackBarUtils.ErrorSnack(PCTOVSSActivity.this,getString(R.string.nodata));
                    }
                }else{
                    SnackBarUtils.ErrorSnack(PCTOVSSActivity.this,getString(R.string.servernotresponding));
                }
                findViewById(R.id.spin_kit).setVisibility(View.GONE);
            }
            @Override
            public void onFailure(Call<List<VSSACKModel>> call, Throwable t) {
                findViewById(R.id.spin_kit).setVisibility(View.GONE);
                SnackBarUtils.ErrorSnack(PCTOVSSActivity.this,getString(R.string.servernotresponding));
            }
        });
    }
    private void updateTab(int position){
        slide();
        bottomSheet.setVisibility(View.GONE);
        if (position==0){
            List<VSSACKModel> pending=new ArrayList<>();
            for (VSSACKModel model:list){
                if (model.getStatusByRFO().equals("Pending")){
                    pending.add(model);
                }
            }
            adapter.updateData(pending);
        }else{
            List<VSSACKModel> other=new ArrayList<>();
            for (VSSACKModel model:list){
                if (!model.getStatusByRFO().equals("Pending")){
                    other.add(model);
                }
            }
            adapter.updateData(other);
        }
        if (findViewById(R.id.filterLayout).getVisibility()==View.VISIBLE){
            Animation bottomUp = AnimationUtils.loadAnimation(this,
                    R.anim.slide_down);
            ViewGroup hiddenPanel = (ViewGroup)findViewById(R.id.filterLayout);
            hiddenPanel.startAnimation(bottomUp);
            hiddenPanel.setVisibility(View.GONE);
        }
    }
    private void modelClick(VSSACKModel model) {
        String[] titles = getResources().getStringArray(R.array.dfo_titles);
        List<List<String>> rows = new ArrayList<>();
        for (VSSNTFPModel ntfpModel : model.getnTFP()) {
            List<String> row = new ArrayList<>();
            String grade1 = ntfpModel.getGrade1Qty()+ntfpModel.getUnit()+" - "+ntfpModel.getGrade1Cost()+" Rs.";
            String grade2 = ntfpModel.getGrade2Qty()+ntfpModel.getUnit()+" - "+ntfpModel.getGrade2Cost()+" Rs.";
            String grade3 = ntfpModel.getGrade3Qty()+ntfpModel.getUnit()+" - "+ntfpModel.getGrade3Cost()+" Rs.";

            row.add(ntfpModel.getnTFPName());
            row.add(grade1);
            row.add(grade2);
            row.add(grade3);
            row.add(ntfpModel.getTotal());
            rows.add(row);
        }
        new ChartAlert(PCTOVSSActivity.this,titles,rows).show();
    }

    public static void setDate(Context context, EditText editText){
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog=new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar mCalender = Calendar.getInstance();
                mCalender.set(Calendar.YEAR,year);
                mCalender.set(Calendar.MONTH,month);
                mCalender.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                dateFormat.setTimeZone(mCalender.getTimeZone());
                editText.setText(dateFormat.format(mCalender.getTime()));
            }
        },year,month,dayOfMonth);

        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });
    }
}