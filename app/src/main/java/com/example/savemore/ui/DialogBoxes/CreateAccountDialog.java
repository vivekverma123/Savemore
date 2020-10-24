package com.example.savemore.ui.DialogBoxes;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.model.Account;
import com.example.model.ProfileInfo;
import com.example.savemore.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateAccountDialog extends Dialog implements android.view.View.OnClickListener
{

    public Activity c;
    public Dialog d;
    public Button SaveButton, CancelButton;
    public DatabaseReference databaseReference;

    public CreateAccountDialog(Activity a) {
            super(a);
            this.c = a;
    }

    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.create_account);
            SaveButton = (Button) findViewById(R.id.save);
            CancelButton = (Button) findViewById(R.id.cancel);
            SaveButton.setOnClickListener(this);
            CancelButton.setOnClickListener(this);
            databaseReference = FirebaseDatabase.getInstance().getReference(ProfileInfo.firebaseUser.getUid());
    }

    public void onClick(View v)
    {
            switch (v.getId())
            {
                case R.id.save:
                    EditText e1 = findViewById(R.id.account_name1);
                    //Toast.makeText(c,"Account with name " + e1.getText().toString() + "will be created",Toast.LENGTH_SHORT).show();

                    String id = databaseReference.child("Accounts").push().getKey();
                    Account account = new Account(e1.getText().toString(),id,0,0,0);
                    databaseReference.child("Accounts").child(id).setValue(account);
                    Toast.makeText(c,"Account added successfully",Toast.LENGTH_SHORT).show();
                    break;

                case R.id.cancel:
                    dismiss();
                    break;

                default:
                        break;
            }
            dismiss();
    }
}

