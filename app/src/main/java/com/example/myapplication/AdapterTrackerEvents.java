package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.ModelClassTrackerLogEntry.M;


import java.util.List;

public class AdapterTrackerEvents extends RecyclerView.Adapter<AdapterTrackerEvents.MyEventViewHolder> {
    private List<M> event_list;
    Context event_context;
    private String photo_url;

    public AdapterTrackerEvents(List<M> event_list, Context event_context){
        this.event_list = event_list;
        this.event_context = event_context;
    }
    @NonNull
    @Override
    public MyEventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item_event_log_entry, parent, false);
        return new MyEventViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyEventViewHolder holder, int position) {
        try{
            holder.event_time.setText(event_list.get(position).getTime());
        }catch (NullPointerException e){}
        try{
            holder.event_date.setText(event_list.get(position).getDate());
        }catch (NullPointerException e){}
        try{
            holder.event_description.setText(event_list.get(position).getDescription() + "\n");
        }catch (NullPointerException e){}
       if(position == event_list.size()-1){
           holder.dotted_line.setVisibility(View.GONE);
       }
    }

    @Override
    public int getItemCount() {
        return event_list.size();
    }

    public class MyEventViewHolder extends RecyclerView.ViewHolder {
        TextView event_date, event_time, event_description;
        ImageView event_photo;
        View dotted_line;
        public MyEventViewHolder(@NonNull View itemView) {
            super(itemView);
            this.event_time = itemView.findViewById(R.id.event_time);
            this.event_date = itemView.findViewById(R.id.event_date);
            this.event_description = itemView.findViewById(R.id.event_description);
            this.dotted_line = itemView.findViewById(R.id.dotted_line);


        }
    }
}
