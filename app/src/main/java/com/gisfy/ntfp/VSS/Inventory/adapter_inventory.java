package com.gisfy.ntfp.VSS.Inventory;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gisfy.ntfp.Collectors.InventoryAdapter;
import com.gisfy.ntfp.HomePage.Home;
import com.gisfy.ntfp.Login.Models.CollectorUser;
import com.gisfy.ntfp.Login.Models.VSSUser;
import com.gisfy.ntfp.R;
import com.gisfy.ntfp.SqliteHelper.DBHelper;
import com.gisfy.ntfp.SqliteHelper.Entity.InventoryEntity;
import com.gisfy.ntfp.SqliteHelper.Entity.InventoryRelation;
import com.gisfy.ntfp.SqliteHelper.NtfpDao;
import com.gisfy.ntfp.SqliteHelper.SynchroniseDatabase;
import com.gisfy.ntfp.Utils.Constants;
import com.gisfy.ntfp.Utils.SharedPref;
import com.gisfy.ntfp.Utils.StaticChecks;
import com.gisfy.ntfp.VSS.RequestForm.StocksModel;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class adapter_inventory  extends RecyclerView.Adapter<adapter_inventory.ViewHolder> {

    list_inventory activity;
    public final List<InventoryRelation> list;
    private final DBHelper db;
    private NtfpDao dao;
    public boolean isinActionMode=false;
    String CCCC;
    private List<InventoryRelation> shallowCopy =new ArrayList<>();

    public adapter_inventory(List<InventoryRelation> list, list_inventory activity) {
        this.list = list;
        this.activity = activity;
        db=new DBHelper(activity);
        dao = SynchroniseDatabase.getInstance(activity).ntfpDao();


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.list_common_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final InventoryRelation data = list.get(position);

        Log.i("Sysnced 53",data.getInventory().isSynced()+"");

        if (data.getInventory().isSynced()){
            holder.cloud.setImageResource(R.drawable.vector_cloud_on);
        } else{
            holder.cloud.setImageResource(R.drawable.vector_notsynced);
        }
        holder.downloadLayout.setVisibility(View.GONE);

        String dateget =data.getInventory().getDate();
        String[] dateParts = dateget.split("-");
        String year = dateParts[0];
        String month = dateParts[1];
        String day = dateParts[2];

        String datanew = day+"-"+ month+"-"+year;

        String subtitle= "Quantity ="+"  "+data.getInventory().getQuantity()+" "+data.getInventory().getMeasurements()+" by "+data.getCollector()+" Date "+ datanew;
        holder.subtitle.setText(subtitle);
        holder.title.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
        if (data.getNtfp()!=null){
            holder.title.setText(data.getNtfp().getNTFPmalayalamname() +"(" +data.getNtfp().getNTFPscientificname()+")" );
        }
        holder.checkBox.setOnCheckedChangeListener(null);

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                data.setSelected(isChecked);
            }
        });

        if (activity.sync.getVisibility()==View.VISIBLE){
            holder.checkBox.setVisibility(View.GONE);
        }else{
            if (!data.getInventory().isSynced()) {
                holder.checkBox.setChecked(data.isSelected());
                holder.checkBox.setVisibility(View.VISIBLE);
            }
        }


        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activity.sync.getVisibility()==View.VISIBLE){
                    showDailog(position,data);
                }
