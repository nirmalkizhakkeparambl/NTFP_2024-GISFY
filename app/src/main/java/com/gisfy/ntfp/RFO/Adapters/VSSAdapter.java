package com.gisfy.ntfp.RFO.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gisfy.ntfp.API.RetrofitClient;
import com.gisfy.ntfp.R;
import com.gisfy.ntfp.RFO.Models.StatusModel;
import com.gisfy.ntfp.RFO.Models.VSSACKModel;
import com.gisfy.ntfp.RFO.Models.VSSNTFPModel;
import com.gisfy.ntfp.RFO.Status.ToVSS;
import com.gisfy.ntfp.Utils.StaticChecks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;

public class VSSAdapter extends RecyclerView.Adapter<VSSAdapter.ViewHolder> {

    Context activity;
    private final List<VSSACKModel> list;
    private final PositionClickListener listener;
    public VSSAdapter(List<VSSACKModel> list, Context activity,PositionClickListener listener) {
        this.list = list;
        this.activity = activity;
        this.listener=listener;
    }

    @NonNull
    @Override
    public VSSAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VSSAdapter.ViewHolder(LayoutInflater.from(activity).inflate(R.layout.list_rfoadpter, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final VSSAdapter.ViewHolder holder, final int position) {

        final VSSACKModel data=list.get(position);

        switch (data.getStatusByRFO()) {
            case "Approved":
                holder.status.setImageResource(R.drawable.vector_check_green);
                break;
            case "Rejected":
                holder.status.setImageResource(R.drawable.vector_clear_red);
                break;
            default:
                holder.status.setImageResource(R.drawable.vector_time);
                break;
        }
        holder.from.setText("From: "+data.getRange());
        holder.to.setText("To: "+data.getvSSName());
        holder.title1.setText(data.getShipmentNumber());
        holder.title2.setText(data.getRange());
        if (data.getDate()!=null){
            String dateget = data.getDate();
            String[] dateParts = dateget.split("-");
            String year = dateParts[0];
            String month = dateParts[1];
            String day = dateParts[2];
            String datanew = day+"-"+ month+"-"+year;
            holder.date.setText(datanew);
        }

        holder.more.setVisibility(View.GONE);
        holder.longPresslayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.itemClicked(data);
            }
        });
    }
    @Override
    public int getItemCount() {
        return list.size();
    }
    public void updateData(List<VSSACKModel> viewModels) {
        list.clear();
        list.addAll(viewModels);
        notifyDataSetChanged();
    }
    public void addItem(int position, VSSACKModel viewModel) {
        list.add(position, viewModel);
        notifyItemInserted(position);
    }

    public void removeItem(int position) {
        list.remove(position);
        notifyItemRemoved(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title1, title2,to,from,date;
        ImageView more,status;
        ConstraintLayout longPresslayout;
        LinearLayout acknowledge;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title1 = itemView.findViewById(R.id.transactionName);
            title2 = itemView.findViewById(R.id.transactionDetail);
            from = itemView.findViewById(R.id.from);
            to = itemView.findViewById(R.id.to);
            date = itemView.findViewById(R.id.transactionTime);
            more = itemView.findViewById(R.id.more_options);
            longPresslayout = itemView.findViewById(R.id.longPresslayout);
            status = itemView.findViewById(R.id.transactionImage);
        }
    }
}