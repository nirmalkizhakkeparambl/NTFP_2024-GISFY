package com.gisfy.ntfp.VSS.Inventory;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidbuts.multispinnerfilter.KeyPairBoolData;
import com.androidbuts.multispinnerfilter.MultiSpinnerListener;
import com.androidbuts.multispinnerfilter.SingleSpinnerListener;
import com.androidbuts.multispinnerfilter.SingleSpinnerSearch;
import com.gisfy.ntfp.Collectors.CollectorInventory;
import com.gisfy.ntfp.Collectors.CollectorStockModel;
import com.gisfy.ntfp.HomePage.NTFPModel;
import com.gisfy.ntfp.Login.Models.VSSUser;
import com.gisfy.ntfp.R;
import com.gisfy.ntfp.RFO.Models.Model_inventory;
import com.gisfy.ntfp.SqliteHelper.DBHelper;
import com.gisfy.ntfp.SqliteHelper.Entity.CollectorsModel;
import com.gisfy.ntfp.SqliteHelper.Entity.MemberModel;
import com.gisfy.ntfp.SqliteHelper.Entity.NTFP;
import com.gisfy.ntfp.SqliteHelper.NtfpDao;
import com.gisfy.ntfp.SqliteHelper.SynchroniseDatabase;
import com.gisfy.ntfp.Utils.JSONStorage;
import com.gisfy.ntfp.Utils.SharedPref;
import com.gisfy.ntfp.Utils.SnackBarUtils;
import com.gisfy.ntfp.Utils.StaticChecks;
import com.gisfy.ntfp.VSS.Payment.Model_payment;
import com.gisfy.ntfp.VSS.RequestForm.StocksModel;
import com.gisfy.ntfp.VSS.Shipment.edit_shipment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class edit_inventory extends AppCompatActivity {

    private TextInputEditText vss,division,range,quantity , loseQ , amountPaid;
    private TextView date;
    private Spinner ntfpgrade,measurement;
    private AutoCompleteTextView ntfptype,member,products,collector,vssSelect;
    private Button proceed,AmoundCalc;
    private StaticChecks checks;
    private NTFP ntfpModel=null;
    private CollectorsModel collectorModel = null;
    private NTFP.ItemType itemTypeModel =null;
    private MemberModel memberModel = null;
    private StocksModel stmodel =null;
    private ArrayAdapter<NTFP.ItemType> ntfpTypeAdapter = null;
    private ArrayAdapter<MemberModel> memberAdapter=null;
    private String uuid="";
    private int mmid ;
    public  String vssnamepage;

    private DBHelper db;
    private CollectorStockModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_inventory);
        intiViews();
        db = new DBHelper(this);
        Intent intentget= getIntent();
        Bundle b = intentget.getExtras();
        this.model = model;
        Log.i("MMMMeeee","");

        quantity.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(4, 2)});

        if(b!=null && b.containsKey("collecternameget")) {

            String collecternameget =(String) b.get("collecternameget");
            String ntfpnameget =(String) b.get("ntfpnameget");
            String ntfptypeget =(String) b.get("ntfptypeget");

            quantity.setText((String) b.get("ntfpuantityget"));
            date.setText((String) b.get("dataget"));
            String intentMember = "";
            if (getIntent().hasExtra("memberId"))

                Log.i("memberIDD",intentMember);
                mmid = Integer.parseInt(getIntent().getStringExtra("memberId"));

        } else if(getIntent().hasExtra("uid")){
            uuid = getIntent().getStringExtra("uid");
            new populateData().execute();
            Log.i("CCCCIDD00",uuid+"");
            if(uuid ==""){
                if(collectorModel!=null){
                   uuid =  String.valueOf(collectorModel.getCid());

                 Log.i("CCCCIDD",uuid+"");
                }

            }
            if(member.getText().toString().length()!=0){
                Log.d("MMMM","");


                mmid= model.getMemberId();
                memberModel.setMemberId(mmid);


            }

        }
        proceed.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                Log.i("REMESHHH","");
                if((vssSelect.getText().toString().equals(vssnamepage))){
                    Log.i("MMMM@@@@@",mmid+"");

                if (collectorModel!=null && itemTypeModel!=null && ntfpModel!=null)
                    if (checks.checkETList(new TextInputEditText[]{vss,division,range,quantity})) {
                        try {
                            Date d = new Date();
                            Inventory model = new Inventory(
                                    mmid,
                                    uuid,
                                    vss.getText().toString(),
                                    division.getText().toString(),
                                    range.getText().toString(),
                                    String.valueOf(collectorModel.getCid()),
                                    String.valueOf(ntfpModel.getNid()),
                                    measurement.getSelectedItem().toString(),
                                    quantity.getText().toString(),
                                    loseQ.getText().toString(),
                                    date.getText().toString(),
                                    amountPaid.getText().toString(),
                                    itemTypeModel.getMycase()+"-"+itemTypeModel.getItemId(),
                                    ntfpgrade.getSelectedItemPosition() != 0 ? ntfpgrade.getSelectedItem().toString() : "",
                                    0,
                                    0,
                                    0);
                            db.insertData(model);
                            Model_payment modelPayment = new Model_payment("TRANS" + d.getDay() + d.getMonth() + d.getYear() + d.getHours() + d.getMinutes() + d.getSeconds(),
                                    vss.getText().toString(), division.getText().toString(),
                                    range.getText().toString(), amountPaid.getText().toString(), date.getText().toString(), "", "",
                                    "Made", collectorModel.getCollectorName(), ntfpModel.getNTFPscientificname(), measurement.getSelectedItem().toString(),
                                    quantity.getText().toString(), 0, 0);
                            db.insertData(modelPayment);
                            startActivity(new Intent(edit_inventory.this, list_inventory.class));
                        } catch (Exception e) {
                            String exc = e.getLocalizedMessage();
                            Log.i("ERORREditP1",exc+"");
                            e.printStackTrace();
                            SnackBarUtils.ErrorSnack(edit_inventory.this, getString(R.string.somethingwentwrong));
                        }
                    }
            }else {
                    Log.i("MMMM@@@@@",mmid+"");
                    if(collector.getText().length()!=0)
                    if (checks.checkETList(new TextInputEditText[]{vss,division,range,quantity})) {
                        try {
                            Date d = new Date();
                            Inventory model = new Inventory(
                                    00,
                                    uuid,
                                    vss.getText().toString(),
                                    division.getText().toString(),
                                    range.getText().toString(),
                                    collector.getText().toString(),
                                    String.valueOf(ntfpModel.getNid()),
                                    measurement.getSelectedItem().toString(),
                                    quantity.getText().toString(),
                                    loseQ.getText().toString(),
                                    date.getText().toString(),
                                    amountPaid.getText().toString(),
                                    itemTypeModel.getMycase()+"-"+itemTypeModel.getItemId(),
                                    ntfpgrade.getSelectedItemPosition() != 0 ? ntfpgrade.getSelectedItem().toString() : "",
                                    0,
                                    0,
                                    0);
                            db.insertData(model);
                            Model_payment modelPayment = new Model_payment("TRANS" + d.getDay() + d.getMonth() + d.getYear() + d.getHours() + d.getMinutes() + d.getSeconds(),
                                    vss.getText().toString(), division.getText().toString(),
                                    range.getText().toString(), amountPaid.getText().toString(), date.getText().toString(), "", "",
                                    "Made",collector.getText().toString(), ntfpModel.getNTFPscientificname(), measurement.getSelectedItem().toString(),
                                    quantity.getText().toString(), 0, 0);
                            db.insertData(modelPayment);
                            startActivity(new Intent(edit_inventory.this, list_inventory.class));
                        } catch (Exception e) {
                            String exc = e.getLocalizedMessage();
                            Log.i("ERORREditP",exc+"");
                            e.printStackTrace();
                            SnackBarUtils.ErrorSnack(edit_inventory.this, getString(R.string.somethingwentwrong));
                        }
                    }
                }}
        });
    }



    private void intiViews() {
        collector = findViewById(R.id.collector_spinner);
        checks=new StaticChecks(this);
        member=findViewById(R.id.spinner_member);
        ntfptype=findViewById(R.id.ntfptype);
        ntfpgrade=findViewById(R.id.ntfpgrade);
        quantity=findViewById(R.id.edit_quantity);
        loseQ=findViewById(R.id.edit_lose);
        amountPaid= findViewById(R.id.amountPaid);
        date=findViewById(R.id.date);
        products = findViewById(R.id.spinner_ntfps);
        AmoundCalc = findViewById(R.id.calC);
        proceed = findViewById(R.id.add_collector_proceed);
        proceed.setText(getResources().getString(R.string.addinv));
        vss = findViewById(R.id.vss_name);
        vssSelect=findViewById(R.id.vss_spinner);
        range=findViewById(R.id.range_name);
        division=findViewById(R.id.division_name);
        measurement=findViewById(R.id.measurement);
        checks.setValues(new TextInputEditText[]{vss,division,range});
        final Calendar c = Calendar.getInstance();
        Date d = c.getTime();
        SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy");
        String date1 = format1.format(d);
        date.setText(date1);
        quantity.setFilters(new InputFilter[]{new edit_inventory.DecimalDigitsInputFilter(4, 2)});
        VSSUser user = new SharedPref(edit_inventory.this).getVSS();
        Log.i("VSSSnme",user.getvSSName()+"");
        vssSelect.setText(user.getvSSName());
        vssnamepage = user.getvSSName();


        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        NtfpDao dao = SynchroniseDatabase.getInstance(this).ntfpDao();

//  if((vssselect.getText().equals("Current VSS")) || vssselect.getText().equals("")){

    ArrayAdapter<CollectorsModel> collectorAdapter = new ArrayAdapter<CollectorsModel>(this,R.layout.multiline_spinner_dropdown_item,dao.getAllCollector());
    collector.setAdapter(collectorAdapter);



        ArrayAdapter<NTFP> ntfpAdapter = new ArrayAdapter<NTFP>(this,R.layout.multiline_spinner_dropdown_item,dao.getAllNTFPs());
        products.setAdapter(ntfpAdapter);

        ntfpTypeAdapter = new ArrayAdapter<>(this, R.layout.multiline_spinner_dropdown_item, dao.getAllNTFPTypes());
        ntfptype.setAdapter(ntfpTypeAdapter);

        member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View arg0) {
                if (collectorModel==null){
                    Toast.makeText(edit_inventory.this, "Select collector first", Toast.LENGTH_SHORT).show();
                }else{

                    member.showDropDown();
                    Log.i("MMMMMMM","");
                }

            }
        });
        collector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View arg0) {
                collector.showDropDown();
            }
        });
        products.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View arg0) {
                products.showDropDown();
            }
        });
        ntfptype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View arg0) {
                if (ntfpModel==null){
                    Toast.makeText(edit_inventory.this, "Select NTFP first", Toast.LENGTH_SHORT).show();
                }else{
                    ntfptype.showDropDown();
                }
            }
        });



        collector.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                collectorModel = collectorAdapter.getItem(position);
                if (collectorModel!=null){
                    memberAdapter = new ArrayAdapter<MemberModel>(edit_inventory.this,R.layout.multiline_spinner_dropdown_item,dao.getMemberFromMemberCId(collectorModel.getCid()));
                    member.setAdapter(memberAdapter);
                }
            }
        });

        member.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (collectorModel!=null && memberAdapter!=null){
                    memberModel = memberAdapter.getItem(position);
                }else
                    Toast.makeText(edit_inventory.this, "select collector first", Toast.LENGTH_SHORT).show();
            }
        });

        products.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ntfpModel = ntfpAdapter.getItem(position);
                if (ntfpModel!=null){
                    ntfpTypeAdapter = new ArrayAdapter<NTFP.ItemType>(edit_inventory.this,R.layout.multiline_spinner_dropdown_item,dao.getItemTypesFromNid(ntfpModel.getNid()));
                    ntfptype.setAdapter(ntfpTypeAdapter);
                }
            }
        });

        ntfptype.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (ntfpModel!=null && ntfpTypeAdapter!=null){
                    itemTypeModel = ntfpTypeAdapter.getItem(position);
                   Log.i("unitttt",ntfpModel.getUnit().toString()+"")  ;
                }else
                    Toast.makeText(edit_inventory.this, "select NTFP first", Toast.LENGTH_SHORT).show();
            }
        });

