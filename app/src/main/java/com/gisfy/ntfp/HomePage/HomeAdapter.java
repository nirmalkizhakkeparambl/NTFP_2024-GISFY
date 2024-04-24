package com.gisfy.ntfp.HomePage;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gisfy.ntfp.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {

    private List<HomeModel> list;
    private Home context;
    private int viewtype;
    public HomeAdapter(List<HomeModel> list, Home activity, int viewtype) {
        this.list = list;
        this.context=activity;
        this.viewtype=viewtype;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.standard_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final HomeModel data = list.get(position);
        holder.title.setText(data.getTitle());
        holder.imageView.setImageResource(data.getImage());
        if (data.isSelected()){

            holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.green));
            holder.cardView.setCardElevation(25);
        }else {
            holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.white));
            holder.cardView.setCardElevation(5);
        }
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.green));
                holder.cardView.setCardElevation(25);
                Log.i("posdoa",position+".//"+data.getTitle());
                if (data.getActivity()!=null){
                    context.startActivity(new Intent(context,data.getActivity()));
                }else {

                    context.backpressCnt=1;
                    Log.i("posdoa",position+".//"+data.getTitle());
                    FragmentManager fragmentManager = context.getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.home_fram_layout, new HomeFrag2(position,data.getTitle()));
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView imageView;
        CardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            imageView = itemView.findViewById(R.id.icon);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}
