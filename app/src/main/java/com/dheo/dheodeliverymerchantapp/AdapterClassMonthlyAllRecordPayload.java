package com.dheo.dheodeliverymerchantapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dheo.dheodeliverymerchantapp.ModelClassClientMonthlyStatementDate.M;

import java.util.List;

public class AdapterClassMonthlyAllRecordPayload extends RecyclerView.Adapter<AdapterClassMonthlyAllRecordPayload.RecordPayloadHolder> {
    private List<M> all_monthly_records_payloads;
    Context all_record_context;
    private  int cliendId;
    private String url;
    public AdapterClassMonthlyAllRecordPayload(List<M> all_monthly_records_payloads, Context all_record_context, int cliendId){
        this.all_monthly_records_payloads = all_monthly_records_payloads;
        this.all_record_context = all_record_context;
        this.cliendId = cliendId;
    }
    @NonNull
    @Override
    public RecordPayloadHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item_monthly_payload_pdf, parent, false);
        return new RecordPayloadHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecordPayloadHolder holder, final int position) {
        holder.all_record_view.setText("\uD83D\uDCC4 " +all_monthly_records_payloads.get(position).getPrettyMonth() +" " + all_monthly_records_payloads.get(position).getYear() + " >");
        holder.all_record_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                url = "https://rocket.dheo.com/client/billing/print_monthly_statement?month="+ all_monthly_records_payloads.get(position).getMonth() +"&year=" + all_monthly_records_payloads.get(position).getYear()+ "&id=" + cliendId +"&mode=deliveries";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse( url), "application/pdf");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                all_record_context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return all_monthly_records_payloads.size();
    }

    public class RecordPayloadHolder extends RecyclerView.ViewHolder {
        TextView all_record_view;
        public RecordPayloadHolder(@NonNull View itemView) {
            super(itemView);
            all_record_view = itemView.findViewById(R.id.monthly_payload_date_time);
        }
    }
}
