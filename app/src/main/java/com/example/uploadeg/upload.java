package com.example.uploadeg;

import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class upload extends Service {
    private int serverResponseCode = 0;
    private ProgressDialog dialog = null;
    String line = null;
    String result = null;
    InputStream is = null;
    int code;
    String url = "http://192.168.1.104/server/";
    private String upLoadServerUri = url+"upload_audio.php";
    String co_id="";
    private String path = "/storage/emulated/0/Sneha";
    String filepath="";
    SqlLiteDbHelper controller=new SqlLiteDbHelper(this);
    ArrayList<HashMap<String, String>> userList;
    public IBinder onBind(Intent arg0) {
        return null;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        userList = controller.getAllUsers();
        SharedPreferences sp =getSharedPreferences("MyPrefs",Context.MODE_PRIVATE);
        co_id=sp.getString("cid", "");
        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        new Thread(new Runnable() {
            public void run() {
                Log.d("Files", "Path: " + path);
                File f = new File(path);
                File file[] = f.listFiles();
                Log.d("Files", "Size: " + file.length);
                for (int i=0; i < file.length; i++)
                {
                    Log.d("Files", "FileName:" + file[i].getName());
                    filepath=path+"/"+file[i].getName();
                    Log.d("Files", "FilePath:" + filepath);
                    uploadFile(filepath);
                }
                for (int i = 0; i < userList.size(); i++) {
                    syncuser(i);
                }

            }
        }).start();
        return START_STICKY;
    }

    @SuppressWarnings("deprecation")
    public int uploadFile(final String sourceFileUri) {

        String fileName = sourceFileUri;

        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File sourceFile = new File(sourceFileUri);

        if (!sourceFile.isFile()) {
            Log.e("uploadFile", "Source File not exist :" + filepath);
            return 0;

        } else {
            try {
                FileInputStream fileInputStream = new FileInputStream(
                        sourceFile);
                URL url = new URL(upLoadServerUri);
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type",
                        "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("uploaded_file", fileName);
                dos = new DataOutputStream(conn.getOutputStream());
                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                        + fileName + "\"" + lineEnd);

                dos.writeBytes(lineEnd);

                // create a buffer of maximum size
                bytesAvailable = fileInputStream.available();

                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {
                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                }

                // send multipart form data necesssary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                // Responses from the server (code and message)
                serverResponseCode = conn.getResponseCode();
                String serverResponseMessage = conn.getResponseMessage();

                Log.i("uploadFile", "HTTP Response is : "
                        + serverResponseMessage + ": " + serverResponseCode);

                if (serverResponseCode == 200) {
                    BufferedReader rd = new BufferedReader(new InputStreamReader(conn
                            .getInputStream()));
                    String line;
                    while ((line = rd.readLine()) != null) {
                        if(line.equals("success"))
                        {
                            sourceFile.delete();
                        }
                    }

                    Thread thread = new Thread(new Runnable() {
                        public void run() {
                            String msg = "File Upload Completed";
                            Toast.makeText(getApplicationContext(),msg, Toast.LENGTH_LONG).show();
                        }
                    });
                }

                // close the streams //
                fileInputStream.close();
                dos.flush();
                dos.close();

            } catch (MalformedURLException ex) {

                ex.printStackTrace();

                Thread thread = new Thread(new Runnable() {
                    public void run() {

                    }
                });

                Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
            } catch (Exception e) {

                e.printStackTrace();

                Thread thread = new Thread(new Runnable() {
                    public void run() {
                    }
                });
                // Log.e("Upload file to server Exception","Exception : " + e.getMessage(), e);
            }
            return serverResponseCode;
        }
    }

    public void syncuser(int i) {
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("co_id", co_id));
        nameValuePairs.add(new BasicNameValuePair("userId", userList.get(i).get("userId")));
        nameValuePairs.add(new BasicNameValuePair("userName", userList.get(i).get("userName")));
        nameValuePairs.add(new BasicNameValuePair("locality", userList.get(i).get("locality")));
        nameValuePairs.add(new BasicNameValuePair("reg_date", userList.get(i).get("reg_date")));
        nameValuePairs.add(new BasicNameValuePair("dob", userList.get(i).get("dob")));
        nameValuePairs.add(new BasicNameValuePair("phone", userList.get(i).get("phone")));
        nameValuePairs.add(new BasicNameValuePair("ns", userList.get(i).get("ns")));
        nameValuePairs.add(new BasicNameValuePair("ss", userList.get(i).get("ss")));
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url + "adduser.php");
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"utf-8"));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
            Log.e("pass 1", "connection success ");
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
        } catch (Exception e) {
            Log.e("Fail 1", e.toString());
            Toast.makeText(getApplicationContext(), "Invalid IP Address",
                    Toast.LENGTH_LONG).show();
        }

        try {
            BufferedReader reader = new BufferedReader
                    (new InputStreamReader(is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            result = sb.toString();
            Log.e("pass 2", "connection success ");
        } catch (Exception e) {
            Log.e("Fail 2", e.toString());
        }

        try {
            JSONObject json_data = new JSONObject(result);
            code = (json_data.getInt("code"));

            if (code == 1) {
                Toast.makeText(getBaseContext(), "Update Successfully",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getBaseContext(), "Sorry, Try Again",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Log.e("Fail 3", e.toString());
        }
    }
}