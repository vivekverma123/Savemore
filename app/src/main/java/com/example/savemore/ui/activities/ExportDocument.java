package com.example.savemore.ui.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.model.Account;
import com.example.model.Transaction;
import com.example.savemore.R;
import com.google.android.material.textfield.TextInputLayout;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicReferenceArray;

public class ExportDocument extends AppCompatActivity {

    private TextInputLayout t1;
    private TextInputLayout t2;
    private Button b1;
    private Account account;
    private ArrayList<Transaction> transactions;
    private EditText from;
    private EditText to;
    final Calendar myCalendar = Calendar.getInstance();
    private ArrayList <Transaction> arrayList;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        setContentView(R.layout.activity_export_document);



        Intent intent = getIntent();
        account = (Account) intent.getSerializableExtra("Account");
        transactions = (ArrayList<Transaction>) intent.getSerializableExtra("Transactions");

        arrayList = new ArrayList<>();

        t1 = findViewById(R.id.form_input);
        t2 = findViewById(R.id.to_layout);

        b1 = findViewById(R.id.plot);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                sdf.setTimeZone(TimeZone.getTimeZone("Asia/Calcutta"));
                Date d1,d2;
                d1 = new Date();
                d2 = new Date();
                try {
                    d1 = sdf.parse(from.getText().toString());
                    d2 = sdf.parse(to.getText().toString());
                }
                catch(Exception e1)
                {

                }

                for(Transaction transaction : transactions)
                {
                    //Toast.makeText(ChartActivity.this,d1.toString() +" " +transaction.giveDate().toString() + " " + d2.toString(),Toast.LENGTH_SHORT).show();
                    //Log.d("Data",d1.toString() + " " + transaction.giveDate().toString() + " " + d2.toString());
                    if(transaction.getTime() >= d1.getTime() && transaction.getTime()<=d2.getTime())
                    {
                        arrayList.add(transaction);
                    }
                }

                isWriteStoragePermissionGranted();
            }
        });

        from = findViewById(R.id.from);
        to = findViewById(R.id.to);
        from.setTextIsSelectable(true);
        to.setTextIsSelectable(true);
        from.setShowSoftInputOnFocus(false);
        to.setShowSoftInputOnFocus(false);

        myCalendar.setTimeZone(TimeZone.getTimeZone("Asia/Calcutta"));

        DatePickerDialog.OnDateSetListener date1 = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel1();
            }

        };

        DatePickerDialog.OnDateSetListener date2 = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel2();
            }

        };

        from.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                new DatePickerDialog(ExportDocument.this, date1, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(ExportDocument.this, date2, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

    }

    private void updateLabel1() {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);

        from.setText(sdf.format(myCalendar.getTime()));
    }

    private void updateLabel2() {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);

        to.setText(sdf.format(myCalendar.getTime()));
    }
    public void writeFile(){

        try {
            String filename = account.getName() + ".xls";
            String root = Environment.getExternalStorageDirectory().toString();
            File file = new File(root + "/Savemore");
            if(!file.exists())
            {
                file.mkdirs();
            }

            File file1 = new File(file,filename);
            if(file1.exists()==true)
            {
                int a = 1;
                file1 = new File(file,account.getName() + a + ".xls");
                while(file1.exists())
                {
                    a += 1;
                    file1 = new File(file,account.getName() + a + ".xls");
                }
            }

            FileOutputStream fileOutputStream = new FileOutputStream(file1);

            HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
            HSSFSheet hssfSheet = hssfWorkbook.createSheet("Sheet1");
            HSSFRow hssfRow = hssfSheet.createRow((short)0);
            Cell cell;
            hssfRow.createCell(0);
            hssfRow.createCell(1);
            hssfRow.createCell(2);
            hssfRow.createCell(3);
            hssfRow.createCell(4);
            cell = hssfRow.getCell(0);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue("#");
            cell = hssfRow.getCell(1);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue("Date");
            cell = hssfRow.getCell(2);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue("Particular");
            cell = hssfRow.getCell(3);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue("Debit");
            cell = hssfRow.getCell(4);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue("Credit");

            int index = 1,balance = 0;
            for(Transaction transaction : arrayList)
            {
                HSSFRow hssfRow1 = hssfSheet.createRow((short)index);
                Cell cell1;
                hssfRow1.createCell(0);
                hssfRow1.createCell(1);
                hssfRow1.createCell(2);
                hssfRow1.createCell(3);
                hssfRow1.createCell(4);

                cell1 = hssfRow1.getCell(0);
                cell1.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                cell1.setCellValue(index);

                cell1 = hssfRow1.getCell(1);
                cell1.setCellType(HSSFCell.CELL_TYPE_STRING);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                cell1.setCellValue(simpleDateFormat.format(transaction.giveDate()));

                cell1 = hssfRow1.getCell(2);
                cell1.setCellType(HSSFCell.CELL_TYPE_STRING);
                cell1.setCellValue(transaction.getParticular());

                cell1 = hssfRow1.getCell(3);
                cell1.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                if(transaction.getType()==0)
                {
                    balance += transaction.getAmount();
                    cell1.setCellValue(transaction.getAmount());
                }
                else
                {
                    cell1.setCellValue(0);
                }

                cell1 = hssfRow1.getCell(4);
                cell1.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                if(transaction.getType()==1)
                {
                    balance -= transaction.getAmount();
                    cell1.setCellValue(transaction.getAmount());
                }
                else
                {
                    cell1.setCellValue(0);
                }

                index += 1;

            }

            hssfWorkbook.write(fileOutputStream);
            fileOutputStream.close();

            Log.d("Success","Success");
            Toast.makeText(ExportDocument.this,"File " + filename + "saved in internal storage in /Savemore",Toast.LENGTH_SHORT).show();
            onBackPressed();
        } catch(Exception e) {
            Log.d("Exception","File not found");
        }


    }

    public  boolean isWriteStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                writeFile();
                return true;
            } else {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation

            return true;
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 2:

                if(grantResults[0]== PackageManager.PERMISSION_GRANTED){

                    //resume tasks needing this permission
                    // downloadPdfFile();
                    writeFile();
                }else{
                    //progress.dismiss();
                }
                break;

        }
    }

}