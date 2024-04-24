package com.gisfy.ntfp.Login;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.gisfy.ntfp.HomePage.Home;
import com.gisfy.ntfp.R;
import com.gisfy.ntfp.Utils.LocaleHelper;
import com.gisfy.ntfp.Utils.SharedPref;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_splash_screen);

        final SharedPref pref=new SharedPref(this);
        if (pref.getString("language").equals("ml")){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                LocaleHelper.updateResources(SplashScreen.this, "ml");
            }
            LocaleHelper.updateResourcesLegacy(SplashScreen.this, "ml");
            pref.addString("language","ml");
        }else{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                LocaleHelper.updateResources(SplashScreen.this, "en");
            }
            LocaleHelper.updateResourcesLegacy(SplashScreen.this, "en");
            pref.addString("language","en");
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (pref.getBool("login")){
                    Intent i = new Intent(SplashScreen.this, Home.class);
                    startActivity(i);
                    finish();
                }else {
                    Intent i = new Intent(SplashScreen.this, Language.class);
                    startActivity(i);
                    finish();
                }
            }
        }, 3000);
    }
}