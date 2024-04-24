package com.gisfy.ntfp.Login;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import com.gisfy.ntfp.R;
import com.gisfy.ntfp.Utils.Constants;
import com.gisfy.ntfp.Utils.LocaleHelper;
import com.gisfy.ntfp.Utils.SharedPref;
import com.gisfy.ntfp.Utils.SnackBarUtils;
import com.gisfy.ntfp.Utils.StaticChecks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class Language extends AppCompatActivity {

    private CardView engCard,malCard;
    private RadioButton engRb,malRb;
    private StaticChecks checks;
    private SharedPref pref;
    private String language=null;
    private int backpress=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
        initViews();
        cardSelection(engCard,engRb,new RadioButton[]{malRb},new CardView[]{malCard},Constants.ENGLISH);
        cardSelection(malCard,malRb,new RadioButton[]{engRb},new CardView[]{engCard},Constants.MALAYALAM);
        findViewById(R.id.next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (language!=null){
                    startActivity(new Intent(Language.this,RoleChoose.class));
                }else{
                    SnackBarUtils.WarningSnack(Language.this, getResources().getString(R.string.pleaseselectlang));
                }
            }
        });
    }

    private void initViews() {
        checks=new StaticChecks(this);
        pref=new SharedPref(this);
        engCard=findViewById(R.id.english_card);
        engRb=findViewById(R.id.englishrb);
        malCard=findViewById(R.id.malayalamcard);
        malRb=findViewById(R.id.malayalamrb);
    }

    @Override
    public void onBackPressed() {
        ++backpress;
        if (backpress==2){
            System.exit(0);
            finish();
        }else if (backpress==1){
            Toast.makeText(this, getString(R.string.clickagain), Toast.LENGTH_SHORT).show();
        }else if(backpress==0){
            super.onBackPressed();
        }
    }

    private void cardSelection(final CardView cardView, final RadioButton radioButton, final RadioButton[] rbs, final CardView[] cards, final String s){
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (s.equals(Constants.ENGLISH)||s.equals(Constants.MALAYALAM)){
                    language=s;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        LocaleHelper.updateResources(Language.this, s);
                    }
                    LocaleHelper.updateResourcesLegacy(Language.this, s);
                    pref.addString("language",s);
                    pref.setLanguage(s);
                }
                radioButton.setChecked(true);
                cardView.setCardElevation(20);
                cardView.setCardBackgroundColor(getResources().getColor(R.color.green));

                for (int i=0;i<rbs.length;i++){
                    rbs[i].setChecked(false);
                    cards[i].setCardElevation(1);
                    cards[i].setCardBackgroundColor(getResources().getColor(R.color.white));
                }
            }
        });
    }

}