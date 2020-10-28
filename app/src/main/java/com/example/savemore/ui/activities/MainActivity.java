package com.example.savemore.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.model.ProfileInfo;
import com.example.savemore.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    Button b1,b2;
    EditText e1,e2;
    Context context;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            init();
        }
        catch(Exception e1)
        {
            Toast.makeText(MainActivity.this,e1.toString(),Toast.LENGTH_SHORT).show();
        }

        System.setProperty("org.apache.poi.javax.xml.stream.XMLInputFactory", "com.fasterxml.aalto.stax.InputFactoryImpl");
        System.setProperty("org.apache.poi.javax.xml.stream.XMLOutputFactory", "com.fasterxml.aalto.stax.OutputFactoryImpl");
        System.setProperty("org.apache.poi.javax.xml.stream.XMLEventFactory", "com.fasterxml.aalto.stax.EventFactoryImpl");


    }

    public void init()
    {

        ProgressBar b = findViewById(R.id.bar2);
        b.setVisibility(View.GONE);

        context = MainActivity.this;

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        if(user!=null)
        {
            ProfileInfo.setFirebaseUser(user);
            startActivity(new Intent(context,DashboardActivity.class));
        }

        b1 = findViewById(R.id.login);
        b2 = findViewById(R.id.signup);
        e1 = findViewById(R.id.email);
        e2 = findViewById(R.id.password);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                b.setVisibility(View.VISIBLE);

                String email = e1.getText().toString();
                String pass = e2.getText().toString();

                if(email.length()==0 || pass.length()==0)
                {
                    Toast.makeText(context, "Please fill all the fields!", Toast.LENGTH_SHORT).show();
                    b.setVisibility(View.GONE);
                }
                else
                {
                    auth.signInWithEmailAndPassword(email, pass)
                            .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseUser user = auth.getCurrentUser();
                                        ProfileInfo.setFirebaseUser(user);
                                        Toast.makeText(MainActivity.this,"Login successful!",Toast.LENGTH_SHORT).show();
                                        b.setVisibility(View.GONE);
                                        startActivity(new Intent(context,DashboardActivity.class));

                                    } else {

                                        Toast.makeText(MainActivity.this,"Login failed!",Toast.LENGTH_SHORT).show();
                                        b.setVisibility(View.GONE);
                                    }


                                }
                            });
                }

            }
        });


        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context,Signup.class));
            }
        });
    }
}