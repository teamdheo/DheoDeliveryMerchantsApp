package com.dheo.dheodeliverymerchantapp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.dheo.dheodeliverymerchantapp.ModelClassSearchPickupOrder.M;

import java.util.List;

public class AdapterClassSearchPickupOrder extends RecyclerView.Adapter<AdapterClassSearchPickupOrder.searchOrderHolder> {
    private List<M> all_search_orders;
    private Context search_order_context;

    public AdapterClassSearchPickupOrder(List<M> all_search_orders, Context search_order_context){
        this.all_search_orders =all_search_orders;
        this.search_order_context = search_order_context;
    }
    @NonNull
    @Override
    public searchOrderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item_load_entry_pickup, parent, false);
        return new searchOrderHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull searchOrderHolder holder, int position) {
        holder.entry_edit_layout.setVisibility(View.GONE);
        try {
            holder.entry_name.setText("Name: "+ all_search_orders.get(position).getCustomerName());
        }catch (NullPointerException e){}
        try {
            holder.entry_address.setText("Address: "+ all_search_orders.get(position).getCustomerAddress());
        }catch (NullPointerException e){}
        try {
            holder.entry_phone.setText("Phone: "+ all_search_orders.get(position).getCustomerPhone());
        }catch (NullPointerException e){}
        try {
            holder.entry_cod.setText("amount: "+ all_search_orders.get(position).getCodAmount()+" TK");
        }catch (NullPointerException e){}
        try {
            holder.entry_order_id.setText("Product ID: "+ all_search_orders.get(position).getProductId());
        }catch (NullPointerException e){}
        try {
            holder.entry_created_on.setText("Created on "+ all_search_orders.get(position).getCreatedTime());
            holder.entry_created_on.setBackground(ContextCompat.getDrawable(search_order_context, R.drawable.delivery_delay));
            holder.entry_created_on.setTextColor(Color.rgb(0, 0, 0));
        }catch (NullPointerException e){}
        try {
            holder.entry_pickup_date.setText("Pickup Date: "+ all_search_orders.get(position).getPickupDate());
        }catch (NullPointerException e){}
        try {
            if(all_search_orders.get(position).getOrderCreated()){
                holder.entry_label.setText("Order Created");
                holder.entry_label.setBackground(ContextCompat.getDrawable(search_order_context, R.drawable.delivery_start));
            }
        }catch (NullPointerException e){}
        try {
            if(all_search_orders.get(position).getPickedUp()){
                holder.entry_label.setText("Pickup Done");
                holder.entry_label.setBackground(ContextCompat.getDrawable(search_order_context, R.drawable.courier_drop));
                holder.entry_label.setTextColor(Color.rgb(0, 0, 0));
            }
        }catch (NullPointerException e){}
        try {
            if(all_search_orders.get(position).getIntakeDone()){
                holder.entry_label.setText("Intake Done");
                holder.entry_label.setBackground(ContextCompat.getDrawable(search_order_context, R.drawable.paid_on));
            }
        }catch (NullPointerException e){}


    }

    @Override
    public int getItemCount() {
        return all_search_orders == null ? 0 : all_search_orders.size();
    }

    public class searchOrderHolder extends RecyclerView.ViewHolder {
        TextView entry_name,entry_address,entry_phone,entry_cod,entry_order_id,entry_pickup_date,entry_label,entry_created_on,edit_btn,delete_btn;
        LinearLayout entry_edit_layout;
        public searchOrderHolder(@NonNull View itemView) {
            super(itemView);
            this.entry_name = itemView.findViewById(R.id.entry_name);
            this.entry_address = itemView.findViewById(R.id.entry_address);
            this.entry_phone = itemView.findViewById(R.id.entry_phone);
            this.entry_cod = itemView.findViewById(R.id.entry_cod);
            this.entry_order_id = itemView.findViewById(R.id.entry_order_id);
            this.entry_pickup_date = itemView.findViewById(R.id.entry_pickup_date);
            this.entry_label = itemView.findViewById(R.id.entry_label);
            this.entry_created_on = itemView.findViewById(R.id.entry_created_on);
            this.entry_edit_layout = itemView.findViewById(R.id.entry_edit_layout);
            this.edit_btn = itemView.findViewById(R.id.edit_btn);
            this.delete_btn = itemView.findViewById(R.id.delete_btn);
        }
    }
}
