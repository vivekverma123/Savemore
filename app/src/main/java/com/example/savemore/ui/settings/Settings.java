package com.example.savemore.ui.settings;

import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.model.ProfileInfo;
import com.example.savemore.R;
import com.example.savemore.ui.activities.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class Settings extends Fragment {

    private TextView t1,t2,t3,t4;
    private EditText e1,e2;
    private com.google.android.material.textfield.TextInputLayout ti1,ti2;
    private FrameLayout frameLayout;
    private FirebaseUser firebaseUser;
    private Button button;
    private SettingsViewModel mViewModel;
    private String password;

    public static Settings newInstance() {
        return new Settings();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)


    {
        View view =  inflater.inflate(R.layout.settings_fragment, container, false);

        firebaseUser = ProfileInfo.firebaseUser;

        button = view.findViewById(R.id.button);

        t1 = view.findViewById(R.id.view1);
        t2 = view.findViewById(R.id.view2);

        t3 = view.findViewById(R.id.textView16);
        t4 = view.findViewById(R.id.textView20);

        if(firebaseUser.isEmailVerified())
        {
            t3.setVisibility(View.GONE);
            t4.setVisibility(View.VISIBLE);
            button.setVisibility(View.GONE);
        }
        else
        {
            t4.setVisibility(View.GONE);
            t3.setVisibility(View.VISIBLE);
            button.setVisibility(View.VISIBLE);
        }

        t1.setText(firebaseUser.getDisplayName());
        t2.setText(firebaseUser.getEmail());

        e1 = view.findViewById(R.id.name);
        e2 = view.findViewById(R.id.email);

        e1.setVisibility(View.GONE);
        e2.setVisibility(View.GONE);

       ti1 = view.findViewById(R.id.input1);
       ti2 = view.findViewById(R.id.input2);

       ti1.setVisibility(View.GONE);
       ti2.setVisibility(View.GONE);

        frameLayout = view.findViewById(R.id.frame);

        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t1.setVisibility(View.GONE);
                ti1.setVisibility(View.VISIBLE);
                e1.setVisibility(View.VISIBLE);
                e1.setText(t1.getText().toString());

            }
        });

        t2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t2.setVisibility(View.GONE);
                ti2.setVisibility(View.VISIBLE);
                e2.setVisibility(View.VISIBLE);
                e2.setText(t2.getText().toString());

            }
        });

        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(ti1.getVisibility()==View.VISIBLE)
                {
                    t1.setText(e1.getText().toString());
                    t1.setVisibility(View.VISIBLE);
                    ti1.setVisibility(View.GONE);

                    UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(t1.getText().toString()).build();

                    firebaseUser.updateProfile(userProfileChangeRequest)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getActivity(),"Name changed successfully.",Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    Toast.makeText(getActivity(),"Unsuccessful!",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                }

                if(ti2.getVisibility()==View.VISIBLE)
                {
                    t2.setText(e2.getText().toString());
                    t2.setVisibility(View.VISIBLE);
                    ti2.setVisibility(View.GONE);


                }

                if(t2.getText().toString().equals(firebaseUser.getEmail())==false)
                {
                    showDialogBox1(t2.getText().toString());
                }

                firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                ProfileInfo.firebaseUser = firebaseUser;

                if(firebaseUser.isEmailVerified())
                {
                    t3.setVisibility(View.GONE);
                    t4.setVisibility(View.VISIBLE);
                    button.setVisibility(View.GONE);
                }
                else
                {
                    t4.setVisibility(View.GONE);
                    t3.setVisibility(View.VISIBLE);
                    button.setVisibility(View.VISIBLE);
                }

                //t1.setText(firebaseUser.getDisplayName().toString());
                //t2.setText(firebaseUser.getEmail().toString());

            }

        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (firebaseUser.isEmailVerified() == false) {

                    firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getActivity(),"Verification Email Sent, after verifying login again to see the changes!",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

    return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);
        // TODO: Use the ViewModel
    }

    public void showDialogBox1(String email)
    {
        final View alert_layout = getLayoutInflater().inflate(R.layout.password_dialog,null);

        final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setView(alert_layout);
        alert.setTitle("You need to re-authenticate in order to see the changes");
        alert.setMessage("Please enter your password");
        alert.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText e1 = alert_layout.findViewById(R.id.pass1);
                e1.setHint("Password");
                e1.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                password = e1.getText().toString();

                firebaseUser.updateEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getActivity(), "Email ID updated, please verify it.", Toast.LENGTH_SHORT).show();
                                    firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                                    try {
                                        AuthCredential authCredential = EmailAuthProvider.getCredential(t2.getText().toString(), password);

                                        firebaseUser.reauthenticate(authCredential).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                ProfileInfo.firebaseUser = firebaseUser;

                                                if (firebaseUser.isEmailVerified()) {
                                                    t3.setVisibility(View.GONE);
                                                    t4.setVisibility(View.VISIBLE);
                                                    button.setVisibility(View.GONE);
                                                } else {
                                                    t4.setVisibility(View.GONE);
                                                    t3.setVisibility(View.VISIBLE);
                                                    button.setVisibility(View.VISIBLE);
                                                }

                                            }
                                        });

                                    }
                                    catch(Exception e1)
                                    {
                                        Toast.makeText(getActivity(),e1.toString(),Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else
                                {
                                    Toast.makeText(getActivity(),"Unsuccessful!",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });



            }
        });

        alert.setNegativeButton("I'll sign in later", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = alert.create();
        alertDialog.show();
    }

}