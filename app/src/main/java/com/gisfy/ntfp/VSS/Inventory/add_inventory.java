package com.gisfy.ntfp.VSS.Inventory;

import static com.gisfy.ntfp.SqliteHelper.DBHelper.TABLE_5_COL_0;
import static com.gisfy.ntfp.SqliteHelper.DBHelper.TABLE_5_COL_2;
import static com.gisfy.ntfp.SqliteHelper.DBHelper.TABLE_5_COL_3;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gisfy.ntfp.Collectors.CollectorInventory;
import com.gisfy.ntfp.Collectors.CollectorStockModel;
import com.gisfy.ntfp.HomePage.Home;
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
import com.gisfy.ntfp.Utils.Constants;
import com.gisfy.ntfp.Utils.JSONStorage;
import com.gisfy.ntfp.Utils.SharedPref;
import com.gisfy.ntfp.Utils.SnackBarUtils;
import com.gisfy.ntfp.Utils.StaticChecks;
import com.gisfy.ntfp.VSS.Collectors.Collector;
import com.gisfy.ntfp.VSS.Collectors.add_collector;
import com.gisfy.ntfp.VSS.Payment.Model_payment;
import com.google.android.material.textfield.TextInputEditText;


import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

public class add_inventory extends AppCompatActivity {

    private TextInputEditText vss,division,range,quantity, amountPaid;
    private TextView date,indication;
    private Spinner ntfpgrade,measurement,location;
    private AutoCompleteTextView ntfptype,member,products,collector,vssSelect;
    private Button proceed,AmoundCalc;
    private StaticChecks check;
    private StaticChecks checks;
    private NTFP ntfpModel=null;
    private CollectorsModel collectorModel = null;
    private NTFP.ItemType itemTypeModel =null;
    private MemberModel memberModel = null;
    private ArrayAdapter<NTFP.ItemType> ntfpTypeAdapter = null;
    public int posi ;
    public int mmmmID,ccccID=0;
    public int posii ;
    private SharedPref pref;
    public int posiii;
    boolean flag = true;
    private  JSONArray arraymem = new JSONArray();
    Double Qnty=0.0;

    private boolean flagLocation = false;
    private boolean flagNoLocation = false;
    private List<String> locationNames = new ArrayList<>();

    public  String VSsSIDselect;
    public  String COLLLIDSelect;
    public  String MEMIDSelect;
    public  String vssnamepage;
    String datanew;
    public  String MeMMIDD;
    private long id = 1;
    private int locationId =-1;


    private CollectorStockModel modell;
    private ArrayAdapter<MemberModel> memberAdapter=null;
    private ArrayAdapter<CollectorsModel> collectorAdapter;
    private ArrayAdapter<String> vssAdapter=null;
    private ArrayAdapter<String> ImplementingAdaptercol;
    private ArrayAdapter<String> ImplementingAdaptermem;
    List<String> collectors = new ArrayList<>();
    private String[] impstr;

    private VSSUser vssUser = null;
    boolean spinnerflag = false;
    List<String> COLLIDD = new ArrayList<>();
    List<String> VsssName = new ArrayList<>();
    List<String> COLName = new ArrayList<>();
    List<String> MEMName = new ArrayList<>();
    List<String> MEMID = new ArrayList<>();

    List<String> VIDD = new ArrayList<>();
    List<String> RangeId = new ArrayList<>();
    List<String> DivisionId = new ArrayList<>();
    private double loseAmound = 00;

    List<String> apiVss = new ArrayList<>();

    private DBHelper db;
    private String inventoryId="",UnitPara="",VssGETT;
    private NtfpDao dao;
    DBHelper databaseHelper = new DBHelper(this);

    public static String getValue(JSONObject obj, String value) throws JSONException {
        if(obj.has(value))
            return obj.getString(value);
        else
            return "null";
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_inventory);

