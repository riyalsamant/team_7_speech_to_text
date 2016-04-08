package com.example.uploadeg;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by bhairavee23 on 03/04/2016.
 */

public class choose extends Activity {

    DatabaseHelper dbHelper;
    Button b1,b2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);
        Intent intent = getIntent();
        b1=(Button)findViewById(R.id.button4);
        b2=(Button)findViewById(R.id.button5);
        dbHelper = new DatabaseHelper(this);
        String s = intent.getExtras().getString("res");
        if(s.equals("NA,"))
        {
            b1.setEnabled(false);
            b1.setBackgroundResource(R.drawable.inactive);
        }
        dbHelper.openDataBase();
        b1.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {

                Intent intent = getIntent();

                String no = intent.getExtras().getString("id");
                Log.i("IDDDDD",no);
                String sno = intent.getExtras().getString("session");
                Log.i("sessionnnnnn IDDDDD",sno);
                String ques = dbHelper.pendingSession(no, sno);
                Log.i("QUES",ques);
                Intent intent1 = new Intent(choose.this, PendingSession.class);
                intent1.putExtra("session", sno);
                intent1.putExtra("id", no);
                intent1.putExtra("ques", ques);
                finish();
                startActivity(intent1);
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                Intent intent = getIntent();

                String no = intent.getExtras().getString("id");
                String sno = intent.getExtras().getString("session");
                Intent intent1 = new Intent(choose.this, Retrieval.class);
                intent1.putExtra("session", sno);
                intent1.putExtra("id", no);
                finish();
                startActivity(intent1);
            }
        });
    }
}