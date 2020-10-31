package com.example.savemore.ui.UserAdapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.model.Account;
import com.example.model.ProfileInfo;
import com.example.savemore.R;
import com.example.savemore.ui.activities.AccountDetail;
import com.example.savemore.ui.activities.Signup;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

                new AlertDialog.Builder(context)
                        .setTitle("Delete Account: This action can't be undone!")
                        .setMessage("Are you sure you want to delete this account?")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                DatabaseReference d1 = FirebaseDatabase.getInstance().getReference(ProfileInfo.firebaseUser.getUid());
                                d1.child("Categories").child(account.getId()).setValue(null);
                                d1.child("Transactions").child(account.getId()).setValue(null);
                                d1.child("Accounts").child(account.getId()).setValue(null);
                                Toast.makeText(context,"Account deleted successfully!",Toast.LENGTH_SHORT).show();
                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

            }
        });

        Button b2 = convertView.findViewById(R.id.edit);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Enter the new title");

                final EditText input = new EditText(context);
                input.setText(account.getName());

                builder.setView(input);

                builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String title = input.getText().toString();
                        account.setName(title);

                        DatabaseReference d1 = FirebaseDatabase.getInstance().getReference(ProfileInfo.firebaseUser.getUid());
                        d1.child("Accounts").child(account.getId()).setValue(account);
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();

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
