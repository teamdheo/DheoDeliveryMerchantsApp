package com.dheo.dheodeliverymerchantapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PickupEntryActivity extends AppCompatActivity {
    private Button order_create_btn,save_entry_btn;
    private DatePicker entry_datePicker;
    private EditText entry_customer_name,entry_customer_address,entry_customer_phone,entry_customer_cod;
    private String month,date;
    private int month_of_year, client_id;
    LinearLayout order_entry_layout;
    Helper helper = new Helper(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pickup_entry);
        order_create_btn = findViewById(R.id.order_create_btn);
        save_entry_btn = findViewById(R.id.save_entry_btn);
        order_entry_layout = findViewById(R.id.order_entry_layout);
        entry_datePicker = findViewById(R.id.entry_datePicker);
        entry_customer_name = findViewById(R.id.entry_customer_name);
        entry_customer_address = findViewById(R.id.entry_customer_address);
        entry_customer_phone = findViewById(R.id.entry_customer_phone);
        entry_customer_cod = findViewById(R.id.entry_customer_cod);
        order_entry_layout.setVisibility(View.GONE);

        getSupportActionBar().setElevation(0);//remove actionbar shadow
        setTitle("My Pickup Orders");
        client_id = helper.getClientId();
        order_create_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                order_entry_layout.setVisibility(View.VISIBLE);
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
                                order_entry_layout.setVisibility(View.GONE);
                                entry_customer_name.getText().clear();
                                entry_customer_address.getText().clear();
                                entry_customer_cod.getText().clear();
                                entry_customer_phone.getText().clear();
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
}