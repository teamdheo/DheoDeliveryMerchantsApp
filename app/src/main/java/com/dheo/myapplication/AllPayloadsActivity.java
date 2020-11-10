package com.dheo.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dheo.myapplication.ModelClassClientDashboardPayloads.ClientDashboardPayloads;
import com.dheo.myapplication.ModelClassClientDashboardPayloads.M;

import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllPayloadsActivity extends AppCompatActivity {
    private RecyclerView client_all_payloads;
    private NestedScrollView nestedScrollView;
    private ProgressBar progressBar;

    Helper helper = new Helper(this);
    private int client_id;
    private RecyclerView.LayoutManager layoutManager;
    List<M> all_payloads;
    private RecyclerView.Adapter adapter_payloads_all;
    int page = 1, limit = 10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_payloads);
        client_all_payloads = (RecyclerView) findViewById(R.id.all_payloads);
        nestedScrollView = findViewById(R.id.payload_scroll_view);
        progressBar = findViewById(R.id.payload_progressbar);
        layoutManager = new LinearLayoutManager(this);
        client_all_payloads.setLayoutManager(layoutManager);
        client_id = helper.getClientId();
        Call<ClientDashboardPayloads> call2 = RetrofitClient
                .getInstance()
                .getApi()
                .client_load_payload_page(client_id, page, limit);
        call2.enqueue(new Callback<ClientDashboardPayloads>() {
            @Override
            public void onResponse(Call<ClientDashboardPayloads> call, Response<ClientDashboardPayloads> response) {
                try {
                    if(response.isSuccessful() && response.body() != null){
                        progressBar.setVisibility(View.GONE);
                        ClientDashboardPayloads payloads = response.body();
                        all_payloads = payloads.getM();
                    }

                }catch (NullPointerException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "failed", Toast.LENGTH_LONG).show();
                }
                adapter_payloads_all = new AdapterAllPayloadList(all_payloads,getApplicationContext());
                client_all_payloads.setAdapter(adapter_payloads_all);

            }

            @Override
            public void onFailure(Call<ClientDashboardPayloads> call, Throwable t) {
                Toasty.error(getApplicationContext(), "স্লো ইন্টারনেটঃ আবার চেস্টা করুন!", Toast.LENGTH_LONG, true).show();
                Intent i = new Intent(getApplicationContext(), ClientDashboardActivity.class);
                startActivity(i);
            }
        });


    }

}