package com.dheo.dheodeliverymerchantapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dheo.dheodeliverymerchantapp.ModelClassClientBasicInfo.ClientBasicInfo;
import com.dheo.dheodeliverymerchantapp.ModelClassGrowthPerDay.GrowthPerDay;
import com.dheo.dheodeliverymerchantapp.ModelClassGrowthPerMonth.GrowthPerMonth;
import com.dheo.dheodeliverymerchantapp.ModelClassGrowthPerMonth.M;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GraphActivity extends AppCompatActivity {
    private List<M> all_growth;
    private List<com.dheo.dheodeliverymerchantapp.ModelClassGrowthPerDay.M> per_day_growth;
    private TextView growth_month_show,growth_per_month;
    ArrayList<Entry> entry_list;
    ArrayList<String> months;
    ArrayList<BarEntry> year_growth;
    ArrayList<String> month_name;
    int client_id;
    LinearLayout graph_layout;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    Helper helper = new Helper(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getSupportActionBar().setElevation(0);//remove actionbar shadow
        setTitle("Performance Graph");
        client_id = helper.getClientId();
        setContentView(R.layout.activity_graph);
        growth_month_show = findViewById(R.id.growth_year);
        growth_per_month = findViewById(R.id.growth_per_month);
        final LineChart lineChart = findViewById(R.id.lineChart);
        final BarChart growth_bar_chart = findViewById(R.id.growth_bar_chart);
        graph_layout = findViewById(R.id.graph_layout);

        Toolbar toolbar = findViewById(R.id.color_toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        setSupportActionBar(toolbar);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toggle.syncState();

        growth_bar_chart.setNoDataText("");
        year_growth = new ArrayList<>();
        month_name = new ArrayList<>();
        month_name.add("");

        entry_list = new ArrayList<Entry>();
        months = new ArrayList<String>();
        Call<GrowthPerDay> growthPerMonthCall = RetrofitClient
                .getInstance()
                .getApi()
                .growth_per_day(client_id);
        growthPerMonthCall.enqueue(new Callback<GrowthPerDay>() {
            @Override
            public void onResponse(Call<GrowthPerDay> call, Response<GrowthPerDay> response) {
                try {
                    if(response.body() != null){
                        GrowthPerDay growthPerDay = response.body();
                        per_day_growth = growthPerDay.getM();
                        growth_month_show.setText("The Performance Of Month "+ per_day_growth.get(0).getMonth());
                        //Toast.makeText(getApplicationContext(), response.body().getM().get(0).getPrettyMonth(), Toast.LENGTH_LONG).show();
                        int i = 0;
                        for(com.dheo.dheodeliverymerchantapp.ModelClassGrowthPerDay.M graph : per_day_growth){
                            entry_list.add(new Entry(i++, graph.getTotal()));
                            months.add(graph.getDay().toString());
                        }
                        LineDataSet set1 = new LineDataSet(entry_list, " ");
                        set1.setAxisDependency(YAxis.AxisDependency.LEFT);
                        set1.setColor(ColorTemplate.getHoloBlue());
                        set1.setValueTextColor(ColorTemplate.getHoloBlue());
                        set1.setLineWidth(1.5f);
                        set1.setDrawCircles(true);
                        set1.setDrawValues(false);
                        set1.setFillAlpha(110);
                        set1.setColor(Color.RED);
                        set1.setCircleColor(Color.RED);
                        set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
                        set1.setDrawCircleHole(true);
                        XAxis xAxis = lineChart.getXAxis();

                        set1.setDrawFilled(false);
                        lineChart.getDescription().setEnabled(false);
                        lineChart.getLegend().setEnabled(false);
                        lineChart.setDragEnabled(true);
                        lineChart.setScaleEnabled(true);
                        lineChart.getXAxis().setDrawGridLines(false);
                        lineChart.getAxisRight().setEnabled(false);
                        // create a data object with the datasets
                        final LineData data = new LineData(set1);
                        data.setValueTextColor(Color.GREEN);
                        data.setValueTextSize(9f);
                        lineChart.setData(data);

                    }
                }catch (NullPointerException e){

                }
            }

            @Override
            public void onFailure(Call<GrowthPerDay> call, Throwable t) {

            }
        });

        Call<GrowthPerMonth> monthCall = RetrofitClient
                .getInstance()
                .getApi()
                .get_growth_graph(client_id);
        monthCall.enqueue(new Callback<GrowthPerMonth>() {
            @Override
            public void onResponse(Call<GrowthPerMonth> call, Response<GrowthPerMonth> response) {
                try {
                    if (response.body() != null) {
                        List<M> s = response.body().getM();
                        growth_per_month.setText("The Performance Of Year "+ s.get(0).getYear());
                        int i = 1;
                        for (M month : s) {
                            year_growth.add(new BarEntry(i++, month.getTotal()));
                            month_name.add(month.getPrettyMonth());
                        }
                        month_name.add("");
//                        dialog.dismiss();

                        BarDataSet bardataset = new BarDataSet(year_growth, "");
                        growth_bar_chart.animateY(2000);
                        BarData data = new BarData(bardataset);
                        bardataset.setColors(ColorTemplate.MATERIAL_COLORS);
                        growth_bar_chart.setData(data);
                        bardataset.setDrawValues(false);

                        XAxis xAxis = growth_bar_chart.getXAxis();
                        xAxis.setLabelCount(month_name.size());
                        xAxis.setValueFormatter(new IndexAxisValueFormatter(month_name));
                        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);
                        Description description = new Description();
                        description.setText("");
                        description.setTextSize(14f);

                        growth_bar_chart.getDescription().setTextAlign(Paint.Align.LEFT);
                        growth_bar_chart.setDescription(description);
                        growth_bar_chart.getLegend().setEnabled(false);
                        xAxis.setDrawGridLines(false);
                        xAxis.setDrawAxisLine(false);
                        xAxis.setLabelRotationAngle(270);
                        xAxis.setGranularity(5f);
                        growth_bar_chart.invalidate();
                    }
                } catch (NullPointerException | Resources.NotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "failed", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<GrowthPerMonth> call, Throwable t) {

            }
        });

        Call<ClientBasicInfo> call1 = RetrofitClient
                .getInstance()
                .getApi()
                .client_basic_info(helper.getClientId());
        call1.enqueue(new Callback<ClientBasicInfo>() {
            @Override
            public void onResponse(Call<ClientBasicInfo> call, Response<ClientBasicInfo> response) {
                try {
                    ClientBasicInfo s = response.body();
                    if(s.getE() == 0){
                        View hView =  navigationView.getHeaderView(0);
                        TextView nav_name = hView.findViewById(R.id.nav_name);
                        ImageView nav_photo = hView.findViewById(R.id.nav_photo);
                        nav_name.setText(s.getM().getName());
                        try {
                            if (s.getM().getProPic().equals("default.svg")) {

                            } else {

                                String photo_url = "https://dheo-static-sg.s3-ap-southeast-1.amazonaws.com/img/rocket/clients/" + s.getM().getProPic();
                                Picasso.get().load(photo_url).memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE).into(nav_photo);
                            }
                        } catch (NullPointerException e) {

                        }
                    }
                }catch (NullPointerException e){

                }
            }

            @Override
            public void onFailure(Call<ClientBasicInfo> call, Throwable t) {

            }
        });

        MenuItem dashboardItem = navigationView.getMenu().findItem(R.id.nav_dashboard);
        dashboardItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(getApplicationContext(), ClientDashboardActivity.class);
                startActivity(intent);
                return true;
            }
        });

        MenuItem billingItem = navigationView.getMenu().findItem(R.id.nav_billing);
        billingItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(getApplicationContext(), ClientDashboardBillingActivity.class);
                startActivity(intent);
                return true;
            }
        });

        MenuItem settingsItem = navigationView.getMenu().findItem(R.id.nav_setting);
        settingsItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(intent);
                return true;
            }
        });

        MenuItem performanceItem = navigationView.getMenu().findItem(R.id.nav_graph);
        performanceItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(getApplicationContext(), GraphActivity.class);
                startActivity(intent);
                return true;
            }
        });
        MenuItem orderCreateItem = navigationView.getMenu().findItem(R.id.nav_order);
        orderCreateItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(getApplicationContext(), PickupEntryActivity.class);
                startActivity(intent);
                return true;
            }
        });

        MenuItem callItem = navigationView.getMenu().findItem(R.id.nav_call);
        callItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel: 09613533533"));
                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                return true;
            }
        });

        MenuItem messageItem = navigationView.getMenu().findItem(R.id.nav_text);
        messageItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                String url = "https://m.me/dheolife";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                return true;
            }
        });

        MenuItem manualeItem = navigationView.getMenu().findItem(R.id.nav_userManual);
        manualeItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                String url = "http://rocket.dheo.com/user-manual";
                Intent intent = new Intent(getApplicationContext(), UserManualActivity.class);
                intent.putExtra("url", url);
                startActivity(intent);
                return true;
            }
        });

        MenuItem meetTeamItem = navigationView.getMenu().findItem(R.id.nav_team);
        meetTeamItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                String url = "https://team.dheo.com";
                Intent intent = new Intent(getApplicationContext(), UserManualActivity.class);
                intent.putExtra("url", url);
                startActivity(intent);
                return true;
            }
        });

        MenuItem logOutItem = navigationView.getMenu().findItem(R.id.nav_logout);
        logOutItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
                editor = sharedPreferences.edit();
                editor.putBoolean("saveLogin", false);
                editor.commit();
