package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.ModelClassClientDashboardPayloads.M;
import com.example.myapplication.ModelClassClientEditPayload.ClientEditPayload;


import java.io.IOException;
import java.util.List;

import es.dmoral.toasty.Toasty;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterDashboardPayloadsList extends RecyclerView.Adapter<AdapterDashboardPayloadsList.PayloadViewHolder> {
    private List<M> dashboard_payload;
    Context payload_contex;
    EditText edit_phone, edit_amount;
    Button save_button, cancel_button;
    private String client_name;

    public AdapterDashboardPayloadsList(List<M> dashboard_payload, Context payload_contex, String client_name) {
        this.dashboard_payload = dashboard_payload;
        this.payload_contex = payload_contex;
        this.client_name = client_name;
    }

    @NonNull
    @Override
    public PayloadViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item_client_dashboard_payloads, parent, false);
        return new PayloadViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final PayloadViewHolder holder, final int position) {
        try {
            try {
                holder.customer_name.setText(dashboard_payload.get(position).getCustomerName());
            } catch (NullPointerException e) {
                holder.customer_name.setVisibility(View.GONE);
            }
            try {
                holder.date_time.setText(dashboard_payload.get(position).getDate());
            } catch (NullPointerException e) {
                holder.date_time.setVisibility(View.GONE);
            }
            try {
                holder.order_no.setText(dashboard_payload.get(position).getOrderNo());
            } catch (NullPointerException e) {
                holder.order_no.setVisibility(View.GONE);
            }
            try {
                if (dashboard_payload.get(position).getAmount() != null) {
                    holder.amount.setText(dashboard_payload.get(position).getAmount() + "TK");
                } else {
                    holder.amount.setVisibility(View.GONE);
                }
            } catch (NullPointerException e) {
                holder.amount.setVisibility(View.GONE);
            }
            try {
                if (dashboard_payload.get(position).getHasReview()) {
                    holder.rating.setRating(Float.parseFloat(dashboard_payload.get(position).getRating()));
                }
            } catch (NullPointerException e) {
                holder.rating.setVisibility(View.GONE);
            }
//

            try {
                if (dashboard_payload.get(position).getCourierDrop()) {
                    holder.label.setVisibility(View.VISIBLE);
                    holder.label.setText("Courier Drop");
                    holder.label.setBackground(ContextCompat.getDrawable(payload_contex, R.drawable.courier_drop));
                    holder.label.setTextColor(Color.rgb(0, 0, 0));
                }
            } catch (NullPointerException e) {
                //holder.label.setVisibility(View.INVISIBLE);
            }
            try {
                if (dashboard_payload.get(position).getDelayed()) {
                    holder.label.setVisibility(View.VISIBLE);
                    holder.label.setText("Delayed");
                    holder.label.setBackground(ContextCompat.getDrawable(payload_contex, R.drawable.delivery_delay));
                    holder.label.setTextColor(Color.rgb(0, 0, 0));
                }
            } catch (NullPointerException e) {
                //holder.label.setVisibility(View.INVISIBLE);
            }
            try {
                if (dashboard_payload.get(position).getDeliveryDone()) {
                    holder.label.setVisibility(View.VISIBLE);
                    holder.label.setText("Delivery Done");
                    holder.label.setBackground(ContextCompat.getDrawable(payload_contex, R.drawable.paid_on));
                }
            } catch (NullPointerException e) {
                // holder.label.setVisibility(View.INVISIBLE);
            }
            try {
                if (dashboard_payload.get(position).getDeliveryStarted()) {
                    holder.label.setVisibility(View.VISIBLE);
                    holder.label.setText("Delivery Started");
                    holder.label.setBackground(ContextCompat.getDrawable(payload_contex, R.drawable.delivery_start));
                }
            } catch (NullPointerException e) {
                //holder.label.setVisibility(View.INVISIBLE);
            }
            try {
                if (dashboard_payload.get(position).getPayloadCancelled()) {
                    holder.label.setVisibility(View.VISIBLE);
                    holder.label.setText("Delivery Canceled");
                    holder.label.setBackground(ContextCompat.getDrawable(payload_contex, R.drawable.delivery_cancel));
                }
            } catch (NullPointerException e) {
                //holder.label.setVisibility(View.INVISIBLE);
            }
            try {
                if (dashboard_payload.get(position).getReturnDone()) {
                    holder.label.setVisibility(View.VISIBLE);
                    holder.label.setText("Return Done");
                    holder.label.setBackground(ContextCompat.getDrawable(payload_contex, R.drawable.paid_on));
                }
            } catch (NullPointerException e) {
                //holder.label.setVisibility(View.INVISIBLE);
            }
            try {
                if (dashboard_payload.get(position).getReturnStarted()) {
                    holder.label.setVisibility(View.VISIBLE);
                    holder.label.setText("Return Started");
                    holder.label.setBackground(ContextCompat.getDrawable(payload_contex, R.drawable.delivery_start));

                }
            } catch (NullPointerException e) {
                //holder.label.setVisibility(View.INVISIBLE);
            }
            try {
                if(dashboard_payload.get(position).getCourierMemoAdded()){
                    holder.label.setText("Courier Memo Added");
                    holder.label.setVisibility(View.VISIBLE);
                    holder.label.setBackground(ContextCompat.getDrawable(payload_contex, R.drawable.paid_on));
                }
            } catch (NullPointerException e){}
            try {
                if (dashboard_payload.get(position).getOnHold()) {
                    holder.label.setText(dashboard_payload.get(position).getOnHoldLabel());
                    holder.label.setTextColor(Color.rgb(0, 0, 0));
                    holder.label.setBackground(ContextCompat.getDrawable(payload_contex, R.drawable.delivery_delay));

                }
            } catch (NullPointerException e) {
            }
            try {
                if (dashboard_payload.get(position).getEnableEdit()) {
                    holder.bar.setVisibility(View.VISIBLE);
                    holder.edit.setVisibility(View.VISIBLE);
                    holder.edit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(final View view) {
                            //Toasty.error(payload_contex, dashboard_payload.get(position).getPayloadId()+"failed", Toast.LENGTH_LONG, true).show();
                            Call<ClientEditPayload> call = RetrofitClient
                                    .getInstance()
                                    .getApi()
                                    .client_editable_payload(dashboard_payload.get(position).getPayloadId());
                            call.enqueue(new Callback<ClientEditPayload>() {
                                @Override
                                public void onResponse(Call<ClientEditPayload> call, Response<ClientEditPayload> response) {
                                    ClientEditPayload s = response.body();
                                    if (s.getE() == 0){
                                        try{
                                            AlertDialog.Builder edit_dialog = new AlertDialog.Builder(view.getRootView().getContext());
                                            edit_dialog.setCancelable(false);
                                            LayoutInflater li = (LayoutInflater) payload_contex.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                            View view = li.inflate(R.layout.dialog_payload_edit, null);

                                            edit_phone = view.findViewById(R.id.edit_phone);
                                            edit_amount = view.findViewById(R.id.edit_amount);
                                            save_button = view.findViewById(R.id.edit_save);
                                            cancel_button = view.findViewById(R.id.edit_cancel);
                                            edit_phone.setText(s.getM().getEdiableNumber());
                                            edit_amount.setText(s.getM().getEditableAmount());


                                            edit_dialog.setView(view);
                                            final AlertDialog dialog = edit_dialog.create();
                                            dialog.show();

                                            save_button.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    if(edit_amount.getText().toString() != null && edit_phone.getText().toString().length() == 11){
                                                        Call<ResponseBody> call1 =RetrofitClient
                                                                .getInstance()
                                                                .getApi()
                                                                .client_update_payload(dashboard_payload.get(position).getPayloadId(), edit_amount.getText().toString(), edit_phone.getText().toString() );
                                                        call1.enqueue(new Callback<ResponseBody>() {
                                                            @Override
                                                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                                try {
                                                                    if (response.body().string().equals("{\"e\":0}")){
                                                                        Toasty.error(payload_contex, "Data updated", Toast.LENGTH_LONG, true).show();
                                                                        dialog.dismiss();
                                                                        Intent intent = new Intent(payload_contex, ClientDashboardActivity.class);
                                                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                                        payload_contex.startActivity(intent);
                                                                    }
                                                                    else{
                                                                        Toasty.error(payload_contex, "failed", Toast.LENGTH_LONG, true).show();
                                                                        dialog.dismiss();
                                                                    }
                                                                } catch (IOException e) {
                                                                    e.printStackTrace();
                                                                }
                                                            }

                                                            @Override
                                                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                                                Toasty.error(payload_contex, "Try again", Toast.LENGTH_LONG, true).show();
                                                            }
                                                        });
                                                    }
                                                    else{
                                                        Toasty.error(payload_contex, "fill properly", Toast.LENGTH_LONG, true).show();
                                                    }
                                                    dialog.dismiss();
                                                }
                                            });
                                            cancel_button.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    dialog.dismiss();
                                                }
                                            });

                                        }catch (NullPointerException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    else{
                                        Toasty.error(payload_contex, "failed", Toast.LENGTH_LONG, true).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<ClientEditPayload> call, Throwable t) {
                                    Toasty.error(payload_contex, "Try again !", Toast.LENGTH_LONG, true).show();

                                }
                            });

                        }


                    });
                }
            } catch (NullPointerException e) {
                holder.bar.setVisibility(View.GONE);
                holder.edit.setVisibility(View.GONE);
                holder.tracking.setVisibility(View.VISIBLE);
            }
            holder.tracking.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Uri uri = Uri.parse("https://dheo.com/rocket?id=" + dashboard_payload.get(position).getShortId() + "&s=1");
