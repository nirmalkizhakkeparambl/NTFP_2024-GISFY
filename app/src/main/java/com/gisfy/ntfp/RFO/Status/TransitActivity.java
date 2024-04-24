package com.gisfy.ntfp.RFO.Status;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gisfy.ntfp.API.RetrofitClient;
import com.gisfy.ntfp.Login.Models.RFOUser;
import com.gisfy.ntfp.R;
import com.gisfy.ntfp.RFO.Adapters.PositionClickListener;
import com.gisfy.ntfp.RFO.Adapters.TransitPassAdapter;
import com.gisfy.ntfp.RFO.Models.DFOACKModel;
import com.gisfy.ntfp.RFO.Models.DFONTFPModel;
import com.gisfy.ntfp.RFO.Models.StatusModel;
import com.gisfy.ntfp.RFO.Models.TransitNTFPModel;
import com.gisfy.ntfp.RFO.Models.TransitPassModel;
import com.gisfy.ntfp.RFO.Models.VSSACKModel;
import com.gisfy.ntfp.RFO.Models.VSSNTFPModel;
import com.gisfy.ntfp.SqliteHelper.Entity.MemberModel;
import com.gisfy.ntfp.SqliteHelper.NtfpDao;
import com.gisfy.ntfp.SqliteHelper.SynchroniseDatabase;
import com.gisfy.ntfp.Utils.FCMNotifications;
import com.gisfy.ntfp.Utils.SharedPref;
import com.gisfy.ntfp.Utils.SnackBarUtils;
import com.gisfy.ntfp.Utils.StaticChecks;
import com.gisfy.ntfp.VSS.RequestForm.ChartAlert;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.util.Pair;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.gisfy.ntfp.RFO.Status.ToVSS.setDate;
import static com.gisfy.ntfp.Utils.StaticChecks.isDateInRange;

