package com.dheo.dheodeliverymerchantapp;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.dheo.dheodeliverymerchantapp.modelClassPickupAddresses.M;

import java.util.List;

public class AdapterPickupAddressList extends RecyclerView.Adapter<AdapterPickupAddressList.MyViewHolder> {
    private List<M> pickup_address;
    private Context mycontex;
    Helper helper;
    String address_id_check;

    public AdapterPickupAddressList(List<M> pickup_address, Context mycontex) {
        this.pickup_address = pickup_address;
        this.mycontex = mycontex;
        Helper helper = new Helper(mycontex);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item_pickup_address, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final AdapterPickupAddressList.MyViewHolder holder, final int position) {
        holder.show_address.setText(pickup_address.get(position).getClientPickupAddress());
        holder.number.setText(pickup_address.get(position).getPhone_no());
        holder.address_id.setText(pickup_address.get(position).getAddress_id());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                address_id_check = pickup_address.get(position).getAddress_id();
                Intent intent = new Intent(mycontex, ListActivityMultiplePickupAddressSlots.class);
                intent.putExtra("address_id", address_id_check);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mycontex.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return pickup_address.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView show_address, number,address_id;
        CardView cardView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.show_address = itemView.findViewById(R.id.show_pickup_address);
            this.number = itemView.findViewById(R.id.add_phone);
            this.address_id = itemView.findViewById(R.id.address_id);
            this.cardView = itemView.findViewById(R.id.recycler_address);
        }
    }
}
