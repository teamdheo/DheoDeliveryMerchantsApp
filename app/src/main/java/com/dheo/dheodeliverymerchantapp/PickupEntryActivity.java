package com.dheo.dheodeliverymerchantapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.dheo.dheodeliverymerchantapp.ModelClassClientBasicInfo.ClientBasicInfo;
import com.dheo.dheodeliverymerchantapp.ModelClassPickupOrders.M;
import com.dheo.dheodeliverymerchantapp.ModelClassPickupOrders.PickupOrders;
import com.dheo.dheodeliverymerchantapp.ModelClassSearchPickupOrder.SearchPickupOrder;
import com.dheo.dheodeliverymerchantapp.modelClassPickupAddresses.PickupAddresses;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import es.dmoral.toasty.Toasty;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PickupEntryActivity extends AppCompatActivity {
    private Button order_create_btn,cancel_entry_btn,save_entry_btn,old_pickup,new_pickup,search_btn,back_btn;
    private DatePicker entry_datePicker,search_datePicker;
    private TextView recycle_hints;
    private EditText entry_customer_name,entry_customer_address,entry_customer_phone,entry_customer_cod;
    private String month,date,search_date,search_month;
    private int month_of_year, search_month_of_year, client_id, page_number =1, load_pickup_remaining;
    private RecyclerView load_pickups_recycle,search_recycle;
    private RecyclerView.Adapter load_pickup_adapter, search_adapter;
    private RecyclerView.LayoutManager load_pickup_layout_manager, search_layout_manager;
    private List<M> all_list_of_orders;
    private List<com.dheo.dheodeliverymerchantapp.modelClassPickupAddresses.M> pickup_entry_address_length;
    private List<com.dheo.dheodeliverymerchantapp.ModelClassSearchPickupOrder.M> search_orders;
    Calendar calendar;
    LinearLayout order_entry_layout,recycle_layout_view;
    Helper helper = new Helper(this);
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private Boolean obx;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pickup_entry);
        order_create_btn = findViewById(R.id.order_create_btn);
        save_entry_btn = findViewById(R.id.save_entry_btn);
        cancel_entry_btn = findViewById(R.id.cancel_entry_btn);
        order_entry_layout = findViewById(R.id.order_entry_layout);
        recycle_layout_view = findViewById(R.id.recycle_layout_view);
        entry_datePicker = findViewById(R.id.entry_datePicker);
        entry_customer_name = findViewById(R.id.entry_customer_name);
        entry_customer_address = findViewById(R.id.entry_customer_address);
        entry_customer_phone = findViewById(R.id.entry_customer_phone);
        entry_customer_cod = findViewById(R.id.entry_customer_cod);
        load_pickups_recycle = findViewById(R.id.load_pickups_recycle);
        recycle_hints = findViewById(R.id.recycle_hints);
        old_pickup = findViewById(R.id.old_pickup);
        new_pickup = findViewById(R.id.new_pickup);
        search_datePicker = findViewById(R.id.search_datePicker);
        search_btn = findViewById(R.id.search_btn);
        search_recycle = findViewById(R.id.search_recycle);
        back_btn = findViewById(R.id.back_btn);

        order_entry_layout.setVisibility(View.GONE);
        load_pickup_layout_manager = new LinearLayoutManager(this);
        load_pickups_recycle.setLayoutManager(load_pickup_layout_manager);
        search_layout_manager = new LinearLayoutManager(this);
        search_recycle.setLayoutManager(search_layout_manager);

        old_pickup.setVisibility(View.GONE);
        new_pickup.setVisibility(View.GONE);
        search_recycle.setVisibility(View.GONE);
        back_btn.setVisibility(View.GONE);


        //getSupportActionBar().setElevation(0);//remove actionbar shadow
        setTitle("My Pickup Orders");

        Toolbar toolbar = findViewById(R.id.color_toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        setSupportActionBar(toolbar);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toggle.syncState();

        client_id = helper.getClientId();

        Call<ClientBasicInfo> call1 = RetrofitClient
                .getInstance()
                .getApi()
                .client_basic_info(helper.getClientId());
        call1.enqueue(new Callback<ClientBasicInfo>() {
            @Override
            public void onResponse(Call<ClientBasicInfo> call, Response<ClientBasicInfo> response) {
                try {
                    ClientBasicInfo s = response.body();
                    if(s.getE() == 0){
                        View hView =  navigationView.getHeaderView(0);
                        TextView nav_name = hView.findViewById(R.id.nav_name);
                        ImageView nav_photo = hView.findViewById(R.id.nav_photo);
                        nav_name.setText(s.getM().getName());
                        obx = s.getM().getOobUx();
                        try {
                            if (s.getM().getProPic().equals("default.svg")) {

                            } else {

                                String photo_url = "https://dheo-static-sg.s3-ap-southeast-1.amazonaws.com/img/rocket/clients/" + s.getM().getProPic();
                                Picasso.get().load(photo_url).memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE).into(nav_photo);
                            }
                        } catch (NullPointerException e) {

                        }
                    }
                }catch (NullPointerException e){

                }
            }

            @Override
            public void onFailure(Call<ClientBasicInfo> call, Throwable t) {

            }
        });
        order_create_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                order_entry_layout.setVisibility(View.VISIBLE);
            }
        });

        loadPickupEntry();
        old_pickup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(load_pickup_remaining - (load_pickup_remaining/(page_number*8)) > 0){
                    page_number++;
                    loadPickupEntry();
                }
            }
        });

        new_pickup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(page_number > 0){
                    page_number--;
                    loadPickupEntry();
                }
            }
        });

        cancel_entry_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                order_entry_layout.setVisibility(View.GONE);
            }
        });
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PickupEntryActivity.class);
                startActivity(intent);
            }
        });
        save_entry_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //entry_datePicker.updateDate(calendar.YEAR, calendar.MONTH, calendar.DAY_OF_MONTH+1);
                month_of_year = entry_datePicker.getMonth();
                if(month_of_year == 0){
                    month = "January";
                }
                else if(month_of_year == 1){
                    month = "February";
                }
                else if(month_of_year == 2){
                    month = "March";
                }
                else if(month_of_year == 3){
                    month = "April";
                }
                else if(month_of_year == 4){
                    month = "May";
                }
                else if(month_of_year == 5){
                    month = "June";
                }
                else if(month_of_year == 6){
                    month = "July";
                }
                else if(month_of_year == 7){
                    month = "August";
                }
                else if(month_of_year == 8){
                    month = "September";
                }
                else if(month_of_year == 9){
                    month = "October";
                }
                else if(month_of_year == 10){
                    month = "November";
                }
                else if(month_of_year == 11){
                    month = "December";
                }
                date = String.valueOf(entry_datePicker.getDayOfMonth()) +" " + month + " "+ String.valueOf(entry_datePicker.getYear());
                if(entry_customer_name.getText().length() > 0 && entry_customer_address.getText().length() > 0 && entry_customer_phone.getText().length() == 11 && entry_customer_cod.getText().length() > 0){
                    //Toast.makeText(getApplicationContext(), obx.compareTo(true), Toast.LENGTH_LONG).show();
                    try{
                        if(obx){
                            final AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(PickupEntryActivity.this, R.style.AppTheme));
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
                                            .user_agreement(client_id, "true");
                                    agreement_call.enqueue(new Callback<ResponseBody>() {
                                        @Override
                                        public void onResponse(Call<ResponseBody> a_call, Response<ResponseBody> a_response) {
                                            String s = null;
                                            try {
                                                s = a_response.body().string();
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                            if (s.equals("{\"e\":0}")) {
                                                dialog.dismiss();
                                                Toasty.success(getApplicationContext(), "Agreement Accepted", Toast.LENGTH_LONG, true).show();
                                                Call<PickupAddresses> call = RetrofitClient
                                                        .getInstance()
                                                        .getApi()
                                                        .get_pickup_address(client_id);

                                                call.enqueue(new Callback<PickupAddresses>() {
                                                    @Override
                                                    public void onResponse(Call<PickupAddresses> call, Response<PickupAddresses> response) {
                                                        try {
                                                            PickupAddresses pickup = response.body();
                                                            //pickup_entry_address_length = pickup.getM();
                                                            if (pickup.getM().size() < 2) {
                                                                Call<ResponseBody> entryCall = RetrofitClient
                                                                        .getInstance()
                                                                        .getApi()
                                                                        .pickup_self_entry(client_id, entry_customer_name.getText().toString(), entry_customer_address.getText().toString(), entry_customer_phone.getText().toString(), entry_customer_cod.getText().toString(),date, pickup.getM().get(0).getAddress_id());
                                                                entryCall.enqueue(new Callback<ResponseBody>() {
                                                                    @Override
                                                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                                        String s = null;
                                                                        try {
                                                                            s = response.body().string();
                                                                        } catch (IOException e) {
                                                                            e.printStackTrace();
                                                                        }

                                                                        if (s.equals("{\"e\":0}")) {
                                                                            entry_customer_name.getText().clear();
                                                                            entry_customer_address.getText().clear();
                                                                            entry_customer_cod.getText().clear();
                                                                            entry_customer_phone.getText().clear();
                                                                            Intent intent = new Intent(getApplicationContext(), PickupEntryActivity.class);
                                                                            startActivity(intent);
                                                                        }
                                                                        else if (s.equals("{\"e\":2}")) {
                                                                            Toasty.info(getApplicationContext(), "Today's pickup time is over. please contact to our customer service for details.", Toast.LENGTH_LONG, true).show();
                                                                        }
                                                                        else{
                                                                            //Toast.makeText(getApplicationContext(),"no token"+ token , Toast.LENGTH_LONG).show();
                                                                        }
                                                                    }

                                                                    @Override
                                                                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                                                                    }
                                                                });


                                                            } else {
                                                                final AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(PickupEntryActivity.this, R.style.AppTheme));
                                                                builder.setCancelable(false);
                                                                final View customLayout = getLayoutInflater().inflate(R.layout.self_entry_multiple_address_dialog, null);
                                                                builder.setView(customLayout);
                                                                final RecyclerView recycler_pickup_entry_address = customLayout.findViewById(R.id.recycler_pickup_entry_address);
                                                                Button cancel_pickup_entry = customLayout.findViewById(R.id.cancel_pickup_entry);
                                                                final RecyclerView.Adapter[] adapter = new RecyclerView.Adapter[1];
                                                                recycler_pickup_entry_address.setHasFixedSize(true);
                                                                recycler_pickup_entry_address.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                                                final AlertDialog dialog = builder.create();
                                                                dialog.show();
                                                                cancel_pickup_entry.setOnClickListener(new View.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(View v) {
                                                                        dialog.dismiss();
                                                                    }
                                                                });

                                                                Call<PickupAddresses> call1 = RetrofitClient
                                                                        .getInstance()
                                                                        .getApi()
                                                                        .get_pickup_address(client_id);

                                                                call1.enqueue(new Callback<PickupAddresses>() {
                                                                    @Override
                                                                    public void onResponse(Call<PickupAddresses> call, Response<PickupAddresses> response) {
                                                                        if(response.body() != null){
                                                                            try {
                                                                                PickupAddresses pickup = response.body();
                                                                                pickup_entry_address_length = pickup.getM();
                                                                            } catch (NullPointerException e) {
                                                                                e.printStackTrace();
                                                                                Toast.makeText(getApplicationContext(), "failed", Toast.LENGTH_LONG).show();
                                                                            }
                                                                        }
                                                                        adapter[0] = new AdapterClassSelfEntryAddresses(pickup_entry_address_length, getApplicationContext(),client_id, entry_customer_name.getText().toString(), entry_customer_address.getText().toString(), entry_customer_phone.getText().toString(), entry_customer_cod.getText().toString(),date);
                                                                        recycler_pickup_entry_address.setAdapter(adapter[0]);
                                                                    }

                                                                    @Override
                                                                    public void onFailure(Call<PickupAddresses> call, Throwable t) {
                                                                        //Toasty.error(getApplicationContext(), "স্লো ইন্টারনেটঃ আবার চেস্টা করুন!", Toast.LENGTH_LONG, true).show();
                                                                    }

                                                                });
                                                            }
                                                        } catch (NullPointerException e) {
                                                            e.printStackTrace();
                                                            //Toast.makeText(getApplicationContext(), "The Server Failed To Response!", Toast.LENGTH_LONG).show();
                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(Call<PickupAddresses> call, Throwable t) {

                                                    }

                                                });



                                            } else {
                                                //Toasty.error(getApplicationContext(), "The Server Failed To Response", Toast.LENGTH_LONG, true).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<ResponseBody> a_call, Throwable t) {
                                            //Toasty.error(getApplicationContext(), "Try again!", Toast.LENGTH_LONG, true).show();
                                        }
                                    });
                                }
                            });
                        }

                    }catch (NullPointerException e){
                        Call<PickupAddresses> call = RetrofitClient
                                .getInstance()
                                .getApi()
                                .get_pickup_address(client_id);

                        call.enqueue(new Callback<PickupAddresses>() {
                            @Override
                            public void onResponse(Call<PickupAddresses> call, Response<PickupAddresses> response) {
                                try {
                                    PickupAddresses pickup = response.body();
                                    //pickup_entry_address_length = pickup.getM();
                                    if (pickup.getM().size() < 2) {
                                        Call<ResponseBody> entryCall = RetrofitClient
                                                .getInstance()
                                                .getApi()
                                                .pickup_self_entry(client_id, entry_customer_name.getText().toString(), entry_customer_address.getText().toString(), entry_customer_phone.getText().toString(), entry_customer_cod.getText().toString(),date, pickup.getM().get(0).getAddress_id());
                                        entryCall.enqueue(new Callback<ResponseBody>() {
                                            @Override
                                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                String s = null;
                                                try {
                                                    s = response.body().string();
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }

                                                if (s.equals("{\"e\":0}")) {
                                                    entry_customer_name.getText().clear();
                                                    entry_customer_address.getText().clear();
                                                    entry_customer_cod.getText().clear();
                                                    entry_customer_phone.getText().clear();
                                                    Intent intent = new Intent(getApplicationContext(), PickupEntryActivity.class);
                                                    startActivity(intent);
                                                }
                                                else if (s.equals("{\"e\":2}")) {
                                                    Toasty.error(getApplicationContext(), "Today's pickup time is over. please contact to our customer service for details.", Toast.LENGTH_LONG, true).show();
                                                }
                                                else{
                                                    //Toast.makeText(getApplicationContext(),"no token"+ token , Toast.LENGTH_LONG).show();
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<ResponseBody> call, Throwable t) {

                                            }
                                        });


                                    } else {
                                        final AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(PickupEntryActivity.this, R.style.AppTheme));
                                        builder.setCancelable(false);
                                        final View customLayout = getLayoutInflater().inflate(R.layout.self_entry_multiple_address_dialog, null);
                                        builder.setView(customLayout);
                                        final RecyclerView recycler_pickup_entry_address = customLayout.findViewById(R.id.recycler_pickup_entry_address);
                                        Button cancel_pickup_entry = customLayout.findViewById(R.id.cancel_pickup_entry);
                                        final RecyclerView.Adapter[] adapter = new RecyclerView.Adapter[1];
                                        recycler_pickup_entry_address.setHasFixedSize(true);
                                        recycler_pickup_entry_address.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                        final AlertDialog dialog = builder.create();
                                        dialog.show();
                                        cancel_pickup_entry.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                dialog.dismiss();
                                            }
                                        });

                                        Call<PickupAddresses> call1 = RetrofitClient
                                                .getInstance()
                                                .getApi()
                                                .get_pickup_address(client_id);

                                        call1.enqueue(new Callback<PickupAddresses>() {
                                            @Override
                                            public void onResponse(Call<PickupAddresses> call, Response<PickupAddresses> response) {
                                                if(response.body() != null){
                                                    try {
                                                        PickupAddresses pickup = response.body();
                                                        pickup_entry_address_length = pickup.getM();
                                                    } catch (NullPointerException e) {
                                                        e.printStackTrace();
                                                        Toast.makeText(getApplicationContext(), "failed", Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                                adapter[0] = new AdapterClassSelfEntryAddresses(pickup_entry_address_length, getApplicationContext(),client_id, entry_customer_name.getText().toString(), entry_customer_address.getText().toString(), entry_customer_phone.getText().toString(), entry_customer_cod.getText().toString(),date);
                                                recycler_pickup_entry_address.setAdapter(adapter[0]);
                                            }

                                            @Override
                                            public void onFailure(Call<PickupAddresses> call, Throwable t) {
                                                //Toasty.error(getApplicationContext(), "স্লো ইন্টারনেটঃ আবার চেস্টা করুন!", Toast.LENGTH_LONG, true).show();
                                            }

                                        });
                                    }
                                } catch (NullPointerException e) {
                                    e.printStackTrace();
                                    //Toast.makeText(getApplicationContext(), "The Server Failed To Response!", Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<PickupAddresses> call, Throwable t) {

                            }

                        });


                    }

                }
                else{
                    Toast.makeText(getApplicationContext(), "text missing", Toast.LENGTH_LONG).show();
                }

