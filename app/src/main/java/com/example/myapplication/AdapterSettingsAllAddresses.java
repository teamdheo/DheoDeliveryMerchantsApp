package com.example.myapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.modelClassPickupAddresses.M;

import java.io.IOException;
import java.util.List;

import es.dmoral.toasty.Toasty;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterSettingsAllAddresses extends RecyclerView.Adapter<AdapterSettingsAllAddresses.AddressViewHolder> {
    private List<M> pickup_address;
    private Context mycontex;
    Helper helper;
    String address_id_check;
    //private ProgressDialog progressDialog;

    public AdapterSettingsAllAddresses(List<M> pickup_address, Context mycontex) {
        this.pickup_address = pickup_address;
        this.mycontex = mycontex;
        Helper helper = new Helper(mycontex);
        //ProgressDialog progressDialog = new ProgressDialog(mycontex);
    }
    @NonNull
    @Override
    public AddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item_setting_addresses, parent, false);
        return new AddressViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final AddressViewHolder holder, final int position) {
        holder.address_id.setText(pickup_address.get(position).getAddress_id());
        holder.setting_Address.setText(pickup_address.get(position).getClientPickupAddress());
        holder.setting_phone.setText(pickup_address.get(position).getPhone_no());
        holder.save_address.setVisibility(View.INVISIBLE);
        holder.delete_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                progressDialog.setMessage("Removing...");
//                progressDialog.show();
                Call<ResponseBody> call = RetrofitClient
                        .getInstance()
                        .getApi()
                        .delete_pickup_address(pickup_address.get(position).getAddress_id());
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        String s = null;
                        try {
                            s = response.body().string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(mycontex, s, Toast.LENGTH_LONG).show();
                        if (s.equals("{\"e\":0}")){
                           // progressDialog.dismiss();
                            Toasty.error(mycontex, "successfully removed", Toast.LENGTH_LONG, true).show();
                            Intent intent = new Intent(mycontex, SettingsActivity.class);
                            mycontex.startActivity(intent);

                        }
                        else{
                            Toasty.error(mycontex, "server failed to response", Toast.LENGTH_LONG, true).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toasty.error(mycontex, "Try Again!", Toast.LENGTH_LONG, true).show();
                    }
                });
            }
        });
        holder.setting_Address.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                holder.save_address.setVisibility(View.VISIBLE);
                holder.save_address.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        progressDialog.setMessage("updating...");
//                        progressDialog.show();
                        Call<ResponseBody> call1 = RetrofitClient
                                .getInstance()
                                .getApi()
                                .update_address(pickup_address.get(position).getAddress_id(),holder.setting_Address.getText().toString(),holder.setting_phone.getText().toString());
                        call1.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                String s = null;
                                try {
                                    s = response.body().string();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                Toast.makeText(mycontex, s, Toast.LENGTH_LONG).show();
                                if (s.equals("{\"e\":0}")){
                                    //progressDialog.dismiss();
                                    Toasty.error(mycontex, "successfully updated", Toast.LENGTH_LONG, true).show();
                                    Intent intent = new Intent(mycontex, SettingsActivity.class);
                                    mycontex.startActivity(intent);

                                }
                                else{
                                    Toasty.error(mycontex, "server failed to response", Toast.LENGTH_LONG, true).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Toasty.error(mycontex, "Try Again!", Toast.LENGTH_LONG, true).show();
                            }
                        });
                    }
                });
            }
        });
        holder.setting_phone.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                holder.save_address.setVisibility(View.VISIBLE);
                holder.save_address.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        progressDialog.setMessage("updating...");
//                        progressDialog.show();
                        Call<ResponseBody> call1 = RetrofitClient
                                .getInstance()
                                .getApi()
                                .update_address(pickup_address.get(position).getAddress_id(),holder.setting_Address.getText().toString(),holder.setting_phone.getText().toString());
                        call1.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                String s = null;
                                try {
                                    s = response.body().string();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                Toast.makeText(mycontex, s, Toast.LENGTH_LONG).show();
                                if (s.equals("{\"e\":0}")){
                                   // progressDialog.dismiss();
                                    Toasty.error(mycontex, "successfully updated", Toast.LENGTH_LONG, true).show();
                                    holder.save_address.setVisibility(View.INVISIBLE);
//                                    Intent intent = new Intent(mycontex, SettingsActivity.class);
//                                    mycontex.startActivity(intent);

                                }
                                else{
                                    Toasty.error(mycontex, "server failed to response", Toast.LENGTH_LONG, true).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Toasty.error(mycontex, "Try Again!", Toast.LENGTH_LONG, true).show();
                            }
                        });
                    }
                });
            }
        });

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
