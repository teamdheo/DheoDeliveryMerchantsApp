package com.example.myapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.database.SQLException;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.myapplication.ModelClassBanksAndBranches.BanksAndBranches;
import com.example.myapplication.ModelClassBanksAndBranches.M;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsActivity extends AppCompatActivity {
    ArrayList<String> bank_name;
    ArrayList<String> branches_name;
    ArrayAdapter<String> arrayAdapter;
    TextView setting_name,go_back;
    ImageView setting_dp;
    private Spinner bank_name_show;
    private String photo_url;
    private Button bank, other_option, cash, bkash, nagad;
    LinearLayout linearLayout;
    Helper helper = new Helper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setElevation(0);//remove actionbar shadow
        setTitle("Account Settings");
        setContentView(R.layout.activity_settings);
        setting_name = findViewById(R.id.setting_name);
        go_back = findViewById(R.id.go_back);
        bank = findViewById(R.id.bank);
        other_option = findViewById(R.id.other_option);
        bank_name_show = findViewById(R.id.bank_name);
//        cash = findViewById(R.id.cash);
//        bkash = findViewById(R.id.bkash);
//        nagad = findViewById(R.id.nagad);
        //linearLayout = findViewById(R.id.linear_address);
        setting_name.setText(helper.getName());
        try{
            if(helper.getPhoto_Url().equals("default.svg")){
                setting_dp = findViewById(R.id.setting_profile_photo);
            }
            else {
                setting_dp = findViewById(R.id.setting_profile_photo);
                photo_url = "https://dheo-static-sg.s3-ap-southeast-1.amazonaws.com/img/rocket/clients/" + helper.getPhoto_Url() ;
                Picasso.get().load(photo_url).into(setting_dp);
            }
        }catch (SQLException e){

        }
        go_back.setText("< Go Back");
        bank_name = new ArrayList<>();

        //linearLayout.setBackgroundColor(Color.argb(0,0,0, (float) 0.2));
        Call<BanksAndBranches> call = RetrofitClient
                .getInstance()
                .getApi()
                .bank_and_branches();
        call.enqueue(new Callback<BanksAndBranches>() {
            @Override
            public void onResponse(Call<BanksAndBranches> call, Response<BanksAndBranches> response) {
                if(response.body() != null){
                    List<M> s = response.body().getM();
                    for(int i = 0; i<s.size(); i++){
                        bank_name.add(s.get(i).getBankName());


                    }

                }
            }


            @Override
            public void onFailure(Call<BanksAndBranches> call, Throwable t) {

            }
        });

    }
}