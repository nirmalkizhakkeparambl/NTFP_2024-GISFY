package com.gisfy.ntfp.ZTest;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.gisfy.ntfp.Utils.Constants;
import com.gisfy.ntfp.VSS.RequestForm.StocksModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NetworkCall extends AsyncTask<String,Integer,String> {

    private final JSONObject body;
    private final String URL;
    private final NetworkCallBack callBack;
    ProgressDialog progressBar;

    public NetworkCall(Context context,JSONObject body, String URL, NetworkCallBack callBack){
        this.body=body;
        this.URL=URL;
        this.callBack=callBack;
        progressBar = new ProgressDialog(context);
        progressBar.setCancelable(true);
        progressBar.setMessage("Loading ...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressBar.setProgress(0);
        progressBar.setMax(100);
        progressBar.setIndeterminate(false);
        progressBar.show();

    }
    @Override
    protected String doInBackground(String... strings) {
        Log.i("body",body.toString());
        Log.i("URL",URL);
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody requestBody = RequestBody.create(mediaType,body.toString());
        Request request = new Request.Builder()
                .url(URL)
                .method("POST", requestBody)
                .addHeader("Content-Type", "application/json")
                .build();
        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
            return Constants.STATUS;
        }
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        progressBar.setProgress(values[0]);
        Log.i("dmsjk",values[0]+"");
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        progressBar.dismiss();
        callBack.NetWorkPostExecute(s);
    }
    public interface NetworkCallBack{
        void NetWorkPostExecute(String body);
    }
}
