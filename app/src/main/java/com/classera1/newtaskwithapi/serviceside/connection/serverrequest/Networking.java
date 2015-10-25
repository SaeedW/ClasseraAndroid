package com.classera1.newtaskwithapi.serviceside.connection.serverrequest;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Saeed on 9/21/2015.
 * class networking contain the get and post functions
 */
public class Networking {

    private static HttpURLConnection con;
    private static BufferedReader reader;

    /*
    Get Request
     */
    public static String getStringFromURL(String link) {
        try {
            //set the url to urlObject
            URL url = new URL(link);
            //init the object of class HttpUrlConnection
            con = (HttpURLConnection) url.openConnection();
            //set the method
            con.setRequestMethod("GET");
            //adding Header Section Here

            // Request values
            // change the values from the database
            con.setRequestProperty("Content-Type", "application/json; charset=utf-8");

            //init the buffered reader object
            reader = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            //get the response
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = reader.readLine()) != null) {
                response.append(inputLine);
            }
            reader.close();
            System.out.print(response.toString());
            return response.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return "ERRORN";
        } catch (ProtocolException e) {
            e.printStackTrace();
            return "ERRORN";
        } catch (IOException e) {
            e.printStackTrace();
            return "ERRORN";
        }
    }


    /*
    Post a request Function
     */
    public static String postToUrl(String link, String... param) {
        try {
            URL url = new URL(link);
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");

            // Request Header
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

            //URL Parameter
            String urlParameters = getParams(param);

            // Send post request
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            //print result
            System.out.println(response.toString());

            return response.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
            return "ERRORN";
        } catch (ProtocolException e) {
            e.printStackTrace();
            return "ERRORN";
        } catch (IOException e) {
            e.printStackTrace();
            return "ERRORN";
        }

    }

    public static String getParams(String[] param) {
        if (param[0].equals("sendComment")) {
            try {
//                return "user_id=" +param[1]+ "&subject_id=" +param[2]+ "&body=" +param[3];
                return "user_id=" + URLEncoder.encode(param[1], "UTF-8") + "&subject_id=" + URLEncoder.encode(param[2], "UTF-8") + "&body=" + URLEncoder.encode(param[3], "UTF-8");
            } catch (Exception e) {
                return e.getMessage().toString();
            }
        }
        else if(param[0].equals("CC")){
            try {
            return "subject_id=" + URLEncoder.encode(param[1], "UTF-8");
            } catch (Exception e) {
                return e.getMessage().toString();
            }
        }else if(param[0].equals("deletecomment")){
            try {
                return "id=" + URLEncoder.encode(param[1], "UTF-8") + "&user_id=" + URLEncoder.encode(param[2]);
            } catch (Exception e) {
                return e.getMessage().toString();
            }
        }
        else {
            try {
                return "user_name=" + URLEncoder.encode(param[1], "UTF-8") + "&email=" + URLEncoder.encode(param[2], "UTF-8") + "&device_id=" + URLEncoder.encode(param[3], "UTF-8");
            } catch (Exception e) {
                return e.getMessage().toString();
            }
        }
    }

}
