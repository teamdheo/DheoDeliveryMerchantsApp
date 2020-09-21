package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.ModelClassClientPaymentReceiptPDF.M;

import java.util.List;

import es.dmoral.toasty.Toasty;

public class AdapterClassPaymentReceiptPdf extends RecyclerView.Adapter<AdapterClassPaymentReceiptPdf.MyAdapter> {
    List<M> payment_receipt_date_list;
    Context receipt_context;
    StringBuffer dd,mm,yy;
    private int mm_1, mm_2;
    public AdapterClassPaymentReceiptPdf(List<M> payment_receipt_date_list, Context receipt_context){
        this.payment_receipt_date_list = payment_receipt_date_list;
        this.receipt_context = receipt_context;
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
    public void onBindViewHolder(@NonNull MyAdapter holder, final int position) {
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
                Intent intent = new Intent(Intent.ACTION_VIEW);

                intent.setDataAndType(Uri.parse( "https://rocket.dheo.com/client/billing/print_daily_statement?date=" + payment_receipt_date_list.get(position).getIdTime()+"&month="+mm_2+"&year=="+yy), "application/pdf");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                receipt_context.startActivity(intent);
                //Toasty.success(receipt_context, mm_2+"", Toast.LENGTH_LONG, true).show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return payment_receipt_date_list.size();
    }

    public class MyAdapter extends RecyclerView.ViewHolder {
        TextView pdf_date;
        public MyAdapter(@NonNull View itemView) {
            super(itemView);
            this.pdf_date = itemView.findViewById(R.id.receipt_date_pdf);
        }
    }
}
