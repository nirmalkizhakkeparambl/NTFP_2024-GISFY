package com.gisfy.ntfp.VSS.Collectors;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidbuts.multispinnerfilter.KeyPairBoolData;
import com.androidbuts.multispinnerfilter.MultiSpinnerListener;
import com.androidbuts.multispinnerfilter.MultiSpinnerSearch;
import com.androidbuts.multispinnerfilter.SingleSpinnerListener;
import com.gisfy.ntfp.Login.Models.VSSUser;
import com.gisfy.ntfp.R;
import com.gisfy.ntfp.SqliteHelper.DBHelper;
import com.gisfy.ntfp.Utils.JSONStorage;
import com.gisfy.ntfp.Utils.SharedPref;
import com.gisfy.ntfp.Utils.SnackBarUtils;
import com.gisfy.ntfp.Utils.StaticChecks;
import com.gisfy.ntfp.VSS.Collectors.FamilyDetails.CollectorsFamilyDetails;
import com.gisfy.ntfp.VSS.Collectors.FamilyDetails.FamilyData;
import com.gisfy.ntfp.VSS.Collectors.FamilyDetails.Family_adapter;
import com.gisfy.ntfp.VSS.Inventory.add_inventory;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class add_collector extends AppCompatActivity {
    private Spinner category, gender, idtype,addfamilySpinner,education;
    private MultiSpinnerSearch product;
    private TextInputEditText village, vss, division, range, collector_name, collector_spouse, age, aadhar, family, bankname, info, accountno, ifsc;
    private TextInputEditText username, password, date;
    private StaticChecks check;
    private SharedPref pref;
    private Button proceed,addfamily;
    private DBHelper dbHelper;
    private String ntfpString;
    private HashMap<String,Integer> NtfpHashMap;
    private RecyclerView recyclerView;
    private Family_adapter adapter;
    public List<FamilyData> familyDataList = new ArrayList<>();
    private String projectId ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_collector);


        intiViews();


        adapter=new Family_adapter(familyDataList,add_collector.this);
        recyclerView.setAdapter(adapter);



        findViewById(R.id.info).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (findViewById(R.id.basicdetails_layout).getVisibility() == View.VISIBLE) {
                    findViewById(R.id.basicdetails_layout).setVisibility(View.GONE);
                } else {
                    findViewById(R.id.basicdetails_layout).setVisibility(View.VISIBLE);
                }
            }
        });
        addfamilySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i==1){
                    addfamily.setVisibility(View.VISIBLE);
                    addfamily.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(add_collector.this,CollectorsFamilyDetails.class);
                            intent.putExtra("projectId",projectId);
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
        int currentMonth = c.get(Calendar.MONTH);
        int CurrentDayOfMonth = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(add_collector.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar mCalender = Calendar.getInstance();
                mCalender.set(Calendar.YEAR, year);
                mCalender.set(Calendar.MONTH, month);
                mCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                dateFormat.setTimeZone(mCalender.getTimeZone());
                date.setText(dateFormat.format(mCalender.getTime()));
                long ageLong = currentYear - year;
                age.setEnabled(false);
                age.setText(String.valueOf(ageLong));
            }
        }, currentYear, currentMonth, CurrentDayOfMonth);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateData() != null) {
                    dbHelper.insertData(Objects.requireNonNull(validateData()));
                    for (FamilyData familyData : familyDataList) {
                        Log.i("adda collecote",familyData.toString());
                        dbHelper.insertData(Objects.requireNonNull(familyData));
                    }
                    check.showSnackBar("Inserted");
                    startActivity(new Intent(add_collector.this, list_collectors.class));
                }

            }
        });
    }
    private void intiViews() {
        NtfpHashMap= new HashMap();
        ntfpString = "";
        dbHelper = new DBHelper(this);
        recyclerView=findViewById(R.id.familyrecyclerView);
        addfamily = findViewById(R.id.addfamily);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        date = findViewById(R.id.date);
        check = new StaticChecks(this);
        pref = new SharedPref(this);
        vss = findViewById(R.id.vss_name);
        idtype = findViewById(R.id.spinner_idtype);
        addfamilySpinner = findViewById(R.id.addfamilySpinner);
        product = findViewById(R.id.spinner_ntfps);
        range = findViewById(R.id.range_name);
        division = findViewById(R.id.division_name);
        accountno = findViewById(R.id.edit_accountno);
        ifsc = findViewById(R.id.edit_ifsc);
        proceed = findViewById(R.id.add_collector_proceed);
        village = findViewById(R.id.village_name);
        category = findViewById(R.id.spinner_category);
        collector_name = findViewById(R.id.edit_collector_name);
        collector_spouse = findViewById(R.id.edit_spouse_name);
        age = findViewById(R.id.edit_age);
        aadhar = findViewById(R.id.edit_adhaarno);
        education = findViewById(R.id.spinner_edu);
        family = findViewById(R.id.edit_familymem);
        bankname = findViewById(R.id.edit_bankname);
        info = findViewById(R.id.edit_info);
        gender = findViewById(R.id.spinner_gender);

        initSpinner();

        check.setValues(new TextInputEditText[]{vss, division, range});
        VSSUser user = pref.getVSS();
        UUID uuid = UUID.randomUUID();
        String[] random = uuid.toString().split("-");
        String usernamestr = user.getDivisionname().substring(0, 3).toUpperCase() + "-" + user.getRangeName().substring(0, 3).toUpperCase() + "-" + user.getvSSName().substring(0, 3).toUpperCase() + "-" + random[0].substring(0, 3).toUpperCase();
        username.setText(usernamestr);
        username.setEnabled(false);
        password.setText("pass@123");
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        if (projectId == null)
            projectId = UUID.randomUUID().toString();
    }

    private void initSpinner() {
        List<KeyPairBoolData> keyPairList = new ArrayList<>();
        try {
            if (JSONStorage.isFilePresent(this, "NTFP.json")) {
                String response = JSONStorage.read(this, "NTFP.json");
                JSONArray jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject ntfpObj = jsonArray.getJSONObject(i);
                    KeyPairBoolData key = new KeyPairBoolData();
                    key.setSelected(false);
                    String ntfpnames=ntfpObj.getString("NTFPmalayalamname")+" ("+ntfpObj.getString("NTFPscientificname")+")";
                    NtfpHashMap.put(ntfpnames,ntfpObj.getInt("Nid"));
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
                for (int i = 0; i < items.size(); i++) {
                    if (items.get(i).isSelected()) {
                        try {
                            ntfpString += items.get(i).getName().trim() + ",";
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(add_collector.this, "Sorry there are no  NTFP's at this moment try after some time", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }
    private boolean validateEditText(EditText editText){
        String text = editText.getText().toString();
        if(text.startsWith(" ")){
            editText.setError("This Field is required");
            Toast.makeText(this,"Please fill "+editText.getHint(),Toast.LENGTH_SHORT).show();

            return false;
        }
        return true;
    }

    private Collector  validateData() {
        Collector model = null;

       if (validateEditText(village))
           if (validateEditText(vss))
               if (validateEditText(division))
                   if (validateEditText(range))
                       if (validateEditText(collector_name))
                      //

   //   if (check.checkETList(new TextInputEditText[]{village, vss, division, range, collector_name, collector_spouse}))
            if (check.checkSpinnerList(new Spinner[]{category,education}))

                if (check.checkETList(new TextInputEditText[]{date, age}))
                    if (check.checkSpinnerList(new Spinner[]{gender,idtype}))
                        if (check.checkETList(new TextInputEditText[]{aadhar, family}))
                if (product.getSelectedItems().size() > 0) {
                    Log.i("Collectorid",projectId);
                    model = new Collector(projectId,
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
                            ntfpString,
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
                } else {
                    SnackBarUtils.ErrorSnack(add_collector.this, getResources().getString(R.string.selectntfps));
                }
        return model;
    }



    private class NTFPTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            findViewById(R.id.spin_kit).setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                VSSUser user = pref.getVSS();
                JSONObject json = new JSONObject();
                json.put("DivisionId", user.getDivisionId() + "");
                json.put("RangeId", user.getRangeId() + "");
                json.put("VSSId", user.getVid() + "");
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
                JSONStorage.create(add_collector.this, "NTFP.json", response.body().string());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
//            findViewById(R.id.spin_kit).setVisibility(View.GONE);
            initSpinner();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123&& RESULT_OK == resultCode && data!=null){
            projectId = data.getStringExtra("projectId");
            FamilyData familyData = (FamilyData) data.getExtras().get("FamilyData");
            if (familyData!=null){
                if (data.hasExtra("position")){
                    familyDataList.set(data.getIntExtra("position",0),familyData);
                    adapter.notifyItemChanged(data.getIntExtra("position",0));
                }else{
                    familyDataList.add(familyData);
                    adapter.notifyItemChanged(familyDataList.size()-1);
                }

            }
        }
    }
}