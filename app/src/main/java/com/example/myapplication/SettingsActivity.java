package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.database.SQLException;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class SettingsActivity extends AppCompatActivity {
    TextView setting_name,go_back;
    ImageView setting_dp;
    private String photo_url;
    Helper helper = new Helper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setElevation(0);//remove actionbar shadow
        setTitle("Account Settings");
        setContentView(R.layout.activity_settings);
        setting_name = findViewById(R.id.setting_name);
        go_back = findViewById(R.id.go_back);
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
    }
}