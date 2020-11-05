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

import com.example.myapplication.modelClassSignupClientInfo.SignupClientInfo;

import java.io.IOException;

import es.dmoral.toasty.Toasty;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {
    private TextView go_to_logIn;
    private EditText businessName, address, phoneNo,pass;
    private ImageView signUp,phone_call, dheo_life,instra_link;
    private ProgressDialog progressDialog;
    private Integer session = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setElevation(0);//remove actionbar shadow
        setTitle("Sign up");
        setContentView(R.layout.activity_sign_up);
        businessName =(EditText) findViewById(R.id.businessname);
        address =(EditText) findViewById(R.id.address);
        phoneNo =(EditText) findViewById(R.id.phNumber);
        pass = (EditText) findViewById(R.id.signpass);
        signUp = findViewById(R.id.btnSignUp);
        phone_call =  findViewById(R.id.phone_call1);
        dheo_life =  findViewById(R.id.dheo_life);
        go_to_logIn = findViewById(R.id.click2);
        instra_link = findViewById(R.id.instra_link);

        progressDialog=new ProgressDialog(this);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setMessage("Loading...");
                progressDialog.show();
                if(businessName.getText().toString().length() >0 && address.getText().toString().length() > 0
                        && phoneNo.getText().toString().length() == 11 && pass.getText().toString().length() > 0) {
                    Call<SignupClientInfo> call = RetrofitClient
                            .getInstance()
                            .getApi()
                            .sign_up(businessName.getText().toString(),address.getText().toString(), phoneNo.getText().toString(),pass.getText().toString());

                    call.enqueue(new Callback<SignupClientInfo>() {
                        @Override
                        public void onResponse(Call<SignupClientInfo> call, Response<SignupClientInfo> response) {
                            try {
                                SignupClientInfo s = response.body();
                                //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();

                                if (s.getE() == 0) {
                                    Toasty.success(getApplicationContext(), s.getM().getId()+"", Toast.LENGTH_LONG, true).show();
                                    Intent intent = new Intent(getApplicationContext(), ClientDashboardActivity.class);
                                    intent.putExtra("PHONE", phoneNo.getText().toString());
                                    intent.putExtra("PASS", pass.getText().toString());
                                    intent.putExtra("CLIENTId", s.getM().getId());
                                    intent.putExtra("PhotoURL", s.getM().getPhotoUrl());
                                    intent.putExtra("name", s.getM().getName());
                                    intent.putExtra("SESSION", session);
                                    intent.putExtra("balance", s.getM().getBalance());
                                    startActivity(intent);
                                    progressDialog.dismiss();
                                }else{
                                    Toasty.error(getApplicationContext(), "fail to get the response!", Toast.LENGTH_LONG, true).show();
                                    Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                                    startActivity(intent);
                                    progressDialog.dismiss();
                                }

                            } catch (NullPointerException e) {
                                e.printStackTrace();
                                progressDialog.dismiss();
                            }
                        }

                        @Override
                        public void onFailure(Call<SignupClientInfo> call, Throwable t) {
                            Toasty.error(getApplicationContext(), "Try again !", Toast.LENGTH_LONG, true).show();
                            Intent i = new Intent(getApplicationContext(), SignUpActivity.class);
                            startActivity(i);
                            progressDialog.dismiss();
                        }
                    });
                }
            }
        });
        phone_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                String url = "https://m.me/dheolife";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
        dheo_life.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://www.facebook.com/groups/bd.ecommerce.network";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
        instra_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://rocket.dheo.com/user-manual";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        go_to_logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SignUpActivity.this, LogInActivity.class);
                startActivity(i);
            }
        });
    }
}