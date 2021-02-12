package com.dheo.dheodeliverymerchantapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
    private Button back_graph_to_db;
    Helper helper = new Helper(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setElevation(0);//remove actionbar shadow
        setTitle("Performance Graph");
        client_id = helper.getClientId();
        setContentView(R.layout.activity_graph);
        growth_month_show = findViewById(R.id.growth_year);
        growth_per_month = findViewById(R.id.growth_per_month);
        final LineChart lineChart = findViewById(R.id.lineChart);
        final BarChart growth_bar_chart = findViewById(R.id.growth_bar_chart);
        graph_layout = findViewById(R.id.graph_layout);
        back_graph_to_db = findViewById(R.id.back_graph_to_db);

        growth_bar_chart.setNoDataText("");
        year_growth = new ArrayList<>();
        month_name = new ArrayList<>();
        month_name.add("");
        back_graph_to_db.setText("<Back");
        back_graph_to_db.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ClientDashboardActivity.class);
                startActivity(intent);
            }
        });


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
                        growth_month_show.setText("The growth graph of Month "+ per_day_growth.get(0).getMonth());
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
                        //set1.setValueTextColor(Color.GREEN);
                        //set1.setFillColor(ColorTemplate.getHoloBlue());
                        //set1.setHighLightColor(Color.rgb(244, 117, 117));
                        set1.setDrawCircleHole(true);
                        XAxis xAxis = lineChart.getXAxis();
                        //xAxis.setLabelCount(months.size());
                        //xAxis.setValueFormatter(new IndexAxisValueFormatter(months));
                        //xAxis.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);

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
                        growth_per_month.setText("The growth graph of year "+ s.get(0).getYear());
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
    }
}