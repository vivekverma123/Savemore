package com.example.savemore.ui.DialogBoxes;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.savemore.R;

public class CreateAccountDialog extends Dialog implements android.view.View.OnClickListener
{

    public Activity c;
    public Dialog d;
    public Button SaveButton, CancelButton;

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
    }

    public void onClick(View v)
    {
            switch (v.getId())
            {
                case R.id.save:

                    EditText e1 = findViewById(R.id.account_name1);
                    Toast.makeText(c,"Account with name " + e1.getText().toString() + "will be created",Toast.LENGTH_SHORT).show();
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

