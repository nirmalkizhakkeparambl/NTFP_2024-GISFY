package com.gisfy.ntfp.Login;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.text.MessageFormat;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gisfy.ntfp.API.RetrofitClient;
import com.gisfy.ntfp.HomePage.Home;
import com.gisfy.ntfp.Login.Models.CollectorUser;
import com.gisfy.ntfp.Login.Models.RFOUser;
import com.gisfy.ntfp.Login.Models.VSSUser;
import com.gisfy.ntfp.R;
import com.gisfy.ntfp.RFO.Models.StatusModel;
import com.gisfy.ntfp.Utils.Constants;
import com.gisfy.ntfp.Utils.SharedPref;
import com.gisfy.ntfp.Utils.SnackBarUtils;
import com.gisfy.ntfp.Utils.StaticChecks;
import com.gisfy.ntfp.VSS.Collectors.add_collector;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPassordActivity extends AppCompatActivity {


    private String type;
    private SharedPref pref;
    private EditText username,secondinput,rePassword;
    private boolean btnFlag=true;
    private String id=null;
    private String fcmid=null;
    private StaticChecks checks;
    private TextView chooseText;
    private VSSUser vssUser;
    private RFOUser rfoUser;
    private CollectorUser collectorUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_passord);
        initViews();

        findViewById(R.id.signin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SnackBarUtils.NetworkSnack(ForgotPassordActivity.this)) {
                    if (checks.checkETList(new EditText[]{username, secondinput})) {
                        if (btnFlag) {
                            HashMap<String, String> json = new HashMap<>();
                            json.put("UserName", username.getText().toString());
                            json.put("Authentication", secondinput.getText().toString());
                            if (type.equals(Constants.VSS)) {
                                json.put("Type", type);
                                checkVSS(json);
                            } else if (type.equals(Constants.RFO)) {
                                json.put("Type", type);
                                checkRFO(json);
                            } else if (type.equals(Constants.COLLECTORS)) {
                                json.put("Type", "Collector");
                                type = "Collector";
                                checkCollector(json);
                            }
                        } else {
                            SnackBarUtils.WarningSnack(ForgotPassordActivity.this, getResources().getString(R.string.wait));
                        }
                    }
                }
            }
        });
    }

    private void initViews() {
        pref=new SharedPref(this);
        checks=new StaticChecks(this);
        type=pref.getString("type");
        rePassword = findViewById(R.id.repassword);
        username=findViewById(R.id.username);
        secondinput=findViewById(R.id.secondinput);
        chooseText=findViewById(R.id.choosetext);
        findViewById(R.id.forgotpass).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ForgotPassordActivity.this,LoginActivity.class));
            }
        });

        findViewById(R.id.changerole).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ForgotPassordActivity.this,RoleChoose.class));
            }
        });

        if (type.equals(Constants.COLLECTORS)){
            secondinput.setHint(getResources().getString(R.string.dobformat));
            secondinput.setCompoundDrawablesWithIntrinsicBounds( R.drawable.vector_dob_green, 0,0, 0 );
            secondinput.setFocusableInTouchMode(false);
            final Calendar c = Calendar.getInstance();
            int currentYear = c.get(Calendar.YEAR);
            int currentMonth = c.get(Calendar.MONTH);
            int CurrentDayOfMonth = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog=new DatePickerDialog(ForgotPassordActivity.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    Calendar mCalender = Calendar.getInstance();
                    mCalender.set(Calendar.YEAR,year);
                    mCalender.set(Calendar.MONTH,month);
                    mCalender.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    dateFormat.setTimeZone(mCalender.getTimeZone());
                    secondinput.setText(dateFormat.format(mCalender.getTime()));
                }
            },currentYear,currentMonth,CurrentDayOfMonth);

            secondinput.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    datePickerDialog.show();
                }
            });
        }else{
            secondinput.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
        }
    }

    private void checkVSS(HashMap<String,String> json){
        btnFlag = false;
        findViewById(R.id.spin_kit).setVisibility(View.VISIBLE);
        Call<List<VSSUser>> call = RetrofitClient.getInstance().getMyApi().vssforgotpass(json);
        call.enqueue(new Callback<List<VSSUser>>() {
            @Override
            public void onResponse(Call<List<VSSUser>> call, Response<List<VSSUser>> response) {
                btnFlag = true;
                if (response.isSuccessful()&&response.body().get(0).getDivisionname()!=null) {
                    List<VSSUser> heroList = response.body();
                    vssUser=heroList.get(0);
                    pref.addString("username",heroList.get(0).getvSSName());
                    id=String.valueOf(heroList.get(0).getVid());
                    fcmid=heroList.get(0).getfCMID();
                    findViewById(R.id.spin_kit).setVisibility(View.GONE);
                    passwordReset();
                }else {
                    findViewById(R.id.spin_kit).setVisibility(View.GONE);
                    SnackBarUtils.ErrorSnack(ForgotPassordActivity.this, getResources().getString(R.string.pleasecheckthedetails));
                }
            }
            @Override
            public void onFailure(Call<List<VSSUser>> call, Throwable t) {
                btnFlag = true;
                findViewById(R.id.spin_kit).setVisibility(View.GONE);
                SnackBarUtils.ErrorSnack(ForgotPassordActivity.this, getResources().getString(R.string.servernotresponding));
            }
        });
    }
    private void checkRFO(HashMap<String,String> json){
        btnFlag = false;
        findViewById(R.id.spin_kit).setVisibility(View.VISIBLE);
        Call<List<RFOUser>> call = RetrofitClient.getInstance().getMyApi().rfoforgotpass(json);
        call.enqueue(new Callback<List<RFOUser>>() {
            @Override
            public void onResponse(Call<List<RFOUser>> call, Response<List<RFOUser>> response) {
                btnFlag = true;
                if (response.isSuccessful()&&response.body().get(0).getDivisionname()!=null) {
                    List<RFOUser> heroList = response.body();
                    rfoUser=heroList.get(0);
                    pref.addString("username",heroList.get(0).getrFOName());
                    id=String.valueOf(heroList.get(0).getrFOId());
                    fcmid=heroList.get(0).getfCMID();
                    findViewById(R.id.spin_kit).setVisibility(View.VISIBLE);
                    passwordReset();
                } else {
                    findViewById(R.id.spin_kit).setVisibility(View.GONE);
                    SnackBarUtils.ErrorSnack(ForgotPassordActivity.this, getResources().getString(R.string.pleasecheckthedetails));
                }
            }
            @Override
            public void onFailure(Call<List<RFOUser>> call, Throwable t) {
                btnFlag = true;
                findViewById(R.id.spin_kit).setVisibility(View.GONE);
                SnackBarUtils.ErrorSnack(ForgotPassordActivity.this, getResources().getString(R.string.servernotresponding));
            }
        });
    }

    private void checkCollector(HashMap<String,String> json){
        Log.i("json",json.toString());
        btnFlag = false;
        findViewById(R.id.spin_kit).setVisibility(View.VISIBLE);
        Call<List<CollectorUser>> call = RetrofitClient.getInstance().getMyApi().collectorForgotpass(json);
        call.enqueue(new Callback<List<CollectorUser>>() {
            @Override
            public void onResponse(Call<List<CollectorUser>> call, Response<List<CollectorUser>> response) {
                btnFlag = true;
                if (response.isSuccessful()&&response.body().get(0).getDivision()!=null) {
                    List<CollectorUser> heroList = response.body();
                    collectorUser=heroList.get(0);
                    pref.addString("username",heroList.get(0).getCollectorName());
                    id=String.valueOf(heroList.get(0).getCid());
                    fcmid=heroList.get(0).getFcmid();
                    findViewById(R.id.spin_kit).setVisibility(View.GONE);
                    passwordReset();
                } else {
                    findViewById(R.id.spin_kit).setVisibility(View.GONE);
                    SnackBarUtils.ErrorSnack(ForgotPassordActivity.this, getResources().getString(R.string.pleasecheckthedetails));
                }
            }
            @Override
            public void onFailure(Call<List<CollectorUser>> call, Throwable t) {
                btnFlag = true;
                findViewById(R.id.spin_kit).setVisibility(View.GONE);
                SnackBarUtils.ErrorSnack(ForgotPassordActivity.this, getResources().getString(R.string.servernotresponding));
            }
        });
    }


    private static Field findField(String name, Class<?> type) {
        for (Field declaredField : type.getDeclaredFields()) {
            if (declaredField.getName().equals(name)) {
                return declaredField;
            }
        }
        if (type.getSuperclass() != null) {
            return findField(name, type.getSuperclass());
        }
        return null;
    }
    private void passwordReset(){
        username.setText("");
        username.clearFocus();
        username.setHint(getResources().getString(R.string.newpassword));
        username.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        username.setCompoundDrawablesWithIntrinsicBounds( R.drawable.vector_lock, 0,0, 0 );

        chooseText.setText(getResources().getText(R.string.resetpassword));

        secondinput.setVisibility(View.GONE);
        rePassword.setVisibility(View.VISIBLE);

        findViewById(R.id.signin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SnackBarUtils.NetworkSnack(ForgotPassordActivity.this)) {
                    if (checks.checkETList(new EditText[]{username, secondinput})) {
                        if (username.getText().toString().equals(rePassword.getText().toString())) {
                            if (btnFlag) {
                                resetPass(id, type);
                            } else {
                                findViewById(R.id.spin_kit).setVisibility(View.GONE);
                                SnackBarUtils.WarningSnack(ForgotPassordActivity.this, getResources().getString(R.string.wait));
                            }
                        } else {
                            findViewById(R.id.spin_kit).setVisibility(View.GONE);
                            SnackBarUtils.ErrorSnack(ForgotPassordActivity.this, getResources().getString(R.string.passdidntmatch));
                        }
                    }
                }
            }
        });
    }

    private void resetPass(String id, final String type){
        findViewById(R.id.spin_kit).setVisibility(View.VISIBLE);
        btnFlag = false;
        HashMap<String,String> json=new HashMap<>();
        json.put("id",id);
        json.put("Password",rePassword.getText().toString());
        json.put("Type",type);
        Call<List<StatusModel>> call = RetrofitClient.getInstance().getMyApi().passReset(json);
        call.enqueue(new Callback<List<StatusModel>>() {
            @Override
            public void onResponse(Call<List<StatusModel>> call, Response<List<StatusModel>> response) {
                btnFlag = true;
                if (response.isSuccessful()&& response.body().get(0).getStatus().equals("Success")) {
                    if (type.equals(Constants.RFO)){
                        pref.saveRFO(rfoUser);
                    }else if (type.equals(Constants.VSS)){
                        pref.saveVSS(vssUser);
                    }else{
                        pref.saveCollector(collectorUser);
                    }
                    pref.addBool("login", true);
                    FirebaseMessaging.getInstance().subscribeToTopic(fcmid).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getApplicationContext(),"Topic Registered",Toast.LENGTH_LONG).show();
                        }
                    });
                    Intent intent = new Intent(getApplicationContext(), Home.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    findViewById(R.id.spin_kit).setVisibility(View.GONE);
                    SnackBarUtils.ErrorSnack(ForgotPassordActivity.this, getResources().getString(R.string.pleasecheckthedetails));
                }
            }
            @Override
            public void onFailure(Call<List<StatusModel>> call, Throwable t) {
                btnFlag = true;
                findViewById(R.id.spin_kit).setVisibility(View.GONE);
                SnackBarUtils.ErrorSnack(ForgotPassordActivity.this, getResources().getString(R.string.servernotresponding));
            }
        });
    }
}