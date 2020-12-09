package com.dheo.dheodeliverymerchantapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dheo.dheodeliverymerchantapp.ModelClassClientBasicInfo.ClientBasicInfo;
import com.dheo.dheodeliverymerchantapp.ModelClassClientMonthlyStatementDate.ClientMonthlyStatementDate;
import com.dheo.dheodeliverymerchantapp.ModelClassClientPaymentPerfInfo.ClientPaymentPerfInfo;
import com.dheo.dheodeliverymerchantapp.ModelClassClientPaymentReceiptPDF.ClientPaymentReceiptPDF;
import com.dheo.dheodeliverymerchantapp.ModelClassLatestAccountActivity.LatestAccountActivity;
import com.dheo.dheodeliverymerchantapp.ModelClassLatestAccountActivity.M;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

import es.dmoral.toasty.Toasty;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClientDashboardBillingActivity extends AppCompatActivity {
    private String name_client,photourl, pro_pic_url;
    private int balance_client, client_id, page_number = 1, remaining_receipt;
    private TextView client_name_billing, total_balance_billing,see_free_delivery,no_monthly_payment_recept,no_latest_activity,no_daily_payment_recept,payment_pref, valued_date,  phone_call, facebook, my_delivery,dashboard_billing,settings, user_manual,log_out, dhep_delivery, the_user_manual, meet_the_team, privacy_policy;
    private ImageView profile_photo_billing;
    private RecyclerView.LayoutManager layoutManager, layoutManager1, layoutManager2;
    private RecyclerView latest_activity, payment_receipt_pdf, monthly_statement_pdf;
    private List<M> latest_payment_activity;
    private List<com.dheo.dheodeliverymerchantapp.ModelClassClientPaymentReceiptPDF.M> pdf_receipt;
    private List<com.dheo.dheodeliverymerchantapp.ModelClassClientMonthlyStatementDate.M> monthly_billing_pdf;
    private RecyclerView.Adapter adapter, monthly_billing_adapter;
    private ProgressBar activity_progressbar, daily_receipt_progressbar, monthly_received_progressbar,name_billing_progress;
    private Button see_older, see_newer,save_client_payment;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    Helper helper = new Helper(this);
    LinearLayout payment_by_client;
    private EditText payable_amount,transaction_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_dashboard_billing);
        total_balance_billing = (TextView) findViewById(R.id.balance_amount);
        client_name_billing = (TextView) findViewById(R.id.client_name);
        payment_pref = (TextView) findViewById(R.id.payment_pref);
        valued_date = (TextView) findViewById(R.id.valued_from);
        latest_activity = (RecyclerView) findViewById(R.id.recycler_account_activity);
        payment_receipt_pdf = (RecyclerView) findViewById(R.id.recycler_payment_receipt);
        see_older =(Button) findViewById(R.id.show_older);
        see_newer=findViewById(R.id.show_newer);
        monthly_statement_pdf = (RecyclerView) findViewById(R.id.recycler_monthly_payment_receipt);
        activity_progressbar = findViewById(R.id.amount_activity_progressbar);
        daily_receipt_progressbar = findViewById(R.id.payment_receipt_progressbar);
        //monthly_received_progressbar  = findViewById(R.id.monthly_receipt_progressbar);
        phone_call = findViewById(R.id.billing_phone);
        facebook = findViewById(R.id.billing_fb);
        my_delivery = findViewById(R.id.billing_my_delivery);
        dashboard_billing = findViewById(R.id.billing_Billing);
        settings = findViewById(R.id.billing_settings);
        user_manual = findViewById(R.id.billing_user_manual);
        log_out =findViewById(R.id.billing_logout);
        dhep_delivery = findViewById(R.id.billing_dheo_delivery);
        the_user_manual = findViewById(R.id.billing_The_manual);
        meet_the_team = findViewById(R.id.billing_meet_team);
        privacy_policy = findViewById(R.id.billing_policy);
        no_latest_activity = findViewById(R.id.no_latest_activity);
        no_daily_payment_recept = findViewById(R.id.no_daily_payment_recept);
        no_monthly_payment_recept = findViewById(R.id.no_monthly_payment_recept);
        see_free_delivery = findViewById(R.id.see_free_delivery);
        payment_by_client = findViewById(R.id.payment_by_client);
        payable_amount = findViewById(R.id.payable_amount);
        transaction_id = findViewById(R.id.transaction_id);
        save_client_payment = findViewById(R.id.save_client_payment);
        name_billing_progress = findViewById(R.id.name_billing_progress);

        layoutManager = new LinearLayoutManager(this);
        latest_activity.setLayoutManager(layoutManager);
        layoutManager1 = new LinearLayoutManager(this);
        payment_receipt_pdf.setLayoutManager(layoutManager1);
        layoutManager2 = new LinearLayoutManager(this);
        monthly_statement_pdf.setLayoutManager(layoutManager2);
        see_older.setVisibility(View.INVISIBLE);
        see_newer.setVisibility(View.INVISIBLE);
        no_latest_activity.setVisibility(View.GONE);
        no_daily_payment_recept.setVisibility(View.GONE);
        no_monthly_payment_recept.setVisibility(View.GONE);
        payment_by_client.setVisibility(View.GONE);

