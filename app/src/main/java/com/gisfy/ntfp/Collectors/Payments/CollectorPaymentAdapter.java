package com.gisfy.ntfp.Collectors.Payments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.gisfy.ntfp.R;
import com.gisfy.ntfp.SqliteHelper.DBHelper;
import com.gisfy.ntfp.SqliteHelper.Entity.CollectorPaymentsModel;
import com.gisfy.ntfp.Utils.StaticChecks;
import com.gisfy.ntfp.VSS.Collectors.Collector;
import com.gisfy.ntfp.VSS.Collectors.edit_collector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class CollectorPaymentAdapter extends RecyclerView.Adapter<CollectorPaymentAdapter.ViewHolder> {

    public final List<CollectorPaymentsModel> list;
    private Activity activity;
    public CollectorPaymentAdapter(List<CollectorPaymentsModel> list, Activity activity) {
        this.list = list;
        this.activity = activity;
    }

    @NonNull
    @Override
    public CollectorPaymentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.list_common_items, parent, false);
        return new CollectorPaymentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CollectorPaymentAdapter.ViewHolder holder, final int position) {
        final CollectorPaymentsModel data = list.get(position);
        holder.title.setText(data.getCollector());
        holder.subtitle.setText(data.getPurchaseOrderNumber());
        holder.cloud.setVisibility(View.GONE);
        holder.downloadimage.setVisibility(View.GONE);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDailog("Order No. "+data.getPurchaseOrderNumber(),data.getnTFP());
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

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, subtitle;
        CheckBox checkBox;
        CardView cardView;
        ImageView cloud,downloadimage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            downloadimage = itemView.findViewById(R.id.downloadimage);
            subtitle = itemView.findViewById(R.id.subtitle);
            cloud = itemView.findViewById(R.id.cloud);
            checkBox = itemView.findViewById(R.id.checkbox);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }

    private void showDailog(String title, List<CollectorPaymentsModel.NTFP> model){
        HorizontalScrollView scrollView = new HorizontalScrollView(activity);
        TableLayout tableLayout=new TableLayout(activity);
        tableLayout.setStretchAllColumns(true);
        tableLayout.removeAllViews();
        scrollView.addView(tableLayout);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                activity);
        String[] titles=new String[]{"NTFP","Grade 1","Grade 2","Grade 3","Total Cost"};
        List<List<String>> lists = new ArrayList<>();
        for (int i=0;i<model.size();i++){
            String grade1 = model.get(i).getGrade1Qty()+model.get(i).getUnit()+" - "+model.get(i).getGrade1Cost()+" Rs.";
            String grade2 = model.get(i).getGrade2Qty()+model.get(i).getUnit()+" - "+model.get(i).getGrade2Cost()+" Rs.";
            String grade3 = model.get(i).getGrade3Qty()+model.get(i).getUnit()+" - "+model.get(i).getGrade3Cost()+" Rs.";
          //  lists.add(Arrays.asList(model.get(i).getCollector(), model.get(0).getnTFP(),grade1,grade2,grade3,model.get(i).getTotalCost()));
          //lists.add(Arrays.asList( model.get(0).getnTFP(),grade1,grade2,grade3,model.get(i).getTotalCost()));
            lists.add(Arrays.asList( model.get(i).getnTFP(),grade1,grade2,grade3,model.get(i).getTotalCost()));
        }

        new StaticChecks(activity).setTableLayout(tableLayout,titles,lists);
        alertDialogBuilder
                .setMessage(title)
                .setCancelable(false)
                .setView(scrollView)
                .setPositiveButton(activity.getString(R.string.close),new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCancelable(true);
        alertDialog.show();

    }

}
