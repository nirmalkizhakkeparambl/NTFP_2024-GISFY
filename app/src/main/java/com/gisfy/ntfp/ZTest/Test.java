package com.gisfy.ntfp.ZTest;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.gisfy.ntfp.Login.Models.PaymentsModel;
import com.gisfy.ntfp.R;
import com.gisfy.ntfp.VSS.RequestForm.StocksModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Test extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
//        Type collectionType = new TypeToken<List<StocksModel>>(){}.getType();
//        List<StocksModel> lcs = (List<StocksModel>) new Gson().fromJson( response.body().string() , collectionType);

        JSONObject object=new JSONObject();
        try {
            object.put("VSS","Kulathupuzha");
            object.put("Division","Thiruvananthapuram");
            object.put("Range","Kulathupuzha");
            NetworkCall networkCall=new NetworkCall(Test.this, object, "http://148.72.208.177/ForestAPI/API/StocksSelect", new NetworkCall.NetworkCallBack() {
                @Override
                public void NetWorkPostExecute(String body) {
                    Log.i("dsf",body);
                }
            });
            networkCall.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}