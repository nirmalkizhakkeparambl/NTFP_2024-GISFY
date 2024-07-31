package com.gisfy.ntfp.VSS.RequestForm;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.gisfy.ntfp.API.RetrofitClient;
import com.gisfy.ntfp.HomePage.Home;
import com.gisfy.ntfp.HomePage.PCModel;
import com.gisfy.ntfp.Login.Models.VSSUser;
import com.gisfy.ntfp.R;
import com.gisfy.ntfp.SqliteHelper.DBHelper;
import com.gisfy.ntfp.Utils.FCMNotifications;
import com.gisfy.ntfp.Utils.SharedPref;
import com.gisfy.ntfp.Utils.SnackBarUtils;
import com.gisfy.ntfp.VSS.Inventory.adapter_inventory;
import com.gisfy.ntfp.VSS.Inventory.list_inventory;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;

import static com.gisfy.ntfp.Utils.StaticChecks.isDateInRange;

public class list_request extends AppCompatActivity {

    private DBHelper dbHelper;
    private final List<StocksModel> finalList=new ArrayList<>();
    private final List<StocksModel> tempList=new ArrayList<>();
    public ImageView filter;
    private BottomSheetBehavior sheetBehavior;
    private SpinKitView spinKitView;
    private adapter_request adapter;
    private Spinner rfo,prcocessingcenter;
    private SharedPref pref;
    private final List<String> rfos=new ArrayList<>();
    private final List<String> fcms=new ArrayList<>();
    private final List<Integer> pcId=new ArrayList<>();
    private Spinner transitfor;
    private EditText other;
    private VSSUser user;
    HashMap<String,String> listHashMap= new HashMap();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_rquest);
        initViews();
        getData();

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });
        findViewById(R.id.request_transit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rfos.size()>0){
                    if (transitfor.getSelectedItemPosition()>0){
                        new uploadTask().execute();
                    }else{
                        SnackBarUtils.ErrorSnack(list_request.this,getString(R.string.pleaseselecttransitfor));
                    }
                }
                else
                    SnackBarUtils.ErrorSnack(list_request.this,getString(R.string.norfo));
            }
        });
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        transitfor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position==1){
                    findViewById(R.id.processingCenterLayout).setVisibility(View.VISIBLE);
                    findViewById(R.id.otherlayout).setVisibility(View.GONE);
                }else if(position==2){
                    findViewById(R.id.processingCenterLayout).setVisibility(View.GONE);
                    findViewById(R.id.otherlayout).setVisibility(View.VISIBLE);
                    other.setHint(getString(R.string.enterscstcategory));
                }else if(position==3){
                    findViewById(R.id.processingCenterLayout).setVisibility(View.GONE);
                    findViewById(R.id.otherlayout).setVisibility(View.VISIBLE);
                    other.setHint(getString(R.string.other));
                }else{
                    findViewById(R.id.otherlayout).setVisibility(View.GONE);
                    other.setText("");
                    prcocessingcenter.setSelection(0);
                    findViewById(R.id.processingCenterLayout).setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent i=new Intent(this,Home.class);
        i.putExtra("position",3);
        i.putExtra("title",getString(R.string.transitpass));
        startActivity(i);
    }

    private void initViews(){
        dbHelper=new DBHelper(this);
        pref=new SharedPref(this);
        user=pref.getVSS();
        filter=findViewById(R.id.filter);
        other=findViewById(R.id.other);
        transitfor=findViewById(R.id.transitfor);
        rfo=findViewById(R.id.spinner_rfo_transit);
        prcocessingcenter=findViewById(R.id.spinner_pc_transit);
        spinKitView=findViewById(R.id.spin_kit);
        LinearLayout layoutBottomSheet = findViewById(R.id.bottomSheet);
        sheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);
        sheetBehavior.setHideable(true);
        RecyclerView recyclerView = findViewById(R.id.collector_recycle);
        adapter = new adapter_request(tempList, list_request.this);
        recyclerView.setAdapter(adapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        initSpinners();
    }


    private void initSpinners() {
       if (pref.getString("RFOs").contains("&&rfos")) {
           String[] tosplit = pref.getString("RFOs").split("&&rfos");
           for (String s : tosplit) {
               rfos.add(s.split("&&fcms")[0].split("-")[0]);
               fcms.add(s.split("&&fcms")[1]);
               listHashMap.put(s.split("&&fcms")[0].split("-")[0],s.split("&&fcms")[0].split("-")[1]);
           }
       }else{
           rfos.add(getResources().getString(R.string.noresources));
           fcms.add(getResources().getString(R.string.noresources));
       }
       ArrayAdapter<String> rfoAdapter = new ArrayAdapter<String>(this,
               android.R.layout.simple_spinner_dropdown_item, rfos);
       rfo.setAdapter(rfoAdapter);
       List<String> pcs=new ArrayList<>();

       if (dbHelper.getPCs().size()>0&&dbHelper.getPCs().get(0).getPcName()!=null) {
           for (int i=0;i<dbHelper.getPCs().size();i++){
               Log.i("rffffoo",dbHelper.getPCs().get(i).getPcName());
               pcs.add(dbHelper.getPCs().get(i).getPcName());
               pcId.add(dbHelper.getPCs().get(i).getPcId());
           }
       }else{
           pcs.add("No Data Found"); }
       ArrayAdapter<String> pcAdapter = new ArrayAdapter<String>(this,
               android.R.layout.simple_spinner_dropdown_item, pcs);
       prcocessingcenter.setAdapter(pcAdapter); }
    private void getData(){
        finalList.clear();
        tempList.clear();
        findViewById(R.id.spin_kit).setVisibility(View.VISIBLE);
        HashMap<String,String> json=new HashMap<>();
        json.put("DivisionId",user.getDivisionId()+"");
        json.put("RangeId",user.getRangeId()+"");
        json.put("VSSId",user.getVid()+"");
        json.put("ForTransit","Yes");
        Log.i("DEBUGGING REQUEST",json.toString());
        Call<List<StocksModel>> pcs = RetrofitClient.getInstance().getMyApi().stockSelect(json);
        pcs.enqueue(new Callback<List<StocksModel>>() {
            @Override
            public void onResponse(Call<List<StocksModel>> call, retrofit2.Response<List<StocksModel>> response) {
                if (response.isSuccessful()&&response.body().get(0).getnTFPName()!=null) {
                    finalList.addAll(response.body());
                    tempList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                } else {
                    SnackBarUtils.ErrorSnack(list_request.this,getString(R.string.nodata));
                }
                findViewById(R.id.spin_kit).setVisibility(View.GONE);
            }
            @Override
            public void onFailure(Call<List<StocksModel>> call, Throwable t) {
                SnackBarUtils.ErrorSnack(list_request.this,getString(R.string.servernotresponding));
                findViewById(R.id.spin_kit).setVisibility(View.GONE);
            }
        });
    }



    private class uploadTask extends AsyncTask<String,String,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            spinKitView.setVisibility(View.VISIBLE);
        }
        @Override
        protected String doInBackground(String... strings) {
            OkHttpClient client = new OkHttpClient().newBuilder().build();
            MediaType mediaType = MediaType.parse("application/json");
            Log.i("kishore232",getRequestBody().toString());
            RequestBody body = RequestBody.create(mediaType,getRequestBody().toString());
            Request request = new Request.Builder()
                    .url("https://vanasree.com/NTFPAPI/API/Transit")
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
                return getString(R.string.select_ntfp_data);
            }
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s.equals(getResources().getString(R.string.synced)))
                SnackBarUtils.SuccessSnack(list_request.this,s);
            else if (s.equals(getResources().getString(R.string.somedetailsnotsynced)))
                SnackBarUtils.WarningSnack(list_request.this,s);
            else
                SnackBarUtils.ErrorSnack(list_request.this,s);
            adapter.notifyDataSetChanged();
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            findViewById(R.id.spin_kit).setVisibility(View.GONE);
            getData();
        }
    }

    private JSONArray getRequestBody(){
        JSONArray jsonArray = new JSONArray();
        for (StocksModel model:adapter.getSelectedItems()){
                JSONObject jsonObject = new JSONObject();

            String dateget = model.getDateandTime();
            String[] dateParts = dateget.split("-");
            String year = dateParts[0];
            String month = dateParts[1];
            String day = dateParts[2];
            String datanew = day+"-"+ month+"-"+year;
                try {
                    String id=listHashMap.get(rfo.getSelectedItem().toString());
                    jsonObject.put("MemberId",model.getMemberID());
                    jsonObject.put("CollectorId",model.getCollectorID());
                    jsonObject.put("NTFPTypeId",model.getnTFPTypeId());
                    jsonObject.put("NTFPId",model.getnTFPId());
                    jsonObject.put("TransUniqueId",model.getRandom());
                    jsonObject.put("DivisionId",user.getDivisionId()+"");
                    jsonObject.put("RangeId",user.getRangeId()+"");
                    jsonObject.put("VSSId",user.getVid()+"");
                    jsonObject.put("NTFPName",model.getnTFPName());
                    jsonObject.put("Unit",model.getUnit());
                    jsonObject.put("Quantity",model.getQuantity());
                    jsonObject.put("DateTime",datanew);
                    jsonObject.put("Status","");
                    jsonObject.put("RFOId",id);
                    jsonObject.put("RFOName",rfo.getSelectedItem().toString().split("-")[0]);
                    jsonObject.put("StocksId",model.getStocksId());
                    jsonObject.put("TransitFor",transitfor.getSelectedItem().toString());
                    jsonObject.put("NTFPType",model.getnTFPType());
                    if (transitfor.getSelectedItemPosition()==1)
                        jsonObject.put("ProcessingCenter",String.valueOf(pcId.get(prcocessingcenter.getSelectedItemPosition())));
                    else
                        jsonObject.put("ProcessingCenter",other.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                jsonArray.put(jsonObject);
                Log.i("requesttranit306",jsonArray.toString());
            }
        return jsonArray;
    }

    private boolean setStatus(String json) throws JSONException {
        boolean flag=true;
        Log.i("response",json);
        JSONArray array = new JSONArray(json);
        for (int i=0;i<array.length();i++){
            JSONObject details=array.getJSONObject(i);
            if (details.getString("Status").equals("Success")) {
                FCMNotifications.NotifyUser(list_request.this,fcms.get(rfo.getSelectedItemPosition()),"transit request","you got a transit request");
            }else {
                flag=false;
            }
        }
        return flag;
    }
    public void showDialog(){
        final MaterialDatePicker.Builder<Pair<Long, Long>> materialDateBuilder = MaterialDatePicker.Builder.dateRangePicker();
        materialDateBuilder.setTitleText("SELECT A DATE");
        final MaterialDatePicker materialDatePicker = materialDateBuilder.build();
        materialDatePicker.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
        materialDatePicker.addOnPositiveButtonClickListener(
                new MaterialPickerOnPositiveButtonClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onPositiveButtonClick(Object selection) {
                        Pair selectedDates = (Pair) materialDatePicker.getSelection();
                        final Pair<Date, Date> rangeDate = new Pair<>(new Date((Long) selectedDates.first), new Date((Long) selectedDates.second));
                        Date startDate = rangeDate.first;
                        Date endDate = rangeDate.second;
                        tempList.clear();
                        for (StocksModel model:finalList) {
                            try {
                                Date actualDate=new SimpleDateFormat("dd-MM-yyyy").parse(model.getDateandTime());
                                if (isDateInRange(startDate,endDate,actualDate)){
                                    tempList.add(model);
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
    }
}