//        Bundle extras = getIntent().getExtras();
//        if (extras != null) {
//            name_client = extras.getString("name_c");
//            balance_client = extras.getInt("balance_c");
//            pro_pic_url = extras.getString("url");
//        }


        getSupportActionBar().setElevation(0);//remove actionbar shadow
        setTitle("My Billing Dashboard");
        client_id = helper.getClientId();

        Call<ClientBasicInfo> call1 = RetrofitClient
                .getInstance()
                .getApi()
                .client_basic_info(client_id);
        call1.enqueue(new Callback<ClientBasicInfo>() {
            @Override
            public void onResponse(Call<ClientBasicInfo> call, Response<ClientBasicInfo> response) {
                final ClientBasicInfo s = response.body();
                if (s.getE() == 0) {
                    client_name_billing.setText(s.getM().getName());
                    total_balance_billing.setText(s.getM().getBalance() + "TK");
                    balance_client = s.getM().getBalance();
                    pro_pic_url = s.getM().getProPic();
                    if(balance_client < 0){
                        int b = balance_client * -1;
                        String a = String.valueOf(b);
                        payable_amount.setText(a);
                    }

                    try {
                        if(balance_client == 0){
                            payment_pref.setText("If some entries are not here, it means they are being processed by our finance team (usually within 1 working day).");
                        }
                    }catch (NullPointerException e){}
                    try {
                        if (s.getM().getProPic().equals("default.svg")) {
                            profile_photo_billing = (ImageView) findViewById(R.id.client_dp);
                            name_billing_progress.setVisibility(View.GONE);
                        } else {
                            profile_photo_billing = (ImageView) findViewById(R.id.client_dp);
                            photourl = "https://dheo-static-sg.s3-ap-southeast-1.amazonaws.com/img/rocket/clients/" + s.getM().getProPic();
                            Picasso.get().load(photourl).memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE).into(profile_photo_billing);
                            name_billing_progress.setVisibility(View.GONE);
                        }
                    } catch (NullPointerException e) {
                    }
                }
            }

            @Override
            public void onFailure(Call<ClientBasicInfo> call, Throwable t) {

            }
        });

        loadPaymentReceipt();

        see_older.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((remaining_receipt - (remaining_receipt/(page_number*8))) > 0){
                    page_number++;
                    loadPaymentReceipt();
                }
            }
        });

        see_newer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(page_number>1){
                    page_number--;
                    loadPaymentReceipt();
                }
            }
        });

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
                                //Toasty.success(getApplicationContext(), s.getM().getTimeLeft()+"", Toast.LENGTH_LONG, true).show();
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
                        try{
                            if(s.getM().getNegativeBalance()){
                                //payment_pref.setText("Please pay your balance through bKash. Our bkash number is 01734440871 (merchant account). Please use the 'payment option' and put " +helper.getPhone_number()+ " as the reference number. ");
                                payment_by_client.setVisibility(View.VISIBLE);
                            }
                        }catch (NullPointerException e){

                        }
                    }
                }catch (NullPointerException e){

                }
            }

            @Override
            public void onFailure(Call<ClientPaymentPerfInfo> call, Throwable t) {
                Toasty.error(getApplicationContext(), "Try Again", Toast.LENGTH_LONG, true).show();
                Intent i = new Intent(getApplicationContext(), ClientDashboardActivity.class);
                startActivity(i);
            }
        });


        Call<LatestAccountActivity> call_1 = RetrofitClient
                .getInstance()
                .getApi()
                .latest_account_activity(client_id);
        call_1.enqueue(new Callback<LatestAccountActivity>() {
            @Override
            public void onResponse(Call<LatestAccountActivity> call, Response<LatestAccountActivity> response) {
                if(response.body() != null){
                    try {
                        LatestAccountActivity latestAccountActivity = response.body();
                        latest_payment_activity = latestAccountActivity.getM();
                        activity_progressbar.setVisibility(View.GONE);
                    }catch (NullPointerException e) {
                        e.printStackTrace();
                        activity_progressbar.setVisibility(View.GONE);
                        no_latest_activity.setVisibility(View.VISIBLE);
                        //Toast.makeText(getApplicationContext(), "failed", Toast.LENGTH_LONG).show();
                    }
                }
                adapter = new AdapterClassLatestPaymentActivity(latest_payment_activity,getApplicationContext(), name_client,pro_pic_url);
                latest_activity.setAdapter(adapter);
                activity_progressbar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<LatestAccountActivity> call, Throwable t) {
                no_latest_activity.setVisibility(View.VISIBLE);
//
            }
        });

        Call<ClientMonthlyStatementDate> call3 = RetrofitClient
                .getInstance()
                .getApi()
                .client_payment_statement_date(client_id);
        call3.enqueue(new Callback<ClientMonthlyStatementDate>() {
            @Override
            public void onResponse(Call<ClientMonthlyStatementDate> call, Response<ClientMonthlyStatementDate> response) {
                try {
                    if(response.body() != null){
                        try {
                            ClientMonthlyStatementDate clientMonthlyStatementDate = response.body();
                            monthly_billing_pdf = clientMonthlyStatementDate.getM();
                        }catch (NullPointerException e) {
                            e.printStackTrace();
                            no_monthly_payment_recept.setVisibility(View.VISIBLE);
                            //Toast.makeText(getApplicationContext(), "failed", Toast.LENGTH_LONG).show();
                        }
                    }
                }catch (NullPointerException | IndexOutOfBoundsException e){
                    no_monthly_payment_recept.setVisibility(View.VISIBLE);
                }

                monthly_billing_adapter = new AdapterClassMonthlyBillingPDF(monthly_billing_pdf, getApplicationContext(), client_id);
                monthly_statement_pdf.setAdapter(monthly_billing_adapter);
            }
            @Override
            public void onFailure(Call<ClientMonthlyStatementDate> call, Throwable t) {
                no_monthly_payment_recept.setVisibility(View.VISIBLE);
                //Toasty.error(getApplicationContext(), "wrong here!", Toast.LENGTH_LONG, true).show();
//                Intent i = new Intent(getApplicationContext(), ClientDashboardBillingActivity.class);
//                startActivity(i);
            }
        });

        save_client_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<ResponseBody> payment_call = RetrofitClient
                        .getInstance()
                        .getApi()
                        .client_bill_payment(client_id,payable_amount.getText().toString(),transaction_id.getText().toString());
                payment_call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        String s = null;
                        try {
                            s = response.body().string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (s.equals("{\"e\":0}")){
                            Toasty.success(getApplicationContext(), "Thank You", Toast.LENGTH_LONG, true).show();
                            Intent i = new Intent(getApplicationContext(), ClientDashboardBillingActivity.class);
                            startActivity(i);

                        }
                        else{
                            Toasty.error(getApplicationContext(), "The Server Failed To Response", Toast.LENGTH_LONG, true).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
            }
        });
        phone_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel: +8801301377181"));
                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://m.me/dheolife";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });

        my_delivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ClientDashboardActivity.class);
                startActivity(intent);
            }
        });
        dashboard_billing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ClientDashboardBillingActivity.class);
                startActivity(intent);
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                intent.putExtra("name_c", name_client);
                startActivity(intent);
            }
        });
        user_manual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://rocket.dheo.com/user-manual";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });

        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPreferences=getSharedPreferences("LoginPrefs", MODE_PRIVATE);
                editor=sharedPreferences.edit();
                editor.putBoolean("saveLogin", false);
                editor.commit();
                Intent intent = new Intent(getApplicationContext(), LogInActivity.class);
                startActivity(intent);
            }
        });
        dhep_delivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ClientDashboardActivity.class);
                startActivity(intent);
            }
        });
        the_user_manual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://rocket.dheo.com/user-manual";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });
        meet_the_team.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://team.dheo.com";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });
        privacy_policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://dheo.com/privacy";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });
        see_free_delivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://rocket.dheo.com/notice";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });
    }

    public void loadPaymentReceipt(){
        Call<ClientPaymentReceiptPDF> call2 = RetrofitClient
                .getInstance()
                .getApi()
                .client_payment_receipt_pdf(client_id, page_number);
        call2.enqueue(new Callback<ClientPaymentReceiptPDF>() {
            @Override
            public void onResponse(Call<ClientPaymentReceiptPDF> call, Response<ClientPaymentReceiptPDF> response) {
                if (response.body() != null){
                    try{
                        ClientPaymentReceiptPDF clientPaymentReceiptPDF = response.body();
                        pdf_receipt = clientPaymentReceiptPDF.getM();
                        daily_receipt_progressbar.setVisibility(View.GONE);
                        try {
                            if(pdf_receipt.get(8).getShowNext()){
                                see_older.setVisibility(View.VISIBLE);
                                see_older.setText("< Older (" + pdf_receipt.get(8).getRecordsRemaining() +")");
                                remaining_receipt = pdf_receipt.get(8).getRecordsRemaining();
                            }
                            if (pdf_receipt.get(8).getShowPrev()){
                                see_newer.setVisibility(View.VISIBLE);
                                see_newer.setText("Newer ("+pdf_receipt.get(8).getOffset()+")>");
                            }
                            if(page_number == 1){
                                see_newer.setVisibility(View.INVISIBLE);
                            }
                            if(pdf_receipt.get(8).getRecordsRemaining() == 0){
                                see_older.setVisibility(View.INVISIBLE);
                            }
                            if(pdf_receipt.get(8).getCount() == 0){
                                no_daily_payment_recept.setVisibility(View.VISIBLE);
                            }

                            //Toast.makeText(getApplicationContext(), remaining_receipt+"", Toast.LENGTH_LONG).show();
                        }catch (IndexOutOfBoundsException e){
                            //Toast.makeText(getApplicationContext(), "you have no information", Toast.LENGTH_LONG).show();
                            see_older.setVisibility(View.GONE);
                            //no_daily_payment_recept.setVisibility(View.VISIBLE);
                        }
                    }catch (NullPointerException | IndexOutOfBoundsException e) {
                        e.printStackTrace();
                        //Toast.makeText(getApplicationContext(), "failed", Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    //Toast.makeText(getApplicationContext(), "no information", Toast.LENGTH_LONG).show();
                    see_older.setVisibility(View.GONE);
                }
                adapter = new AdapterClassPaymentReceiptPdf(pdf_receipt,getApplicationContext(), client_id);
                payment_receipt_pdf.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<ClientPaymentReceiptPDF> call, Throwable t) {
                //no_daily_payment_recept.setVisibility(View.VISIBLE);
            }
        });
    }
}