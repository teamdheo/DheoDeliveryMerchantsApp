package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.ModelClassClientDashboardPayloads.M;

import java.util.List;

public class AdapterAllPayloadList extends RecyclerView.Adapter<AdapterAllPayloadList.AllPayloadHolder> {
    private List<M> clients_all_payload;
    Context all_payload_context;
    public  AdapterAllPayloadList(List<M> clients_all_payload, Context all_payload_context){
        this.clients_all_payload = clients_all_payload;
        this.all_payload_context = all_payload_context;
    }
    @NonNull
    @Override
    public AllPayloadHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item_all_payloads, parent, false);
        return  new AllPayloadHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final AllPayloadHolder holder, final int position) {
        try{
            try{
                holder.customer_name.setText(clients_all_payload.get(position).getCustomerName());
            }catch (NullPointerException e){
                holder.customer_name.setVisibility(View.GONE);
            }
            try {
                holder.date_time.setText(clients_all_payload.get(position).getDate());
            }catch (NullPointerException e){
                holder.date_time.setVisibility(View.GONE);
            }
            try {
                holder.order_no.setText(clients_all_payload.get(position).getOrderNo());
            }catch (NullPointerException e){
                holder.order_no.setVisibility(View.GONE);
            }
            try {
                if(clients_all_payload.get(position).getAmount() != null){
                    holder.amount.setText(clients_all_payload.get(position).getAmount() + "TK");
                }
                else{
                    holder.amount.setVisibility(View.GONE);
                }
            }catch (NullPointerException e) {
                holder.amount.setVisibility(View.GONE);
            }
            try {
                if(clients_all_payload.get(position).getHasReview()){
                    holder.rating.setRating(Float.parseFloat(clients_all_payload.get(position).getRating()));
                }
            }catch (NullPointerException e) {
                holder.rating.setVisibility(View.GONE);
            }
            try {
                if (clients_all_payload.get(position).getCourierDrop()){
                    holder.label.setVisibility(View.VISIBLE);
                    holder.label.setText("Courier Drop");
                    holder.label.setBackground(ContextCompat.getDrawable(all_payload_context, R.drawable.courier_drop));
                    holder.label.setTextColor(Color.rgb(0,0,0));
                }
            }catch (NullPointerException e) {
                //holder.label.setVisibility(View.INVISIBLE);
            }
            try {
                if (clients_all_payload.get(position).getDelayed()){
                    holder.label.setVisibility(View.VISIBLE);
                    holder.label.setText("Delayed");
                    holder.label.setBackground(ContextCompat.getDrawable(all_payload_context, R.drawable.delivery_delay));
                    holder.label.setTextColor(Color.rgb(0,0,0));
                }
            }catch (NullPointerException e) {
                //holder.label.setVisibility(View.INVISIBLE);
            }
            try {
                if (clients_all_payload.get(position).getDeliveryDone()){
                    holder.label.setVisibility(View.VISIBLE);
                    holder.label.setText("Delivery Done");
                    holder.label.setBackground(ContextCompat.getDrawable(all_payload_context, R.drawable.paid_on));
                }
            }catch (NullPointerException e) {
                // holder.label.setVisibility(View.INVISIBLE);
            }
            try {
                if (clients_all_payload.get(position).getDeliveryStarted()){
                    holder.label.setVisibility(View.VISIBLE);
                    holder.label.setText("Delivery Started");
                    holder.label.setBackground(ContextCompat.getDrawable(all_payload_context, R.drawable.delivery_start));
                }
            }catch (NullPointerException e) {
                //holder.label.setVisibility(View.INVISIBLE);
            }
            try {
                if (clients_all_payload.get(position).getPayloadCancelled()){
                    holder.label.setVisibility(View.VISIBLE);
                    holder.label.setText("Delivery Canceled");
                    holder.label.setBackground(ContextCompat.getDrawable(all_payload_context, R.drawable.delivery_cancel));
                }
            }catch (NullPointerException e) {
                //holder.label.setVisibility(View.INVISIBLE);
            }
            try {
                if (clients_all_payload.get(position).getReturnDone()){
                    holder.label.setVisibility(View.VISIBLE);
                    holder.label.setText("Return Done");
                    holder.label.setBackground(ContextCompat.getDrawable(all_payload_context, R.drawable.paid_on));
                }
            }catch (NullPointerException e) {
                //holder.label.setVisibility(View.INVISIBLE);
            }
            try {
                if (clients_all_payload.get(position).getReturnStarted()){
                    holder.label.setVisibility(View.VISIBLE);
                    holder.label.setText("Return Started");
                    holder.label.setBackground(ContextCompat.getDrawable(all_payload_context, R.drawable.delivery_start));
                }
            }catch (NullPointerException e) {
                //holder.label.setVisibility(View.INVISIBLE);
            }
            try {
                if(clients_all_payload.get(position).getOnHold()){
                    holder.label.setText(clients_all_payload.get(position).getOnHoldLabel());
                    holder.label.setBackground(ContextCompat.getDrawable(all_payload_context, R.drawable.delivery_delay));
                    holder.label.setTextColor(Color.rgb(0,0,0));
                }
            }catch (NullPointerException e){}
            try {
                if(clients_all_payload.get(position).getEnableEdit()){
                    holder.bar.setVisibility(View.VISIBLE);
                    holder.edit.setVisibility(View.VISIBLE);

                }
            }catch (NullPointerException e){
                holder.bar.setVisibility(View.GONE);
                holder.edit.setVisibility(View.GONE);
            }

        }catch (NullPointerException e){}
        holder.tracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://dheo.com/rocket?id=" + clients_all_payload.get(position).getShortId() +"&s=1");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                all_payload_context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return clients_all_payload.size();
    }

    public class AllPayloadHolder extends RecyclerView.ViewHolder {
        TextView customer_name, date_time, order_no, edit, bar, tracking,label, amount;
        RatingBar rating;
        public AllPayloadHolder(@NonNull View itemView) {
            super(itemView);
            this.customer_name = itemView.findViewById(R.id.all_item_customer_name);
            this.date_time = itemView.findViewById(R.id.all_item_date_time);
            this.rating = itemView.findViewById(R.id.all_item_rating);
            this.order_no = itemView.findViewById(R.id.all_item_order_no);
            this.edit =itemView.findViewById(R.id.all_item_edit);
            this.tracking = itemView.findViewById(R.id.all_item_track);
            this.label = itemView.findViewById(R.id.all_item_label);
            this.amount = itemView.findViewById(R.id.all_item_amount);
            this.bar = itemView.findViewById(R.id.all_item_bar);
        }
    }
}
