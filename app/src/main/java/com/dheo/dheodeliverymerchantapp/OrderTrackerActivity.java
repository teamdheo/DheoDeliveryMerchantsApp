package com.dheo.dheodeliverymerchantapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dheo.dheodeliverymerchantapp.ModelClassDeliveryMapInfo.DeliveryMapInfo;
import com.dheo.dheodeliverymerchantapp.ModelClassOrderStatusPageInfo.OrderStatusPageInfo;
import com.dheo.dheodeliverymerchantapp.ModelClassTrackerLogEntry.M;
import com.dheo.dheodeliverymerchantapp.ModelClassTrackerLogEntry.TrackerLogEntry;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderTrackerActivity extends AppCompatActivity implements OnMapReadyCallback {
    private String short_id, client_name, photo_url,label_image_url,pro_pic_url;
    private Button call_courier;
    private int payload_id,day;
    private TextView track_client_name,order_no,friday_note,track_courier, customer_name, customer_phone, courier_name, courier_phone,review,name_rating,customer_review;
    private ImageView track_client_image,label_image, courier_photo;
    private RecyclerView tracker_events;
    private RecyclerView.Adapter adapter;
    private RatingBar customer_rating;
    private RecyclerView.LayoutManager layoutManager;
    private List<M> all_log_entries;
    LinearLayout delivery_map_layout,customer_info,cash_payment_layout,label_image_layout;
    RelativeLayout customer_review_sec;
    SupportMapFragment fm;
    private double latitude;
    private double longitude;
    private LatLng latLng;
    private float zoomLevel;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    Helper helper = new Helper(this);

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getSupportActionBar().setElevation(0);//remove actionbar shadow
        setTitle("Order Tracker Page");
        setContentView(R.layout.activity_order_tracker);
        tracker_events = findViewById(R.id.tracker_events);
        track_client_name = findViewById(R.id.tracking_name);
        order_no = findViewById(R.id.order_no);
        friday_note = findViewById(R.id.friday_note);
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
        customer_info = findViewById(R.id.customer_info);
        label_image = findViewById(R.id.label_image);
        cash_payment_layout = findViewById(R.id.cash_payment_layout);
        label_image_layout = findViewById(R.id.label_image_layout);

        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            short_id = extras.getString("short_id");
            payload_id = extras.getInt("payload_id");
            client_name = extras.getString("client_name");
            pro_pic_url = extras.getString("image_url");
        }

        Toolbar toolbar = findViewById(R.id.color_toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        setSupportActionBar(toolbar);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toggle.syncState();

        review.setVisibility(View.GONE);
        customer_review_sec.setVisibility(View.GONE);
        friday_note.setVisibility(View.GONE);
        track_client_name.setText(client_name);
        order_no.setText("Order number: " + helper.getClientId() + "." + payload_id);
        day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        if (day == 6) {
            friday_note.setVisibility(View.VISIBLE);
        }
        View hView =  navigationView.getHeaderView(0);
        TextView nav_name = hView.findViewById(R.id.nav_name);
        ImageView nav_photo = hView.findViewById(R.id.nav_photo);
        nav_name.setText(client_name);
        //Toast.makeText(getApplicationContext(), payload_id+"", Toast.LENGTH_LONG).show();
        try {
            if (pro_pic_url.equals("default.svg")) {
                track_client_image = (ImageView) findViewById(R.id.tracker_profile_photo);
            } else {
                track_client_image = (ImageView) findViewById(R.id.tracker_profile_photo);
                photo_url = "https://dheo-static-sg.s3-ap-southeast-1.amazonaws.com/img/rocket/clients/" + pro_pic_url;
                Picasso.get().load(photo_url).memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE).into(track_client_image);
                Picasso.get().load(photo_url).memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE).into(nav_photo);
            }
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
               if(response.body() != null){
                   progressDialog.dismiss();
                   try {
                       TrackerLogEntry trackerLogEntry = response.body();
                       all_log_entries = trackerLogEntry.getM();
                       if (trackerLogEntry.getM().size() < 1) {
                           tracker_events.setVisibility(View.GONE);
                       }

                   } catch (NullPointerException e) {
                   }
                   adapter = new AdapterTrackerEvents(all_log_entries, getApplicationContext());
                   tracker_events.setAdapter(adapter);
               }
            }

            @Override
            public void onFailure(Call<TrackerLogEntry> call, Throwable t) {
                Toasty.error(getApplicationContext(), "Try Again!", Toast.LENGTH_LONG, true).show();
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
                    if (s.getE() == 0) {
                        try {
                            if (s.getM().getCashPayment()) {
                                customer_info.setVisibility(View.VISIBLE);
                                label_image_layout.setVisibility(View.VISIBLE);
                            }
                        } catch (NullPointerException e) {
                            label_image_layout.setVisibility(View.VISIBLE);
                            try {
                                label_image_url = "https://dheo-static.s3.ap-south-1.amazonaws.com/img/intakes/" + short_id + ".jpg";
                                Picasso.get().load(label_image_url).into(label_image);
                                cash_payment_layout.setVisibility(View.VISIBLE);

                            } catch (NullPointerException ef) {

                            }
                            customer_name.setText("Name: " + s.getM().getCustomerName());
                            customer_phone.setText("Phone: " + s.getM().getCustomerPhone());
                            customer_phone.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View arg0) {
                                    Intent intent = new Intent(Intent.ACTION_DIAL);
                                    intent.setData(Uri.parse("tel: +88" + s.getM().getCustomerPhone()));
                                    //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }
                            });
                            cash_payment_layout.setVisibility(View.GONE);
                        }
                        //Toast.makeText(getApplicationContext(), s.getM().getCustomerPhone()+"", Toast.LENGTH_LONG).show();
                        courier_name.setText(s.getM().getCourierName());
                        courier_phone.setText(s.getM().getCourierPhone());
                        String courier_url = "https://dheo-static-sg.s3.ap-southeast-1.amazonaws.com/img/community/team/" + s.getM().getCourierPhoto();
                        Picasso.get().load(courier_url).into(courier_photo);

                        call_courier.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View arg0) {
                                Intent intent = new Intent(Intent.ACTION_DIAL);
                                intent.setData(Uri.parse("tel: +88" + s.getM().getCourierPhone()));
                                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        });
                        try {
                            if (s.getM().getHasReview()) {
                                review.setVisibility(View.VISIBLE);
                                customer_review_sec.setVisibility(View.VISIBLE);
                                name_rating.setText(s.getM().getCustomerName() + "'s review: ");
                                customer_rating.setRating(s.getM().getCustomerRating());
                                customer_review.setText(s.getM().getCustomerReview());
                            }
                        } catch (NullPointerException e) {
                        }
                    }
                } catch (NullPointerException e) {
                }
            }

            @Override
            public void onFailure(Call<OrderStatusPageInfo> call, Throwable t) {
                Toasty.error(getApplicationContext(), "Try Again!", Toast.LENGTH_LONG, true).show();
            }
        });


        MenuItem dashboardItem = navigationView.getMenu().findItem(R.id.nav_dashboard);
        dashboardItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(getApplicationContext(), ClientDashboardActivity.class);
                startActivity(intent);
                return true;
            }
        });

        MenuItem billingItem = navigationView.getMenu().findItem(R.id.nav_billing);
        billingItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(getApplicationContext(), ClientDashboardBillingActivity.class);
                startActivity(intent);
                return true;
            }
        });

        MenuItem settingsItem = navigationView.getMenu().findItem(R.id.nav_setting);
        settingsItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(intent);
                return true;
            }
        });

        MenuItem performanceItem = navigationView.getMenu().findItem(R.id.nav_graph);
        performanceItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(getApplicationContext(), GraphActivity.class);
                startActivity(intent);
                return true;
            }
        });

        MenuItem orderCreateItem = navigationView.getMenu().findItem(R.id.nav_order);
        orderCreateItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(getApplicationContext(), PickupEntryActivity.class);
                startActivity(intent);
                return true;
            }
        });

        MenuItem callItem = navigationView.getMenu().findItem(R.id.nav_call);
        callItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel: 09613533533"));
                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                return true;
            }
        });

        MenuItem messageItem = navigationView.getMenu().findItem(R.id.nav_text);
        messageItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                String url = "https://m.me/dheolife";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                return true;
            }
        });

        MenuItem manualeItem = navigationView.getMenu().findItem(R.id.nav_userManual);
        manualeItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                String url = "http://rocket.dheo.com/user-manual";