        intiViews();

//        Home home= new Home();
//
//try {
//    VSSUser user=pref.getVSS();
//    HashMap<String, String> json = new HashMap<>();
//    json.put("DivisionId", user.getDivisionId() + "");
//    json.put("RangeId", user.getRangeId() + "");
//    json.put("VSSId", user.getVid() + "");
//    home.setupMemberData(json);
//}catch (Exception e ){
//    String exc = e.getLocalizedMessage();
//    Log.i("ERORRupdatecollP2A",exc+"");
//}
        dao = SynchroniseDatabase.getInstance(this).ntfpDao();
        db=new DBHelper(this);
        if (getIntent().hasExtra("uid")) {


            proceed.setText(getString(R.string.update));
            inventoryId = getIntent().getStringExtra("uid");
            InventoryRelation relation = dao.getInventoryFromId(inventoryId);
            if (relation.getItemType()!=null){
                ntfptype.setText(relation.getItemType().getMycase(),false);
            }
            itemTypeModel = relation.getItemType();
            if (relation.getInventory().getGrade()!=null){
                switch (relation.getInventory().getGrade()) {
                    case "Grade1":
                        ntfpgrade.setSelection(1);
                        break;
                    case "Grade2":
                        ntfpgrade.setSelection(2);
                        break;
                    case "Grade3":
                        ntfpgrade.setSelection(3);
                        break;
                    default:
                        ntfpgrade.setSelection(0);
                        break;
                }
            }
            products.setText(relation.getNtfp()+"",false);
            ntfpModel = relation.getNtfp();
            String MemberName;
            if (relation.getMember()!=null){
                MemberName = relation.getMember().getName();
            }
            else MemberName = "";
            vssSelect.setText(String.valueOf(relation.getInventory().getVssname()));
//            if(vssSelect.getText().length()!=0){
//                vssnamepage =vssSelect.getText().toString();
//            }



            if(vssSelect.getText().equals( String.valueOf(relation.getInventory().getVssname() ) )){
                member.setText(MemberName,false);

                ccccID=relation.getInventory().getCollectorId();

                memberModel = relation.getMember();
                collector.setText(relation.getCollector().getCollectorName(),false);
            }else {
                member.setText(MemberName,false);

                memberModel = relation.getMember();
                collector.setText(relation.getInventory().getColectname());
                ccccID=relation.getInventory().getCollectorId();


            }

            //  collectorModel = relation.getCollector();

//            if(vssSelect.getText().equals("Current VSS")) {
//                collector.setText(relation.getCollector().getCollectorName(), false);
//            }

            String losss= String.valueOf(relation.getInventory().getLoseAmound());
            if(losss ==" "){

                losss ="0";

            }else{
                losss = String.valueOf(relation.getInventory().getLoseAmound());
            }

            amountPaid.setText(String.valueOf(relation.getInventory().getPrice()));

            Double Lo1at = Double.valueOf(relation.getInventory().getLoseAmound());
            if (Lo1at > 0){
                Double QNTYYY= Double.valueOf(relation.getInventory().getQuantity());
                Double QQQ= (QNTYYY + Lo1at);
                quantity.setText(QQQ.toString());

            }else {
                quantity.setText(String.valueOf(relation.getInventory().getQuantity()));
            }

//            loseQ.setText(losss);
            Log.i("measurementUnit121",relation.getInventory().getMeasurements()+"");



            if (relation.getInventory().getMeasurements().equals("Kilogram")){
                measurement.setSelection(1);
                flag = true;}
            else if(relation.getInventory().getMeasurements().equals("Gram")){
                measurement.setSelection(2);
                flag = true;}
            else if(relation.getInventory().getMeasurements().equals("Litre")){
                measurement.setSelection(3);
                flag = false;
            }
            else if(relation.getInventory().getMeasurements().equals("Millilitre")) {
                measurement.setSelection(4);
                flag = false;
            }
            else
                measurement.setSelection(0);


        }
        else if(getIntent().hasExtra("CollectorModel"))
        {

            CollectorStockModel model = (CollectorStockModel) getIntent().getSerializableExtra("CollectorModel");
            ntfptype.setText(model.getnTFPType(),false);
            itemTypeModel = dao.getAllNTFPTypes().get(model.getnTFPTypeId());
            ntfpModel = dao.getNTFPFromNId(model.getnTFPId());
            products.setText(ntfpModel.getNTFPmalayalamname()+"( " +ntfpModel.getNTFPscientificname() + " )",false);
            collector.setText(model.getCollectorName());
            member.setText(model.getmName());
          //  member.setEnabled(false);

            if(model.getLoseAmound()!=null) {
                Double Lo1at = Double.valueOf(model.getLoseAmound());
                if (Lo1at > 0.0) {
                    Double QNTYYY = Double.valueOf(model.getQuantity());
                    Double QQQ = (QNTYYY + Lo1at);
                    quantity.setText(QQQ.toString());

                } else {
                    quantity.setText(model.getQuantity());
                }
//                loseQ.setText(model.getLoseAmound());
            }
            quantity.setText(model.getQuantity());
            inventoryId = model.getRandom();
        if (model.getDateTime() != null) {
            String dateget = model.getDateTime();
            String[] dateParts = dateget.split("-");
            String year = dateParts[0];
            String month = dateParts[1];
            String day = dateParts[2];
            datanew = day + "-" + month + "-" + year;
            date.setText(datanew);

        }
           date.setText(datanew);
            collectorModel = dao.getCollectorFromCId(model.getCollectorID());
            Log.d("VSSGETT", model.getvSS()+"");

       //     vssSelect.setText(model.getvSS());

//            InventoryRelation relation = dao.getInventoryFromId(inventoryId);
//            ntfpModel = relation.getNtfp();
            if(vssSelect.getText().toString().equals(vssnamepage)){
                collector.setText(model.getCollectorName());
            }
//            if(vssSelect.getText().equals(String.valueOf(relation.getInventory().getVssname())) || ( vssSelect.getText().equals(" ") )){
//                collector.setText(model.getCollectorName());
//            }
         //   collector.setText(model.getCollectorName());
         //     vssSelect.setHint(vssUser.getvSSName());


//            collector.setEnabled(false);
            ntfpTypeAdapter = new ArrayAdapter<>(this, R.layout.multiline_spinner_dropdown_item, dao.getItemTypesFromNid(model.getnTFPId()));
            ntfptype.setAdapter(ntfpTypeAdapter);
            measurementSpinner(measurement, model.getUnit());
            if((model.getUnit().equals("Litre"))||(model.getUnit().equals("Millilitre"))){
                 flag = false;
            }else {
                flag = true;
            }


            gradeSpinner(ntfpgrade, "Select NTFP Grade");

            double basePrice;
            if(ntfpgrade.getSelectedItemPosition()==0)
            basePrice = 0;
            else if (ntfpgrade.getSelectedItemPosition()==1)
                basePrice = itemTypeModel.getGrade1Price();
            else if (ntfpgrade.getSelectedItemPosition() == 2)
                basePrice = itemTypeModel.getGrade2Price();
            else
                basePrice = itemTypeModel.getGrade3Price();
            Double Quntity;
            if (quantity.getText().length()>0) {
//                if(loseQ.getText().toString().isEmpty()){
                    Quntity = Double.parseDouble(quantity.getText().toString());
                    Double amoundY =(Quntity * basePrice);
//                    loseQ.setText("0.0");
                    amountPaid.setText(amoundY.toString());
//                }
//                else {
//
//
//                    Quntity = Double.parseDouble(quantity.getText().toString());
//                }
//                Double amoundY =(Quntity * basePrice);
//                amountPaid.setText(amoundY.toString());

            }
            else
                amountPaid.setText("");

        }
        else{
            Date d = new Date();
            inventoryId = "TRANS-"+new SharedPref(add_inventory.this).getVSS().getvSSName().substring(0,3).toUpperCase()+"-" + d.getDay() + d.getMonth() + d.getMinutes() + d.getSeconds();
        }
        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                CollectorStockModel model = (CollectorStockModel) getIntent().getSerializableExtra("CollectorModel");
                InventoryRelation relation = dao.getInventoryFromId(inventoryId);

