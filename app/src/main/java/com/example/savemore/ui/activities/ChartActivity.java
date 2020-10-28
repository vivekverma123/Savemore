package com.example.savemore.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import com.example.model.Account;
import com.example.model.Transaction;
import com.example.savemore.R;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ChartActivity extends AppCompatActivity {

    ArrayList<Transaction> transactions;
    Account account;
    TreeMap<String,Integer> mp1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        Intent intent = getIntent();
        transactions = (ArrayList<Transaction>)intent.getSerializableExtra("Transactions");
        account = (Account)intent.getSerializableExtra("Account");

        mp1 = new TreeMap<>();

        for(Transaction transaction : transactions)
        {
            if(transaction.getType()==1) {
                if(mp1.get(transaction.getCategory())==null)
                {
                    mp1.put(transaction.getCategory(),0);
                }
                mp1.put(transaction.getCategory(), mp1.get(transaction.getCategory()) + transaction.getAmount());
            }
        }


        List<PieEntry> list = new ArrayList<>();

        Iterator iterator = mp1.entrySet().iterator();

        while(iterator.hasNext())
        {
            Map.Entry element = (Map.Entry)iterator.next();
            list.add(new PieEntry((Integer)element.getValue(),(String)element.getKey()));
        }

        PieChart pieChart = findViewById(R.id.pie_chart);


        PieDataSet pieDataSet = new PieDataSet(list,"Expenses Breakdown");

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

        pieChart.setData(data);
        pieChart.invalidate();

    }
}