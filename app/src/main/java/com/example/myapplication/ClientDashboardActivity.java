package com.example.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.modelClassPickupAddresses.M;
import com.example.myapplication.modelClassPickupAddresses.PickupAddresses;
import com.squareup.picasso.Picasso;

import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ClientDashboardActivity extends AppCompatActivity {
    SQLiteDatabase sqLiteDatabase;
    private int session = 0;
    private String phone;
    private String photoUrl;
    private int clientId;
    private String password;
    private ImageView profile_photo;
    private TextView client_name, total_balance;
    private String photo_url;
    private String name;
    private int balance;
    private Button request_pickup;
    private List<M> pickup_address_length;
    Helper helper = new Helper(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        helper.checkInternetConnection();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_dashboard);
        client_name = (TextView) findViewById(R.id.name);
        total_balance = (TextView) findViewById(R.id.amount) ;
        request_pickup =(Button) findViewById(R.id.pickup_request);
        //check = (TextView) findViewById(R.id.check);

        try{
            if(helper.getPhoto_Url() != "default.svg"){
                profile_photo =(ImageView) findViewById(R.id.profile_photo);
                photo_url = "https://dheo-static-sg.s3-ap-southeast-1.amazonaws.com/img/rocket/clients/" + helper.getPhoto_Url() ;
                Picasso.get().load(photo_url).into(profile_photo);
            }
            else {
                profile_photo =(ImageView) findViewById(R.id.profile_photo);
            }
        }catch (SQLiteException e){

        }

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            phone = extras.getString("PHONE");
            photoUrl = extras.getString("PhotoURL");
            password = extras.getString("PASS");
            clientId = extras.getInt("CLIENTId");
            session = extras.getInt("SESSION");
            name = extras.getString("name");
            balance = extras.getInt("balance");
        }
        client_name.setText(name);
        total_balance.setText(balance + "TK");

        sqLiteDatabase = getBaseContext().openOrCreateDatabase("SQLite", MODE_PRIVATE, null);
        String sql = "CREATE TABLE IF NOT EXISTS ClientProfileInfo (_id Integer Primary Key,phone TEXT,image TEXT,clientId Integer,password TEXT);";
        sqLiteDatabase.execSQL(sql);
        if (session == 1) {
            sql = "INSERT or REPLACE INTO ClientProfileInfo VALUES ( 1,'" + phone + "','" + photoUrl + "','" + clientId + "','" + password + "');";
            sqLiteDatabase.execSQL(sql);
        }
//
        Cursor query = sqLiteDatabase.rawQuery("SELECT * FROM ClientProfileInfo", null);
        if (query.moveToFirst()) {
            try {
                phone = query.getString(1);
                photoUrl = query.getString(2);
                clientId = query.getInt(3);
                password = query.getString(4);

            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        } else {
            Intent i = new Intent(getApplicationContext(), LogInActivity.class);
            startActivity(i);
        }
        query.close();
        request_pickup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<PickupAddresses> call = RetrofitClient
                        .getInstance()
                        .getApi()
                        .get_pickup_address(clientId);

                call.enqueue(new Callback<PickupAddresses>() {
                    @Override
                    public void onResponse(Call<PickupAddresses> call, Response<PickupAddresses> response) {
                        try {
                            PickupAddresses pickup = response.body();
                            pickup_address_length = pickup.getM();
                            if(pickup_address_length.size() < 2){
                                Intent i = new Intent(ClientDashboardActivity.this,SingleAddressPickupSlotActivity.class);
                                i.putExtra("address_id", pickup.getM().get(0).getAddress_id());
                                startActivity(i);
                            }
                            else{
                                Intent i = new Intent(ClientDashboardActivity.this,ListActivityPickupAddress.class);
                                startActivity(i);
                            }
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "failed", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<PickupAddresses> call, Throwable t) {
                        Toasty.error(getApplicationContext(), "স্লো ইন্টারনেটঃ আবার চেস্টা করুন!", Toast.LENGTH_LONG, true).show();
                        Intent i = new Intent(getApplicationContext(), ClientDashboardActivity.class);
                        startActivity(i);
                    }

                });


//                Intent i = new Intent(ClientDashboardActivity.this,ListActivityPickupAddress.class);
//                startActivity(i);
            }
        });
    }
}