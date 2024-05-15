package com.gisfy.ntfp.VSS.Collectors;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidbuts.multispinnerfilter.KeyPairBoolData;
import com.androidbuts.multispinnerfilter.MultiSpinnerListener;
import com.androidbuts.multispinnerfilter.MultiSpinnerSearch;
import com.gisfy.ntfp.Login.Models.VSSUser;
import com.gisfy.ntfp.R;
import com.gisfy.ntfp.SqliteHelper.DBHelper;
import com.gisfy.ntfp.SqliteHelper.NtfpDao;
import com.gisfy.ntfp.SqliteHelper.SynchroniseDatabase;
import com.gisfy.ntfp.Utils.JSONStorage;
import com.gisfy.ntfp.Utils.SharedPref;
import com.gisfy.ntfp.Utils.SnackBarUtils;
import com.gisfy.ntfp.Utils.StaticChecks;
import com.gisfy.ntfp.VSS.Collectors.FamilyDetails.CollectorsFamilyDetails;
import com.gisfy.ntfp.VSS.Collectors.FamilyDetails.FamilyData;
import com.gisfy.ntfp.VSS.Collectors.FamilyDetails.Family_adapter;
import com.gisfy.ntfp.VSS.Inventory.edit_inventory;
import com.google.android.material.textfield.TextInputEditText;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class edit_collector extends AppCompatActivity {

    private Spinner category,gender,idtype,addfamilySpinner,education;
    private MultiSpinnerSearch product;
    private TextInputEditText username, password,date;
    private TextInputEditText village,vss,division,range,collector_name,collector_spouse,age,aadhar,family,bankname,info,accountno,ifsc;
    private StaticChecks check;
    private SharedPref pref;
    private Button proceed,addfamily;
    private DBHelper dbHelper;
    private String uuid;
    private String ntfpTemp="";
    private RecyclerView recyclerView;
    private Family_adapter adapter;
    private NtfpDao dao;
    public List<FamilyData> familylist=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_collector);
        dao = SynchroniseDatabase.getInstance(this).ntfpDao();

        intiViews();

        new populateData().execute();



        addfamilySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i==1){
                    addfamily.setVisibility(View.VISIBLE);
                    addfamily.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(edit_collector.this, CollectorsFamilyDetails.class);
                            intent.putExtra("projectId",uuid);
                            startActivityForResult(intent, 123);
                        }
                    });
                }
                else{
                    addfamily.setVisibility(View.GONE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        final Calendar c = Calendar.getInstance();
        int currentYear = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog=new DatePickerDialog(edit_collector.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar mCalender = Calendar.getInstance();
                mCalender.set(Calendar.YEAR,year);
                mCalender.set(Calendar.MONTH,month);
                mCalender.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                dateFormat.setTimeZone(mCalender.getTimeZone());
                date.setText(dateFormat.format(mCalender.getTime()));

                long ageLong = currentYear - year;
                age.setEnabled(false);
                age.setText(String.valueOf(ageLong));
            }
        },currentYear,month,dayOfMonth);

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dao.getAllCollector().clear();

                if (validateData()==null){
                    check.showSnackBar(getString(R.string.fillalldata));
                }else {
                    try {
                        dbHelper.updateData(Objects.requireNonNull(validateData()));
                        for (FamilyData familyData : familylist) {
                            dbHelper.insertData(Objects.requireNonNull(familyData));
                        }
                        startActivity(new Intent(edit_collector.this, list_collectors.class));

                    } catch (Exception e) {
                        e.printStackTrace();
                        SnackBarUtils.ErrorSnack(edit_collector.this,e.getLocalizedMessage());
                    }
                }
            }
        });
    }
    private void intiViews() {
        recyclerView=findViewById(R.id.familyrecyclerView);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        pref=new SharedPref(this);
        check=new StaticChecks(this);
        dbHelper=new DBHelper(this);
        vss=findViewById(R.id.vss_name);
        product=findViewById(R.id.spinner_ntfps);
        idtype=findViewById(R.id.spinner_idtype);
        addfamily = findViewById(R.id.addfamily);
        addfamilySpinner = findViewById(R.id.addfamilySpinner);

        range=findViewById(R.id.range_name);
        division=findViewById(R.id.division_name);
        date=findViewById(R.id.date);
        accountno=findViewById(R.id.edit_accountno);
        ifsc=findViewById(R.id.edit_ifsc);
        proceed=findViewById(R.id.add_collector_proceed);
        village=findViewById(R.id.village_name);
        category=findViewById(R.id.spinner_category);
        collector_name=findViewById(R.id.edit_collector_name);
        collector_spouse=findViewById(R.id.edit_spouse_name);
        age=findViewById(R.id.edit_age);
        aadhar=findViewById(R.id.edit_adhaarno);
        education=findViewById(R.id.spinner_edu);
        family=findViewById(R.id.edit_familymem);
        bankname=findViewById(R.id.edit_bankname);
        info=findViewById(R.id.edit_info);
        gender=findViewById(R.id.spinner_gender);

        proceed.setText(R.string.updatecollector);
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    private FamilyData editData(){
        FamilyData data= null;
        Intent intentget= getIntent();
        Bundle checkdata = intentget.getExtras();
        if(checkdata!=null)
        {
            data = (FamilyData) checkdata.get("FamilyEditdata");
        }
        return data;
    }

    private Collector validateData(){
        boolean etFlag=check.checkETList(new TextInputEditText[]{village,collector_name,collector_spouse,age,aadhar
                ,family});
        boolean spinFlag=check.checkSpinnerList(new Spinner[]{category,gender,education});
        if (etFlag && spinFlag && product.getSelectedItems().size()>0){
            String ntfps="";
            for (KeyPairBoolData key:product.getSelectedItems()){
                if (key.isSelected())
                    ntfps+=key.getName().trim()+",";
            }
            return new Collector(uuid,
                    vss.getText().toString(),
                    division.getText().toString(),
                    range.getText().toString(),
                    village.getText().toString(),
                    collector_name.getText().toString(),
                    collector_spouse.getText().toString(),
                    category.getSelectedItem().toString(),
                    age.getText().toString(),
                    gender.getSelectedItem().toString(),
                    date.getText().toString(),
                    idtype.getSelectedItem().toString(),
                    aadhar.getText().toString(),
                    ntfps,
                    education.getSelectedItem().toString(),
                    family.getText().toString(),
                    bankname.getText().toString(),
                    accountno.getText().toString(),
                    ifsc.getText().toString(),
                    username.getText().toString(),
                    password.getText().toString(),
                    info.getText().toString(),
                    0
            );
        }else{
            return null;
        }
    }

    private class populateData extends AsyncTask<String,String,String>{
        ProgressDialog pd;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd=new ProgressDialog(edit_collector.this);
            pd.setMessage("Loading...");
            pd.dismiss();
        }

        @Override
        protected String doInBackground(String... strings) {
           
            String uid=getIntent().getStringExtra("uid");
            Log.i("uidget",uid+"");
            if (uid!=null){
                final Collector model=dbHelper.getAllDataFromCollectorsWhere("uid='"+uid+"'").get(0);
                if (dbHelper.getAllDataFromFamilyWhere("uid='"+uid+"'").size()>0){
                    familylist.addAll(dbHelper.getAllDataFromFamilyWhere("uid='"+uid+"'"));
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        uuid=model.getUid();
                        List<String> list= Arrays.asList(getResources().getStringArray(R.array.sampleSpinner));
                        collector_name.setText(model.getCollector_name());
                        collector_spouse.setText(model.getCollector_spouse());
                        age.setText(String.valueOf(model.getAge()));
                        date.setText(model.getDob());
                        gender.setSelection(Arrays.asList(getResources().getStringArray(R.array.gender)).indexOf(model.getGender()));
                        idtype.setSelection(Arrays.asList(getResources().getStringArray(R.array.IDtype)).indexOf(model.getIdtype()));
                        aadhar.setText(model.getIdno());
                        education.setSelection(Arrays.asList(getResources().getStringArray(R.array.sampleSpinnerEd)).indexOf(model.getEducation()));
                        family.setText(model.getFamily());
                        bankname.setText(model.getBankname());
                        info.setText(model.getInfo());
                        vss.setText(model.getVss());
                        division.setText(model.getDivision());
                        range.setText(model.getRange());
                        village.setText(model.getVillage());
                        category.setSelection(list.indexOf(model.getCategory()));
                        bankname.setText(model.getBankname());
                        accountno.setText(model.getBankaccountno());
                        ifsc.setText(model.getBankifsc());
                        username.setText(model.getUsername());
                        password.setText(model.getPassword());
                        initSpinner(model.getNtfps());
                        ntfpTemp=model.getNtfps();
                        Log.i("Modwel","mks"+model.getNtfps());

                    }
                });
            }
            return "OK";
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (!s.equals("OK"))
                SnackBarUtils.ErrorSnack(edit_collector.this,s);
            adapter=new Family_adapter(familylist,edit_collector.this);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            if (familylist.size()>0){
                addfamilySpinner.setSelection(1);
                addfamily.setVisibility(View.VISIBLE);
            }else {
                addfamilySpinner.setSelection(2);
                addfamily.setVisibility(View.GONE);
            }
        }
    }

    private void initSpinner(String selected) {
        Log.i("selected ntfp ",selected);
        List<KeyPairBoolData> keyPairList = new ArrayList<>();
        try {
            if (JSONStorage.isFilePresent(this, "NTFP.json")) {
                String response = JSONStorage.read(this, "NTFP.json");
                JSONArray jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject ntfpObj = jsonArray.getJSONObject(i);
                    KeyPairBoolData key = new KeyPairBoolData();
                    String ntfpnames=ntfpObj.getString("NTFPmalayalamname")+" ("+ntfpObj.getString("NTFPscientificname")+")";
                    key.setSelected(selected.contains(ntfpObj.getString("NTFPscientificname")));
                    key.setName(ntfpnames);
                    keyPairList.add(key);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("Some details are not fetched properly try again");
            alertDialogBuilder.setPositiveButton("Refresh",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            new NTFPTask().execute();
                        }
                    });
            alertDialogBuilder.setNegativeButton("close", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    onBackPressed();
                }
            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }

        product.setSearchEnabled(true);
        product.setSearchHint(getResources().getString(R.string.type_to_search));
        product.setEmptyTitle(getResources().getString(R.string.nodata));
        product.setClearText(getResources().getString(R.string.close));
        product.setItems(keyPairList, new MultiSpinnerListener() {
            @Override
            public void onItemsSelected(List<KeyPairBoolData> items) {

            }
        });
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
                VSSUser user=pref.getVSS();
                JSONObject json=new JSONObject();
                json.put("DivisionId",user.getDivisionId()+"");
                json.put("RangeId",user.getRangeId()+"");
                json.put("VSSId",user.getVid()+"");
                OkHttpClient client = new OkHttpClient().newBuilder()
                        .build();
                MediaType mediaType = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(mediaType, json.toString());
                Request request = new Request.Builder()
                        .url("https://vanasree.com/NTFPAPI/API/NTFPList")
                        .method("POST", body)
                        .addHeader("Content-Type", "application/json")
                        .build();
                okhttp3.Response response = client.newCall(request).execute();
                JSONStorage.create(edit_collector.this,"NTFP.json",response.body().string());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
//            findViewById(R.id.spin_kit).setVisibility(View.GONE);
            initSpinner(ntfpTemp);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123&& RESULT_OK == resultCode && data!=null){
            String collectorUId = data.getStringExtra("projectId");
            FamilyData familyData = (FamilyData) data.getExtras().get("FamilyData");
            if (familyData!=null){
                if (data.hasExtra("position")){
                    familylist.set(data.getIntExtra("position",0),familyData);
                    adapter.notifyItemChanged(data.getIntExtra("position",0));
                }else{
                    familylist.add(familyData);
                    adapter.notifyItemChanged(familylist.size()-1);
                }

            }
        }
    }
}