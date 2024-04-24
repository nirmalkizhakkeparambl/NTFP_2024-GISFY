package com.gisfy.ntfp.Login;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gisfy.ntfp.API.RetrofitClient;
import com.gisfy.ntfp.HomePage.Home;
import com.gisfy.ntfp.Login.Models.CollectorUser;
import com.gisfy.ntfp.Login.Models.RFOUser;
import com.gisfy.ntfp.Login.Models.VSSUser;
import com.gisfy.ntfp.R;
import com.gisfy.ntfp.SqliteHelper.Entity.CollectorsModel;
import com.gisfy.ntfp.SqliteHelper.Entity.MemberModel;
import com.gisfy.ntfp.SqliteHelper.SynchroniseDatabase;
import com.gisfy.ntfp.Utils.Constants;
import com.gisfy.ntfp.Utils.CustomEditText;
import com.gisfy.ntfp.Utils.DrawableClickListener;
import com.gisfy.ntfp.Utils.SharedPref;
import com.gisfy.ntfp.Utils.SnackBarUtils;
import com.gisfy.ntfp.Utils.StaticChecks;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class LoginActivity extends AppCompatActivity {
    private Button signin;
    private EditText username,password;
//    private CustomEditText password;
    private StaticChecks checks;
    private SharedPref pref;
    private boolean btnFlag=false;
    private String type=null;
    private boolean passwordShown=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
        FirebaseApp.initializeApp(this);
        findViewById(R.id.changerole).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,RoleChoose.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.alert).setVisibility(View.GONE);
                if (SnackBarUtils.NetworkSnack(LoginActivity.this)) {
                    if (checks.checkETList(new EditText[]{username, password})) {
                        if (!btnFlag) {
                            getUsers();
                        } else {
                            SnackBarUtils.WarningSnack(LoginActivity.this, getResources().getString(R.string.wait));
                        }
                    }
                }
            }
        });
        findViewById(R.id.forgotpass).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,ForgotPassordActivity.class));
            }
        });

        password.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (password.getRight() - password.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        if (passwordShown) {
                            passwordShown = false;
                            // 129 is obtained by bitwise ORing InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD
                            password.setInputType(129);

                            // Need to call following as the font is changed to mono-space by default for password fields
                            password.setTypeface(Typeface.SANS_SERIF);
                            password.setCompoundDrawablesWithIntrinsicBounds(R.drawable.vector_lock, 0, R.drawable.ic_baseline_visibility_24, 0); // This is lock icon
                        } else {
                            passwordShown = true;
                            password.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);

                            password.setCompoundDrawablesWithIntrinsicBounds(R.drawable.vector_lock, 0, R.drawable.ic_baseline_visibility_off_24, 0); // Unlock icon
                        }

                        return true;
                    }
                }
                return false;
            }
        });
    }

    private void initViews() {
        signin=findViewById(R.id.signin);
        username=findViewById(R.id.email);
        pref=new SharedPref(this);
        type=pref.getString("type");
        Log.d("TTTYPE",type);

        if(type.trim() == "VSS"){
            username.setHint(getString(R.string.userid));
        }else if(type.trim() == "RFO"){
            username.setHint(getString(R.string.userid));
        }else  username.setHint(getString(R.string.phoneno));
        password=findViewById(R.id.password);
        checks=new StaticChecks(this);


        TextView chooseText = findViewById(R.id.choosetext);
        chooseText.setText(getString(R.string.login));
    }
    private void getUsers() {
        findViewById(R.id.spin_kit).setVisibility(View.VISIBLE);
        btnFlag=true;
        HashMap<String,String> json=new HashMap<>();
        json.put("UserName",username.getText().toString());
        json.put("Password",password.getText().toString());
        Log.i("DEBUGGING REQUESTBODY",json.toString());
        switch (type) {
            case Constants.VSS: {


                Call<List<VSSUser>> call = RetrofitClient.getInstance().getMyApi().getVSSUser(json);
                Log.i("DEBUGGING CALL",call.toString());
                call.enqueue(new Callback<List<VSSUser>>() {
                    @Override
                    public void onResponse(Call<List<VSSUser>> call, Response<List<VSSUser>> response) {
                        btnFlag = false;
                        Log.i("DEBUGGIN RESPONSE",response.message()+"//"+response.body().size());
                        if (response.isSuccessful()&&response.body().get(0).getDivisionname()!=null) {
                            List<VSSUser> heroList = response.body();
                            pref.saveVSS(heroList.get(0));
                            pref.addString("Role","VSS");
                            pref.addInt("RangeId",heroList.get(0).getRangeId());
                            pref.addString("username",heroList.get(0).getvSSName());
                            pref.addBool("login", true);
                            pref.addString("ProfileImage",getString(R.string.profileURL)+heroList.get(0).getImage());
                            FirebaseMessaging.getInstance().subscribeToTopic(heroList.get(0).getfCMID()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                }
                            });
                            findViewById(R.id.spin_kit).setVisibility(View.GONE);
                            Intent intent = new Intent(getApplicationContext(), SynchronizeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            findViewById(R.id.spin_kit).setVisibility(View.GONE);
                            SnackBarUtils.ErrorSnack(LoginActivity.this, getResources().getString(R.string.recheckpass));
                        }
                    }
                    @Override
                    public void onFailure(Call<List<VSSUser>> call, Throwable t) {
                        btnFlag = false;
                        findViewById(R.id.spin_kit).setVisibility(View.GONE);
                        SnackBarUtils.ErrorSnack(LoginActivity.this, getResources().getString(R.string.servernotresponding));
                    }
                });
                break;
            }
            case Constants.RFO: {

                Call<List<RFOUser>> call = RetrofitClient.getInstance().getMyApi().getRFOUser(json);
                call.enqueue(new Callback<List<RFOUser>>() {
                    @Override
                    public void onResponse(Call<List<RFOUser>> call, Response<List<RFOUser>> response) {
                        btnFlag = false;
                        if (response.isSuccessful()&&response.body().get(0).getDivisionname()!=null) {
                            List<RFOUser> heroList = response.body();
                            pref.saveRFO(heroList.get(0));
                            pref.addBool("login", true);
                            pref.addString("Role","RFO");
                            pref.addString("username",heroList.get(0).getrFOName());
                            pref.addString("ProfileImage",getString(R.string.profileURL)+heroList.get(0).getImage());
                            FirebaseMessaging.getInstance().subscribeToTopic(heroList.get(0).getfCMID()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                }
                            });
                            findViewById(R.id.spin_kit).setVisibility(View.GONE);
                            Intent intent = new Intent(getApplicationContext(), Home.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            findViewById(R.id.spin_kit).setVisibility(View.GONE);
                            SnackBarUtils.ErrorSnack(LoginActivity.this, getResources().getString(R.string.recheckpass));
                        }
                    }

                    @Override
                    public void onFailure(Call<List<RFOUser>> call, Throwable t) {
                        btnFlag = false;
                        findViewById(R.id.spin_kit).setVisibility(View.GONE);
                        SnackBarUtils.ErrorSnack(LoginActivity.this, getResources().getString(R.string.servernotresponding));
                    }
                });
                break;
            }
            case Constants.COLLECTORS:
                Call<List<CollectorUser>> call = RetrofitClient.getInstance().getMyApi().getCollectorUser(json);
                call.enqueue(new Callback<List<CollectorUser>>() {
                    @Override
                    public void onResponse(Call<List<CollectorUser>> call, Response<List<CollectorUser>> response) {
                        btnFlag = false;
                        if (response.isSuccessful()&&response.body().get(0).getCollectorName()!=null) {
                            List<CollectorUser> heroList = response.body();
                            pref.addString("Role","Collector");
                            setupMemberData(heroList.get(0));
                        } else {
                            findViewById(R.id.spin_kit).setVisibility(View.GONE);
                            SnackBarUtils.ErrorSnack(LoginActivity.this, getResources().getString(R.string.recheckpass));
                        }
                    }
                    @Override
                    public void onFailure(Call<List<CollectorUser>> call, Throwable t) {
                        btnFlag = false;
                        findViewById(R.id.spin_kit).setVisibility(View.GONE);
                        SnackBarUtils.ErrorSnack(LoginActivity.this, getResources().getString(R.string.servernotresponding));
                    }
                });
                break;
        }
    }


    public void setupMemberData(CollectorUser user){
        HashMap<String,String> json = new HashMap<>();
        json.put("DivisionId", String.valueOf(user.getDivisionId()));
        json.put("RangeId", String.valueOf(user.getRangeId()));
        json.put("VSSId", String.valueOf(user.getvSSId()));
        Log.i("Line243membersdata",json.toString());
        Call<List<CollectorsModel>> call = RetrofitClient.getInstance().getMyApi().getCollectors(json);
        call.enqueue(new Callback<List<CollectorsModel>>() {
            @Override
            public void onResponse(Call<List<CollectorsModel>> call, Response<List<CollectorsModel>> response) {
                btnFlag = false;
                if (response.isSuccessful()&&response.body().get(0).getCid()!=0) {
                    Log.i("Line 2430",response.body().get(0).toString());
                    List<CollectorsModel> memberList = response.body();
                    SynchroniseDatabase database = SynchroniseDatabase.getInstance(LoginActivity.this);
                    database.ntfpDao().insertAllCollector(memberList);
                    for (CollectorsModel model:memberList){
                        database.ntfpDao().insertAllMembers(model.getMember());
                    }
                    pref.saveCollector(user);
                    pref.addBool("login", true);
                    pref.addString("username",user.getCollectorName());
                    pref.addString("userId",json.get("UserName"));
                    pref.addString("ProfileImage",getString(R.string.profileURL)+user.getImage());
                    FirebaseMessaging.getInstance().subscribeToTopic(user.getFcmid()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                        }
                    });
                    findViewById(R.id.spin_kit).setVisibility(View.GONE);
                    Intent intent = new Intent(getApplicationContext(), SynchronizeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    findViewById(R.id.spin_kit).setVisibility(View.GONE);
                    SnackBarUtils.ErrorSnack(LoginActivity.this, getResources().getString(R.string.unabletofetch));
                }
            }
            @Override
            public void onFailure(Call<List<CollectorsModel>> call, Throwable t) {
                btnFlag = false;
                findViewById(R.id.spin_kit).setVisibility(View.GONE);
                SnackBarUtils.ErrorSnack(LoginActivity.this, getResources().getString(R.string.servernotresponding));
            }
        });
    }
}