package com.gisfy.ntfp.RFO;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gisfy.ntfp.HomePage.Home;
import com.gisfy.ntfp.Login.Language;
import com.gisfy.ntfp.Login.Models.RFOUser;
import com.gisfy.ntfp.Login.ResetPassword;
import com.gisfy.ntfp.Profile.Profile;
import com.gisfy.ntfp.Profile.ProfileAdapter;
import com.gisfy.ntfp.Profile.ProfileModel;
import com.gisfy.ntfp.R;
import com.gisfy.ntfp.Utils.SharedPref;
import com.gisfy.ntfp.Utils.StaticChecks;

import java.util.ArrayList;
import java.util.List;

public class RFOProfile extends AppCompatActivity {
    private SharedPref pref;
    private RecyclerView recyclerView;
    private final List<ProfileModel> list=new ArrayList<>();
    private String id=null;
    private String type=null;
    private StaticChecks checks;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rfoprofile);
        intiViews();
        RFOUser rfoUser = pref.getRFO();
        id=String.valueOf(rfoUser.getrFOId());
        String getName = pref.getString("username").replace("Range Officer","");
        list.add(new ProfileModel(getString(R.string.division), rfoUser.getDivisionname(), R.drawable.vector_email));
        list.add(new ProfileModel(getString(R.string.range), rfoUser.getRangeName(), R.drawable.vector_email));
        list.add(new ProfileModel(getString(R.string.incharge), rfoUser.getrFOIncharge(), R.drawable.vector_email));
        list.add(new ProfileModel(getString(R.string.phoneno), rfoUser.getrFOInchargeNum(), R.drawable.vector_email));
        list.add(new ProfileModel(getString(R.string.name), "Range Officer "+ getName, R.drawable.vector_email));
        ProfileAdapter adapter=new ProfileAdapter( RFOProfile.this,list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true));
        recyclerView.setAdapter(adapter);

        findViewById(R.id.logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                new DBHelper(Profile.this).deleteAllData();


                AlertDialog.Builder builder = new AlertDialog.Builder(RFOProfile.this);
                builder.setTitle(R.string.Are_you_sure_do_you_want_to_Logout)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                new SharedPref(RFOProfile.this).clearPref();
                                Intent intent = new Intent(getApplicationContext(), Language.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .show();


            }
        });
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Home.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        findViewById(R.id.resetpassword).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ResetPassword.class);
                startActivity(intent);
            }
        });


    }
    private void intiViews() {
        checks=new StaticChecks(this);
        pref=new SharedPref(this);
        recyclerView=findViewById(R.id.profile_recycle_rfo);
        type=pref.getString("type");
    }
}