package com.example.savemore.ui.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.model.Account;
import com.example.model.Transaction;
import com.example.savemore.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.TreeMap;

public class ChartActivity extends AppCompatActivity {

    ArrayList<Transaction> transactions;
    Account account;
    EditText from;
    EditText to;
    final Calendar myCalendar = Calendar.getInstance();
    private PieChart pieChart;
    private BarChart barChart;
    private TextInputLayout t1;
    private TextInputLayout t2;
    private Button b1;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        setContentView(R.layout.activity_chart);




        t1 = findViewById(R.id.form_input);
        t2 = findViewById(R.id.to_layout);

        Intent intent = getIntent();
        transactions = (ArrayList<Transaction>)intent.getSerializableExtra("Transactions");
        account = (Account)intent.getSerializableExtra("Account");

        pieChart = findViewById(R.id.pie_chart_view);
        pieChart.setVisibility(View.GONE);

        barChart = findViewById(R.id.bar_chart_view);
        barChart.setVisibility(View.GONE);

        RadioGroup radioGroup = findViewById(R.id.chart_group);

        b1 = findViewById(R.id.plot);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                sdf.setTimeZone(TimeZone.getTimeZone("Asia/Calcutta"));
                Date d1,d2;
                d1 = new Date();
                d2 = new Date();
                try {
                    d1 = sdf.parse(from.getText().toString());
                    d2 = sdf.parse(to.getText().toString());
                }
                catch(Exception e1)
                {

                }

                ArrayList <Transaction> transactions1 = new ArrayList<>();
                for(Transaction transaction : transactions)
                {
                    //Toast.makeText(ChartActivity.this,d1.toString() +" " +transaction.giveDate().toString() + " " + d2.toString(),Toast.LENGTH_SHORT).show();
                    //Log.d("Data",d1.toString() + " " + transaction.giveDate().toString() + " " + d2.toString());
                    if(transaction.getTime() >= d1.getTime() && transaction.getTime()<=d2.getTime())
                    {
                        transactions1.add(transaction);
                    }
                }

