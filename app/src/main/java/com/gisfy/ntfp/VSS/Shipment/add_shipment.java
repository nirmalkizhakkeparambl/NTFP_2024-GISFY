package com.gisfy.ntfp.VSS.Shipment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidbuts.multispinnerfilter.KeyPairBoolData;
import com.androidbuts.multispinnerfilter.MultiSpinnerListener;
import com.androidbuts.multispinnerfilter.MultiSpinnerSearch;
import com.gisfy.ntfp.API.RetrofitClient;
import com.gisfy.ntfp.HomePage.Home;
import com.gisfy.ntfp.HomePage.NTFPModel;
import com.gisfy.ntfp.HomePage.PCModel;
import com.gisfy.ntfp.Login.Models.VSSUser;
import com.gisfy.ntfp.R;
import com.gisfy.ntfp.RFO.Models.TransitPassModel;
import com.gisfy.ntfp.SqliteHelper.DBHelper;
import com.gisfy.ntfp.Utils.MultiSelectionSpinner;
import com.gisfy.ntfp.Utils.SharedPref;
import com.gisfy.ntfp.Utils.SnackBarUtils;
import com.gisfy.ntfp.Utils.StaticChecks;
import com.gisfy.ntfp.VSS.RequestForm.RFOModel;
import com.gisfy.ntfp.VSS.RequestForm.view_requests;
import com.google.android.material.textfield.TextInputEditText;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class add_shipment extends AppCompatActivity {

    private TextInputEditText vss,range,division,vehicle_no,vehicleOther;
    private TextView date;
    private Spinner processing_Center,vehicle_type;
    private MultiSpinnerSearch transitpass;
    private Button proceed;
    private StaticChecks checks;
    private DBHelper db;
    LinearLayout otherlayout;
    String vechicldata = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_shipment);
        intiViews();
        if (getIntent().hasExtra("date")){
            List<String> pcs=new ArrayList<>();
            date.setText(getIntent().getStringExtra("date"));
            pcs.add(getString(R.string.selectpc));
            pcs.add(getIntent().getStringExtra("pc"));
            initSpinner(Arrays.asList(getIntent().getStringExtra("pass").split(",")));
            ArrayAdapter<String> pcAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, pcs);
            processing_Center.setAdapter(pcAdapter);
        }else{
            final Calendar c = Calendar.getInstance();
            Date d = c.getTime();
            SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy");
            String date1 = format1.format(d);
            date.setText(date1);
            List<String> pcs=new ArrayList<>();
            pcs.add(getString(R.string.selectpc));
            try {
                for (String pc:db.getPCStrings()){
                    if (pc!=null)
                        pcs.add(pc);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            ArrayAdapter<String> pcAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_dropdown_item, pcs);
            processing_Center.setAdapter(pcAdapter);
            getTransitData();
        }
vehicle_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        if (i==8){
            otherlayout.setVisibility(View.VISIBLE);
        }else {
            otherlayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
});
        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (transitpass.getSelectedItems().size()>0){
                   if (processing_Center.getSelectedItemPosition()>0) {
                       if (vehicle_type.getSelectedItemPosition() > 0) {
                           if (checks.checkETList(new TextInputEditText[]{vss, range, division})) {
                               if (vehicle_type.getSelectedItem().toString().equals("Other")) {
                                   if (checks.checkETList(new TextInputEditText[]{vehicleOther})){
                                        vechicldata = vehicleOther.getText().toString();
                                   }
                               }else {
                                   vechicldata = vehicle_type.getSelectedItem().toString();
                               }
                               Log.i("vechicldata128",vechicldata);
                                   try {
                                   String transits = "";
                                   for (KeyPairBoolData keys : transitpass.getSelectedItems()) {
                                       if (keys.isSelected())
                                           transits += keys.getName().trim() + ",";
                                   }
                                   Date d = new Date();
                                   Log.i("vsscheck", vss.getText().toString());
                                   db.insertData(new Model_shipment(
                                           "SHIP" + d.getDay() + d.getMonth() + d.getMinutes() + d.getSeconds(),
                                           0,
                                           vss.getText().toString(),
                                           division.getText().toString(),
                                           range.getText().toString(),
                                           db.getpcIDfrompcName(processing_Center.getSelectedItem().toString()),
                                           date.getText().toString(),
                                           transits,
                                           vechicldata,
                                           vehicle_no.getText().toString()));
                                   startActivity(new Intent(add_shipment.this, list_shipment.class));
                               } catch (Exception e) {
                                   e.printStackTrace();
                                   SnackBarUtils.ErrorSnack(add_shipment.this, getString(R.string.somethingwentwrong));
                               }
                           }
                       } else {
                           SnackBarUtils.ErrorSnack(add_shipment.this,getString(R.string.vehicletype));
                       }
                   }
                       else{
                       SnackBarUtils.ErrorSnack(add_shipment.this,getString(R.string.selectpc));
                   }
                }else{
                    SnackBarUtils.ErrorSnack(add_shipment.this,getString(R.string.selecttransitpass));
                }
            }
        });
    }

    private void initSpinner(List<String> transits) {
        Log.i("shipment",transits.size()+"");

        List<KeyPairBoolData> keyPairList=new ArrayList<>();
        try {
            KeyPairBoolData tempKey = new KeyPairBoolData();
//            tempKey.setSelected(false);
//            tempKey.setName("Select Transit");
            keyPairList.remove(tempKey);
            for (String transit : transits) {
                if (transit!=null) {
                    KeyPairBoolData key = new KeyPairBoolData();
                    key.setSelected(false);
                    key.setName(transit);
                    keyPairList.add(key);
                }
            }
            if (keyPairList.size()<1)
                transitpass.setEnabled(false);
        } catch (Exception e) {
            e.printStackTrace();
        }

        transitpass.setSearchEnabled(true);
        transitpass.setSearchHint(getResources().getString(R.string.type_to_search));
        transitpass.setEmptyTitle(getResources().getString(R.string.nodata));
        transitpass.setClearText(getResources().getString(R.string.close));
        transitpass.setItems(keyPairList, new MultiSpinnerListener() {
            @Override
            public void onItemsSelected(List<KeyPairBoolData> items) {

            }
        });
    }

    private void intiViews() {
        checks=new StaticChecks(this);
        db=new DBHelper(this);
        TextView title=findViewById(R.id.titlebar_title);
        title.setText(getResources().getString(R.string.shipment));
        vss=findViewById(R.id.vss_name);
        range=findViewById(R.id.range_name);
        division=findViewById(R.id.division_name);
        transitpass= findViewById(R.id.spinner_transitpass);
        processing_Center=findViewById(R.id.spinner_processing);
        date=findViewById(R.id.date);
        vehicle_type=findViewById(R.id.vehicletype);
        vehicle_no=findViewById(R.id.edit_vehicle_no);
        vehicleOther=findViewById(R.id.vehicleOther);
        proceed=findViewById(R.id.add_collector_proceed);
         otherlayout = findViewById(R.id.vehicleOtherLayout);

        checks.setValues(new TextInputEditText[]{vss,division,range});
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        Log.i("Line 183","OK");
    }

    private void getTransitData() {
        ProgressDialog pd=new ProgressDialog(this);
        pd.setMessage("Loading Data...");
        pd.show();
        SharedPref pref=new SharedPref(this);
        VSSUser user=pref.getVSS();
        HashMap<String,String> json=new HashMap<>();
        json.put("DivisionId",user.getDivisionId()+"");
        json.put("RangeId",user.getRangeId()+"");
        json.put("VSSId",user.getVid()+"");
        json.put("ForShipment","Yes");
        Log.i("transitdata",json.toString());
        Call<List<TransitPassModel>> call = RetrofitClient.getInstance().getMyApi().getTransitPass(json);
        call.enqueue(new Callback<List<TransitPassModel>>() {
            @Override
            public void onResponse(Call<List<TransitPassModel>> call, Response<List<TransitPassModel>> response) {
                if (response.isSuccessful()){
                    List<String> transits=new ArrayList<>();
                    for (TransitPassModel model:response.body()){
                        transits.add(model.getTransUniqueId());
                    }
                    initSpinner(transits);
                }else{
                    List<String> transits=new ArrayList<>();
                    transits.add("No New Data");
                    initSpinner(transits);
                }
                pd.dismiss();
            }
            @Override
            public void onFailure(Call<List<TransitPassModel>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                pd.dismiss();
                List<String> transits=new ArrayList<>();
                transits.add("No New Data");
                initSpinner(transits);
            }
        });
    }
}