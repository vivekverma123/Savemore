package com.example.savemore.ui.accounts;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.savemore.R;
import com.example.savemore.ui.DialogBoxes.CreateAccountDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Accounts extends Fragment {

    private AccountsViewModel mViewModel;

    public static Accounts newInstance() {
        return new Accounts();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.accounts_fragment, container, false);
        FloatingActionButton f1 = view.findViewById(R.id.open_account);
        f1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(),"Services are going to start shortly!",Toast.LENGTH_SHORT).show();
                CreateAccountDialog cdd=new CreateAccountDialog(getActivity());
                cdd.show();
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(AccountsViewModel.class);
        // TODO: Use the ViewModel

    }

}