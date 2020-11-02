package com.example.savemore.ui.UserAdapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.model.Account;
import com.example.model.ProfileInfo;
import com.example.model.Transaction;
import com.example.savemore.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class TransactionInfoAdapter extends ArrayAdapter<Transaction>
{

    Context context;
    public TransactionInfoAdapter(@NonNull Context context, ArrayList<Transaction> transactions)
    {
        super(context, 0,transactions);
        this.context = context;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Transaction transaction = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.transaction_item, parent, false);
        }

        TextView t1 = convertView.findViewById(R.id.textView7);
        TextView t2 = convertView.findViewById(R.id.textView8);
        TextView t3 = convertView.findViewById(R.id.textView9);
        TextView t4 = convertView.findViewById(R.id.textView10);

        SimpleDateFormat sdf =  new SimpleDateFormat("dd/MM/yy");
        String date = sdf.format(transaction.giveDate());

        t1.setText(date);
        t2.setText(transaction.getParticular());
        t3.setText(transaction.getAmount() + "");
        t4.setText(transaction.getBal() + "");

        if(transaction.getType()==0) {
            t1.setTextColor(Color.parseColor("#003d00"));
            t2.setTextColor(Color.parseColor("#003d00"));
            t3.setTextColor(Color.parseColor("#003d00"));
            t4.setTextColor(Color.parseColor("#003d00"));
        }
        else
        {
            t1.setTextColor(Color.parseColor("#8e0000"));
            t2.setTextColor(Color.parseColor("#8e0000"));
            t3.setTextColor(Color.parseColor("#8e0000"));
            t4.setTextColor(Color.parseColor("#8e0000"));
        }

        // Return the completed view to render on screen
        return convertView;
    }
}
