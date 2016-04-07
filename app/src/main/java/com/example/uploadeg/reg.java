package com.example.uploadeg;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.app.DatePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by bhairavee23 on 02/04/2016.
 */





    public class reg extends Activity {

        private DatePicker datePicker;
        private Calendar calendar;
        private EditText dateView, name, age,add,phone;
        private int year, month, day,yr;
        private String date,d2;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_reg);
            name = (EditText) findViewById(R.id.name);
            add = (EditText) findViewById(R.id.add);
            age = (EditText) findViewById(R.id.age);
            phone = (EditText) findViewById(R.id.phone);
            dateView = (EditText) findViewById(R.id.date);
            calendar = Calendar.getInstance();
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);
            d2 = year+"-"+month+1+"-"+day;
            showDate(year, month + 1, day);
        }

        @SuppressWarnings("deprecation")
        public void setDate(View view) {
            showDialog(999);
            Toast.makeText(getApplicationContext(), "ca", Toast.LENGTH_SHORT)
                    .show();
        }

        @Override
        protected Dialog onCreateDialog(int id) {
            // TODO Auto-generated method stub
            if (id == 999) {
                return new DatePickerDialog(this, myDateListener, year, month, day);
            }
            return null;
        }

        private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub
                // arg1 = year
                // arg2 = month
                // arg3 = day
                date=arg1+"-"+(arg2+1)+"-"+arg3;
                showDate(arg1, arg2 + 1, arg3);

            }
        };

        private void showDate(int year, int month, int day) {
            dateView.setText(new StringBuilder().append(day).append("/")
                    .append(month).append("/").append(year));
        }

        public void sessbegin(View view)
        {

            String n = name.getText().toString();
            String names[]=n.split(" ");
            String ss="na";
            String ns=Soundex.soundex(names[0]);
            if(names.length>1)
                ss=Soundex.soundex(names[1]);
            Log.i("Yooo",ns+" "+ss);
            String ad = add.getText().toString();
            String pno = phone.getText().toString();
            String d = date;

            HashMap<String,String>hp = new HashMap<String, String>();
            hp.put("name", n);
            hp.put("add",ad);
            hp.put("date",d);
            hp.put("phone",pno);
            hp.put("ns",ns);
            hp.put("ss",ss);
            SqlLiteDbHelper sdb = new SqlLiteDbHelper(this);
            int xy = sdb.insertDB(hp);//check this
            Toast.makeText(getApplicationContext(), "success "+xy, Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this,MainActivity.class);
            int x =5;
            intent.putExtra("id",xy);
            intent.putExtra("no",x);
            intent.putExtra("session",1);
            startActivity(intent);
        }

    }