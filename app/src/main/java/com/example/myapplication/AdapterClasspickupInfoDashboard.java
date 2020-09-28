package com.example.myapplication;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.ModelClassAssingedCourierInfoDashboard.M;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AdapterClasspickupInfoDashboard extends RecyclerView.Adapter<AdapterClasspickupInfoDashboard.AgentViewHolder> {
    private List<M> pickup_info;
    private Context myContex;
    Helper helper;
    private String photo_url,icon_url;
    int start, end, dd_1, mm_1,yy_1;
    StringBuffer start_time, end_time,dd,mm,yy;
    public AdapterClasspickupInfoDashboard(List<M> pickup_info, Context myContex){
        this.pickup_info = pickup_info;
        this.myContex = myContex;
        Helper helper = new Helper(myContex);
    }

    @NonNull
    @Override
    public AgentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemview = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item_client_info_dashboard, parent, false);
        return new AgentViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull final AgentViewHolder holder, final int position) {
        holder.agent_name.setText(pickup_info.get(position).getAssignedAgentName());
        holder.agent_number.setText(pickup_info.get(position).getAssignedAgentNumber());
        photo_url = "https://dheo-static-sg.s3.ap-southeast-1.amazonaws.com/img/community/team/" + pickup_info.get(position).getAssignedAgentPhoto() ;
        Picasso.get().load(photo_url).into(holder.agent_photo);
        holder.pickup_address.setText(pickup_info.get(position).getPickupAddress());
        icon_url = "https://dheo-static-sg.s3-ap-southeast-1.amazonaws.com/img/location_icon.png";
        Picasso.get().load(icon_url).into(holder.location_icon);

        start_time = new StringBuffer(2);
        end_time = new StringBuffer(2);
        dd = new StringBuffer(2);
        mm = new StringBuffer(2);
        yy = new StringBuffer(4);
        dd.append(pickup_info.get(position).getStart().toString(), 8, 10);
        mm.append(pickup_info.get(position).getStart().toString(), 5, 7);
        yy.append(pickup_info.get(position).getStart().toString(), 0, 4);
        end_time.append(pickup_info.get(position).getEnd().toString(), 11, 13);
        //date.append(pick_up_slots.get(position).getStart().toString(), 0, 10);
        start_time.append(pickup_info.get(position).getStart().toString(), 11, 13);
        start = Integer.parseInt(start_time.toString());
        end = Integer.parseInt(end_time.toString());
        dd_1 = Integer.parseInt(dd.toString());
        mm_1 = Integer.parseInt(mm.toString());
        yy_1 = Integer.parseInt(yy.toString());
        String final_date = mm_1 + "/" + dd_1 + "/" + yy_1;
        //holder.day.setText(final_date);
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date d = new Date(final_date);
        String dayOfTheWeek = sdf.format(d);

        if(mm_1 == 1){
            holder.day_and_date.setText(dayOfTheWeek + ",  Jan " + dd_1+ "th");
        }
        else if(mm_1 == 2){
            holder.day_and_date.setText(dayOfTheWeek + ",  Feb " + dd_1+ "th");
        }
        else if(mm_1 == 3){
            holder.day_and_date.setText(dayOfTheWeek + ",  Mar " + dd_1+ "th");
        }
        else if(mm_1 == 4){
            holder.day_and_date.setText(dayOfTheWeek + ",  Apr " + dd_1+ "th");
        }
        else if(mm_1 == 5){
            holder.day_and_date.setText(dayOfTheWeek + ",  May " + dd_1+ "th");
        }
        else if(mm_1 == 6){
            holder.day_and_date.setText(dayOfTheWeek + ",  June " + dd_1+ "th");
        }
        else if(mm_1 == 7){
            holder.day_and_date.setText(dayOfTheWeek + ",  July " + dd_1+ "th");
        }
        else if(mm_1 == 8){
            holder.day_and_date.setText(dayOfTheWeek + ",  Aug " + dd_1+ "th");
        }
        else if(mm_1 == 9){
            holder.day_and_date.setText(dayOfTheWeek + ",  Sep " + dd_1+ "th");
        }
        else if(mm_1 == 10){
            holder.day_and_date.setText(dayOfTheWeek + ",  Oct " + dd_1+ "th");
        }
        else if(mm_1 == 11){
            holder.day_and_date.setText(dayOfTheWeek + ",  Nov " + dd_1+ "th");
        }
        else if(mm_1 == 12){
            holder.day_and_date.setText(dayOfTheWeek + ",  Dec " + dd_1+ "th");
        }




        start+=6;
        if (start > 12) {
            start = start - 12;
            holder.time_of_pickup.setText( "(" + start + ":00 pm ");
        } else if (start == 12) {
            holder.time_of_pickup.setText( "(" + start + ":00 pm ");
        } else {
            holder.time_of_pickup.setText( "(" + start + ":00 am ");
        }
        end+=6;
        if (end > 12) {
            end = end - 12;
            holder.time_of_pickup.append(" -  " + end + ":00 pm)");
        } else if (end == 12) {
            holder.time_of_pickup.append(" -  " + end + ":00 pm)");
        } else {
            holder.time_of_pickup.append(" - " + end + ":00 am)");
        }

    }

    @Override
    public int getItemCount() {
        return 1;
    }




    public class AgentViewHolder extends RecyclerView.ViewHolder {
        TextView day_and_date, time_of_pickup, pickup_address, agent_name, agent_number;
        ImageView agent_photo, location_icon;
        public AgentViewHolder(@NonNull View itemView) {
            super(itemView);
            this.day_and_date = itemView.findViewById(R.id.date_and_day);
            this.time_of_pickup = itemView.findViewById(R.id.time_of_pickup);
            this.pickup_address = itemView.findViewById(R.id.pickup_location);
            this.agent_name = itemView.findViewById(R.id.agent_name);
            this.agent_number = itemView.findViewById(R.id.agent_number);
            this.agent_photo = itemView.findViewById(R.id.agent_photo);
            this.location_icon = itemView.findViewById(R.id.location_icon);
        }
    }
}
