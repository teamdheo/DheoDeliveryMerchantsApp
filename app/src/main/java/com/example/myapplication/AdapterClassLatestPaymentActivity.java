package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.ModelClassLatestAccountActivity.M;

import java.util.List;

public class AdapterClassLatestPaymentActivity extends RecyclerView.Adapter<AdapterClassLatestPaymentActivity.MyviewHolder> {
    private List<M> latest_account_activity;
    Context activity_context;
    Helper helper;
    public AdapterClassLatestPaymentActivity(List<M> latest_account_activity, Context activity_context){
        this.latest_account_activity = latest_account_activity;
        this.activity_context = activity_context;
        Helper helper = new Helper(activity_context);
    }
    @NonNull
    @Override
    public AdapterClassLatestPaymentActivity.MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item_latest_account_activity,parent, false);
        return new MyviewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final AdapterClassLatestPaymentActivity.MyviewHolder holder,final int position) {
        holder.date_month.setText(latest_account_activity.get(position).getCreatedAt());
        try {
            holder.customer_name.setText(latest_account_activity.get(position).getCustomerName());
        }catch (NullPointerException e){
            holder.customer_name.setVisibility(View.GONE);
        }
        try {
            if(latest_account_activity.get(position).getHasTaggedEntry()){
                holder.paid_on.setText(latest_account_activity.get(position).getTaggedAccountingEntry());
            }
        }catch (NullPointerException e){
            holder.paid_on.setVisibility(View.GONE);
        }
        try {
            if(latest_account_activity.get(position).getIsNegative()){
                holder.lebel.setText(latest_account_activity.get(position).getLebel());
            }
            else {
                holder.lebel.setText("+" + latest_account_activity.get(position).getLebel());
            }
        }catch (NullPointerException e){
            holder.lebel.setVisibility(View.GONE);
        }
        holder.activity_amount.setText(latest_account_activity.get(position).getAmount());
        try {
            holder.payload_id.setText(latest_account_activity.get(position).getPayloadId());
        }catch (NullPointerException e){
            holder.payload_id.setVisibility(View.GONE);
        }


    }

    @Override
    public int getItemCount() {
        return latest_account_activity.size();
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
