package com.example.myapplication;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.ModelClassBankBranches.BankBranches;
import com.example.myapplication.ModelClassBanksAndBranches.BanksAndBranches;
import com.example.myapplication.ModelClassBanksAndBranches.M;
import com.example.myapplication.ModelClassClientPrefInfoAccountSetting.ClientPrefInfoAccountSetting;
import com.example.myapplication.modelClassPickupAddresses.PickupAddresses;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import es.dmoral.toasty.Toasty;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsActivity extends AppCompatActivity {
    private static final int SELECT_PICTURE = 1;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private static final int CAMERA_REQUEST = 2 ;
    private String selectedImagePath;
    private String currentImagePath, national_id;
    ArrayList<String> bank_name;
    ArrayList<String> branches_name;
    private TextView setting_name,go_back, valid_from,nagad_hint,bkash_hint,verify_submit_date, phone_call, facebook, my_delivery,dashboard_billing,settings, user_manual,log_out, dhep_delivery, the_user_manual, meet_the_team, privacy_policy,image_upload,show_upload_image,reset_pass;
    private EditText bkash_or_nagad, edit_branch_name, edit_account_name, edit_account_num, edit_nagad_num, add_new_add, add_new_phone,edit_web_link,change_account_phone;
    ImageView setting_dp;
    private int client_id;
    private Spinner bank_name_show, bank_branches_show;
    private String photo_url, mode;
    private Button bank, other_option, cash, bkash, nagad, save_payment_method, add_address_btn, save_new_address, cancel_new_add, add_web_address_btn, change_phone_btn,upload_image_to_server;
    LinearLayout bank_layout, other_option_layout, bkash_option, address_sec_layout;
    private RecyclerView all_address;
    private RecyclerView.Adapter adapter;
    List<com.example.myapplication.modelClassPickupAddresses.M> all_add_settings;
    File imageFile;
    String picturePath = null;
    private ProgressDialog progressDialog;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
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
        bank_branches_show = findViewById(R.id.branch_name);
        bank_layout = findViewById(R.id.linear_bank);
        other_option_layout = findViewById(R.id.linear_other);
        bkash_option = findViewById(R.id.bikash_option);
        cash = findViewById(R.id.cash);
        bkash = findViewById(R.id.bkash);
        nagad = findViewById(R.id.nagad);
        edit_branch_name = findViewById(R.id.edit_bank_branch);
        edit_account_name = findViewById(R.id.edit_account_name);
        edit_account_num = findViewById(R.id.edit_account_number);
        bkash_or_nagad = findViewById(R.id.edit_bkash_num);
        //nagad_option = findViewById(R.id.nagad_option);
        edit_nagad_num = findViewById(R.id.edit_nagad_num);
        save_payment_method = findViewById(R.id.save_payment_method);
        nagad_hint = findViewById(R.id.nagd_hint);
        bkash_hint = findViewById(R.id.bkash_hint);
        all_address = findViewById(R.id.all_address);
        all_address.setHasFixedSize(true);
        all_address.setLayoutManager(new LinearLayoutManager(this));
        add_address_btn = findViewById(R.id.add_address_btn);
        address_sec_layout = findViewById(R.id.address_sec_layout);
        add_new_add = findViewById(R.id.add_new_add);
        add_new_phone = findViewById(R.id.add_new_phone);
        save_new_address = findViewById(R.id.save_new_address);
        cancel_new_add = findViewById(R.id.cancel_new_address);
        edit_web_link = findViewById(R.id.edit_web_link);
        add_web_address_btn = findViewById(R.id.add_web_address_btn);
        change_account_phone = findViewById(R.id.change_account_phone);
        change_phone_btn = findViewById(R.id.change_phone_btn);
        verify_submit_date = findViewById(R.id.verify_submit_date);
        valid_from = findViewById(R.id.valued_from_s);
        image_upload = findViewById(R.id.upload_image);
        show_upload_image = findViewById(R.id.show_upload_image);
        reset_pass = findViewById(R.id.reset_pass);
        upload_image_to_server = findViewById(R.id.upload_image_to_server);

        phone_call = findViewById(R.id.billing_phone5);
        facebook = findViewById(R.id.billing_fb);
        my_delivery = findViewById(R.id.billing_my_delivery);
        dashboard_billing = findViewById(R.id.billing_Billing);
        settings = findViewById(R.id.billing_settings);
        user_manual = findViewById(R.id.billing_user_manual);
        log_out = findViewById(R.id.billing_logout);
        dhep_delivery = findViewById(R.id.billing_dheo_delivery);
        the_user_manual = findViewById(R.id.billing_The_manual);
        meet_the_team = findViewById(R.id.billing_meet_team);
        privacy_policy = findViewById(R.id.billing_policy);

        setting_name.setText(helper.getName());
        progressDialog = new ProgressDialog(this);
        try {
            if (helper.getPhoto_Url().equals("default.svg")) {
                setting_dp = findViewById(R.id.setting_profile_photo);
            } else {
                setting_dp = findViewById(R.id.setting_profile_photo);
                photo_url = "https://dheo-static-sg.s3-ap-southeast-1.amazonaws.com/img/rocket/clients/" + helper.getPhoto_Url();
                Picasso.get().load(photo_url).into(setting_dp);
            }
        } catch (SQLException e) {

        }
        client_id = helper.getClientId();
        go_back.setText("< Go Back");
        bank_name = new ArrayList<>();
        branches_name = new ArrayList<>();
        bank_name_show.setPrompt("Select a Bank");
        bank_branches_show.setPrompt("Select a Branch ");
//
        other_option_layout.setVisibility(View.GONE);
        //nagad_option.setVisibility(View.GONE);
        bank_layout.setVisibility(View.VISIBLE);
        bkash_option.setVisibility(View.GONE);
        address_sec_layout.setVisibility(View.GONE);
        verify_submit_date.setVisibility(View.GONE);
        other_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                other_option_layout.setVisibility(View.VISIBLE);
                bank_layout.setVisibility(View.GONE);
                other_option.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.rounded_signup));
                other_option.setTextColor(Color.rgb(255, 255, 255));
                bank.setTextColor(Color.rgb(0, 0, 0));
                cash.setTextColor(Color.rgb(0, 0, 0));
                bkash.setTextColor(Color.rgb(0, 0, 0));
                nagad.setTextColor(Color.rgb(0, 0, 0));
                bank.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.defult_button));
                cash.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bkash_option.setVisibility(View.GONE);
