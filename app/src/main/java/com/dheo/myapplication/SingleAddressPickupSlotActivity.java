package com.dheo.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dheo.myapplication.modelClassAvaiablePickupSlot.AvailablePickupSlot;
import com.dheo.myapplication.modelClassAvaiablePickupSlot.M;


import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SingleAddressPickupSlotActivity extends AppCompatActivity {
    List<M> client_pickup_slots;
    private RecyclerView pickup_slot_view;
    private RecyclerView.Adapter adapter;
    private Button back_to_dashboard1;
    Helper helper = new Helper(this);
    private ProgressBar progressBar;
    private int clientId;
    private String address_id_get;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setElevation(0);//remove actionbar shadow
        setTitle("Book a pickup");
        setContentView(R.layout.activity_single_address_pickup_slot);
        pickup_slot_view =(RecyclerView) findViewById(R.id.recycler_pickup_slot);
        back_to_dashboard1 = (Button) findViewById(R.id.back_to_dashboard1);
        progressBar = findViewById(R.id.single_add_progress);
        pickup_slot_view.setHasFixedSize(true);
        pickup_slot_view.setLayoutManager(new LinearLayoutManager(this));
        back_to_dashboard1.setVisibility(View.INVISIBLE);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            address_id_get = extras.getString("address_id");

        }
        clientId = helper.getClientId();
        Call<AvailablePickupSlot> call = RetrofitClient
                .getInstance()
                .getApi()
                .get_avaiable_pickup_slot(clientId);
        call.enqueue(new Callback<AvailablePickupSlot>() {
            @Override
            public void onResponse(Call<AvailablePickupSlot> call, Response<AvailablePickupSlot> response) {
                if(response.body() != null){
                    try {
                        AvailablePickupSlot s = response.body();
                        client_pickup_slots = s.getM();
                        progressBar.setVisibility(View.GONE);
                        back_to_dashboard1.setVisibility(View.VISIBLE);

                    } catch (NullPointerException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "failed", Toast.LENGTH_LONG).show();
                    }
                }
                adapter = new AdapterSingleAddressSlotList(client_pickup_slots, getApplicationContext(),address_id_get);
                pickup_slot_view.setAdapter(adapter);

            }

            @Override
            public void onFailure(Call<AvailablePickupSlot> call, Throwable t) {
                Toasty.error(getApplicationContext(), "Try again !", Toast.LENGTH_LONG, true).show();
                Intent i = new Intent(getApplicationContext(), ClientDashboardActivity.class);
                startActivity(i);

            }
        });
        back_to_dashboard1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SingleAddressPickupSlotActivity.this,ClientDashboardActivity.class);
                startActivity(i);
            }
        });
    }
}