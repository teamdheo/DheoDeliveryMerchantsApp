package com.dheo.dheodeliverymerchantapp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.dheo.dheodeliverymerchantapp.ModelClassPickupOrders.M;

import java.util.List;

public class AdapterClassLoadPickups extends RecyclerView.Adapter<AdapterClassLoadPickups.entryPickupviewHolder> {
    private List<M> pickup_orders;
    private Context order_context;
    public  AdapterClassLoadPickups(List<M> pickup_orders, Context order_context){
        this.pickup_orders = pickup_orders;
        this.order_context = order_context;
    }
    @NonNull
    @Override
    public entryPickupviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item_load_entry_pickup, parent, false);
        return new entryPickupviewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull entryPickupviewHolder holder, int position) {
        try {
            holder.entry_name.setText("Name: "+ pickup_orders.get(position).getCustomerName());
        }catch (NullPointerException e){}
        try {
            holder.entry_address.setText("Address: "+ pickup_orders.get(position).getCustomerAddress());
        }catch (NullPointerException e){}
        try {
            holder.entry_phone.setText("Phone: "+ pickup_orders.get(position).getCustomerPhone());
        }catch (NullPointerException e){}
        try {
            holder.entry_cod.setText("amount: "+ pickup_orders.get(position).getCodAmount()+" TK");
        }catch (NullPointerException e){}
        try {
            holder.entry_order_id.setText("Product ID: "+ pickup_orders.get(position).getProductId());
        }catch (NullPointerException e){}
        try {
            holder.entry_created_on.setText("Created on "+ pickup_orders.get(position).getCreatedTime());
            holder.entry_created_on.setBackground(ContextCompat.getDrawable(order_context, R.drawable.delivery_delay));
            holder.entry_created_on.setTextColor(Color.rgb(0, 0, 0));
        }catch (NullPointerException e){}
        try {
            holder.entry_pickup_date.setText("Pickup Date: "+ pickup_orders.get(position).getPickupDate());
        }catch (NullPointerException e){}
        try {
            if(pickup_orders.get(position).getOrderCreated()){
                holder.entry_label.setText("Order Created");
                holder.entry_label.setBackground(ContextCompat.getDrawable(order_context, R.drawable.delivery_start));
            }
        }catch (NullPointerException e){}
        try {
            if(pickup_orders.get(position).getPickedUp()){
                holder.entry_label.setText("Pickup Done");
                holder.entry_label.setBackground(ContextCompat.getDrawable(order_context, R.drawable.courier_drop));
                holder.entry_label.setTextColor(Color.rgb(0, 0, 0));
            }
        }catch (NullPointerException e){}
        try {
            if(pickup_orders.get(position).getIntakeDone()){
                holder.entry_label.setText("Intake Done");
                holder.entry_label.setBackground(ContextCompat.getDrawable(order_context, R.drawable.paid_on));
            }
        }catch (NullPointerException e){}


    }

    @Override
    public int getItemCount() {
        return pickup_orders == null ? 0 : pickup_orders.size()-1;
    }

    public class entryPickupviewHolder extends RecyclerView.ViewHolder {
        TextView entry_name,entry_address,entry_phone,entry_cod,entry_order_id,entry_pickup_date,entry_label,entry_created_on;
        public entryPickupviewHolder(@NonNull View itemView) {
            super(itemView);
            this.entry_name = itemView.findViewById(R.id.entry_name);
            this.entry_address = itemView.findViewById(R.id.entry_address);
            this.entry_phone = itemView.findViewById(R.id.entry_phone);
            this.entry_cod = itemView.findViewById(R.id.entry_cod);
            this.entry_order_id = itemView.findViewById(R.id.entry_order_id);
            this.entry_pickup_date = itemView.findViewById(R.id.entry_pickup_date);
            this.entry_label = itemView.findViewById(R.id.entry_label);
            this.entry_created_on = itemView.findViewById(R.id.entry_created_on);
        }
    }
}
