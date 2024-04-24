package com.gisfy.ntfp.Collectors;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gisfy.ntfp.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PDFAdapter extends RecyclerView.Adapter<PDFAdapter.ViewHolder> {

    PDFList activity;
    private List<PDFModel> inventoryList;
    public PDFAdapter(List<PDFModel> inventoryList, PDFList activity) {
        this.inventoryList = inventoryList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public PDFAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.list_pdf, parent, false);
        return new PDFAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final PDFAdapter.ViewHolder holder, final int position) {
        final PDFModel data = inventoryList.get(position);
        holder.title1.setText(data.getTitle());
        holder.longPresslayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Uri path = Uri.parse("android.resource://" + activity.getPackageName() + "/" + R.raw.document);
                activity.startActivity(new Intent(activity,PDFViewer.class));
            }
        });
    }
    @Override
    public int getItemCount() {
        return inventoryList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title1;
        ImageView icon;
        LinearLayout longPresslayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title1 = itemView.findViewById(R.id.title);
            icon = itemView.findViewById(R.id.icon);
            longPresslayout = itemView.findViewById(R.id.longPresslayout);
        }
    }
}
