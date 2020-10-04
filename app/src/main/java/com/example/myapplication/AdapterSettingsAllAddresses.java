package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.modelClassPickupAddresses.M;

import java.util.List;

public class AdapterSettingsAllAddresses extends RecyclerView.Adapter<AdapterSettingsAllAddresses.AddressViewHolder> {
    private List<M> pickup_address;
    private Context mycontex;
    Helper helper;
    String address_id_check;

    public AdapterSettingsAllAddresses(List<M> pickup_address, Context mycontex) {
        this.pickup_address = pickup_address;
        this.mycontex = mycontex;
        Helper helper = new Helper(mycontex);
    }
    @NonNull
    @Override
    public AddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item_setting_addresses, parent, false);
        return new AddressViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressViewHolder holder, int position) {
        holder.address_id.setText(pickup_address.get(position).getAddress_id());
        holder.setting_Address.setText(pickup_address.get(position).getClientPickupAddress());
        holder.setting_phone.setText(pickup_address.get(position).getPhone_no());

    }

    @Override
    public int getItemCount() {
        return pickup_address.size();
    }

    public class AddressViewHolder extends RecyclerView.ViewHolder {
        EditText setting_phone, setting_Address;
        TextView address_id, delete_address;
        Button save_address;
        public AddressViewHolder(@NonNull View itemView) {
            super(itemView);
            setting_Address = itemView.findViewById(R.id.setting_address);
            setting_phone = itemView.findViewById(R.id.setting_phone);
            address_id = itemView.findViewById(R.id.address_id_s);
            delete_address = itemView.findViewById(R.id.delete_address);
            save_address = itemView.findViewById(R.id.save_address);
        }
    }
}
