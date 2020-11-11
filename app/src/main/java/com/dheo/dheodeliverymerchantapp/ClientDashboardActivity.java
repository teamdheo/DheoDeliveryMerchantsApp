package com.dheo.dheodeliverymerchantapp;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dheo.dheodeliverymerchantapp.ModelClassAssingedCourierInfoDashboard.AssingedCourierInfoDashboard;
import com.dheo.dheodeliverymerchantapp.ModelClassBlogUpdateTitle.BlogUpdateTitle;
import com.dheo.dheodeliverymerchantapp.ModelClassClientBasicInfo.ClientBasicInfo;
import com.dheo.dheodeliverymerchantapp.ModelClassClientDashboardPayloads.ClientDashboardPayloads;
import com.dheo.dheodeliverymerchantapp.ModelClassClientMonthlyStatementDate.ClientMonthlyStatementDate;
import com.dheo.dheodeliverymerchantapp.ModelClassClientPayloadSearch.ClientPayloadSearch;
import com.dheo.dheodeliverymerchantapp.ModelClassPickupMapInfo.PickupMapInfo;
import com.dheo.dheodeliverymerchantapp.modelClassPickupAddresses.M;
import com.dheo.dheodeliverymerchantapp.modelClassPickupAddresses.PickupAddresses;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

import es.dmoral.toasty.Toasty;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ClientDashboardActivity extends AppCompatActivity implements OnMapReadyCallback {
    SQLiteDatabase sqLiteDatabase;
    private int session = 0;
    private String phone;
    private String photoUrl, scooter_url, cover_url;
    boolean doubleBackToExitPressedOnce = false;
    private int clientId,page_number = 1,payload_remaining;
    private String password;
    private ImageView profile_photo, scooter, octopus_red_body, cover, blog_photo;
    private TextView client_name, total_balance, phone_call, facebook, my_delivery, dashboard_billing, settings, user_manual, log_out, dhep_delivery, the_user_manual, meet_the_team, privacy_policy, blog_title, blog_see_more;
    private String photo_url, blog_url;
    private String name;
    private int balance;
    private EditText payload_search_editText;
    private ProgressBar payload_progressbar, monthly_record_progress_bar;
    private Button request_pickup, next_pickup, see_older, see_newer, payload_search_btn, go_back;
    private List<M> pickup_address_length;
    SupportMapFragment fm;
    private double latitude;
    private double longitude;
    private LatLng latLng;
    private float zoomLevel;
    private List<com.dheo.dheodeliverymerchantapp.ModelClassClientDashboardPayloads.M> all_dashboard_payload;
    private List<com.dheo.dheodeliverymerchantapp.ModelClassAssingedCourierInfoDashboard.M> pickup_info_dashboard;
    private List<com.dheo.dheodeliverymerchantapp.ModelClassClientMonthlyStatementDate.M> monthly_payload_all_records;
    private List<com.dheo.dheodeliverymerchantapp.ModelClassClientPayloadSearch.M> payload_search;
    private List<com.dheo.dheodeliverymerchantapp.ModelClassPickupMapInfo.M> map_info;
    private RecyclerView pickup_list, dashboard_payloads, all_record_payload, recycler_search_payload;
    private RecyclerView.Adapter adapter, adapter_payload, adapter_record_payload, adapter_serch_payload;
    private RecyclerView.LayoutManager layoutManager, layoutManager1, layoutManager2;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    LinearLayout map_layout;
    Helper helper = new Helper(this);
    private GoogleMap mMap;
    LocationManager locationManager;
    LocationListener locationListener;
    NestedScrollView scrollView;


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (android.location.LocationListener) locationListener);

                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }
        }
    }


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
        see_older = (Button) findViewById(R.id.show_older);
        see_newer = findViewById(R.id.show_newer);
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
        recycler_search_payload = (RecyclerView) findViewById(R.id.recycler_search_payload);
        //monthly_record_progress_bar = findViewById(R.id.monthly_record_payload_progressbar);
        go_back = findViewById(R.id.go_back);
        map_layout = findViewById(R.id.map_layout);
        cover = findViewById(R.id.cover);
        blog_photo = findViewById(R.id.blog_photo);
        blog_title = findViewById(R.id.blog_title);
        blog_see_more = findViewById(R.id.blog_see_more);
        pickup_list.setHasFixedSize(true);
        pickup_list.setLayoutManager(new LinearLayoutManager(this));
        layoutManager = new LinearLayoutManager(this);
        dashboard_payloads.setLayoutManager(layoutManager);
        layoutManager1 = new LinearLayoutManager(this);
        all_record_payload.setLayoutManager(layoutManager1);
        layoutManager2 = new LinearLayoutManager(this);
        recycler_search_payload.setLayoutManager(layoutManager2);
        see_older.setVisibility(View.INVISIBLE);
        see_newer.setVisibility(View.INVISIBLE);
        next_pickup.setVisibility(View.GONE);
        scooter.setVisibility(View.GONE);
        recycler_search_payload.setVisibility(View.GONE);
        go_back.setVisibility(View.GONE);
        dashboard_payloads.setVisibility(View.VISIBLE);
        fm = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        fm.getMapAsync(this);
        map_layout.setVisibility(View.GONE);
