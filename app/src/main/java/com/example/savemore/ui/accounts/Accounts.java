package com.example.savemore.ui.accounts;

import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.model.Account;
import com.example.model.ProfileInfo;
import com.example.savemore.R;
import com.example.savemore.ui.DialogBoxes.CreateAccountDialog;
import com.example.savemore.ui.UserAdapters.AccountInfoAdapter;
import com.example.savemore.ui.activities.AccountDetail;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Accounts extends Fragment {

    private AccountsViewModel mViewModel;
    private ListView listView;
    DatabaseReference d1;

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

        listView = view.findViewById(R.id.account_list);

        d1 = FirebaseDatabase.getInstance().getReference(ProfileInfo.firebaseUser.getUid());
        d1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Account> arrayList = new ArrayList<>();
                for(DataSnapshot dataSnapshot : snapshot.child("Accounts").getChildren())
                {
                    Account account = dataSnapshot.getValue(Account.class);
                    arrayList.add(account);
                }
                AccountInfoAdapter accountInfoAdapter = new AccountInfoAdapter(getActivity(),arrayList);
                listView.setAdapter(accountInfoAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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