public class TransitActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private SharedPref pref;
    private List<TransitPassModel> list=new ArrayList<>();
    private TransitPassAdapter adapter;
    public ImageView cancel;
    public TableLayout tableLayout;
    public TextInputEditText remarks;
    public TextInputLayout remarksLayout;
    public Button reject,accept;
    public TextView fromdate;
    public static TextView todate;
    private final String datefrom=null;
    private String dateto=null;
    public Spinner reportSpinner,vssSpinner,statusSpinner;
    public SpinKitView spinKitView;
    public ConstraintLayout bottomSheet;
    private Button filter,filterCancel;
    private final List<String> vssList=new ArrayList<>();
    private ArrayAdapter<String> vssAdapter;
    private NtfpDao dao;
    private int mmID;
    private MemberModel memberModel = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transit);
        tabLayout=(TabLayout)findViewById(R.id.tabLayout);
        pref=new SharedPref(this);
        initViews();
        RFOUser user=pref.getRFO();
        final HashMap<String,String> json=new HashMap<>();
        json.put("DivisionId",user.getDivisionId()+"");
        json.put("RangeId", user.getRangeId()+"");


        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.pending)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.approvedrejected)));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter=new TransitPassAdapter(list, TransitActivity.this, new PositionClickListener() {
            @Override
            public void itemClicked(TransitPassModel position) {
                modelClick(position);
            }

            @Override
            public void itemClicked(DFOACKModel position) {

            }
            @Override
            public void itemClicked(VSSACKModel position) {

            }
        });
        recyclerView.setAdapter(adapter);

        getData(json);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                try {
                    updateTab(tab.getPosition());
                } catch (Exception e) {
                    e.printStackTrace();
                    SnackBarUtils.ErrorSnack(TransitActivity.this,getString(R.string.somethingwentwrong));
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void initViews() {
        remarksLayout=findViewById(R.id.remarks_layout);
        remarks=findViewById(R.id.remarks);
        accept=findViewById(R.id.accept);
        reject=findViewById(R.id.reject);
        fromdate=findViewById(R.id.from);
        todate=findViewById(R.id.to);
        cancel=findViewById(R.id.cancel_button);
        tableLayout=findViewById(R.id.tableLayout);
        reportSpinner=findViewById(R.id.spinner);
        spinKitView=findViewById(R.id.spin_kit);
        bottomSheet=findViewById(R.id.bottomSheet_Layout);
        bottomSheet.bringToFront();
        reportSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (reportSpinner.getSelectedItem().toString().equals("Other")) {
                    findViewById(R.id.remarks_layout).setVisibility(View.VISIBLE);
                }else {
                    findViewById(R.id.remarks_layout).setVisibility(View.GONE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        ArrayAdapter<String> pcAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item,getResources().getStringArray(R.array.transit_report));
        reportSpinner.setAdapter(pcAdapter);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.bottomSheet_Layout).setVisibility(View.GONE);
            }
        });
        findViewById(R.id.backpayment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String formattedDate = df.format(c);
        fromdate.setText(formattedDate);
        String outputDate = addDays(new Date(),10); //for current date
        todate.setText(outputDate);

        todate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getfromdate = fromdate.getText().toString().trim();
                String getfrom[] = getfromdate.split("-");
                int year,month,day;
                year= Integer.parseInt(getfrom[2]);
                month = Integer.parseInt(getfrom[1]);
                day = Integer.parseInt(getfrom[0]);
                final Calendar c = Calendar.getInstance();
                c.set(year,month,day+1);
                DatePickerDialog datePickerDialog = new DatePickerDialog(TransitActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                dateto = (dayOfMonth ) + "-" + (month + 1) + "-" +year ;
                                todate.setText("  "+dateto);
                            }
                        }, year, month, day);

                datePickerDialog.show();
            }
        });
        initFilterViews();

    }

    private void initFilterViews() {
        filter = (Button) findViewById(R.id.save);
        filterCancel = (Button) findViewById(R.id.cancel);
        vssSpinner = (Spinner) findViewById(R.id.title_List);
        EditText from = (EditText) findViewById(R.id.fromDate);
        EditText to = (EditText) findViewById(R.id.toDate);
        statusSpinner = (Spinner) findViewById(R.id.statusSpinner);

        filterCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slide();
            }
        });
        findViewById(R.id.filter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slide();
            }
        });
        vssList.add("Select any option");
        Log.i("vssList258",vssList.size()+"");
        vssAdapter = new ArrayAdapter<String>(TransitActivity.this,
                android.R.layout.simple_spinner_dropdown_item,vssList);
        vssSpinner.setAdapter(vssAdapter);

        ArrayAdapter<String> statusadap = new ArrayAdapter<String>(TransitActivity.this,
                android.R.layout.simple_spinner_dropdown_item,getResources().getStringArray(R.array.status_spinner_accepted));
        statusSpinner.setAdapter(statusadap);
        setDate(TransitActivity.this,to);
        setDate(TransitActivity.this,from);

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RFOUser user=pref.getRFO();
                String startTime="";
                final HashMap<String,String> json=new HashMap<>();

                String dateget = from.getText().toString();
                String[] dateParts = dateget.split("-");
                String year = dateParts[0];
                String month = dateParts[1];
                String day = dateParts[2];
                String fromdate = day+"-"+ month+"-"+year;

                String toget = to.getText().toString();
                String[] todateParts = toget.split("-");
                String toyear = todateParts[0];
                String tomonth = todateParts[1];
                String today = todateParts[2];
                String todate = today+"-"+ tomonth+"-"+toyear;


                json.put("DivisionId",user.getDivisionId()+"");
                json.put("RangeId", user.getRangeId()+"");
                json.put("FromDate",fromdate);
                if (!to.getText().toString().equals(""))
                    Log.i("todateCheck293",todate);
                json.put("ToDate",todate);
                if (vssSpinner.getSelectedItemPosition()!=0)
                    json.put("VSS",vssSpinner.getSelectedItem().toString());
                if (statusSpinner.getSelectedItemPosition()!=0)
                    json.put("TransitStatus",statusSpinner.getSelectedItem().toString());
                Log.i("json",json.toString());
                getData(json);
                slide();
            }
        });
    }

    public void slide(){
        if(findViewById(R.id.filterLayout).getVisibility()==View.GONE) {
            if (tabLayout.getSelectedTabPosition()==0){
                findViewById(R.id.statustv).setVisibility(View.GONE);
                findViewById(R.id.statusSpinner).setVisibility(View.GONE);
            }else{
                findViewById(R.id.statustv).setVisibility(View.VISIBLE);
                findViewById(R.id.statusSpinner).setVisibility(View.VISIBLE);
            }
            Animation bottomUp = AnimationUtils.loadAnimation(this,
                    R.anim.slide_up);
            ViewGroup hiddenPanel = (ViewGroup) findViewById(R.id.filterLayout);
            hiddenPanel.startAnimation(bottomUp);
            hiddenPanel.setVisibility(View.VISIBLE);
            hiddenPanel.bringToFront();
        }else{
            Animation bottomUp = AnimationUtils.loadAnimation(this,
                    R.anim.slide_down);
            ViewGroup hiddenPanel = (ViewGroup)findViewById(R.id.filterLayout);
            hiddenPanel.startAnimation(bottomUp);
            hiddenPanel.setVisibility(View.GONE);
        }
    }

    public void getData(HashMap<String,String> json){
        list.clear();
        Log.i("json 314",json.toString());
        findViewById(R.id.spin_kit).setVisibility(View.VISIBLE);
        Call<List<TransitPassModel>> call = RetrofitClient.getInstance().getMyApi().getTransitPass(json);
        call.enqueue(new Callback<List<TransitPassModel>>() {
            @Override
            public void onResponse(Call<List<TransitPassModel>> call, Response<List<TransitPassModel>> response) {
                if (response.isSuccessful()){
                    list=response.body();
                    Log.i("listresponse334",response.body()+"");
                    try {
                        updateTab(tabLayout.getSelectedTabPosition());
                    } catch (Exception e) {
                        e.printStackTrace();
                        SnackBarUtils.ErrorSnack(TransitActivity.this,getString(R.string.nodata));
                    }
                }else{
                    SnackBarUtils.ErrorSnack(TransitActivity.this,  getString(R.string.servernotresponding));

                }
                findViewById(R.id.spin_kit).setVisibility(View.GONE);
            }
            @Override
            public void onFailure(Call<List<TransitPassModel>> call, Throwable t) {

                SnackBarUtils.ErrorSnack(TransitActivity.this, getString(R.string.servernotresponding));
            }
        });
    }

    private void updateTab(int position){
        vssList.clear();
        vssList.add("Select Vss");
        bottomSheet.setVisibility(View.GONE);
        if (position==0){
            List<TransitPassModel> pending=new ArrayList<>();
            for (TransitPassModel model:list){
           //     Log.i("MemberIddata361",model.getMemberID()+"");
//                if(TransitPassModel!= null) {
//                    mmID = model.getMemberID();
//                }
            //    Log.i("MembermmID",mmID+"");
              if ((model.getTransitStatus()!=null)&&(!model.getTransitStatus().equals("Accepted")&&!model.getTransitStatus().equals("Rejected") )){
           //     if (model.getTransitStatus().equals(null)){
                    pending.add(model);
                    if (!vssList.contains(model.getvSSName()))
                        vssList.add(model.getvSSName());
                }
            }
            adapter.updateData(pending);
        }else{
            List<TransitPassModel> other=new ArrayList<>();
            for (TransitPassModel model:list){
                if (model.getTransitStatus().equals("Accepted")||model.getTransitStatus().equals("Rejected")){
                    other.add(model);
                    if (!vssList.contains(model.getvSSName()))
                        vssList.add(model.getvSSName());
                }
            }
            adapter.updateData(other);
        }
        Log.i("vssListsize389Trans",vssList.size()+"");

        adapter.notifyDataSetChanged();
        vssAdapter.notifyDataSetChanged();
        if (findViewById(R.id.filterLayout).getVisibility()==View.VISIBLE){
            Animation bottomUp = AnimationUtils.loadAnimation(this,
                    R.anim.slide_down);
            ViewGroup hiddenPanel = (ViewGroup)findViewById(R.id.filterLayout);
            hiddenPanel.startAnimation(bottomUp);
            hiddenPanel.setVisibility(View.GONE);
        }
    }

    private void modelClick(TransitPassModel model) {
        if (!model.getTransitStatus().equals("Accepted") && !model.getTransitStatus().equals("Rejected")) {
            reject.setVisibility(View.VISIBLE);
            accept.setText(getString(R.string.accept));
            reportSpinner.setVisibility(View.GONE);
            remarksLayout.setVisibility(View.GONE);

            String collectorName = model.getCollectorName();
            String[] titles = getResources().getStringArray(R.array.transit_titles2);
            List<List<String>> rows = new ArrayList<>();
            for (TransitNTFPModel ntfpModel : model.getnTFP()) {
                List<String> row = new ArrayList<>();
                row.add(ntfpModel.getnTFPName());
                row.add(ntfpModel.getQuantity()+ntfpModel.getUnit());
                row.add(collectorName);
                rows.add(row);
            }
            new StaticChecks(TransitActivity.this).setTableLayout(tableLayout, titles, rows);
            bottomSheet.setVisibility(View.VISIBLE);
            StaticChecks.slideInFromBottom(TransitActivity.this,bottomSheet);

            accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!model.getTransitStatus().equals("Accepted")&&!model.getTransitStatus().equals("Rejected")){
                        if (reportSpinner.getVisibility() == View.VISIBLE) {
                            if (reportSpinner.getSelectedItemPosition()!=0){
                                if (!reportSpinner.getSelectedItem().toString().equals("Other")){
                                    setACK( model.getTransUniqueId(), "Rejected",reportSpinner.getSelectedItem().toString(),model);
                                }else{
                                    setACK(model.getTransUniqueId(), "Rejected",remarks.getText().toString(),model);
                                }
                            }else{
                                SnackBarUtils.ErrorSnack(TransitActivity.this,"Select any reason");
                            }
                        } else {
                            setACK(model.getTransUniqueId(), "Accepted",remarks.getText().toString(),model);
                        }
                    }
                }
            });
            reject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    reject.setVisibility(View.GONE);
                    reportSpinner.setVisibility(View.VISIBLE);
                    accept.setText("Submit");
                }
            });
        }
        else{
            String[] titles = getResources().getStringArray(R.array.transit_titles);
            List<List<String>> rows = new ArrayList<>();

            for (TransitNTFPModel ntfpModel : model.getnTFP()) {
                List<String> row = new ArrayList<>();
                row.add(ntfpModel.getnTFPName());
                row.add(ntfpModel.getQuantity()+ntfpModel.getUnit());
//                dao = SynchroniseDatabase.getInstance(TransitActivity.this).ntfpDao();
//                memberModel = dao.getMemberFromMemberId(model.getMemberID());
//                if(memberModel.getName().isEmpty() || memberModel.getName().equals(null) ){
//                    row.add(String.valueOf(ntfpModel.getMemberID()));
//                }else { row.add(memberModel.getName());}
                row.add(String.valueOf(model.getMemberID()));




                rows.add(row);
            }
            new ChartAlert(TransitActivity.this,titles,rows).show();
        }
    }

    private void setACK( final String id, final String status, final String remarks, TransitPassModel model){
        spinKitView.setVisibility(View.VISIBLE);


        final HashMap<String,String> json=new HashMap<>();
        json.put("TransUniqueId",id);
        json.put("TransitStatus", status);
        json.put("Remarks", remarks);
        String dateget = fromdate.getText().toString();
        String[] dateParts = dateget.split("-");
        String year = dateParts[0];
        String month = dateParts[1];
        String day = dateParts[2];
        String fromdate = day+"-"+ month+"-"+year;
        json.put("FromDate", fromdate);

        String toget = todate.getText().toString();
        String[] todateParts = toget.split("-");
        String toyear = todateParts[0];
        String tomonth = todateParts[1];
        String today = todateParts[2];
        String todate = today+"-"+ tomonth+"-"+toyear;

        json.put("ToDate", todate);
        json.put("MemberId", String.valueOf(model.getMemberID()));
        json.put("CollectorId", String.valueOf(model.getCollectorID()));
        Log.i("checkdata",json+"");
        Call<List<StatusModel>> call = RetrofitClient.getInstance().getMyApi().setTransitAck(json);
        call.enqueue(new Callback<List<StatusModel>>() {
            @Override
            public void onResponse(Call<List<StatusModel>> call, retrofit2.Response<List<StatusModel>> response) {
                if (response.isSuccessful()){
                    Log.i("body",response.body().get(0).getStatus());
                    if (response.body().get(0).getStatus().equals("Success")){
                        RFOUser user=pref.getRFO();
                        if (status.equals("Accepted")) {
                            FCMNotifications.NotifyUser(TransitActivity.this,model.getfCMID(),"Transit Request: "+id,"you can move the transit as your request was approved");
                            final HashMap<String,String> json=new HashMap<>();
                            json.put("DivisionId",user.getDivisionId()+"");
                            json.put("RangeId", user.getRangeId()+"");
                            getData(json);
                        }
                        else {
                            FCMNotifications.NotifyUser(TransitActivity.this,model.getfCMID(),"Transit Request: "+id,"Rejected due to "+remarks);
                            final HashMap<String,String> json=new HashMap<>();
                            json.put("DivisionId",user.getDivisionId()+"");
                            json.put("RangeId", user.getRangeId()+"");
                            getData(json);
                        }
                    }else{
                        SnackBarUtils.ErrorSnack(TransitActivity.this,getString(R.string.unabletofetch));
                    }
                }
                spinKitView.setVisibility(View.GONE);
                bottomSheet.setVisibility(View.GONE);
            }
            @Override
            public void onFailure(Call<List<StatusModel>> call, Throwable t) {
                spinKitView.setVisibility(View.GONE);
                bottomSheet.setVisibility(View.GONE);
                SnackBarUtils.ErrorSnack(TransitActivity.this,getString(R.string.servernotresponding));
            }
        });
    }

    public static String addDays(Date startDate,int numberOfDays) {
        Calendar c = Calendar.getInstance();
        c.setTime(startDate);
        c.add(Calendar.DAY_OF_WEEK,numberOfDays);
        SimpleDateFormat dateFormat=new SimpleDateFormat("dd-MM-yyyy",  Locale.ENGLISH);
        String resultDate=dateFormat.format(c.getTime());
        return resultDate;
    }
}