//                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    payload_contex.startActivity(intent);
                    Intent intent = new Intent(payload_contex, OrderTrackerActivity.class);
                    intent.putExtra("short_id", dashboard_payload.get(position).getShortId());
                    intent.putExtra("payload_id", dashboard_payload.get(position).getPayloadId());
                    intent.putExtra("client_name", client_name);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    payload_contex.startActivity(intent);
                }
            });

        } catch (NullPointerException e) {
        }

    }

    @Override
    public int getItemCount() {
        return dashboard_payload.size();
    }

    public class PayloadViewHolder extends RecyclerView.ViewHolder {
        TextView customer_name, date_time, order_no, bar, edit, tracking, label, amount;
        RatingBar rating;

        public PayloadViewHolder(@NonNull View itemView) {
            super(itemView);
            this.customer_name = itemView.findViewById(R.id.item_customer_name);
            this.date_time = itemView.findViewById(R.id.item_date_time);
            this.rating = itemView.findViewById(R.id.item_rating);
            this.order_no = itemView.findViewById(R.id.item_order_no);
            this.edit = itemView.findViewById(R.id.item_edit);
            this.tracking = itemView.findViewById(R.id.item_track);
            this.label = itemView.findViewById(R.id.item_label);
            this.amount = itemView.findViewById(R.id.item_amount);
            this.bar = itemView.findViewById(R.id.item_bar);
        }
    }
}