//                        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) cash.getLayoutParams();
//                        params.bottomMargin = 20;params.leftMargin = 100;
                        cash.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.rounded_signup));
                        cash.setTextColor(Color.rgb(255, 255, 255));
                        bank.setTextColor(Color.rgb(0, 0, 0));
                        bkash.setTextColor(Color.rgb(0, 0, 0));
                        nagad.setTextColor(Color.rgb(0, 0, 0));
                        bkash.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.defult_button));
                        nagad.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.defult_button));
                    }
                });
                bkash.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bkash_option.setVisibility(View.VISIBLE);
                        edit_nagad_num.setVisibility(View.GONE);
                        nagad_hint.setVisibility(View.GONE);
                        bkash_hint.setVisibility(View.VISIBLE);
                        bkash_or_nagad.setVisibility(View.VISIBLE);
                        //nagad_option.setVisibility(View.GONE);
                        bkash.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.rounded_signup));
                        bkash.setTextColor(Color.rgb(255, 255, 255));
                        bank.setTextColor(Color.rgb(0, 0, 0));
                        cash.setTextColor(Color.rgb(0, 0, 0));
                        nagad.setTextColor(Color.rgb(0, 0, 0));
                        cash.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.defult_button));
                        nagad.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.defult_button));
                    }
                });
                nagad.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bkash_option.setVisibility(View.VISIBLE);
                        //nagad_option.setVisibility(View.VISIBLE);
                        edit_nagad_num.setVisibility(View.VISIBLE);
                        bkash_or_nagad.setVisibility(View.GONE);
                        nagad_hint.setVisibility(View.VISIBLE);
                        bkash_hint.setVisibility(View.GONE);
                        nagad.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.rounded_signup));
                        nagad.setTextColor(Color.rgb(255, 255, 255));
                        bank.setTextColor(Color.rgb(0, 0, 0));
                        cash.setTextColor(Color.rgb(0, 0, 0));
                        bkash.setTextColor(Color.rgb(0, 0, 0));
                        cash.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.defult_button));
                        bkash.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.defult_button));
                    }
                });

            }
        });
        bank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                other_option_layout.setVisibility(View.GONE);
                bank_layout.setVisibility(View.VISIBLE);
                bkash_option.setVisibility(View.GONE);
                other_option.setTextColor(Color.rgb(0, 0, 0));
                //nagad_option.setVisibility(View.GONE);
                bank.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.rounded_signup));
                bank.setTextColor(Color.rgb(255, 255, 255));
                other_option.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.defult_button));
            }
        });
        add_address_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                address_sec_layout.setVisibility(View.VISIBLE);
            }
        });
        cancel_new_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                address_sec_layout.setVisibility(View.GONE);
            }
        });
        progressDialog.setMessage("Processing...");
        progressDialog.show();
        Call<ClientPrefInfoAccountSetting> call2 = RetrofitClient
                .getInstance()
                .getApi()
                .client_account_pref_setting_info(client_id);
        call2.enqueue(new Callback<ClientPrefInfoAccountSetting>() {
            @Override
            public void onResponse(Call<ClientPrefInfoAccountSetting> call, Response<ClientPrefInfoAccountSetting> response) {
                try {
                    if (response.body() != null) {
                        progressDialog.dismiss();
                        ClientPrefInfoAccountSetting s = response.body();
                        try {
//                            ArrayAdapter<String> adapter = (ArrayAdapter<String>) bank_name_show.getAdapter();
//                            int position = adapter.getPosition(s.getM().getBankName());
//                            bank_name_show.setSelection(position);
                        } catch (NullPointerException e) {
                        }
                        try {
                            edit_web_link.setText(s.getM().getLink());
                        } catch (NullPointerException e) {
                        }
                        try {
                            change_account_phone.setText(s.getM().getPhoneNo());
                        } catch (NullPointerException e) {
                        }
                        try {
                            if (s.getM().getSubmitted()) {
                                verify_submit_date.setVisibility(View.VISIBLE);
                                verify_submit_date.setText("You submitted your ID on " + s.getM().getSubmittedDate());
                            }
                        } catch (NullPointerException e) {
                        }
                        try {
                            valid_from.setText("Valued from " + s.getM().getStatDate());

                            //bank_name_show.setI
                            if (s.getM().getPrefersCash()) {
                                other_option_layout.setVisibility(View.VISIBLE);
                                bank_layout.setVisibility(View.GONE);
                                //nagad_option.setVisibility(View.GONE);
                                other_option.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.rounded_signup));
                                other_option.setTextColor(Color.rgb(255, 255, 255));
                                bank.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.defult_button));
                                bkash_option.setVisibility(View.GONE);
                                cash.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.rounded_signup));
                                cash.setTextColor(Color.rgb(255, 255, 255));
                                bkash.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.defult_button));
                                nagad.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.defult_button));
                            }
                        } catch (NullPointerException e) {
                        }
                        try {
                            if (s.getM().getPrefersBkash()) {
                                other_option_layout.setVisibility(View.VISIBLE);
                                bank_layout.setVisibility(View.GONE);
                                //nagad_option.setVisibility(View.GONE);
                                nagad_hint.setVisibility(View.GONE);
                                bkash_hint.setVisibility(View.VISIBLE);
                                other_option.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.rounded_signup));
                                other_option.setTextColor(Color.rgb(255, 255, 255));
                                bank.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.defult_button));
                                bkash_or_nagad.setText(s.getM().getNumber());

                                bkash_option.setVisibility(View.VISIBLE);
                                edit_nagad_num.setVisibility(View.GONE);
                                bkash_or_nagad.setVisibility(View.VISIBLE);
                                bkash.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.rounded_signup));
                                bkash.setTextColor(Color.rgb(255, 255, 255));
                                cash.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.defult_button));
                                nagad.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.defult_button));

                            }
                        } catch (NullPointerException e) {
                        }
                        try {
                            if (s.getM().getPrefersNagad()) {
                                other_option_layout.setVisibility(View.VISIBLE);
                                bank_layout.setVisibility(View.GONE);
                                other_option.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.rounded_signup));
                                other_option.setTextColor(Color.rgb(255, 255, 255));
                                bank.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.defult_button));
                                bkash_or_nagad.setText(s.getM().getNumber());

                                bkash_option.setVisibility(View.GONE);
                                //nagad_option.setVisibility(View.VISIBLE);
                                edit_nagad_num.setVisibility(View.VISIBLE);
                                bkash_or_nagad.setVisibility(View.GONE);
                                nagad_hint.setVisibility(View.VISIBLE);
                                bkash_hint.setVisibility(View.GONE);
                                nagad.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.rounded_signup));
                                nagad.setTextColor(Color.rgb(255, 255, 255));
                                cash.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.defult_button));
                                bkash.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.defult_button));
                            }
                        } catch (NullPointerException e) {
                        }
                        try {
                            if (s.getM().getPrefersBank()) {
                                other_option_layout.setVisibility(View.GONE);
                                bank_layout.setVisibility(View.VISIBLE);
                                bkash_option.setVisibility(View.GONE);
                                //nagad_option.setVisibility(View.GONE);
                                bank.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.rounded_signup));
                                bank.setTextColor(Color.rgb(255, 255, 255));
                                other_option.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.defult_button));
                                edit_branch_name.setText(s.getM().getBranchName());
                                edit_account_name.setText(s.getM().getAccountName());
                                edit_account_num.setText(s.getM().getAccountNumber());
                            }
                        } catch (NullPointerException e) {
                        }
                    }
                } catch (NullPointerException e) {
                    Toasty.error(getApplicationContext(), "null", Toast.LENGTH_LONG, true).show();
                }
            }

            @Override
            public void onFailure(Call<ClientPrefInfoAccountSetting> call, Throwable t) {
                Toasty.error(getApplicationContext(), "unable to response", Toast.LENGTH_LONG, true).show();
            }
        });

        //linearLayout.setBackgroundColor(Color.argb(0,0,0, (float) 0.2));
        Call<BanksAndBranches> call = RetrofitClient
                .getInstance()
                .getApi()
                .bank_and_branches();
        call.enqueue(new Callback<BanksAndBranches>() {
            @Override
            public void onResponse(Call<BanksAndBranches> call, Response<BanksAndBranches> response) {
                try {
                    if (response.body() != null) {
                        final List<M> s = response.body().getM();
                        for (int i = 0; i < s.size(); i++) {
                            bank_name.add(s.get(i).getBankName());

                        }
                        setBankNameAdapter();
                        bank_name_show.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
                                Integer item = s.get(position).getBankId();
                                Call<BankBranches> call1 = RetrofitClient
                                        .getInstance()
                                        .getApi()
                                        .branches(item);
                                //Toasty.error(getApplicationContext(), item+"", Toast.LENGTH_LONG, true).show();

                                call1.enqueue(new Callback<BankBranches>() {
                                    @Override
                                    public void onResponse(Call<BankBranches> call, Response<BankBranches> response) {
                                        try {
                                            final BankBranches bankBranches = response.body();
                                            branches_name.clear();
                                            for (int j = 0; j < bankBranches.getM().getBankBranches().size(); j++) {
                                                branches_name.add(bankBranches.getM().getBankBranches().get(j));
                                            }

                                            setBranchesAdapter();
//                                            bank_branches_show.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                                                    //Integer item = s.get(position).getBankId();
//                                                    edit_branch_name.setText(bankBranches.getM().getBankBranches().get(position));
//                                                }
//                                                public void onNothingSelected(AdapterView<?> parent) {
//                                                }
//                                            });
                                        } catch (NullPointerException e) {
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<BankBranches> call, Throwable t) {
                                        Toasty.error(getApplicationContext(), "Try Again", Toast.LENGTH_LONG, true).show();

                                    }
                                });
                            }

                            public void onNothingSelected(AdapterView<?> parent) {
                            }
                        });
                    } else {
                        Toast.makeText(getApplicationContext(), "Response failed", Toast.LENGTH_LONG).show();
                    }
                } catch (NullPointerException e) {
                }
            }

            @Override
            public void onFailure(Call<BanksAndBranches> call, Throwable t) {
                Toasty.error(getApplicationContext(), "Try Again", Toast.LENGTH_LONG, true).show();
            }
        });

