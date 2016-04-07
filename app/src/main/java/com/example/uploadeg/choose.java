package com.example.uploadeg;


import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
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

        dbHelper.openDataBase();
        b1.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {

                Intent intent = getIntent();

                String no = intent.getExtras().getString("id");
                String sno = intent.getExtras().getString("session");
                String ques[] = dbHelper.pendingSession(no, sno);
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
        String ques[] = dbHelper.pendingSession(no, sno);
        Intent intent1 = new Intent(choose.this, Retrieval.class);
        intent1.putExtra("session", sno);
        intent1.putExtra("id", no);
        finish();
        startActivity(intent1);
    }
});
    }
}