package com.example.uploadeg;

/**
 * Created by bhairavee23 on 26/03/2016.
 */
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.support.v7.app.AlertDialog;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.view.View;
        import android.widget.Button;
        import android.widget.TextView;

        import java.util.HashMap;

        import javax.microedition.khronos.opengles.GL;

public class end extends AppCompatActivity {
    long  id,session;
    String str;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);
        Button submit;

        Intent intent = getIntent();
        id= intent.getExtras().getLong("id1");
        session= intent.getExtras().getLong("session1");
        str = intent.getExtras().getString("skipped");
        Log.i("idE", "" + id);
        Log.i("skippedE", "" + str);
        Log.i("sessionE", "" + session);
        TextView s =(TextView) findViewById(R.id.skipQ);
        s.setText("Skipped : " + str);

        SqlLiteDbHelper sdb = new SqlLiteDbHelper(this);
        int xy = sdb.insertSessionDB(id,session,str);

       /* submit=(Button)findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);

            }
        });
*/
    }

@Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Exit Application?");
        alertDialogBuilder
                .setMessage("Click yes to exit!")
                .setCancelable(false)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                moveTaskToBack(true);
                                android.os.Process.killProcess(android.os.Process.myPid());
                                System.exit(1);
                            }
                        })

                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void gohome(View view) {
        Intent intent = new Intent(this, welcome.class);
        startActivity(intent);
    }
}
