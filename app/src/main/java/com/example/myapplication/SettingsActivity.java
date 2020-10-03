package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.database.SQLException;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.ModelClassBankBranches.BankBranches;
import com.example.myapplication.ModelClassBanksAndBranches.BanksAndBranches;
import com.example.myapplication.ModelClassBanksAndBranches.M;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsActivity extends AppCompatActivity {
    ArrayList<String> bank_name;
    ArrayList<String> branch_bank;

    ArrayList<String> branches_name;
    List<BankBranches> all_branches = new ArrayList<>();
    List<BankBranches> clone_all_branches;
    TextView setting_name,go_back;
    ImageView setting_dp;
    private Spinner bank_name_show, bank_branches_show;
    private String photo_url;
    private Button bank, other_option, cash, bkash, nagad;
    LinearLayout bank_layout, other_option_layout, bkash_option;
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
        branches_name = new ArrayList<>();
        bank_name_show.setPrompt("Select a Bank");
        bank_branches_show.setPrompt("Select a Branch ");
        other_option_layout.setVisibility(View.INVISIBLE);
        bkash_option.setVisibility(View.INVISIBLE);
        bank_layout.setVisibility(View.VISIBLE);
        other_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                other_option_layout.setVisibility(View.VISIBLE);
                bank_layout.setVisibility(View.INVISIBLE);
                other_option.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.rounded_signup));
                other_option.setTextColor(Color.rgb(0, 0, 0));
                bank.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.defult_button));
                cash.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bkash_option.setVisibility(View.INVISIBLE);
                        cash.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.rounded_signup));
                        cash.setTextColor(Color.rgb(0, 0, 0));
                        bkash.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.defult_button));
                        nagad.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.defult_button));
                    }
                });
                bkash.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bkash_option.setVisibility(View.VISIBLE);
                        bkash.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.rounded_signup));
                        bkash.setTextColor(Color.rgb(0, 0, 0));
                        cash.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.defult_button));
                        nagad.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.defult_button));
                    }
                });
                nagad.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bkash_option.setVisibility(View.VISIBLE);
                        nagad.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.rounded_signup));
                        nagad.setTextColor(Color.rgb(0, 0, 0));
                        cash.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.defult_button));
                        bkash.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.defult_button));
                    }
                });

            }
        });
        bank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                other_option_layout.setVisibility(View.INVISIBLE);
                bank_layout.setVisibility(View.VISIBLE);
                bkash_option.setVisibility(View.INVISIBLE);
                bank.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.rounded_signup));
                bank.setTextColor(Color.rgb(0, 0, 0));
                other_option.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.defult_button));
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
                    if(response.body() != null){
                        final List<M> s = response.body().getM();
                        for(int i = 0; i<s.size(); i++){
                            bank_name.add(s.get(i).getBankName());

                        }
                        setBankNameAdapter();
                        bank_name_show.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
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
                                            BankBranches bankBranches = response.body();
                                            branches_name.clear();
                                            for(int j = 0; j<bankBranches.getM().getBankBranches().size(); j++){
                                               branches_name.add(bankBranches.getM().getBankBranches().get(j));
                                            }

                                            setBranchesAdapter();
                                        }catch (NullPointerException e){}
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
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Response failed", Toast.LENGTH_LONG).show();
                    }
                }catch (NullPointerException e){}
            }



            @Override
            public void onFailure(Call<BanksAndBranches> call, Throwable t) {
                Toasty.error(getApplicationContext(), "Try Again", Toast.LENGTH_LONG, true).show();
            }
        });

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