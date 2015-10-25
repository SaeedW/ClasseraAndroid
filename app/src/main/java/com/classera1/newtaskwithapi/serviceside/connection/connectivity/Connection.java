package com.classera1.newtaskwithapi.serviceside.connection.connectivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by classera 1 on 12/10/2015.
 */
public class Connection {

    static Context context;

    public Connection(Context context){
        this.context = context;
    }

    public static boolean isOnline(){
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        // test for connection
        if (cm.getActiveNetworkInfo() != null
                && cm.getActiveNetworkInfo().isAvailable()
                && cm.getActiveNetworkInfo().isConnected()) {
            return true;
        } else {
            Log.v("Connection", "Internet Connection Not Present");
            return false;
        }
    }


}