//                else{
//                    data.setSelected(holder.checkBox.isChecked());
//                    holder.checkBox.setChecked(!holder.checkBox.isChecked());
//                }
            }
        });
    }

    private void showDailog(int position,InventoryRelation model){
        final InventoryRelation data = list.get(position);
        HorizontalScrollView scrollView = new HorizontalScrollView(activity);
        TableLayout tableLayout=new TableLayout(activity);
        tableLayout.setStretchAllColumns(true);
        tableLayout.removeAllViews();
        scrollView.addView(tableLayout);

        String[] titles=new String[]{"Collector","NTFP","Quantity"};

        String ntfpName = "";
        if (new SharedPref(activity).getLanguage().equals(Constants.MALAYALAM)){
            if (model.getNtfp()!=null){
                ntfpName=model.getNtfp().getNTFPmalayalamname();
            }
        }
        else{
            if (model.getNtfp()!=null){
                ntfpName=model.getNtfp().getNTFPscientificname();
            }
        }

        String dateget =model.getInventory().getDate();
        String[] dateParts = dateget.split("-");
        String year = dateParts[0];
        String month = dateParts[1];
        String day = dateParts[2];



        String datanew = day+"-"+ month+"-"+year;

        List<List<String>> lists= Collections.singletonList(Arrays.asList(model.getInventory().getColectname()+"", ntfpName, model.getInventory().getQuantity()+" "+model.getInventory().getMeasurements()));
        new StaticChecks(activity).setTableLayout(tableLayout,titles,lists);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                activity);
        alertDialogBuilder
                .setMessage(activity.getString(R.string.selectupdateordelete))
                .setCancelable(false)
                .setView(scrollView)
                .setPositiveButton(activity.getString(R.string.update),new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        Intent i=new Intent(activity, add_inventory.class);
                        i.putExtra("uid",model.getInventory().getInventoryId());
                        activity.startActivity(i);
                    }
                })
                .setNegativeButton(activity.getString(R.string.delete),new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {

                        AlertDialog.Builder builder1 = new AlertDialog.Builder(activity);
                        builder1.setMessage("Are you sure you want to delete?");
                        builder1.setCancelable(true);

                        builder1.setPositiveButton(
                                "Yes",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                        dao = SynchroniseDatabase.getInstance(activity).ntfpDao();
                                        dao.deleteInventory(model.getInventory().getInventoryId());
                                        list.remove(position);
                                        notifyItemRangeRemoved(0, list.size()+1);
                                        notifyItemChanged(position);
                                        dialog.dismiss();
                                    }
                                });

                        builder1.setNegativeButton(
                                "No",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                        AlertDialog alert11 = builder1.create();
                        alert11.show();
                    }
                });


        if(!model.getInventory().isSynced()) {
            alertDialogBuilder.setNeutralButton("Submit", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // Code to be executed when the "Neutral" button is clicked
                    // You can add your logic here
                    showSecondDialog( position,  model);
                }
            });
        }


        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCancelable(true);
        alertDialog.show();

    }
    public List<InventoryRelation> getSelectedItems() {
        List<InventoryRelation> tempList=new ArrayList<>();
        for(InventoryRelation model:list){
            if(model.isSelected())
                tempList.add(model);
        }
        return tempList;
    }
