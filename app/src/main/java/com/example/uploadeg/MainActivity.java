package com.example.uploadeg;

/**
 * Created by RAMS on 10-Mar-16.
 */
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.media.MediaRecorder;

import android.os.Bundle;
import android.os.Environment;

import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class MainActivity extends Activity {
    private static final String SELECT_SQL = "SELECT * FROM questions";
    SqlLiteDbHelper dbHelper;
    VisualizerView visualizerView;
    public static final int REPEAT_INTERVAL = 40;
    public static String s=null, con=null;
    private Handler handler;
    Contact contacts ;
    static int i=0;
    static int no =0;
    static int count=0;
    TextView t1;
    Button record;
    ImageButton skip;
    public static ArrayList<String> skipped;
    private MediaRecorder myAudioRecorder;
    private String outputFile = null;
    String url="Sneha/";
    boolean isRecording=false;
    View fullview;
    long id,session;
    String coid="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbHelper = new SqlLiteDbHelper(this);
        SharedPreferences sp =getSharedPreferences("MyPrefs",Context.MODE_PRIVATE);
        coid=sp.getString("cid","");
        dbHelper.openDataBase();
        contacts = new Contact();
        contacts = dbHelper.Get_ContactDetails();
        skipped = new ArrayList<String>();
        setContentView(R.layout.activity_main);
        fullview = (View)findViewById(R.id.fullview);
        visualizerView = (VisualizerView) findViewById(R.id.visualizer);
        handler = new Handler();
        Intent intent = getIntent();
        if(count==0)
            no= intent.getExtras().getInt("no");
        id= intent.getExtras().getInt("id");
        session= intent.getExtras().getInt("session");
        record=(Button)findViewById(R.id.button);
        skip=(ImageButton)findViewById(R.id.skip);
        t1 = (TextView) findViewById(R.id.que);
        BitmapDrawable bt = (BitmapDrawable)getResources().getDrawable(R.drawable.inactive);
        BitmapDrawable bt1 = (BitmapDrawable)getResources().getDrawable(R.drawable.active);
        record.setBackground(bt);
        record.setEnabled(false);
        //record.setBackgroundColor(Color.parseColor("#c0c0c0"));
        File folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath() +
                File.separator + "Sneha");
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdir();
        }
        Date date=new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String string =sdf.format(date) ;

        String path="/Sneha/"+string+"_"+coid+"_"+id+"_"+session+"_"+(no+1)+".mp3";
        Log.i("Path to the file is ",path);
        outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + path;
        String str = null;
        con = contacts.getQuestion(no);
        if(!con.equals("finish"))
            t1.setText("Question "+(no+1)+" : \n\n"+con);
        if(con.equals("finish"))
        {
            t1.setText("Session Completed\nClick Button To finish");
            skip.setEnabled(false);
            skip.setBackground(bt);
            record.setBackground(bt1);
            record.setEnabled(true);
        }
        myAudioRecorder=new MediaRecorder();
        myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        myAudioRecorder.setOutputFile(outputFile);


        skip.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {

                //skipped.add(String.valueOf(no));
                if(i==0)
                {
                    s=String.valueOf(no+1);
                    i++;
                }
                else
                {
                    s+=","+String.valueOf(no+1);
                }
                no++;
                count+=2;
                Intent intent = getIntent();
                finish();
                startActivity(intent);
                Toast.makeText(getApplicationContext(), "Question Skipped", Toast.LENGTH_LONG).show();
            }
        });


        fullview.setOnTouchListener(new OnSwipeTouchListener(MainActivity.this) {
            public void onSwipeTop() {
                Toast.makeText(MainActivity.this, "top", Toast.LENGTH_SHORT).show();
            }

            public void onSwipeRight() {
                TextView t1 = (TextView) findViewById(R.id.que);


            }

            public void onSwipeLeft() {
                TextView t1 = (TextView) findViewById(R.id.que);
                if(!con.equals("finished")) {
                    if (count % 2 == 0) {
                        count++;
                        try {
                            isRecording = true;
                            myAudioRecorder.prepare();
                            myAudioRecorder.start();
                            handler.post(updateVisualizer);
                        } catch (IllegalStateException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }


                        Toast.makeText(getApplicationContext(), "Recording started", Toast.LENGTH_SHORT).show();
                    } else {
                        count++;
                        releaseRecorder();
                        Toast.makeText(getApplicationContext(), "Audio recorded successfully", Toast.LENGTH_SHORT).show();
                        MediaPlayer m = new MediaPlayer();

                        try {
                            m.setDataSource(outputFile);
                            no++;
                            Intent intent = getIntent();
                            finish();
                            startActivity(intent);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        try {
                            m.prepare();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }

            public void onSwipeBottom() {
                Toast.makeText(MainActivity.this, "bottom", Toast.LENGTH_SHORT).show();
            }

        });
    }
    Runnable updateVisualizer = new Runnable() {
        @Override
        public void run() {
            if (isRecording) // if we are already recording
            {
                // get the current amplitude
                int x =myAudioRecorder.getMaxAmplitude();
                visualizerView.addAmplitude(x); // update the VisualizeView
                visualizerView.invalidate(); // refresh the VisualizerView

                // update in 40 milliseconds
                handler.postDelayed(this, REPEAT_INTERVAL);
            }
        }
    };
    private void releaseRecorder() {
        if (myAudioRecorder != null) {
            isRecording = false; // stop recording
            handler.removeCallbacks(updateVisualizer);
            visualizerView.clear();
            myAudioRecorder.stop();
            myAudioRecorder.reset();
            myAudioRecorder.release();
            myAudioRecorder = null;
            Log.i("S",s);
        }
    }
    public void finish1(View view) {
        /*String str = skipped.get(0) ;
        for(int i=1;i<skipped.size()-1;i++)
        {
            str+=","+skipped.get(i);
        }*/
        if(s==null)
            s="0";
        Intent intent = new Intent(this, end.class);
        intent.putExtra("session1",session);
        intent.putExtra("id1", id);
        intent.putExtra("skipped",s);
        Log.i("idMA", "" + id);
        Log.i("skipped", "" + s);
        Log.i("sessionMA", "" + session);
        i=0;
        count=0;
        //finish();
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