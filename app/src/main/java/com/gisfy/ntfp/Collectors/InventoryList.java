package com.gisfy.ntfp.Collectors;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Toast;

import com.gisfy.ntfp.HomePage.Home;
import com.gisfy.ntfp.Login.Models.CollectorUser;
import com.gisfy.ntfp.R;
import com.gisfy.ntfp.SqliteHelper.DBHelper;
import com.gisfy.ntfp.SqliteHelper.Entity.InventoryEntity;
import com.gisfy.ntfp.SqliteHelper.Entity.InventoryRelation;
import com.gisfy.ntfp.SqliteHelper.NtfpDao;
import com.gisfy.ntfp.SqliteHelper.SynchroniseDatabase;
import com.gisfy.ntfp.Utils.SharedPref;
import com.gisfy.ntfp.Utils.SnackBarUtils;
import com.gisfy.ntfp.Utils.StaticChecks;
import com.gisfy.ntfp.VSS.Collectors.Collector;
import com.gisfy.ntfp.VSS.Collectors.list_collectors;
import com.gisfy.ntfp.VSS.Inventory.adapter_inventory;
import com.gisfy.ntfp.VSS.Inventory.list_inventory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class InventoryList extends AppCompatActivity {
    private InventoryAdapter adapter;
    private final List<InventoryRelation> list=new ArrayList<>();
    private final List<InventoryRelation> inventeryRelationList =new ArrayList<>();
    private List<InventoryRelation> shallowCopy =new ArrayList<>();

    private DBHelper db;
    public ImageView sync;
    public boolean isinActionMode=false;
    private ImageView upload;
    private NtfpDao dao;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_list);
        recyclerView=findViewById(R.id.recyclerView);

        sync=findViewById(R.id.synchronise);
        upload=findViewById(R.id.upload);
        db=new DBHelper(this);
        dao = SynchroniseDatabase.getInstance(this).ntfpDao();
        RecyclerView recyclerView=findViewById(R.id.recyclerView);
        adapter=new InventoryAdapter(list,InventoryList.this);
        recyclerView.setAdapter(adapter);
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
                        new StaticChecks(InventoryList.this).showSnackBar("Filtered...");
                    }
                }, mYear, mMonth, mDay);

        sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload.setVisibility(View.VISIBLE);
                sync.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
            }
        });
        findViewById(R.id.upload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adapter.getSelectedItems().size()>0) {
                    new publishTask().execute();


                }else{
                    SnackBarUtils.ErrorSnack(InventoryList.this,getResources().getString(R.string.nodata));
            }}
        });
        findViewById(R.id.backinv).setOnClickListener(new View.OnClickListener() {
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
            i.putExtra("position",0);
            i.putExtra("title",getString(R.string.inventory));
            startActivity(i);
        }
    }

    private class publishTask extends AsyncTask<String,String,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            findViewById(R.id.spin_kit).setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {
            Log.i("jsonarraytoString93", getjsonarray().toString()+"");

            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, getjsonarray().toString());
            Request request = new Request.Builder()
                    .url("http://vanasree.com/NTFPAPI/API/CollectorInventoty")
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .build();
            try {
                okhttp3.Response response = client.newCall(request).execute();
                String responseData = response.body().string();
                Log.i("response105",responseData+"");
                if (setStatus(responseData))
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
            Intent i = new Intent(InventoryList.this, Home.class);

            startActivity(i);
            super.onPostExecute(s);
            findViewById(R.id.spin_kit).setVisibility(View.VISIBLE);
            if (s.equals(getResources().getString(R.string.synced)))
                Toast.makeText(getApplicationContext()
                                ,getString(R.string.synced),
                                Toast.LENGTH_SHORT)
                        .show();
            else if(s.equals(getResources().getString(R.string.somedetailsnotsynced)))
                SnackBarUtils.WarningSnack(InventoryList.this,getString(R.string.somedetailsnotsynced));
            else if (s.equals("JSONException")||s.equals("SQLiteException"))
                SnackBarUtils.ErrorSnack(InventoryList.this,getString(R.string.somethingwentwrong));
            else
                SnackBarUtils.ErrorSnack(InventoryList.this,getString(R.string.servernotresponding));
            isinActionMode=false;
            upload.setVisibility(View.GONE);
            sync.setVisibility(View.VISIBLE);
            adapter.notifyDataSetChanged();
            findViewById(R.id.spin_kit).setVisibility(View.GONE);
        }

    }


private JSONArray getjsonarray(){
    JSONArray jsonArray=new JSONArray();
    try {
        CollectorUser user = new SharedPref(InventoryList.this).getCollector();
        for (InventoryRelation model:adapter.getSelectedItems()){
            JSONObject object=new JSONObject();
            object.put("DivisionId",user.getDivisionId());
            object.put("RangeId",user.getRangeId());
            object.put("VSSId",user.getvSSId());
            if (model.getNtfp()!=null){
                object.put("NTFPName",model.getNtfp().getNTFPscientificname());
                object.put("NTFPId",model.getNtfp().getNid());
            }
            object.put("Unit",model.getInventory().getMeasurements());   //
            object.put("Quantity",model.getInventory().getQuantity());
//          object.put("Loss",00);
            object.put("DateTime",model.getInventory().getDate());
            object.put("Random",model.getInventory().getInventoryId());
            object.put("CollectorID",user.getCid());
            if (model.getItemType()!=null){
             object.put("NTFPType",model.getItemType().getMycase());
             object.put("NTFPTypeId",model.getItemType().getItemId()); }
            object.put("MemberId",model.getMember()!=null?model.getMember().getMemberId():-1);
            object.put("location_id",model.getInventory().getLocationId());
            jsonArray.put(object);
            Log.i("inventorydata",jsonArray.toString());
        }
    } catch (JSONException e) {
        e.printStackTrace();
    }
    return jsonArray;
}

    private class fetchTask extends AsyncTask<String,String,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            findViewById(R.id.spin_kit).setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {
            try {

                list.addAll(dao.getAllInventories());


                if (list.size()>0){

                    shallowCopy = list.subList(0, list.size());

                    Collections.reverse(shallowCopy);
                    Log.i("ListSize221",shallowCopy.size()+"");

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
            adapter=new InventoryAdapter(shallowCopy,  InventoryList.this);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(layoutManager);
            if (shallowCopy.size()>0)
                Collections.sort(shallowCopy,new  InventoryList.SortByDate());
            recyclerView.setAdapter(adapter);
            if (s!=null)
                SnackBarUtils.ErrorSnack( InventoryList.this,s);

        }
    }

    private boolean setStatus(String json) throws JSONException {
        Log.i("kishore",json);
        boolean flag=true;
        JSONArray jsonArray=new JSONArray(json);
        for (int i=0;i<jsonArray.length();i++){
            JSONObject details=jsonArray.getJSONObject(i);
            Log.i("data194response",details.getString("Status"));
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
    static class SortByDate implements Comparator<InventoryRelation> {
        @Override
        public int compare(InventoryRelation a, InventoryRelation b) {
            return Boolean.compare(a.getInventory().isSynced(), b.getInventory().isSynced());
        }
    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        Intent i=new Intent(this,Home.class);
//        i.putExtra("position",2);
//        i.putExtra("title",getString(R.string.inventory));
//        startActivity(i);
//    }
}