//    public List<Inventory> getSelectedItems() {
//        List<Inventory> tempList1=new ArrayList<>();
//        for(Inventory model:list){
//            if(model.isSelected())
//                tempList1.add(model);
//        }
//        return tempList1;
//    }
    @Override
    public int getItemCount() {
        return list.size();
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    private void showSecondDialog(int position, InventoryRelation model) {
        AlertDialog.Builder secondDialogBuilder = new AlertDialog.Builder(activity);

        secondDialogBuilder.setMessage("Are you sure you want to submit this item ?");

        // Set up the "Agree" button in the second dialog
        secondDialogBuilder.setPositiveButton("SUBMIT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Perform the desired action when "Agree" is clicked
                // For example, call PublishTask here
                PublishTask publishTask = new PublishTask(activity, model);
                publishTask.execute();

                // You can dismiss the second dialog if needed
                dialog.dismiss();
            }
        });

        // Set up the "Cancel" button in the second dialog
        secondDialogBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Dismiss the second dialog if "Cancel" is clicked
                dialog.dismiss();
            }
        });

        // Show the second dialog
        secondDialogBuilder.create().show();
    }
    private class PublishTask extends AsyncTask<String,String,String> {
        private WeakReference<Activity> activityReference;
        private InventoryRelation model;
        private Context context;
        void PublishTask(Activity activity) {
            activityReference = new WeakReference<>(activity);
        }
        PublishTask(Context context, InventoryRelation model) {
            this.context = context;
            this.model =model;
        }

        @Override

        protected void onPreExecute() {
            super.onPreExecute();

            if (activity != null) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Update UI elements here
                        View view = activity.findViewById(R.id.spin_kit);
                        if (view != null) {
                            view.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
//            findViewById(R.id.spin_kit).setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {


            OkHttpClient client = new OkHttpClient().newBuilder().build();
            MediaType mediaType = MediaType.parse("application/json");
            Log.i("jsonData188",jsonwriter(model)+"");
            RequestBody body = RequestBody.create(mediaType,jsonwriter(model).toString());
            Request request = new Request.Builder()
                    .url("https://vanasree.com/NTFPAPI/API/Stocks")
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .build();
            try {
                okhttp3.Response response = client.newCall(request).execute();
                String responseData = response.body().string();
                Log.i("response105",responseData+"");

                String syncedMessage = context.getString(R.string.synced);
                String somedetailsnotsyncedMessage = context.getString(R.string.somedetailsnotsynced);
                if (setStatus(responseData))

                    return syncedMessage;
                else
                    return somedetailsnotsyncedMessage;


            } catch (Exception e) {
                e.printStackTrace();
                return e.getClass().getSimpleName();
            }
        }
        @Override
        protected void onPostExecute(String s) {

            super.onPostExecute(s);
            if (context != null) {
                Intent i = new Intent(context, Home.class);

                context.startActivity(i);
                activity.findViewById(R.id.spin_kit).setVisibility(View.VISIBLE);
                String syncedMessage = context.getString(R.string.synced);
                String somedetailsnotsyncedMessage = context.getString(R.string.somedetailsnotsynced);
                if (s.equals(syncedMessage))
                    Toast.makeText(context, syncedMessage, Toast.LENGTH_SHORT).show();
                else if(s.equals(somedetailsnotsyncedMessage))
                    Toast.makeText(context, somedetailsnotsyncedMessage, Toast.LENGTH_SHORT).show();
                else if (s.equals("JSONException")||s.equals("SQLiteException"))
                    Toast.makeText(context, somedetailsnotsyncedMessage, Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(context, somedetailsnotsyncedMessage, Toast.LENGTH_SHORT).show();
                isinActionMode=false;
                ImageView upload = activity.findViewById(R.id.upload);
                ImageView sync = activity.findViewById(R.id.synchronise);
                View view = activity.findViewById(R.id.spin_kit);
                upload.setVisibility(View.GONE);
                sync.setVisibility(View.VISIBLE);

                view.setVisibility(View.GONE);
            }}

    }

    private JSONArray jsonwriter(InventoryRelation model){
        List<String> newobjects=new ArrayList<>();

        JSONArray jsonArray=new JSONArray();
        int memberId;
        if (model.getMember()!=null) {

            memberId = model.getMember().getMemberId();}

        else {


            Log.i("111111", "");
            memberId= -1;

        }
        int CIID;
        if(model.getCollector()!=null) {
            CIID = model.getCollector().getCid();
            Log.i("CIID1", CIID + "");
        }else{ CIID = 0;}
        try {
            VSSUser user = new SharedPref(activity.getApplicationContext()).getVSS();
            JSONObject object=new JSONObject();

            object.put("Random",model.getInventory().getInventoryId());
            object.put("DivisionId",user.getDivisionId());
            object.put("RangeId",user.getRangeId());
            object.put("VSSId",user.getVid());
            object.put("FromVSSId",model.getInventory().getVssnamesle());
            if (model.getNtfp()!=null){
                object.put("NTFPName",model.getNtfp().getNTFPscientificname());
                object.put("NTFPId",model.getNtfp().getNid());
            }
            object.put("MemberId",memberId);
            object.put("CollectorID",CIID);
            object.put("NTFPTypeId",model.getItemType().getItemId());
            object.put("Unit",model.getInventory().getMeasurements());   //
            object.put("Quantity",model.getInventory().getQuantity());
            object.put("NTFPType",model.getItemType().getMycase());
            object.put("Collector",model.getInventory().getColectname());
            object.put("Amount",model.getInventory().getPrice());
            object.put("Loss",00);
            object.put("DateandTime",model.getInventory().getDate());

            object.put("location_id",model.getInventory().getLocationId());

            jsonArray.put(object);
            Log.i("inventorydataVSS",jsonArray.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonArray;
    }

    private boolean setStatus(String json) throws JSONException {
        boolean flag=true;
        Log.i("kishore",json);
        JSONArray jsonArray=new JSONArray(json);
        for (int i=0;i<jsonArray.length();i++){
            JSONObject details=jsonArray.getJSONObject(i);
            if (details.getString("Status").equals("Success")) {
                dao.setSyncStatus(true,details.getString("Random"));
            }else{
                flag=false;
                dao.setSyncStatus(false,details.getString("Random"));
            }
        }
        shallowCopy.clear();
        shallowCopy.addAll(dao.getAllInventories());
        return flag;
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, subtitle;
        CheckBox checkBox;
        CardView cardView;
        ImageView cloud,download;
        LinearLayout downloadLayout;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            title = itemView.findViewById(R.id.title);
            subtitle = itemView.findViewById(R.id.subtitle);
            cloud = itemView.findViewById(R.id.cloud);
            checkBox = itemView.findViewById(R.id.checkbox);
            cardView = itemView.findViewById(R.id.cardView);
            download = itemView.findViewById(R.id.downloadimage);
            downloadLayout= itemView.findViewById(R.id.downloadLayout);
        }
    }
}
