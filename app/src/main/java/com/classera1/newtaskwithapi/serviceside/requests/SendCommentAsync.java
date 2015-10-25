package com.classera1.newtaskwithapi.serviceside.requests;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.ListView;

import com.classera1.newtaskwithapi.adapting.CommentAdapter;
import com.classera1.newtaskwithapi.serviceside.api.Links;
import com.classera1.newtaskwithapi.serviceside.connection.serverrequest.Networking;
import com.classera1.newtaskwithapi.serviceside.parser.Parser;


/**
 * Created by classera 1 on 21/10/2015.
 */
public class SendCommentAsync extends AsyncTask<Void, Void, String> {


    Context context;
    ListView list;
    String uid, lid, text;
    ProgressDialog dialog;
    CommentAdapter adapter;

    public SendCommentAsync(Context context, ListView list, String uid, String lid, String text) {
        this.context = context;
        this.list = list;
        this.uid = uid;
        this.lid = lid;
        this.text = text;
        dialog= new ProgressDialog(context);
    }

    @Override
    protected String doInBackground(Void... params) {
        String result = new Parser(context).Parseers(Networking.postToUrl(Links.SEND_COMMENT_URL + Links.ENDOFURL, "sendComment", uid, lid, text));
        if(result.contains("DONE")) {
            return "DONE";
        }
        else {
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

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog.setMessage("Sending..");
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        dialog.dismiss();
        asyncDone.onFinish(s);
        adapter = new CommentAdapter(context,lid);
        list.setAdapter(adapter);
    }
}

