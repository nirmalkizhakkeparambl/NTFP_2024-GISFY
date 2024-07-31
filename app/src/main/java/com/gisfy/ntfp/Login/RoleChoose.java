package com.gisfy.ntfp.Login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;

import com.gisfy.ntfp.R;
import com.gisfy.ntfp.Utils.Constants;
import com.gisfy.ntfp.Utils.SharedPref;
import com.gisfy.ntfp.Utils.SnackBarUtils;
import com.gisfy.ntfp.Utils.StaticChecks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class RoleChoose extends AppCompatActivity {

    private CardView collector_card,rfo_card,vss_card;
    private RadioButton collector_rb,rfo_rb,vss_rb;
    private String type=null;
    private SharedPref pref;
    private StaticChecks checks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role_choose);
        initViews();
        cardSelection(collector_card,collector_rb,new RadioButton[]{vss_rb,rfo_rb},new CardView[]{rfo_card,vss_card}, Constants.COLLECTORS);
        cardSelection(rfo_card,rfo_rb,new RadioButton[]{collector_rb,vss_rb},new CardView[]{collector_card,vss_card},Constants.RFO);
        cardSelection(vss_card,vss_rb,new RadioButton[]{collector_rb,rfo_rb},new CardView[]{rfo_card,collector_card},Constants.VSS);

        findViewById(R.id.next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (type!=null){
                    pref.addString("type",type);
                    Log.d("TYPE",type);
                    startActivity(new Intent(RoleChoose.this,LoginActivity.class));
                }else{
                    SnackBarUtils.WarningSnack(RoleChoose.this, getResources().getString(R.string.selectrole));
                }
            }
        });
        findViewById(R.id.prev).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RoleChoose.this,Language.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
            }
        });
    }

    private void initViews() {
        collector_card=findViewById(R.id.collector_card_view);
        collector_rb=findViewById(R.id.collector_rb);
        vss_card=findViewById(R.id.vss_card_view);
        vss_rb=findViewById(R.id.vss_rb);
        rfo_card=findViewById(R.id.rfo_card_view);
        rfo_rb=findViewById(R.id.rfo_rb);
        checks=new StaticChecks(this);
        pref=new SharedPref(this);
    }
    private void cardSelection(final CardView cardView, final RadioButton radioButton, final RadioButton[] rbs, final CardView[] cards, final String s){
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type=s;
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