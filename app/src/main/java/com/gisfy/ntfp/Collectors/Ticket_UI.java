package com.gisfy.ntfp.Collectors;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gisfy.ntfp.Login.Models.CollectorUser;
import com.gisfy.ntfp.R;
import com.gisfy.ntfp.Utils.Constants;
import com.gisfy.ntfp.Utils.SharedPref;

import androidx.appcompat.app.AppCompatActivity;

public class Ticket_UI extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_ui);
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        TextView name=findViewById(R.id.name);
        TextView vssname=findViewById(R.id.vssnameidcard);
        TextView userName=findViewById(R.id.userid);
        TextView spouse=findViewById(R.id.spouse);
        TextView division=findViewById(R.id.division);
        TextView range=findViewById(R.id.range);
        ImageView profile_image=findViewById(R.id.profile_image);
        ImageView statusImage=findViewById(R.id.statusImage);
        SharedPref pref=new SharedPref(Ticket_UI.this);
        CollectorUser user=pref.getCollector();

        if (pref.getString("language").equals(Constants.ENGLISH)){
            profile_image.setImageResource(R.drawable.vanashree_logo_english);
        }else {
            profile_image.setImageResource(R.drawable.vanashreelogo);
        }

        Log.i("test38",user.isPassStatus()+"");
        if (user.isPassStatus()){
            Log.i("test40",user.isPassStatus()+"");

        }else {
            Log.i("test43",user.isPassStatus()+"");
        }
        if (user.isPassStatus()){
            statusImage.setImageResource(R.drawable.approoved);
            String date = user.getPassExpieryDate();
            String[] dateParts = date.split("-");
            String year = dateParts[0];
            String month = dateParts[1];
            String day = dateParts[2];
            String data = day+"-"+ month+"-"+year;
            spouse.setText(data);
        }else{
            statusImage.setImageResource(R.drawable.expired);
            spouse.setText("Expired");
        }
        userName.setText(pref.getString("userId"));
        name.setText(user.getCollectorName());
        vssname.setText(user.getvSSName());

        division.setText(user.getDivision());
        range.setText(user.getRange());

        Glide.with(this).load(pref.getString("ProfileImage"))
                .error(R.drawable.vanashreelogo).into(profile_image);


    }
}