package com.example.uploadeg;

/**
 * Created by bhairavee23 on 25/03/2016.
 */

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
import java.util.ArrayList;
import java.util.HashMap;

public class SqlLiteDbHelper extends SQLiteOpenHelper {

    // All Static variables
// Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "speech.db";
    private static final String DB_PATH_SUFFIX = "/databases/";
    public static final String TABLE_NAME = "registration";
    public static final String USER_ID = "user_name";
    public static final String USER_NAME = "user_name";
    public static final String LOCALITY = "locality";
    public static final String REGISTRATION_DATE = "registration_date";
    public static final String DOB = "dob";
    public static final String PHONE = "phone";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE "+TABLE_NAME+" (user_id INTEGER PRIMARY KEY AUTOINCREMENT, " +USER_NAME+" TEXT, "+LOCALITY+"TEXT, "+REGISTRATION_DATE+" DATE, "+DOB+" DATE, "+PHONE+" TEXT);";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;
    static Context ctx;
    ArrayList<String> list = new ArrayList<>();
    public SqlLiteDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        ctx = context;
    }

    // Getting single contact
    public Contact Get_ContactDetails() {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM demo where session_id=1", null);
        cursor.moveToFirst();
        String a="";
        while (cursor.isAfterLast() == false){
            list.add(cursor.getString(1));
            a+=cursor.getString(1);
            cursor.moveToNext();
// return contact
        }
        Contact cont = new Contact(list);
        cursor.close();
        db.close();
        return cont;
    }
    public void copy()
    {
        try {
            CopyDataBaseFromAsset();
        }
        catch(Exception e){

        }

    }
    public String[][] getDetails(String name)
    {
       /*
       */ SQLiteDatabase db = this.getReadableDatabase();
        name=name.toLowerCase();
        int v=0;
        String arr[][];
        String str="SELECT count(*) FROM registration where user_name='"+name+"'";
        Cursor cs = db.rawQuery("SELECT count(*) FROM registration where user_name='"+name+"'", null);
        Log.i("_.>",""+str);
        cs.moveToFirst();
        v=cs.getInt(0);
        Log.i("_.>",""+v);
        if( v>0)
        {
            Log.i("HELLLLLLLLLLLLYAAAAAAA",Integer.toString(v));
            arr = new String[v][2];
            int i = 0;
            cs = db.rawQuery("SELECT * FROM registration where user_name='" + name + "'", null);
            if (cs.moveToFirst()) {

                while (!cs.isAfterLast()) {
                    arr[i][0] = Integer.toString(cs.getInt(0));
                    arr[i][1] = cs.getString(1);
                    Log.i("HELLLLLLLLLLLL", arr[i][0] + arr[i][1]);
                    cs.moveToNext();
                    i++;
                }
            }
            return arr;
        }
        else
        {
            Log.i("HELLLLLLLLLLLL",name);
            String names[]=name.split("\\s");
            String ss="na";
            String ns=Soundex.soundex(names[0]);
            if(names.length>1)
                ss=Soundex.soundex(names[1]);
            Log.i("ns",ns);
            Log.i("ss",ss);
            cs= db.rawQuery("SELECT count(*) FROM registration where ns='"+ns+"' AND ss='"+ss+"'", null);
            cs.moveToFirst();
            v=cs.getInt(0);
            if(v>0) {
                arr= new String[v][2];
                int i = 0;
                cs = db.rawQuery("SELECT * FROM registration where  ns='"+ns+"' AND ss='"+ss+"'", null);
                if (cs.moveToFirst()) {

                    while (!cs.isAfterLast()) {
                        arr[i][0] = Integer.toString(cs.getInt(0));
                        arr[i][1] = cs.getString(1);
                        cs.moveToNext();
                        i++;
                    }
                }
                return arr;
            }
            else
            {
                arr=new String[1][2];
                arr[0][0]="No records ";
                arr[0][1]=" Found";
                return arr;
            }


        }
    }

    public String[][] getSessionDetails(int id)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        int v=0;

        Cursor cs = db.rawQuery("SELECT count(*) FROM session where user_id='"+id+"' order by rowid desc", null);
        if(cs.moveToFirst())
            v = cs.getInt(0);
        Log.i("count", Integer.toString(v));
        String arr[][]=new String[v][2];

        int i=0;
        cs = db.rawQuery("SELECT * FROM session where user_id='"+id+"' order by rowid desc",null);
        if(cs.moveToFirst())
        {

            boolean flag=false;
            while (!cs.isAfterLast())
            {
                flag=false;

                String sess=Integer.toString(cs.getInt(1));
                for(int k=0;k<i;k++)
                {
                    if(arr[k][0].equals(sess))
                    {
                        flag=true;
                        Log.i("yo man yo",sess);
                        break;
                    }
                }
                if(flag)
                {
                    cs.moveToNext();
                    continue;

                }
                arr[i][0]=Integer.toString(cs.getInt(1));

                arr[i][1]=cs.getString(4);
                Log.i("session",arr[i][0]+arr[i][1]);
                cs.moveToNext();
                i++;
            }

        }
        String arr1[][]=new String [i][2];
        for(int k=0;k<i;k++)
        {
            arr1[k][0]=arr[k][0];
            arr1[k][1]=arr[k][1];
        }
        return arr1;


    }

    public void CopyDataBaseFromAsset() throws IOException{

        InputStream myInput = ctx.getAssets().open(DATABASE_NAME);

// Path to the just created empty db
        String outFileName = getDatabasePath();

// if the path doesn't exist first, create it
        File f = new File(ctx.getApplicationInfo().dataDir + DB_PATH_SUFFIX);
        if (!f.exists())
            f.mkdir();

// Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

// transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

// Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }
    private static String getDatabasePath() {
        return ctx.getApplicationInfo().dataDir + DB_PATH_SUFFIX
                + DATABASE_NAME;
    }

    public SQLiteDatabase openDataBase() throws SQLException{
        File dbFile = ctx.getDatabasePath(DATABASE_NAME);

        if (!dbFile.exists()) {
            try {
                CopyDataBaseFromAsset();
                System.out.println("Copying sucess from Assets folder");
            } catch (IOException e) {
                throw new RuntimeException("Error creating source database", e);
            }
        }

        return SQLiteDatabase.openDatabase(dbFile.getPath(), null, SQLiteDatabase.NO_LOCALIZED_COLLATORS | SQLiteDatabase.CREATE_IF_NECESSARY);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
// TODO Auto-generated method stub
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public int insertDB(HashMap<String, String> queryValues )
    {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_name", queryValues.get("name"));
        values.put("locality", queryValues.get("add"));
        values.put("dob", queryValues.get("date"));
        values.put("phone", queryValues.get("phone"));
        values.put("ns", queryValues.get("ns"));
        values.put("ss", queryValues.get("ss"));
        int x = (int) database.insert("registration", null, values);
        database.close();
        return(x);
    }

    public int insertSessionDB(String id,String session,String skipped)
    {

        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        Log.i("idddddddddddddddddddd", "" + id);
        Log.i("session_nodddddddddddd", "" + session);
        values.put("user_id",id);
        values.put("session_no", session);
        values.put("skipped", skipped);
        int x = (int) database.insert("session", null, values);
        //  database.close();
        return(x);
    }

    public long getid(String  name) throws SQLException
    {
        SQLiteDatabase db = this.getReadableDatabase();
        System.out.println("user_name=" + name);
        long recc=0;
        String rec=null;
        Cursor mCursor = db.rawQuery(
                "SELECT user_id  FROM  registration WHERE user_name= '"+name+"'" , null);
        if (mCursor != null)
        {
            mCursor.moveToFirst();
            recc=mCursor.getLong(0);
            rec=String.valueOf(recc);
        }
        System.out.println("user_id=" + rec);
        return recc;
    }
    public ArrayList<HashMap<String, String>> getAllUsers() {
        ArrayList<HashMap<String, String>> usersList;
        usersList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT  * FROM registration";
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("userId", cursor.getString(0));
                map.put("userName", cursor.getString(1));
                map.put("locality", cursor.getString(2));
                map.put("reg_date", cursor.getString(3));
                map.put("dob", cursor.getString(4));
                map.put("phone", cursor.getString(5));
                map.put("ns", cursor.getString(6));
                map.put("ss", cursor.getString(7));
                usersList.add(map);
            } while (cursor.moveToNext());
        }
        database.close();
        return usersList;
    }
}