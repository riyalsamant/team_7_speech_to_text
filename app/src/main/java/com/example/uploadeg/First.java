package com.example.uploadeg;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class First extends Activity {
String cid="";
    EditText e1;
    SqlLiteDbHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean isFirstTime = GlobalActivity.isFirst(First.this);
        if(!isFirstTime)
        {
            Intent intent = new Intent(this,welcome.class);
            startActivity(intent);
        }
        else
        {
            dbHelper = new SqlLiteDbHelper(First.this);
            dbHelper.copy();
        }
        setContentView(R.layout.activity_first);
   e1=(EditText)findViewById(R.id.cid);


        Button b=(Button)findViewById(R.id.button1);
        b.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                cid=e1.getText().toString();
                if (cid.isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "ID cannot be blank", Toast.LENGTH_SHORT).show();
                }
                else
                {
                SharedPreferences sp =getSharedPreferences("MyPrefs",
                                Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("cid", cid);
                Toast.makeText(First.this, cid, Toast.LENGTH_SHORT).show();
// Apply the edits!
                editor.commit();
               /// editor.apply();
                Intent intent = new Intent(First.this,welcome.class);
                startActivity(intent);}
            }
        });    }

}
