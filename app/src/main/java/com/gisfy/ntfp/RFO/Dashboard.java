package com.gisfy.ntfp.RFO;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.gisfy.ntfp.API.RetrofitClient;
import com.gisfy.ntfp.Login.Models.RFOUser;
import com.gisfy.ntfp.R;
import com.gisfy.ntfp.RFO.Models.DFOACKModel;
import com.gisfy.ntfp.RFO.Models.TransitPassModel;
import com.gisfy.ntfp.RFO.Models.VSSACKModel;
import com.gisfy.ntfp.Utils.BarChartInit;
import com.gisfy.ntfp.Utils.SharedPref;
import com.gisfy.ntfp.VSS.Dashboard.DashboardTransitModel;
import com.gisfy.ntfp.VSS.Dashboard.PaymentsModel;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Dashboard extends AppCompatActivity {

    BarChart transitChart,vssChart,dfoChart;
    SharedPref pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rfo_dashboard);
        initViews();
        getDFOData();
        getVSSData();
        getTransitData();
        findViewById(R.id.filter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                datePickerDialog.show();
            }
        });

    }

    private void initViews() {
        transitChart=findViewById(R.id.transitChart);
        vssChart=findViewById(R.id.vssChart);
        dfoChart=findViewById(R.id.dfoChart);
        pref=new SharedPref(this);
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void getDFOData(){
        findViewById(R.id.spin_kit).setVisibility(View.VISIBLE);
        RFOUser user=pref.getRFO();
        final HashMap<String,String> json=new HashMap<>();
        json.put("DivisionId",user.getDivisionId()+"");
        json.put("RangeId", user.getRangeId()+"");
        Log.i("json",json.toString());
        Call<List<DFOACKModel>> call = RetrofitClient.getInstance().getMyApi().getDFOACK(json);
        call.enqueue(new Callback<List<DFOACKModel>>() {
            @Override
            public void onResponse(Call<List<DFOACKModel>> call, Response<List<DFOACKModel>> response) {
                if (response.isSuccessful()){
                    List<DFOACKModel> vssList;
                    vssList =response.body();
                    ArrayList<BarDataSet> dataSets =  new ArrayList<>();
                    ArrayList<BarEntry> valueSet1 = new ArrayList<>();
                    ArrayList<String> xAxis = new ArrayList<>();
                    int accepted = 0,rejected=0,pending = 0;
                    for (DFOACKModel model:vssList){
                        if (model.getStatusByRFO() != null) {
                            if (model.getStatusByRFO().equals("Approved"))
                                accepted++;
                            if (model.getStatusByRFO().equals("Rejected"))
                                rejected++;
                            if (!model.getStatusByRFO().equals("Approved")&&!model.getStatusByRFO().equals("Rejected"))
                                pending++;
                        }
                    }
                    valueSet1.add(new BarEntry(accepted,0));
                    valueSet1.add(new BarEntry(rejected,1));
                    valueSet1.add(new BarEntry(pending,2));
                    xAxis.add("Approved");
                    xAxis.add("Rejected");
                    xAxis.add("Pending");
                    BarDataSet barDataSet1 = new BarDataSet(valueSet1, "PC To DFO");
                    barDataSet1.setColor(Color.rgb(0, 155, 0));
                    barDataSet1.setBarSpacePercent(80);
                    dataSets.add(barDataSet1);

                    BarData data = new BarData(xAxis, dataSets);
                    dfoChart.setData(data);
                    dfoChart.setDescription("PC To DFO Acknowledgements");
                    dfoChart.animateXY(2000, 2000);
                    dfoChart.invalidate();
                }
                findViewById(R.id.spin_kit).setVisibility(View.GONE);
            }
            @Override
            public void onFailure(Call<List<DFOACKModel>> call, Throwable t) {
                findViewById(R.id.spin_kit).setVisibility(View.GONE);
            }
        });
    }

    private void getVSSData(){
        findViewById(R.id.spin_kit).setVisibility(View.VISIBLE);
        RFOUser user=pref.getRFO();
        final HashMap<String,String> json=new HashMap<>();
        json.put("DivisionId",user.getDivisionId()+"");
        json.put("RangeId", user.getRangeId()+"");
        Call<List<VSSACKModel>> call = RetrofitClient.getInstance().getMyApi().getVSSACK(json);
        call.enqueue(new Callback<List<VSSACKModel>>() {
            @Override
            public void onResponse(Call<List<VSSACKModel>> call, Response<List<VSSACKModel>> response) {
                if (response.isSuccessful()){
                    List<VSSACKModel> vssList;
                    vssList =response.body();
                    ArrayList<BarDataSet> dataSets =  new ArrayList<>();
                    ArrayList<BarEntry> valueSet1 = new ArrayList<>();
                    ArrayList<String> xAxis = new ArrayList<>();
                    int accepted = 0,rejected=0,pending = 0;
                    for (VSSACKModel model:vssList){
                        if (model.getStatusByRFO() != null) {
                            if (model.getStatusByRFO().equals("Approved")) {
                                accepted++;
                                Log.i(model.getStatusByRFO(),accepted+"");
                            }
                            if (model.getStatusByRFO().equals("Rejected"))
                                rejected++;
                            if (!model.getStatusByRFO().equals("Approved")&&!model.getStatusByRFO().equals("Rejected"))
                                pending++;
                        }
                    }
                    valueSet1.add(new BarEntry(accepted,0));
                    valueSet1.add(new BarEntry(rejected,1));
                    valueSet1.add(new BarEntry(pending,2));
                    xAxis.add("Approved");
                    xAxis.add("Rejected");
                    xAxis.add("Pending");
                    BarDataSet barDataSet1 = new BarDataSet(valueSet1, "PC To VSS");
                    barDataSet1.setColor(Color.rgb(0, 155, 0));
                    barDataSet1.setBarSpacePercent(80);
                    dataSets.add(barDataSet1);

                    BarData data = new BarData(xAxis, dataSets);
                    vssChart.setData(data);

                    vssChart.setDescription("PC To VSS Acknowledgements");
                    vssChart.animateXY(2000, 2000);
                    vssChart.invalidate();
                }
                findViewById(R.id.spin_kit).setVisibility(View.GONE);
            }
            @Override
            public void onFailure(Call<List<VSSACKModel>> call, Throwable t) {
                findViewById(R.id.spin_kit).setVisibility(View.GONE);
            }
        });
    }

    private void getTransitData(){
        findViewById(R.id.spin_kit).setVisibility(View.VISIBLE);
        RFOUser user=pref.getRFO();
        final HashMap<String,String> json=new HashMap<>();
        json.put("DivisionId",user.getDivisionId()+"");
        json.put("RangeId", user.getRangeId()+"");
        Call<List<TransitPassModel>> call = RetrofitClient.getInstance().getMyApi().getTransitPass(json);
        call.enqueue(new Callback<List<TransitPassModel>>() {
            @Override
            public void onResponse(Call<List<TransitPassModel>> call, Response<List<TransitPassModel>> response) {
                if (response.isSuccessful()){
                    List<TransitPassModel> vssList;
                    vssList =response.body();
                    ArrayList<BarDataSet> dataSets =  new ArrayList<>();
                    ArrayList<BarEntry> valueSet1 = new ArrayList<>();
                    ArrayList<String> xAxis = new ArrayList<>();
                    int accepted = 0,rejected=0,pending = 0;
                    for (TransitPassModel model:vssList){
                        if (model.getTransitStatus() != null) {
                            if (model.getTransitStatus().equals("Accepted"))
                                accepted++;
                            if (model.getTransitStatus().equals("Rejected"))
                                rejected++;
                            if (!model.getTransitStatus().equals("Accepted")&&!model.getTransitStatus().equals("Rejected")) {
                                ++pending;
                            }
                        }
                    }
                    valueSet1.add(new BarEntry(accepted,0));
                    valueSet1.add(new BarEntry(rejected,1));
                    valueSet1.add(new BarEntry(pending,2));
                    xAxis.add("Approved");
                    xAxis.add("Rejected");
                    xAxis.add("Pending");
                    BarDataSet barDataSet1 = new BarDataSet(valueSet1, "Transits");
                    barDataSet1.setColor(Color.rgb(0, 155, 0));
                    barDataSet1.setBarSpacePercent(80);
                    dataSets.add(barDataSet1);

                    BarData data = new BarData(xAxis, dataSets);
                    transitChart.setData(data);
                    transitChart.setDescription("Transits Requests");
                    transitChart.animateXY(2000, 2000);
                    transitChart.invalidate();
                }
                findViewById(R.id.spin_kit).setVisibility(View.GONE);
            }
            @Override
            public void onFailure(Call<List<TransitPassModel>> call, Throwable t) {
                findViewById(R.id.spin_kit).setVisibility(View.GONE);
            }
        });
    }
}