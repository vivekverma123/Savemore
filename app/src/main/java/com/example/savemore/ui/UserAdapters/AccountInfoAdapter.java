package com.example.savemore.ui.UserAdapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.model.Account;
import com.example.model.ProfileInfo;
import com.example.savemore.R;
import com.example.savemore.ui.activities.AccountDetail;
import com.example.savemore.ui.activities.Signup;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class AccountInfoAdapter extends ArrayAdapter<Account>
{

    Context context;
    public AccountInfoAdapter(@NonNull Context context, ArrayList<Account> account)
    {
        super(context, 0,account);
        this.context = context;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Account account = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.account_info, parent, false);
        }

        TextView t1 = convertView.findViewById(R.id.acc_name);
        TextView t2 = convertView.findViewById(R.id.balance);

        Button b1 = convertView.findViewById(R.id.delete);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference d1 = FirebaseDatabase.getInstance().getReference(ProfileInfo.firebaseUser.getUid());
                d1.child("Categories").child(account.getId()).setValue(null);
                d1.child("Transactions").child(account.getId()).setValue(null);
                d1.child("Accounts").child(account.getId()).setValue(null);
                Toast.makeText(context,"Account deleted successfully!",Toast.LENGTH_SHORT).show();
            }
        });

        Button b3 = convertView.findViewById(R.id.see);
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AccountDetail.class);
                intent.putExtra("Account",account);
                //Toast.makeText(context,"Clicked",Toast.LENGTH_SHORT).show();
                context.startActivity(intent);
            }
        });

        t1.setText(account.getName());
        t2.setText("₹" + account.getBalance());

        /*if(position%2==0) {
            convertView.setBackgroundColor(Color.parseColor("#f48fb1"));
        }
        else
        {
            convertView.setBackgroundColor(Color.WHITE);
        }*/

        // Return the completed view to render on screen
        return convertView;
    }

}
