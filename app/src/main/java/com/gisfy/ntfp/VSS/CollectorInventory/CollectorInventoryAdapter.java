package com.gisfy.ntfp.VSS.CollectorInventory;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gisfy.ntfp.Collectors.CollectorStockModel;
import com.gisfy.ntfp.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class CollectorInventoryAdapter extends RecyclerView.Adapter<CollectorInventoryAdapter.ViewHolder> {

    Context activity;
    private final List<CollectorStockModel> list;
    private final PositionClickListener listener;
    public CollectorInventoryAdapter(List<CollectorStockModel> list, Context activity,PositionClickListener listener) {
        this.list = list;
        this.listener=listener;
        this.activity = activity;
    }

    public interface PositionClickListener
    {
        void itemClicked(CollectorStockModel data);
    }
    @NonNull
    @Override
    public CollectorInventoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CollectorInventoryAdapter.ViewHolder(LayoutInflater.from(activity).inflate(R.layout.list_rfoadpter, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final CollectorInventoryAdapter.ViewHolder holder, final int position) {

        final CollectorStockModel data=list.get(position);
        Log.i("data",data.getnTFP());
        holder.title1.setText(data.getnTFP());
        holder.from.setText(activity.getString(R.string.from)+data.getCollectorName());
        holder.to.setText(activity.getString(R.string.To)+data.getvSS());

        holder.title2.setText(data.getnTFPType());
        String dateget = data.getDateTime();
        String[] dateParts = dateget.split("-");
        String year = dateParts[0];
        String month = dateParts[1];
        String day = dateParts[2];
        String datanew = day+"-"+ month+"-"+year;
        holder.date.setText(datanew);
        holder.more.setVisibility(View.GONE);
        if (data.getRequestStatus()!=""){
            if (data.getvSSStatus().equals("true"))
                holder.status.setImageResource(R.drawable.vector_check_green);
            else if(data.getvSSStatus().equals("false"))
                holder.status.setImageResource(R.drawable.vector_clear_red);
            else
                holder.status.setImageResource(R.drawable.vector_time);
        }else{
            holder.status.setImageResource(R.drawable.vector_time);
        }

        holder.longPresslayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
    public void updateData(List<CollectorStockModel> viewModels) {
        list.clear();
        list.addAll(viewModels);
        notifyDataSetChanged();
    }
    public void addItem(int position, CollectorStockModel viewModel) {
        list.add(position, viewModel);
        notifyItemInserted(position);
    }

    public void removeItem(int position) {
        list.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title1, title2,to,from,date;
        ImageView more,status;
        ConstraintLayout longPresslayout;
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
