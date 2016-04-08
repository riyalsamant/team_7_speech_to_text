package com.example.uploadeg;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by riyal on 11/12/2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME="speech.db";
    public static final String TABLE_NAME="questions";
    public static final String c1="question_id";
    public static final String c2="question";
    public static final String c3="option";
    public static final String c4="session";
    public Context context;
    private static String DB_PATH;
    static SQLiteDatabase db;
    public String a[];
    public String arr[][];


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.context = context;
        DB_PATH = context.getDatabasePath(DATABASE_NAME).getPath();
        try {
            createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }
        a=new String[6];


        // SQLiteDatabase db=this.getWritableDatabase();
    }
    public void createDataBase() throws IOException{
        boolean databaseExist = checkDataBase();
        if(databaseExist) {
        }else{
            this.getWritableDatabase();
            copyDataBase();
        }
    }

    public boolean checkDataBase(){
        File databaseFile = new File(DB_PATH + DATABASE_NAME);
        return databaseFile.exists();
    }

    private void copyDataBase() throws IOException{
        InputStream myInput = context.getAssets().open(DATABASE_NAME);
        String outFileName = DB_PATH + DATABASE_NAME;
        OutputStream myOutput = new FileOutputStream(outFileName);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    public SQLiteDatabase openDataBase() throws SQLException
    {
        String myPath = DB_PATH + DATABASE_NAME;
        return SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + "(question__id INTEGER PRIMARY KEY AUTOINCREMENT  NOT NULL,question  VARCHAR NOT NULL ,option VARCHAR,session_id VARCHAR)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists" + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String question_id,String question,String option,String session_id)
    {
        db=openDataBase();
        //SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(c1,question_id);
        cv.put(c2,question);
        cv.put(c3,option);
        cv.put(c4,session_id);
        long result= db.insert(TABLE_NAME,null,cv);
        if(result==-1)
            return false;
        else
            return true;


    }
    public String[] question(int x) {
        db=openDataBase();
        //db = this.getWritableDatabase();
        int i = 0;
        x=1;
        String query = "select * from demo where session_id='" + x + "'";
        Cursor cs = db.rawQuery(query, null);
        if (cs.moveToFirst()) {
            a[i++] = cs.getString(0);
            a[i++] = cs.getString(1);
            a[i++] = cs.getString(2);
            a[i++] = cs.getString(3);
            cs.close();

        }
        return a;
    }

    public String pendingSession(String id,String sno)
    {
        db=getReadableDatabase();
        String query = "select * from session where session_no='" + sno + "' AND user_id='"+id+"' order by rowid desc LIMIT 1";
        Cursor cs = db.rawQuery(query, null);
        cs.moveToFirst();
        String  a=cs.getString(4);
        Log.i("RESULTTTT",a);
        String ques[]=null;
        if(a.contains(","))
            ques=a.split(",");
        cs.close();
        return a;

    }

}