                if( vssSelect.getText().toString().equals(vssnamepage)) {
                    if ((collectorModel!=null ) || (collector.getText().length()>0)){

                        if (itemTypeModel!=null ) {

                                      String dateget = date.getText().toString();
                                      String[] dateParts = dateget.split("-");
                                      String day = dateParts[0];
                                      String month = dateParts[1];
                                      String year = dateParts[2];
                                      String datanew = year+"-"+ month+"-"+day;



                            if (check.checkSpinnerList(new Spinner[]{ntfpgrade,measurement}))
                                if (checks.checkETList(new TextInputEditText[]{quantity})) {
                                    try {
                                        Date d = new Date();
                                         int memberId=0;
                                    if (memberModel != null) {
                                        memberId = memberModel.getMemberId();
                                        Log.i("VALUEMEMBER", "");
                                    }
                                    else if(member.getText().length()==0){


                                            memberId = -1;
                                        }
                                    else if(model.getmName()!=null){


                                        memberId = model.getMemberId();
                                    }
                                    else {
                                        memberId =-1;
                                    }
                                int IDDD = 0;

                                if( collectorModel!=null){
                                    IDDD =collectorModel.getCid();
                                    Log.i("CCID1",IDDD+"");
                                }else if (ccccID>0){
                                    IDDD = ccccID;

                                }else
                                {
                                   // InventoryRelation model=new InventoryRelation();
                                    IDDD = 0;
                                    Log.i("CCID2",IDDD+"");

                                }
                               Double loQ  = 0.0;
                                if( loQ > 0){

                                    Double losQa  = 0.0;
                                    Double QtyA = Double.valueOf(quantity.getText().toString());
                                    Qnty = (QtyA - losQa);


                                }else {
//                                    loseQ.setText(Qnty.toString());
                                    Double QtyA = Double.valueOf(quantity.getText().toString());
                                    Qnty =QtyA;
                                }

                                       Log.i("getNtfpId173", ntfpModel.getNid() + "");
                                        InventoryEntity entity = new InventoryEntity(
                                                inventoryId,
                                                IDDD,
                                                memberId,
                                                ntfpModel.getNid(),
                                                ntfpModel.getNid(),
                                                vssSelect.getText().toString(),
                                                "0",
                                                collector.getText().toString(),
                                                ntfpgrade.getSelectedItem().toString(),
                                                measurement.getSelectedItem().toString(),
                                                Qnty,
                                                0.0,
                                                Double.parseDouble(amountPaid.getText().toString()),
                                                datanew,
                                                location.getSelectedItem().toString(),
                                                locationId);
                                        VSSUser user = new SharedPref(add_inventory.this).getVSS();
                                        Model_payment payment = new Model_payment(
                                                inventoryId,
                                                user.getvSSName(),
                                                user.getDivisionname(),
                                                user.getRangeName(),
                                                amountPaid.getText().toString(),
                                                datanew,
                                                "",
                                                "",
                                                "Made",
                                                collector.getText().toString(),
                                                ntfpModel.getNTFPmalayalamname()+"("+ntfpModel.getNTFPscientificname()+")",
                                                measurement.getSelectedItem().toString(),
                                                quantity.getText().toString(),
                                                0,
                                                0);
                                        db.insertData(payment);
                                        if (getIntent().hasExtra("")) {
                                            String body = "{\n" +
                                                    "\t\"InventID\":\"" + inventoryId + "\",\n" +
                                                    "\t\"VSSStatus\":\"true\"\n" +
                                                    "}";
                                            new uploadTask(entity, payment).execute(body);
                                        } else {
                                            dao.insertInventory(entity);
                                            startActivity(new Intent(add_inventory.this, list_inventory.class));
                                        }
                                    } catch (Exception e) {
                                        String exc = e.getLocalizedMessage();
                                        Log.i("ERORREditP2A",exc+"");
                                        e.printStackTrace();
                                        SnackBarUtils.ErrorSnack(add_inventory.this, getString(R.string.somethingwentwrong));
                                    }
                                }
                        }else {
                            Toast.makeText(add_inventory.this, "please select NTFP Names and NTFP Type", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(add_inventory.this, "please select Collector", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {

Log.i("COLLLNamelengtyh",collector.getText().length()+"");
                if(collector.getText().length()!=0){
                    String dateget = date.getText().toString();
                    String[] dateParts = dateget.split("-");
                    String day = dateParts[0];
                    String month = dateParts[1];
                    String year = dateParts[2];
                    String datanew = year+"-"+ month+"-"+day;
                    if(itemTypeModel!=null ) {
                        if(check.checkSpinnerList(new Spinner[]{ntfpgrade,measurement}))
                            if (checks.checkETList(new TextInputEditText[]{quantity})) {
                                try {
                                    try {
                                        Date d = new Date();
                                        String memberId= "0";

                                        Cursor cursor = databaseHelper.getSingleRow(DBHelper.TABLE_5, TABLE_5_COL_0,id+"");
                                        if(COLLLIDSelect == null){
                                            COLLLIDSelect = (databaseHelper.TABLE_5_COL_3);
                                            String ccc= collector.getText().toString();
                                            if(ccc != null) {
                                                ccc = (databaseHelper.TABLE_5_COL_2);
                                            }
                                        }
                                        if (VSsSIDselect == null) {
                                            VSsSIDselect =(databaseHelper.TABLE_5_COL_5);
                                            MEMIDSelect=(databaseHelper.TABLE_5_COL_6);
                                        }
                                        Double loQ  = 0.0;
                                        if( loQ > 0){

                                            Double losQa  = 0.0;
                                            Double QtyA = Double.valueOf(quantity.getText().toString());
                                            Qnty = (QtyA - losQa);


                                        }else {
//                                            loseQ.setText("0.0");
                                            Qnty =Double.valueOf(quantity.getText().toString());
                                        }

                                        Log.i("getNtfpId173", ntfpModel.getNid() + "");
                                        InventoryEntity entity = new InventoryEntity(
                                                inventoryId,

                                                Integer.parseInt(COLLLIDSelect),
                                                Integer.parseInt(MEMIDSelect),
                                                ntfpModel.getNid(),
                                                ntfpModel.getNid(),
                                                vssSelect.getText().toString(),
                                                VSsSIDselect,
                                                collector.getText().toString(),
                                                ntfpgrade.getSelectedItem().toString(),
                                                measurement.getSelectedItem().toString(),
                                                Qnty,
                                                00,
                                                Double.parseDouble(amountPaid.getText().toString()),
                                                datanew,
                                                location.getSelectedItem().toString(),locationId);

                                        VSSUser user = new SharedPref(add_inventory.this).getVSS();

                                        Model_payment payment = new Model_payment(
                                                inventoryId,

                                                user.getvSSName(),
                                                user.getDivisionname(),
                                                user.getRangeName(),
                                                amountPaid.getText().toString(),
                                                datanew,
                                                "",
                                                "",
                                                "Made",
                                                collector.getText().toString(),
                                                ntfpModel.getNTFPmalayalamname()+"("+ntfpModel.getNTFPscientificname()+")",
                                                measurement.getSelectedItem().toString(),
                                                quantity.getText().toString(),
                                                0,
                                                0);
                                        db.insertData(payment);
                                        if (getIntent().hasExtra("")) {
                                            String body = "{\n" +
                                                    "\t\"InventID\":\"" + inventoryId + "\",\n" +
                                                    "\t\"VSSStatus\":\"true\"\n" +
                                                    "}";
                                            new uploadTask(entity, payment).execute(body);
                                        } else {
                                            dao.insertInventory(entity);
                                            startActivity(new Intent(add_inventory.this, list_inventory.class));
                                        }
                                    }catch (NumberFormatException e){
                                        e.printStackTrace();
                                    }

                                } catch (Exception e) {
                                    String exc = e.getLocalizedMessage();
                                    Log.i("ERORREditP1A",exc+"");
                                    e.printStackTrace();
                                    SnackBarUtils.ErrorSnack(add_inventory.this, getString(R.string.somethingwentwrong));
                                }
                            }
                    }else {
                        Toast.makeText(add_inventory.this, "please select NTFP Names and NTFP Type", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(add_inventory.this, "please select Collector", Toast.LENGTH_SHORT).show();
                }}
            }
        });

        findViewById(R.id.info).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (findViewById(R.id.basicdetails_layout).getVisibility()==View.VISIBLE){
                    findViewById(R.id.basicdetails_layout).setVisibility(View.GONE);
                }else{
                    findViewById(R.id.basicdetails_layout).setVisibility(View.VISIBLE);
                }
            }
        });

    }
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("Do you leave before submit?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user pressed "yes", then he is allowed to exit from application
                finish();
            }
        });
        builder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user select "No", just cancel this dialog and continue with app
                dialog.cancel();
            }
        });
        AlertDialog alert=builder.create();
        alert.show();
    }

    private void VssNameGet() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Getting Data...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        String URL="https://vanasree.com/NTFPAPI/API/VSSList";
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest loginRequest = new JsonArrayRequest(com.android.volley.Request.Method.POST, URL,
                null,new Response.Listener<JSONArray>()
        {
            public void onResponse(JSONArray resp) {
                progressDialog.dismiss();

                try {

                    {
                        Log.i("JsonArryyy332", resp.toString() + "");

                        JSONArray jsonArray = (JSONArray)resp;
                        for (int j = 0; j < jsonArray.length(); j++) {
                            JSONObject arrayJSONObject = jsonArray.getJSONObject(j);
                            VIDD.add(arrayJSONObject.getString("Vid"));
                            VsssName.add(arrayJSONObject.getString("VSSName"));
                            RangeId.add(arrayJSONObject.getString("RangeId"));
                            DivisionId.add(arrayJSONObject.getString("DivisionId"));
                            Log.i("iddata", arrayJSONObject.getString("Vid"));
                            Log.i("blockdata", arrayJSONObject.getString("VSSName"));
                            Log.i("RangeId", arrayJSONObject
                                    .getString("RangeId"));
                            Log.i("DivisionId", arrayJSONObject.getString("DivisionId"));
                        }
                        VsssName.add(0, "Current VSS");
                        ArrayAdapter<String> ImplementingAdapter = new ArrayAdapter<String>(add_inventory.this, R.layout.multiline_spinner_dropdown_item, VsssName);
                        ImplementingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vssSelect.setAdapter(ImplementingAdapter);
                        VSSUser user = new SharedPref(add_inventory.this).getVSS();
                     Log.i("VSSSnme",user.getvSSName()+"");
                        vssSelect.setText(user.getvSSName());
                        vssnamepage = user.getvSSName();

                        vssSelect.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long rowId) {
                                String selection = (String) parent.getItemAtPosition(position);
                                int pos;
                                for (int i = 0; i < VsssName.size(); i++) {

                                    Log.i("VssSIZEE",VsssName.size()+"");
                                    if (VsssName.get(i).equals(selection)) {
                                        pos = i;
                                        posi = pos;
                                        try {
                                            JSONObject jsonObject1 = new JSONObject();
                                            JSONObject jsonObject2 = new JSONObject();
                                            JSONObject jsonObject3 = new JSONObject();
                                            jsonObject1.put("VSSId",VIDD.get(posi-1));
                                            VSsSIDselect = VIDD.get(posi-1);
                                            Log.i("VSSSSSSSID",VSsSIDselect+"");
                                            jsonObject1.put("RangeId",RangeId.get(posi-1));
                                            jsonObject1.put("DivisionId",DivisionId.get(posi-1));
                                            JSONArray array = new JSONArray();
                                            array.put(jsonObject1);
                                            Log.i("jsonsd", jsonObject1.toString());
                                            Log.i("jsonsdposs", posi +"");
                                            if(ImplementingAdaptercol!=null) {
                                                ImplementingAdaptercol.notifyDataSetChanged();
                                            }
                                            vssSelect.addTextChangedListener(new TextWatcher() {

                                                @Override
                                                public void onTextChanged(CharSequence s, int start, int before, int count) {

                                                    ImplementingAdaptercol.clear();
                                                }

                                                @Override
                                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                                }

                                                @Override
                                                public void afterTextChanged(Editable s) {


                                                }
                                            });


                                            if (vssSelect.getText().toString().equals(vssnamepage)){

                                                    ImplementingAdaptercol.clear();
                                                    collectorAdapter = new ArrayAdapter<CollectorsModel>(add_inventory.this,R.layout.multiline_spinner_dropdown_item,dao.getAllCollector());
                                                collector.setAdapter(collectorAdapter);
                                                collector.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                    @Override
                                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                        collectorModel = collectorAdapter.getItem(position);
                                                        if (collectorModel!=null){
                                                            memberAdapter = new ArrayAdapter<MemberModel>(add_inventory.this,R.layout.multiline_spinner_dropdown_item,dao.getMemberFromMemberCId(collectorModel.getCid()));
                                                            member.setAdapter(memberAdapter);
                                                        }


                                                    }
                                                });
                                            }
                                            else {
                                                postDataUsingVolley(array);

                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                        break;
                                    }
                                }
                                System.out.println("Position " + posi); //check it now in Logcat
                            }

                        });
                         collectorAdapter = new ArrayAdapter<CollectorsModel>(add_inventory.this,R.layout.multiline_spinner_dropdown_item,dao.getAllCollector());
                        collector.setAdapter(collectorAdapter);
                        collector.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                collectorModel = collectorAdapter.getItem(position);
                                if (collectorModel!=null){
                                    memberAdapter = new ArrayAdapter<MemberModel>(add_inventory.this,R.layout.multiline_spinner_dropdown_item,dao.getMemberFromMemberCId(collectorModel.getCid()));
                                    member.setAdapter(memberAdapter);
                                }

                            }
                        });
                    }

                } catch (JSONException jsonException) {
                    jsonException.printStackTrace();
                }
            }
        },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString()+"");
                    }
                });
        queue.add(loginRequest);


    }
    public static boolean hasValue(JSONObject obj, String value) throws JSONException {
        if(obj.has(value))
            return true;
        else
            return false;
    }

    private void postDataUsingVolley(JSONArray requestBody) {
        //  Log.i("getREQUESTBODY ",requestBody.toString());
        String URL = "https://vanasree.com/NTFPAPI/API/CollectorList";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Getting Data...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        JsonArrayRequest loginRequest = new JsonArrayRequest(com.android.volley.Request.Method.POST ,URL ,requestBody ,
                (Response.Listener<JSONArray>) resp -> {

                    progressDialog.dismiss();
                    try {


                        JSONArray jsonArray = (JSONArray)resp;

                        Log.i("JsonArryyyCOLLL", resp.toString() + "");
                        for (int j = 0; j < jsonArray.length(); j++) {

                            JSONObject arrayJSONObject = jsonArray.getJSONObject(j);
//
//
                            COLName.add(arrayJSONObject.getString("CollectorName"));
                            COLLIDD.add(arrayJSONObject.getString("Cid"));

                            Log.i("COLLLLLL", arrayJSONObject.getString("CollectorName"));
                            Log.i("COLLLLLL", arrayJSONObject.getString("Cid"));
                            //  Collections.sort(COLName);




                        }
                        ImplementingAdaptercol = new ArrayAdapter<String>(add_inventory.this, R.layout.multiline_spinner_dropdown_item, COLName);
                        ImplementingAdaptercol.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        collector.setAdapter(ImplementingAdaptercol);

                            collector.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                                    String selection = (String) parent.getItemAtPosition(position);
                                        int pos;
                                    Log.i("COLLSIZEE",COLName.size()+"");
                                        for (int i = 0; i < COLName.size(); i++){

                                            if (COLName.get(i).equals(selection)) {
                                                pos = i;
                                                posii = pos;
                                                COLLLIDSelect = COLLIDD.get(posii);

                                           }}
                                                if (id == -1) {

                                                    Log.i("IDDDDD",id+"");
                                                }

                                                 databaseHelper.TABLE_5_COL_3 = COLLLIDSelect;

                                                Log.i("COLLSIZEE",COLLLIDSelect + "");
                                                try {


                                                    JSONObject jsonObject1 = new JSONObject();

                                                    jsonObject1.put("Cid",COLLLIDSelect);

                                                    JSONArray arraymem = new JSONArray();
                                                    arraymem.put(jsonObject1);
                                                    Log.i("jsonsdMEM", jsonObject1.toString());

                                                    postDataUsingVolleyone(arraymem);

                                                    if (vssSelect.getText().equals(vssnamepage)) {

                                                            ImplementingAdaptercol.clear();

                                                        collectorAdapter = new ArrayAdapter<CollectorsModel>(add_inventory.this, R.layout.multiline_spinner_dropdown_item, dao.getAllCollector());
                                                        collector.setAdapter(collectorAdapter);
                                                        collector.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                            @Override
                                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                                collectorModel = collectorAdapter.getItem(position);
                                                                if (collectorModel != null) {
                                                                    memberAdapter = new ArrayAdapter<MemberModel>(add_inventory.this, R.layout.multiline_spinner_dropdown_item, dao.getMemberFromMemberCId(collectorModel.getCid()));
                                                                    member.setAdapter(memberAdapter);
                                                                }

                                                            }
                                                        });
                                                    }



                                                }catch (Exception e){
                                                    e.printStackTrace();
                                                }


                                            }

                            });




                    }catch (Exception e){
                        progressDialog.dismiss();
                        String exc = e.getLocalizedMessage();
                        Log.i("EXXppp22",exc+"");
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("EXXppp21",error+"");
                progressDialog.dismiss();
            }
        });
        requestQueue.add(loginRequest);

    }

    private void postDataUsingVolleyone(JSONArray requestBody) {

        String URL = "https://vanasree.com/NTFPAPI/API/MemberList";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Getting Data...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        JsonArrayRequest loginRequest = new JsonArrayRequest(com.android.volley.Request.Method.POST ,URL ,requestBody ,
                (Response.Listener<JSONArray>) respo -> {
                    Log.i("JsonArryyyCOLLL22", respo.toString() + "");

                    progressDialog.dismiss();
                    try {


                        JSONArray jsonArray = (JSONArray)respo;
                        //
                        Log.i("JsonArryyyCOLLL2", respo.toString() + "");
                        for (int j = 0; j < jsonArray.length(); j++) {

                            JSONObject arrayJSONObject = jsonArray.getJSONObject(j);

                            MEMName.add(arrayJSONObject.getString("Membername"));
                            MEMID.add(arrayJSONObject.getString("MemberId"));

                            Log.i("COLLLLLLMEM", arrayJSONObject.getString("Membername"));
                            Log.i("COLLLLLLMEM", arrayJSONObject.getString("MemberId"));

                        }


                            ImplementingAdaptermem = new ArrayAdapter<String>(add_inventory.this, R.layout.multiline_spinner_dropdown_item, MEMName);
                            ImplementingAdaptermem.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            member.setAdapter(ImplementingAdaptermem);

                            member.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                                    String selection = (String) parent.getItemAtPosition(position);
                                    int pos;
                                    Log.i("MEMSIZEE",MEMName.size()+"");
                                    for (int i = 0; i < MEMName.size(); i++){

                                        if (MEMName.get(i).equals(selection)) {
                                            pos = i;
                                            posiii = pos;
                                            MEMIDSelect = MEMID.get(posiii);
                                            Log.i("COLLSIZEE",MEMIDSelect+"");


                                            long id = databaseHelper.addFamilyMember(

                                                    "",
                                                    "",
                                                    COLLLIDSelect,
                                                    VSsSIDselect,
                                                    MEMIDSelect

                                            );
                                        }
                                    }

                                }
                            });




                    }catch (Exception e){
                        progressDialog.dismiss();
                         e.printStackTrace();
                        String exc = e.getLocalizedMessage();
                        Log.i("EXXppp22",exc+"");
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                error.printStackTrace();
                Log.i("EXXppp211",error+"");
            }
        });
        progressDialog.dismiss();
        requestQueue.add(loginRequest);

    }

    private void intiViews() {
        pref = new SharedPref(this);
        location = findViewById(R.id.location);
        check = new StaticChecks(this);
        collector = findViewById(R.id.collector_spinner);
        checks=new StaticChecks(this);
        member=findViewById(R.id.spinner_member);
        ntfptype=findViewById(R.id.ntfptype);
        ntfpgrade=findViewById(R.id.ntfpgrade);
        quantity=findViewById(R.id.edit_quantity);
//        loseQ=findViewById(R.id.edit_lose);
        amountPaid= findViewById(R.id.amountPaid);
        date=findViewById(R.id.date);
        products=findViewById(R.id.spinner_ntfps);
        proceed=findViewById(R.id.add_collector_proceed);
        vssSelect=findViewById(R.id.vss_spinner);
        AmoundCalc=findViewById(R.id.calC);
        proceed.setText(getResources().getString(R.string.addinv));
        vss=findViewById(R.id.vss_name);
        indication = findViewById(R.id.measurementincation);
        range=findViewById(R.id.range_name);
        division=findViewById(R.id.division_name);
        measurement=findViewById(R.id.measurement);
        new locationFeach().execute();
        checks.setValues(new TextInputEditText[]{vss,division,range});
        final Calendar c = Calendar.getInstance();
        Date d = c.getTime();
        SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy");
        String date1 = format1.format(d);
        date.setText(date1);
        quantity.setFilters(new InputFilter[]{
                new DecimalDigitsInputFilter(4, 2)
        });

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        vssAdapter = new ArrayAdapter<String>(this, R.layout.multiline_spinner_dropdown_item,VsssName);
//        ArrayAdapter<ListDocument.List> vssAdapter = new ArrayAdapter<List>(add_inventory.this,R.layout.multiline_spinner_dropdown_item,districts);
        vssSelect.setAdapter(vssAdapter);


        VssNameGet();


      //  postDataUsingVolleyone(arraymem);

        NtfpDao dao = SynchroniseDatabase.getInstance(this).ntfpDao();
        ArrayAdapter<NTFP> ntfpAdapter = new ArrayAdapter<NTFP>(this,R.layout.multiline_spinner_dropdown_item,dao.getAllNTFPs());
        products.setAdapter(ntfpAdapter);

        ntfpTypeAdapter = new ArrayAdapter<>(this, R.layout.multiline_spinner_dropdown_item, dao.getAllNTFPTypes());
        ntfptype.setAdapter(ntfpTypeAdapter);

        member.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (collectorModel==null)
                {
                    if(ImplementingAdaptermem!= null){

                }
                //    Toast.makeText(add_inventory.this, "Select collector first", Toast.LENGTH_SHORT).show();
                }else{
                    member.showDropDown();
                }
            }
        });




        SnackBarUtils.initAutoSpinner(new AutoCompleteTextView[]{vssSelect});

