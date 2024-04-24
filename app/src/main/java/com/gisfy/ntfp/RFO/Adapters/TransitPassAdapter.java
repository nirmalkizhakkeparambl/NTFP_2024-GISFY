package com.gisfy.ntfp.RFO.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gisfy.ntfp.API.RetrofitClient;
import com.gisfy.ntfp.R;
import com.gisfy.ntfp.RFO.Models.StatusModel;
import com.gisfy.ntfp.RFO.Models.TransitNTFPModel;
import com.gisfy.ntfp.RFO.Models.TransitPassModel;
import com.gisfy.ntfp.RFO.Status.TransitActivity;
import com.gisfy.ntfp.Utils.FCMNotifications;
import com.gisfy.ntfp.Utils.StaticChecks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;

public class TransitPassAdapter extends RecyclerView.Adapter<TransitPassAdapter.ViewHolder> {

    TransitActivity activity;
    private final List<TransitPassModel> list;
    private final PositionClickListener listener;


    public TransitPassAdapter(List<TransitPassModel> list, TransitActivity activity, PositionClickListener listener) {
        this.list = list;
        this.activity = activity;
        this.listener=listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(activity).inflate(R.layout.list_rfoadpter, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final TransitPassModel data=list.get(position);
        switch (data.getTransitStatus()) {
            case "Accepted":
                holder.status.setImageResource(R.drawable.vector_check_green);
                break;
            case "Rejected":
                holder.status.setImageResource(R.drawable.vector_clear_red);
                break;
            default:
                holder.status.setImageResource(R.drawable.vector_time);
                break;
        }
        holder.from.setText("From: "+data.getvSSName());
        holder.to.setText("To: "+data.getpCName());
        holder.title1.setText(data.getTransUniqueId());
        holder.title2.setText(data.getvSSName());


        String dateget = data.getDate();
        String[] dateParts = dateget.split("-");
        String year = dateParts[0];
        String month = dateParts[1];
        String day = dateParts[2];
        String datadate = day+"-"+ month+"-"+year;

        holder.date.setText(datadate);



        holder.more.setVisibility(View.GONE);
        holder.longPresslayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(data.getTransUniqueId(),data.getTransitStatus());
                listener.itemClicked(data);
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


    public void updateData(List<TransitPassModel> viewModels) {
        list.clear();
        list.addAll(viewModels);
        notifyDataSetChanged();
    }
    public void addItem(int position, TransitPassModel viewModel) {
        list.add(position, viewModel);
        notifyItemInserted(position);
    }

    public void removeItem(int position) {
        list.remove(position);
        notifyItemRemoved(position);
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
