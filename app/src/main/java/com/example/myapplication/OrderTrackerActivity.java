package com.example.myapplication;

import android.content.Intent;
import android.database.SQLException;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.ModelClassDeliveryMapInfo.DeliveryMapInfo;
import com.example.myapplication.ModelClassOrderStatusPageInfo.OrderStatusPageInfo;
import com.example.myapplication.ModelClassTrackerLogEntry.M;
import com.example.myapplication.ModelClassTrackerLogEntry.TrackerLogEntry;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderTrackerActivity extends AppCompatActivity implements OnMapReadyCallback {
    private String short_id, client_name, photo_url,label_image_url;
    private Button call_courier;
    private int payload_id,day;
    private TextView track_client_name,order_no,friday_note, dhep_delivery, the_user_manual, meet_the_team, privacy_policy,track_courier, customer_name, customer_phone, courier_name, courier_phone,review,name_rating,customer_review,phone_call, facebook;
    private ImageView track_client_image,label_image, courier_photo;
    private RecyclerView tracker_events;
    private RecyclerView.Adapter adapter;
    private RatingBar customer_rating;
    private RecyclerView.LayoutManager layoutManager;
    private List<M> all_log_entries;
    LinearLayout delivery_map_layout;
    RelativeLayout customer_review_sec;
    SupportMapFragment fm;
    private double latitude;
    private double longitude;
    private LatLng latLng;
    private float zoomLevel;
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
        delivery_map_layout = findViewById(R.id.delivery_map_layout);
        track_courier = findViewById(R.id.track_courier);
        customer_name = findViewById(R.id.customer_name);
        customer_phone = findViewById(R.id.customer_phone);
        courier_name = findViewById(R.id.courier_name);
        courier_phone = findViewById(R.id.courier_phone);
        courier_photo = findViewById(R.id.courier_photo);
        call_courier = findViewById(R.id.call_courier);
        customer_review_sec = findViewById(R.id.customer_review_sec);
        review = findViewById(R.id.review);
        name_rating = findViewById(R.id.name_rating);
        customer_rating = findViewById(R.id.customer_rating);
        customer_review = findViewById(R.id.customer_review);
        phone_call = findViewById(R.id.tracking_phone);
        facebook = findViewById(R.id.tracking_fb);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            short_id = extras.getString("short_id");
            payload_id = extras.getInt("payload_id");
            client_name = extras.getString("client_name");
        }
        review.setVisibility(View.GONE);
        customer_review_sec.setVisibility(View.GONE);
        friday_note.setVisibility(View.GONE);
        track_client_name.setText(client_name);
        order_no.setText("Order number: "+helper.getClientId()+"."+payload_id);
        day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        if(day == 6){
            friday_note.setVisibility(View.VISIBLE);
        }
        //Toast.makeText(getApplicationContext(), payload_id+"", Toast.LENGTH_LONG).show();
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
        fm = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.delivery_map);
        fm.getMapAsync(this);
        delivery_map_layout.setVisibility(View.GONE);
        track_courier.setVisibility(View.GONE);

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

        Call<OrderStatusPageInfo> call2 = RetrofitClient
                .getInstance()
                .getApi()
                .order_status_page_info(payload_id);
        call2.enqueue(new Callback<OrderStatusPageInfo>() {
            @Override
            public void onResponse(Call<OrderStatusPageInfo> call, Response<OrderStatusPageInfo> response) {
               try {
                   final OrderStatusPageInfo s = response.body();
                   if(s.getE() == 0){
                       //Toast.makeText(getApplicationContext(), s.getM().getCustomerPhone()+"", Toast.LENGTH_LONG).show();
                       customer_name.setText("Name: " + s.getM().getCustomerName());
                       customer_phone.setText("Phone: "+ s.getM().getCustomerPhone());
                       courier_name.setText(s.getM().getCourierName());
                       courier_phone.setText(s.getM().getCourierPhone());
                       String courier_url = "https://dheo-static-sg.s3.ap-southeast-1.amazonaws.com/img/community/team/" + s.getM().getCourierPhoto() ;
                       Picasso.get().load(courier_url).into(courier_photo);
                       customer_phone.setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View arg0) {
                               Intent intent = new Intent(Intent.ACTION_DIAL);
                               intent.setData(Uri.parse("tel: +88"+s.getM().getCustomerPhone()));
                               //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                               startActivity(intent);
                           }
                       });
                       call_courier.setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View arg0) {
                               Intent intent = new Intent(Intent.ACTION_DIAL);
                               intent.setData(Uri.parse("tel: +88"+s.getM().getCourierPhone()));
                               //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                               startActivity(intent);
                           }
                       });
                      try {
                          if(s.getM().getHasReview()){
                              review.setVisibility(View.VISIBLE);
                              customer_review_sec.setVisibility(View.VISIBLE);
                              name_rating.setText(s.getM().getCustomerName() +"'s review: ");
                              customer_rating.setRating(s.getM().getCustomerRating());
                              customer_review.setText(s.getM().getCustomerReview());
                          }
                      }catch (NullPointerException e){}
                   }
               }catch (NullPointerException e){}
            }

            @Override
            public void onFailure(Call<OrderStatusPageInfo> call, Throwable t) {

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
        phone_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel: +8801301377181"));
                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://m.me/dheolife";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });
    }

    @Override
    public void onMapReady(final GoogleMap map) {
        Call<DeliveryMapInfo> call1 = RetrofitClient
                .getInstance()
                .getApi()
                .delivery_map_info(payload_id);
        call1.enqueue(new Callback<DeliveryMapInfo>() {
            @Override
            public void onResponse(Call<DeliveryMapInfo> call, Response<DeliveryMapInfo> response) {
                try {
                    DeliveryMapInfo s = response.body();
                    if (s.getE() == 0){
                        if(s.getM().getCourierPingMap()){
                            delivery_map_layout.setVisibility(View.VISIBLE);
                            track_courier.setVisibility(View.VISIBLE);
                            //Toast.makeText(getApplicationContext(), s.getM().getCourierName()+"", Toast.LENGTH_LONG).show();
                            latitude = Double.parseDouble(s.getM().getLatitude());
                            longitude =  Double.parseDouble(s.getM().getLongitude());
                            latLng = new LatLng(latitude,longitude);
                            MarkerOptions marker = new MarkerOptions().position(new LatLng(latitude, longitude)).title(s.getM().getCourierName() + "( "+ s.getM().getCourierPhone() + " )")
                                    .snippet("last seen " + s.getM().getLastSeen());
                            zoomLevel = 12.0f; //This goes up to 21
                            Marker m = map.addMarker(marker);
                            m.showInfoWindow();
                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));
                            //map.setMyLocationEnabled(true);
                            map.setTrafficEnabled(true);
                            map.setIndoorEnabled(true);
                            map.setBuildingsEnabled(true);

                            map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                            map.getUiSettings().setZoomControlsEnabled(true);
                        }

                    }
                }catch (NullPointerException e){}
            }

            @Override
            public void onFailure(Call<DeliveryMapInfo> call, Throwable t) {

            }
        });
    }
}