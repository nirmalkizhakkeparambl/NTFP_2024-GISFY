package com.gisfy.ntfp.VSS.Payment.Recieved;

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
import android.widget.TableLayout;
import android.widget.TextView;

import com.gisfy.ntfp.R;
import com.gisfy.ntfp.SqliteHelper.DBHelper;
import com.gisfy.ntfp.Utils.SnackBarUtils;
import com.gisfy.ntfp.Utils.StaticChecks;
import com.gisfy.ntfp.VSS.Payment.MakePaymentsActivity;
import com.gisfy.ntfp.VSS.Payment.Model_payment;
import com.gisfy.ntfp.VSS.Payment.PaymentsAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class ReceivedAdapter extends RecyclerView.Adapter<ReceivedAdapter.ViewHolder> {


    private final ReceivedPaymentsActivity activity;
    public final List<ReceivedPaymentsModel> list;
    private final ReceivedAdapter.OnClickListener listener;

    public ReceivedAdapter(List<ReceivedPaymentsModel> list, ReceivedPaymentsActivity activity, ReceivedAdapter.OnClickListener listener) {
        this.list = list;
        this.activity = activity;
        this.listener=listener;
    }
    public interface OnClickListener {
        void onClick(ReceivedPaymentsModel model);
    }
    @NonNull
    @Override
    public ReceivedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.list_payment, parent, false);
        return new ReceivedAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ReceivedAdapter.ViewHolder holder, final int position) {
        final ReceivedPaymentsModel data = list.get(position);

        if (data.getPaidDate()!=null){
                    String dateget = data.getPaidDate();
        String[] dateParts = dateget.split("-");
        String year = dateParts[0];
        String month = dateParts[1];
        String day = dateParts[2];
        String datanew = day+"-"+ month+"-"+year;
            holder.date.setText(datanew);
        }

        holder.title.setText(data.getPurchaseordernumber());
        holder.amount.setText(data.getAmount());

        if (data.getPayStatus().equals("Pending")) {
            holder.icon.setImageResource(R.drawable.vector_time);
            holder.amount.setTextColor(activity.getResources().getColor(R.color.yellow));
            holder.subtitle.setText(data.getPcName());
            String from = activity.getString(R.string.shipmentno)+data.getShipmentNumber();
            holder.from.setText(from);
            holder.rupees.setText("");
        }else {
            holder.icon.setImageResource(R.drawable.vector_payment_green);
            holder.amount.setTextColor(activity.getResources().getColor(R.color.green_payment));
            holder.subtitle.setText(data.getChequeNo());
            String from = activity.getString(R.string.fromfor)+data.getdFOName();
            holder.from.setText(from);
            holder.rupees.setText(activity.getResources().getString(R.string.rupees));
        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPaymentsDailog(data);
            }
        });
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
        TextView from, date,amount,title,subtitle,rupees;
        CardView cardView;
        ImageView icon;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            from = itemView.findViewById(R.id.addtransactionStatus);
            date = itemView.findViewById(R.id.transaction_Time);
            amount = itemView.findViewById(R.id.transaction_cash);
            title = itemView.findViewById(R.id.title);
            subtitle = itemView.findViewById(R.id.subtitle);
            cardView = itemView.findViewById(R.id.cardView);
            icon = itemView.findViewById(R.id.transaction_Image);
            rupees = itemView.findViewById(R.id.rupees);
        }
    }
    private void showPaymentsDailog( ReceivedPaymentsModel model){
        HorizontalScrollView scrollView = new HorizontalScrollView(activity);
        TableLayout tableLayout=new TableLayout(activity);
        tableLayout.setStretchAllColumns(true);
        tableLayout.removeAllViews();
        scrollView.addView(tableLayout);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                activity);
        String[] titles=new String[]{"Order No","Cheque No","Shipment No","PcName","Bank","IFSC","Received"};
        List<List<String>> lists= Collections.singletonList(Arrays.asList(model.getPurchaseordernumber(), model.getChequeNo(), model.getShipmentNumber(), model.getPcName(),model.getBankName(), model.getiFSCcode(),model.isReceivedByVSS()+""));
        new StaticChecks(activity).setTableLayout(tableLayout,titles,lists);
        alertDialogBuilder
                .setMessage(activity.getString(R.string.paymentdetails))
                .setCancelable(false)
                .setView(scrollView)
                .setNegativeButton(activity.getString(R.string.close),new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.dismiss();
                    }
                });
        if (model.getPayStatus().equals("Paid")&&!model.isReceivedByVSS()) {
            alertDialogBuilder.setPositiveButton(activity.getString(R.string.recieved), new DialogInterface.OnClickListener() {
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
