package com.example.uploadeg;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by bhairavee23 on 03/04/2016.
 */

public class session extends Activity {

    SqlLiteDbHelper dbHelper;
    int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session);
        Intent intent = getIntent();
        String x = intent.getExtras().getString("result");
        TextView t = (TextView)findViewById(R.id.a2);
        /*BitmapDrawable b1 = (BitmapDrawable)getResources().getDrawable(R.drawable.red);
        BitmapDrawable b2 = (BitmapDrawable)getResources().getDrawable(R.drawable.yellow);
        BitmapDrawable b3 = (BitmapDrawable)getResources().getDrawable(R.drawable.green);
        */
        ImageView[] img=new ImageView[3];
        img[0]= (ImageView)findViewById(R.id.img1);
        img[1] = (ImageView)findViewById(R.id.img2);
        img[2] = (ImageView)findViewById(R.id.img3);
        t.setText("Beneficiary Session Details\n\n");
        id = Integer.parseInt(x);
        dbHelper = new SqlLiteDbHelper(session.this);
        dbHelper.openDataBase();
        String arr[][]=  dbHelper.getSessionDetails(id);

        for(int i=0;i<arr.length;i++)
        {
            if(arr[i][1].equals("NA"))
            {
                img[i].setImageResource(R.drawable.green);

            }
            else
            {
                img[i].setImageResource(R.drawable.yellow);
            }
        }

        Button b1 = (Button)findViewById(R.id.b1);
        Button b2 = (Button)findViewById(R.id.b2);
        Button b3 = (Button)findViewById(R.id.b3);
        b1.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {

                String s =  ((Button) findViewById(R.id.b1)).getText().toString();
                String[] str = s.split("\\s");
                Intent intent = new Intent(session.this,choose.class);
                Intent intent1=getIntent();
                dbHelper = new SqlLiteDbHelper(session.this);
                dbHelper.openDataBase();
                String arr[][]=  dbHelper.getSessionDetails(id);
                String str2 = arr[0][1];
                Log.i("Heyy",str2);
                String id=intent1.getExtras().getString("result");
                intent.putExtra("session",str[1]);
                intent.putExtra("id",id);
                intent.putExtra("res", str2);
                finish();
                startActivity(intent);
                Toast.makeText(getApplicationContext(), id, Toast.LENGTH_LONG).show();
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {

                String s =  ((Button) findViewById(R.id.b2)).getText().toString();
                String[] str = s.split("\\s");
                dbHelper = new SqlLiteDbHelper(session.this);
                dbHelper.openDataBase();
                String arr[][]=  dbHelper.getSessionDetails(id);
                String str2 = arr[1][1];
                Log.i("heyyy",str2);
                Intent intent = new Intent(session.this,choose.class);
                Intent intent1=getIntent();
                String id=intent1.getExtras().getString("result");
                intent.putExtra("session",str[1]);
                intent.putExtra("id",id);
                intent.putExtra("res",str2);
                finish();
                startActivity(intent);
                // Toast.makeText(getApplicationContext(), id, Toast.LENGTH_LONG).show();
            }
        });

        b3.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {

                String s =  ((Button) findViewById(R.id.b3)).getText().toString();
                String[] str = s.split(" ");
                dbHelper = new SqlLiteDbHelper(session.this);
                dbHelper.openDataBase();
                String arr[][]=  dbHelper.getSessionDetails(id);
                String str2 = arr[2][1];
                Log.i("heyy",str2);
                Intent intent = new Intent(session.this,choose.class);
                Intent intent1=getIntent();
                String id=intent1.getExtras().getString("result");
                intent.putExtra("session",str[1]);
                intent1.putExtra("id",id);
                intent1.putExtra("res",str2);
                finish();
                startActivity(intent);

            }
        });
    }
}
