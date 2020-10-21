package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.SQLException;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.ModelClassTrackerLogEntry.M;
import com.example.myapplication.ModelClassTrackerLogEntry.TrackerLogEntry;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderTrackerActivity extends AppCompatActivity {
    private String short_id, client_name, photo_url;
    private int payload_id;
    private TextView track_client_name,order_no;
    private ImageView track_client_image;
    private RecyclerView tracker_events;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<M> all_log_entries;
    Helper helper = new Helper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_tracker);
        tracker_events = findViewById(R.id.tracker_events);
        track_client_name = findViewById(R.id.tracking_name);
        order_no = findViewById(R.id.order_no);
        layoutManager = new LinearLayoutManager(this);
        tracker_events.setLayoutManager(layoutManager);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            short_id = extras.getString("short_id");
            payload_id = extras.getInt("payload_id");
            client_name = extras.getString("client_name");
        }
        track_client_name.setText(client_name);
        order_no.setText("Order number: "+helper.getClientId()+"."+payload_id);
        try {
            if (helper.getPhoto_Url().equals("default.svg")) {
                track_client_image = (ImageView) findViewById(R.id.tracker_profile_photo);
            } else {
                track_client_image = (ImageView) findViewById(R.id.tracker_profile_photo);
                photo_url = "https://dheo-static-sg.s3-ap-southeast-1.amazonaws.com/img/rocket/clients/" + helper.getPhoto_Url();
                Picasso.get().load(photo_url).into(track_client_image);
            }
        } catch (SQLException e) {

        }

        Call<TrackerLogEntry> call = RetrofitClient
                .getInstance()
                .getApi()
                .order_tracker_log_entry(payload_id);
        call.enqueue(new Callback<TrackerLogEntry>() {
            @Override
            public void onResponse(Call<TrackerLogEntry> call, Response<TrackerLogEntry> response) {
               try {
                   TrackerLogEntry trackerLogEntry = response.body();
                   all_log_entries = trackerLogEntry.getM();
                   if (trackerLogEntry.getM().size()<1){
                       tracker_events.setVisibility(View.GONE);
                   }
                   Toast.makeText(getApplicationContext(), trackerLogEntry.getM().size() +"", Toast.LENGTH_LONG).show();
               }catch (NullPointerException e){}
               adapter = new AdapterTrackerEvents(all_log_entries,getApplicationContext());
               tracker_events.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<TrackerLogEntry> call, Throwable t) {

            }
        });
    }
}