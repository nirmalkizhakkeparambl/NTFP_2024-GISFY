package com.gisfy.ntfp.VSS.Shipment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.gisfy.ntfp.Collectors.CollectorInventory;
import com.gisfy.ntfp.Login.Models.VSSUser;
import com.gisfy.ntfp.R;
import com.gisfy.ntfp.RFO.Models.TransitPassModel;
import com.gisfy.ntfp.SqliteHelper.DBHelper;
import com.gisfy.ntfp.Utils.MultiSelectionSpinner;
import com.gisfy.ntfp.Utils.SharedPref;
import com.gisfy.ntfp.Utils.SnackBarUtils;
import com.gisfy.ntfp.Utils.StaticChecks;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class edit_shipment extends AppCompatActivity {

    private TextInputEditText vss,range,division,vehicle_no,vehicleOther;
    private TextView date;
    private Spinner processing_Center,vehicle_type;
    private MultiSpinnerSearch transitpass;
    private Button proceed;
    private StaticChecks checks;
    private DBHelper db;
    private int syncstatus;
    private ArrayAdapter<String> pcAdapter;
    private String uuid;
    LinearLayout otherlayout;
    String vechicldata = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_shipment);
        intiViews();
        new populateData().execute();
        final Calendar c = Calendar.getInstance();


        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checks.checkETList(new TextInputEditText[]{})){
                    if (transitpass.getSelectedItems().size()>0){
                        if (processing_Center.getSelectedItemPosition()!=0) {
                            try {
                                db.updateData(new Model_shipment(
                                        uuid,
                                        0,
                                        vss.getText().toString(),
                                        division.getText().toString(),
                                        range.getText().toString(),
                                        db.getpcIDfrompcName(processing_Center.getSelectedItem().toString()),
                                        date.getText().toString(),
                                        transitpass.getSelectedItem().toString(),
                                        vehicle_type.getSelectedItem().toString(),
                                        vehicle_no.getText().toString()));
                                startActivity(new Intent(edit_shipment.this, list_shipment.class));
                            } catch (Exception e) {
                                e.printStackTrace();
                                SnackBarUtils.ErrorSnack(edit_shipment.this,getString(R.string.somethingwentwrong));
                            }
                        }else{
                            SnackBarUtils.ErrorSnack(edit_shipment.this,getString(R.string.selectpc));
                        }
                    }else{
                        SnackBarUtils.ErrorSnack(edit_shipment.this,getString(R.string.selecttransitpass));
                    }
                }
            }
        });
    }
    private void intiViews() {
        checks=new StaticChecks(this);
        db=new DBHelper(this);
        vss=findViewById(R.id.vss_name);
        TextView title=findViewById(R.id.titlebar_title);
        title.setText(getResources().getString(R.string.shipment));
        range=findViewById(R.id.division_name);
        division=findViewById(R.id.range_name);
        transitpass= findViewById(R.id.spinner_transitpass);
        processing_Center=findViewById(R.id.spinner_processing);
        date=findViewById(R.id.date);
        vehicle_type=findViewById(R.id.vehicletype);
        vehicle_no=findViewById(R.id.edit_vehicle_no);
        vehicleOther=findViewById(R.id.vehicleOther);
        proceed=findViewById(R.id.add_collector_proceed);
        otherlayout = findViewById(R.id.vehicleOtherLayout);

        List<String> pcs=new ArrayList<>();
        pcs.add("Select Processing Center");
        try {
            pcs.addAll(db.getPCStrings());
            if (!db.getPCStrings().contains(getIntent().getStringExtra("pc")))
                pcs.add(getIntent().getStringExtra("pc"));

            pcAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_dropdown_item, pcs);
            processing_Center.setAdapter(pcAdapter);

        } catch (Exception e) {
            e.printStackTrace();
        }

        proceed.setText(getString(R.string.updateShipment));

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    private class populateData extends AsyncTask<String,String,String> {
        ProgressDialog pd;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd=new ProgressDialog(edit_shipment.this);
            pd.setMessage("Loading...");
            pd.dismiss();
        }
        @Override
        protected String doInBackground(String... strings) {
            String uid=getIntent().getStringExtra("uid");
            try {
                final Model_shipment model=db.getAllDataFromShipmentsWhere("uid='"+uid+"'").get(0);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                            uuid=model.getUid();
                            date.setText(model.getDate());
                            vss.setText(model.getVss());
                            division.setText(model.getDivision());
                            range.setText(model.getRange());
                        ArrayAdapter<String> stateadapter = new ArrayAdapter<String>(edit_shipment.this,R.layout.support_simple_spinner_dropdown_item,getResources().getStringArray(R.array.vehicletype));
                        vehicle_type.setAdapter(stateadapter);
                        if (Arrays.asList(getResources().getStringArray(R.array.vehicletype)).contains(model.getVehicleType())) {
                            vehicle_type.setSelection(stateadapter.getPosition(model.getVehicleType()));
                        }else {
                            vehicle_type.setSelection(stateadapter.getPosition("Other"));
                            otherlayout.setVisibility(View.VISIBLE);
                            vehicleOther.setText(model.getVehicleType());
                        }


                            vehicle_no.setText(model.getVehicleNo());
                            syncstatus=model.getSynced();
                            if (SnackBarUtils.NetworkSnack(edit_shipment.this)){
                                getTransitData(model.getTransitPass());
                            }else{

                                initSpinner(Arrays.asList(model.getTransitPass().split(",")),model.getTransitPass());
                            }
                            processing_Center.setSelection(pcAdapter.getPosition(db.getpcpcNamefrompcID(model.getProcessing_center())));
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                return getString(R.string.foundsomeissues);
            }

            return "";
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (!s.equals(""))
                SnackBarUtils.ErrorSnack(edit_shipment.this,s);
            pd.dismiss();
        }
    }

    private void initSpinner(List<String> transits,String item) {

        Log.i("shipment",item);

        List<KeyPairBoolData> keyPairList=new ArrayList<>();
        try {
            KeyPairBoolData tempKey = new KeyPairBoolData();
            tempKey.setSelected(transits.contains(item));
            tempKey.setName("Select Transit");
            keyPairList.add(tempKey);
            for (String transit : transits) {
                if (transit!=null) {
                    Log.i("tansit",transit);
                    KeyPairBoolData key = new KeyPairBoolData();
                    key.setSelected(transit.equals(item.replace(",","")));
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
    public List<String> removeDuplicateElements(List<String> list) {
        List<String> newList = new ArrayList<String>();
        for (String element : list) {
            Log.i("element",element);
            if (!newList.contains(element.trim())) {
                newList.add(element);
            }
        }
        return newList;
    }
    private void getTransitData(String data) {
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
        Log.i("Transit pass json",json.toString());
        Call<List<TransitPassModel>> call = RetrofitClient.getInstance().getMyApi().getTransitPass(json);
        call.enqueue(new Callback<List<TransitPassModel>>() {
            @Override
            public void onResponse(Call<List<TransitPassModel>> call, Response<List<TransitPassModel>> response) {
                if (response.isSuccessful()){
                    List<String> transits = new ArrayList<>(addElements(data.split(",")));
                    for (TransitPassModel model:response.body()){
                        transits.add(model.getTransUniqueId().trim());
                    }
                    List<String> arr=removeDuplicateElements(transits);
                    initSpinner(arr,data);
                }
                pd.dismiss();
            }
            @Override
            public void onFailure(Call<List<TransitPassModel>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
        });
    }
    private List<String> addElements(String[] arr){
        List<String> newElements=new ArrayList<>();
        for (String element:arr){
            newElements.add(element.trim());
        }
        return newElements;
    }
}