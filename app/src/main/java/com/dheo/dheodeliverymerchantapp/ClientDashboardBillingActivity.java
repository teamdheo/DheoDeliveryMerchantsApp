package com.dheo.dheodeliverymerchantapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dheo.dheodeliverymerchantapp.ModelClassClientBasicInfo.ClientBasicInfo;
import com.dheo.dheodeliverymerchantapp.ModelClassClientMonthlyStatementDate.ClientMonthlyStatementDate;
import com.dheo.dheodeliverymerchantapp.ModelClassClientPaymentPerfInfo.ClientPaymentPerfInfo;
import com.dheo.dheodeliverymerchantapp.ModelClassClientPaymentReceiptPDF.ClientPaymentReceiptPDF;
import com.dheo.dheodeliverymerchantapp.ModelClassLatestAccountActivity.LatestAccountActivity;
import com.dheo.dheodeliverymerchantapp.ModelClassLatestAccountActivity.M;
import com.google.android.material.navigation.NavigationView;
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
    private TextView client_name_billing, total_balance_billing,see_free_delivery,no_monthly_payment_recept,no_latest_activity,no_daily_payment_recept,payment_pref, valued_date;
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
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;

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

        Toolbar toolbar = findViewById(R.id.color_toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        setSupportActionBar(toolbar);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toggle.syncState();
        // getSupportActionBar().setElevation(0);//remove actionbar shadow
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
                    View hView =  navigationView.getHeaderView(0);
                    TextView nav_name = hView.findViewById(R.id.nav_name);
                    ImageView nav_photo = hView.findViewById(R.id.nav_photo);
                    client_name_billing.setText(s.getM().getName());
                    total_balance_billing.setText(s.getM().getBalance() + "TK");
                    balance_client = s.getM().getBalance();
                    nav_name.setText(s.getM().getName());
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
                            Picasso.get().load(photourl).memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE).into(nav_photo);
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
                            if(monthly_billing_pdf.size()<1){
                                no_monthly_payment_recept.setVisibility(View.VISIBLE);
                            }
                        }catch (NullPointerException e) {
                            e.printStackTrace();
                            no_monthly_payment_recept.setVisibility(View.VISIBLE);

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
        Call<ClientPaymentReceiptPDF> call_null_check = RetrofitClient
                .getInstance()
                .getApi()
                .client_payment_receipt_pdf(client_id, page_number);
        call_null_check.enqueue(new Callback<ClientPaymentReceiptPDF>() {
            @Override
            public void onResponse(Call<ClientPaymentReceiptPDF> call, Response<ClientPaymentReceiptPDF> response) {
                if(response.body()!=null){
                    try {
                        if(response.body().getM().size()<2){
                            no_daily_payment_recept.setVisibility(View.VISIBLE);
                        }
                    }catch (NullPointerException | IndexOutOfBoundsException e){
                        no_daily_payment_recept.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<ClientPaymentReceiptPDF> call, Throwable t) {

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
                String url = "http://rocket.dheo.com/user-manual";
//                Intent i = new Intent(Intent.ACTION_VIEW);
//                i.setData(Uri.parse(url));
//                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(i);
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
//                Intent i = new Intent(Intent.ACTION_VIEW);
//                i.setData(Uri.parse(url));
//                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(i);
//                return true;
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
                            if(pdf_receipt.get(pdf_receipt.size() - 1).getShowNext()){
                                see_older.setVisibility(View.VISIBLE);
                                see_older.setText("< Older (" + pdf_receipt.get(pdf_receipt.size() - 1).getRecordsRemaining() +")");
                                remaining_receipt = pdf_receipt.get(pdf_receipt.size() - 1).getRecordsRemaining();
                            }
                            if (pdf_receipt.get(pdf_receipt.size() - 1).getShowPrev()){
                                see_newer.setVisibility(View.VISIBLE);
                                see_newer.setText("Newer ("+pdf_receipt.get(pdf_receipt.size() - 1).getOffset()+")>");
                            }
                            if(page_number == 1){
                                see_newer.setVisibility(View.INVISIBLE);
                            }
                            if(pdf_receipt.get(pdf_receipt.size() - 1).getRecordsRemaining() == 0){
                                see_older.setVisibility(View.INVISIBLE);
                            }
                            if(pdf_receipt.get(pdf_receipt.size() - 1).getCount() == 0){
                                no_daily_payment_recept.setVisibility(View.VISIBLE);
                            }

                            //Toast.makeText(getApplicationContext(), remaining_receipt+"", Toast.LENGTH_LONG).show();
                        }catch (IndexOutOfBoundsException e){
                            //Toast.makeText(getApplicationContext(), "you have no information", Toast.LENGTH_LONG).show();
                            see_older.setVisibility(View.GONE);
                        }
                    }catch (NullPointerException | IndexOutOfBoundsException e) {
                        e.printStackTrace();
                        //Toast.makeText(getApplicationContext(), "failed", Toast.LENGTH_LONG).show();
                    }
                }
                else{
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