                int id = radioGroup.getCheckedRadioButtonId();
                switch(id)
                {
                    case R.id.piechart:
                        plotPieChart(transactions1);
                        break;

                    case R.id.barchart:
                        plotBarChart(transactions1);
                        break;

                    default:

                }

            }
        });

        from = findViewById(R.id.from);
        to = findViewById(R.id.to);
        from.setTextIsSelectable(true);
        to.setTextIsSelectable(true);
        from.setShowSoftInputOnFocus(false);
        to.setShowSoftInputOnFocus(false);

        myCalendar.setTimeZone(TimeZone.getTimeZone("Asia/Calcutta"));

        DatePickerDialog.OnDateSetListener date1 = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel1();
            }

        };

        DatePickerDialog.OnDateSetListener date2 = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel2();
            }

        };

        from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(ChartActivity.this, date1, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(ChartActivity.this, date2, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void updateLabel1() {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);

        from.setText(sdf.format(myCalendar.getTime()));
    }

    private void updateLabel2() {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);

        to.setText(sdf.format(myCalendar.getTime()));
    }

    void plotPieChart(ArrayList <Transaction> transactions)
    {
        TreeMap<String,Integer> mp1 = new TreeMap<>();

        int total = 0;

        for(Transaction transaction : transactions)
        {
            if(transaction.getType()==1) {
                if(mp1.get(transaction.getCategory())==null)
                {
                    mp1.put(transaction.getCategory(),0);
                }
                mp1.put(transaction.getCategory(), mp1.get(transaction.getCategory()) + transaction.getAmount());
                total += transaction.getAmount();
            }
        }


        List<PieEntry> list = new ArrayList<>();

        Iterator iterator = mp1.entrySet().iterator();

        while(iterator.hasNext())
        {
            Map.Entry element = (Map.Entry)iterator.next();

            int value = (Integer) element.getValue();
            double percent = ((double)value / total) * 100;

            list.add(new PieEntry((Integer)element.getValue(),((String)element.getKey()) + "(" +  String.format("%.2f",percent) + "%)"));
        }


        PieDataSet pieDataSet = new PieDataSet(list,"Expense Type");

        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());
        pieDataSet.setColors(colors);

        PieData data = new PieData(pieDataSet);

        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.GRAY);


        Description description = pieChart.getDescription();
        description.setText("Expenses Breakdown");
        description.setTextColor(Color.GRAY);
        description.setTextSize(14f);

        Legend legend = pieChart.getLegend();
        legend.setTextColor(Color.GRAY);
        legend.setWordWrapEnabled(true);

        pieChart.setData(data);
        pieChart.invalidate();

        pieChart.setVisibility(View.VISIBLE);
        barChart.setVisibility((View.GONE));

    }

    void plotBarChart(ArrayList <Transaction> transactions)
    {
        TreeMap <String,Integer> mp2,mp3;

        mp2 = new TreeMap <> ();
        mp3 = new TreeMap <> ();

        SimpleDateFormat sdf = new SimpleDateFormat("MM/YYYY");
        for(Transaction transaction : transactions)
        {
            String date = sdf.format(transaction.giveDate());
            if(mp2.get(date)==null)
            {
                mp2.put(date,0);
            }
            if(mp3.get(date)==null)
            {
                mp3.put(date,0);
            }
            if(transaction.getType()==0)
            {
                mp2.put(date,mp2.get(date) + transaction.getAmount());
            }
            else
            {
                mp3.put(date,mp3.get(date) + transaction.getAmount());
            }
        }

        List <BarEntry> debit = new ArrayList<>();
        List <BarEntry> credit = new ArrayList<>();

        ArrayList<String> labels = new ArrayList<>();
        labels.add("");

        Iterator iterator1 = mp2.entrySet().iterator();
        int i = 0;
        while(iterator1.hasNext())
        {
            i += 1;
            Map.Entry entry = (Map.Entry) iterator1.next();
            debit.add(new BarEntry(i, (Integer)entry.getValue()));
            labels.add((String)entry.getKey());
        }

        iterator1 = mp3.entrySet().iterator();
        i = 0;
        while(iterator1.hasNext())
        {
            i += 1;
            Map.Entry entry = (Map.Entry) iterator1.next();
            credit.add(new BarEntry(i, (Integer)entry.getValue()));
        }

        BarDataSet barDataSet1 = new BarDataSet(debit,"Earning");
        BarDataSet barDataSet2 = new BarDataSet(credit,"Expenditure");

        barDataSet2.setColor(Color.parseColor("#ffa4a2"));
        barDataSet1.setColor(Color.parseColor("#b2fab4"));

        barDataSet1.setValueTextColor(Color.GRAY);
        barDataSet2.setValueTextColor(Color.GRAY);
        barDataSet1.setValueTextSize(12f);
        barDataSet2.setValueTextSize(12f);

        float groupSpace = 0.06f;
        float barSpace = 0.02f;
        float barWidth = 0.45f;

        Legend legend = barChart.getLegend();
        legend.setTextColor(Color.GRAY);
        legend.setTextSize(12f);

        Description description = barChart.getDescription();
        description.setTextSize(14f);
        description.setTextSize(12f);
        description.setText("Monthly Income and Expenditure");
        description.setPosition(3f,3f);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setAxisMinimum(0);
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setTextColor(Color.GRAY);
        xAxis.setTextSize(10f);
        //xAxis.setLabelRotationAngle(120f);

        YAxis yAxis = barChart.getAxisLeft();
        yAxis.setTextColor(Color.GRAY);
        yAxis.setTextSize(10f);
        yAxis.setAxisMinimum(0);
        yAxis.setGranularity(1f);
        yAxis.setGranularityEnabled(true);

        YAxis yAxis1 = barChart.getAxisRight();
        yAxis1.setTextColor(Color.GRAY);
        yAxis1.setTextSize(10f);
        yAxis1.setAxisMinimum(0);
        yAxis1.setGranularity(1f);
        yAxis1.setGranularityEnabled(true);


        BarData barData = new BarData(barDataSet1,barDataSet2);
        barData.setBarWidth(0.4f); // set the width of each bar
        barChart.setData(barData);
        barChart.groupBars(0.5f, 0.2f, 0); // perform the "explicit" grouping
        barChart.setDrawValueAboveBar(true);
        barChart.setVisibleXRange(0, 4);
        barChart.invalidate(); // refresh
        barChart.getAxisRight().setDrawLabels(false);

        pieChart.setVisibility(View.GONE);
        barChart.setVisibility((View.VISIBLE));
    }


}