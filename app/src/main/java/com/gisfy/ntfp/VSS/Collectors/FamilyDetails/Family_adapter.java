package com.gisfy.ntfp.VSS.Collectors.FamilyDetails;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.gisfy.ntfp.Utils.StaticChecks;
import com.gisfy.ntfp.VSS.Collectors.Collector;
import com.gisfy.ntfp.VSS.Collectors.add_collector;
import com.gisfy.ntfp.VSS.Collectors.edit_collector;
import com.gisfy.ntfp.VSS.Collectors.list_collectors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class Family_adapter extends RecyclerView.Adapter<Family_adapter.ViewHolder> {

    Activity activity;
    public final List<FamilyData> list;
    private final DBHelper db;
    public Family_adapter(List<FamilyData> list, Activity activity) {
        this.list = list;
        this.activity = activity;
        db=new DBHelper(activity);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.familydata, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final FamilyData data = list.get(position);

        holder.subtitle.setText(data.getVillage());

        holder.title.setText(data.getFamily_name());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    showDailog(position,data);
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
    private void showDailog(int position, FamilyData model){
        HorizontalScrollView scrollView = new HorizontalScrollView(activity);
        TableLayout tableLayout=new TableLayout(activity);
        tableLayout.setStretchAllColumns(true);
        tableLayout.removeAllViews();
        scrollView.addView(tableLayout);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                activity);
        alertDialogBuilder
                .setMessage(activity.getString(R.string.selectupdateordelete))
                .setCancelable(false)
                .setView(scrollView)
                .setPositiveButton(activity.getString(R.string.update),new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        Intent intent = new Intent(activity,CollectorsFamilyDetails.class);
                        intent.putExtra("FamilyData",model);
                        intent.putExtra("position",position);
                        if (activity instanceof edit_collector)
                                intent.putExtra("Edit","Edit");
                        activity.startActivityForResult(intent, 123);
                    }
                })
                .setNegativeButton(activity.getString(R.string.delete),new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        db.deleteData(DBHelper.FAMILYDETAILS,model.getUid());
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

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, subtitle;
        CardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            subtitle = itemView.findViewById(R.id.subtitle);
            cardView =itemView.findViewById(R.id.cardView);
        }
    }
}
