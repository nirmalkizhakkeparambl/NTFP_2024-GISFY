package com.gisfy.ntfp.VSS.CollectorInventory;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gisfy.ntfp.API.RetrofitClient;
import com.gisfy.ntfp.Collectors.CollectorInventoryModel;
import com.gisfy.ntfp.Collectors.CollectorStockModel;
import com.gisfy.ntfp.Collectors.StockRequest;
import com.gisfy.ntfp.Login.Models.VSSUser;
import com.gisfy.ntfp.R;
import com.gisfy.ntfp.SqliteHelper.DBHelper;
import com.gisfy.ntfp.Utils.FCMNotifications;
import com.gisfy.ntfp.Utils.SharedPref;
import com.gisfy.ntfp.Utils.SnackBarUtils;
import com.gisfy.ntfp.Utils.StaticChecks;
import com.gisfy.ntfp.VSS.Collectors.edit_collector;
import com.gisfy.ntfp.VSS.Inventory.add_inventory;
import com.gisfy.ntfp.VSS.Inventory.edit_inventory;
import com.gisfy.ntfp.VSS.RequestForm.ChartAlert;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.gisfy.ntfp.RFO.Status.ToVSS.setDate;

public class CollectorInventory extends AppCompatActivity {
    private TabLayout tabLayout;
    private SharedPref pref;
    public List<CollectorStockModel> list=new ArrayList<>();
    private CollectorInventoryAdapter adapter;
    final HashMap<String,String> json=new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transit);
        pref=new SharedPref(this);
        tabLayout=(TabLayout)findViewById(R.id.tabLayout);
        initViews();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.pending)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.ack)));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter=new CollectorInventoryAdapter(list, CollectorInventory.this, new CollectorInventoryAdapter.PositionClickListener() {
            @Override
            public void itemClicked(CollectorStockModel data) {
                showDailog(data);
            }
        });
        recyclerView.setAdapter(adapter);


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
        findViewById(R.id.filter).setVisibility(View.GONE);
        TextView tv=findViewById(R.id.titlebar_title);
        tv.setText(getResources().getString(R.string.stockrequest));
        VSSUser user=pref.getVSS();
        json.put("DivisionId",user.getDivisionId()+"");
        json.put("RangeId", user.getRangeId()+"");
        json.put("VSSId", user.getVid()+"");
        Log.i("stockCheck",json.toString());
        getData(json);
        findViewById(R.id.backpayment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }


    private void getData(HashMap<String,String> json){
        Log.i("json141",json.toString());
        list.clear();
        findViewById(R.id.spin_kit).setVisibility(View.VISIBLE);
        Call<List<CollectorStockModel>> call = RetrofitClient.getInstance().getMyApi().getCollectorStocksfromvss(json);
        call.enqueue(new Callback<List<CollectorStockModel>>() {
            @Override
            public void onResponse(Call<List<CollectorStockModel>> call, Response<List<CollectorStockModel>> response) {
                if (response.isSuccessful()){
                    list=response.body();
                    Log.i("response150",response.body()+"");
                    try {
                        updateTab(tabLayout.getSelectedTabPosition());
                    } catch (Exception e) {
                        e.printStackTrace();
                        SnackBarUtils.ErrorSnack(CollectorInventory.this,getString(R.string.nodata));
                    }
                }else{
                    SnackBarUtils.ErrorSnack(CollectorInventory.this,getString(R.string.unabletofetch));
                }
                findViewById(R.id.spin_kit).setVisibility(View.GONE);
            }
            @Override
            public void onFailure(Call<List<CollectorStockModel>> call, Throwable t) {
                findViewById(R.id.spin_kit).setVisibility(View.GONE);
                SnackBarUtils.ErrorSnack(CollectorInventory.this,getString(R.string.servernotresponding));
            }
        });
    }
    private void updateTab(int position){
        if (position==0){
            List<CollectorStockModel> pending=new ArrayList<>();
            for (CollectorStockModel model:list){
                Log.i("modelNTFP174",model.getnTFP());
                if (model.getvSSStatus().equals("Pending")){
                    pending.add(model);
                }
            }
            adapter.updateData(pending);
        }else{
            List<CollectorStockModel> other=new ArrayList<>();
            for (CollectorStockModel model:list){
                if (!model.getvSSStatus().equals("Pending")){
                    other.add(model);
                }
            }
            adapter.updateData(other);
        }
    }

    private void showDailog(CollectorStockModel model){
        HorizontalScrollView scrollView = new HorizontalScrollView(this);
        TableLayout tableLayout=new TableLayout(this);
        tableLayout.setStretchAllColumns(true);
        tableLayout.removeAllViews();
        scrollView.addView(tableLayout);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);
        String[] titles=new String[]{"Inventory ID","NTFP","Type","Quantity","Collector","Member Name"};
        List<List<String>> lists= Collections.singletonList(Arrays.asList(model.getInventID()+"", model.getnTFP(), model.getnTFPType(), model.getQuantity()+" "+model.getUnit(), model.getCollectorName(),model.getmName()));

        new StaticChecks(this).setTableLayout(tableLayout,titles,lists);
        alertDialogBuilder
                .setMessage(getString(R.string.selectaceptorreject))
                .setCancelable(false)
                .setView(scrollView);
        if (!model.getvSSStatus().equals("true")&&!model.getvSSStatus().equals("false")) {
                    alertDialogBuilder.setPositiveButton(getString(R.string.save), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String body = "{\n" +
                                "\t\"InventID\":\"" + model.getInventID() + "\",\n" +
                                "\t\"VSSStatus\":\"true\"\n" +
                                "}";
                        new uploadTask(model).execute(body);
                            Intent intent = new Intent(CollectorInventory.this, add_inventory.class);
                            intent.putExtra("CollectorModel", (Serializable) model);
                            startActivity(intent);
                        }
                    }).setNegativeButton(getString(R.string.reject), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            String body = "{\n" +
                                    "\t\"InventID\":\"" + model.getInventID() + "\",\n" +
                                    "\t\"VSSStatus\":\"false\"\n" +
                                    "}";
                            new uploadTask(model).execute(body);
                        }
                    });
        }
        alertDialogBuilder.setNeutralButton(getString(R.string.close), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCancelable(true);
        alertDialog.show();
    }

    private class uploadTask extends AsyncTask<String,String,String>{

        private CollectorStockModel model;


        public uploadTask(CollectorStockModel model){
            this.model = model;
            Log.i("MMMMEEEEEMMM",model.getMemberId()+"");
            Log.i("MMMEEMMM",model.getmName()+"");
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            findViewById(R.id.spin_kit).setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {
            Log.i("Stock REq",strings[0]);
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, strings[0]);
            Request request = new Request.Builder()
                    .url("https://vanasree.com/NTFPAPI/API/VSSUppdateStatusForCollectorStock")
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
                return e.getClass().getSimpleName();
            }
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.equals(getResources().getString(R.string.synced))){
                SnackBarUtils.SuccessSnack(CollectorInventory.this,s);
//                Intent intent = new Intent(CollectorInventory.this, add_inventory.class);
//                intent.putExtra("CollectorModel", (Serializable) model);
//                startActivity(intent);
            }
            else if(s.equals(getResources().getString(R.string.somedetailsnotsynced)))
                SnackBarUtils.WarningSnack(CollectorInventory.this,getString(R.string.somedetailsnotsynced));
            else if (s.equals("JSONException")||s.equals("SQLiteException"))
                SnackBarUtils.ErrorSnack(CollectorInventory.this,getString(R.string.somethingwentwrong));
            else
                SnackBarUtils.ErrorSnack(CollectorInventory.this,getString(R.string.servernotresponding));
            findViewById(R.id.spin_kit).setVisibility(View.GONE);
            getData(json);
        }
    }

    private boolean setStatus(String json) throws JSONException {
        Log.i("ksdks",json);
        boolean flag=true;
        JSONArray jsonArray=new JSONArray(json);
        for (int i=0;i<jsonArray.length();i++){
            JSONObject details=jsonArray.getJSONObject(i);
            if (details.getString("Status").equals("Success")) {
                FCMNotifications.NotifyUser(CollectorInventory.this,"","","");
            }else{
                flag=false;
            }
        }
        return flag;
    }

}