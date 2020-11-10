package com.dheo.myapplication;

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

import com.dheo.myapplication.ModelClassClientPayloadSearch.M;

import java.util.List;

public class AdapterSearchPayload extends RecyclerView.Adapter<AdapterSearchPayload.SearchPayloadHolder> {
    private List<M> search_payload;
    Context search_contex;

    public AdapterSearchPayload(List<M> search_payload, Context search_contex){
        this.search_payload = search_payload;
        this.search_contex = search_contex;
    }
    @NonNull
    @Override
    public SearchPayloadHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item_dashboard_search_payload, parent, false);
        return new SearchPayloadHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchPayloadHolder holder, final int position) {
        try {
            try {
                holder.customer_name.setText(search_payload.get(position).getCustomerName());
            } catch (NullPointerException e) {
                holder.customer_name.setVisibility(View.GONE);
            }
            try {
                holder.date_time.setText(search_payload.get(position).getDate());
            } catch (NullPointerException e) {
                holder.date_time.setVisibility(View.GONE);
            }
            try {
                holder.order_no.setText(search_payload.get(position).getOrderNo());
            } catch (NullPointerException e) {
                holder.order_no.setVisibility(View.GONE);
            }
            try {
                if (search_payload.get(position).getAmount() != null) {
                    holder.amount.setText(search_payload.get(position).getAmount() + "TK");
                } else {
                    holder.amount.setVisibility(View.GONE);
                }
            } catch (NullPointerException e) {
                holder.amount.setVisibility(View.GONE);
            }
            try {
                if (search_payload.get(position).getHasReview()) {
                    holder.rating.setRating(Float.parseFloat(search_payload.get(position).getRating()));
                }
            } catch (NullPointerException e) {
                holder.rating.setVisibility(View.GONE);
            }
//

            try {
                if (search_payload.get(position).getCourierDrop()) {
                    holder.label.setVisibility(View.VISIBLE);
                    holder.label.setText("Courier Drop");
                    holder.label.setBackground(ContextCompat.getDrawable(search_contex, R.drawable.courier_drop));
                    holder.label.setTextColor(Color.rgb(0, 0, 0));
                }
            } catch (NullPointerException e) {
                //holder.label.setVisibility(View.INVISIBLE);
            }
            try {
                if (search_payload.get(position).getDelayed()) {
                    holder.label.setVisibility(View.VISIBLE);
                    holder.label.setText("Delayed");
                    holder.label.setBackground(ContextCompat.getDrawable(search_contex, R.drawable.delivery_delay));
                    holder.label.setTextColor(Color.rgb(0, 0, 0));
                }
            } catch (NullPointerException e) {
                //holder.label.setVisibility(View.INVISIBLE);
            }
            try {
                if (search_payload.get(position).getDeliveryDone()) {
                    holder.label.setVisibility(View.VISIBLE);
                    holder.label.setText("Delivery Done");
                    holder.label.setBackground(ContextCompat.getDrawable(search_contex, R.drawable.paid_on));
                }
            } catch (NullPointerException e) {
                // holder.label.setVisibility(View.INVISIBLE);
            }
            try {
                if (search_payload.get(position).getDeliveryStarted()) {
                    holder.label.setVisibility(View.VISIBLE);
                    holder.label.setText("Delivery Started");
                    holder.label.setBackground(ContextCompat.getDrawable(search_contex, R.drawable.delivery_start));
                }
            } catch (NullPointerException e) {
                //holder.label.setVisibility(View.INVISIBLE);
            }
            try {
                if (search_payload.get(position).getPayloadCancelled()) {
                    holder.label.setVisibility(View.VISIBLE);
                    holder.label.setText("Delivery Canceled");
                    holder.label.setBackground(ContextCompat.getDrawable(search_contex, R.drawable.delivery_cancel));
                }
            } catch (NullPointerException e) {
                //holder.label.setVisibility(View.INVISIBLE);
            }
            try {
                if (search_payload.get(position).getReturnDone()) {
                    holder.label.setVisibility(View.VISIBLE);
                    holder.label.setText("Return Done");
                    holder.label.setBackground(ContextCompat.getDrawable(search_contex, R.drawable.paid_on));
                }
            } catch (NullPointerException e) {
                //holder.label.setVisibility(View.INVISIBLE);
            }
            try {
                if (search_payload.get(position).getReturnStarted()) {
                    holder.label.setVisibility(View.VISIBLE);
                    holder.label.setText("Return Started");
                    holder.label.setBackground(ContextCompat.getDrawable(search_contex, R.drawable.delivery_start));

                }
            } catch (NullPointerException e) {
                //holder.label.setVisibility(View.INVISIBLE);
            }
            try {
                if(search_payload.get(position).getCourierMemoAdded()){
                    holder.label.setText("Courier Memo Added");
                    holder.label.setVisibility(View.VISIBLE);
                    holder.label.setBackground(ContextCompat.getDrawable(search_contex, R.drawable.paid_on));
                }
            } catch (NullPointerException e){}
            try {
                if (search_payload.get(position).getOnHold()) {
                    holder.label.setText(search_payload.get(position).getOnHoldLabel());
                    holder.label.setTextColor(Color.rgb(0, 0, 0));
                    holder.label.setBackground(ContextCompat.getDrawable(search_contex, R.drawable.delivery_delay));

                }
            } catch (NullPointerException e) {
            }

            holder.tracking.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Uri uri = Uri.parse("https://dheo.com/rocket?id=" + search_payload.get(position).getShortId() + "&s=1");
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    search_contex.startActivity(intent);
                }
            });

        } catch (NullPointerException e) {
        }

    }

    @Override
    public int getItemCount() {
        return search_payload.size();
    }

    public class SearchPayloadHolder extends RecyclerView.ViewHolder {
        TextView customer_name, date_time, order_no, bar, tracking, label, amount;
        RatingBar rating;
        public SearchPayloadHolder(@NonNull View itemView) {
            super(itemView);
            this.customer_name = itemView.findViewById(R.id.search_customer_name);
            this.date_time = itemView.findViewById(R.id.search_date_time);
            this.rating = itemView.findViewById(R.id.search_rating);
            this.order_no = itemView.findViewById(R.id.search_order_no);
            this.tracking = itemView.findViewById(R.id.search_track);
            this.label = itemView.findViewById(R.id.search_label);
            this.amount = itemView.findViewById(R.id.search_amount);
            this.bar = itemView.findViewById(R.id.search_bar);
        }
    }
}
