package com.dheo.dheodeliverymerchantapp;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.dheo.dheodeliverymerchantapp.modelClassClientInfo.ClientInfo;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class LogInActivity extends AppCompatActivity {
    private TextView forgetPass,signUp1;
    private EditText number, password;
    private ProgressDialog progressDialog;
    private CheckBox checkBox;
    private ImageView button,phone_call,dheo_life, instagram_link;
    private Integer session = 1;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Boolean saveLogin;
    private String name;
    private  int balance;
    SQLiteDatabase sqLiteDatabase;
    Helper helper = new Helper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().setElevation(0);//remove actionbar shadow
        setTitle("Log In");
        helper.checkInternetConnection();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        signUp1 =  findViewById(R.id.click);
        number = (EditText) findViewById(R.id.phone);
        password = (EditText) findViewById(R.id.txtPwd);
        checkBox = (CheckBox) findViewById(R.id.rememberme);
        forgetPass =(TextView) findViewById(R.id.forgrtpass);
        button =  findViewById(R.id.btnLogin) ;
        phone_call =  findViewById(R.id.phone_call_link);
        dheo_life =  findViewById(R.id.facebook_link);
        instagram_link = findViewById(R.id.instra_link);
        progressDialog=new ProgressDialog(this);

        sharedPreferences=getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        editor=sharedPreferences.edit();
        String phone_number=sharedPreferences.getString("number","");
        String passwords=sharedPreferences.getString("password","");
        saveLogin = sharedPreferences.getBoolean("saveLogin", false);
        checkBox.setChecked(true);
        if (saveLogin == true) {
            checkBox.setChecked(true);
            Intent intent = new Intent(getApplicationContext(), ClientDashboardActivity.class);
            startActivity(intent);

        }
        number.setText(phone_number);
        password.setText(passwords);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setMessage("Authenticating...");
                progressDialog.show();
                if(number.getText().toString().length() == 11 && password.getText().toString().length() > 0) {
                    Call<ClientInfo> call = RetrofitClient
                            .getInstance()
                            .getApi()
                            .log_in(number.getText().toString(),password.getText().toString());
                    call.enqueue(new Callback<ClientInfo>() {
                        @Override
                        public void onResponse(Call<ClientInfo> call, Response<ClientInfo> response) {
                            try {
                                ClientInfo s = response.body();
                                //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                                if (s.getE() == 0) {
                                    Toasty.success(getApplicationContext(), "Welcome Back!", Toast.LENGTH_LONG, true).show();
                                    Intent intent = new Intent(getApplicationContext(), ClientDashboardActivity.class);
                                    intent.putExtra("PHONE", number.getText().toString());
                                    intent.putExtra("PASS", password.getText().toString());
                                    intent.putExtra("CLIENTId", s.getM().getId());
                                    intent.putExtra("PhotoURL", s.getM().getPhotoUrl());
                                    intent.putExtra("SESSION", session);

                                    if(checkBox.isChecked()){
                                        editor.putString("number",number.getText().toString());
                                        editor.putString("password",password.getText().toString());
                                        editor.putBoolean("saveLogin", true);
                                        editor.commit();
                                    }else{
                                        editor.putString("number","");
                                        editor.putString("password","");
                                        editor.commit();
                                    }
                                    startActivity(intent);
                                }else{
                                    Toasty.error(getApplicationContext(), "Wrong Credential", Toast.LENGTH_LONG, true).show();
                                    Intent intent = new Intent(getApplicationContext(), LogInActivity.class);
                                    startActivity(intent);
                                }
                                progressDialog.dismiss();
                            } catch (NullPointerException e) {
                                e.printStackTrace();
                                progressDialog.dismiss();
                            }
                        }
                        @Override
                        public void onFailure(Call<ClientInfo> call, Throwable t) {
                            Toasty.error(getApplicationContext(), "Try Again !", Toast.LENGTH_LONG, true).show();
                            Intent i = new Intent(getApplicationContext(), LogInActivity.class);
                            startActivity(i);
                            progressDialog.dismiss();
                        }
                    });
                }
                else{
                    Toasty.error(getApplicationContext(), "Incorrect Password !", Toast.LENGTH_LONG, true).show();
                    progressDialog.dismiss();
                }
            }
        });


        forgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LogInActivity.this,PasswordResetActivity.class);
                startActivity(i);
            }
        });
        signUp1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LogInActivity.this,SignUpActivity.class);
                startActivity(i);
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
        instagram_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://rocket.dheo.com/user-manual";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

    }
    

    public void onClick(View arg0)
    {
        Intent i = new Intent(LogInActivity.this,SignUpActivity.class);
        startActivity(i);
    }


}