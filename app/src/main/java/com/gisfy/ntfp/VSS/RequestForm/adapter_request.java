package com.gisfy.ntfp.VSS.RequestForm;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gisfy.ntfp.R;
import com.gisfy.ntfp.RFO.Models.TransitNTFPModel;
import com.gisfy.ntfp.RFO.Models.VSSNTFPModel;
import com.gisfy.ntfp.RFO.Status.ToVSS;
import com.gisfy.ntfp.SqliteHelper.Entity.InventoryRelation;
import com.gisfy.ntfp.SqliteHelper.Entity.MemberModel;
import com.gisfy.ntfp.SqliteHelper.NtfpDao;
import com.gisfy.ntfp.SqliteHelper.SynchroniseDatabase;
import com.gisfy.ntfp.Utils.SnackBarUtils;
import com.gisfy.ntfp.VSS.Inventory.Inventory;
import com.google.android.material.checkbox.MaterialCheckBox;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class adapter_request extends RecyclerView.Adapter<adapter_request.ViewHolder> {

    list_request activity;
    private List<StocksModel> list;
    private NtfpDao dao;
    private MemberModel memberModel = null;

    public adapter_request(List<StocksModel> list, list_request activity) {
        this.list = list;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.list_collectors, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final StocksModel data = list.get(position);
        holder.cloud.setVisibility(View.GONE);
        holder.title1.setText(data.getnTFPmalayalamname()+"\n("+data.getnTFPName().split("-")[0]+")");

        String dateget = data.getDateandTime();
        String[] dateParts = dateget.split("-");
        String year = dateParts[0];
        String month = dateParts[1];
        String day = dateParts[2];
        String datanew = day+"-"+ month+"-"+year;
        holder.title2.setText(data.getDateandTime());

        holder.title3.setText(activity.getString(R.string.totalstock)+data.getQuantity());
        holder.more.setVisibility(View.GONE);
        holder.checkBox.setVisibility(View.VISIBLE);
        holder.longPresslayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] titles = activity.getResources().getStringArray(R.array.transit_titles2);
                List<List<String>> rows = new ArrayList<>();
                List<String> row = new ArrayList<>();
                row.add(data.getnTFPmalayalamname()+"("+data.getnTFPName().split("-")[0]+")");
                row.add(data.getQuantity()+" "+data.getUnit());
                dao = SynchroniseDatabase.getInstance(v.getContext()).ntfpDao();
                memberModel = dao.getMemberFromMemberId(data.getMemberID());
                String memberName;
                if (memberModel!=null)
                    memberName = memberModel.getName();
                else memberName = "";
                row.add(memberName);
                rows.add(row);
                new ChartAlert(activity,titles,rows).show();
            }
        });
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
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

    public List<StocksModel> getSelectedItems(){
        List<StocksModel> temp=new ArrayList<>();
        for (StocksModel model:list){
            if (model.isSelected())
                temp.add(model);
        }
        return temp;
    }
    public void updateData(List<StocksModel> viewModels) {
        list.clear();
        list.addAll(viewModels);
        notifyDataSetChanged();
    }
    public void addItem(int position, StocksModel viewModel) {
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
