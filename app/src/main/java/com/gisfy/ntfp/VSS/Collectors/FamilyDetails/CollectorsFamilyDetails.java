package com.gisfy.ntfp.VSS.Collectors.FamilyDetails;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.androidbuts.multispinnerfilter.KeyPairBoolData;
import com.androidbuts.multispinnerfilter.MultiSpinnerListener;
import com.androidbuts.multispinnerfilter.MultiSpinnerSearch;
import com.gisfy.ntfp.Login.Models.VSSUser;
import com.gisfy.ntfp.R;
import com.gisfy.ntfp.SqliteHelper.DBHelper;
import com.gisfy.ntfp.SqliteHelper.Entity.NTFP;
import com.gisfy.ntfp.SqliteHelper.NtfpDao;
import com.gisfy.ntfp.SqliteHelper.SynchroniseDatabase;
import com.gisfy.ntfp.Utils.JSONStorage;
import com.gisfy.ntfp.Utils.SharedPref;
import com.gisfy.ntfp.Utils.SnackBarUtils;
import com.gisfy.ntfp.Utils.StaticChecks;
import com.gisfy.ntfp.VSS.Collectors.add_collector;
import com.gisfy.ntfp.VSS.Collectors.edit_collector;
import com.gisfy.ntfp.VSS.Collectors.list_collectors;
import com.gisfy.ntfp.VSS.Inventory.add_inventory;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class CollectorsFamilyDetails extends AppCompatActivity {
    private Spinner category, gender, idtype,relatiionspinner,education;
    private MultiSpinnerSearch product;
    private TextInputEditText address,village, familyname, age, aadhar, bankname, info, accountno, ifsc;
    private TextInputEditText  date;
    private StaticChecks check;
    private Button proceed;
    private NTFP ntfpModel = null;

    private NtfpDao dao;
    private SharedPref pref;
    private HashMap<String,Integer> NtfpHashMap= new HashMap();
    private String projectId="",uid="";
    private String ntfpString = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collectors_family_details);
        intiViews();

        if (getIntent().hasExtra("FamilyData")){
            FamilyData familyData = (FamilyData) getIntent().getExtras().get("FamilyData");
            Log.i("FamilyData",familyData.toString());
            if (familyData!=null){
                uid = familyData.getFamilyid();
                projectId = familyData.getUid();

                List<String> list= Arrays.asList(getResources().getStringArray(R.array.sampleSpinner));
                familyname.setText(familyData.getFamily_name());
                age.setText(String.valueOf(familyData.getAge()));
                date.setText(familyData.getDob());
                gender.setSelection(Arrays.asList(getResources().getStringArray(R.array.gender)).indexOf(familyData.getGender()));
                idtype.setSelection(Arrays.asList(getResources().getStringArray(R.array.IDtype)).indexOf(familyData.getIdtype()));
                relatiionspinner.setSelection(Arrays.asList(getResources().getStringArray(R.array.relation)).indexOf(familyData.getRelationship()));
                address.setText(familyData.getAddress());
                aadhar.setText(familyData.getIdno());
                education.setSelection(Arrays.asList(getResources().getStringArray(R.array.sampleSpinnerEd)).indexOf(familyData.getEducation()));
                bankname.setText(familyData.getBankname());
                village.setText(familyData.getVillage());
                category.setSelection(list.indexOf(familyData.getCategory()));
                bankname.setText(familyData.getBankname());
                accountno.setText(familyData.getBankaccountno());
                ifsc.setText(familyData.getBankifsc());
                initSpinner(familyData.getNtfps());
            }
        }else{
            Log.i("projtid",getIntent().getStringExtra("projectId")+"");
            projectId = getIntent().getStringExtra("projectId");
            uid = UUID.randomUUID().toString();
        }

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

        final Calendar c = Calendar.getInstance();
        int currentYear = c.get(Calendar.YEAR);
        int currentMonth = c.get(Calendar.MONTH);
        int CurrentDayOfMonth = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(CollectorsFamilyDetails.this, new DatePickerDialog.OnDateSetListener() {
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
                    Intent intent;
                    if(getIntent().hasExtra("Edit"))
                         intent = new Intent(CollectorsFamilyDetails.this,edit_collector.class);
                    else
                        intent = new Intent(CollectorsFamilyDetails.this,add_collector.class);
                    intent.putExtra("FamilyData",validateData());
                    intent.putExtra("projectId",projectId);
                    if (getIntent().hasExtra("position"))
                        intent.putExtra("position",getIntent().getIntExtra("position",-1));
                    setResult(Activity.RESULT_OK,intent);
                    finish();

                }
            }
        });
    }

    private void intiViews() {
        pref = new SharedPref(this);

        dao = SynchroniseDatabase.getInstance(this).ntfpDao();
        date = findViewById(R.id.date);
        check = new StaticChecks(this);
        idtype = findViewById(R.id.idtypeSpinner);
        gender = findViewById(R.id.gender);
        relatiionspinner =findViewById(R.id.relatiionspinner);
        product = findViewById(R.id.spinner_ntfps);
        accountno = findViewById(R.id.edit_accountno);
        ifsc = findViewById(R.id.edit_ifsc);
        proceed = findViewById(R.id.add_collector_proceed);
        village = findViewById(R.id.village_family);
        address = findViewById(R.id.address);
        category = findViewById(R.id.socialcatSpinner);
        familyname = findViewById(R.id.familyname);
        age = findViewById(R.id.edit_age);
        aadhar = findViewById(R.id.idno);
        education = findViewById(R.id.spinner_eduM);
        bankname = findViewById(R.id.edit_bankname);



        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        initSpinner("");
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

    private FamilyData validateData() {
        if (check.checkSpinnerList(new Spinner[]{relatiionspinner}))
         //   if (check.checkETList(new TextInputEditText[]{ familyname, date, age, village}))
            if (validateEditText(familyname))
                if (validateEditText(village))
                if (check.checkETList(new TextInputEditText[]{  date, age}))
                if (check.checkSpinnerList(new Spinner[]{category, gender, idtype}))
                    if (validateEditText(bankname))
                        if (validateEditText(aadhar))
                            if (check.checkSpinnerList(new Spinner[]{education}))
                  //  if (check.checkETList(new TextInputEditText[]{ aadhar, education}))
                        if (product.getSelectedItems().size()>0) {
                            String ntfps = "";
                            for (KeyPairBoolData key : product.getSelectedItems()) {
                                if (key.isSelected())
                                    ntfps += key.getName().trim() + ",";
                            }
                            Log.i("CCCCCCIIII",uid+"");
                            FamilyData model = new FamilyData(
                                    projectId,
                                    uid,
                                    village.getText().toString(),
                                    familyname.getText().toString(),
                                    category.getSelectedItem().toString(),
                                    age.getText().toString(),
                                    gender.getSelectedItem().toString(),
                                    date.getText().toString(),
                                    idtype.getSelectedItem().toString(),
                                    aadhar.getText().toString(),
                                    ntfpString != "" ? ntfpString : ntfps,
                                    education.getSelectedItem().toString(),
                                    bankname.getText().toString(),
                                    accountno.getText().toString(),
                                    ifsc.getText().toString(),
                                    "",
                                    0,
                                    relatiionspinner.getSelectedItem().toString(),
                                    address.getText().toString()

                            );

                            return model;
                        }
                            return null;



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
                        .url("http://vanasree.com/NTFPAPI/API/NTFPList")
                        .method("POST", body)
                        .addHeader("Content-Type", "application/json")
                        .build();
                okhttp3.Response response = client.newCall(request).execute();
                JSONStorage.create(CollectorsFamilyDetails.this, "NTFP.json", response.body().string());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
//            findViewById(R.id.spin_kit).setVisibility(View.GONE);
            initSpinner("");
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

}