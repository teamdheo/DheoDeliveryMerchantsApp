package com.example.myapplication;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.modelClassAvaiablePickupSlot.M;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import es.dmoral.toasty.Toasty;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterSingleAddressSlotList extends RecyclerView.Adapter<AdapterSingleAddressSlotList.MyViewHolder> {
    private List<M> pick_up_slots;
    private Context context;
    Helper helper;
    int start, end, dd_1, mm_1,yy_1;
    StringBuffer start_time, end_time,dd,mm,yy;
    private boolean is_booked = false;
    Date todays_date = new Date();
    private String address_id;
    private  String slot_id;
    private int client_id;
    private String  v;
    private EditText edit_note;
    private Button save_note, cancel_note;

    public AdapterSingleAddressSlotList(List<M> pick_up_slots,Context context, String address_id){
        this.pick_up_slots = pick_up_slots;
        this.context = context;
        this.helper = new Helper(context);
        this.address_id = address_id;
    }


    @NonNull
    @Override
    public AdapterSingleAddressSlotList.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_single_address_slot_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final AdapterSingleAddressSlotList.MyViewHolder holder, final int position) {

        holder.id.setText(pick_up_slots.get(position).getId());
        try{
            if(pick_up_slots.get(position).getNextDay()){
                holder.delivery_day.setText("Delivery: Next day");
                holder.book.setText("Book");
                holder.booked.setVisibility(View.INVISIBLE);
            }
        }catch (NullPointerException e){

        }
        try{
            if(pick_up_slots.get(position).getSameDay()){
                holder.delivery_day.setText("Delivery: Today");
                holder.book.setText("Book");
                holder.booked.setVisibility(View.INVISIBLE);
            }
        }catch (NullPointerException e){

        }
        try{
            if(pick_up_slots.get(position).getSecondDay()){
                holder.delivery_day.setText("Delivery: Second Day");
                holder.book.setText("Book");
                holder.booked.setVisibility(View.INVISIBLE);
            }
        }catch (NullPointerException e){

        }
        try{
            if(pick_up_slots.get(position).getNextDay() && pick_up_slots.get(position).getAlreadyBooked()){
                holder.delivery_day.setText("Delivery: Next Day");
                holder.booked.setVisibility(View.VISIBLE);
                holder.booked.setText("Booked! ❎");
                holder.book.setVisibility(View.INVISIBLE);

            }
        }catch (NullPointerException e){

        }
        try{
            if(pick_up_slots.get(position).getSameDay() && pick_up_slots.get(position).getAlreadyBooked()){
                holder.delivery_day.setText("Delivery: Today");
                holder.booked.setVisibility(View.VISIBLE);
                holder.booked.setText("Booked! ❎");
                holder.book.setVisibility(View.INVISIBLE);

            }
        }catch (NullPointerException e){

        }
        try{
            if(pick_up_slots.get(position).getSecondDay() && pick_up_slots.get(position).getAlreadyBooked()){
                holder.delivery_day.setText("Delivery: Second Day");
                holder.booked.setVisibility(View.VISIBLE);
                holder.booked.setText("Booked! ❎");
                holder.book.setVisibility(View.INVISIBLE);

            }
        }catch (NullPointerException e){

        }
        try{
            if(pick_up_slots.get(position).getUnbookable() && pick_up_slots.get(position).getAlreadyBooked()){
                holder.booked.setVisibility(View.VISIBLE);
                holder.booked.setText("Booked! ❎");
                holder.delivery_day.setVisibility(View.INVISIBLE);
                holder.book.setVisibility(View.INVISIBLE);

            }
        }catch (NullPointerException e) {

        }
        client_id = helper.getClientId();

        holder.book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toasty.success(context, holder.id.getText()+"", Toast.LENGTH_LONG, true).show();
                Toasty.success(context, "done", Toast.LENGTH_LONG, true).show();
                holder.book.setVisibility(View.INVISIBLE);
                holder.booked.setVisibility(View.VISIBLE);
                holder.booked.setText("Booked! ❎");
//                Intent intent = new Intent(context, ListActivityMultiplePickupAddressSlots.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                context.startActivity(intent);
                Call<ResponseBody> call = RetrofitClient
                        .getInstance()
                        .getApi()
                        .book_address_pickup(client_id,address_id, holder.id.getText().toString());
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            String body = response.body().toString();
                            if(body.equals("{\"e\":0}")){

                            }
                        }catch (NullPointerException e){
                            Toasty.success(context, "Try again", Toast.LENGTH_LONG, true).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toasty.success(context, "server not connected", Toast.LENGTH_LONG, true).show();
                    }
                });

            }
        });
        holder.booked.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Toasty.success(context, "Cancel done", Toast.LENGTH_LONG, true).show();
                 holder.booked.setVisibility(View.INVISIBLE);
                 holder.book.setVisibility(View.VISIBLE);
                 holder.book.setText("Book");
                 Call<ResponseBody> call = RetrofitClient
                         .getInstance()
                         .getApi()
                         .cancel_pickup(client_id, holder.id.getText().toString());
                 call.enqueue(new Callback<ResponseBody>() {
                     @Override
                     public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                         try {
                             String body = response.body().toString();
                             if(body.equals("{\"e\":0}")){

                             }
                         }catch (NullPointerException e){
                             Toasty.success(context, "Try again", Toast.LENGTH_LONG, true).show();
                         }
                     }

                     @Override
                     public void onFailure(Call<ResponseBody> call, Throwable t) {
                         Toasty.success(context, "server not connected", Toast.LENGTH_LONG, true).show();
                     }
                 });
             }
         });

        holder.add_a_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder edit_dialog = new AlertDialog.Builder(view.getRootView().getContext());
                edit_dialog.setCancelable(false);
                LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View new_view = li.inflate(R.layout.add_note_dialog, null);
                edit_note = new_view.findViewById(R.id.edit_note);
                save_note = new_view.findViewById(R.id.edit_save_note);
                cancel_note = new_view.findViewById(R.id.edit_cancel_note);
                edit_dialog.setView(new_view);
                final AlertDialog dialog = edit_dialog.create();
                dialog.show();
                cancel_note.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

            }
        });


        //date = new StringBuffer(10);
        start_time = new StringBuffer(2);
        end_time = new StringBuffer(2);
        dd = new StringBuffer(2);
        mm = new StringBuffer(2);
        yy = new StringBuffer(4);
        dd.append(pick_up_slots.get(position).getStart().toString(), 8, 10);
        mm.append(pick_up_slots.get(position).getStart().toString(), 5, 7);
        yy.append(pick_up_slots.get(position).getStart().toString(), 0, 4);
        end_time.append(pick_up_slots.get(position).getEnd().toString(), 11, 13);
        //date.append(pick_up_slots.get(position).getStart().toString(), 0, 10);
        start_time.append(pick_up_slots.get(position).getStart().toString(), 11, 13);
        start = Integer.parseInt(start_time.toString());
        end = Integer.parseInt(end_time.toString());
        dd_1 = Integer.parseInt(dd.toString());
        mm_1 = Integer.parseInt(mm.toString());
        yy_1 = Integer.parseInt(yy.toString());
        if(mm_1 == 1){
            holder.date.setText(" ( Jan " + dd_1+ "th )");
        }
        else if(mm_1 == 2){
            holder.date.setText(" ( Feb " + dd_1+ "th )");
        }
        else if(mm_1 == 3){
            holder.date.setText(" ( Mar " + dd_1+ "th )");
        }
        else if(mm_1 == 4){
            holder.date.setText(" ( Apr " + dd_1+ "th )");
        }
        else if(mm_1 == 5){
            holder.date.setText(" ( May " + dd_1+ "th )");
        }
        else if(mm_1 == 6){
            holder.date.setText(" ( Jun " + dd_1+ "th )");
        }
        else if(mm_1 == 7){
            holder.date.setText(" ( July " + dd_1+ "th )");
        }
        else if(mm_1 == 8){
            holder.date.setText(" ( Aug " + dd_1+ "th )");
        }
        else if(mm_1 == 9){
            holder.date.setText(" ( Sep " + dd_1+ "th )");
        }
        else if(mm_1 == 10){
            holder.date.setText(" ( Oct " + dd_1+ "th )");
        }
        else if(mm_1 == 11){
            holder.date.setText(" ( Nov " + dd_1+ "th )");
        }
        else if(mm_1 == 12){
            holder.date.setText(" ( Dec " + dd_1+ "th )");
        }
        String final_date = mm_1 + "/" + dd_1 + "/" + yy_1;
        //holder.day.setText(final_date);
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date d = new Date(final_date);
        String dayOfTheWeek = sdf.format(d);
        holder.day.setText(dayOfTheWeek);



        start+=6;
        if (start > 12) {
            start = start - 12;
            holder.time.setText( " (" + start + ":00 pm ");
        } else if (start == 12) {
            holder.time.setText( " (" + start + ":00 pm ");
        } else {
            holder.time.setText( " (" + start + ":00 am ");
        }
        end+=6;
        if (end > 12) {
            end = end - 12;
            holder.time.append(" -  " + end + ":00 pm)");
        } else if (end == 12) {
            holder.time.append(" -  " + end + ":00 pm)");
        } else {
            holder.time.append(" - " + end + ":00 am)");
        }


    }

    @Override
    public int getItemCount() {
        return pick_up_slots.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView id, day,date,time, delivery_day, add_a_note;
        Button book,booked;
        CardView cardView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.id = itemView.findViewById(R.id.slot_id);
            this.day = itemView.findViewById(R.id.day);
            this.date = itemView.findViewById(R.id.date);
            this.delivery_day = itemView.findViewById(R.id.delivery_day);
            this.time = itemView.findViewById(R.id.time_slap);
            this.book = itemView.findViewById(R.id.book);
            this.booked = itemView.findViewById(R.id.booked);
            this.cardView = itemView.findViewById(R.id.recycler_red);
            this.add_a_note = itemView.findViewById(R.id.add_a_note);
        }
    }
}
