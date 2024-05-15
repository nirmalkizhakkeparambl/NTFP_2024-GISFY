package com.gisfy.ntfp.VSS.Shipment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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

import com.airbnb.lottie.L;
import com.gisfy.ntfp.Login.Models.ShipmentModel;
import com.gisfy.ntfp.R;
import com.gisfy.ntfp.RFO.Models.TransitNTFPModel;
import com.gisfy.ntfp.SqliteHelper.DBHelper;
import com.gisfy.ntfp.Utils.PermissionUtils;
import com.gisfy.ntfp.Utils.StaticChecks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class adapter_shipment  extends RecyclerView.Adapter<adapter_shipment.ViewHolder> {

    list_shipment activity;
    public final List<Model_shipment> list;
    private final DBHelper db;
    PermissionUtils permissionUtils;
    public adapter_shipment(List<Model_shipment> list, list_shipment activity) {
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
        final Model_shipment data = list.get(position);


        if (data.getSynced()==1){
            holder.cloud.setImageResource(R.drawable.vector_cloud_on);
            holder.downloadLayout.setVisibility(View.VISIBLE);
        }

        else{
            holder.cloud.setImageResource(R.drawable.vector_notsynced);
            holder.downloadLayout.setVisibility(View.GONE);

        }

        String title=activity.getResources().getString(R.string.shipmentno)+" "+data.getUid();
        String subtitle=activity.getString(R.string.transitpass)+": "+data.getTransitPass();

        holder.title.setText(title);
        holder.subtitle.setText(subtitle);


        holder.checkBox.setOnCheckedChangeListener(null);

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                data.setSelected(isChecked);
            }
        });
        holder.downloadLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                permissionUtils = new PermissionUtils();
                if (permissionUtils.checkPermission(activity, 1, view)) {
                    try {
                        activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://vanasree.com/NTFPIMS/VSSTransitPass.aspx?ShipmentNumber="+data.getUid())));
                    } catch (Exception e) {
                        e.getStackTrace();
                    }

                }
            }
        });

        if (activity.sync.getVisibility()==View.VISIBLE){
            holder.checkBox.setVisibility(View.GONE);
        }else{
//            holder.checkBox.setVisibility(View.VISIBLE);
            if (data.getSynced()==0) {
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
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
    private void showDailog(int position, Model_shipment  model){
        HorizontalScrollView scrollView = new HorizontalScrollView(activity);
        TableLayout tableLayout=new TableLayout(activity);
        tableLayout.setStretchAllColumns(true);
        tableLayout.removeAllViews();
        scrollView.addView(tableLayout);
        String[] titles=new String[]{"Transit NO","NTFP","Quantity","Stocks ID"};
        List<List<String>> lists=new ArrayList<>();
        Log.i("shipmentdata",model.getTransitPass());
        for (String transit:model.getTransitPass().split(",")){
            List<TransitNTFPModel> ntfps=db.getTransitNTFPModels(transit);
            Log.i("shipmentdata1",ntfps.toString());
            for (TransitNTFPModel model1:ntfps){
                List<String> list=new ArrayList<>();
                list.add(transit);
                list.add(model1.getnTFPmalayalamname());
                list.add(model1.getQuantity()+" "+model1.getUnit());
                list.add(String.valueOf(model1.getStocksId()));
                lists.add(list);
                Log.i("shipmentdataList",lists.toString());
            }
        }
        new StaticChecks(activity).setTableLayout(tableLayout,titles,lists);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                activity);
        alertDialogBuilder
                .setMessage(activity.getString(R.string.selectupdateordelete))
                .setCancelable(false)
                .setView(scrollView)
                .setPositiveButton(activity.getString(R.string.update),new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        Intent i=new Intent(activity, edit_shipment.class);
                        i.putExtra("uid",model.getUid());
                        i.putExtra("pc",model.getProcessing_center());
                        activity.startActivity(i);
                    }
                })
                .setNegativeButton(activity.getString(R.string.delete),new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        db.deleteData(DBHelper.SHIPMENTS,model.getUid());
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

    public List<Model_shipment> getSelectedItems() {
        List<Model_shipment> tempList=new ArrayList<>();
        for(Model_shipment model:list){
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
