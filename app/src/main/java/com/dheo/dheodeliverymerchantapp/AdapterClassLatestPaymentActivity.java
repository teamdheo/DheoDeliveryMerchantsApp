package com.dheo.dheodeliverymerchantapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.dheo.dheodeliverymerchantapp.ModelClassLatestAccountActivity.M;

import java.util.List;

public class AdapterClassLatestPaymentActivity extends RecyclerView.Adapter<AdapterClassLatestPaymentActivity.MyviewHolder> {
    private List<M> latest_account_activity;
    Context activity_context;
    Helper helper;
    private String client_name,pro_pic_url;
    private int extensionRemoved;

    public AdapterClassLatestPaymentActivity(List<M> latest_account_activity, Context activity_context, String client_name, String pro_pic_url) {
        this.latest_account_activity = latest_account_activity;
        this.activity_context = activity_context;
        this.client_name = client_name;
        this.pro_pic_url = pro_pic_url;
        Helper helper = new Helper(activity_context);
    }

    @NonNull
    @Override
    public AdapterClassLatestPaymentActivity.MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item_latest_account_activity, parent, false);
        return new AdapterClassLatestPaymentActivity.MyviewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final AdapterClassLatestPaymentActivity.MyviewHolder holder, final int position) {
        try {
            try {
                holder.date_month.setText(latest_account_activity.get(position).getCreatedAt());
            }catch (NullPointerException e) {
                holder.date_month.setVisibility(View.GONE);
            }
            try {
                holder.customer_name.setText(latest_account_activity.get(position).getCustomerName());

            } catch (NullPointerException e) {
                holder.customer_name.setVisibility(View.GONE);
            }
            try {
                if (latest_account_activity.get(position).getHasTaggedEntry()) {
                    holder.paid_on.setText("paid on " + latest_account_activity.get(position).getTaggedAccountingEntry());
                    holder.paid_on.setBackground(ContextCompat.getDrawable(activity_context, R.drawable.paid_on));
                }
            } catch (NullPointerException e) {
                //holder.paid_on.setVisibility(View.GONE);
            }
            try {
                if (latest_account_activity.get(position).getHasPainding()) {
                    holder.paid_on.setText("Pending Confirmation");
                    holder.paid_on.setBackground(ContextCompat.getDrawable(activity_context, R.drawable.delivery_delay));
                    holder.paid_on.setTextColor(Color.rgb(0, 0, 0));
                }
            } catch (NullPointerException e) {
                //holder.paid_on.setVisibility(View.GONE);
            }
            try {
                if (latest_account_activity.get(position).getHasRejected()) {
                    holder.paid_on.setText("Payment Failed");
                    holder.paid_on.setBackground(ContextCompat.getDrawable(activity_context, R.drawable.delivery_cancel));
                }
            } catch (NullPointerException e) {
                //holder.paid_on.setVisibility(View.GONE);
            }
            try {
                if(latest_account_activity.get(position).getAmount() < 0){
                    holder.activity_amount.setText(latest_account_activity.get(position).getAmount().toString() + "TK");
                }
                else  if(latest_account_activity.get(position).getAmount() > 0){
                    holder.activity_amount.setText("+" + latest_account_activity.get(position).getAmount().toString() +"TK");
                }

            } catch (NullPointerException e) {

            }

            try {
                holder.lebel.setText(latest_account_activity.get(position).getLebel());
            } catch (NullPointerException e) {
                holder.lebel.setVisibility(View.GONE);
            }
            try {
                holder.payload_id.setText(latest_account_activity.get(position).getPayloadId());
                holder.payload_id.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        extensionRemoved =Integer.parseInt(latest_account_activity.get(position).getId());
                        //Toast.makeText(activity_context.getApplicationContext(), extensionRemoved+"", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(activity_context, OrderTrackerActivity.class);
                        intent.putExtra("short_id", latest_account_activity.get(position).getShortId());
                        intent.putExtra("payload_id",extensionRemoved);
                        intent.putExtra("client_name", client_name);
                        intent.putExtra("image_url", pro_pic_url);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        activity_context.startActivity(intent);
                    }
                });
            } catch (NullPointerException e) {
                holder.payload_id.setVisibility(View.GONE);
            }

        }catch (NullPointerException e){}

    }

    @Override
    public int getItemCount() {
        return latest_account_activity == null ? 0 : latest_account_activity.size();
    }

    public class MyviewHolder extends RecyclerView.ViewHolder {
        TextView date_month, customer_name, paid_on, lebel, activity_amount, payload_id;

        public MyviewHolder(@NonNull View itemView) {
            super(itemView);
            this.date_month = itemView.findViewById(R.id.month_year);
            this.customer_name = itemView.findViewById(R.id.customer_name);
            this.paid_on = itemView.findViewById(R.id.paid_on);
            this.lebel = itemView.findViewById(R.id.lebel);
            this.activity_amount = itemView.findViewById(R.id.activity_amount);
            this.payload_id = itemView.findViewById(R.id.payload_id);
        }
    }
}
