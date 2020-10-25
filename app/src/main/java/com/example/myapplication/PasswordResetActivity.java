package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.modelClassPassResetRequest.PassResetRequest;

import java.io.IOException;
import es.dmoral.toasty.Toasty;
import okhttp3.ResponseBody;
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
        getSupportActionBar().setElevation(0);//remove actionbar shadow
        setTitle("Reset Password");
        setContentView(R.layout.activity_password_reset);
        client_number = (EditText) findViewById(R.id.phone_req);
        button = (Button) findViewById(R.id.btnnext);
        phone_call = (TextView) findViewById(R.id.phone_call2);
        dheo_life = (TextView) findViewById(R.id.dheo_life2);
        progressDialog=new ProgressDialog(this);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setMessage("Wait a moment...");
                progressDialog.show();
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
                                    Toasty.success(getApplicationContext(), "Dheo will send you a sms", Toast.LENGTH_LONG, true).show();
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
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel: +8801301377181"));
                startActivity(intent);
            }
        });

        dheo_life.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://m.me/dheolife";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
    }
}