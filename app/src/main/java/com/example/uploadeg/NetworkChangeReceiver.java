package com.example.uploadeg;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class NetworkChangeReceiver extends BroadcastReceiver {
    @Override

    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
            Toast.makeText(context, "connect to the internet", Toast.LENGTH_LONG).show();
                /*upload background upload service*/
            Intent serviceIntent = new Intent(context,upload.class);
            context.startService(serviceIntent);


        }else{
            Toast.makeText(context, "Connection faild", Toast.LENGTH_LONG).show();

        }
    }
}