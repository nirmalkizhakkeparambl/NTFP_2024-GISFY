package com.gisfy.ntfp.Collectors;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gisfy.ntfp.API.RetrofitClient;
import com.gisfy.ntfp.HomePage.Home;
import com.gisfy.ntfp.Login.Models.CollectorUser;
import com.gisfy.ntfp.Login.Models.RFOUser;
import com.gisfy.ntfp.R;
import com.gisfy.ntfp.SqliteHelper.DBHelper;
import com.gisfy.ntfp.Utils.FCMNotifications;
import com.gisfy.ntfp.Utils.SharedPref;
import com.gisfy.ntfp.Utils.SnackBarUtils;
import com.gisfy.ntfp.Utils.StaticChecks;
import com.gisfy.ntfp.VSS.Collectors.list_collectors;
import com.gisfy.ntfp.VSS.RequestForm.ChartAlert;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StockRequest extends AppCompatActivity {

    private TabLayout tabLayout;
    private SharedPref pref;
    public List<CollectorStockModel> list=new ArrayList<>();
    private StockRequestAdapter adapter;
    public TableLayout tableLayout;
    public TextInputEditText remarks;
    private RecyclerView recyclerView;
    private CollectorUser user;
    private boolean btnFlag=false;
    final HashMap<String,Integer> json=new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transit);
        initViews();
        adapter=new StockRequestAdapter(list, StockRequest.this, new StockRequestAdapter.PositionClickListener() {
            @Override
            public void itemClicked(CollectorStockModel data) {
                String[] titles = getResources().getStringArray(R.array.transit_titles3);
                List<List<String>> rows = new ArrayList<>();
                List<String> row = new ArrayList<>();
                row.add(data.getnTFP());
                row.add(data.getQuantity()+data.getUnit());
                String memberName = "Nil";
                if (data.getmName()!=null)
                    memberName = data.getmName();
                row.add(memberName);
                rows.add(row);
                new ChartAlert(StockRequest.this,titles,rows).show();
            }

        });
        recyclerView.setAdapter(adapter);
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
        tv.setText(getResources().getString(R.string.stockrequest));
        pref=new SharedPref(this);
        tabLayout=(TabLayout)findViewById(R.id.tabLayout);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.pending)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.ack)));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        findViewById(R.id.backpayment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        user=pref.getCollector();
        json.put("CollectorID",user.getCid());
        ImageView filter=findViewById(R.id.filter);
        filter.setImageResource(R.drawable.vector_sync);
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new uploadTask().execute();
                //Intent i = new Intent(StockRequest.this, Home.class);
                //startActivity(i);
            }
        });
    }


    private void getData(HashMap<String,Integer> json){
        Log.i("json126",json.toString());
        list.clear();
        findViewById(R.id.spin_kit).setVisibility(View.VISIBLE);
        Call<List<CollectorStockModel>> call = RetrofitClient.getInstance().getMyApi().getCollectorStocksfromcollector(json);
        call.enqueue(new Callback<List<CollectorStockModel>>() {
            @Override
            public void onResponse(Call<List<CollectorStockModel>> call, Response<List<CollectorStockModel>> response) {
                if (response.isSuccessful()){
                    Log.i("response138",response.body().toString()+"");
                    list=response.body();
                    try {
                        updateTab(tabLayout.getSelectedTabPosition());
                    } catch (Exception e) {
                        e.printStackTrace();
                        SnackBarUtils.ErrorSnack(StockRequest.this,getString(R.string.nodata));
                    }
                }else{
                    SnackBarUtils.ErrorSnack(StockRequest.this,getString(R.string.unabletofetch));
                }
                findViewById(R.id.spin_kit).setVisibility(View.GONE);
            }
            @Override
            public void onFailure(Call<List<CollectorStockModel>> call, Throwable t) {
                findViewById(R.id.spin_kit).setVisibility(View.GONE);
                SnackBarUtils.ErrorSnack(StockRequest.this,getString(R.string.servernotresponding));
            }
        });
    }
    private void updateTab(int position){
        if (position==0){
            List<CollectorStockModel> pending=new ArrayList<>();
            for (CollectorStockModel model:list){
                if (!model.getRequestStatus().equals("true")){
                    pending.add(model);
                }
            }
            adapter.updateData(pending);
        }else{
            List<CollectorStockModel> other=new ArrayList<>();
            for (CollectorStockModel model:list){
                if (model.getRequestStatus().equals("true")){
                    other.add(model);
                    Log.i("afgsd",model.getRequestStatus());

                }
            }
            adapter.updateData(other);
        }
        adapter.notifyDataSetChanged();
    }
    private class uploadTask extends AsyncTask<String,String,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            findViewById(R.id.spin_kit).setVisibility(View.VISIBLE);
            btnFlag=true;
        }
        @Override
        protected String doInBackground(String... strings) {
            Log.i("getJsonArray192",getJsonArray().toString());
            OkHttpClient client = new OkHttpClient().newBuilder().build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, getJsonArray().toString());
            Request request = new Request.Builder()
                    .url("http://vanasree.com//NTFPAPI/API/CollectorStockRequestToVSS")
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .build();
            try {
                okhttp3.Response response = client.newCall(request).execute();
                if (setStatus(response.body().string()))
                    return getResources().getString(R.string.synced);
                else
                    return getResources().getString(R.string.somedetailsnotsynced);
            } catch (Exception e) {
                e.printStackTrace();
                btnFlag=false;
                return e.getClass().getSimpleName();
            }
        }
        @Override
        protected void onPostExecute(String s) {
            Intent i = new Intent(StockRequest.this, Home.class);

            startActivity(i);

            super.onPostExecute(s);
            if (s.equals(getResources().getString(R.string.synced)))
                Toast.makeText(getApplicationContext()
                                ,getString(R.string.synced),
                                Toast.LENGTH_SHORT)
                        .show();

            else if(s.equals(getResources().getString(R.string.somedetailsnotsynced)))
                SnackBarUtils.WarningSnack(StockRequest.this,getString(R.string.somedetailsnotsynced));
            else if (s.equals("JSONException")||s.equals("SQLiteException"))
                SnackBarUtils.ErrorSnack(StockRequest.this,getString(R.string.somethingwentwrong));
            else
                SnackBarUtils.ErrorSnack(StockRequest.this,getString(R.string.servernotresponding));
            findViewById(R.id.spin_kit).setVisibility(View.GONE);
            adapter.notifyDataSetChanged();

            getData(json);
        }
    }

    private JSONArray getJsonArray(){
        JSONArray jsonArray=new JSONArray();
        try {
            for (CollectorStockModel model:adapter.getSelectedItems()){
                JSONObject jsonObject=new JSONObject();
                jsonObject.put("InventID",model.getInventID());
                jsonArray.put(jsonObject);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonArray;
    }
    private boolean setStatus(String json) throws JSONException {
        Log.i("ksdks",getJsonArray()+"///"+json);
        boolean flag=true;
        JSONArray jsonArray=new JSONArray(json);
        for (int i=0;i<jsonArray.length();i++){
            JSONObject details=jsonArray.getJSONObject(i);
            if (details.getString("Status").equals("Success")) {
                FCMNotifications.NotifyUser(StockRequest.this,user.getVssfcmid(),"You got a Stock Request","Inventory ID: "+details.getString("InventID")+" From: "+user.getCollectorName());
            }else{
                flag=false;
            }
        }
        return flag;
    }
}