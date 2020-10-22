package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.SQLException;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.ModelClassTrackerLogEntry.M;
import com.example.myapplication.ModelClassTrackerLogEntry.TrackerLogEntry;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderTrackerActivity extends AppCompatActivity {
    private String short_id, client_name, photo_url,label_image_url;
    private int payload_id,day;
    private TextView track_client_name,order_no,friday_note, dhep_delivery, the_user_manual, meet_the_team, privacy_policy;
    private ImageView track_client_image,label_image;
    private RecyclerView tracker_events;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<M> all_log_entries;
    Helper helper = new Helper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setElevation(0);//remove actionbar shadow
        setTitle("Order tracker page");
        setContentView(R.layout.activity_order_tracker);
        tracker_events = findViewById(R.id.tracker_events);
        track_client_name = findViewById(R.id.tracking_name);
        order_no = findViewById(R.id.order_no);
        friday_note = findViewById(R.id.friday_note);
        dhep_delivery = findViewById(R.id.billing_dheo_delivery);
        the_user_manual = findViewById(R.id.billing_The_manual);
        meet_the_team = findViewById(R.id.billing_meet_team);
        privacy_policy = findViewById(R.id.billing_policy);
        layoutManager = new LinearLayoutManager(this);
        tracker_events.setLayoutManager(layoutManager);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            short_id = extras.getString("short_id");
            payload_id = extras.getInt("payload_id");
            client_name = extras.getString("client_name");
        }
        friday_note.setVisibility(View.GONE);
        track_client_name.setText(client_name);
        order_no.setText("Order number: "+helper.getClientId()+"."+payload_id);
        day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        if(day == 6){
            friday_note.setVisibility(View.VISIBLE);
        }
        //Toast.makeText(getApplicationContext(), day+"", Toast.LENGTH_LONG).show();
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
        try {
            label_image = findViewById(R.id.label_image);
            label_image_url = "https://dheo-static.s3.ap-south-1.amazonaws.com/img/intakes/" +short_id + ".jpg";
            Picasso.get().load(label_image_url).into(label_image);

        } catch (NullPointerException e) {

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

               }catch (NullPointerException e){}
               adapter = new AdapterTrackerEvents(all_log_entries,getApplicationContext());
               tracker_events.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<TrackerLogEntry> call, Throwable t) {

            }
        });
        dhep_delivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ClientDashboardActivity.class);
                startActivity(intent);
            }
        });
        the_user_manual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://rocket.dheo.com/user-manual";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });
        meet_the_team.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://team.dheo.com";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });
        privacy_policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://dheo.com/privacy";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });
    }
}