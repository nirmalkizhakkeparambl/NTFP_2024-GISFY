package com.gisfy.ntfp.VSS.Inventory;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gisfy.ntfp.Collectors.CollectorStockModel;
import com.gisfy.ntfp.HomePage.Home;
import com.gisfy.ntfp.Login.Models.VSSUser;
import com.gisfy.ntfp.R;
import com.gisfy.ntfp.SqliteHelper.DBHelper;
import com.gisfy.ntfp.SqliteHelper.Entity.CollectorsModel;
import com.gisfy.ntfp.SqliteHelper.Entity.InventoryRelation;
import com.gisfy.ntfp.SqliteHelper.Entity.MemberModel;
import com.gisfy.ntfp.SqliteHelper.NtfpDao;
import com.gisfy.ntfp.SqliteHelper.SynchroniseDatabase;
import com.gisfy.ntfp.Utils.SharedPref;
import com.gisfy.ntfp.Utils.SnackBarUtils;
import com.gisfy.ntfp.Utils.StaticChecks;
import com.gisfy.ntfp.VSS.Collectors.Collector;
import com.gisfy.ntfp.VSS.Collectors.list_collectors;
import com.gisfy.ntfp.VSS.Payment.MakePaymentsActivity;
import com.gisfy.ntfp.VSS.Payment.Model_payment;
import com.gisfy.ntfp.VSS.RequestForm.StocksModel;
import com.gisfy.ntfp.VSS.Shipment.Model_shipment;
import com.github.ybq.android.spinkit.SpinKitView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
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

import static com.gisfy.ntfp.Utils.StaticChecks.stringtoDate;

public class list_inventory  extends AppCompatActivity {