//        member.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(final View arg0) {
//                if (collectorModel==null){
//                    Toast.makeText(add_inventory.this, "Select collector first", Toast.LENGTH_SHORT).show();
//                }else{
//
//                    member.showDropDown();
//                }
//
//            }
//        });



        SnackBarUtils.initAutoSpinner(new AutoCompleteTextView[]{products,collector});
        ntfptype.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (ntfpModel==null){
                    Toast.makeText(add_inventory.this, "Select NTFP first", Toast.LENGTH_SHORT).show();
                }else{
                    Log.i("LogUnittt",ntfpModel.getUnit()+"");
                    ntfptype.showDropDown();
                }
            }
        });
        ntfptype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View arg0) {
                if (ntfpModel==null){
                    Toast.makeText(add_inventory.this, "Select NTFP first", Toast.LENGTH_SHORT).show();
                }else{
                    Log.i("LogUnittt",ntfpModel.getUnit()+"");
                    ntfptype.showDropDown();
                }
            }
        });


        member.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (collectorModel!=null && memberAdapter!=null ){
                    memberModel = memberAdapter.getItem(position);

                }else if(ImplementingAdaptermem == null)


                    Toast.makeText(add_inventory.this, "select collector first", Toast.LENGTH_SHORT).show();
            }
        });

        products.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ntfpModel = ntfpAdapter.getItem(position);
                if (ntfpModel!=null){
                    ntfpTypeAdapter = new ArrayAdapter<NTFP.ItemType>(add_inventory.this,R.layout.multiline_spinner_dropdown_item,dao.getItemTypesFromNid(ntfpModel.getNid()));
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
                }else
                    Toast.makeText(add_inventory.this, "select NTFP first", Toast.LENGTH_SHORT).show();
            }
        });


        AmoundCalc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (itemTypeModel!=null && ntfpgrade.getSelectedItemPosition()>0)
                {
                    double basePrice;
                    if(ntfpgrade.getSelectedItemPosition()==0){
                        basePrice =0;
                    }
                   else if (ntfpgrade.getSelectedItemPosition()==1)
                        basePrice = itemTypeModel.getGrade1Price();
                    else if (ntfpgrade.getSelectedItemPosition() == 2)
                        basePrice = itemTypeModel.getGrade2Price();
                    else
                        basePrice = itemTypeModel.getGrade3Price();

                    double quntityM = Double.valueOf(quantity.getText().toString());
//                    if(loseQ.getText().toString().isEmpty()){
//                         loseAmound = 00;
//                    }else {
//
//                        loseAmound = Double.valueOf(loseQ.getText().toString());
//                    }
                    loseAmound = 00;
                    String amundBy=(String) measurement.getSelectedItem();

                    if((flag == false) && (amundBy.equals("Kilogram")||amundBy.equals("Gram"))){
                        Toast.makeText(add_inventory.this, "Select required Unit first", Toast.LENGTH_SHORT).show();

                    }
                    if((flag == true) && (amundBy.equals("Litre")||amundBy.equals("Millilitre"))){
                        Toast.makeText(add_inventory.this, "Select required Unit first", Toast.LENGTH_SHORT).show();

                    }
                    else if(((flag != false) && (amundBy.equals("Kilogram")||amundBy.equals("Gram")))||((flag != true) && (amundBy.equals("Litre")||amundBy.equals("Millilitre")))){

                    if (quntityM > 0)
                    {
                        quntityM = (quntityM) - (loseAmound);


                        Log.i("getMEss",amundBy.toString()+"");
                        if(amundBy.equals("Kilogram")){
                            float fi=(Float.parseFloat(String.valueOf(basePrice * quntityM)));
                            amountPaid.setText (String.valueOf(fi));
                        }else if (amundBy.equals("Gram")){
                            String amound = String.valueOf(quntityM * basePrice);
                            float fi=(Float.parseFloat(amound)/1000);
                            amountPaid.setText(String.valueOf(fi));
                        }else if(amundBy.equals("Litre"))
                        {
                            amountPaid.setText(String.valueOf(quntityM * basePrice));
                        }
                        else if(amundBy.equals("Millilitre")){
                            String amound = String.valueOf(quntityM * basePrice);
                            float fi=(Float.parseFloat(amound)/1000);
                            amountPaid.setText(String.valueOf(fi));
                        }
                    }else {

                        amountPaid.setText("");
                    }
                    }


                }
                else{
                    Toast.makeText(add_inventory.this, "Select type and grade first", Toast.LENGTH_SHORT).show();
                }

            }
        });

