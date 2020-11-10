package com.dheo.dheodeliverymerchantapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dheo.dheodeliverymerchantapp.modelClassAvaiablePickupSlot.AvailablePickupSlot;
import com.dheo.dheodeliverymerchantapp.modelClassAvaiablePickupSlot.M;

import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListActivityMultiplePickupAddressSlots extends AppCompatActivity {
    List<M> client_pickup_slots;
    private String address_id_get;
    private RecyclerView pickup_slot_view;
    private RecyclerView.Adapter adapter;
    private Button back_to_dashboard;
    private ProgressBar progressBar;
    Helper helper = new Helper(this);
    private int client_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setElevation(0);//remove actionbar shadow
        setTitle("Book a pickup");
        setContentView(R.layout.activity_list_multiple_pickup_address_slots);
        pickup_slot_view = (RecyclerView) findViewById(R.id.multiple_address_slot) ;
        back_to_dashboard = (Button) findViewById(R.id.back_to_dashboard);
        progressBar = findViewById(R.id.multiple_address_slot_progress);
        pickup_slot_view.setHasFixedSize(true);
        pickup_slot_view.setLayoutManager(new LinearLayoutManager(this));
        back_to_dashboard.setVisibility(View.INVISIBLE);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            address_id_get = extras.getString("address_id");

        }
        client_id =helper.getClientId();


        Call<AvailablePickupSlot> call = RetrofitClient
                .getInstance()
                .getApi()
                .get_multiple_address_slot(client_id,address_id_get);
        call.enqueue(new Callback<AvailablePickupSlot>() {
            @Override
            public void onResponse(Call<AvailablePickupSlot> call, Response<AvailablePickupSlot> response) {
                if(response.body() != null){
                    try {
                        AvailablePickupSlot s = response.body();
                        client_pickup_slots = s.getM();
                        back_to_dashboard.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);

                    } catch (NullPointerException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "failed", Toast.LENGTH_LONG).show();
                    }
                }
                adapter = new AdapterSingleAddressSlotList(client_pickup_slots,getApplicationContext(),address_id_get);

                pickup_slot_view.setAdapter(adapter);

            }

            @Override
            public void onFailure(Call<AvailablePickupSlot> call, Throwable t) {
                Toasty.error(getApplicationContext(), "Try again !", Toast.LENGTH_LONG, true).show();
                Intent i = new Intent(getApplicationContext(), ClientDashboardActivity.class);
                startActivity(i);

            }
        });

        back_to_dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ListActivityMultiplePickupAddressSlots.this,ClientDashboardActivity.class);
                startActivity(i);
            }
        });
    }

}