//        if(bank. getBackground().getConstantState().equals(getResources().getDrawable(R.drawable.rounded_signup).getConstantState())){
//            mode = "bank" ;
//        }
//        if(cash.getBackground().getConstantState().equals(getResources().getDrawable(R.drawable.rounded_signup).getConstantState())){
//            mode = "cash" ;
//        }
//        if(bkash.getBackground().getConstantState().equals(getResources().getDrawable(R.drawable.rounded_signup).getConstantState())){
//            mode = "bkash" ;
//        }
//        if(nagad.getBackground().getConstantState().equals(getResources().getDrawable(R.drawable.rounded_signup).getConstantState())){
//            mode = "nagad" ;
//        }
//        Drawable myDrawable = getResources().getDrawable(R.drawable.rounded_signup);
//        if(bank.getBackground().getConstantState().equals(myDrawable.getConstantState())){
//            mode = "bank";
//        }
//        else if(cash.getBackground().getConstantState().equals(myDrawable.getConstantState())){
//            mode = "cash";
//        }
//        else if(bkash.getBackground().getConstantState().equals(myDrawable.getConstantState())){
//            mode = "bkash";
//        }
//        else if(nagad.getBackground().getConstantState().equals(myDrawable.getConstantState())){
//            mode = "nagad";
//        }
        if (!edit_account_name.getText().toString().equals(null) && !edit_account_num.getText().toString().equals(null)) {
            mode = "bank";
        } else if (!edit_nagad_num.getText().toString().equals(null)) {
            mode = "nagad";
        } else if (!bkash_or_nagad.getText().toString().equals(null)) {
            mode = "bkash";
        } else {
            mode = "cash";
        }

        Toast.makeText(getApplicationContext(), mode, Toast.LENGTH_LONG).show();
        save_payment_method.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), bank_name_show.getSelectedItem() + "", Toast.LENGTH_LONG).show();
