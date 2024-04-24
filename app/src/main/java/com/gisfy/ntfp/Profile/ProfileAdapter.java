package com.gisfy.ntfp.Profile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gisfy.ntfp.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ViewHolder> {

    private final List<ProfileModel> list;
    private final Context context;


    public ProfileAdapter(Context context, List<ProfileModel> list) {
        this.context = context;
        this.list=list;
    }

    @NonNull
    @Override
    public ProfileAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
          return new ProfileAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.list_profile, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ProfileAdapter.ViewHolder holder, final int position) {
        final ProfileModel data = list.get(position);
        holder.object.setText(data.getObject());
        holder.key.setText(data.getKey());
        holder.logo.setImageResource(data.getImage());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView object,key;
        ImageView logo;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            object = itemView.findViewById(R.id.object);
            key = itemView.findViewById(R.id.key);
            logo = itemView.findViewById(R.id.icon);
        }
    }
}