//                Intent intent = new Intent(getApplicationContext(), PickupEntryActivity.class);
//                startActivity(intent);
            }
        });

        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_month_of_year = search_datePicker.getMonth();
                if(search_month_of_year == 0){
                    search_month = "January";
                }
                else if(search_month_of_year == 1){
                    search_month = "February";
                }
                else if(search_month_of_year == 2){
                    search_month = "March";
                }
                else if(search_month_of_year == 3){
                    search_month = "April";
                }
                else if(search_month_of_year == 4){
                    search_month = "May";
                }
                else if(search_month_of_year == 5){
                    search_month = "June";
                }
                else if(search_month_of_year == 6){
                    search_month = "July";
                }
                else if(search_month_of_year == 7){
                    search_month = "August";
                }
                else if(search_month_of_year == 8){
                    search_month = "September";
                }
                else if(search_month_of_year == 9){
                    search_month = "October";
                }
                else if(search_month_of_year == 10){
                    search_month = "November";
                }
                else if(search_month_of_year == 11){
                    search_month = "December";
                }
                search_date = String.valueOf(search_datePicker.getDayOfMonth()) +" " + search_month + " "+ String.valueOf(search_datePicker.getYear());
                //Toast.makeText(getApplicationContext(), search_month_of_year +" "+search_month, Toast.LENGTH_LONG).show();
                Call<SearchPickupOrder> search_call = RetrofitClient
                        .getInstance()
                        .getApi()
                        .search_pickup_orders(client_id,search_date);
                search_call.enqueue(new Callback<SearchPickupOrder>() {
                    @Override
                    public void onResponse(Call<SearchPickupOrder> call, Response<SearchPickupOrder> response) {
                        if(response.body() != null){
                            SearchPickupOrder searchPickupOrder = response.body();
                            search_orders = searchPickupOrder.getM();
                            //Toast.makeText(getApplicationContext(), search_orders.get(0).getCodAmount(), Toast.LENGTH_LONG).show();
                            if(response.body().getM().size()< 1){
                                Toast.makeText(getApplicationContext(), "You don't have any pickup order on "+ search_date, Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getApplicationContext(), PickupEntryActivity.class);
                                startActivity(intent);
                            }
                            else {
                                try{
                                    search_recycle.setVisibility(View.VISIBLE);
                                    back_btn.setVisibility(View.VISIBLE);
                                    load_pickups_recycle.setVisibility(View.GONE);
                                    old_pickup.setVisibility(View.GONE);
                                    new_pickup.setVisibility(View.GONE);
                                    recycle_hints.setVisibility(View.GONE);

                                }catch (NullPointerException e){

                                }
                            }
                            search_adapter = new AdapterClassSearchPickupOrder(search_orders, getApplicationContext());
                            search_recycle.setAdapter(search_adapter);
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "You don't have any pickup order on "+ search_date, Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplicationContext(), PickupEntryActivity.class);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onFailure(Call<SearchPickupOrder> call, Throwable t) {

                    }
                });
            }
        });

        Call<PickupOrders> pickupOrdersCall = RetrofitClient
                .getInstance()
                .getApi()
                .load_pickup_orders(client_id, page_number);
        pickupOrdersCall.enqueue(new Callback<PickupOrders>() {
            @Override
            public void onResponse(Call<PickupOrders> call, Response<PickupOrders> response) {
                if(response.body() != null){
                    if(response.body().getM().size() < 2){
                        recycle_layout_view.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<PickupOrders> call, Throwable t) {

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
                String url = "https://rocket.dheo.com/user-manual";
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

    public void loadPickupEntry(){
        Call<PickupOrders> ordersCall = RetrofitClient
                .getInstance()
                .getApi()
                .load_pickup_orders(client_id,page_number);
        ordersCall.enqueue(new Callback<PickupOrders>() {
            @Override
            public void onResponse(Call<PickupOrders> call, Response<PickupOrders> response) {
                if(response.body() != null){
                    try {
                        PickupOrders pickupOrders = response.body();
                        all_list_of_orders = pickupOrders.getM();
                        if(all_list_of_orders.get(all_list_of_orders.size()-1).getShowNext()){
                            old_pickup.setVisibility(View.VISIBLE);
                            old_pickup.setText("<Older ("+all_list_of_orders.get(all_list_of_orders.size()-1).getRecordsRemaining()+")");
                            load_pickup_remaining = all_list_of_orders.get(all_list_of_orders.size()-1).getRecordsRemaining();
                        }

                        if(all_list_of_orders.get(all_list_of_orders.size()-1).getRecordsRemaining() == 0){
                            old_pickup.setVisibility(View.INVISIBLE);
                        }
                        if(all_list_of_orders.get(all_list_of_orders.size()-1).getShowPrev()){
                            new_pickup.setVisibility(View.VISIBLE);
                            new_pickup.setText("Newer ("+all_list_of_orders.get(all_list_of_orders.size()-1).getOffset()+")");
                        };
                        if(page_number == 1){
                            new_pickup.setVisibility(View.INVISIBLE);
                        };
                    }catch (NullPointerException | IndexOutOfBoundsException e) {

                        e.printStackTrace();
                        //Toast.makeText(getApplicationContext(), "failed", Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_LONG).show();
                }
                load_pickup_adapter = new AdapterClassLoadPickups(all_list_of_orders, getApplicationContext());
                Log.d("Data", "onResponse: "+all_list_of_orders.get(0).getCustomerAddress());
                load_pickups_recycle.setAdapter(load_pickup_adapter);
            }

            @Override
            public void onFailure(Call<PickupOrders> call, Throwable t) {

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
    //
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