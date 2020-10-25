package com.example.savemore.ui.DialogBoxes;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.model.Account;
import com.example.model.Category;
import com.example.model.ProfileInfo;
import com.example.savemore.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CategoryDialog extends Dialog implements android.view.View.OnClickListener
{

    public Activity c;
    public Dialog d;
    public Button AddNew, DeleteSelected;
    public DatabaseReference databaseReference;
    public Account account;
    public Category toDelete;
    Spinner spinner;

    public CategoryDialog(Activity a, Account account) {
        super(a);
        this.c = a;
        this.account = account;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.create_category);
        AddNew = findViewById(R.id.add_new);
        DeleteSelected = findViewById(R.id.delete_selected);
        AddNew.setOnClickListener(this);
        DeleteSelected.setOnClickListener(this);
        databaseReference = FirebaseDatabase.getInstance().getReference(ProfileInfo.firebaseUser.getUid()).child("Categories").child(account.getId());
        toDelete = new Category();
        spinner = findViewById(R.id.categories);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                toDelete = (Category) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList <Category> arrayList = new ArrayList<>();
                Category category1 = new Category("","Other (default)");
                arrayList.add(category1);
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    Category category = dataSnapshot.getValue(Category.class);
                    arrayList.add(category);
                }
                ArrayAdapter <Category> arrayAdapter = new ArrayAdapter(c,android.R.layout.simple_spinner_item,arrayList);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.add_new:
                EditText editText = findViewById(R.id.category);
                String id = databaseReference.push().getKey();
                Category category = new Category(id,editText.getText().toString());
                databaseReference.child(id).setValue(category);
                Toast.makeText(c,"Added Successfully",Toast.LENGTH_SHORT).show();
                break;

            case R.id.delete_selected:

                if(toDelete.getId().equals(""))
                {
                    Toast.makeText(c,"Cannot Delete System Default Categories",Toast.LENGTH_SHORT).show();
                }
                else {
                    databaseReference.child(toDelete.getId()).setValue(null);
                    Toast.makeText(c, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                }
                break;

            default:
                break;
        }
        dismiss();
    }
}
