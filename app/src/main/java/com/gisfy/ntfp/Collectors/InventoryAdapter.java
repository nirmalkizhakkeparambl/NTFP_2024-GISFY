package com.gisfy.ntfp.Collectors;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.gisfy.ntfp.HomePage.Home;
import com.gisfy.ntfp.Login.Models.CollectorUser;
import com.gisfy.ntfp.R;
import com.gisfy.ntfp.SqliteHelper.DBHelper;
import com.gisfy.ntfp.SqliteHelper.Entity.InventoryRelation;
import com.gisfy.ntfp.SqliteHelper.NtfpDao;
import com.gisfy.ntfp.SqliteHelper.SynchroniseDatabase;
import com.gisfy.ntfp.Utils.SharedPref;
import com.gisfy.ntfp.Utils.StaticChecks;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import static com.gisfy.ntfp.SqliteHelper.DBHelper.STOCKSID;

import static java.security.AccessController.getContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class InventoryAdapter  extends RecyclerView.Adapter<InventoryAdapter.ViewHolder> {

    InventoryList activity;
    private InventoryAdapter adapter;
    public final List<InventoryRelation> list;
    private final DBHelper db;
    private NtfpDao dao;
    public boolean isinActionMode=false;
    private String inventoryId="";
    private List<InventoryRelation> shallowCopy =new ArrayList<>();
    public InventoryAdapter(List<InventoryRelation> list, InventoryList activity) {
        this.list = list;
        this.activity = activity;
        db=new DBHelper(activity);
        dao = SynchroniseDatabase.getInstance(activity).ntfpDao();
    }

    @NonNull
    @Override
    public InventoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.list_common_items, parent, false);
        return new InventoryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final InventoryAdapter.ViewHolder holder, final int position) {
        final InventoryRelation data = list.get(position);
        holder.downloadLayout.setVisibility(View.GONE);

        if (data.getInventory().isSynced())
            holder.cloud.setImageResource(R.drawable.vector_cloud_on);
        else
            holder.cloud.setImageResource(R.drawable.vector_notsynced);

            holder.subtitle.setText(data.getInventory().getDate());

//        holder.title.setText(data.getNtfp()!=null?data.getNtfp().getNTFPmalayalamname():"");
        if (data.getNtfp()!=null){
            holder.title.setText(data.getNtfp().getNTFPmalayalamname()+"("+data.getNtfp().getNTFPscientificname()+")");
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
//            holder.checkBox.setVisibility(View.VISIBLE);
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
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
    private void showDailog(int position, InventoryRelation model){
        HorizontalScrollView scrollView = new HorizontalScrollView(activity);
        TableLayout tableLayout=new TableLayout(activity);
        tableLayout.setStretchAllColumns(true);
        tableLayout.removeAllViews();
        scrollView.addView(tableLayout);
        String ntfpName = "";
        String memberName ="";

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                activity);
        String[] titles=new String[]{"Collector","Type","Quantity","Member Name"};


        if (model.getMember()!=null) {memberName = model.getMember().getName();}
        else memberName = "";
  //   List<List<String>> lists= Collections.singletonList(Arrays.asList(model.getNtfp().getNTFPscientificname(), model.getItemType().getMycase(), model.getInventory().getQuantity()+model.getInventory().getMeasurements(),memberName));
   // List<List<String>> lists= Collections.singletonList(Arrays.asList( model.getInventory().getColectname(),  model.getItemType().getMycase(), model.getInventory().getQuantity()+" "+model.getInventory().getMeasurements(),memberName));
        List<List<String>> lists= Collections.singletonList(Arrays.asList( model.getInventory().getColectname(),  model.getItemType().getMycase(), model.getInventory().getQuantity()+" "+model.getInventory().getMeasurements(),memberName));

        new StaticChecks(activity).setTableLayout(tableLayout,titles,lists);
        alertDialogBuilder
                .setMessage(activity.getString(R.string.selectupdateordelete))
                .setCancelable(false)
                .setView(scrollView)
                .setPositiveButton(activity.getString(R.string.update),new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        Intent i=new Intent(activity, CollectorInventory.class);
                        i.putExtra("uid",model.getInventory().getInventoryId());
                        activity.startActivity(i);
                    }
                })
                .setNegativeButton(activity.getString(R.string.delete),new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        db.deleteData(DBHelper.COLLECTOR_INV_TABLE,STOCKSID,model.getInventory().getInventoryId());
                        list.remove(position);
                        notifyItemRangeRemoved(0, list.size()+1);
                        notifyItemChanged(position);
                        dialog.dismiss();
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

    private JSONArray getjsonarray(InventoryRelation model){
        JSONArray jsonArray=new JSONArray();

        try {
            CollectorUser user = new SharedPref(activity.getApplicationContext()).getCollector();

                JSONObject object=new JSONObject();
                object.put("DivisionId",user.getDivisionId());
                object.put("RangeId",user.getRangeId());
                object.put("VSSId",user.getvSSId());
                if (model.getNtfp()!=null){
                    object.put("NTFPName",model.getNtfp().getNTFPscientificname());
                    object.put("NTFPId",model.getNtfp().getNid());
                }
                object.put("Unit",model.getInventory().getMeasurements());   //
                object.put("Quantity",model.getInventory().getQuantity());
//          object.put("Loss",00);
                object.put("DateTime",model.getInventory().getDate());
                object.put("Random",model.getInventory().getInventoryId());
                object.put("CollectorID",user.getCid());
                if (model.getItemType()!=null){
                    object.put("NTFPType",model.getItemType().getMycase());
                    object.put("NTFPTypeId",model.getItemType().getItemId()); }
                object.put("MemberId",model.getMember()!=null?model.getMember().getMemberId():-1);
                object.put("location_id",model.getInventory().getLocationId());

                jsonArray.put(object);
                Log.i("inventorydata",jsonArray.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonArray;
    }

    private boolean setStatus(String json) throws JSONException {

        Log.i("kishore",json);
        boolean flag=true;
        JSONArray jsonArray=new JSONArray(json);
        for (int i=0;i<jsonArray.length();i++){
            JSONObject details=jsonArray.getJSONObject(i);
            Log.i("data194response",details.getString("Status"));
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
            Log.i("jsonarraytoString93", getjsonarray(model).toString()+"");

            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, getjsonarray(model).toString());
            Request request = new Request.Builder()
                    .url("http://vanasree.com/NTFPAPI/API/CollectorInventoty")
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

    public List<InventoryRelation> getSelectedItems() {
        List<InventoryRelation> tempList=new ArrayList<>();
        for(InventoryRelation model:list){
            if(model.isSelected())
                tempList.add(model);
        }
        return tempList;
    }
    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, subtitle;
        CheckBox checkBox;
        CardView cardView;
        ImageView cloud;
        LinearLayout downloadLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            subtitle = itemView.findViewById(R.id.subtitle);
            cloud = itemView.findViewById(R.id.cloud);
            checkBox = itemView.findViewById(R.id.checkbox);
            cardView = itemView.findViewById(R.id.cardView);
            downloadLayout = itemView.findViewById(R.id.downloadLayout);

        }
    }
}
