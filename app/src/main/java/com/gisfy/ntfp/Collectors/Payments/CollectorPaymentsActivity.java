package com.gisfy.ntfp.Collectors.Payments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.gisfy.ntfp.API.RetrofitClient;
import com.gisfy.ntfp.HomePage.Home;
import com.gisfy.ntfp.Login.LoginActivity;
import com.gisfy.ntfp.Login.Models.CollectorUser;
import com.gisfy.ntfp.R;
import com.gisfy.ntfp.RFO.Models.DFOACKModel;
import com.gisfy.ntfp.SqliteHelper.Entity.CollectorPaymentsModel;
import com.gisfy.ntfp.SqliteHelper.Entity.MemberModel;
import com.gisfy.ntfp.SqliteHelper.SynchroniseDatabase;
import com.gisfy.ntfp.Utils.SharedPref;
import com.gisfy.ntfp.Utils.SnackBarUtils;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CollectorPaymentsActivity extends AppCompatActivity {

    private void initViews() {
        titleText = findViewById(R.id.titlebar_title);
        titleText.setText(getResources().getString(R.string.paymentsList));
        findViewById(R.id.synchronise).setVisibility(View.GONE);
        recyclerView = findViewById(R.id.recyclerView);
        tabLayout=(TabLayout)findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.sucess)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.pending)));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        adapter = new CollectorPaymentAdapter(adapterList,CollectorPaymentsActivity.this);
        recyclerView.setAdapter(adapter);

    }

    private TextView titleText;
    private RecyclerView recyclerView;
    private TabLayout tabLayout;
    private List<CollectorPaymentsModel> list = new ArrayList<>();
    private List<CollectorPaymentsModel> adapterList = new ArrayList<>();
    private CollectorPaymentAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transit);
        findViewById(R.id.backpayment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        initViews();
        fetchCollectorData();
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

    private void updateTab(int position){
        adapterList.clear();
        if (position==0){
            for (CollectorPaymentsModel model:list){
                if (model.getPayStatus().equals("Paid")){
                    adapterList.add(model);
                }
            }
        }else{
            for (CollectorPaymentsModel model:list){
                if (!model.getPayStatus().equals("Paid")){
                    adapterList.add(model);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    public void fetchCollectorData(){
        CollectorUser user = new SharedPref(this).getCollector();
        HashMap<String,String> json = new HashMap<>();
        json.put("Cid", String.valueOf(user.getCid()));
        Log.i("Line 2470",json.toString());
        findViewById(R.id.spin_kit).setVisibility(View.VISIBLE);
        Call<List<CollectorPaymentsModel>> call = RetrofitClient.getInstance().getMyApi().getCollectorPayments(json);
        call.enqueue(new Callback<List<CollectorPaymentsModel>>() {
            @Override
            public void onResponse(Call<List<CollectorPaymentsModel>> call, Response<List<CollectorPaymentsModel>> response) {
                Log.i("Line 2430",response.body().get(0).toString());
                if (response.isSuccessful() && response.body().get(0).getPurchaseOrderNumber()!=null) {
                    list.addAll(response.body());
                    updateTab(tabLayout.getSelectedTabPosition());
                    findViewById(R.id.spin_kit).setVisibility(View.GONE);
                } else {
                    findViewById(R.id.spin_kit).setVisibility(View.GONE);
                    SnackBarUtils.ErrorSnack(CollectorPaymentsActivity.this, getResources().getString(R.string.unabletofetch));
                }
            }
            @Override
            public void onFailure(Call<List<CollectorPaymentsModel>> call, Throwable t) {
                findViewById(R.id.spin_kit).setVisibility(View.GONE);
                SnackBarUtils.ErrorSnack(CollectorPaymentsActivity.this, getResources().getString(R.string.servernotresponding));
            }
        });
    }
}
