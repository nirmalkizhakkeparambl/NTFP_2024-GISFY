package com.gisfy.ntfp.VSS.Payment.Recieved;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gisfy.ntfp.API.RetrofitClient;
import com.gisfy.ntfp.HomePage.Home;
import com.gisfy.ntfp.Login.Models.VSSUser;
import com.gisfy.ntfp.R;
import com.gisfy.ntfp.RFO.Models.StatusModel;
import com.gisfy.ntfp.SqliteHelper.DBHelper;
import com.gisfy.ntfp.Utils.SharedPref;
import com.gisfy.ntfp.Utils.SnackBarUtils;
import com.gisfy.ntfp.Utils.StaticChecks;
import com.gisfy.ntfp.VSS.Dashboard.DashboardShipmentModel;
import com.gisfy.ntfp.VSS.Payment.Model_payment;
import com.gisfy.ntfp.VSS.Payment.PaymentsAdapter;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;

public class ReceivedPaymentsActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    public List<ReceivedPaymentsModel> list=new ArrayList<>();
    private ReceivedAdapter adapter;
    private final List<ReceivedPaymentsModel> tempList=new ArrayList<>();
    public boolean isinActionMode=false;
    public ImageView sync,upload;
    private HashMap<String,String> body;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_payments);

        initViews();
        VSSUser user=new SharedPref(this).getVSS();
        body=new HashMap<>();
        body.put("VSSId",user.getVid()+"");
        body.put("RangeId",user.getRangeId()+"");
        body.put("DivisionId",user.getDivisionId()+"");
        body.put("PaymentType","Received");
        getdata(body);
//        list.add(new ReceivedPaymentsModel("Ship76ewh","#PO202107226850",78412,"Thiruvananthapuram","Kulathupuzha","Revanth","Pottmav Processing Centre","Paid","ds4s5d","2021-09-01","SBI","SBIN001244","Thiruvananthapuram Forest Division Office","7800","Pottomavu VSS",true,0,false));
//        list.add(new ReceivedPaymentsModel("Ship76se3","#PO202107226851",78417,"Thiruvananthapuram","Kulathupuzha","Manohar","Pottmav Processing Centre","Paid","ds7832","2021-09-01","Kotak Bank","KTKIB001244","Thiruvananthapuram Forest Division Office","2000","Pottomavu VSS",true,0,false));
//        list.add(new ReceivedPaymentsModel("Ship7aw23","#PO202107226852",78411,"Thiruvananthapuram","Kulathupuzha","Anju Mathew","Pottmav Processing Centre","Paid","ds5ew4","2021-09-01","Axis Bank","AXI011244","Thiruvananthapuram Forest Division Office","3052","Pottomavu VSS",false,0,false));
//        list.add(new ReceivedPaymentsModel("Shipdw343","#PO202107226855",78416,"Thiruvananthapuram","Kulathupuzha","Bala Krishna","Pottmav Processing Centre","Paid","dsw44","2021-09-01","ICICI Bank","ICICI78745","Thiruvananthapuram Forest Division Office","4102","Pottomavu VSS",false,0,false));
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        RecyclerView recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(layoutManager);
        adapter=new ReceivedAdapter(tempList, ReceivedPaymentsActivity.this, new ReceivedAdapter.OnClickListener() {
            @Override
            public void onClick(ReceivedPaymentsModel model) {
                changeStatus(model);
            }
        });
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        updateTab(0);
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

    private void getdata(HashMap<String, String> body) {
        Log.i("skdsk",body.toString());
        findViewById(R.id.spin_kit).setVisibility(View.VISIBLE);
        Call<List<ReceivedPaymentsModel>> call = RetrofitClient.getInstance().getMyApi().paymentsReceived(body);
        call.enqueue(new Callback<List<ReceivedPaymentsModel>>() {
            @Override
            public void onResponse(Call<List<ReceivedPaymentsModel>> call, retrofit2.Response<List<ReceivedPaymentsModel>> response) {
                if (response.isSuccessful()&&response.body().get(0).getPcName()!=null){
                   list=response.body();
                    Collections.sort(list, new Comparator<ReceivedPaymentsModel>() {
                        @Override
                        public int compare(ReceivedPaymentsModel abc1, ReceivedPaymentsModel abc2) {
                            return Boolean.compare(abc1.getPayStatus().equals("Pending"),abc2.getPayStatus().equals("Pending"));
                        }
                    });

                   updateTab(tabLayout.getSelectedTabPosition());
                   adapter.notifyDataSetChanged();
                }
                findViewById(R.id.spin_kit).setVisibility(View.GONE);
            }
            @Override
            public void onFailure(Call<List<ReceivedPaymentsModel>> call, Throwable t) {
                findViewById(R.id.spin_kit).setVisibility(View.GONE);
            }
        });
    }

    private void initViews() {
        TextView tv=findViewById(R.id.titlebar_title);
        sync=findViewById(R.id.synchronise);
        upload =findViewById(R.id.upload);
        tv.setText(getResources().getString(R.string.recievedpayments));
        tabLayout=(TabLayout)findViewById(R.id.tabLayout);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.pending)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.paid)));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    private void updateTab(int position){
        tempList.clear();

        if (position==0){
            for (ReceivedPaymentsModel model:list){
                if (!model.isReceivedByVSS()){
                    tempList.add(model);
                }
            }
        }else{
            for (ReceivedPaymentsModel model:list){
                if (model.isReceivedByVSS()){
                    tempList.add(model);
                }
            }
        }
        upload.setVisibility(View.GONE);
        sync.setVisibility(View.VISIBLE);
        adapter.notifyDataSetChanged();
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

    public void changeStatus(ReceivedPaymentsModel model){
        findViewById(R.id.spin_kit).setVisibility(View.VISIBLE);
        HashMap<String,String> hashMap=new HashMap<>();
        hashMap.put("PayID",model.getPayID()+"");
        Log.i("payid",hashMap.toString());
        Call<List<StatusModel>> call = RetrofitClient.getInstance().getMyApi().VSSPaymentStatus(hashMap);
        call.enqueue(new Callback<List<StatusModel>>() {
            @Override
            public void onResponse(Call<List<StatusModel>> call, retrofit2.Response<List<StatusModel>> response) {
                if (response.isSuccessful()&&response.body().get(0).getStatus()!=null){
                    if (response.body().get(0).getStatus().equals("Success")) {
                        getdata(body);
                    }else {
                        SnackBarUtils.ErrorSnack(ReceivedPaymentsActivity.this,"Failed");
                    }
                }else{
                    SnackBarUtils.ErrorSnack(ReceivedPaymentsActivity.this,"Unable to connect");
                }
                findViewById(R.id.spin_kit).setVisibility(View.GONE);
            }
            @Override
            public void onFailure(Call<List<StatusModel>> call, Throwable t) {
                findViewById(R.id.spin_kit).setVisibility(View.GONE);
                SnackBarUtils.ErrorSnack(ReceivedPaymentsActivity.this,"Unable to connect");
            }
        });
    }
}