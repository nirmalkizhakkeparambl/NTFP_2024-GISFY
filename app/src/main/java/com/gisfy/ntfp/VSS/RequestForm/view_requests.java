package com.gisfy.ntfp.VSS.RequestForm;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;

import com.gisfy.ntfp.API.RetrofitClient;
import com.gisfy.ntfp.HomePage.Home;
import com.gisfy.ntfp.Login.Models.RFOUser;
import com.gisfy.ntfp.Login.Models.VSSUser;
import com.gisfy.ntfp.R;
import com.gisfy.ntfp.RFO.Adapters.PositionClickListener;
import com.gisfy.ntfp.RFO.Adapters.TransitPassAdapter;
import com.gisfy.ntfp.RFO.Models.DFOACKModel;
import com.gisfy.ntfp.RFO.Models.StatusModel;
import com.gisfy.ntfp.RFO.Models.TransitNTFPModel;
import com.gisfy.ntfp.RFO.Models.TransitPassModel;
import com.gisfy.ntfp.RFO.Models.VSSACKModel;
import com.gisfy.ntfp.SqliteHelper.DBHelper;
import com.gisfy.ntfp.Utils.FCMNotifications;
import com.gisfy.ntfp.Utils.SharedPref;
import com.gisfy.ntfp.Utils.SnackBarUtils;
import com.gisfy.ntfp.Utils.StaticChecks;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.gisfy.ntfp.RFO.Status.ToVSS.setDate;

public class view_requests extends AppCompatActivity {

    private static final int STORAGE_PERMISSION_REQUEST_CODE = 1;

    private TabLayout tabLayout;
    private SharedPref pref;
    private List<TransitPassModel> list=new ArrayList<>();
    private adapter_request_list adapter;
    public ImageView cancel;
    public TableLayout tableLayout;
    public TextInputEditText remarks;
    public TextInputLayout remarksLayout;
    public Button reject,accept;
    public Spinner reportSpinner,statusSpinner;
    public SpinKitView spinKitView;
    public ConstraintLayout bottomSheet;
    private Button filter,filterCancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transit);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

        tabLayout=(TabLayout)findViewById(R.id.tabLayout);
        pref=new SharedPref(this);
        initViews();
        VSSUser user=pref.getVSS();
        final HashMap<String,String> json=new HashMap<>();
        json.put("DivisionId",user.getDivisionId()+"");
        json.put("RangeId", user.getRangeId()+"");
        json.put("VSSId",user.getVid()+"");
        Log.i("dataget90da",json.toString());

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.pending)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.approvedrejected)));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter=new adapter_request_list(list, view_requests.this);
        recyclerView.setAdapter(adapter);


        getData(json);


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                try {
                    updateTab(tab.getPosition());
                } catch (Exception e) {
                    e.printStackTrace();
                    SnackBarUtils.ErrorSnack(view_requests.this,getString(R.string.somethingwentwrong));
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
        ArrayAdapter<String> pcAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item,getResources().getStringArray(R.array.transit_report));
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
        filterCancel = (Button) findViewById(R.id.cancel);
        EditText from = (EditText) findViewById(R.id.fromDate);
        EditText to = (EditText) findViewById(R.id.toDate);
        statusSpinner = (Spinner) findViewById(R.id.statusSpinner);

        filterCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slide();
            }
        });
        findViewById(R.id.filter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slide();
            }
        });


        ArrayAdapter<String> statusadap = new ArrayAdapter<String>(view_requests.this,
        android.R.layout.simple_spinner_dropdown_item,getResources().getStringArray(R.array.status_spinner_accepted));
        statusSpinner.setAdapter(statusadap);
        setDate(view_requests.this,from);
        setDate(view_requests.this,to);

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VSSUser user=pref.getVSS();
                final HashMap<String,String> json=new HashMap<>();
                json.put("DivisionId",user.getDivisionId()+"");
                json.put("RangeId", user.getRangeId()+"");
                json.put("VSSId",user.getVid()+"");
                if (from.getText().length()>0)
                    json.put("FromDate",from.getText().toString());
                if (to.getText().length()>0)
                    json.put("ToDate",to.getText().toString());
                if (statusSpinner.getSelectedItemPosition()!=0)
                    json.put("TransitStatus",statusSpinner.getSelectedItem().toString());
                Log.i("json212",json.toString());
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
        findViewById(R.id.spin_kit).setVisibility(View.VISIBLE);
        Call<List<TransitPassModel>> call = RetrofitClient.getInstance().getMyApi().getTransitPass(json);
        call.enqueue(new Callback<List<TransitPassModel>>() {
            @Override
            public void onResponse(Call<List<TransitPassModel>> call, Response<List<TransitPassModel>> response) {
                if (response.isSuccessful()){
                    Log.i("LogResponce",list.size()+"");
                    list=response.body();
                    try {
                        updateTab(tabLayout.getSelectedTabPosition());
                    } catch (Exception e) {
                        e.printStackTrace();
                        SnackBarUtils.ErrorSnack(view_requests.this,getString(R.string.nodata));
                    }
                }else{
                    SnackBarUtils.ErrorSnack(view_requests.this,getString(R.string.servernotresponding));
                }
                findViewById(R.id.spin_kit).setVisibility(View.GONE);
            }
            @Override
            public void onFailure(Call<List<TransitPassModel>> call, Throwable t) {

                SnackBarUtils.ErrorSnack(view_requests.this,getString(R.string.nodata));
            }
        });
    }
    private void updateTab(int position){
        bottomSheet.setVisibility(View.GONE);
        if (position==0){
            findViewById(R.id.title_List).setVisibility(View.GONE);
            findViewById(R.id.titletv).setVisibility(View.GONE);
            findViewById(R.id.statustv).setVisibility(View.GONE);
            findViewById(R.id.statusSpinner).setVisibility(View.GONE);


            List<TransitPassModel> pending=new ArrayList<>();
            for (TransitPassModel model:list){
                if (!model.getTransitStatus().equals("Accepted")&&!model.getTransitStatus().equals("Rejected")){
                    pending.add(model);
                }
            }
            adapter.updateData(pending);
        }else{
            List<TransitPassModel> other=new ArrayList<>();
            findViewById(R.id.title_List).setVisibility(View.GONE);
            findViewById(R.id.titletv).setVisibility(View.GONE);
            findViewById(R.id.statustv).setVisibility(View.VISIBLE);
            findViewById(R.id.statusSpinner).setVisibility(View.VISIBLE);
            for (TransitPassModel model:list){
                if (model.getTransitStatus().equals("Accepted")||model.getTransitStatus().equals("Rejected")){
                    other.add(model);
                }
            }
            adapter.updateData(other);
        }
        adapter.notifyDataSetChanged();
        if (findViewById(R.id.filterLayout).getVisibility()==View.VISIBLE){
            Animation bottomUp = AnimationUtils.loadAnimation(this,
                    R.anim.slide_down);
            ViewGroup hiddenPanel = (ViewGroup)findViewById(R.id.filterLayout);
            hiddenPanel.startAnimation(bottomUp);
            hiddenPanel.setVisibility(View.GONE);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case STORAGE_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Snackbar.make(urlTextInputLayout, "Permission Granted, Now you can write storage.", Snackbar.LENGTH_LONG).show();
                } else {
                    //Snackbar.make(urlTextInputLayout, "Permission Denied, You cannot access storage.", Snackbar.LENGTH_LONG).show();
                }
                break;
        }
    }
}