package com.gisfy.ntfp.Collectors;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.gisfy.ntfp.R;
import com.gisfy.ntfp.RFO.Adapters.PositionClickListener;
import com.gisfy.ntfp.RFO.Models.DFOACKModel;
import com.gisfy.ntfp.RFO.Models.TransitPassModel;
import com.gisfy.ntfp.RFO.Models.VSSACKModel;
import com.gisfy.ntfp.SqliteHelper.DBHelper;
import com.gisfy.ntfp.Utils.StaticChecks;
import com.gisfy.ntfp.VSS.RequestForm.ChartAlert;
import com.google.android.material.checkbox.MaterialCheckBox;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static com.gisfy.ntfp.SqliteHelper.DBHelper.STOCKSID;

public class StockRequestAdapter  extends RecyclerView.Adapter<StockRequestAdapter.ViewHolder> {

    StockRequest activity;
    private final List<CollectorStockModel> list;
    private final PositionClickListener listener;
    public StockRequestAdapter(List<CollectorStockModel> list, StockRequest activity,PositionClickListener listener) {
        this.list = list;
        this.activity = activity;
        this.listener=listener;
    }
    public interface PositionClickListener
    {
        void itemClicked(CollectorStockModel position);
    }
    @NonNull
    @Override
    public StockRequestAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.list_collectors, parent, false);
        return new StockRequestAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final StockRequestAdapter.ViewHolder holder, final int position) {
        final CollectorStockModel data = list.get(position);
        Log.i("checkbox",data.getnTFP()+"-"+data.getmName()+"-null"+data.getvSSStatus()+" -");
        if (data.getRequestStatus().equals("true")){
            holder.checkBox.setVisibility(View.GONE);
            holder.cloud.setVisibility(View.VISIBLE);
            if (data.getRequestStatus()!="") {
                if (data.getvSSStatus().equals("true"))
                    holder.cloud.setImageResource(R.drawable.vector_check_green);
                else if (data.getvSSStatus().equals("false"))
                    holder.cloud.setImageResource(R.drawable.vector_clear_red);
                else
                    holder.cloud.setImageResource(R.drawable.vector_time);
            }else {
                holder.cloud.setImageResource(R.drawable.vector_time);
            }
        }else{
            holder.checkBox.setVisibility(View.VISIBLE);
            holder.cloud.setVisibility(View.GONE);
        }

        holder.title1.setText(data.getnTFP());
        holder.title2.setText(data.getDateTime());
        holder.title3.setText(activity.getString(R.string.totalstock)+data.getQuantity()+ " " +data.getUnit());
        holder.more.setVisibility(View.GONE);
        holder.longPresslayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.itemClicked(data);
            }
        });
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                Log.i("ischecked",isChecked+"");
                list.get(position).setSelected(isChecked);
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

    public List<CollectorStockModel> getSelectedItems(){
        List<CollectorStockModel> temp=new ArrayList<>();
        Log.i("temptemp",temp.size()+"");
        for (CollectorStockModel model:list){
            if (model.isSelected())
                temp.add(model);
        }
        return temp;
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
        TextView title1, title2, title3;
        ImageView more,cloud;
        MaterialCheckBox checkBox;
        LinearLayout longPresslayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cloud=itemView.findViewById(R.id.cloud);
            title1 = itemView.findViewById(R.id.title_collectors_list);
            title2 = itemView.findViewById(R.id.title2_collectors_list);
            title3 = itemView.findViewById(R.id.title3_collectors_list);
            more = itemView.findViewById(R.id.more_options);
            checkBox = itemView.findViewById(R.id.checkbox);
            longPresslayout = itemView.findViewById(R.id.longPresslayout);
        }
    }
}
