package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.database.SQLException;
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
    List<String> branches_name;
    ArrayList<Integer> bank_id;
    TextView setting_name,go_back;
    ImageView setting_dp;
    private Spinner bank_name_show, bank_branches_show;
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
        bank_branches_show = findViewById(R.id.branch_name);
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
                                        .branches(s.get(position).getBankId());
                                call1.enqueue(new Callback<BankBranches>() {
                                    @Override
                                    public void onResponse(Call<BankBranches> call, Response<BankBranches> response) {
                                        try {
                                            BankBranches bankBranches = response.body();
                                            branches_name = (List<String>) bankBranches.getM();
                                            for(int j = 0; j<branches_name.size(); j++){
                                                branch_bank.add(branches_name.get(j));
                                            }

                                            setBranchesAdapter();
                                        }catch (NullPointerException e){}
                                    }

                                    @Override
                                    public void onFailure(Call<BankBranches> call, Throwable t) {

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
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, branch_bank);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bank_branches_show.setAdapter(dataAdapter);
    }


    private void setBankNameAdapter() {
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, bank_name);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bank_name_show.setAdapter(dataAdapter);
    }

}