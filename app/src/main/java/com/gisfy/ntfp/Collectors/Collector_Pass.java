package com.gisfy.ntfp.Collectors;

import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gisfy.ntfp.Login.Models.CollectorUser;
import com.gisfy.ntfp.R;
import com.gisfy.ntfp.Utils.SharedPref;
import com.gisfy.ntfp.Utils.StaticChecks;

import androidx.appcompat.app.AppCompatActivity;

public class Collector_Pass extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collector_pass);
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        TextView sNo=findViewById(R.id.textView6);
        TextView nameAddress=findViewById(R.id.nameaddress);
        TextView where=findViewById(R.id.where);
        TextView date=findViewById(R.id.date);
        ImageView statusImage=findViewById(R.id.statusImage);

        SharedPref pref=new SharedPref(this);
        CollectorUser user=pref.getCollector();

        sNo.setText(user.getCid()+"");
        nameAddress.setText(user.getCollectorName()+"\n"+user.getvSSName());
        if (user.isPassStatus()){
            date.setText(user.getPassApprovedDate()+"\n"+user.getPassExpieryDate());
            statusImage.setImageResource(R.drawable.approoved);
        }else{
            date.setText(getString(R.string.expired));
            statusImage.setImageResource(R.drawable.expired);
        }

        where.setText(user.getvSSName());
    }

    public void generatePDF(View view) {
        new StaticChecks(this).showSnackBar("Coming Soon...");
    }
}