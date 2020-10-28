package com.example.savemore.ui.DialogBoxes;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.model.Account;
import com.example.model.ProfileInfo;
import com.example.model.Transaction;
import com.example.savemore.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class CreateTransactionDialog extends Dialog implements android.view.View.OnClickListener
{

    public Activity c;
    public Dialog d;
    public Button SaveButton, CancelButton;
    public DatabaseReference databaseReference;
    public Account account;
    public Transaction transaction;
    public int x;
    DatePicker datePicker;
    RadioGroup radioGroup;
    EditText e1;
    EditText e2;
    public TreeMap<String,Integer> mp1;
    String selectedCategory;

    public CreateTransactionDialog(Activity a, Account account, TreeMap <String,Integer> mp1) {
        super(a);
        this.c = a;
        this.account = account;
        x = 0;
        this.mp1 = mp1;

        if(mp1==null)
        {
            Toast.makeText(c,"Empty",Toast.LENGTH_SHORT).show();
        }
    }

    public CreateTransactionDialog(Activity a, Account account, Transaction transaction, TreeMap <String,Integer> mp1) {
        super(a);
        this.c = a;
        this.account = account;
        this.transaction = transaction;
        x = 1;
        this.mp1 = mp1;
    }

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.create_transaction);
        SaveButton = (Button) findViewById(R.id.save1);
        CancelButton = (Button) findViewById(R.id.cancel1);
        SaveButton.setOnClickListener(this);
        CancelButton.setOnClickListener(this);
        databaseReference = FirebaseDatabase.getInstance().getReference(ProfileInfo.firebaseUser.getUid()).child("Transactions").child(account.getId());

        datePicker = findViewById(R.id.datePicker1);
        radioGroup = findViewById(R.id.transaction_type);
        e1 = findViewById(R.id.amount);
        e2 = findViewById(R.id.particular);
        Spinner spinner = findViewById(R.id.categories2);

        Iterator iterator = mp1.entrySet().iterator();
        ArrayList <String> arrayList = new ArrayList<>();
        while(iterator.hasNext())
        {
            Map.Entry element = (Map.Entry)iterator.next();
            arrayList.add(element.getKey().toString());
        }

        ArrayAdapter arrayAdapter = new ArrayAdapter(c,R.layout.support_simple_spinner_dropdown_item,arrayList);
        spinner.setAdapter(arrayAdapter);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCategory = (String)parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if(x==1)
        {
            String day,month,year;
            SimpleDateFormat sdf = new SimpleDateFormat("dd");
            day = sdf.format(transaction.giveDate());
            sdf = new SimpleDateFormat("MM");
            month = sdf.format(transaction.giveDate());
            sdf = new SimpleDateFormat("yyyy");
            year = sdf.format(transaction.giveDate());

            datePicker.updateDate(Integer.parseInt(year),Integer.parseInt(month)-1,Integer.parseInt(day));
            if(transaction.getType()==0)
            {
                radioGroup.check(R.id.debit);
            }
            else
            {
                radioGroup.check(R.id.credit);
            }

            e1.setText(Integer.toString(transaction.getAmount()));
            e2.setText(transaction.getParticular());

            if(mp1.get(transaction.getCategory())!=null) {
                spinner.setSelection(mp1.get(transaction.getCategory()));
            }

        }

    }

    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.save1:
                //Toast.makeText(c,"Services will start shortly!",Toast.LENGTH_SHORT).show();
                init();
                break;

            case R.id.cancel1:
                dismiss();
                break;

            default:
                break;
        }
        dismiss();
    }

    public void init()
    {
        String date = datePicker.getDayOfMonth() + "/" + (datePicker.getMonth() + 1) + "/" + datePicker.getYear();
        SimpleDateFormat sdf =  new SimpleDateFormat("dd/MM/yyyy");
        Date d1 = new Date();
        try {
            d1 = sdf.parse(date);
        }
        catch(Exception e)
        {

        }


        int selectedId = radioGroup.getCheckedRadioButtonId();
        int type = 0;
        switch(selectedId)
        {
            case R.id.debit:
                type = 0;
                break;

            case R.id.credit:
                type = 1;
                break;

            default:
                type = -1;
        }



        if(e1.getText().toString().equals("") || e2.getText().toString().equals("") || type == -1)
        {
            Toast.makeText(c,"Please choose fill all the fields",Toast.LENGTH_SHORT).show();
        }
        else
        {
            try
            {
                int amount = Integer.parseInt(e1.getText().toString());
                String id;
                if(x==0)
                {
                    id = databaseReference.push().getKey();
                }
                else
                {
                    id = transaction.getId();
                }
                if(type==0)
                {
                    selectedCategory = "Not Applicable";
                }
                Transaction transaction = new Transaction(d1.getTime(),type,amount,0,selectedCategory,e2.getText().toString(),id);
                databaseReference.child(transaction.getId()).setValue(transaction);

            }
            catch(Exception e)
            {
                Toast.makeText(c,"Invalid Amount",Toast.LENGTH_SHORT).show();
            }
        }
    }

}

