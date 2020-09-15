package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.modelClassAvaiablePickupSlot.AvailablePickupSlot;
import com.example.myapplication.modelClassAvaiablePickupSlot.M;

import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SingleAddressPickupSlotActivity extends AppCompatActivity {
    List<M> client_pickup_slots;
    private RecyclerView pickup_slot_view;
    private RecyclerView.Adapter adapter;
    private TextView list;
    Helper helper = new Helper(this);
    private int clientId;
    private String address_id_get;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_address_pickup_slot);
        pickup_slot_view =(RecyclerView) findViewById(R.id.recycler_pickup_slot);
        pickup_slot_view.setHasFixedSize(true);
        pickup_slot_view.setLayoutManager(new LinearLayoutManager(this));
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
                try {
                    AvailablePickupSlot s = response.body();
                    client_pickup_slots = s.getM();
                    //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();

                } catch (NullPointerException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "failed", Toast.LENGTH_LONG).show();
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
    }
}