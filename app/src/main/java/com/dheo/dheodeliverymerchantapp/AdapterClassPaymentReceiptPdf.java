package com.dheo.dheodeliverymerchantapp;

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

import com.dheo.dheodeliverymerchantapp.ModelClassClientPaymentReceiptPDF.M;

import java.util.List;

public class AdapterClassPaymentReceiptPdf extends RecyclerView.Adapter<AdapterClassPaymentReceiptPdf.MyAdapter> {
    List<M> payment_receipt_date_list;
    Context receipt_context;
    StringBuffer dd,mm,yy;
    private int mm_1, mm_2, client_id;
    private String yy_1, mm3,url, id, final_date;
    Helper helper;
    public AdapterClassPaymentReceiptPdf(List<M> payment_receipt_date_list, Context receipt_context, int client_id){
        this.payment_receipt_date_list = payment_receipt_date_list;
        this.client_id = client_id;
        this.receipt_context = receipt_context;
        Helper helper = new Helper(receipt_context);
    }
    @NonNull
    @Override
    public MyAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item_payment_receipt_pdf, parent, false);
        return new MyAdapter(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final MyAdapter holder, final int position) {
        try{
            try{
                holder.pdf_date.setText("\uD83D\uDCC4 " +payment_receipt_date_list.get(position).getTime()+" >");
            }catch (NullPointerException e){
                holder.pdf_date.setVisibility(View.GONE);
            }

            holder.pdf_date.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dd = new StringBuffer(2);
                    mm = new StringBuffer(2);
                    yy = new StringBuffer(4);
                    dd.append(payment_receipt_date_list.get(position).getIdTime().toString(), 8, 10);
                    mm.append(payment_receipt_date_list.get(position).getIdTime().toString(), 5, 7);
                    yy.append(payment_receipt_date_list.get(position).getIdTime().toString(), 0, 4);
                    mm_1 = Integer.parseInt(mm.toString());
                    mm_2 = mm_1 -1;
                    mm3 = String.valueOf(mm_2);
                    final_date =payment_receipt_date_list.get(position).getIdTime().toString();
                    yy_1 = yy.toString();
                    url = "https://rocket.dheo.com/client/billing/print_daily_statement?id="+ client_id +"&date=" + final_date+ "&month=" + mm3 +"&year="+ yy_1;
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.parse( url), "application/pdf");
                    //intent.setDataAndType(Uri.parse( "https://rocket.dheo.com/client/billing/print_daily_statement?id=2&date=2020-09-06T07:40:00.044Z&month=8&year=2020"), "application/pdf");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    receipt_context.startActivity(intent);


                }
            });
        }catch (NullPointerException e){

        }
    }

    @Override
    public int getItemCount() {
        return payment_receipt_date_list.size()-1;
    }

    public class MyAdapter extends RecyclerView.ViewHolder {
        TextView pdf_date;
        public MyAdapter(@NonNull View itemView) {
            super(itemView);
            this.pdf_date = itemView.findViewById(R.id.receipt_date_pdf);
        }
    }
}
