package com.dheo.dheodeliverymerchantapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.dheo.dheodeliverymerchantapp.ModelClassEditablePickupOrder.EditablePickupOrder;
import com.dheo.dheodeliverymerchantapp.ModelClassPickupOrders.M;

import java.io.IOException;
import java.util.List;

import es.dmoral.toasty.Toasty;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterClassLoadPickups extends RecyclerView.Adapter<AdapterClassLoadPickups.entryPickupviewHolder> {
    private List<M> pickup_orders;
    private Context order_context;
    private EditText edit_customer_name,edit_customer_address,edit_customer_phone,edit_customer_cod,edit_customer_date;
    private Button save_edit_btn,cancel_edit_btn, delete_done, delete_cancel;
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
    public void onBindViewHolder(@NonNull entryPickupviewHolder holder, final int position) {
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
            holder.entry_cod.setText("Amount: "+ pickup_orders.get(position).getCodAmount()+" TK");
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
                holder.entry_edit_layout.setVisibility(View.GONE);
            }
        }catch (NullPointerException e){}

        holder.edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Call<EditablePickupOrder> editablePickupOrderCall = RetrofitClient
                        .getInstance()
                        .getApi()
                        .editable_pickup_order(pickup_orders.get(position).getPickupId());
                editablePickupOrderCall.enqueue(new Callback<EditablePickupOrder>() {
                    @Override
                    public void onResponse(Call<EditablePickupOrder> call, Response<EditablePickupOrder> response) {
                        if (response.body().getE() == 0){
                            EditablePickupOrder s = response.body();
                            try {
                                AlertDialog.Builder edit_dialog = new AlertDialog.Builder(v.getRootView().getContext());
                                edit_dialog.setCancelable(false);
                                LayoutInflater li = (LayoutInflater) order_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                View view = li.inflate(R.layout.dialog_order_edit, null);

                                edit_customer_name = view.findViewById(R.id.edit_customer_name);
                                edit_customer_address = view.findViewById(R.id.edit_customer_address);
                                edit_customer_phone = view.findViewById(R.id.edit_customer_phone);
                                edit_customer_cod = view.findViewById(R.id.edit_customer_cod);
                                edit_customer_date = view.findViewById(R.id.edit_customer_date);
                                save_edit_btn = view.findViewById(R.id.save_edit_btn);
                                cancel_edit_btn = view.findViewById(R.id.cancel_edit_btn);
                                edit_customer_name.setText(s.getM().getCustomerName());
                                edit_customer_address.setText(s.getM().getCustomerAddress());
                                edit_customer_phone.setText(s.getM().getCustomerPhone());
                                edit_customer_cod.setText(s.getM().getCustomerCod());
                                edit_customer_date.setText(s.getM().getPickupDate());

                                edit_dialog.setView(view);
                                final AlertDialog dialog = edit_dialog.create();
                                dialog.show();

                                cancel_edit_btn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                    }
                                });

                                save_edit_btn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Call<ResponseBody> responseBodyCall = RetrofitClient
                                                .getInstance()
                                                .getApi()
                                                .update_edited_order(pickup_orders.get(position).getPickupId(),edit_customer_name.getText().toString(),edit_customer_address.getText().toString(),edit_customer_phone.getText().toString(),edit_customer_cod.getText().toString(),edit_customer_date.getText().toString());
                                        responseBodyCall.enqueue(new Callback<ResponseBody>() {
                                            @Override
                                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                try {
                                                    if (response.body().string().equals("{\"e\":0}")){
                                                        Toasty.success(order_context, "Succesfully Updated", Toast.LENGTH_LONG, true).show();
                                                        dialog.dismiss();
                                                        Intent intent = new Intent(order_context, PickupEntryActivity.class);
                                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                        order_context.startActivity(intent);
                                                    }
                                                    else{
                                                        Toasty.error(order_context, "The Server Failed To Response", Toast.LENGTH_LONG, true).show();
                                                        dialog.dismiss();
                                                    }
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<ResponseBody> call, Throwable t) {

                                            }
                                        });
                                    }
                                });
                            }catch (NullPointerException e){

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<EditablePickupOrder> call, Throwable t) {

                    }
                });

            }
        });

        holder.delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder edit_dialog = new AlertDialog.Builder(v.getRootView().getContext());
                edit_dialog.setCancelable(false);
                LayoutInflater li = (LayoutInflater) order_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = li.inflate(R.layout.dialog_order_remove, null);

                delete_done = view.findViewById(R.id.delete_done);
                delete_cancel = view.findViewById(R.id.delete_cancel);

                edit_dialog.setView(view);
                final AlertDialog dialog = edit_dialog.create();
                dialog.show();

                delete_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                delete_done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Call<ResponseBody> call = RetrofitClient
                                .getInstance()
                                .getApi()
                                .remove_order(pickup_orders.get(position).getPickupId());
                        call.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                try {
                                    if (response.body().string().equals("{\"e\":0}")){
                                        Toasty.success(order_context, "Succesfully removed", Toast.LENGTH_LONG, true).show();
                                        dialog.dismiss();
                                        Intent intent = new Intent(order_context, PickupEntryActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        order_context.startActivity(intent);
                                    }
                                    else{
                                        Toasty.error(order_context, "The Server Failed To Response", Toast.LENGTH_LONG, true).show();
                                        dialog.dismiss();
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {

                            }
                        });
                    }
                });

            }
        });


    }

    @Override
    public int getItemCount() {
        return pickup_orders == null ? 0 : pickup_orders.size()-1;
    }

    public class entryPickupviewHolder extends RecyclerView.ViewHolder {
        TextView entry_name,entry_address,entry_phone,entry_cod,entry_order_id,entry_pickup_date,entry_label,entry_created_on,edit_btn,delete_btn;
        LinearLayout entry_edit_layout;
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
            this.entry_edit_layout = itemView.findViewById(R.id.entry_edit_layout);
            this.edit_btn = itemView.findViewById(R.id.edit_btn);
            this.delete_btn = itemView.findViewById(R.id.delete_btn);
        }
    }
}
