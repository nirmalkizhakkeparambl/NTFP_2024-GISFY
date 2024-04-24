package com.gisfy.ntfp.VSS.Inventory;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class adapter_inventory  extends RecyclerView.Adapter<adapter_inventory.ViewHolder> {

    list_inventory activity;
    public final List<InventoryRelation> list;
    private final DBHelper db;
    private NtfpDao dao;
    String CCCC;

    public adapter_inventory(List<InventoryRelation> list, list_inventory activity) {
        this.list = list;
        this.activity = activity;
        db=new DBHelper(activity);


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
