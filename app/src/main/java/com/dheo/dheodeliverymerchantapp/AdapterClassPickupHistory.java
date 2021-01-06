package com.dheo.dheodeliverymerchantapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dheo.dheodeliverymerchantapp.ModelClassPickupHistory.M;
import com.squareup.picasso.Picasso;

import java.util.List;


public class AdapterClassPickupHistory extends RecyclerView.Adapter<AdapterClassPickupHistory.historyViewHolder> {
    private List<M> history_pickup;
    Context history_contex;

    public AdapterClassPickupHistory(List<M> history_pickup, Context history_contex){
        this.history_contex = history_contex;
        this.history_pickup = history_pickup;
    }
    @NonNull
    @Override
    public historyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item_pickup_history, parent, false);
        return new historyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final historyViewHolder holder,final int position) {
        try{
            String photo_url = "https://dheo-static-sg.s3.ap-southeast-1.amazonaws.com/img/community/team/" + history_pickup.get(position).getAgentPhoto() ;
            Picasso.get().load(photo_url).into(holder.pickup_history_agent_photo);
        }catch (NullPointerException e){

        }
        try{
            holder.pickup_history_agent_name.setText(history_pickup.get(position).getAgentName());
        }catch (NullPointerException e){}
        try{
            holder.pickup_history_date.setText(history_pickup.get(position).getPickupDate());
        }catch (NullPointerException e){}
        try{
            holder.pickup_history_number.setText(history_pickup.get(position).getTotalPickup()+" percels were picked up from "+ history_pickup.get(position).getPickupAddress());
        }catch (NullPointerException e){}

    }

    @Override
    public int getItemCount() {
        return history_pickup == null ? 0 : history_pickup.size()-1;
    }

    public class historyViewHolder extends RecyclerView.ViewHolder {
        ImageView pickup_history_agent_photo;
        TextView pickup_history_date,pickup_history_agent_name,pickup_history_number;
        public historyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.pickup_history_agent_photo = itemView.findViewById(R.id.pickup_history_agent_photo);
            this.pickup_history_date = itemView.findViewById(R.id.pickup_history_date);
            this.pickup_history_agent_name = itemView.findViewById(R.id.pickup_history_agent_name);
            this.pickup_history_number = itemView.findViewById(R.id.pickup_history_number);
        }
    }
}
