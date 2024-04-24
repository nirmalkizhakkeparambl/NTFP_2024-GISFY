package com.gisfy.ntfp.VSS.Dashboard;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import com.gisfy.ntfp.API.RetrofitClient;
import com.gisfy.ntfp.Login.Models.RFOUser;
import com.gisfy.ntfp.Login.Models.VSSUser;
import com.gisfy.ntfp.R;
import com.gisfy.ntfp.RFO.Models.DFOACKModel;
import com.gisfy.ntfp.RFO.Models.TransitPassModel;
import com.gisfy.ntfp.RFO.Models.VSSACKModel;
import com.gisfy.ntfp.Utils.BarChartInit;
import com.gisfy.ntfp.Utils.SharedPref;
import com.gisfy.ntfp.Utils.StaticChecks;
import com.gisfy.ntfp.VSS.Inventory.list_inventory;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Dashboard extends AppCompatActivity {

    BarChart transitChart,paymentschart,shipmentschart;
    SharedPref pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_vss);
        initViews();
        final HashMap<String,String> json=new HashMap<>();
        VSSUser user=pref.getVSS();
        json.put("DivisionId",user.getDivisionId()+"");
        json.put("RangeId", user.getRangeId()+"");
        getPurchaseData(json);
        json.put("VSSId", user.getVid()+"");
        json.put("Month","");
        Log.i("dashData",json.toString());
        getShipmentData(json);
        getTransitData(json);
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        final DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        Calendar cal=Calendar.getInstance();
                        cal.set(year,monthOfYear,dayOfMonth);
                        SimpleDateFormat format = new SimpleDateFormat("MMMM");
                        format.setCalendar(cal);
                        HashMap<String,String> json=new HashMap<>();
                        json.put("DivisionId",user.getDivisionId()+"");
                        json.put("RangeId", user.getRangeId()+"");
                        getPurchaseData(json);
                        json.put("VSSId", user.getVid()+"");
                        json.put("Month",format.format(cal.getTime()));
                        getShipmentData(json);
                        getTransitData(json);
                        System.out.println(format.format(cal.getTime()));
                    }
                }, mYear, mMonth, mDay);
        findViewById(R.id.filter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show();
            }
        });
    }

    private void initViews() {

        TextView pctitle,transititle,shipmenttile;
        pctitle=findViewById(R.id.paymentscharttitle);
        transititle=findViewById(R.id.transitcharttitle);
        shipmenttile=findViewById(R.id.shipmentscharttitle);

        pctitle.setText(getResources().getString(R.string.paymentdetails));
        transititle.setText(getResources().getString(R.string.nooftransits));
        shipmenttile.setText(getResources().getString(R.string.shipmentdetails));

        transitChart=findViewById(R.id.transitChart);
        paymentschart=findViewById(R.id.vssChart);
        shipmentschart=findViewById(R.id.dfoChart);
        pref=new SharedPref(this);
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void getShipmentData(HashMap<String,String> json){
        findViewById(R.id.spin_kit).setVisibility(View.VISIBLE);
        Log.i("json",json.toString());
        Call<List<DashboardShipmentModel>> call = RetrofitClient.getInstance().getDashboardApi().ShipmentsData(json);
        call.enqueue(new Callback<List<DashboardShipmentModel>>() {
            @Override
            public void onResponse(Call<List<DashboardShipmentModel>> call, Response<List<DashboardShipmentModel>> response) {
                if (response.isSuccessful()&&response.body().get(0).getMonth()!=null){
                    List<DashboardShipmentModel> vssList;
                    vssList =response.body();
                    ArrayList<BarDataSet> dataSets =  new ArrayList<>();
                    ArrayList<BarEntry> valueSet1 = new ArrayList<>();
                    ArrayList<String> xAxis = new ArrayList<>();
                    BarDataSet barDataSet1 = new BarDataSet(valueSet1, "Purchase");
                    for (int i=0;i<vssList.size();i++){
                        DashboardShipmentModel model=vssList.get(i);
                        valueSet1.add(new BarEntry((float)model.getPurchase(), i));
                        xAxis.add(model.getMonth().replace(" ",""));
                        barDataSet1.setColor(Color.rgb(0, 155, 0));
                        barDataSet1.setBarSpacePercent(80);
                    }
                    dataSets.add(barDataSet1);

                    BarData data = new BarData(xAxis, dataSets);
                    shipmentschart.setData(data);
                    shipmentschart.setDescription("Shipments");
                    shipmentschart.animateXY(2000, 2000);
                    shipmentschart.invalidate();
                }
                findViewById(R.id.spin_kit).setVisibility(View.GONE);
            }
            @Override
            public void onFailure(Call<List<DashboardShipmentModel>> call, Throwable t) {
                findViewById(R.id.spin_kit).setVisibility(View.GONE);
            }
        });
    }

    private void getPurchaseData(HashMap<String,String> json){
        findViewById(R.id.spin_kit).setVisibility(View.VISIBLE);
        Call<List<PaymentsModel>> call = RetrofitClient.getInstance().getDashboardApi().PCPaymentsData(json);
        call.enqueue(new Callback<List<PaymentsModel>>() {
            @Override
            public void onResponse(Call<List<PaymentsModel>> call, Response<List<PaymentsModel>> response) {
                if (response.isSuccessful()&&response.body().get(0).getGroup()!=null){
                    List<PaymentsModel> vssList;
                    vssList =response.body();
                    ArrayList<BarDataSet> dataSets =  new ArrayList<>();
                    ArrayList<BarEntry> valueSet1 = new ArrayList<>();
                    ArrayList<BarEntry> valueSet2 = new ArrayList<>();
                    ArrayList<String> xAxis = new ArrayList<>();
                    for (int i=0;i<vssList.size();i++){
                        PaymentsModel model=vssList.get(i);
                        float p=Float.parseFloat(model.getPurchase());
                        float s=Float.parseFloat(model.getSales());
                        valueSet1.add(new BarEntry((float) p, i));
                        valueSet2.add(new BarEntry((float) s, i));

                        if (model.getGroup()!=null)
                            xAxis.add(model.getGroup().replace(" ",""));
                        else
                            xAxis.add("NA");

                    }
                    BarDataSet barDataSet1 = new BarDataSet(valueSet1, "Purchase");
                    barDataSet1.setColor(Color.rgb(0, 155, 0));
                    BarDataSet barDataSet2 = new BarDataSet(valueSet2, "Sales");
                    barDataSet2.setColor(Color.rgb(155, 0, 0));
                    barDataSet1.setBarSpacePercent(25);
                    barDataSet2.setBarSpacePercent(25);
                    dataSets.add(barDataSet1);
                    dataSets.add(barDataSet2);
                    BarData data = new BarData(xAxis, dataSets);
                    paymentschart.setData(data);
                    data.setValueFormatter(new MyValueFormatter());
                    paymentschart.setDescription("Payments");
                    paymentschart.animateXY(2000, 2000);
                    paymentschart.invalidate();
                }
                findViewById(R.id.spin_kit).setVisibility(View.GONE);
            }
            @Override
            public void onFailure(Call<List<PaymentsModel>> call, Throwable t) {
                findViewById(R.id.spin_kit).setVisibility(View.GONE);
            }
        });
    }

    private void getTransitData(HashMap<String,String> json){
        findViewById(R.id.spin_kit).setVisibility(View.VISIBLE);
        Log.i("DEBUGGING Requestbody",json.toString());
        Call<List<DashboardTransitModel>> call = RetrofitClient.getInstance().getDashboardApi().TransitpassData(json);
        call.enqueue(new Callback<List<DashboardTransitModel>>() {
            @Override
            public void onResponse(Call<List<DashboardTransitModel>> call, Response<List<DashboardTransitModel>> response) {
                if (response.isSuccessful()&&response.body().get(0).getTransitStatus()!=null){
                    List<DashboardTransitModel> vssList;
                    vssList =response.body();
                    ArrayList<BarDataSet> dataSets =  new ArrayList<>();
                    ArrayList<BarEntry> valueSet1 = new ArrayList<>();

                    ArrayList<String> xAxis = new ArrayList<>();
                    for (int i=0;i<vssList.size();i++){
                        DashboardTransitModel model=vssList.get(i);
                        valueSet1.add(new BarEntry((float)model.getTotal(), i));
                        if (model.getTransitStatus().equals(""))
                            xAxis.add("Pending");
                        else
                            xAxis.add(model.getTransitStatus());
                    }
                    BarDataSet barDataSet1 = new BarDataSet(valueSet1, "Transits");
                    barDataSet1.setColor(Color.rgb(0, 155, 0));
                    barDataSet1.setBarSpacePercent(80);
                    dataSets.add(barDataSet1);

                    BarData data = new BarData(xAxis, dataSets);
                    data.setValueFormatter(new MyValueFormatter());
                    transitChart.setData(data);
                    transitChart.setDescription("Transit Data");
                    transitChart.animateXY(2000, 2000);
                    transitChart.invalidate();
                }
                findViewById(R.id.spin_kit).setVisibility(View.GONE);
            }
            @Override
            public void onFailure(Call<List<DashboardTransitModel>> call, Throwable t) {
                findViewById(R.id.spin_kit).setVisibility(View.GONE);
            }
        });
    }
}