//        View customView = null;
//        NestedScrollView scrollViewParent = null;

//        scrollViewParent = (NestedScrollView) findViewById(R.id.scrollViewParent);
//        customView = (View) findViewById(R.id.customView);


        getSupportActionBar().setElevation(0);//remove actionbar shadow
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
//        try {
//            cover_url = "https://dheo-static-sg.s3-ap-southeast-1.amazonaws.com/img/dheo_rocket_packages_app.jpg";
//            Picasso.get().load(cover_url).into(cover);
//
//        } catch (NullPointerException e) {
//
//        }
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            phone = extras.getString("PHONE");
            photoUrl = extras.getString("PhotoURL");
            password = extras.getString("PASS");
            clientId = extras.getInt("CLIENTId");
            session = extras.getInt("SESSION");
//            name = extras.getString("name");
//            balance = extras.getInt("balance");
        }
//        client_name.setText(name);
//        total_balance.setText(balance + "TK");

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
        Call<ClientBasicInfo> call1 = RetrofitClient
                .getInstance()
                .getApi()
                .client_basic_info(clientId);
        call1.enqueue(new Callback<ClientBasicInfo>() {
            @Override
            public void onResponse(Call<ClientBasicInfo> call, Response<ClientBasicInfo> response) {
                try {
                    final ClientBasicInfo s = response.body();
                    if (s.getE() == 0) {
                        client_name.setText(s.getM().getName());
                        total_balance.setText(" " + s.getM().getBalance() + "TK");
                        name = s.getM().getName();
                        balance = s.getM().getBalance();
                        total_balance.setOnClickListener(new View.OnClickListener() {
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
                                intent.putExtra("name_c", name);
                                startActivity(intent);
                            }
                        });

                        request_pickup.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                try {
                                    if (s.getM().getOobUx()) {
                                        final AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(ClientDashboardActivity.this, R.style.AppTheme));
                                        builder.setCancelable(false);
                                        final View customLayout = getLayoutInflater().inflate(R.layout.client_agriment_layput, null);
                                        builder.setView(customLayout);
                                        Button i_accept = customLayout.findViewById(R.id.i_accept);
                                        Button cancel_ag = customLayout.findViewById(R.id.cancel_accept);
                                        final AlertDialog dialog = builder.create();
                                        dialog.show();
                                        cancel_ag.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                dialog.dismiss();
                                            }
                                        });
                                        i_accept.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Call<ResponseBody> agreement_call = RetrofitClient
                                                        .getInstance()
                                                        .getApi()
                                                        .user_agreement(clientId, "true");
                                                agreement_call.enqueue(new Callback<ResponseBody>() {
                                                    @Override
                                                    public void onResponse(Call<ResponseBody> a_call, Response<ResponseBody> a_response) {
                                                        String s = null;
                                                        try {
                                                            s = a_response.body().string();
                                                        } catch (IOException e) {
                                                            e.printStackTrace();
                                                        }
                                                        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                                                        if (s.equals("{\"e\":0}")) {
                                                            dialog.dismiss();
                                                            Toasty.error(getApplicationContext(), "Agreement accepted", Toast.LENGTH_LONG, true).show();
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
                                                                    Toasty.error(getApplicationContext(), "Try again!", Toast.LENGTH_LONG, true).show();
                                                                    Intent i = new Intent(getApplicationContext(), ClientDashboardActivity.class);
                                                                    startActivity(i);
                                                                }

                                                            });

                                                        } else {
                                                            Toasty.error(getApplicationContext(), "server failed to response", Toast.LENGTH_LONG, true).show();
                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(Call<ResponseBody> a_call, Throwable t) {
                                                        Toasty.error(getApplicationContext(), "Try again!", Toast.LENGTH_LONG, true).show();
                                                    }
                                                });
                                            }
                                        });
                                    }

                                } catch (NullPointerException e) {
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
                                            Toasty.error(getApplicationContext(), "Try again!", Toast.LENGTH_LONG, true).show();
                                            Intent i = new Intent(getApplicationContext(), ClientDashboardActivity.class);
                                            startActivity(i);
                                        }

                                    });
                                }
                            }
                        });

                    }
                } catch (NullPointerException e) {
                }
            }

            @Override
            public void onFailure(Call<ClientBasicInfo> call, Throwable t) {
                Toasty.error(getApplicationContext(), "Try Again!", Toast.LENGTH_LONG, true).show();
            }
        });
        Call<BlogUpdateTitle> call8 = RetrofitClient
                .getInstance()
                .getApi()
                .blog_update_title();
        call8.enqueue(new Callback<BlogUpdateTitle>() {
            @Override
            public void onResponse(Call<BlogUpdateTitle> call, Response<BlogUpdateTitle> response) {
                BlogUpdateTitle s = response.body();
                try {
                    if (s.getE() == 0) {
                        blog_url = "https://dheo-static-sg.s3.ap-southeast-1.amazonaws.com/img/community/team/" + s.getM().getPhoto();
                        Picasso.get().load(blog_url).into(blog_photo);
                        String text = s.getM().getTitle();
                        String array[] = text.split(" ");
                        blog_title.setText(array[0] + " " + array[1] + " " + array[2] + "...");
                        blog_see_more.setText("See >");
                        blog_see_more.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String url = "https://rocket.dheo.com/updates";
                                Intent i = new Intent(Intent.ACTION_VIEW);
                                i.setData(Uri.parse(url));
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i);
                            }
                        });
                    }
                } catch (NullPointerException e) {
                }
            }

            @Override
            public void onFailure(Call<BlogUpdateTitle> call, Throwable t) {
                Toasty.error(getApplicationContext(), "Try Again!", Toast.LENGTH_LONG, true).show();
            }
        });
        Call<PickupMapInfo> call6 = RetrofitClient
                .getInstance()
                .getApi()
                .client_pickup_map(clientId);
        call6.enqueue(new Callback<PickupMapInfo>() {
            @Override
            public void onResponse(Call<PickupMapInfo> call, Response<PickupMapInfo> response) {
                PickupMapInfo info = response.body();
                if (info.getE() == 0) {
                    try {
                        if (info.getM().getCourierPingMap().getAgents().size() > 0) {
                            map_layout.setVisibility(View.VISIBLE);
                            next_pickup.setVisibility(View.GONE);
                            scooter.setVisibility(View.GONE);
                        }
                    } catch (NullPointerException e) {
                    }
                }
            }

            @Override
            public void onFailure(Call<PickupMapInfo> call, Throwable t) {
                Toasty.error(getApplicationContext(), "Try Again!", Toast.LENGTH_LONG, true).show();
            }
        });

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
                    //Toasty.success(getApplicationContext(),pickup_info_dashboard.size()+"" , Toast.LENGTH_LONG, true).show();
                    if (pickup_info_dashboard.size() > 0) {
                        next_pickup.setVisibility(View.VISIBLE);
                        scooter.setVisibility(View.VISIBLE);
                        next_pickup.setVisibility(View.VISIBLE);
                        map_layout.setVisibility(View.GONE);
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
                    } else {
                        //Toasty.success(getApplicationContext(),"no data" , Toast.LENGTH_LONG, true).show();
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
                Toasty.error(getApplicationContext(), "Try Again!", Toast.LENGTH_LONG, true).show();
                Intent i = new Intent(getApplicationContext(), ClientDashboardActivity.class);
                startActivity(i);
            }

        });

        loadDashboardPayload();

        see_older.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((payload_remaining - (payload_remaining/(page_number*8))) > 0) {
                    page_number++;
                    loadDashboardPayload();
                }

            }
        });
        see_newer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(page_number>1){
                    page_number--;
                    loadDashboardPayload();
                }

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
                Toasty.error(getApplicationContext(), "You don't have any statement!", Toast.LENGTH_LONG, true).show();
            }
        });
        payload_search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if (payload_search_editText.getText().toString().length() != 0) {
                    see_newer.setVisibility(View.INVISIBLE);
                    see_older.setVisibility(View.INVISIBLE);
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
                            adapter_serch_payload = new AdapterSearchPayload(payload_search, getApplicationContext(), name);
                            recycler_search_payload.setAdapter(adapter_serch_payload);
                        }

                        @Override
                        public void onFailure(Call<ClientPayloadSearch> call, Throwable t) {
                            Toast.makeText(getApplicationContext(), "Try again1!", Toast.LENGTH_LONG).show();
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


        //Toasty.success(getApplicationContext(), name+"", Toast.LENGTH_LONG, true).show();

    }

    public void loadDashboardPayload() {
        Call<ClientDashboardPayloads> call2 = RetrofitClient
                .getInstance()
                .getApi()
                .client_dashboard_payloads(clientId, page_number);
        call2.enqueue(new Callback<ClientDashboardPayloads>() {
            @Override
            public void onResponse(Call<ClientDashboardPayloads> call, Response<ClientDashboardPayloads> response) {
                if (response.body() != null) {
                    ClientDashboardPayloads clientDashboardPayloads = response.body();
                    all_dashboard_payload = clientDashboardPayloads.getM();
                    try {
                        if (all_dashboard_payload.size()> 8){
                            if (all_dashboard_payload.get(8).getShowNext()){
                                see_older.setVisibility(View.VISIBLE);
                                see_older.setText("<Older (" + all_dashboard_payload.get(8).getRecordsRemaining() + ")");
                                payload_remaining = all_dashboard_payload.get(8).getRecordsRemaining();
//                            Toasty.error(getApplicationContext(), payload_remaining, Toast.LENGTH_LONG, true).show();
                            }
                            if (all_dashboard_payload.get(8).getShowPrev()){
                                see_newer.setVisibility(View.VISIBLE);
                                see_newer.setText("Newer ("+all_dashboard_payload.get(8).getOffset()+")>");
                                payload_remaining = all_dashboard_payload.get(8).getRecordsRemaining();
                            }
                        }
                        if(page_number == 1){
                            see_newer.setVisibility(View.INVISIBLE);
                        }
                        payload_progressbar.setVisibility(View.GONE);
                    } catch (NullPointerException | IndexOutOfBoundsException e) {
                        dashboard_payloads.setVisibility(View.GONE);
                        payload_progressbar.setVisibility(View.GONE);
                        e.printStackTrace();
                        //Toast.makeText(getApplicationContext(), "You don't have any payload", Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    Toasty.error(getApplicationContext(), "You have no payload!", Toast.LENGTH_LONG, true).show();
                    Intent i = new Intent(getApplicationContext(), ClientDashboardActivity.class);
                    startActivity(i);
                }
                adapter_payload = new AdapterDashboardPayloadsList(all_dashboard_payload, getApplicationContext(), name);
                dashboard_payloads.setAdapter(adapter_payload);
            }

            @Override
            public void onFailure(Call<ClientDashboardPayloads> call, Throwable t) {
                Toasty.error(getApplicationContext(), "Try Again!", Toast.LENGTH_LONG, true).show();
                Intent i = new Intent(getApplicationContext(), ClientDashboardActivity.class);
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
                try {
                    if (response.body() != null) {
                        final PickupMapInfo pickupMapInfo = response.body();
                        if (pickupMapInfo.getE() == 0) {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                            sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
                            for (int i = 0; i < pickupMapInfo.getM().getCourierPingMap().getAgents().size(); i++) {
                                try {
                                    long time = sdf.parse(pickupMapInfo.getM().getCourierPingMap().getAgents().get(i).getPing().getUpdatedAt()).getTime();
                                    long now = System.currentTimeMillis();
                                    CharSequence ago = DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS);
                                    latitude = Double.parseDouble(pickupMapInfo.getM().getCourierPingMap().getAgents().get(i).getPing().getCoordinates().getLat());
                                    longitude = Double.parseDouble(pickupMapInfo.getM().getCourierPingMap().getAgents().get(i).getPing().getCoordinates().getLong());
                                    latLng = new LatLng(latitude, longitude);
                                    MarkerOptions marker = new MarkerOptions().position(new LatLng(latitude, longitude)).title(pickupMapInfo.getM().getCourierPingMap().getAgents().get(i).getName() + "(" + pickupMapInfo.getM().getCourierPingMap().getAgents().get(i).getPhone() + ")")
                                            .snippet("last seen " + ago.toString());
                                    ;
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
                } catch (NullPointerException e) {
                }
            }


            @Override
            public void onFailure(Call<PickupMapInfo> call, Throwable t) {

            }
        });

    }
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            finishAffinity();
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please press BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }
}