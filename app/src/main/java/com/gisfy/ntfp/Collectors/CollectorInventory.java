package com.gisfy.ntfp.Collectors;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidbuts.multispinnerfilter.KeyPairBoolData;
import com.androidbuts.multispinnerfilter.SingleSpinnerListener;
import com.androidbuts.multispinnerfilter.SingleSpinnerSearch;
import com.gisfy.ntfp.HomePage.NTFPModel;
import com.gisfy.ntfp.Login.Models.CollectorUser;
import com.gisfy.ntfp.Login.Models.VSSUser;
import com.gisfy.ntfp.R;
import com.gisfy.ntfp.SqliteHelper.DBHelper;
import com.gisfy.ntfp.SqliteHelper.Entity.CollectorsModel;
import com.gisfy.ntfp.SqliteHelper.Entity.InventoryEntity;
import com.gisfy.ntfp.SqliteHelper.Entity.InventoryRelation;
import com.gisfy.ntfp.SqliteHelper.Entity.MemberModel;
import com.gisfy.ntfp.SqliteHelper.Entity.NTFP;
import com.gisfy.ntfp.SqliteHelper.NtfpDao;
import com.gisfy.ntfp.SqliteHelper.SynchroniseDatabase;
import com.gisfy.ntfp.Utils.JSONStorage;
import com.gisfy.ntfp.Utils.SharedPref;
import com.gisfy.ntfp.Utils.SnackBarUtils;
import com.gisfy.ntfp.Utils.StaticChecks;
import com.gisfy.ntfp.VSS.Inventory.Inventory;
import com.gisfy.ntfp.VSS.Inventory.add_inventory;
import com.gisfy.ntfp.VSS.Inventory.edit_inventory;
import com.gisfy.ntfp.VSS.Inventory.list_inventory;
import com.gisfy.ntfp.VSS.Payment.Model_payment;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.gisfy.ntfp.SqliteHelper.DBHelper.STOCKSID;
import static com.gisfy.ntfp.SqliteHelper.DBHelper.VSS;

public class CollectorInventory extends AppCompatActivity {

    private TextInputEditText vss,division,range,quantity;
    private TextView date,headd,indication;
    private Spinner measurement;
    private AutoCompleteTextView ntfptype,member,products,collector,vssSelect;
    private Button proceed;
    private StaticChecks checks;
    private NTFP ntfpModel=null;
    boolean flag = true;

    private NTFP.ItemType itemTypeModel =null;
    private MemberModel memberModel = null;
    private ArrayAdapter<NTFP.ItemType> ntfpTypeAdapter = null;
    private ArrayAdapter<MemberModel> memberAdapter=null;

