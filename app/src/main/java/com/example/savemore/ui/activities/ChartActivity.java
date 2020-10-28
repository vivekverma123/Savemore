package com.example.savemore.ui.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.example.model.Account;
import com.example.model.Transaction;
import com.example.savemore.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ChartActivity extends AppCompatActivity {

    ArrayList<Transaction> transactions;
    Account account;
    EditText from;
    EditText to;
    final Calendar myCalendar = Calendar.getInstance();





    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        Intent intent = getIntent();
        transactions = (ArrayList<Transaction>)intent.getSerializableExtra("Transactions");
        account = (Account)intent.getSerializableExtra("Account");


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

        PieChart pieChart = findViewById(R.id.pie_chart_view);
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
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.GRAY);

        Description description = pieChart.getDescription();
        description.setText("Expenses Breakdown");
        description.setTextColor(Color.GRAY);
        description.setTextSize(11f);

        Legend legend = pieChart.getLegend();
        legend.setTextColor(Color.GRAY);
        legend.setWordWrapEnabled(true);


        pieChart.setData(data);
        pieChart.invalidate();



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

        pieChart.setVisibility(View.GONE);

        BarChart barChart = findViewById(R.id.bar_chart_view);
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

        barDataSet1.setColor(Color.GREEN);
        barDataSet2.setColor(Color.RED);

        float groupSpace = 0.06f;
        float barSpace = 0.02f;
        float barWidth = 0.45f;

        XAxis xAxis = barChart.getXAxis();
        xAxis.setAxisMinimum(0);
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        //xAxis.setLabelRotationAngle(120f);

        BarData barData = new BarData(barDataSet1,barDataSet2);
        barData.setBarWidth(0.4f); // set the width of each bar
        barChart.setData(barData);
        barChart.groupBars(0.5f, 0.2f, 0); // perform the "explicit" grouping
        barChart.setDrawValueAboveBar(true);
        barChart.setVisibleXRangeMaximum(6);
        barChart.invalidate(); // refresh

        from = findViewById(R.id.from);
        to = findViewById(R.id.to);
        from.setTextIsSelectable(true);
        to.setTextIsSelectable(true);
        from.setShowSoftInputOnFocus(false);
        to.setShowSoftInputOnFocus(false);

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
        String myFormat = "dd/MM/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);

        to.setText(sdf.format(myCalendar.getTime()));
    }



}