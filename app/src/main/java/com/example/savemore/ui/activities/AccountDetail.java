package com.example.savemore.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
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
import com.example.savemore.ui.DialogBoxes.CreateAccountDialog;
import com.example.savemore.ui.DialogBoxes.CreateTransactionDialog;
import com.example.savemore.ui.UserAdapters.TransactionInfoAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class AccountDetail extends AppCompatActivity {


    Account account;
    ListView listView;
    FloatingActionButton floatingActionButton,floatingActionButton2;
    TextView textView,textView2;
    DatabaseReference databaseReference;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_detail);

        Intent intent = getIntent();
        account = (Account)intent.getSerializableExtra("Account");

        init();

        textView2 = findViewById(R.id.textView5);

        String text = "<font color=#33691e>Debit / </font> <font color=#7f0000>Credit</font>";

        textView2.setText(Html.fromHtml(text));
        ListView listView = findViewById(R.id.transactions);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                PopupMenu popupMenu = new PopupMenu(AccountDetail.this,view);
                popupMenu.getMenuInflater().inflate(R.menu.transactmenu, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        if(item.getItemId()==R.id.edit)
                        {
                            Toast.makeText(AccountDetail.this,"Edit service will start shortly",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(AccountDetail.this,"Delete service will start shortly",Toast.LENGTH_SHORT).show();
                        }
                        return true;
                    }
                });

                popupMenu.show();

            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference(ProfileInfo.firebaseUser.getUid()).child("Transactions").child(account.getId());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int balance = 0;

                ArrayList <Transaction> arrayList = new ArrayList<>();
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
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
                        balance += t1.getAmount();
                    }
                    else
                    {
                        balance -= t1.getAmount();
                    }
                    t1.setBal(balance);
                    arrayList1.add(t1);
                }

                TransactionInfoAdapter transactionInfoAdapter = new TransactionInfoAdapter(AccountDetail.this,arrayList1);
                listView.setAdapter(transactionInfoAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void init()
    {

        textView = findViewById(R.id.textView11);
        textView.setText(account.getName());

        FloatingActionButton floatingActionButton = findViewById(R.id.transact);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CreateTransactionDialog cdd=new CreateTransactionDialog(AccountDetail.this,account);
                cdd.show();
                //Toast.makeText(AccountDetail.this,"Services are going to start shortly!!!",Toast.LENGTH_SHORT).show();
            }
        });

        FloatingActionButton floatingActionButton1 = findViewById(R.id.addcategory);
        floatingActionButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    CategoryDialog categoryDialog = new CategoryDialog(AccountDetail.this, account);
                    categoryDialog.show();
                }
                catch(Exception e1)
                {
                    Toast.makeText(AccountDetail.this,e1.toString(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}