package com.example.uploadeg;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class view extends Activity {
    Object[] objectArray;
    int x;
    String id ;
    Intent intent;
    String[][] a1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        objectArray = (Object[]) getIntent().getExtras().getSerializable("result");
        x=objectArray.length;
        a1 = new String[x][];
        String y="";
        //final Context context = this;
        if(objectArray!=null){
            for(int i=0;i<objectArray.length;i++){
                a1[i]=(String[]) objectArray[i];
                y+=a1[i][0]+"   "+a1[i][1]+"\n";

            }}
        TextView tv=(TextView)findViewById(R.id.uname);
        String xyz[] = y.split("\\s");
        id=xyz[0];
        tv.setText(y);
        intent = new Intent(view.this, session.class);
        Log.i("IDV",id );
        intent.putExtra("result", id);
        Toast.makeText(getApplicationContext(), id, Toast.LENGTH_SHORT).show();

    }

    public void go(View view) {


        startActivity(intent);
    }
}
