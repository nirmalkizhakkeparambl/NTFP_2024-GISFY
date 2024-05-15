package com.gisfy.ntfp.Login;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.TintInfo;
import retrofit2.Call;
import retrofit2.Callback;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.gisfy.ntfp.API.RetrofitClient;
import com.gisfy.ntfp.Login.Models.VSSUser;
import com.gisfy.ntfp.Profile.Profile;
import com.gisfy.ntfp.R;
import com.gisfy.ntfp.Utils.Constants;
import com.gisfy.ntfp.Utils.FormUtils.FormUtils;
import com.gisfy.ntfp.Utils.SharedPref;
import com.gisfy.ntfp.Utils.SnackBarUtils;
import com.gisfy.ntfp.Utils.StaticChecks;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class ResetPassword extends AppCompatActivity {
    private SharedPref pref;
   private EditText userName, password, rePassword;
    private StaticChecks checks;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        pref=new SharedPref(this);
        userName = findViewById(R.id.username);
        password = findViewById(R.id.password);
        rePassword = findViewById(R.id.repassword);
        checks=new StaticChecks(this);
        findViewById(R.id.signin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if (SnackBarUtils.NetworkSnack(ResetPassword.this)) {
                    if (checks.checkETList(new EditText[]{userName, password,rePassword})) {
                        if (password.getText().toString().equals(rePassword.getText().toString())){
                            try {
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("UserName", userName.getText().toString());
                                jsonObject.put("Password", password.getText().toString());
                                jsonObject.put("Role",pref.getString("Role"));

                                putData(jsonObject,"https://vanasree.com/NTFPAPI/API/ResetPassword");

                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(ResetPassword.this, "Unable to wrap the data", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(ResetPassword.this, "Confirmation Password Doesn't Match", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
            }
        });

    }
    private void putData(JSONObject requestBody,String URL){
        Log.i("REQUESTBODY ",requestBody.toString()+"//"+URL);
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Changing...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        JsonObjectRequest loginRequest = new JsonObjectRequest(Request.Method.POST, URL,
                requestBody,new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();
                Log.d("DEBUGGING","RES"+response.toString());
                try {
                    if (FormUtils.hasValue(response,"Status")&&FormUtils.getValue(response,"Status").equals("Success")){
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(ResetPassword.this);
                            builder1.setTitle("Password Changed Successfully");
                            builder1.setMessage("Please RE-Login.");
                            builder1.setCancelable(false);

                        builder1.setPositiveButton(
                                    "OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                            new SharedPref(ResetPassword.this).clearPref();
                                            Intent intent = new Intent(getApplicationContext(), Language.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                            finish();
                                        }
                                    });
                        Dialog dialog = builder1.create();
                        dialog.show();
                        dialog.setCanceledOnTouchOutside(false);

                    }else{
                        Toast.makeText(ResetPassword.this, FormUtils.getValue(response,"message"), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(ResetPassword.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.d("DEBUGGING","Err: "+error.getMessage());
                String message = "Error occurred.. try again later";
                if (error instanceof NetworkError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (error instanceof ServerError) {
                    message = "The server could not be found. Please try again after some time!!";
                } else if (error instanceof AuthFailureError) {
                    message = "Cannot Authenticate, Please Login again";
                } else if (error instanceof ParseError) {
                    message = "Parsing error! Please try again after some time!!";
                } else if (error instanceof NoConnectionError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (error instanceof TimeoutError) {
                    message = "Connection TimeOut! Your internet connection is slow.";
                }
                Toast.makeText(ResetPassword.this,message,Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(loginRequest);
    }
}