package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.ModelClassAssingedCourierInfoDashboard.AssingedCourierInfoDashboard;
import com.example.myapplication.ModelClassClientDashboardPayloads.ClientDashboardPayloads;
import com.example.myapplication.ModelClassClientMonthlyStatementDate.ClientMonthlyStatementDate;
import com.example.myapplication.ModelClassClientPayloadSearch.ClientPayloadSearch;
import com.example.myapplication.ModelClassPickupMapInfo.PickupMapInfo;
import com.example.myapplication.modelClassPickupAddresses.M;
import com.example.myapplication.modelClassPickupAddresses.PickupAddresses;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ClientDashboardActivity extends FragmentActivity implements OnMapReadyCallback {
    SQLiteDatabase sqLiteDatabase;
    private int session = 0;
    private String phone;
    private String photoUrl, scooter_url;
    private int clientId;
    private String password;
    private ImageView profile_photo, scooter, octopus_red_body;
    private TextView client_name, total_balance, phone_call, facebook, my_delivery, dashboard_billing, settings, user_manual, log_out, dhep_delivery, the_user_manual, meet_the_team, privacy_policy;
    private String photo_url;
    private String name;
    private int balance;
    private EditText payload_search_editText;
    private ProgressBar payload_progressbar, monthly_record_progress_bar;
    private Button request_pickup, next_pickup, see_more, payload_search_btn, go_back;
    private List<M> pickup_address_length;
    LinearLayout map_layout;
    SupportMapFragment fm;
    private double latitude;
    private double longitude;
    private LatLng latLng;
    private float zoomLevel;
    private List<com.example.myapplication.ModelClassClientDashboardPayloads.M> all_dashboard_payload;
    private List<com.example.myapplication.ModelClassAssingedCourierInfoDashboard.M> pickup_info_dashboard;
    private List<com.example.myapplication.ModelClassClientMonthlyStatementDate.M> monthly_payload_all_records;
    private List<com.example.myapplication.ModelClassClientPayloadSearch.M> payload_search;
    private List<com.example.myapplication.ModelClassPickupMapInfo.M> map_info;
    private RecyclerView pickup_list, dashboard_payloads, all_record_payload, recycler_search_payload;
    private RecyclerView.Adapter adapter, adapter_payload, adapter_record_payload, adapter_serch_payload;
    private RecyclerView.LayoutManager layoutManager, layoutManager1, layoutManager2;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    Helper helper = new Helper(this);
    private GoogleMap mMap;
    LocationManager locationManager;
    LocationListener locationListener;


//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//
//        if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//
//            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
//                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0, (android.location.LocationListener) locationListener);
//
//                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//            }
//        }
//    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        helper.checkInternetConnection();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_dashboard);
        client_name = (TextView) findViewById(R.id.name);
        total_balance = (TextView) findViewById(R.id.amount);
        request_pickup = (Button) findViewById(R.id.pickup_request);
        next_pickup = (Button) findViewById(R.id.Next_pickups);
        scooter = (ImageView) findViewById(R.id.scooter);
        see_more = (Button) findViewById(R.id.see_more);
        pickup_list = (RecyclerView) findViewById(R.id.recycler_pickup_agent);
        dashboard_payloads = (RecyclerView) findViewById(R.id.recycler_dashboard_payloads);
        all_record_payload = (RecyclerView) findViewById(R.id.recycler_monthly_payload_records);
        octopus_red_body = (ImageView) findViewById(R.id.octopus_red_body);
        phone_call = findViewById(R.id.dashboard_phone);
        facebook = findViewById(R.id.dashboard_fb);
        my_delivery = findViewById(R.id.dashboard_my_delivery);
        dashboard_billing = findViewById(R.id.dashboard_Billing);
        settings = findViewById(R.id.dashboard_settings);
        user_manual = findViewById(R.id.dashboard_user_manual);
        log_out = findViewById(R.id.dashboard_logout);
        dhep_delivery = findViewById(R.id.dashboard_dheo_delivery);
        the_user_manual = findViewById(R.id.dashboard_The_manual);
        meet_the_team = findViewById(R.id.dashboard_meet_team);
        privacy_policy = findViewById(R.id.dashboard_policy);
        payload_progressbar = findViewById(R.id.dashboard_payload_progressbar);
        payload_search_editText = findViewById(R.id.payload_search_editText);
        payload_search_btn = findViewById(R.id.payload_search_btn);
        map_layout = findViewById(R.id.map_layout);
        recycler_search_payload = (RecyclerView) findViewById(R.id.recycler_search_payload);
        //monthly_record_progress_bar = findViewById(R.id.monthly_record_payload_progressbar);
        go_back = findViewById(R.id.go_back);
        pickup_list.setHasFixedSize(true);
        pickup_list.setLayoutManager(new LinearLayoutManager(this));
        layoutManager = new LinearLayoutManager(this);
        dashboard_payloads.setLayoutManager(layoutManager);
        layoutManager1 = new LinearLayoutManager(this);
        all_record_payload.setLayoutManager(layoutManager1);
        layoutManager2 = new LinearLayoutManager(this);
        recycler_search_payload.setLayoutManager(layoutManager2);
        see_more.setVisibility(View.INVISIBLE);
        next_pickup.setVisibility(View.GONE);
        scooter.setVisibility(View.GONE);
        recycler_search_payload.setVisibility(View.GONE);
        go_back.setVisibility(View.GONE);
        dashboard_payloads.setVisibility(View.VISIBLE);
        fm = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        fm.getMapAsync(this);
        map_layout.setVisibility(View.GONE);


        //getSupportActionBar().setElevation(0);//remove actionbar shadow
        setTitle("My Dashboard");
        final androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(new ContextThemeWrapper(ClientDashboardActivity.this, R.style.AppTheme));
        builder.setCancelable(false);
        final View customLayout = getLayoutInflater().inflate(R.layout.loading_dialog, null);
        builder.setView(customLayout);
        final AlertDialog dialog = builder.create();
        try {
            if (helper.getPhoto_Url().equals("default.svg")) {
                profile_photo = (ImageView) findViewById(R.id.profile_photo);
            } else {
                profile_photo = (ImageView) findViewById(R.id.profile_photo);
                photo_url = "https://dheo-static-sg.s3-ap-southeast-1.amazonaws.com/img/rocket/clients/" + helper.getPhoto_Url();
                Picasso.get().load(photo_url).into(profile_photo);
            }
        } catch (SQLException e) {

        }
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            phone = extras.getString("PHONE");
            photoUrl = extras.getString("PhotoURL");
            password = extras.getString("PASS");
            clientId = extras.getInt("CLIENTId");
            session = extras.getInt("SESSION");
            name = extras.getString("name");
            balance = extras.getInt("balance");
        }
        client_name.setText(helper.getName());
        total_balance.setText(helper.getBalance() + "TK");

        sqLiteDatabase = getBaseContext().openOrCreateDatabase("SQLite", MODE_PRIVATE, null);
        String sql = "CREATE TABLE IF NOT EXISTS ClientProfileInfo (_id Integer Primary Key,phone TEXT,image TEXT,clientId Integer,password TEXT);";
        sqLiteDatabase.execSQL(sql);
        if (session == 1) {
            sql = "INSERT or REPLACE INTO ClientProfileInfo VALUES ( 1,'" + phone + "','" + photoUrl + "','" + clientId + "','" + password + "');";
            sqLiteDatabase.execSQL(sql);
        }
