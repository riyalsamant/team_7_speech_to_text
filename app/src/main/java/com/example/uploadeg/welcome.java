package com.example.uploadeg;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;

/**
 * Created by bhairavee23 on 03/04/2016.
 */
public class welcome extends Activity {

    String cid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        TextView txt = (TextView) findViewById(R.id.cid);
        SharedPreferences sp =
                getSharedPreferences("MyPrefs",
                        Context.MODE_PRIVATE);
        txt.setText("Welcome " + sp.getString("cid", cid));
    }
    public void sess1(View view) {
        Intent intent = new Intent(this, reg.class);

        startActivity(intent);
    }

    public void login(View view) {
        Intent intent = new Intent(this, start.class);

        startActivity(intent);
    }


}