//                editor.clear();
//                editor.apply();
                Intent intent = new Intent(getApplicationContext(), LogInActivity.class);
                startActivity(intent);
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.three_dot_menu, menu);
        // Associate searchable configuration with the SearchView

//        SearchManager searchManager =
//                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//        SearchView searchView =
//                (SearchView) menu.findItem(R.id.search).getActionView();
//        searchView.setSearchableInfo(
//                searchManager.getSearchableInfo(getComponentName()));
//        searchView.setSubmitButtonEnabled(true);
        for(int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            SpannableString spanString = new SpannableString(menu.getItem(i).getTitle().toString());
            spanString.setSpan(new ForegroundColorSpan(Color.WHITE), 0,     spanString.length(), 0); //fix the color to white
            item.setTitle(spanString);
        }

        return true;
    }
    //
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
            editor = sharedPreferences.edit();
            editor.putBoolean("saveLogin", false);
            editor.commit();
//                editor.clear();
//                editor.apply();
            Intent intent = new Intent(getApplicationContext(), LogInActivity.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.condition) {
            String url = "https://dheo.com/privacy";
            Intent intent = new Intent(getApplicationContext(), UserManualActivity.class);
            intent.putExtra("url", url);
            startActivity(intent);
            return true;
        }
        else if (item.getItemId() == R.id.settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }
        else if(item.getItemId()  == R.id.user_manual){
            String url = "https://rocket.dheo.com/user-manual";
            Intent intent = new Intent(getApplicationContext(), UserManualActivity.class);
            intent.putExtra("url", url);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    } //end//3 dot overflow menu
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        };


    }
}