    private RecyclerView recyclerView;
    private adapter_inventory adapter;
    public boolean isinActionMode=false;
    private List<InventoryRelation> list=new ArrayList<>();
    public ImageView sync,upload;
    private SpinKitView spinKitView;
    private boolean btnFlag=false;
    private NtfpDao dao;
    private DBHelper db;
    private List<InventoryRelation> shallowCopy =new ArrayList<>();
    private CollectorsModel collectorModel = null;
    private Inventory modelll;
    private MemberModel modell;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_collectors);
        intiViews();

        dao = SynchroniseDatabase.getInstance(this).ntfpDao();
        new fetchTask().execute();
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        db=new DBHelper(this);
        final DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        new StaticChecks(list_inventory.this).showSnackBar("Filtered...");
                    }
                }, mYear, mMonth, mDay);

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btnFlag)
                    SnackBarUtils.WarningSnack(list_inventory.this,getString(R.string.wait));
                else {
                    if (adapter.getSelectedItems().size()>0){

                        new uploadTaskPayment().execute();
                        new uploadTask().execute();

                        Intent i =new Intent(list_inventory.this,Home.class);
                        startActivity(i);
                    }else{
                        SnackBarUtils.ErrorSnack(list_inventory.this,"No Items Selected");
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
            i.putExtra("position",2);
            i.putExtra("title",getString(R.string.inventory));
            startActivity(i);
        }
    }
    private void intiViews() {
        this.modelll = modelll;
        TextView title = findViewById(R.id.titlebar_title);
        spinKitView=findViewById(R.id.spin_kit);
        title.setText(R.string.listinv);
        recyclerView=findViewById(R.id.recyclerView);
        sync=findViewById(R.id.synchronise);
        upload=findViewById(R.id.upload);
        list_inventory.this.setFinishOnTouchOutside(true);
        title.setText(getString(R.string.listinv));
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
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            spinKitView.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                list.addAll(dao.getAllInventories());


                if (list.size()>0){

                    shallowCopy = list.subList(0, list.size());

                    Collections.reverse(shallowCopy);
                    Log.i("ListSize228",shallowCopy.size()+"");

                }

                Log.i("ListSize220",list.size()+"");

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            adapter=new adapter_inventory(shallowCopy,list_inventory.this);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(layoutManager);
            if (shallowCopy.size()>0)
                Collections.sort(shallowCopy,new  list_inventory.SortByDate());
            recyclerView.setAdapter(adapter);
            if (s!=null)
                SnackBarUtils.ErrorSnack(list_inventory.this,s);
            spinKitView.setVisibility(View.GONE);
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
            new uploadTaskPayment().execute();
            OkHttpClient client = new OkHttpClient().newBuilder().build();
            MediaType mediaType = MediaType.parse("application/json");
            Log.i("jsonData188",jsonwriter()+"");
            RequestBody body = RequestBody.create(mediaType,jsonwriter());
            Request request = new Request.Builder()
                    .url("http://vanasree.com/NTFPAPI/API/Stocks")
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .build();


            try {
                Response response = client.newCall(request).execute();
                if (setStatus(response.body().string().trim()))
                    return response.message();
                else
                    return getResources().getString(R.string.somedetailsnotsynced);
            }
            catch (Exception e) {
                e.printStackTrace();
                btnFlag=false;
                return e.getClass().getSimpleName();
            }
        }

        @Override
        protected void onPostExecute(String s) {
            btnFlag=false;
            if (s.equals("OK"))
                Toast.makeText(getApplicationContext()
                                ,"",
                                Toast.LENGTH_SHORT)
                        .show();

            else if(s.equals(getResources().getString(R.string.somedetailsnotsynced)))
                SnackBarUtils.WarningSnack(list_inventory.this,s);
            else
                SnackBarUtils.ErrorSnack(list_inventory.this,getResources().getString(R.string.servernotresponding));
            findViewById(R.id.spin_kit).setVisibility(View.GONE);
            isinActionMode=false;
            upload.setVisibility(View.GONE);
            sync.setVisibility(View.VISIBLE);
            if (shallowCopy.size()>0)
                Collections.sort(shallowCopy,new list_inventory.SortByDate());
            adapter.notifyDataSetChanged();
            super.onPostExecute(s);
        }

    }
    private class uploadTaskPayment extends AsyncTask<String,String,String>{

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
            MediaType mediaTypep = MediaType.parse("application/json");
            Log.i("jsonData188PY",jsonwriterp()+"");
            RequestBody bodyp = RequestBody.create(mediaTypep,jsonwriterp().toString());
            Request request = new Request.Builder()
                    .url("http://vanasree.com/NTFPAPI/API/Payment")
                    .method("POST", bodyp)
                    .addHeader("Content-Type", "application/json")
                    .build();


            try {
                Response response = client.newCall(request).execute();
                if (setStatusp(response.body().string().trim()))
                    return response.message();
                else
                    return getResources().getString(R.string.somedetailsnotsynced);
            }
            catch (Exception e) {
                e.printStackTrace();
                btnFlag=false;
                return e.getClass().getSimpleName();
            }
        }

        @Override
        protected void onPostExecute(String s) {
            btnFlag=false;
            if (s.equals("OK"))
                Toast.makeText(getApplicationContext()
                                ,"",
                                Toast.LENGTH_SHORT)
                        .show();

            else if(s.equals(getResources().getString(R.string.somedetailsnotsynced)))
                SnackBarUtils.WarningSnack(list_inventory.this,s);
            else
                SnackBarUtils.ErrorSnack(list_inventory.this,getResources().getString(R.string.servernotresponding));
            findViewById(R.id.spin_kit).setVisibility(View.GONE);
            isinActionMode=false;



            super.onPostExecute(s);
        }

    }



    private String jsonwriter(){
        List<String> newobjects=new ArrayList<>();
        VSSUser user=new SharedPref(this).getVSS();


        for (InventoryRelation model:adapter.getSelectedItems()){

            if(model.getInventory().getVssname().equals(user.getvSSName())){
                if (model.isSelected()&&!model.getInventory().isSynced()){
                    int CIID;
                    if(model.getCollector()!=null) {
                        CIID = model.getCollector().getCid();
                        Log.i("CIID1", CIID + "");
                    }else{ CIID = 0;}

                    int memberId;
                    if (model.getMember()!=null) {

                        memberId = model.getMember().getMemberId();}

                    else {


                        Log.i("111111", "");
                        memberId= -1;

                    }


                    newobjects.add("{\n" +
                            "    \"Random\":\""+model.getInventory().getInventoryId()+"\",\n" +
                            "    \"DivisionId\":\""+user.getDivisionId()+"\",\n" +
                            "    \"RangeId\":\""+user.getRangeId()+"\",\n" +
                            "    \"VSSId\":\""+user.getVid()+"\",\n" +
                            "    \"FromVSSId\":\""+model.getInventory().getVssnamesle()+"\",\n" +
                            "    \"NTFPName\":\""+model.getNtfp().getNTFPscientificname()+"\",\n" +
                            "    \"NTFPId\":\""+model.getNtfp().getNid()+"\",\n" +
                            "    \"MemberId\":\""+memberId+"\",\n" +
                            "    \"CollectorId\":\""+CIID+"\",\n" +
                            "    \"NTFPTypeId\":\""+model.getItemType().getItemId()+"\",\n" +
                            "    \"Unit\":\""+model.getInventory().getMeasurements()+"\",\n" +
                            "    \"Quantity\":\""+model.getInventory().getQuantity()+"\",\n" +
                            "    \"NTFPType\":\""+model.getItemType().getMycase()+"\",\n" +
                            "    \"Collector\":\""+model.getInventory().getColectname()+"\",\n" +
                            "    \"Amount\":\""+model.getInventory().getPrice()+"\",\n" +
                            "    \"DateandTime\":\""+model.getInventory().getDate()+"\",\n" +
                            "    \"Loss\":\""+model.getInventory().getLoseAmound()+"\"\n" +
                            "}");
                }
            }else {
                if (model.isSelected()&&!model.getInventory().isSynced()){
                    int memberId;
                    if (model.getMember()!=null) memberId = model.getMember().getMemberId();
                    else memberId = -1;
                    Log.i("2222222","");

                    newobjects.add("{\n" +
                            "    \"Random\":\""+model.getInventory().getInventoryId()+"\",\n" +
                            "    \"DivisionId\":\""+user.getDivisionId()+"\",\n" +
                            "    \"RangeId\":\""+user.getRangeId()+"\",\n" +
                            "    \"VSSId\":\""+user.getVid()+"\",\n" +
                            "    \"FromVSSId\":\""+model.getInventory().getVssnamesle()+"\",\n" +
                            "    \"NTFPName\":\""+model.getNtfp().getNTFPscientificname()+"\",\n" +
                            "    \"NTFPId\":\""+model.getNtfp().getNid()+"\",\n" +
                            "    \"MemberId\":\""+memberId+"\",\n" +
                            "    \"CollectorId\":\""+model.getInventory().getCollectorId()+"\",\n" +
                            "    \"NTFPTypeId\":\""+model.getItemType().getItemId()+"\",\n" +
                            "    \"Unit\":\""+model.getInventory().getMeasurements()+"\",\n" +
                            "    \"Quantity\":\""+model.getInventory().getQuantity()+"\",\n" +
                            "    \"NTFPType\":\""+model.getItemType().getMycase()+"\",\n" +
                            "    \"Collector\":\""+model.getInventory().getColectname()+"\",\n" +
                            "    \"Amount\":\""+model.getInventory().getPrice()+"\",\n" +
                            "    \"DateandTime\":\""+model.getInventory().getDate()+"\",\n" +
                            "    \"Loss\":\""+model.getInventory().getLoseAmound()+"\"\n" +
                            "}");

                }


            }

        }
        StringBuilder sb=new StringBuilder("[");
        if (newobjects.size()>0){
            for (int i=0;i<newobjects.size();i++){
                if (i==newobjects.size()-1){
                    sb.append(newobjects.get(i));
                }else {
                    sb.append(newobjects.get(i)).append(",");
                }
            }
            sb.append("]");
        }
        return sb.toString();
    }


    private boolean setStatus(String json) throws JSONException {
        boolean flag=true;
        Log.i("kishore",json);
        JSONArray jsonArray=new JSONArray(json);
        for (int i=0;i<jsonArray.length();i++){
            JSONObject details=jsonArray.getJSONObject(i);
            if (details.getString("Status").equals("Success")) {
                dao.setSyncStatus(true,details.getString("Random"));
            }else{
                flag=false;
                dao.setSyncStatus(false,details.getString("Random"));
            }
        }
        shallowCopy.clear();
        shallowCopy.addAll(dao.getAllInventories());
        return flag;
    }
    private boolean setStatusp(String json) throws JSONException {
        boolean flag=true;
        Log.i("kishorep",json);
        JSONArray jsonArray=new JSONArray(json);
        for (int i=0;i<jsonArray.length();i++){
            JSONObject details=jsonArray.getJSONObject(i);
            if (details.getString("Status").equals("Success")) {

           return flag;
            }
        }

        return flag;
    }

    //    private String jsonwriterp(){
