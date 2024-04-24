package com.gisfy.ntfp.VSS.Collectors;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Toast;

import com.gisfy.ntfp.Collectors.InventoryList;
import com.gisfy.ntfp.Login.Models.*;
import com.gisfy.ntfp.HomePage.Home;
import com.gisfy.ntfp.R;
import com.gisfy.ntfp.SqliteHelper.DBHelper;
import com.gisfy.ntfp.SqliteHelper.Entity.CollectorsModel;
import com.gisfy.ntfp.SqliteHelper.Entity.InventoryRelation;
import com.gisfy.ntfp.Utils.*;
import com.gisfy.ntfp.Utils.StaticChecks;
import com.gisfy.ntfp.VSS.Collectors.FamilyDetails.FamilyData;
import com.gisfy.ntfp.VSS.Collectors.FamilyDetails.Family_adapter;
import com.gisfy.ntfp.VSS.Inventory.adapter_inventory;
import com.gisfy.ntfp.VSS.Inventory.list_inventory;
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

public class list_collectors extends AppCompatActivity {
    private DBHelper dbHelper;
    private RecyclerView recyclerView;
    private adapter_collectors adapter;
    public boolean isinActionMode=false;
    public List<Collector> list=new ArrayList<>();
    public ImageView sync, upload;
    public SpinKitView spinKitView;
    private boolean btnFlag=false;
    public List<FamilyData> familylist=new ArrayList<>();
    JSONArray jsonArray1 = new JSONArray();
    private List<Collector> shallowCopy =new ArrayList<>();
    private List<CollectorsModel> memberList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_collectors);
        intiViews();
        new fetchTask().execute();
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        final DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        new StaticChecks(list_collectors.this).showSnackBar("Filtered...");
                    }
                }, mYear, mMonth, mDay);

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btnFlag)
                    SnackBarUtils.WarningSnack(list_collectors.this,getString(R.string.wait));
                else {
                    if (adapter.getSelectedItems().size()>0){
                        try {
                            new uploadTask().execute();
                            Intent i = new Intent(list_collectors.this, Home.class);
                            startActivity(i);
                        }catch (Exception e){
                            Log.i("EXXXP",e+"");
                        }

                    }else{
                        SnackBarUtils.ErrorSnack(list_collectors.this,"No Items Selected");
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
    private void intiViews() {
        dbHelper=new DBHelper(this);
        recyclerView=findViewById(R.id.recyclerView);
        sync=findViewById(R.id.synchronise);
        list_collectors.this.setFinishOnTouchOutside(true);
        upload =findViewById(R.id.upload);
        spinKitView=findViewById(R.id.spin_kit);

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
    @Override
    public void onBackPressed() {
        if (isinActionMode) {
            isinActionMode = false;
            adapter.notifyDataSetChanged();
        }else {
            Intent i=new Intent(this,Home.class);
            i.putExtra("position",1);
            i.putExtra("title",getString(R.string.collectors));
            startActivity(i);
        }
    }
    private class fetchTask extends AsyncTask<String,String,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            spinKitView.setVisibility(View.VISIBLE);
        }
        @Override
        protected String doInBackground(String... strings) {
            try {
                list=dbHelper.getAllDataFromCollectors();


                if (list.size()>0){

                    shallowCopy = list.subList(0, list.size());

                    Collections.reverse(shallowCopy);
                    Log.i("ListSize224",shallowCopy.size()+"");

                }

                return "OK";
            } catch (Exception e) {
                e.printStackTrace();
                return getString(R.string.somethingwentwrong);
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (!s.equals("OK"))
                SnackBarUtils.ErrorSnack(list_collectors.this,s);

            adapter=new adapter_collectors(shallowCopy, list_collectors.this);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());


            recyclerView.setLayoutManager(layoutManager);
            if (shallowCopy.size()>0)
                Collections.sort(shallowCopy,new  list_collectors.SortByDate());
            recyclerView.setAdapter(adapter);

            recyclerView.setAdapter(adapter);
            spinKitView.setVisibility(View.GONE);
        }
    }
    static class SortByDate implements Comparator<Collector> {
        @Override
        public int compare(Collector a, Collector b) {
            return Boolean.compare(a.getSynced()==1, b.getSynced()==1);
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
            Log.i("jsonobody176",getRequestBody().toString());
            RequestBody body = RequestBody.create(mediaType,getRequestBody().toString());
            Request request = new Request.Builder()
                    .url(getString(R.string.baseURL)+"NTFPAPI/API/Collector")
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
                return e.getClass().getSimpleName();
            }
        }

        @Override
        protected void onPostExecute(String s) {
            btnFlag=false;
            if (s.equals(getResources().getString(R.string.synced)))
                Toast.makeText(getApplicationContext()
                                ,getString(R.string.synced),
                                Toast.LENGTH_SHORT)
                        .show();
            else if(s.equals(getResources().getString(R.string.somedetailsnotsynced)))
                SnackBarUtils.WarningSnack(list_collectors.this,getString(R.string.somedetailsnotsynced));
            else if (s.equals("JSONException"))
                SnackBarUtils.ErrorSnack(list_collectors.this,getString(R.string.somethingwentwrong));
            else
                SnackBarUtils.ErrorSnack(list_collectors.this,getString(R.string.servernotresponding));
            isinActionMode=false;
            upload.setVisibility(View.GONE);
            sync.setVisibility(View.VISIBLE);

//            if (shallowCopy.size()>0)
//                Collections.sort(shallowCopy,new list_collectors.SortByDate());
            adapter.notifyDataSetChanged();
            findViewById(R.id.spin_kit).setVisibility(View.GONE);
            super.onPostExecute(s);
        }
    }
    private JSONArray getRequestBody(){
        JSONArray jsonArray = new JSONArray();
        for (Collector model:adapter.getSelectedItems()){
                JSONObject jsonObject = new JSONObject();
                try {
                    VSSUser user = new SharedPref(list_collectors.this).getVSS();
                    jsonObject.put("Random",model.getUid());
                    jsonObject.put("CollectorName",model.getCollector_name());
                    jsonObject.put("Village",model.getVillage());
                    jsonObject.put("DivisionId",user.getDivisionId());
                    jsonObject.put("RangeId",user.getRangeId());
//                    jsonObject.put("RegionId",model.getRange());
                    jsonObject.put("VSSId",user.getVid());
                    jsonObject.put("SpouseName",model.getCollector_spouse());
                    jsonObject.put("SocialCategory",model.getCategory());
                    jsonObject.put("Age",model.getAge());
                    jsonObject.put("Gender",model.getGender());
                    jsonObject.put("Rights","Yes");
                    jsonObject.put("DOB",model.getDob());
                    jsonObject.put("TypeOfId",model.getIdtype());
                    jsonObject.put("IDNumber",model.getIdno());
                    jsonObject.put("EducationQualification",model.getEducation());
                    jsonObject.put("TotalFamilycount",model.getFamily());
                    jsonObject.put("BankName",model.getBankname());
                    String str = model.getNtfps();
                    Matcher m = Pattern.compile("\\((.*?)\\)").matcher(str);
                    while(m.find())
                    {
                        jsonObject.put("MajorCrop",m.group(1));
                    }
                    jsonObject.put("BankIFSCCode",model.getBankifsc());
                    jsonObject.put("BankAccount",model.getBankaccountno());
                    jsonObject.put("Remarks",model.getInfo());
                    jsonObject.put("UserName",model.getUsername());
                    jsonObject.put("Password",model.getPassword());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                jsonArray.put(jsonObject);
            Log.i("Collectorsdata", jsonArray.toString());
        }
        return jsonArray;
    }
//    private boolean setStatus(String json) throws JSONException {
//        boolean flag=true;
//        Log.i("jresponse",json);
//        JSONArray jsonArray=new JSONArray(json);
//        for (int i=0;i<jsonArray.length();i++){
//            JSONObject details=jsonArray.getJSONObject(i);
//            if (details.getString("Status").equals("Success")) {
//                Collector model = dbHelper.getAllDataFromCollectorsWhere(" uid='" + details.getString("Random")+"'").get(0);
//                model.setSynced(1);
//                dbHelper.updateData(model);
//                String cID = details.getString("CollectorId");
//                if (dbHelper.getAllDataFromFamilyWhere("uid='"+details.getString("Random")+"'").size()>0){
//                    final FamilyData familymodel=dbHelper.getAllDataFromFamilyWhere("uid='"+details.getString("Random")+"'").get(0);
//                    Log.i("familymodel",familymodel.getFamily_name()+"");
//                    jsonArray1 = getFamilyRequestBody(familymodel, cID);
//                    if (jsonArray1.toString()!=null){
//                        new uploadfamilyTask().execute();
//                    }else {
//                        Toast.makeText(list_collectors.this, "Family Request Bosy is null", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }else{
//                flag=false;
//                Collector model = dbHelper.getAllDataFromCollectorsWhere(" uid='" + details.getString("Random")+"'").get(0);
//                model.setSynced(0);
//                dbHelper.updateData(model);
//            }
//        }
//        shallowCopy.clear();
//        shallowCopy.addAll(dbHelper.getAllDataFromCollectors());
//
//        return flag;
//    }
private boolean setStatus(String json) throws JSONException {
    boolean flag=true;
    Log.i("jresponse",json);
    JSONArray jsonArray=new JSONArray(json);
    for (int i=0;i<jsonArray.length();i++){
        JSONObject details=jsonArray.getJSONObject(i);
        if (details.getString("Status").equals("Success")) {
            Collector model = dbHelper.getAllDataFromCollectorsWhere("uid='" + details.getString("Random")+"'").get(0);
            model.setSynced(1);
            dbHelper.updateData(model);
            String cID = details.getString("CollectorId");
                if (dbHelper.getAllDataFromFamilyWhere("uid='"+details.getString("Random")+"'").size()>0){
                    int n = dbHelper.getAllDataFromFamilyWhere("uid='"+details.getString("Random")+"'").size();
                    for(int a=0;a<n;a++){

                    final FamilyData familymodel = dbHelper.getAllDataFromFamilyWhere("uid='"+details.getString("Random")+"'").get(a);
                    Log.i("familymodel",familymodel.getFamily_name()+"");
                    jsonArray1 = getFamilyRequestBody(familymodel, cID);
                    if (jsonArray1.toString()!=null){
                        new uploadfamilyTask().execute();
                    }else {
                        Toast.makeText(list_collectors.this, "Family Request is null", Toast.LENGTH_SHORT).show();
                    }}
            }
         }else{
            flag=false;
            Collector model = dbHelper.getAllDataFromCollectorsWhere(" uid='" + details.getString("Random")+"'").get(0);
            model.setSynced(0);
            dbHelper.updateData(model);
        }
    }
    list.clear();
    list.addAll(dbHelper.getAllDataFromCollectors());
    return flag;
}
    private boolean setfamilyStatus(String json) throws JSONException {
        boolean flag = true;
        Log.i("jresponse293",json);
        JSONArray jsonArray=new JSONArray(json);
        for (int i=0;i<jsonArray.length();i++){
            JSONObject details=jsonArray.getJSONObject(i);
            if (details.getString("Status").equals("Success")) {
                flag=true;
            }else{
                flag=false;
            }
        }
        return flag;
    }
//    private boolean setfamilyStatus(String json) throws JSONException {
//        boolean flag=true;
//        Log.i("jresponse293",json);
//        JSONArray jsonArray=new JSONArray(json);
//        for (int i=0;i<jsonArray.length();i++){
//            JSONObject details=jsonArray.getJSONObject(i);
//            if (details.getString("Status").equals("Success")) {b
//                flag=true;
//            }else{
//                flag=false;
//            }
//        }
//        return flag;
//    }



    private JSONArray getFamilyRequestBody(FamilyData familyData , String cid){

            JSONObject jsonObject = new JSONObject();
            try {
                VSSUser user = new SharedPref(list_collectors.this).getVSS();
                jsonObject.put("Cid",cid);
                jsonObject.put("Random",familyData.getFamilyid());
                jsonObject.put("CollectorName",familyData.getFamily_name());
                jsonObject.put("Village",familyData.getVillage());
                jsonObject.put("DivisionId",user.getDivisionId());
                jsonObject.put("RangeId",user.getRangeId());
//              jsonObject.put("RegionId",model.getRange());
                jsonObject.put("VSSId",user.getVid());
                jsonObject.put("SocialCategory",familyData.getCategory());
                jsonObject.put("Age",familyData.getAge());
                jsonObject.put("Gender",familyData.getGender());
                jsonObject.put("Rights","Yes");
                jsonObject.put("DOB",familyData.getDob());
                jsonObject.put("TypeOfId",familyData.getIdtype());
                jsonObject.put("IDNumber",familyData.getIdno());

                jsonObject.put("EducationQualification",familyData.getEducation());
                jsonObject.put("BankName",familyData.getBankname());
                String str = familyData.getNtfps();
                Matcher m = Pattern.compile("\\((.*?)\\)").matcher(str);
                while(m.find())
                {
                    jsonObject.put("MajorCrop",m.group(1));
                }
                jsonObject.put("BankIFSCCode",familyData.getBankifsc());
                jsonObject.put("BankAccount",familyData.getBankaccountno());
                jsonObject.put("Relation",familyData.getRelationship());
                jsonObject.put("Address",familyData.getAddress());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonArray1.put(jsonObject);
            Log.i("Collectors318", jsonArray1.toString());

        return jsonArray1;
    }
    private class uploadfamilyTask extends AsyncTask<String,String,String> {


        @Override
        protected String doInBackground(String... strings) {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("application/json");
            Log.i("jsonobody339", jsonArray1.toString());
            RequestBody body = RequestBody.create(mediaType, jsonArray1.toString());
            Request request = new Request.Builder()
                    .url(getString(R.string.baseURL) + "NTFPAPI/API/CollectorMember")
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .build();
            try {
                Response response = client.newCall(request).execute();
                if (setfamilyStatus(response.body().string()))
                    return getResources().getString(R.string.synced);

                else
                    return getResources().getString(R.string.familydetailsnotsynced);
            } catch (Exception e) {
                e.printStackTrace();
                return e.getClass().getSimpleName();
            }
        }



    @Override
        protected void onPostExecute(String s) {
            btnFlag=false;
            if (s.equals(getResources().getString(R.string.synced)))
                SnackBarUtils.SuccessSnack(list_collectors.this,s);
            else if(s.equals(getResources().getString(R.string.somedetailsnotsynced)))
                SnackBarUtils.WarningSnack(list_collectors.this,getString(R.string.somedetailsnotsynced));
            else if (s.equals("JSONException"))
                SnackBarUtils.ErrorSnack(list_collectors.this,getString(R.string.somethingwentwrong));
            else
                SnackBarUtils.ErrorSnack(list_collectors.this,getString(R.string.servernotresponding));
            isinActionMode=false;
            upload.setVisibility(View.GONE);
            sync.setVisibility(View.VISIBLE);
            adapter.notifyDataSetChanged();
            findViewById(R.id.spin_kit).setVisibility(View.GONE);
            super.onPostExecute(s);
        }
    }

}