package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.ModelClassClientPaymentPerfInfo.ClientPaymentPerfInfo;
import com.example.myapplication.ModelClassLatestAccountActivity.LatestAccountActivity;
import com.example.myapplication.ModelClassLatestAccountActivity.M;
import com.squareup.picasso.Picasso;

import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClientDashboardBillingActivity extends AppCompatActivity {
    private String name_client,photourl;
    private int balance_client, client_id;
    private TextView client_name_billing, total_balance_billing,payment_pref, valued_date;
    private ImageView profile_photo_billing;
    private RecyclerView latest_activity;
    private RecyclerView.LayoutManager layoutManager;
    private List<M> latest_payment_activity;
    private RecyclerView.Adapter adapter;
    Helper helper = new Helper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_dashboard_billing);
        total_balance_billing = (TextView) findViewById(R.id.balance_amount);
        client_name_billing = (TextView) findViewById(R.id.client_name);
        payment_pref = (TextView) findViewById(R.id.payment_pref);
        valued_date = (TextView) findViewById(R.id.valued_from);
        latest_activity = (RecyclerView) findViewById(R.id.recycler_account_activity);

        layoutManager = new LinearLayoutManager(this);
        latest_activity.setLayoutManager(layoutManager);

        try {
            if (helper.getPhoto_Url().equals("default.svg")) {
                profile_photo_billing = (ImageView) findViewById(R.id.client_dp);
            } else {
                profile_photo_billing = (ImageView) findViewById(R.id.client_dp);
                photourl = "https://dheo-static-sg.s3-ap-southeast-1.amazonaws.com/img/rocket/clients/" + helper.getPhoto_Url();
                Picasso.get().load(photourl).into(profile_photo_billing);
            }
        } catch (SQLException e) {
        }
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            name_client = extras.getString("name_c");
            balance_client = extras.getInt("balance_c");
        }
        client_name_billing.setText(name_client);
        total_balance_billing.setText(balance_client + "TK");
        getSupportActionBar().setElevation(0);//remove actionbar shadow
        setTitle("My Billing Dashboard");
        client_id = helper.getClientId();
        Call<ClientPaymentPerfInfo> call = RetrofitClient
                .getInstance()
                .getApi()
                .client_payment_perf_info(client_id);
        call.enqueue(new Callback<ClientPaymentPerfInfo>() {
            @Override
            public void onResponse(Call<ClientPaymentPerfInfo> call, Response<ClientPaymentPerfInfo> response) {
                try {
                    ClientPaymentPerfInfo s = response.body();
                    if (s.getE() == 0){
                        valued_date.setText("Valued client since " + s.getM().getStatDate());
                        try {
                            if(s.getM().getPositiveBalance()) {
                                try {
                                    if(s.getM().getTimeLeft() > 0){
                                        try {
                                            if(s.getM().getPrefersBank()){
                                                payment_pref.setText("Your money will be ready in the next " + s.getM().getTimeLeft()+" hours. Dheo will pay you through bank.");
                                            }
                                        }catch (NullPointerException e){}
                                        try {
                                            if(s.getM().getPrefersBkash()){
                                                payment_pref.setText("Your money will be ready in the next " + s.getM().getTimeLeft()+" hours. Dheo will pay you through bKash.");
                                            }
                                        }catch (NullPointerException e){}
                                        try {
                                            if(s.getM().getPrefersCash()){
                                                payment_pref.setText("Your money will be ready in the next " + s.getM().getTimeLeft()+" hours. Dheo will send your cash two times a week.");
                                            }
                                        }catch (NullPointerException e){}
                                        try {
                                            if(s.getM().getPrefersNagad()){
                                                payment_pref.setText("Your money will be ready in the next " + s.getM().getTimeLeft()+" hours. Dheo will pay you through nagad.");
                                            }
                                        }catch (NullPointerException e){}
                                    }
                                }catch (NullPointerException e){}

                                try {
                                    if(s.getM().getTimeLeft() <= 0){
                                        try {
                                            if(s.getM().getPrefersBank()){
                                                payment_pref.setText("Dheo will pay you through bank.");
                                            }
                                        }catch (NullPointerException e){}
                                        try {
                                            if(s.getM().getPrefersBkash()){
                                                payment_pref.setText("Dheo will pay you through bKash.");
                                            }
                                        }catch (NullPointerException e){}
                                        try {
                                            if(s.getM().getPrefersCash()){
                                                payment_pref.setText("Dheo will send your cash two times a week.");
                                            }
                                        }catch (NullPointerException e){}
                                        try {
                                            if(s.getM().getPrefersNagad()){
                                                payment_pref.setText("Dheo will pay you through nagad.");
                                            }
                                        }catch (NullPointerException e){}
                                    }
                                }catch (NullPointerException e){}
                            }
                        }catch (NullPointerException e){}
                        try {
                             if (s.getM().getNegativeBalance()){

                            }
                        }catch (NullPointerException e){}
                        try{
                            if(s.getM().getNegativeBalance()){
                                payment_pref.setText("Please pay your balance through bKash. Our bkash number is 01734440871 (merchant account). Please use the 'payment option' and put " +helper.getPhone_number()+ " as the reference number. ");
                            }
                        }catch (NullPointerException e){

                        }
                        try {
                            if(!s.getM().getNegativeBalance() && !s.getM().getPositiveBalance()){
                                payment_pref.setText("If some entries are not here, it means they are being processed by our finance team (usually within 1 working day).");
                            }
                        }catch (NullPointerException e){}
                    }
                }catch (NullPointerException e){

                }
            }

            @Override
            public void onFailure(Call<ClientPaymentPerfInfo> call, Throwable t) {

            }
        });


        Call<LatestAccountActivity> call_1 = RetrofitClient
                .getInstance()
                .getApi()
                .latest_account_activity(client_id);
        call_1.enqueue(new Callback<LatestAccountActivity>() {
            @Override
            public void onResponse(Call<LatestAccountActivity> call, Response<LatestAccountActivity> response) {
                try {
                    LatestAccountActivity latestAccountActivity = response.body();
                    latest_payment_activity = latestAccountActivity.getM();
                    Toasty.success(getApplicationContext(), latestAccountActivity.getM().size()+"", Toast.LENGTH_LONG, true).show();
                }catch (NullPointerException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "failed", Toast.LENGTH_LONG).show();
                }
                adapter = new AdapterClassLatestPaymentActivity(latest_payment_activity,getApplicationContext());
                latest_activity.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<LatestAccountActivity> call, Throwable t) {
                Toasty.error(getApplicationContext(), "স্লো ইন্টারনেটঃ আবার চেস্টা করুন!", Toast.LENGTH_LONG, true).show();
                Intent i = new Intent(getApplicationContext(), ClientDashboardActivity.class);
                startActivity(i);
            }
        });
    }
}