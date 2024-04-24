package com.gisfy.ntfp.VSS.Collectors;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Layout;
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
import com.gisfy.ntfp.VSS.Payment.Model_payment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class adapter_collectors  extends RecyclerView.Adapter<adapter_collectors.ViewHolder> {

    list_collectors activity;
    public final List<Collector> list;
    private final DBHelper db;
    public adapter_collectors(List<Collector> list, list_collectors activity) {
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
        final Collector data = list.get(position);

        holder.downloadLayout.setVisibility(View.GONE);
//        Log.i("dataSynced56",data.getSynced()+"");
        if (data.getSynced() == 1)
            holder.cloud.setImageResource(R.drawable.vector_cloud_on);
        else
            holder.cloud.setImageResource(R.drawable.vector_notsynced);

        holder.subtitle.setText("Major NTFP'S: "+data.getNtfps());

        holder.title.setText(data.getCollector_name());


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
    private void showDailog(int position, Collector model){
        HorizontalScrollView scrollView = new HorizontalScrollView(activity);
        TableLayout tableLayout=new TableLayout(activity);
        tableLayout.setStretchAllColumns(true);
        tableLayout.removeAllViews();
        scrollView.addView(tableLayout);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                activity);
        String[] titles=new String[]{"Name","DOB","ID Type","ID NO","User Name"};
        List<List<String>> lists= Collections.singletonList(Arrays.asList(model.getCollector_name(), model.getDob(), model.getIdtype(), model.getIdno(), model.getUsername()));
        new StaticChecks(activity).setTableLayout(tableLayout,titles,lists);
        alertDialogBuilder
                .setMessage(activity.getString(R.string.selectupdateordelete))
                .setCancelable(false)
                .setView(scrollView)
                .setPositiveButton(activity.getString(R.string.update),new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        Log.i("RadomNumber123",model.getUid()+"");
                        Intent i=new Intent(activity, edit_collector.class);
                        i.putExtra("uid",model.getUid());
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
                                        db.deleteData(DBHelper.COLLECTORS,model.getUid());
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

    public List<Collector> getSelectedItems() {
        List<Collector> tempList = new ArrayList<>();
        for(Collector model:list){
            if(model.isSelected())
                tempList.add(model);

        }
        return tempList;
    }
    @Override
    public int getItemCount()

    {
//        Log.i("dataLISTTSIZE",list.size()+"");
        int d = 0;
        try {
          d = list.size();
        }catch (Exception e){
        }
     return d;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
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