//                progressDialog.setMessage("Updating...");
//                progressDialog.show();
                Call<ResponseBody> call3 = RetrofitClient
                        .getInstance()
                        .getApi()
                        .client_payment_settings_update(client_id, mode, bank_name_show.getSelectedItem().toString(), bank_branches_show.getSelectedItem().toString(), edit_account_name.getText().toString(), edit_account_num.getText().toString(), bkash_or_nagad.getText().toString(), edit_nagad_num.getText().toString());
                call3.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        String s = null;
                        try {
                            s = response.body().string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                        if (s.equals("{\"e\":0}")) {
                            progressDialog.dismiss();
                            Toasty.error(getApplicationContext(), "successfully updated", Toast.LENGTH_LONG, true).show();
                            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                            startActivity(intent);

                        } else {
                            Toasty.error(getApplicationContext(), "server failed to response", Toast.LENGTH_LONG, true).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toasty.error(getApplicationContext(), "slow internet, try again", Toast.LENGTH_LONG, true).show();
                    }
                });
                //Toasty.error(getApplicationContext(), bank_name_show.getSelectedItem().toString(), Toast.LENGTH_LONG, true).show();
            }
        });

        Call<PickupAddresses> call1 = RetrofitClient
                .getInstance()
                .getApi()
                .get_pickup_address(client_id);
        call1.enqueue(new Callback<PickupAddresses>() {
            @Override
            public void onResponse(Call<PickupAddresses> call, Response<PickupAddresses> response) {
                if (response.body() != null) {
                    try {
                        PickupAddresses pickupAddresses = response.body();
                        all_add_settings = pickupAddresses.getM();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "failed", Toast.LENGTH_LONG).show();
                    }
                    adapter = new AdapterSettingsAllAddresses(all_add_settings, getApplicationContext());
                    all_address.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<PickupAddresses> call, Throwable t) {
                Toasty.error(getApplicationContext(), "slow internet, try again", Toast.LENGTH_LONG, true).show();
            }
        });

        save_new_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setMessage("Updating...");
                progressDialog.show();
                Call<ResponseBody> call4 = RetrofitClient
                        .getInstance()
                        .getApi()
                        .add_new_address(client_id, add_new_phone.getText().toString(), add_new_add.getText().toString());
                call4.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        String s = null;
                        try {
                            s = response.body().string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                        if (s.equals("{\"e\":0}")) {
                            progressDialog.dismiss();
                            Toasty.error(getApplicationContext(), "successfully updated", Toast.LENGTH_LONG, true).show();
                            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                            startActivity(intent);

                        } else {
                            Toasty.error(getApplicationContext(), "server failed to response", Toast.LENGTH_LONG, true).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toasty.error(getApplicationContext(), "slow internet, try again", Toast.LENGTH_LONG, true).show();

                    }
                });

            }
        });

        add_web_address_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setMessage("Updating...");
                progressDialog.show();
                Call<ResponseBody> call5 = RetrofitClient
                        .getInstance()
                        .getApi()
                        .update_link(client_id, edit_web_link.getText().toString());
                call5.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        String s = null;
                        try {
                            s = response.body().string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                        if (s.equals("{\"e\":0}")) {
                            progressDialog.dismiss();
                            Toasty.error(getApplicationContext(), "successfully updated", Toast.LENGTH_LONG, true).show();
                            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                            startActivity(intent);

                        } else {
                            Toasty.error(getApplicationContext(), "server failed to response", Toast.LENGTH_LONG, true).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toasty.error(getApplicationContext(), "slow internet, try again", Toast.LENGTH_LONG, true).show();
                    }
                });
            }
        });

        change_phone_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setMessage("Updating...");
                progressDialog.show();
                Call<ResponseBody> call6 = RetrofitClient
                        .getInstance()
                        .getApi()
                        .update_number(client_id, change_account_phone.getText().toString());
                call6.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        String s = null;
                        try {
                            s = response.body().string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                        if (s.equals("{\"e\":0}")) {
                            progressDialog.dismiss();
                            Toasty.error(getApplicationContext(), "successfully updated", Toast.LENGTH_LONG, true).show();
                            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                            startActivity(intent);

                        } else {
                            Toasty.error(getApplicationContext(), "server failed to response", Toast.LENGTH_LONG, true).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toasty.error(getApplicationContext(), "slow internet, try again", Toast.LENGTH_LONG, true).show();
                    }
                });
            }
        });

        phone_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel: +8801301377181"));
                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://m.me/dheolife";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });

        my_delivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ClientDashboardActivity.class);
                startActivity(intent);
            }
        });
        dashboard_billing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ClientDashboardBillingActivity.class);
                startActivity(intent);
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(intent);
            }
        });
        user_manual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://rocket.dheo.com/user-manual";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });

        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
                editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();
                Intent intent = new Intent(getApplicationContext(), LogInActivity.class);
                startActivity(intent);
            }
        });
        dhep_delivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ClientDashboardActivity.class);
                startActivity(intent);
            }
        });
        the_user_manual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://rocket.dheo.com/user-manual";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });
        meet_the_team.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://team.dheo.com";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });
        privacy_policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://dheo.com/privacy";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });
        reset_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PasswordResetActivity.class);
                startActivity(intent);
            }
        });

        image_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(SettingsActivity.this, R.style.AppTheme));
                builder.setCancelable(false);
                final View customLayout = getLayoutInflater().inflate(R.layout.national_id_upload_dialog, null);
                builder.setView(customLayout);
                TextView take_a_photo = customLayout.findViewById(R.id.take_a_photo);
                TextView gallery_upload = customLayout.findViewById(R.id.gallery_upload);
                Button cancel_btn = customLayout.findViewById(R.id.cancel_btn);
                final AlertDialog dialog = builder.create();
                dialog.show();
                cancel_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                gallery_upload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            if (ActivityCompat.checkSelfPermission(SettingsActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(SettingsActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, SELECT_PICTURE);
                            } else {
                                Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(pickPhoto , SELECT_PICTURE);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        dialog.dismiss();
                    }
                });
                take_a_photo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        captureImage(view);
                        dialog.dismiss();
                    }
                });
            }
        });
        upload_image_to_server.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setMessage("Updating...");
                progressDialog.show();
                try {
                    if(!national_id.equals(null)){
                        Toast.makeText(getApplicationContext(), "image update", Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                        Call<ResponseBody> call_national_id = RetrofitClient
                                .getInstance()
                                .getApi()
                                .upload_national_id(client_id,national_id);
                        call_national_id.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                String s = null;
                                try {
                                    s = response.body().string();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                                if (s.equals("{\"e\":0}")) {
                                    progressDialog.dismiss();
                                    Toasty.error(getApplicationContext(), "successfully updated", Toast.LENGTH_LONG, true).show();
                                    Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                                    startActivity(intent);

                                } else {
                                    Toasty.error(getApplicationContext(), "server failed to response", Toast.LENGTH_LONG, true).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Toasty.error(getApplicationContext(), "Try again!", Toast.LENGTH_LONG, true).show();
                            }
                        });
                    }

                }catch (NullPointerException e){
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "no image found", Toast.LENGTH_LONG).show();
                }
            }
        });


    }
    public void captureImage(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            imageFile = null;
            try {
                imageFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (imageFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.myapplication.fileprovider",
                        imageFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
//                intent.putExtra("image_path",currentImagePath);
                startActivityForResult(intent, CAMERA_REQUEST);
            }
        }
    }
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentImagePath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_PICTURE && resultCode == RESULT_OK && data != null) {
            Uri selectedImage =  data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            if (selectedImage != null) {
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                if (cursor != null) {
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
                    File file = new File(picturePath);
                    String imageName = file.getName();
                    show_upload_image.setText(imageName);
                    show_upload_image.setTextSize(12);

                    float aspectRatio = bitmap.getWidth() /
                            (float) bitmap.getHeight();
                    int width = 500;
                    int height = Math.round(width / aspectRatio);
                    bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true); //end
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] imageBytes = baos.toByteArray();
                    //todo out of memory exception
                    bitmap.recycle();
                    national_id = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                    cursor.close();
                }
            }

        }
        else if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap bitmap = BitmapFactory.decodeFile(currentImagePath);
            //imageView.setImageBitmap(bitmap);
            File file = new File(currentImagePath);
            String imageName = file.getName();
            show_upload_image.setText(imageName);
            show_upload_image.setTextSize(10);
            float aspectRatio = bitmap.getWidth() /
                    (float) bitmap.getHeight();
            int width = 500;
            int height = Math.round(width / aspectRatio);
            bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true); //end
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageBytes = baos.toByteArray();
            //todo out of memory exception
            bitmap.recycle();
            national_id = Base64.encodeToString(imageBytes, Base64.DEFAULT);


        }
    }

    private void setBranchesAdapter() {
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, branches_name);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bank_branches_show.setAdapter(dataAdapter);
    }


    private void setBankNameAdapter() {
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, bank_name);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bank_name_show.setAdapter(dataAdapter);
    }
}