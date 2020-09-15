package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.modelClassPickupAddresses.M;
import com.example.myapplication.modelClassPickupAddresses.PickupAddresses;

import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListActivityPickupAddress extends AppCompatActivity {
    List<M> client_pickup_addresses;
    private RecyclerView pickup_list;
    private RecyclerView.Adapter adapter;
    Helper helper = new Helper(this);
    private int clientId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setElevation(0);//remove actionbar shadow
        setTitle("Select an address");
        setContentView(R.layout.activity_list_pickup_address);
        pickup_list = (RecyclerView) findViewById(R.id.recycler_pickup);
        pickup_list.setHasFixedSize(true);
        pickup_list.setLayoutManager(new LinearLayoutManager(this));
        clientId = helper.getClientId();
        Call<PickupAddresses> call = RetrofitClient
                .getInstance()
                .getApi()
                .get_pickup_address(clientId);

        call.enqueue(new Callback<PickupAddresses>() {
            @Override
            public void onResponse(Call<PickupAddresses> call, Response<PickupAddresses> response) {
                try {
                    PickupAddresses pickup = response.body();
                     client_pickup_addresses = pickup.getM();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "failed", Toast.LENGTH_LONG).show();
                }
                adapter = new AdapterPickupAddressList(client_pickup_addresses, getApplicationContext());
                pickup_list.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<PickupAddresses> call, Throwable t) {
                Toasty.error(getApplicationContext(), "স্লো ইন্টারনেটঃ আবার চেস্টা করুন!", Toast.LENGTH_LONG, true).show();
                Intent i = new Intent(getApplicationContext(), ClientDashboardActivity.class);
                startActivity(i);
            }

        });

    }
}