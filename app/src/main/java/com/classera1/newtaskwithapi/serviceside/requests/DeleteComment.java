package com.classera1.newtaskwithapi.serviceside.requests;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.ListView;

import com.classera1.newtaskwithapi.adapting.CommentAdapter;
import com.classera1.newtaskwithapi.database.DBHelper;
import com.classera1.newtaskwithapi.serviceside.api.Links;
import com.classera1.newtaskwithapi.serviceside.connection.serverrequest.Networking;
import com.classera1.newtaskwithapi.serviceside.parser.Parser;

/**
 * Created by classera 1 on 24/10/2015.
 */
public class DeleteComment extends AsyncTask<Void,Void,String> {

    Context context;
    String uid, cid,lid;
    ProgressDialog dialog;
    CommentAdapter adapter;
    DBHelper DB;

    public DeleteComment(Context context,String uid, String cid,String lid,CommentAdapter adapter) {
        this.context = context;
        this.uid = uid;
        this.cid = cid;
        this.lid = lid;
        this.adapter = adapter;
        dialog= new ProgressDialog(context);
        DB = new DBHelper(context);
        DB.deleteLessonCommentTable();
    }

    @Override
    protected String doInBackground(Void... params) {
        String result = new Parser(context).Parseers(Networking.postToUrl(Links.DELETE_COMMENT, "deletecomment", cid, uid));
        if(result.contains("DONE")) {
            //retrive the comments
            String result2 = new Parser(context).getCommetnsParser(Networking.postToUrl(Links.GET_COMMENTS+lid+Links.ENDOFURL,"CC",lid));
            if(result2.equals("DONE")) {
                //notifi adapter
                return "DONE";
            }else{
                return "DONE";
            }
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
        dialog.setMessage("Delete Comment..");
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        dialog.dismiss();
        asyncDone.onFinish(s);
        adapter.notifyDataSetChanged();
    }
}
