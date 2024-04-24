package com.gisfy.ntfp.RFO.Adapters;

import android.app.Activity;
import android.app.Dialog;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gisfy.ntfp.API.RetrofitClient;
import com.gisfy.ntfp.R;
import com.gisfy.ntfp.RFO.Models.DFOACKModel;
import com.gisfy.ntfp.RFO.Models.DFONTFPModel;
import com.gisfy.ntfp.RFO.Models.StatusModel;
import com.gisfy.ntfp.RFO.Status.ToDFO;
import com.gisfy.ntfp.Utils.StaticChecks;
import com.github.ybq.android.spinkit.SpinKitView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;

public class DFOAdapter extends RecyclerView.Adapter<DFOAdapter.ViewHolder> {

    ToDFO activity;
    private final List<DFOACKModel> list;
    private final PositionClickListener listener;
    public DFOAdapter(List<DFOACKModel> list, ToDFO activity,PositionClickListener listener) {
        this.list = list;
        this.listener=listener;
        this.activity = activity;
    }


    @NonNull
    @Override
    public DFOAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DFOAdapter.ViewHolder(LayoutInflater.from(activity).inflate(R.layout.list_rfoadpter, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final DFOAdapter.ViewHolder holder, final int position) {

        final DFOACKModel data=list.get(position);

        holder.from.setText(activity.getString(R.string.from)+data.getDivision());
        holder.to.setText(activity.getString(R.string.To)+data.getvSS());
        holder.title1.setText(data.getPurchaseOrderNumber());
        holder.title2.setText(data.getRange());


        String dateget = data.getDate();
        String[] dateParts = dateget.split("-");
        String year = dateParts[0];
        String month = dateParts[1];
        String day = dateParts[2];
        String datadate = day+"-"+ month+"-"+year;
        holder.date.setText(datadate);


        holder.more.setVisibility(View.GONE);

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
    public void updateData(List<DFOACKModel> viewModels) {
        list.clear();
        list.addAll(viewModels);
        notifyDataSetChanged();
    }
    public void addItem(int position, DFOACKModel viewModel) {
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
