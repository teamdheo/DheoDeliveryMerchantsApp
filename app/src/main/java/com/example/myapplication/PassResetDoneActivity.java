package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.modelClassResetDoneClientInfo.ResetDoneClientInfo;

import java.io.IOException;

import es.dmoral.toasty.Toasty;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PassResetDoneActivity extends AppCompatActivity {
    private EditText newpass, token;
    private ProgressDialog progressDialog;
    private TextView phone_call, dheo_life;
    private ImageView button;
    private String pass_token;
    private Integer session = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().setElevation(0);//remove actionbar shadow
        setTitle("Reset Done");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_reset_done);
        newpass = (EditText) findViewById(R.id.newpass);
        token = (EditText) findViewById(R.id.token);
        button =  findViewById(R.id.btnsubmit);
//        phone_call = findViewById(R.id.phonecall4);
//        dheo_life = findViewById(R.id.dheolife4);
        progressDialog=new ProgressDialog(this);
        Intent intent = getIntent();
        String action = intent.getAction();
       try{
           if(action.equals(Intent.ACTION_VIEW)){
               Uri data = intent.getData();
               if (data.getQueryParameter("token") != null ) {

                   String param = data.getQueryParameter("token");
                   token.setText(param);
               }
               else{
                   Toast.makeText(getApplicationContext(), "nothing here", Toast.LENGTH_LONG).show();
               }
           }
       }catch (NullPointerException e){}
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            pass_token = extras.getString("token");
            //token.setText(pass_token);
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setMessage("Authenticating...");
                progressDialog.show();
                    Call<ResetDoneClientInfo> call = RetrofitClient
                            .getInstance()
                            .getApi()
                            .reset_password(newpass.getText().toString(), token.getText().toString());

                    call.enqueue(new Callback<ResetDoneClientInfo>() {
                        @Override
                        public void onResponse(Call<ResetDoneClientInfo> call, Response<ResetDoneClientInfo> response) {
                            try {
                                ResetDoneClientInfo s = response.body();
                                //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();

                                if (s.getE() == 0) {
                                    Toasty.success(getApplicationContext(), "Welcome to Dheo!", Toast.LENGTH_LONG, true).show();
                                    Intent intent = new Intent(getApplicationContext(), ClientDashboardActivity.class);
                                    intent.putExtra("PHONE", s.getM().getPhone_number());
                                    intent.putExtra("PASS", newpass.getText().toString());
                                    intent.putExtra("CLIENTId", s.getM().getId());
                                    intent.putExtra("PhotoURL", s.getM().getPhotoUrl());
                                    intent.putExtra("name", s.getM().getName());
                                    intent.putExtra("SESSION", session);
                                    intent.putExtra("balance", s.getM().getBalance());
                                    startActivity(intent);
                                    progressDialog.dismiss();
                                }else{
                                    Toasty.error(getApplicationContext(), "Try again!", Toast.LENGTH_LONG, true).show();
                                    Intent intent = new Intent(getApplicationContext(), PassResetDoneActivity.class);
                                    startActivity(intent);
                                    progressDialog.dismiss();
                                }

                            } catch (NullPointerException e) {
                                e.printStackTrace();
                                progressDialog.dismiss();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResetDoneClientInfo> call, Throwable t) {
                            Toasty.error(getApplicationContext(), "Try again !", Toast.LENGTH_LONG, true).show();
                            Intent i = new Intent(getApplicationContext(), PassResetDoneActivity.class);
                            startActivity(i);
                            progressDialog.dismiss();
                        }
                    });
            }
        });

//        phone_call.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View arg0) {
//                Intent intent = new Intent(Intent.ACTION_DIAL);
//                intent.setData(Uri.parse("tel: +8801301377181"));
//                startActivity(intent);
//            }
//        });
//        dheo_life.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String url = "https://m.me/dheolife";
//                Intent i = new Intent(Intent.ACTION_VIEW);
//                i.setData(Uri.parse(url));
//                startActivity(i);
//            }
//        });
    }
}