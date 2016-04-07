package com.example.uploadeg;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import android.app.Service;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class upload extends Service {
    private int serverResponseCode = 0;
    private ProgressDialog dialog = null;

    private String upLoadServerUri = "http://192.168.1.104/server/upload_audio.php";
    private String path = "/storage/emulated/0/Sneha";
    String filepath="";
    public IBinder onBind(Intent arg0) {
        return null;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
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

            }
        }).start();
        return START_STICKY;
    }

    @SuppressWarnings("deprecation")
    public String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();

        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();
        System.out.println("SELECT"+document_id);
        cursor = getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
        System.out.println("SELECT" + path);
        cursor.close();

        return path;
    }


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

            Thread thread = new Thread(new Runnable() {
                public void run() {

                }
            });

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
}