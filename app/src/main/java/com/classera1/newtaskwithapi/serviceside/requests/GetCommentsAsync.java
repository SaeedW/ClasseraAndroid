package com.classera1.newtaskwithapi.serviceside.requests;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.classera1.newtaskwithapi.serviceside.api.Links;
import com.classera1.newtaskwithapi.serviceside.connection.serverrequest.Networking;
import com.classera1.newtaskwithapi.serviceside.parser.Parser;

/**
 * Created by classera 1 on 22/10/2015.
 */
public class GetCommentsAsync extends AsyncTask<Void,Void,String> {

    Context context;
    String lId;
    ProgressDialog dialog;


    public GetCommentsAsync(Context context,String lId){
        this.context = context;
        this.dialog = new ProgressDialog(context);
        this.lId = lId;
    }

    @Override
    protected String doInBackground(Void... params) {
        String result = new Parser(context).getCommetnsParser(Networking.postToUrl(Links.GET_COMMENTS+lId+Links.ENDOFURL,"CC",lId));
        if(result.equals("DONE"))
          return  "DONE";
        else
        return "ERROR";
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog.setMessage("Loading ...");
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        dialog.dismiss();
        asyncDone.onFinish(s);
    }

    public asyncDoneLoading asyncDone;

    public void asyncDone(asyncDoneLoading asyncDoneLoading) {
        asyncDone = asyncDoneLoading;
    }

    public interface asyncDoneLoading {
        public void onFinish(String s);
    }
}

