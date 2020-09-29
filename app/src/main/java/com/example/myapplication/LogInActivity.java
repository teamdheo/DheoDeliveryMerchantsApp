package com.example.myapplication;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.myapplication.modelClassClientInfo.ClientInfo;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class LogInActivity extends AppCompatActivity {
    private TextView forgetPass, phone_call, dheo_life;
    private EditText number, password;
    private ProgressDialog progressDialog;
    private CheckBox checkBox;
    private Button button, signUp1;
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
        helper.checkInternetConnection();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        signUp1 = (Button) findViewById(R.id.click);
        number = (EditText) findViewById(R.id.phone);
        password = (EditText) findViewById(R.id.txtPwd);
        checkBox = (CheckBox) findViewById(R.id.rememberme);
        forgetPass =(TextView) findViewById(R.id.forgrtpass);
        button = (Button) findViewById(R.id.btnLogin) ;
        phone_call = (TextView) findViewById(R.id.phonecall);
        dheo_life = (TextView) findViewById(R.id.dheolife);
        progressDialog=new ProgressDialog(this);

        sharedPreferences=getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        editor=sharedPreferences.edit();
        String phone_number=sharedPreferences.getString("number","");
        String passwords=sharedPreferences.getString("password","");
        saveLogin = sharedPreferences.getBoolean("saveLogin", false);
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
                                    Toasty.success(getApplicationContext(), s.getM().getId()+"", Toast.LENGTH_LONG, true).show();
                                    Intent intent = new Intent(getApplicationContext(), ClientDashboardActivity.class);
                                    intent.putExtra("PHONE", number.getText().toString());
                                    intent.putExtra("PASS", password.getText().toString());
                                    intent.putExtra("CLIENTId", s.getM().getId());
                                    intent.putExtra("PhotoURL", s.getM().getPhotoUrl());
                                    intent.putExtra("SESSION", session);
                                    intent.putExtra("name", s.getM().getName());
                                    intent.putExtra("balance", s.getM().getBalance());
                                    sqLiteDatabase = getBaseContext().openOrCreateDatabase("SQLite", MODE_PRIVATE, null);
                                    String sql = "CREATE TABLE IF NOT EXISTS ClientBasicInfo (_id Integer Primary Key, name TEXT,balance Integer);";
                                    sqLiteDatabase.execSQL(sql);
                                    if (session == 1) {
                                        sql = "INSERT or REPLACE INTO ClientBasicInfo VALUES ( 1,'" + s.getM().getName()+ "','" + s.getM().getBalance() + "');";
                                        sqLiteDatabase.execSQL(sql);
                                    }
//
                                    Cursor query = sqLiteDatabase.rawQuery("SELECT * FROM ClientBasicInfo", null);
                                    if (query.moveToFirst()) {
                                        try {
                                            name = query.getString(1);
                                            balance = query.getInt(2);

                                        } catch (IllegalStateException e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        Intent i = new Intent(getApplicationContext(), LogInActivity.class);
                                        startActivity(i);
                                    }
                                    query.close();

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
                                    Toasty.error(getApplicationContext(), "wrong credential", Toast.LENGTH_LONG, true).show();
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
                            Toasty.error(getApplicationContext(), "Try again !", Toast.LENGTH_LONG, true).show();
                            Intent i = new Intent(getApplicationContext(), LogInActivity.class);
                            startActivity(i);
                            progressDialog.dismiss();
                        }
                    });
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
    

    public void onClick(View arg0)
    {
        Intent i = new Intent(LogInActivity.this,SignUpActivity.class);
        startActivity(i);
    }


}