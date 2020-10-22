package com.example.savemore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.transition.Visibility;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Signup extends AppCompatActivity {

    Button b1,b2;
    Context context;
    EditText e1,e2,e3,e4;

    private FirebaseAuth auth;
    private ProgressBar p1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        init();
    }

    public void init()
    {

        context = Signup.this;

        auth = FirebaseAuth.getInstance();

        b1 = findViewById(R.id.verify);
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
                } else {
                    if (e2.equals(e3) == false) {
                        Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    } else {

                        auth.createUserWithEmailAndPassword(email, pass1)
                                .addOnCompleteListener(Signup.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {

                                            Toast.makeText(Signup.this,"Registration successful!",Toast.LENGTH_SHORT).show();
                                            p1.setVisibility(View.GONE);
                                            onBackPressed();

                                        } else {
                                            Toast.makeText(Signup.this,"Registration failed!!",Toast.LENGTH_SHORT).show();
                                            p1.setVisibility(View.GONE);
                                            onBackPressed();
                                        }
                                    }
                                });

                    }
                }
            }
        });

    }
}