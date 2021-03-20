package com.dheo.dheodeliverymerchantapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.dheo.dheodeliverymerchantapp.modelClassPickupAddresses.M;

import java.io.IOException;
import java.util.List;

import es.dmoral.toasty.Toasty;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterClassSelfEntryAddresses extends RecyclerView.Adapter<AdapterClassSelfEntryAddresses.EntryAddressHolder> {
    private List<M> pickup_address;
    private Context mycontex;
    Helper helper;
    private int client_id;
    private String customer_name, customer_address, customer_phone, cod_amount, date;

    public AdapterClassSelfEntryAddresses(List<M> pickup_address, Context mycontex, int client_id, String customer_name, String customer_address, String customer_phone, String cod_amount, String date ) {
        this.pickup_address = pickup_address;
        this.mycontex = mycontex;
        Helper helper = new Helper(mycontex);
        this.client_id = client_id;
        this.customer_name = customer_name;
        this.customer_address = customer_address;
        this.customer_phone = customer_phone;
        this.cod_amount = cod_amount;
        this.date = date;
    }
    @NonNull
    @Override
    public EntryAddressHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item_pickup_address, parent, false);
        return new EntryAddressHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull EntryAddressHolder holder, final int position) {
        holder.show_address.setText(pickup_address.get(position).getClientPickupAddress());
        holder.number.setText(pickup_address.get(position).getPhone_no());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<ResponseBody> entryCall = RetrofitClient
                        .getInstance()
                        .getApi()
                        .pickup_self_entry(client_id, customer_name, customer_address, customer_phone, cod_amount,date, pickup_address.get(position).getAddress_id());
                entryCall.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        String s = null;
                        try {
                            s = response.body().string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        if (s.equals("{\"e\":0}")) {

                            Intent intent = new Intent(mycontex, PickupEntryActivity.class);
                            mycontex.startActivity(intent);
                        }
                        else if (s.equals("{\"e\":2}")) {
                            Toasty.error(mycontex, "Today's pickup time is over. please contact to our customer service for details.", Toast.LENGTH_LONG, true).show();
                        }
                        else{
                            //Toast.makeText(getApplicationContext(),"no token"+ token , Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return pickup_address == null ? 0 : pickup_address.size();
    }

    public class EntryAddressHolder extends RecyclerView.ViewHolder {
        TextView show_address, number,address_id;
        CardView cardView;
        public EntryAddressHolder(@NonNull View itemView) {
            super(itemView);
            this.show_address = itemView.findViewById(R.id.show_pickup_address);
            this.number = itemView.findViewById(R.id.add_phone);
            this.address_id = itemView.findViewById(R.id.address_id);
            this.cardView = itemView.findViewById(R.id.recycler_address);
        }
    }
}
