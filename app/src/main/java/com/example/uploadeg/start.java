package com.example.uploadeg;

/**
 * Created by bhairavee23 on 24/03/2016.
 */
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class start extends AppCompatActivity {
    String cid="";
    SqlLiteDbHelper dbHelper;

    EditText e1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        e1=(EditText)findViewById(R.id.un);
        Button b=(Button)findViewById(R.id.uid);
        b.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String uname=e1.getText().toString();
                if(uname.isEmpty())
                    Toast.makeText(getApplicationContext(), "User name cannot be blank", Toast.LENGTH_SHORT).show();
else
                {
                    dbHelper = new SqlLiteDbHelper(start.this);

                    dbHelper.openDataBase();
                  String arr[][]=  dbHelper.getDetails(uname);
                    Intent intent = new Intent(start.this,view.class);
                    intent.putExtra("result",arr);
                    Log.i("ID",arr.toString());
                    startActivity(intent);
                }



            }});
    }

    public void open(View view) {
        Intent intent = new Intent(this, MainActivity.class);

        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