//        quantity.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (itemTypeModel==null && ntfpgrade.getSelectedItemPosition()<1)
//                    Toast.makeText(edit_inventory.this, "Select type and grade first", Toast.LENGTH_SHORT).show();
//                else{
//                    double basePrice;
//                    if (ntfpgrade.getSelectedItemPosition()==1)
//                        basePrice = itemTypeModel.getGrade1Price();
//                    else if (ntfpgrade.getSelectedItemPosition() == 2)
//                        basePrice = itemTypeModel.getGrade2Price();
//                    else
//                        basePrice = itemTypeModel.getGrade3Price();
//
//                    if (quantity.getText().length()>0)
//                        amountPaid.setText(String.valueOf(Double.parseDouble(quantity.getText().toString())*basePrice));
//                    else
//                        amountPaid.setText("");
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });

        AmoundCalc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (itemTypeModel!=null && ntfpgrade.getSelectedItemPosition()>0)
                {
                    double basePrice;
                    if (ntfpgrade.getSelectedItemPosition()==1)
                        basePrice = itemTypeModel.getGrade1Price();
                    else if (ntfpgrade.getSelectedItemPosition() == 2)
                        basePrice = itemTypeModel.getGrade2Price();
                    else
                        basePrice = itemTypeModel.getGrade3Price();


                    String quntityM=quantity.getText().toString();
                    String loseAmound = loseQ.getText().toString();
                    if (quntityM.length()>0) {
                        quntityM = String.valueOf(Double.parseDouble(quntityM) - Double.parseDouble(loseAmound));


                        String amundBy=(String) measurement.getSelectedItem();
                        Log.i("getMEss",amundBy.toString()+"");
                        if(amundBy.equals("kilogram")){
                            amountPaid.setText(String.valueOf(Double.parseDouble(quntityM) * basePrice));
                        }else if (amundBy.equals("Gram")){
                            String amound = String.valueOf(Double.parseDouble(quntityM) * basePrice);
                            float fi=(Float.parseFloat(amound)/1000);

                            amountPaid.setText(String.valueOf(fi));
                        }else if(amundBy.equals("Liter")){
                            amountPaid.setText(String.valueOf(Double.parseDouble(quntityM) * basePrice));
                        }else if(amundBy.equals("Milliliter")){
                            String amound = String.valueOf(Double.parseDouble(quntityM) * basePrice);
                            float fi=(Float.parseFloat(amound)/1000);

                            amountPaid.setText(String.valueOf(fi));
                        }

                    }else{
                        amountPaid.setText("");

                    }
                }
                else{
                    Toast.makeText(edit_inventory.this, "Select type and grade first", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }


    private class populateData extends AsyncTask<String,String,String> {
        ProgressDialog pd;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd=new ProgressDialog(edit_inventory.this);
            pd.setMessage("Loading...");
            pd.dismiss();
        }

        @Override
        protected String doInBackground(String... strings) {
            String uid=getIntent().getStringExtra("uid");
            try {
                final Inventory model=db.getAllDataFromInventoriesWhere("uid='"+uid+"'").get(0);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        NtfpDao dao = SynchroniseDatabase.getInstance(edit_inventory.this).ntfpDao();
//                        uuid=model.getUid();
//                        vss.setText(model.getVss());
//                        range.setText(model.getRange());
//                        division.setText(model.getDivision());
//
//                        memberModel = dao.getMemberFromMemberId(model.getMemberId());
//                        ntfpModel = dao.getNTFPFromNId(model.getNtfp());
//
//                        ArrayAdapter<String> stateadapter = new ArrayAdapter<String>(edit_inventory.this,R.layout.support_simple_spinner_dropdown_item,getResources().getStringArray(R.array.measurement));
//                        measurement.setAdapter(stateadapter);
//                        measurement.setSelection(stateadapter.getPosition(model.getUnit()));
//
//                        quantity.setText(model.getQuantity());
//                        date.setText(model.getDate());
//                        transitstate=model.getTransit();
//                        ntfpgrade.setSelection(ntfpGradeAdapter.getPosition(model.getNtfpGrade()));
//                        payment=model.getPayment();
//                        amountPaid.setText(model.getAmount());
//                        initSpinner(model);
//                        initNTFPSpinner(model.getNtfp(),model.getNtfpType());
//                        amountPaid.setText(model.getAmount());
//                        quantity.addTextChangedListener(new TextWatcher() {
//                            @Override
//                            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
//                                Log.i("onTextChanged",quantity.getText().toString());
//                            }
//                            @Override
//                            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
//                                Log.i("beforeTextChanged",quantity.getText().toString());
//                            }
//                            @Override
//                            public void afterTextChanged(Editable arg0) {
//                                Log.i("afterTextChanged",quantity.getText().toString()+"//"+amount);
//                                try {
//                                    amountPaid.setText(String.valueOf(amount*
//                                            Integer.parseInt(quantity.getText().toString())));
//                                } catch (NumberFormatException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        });
//                        int memberPos = -1;
//                        List<MemberModel> memberModels = SynchroniseDatabase.getInstance(edit_inventory.this).ntfpDao().getAllMembers();
//                        for (MemberModel memberModel : memberModels){
//                            memberPos++;
//                            if (memberModel.getMemberId() == model.getMemberId()){
//                                break;
//                            }
//                        }
//                        ArrayAdapter<MemberModel> memberAdapter = new ArrayAdapter<MemberModel>(edit_inventory.this,R.layout.multiline_spinner_dropdown_item,memberModels);
//                        member.setAdapter(memberAdapter);
//                        if (memberPos!=-1)
//                            member.setSelection(memberPos);
////                        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
////                                edit_inventory.this, android.R.layout.simple_spinner_item, R.array.ntfp_grade);
////                        spinnerArrayAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
////                        ntfptype.setAdapter(spinnerArrayAdapter);

                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                return getString(R.string.foundsomeissues);
            }
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (!s.equals(""))
                SnackBarUtils.ErrorSnack(edit_inventory.this,s);
            pd.dismiss();
        }
    }
    private class NTFPTask extends AsyncTask<String,String,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            findViewById(R.id.spin_kit).setVisibility(View.VISIBLE);
        }
        @Override
        protected String doInBackground(String... strings) {
            try {
//                VSSUser user=pref.getVSS();
                JSONObject json=new JSONObject();
//                json.put("DivisionId",user.getDivisionId()+"");
//                json.put("RangeId",user.getRangeId()+"");
//                json.put("VSSId",user.getVid()+"");
                Log.i("editinventory",json.toString());
                OkHttpClient client = new OkHttpClient().newBuilder()
                        .build();
                MediaType mediaType = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(mediaType, json.toString());
                Request request = new Request.Builder()
                        .url("http://13.127.166.242/NTFPAPI/API/NTFPList")
                        .method("POST", body)
                        .addHeader("Content-Type", "application/json")
                        .build();
                okhttp3.Response response = client.newCall(request).execute();
                JSONStorage.create(edit_inventory.this,"NTFP.json",response.body().string());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
//            findViewById(R.id.spin_kit).setVisibility(View.GONE);

        }
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
                Toast.makeText(edit_inventory.this,"Please "+et.getSelectedItem().toString() , Toast.LENGTH_SHORT).show();
                flag=false;
                break;
            }
        }
        return flag;
    }

}