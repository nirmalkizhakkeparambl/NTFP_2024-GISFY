package com.gisfy.ntfp.HomePage;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gisfy.ntfp.API.RetrofitClient;
import com.gisfy.ntfp.Login.LoginActivity;
import com.gisfy.ntfp.Login.Models.CollectorUser;
import com.gisfy.ntfp.Login.Models.RFOUser;
import com.gisfy.ntfp.Login.Models.VSSUser;
import com.gisfy.ntfp.Profile.Profile;
import com.gisfy.ntfp.R;
import com.gisfy.ntfp.RFO.Models.TransitPassModel;
import com.gisfy.ntfp.RFO.Models.VSSModel;
import com.gisfy.ntfp.SqliteHelper.DBHelper;
import com.gisfy.ntfp.SqliteHelper.Entity.CollectorsModel;
import com.gisfy.ntfp.SqliteHelper.Entity.NTFP;
import com.gisfy.ntfp.SqliteHelper.SynchroniseDatabase;
import com.gisfy.ntfp.Utils.Constants;
import com.gisfy.ntfp.Utils.JSONStorage;
import com.gisfy.ntfp.Utils.SharedPref;
import com.gisfy.ntfp.Utils.SnackBarUtils;
import com.gisfy.ntfp.Utils.StaticChecks;
import com.gisfy.ntfp.VSS.RequestForm.RFOModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Home extends AppCompatActivity {

    private StaticChecks checks;
    private SharedPref pref;
    public int backpressCnt=0;
    private DBHelper db;
    private ImageView wishImage;
    private List<CollectorsModel> memberList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_home);
        initViews();
        if (pref.getString("language").equals(Constants.ENGLISH)){
            wishImage.setImageResource(R.drawable.vanashree_logo_english);
        }else {
            wishImage.setImageResource(R.drawable.vanashreelogo);
        }
        if (SnackBarUtils.NetworkSnack(Home.this)&&pref.getString("type").equals(Constants.VSS)) {
            getVSSData();
        }else if (SnackBarUtils.NetworkSnack(Home.this)&&pref.getString("type").equals(Constants.RFO)){
            getRFOData();
        }else if (SnackBarUtils.NetworkSnack(Home.this)&&pref.getString("type").equals(Constants.COLLECTORS)){
            getCollectorData();
        }

    }

    private void initViews() {
        checks=new StaticChecks(this);
        pref=new SharedPref(this);
        db=new DBHelper(this);
        TextView name = findViewById(R.id.username);
         wishImage = findViewById(R.id.profile_image);
//        TextView welcome = findViewById(R.id.welcome);
        if (pref.getString("type").equals(Constants.RFO)) {
            String getName = pref.getString("username").replace("Range Officer","");
            name.setText("Range Officer \n"+ getName);
        }else{
            if (pref.getString("username").length() > 15) {
                if (pref.getString("username").contains(" ")) {
                    String[] names = pref.getString("username").split(" ");
                    String namestr = "";
                    for (String nam : names) {
                        namestr += nam + "\n";
                    }
                    name.setText(namestr);
                } else {
                    name.setText(pref.getString("username"));
                }
            } else {
                name.setText(pref.getString("username"));
            }
        }
//        checks.setWishes(welcome);

        if (getIntent().hasExtra("position")){
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.home_fram_layout, new HomeFrag2(getIntent().getIntExtra("position",2),getIntent().getStringExtra("title")));
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            backpressCnt=1;
        }else{
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.home_fram_layout, new HomeFrag1());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }

    @Override
    public void onBackPressed() {
        Log.i("onbackPressed",backpressCnt+"");
        if (backpressCnt==1){
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.home_fram_layout, new HomeFrag1());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            --backpressCnt;
        }else {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Closing Application")
                    .setMessage("Are you sure you want to close the Application?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finishAffinity();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
        }
    }
    private void getRFOData() {
        RFOUser user=pref.getRFO();
        HashMap<String,String> json=new HashMap<>();
        json.put("DivisionId",user.getDivisionId()+"");
        json.put("RangeId",user.getRangeId()+"");

        Call<List<VSSModel>> vss = RetrofitClient.getInstance().getMyApi().getVSS(json);
        vss.enqueue(new Callback<List<VSSModel>>() {
            @Override
            public void onResponse(Call<List<VSSModel>> call, retrofit2.Response<List<VSSModel>> response) {
                if (response.isSuccessful()) {
                    List<VSSModel> heroList = response.body();
                    StringBuilder vss= new StringBuilder();
                    for (VSSModel model:heroList){
                        vss.append(model.getvSSName()).append("&&fcms").append(model.getfCMID()).append("&&vss");
                    }
                    pref.addString("VSSs",vss.toString());
                } else {
                    checks.showSnackBar(getResources().getString(R.string.unabletofetch));
                }
            }
            @Override
            public void onFailure(Call<List<VSSModel>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        Call<List<PCModel>> pcs = RetrofitClient.getInstance().getMyApi().getPCS(json);
        pcs.enqueue(new Callback<List<PCModel>>() {
            @Override
            public void onResponse(Call<List<PCModel>> call, retrofit2.Response<List<PCModel>> response) {
                if (response.isSuccessful()) {
                    List<PCModel> heroList = response.body();
                    try {
                        db.deleteData(DBHelper.PROCESSING_CENTER);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    for (PCModel model : heroList) {
                        db.Insert(model);
                    }
                } else {
                    checks.showSnackBar(getResources().getString(R.string.unabletofetch));
                }
            }
            @Override
            public void onFailure(Call<List<PCModel>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getCollectorData(){
        CollectorUser user=pref.getCollector();
        HashMap<String,String> json = new HashMap<>();
        json.put("DivisionId", String.valueOf(user.getDivisionId()));
        json.put("RangeId", String.valueOf(user.getRangeId()));
        json.put("VSSId", String.valueOf(user.getvSSId()));
        setupMemberData(json);
        setupNTFPs(json);
    }


    private void getVSSData() {
        VSSUser user=pref.getVSS();

        HashMap<String,String> json=new HashMap<>();
        json.put("DivisionId",user.getDivisionId()+"");
        json.put("RangeId",user.getRangeId()+"");
        json.put("VSSId",user.getVid()+"");
        setupMemberData(json);
        setupNTFPs(json);
        String bidy="{\n" +
                "    \"DivisionId\": \""+user.getDivisionId()+"\",\n" +
                "    \"RangeId\": \""+user.getRangeId()+"\",\n" +
                "    \"VSSId\": \""+user.getVid()+"\"\n" +
                "}";
        Log.i("ntfpName",json.toString());
        new NTFPTask().execute(bidy);
        Call<List<PCModel>> pcs = RetrofitClient.getInstance().getMyApi().getPCS(json);
        pcs.enqueue(new Callback<List<PCModel>>() {
            @Override
            public void onResponse(Call<List<PCModel>> call, retrofit2.Response<List<PCModel>> response) {
                if (response.isSuccessful()) {
                    List<PCModel> heroList = response.body();
                    try {
                        db.deleteData(DBHelper.PROCESSING_CENTER);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    for (PCModel model : heroList) {
                        db.Insert(model);
                    }
                } else {
                    checks.showSnackBar(getResources().getString(R.string.unabletofetch));
                }
            }
            @Override
            public void onFailure(Call<List<PCModel>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        Log.i("RFO DEBUGGING",json.toString());
        Call<List<RFOModel>> rfOs = RetrofitClient.getInstance().getMyApi().getRFOs(json);
        rfOs.enqueue(new Callback<List<RFOModel>>() {
            @Override
            public void onResponse(Call<List<RFOModel>> call, retrofit2.Response<List<RFOModel>> response) {
                if (response.isSuccessful()) {
                    List<RFOModel> heroList = response.body();
                    StringBuilder rfo= new StringBuilder();
                    for (RFOModel model:heroList){
                        rfo.append(model.getrFOName()).append("-").append(model.getrFOId()).append("&&fcms").append(model.getfCMID()).append("&&rfos");
                    }
                    pref.addString("RFOs",rfo.toString());
                } else {
                    checks.showSnackBar(getResources().getString(R.string.unabletofetch));
                }
            }
            @Override
            public void onFailure(Call<List<RFOModel>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
//        json.put("TransitStatus","Accepted");
//        json.put("ForShipment","Yes");
        Log.i("Transit pass json",json.toString());
        Call<List<TransitPassModel>> call = RetrofitClient.getInstance().getMyApi().getTransitPass(json);

        call.enqueue(new Callback<List<TransitPassModel>>() {
            @Override
            public void onResponse(Call<List<TransitPassModel>> call, Response<List<TransitPassModel>> response) {
                if (response.isSuccessful()){
                    List<TransitPassModel> list=response.body();
//                    StringBuilder sb=new StringBuilder();
                    for (TransitPassModel model:list){
                        db.Insert(model);
//                        sb.append(model.getTransUniqueId()).append("&&kish");
                    }
//                    pref.addString("TransitPass",sb.toString());
                }
            }
            @Override
            public void onFailure(Call<List<TransitPassModel>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void setupNTFPs(HashMap<String,String> json) {
        Log.i("Line 2470",json.toString());
        Call<List<NTFP>> call = RetrofitClient.getInstance().getMyApi().getNTFPS(json);
        call.enqueue(new Callback<List<NTFP>>() {
            @Override
            public void onResponse(Call<List<NTFP>> call, Response<List<NTFP>> response) {
                if (response.isSuccessful()&&response.body().get(0).getNid()!=0) {
                    Log.i("Line 2430",response.body().get(0).toString());
                    List<NTFP> memberList = response.body();
                    SynchroniseDatabase database = SynchroniseDatabase.getInstance(Home.this);
                    database.ntfpDao().insertAllNTFPS(memberList);
                    for (NTFP model:memberList){
                        database.ntfpDao().insertAllNTFPTypes(model.getItemType());
                    }
                    findViewById(R.id.spin_kit).setVisibility(View.GONE);
                } else {
                    findViewById(R.id.spin_kit).setVisibility(View.GONE);
                }
            }
            @Override
            public void onFailure(Call<List<NTFP>> call, Throwable t) {
                findViewById(R.id.spin_kit).setVisibility(View.GONE);
            }
        });
    }

    public void setupMemberData(HashMap<String,String> json){


        Log.i("Line 2470",json.toString());
        Call<List<CollectorsModel>> call = RetrofitClient.getInstance().getMyApi().getCollectors(json);
        call.enqueue(new Callback<List<CollectorsModel>>() {
            @Override
            public void onResponse(Call<List<CollectorsModel>> call, Response<List<CollectorsModel>> response) {
                if (response.isSuccessful()&&response.body().get(0).getCid()!=0) {
                    Log.i("Line 2430",response.body().get(0).toString());
                    if(memberList!=null) {
                        memberList.clear();
                    }
                    memberList = response.body();
                    SynchroniseDatabase database = SynchroniseDatabase.getInstance(Home.this);
                  //  database.ntfpDao().getAllMembers().clear();
                    database.ntfpDao().insertAllCollector(memberList);
                    for (CollectorsModel model:memberList){
                        database.ntfpDao().insertAllMembers(model.getMember());
                    }
//                    findViewById(R.id.spin_kit).setVisibility(View.GONE);
                } else {
//                    findViewById(R.id.spin_kit).setVisibility(View.GONE);
                }
            }
            @Override
            public void onFailure(Call<List<CollectorsModel>> call, Throwable t) {
             //   findViewById(R.id.spin_kit).setVisibility(View.GONE);
            }
        });
    }

    private class NTFPTask extends AsyncTask<String,String,String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            findViewById(R.id.spin_kit).setVisibility(View.VISIBLE);
        }
        @Override
        protected String doInBackground(String... strings) {
            try {

                OkHttpClient client = new OkHttpClient().newBuilder()
                        .build();
                MediaType mediaType = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(mediaType,strings[0]);
                Log.i("BODDDDDY374",body+"");
                Request request = new Request.Builder()
                        .url("https://vanasree.com/NTFPAPI/API/NTFPList")
                        .method("POST", body)
                        .addHeader("Content-Type", "application/json")
                        .build();
                okhttp3.Response response = client.newCall(request).execute();
                JSONStorage.create(Home.this,"NTFP.json",response.body().string());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            findViewById(R.id.spin_kit).setVisibility(View.GONE);
        }
    }

}