//                Intent i = new Intent(Intent.ACTION_VIEW);
//                i.setData(Uri.parse(url));
//                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(i);
                Intent intent = new Intent(getApplicationContext(), UserManualActivity.class);
                intent.putExtra("url", url);
                startActivity(intent);
                return true;
            }
        });

        MenuItem meetTeamItem = navigationView.getMenu().findItem(R.id.nav_team);
        meetTeamItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                String url = "https://team.dheo.com";
//                Intent i = new Intent(Intent.ACTION_VIEW);
//                i.setData(Uri.parse(url));
//                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(i);
//                return true;
                Intent intent = new Intent(getApplicationContext(), UserManualActivity.class);
                intent.putExtra("url", url);
                startActivity(intent);
                return true;
            }
        });

        MenuItem logOutItem = navigationView.getMenu().findItem(R.id.nav_logout);
        logOutItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
                editor = sharedPreferences.edit();
                editor.putBoolean("saveLogin", false);
                editor.commit();
//                editor.clear();
//                editor.apply();
                Intent intent = new Intent(getApplicationContext(), LogInActivity.class);
                startActivity(intent);
                return true;
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
                Toasty.error(getApplicationContext(), "Try Again!", Toast.LENGTH_LONG, true).show();
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.three_dot_menu, menu);
        // Associate searchable configuration with the SearchView

//        SearchManager searchManager =
//                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//        SearchView searchView =
//                (SearchView) menu.findItem(R.id.search).getActionView();
//        searchView.setSearchableInfo(
//                searchManager.getSearchableInfo(getComponentName()));
//        searchView.setSubmitButtonEnabled(true);
        for(int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            SpannableString spanString = new SpannableString(menu.getItem(i).getTitle().toString());
            spanString.setSpan(new ForegroundColorSpan(Color.WHITE), 0,     spanString.length(), 0); //fix the color to white
            item.setTitle(spanString);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
            editor = sharedPreferences.edit();
            editor.putBoolean("saveLogin", false);
            editor.commit();
//                editor.clear();
//                editor.apply();
            Intent intent = new Intent(getApplicationContext(), LogInActivity.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.condition) {
            String url = "https://dheo.com/privacy";
            Intent intent = new Intent(getApplicationContext(), UserManualActivity.class);
            intent.putExtra("url", url);
            startActivity(intent);
            return true;
        }
        else if (item.getItemId() == R.id.settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }
        else if(item.getItemId()  == R.id.user_manual){
            String url = "https://rocket.dheo.com/user-manual";
            Intent intent = new Intent(getApplicationContext(), UserManualActivity.class);
            intent.putExtra("url", url);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    } //end//3 dot overflow menu

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        };


    }

}