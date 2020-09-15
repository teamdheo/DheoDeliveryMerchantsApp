package com.example.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.modelClassPassResetRequest.PassResetRequest;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PasswordResetActivity extends AppCompatActivity {
    private EditText client_number;
    private  TextView phone_call, dheo_life;
    private ProgressDialog progressDialog;
    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);
        client_number = (EditText) findViewById(R.id.phone_req);
        button = (Button) findViewById(R.id.btnnext);
        phone_call = (TextView) findViewById(R.id.phone_call2);
        dheo_life = (TextView) findViewById(R.id.dheo_life2);
        progressDialog=new ProgressDialog(this);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(client_number.getText().toString().length() == 11) {
                    Call<PassResetRequest> call = RetrofitClient
                            .getInstance()
                            .getApi()
                            .reset_request(client_number.getText().toString());

                    call.enqueue(new Callback<PassResetRequest>() {
                        @Override
                        public void onResponse(Call<PassResetRequest> call, Response<PassResetRequest> response) {
                            try {
                                PassResetRequest s = response.body();
                                //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();

                                if (s.getE() == 0) {
                                    Toasty.success(getApplicationContext(), "create a new password", Toast.LENGTH_LONG, true).show();
                                    Intent intent = new Intent(getApplicationContext(), PassResetDoneActivity.class);
                                    intent.putExtra("token", s.getM().getSms_token());
                                    startActivity(intent);
                                    progressDialog.dismiss();
                                }else{
                                    Toasty.error(getApplicationContext(), "Try again!", Toast.LENGTH_LONG, true).show();
                                    Intent intent = new Intent(getApplicationContext(), PasswordResetActivity.class);
                                    startActivity(intent);
                                    progressDialog.dismiss();
                                }

                            } catch (NullPointerException e) {
                                e.printStackTrace();
                                progressDialog.dismiss();
                            }
                        }

                        @Override
                        public void onFailure(Call<PassResetRequest> call, Throwable t) {
                            Toasty.error(getApplicationContext(), "Try again !", Toast.LENGTH_LONG, true).show();
                            Intent i = new Intent(getApplicationContext(), PasswordResetActivity.class);
                            startActivity(i);
                            progressDialog.dismiss();
                        }
                    });
                }
            }
        });
        phone_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PasswordResetActivity.this,PassResetDoneActivity.class);
                startActivity(i);
            }
        });

    }
}