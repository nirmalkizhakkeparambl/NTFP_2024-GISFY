package com.gisfy.ntfp.VSS.Payment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.gisfy.ntfp.Utils.SnackBarUtils;
import com.gisfy.ntfp.Utils.StaticChecks;
import com.gisfy.ntfp.VSS.Payment.Recieved.ReceivedPaymentsActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class PaymentsAdapter extends RecyclerView.Adapter<PaymentsAdapter.ViewHolder> {


    MakePaymentsActivity makePaymentsActivity ;
    public final List<Model_payment> list;
    Context context;
    OnClickListener listener;
    public PaymentsAdapter(List<Model_payment> list, MakePaymentsActivity activity,OnClickListener listener) {
        this.list = list;
        this.makePaymentsActivity = activity;
        this.context=activity;
        this.listener=listener;
    }
    public interface OnClickListener {
        void onClick(Model_payment v);
    }

    @NonNull
    @Override
    public PaymentsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_common_items, parent, false);
        return new PaymentsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final PaymentsAdapter.ViewHolder holder, final int position) {
        final Model_payment data = list.get(position);
//        if (data.getPaymentType().equals("Made")) {
        Log.i("getProduct62",data.getProduct());
            String title = context.getString(R.string.paidfor)+data.getProduct();
            holder.downloadLayout.setVisibility(View.GONE);
            holder.title.setText(title);
            holder.downloadimage.setVisibility(View.GONE);
            holder.title.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
            String subtitle = context.getString(R.string.amountof)+data.getAmount()+context.getString(R.string.quantityfor)+data.getQuantity()+data.getMeasurement();
            holder.subtitle.setText(subtitle);
            if (data.getSynced()==1)
                holder.cloud.setImageResource(R.drawable.vector_cloud_on);
            else
                holder.cloud.setImageResource(R.drawable.vector_notsynced);

            if(data.getStatus()==0){
                holder.cloud.setVisibility(View.GONE);
            }else{
                holder.cloud.setVisibility(View.VISIBLE);
            }
            if (makePaymentsActivity.sync.getVisibility()==View.VISIBLE){
                holder.checkBox.setVisibility(View.GONE);
            }else{
                if (data.getSynced()==0) {
                    holder.checkBox.setChecked(data.isSelected());
                    holder.checkBox.setVisibility(View.VISIBLE);
                }
            }
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showMakePaymentsDailog(data);
                }
            });


            holder.checkBox.setOnCheckedChangeListener(null);

            holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    data.setSelected(isChecked);
                }
            });
//        }
    }


    public List<Model_payment> getSelectedItems() {
        List<Model_payment> tempList=new ArrayList<>();
        for(Model_payment model:list){
            if(model.isSelected())
                tempList.add(model);
        }
        return tempList;
    }



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
        ImageView cloud,downloadimage;
        LinearLayout downloadLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            downloadimage = itemView.findViewById(R.id.downloadimage);
            title = itemView.findViewById(R.id.title);
            subtitle = itemView.findViewById(R.id.subtitle);
            cloud = itemView.findViewById(R.id.cloud);
            checkBox = itemView.findViewById(R.id.checkbox);
            cardView = itemView.findViewById(R.id.cardView);
            downloadLayout = itemView.findViewById(R.id.downloadLayout);
        }
    }

    private void showMakePaymentsDailog( Model_payment model){
        HorizontalScrollView scrollView = new HorizontalScrollView(context);
        TableLayout tableLayout=new TableLayout(context);
        tableLayout.setStretchAllColumns(true);
        tableLayout.removeAllViews();
        scrollView.addView(tableLayout);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        String[] titles=new String[]{"ID","NTFP","Quantity","Amount"};
        List<List<String>> lists= Collections.singletonList(Arrays.asList(model.getUid(), model.getProduct(), model.getQuantity()+model.getMeasurement(), model.getAmount()));
        new StaticChecks(context).setTableLayout(tableLayout,titles,lists);
        alertDialogBuilder
                .setMessage(context.getString(R.string.paymentdetails))
                .setCancelable(false)
                .setView(scrollView)
                .setNegativeButton(context.getString(R.string.close),new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.dismiss();
                    }
                });
        if (model.getStatus()==0) {
            alertDialogBuilder.setPositiveButton(context.getString(R.string.paid), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    listener.onClick(model);
                }
            });
        }
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCancelable(true);
        alertDialog.show();
    }
}