//
        Cursor query = sqLiteDatabase.rawQuery("SELECT * FROM ClientProfileInfo", null);
        if (query.moveToFirst()) {
            try {
                phone = query.getString(1);
                photoUrl = query.getString(2);
                clientId = query.getInt(3);
                password = query.getString(4);

            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        } else {
            Intent i = new Intent(getApplicationContext(), LogInActivity.class);
            startActivity(i);
        }
        query.close();
//        Call<PickupMapInfo> call6 = RetrofitClient
//                .getInstance()
//                .getApi()
//                .client_pickup_map(clientId);
//        call6.enqueue(new Callback<PickupMapInfo>() {
//            @Override
//            public void onResponse(Call<PickupMapInfo> call, Response<PickupMapInfo> response) {
//                PickupMapInfo info = response.body();
//                try{
//                    if(info.getM().getCourierPingMap().getAgents().size() > 0){
//                        map_layout.setVisibility(View.VISIBLE);
//                        next_pickup.setVisibility(View.GONE);
//                        scooter.setVisibility(View.GONE);
//                        pickup_list.setVisibility(View.GONE);
//
//                    }
//                }catch (NullPointerException e){}
//            }
//
//            @Override
//            public void onFailure(Call<PickupMapInfo> call, Throwable t) {
//
//            }
//        });

        Call<AssingedCourierInfoDashboard> call = RetrofitClient
                .getInstance()
                .getApi()
                .assigned_agent_info_dashboard(clientId);

        call.enqueue(new Callback<AssingedCourierInfoDashboard>() {
            @Override
            public void onResponse(Call<AssingedCourierInfoDashboard> call, Response<AssingedCourierInfoDashboard> response) {
                try {
                    AssingedCourierInfoDashboard pickup = response.body();
                    pickup_info_dashboard = pickup.getM();
                    if (pickup_info_dashboard.size() > 0) {

                        next_pickup.setVisibility(View.VISIBLE);
                        scooter.setVisibility(View.VISIBLE);
                        scooter_url = "https://dheo-static-sg.s3-ap-southeast-1.amazonaws.com/img/scooter.png";
                        Picasso.get().load(scooter_url).into(scooter);
                        next_pickup.setText("Next pickups");
                        request_pickup.setText("Cancel or Change");
                        Animation animation = new AlphaAnimation((float) 0.5, 0);
                        animation.setDuration(1000);
                        animation.setInterpolator(new LinearInterpolator());
                        animation.setRepeatCount(Animation.INFINITE);
                        animation.setRepeatMode(Animation.REVERSE);
                        scooter.startAnimation(animation);
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "failed", Toast.LENGTH_LONG).show();
                }
                adapter = new AdapterClasspickupInfoDashboard(pickup_info_dashboard, getApplicationContext());
                pickup_list.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<AssingedCourierInfoDashboard> call, Throwable t) {
                Toasty.error(getApplicationContext(), "স্লো ইন্টারনেটঃ আবার চেস্টা করুন!", Toast.LENGTH_LONG, true).show();
                Intent i = new Intent(getApplicationContext(), ClientDashboardActivity.class);
                startActivity(i);
            }

        });

        request_pickup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<PickupAddresses> call = RetrofitClient
                        .getInstance()
                        .getApi()
                        .get_pickup_address(clientId);

                call.enqueue(new Callback<PickupAddresses>() {
                    @Override
                    public void onResponse(Call<PickupAddresses> call, Response<PickupAddresses> response) {
                        try {
                            PickupAddresses pickup = response.body();
                            pickup_address_length = pickup.getM();
                            if (pickup_address_length.size() < 2) {
                                Intent i = new Intent(ClientDashboardActivity.this, SingleAddressPickupSlotActivity.class);
                                i.putExtra("address_id", pickup.getM().get(0).getAddress_id());
                                startActivity(i);
                            } else {
                                Intent i = new Intent(ClientDashboardActivity.this, ListActivityPickupAddress.class);
                                startActivity(i);
                            }
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "failed", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<PickupAddresses> call, Throwable t) {
                        Toasty.error(getApplicationContext(), "স্লো ইন্টারনেটঃ আবার চেস্টা করুন!", Toast.LENGTH_LONG, true).show();
                        Intent i = new Intent(getApplicationContext(), ClientDashboardActivity.class);
                        startActivity(i);
                    }

                });


//                Intent i = new Intent(ClientDashboardActivity.this,ListActivityPickupAddress.class);
//                startActivity(i);
            }
        });
        total_balance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ClientDashboardBillingActivity.class);
                intent.putExtra("name_c", name);
                intent.putExtra("balance_c", balance);
                startActivity(intent);
            }
        });
        Call<ClientDashboardPayloads> call2 = RetrofitClient
                .getInstance()
                .getApi()
                .client_dashboard_payloads(clientId);
        call2.enqueue(new Callback<ClientDashboardPayloads>() {
            @Override
            public void onResponse(Call<ClientDashboardPayloads> call, Response<ClientDashboardPayloads> response) {
                if (response.body() != null) {
                    try {
                        ClientDashboardPayloads clientDashboardPayloads = response.body();
                        all_dashboard_payload = clientDashboardPayloads.getM();
                        payload_progressbar.setVisibility(View.GONE);
                        if (all_dashboard_payload.size() >= 10) {
                            see_more.setVisibility(View.VISIBLE);
                        } else {
                            see_more.setVisibility(View.GONE);
                        }
                    } catch (NullPointerException e) {
                        see_more.setVisibility(View.GONE);
                        dashboard_payloads.setVisibility(View.GONE);
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "failed", Toast.LENGTH_LONG).show();
                    }
                }
                adapter_payload = new AdapterDashboardPayloadsList(all_dashboard_payload, getApplicationContext());
                dashboard_payloads.setAdapter(adapter_payload);
            }

            @Override
            public void onFailure(Call<ClientDashboardPayloads> call, Throwable t) {
                Toasty.error(getApplicationContext(), "স্লো ইন্টারনেটঃ আবার চেস্টা করুন!", Toast.LENGTH_LONG, true).show();
                Intent i = new Intent(getApplicationContext(), ClientDashboardActivity.class);
                startActivity(i);
            }
        });
        see_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AllPayloadsActivity.class);
                startActivity(intent);
            }
        });

        Call<ClientMonthlyStatementDate> record_call = RetrofitClient
                .getInstance()
                .getApi()
                .client_payment_statement_date(clientId);
        record_call.enqueue(new Callback<ClientMonthlyStatementDate>() {
            @Override
            public void onResponse(Call<ClientMonthlyStatementDate> call, Response<ClientMonthlyStatementDate> response) {
                if (response.body() != null) {
                    try {
                        ClientMonthlyStatementDate clientMonthlyStatementDate1 = response.body();
                        monthly_payload_all_records = clientMonthlyStatementDate1.getM();
                    } catch (NullPointerException e) {
                        all_record_payload.setVisibility(View.GONE);
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "failed", Toast.LENGTH_LONG).show();
                    }
                }
                if (response.body().getM().size() < 1) {
                    monthly_record_progress_bar.setVisibility(View.GONE);
                }

                adapter_record_payload = new AdapterClassMonthlyAllRecordPayload(monthly_payload_all_records, getApplicationContext(), clientId);
                all_record_payload.setAdapter(adapter_record_payload);
            }

            @Override
            public void onFailure(Call<ClientMonthlyStatementDate> call, Throwable t) {

            }
        });
        payload_search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if (payload_search_editText.getText().toString().length() != 0) {
                    dialog.show();
                    Call<ClientPayloadSearch> call_search = RetrofitClient
                            .getInstance()
                            .getApi()
                            .client_payload_search(clientId, payload_search_editText.getText().toString());
                    call_search.enqueue(new Callback<ClientPayloadSearch>() {
                        @Override
                        public void onResponse(Call<ClientPayloadSearch> call, Response<ClientPayloadSearch> response) {
                            if (response.body() != null) {
                                try {
                                    ClientPayloadSearch clientPayloadSearch = response.body();
                                    payload_search = clientPayloadSearch.getM();
                                    recycler_search_payload.setVisibility(View.VISIBLE);
                                    go_back.setText("< Back");
                                    go_back.setVisibility(View.VISIBLE);
                                    dashboard_payloads.setVisibility(View.GONE);
                                    see_more.setVisibility(View.GONE);
                                    dialog.dismiss();
                                    go_back.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent intent = new Intent(getApplicationContext(), ClientDashboardActivity.class);
                                            startActivity(intent);
                                        }
                                    });
                                } catch (NullPointerException e) {
                                    e.printStackTrace();
                                    Toast.makeText(getApplicationContext(), "failed", Toast.LENGTH_LONG).show();
                                }
                            }
                            if (response.body().getM().size() < 1) {
                                dialog.dismiss();
                                Toasty.error(getApplicationContext(), "Wrong input", Toast.LENGTH_LONG, true).show();
                            }
                            adapter_serch_payload = new AdapterSearchPayload(payload_search, getApplicationContext());
                            recycler_search_payload.setAdapter(adapter_serch_payload);
                        }

                        @Override
                        public void onFailure(Call<ClientPayloadSearch> call, Throwable t) {
                            Toast.makeText(getApplicationContext(), "Try again!", Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                        }
                    });
                } else {
                    Toasty.error(getApplicationContext(), "Input missing", Toast.LENGTH_LONG, true).show();
                }
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
        my_delivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ClientDashboardActivity.class);
                startActivity(intent);
            }
        });
        dashboard_billing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ClientDashboardBillingActivity.class);
                intent.putExtra("name_c", name);
                intent.putExtra("balance_c", balance);
                startActivity(intent);
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(intent);
            }
        });
        user_manual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://rocket.dheo.com/user-manual";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });

        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
                editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();
                Intent intent = new Intent(getApplicationContext(), LogInActivity.class);
                startActivity(intent);
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

    @Override
    public void onMapReady(final GoogleMap map) {
        Call<PickupMapInfo> call5 = RetrofitClient
                .getInstance()
                .getApi()
                .client_pickup_map(clientId);
        call5.enqueue(new Callback<PickupMapInfo>() {
            @Override
            public void onResponse(Call<PickupMapInfo> call, Response<PickupMapInfo> response) {
                try{
                    if(response.body() != null){
                        PickupMapInfo pickupMapInfo = response.body();
                        if(pickupMapInfo.getE() == 0){
                            for(int i = 0; i< pickupMapInfo.getM().getCourierPingMap().getAgents().size(); i++){
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                                sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
                                try {
                                    long time = sdf.parse(pickupMapInfo.getM().getCourierPingMap().getAgents().get(i).getPing().getUpdatedAt()).getTime();
                                    long now = System.currentTimeMillis();
                                    CharSequence ago = DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS);
                                    latitude = Double.parseDouble(pickupMapInfo.getM().getCourierPingMap().getAgents().get(i).getPing().getCoordinates().getLat());
                                    longitude =  Double.parseDouble(pickupMapInfo.getM().getCourierPingMap().getAgents().get(i).getPing().getCoordinates().getLong());
                                    latLng = new LatLng(latitude,longitude);
//                                Target target = new PicassoM;
//                                targets.add(target);
//                                Picasso.with(MapsActivity.this).load(myMarker.getmIcon()).resize(84, 125).into(target);
//
//                                photo_url = "https://dheo-static-sg.s3.ap-southeast-1.amazonaws.com/img/community/team/" + pickupMapInfo.getM().getCourierPingMap().getAgents().get(0).getPhoto() ;
//                                Picasso.get().load(photo_url).into();
                                    MarkerOptions marker = new MarkerOptions().position(new LatLng(latitude, longitude)).title(pickupMapInfo.getM().getCourierPingMap().getAgents().get(i).getName()+"("+pickupMapInfo.getM().getCourierPingMap().getAgents().get(i).getPhone()+")")
                                            .snippet("Last seen "+ago.toString());
                                    zoomLevel = 10.0f; //This goes up to 21
                                    Marker m = map.addMarker(marker);
                                    m.showInfoWindow();
                                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));
                                    //map.setMyLocationEnabled(true);
                                    map.setTrafficEnabled(true);
                                    map.setIndoorEnabled(true);
                                    map.setBuildingsEnabled(true);

                                    map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                                    map.getUiSettings().setZoomControlsEnabled(true);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }catch (NullPointerException e){}
            }

            @Override
            public void onFailure(Call<PickupMapInfo> call, Throwable t) {
                Toasty.error(getApplicationContext(), "failed", Toast.LENGTH_LONG, true).show();
            }
        });

    }
}