//        quantity.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//    public void onTextChanged(CharSequence s, int start, int before, int count) {
//        if (itemTypeModel!=null && ntfpgrade.getSelectedItemPosition()>0)
//        {
//            double basePrice;
//            if (ntfpgrade.getSelectedItemPosition()==1)
//                basePrice = itemTypeModel.getGrade1Price();
//            else if (ntfpgrade.getSelectedItemPosition() == 2)
//                basePrice = itemTypeModel.getGrade2Price();
//            else
//                basePrice = itemTypeModel.getGrade3Price();
//
//
//            String quntityM=quantity.getText().toString();
//            String loseAmound = loseQ.getText().toString();
//            if (quntityM.length()>0) {
//                quntityM = String.valueOf(Double.parseDouble(quntityM) - Double.parseDouble(loseAmound));
//
//
//                String amundBy=(String) measurement.getSelectedItem();
//                Log.i("getMEss",amundBy.toString()+"");
//                if(amundBy.equals("kilogram")){
//                    amountPaid.setText(String.valueOf(Double.parseDouble(quntityM) * basePrice));
//                }else if (amundBy.equals("Gram")){
//                    String amound = String.valueOf(Double.parseDouble(quntityM) * basePrice);
//                    float fi=(Float.parseFloat(amound)/1000);
//
//                    amountPaid.setText(String.valueOf(fi));
//                }else if(amundBy.equals("Liter")){
//                    amountPaid.setText(String.valueOf(Double.parseDouble(quntityM) * basePrice));
//                }else if(amundBy.equals("Milliliter")){
//                    String amound = String.valueOf(Double.parseDouble(quntityM) * basePrice);
//                    float fi=(Float.parseFloat(amound)/1000);
//
//                    amountPaid.setText(String.valueOf(fi));
//                }
//
//            }else{
//                amountPaid.setText("");}
//        }
//        else{
//            Toast.makeText(add_inventory.this, "Select type and grade first", Toast.LENGTH_SHORT).show();
//        }
//      }
//
//      @Override
//      public void afterTextChanged(Editable s) {
//
//     }
//    });
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
                Toast.makeText(add_inventory.this,"Please "+et.getSelectedItem().toString() , Toast.LENGTH_SHORT).show();
                flag=false;
                break;
            }
        }
        return flag;
    }

    private class uploadTask extends AsyncTask<String,String,String> {

        private InventoryEntity model;
        private Model_payment payment;

        public uploadTask(InventoryEntity model,Model_payment payment){
            this.model = model;
            this.payment = payment;
        }
        private ProgressDialog dialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(add_inventory.this);
            dialog.setMessage("Submitting request");
            dialog.setCancelable(false);
            dialog.show();
            findViewById(R.id.spin_kit).setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, strings[0]);
            Request request = new Request.Builder()
                    .url("https://13.127.166.242/NTFPAPI/API/VSSUppdateStatusForCollectorStock")
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .build();
            try {
                okhttp3.Response response = client.newCall(request).execute();
                if (setStatus(response.body().string()))
                    return getResources().getString(R.string.synced);
                else
                    return getResources().getString(R.string.somedetailsnotsynced);
            } catch (Exception e) {
                e.printStackTrace();
                return e.getClass().getSimpleName();
            }
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();
            if (s.equals(getResources().getString(R.string.synced))){
                dao.insertInventory(model);
                db.insertData(payment);
                startActivity(new Intent(add_inventory.this, list_inventory.class));
            }
            else if(s.equals(getResources().getString(R.string.somedetailsnotsynced)))
                SnackBarUtils.WarningSnack(add_inventory.this,getString(R.string.somedetailsnotsynced));
            else if (s.equals("JSONException")||s.equals("SQLiteException"))
                SnackBarUtils.ErrorSnack(add_inventory.this,getString(R.string.somethingwentwrong));
            else
                SnackBarUtils.ErrorSnack(add_inventory.this,getString(R.string.servernotresponding));

        }
    }

    private boolean setStatus(String json) throws JSONException {
        Log.i("ksdks",json);
        boolean flag=true;
        JSONArray jsonArray=new JSONArray(json);
        for (int i=0;i<jsonArray.length();i++){
            JSONObject details=jsonArray.getJSONObject(i);
            if (details.getString("Status").equals("Success")) {
//                FCMNotifications.NotifyUser(add_inventory.this,"","","");
            }else{
                flag=false;
            }
        }
        return flag;
    }
    public void measurementSpinner(Spinner mSpinner, String compareValue) {
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
    }
    public void gradeSpinner(Spinner mSpinner, String compareValue) {
        ArrayAdapter<CharSequence> adapterQ = ArrayAdapter.createFromResource(this, R.array.ntfp_grade, R.layout.spinner_layout);
        adapterQ.setDropDownViewResource(R.layout.spinner_layout);
        mSpinner.setAdapter(adapterQ);
        int arg2;
        if (compareValue != null) {
            arg2 = adapterQ.getPosition(compareValue);
            mSpinner.setSelection(arg2);
            if (arg2 != 0) {
                spinnerflag = true;
            }
        }
    }

