package com.classera1.newtaskwithapi.serviceside.requests;

import android.content.Context;
import android.os.AsyncTask;

import com.classera1.newtaskwithapi.database.DBHelper;

/**
 * Created by classera 1 on 21/10/2015.
 */
public class UpdateUserInformationAsync extends AsyncTask<Void, Void, String> {

    Context context;
    DBHelper DB;
    String name, email;

    public UpdateUserInformationAsync(Context context, String name, String email) {
        this.context = context;
        this.name = name;
        this.email = email;
        this.DB = new DBHelper(context);
    }


    @Override
    protected String doInBackground(Void... params) {
        DB.updateUserData(name, email);
        return "DONE";
    }


    public asyncDoneLoading asyncDone;

    public void asyncDone(asyncDoneLoading asyncDoneLoading) {
        asyncDone = asyncDoneLoading;
    }

    public interface asyncDoneLoading {
        public void onFinish(String s);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        asyncDone.onFinish(s);
    }
}

