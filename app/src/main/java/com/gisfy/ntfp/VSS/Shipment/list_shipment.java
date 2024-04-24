package com.gisfy.ntfp.VSS.Shipment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import com.gisfy.ntfp.Login.Models.*;

import com.gisfy.ntfp.API.Api;
import com.gisfy.ntfp.HomePage.Home;
import com.gisfy.ntfp.Login.Models.ShipmentModel;
import com.gisfy.ntfp.R;
import com.gisfy.ntfp.SqliteHelper.DBHelper;
import com.gisfy.ntfp.Utils.*;
import com.gisfy.ntfp.Utils.StaticChecks;
import com.gisfy.ntfp.VSS.Collectors.list_collectors;
import com.gisfy.ntfp.VSS.Inventory.Inventory;
import com.gisfy.ntfp.VSS.RequestForm.view_requests;
import com.github.ybq.android.spinkit.SpinKitView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Retrofit;

public class list_shipment extends AppCompatActivity {

    private DBHelper dbHelper;
    private adapter_shipment adapter;
    public boolean isinActionMode=false;
    private final List<Model_shipment> list=new ArrayList<>();
    public ImageView sync, upload;
    private StaticChecks checks;
    private SpinKitView spinKitView;
    private boolean btnFlag=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_collectors);
        intiViews();
        new fetchTask().execute();

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btnFlag)
                    checks.showSnackBar(getString(R.string.wait));
                else {
                    if (adapter.getSelectedItems().size()>0){
                        new uploadTask().execute();
                    }else{
                        SnackBarUtils.ErrorSnack(list_shipment.this,"No Items Selected");
                    }
                }
            }
        });
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
    @Override
    public void onBackPressed() {
        if (isinActionMode) {
            isinActionMode = false;
            adapter.notifyDataSetChanged();
        }else {
            Intent i=new Intent(this,Home.class);
            i.putExtra("position",4);
            i.putExtra("title",getString(R.string.shipment));
            startActivity(i);
        }
    }
    private void intiViews() {
        checks=new StaticChecks(this);
        dbHelper=new DBHelper(this);
        spinKitView=findViewById(R.id.spin_kit);
        upload =findViewById(R.id.upload);
        TextView title = findViewById(R.id.titlebar_title);
        title.setText(R.string.shipmentList);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        sync=findViewById(R.id.synchronise);
        adapter=new adapter_shipment(list,list_shipment.this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        list_shipment.this.setFinishOnTouchOutside(true);
        sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload.setVisibility(View.VISIBLE);
                sync.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload.setVisibility(View.GONE);
                sync.setVisibility(View.VISIBLE);
                adapter.notifyDataSetChanged();
            }
        });
    }
    private class fetchTask extends AsyncTask<String,String,String> {
        ProgressDialog pd;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd=new ProgressDialog(list_shipment.this);
            pd.setMessage("Loading...");
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                list.addAll(dbHelper.getAllDataFromShipments());
                return null;
            } catch (Exception e) {
                e.printStackTrace();
                return getString(R.string.somethingwentwrong);
            }
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s!=null)
                SnackBarUtils.ErrorSnack(list_shipment.this,s);
            if (list.size()>0)
                Collections.sort(list,new SortByDate());
            adapter.notifyDataSetChanged();
            pd.dismiss();
        }
    }
    private class uploadTask extends AsyncTask<String,String,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            spinKitView.setVisibility(View.VISIBLE);
            btnFlag=true;
        }

        @Override
        protected String doInBackground(String... strings) {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("application/json");
            Log.i("json173",getRequestBody().toString());
            RequestBody body = RequestBody.create(mediaType,getRequestBody().toString());
            Request request = new Request.Builder()
                    .url(getString(R.string.baseURL)+"NTFPAPI/API/Shipment")
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .build();
            try {
                Response response = client.newCall(request).execute();
                if (setStatus(response.body().string()))
                    return getResources().getString(R.string.synced);
                else
                    return getResources().getString(R.string.somedetailsnotsynced);
            }catch (Exception e) {
                e.printStackTrace();
                return getResources().getString(R.string.servernotresponding);
            }
        }
        @Override
        protected void onPostExecute(String s) {
            btnFlag=false;
            if(s.equals(getResources().getString(R.string.synced)))
                SnackBarUtils.SuccessSnack(list_shipment.this,s);
            else if (s.equals(getResources().getString(R.string.somedetailsnotsynced)))
                SnackBarUtils.WarningSnack(list_shipment.this,s);
            else
                SnackBarUtils.ErrorSnack(list_shipment.this,s);
            findViewById(R.id.spin_kit).setVisibility(View.GONE);
            isinActionMode=false;
            upload.setVisibility(View.GONE);
            sync.setVisibility(View.VISIBLE);
            if (list.size()>0)
                Collections.sort(list,new SortByDate());
            adapter.notifyDataSetChanged();
            super.onPostExecute(s);
        }
    }
    private JSONArray getRequestBody(){
        JSONArray jsonArray = new JSONArray();
        for (Model_shipment model:adapter.getSelectedItems()){
            if (model.isSelected()&&model.getSynced()==0){
                Log.i("Line214",model.getTransitPass());
                for (String transiID:model.getTransitPass().split(",")) {
                    Log.i("Line216",transiID);

                    String startTime = model.getDate();
                    Log.i("startTime",startTime+"");
                    SimpleDateFormat input = new SimpleDateFormat("dd-MM-yyyy");
                    Date dateValue = null;
                    try {
                        dateValue = input.parse(startTime);
                        Log.i("dateValue",dateValue+"");
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    SimpleDateFormat output = new SimpleDateFormat("yyyy-MM-dd");
                    Log.i("output",output.format(dateValue)+"");
                    JSONObject jsonObject = new JSONObject();
                    try {
                        VSSUser user = new SharedPref(list_shipment.this).getVSS();
                        jsonObject.put("Random", model.getUid());
                        jsonObject.put("DivisionId",user.getDivisionId());
                        jsonObject.put("RangeId", user.getRangeId());
                        jsonObject.put("VSSId", user.getVid());
                        jsonObject.put("ProcessingCenter", model.getProcessing_center());
                        jsonObject.put("TransitPass", transiID.replace(" ",""));
                        jsonObject.put("Transportation", model.getVehicleType());
                        jsonObject.put("VehicleNo", model.getVehicleNo());
                        jsonObject.put("DateTime", output.format(dateValue));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    jsonArray.put(jsonObject);
                    Log.i("shipList",jsonArray.toString());
                }
            }
        }
        return jsonArray;
    }

    private boolean setStatus(String json) throws JSONException {
        boolean flag=true;
        Log.i("kishore",json);
        JSONArray jsonArray=new JSONArray(json);
        for (int i=0;i<jsonArray.length();i++){
            JSONObject details=jsonArray.getJSONObject(i);
            Model_shipment model = dbHelper.getAllDataFromShipmentsWhere(" uid='" + details.getString("Random")+"'").get(0);
            if (details.getString("Status").equals("Success")) {
                model.setSynced(1);
            }else{
                model.setSynced(0);
                flag=false;
            }
            dbHelper.updateData(model);
        }
        list.clear();
        list.addAll(dbHelper.getAllDataFromShipments());
        return flag;
    }
    static class SortByDate implements Comparator<Model_shipment> {
        @Override
        public int compare(Model_shipment a, Model_shipment b) {
            return Integer.compare(a.getSynced(), b.getSynced());
        }
    }
}