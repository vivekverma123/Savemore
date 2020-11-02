package com.example.savemore.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.model.Account;
import com.example.model.ProfileInfo;
import com.example.model.Transaction;
import com.example.savemore.R;
import com.example.savemore.ui.DialogBoxes.CategoryDialog;
import com.example.savemore.ui.DialogBoxes.CreateTransactionDialog;
import com.example.savemore.ui.UserAdapters.TransactionInfoAdapter;
import com.example.utilities.RealPathUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.TreeMap;

public class AccountDetail extends AppCompatActivity {



    Account account;
    ListView listView;
    FloatingActionButton floatingActionButton,floatingActionButton2;
    TextView textView,textView2;
    DatabaseReference databaseReference;
    private ValueEventListener listener;
    private TreeMap <String,Integer> mp1;
    private ArrayList <Transaction> arrayList;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        setContentView(R.layout.activity_account_detail);



    }

    public void onResume() {

        super.onResume();
        Intent intent = getIntent();
        account = (Account)intent.getSerializableExtra("Account");
        databaseReference = FirebaseDatabase.getInstance().getReference(ProfileInfo.firebaseUser.getUid());

        init();

        textView2 = findViewById(R.id.textView5);

        String text = "<font color=#33691e>Debit / </font> <font color=#7f0000>Credit</font>";

        textView2.setText(Html.fromHtml(text));
        ListView listView = findViewById(R.id.transactions);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Transaction transaction = (Transaction) parent.getItemAtPosition(position);

                PopupMenu popupMenu = new PopupMenu(AccountDetail.this,view);
                popupMenu.getMenuInflater().inflate(R.menu.transactmenu, popupMenu.getMenu());



                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        if(item.getItemId()==R.id.edit)
                        {
                            //Toast.makeText(AccountDetail.this,"Edit service will start shortly",Toast.LENGTH_SHORT).show();
                            CreateTransactionDialog createTransactionDialog = new CreateTransactionDialog(AccountDetail.this,account,transaction,mp1);
                            createTransactionDialog.show();
                        }
                        else
                        {
                            //Toast.makeText(AccountDetail.this,"Delete service will start shortly",Toast.LENGTH_SHORT).show();
                            databaseReference.child("Transactions").child(account.getId()).child(transaction.getId()).setValue(null);
                        }
                        return true;
                    }
                });

                popupMenu.show();

            }
        });

        listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int balance = 0,debit = 0;

                mp1 = new TreeMap<>();
                int index = 0;
                for(DataSnapshot dataSnapshot : snapshot.child("Categories").child(account.getId()).getChildren())
                {
                    mp1.put(dataSnapshot.getValue(String.class),index);
                    index += 1;
                }
                setMp1(mp1);

                ArrayList <Transaction> arrayList = new ArrayList<>();
                for(DataSnapshot dataSnapshot : snapshot.child("Transactions").child(account.getId()).getChildren())
                {
                    Transaction transaction = dataSnapshot.getValue(Transaction.class);
                    arrayList.add(transaction);
                }

                Collections.sort(arrayList, new Comparator<Transaction>() {
                    @Override
                    public int compare(Transaction o1, Transaction o2) {

                        if(o1.getTime()<o2.getTime())
                        {
                            return -1;
                        }
                        else if(o1.getTime()==o2.getTime())
                        {
                            return 0;
                        }
                        else
                        {
                            return 1;
                        }
                    }
                });

                ArrayList <Transaction> arrayList1 = new ArrayList<>();

                for(Transaction t1 : arrayList)
                {
                    if(t1.getType()==0)
                    {
                        debit += t1.getAmount();
                        balance += t1.getAmount();
                    }
                    else
                    {
                        balance -= t1.getAmount();
                    }
                    t1.setBal(balance);
                    arrayList1.add(t1);
                }

                setArrayList(arrayList1);

                account.setBalance(balance);
                account.setDebit(debit);
                account.setDebit(debit-balance);
                DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference(ProfileInfo.firebaseUser.getUid()).child("Accounts").child(account.getId());
                databaseReference1.setValue(account);


                int x = 0,temp1 = 0;
                if(listView.getAdapter()==null) {

                }
                else
                {
                    x = listView.getFirstVisiblePosition();
                    temp1 = listView.getAdapter().getCount();
                }

                TransactionInfoAdapter transactionInfoAdapter = new TransactionInfoAdapter(AccountDetail.this, arrayList1);
                listView.setAdapter(transactionInfoAdapter);

                if(listView.getAdapter()==null) {

                }
                else
                {
                    if(temp1<arrayList1.size()) {
                        listView.setSelection(x+1);
                    }
                    else if(temp1>arrayList1.size())
                    {
                        listView.setSelection(Math.max(x-1,0));
                    }
                    else
                    {
                        listView.setSelection(x);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        databaseReference.addValueEventListener(listener);
    }

    public void init()
    {

        textView = findViewById(R.id.textView11);
        textView.setText(account.getName());

        FloatingActionButton floatingActionButton = findViewById(R.id.transact);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mp1.size()==0)
                {
                    Toast.makeText(AccountDetail.this,"You must add one or more transaction categories to proceed",Toast.LENGTH_SHORT).show();
                }
                else {
                    CreateTransactionDialog cdd = new CreateTransactionDialog(AccountDetail.this, account, mp1);
                    cdd.show();
                    //Toast.makeText(AccountDetail.this,"Services are going to start shortly!!!",Toast.LENGTH_SHORT).show();
                }
            }
        });

        FloatingActionButton floatingActionButton1 = findViewById(R.id.addcategory);
        floatingActionButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    CategoryDialog categoryDialog = new CategoryDialog(AccountDetail.this, account,mp1);
                    categoryDialog.show();
                }
                catch(Exception e1)
                {
                    Toast.makeText(AccountDetail.this,e1.toString(),Toast.LENGTH_SHORT).show();
                }
            }
        });

        FloatingActionButton floatingActionButton3 = findViewById(R.id.charts);
        floatingActionButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try{
                Intent intent = new Intent(AccountDetail.this,ChartActivity.class);
                intent.putExtra("Account",account);
                intent.putExtra("Transactions",arrayList);
                startActivity(intent);
                }
                catch(Exception e1)
                {
                    Toast.makeText(AccountDetail.this,e1.toString(),Toast.LENGTH_SHORT).show();
                }
            }
        });

        FloatingActionButton floatingActionButton4 = findViewById(R.id.excelfile);
        floatingActionButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AccountDetail.this,ImportAccount.class);
                intent.putExtra("Account",account);
                startActivity(intent);
            }
        });

        FloatingActionButton floatingActionButton5 = findViewById(R.id.export);
        floatingActionButton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try{
                    Intent intent = new Intent(AccountDetail.this,ExportDocument.class);
                    intent.putExtra("Account",account);
                    intent.putExtra("Transactions",arrayList);
                    startActivity(intent);
                }
                catch(Exception e1)
                {
                    Toast.makeText(AccountDetail.this,e1.toString(),Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void onPause() {
        super.onPause();
        databaseReference.removeEventListener(listener);
    }

    public void onDestroy() {
        super.onDestroy();
        databaseReference.removeEventListener(listener);
    }

    public void setArrayList(ArrayList<Transaction> arrayList) {
        this.arrayList = arrayList;
    }

    public void setMp1(TreeMap<String, Integer> mp1) {
        this.mp1 = mp1;
    }

    // Request code for selecting a PDF document.
    private static final int PICK_PDF_FILE = 2;



}
