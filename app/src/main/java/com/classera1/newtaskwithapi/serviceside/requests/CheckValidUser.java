package com.classera1.newtaskwithapi.serviceside.requests;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.classera1.newtaskwithapi.serviceside.api.Links;
import com.classera1.newtaskwithapi.serviceside.connection.serverrequest.Networking;
import com.classera1.newtaskwithapi.serviceside.parser.Parser;

/**
 * Created by classera 1 on 22/10/2015.
 */
public class CheckValidUser extends AsyncTask<Void, Void, String> {

    private String email, nickname, deviceid;
    private Context context;
    private ProgressDialog dialog;


    public CheckValidUser(Context context, String email, String nickname, String deviceid) {
        this.context = context;
        this.email = email;
        this.nickname = nickname;
        this.deviceid = deviceid;
        this.dialog = new ProgressDialog(context);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        dialog.dismiss();
        asyncDone.onFinish(s);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog.setMessage("Checking Linsence ...");
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    protected String doInBackground(Void... params) {

        // get the link -> // make the request -> // get the parsing result -> // return the value
        try {
            String result = new Parser(context).Parseers(Networking.postToUrl(Links.CHECK_USER_LISENSE + Links.ENDOFURL, "checkUser", nickname, email, deviceid));
            if (result.contains("DONE")) {
                return "DONE";
            }
            else if(result.equals("ERRORN")){
                return "ERRORN";
            }
            else {
                return "ERROR";
            }
        } catch (Exception e) {
            dialog.dismiss();
            Log.d("ERROR", "ERROR IN REQUEST  " + e.getMessage().toString());
            return "ERROR";
        }

    }

    public asyncDoneLoading asyncDone;

    public void asyncDone(asyncDoneLoading asyncDoneLoading) {
        asyncDone = asyncDoneLoading;
    }

    public interface asyncDoneLoading {
        public void onFinish(String s);
    }
}