    private String inventoryId="";
    private NtfpDao dao;
    private SharedPref pref;
    boolean spinnerflag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_inventory);
        intiViews();
        pref = new SharedPref(this);

        collector.setText(pref.getCollector().getCollectorName());
        collector.setEnabled(false);
        dao = SynchroniseDatabase.getInstance(this).ntfpDao();



        if (getIntent().hasExtra("uid")) {
            proceed.setText(getString(R.string.update));
            inventoryId = getIntent().getStringExtra("uid");
            InventoryRelation relation = dao.getInventoryFromId(inventoryId);
            ntfptype.setText(relation.getItemType().getMycase(),false);
            itemTypeModel = relation.getItemType();
            Log.i("getNTFP",relation.getNtfp()+"");
            Log.i("getNTFPscientificname",relation.getNtfp().getNTFPscientificname()+"");
            products.setText(relation.getNtfp().getNTFPscientificname()+"",false);
            ntfpModel = relation.getNtfp();
            String memberName;
            if (relation.getMember()!=null) memberName = relation.getMember().getName();
            else memberName = "";
            member.setText(memberName,false);
            memberModel = relation.getMember();
            collector.setText(relation.getCollector().getCollectorName(),false);
            quantity.setText(String.valueOf(relation.getInventory().getQuantity()));
            measurementSpinner(measurement, relation.getInventory().getMeasurements());

        }else{
            Date d = new Date();
            inventoryId = "TRANS-"+new SharedPref(CollectorInventory.this).getCollector().getCollectorName().substring(0,3).toUpperCase()+"-" + d.getDay() + d.getMonth() + d.getMinutes() + d.getSeconds();
            inventoryId = inventoryId.replace(".","").replace(" ","");
        }
        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    if(itemTypeModel!=null){
                            if (!measurement.getSelectedItem().toString().equals("Select Measurement")){
                    if (!TextUtils.isEmpty(quantity.getText().toString())) {
                        String amundBy=(String) measurement.getSelectedItem();

                        if((flag == false) && (amundBy.equals("Kilogram")||amundBy.equals("Gram"))){
                            Toast.makeText(CollectorInventory.this, "Select required Unit first", Toast.LENGTH_SHORT).show();

                        }
                        if((flag == true) && (amundBy.equals("Litre")||amundBy.equals("Millilitre"))){
                            Toast.makeText(CollectorInventory.this, "Select required Unit first", Toast.LENGTH_SHORT).show();

                        }
                        else if(((flag != false) && (amundBy.equals("Kilogram")||amundBy.equals("Gram")))||((flag != true) && (amundBy.equals("Litre")||amundBy.equals("Millilitre")))){
                        try {
                            InventoryEntity entity = new InventoryEntity(
                                    inventoryId,
                                    pref.getCollector().getCid(),
                                    memberModel!=null?memberModel.getMemberId():-1,
                                    itemTypeModel.getItemId(),
                                    ntfpModel.getNid(),
                                    "",
                                    "0",
                                    collector.getText().toString(),
                                    "NA",
                                    measurement.getSelectedItem().toString(),
                                    Double.parseDouble(quantity.getText().toString()),
                                    0.0,
                                    0.0,
                                    date.getText().toString());
                            dao.insertInventory(entity);
                            startActivity(new Intent(CollectorInventory.this, InventoryList.class));
                        } catch (Exception e) {
                            e.printStackTrace();
                            SnackBarUtils.ErrorSnack(CollectorInventory.this, getString(R.string.somethingwentwrong));
                        }}
                    }else { Toast.makeText(CollectorInventory.this, "Please Enter Quantity", Toast.LENGTH_SHORT).show(); }
                    }else { Toast.makeText(CollectorInventory.this, "Please Select Unit Measurement", Toast.LENGTH_SHORT).show(); }
                    }else { Toast.makeText(CollectorInventory.this, "Please Select NTFP Names And Ntfp Type", Toast.LENGTH_SHORT).show(); }
                }

        });
    }

    private void intiViews() {
        findViewById(R.id.calC).setVisibility(View.GONE);
        findViewById(R.id.amountLayout).setVisibility(View.GONE);
        findViewById(R.id.gradeTitle).setVisibility(View.GONE);
        findViewById(R.id.ntfpgrade).setVisibility(View.GONE);
        findViewById(R.id.edit_lose).setVisibility(View.GONE);
        collector = findViewById(R.id.collector_spinner);
        checks=new StaticChecks(this);
        pref=new SharedPref(this);
        member=findViewById(R.id.spinner_member);
        ntfptype=findViewById(R.id.ntfptype);
        quantity=findViewById(R.id.edit_quantity);
        date=findViewById(R.id.date);
        products=findViewById(R.id.spinner_ntfps);
        proceed=findViewById(R.id.add_collector_proceed);
        proceed.setText(getResources().getString(R.string.addinv));
        vss=findViewById(R.id.vss_name);
        vssSelect=findViewById(R.id.vss_spinner);
        range=findViewById(R.id.range_name);
        indication = findViewById(R.id.measurementincation);
        division=findViewById(R.id.division_name);
        measurement=findViewById(R.id.measurement);
        final Calendar c = Calendar.getInstance();
        headd=findViewById(R.id.vss_spinnerHead);
        Date d = c.getTime();
        SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy");
        String date1 = format1.format(d);
        date.setText(date1);
        quantity.setFilters(new InputFilter[]{new CollectorInventory.DecimalDigitsInputFilter(4, 2)});
        vssSelect.setVisibility(View.GONE);
        headd.setVisibility(View.GONE);

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        NtfpDao dao = SynchroniseDatabase.getInstance(this).ntfpDao();

        ArrayAdapter<CollectorsModel> collectorAdapter = new ArrayAdapter<CollectorsModel>(this,R.layout.multiline_spinner_dropdown_item,dao.getAllCollector());
        collector.setAdapter(collectorAdapter);

        Log.i("TEST AT 191",dao.getMemberFromMemberCId(pref.getCollector().getCid()).size()+"//"+pref.getCollector().getCid());
        memberAdapter = new ArrayAdapter<MemberModel>(CollectorInventory.this,R.layout.multiline_spinner_dropdown_item,dao.getMemberFromMemberCId(pref.getCollector().getCid()));
        member.setAdapter(memberAdapter);

        ArrayAdapter<NTFP> ntfpAdapter = new ArrayAdapter<NTFP>(this,R.layout.multiline_spinner_dropdown_item,dao.getAllNTFPs());
        products.setAdapter(ntfpAdapter);


        ntfpTypeAdapter = new ArrayAdapter<>(this, R.layout.multiline_spinner_dropdown_item, dao.getAllNTFPTypes());
        ntfptype.setAdapter(ntfpTypeAdapter);

        SnackBarUtils.initAutoSpinner(new AutoCompleteTextView[]{products,collector,member});
        ntfptype.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (ntfpModel==null){
                    Toast.makeText(CollectorInventory.this, "Select NTFP first", Toast.LENGTH_SHORT).show();
                }else{
                    ntfptype.showDropDown();
                }

            }

        });
        ntfptype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View arg0) {
                if (ntfpModel==null){
                    Toast.makeText(CollectorInventory.this, "Select NTFP first", Toast.LENGTH_SHORT).show();
                }else{
                    ntfptype.showDropDown();

                }
            }
        });


        member.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (memberAdapter!=null){
                    memberModel = memberAdapter.getItem(position);
                }else
                    Toast.makeText(CollectorInventory.this, "select collector first", Toast.LENGTH_SHORT).show();
            }
        });

        products.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ntfpModel = ntfpAdapter.getItem(position);
                if (ntfpModel!=null){
                    ntfpTypeAdapter = new ArrayAdapter<NTFP.ItemType>(CollectorInventory.this,R.layout.multiline_spinner_dropdown_item,dao.getItemTypesFromNid(ntfpModel.getNid()));
                    ntfptype.setAdapter(ntfpTypeAdapter);
                    Log.i("unittttAd",ntfpModel.getUnit().toString()+"")  ;
                    String UNITT = ntfpModel.getUnit();
                    Log.i("unittttUNIT",UNITT+"")  ;
                    if(UNITT.equals("litre") ) {
                        flag = false;
                        indication.setText("Unit of Measurement in Liter*");

                    }else {
                        flag = true;
                        indication.setText("Unit of Measurement in Kilogram*");

                    }
                }
            }
        });

        ntfptype.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (ntfpModel!=null && ntfpTypeAdapter!=null){
                    itemTypeModel = ntfpTypeAdapter.getItem(position);

//                    Log.i("UnitPara382",ntfpModel.getUnit()+"");
//                    if(ntfpModel.getUnit() == "kg") {
//
//
//                        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
//                                getApplicationContext(), R.array.measurementLiter, android.R.layout.simple_spinner_item);
//                        adapter.setDropDownViewResource(R.layout.spinner_layout);
//                        measurement.setAdapter(adapter);
//                    }
//                    else
//                    {
//                        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
//                                getApplicationContext(), R.array.measurementKG, android.R.layout.simple_spinner_item);
//                        adapter.setDropDownViewResource(R.layout.spinner_layout);
//                        measurement.setAdapter(adapter);
//
//
//                    }
                }else
                    Toast.makeText(CollectorInventory.this, "select NTFP first", Toast.LENGTH_SHORT).show();
            }
        });

    }

    class DecimalDigitsInputFilter implements InputFilter {
        private Pattern mPattern;
        DecimalDigitsInputFilter(int digitsBeforeZero, int digitsAfterZero) {
            mPattern = Pattern.compile("[0-9]{0," + (digitsBeforeZero - 1) + "}+((\\.[0-9]{0," + (digitsAfterZero - 1) + "})?)||(\\.)?");
        }
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            Matcher matcher = mPattern.matcher(dest);
            if (!matcher.matches())
                return "";
            return null;
        }
    }

    public boolean checkSpinner(Spinner[] ets){
        boolean flag=true;
        for(Spinner et:ets){
            if (et.getSelectedItemPosition()==0){
                Toast.makeText(CollectorInventory.this,"Please "+et.getSelectedItem().toString() , Toast.LENGTH_SHORT).show();
                flag=false;
                break;
            }
        }
        return flag;
    }
    public void measurementSpinner(Spinner mSpinner, String compareValue) {
        Log.i("Log291",ntfpModel.getUnit()+"");
     //   if(ntfpModel.getUnit()=="kg") {
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.measurementKG, R.layout.spinner_layout);
            adapter.setDropDownViewResource(R.layout.spinner_layout);
            mSpinner.setAdapter(adapter);
            int arg2;
            if (compareValue != null) {
                arg2 = adapter.getPosition(compareValue);
                mSpinner.setSelection(arg2);
                if (arg2 != 0) {
                    spinnerflag = true;
                }
            }
//        }
   }
}