//        List<String> newobjectsp=new ArrayList<>();
//        VSSUser user=new SharedPref(this).getVSS();
//
//
//        for (InventoryRelation model:adapter.getSelectedItems()){
//
//            if(model.getInventory().getVssname().equals(user.getvSSName())){
//                if (model.isSelected()&&!model.getInventory().isSynced()){
//                    int CIID;
//                    if(model.getCollector()!=null) {
//                        CIID = model.getCollector().getCid();
//                        Log.i("CIID1", CIID + "");
//                    }else{ CIID = 0;}
//
//                    int memberId;
//                    if (model.getMember()!=null) {
//
//                        memberId = model.getMember().getMemberId();}
//
//                    else {
//
//
//                        Log.i("111111", "");
//                        memberId= -1;
//
//                    }
//
//
//                    newobjectsp.add("{\n" +
//                            "    \"VSSId\":\""+user.getVid()+"\",\n" +
//                            "    \"DivisionId\":\""+user.getDivisionId()+"\",\n" +
//                            "    \"RangeId\":\""+user.getRangeId()+"\",\n" +
//                            "    \"Amount\":\""+model.getInventory().getPrice()+"\",\n" +
//                            "    \"DateandTime\":\""+model.getInventory().getDate()+"\",\n" +
//                            "    \"ReceivedFrom\":\""+model.getInventory().getDate()+"\",\n" +
//                            "    \"SocietyName\":\""+model.getInventory().getDate()+"\",\n" +
//                            "    \"ThirdParty\":\""+model.getInventory().getDate()+"\",\n" +
//                            "    \"Random\":\""+model.getInventory().getInventoryId()+"\",\n" +
//                            "    \"Random\":\""+"pending"+"\",\n" +
//                            "    \"Collector\":\""+model.getInventory().getColectname()+"\",\n" +
//                            "    \"Product\":\""+model.getItemType().getMycase()+"\",\n" +
//                            "    \"QuantityMeasurement\":\""+model.getInventory().getMeasurements()+"\",\n" +
//                            "    \"Quantity\":\""+model.getInventory().getQuantity()+"\",\n" +
//
//                            "}");
//
//                }
//            }else {
//                if (model.isSelected()&&!model.getInventory().isSynced()){
//                    int memberId;
//                    if (model.getMember()!=null) memberId = model.getMember().getMemberId();
//                    else memberId = -1;
//                    Log.i("2222222","");
//
//                    newobjectsp.add("{\n" +
//                            "    \"VSSId\":\""+user.getVid()+"\",\n" +
//                            "    \"DivisionId\":\""+user.getDivisionId()+"\",\n" +
//                            "    \"RangeId\":\""+user.getRangeId()+"\",\n" +
//                            "    \"Amount\":\""+model.getInventory().getPrice()+"\",\n" +
//                            "    \"DateandTime\":\""+model.getInventory().getDate()+"\",\n" +
//                            "    \"ReceivedFrom\":\""+model.getInventory().getDate()+"\",\n" +
//                            "    \"SocietyName\":\""+model.getInventory().getDate()+"\",\n" +
//                            "    \"ThirdParty\":\""+model.getInventory().getDate()+"\",\n" +
//                            "    \"Random\":\""+model.getInventory().getInventoryId()+"\",\n" +
//                            "    \"Random\":\""+"pending"+"\",\n" +
//                            "    \"Collector\":\""+model.getInventory().getColectname()+"\",\n" +
//                            "    \"Product\":\""+model.getItemType().getMycase()+"\",\n" +
//                            "    \"QuantityMeasurement\":\""+model.getInventory().getMeasurements()+"\",\n" +
//                            "    \"Quantity\":\""+model.getInventory().getQuantity()+"\",\n" +
//                            "}");
//                }
//
//            }
//
//        }
//        StringBuilder sb=new StringBuilder();
//        if (newobjectsp.size()>0){
//            for (int i=0;i<newobjectsp.size();i++){
//                if (i==newobjectsp.size()-1){
//                    sb.append(newobjectsp.get(i));
//                }else {
//                    sb.append(newobjectsp.get(i)).append(",");
//                }
//            }
//            sb.append();
//        }
//        return sb.toString();
//    }

    private JSONArray jsonwriterp(){
        JSONArray jsonArray=new JSONArray();
        VSSUser user=new SharedPref(this).getVSS();
        for (InventoryRelation model:adapter.getSelectedItems()){
            if(model.getInventory().getVssname().equals(user.getvSSName())){


                try {


                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("VSSId", user.getVid());
                    jsonObject.put("DivisionId", user.getDivisionId());
                    jsonObject.put("RangeId", user.getRangeId());
                    jsonObject.put("Amount", model.getInventory().getPrice());
                    jsonObject.put("DateTime", model.getInventory().getDate());
                    jsonObject.put("ReceivedFrom", "");
                    jsonObject.put("SocietyName", "");
                    jsonObject.put("ThirdParty", "");
                    jsonObject.put("Random", model.getInventory().getInventoryId());
                    jsonObject.put("PaymentStatus", 0);
                    jsonObject.put("PaymentType", "");
                    jsonObject.put("Collector", model.getInventory().getColectname());
                    jsonObject.put("Product", model.getItemType().getMycase());
                    jsonObject.put("QuantityMeasurement", model.getInventory().getMeasurements());
                    jsonObject.put("Quantity", model.getInventory().getQuantity());
                    jsonArray.put(jsonObject);
                    Log.i("paymettestoneee", jsonArray.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            }


        return jsonArray;
    }

    static class SortByDate implements Comparator<InventoryRelation> {
        @Override
        public int compare(InventoryRelation a, InventoryRelation b) {
            return Boolean.compare(a.getInventory().isSynced(), b.getInventory().isSynced());
        }
    }
    }