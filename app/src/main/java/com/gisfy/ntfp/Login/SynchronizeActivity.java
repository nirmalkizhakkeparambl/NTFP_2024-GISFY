package com.gisfy.ntfp.Login;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.gisfy.ntfp.API.RetrofitClient;
import com.gisfy.ntfp.HomePage.CollectorNames;
import com.gisfy.ntfp.HomePage.Home;
import com.gisfy.ntfp.Login.Models.CollectorUser;
import com.gisfy.ntfp.Login.Models.PaymentsModel;
import com.gisfy.ntfp.Login.Models.ShipmentModel;
import com.gisfy.ntfp.Login.Models.VSSUser;
import com.gisfy.ntfp.R;
import com.gisfy.ntfp.SqliteHelper.DBHelper;
import com.gisfy.ntfp.SqliteHelper.Entity.CollectorsModel;
import com.gisfy.ntfp.SqliteHelper.Entity.InventoryEntity;
import com.gisfy.ntfp.SqliteHelper.NtfpDao;
import com.gisfy.ntfp.SqliteHelper.SynchroniseDatabase;
import com.gisfy.ntfp.Utils.SharedPref;
import com.gisfy.ntfp.Utils.SnackBarUtils;
import com.gisfy.ntfp.VSS.Collectors.Collector;
import com.gisfy.ntfp.VSS.Collectors.FamilyDetails.FamilyData;
import com.gisfy.ntfp.VSS.Collectors.list_collectors;
import com.gisfy.ntfp.VSS.RequestForm.StocksModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class SynchronizeActivity extends AppCompatActivity {
    private NtfpDao dao;
    private SharedPref pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_syncronise);
        pref=new SharedPref(this);
        dao = SynchroniseDatabase.getInstance(this).ntfpDao();
        new pushTask().execute();
    }

    private class pushTask extends AsyncTask<String,String,String>{
        @Override
        protected String doInBackground(String... strings) {
           if (pref.getString("Role").equals("VSS")){
               VSSUser user=new SharedPref(SynchronizeActivity.this).getVSS();
               DBHelper db=new DBHelper(SynchronizeActivity.this);
               HashMap<String,String> json=new HashMap<>();
               json.put("VSSId",user.getVid()+"");
               json.put("DivisionId",user.getDivisionId()+"");
               json.put("RangeId",user.getRangeId()+"");
               Log.i("Request60get",json.toString()+"");

               Call<List<Collector>> CollectorSelect = RetrofitClient.getInstance().getMyApi().CollectorSelect(json);
               CollectorSelect.enqueue(new Callback<List<Collector>>() {
                   @Override
                   public void onResponse(Call<List<Collector>> call, Response<List<Collector>> response) {
                       Log.i("respppp70",response+"");
                       if (response.isSuccessful()&&response.body().get(0).getUid()!=null) {
                           List<Collector> heroList = response.body();
                           Log.i("ListSize69",heroList.size()+"");
                           for (Collector model:heroList){
                               db.insertData(model);

                               HashMap<String,String> jsonFamily=new HashMap<>();
                               jsonFamily.put("Cid",model.getCid()+"");
                                   Call<List<FamilyData>> FamilyDataSelect = RetrofitClient.getInstance().getMyApi().MemberListSelect(jsonFamily);
                               FamilyDataSelect.enqueue(new Callback<List<FamilyData>>() {
                                   @Override
                                   public void onResponse(Call<List<FamilyData>> call, Response<List<FamilyData>> response) {
                                       if (response.isSuccessful()&&response.body().get(0).getFamilyid()!=null) {
                                           List<FamilyData> heroList = response.body();
                                           Log.i("ListSize69",heroList.size()+"");
                                           for (FamilyData model:heroList){
                                               db.insertData(model);
                                           }
                                       }
                                   }
                                   @Override
                                   public void onFailure(Call<List<FamilyData>> call, Throwable t) {
                                   }
                               });
                           }


                       }
                   }
                   @Override
                   public void onFailure(Call<List<Collector>> call, Throwable t) {
                   }
               });


               Call<List<ShipmentModel>> shipmentSelect = RetrofitClient.getInstance().getMyApi().shipmentSelect(json);
               shipmentSelect.enqueue(new Callback<List<ShipmentModel>>() {
                   @Override
                   public void onResponse(Call<List<ShipmentModel>> call, Response<List<ShipmentModel>> response) {
                       if (response.isSuccessful()&&response.body().get(0).getShipmentNumber()!=null) {
                           List<ShipmentModel> heroList = response.body();
                           for (ShipmentModel model:heroList){
                               db.Insert(model);
                           }
                       }
                   }
                   @Override
                   public void onFailure(Call<List<ShipmentModel>> call, Throwable t) {
                   }
               });

               VSSUser vSSUserInventory =new SharedPref(SynchronizeActivity.this).getVSS();
               HashMap<String,String> vSSinventoryEntityjson=new HashMap<>();
               vSSinventoryEntityjson.put("VSSId",vSSUserInventory.getVid()+"");

               Call<List<InventoryEntity>> inventoryEntity = RetrofitClient.getInstance().getMyApi().InventoryEntitySelect(vSSinventoryEntityjson);
               inventoryEntity.enqueue(new Callback<List<InventoryEntity>>() {
                   @Override
                   public void onResponse(Call<List<InventoryEntity>> call, retrofit2.Response<List<InventoryEntity>> response) {
                       if (response.isSuccessful()&&response.body().get(0).getRandom()!=null) {
                           List<InventoryEntity> heroList = response.body();
                           for (InventoryEntity model:heroList){
                               dao.insertInventory(model);
                           }
                       }
                   }
                   @Override
                   public void onFailure(Call<List<InventoryEntity>> call, Throwable t) {
                   }
               });
//               Call<List<CollectorsModel>> ColectorSelect = RetrofitClient.getInstance().getMyApi().CollectorSelect(json);
//               ColectorSelect.enqueue(new Callback<List<CollectorsModel>>() {
//                   @Override
//                   public void onResponse(Call<List<CollectorsModel>> call, retrofit2.Response<List<CollectorsModel>> response) {
//                       if (response.isSuccessful()&&response.body().get(0).getRandom()!=null) {
//                           List<CollectorsModel> heroList = response.body();
//                           for (CollectorsModel model:heroList){
//                               db.Insert(model);
//                           }
//                       }
//                   }
//                   @Override
//                   public void onFailure(Call<List<StocksModel>> call, Throwable t) {
//                   }
//               });


               Call<List<StocksModel>> stockSelect = RetrofitClient.getInstance().getMyApi().stockSelect(json);
               stockSelect.enqueue(new Callback<List<StocksModel>>() {
                   @Override
                   public void onResponse(Call<List<StocksModel>> call, retrofit2.Response<List<StocksModel>> response) {
                       if (response.isSuccessful()&&response.body().get(0).getRandom()!=null) {
                           List<StocksModel> heroList = response.body();
                           for (StocksModel model:heroList){
                               db.Insert(model);
                           }
                       }
                   }
                   @Override
                   public void onFailure(Call<List<StocksModel>> call, Throwable t) {
                   }
               });
               json.put("PaymentType","Made");
               Call<List<PaymentsModel>> paymentsSelect = RetrofitClient.getInstance().getMyApi().paymentsSelect(json);
               paymentsSelect.enqueue(new Callback<List<PaymentsModel>>() {
                   @Override
                   public void onResponse(Call<List<PaymentsModel>> call, retrofit2.Response<List<PaymentsModel>> response) {
                       if (response.isSuccessful()&&response.body().get(0).getRandom()!=null) {
                           List<PaymentsModel> heroList = response.body();
                           for (PaymentsModel model:heroList){
                               db.Insert(model);
                           }
                       }
                   }
                   @Override
                   public void onFailure(Call<List<PaymentsModel>> call, Throwable t) {
                   }
               });

            }
            else if (pref.getString("Role").equals("Collector")){
               CollectorUser CollectorUseruser=new SharedPref(SynchronizeActivity.this).getCollector();

               HashMap<String,String> inventoryEntityjson=new HashMap<>();
               inventoryEntityjson.put("VSSId",CollectorUseruser.getvSSId()+"");
               inventoryEntityjson.put("CollectorId",CollectorUseruser.getCid()+"");
               Log.i("Request94",inventoryEntityjson.toString());

               Call<List<InventoryEntity>> inventoryEntity = RetrofitClient.getInstance().getMyApi().InventoryEntitySelect(inventoryEntityjson);
               inventoryEntity.enqueue(new Callback<List<InventoryEntity>>() {
                   @Override
                   public void onResponse(Call<List<InventoryEntity>> call, retrofit2.Response<List<InventoryEntity>> response) {
                       if (response.isSuccessful()&&response.body().get(0).getRandom()!=null) {
                           List<InventoryEntity> heroList = response.body();
                           for (InventoryEntity model:heroList){
                               dao.insertInventory(model);
                           }
                       }
                   }
                   @Override
                   public void onFailure(Call<List<InventoryEntity>> call, Throwable t) {
                   }
               });


           }

            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Intent intent = new Intent(getApplicationContext(), Home.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
    }

}