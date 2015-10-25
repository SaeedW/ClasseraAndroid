package com.classera1.newtaskwithapi.adapting;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.classera1.newtaskwithapi.R;
import com.classera1.newtaskwithapi.database.DBHelper;
import com.classera1.newtaskwithapi.oop.Comments;
import com.classera1.newtaskwithapi.oop.User;
import com.classera1.newtaskwithapi.serviceside.connection.connectivity.Connection;
import com.classera1.newtaskwithapi.serviceside.requests.DeleteComment;


import java.util.ArrayList;

/**
 * Created by classera 1 on 21/10/2015.
 */
public class CommentAdapter extends BaseAdapter {

    Context context;
    DBHelper DB;
    ArrayList<Comments> commentData;
    ArrayList<User> userData;
    LayoutInflater layoutInflater;
    TextView comment;
    Button delete;
    String uid, lid;

    public CommentAdapter() {
    }

    public CommentAdapter(Context context, String lid) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.DB = new DBHelper(context);
        this.commentData = DB.getLessonCommentsByLessonId();
        this.userData = DB.getUser();
        this.lid = lid;
    }

    @Override
    public int getCount() {
        return commentData.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.custom_comment, parent, false);
        }
        uid = userData.get(0).getId();
        final String luid = commentData.get(position).getUid();
        comment = (TextView) convertView.findViewById(R.id.comment_item);
        delete = (Button) convertView.findViewById(R.id.deletecomment);
        comment.setText(commentData.get(position).getText());
        if (uid.equals(luid)) {
            delete.setVisibility(View.VISIBLE);
        } else {
            delete.setVisibility(View.GONE);
        }
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDeleteDialog(position, "Delete Comment!", "Are You Sure", "Ok", "Cancel");
            }
        });
        return convertView;
    }


    public void ShowDeleteDialog(final int pos, String title, String message, String ok, String cancel) {
        AlertDialog.Builder alertDialogBuild = new AlertDialog.Builder(context);
        alertDialogBuild.setMessage(message)
                .setTitle(title)
                .setCancelable(false)
                .setPositiveButton(ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (new Connection(context).isOnline()) {
                            DeleteComment deleteComment = new DeleteComment(context, uid, commentData.get(pos).getId(), lid, new CommentAdapter());
                            deleteComment.asyncDone(new DeleteComment.asyncDoneLoading() {
                                @Override
                                public void onFinish(String s) {
                                    if (s.equals("DONE")) {
                                        Toast.makeText(context, "Deleted", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(context, "Deleted", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                            deleteComment.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        } else {
                            Toast.makeText(context, "Check Your Connection !", Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .setNegativeButton(cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuild.create();
        alertDialog.show();
    }
}