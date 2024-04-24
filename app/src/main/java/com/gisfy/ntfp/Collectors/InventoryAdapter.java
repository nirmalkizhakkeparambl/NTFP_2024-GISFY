package com.gisfy.ntfp.Collectors;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
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
import com.gisfy.ntfp.SqliteHelper.Entity.MemberModel;
import com.gisfy.ntfp.SqliteHelper.NtfpDao;
import com.gisfy.ntfp.SqliteHelper.SynchroniseDatabase;
import com.gisfy.ntfp.Utils.StaticChecks;
import com.gisfy.ntfp.VSS.Collectors.Collector;
import com.gisfy.ntfp.VSS.Collectors.adapter_collectors;
import com.gisfy.ntfp.VSS.Collectors.edit_collector;
import com.gisfy.ntfp.VSS.Collectors.list_collectors;
import com.gisfy.ntfp.VSS.Inventory.Inventory;
import com.gisfy.ntfp.VSS.Inventory.adapter_inventory;
import com.gisfy.ntfp.VSS.Inventory.edit_inventory;
import com.gisfy.ntfp.VSS.Inventory.list_inventory;
import com.gisfy.ntfp.VSS.RequestForm.ChartAlert;
import com.gisfy.ntfp.VSS.RequestForm.StocksModel;
import com.gisfy.ntfp.VSS.RequestForm.adapter_request;
import com.gisfy.ntfp.VSS.RequestForm.list_request;
import com.google.android.material.checkbox.MaterialCheckBox;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import static com.gisfy.ntfp.SqliteHelper.DBHelper.STOCKSID;

public class InventoryAdapter  extends RecyclerView.Adapter<InventoryAdapter.ViewHolder> {

    InventoryList activity;
    public final List<InventoryRelation> list;
    private final DBHelper db;
    private NtfpDao dao;
    private String inventoryId="";
    public InventoryAdapter(List<InventoryRelation> list, InventoryList activity) {
        this.list = list;
        this.activity = activity;
        db=new DBHelper(activity);
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