JSONArray jsonArray;
    private class locationFeach extends AsyncTask<Void, Void, String> {
        protected void onPreExecute() {
            super.onPreExecute();
//            findViewById(R.id.spin_kit).setVisibility(View.VISIBLE);

        }
        @Override
        protected String doInBackground(Void... params) {
            VSSUser user = new SharedPref(add_inventory.this).getVSS();
            try {
                OkHttpClient client = new OkHttpClient().newBuilder()
                        .build();
                // Replace with your API URL

                JSONObject jsonBody = new JSONObject();
                jsonBody.put("RangeId",  user.getRangeId());

                // Convert the JSON object to a string
                String requestBody = jsonBody.toString();
                Log.d("ReQQQQ111",requestBody);
                RequestBody requestJsonBody = RequestBody.create(MediaType.parse("application/json"), requestBody);
                // Replace with your request body, if needed

                Request request = new Request.Builder()
                        .url("https://vanasree.com/NTFPAPI/API/LocationList")
                        .method("POST", requestJsonBody)
                        .addHeader("Content-Type", "application/json")
                        .build();

                okhttp3.Response response = client.newCall(request).execute();
                String responseData = response.body().string();
                if (response.isSuccessful()) {
                    ResponseBody responseBodyy = response.body();
                    if(responseBodyy != null){
                        try {

                            jsonArray = new JSONArray(responseData);


                            // Check if the response contains location data
                            if (jsonArray.length() > 0 && jsonArray.getJSONObject(0).has("location_name")) {

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject item = jsonArray.getJSONObject(i);
                                    String locationName = item.getString("location_name");
                                    locationNames.add(locationName);
                                }
                                flagLocation= true;
                                flagNoLocation = false;


                            } else {
                                flagNoLocation = true;
                                flagLocation = false;
                                // Handle the case where "Status" is "Not Found!"

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                } else {

                    // Handle non-successful response
                    return "Error: " + response.code();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if(flagLocation == true){
                // Populate the Spinner with location names

                ArrayAdapter<String> adapter = new ArrayAdapter<>(add_inventory.this, android.R.layout.simple_spinner_item, locationNames);
                adapter.setDropDownViewResource(R.layout.spinner_layout);
                location.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                // Set an OnItemSelectedListener to handle item selection and get the corresponding id
                location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                        // Get the corresponding id from the JSON data
                        try {
                            JSONObject selectedItem = jsonArray.getJSONObject(position);
                            locationId = selectedItem.getInt("id");

                            // Use locationId as needed
                            // ...
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parentView) {
                        // Handle nothing selected
                        locationId = 0;
                    }
                });
                findViewById(R.id.spin_kit).setVisibility(View.GONE);
            }
            else if(flagNoLocation == true){
                ArrayAdapter<String> adapter = new ArrayAdapter<>(add_inventory.this, android.R.layout.simple_spinner_item, new String[]{"No location available"});
                adapter.setDropDownViewResource(R.layout.spinner_layout);
                location.setAdapter(adapter);
                locationId = 0;
                adapter.notifyDataSetChanged();
                findViewById(R.id.spin_kit).setVisibility(View.GONE);
            }
            // Handle the result of the HTTP request here
            // This method runs on the UI thread, so you can update UI elements here
            // For example, display the result in a TextView


        }
    }

}








