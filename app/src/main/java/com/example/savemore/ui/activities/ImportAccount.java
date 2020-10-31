package com.example.savemore.ui.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.model.Account;
import com.example.model.ProfileInfo;
import com.example.model.Transaction;
import com.example.savemore.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ImportAccount extends AppCompatActivity {

    private Spinner spinner;
    Button button;
    boolean rule = true;
    String format;
    private final int REQUEST_CODE_DOC = 65;
    final public String TAG = "Permission";
    private Account account;
    private ProgressBar progressBar;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        setContentView(R.layout.activity_import_account);


        spinner = findViewById(R.id.spinner4);
        button = findViewById(R.id.choose);
        rule = true;
        progressBar = findViewById(R.id.bar3);



        Intent intent = getIntent();
        account = (Account) intent.getSerializableExtra("Account");
        databaseReference = FirebaseDatabase.getInstance().getReference(ProfileInfo.firebaseUser.getUid()).child("Transactions").child(account.getId());

        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("dd/MM/yyyy");
        arrayList.add("dd-MM-yyyy");
        arrayList.add("MM/dd/yyyy");
        arrayList.add("MM-dd-yyyy");
        arrayList.add("yyyy/MM/dd");
        arrayList.add("yyyy-MM-dd");

        ArrayAdapter arrayAdapter = new ArrayAdapter (ImportAccount.this,R.layout.support_simple_spinner_dropdown_item,arrayList);
        spinner.setAdapter(arrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                format = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                format = "dd/MM/yyyy";
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isReadStoragePermissionGranted();
                progressBar.setVisibility(View.VISIBLE);
                //onBackPressed();
            }
        });


    }

    public void onCheckBoxClicked(View view)
    {
        if(view.getId()==R.id.checkBox)
        {
            rule = false;
        }
    }

    private void browseDocuments(){

        String[] mimeTypes =
                {"application/msword","application/vnd.openxmlformats-officedocument.wordprocessingml.document", // .doc & .docx
                        "application/vnd.ms-powerpoint","application/vnd.openxmlformats-officedocument.presentationml.presentation", // .ppt & .pptx
                        "application/vnd.ms-excel","application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", // .xls & .xlsx
                        "text/plain",
                        "application/pdf",
                        "application/zip"};

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent.setType(mimeTypes.length == 1 ? mimeTypes[0] : "*/*");
            if (mimeTypes.length > 0) {
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
            }
        } else {
            String mimeTypesStr = "";
            for (String mimeType : mimeTypes) {
                mimeTypesStr += mimeType + "|";
            }
            intent.setType(mimeTypesStr.substring(0,mimeTypesStr.length() - 1));
        }
        startActivityForResult(Intent.createChooser(intent,"ChooseFile"), REQUEST_CODE_DOC);

    }

    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);
        if (requestCode == REQUEST_CODE_DOC
                && resultCode == Activity.RESULT_OK) {
            // The result data contains a URI for the document or directory that
            // the user selected.
            Uri uri = null;
            if (resultData != null) {

                try {

                    uri = resultData.getData();

                }
                catch(Exception e1)
                {
                    Log.d("File",e1.toString());
                }

                //Perform operations on the document using its URI.
                try {
                    OpenFile(uri);
                } catch (Exception e) {
                    Log.d("Exception: ",e.toString());
                }
            }
        }
    }

    public void OpenFile(Uri uri) throws IOException {


        progressBar.setVisibility(View.VISIBLE);
        InputStream fileInputStream = getContentResolver().openInputStream(uri);


        HSSFWorkbook wb = new HSSFWorkbook(fileInputStream);
        HSSFSheet sheet = wb.getSheetAt(0);
        FormulaEvaluator formulaEvaluator = wb.getCreationHelper().createFormulaEvaluator();
        int x = 0;
        for (Row row : sheet) {
            if(x==0)
            {
                x += 1;
                continue;
            }
            int y = 0;
            Transaction transaction = new Transaction();
            Date date = new Date();
            try {

                for (Cell cell : row) {
                    switch (y) {
                        case 0:
                            break;

                        case 1:
                            try {
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
                                date = simpleDateFormat.parse(cell.getStringCellValue());
                            } catch (Exception e1) {

                                date = new Date();
                                Log.d("Exception","Unsupported date format");
                            }
                            break;

                        case 2:
                            String particular = cell.getStringCellValue();
                            transaction.setParticular(particular);
                            break;

                        case 3:
                            int data = (int) cell.getNumericCellValue();
                            if(rule==true) {
                                if (data == 0) {

                                } else {
                                    transaction.setType(0);
                                    transaction.setAmount(data);
                                }
                            }
                            else
                            {
                                if(data==0)
                                {

                                }
                                else
                                {
                                    transaction.setType(1);
                                    transaction.setAmount(data);
                                }
                            }
                            break;

                        case 4:
                            int data1 = (int) cell.getNumericCellValue();
                            if(rule==true) {
                                if (data1 == 0) {

                                } else {
                                    transaction.setType(1);
                                    transaction.setAmount(data1);
                                }
                            }
                            else
                            {
                                if (data1 == 0) {

                                } else {
                                    transaction.setType(0);
                                    transaction.setAmount(data1);
                                }
                            }
                            break;

                    }
                    y += 1;
                }}
            catch(Exception e2)
            {
                Toast.makeText(ImportAccount.this,e2.toString(),Toast.LENGTH_SHORT).show();
            }
            x += 1;
            transaction.setTime(date.getTime());
            String id;
            id = databaseReference.push().getKey();
            transaction.setId(id);
            transaction.setCategory("Not supported");
            databaseReference.child(id).setValue(transaction);
            //Toast.makeText(ImportAccount.this,transaction.giveDate().toString() + " " + transaction.getParticular() + " " +  transaction.getType() + " " + transaction.getAmount(),Toast.LENGTH_SHORT).show();

        }
        Toast.makeText(ImportAccount.this,"Account Imported Successfully",Toast.LENGTH_SHORT).show();
        progressBar.setVisibility(View.GONE);
    }

    public  boolean isReadStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted1");
                browseDocuments();
                return true;
            } else {

                Log.v(TAG,"Permission is revoked1");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 3);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted1");
            return true;
        }
    }

    public  boolean isWriteStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted2");
                return true;
            } else {

                Log.v(TAG,"Permission is revoked2");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted2");
            return true;
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 2:
                Log.d(TAG, "External storage2");
                if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    Log.v(TAG,"Permission: "+permissions[0]+ "was "+grantResults[0]);
                    //resume tasks needing this permission
                    // downloadPdfFile();
                }else{
                    //progress.dismiss();
                }
                break;

            case 3:
                Log.d(TAG, "External storage1");
                if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    Log.v(TAG,"Permission: "+permissions[0]+ "was "+grantResults[0]);
                    //resume tasks needing this permission
                    //SharePdfFile();
                    browseDocuments();
                }else{
                    //progress.dismiss();
                }
                break;
        }
    }
}