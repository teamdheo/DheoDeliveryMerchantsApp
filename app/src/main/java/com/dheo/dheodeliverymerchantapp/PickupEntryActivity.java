package com.dheo.dheodeliverymerchantapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dheo.dheodeliverymerchantapp.ModelClassPickupHistory.PickupHistory;
import com.dheo.dheodeliverymerchantapp.ModelClassPickupOrders.M;
import com.dheo.dheodeliverymerchantapp.ModelClassPickupOrders.PickupOrders;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PickupEntryActivity extends AppCompatActivity {
    private Button order_create_btn,cancel_entry_btn,save_entry_btn,old_pickup,new_pickup;
    private DatePicker entry_datePicker;
    private EditText entry_customer_name,entry_customer_address,entry_customer_phone,entry_customer_cod;
    private String month,date;
    private int month_of_year, client_id, page_number =1, load_pickup_remaining;
    private RecyclerView load_pickups_recycle;
    private RecyclerView.Adapter load_pickup_adapter;
    private RecyclerView.LayoutManager load_pickup_layout_manager;
    private List<M> all_list_of_orders;
    LinearLayout order_entry_layout;
    Helper helper = new Helper(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pickup_entry);
        order_create_btn = findViewById(R.id.order_create_btn);
        save_entry_btn = findViewById(R.id.save_entry_btn);
        cancel_entry_btn = findViewById(R.id.cancel_entry_btn);
        order_entry_layout = findViewById(R.id.order_entry_layout);
        entry_datePicker = findViewById(R.id.entry_datePicker);
        entry_customer_name = findViewById(R.id.entry_customer_name);
        entry_customer_address = findViewById(R.id.entry_customer_address);
        entry_customer_phone = findViewById(R.id.entry_customer_phone);
        entry_customer_cod = findViewById(R.id.entry_customer_cod);
        load_pickups_recycle = findViewById(R.id.load_pickups_recycle);
        old_pickup = findViewById(R.id.old_pickup);
        new_pickup = findViewById(R.id.new_pickup);
        order_entry_layout.setVisibility(View.GONE);
        load_pickup_layout_manager = new LinearLayoutManager(this);
        load_pickups_recycle.setLayoutManager(load_pickup_layout_manager);

        getSupportActionBar().setElevation(0);//remove actionbar shadow
        setTitle("My Pickup Orders");
        client_id = helper.getClientId();
        order_create_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                order_entry_layout.setVisibility(View.VISIBLE);
            }
        });

        loadPickupEntry();
        cancel_entry_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                order_entry_layout.setVisibility(View.GONE);
            }
        });
        save_entry_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                    Toast.makeText(getApplicationContext(), date, Toast.LENGTH_LONG).show();
                    Call<ResponseBody> entryCall = RetrofitClient
                            .getInstance()
                            .getApi()
                            .pickup_self_entry(client_id, entry_customer_name.getText().toString(), entry_customer_address.getText().toString(), entry_customer_phone.getText().toString(), entry_customer_cod.getText().toString(),date);
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
                                loadPickupEntry();
                            }
                            else{
                                //Toast.makeText(getApplicationContext(),"no token"+ token , Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {

                        }
                    });

                }
                else{
                    Toast.makeText(getApplicationContext(), "text missing", Toast.LENGTH_LONG).show();
                }

//                Intent intent = new Intent(getApplicationContext(), PickupEntryActivity.class);
//                startActivity(intent);
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
                load_pickups_recycle.setAdapter(load_pickup_adapter);
            }

            @Override
            public void onFailure(Call<PickupOrders> call, Throwable t) {

            }
        });
    }
}