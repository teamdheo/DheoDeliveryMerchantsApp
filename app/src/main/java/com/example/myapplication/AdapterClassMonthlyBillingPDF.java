package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.ModelClassClientMonthlyStatementDate.M;

import java.util.List;

public class AdapterClassMonthlyBillingPDF extends RecyclerView.Adapter<AdapterClassMonthlyBillingPDF.MonthlyBillingholder> {
    private List<M> monthly_payment_all_date;
    Context monthly_billing_context;
    private int client_id;
    private String url;
    Helper helper;
    public AdapterClassMonthlyBillingPDF(List<M> monthly_payment_all_date, Context monthly_billing_context, int client_id){
        this.monthly_payment_all_date = monthly_payment_all_date;
        this.monthly_billing_context = monthly_billing_context;
        Helper helper = new Helper(monthly_billing_context);
        this.client_id = client_id;
    }
    @NonNull
    @Override
    public MonthlyBillingholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item_monthly_billing_pdf, parent, false);
        return new MonthlyBillingholder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MonthlyBillingholder holder, final int position) {
        holder.monthly_billing_date.setText("\uD83D\uDCC4 " +monthly_payment_all_date.get(position).getPrettyMonth() +" " + monthly_payment_all_date.get(position).getYear() + " >");
        holder.monthly_billing_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                url = "https://rocket.dheo.com/client/billing/print_monthly_statement?month="+ monthly_payment_all_date.get(position).getMonth() +"&year=" + monthly_payment_all_date.get(position).getYear()+ "&id=" + client_id +"&mode=billings";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse( url), "application/pdf");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                monthly_billing_context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return monthly_payment_all_date.size();
    }

    public class MonthlyBillingholder extends RecyclerView.ViewHolder {
        TextView monthly_billing_date;
        public MonthlyBillingholder(@NonNull View itemView) {
            super(itemView);
            monthly_billing_date = itemView.findViewById(R.id.monthly_billing_date_time);
        }
    }
}
