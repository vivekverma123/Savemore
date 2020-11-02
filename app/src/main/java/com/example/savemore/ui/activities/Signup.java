package com.example.savemore.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.savemore.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Signup extends AppCompatActivity {

    Button b2;
    Context context;
    EditText e1,e2,e3,e4;

    private FirebaseAuth auth;
    private ProgressBar p1;
    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        setContentView(R.layout.activity_signup);

        init();
    }

    public void init()
    {

        context = Signup.this;

        auth = FirebaseAuth.getInstance();

        b2 = findViewById(R.id.create);

        e1 = findViewById(R.id.email);
        e2 = findViewById(R.id.password);
        e3 = findViewById(R.id.password);
        e4 = findViewById(R.id.confirm_password);

        p1 = findViewById(R.id.bar1);
        p1.setVisibility(View.GONE);

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                p1.setVisibility(View.VISIBLE);
                String email = e1.getText().toString();
                String pass1 = e2.getText().toString();
                String pass2 = e3.getText().toString();

                if (e1.length() == 0 || e2.length() == 0 || e3.length() == 0) {
                    Toast.makeText(context, "Please fill all the fields!", Toast.LENGTH_SHORT).show();
                    p1.setVisibility(View.GONE);

                } else {
                    if (e2.equals(e3) == false) {
                        Toast.makeText(context, "Passwords do not match!", Toast.LENGTH_SHORT).show();
                        p1.setVisibility(View.GONE);
                    } else {

                        auth.createUserWithEmailAndPassword(email, pass1)
                                .addOnCompleteListener(Signup.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {

                                            Toast.makeText(Signup.this,"Registration successful!",Toast.LENGTH_SHORT).show();

                                        } else {
                                            Toast.makeText(Signup.this,"Registration failed!!",Toast.LENGTH_SHORT).show();
                                        }
                                        p1.setVisibility(View.GONE);
                                        onBackPressed();
                                    }
                                });
                    }
                }
            }
        });

    }
}