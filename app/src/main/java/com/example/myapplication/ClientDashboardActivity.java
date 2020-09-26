package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.ModelClassAssingedCourierInfoDashboard.AssingedCourierInfoDashboard;
import com.example.myapplication.ModelClassClientDashboardPayloads.ClientDashboardPayloads;
import com.example.myapplication.ModelClassClientMonthlyStatementDate.ClientMonthlyStatementDate;
import com.example.myapplication.modelClassPickupAddresses.M;
import com.example.myapplication.modelClassPickupAddresses.PickupAddresses;
import com.squareup.picasso.Picasso;

import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ClientDashboardActivity extends AppCompatActivity {
    SQLiteDatabase sqLiteDatabase;
    private int session = 0;
    private String phone;
    private String photoUrl, scooter_url;
    private int clientId;
    private String password;
    private ImageView profile_photo, scooter, octopus_red_body;
    private TextView client_name, total_balance;
    private String photo_url;
    private String name;
    private int balance;
    private Button request_pickup, next_pickup, see_more;
    private List<M> pickup_address_length;
    private List<com.example.myapplication.ModelClassClientDashboardPayloads.M> all_dashboard_payload;
    private List<com.example.myapplication.ModelClassAssingedCourierInfoDashboard.M> pickup_info_dashboard;
    private List<com.example.myapplication.ModelClassClientMonthlyStatementDate.M> monthly_payload_all_records;
    private RecyclerView pickup_list, dashboard_payloads, all_record_payload;
    private RecyclerView.Adapter adapter, adapter_payload, adapter_record_payload;
    private RecyclerView.LayoutManager layoutManager, layoutManager1;
    Helper helper = new Helper(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        helper.checkInternetConnection();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_dashboard);
        client_name = (TextView) findViewById(R.id.name);
        total_balance = (TextView) findViewById(R.id.amount) ;
        request_pickup =(Button) findViewById(R.id.pickup_request);
        next_pickup =(Button) findViewById(R.id.Next_pickups);
        scooter = (ImageView) findViewById(R.id.scooter);
        see_more = (Button) findViewById(R.id.see_more);
        pickup_list = (RecyclerView) findViewById(R.id.recycler_pickup_agent);
        dashboard_payloads = (RecyclerView) findViewById(R.id.recycler_dashboard_payloads);
        all_record_payload =(RecyclerView) findViewById(R.id.recycler_monthly_payload_records);
        octopus_red_body = (ImageView) findViewById(R.id.octopus_red_body);
        pickup_list.setHasFixedSize(true);
        pickup_list.setLayoutManager(new LinearLayoutManager(this));
        layoutManager = new LinearLayoutManager(this);
        dashboard_payloads.setLayoutManager(layoutManager);
        layoutManager1 = new LinearLayoutManager(this);
        all_record_payload.setLayoutManager(layoutManager1);
        next_pickup.setVisibility(View.GONE);
        scooter.setVisibility(View.GONE);
        getSupportActionBar().setElevation(0);//remove actionbar shadow
        setTitle("My Dashboard");
        try{
            if(helper.getPhoto_Url().equals("default.svg")){
                profile_photo =(ImageView) findViewById(R.id.profile_photo);
            }
            else {
                profile_photo =(ImageView) findViewById(R.id.profile_photo);
                photo_url = "https://dheo-static-sg.s3-ap-southeast-1.amazonaws.com/img/rocket/clients/" + helper.getPhoto_Url() ;
                Picasso.get().load(photo_url).into(profile_photo);
            }
        }catch (SQLException e){

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
                    if(pickup_info_dashboard.size() > 0){

                        next_pickup.setVisibility(View.VISIBLE);
                        scooter.setVisibility(View.VISIBLE);
                        scooter_url= "https://dheo-static-sg.s3-ap-southeast-1.amazonaws.com/img/scooter.png";
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
                            if(pickup_address_length.size() < 2){
                                Intent i = new Intent(ClientDashboardActivity.this,SingleAddressPickupSlotActivity.class);
                                i.putExtra("address_id", pickup.getM().get(0).getAddress_id());
                                startActivity(i);
                            }
                            else{
                                Intent i = new Intent(ClientDashboardActivity.this,ListActivityPickupAddress.class);
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
                try {
                    ClientDashboardPayloads clientDashboardPayloads = response.body();
                    all_dashboard_payload = clientDashboardPayloads.getM();
                }catch (NullPointerException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "failed", Toast.LENGTH_LONG).show();
                }
                adapter_payload = new AdapterDashboardPayloadsList(all_dashboard_payload,getApplicationContext());
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
                try{
                    ClientMonthlyStatementDate clientMonthlyStatementDate1 = response.body();
                    monthly_payload_all_records  = clientMonthlyStatementDate1.getM();
                }catch (NullPointerException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "failed", Toast.LENGTH_LONG).show();
                }
                adapter_record_payload = new AdapterClassMonthlyAllRecordPayload(monthly_payload_all_records, getApplicationContext(), clientId);
                all_record_payload.setAdapter(adapter_record_payload);
            }

            @Override
            public void onFailure(Call<ClientMonthlyStatementDate> call, Throwable t) {

            }
        });

    }
}