package com.gisfy.ntfp.VSS.Payment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gisfy.ntfp.HomePage.Home;
import com.gisfy.ntfp.Login.Models.PaymentsModel;
import com.gisfy.ntfp.Login.Models.VSSUser;
import com.gisfy.ntfp.R;
import com.gisfy.ntfp.SqliteHelper.DBHelper;
import com.gisfy.ntfp.Utils.SharedPref;
import com.gisfy.ntfp.Utils.SnackBarUtils;
import com.gisfy.ntfp.Utils.StaticChecks;
import com.gisfy.ntfp.VSS.Collectors.list_collectors;
import com.gisfy.ntfp.VSS.Inventory.Inventory;
import com.gisfy.ntfp.VSS.RequestForm.list_request;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MakePaymentsActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    public List<Model_payment> list=new ArrayList<>();
    private PaymentsAdapter adapter;
    private DBHelper db;
    private final List<Model_payment> tempList=new ArrayList<>();
    public boolean isinActionMode=false;
    public ImageView sync,upload;
    private boolean btnFlag=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_payments);
        initViews();
        new fetchTask().execute();
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btnFlag)
                    SnackBarUtils.WarningSnack(MakePaymentsActivity.this,getString(R.string.wait));
                else {
                    if (adapter.getSelectedItems().size()>0){
                        new uploadTask().execute();
                    }else{
                        SnackBarUtils.ErrorSnack(MakePaymentsActivity.this,"No Items Selected");
                    }
                }
            }
        });

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
        db=new DBHelper(this);
        StaticChecks checks = new StaticChecks(this);
        TextView tv=findViewById(R.id.titlebar_title);
        sync=findViewById(R.id.synchronise);
        upload =findViewById(R.id.upload);
        tv.setText(getResources().getString(R.string.makepayments));
        tabLayout=(TabLayout)findViewById(R.id.tabLayout);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.pending)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.paid)));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter=new PaymentsAdapter(tempList, MakePaymentsActivity.this, new PaymentsAdapter.OnClickListener() {
            @Override
            public void onClick(Model_payment model) {
                model.setStatus(1);
                model.setSynced(0);
                db.updateData(model);
                new fetchTask().execute();
            }
        });
        recyclerView.setAdapter(adapter);
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
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

    private void updateTab(int position){
        if (position==0){
            upload.setVisibility(View.GONE);
            sync.setVisibility(View.GONE);
        }else {
            upload.setVisibility(View.GONE);
            sync.setVisibility(View.VISIBLE);
        }
        tempList.clear();
        for (Model_payment model:list){
            Log.i("model154",model.getProduct());
            if (model.getStatus()==position)
                tempList.add(model);
        }

        adapter.notifyDataSetChanged();
    }

    private class fetchTask extends AsyncTask<String,String,String>{
        @Override
        protected void onPreExecute() {
            findViewById(R.id.spin_kit).setVisibility(View.VISIBLE);
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... strings) {
            list.clear();
            try {
                list=db.getAllDataFromPayments();
                return "";
            } catch (Exception e) {
                e.printStackTrace();
                return getResources().getString(R.string.somethingwentwrong);
            }
        }
        @Override
        protected void onPostExecute(String s) {
            findViewById(R.id.spin_kit).setVisibility(View.GONE);
            if (!s.equals(""))
                SnackBarUtils.ErrorSnack(MakePaymentsActivity.this,s);
            if (list.size()>0)
                Log.i("list184",list+"");
                Collections.sort(list,new SortByDate());
            updateTab(tabLayout.getSelectedTabPosition());
            super.onPostExecute(s);
        }
    }

    private class uploadTask extends AsyncTask<String,String,String> {
        @Override
        protected void onPreExecute() {
            findViewById(R.id.spin_kit).setVisibility(View.VISIBLE);
            super.onPreExecute();
            btnFlag=true;
        }

        @Override
        protected String doInBackground(String... strings) {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            Log.i("jsonwriter",jsonwriter().toString());
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType,jsonwriter().toString());
            Request request = new Request.Builder()
                    .url(getString(R.string.baseURL)+"NTFPAPI/API/Payment")
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .build();
            try {
                Response response = client.newCall(request).execute();
                if (setStatus(response.body().string()))
                    return getResources().getString(R.string.synced);
                else
                    return getResources().getString(R.string.somedetailsnotsynced);
            } catch (Exception e) {
                e.printStackTrace();
                isinActionMode=false;
                btnFlag=false;
                return getResources().getString(R.string.servernotresponding);
            }
        }

        @Override
        protected void onPostExecute(String s) {
            new fetchTask().execute();
            findViewById(R.id.spin_kit).setVisibility(View.GONE);
            if(s.equals(getResources().getString(R.string.synced)))
                SnackBarUtils.SuccessSnack(MakePaymentsActivity.this,s);
            else if (s.equals(getResources().getString(R.string.somedetailsnotsynced)))
                SnackBarUtils.WarningSnack(MakePaymentsActivity.this,s);
            else
                SnackBarUtils.ErrorSnack(MakePaymentsActivity.this,s);
            isinActionMode=false;
            upload.setVisibility(View.GONE);
            sync.setVisibility(View.VISIBLE);
            btnFlag=false;
            if (list.size()>0)
                Collections.sort(list,new SortByDate());
            super.onPostExecute(s);
        }
    }

    private JSONArray jsonwriter(){
        JSONArray jsonArray=new JSONArray();
        for (Model_payment model:list){
            if (model.isSelected()&&model.getSynced()==0){
                try {
                    VSSUser user = new SharedPref(MakePaymentsActivity.this).getVSS();
                    JSONObject jsonObject=new JSONObject();
                    jsonObject.put("VSSId",user.getVid());
                    jsonObject.put("DivisionId",user.getDivisionId());
                    jsonObject.put("RangeId", user.getRangeId());
                    jsonObject.put("Amount",model.getAmount());
                    jsonObject.put("DateTime",model.getDate());
                    jsonObject.put("ReceivedFrom",model.getSourceName());
                    jsonObject.put("SocietyName",model.getSourceName());
                    jsonObject.put("ThirdParty",model.getSource());
                    jsonObject.put("Random",model.getUid());
                    jsonObject.put("PaymentStatus",model.getStatus());
                    jsonObject.put("PaymentType",model.getPaymentType());
                    jsonObject.put("Collector",model.getCollector());

                    Matcher m = Pattern.compile("\\((.*?)\\)").matcher(model.getProduct());
                    while(m.find()) {
                        jsonObject.put("Product",m.group(1)); }
                    jsonObject.put("QuantityMeasurement",model.getMeasurement());
                    jsonObject.put("Quantity",model.getQuantity());
                    jsonArray.put(jsonObject);
                    Log.i("paymettest",jsonArray.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return jsonArray;
    }
    private boolean setStatus(String json) throws JSONException {
        Log.i("jsonrespo",json);
        boolean flag=true;
        JSONArray jsonArray=new JSONArray(json);
        for (int i=0;i<jsonArray.length();i++){
            JSONObject details=jsonArray.getJSONObject(i);
            Model_payment model = db.getAllDataFromPaymentsWhere(" uid='" + details.getString("Random")+"'").get(0);
            if (details.getString("Status").equals("Success")) {
                model.setSynced(1);
                db.updateData(model);
            }else{
                flag=false;
            }
        }
        return flag;
    }
    @Override
    public void onBackPressed() {
        if (isinActionMode){
            isinActionMode=false;
            sync.setVisibility(View.GONE);
        }else{
            Intent i=new Intent(this,Home.class);
            i.putExtra("position",5);
            i.putExtra("title",getString(R.string.payment));
            startActivity(i);
        }
    }
    static class SortByDate implements Comparator<Model_payment> {
        @Override
        public int compare(Model_payment a, Model_payment b) {
            return Integer.compare(a.getSynced(), b.getSynced());
        }
    }
}

