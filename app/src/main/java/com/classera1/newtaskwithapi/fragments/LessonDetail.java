package com.classera1.newtaskwithapi.fragments;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.VideoView;

import com.classera1.newtaskwithapi.R;
import com.classera1.newtaskwithapi.activites.FullVideoActivity;
import com.classera1.newtaskwithapi.adapting.CommentAdapter;
import com.classera1.newtaskwithapi.database.DBHelper;
import com.classera1.newtaskwithapi.oop.Lessons;
import com.classera1.newtaskwithapi.oop.User;
import com.classera1.newtaskwithapi.serviceside.connection.connectivity.Connection;
import com.classera1.newtaskwithapi.serviceside.requests.GetCommentsAsync;
import com.classera1.newtaskwithapi.serviceside.requests.SendCommentAsync;
import com.classera1.newtaskwithapi.util.Validation;

import java.util.ArrayList;


public class LessonDetail extends Fragment {

    View lessonDetailView;
    VideoView videoView;
    ImageButton playButton;
    TextView descriptionText, noComment;
    ListView listOfComments;
    EditText commentEdit;
    Button sendComments;
    String lessonId, userId;
    DBHelper DB;
    ArrayList<User> userData;
    ArrayList<Lessons> lessonData;
    CommentAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        lessonDetailView = inflater.inflate(R.layout.fragment_lesson_detail, container, false);

        // declare the views
        declare();

        //get the data
        getingData();

        //listeners
        listeners();

        //onBackPressed
        BackButtonPressed();

        return lessonDetailView;
    }

    private void getingData() {

        // check the connection

        // check the database

        // if kolhm null

        // run el async

        // else

        // jeb data men el db

        // bna2n 3la el lesson id

        Bundle bundle = this.getArguments();
        lessonId = bundle.getString("lesson_id");
        lessonData = DB.getLessonDetailById(lessonId);
        descriptionText.setText(lessonData.get(0).getDesc());
        DB.deleteLessonCommentTable();
        GetCommentsAsync getCommentsAsync = new GetCommentsAsync(getActivity(), lessonId);
        getCommentsAsync.asyncDone(new GetCommentsAsync.asyncDoneLoading() {
            @Override
            public void onFinish(String s) {
                if (s.equals("DONE")) {
                    noComment.setVisibility(View.GONE);
                    listOfComments.setAdapter(adapter);
                } else {
                    noComment.setVisibility(View.VISIBLE);
                    noComment.setGravity(17);
                    noComment.setText(Html.fromHtml("<b> No Comments For This Lesson </b>"));
                }
            }
        });
        getCommentsAsync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    private void listeners() {
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getActivity(), FullVideoActivity.class).putExtra("url", lessonData.get(0).getVideoUrl()));
            }
        });
        sendComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Validation.isValidateText(commentEdit)) {

                    // get the user id
                    userId = userData.get(0).getId();

                    // check the connection
                    if (new Connection(getActivity()).isOnline()) {

                        // run the asyncTask
                        sendCommentAsyc();

                        // if every thing is right

                        // // print done and update the list from the asyncTask
                    } else {

                        Snackbar.make(lessonDetailView, "Check Your Connection!", Snackbar.LENGTH_INDEFINITE)
                                .setAction("Retry", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        sendCommentAsyc();
                                    }
                                })
                                .show();

                    }
                } else {
                    commentEdit.setError("Cannot Be Empty");
                }
            }
        });
    }

    private void sendCommentAsyc() {
        SendCommentAsync sendComment = new SendCommentAsync(getActivity(), listOfComments, userId, lessonId, commentEdit.getText().toString().trim());
        sendComment.asyncDone(new SendCommentAsync.asyncDoneLoading() {
            @Override
            public void onFinish(String s) {
                if (s.equals("DONE")) {
                    Snackbar.make(lessonDetailView, "Thank You!", Snackbar.LENGTH_LONG).show();
                    commentEdit.setText("");
                    adapter.notifyDataSetChanged();
                } else {
                    Snackbar.make(lessonDetailView, "Try Again", Snackbar.LENGTH_LONG).show();
                }
            }
        });
        sendComment.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }


    private void declare() {
        videoView = (VideoView) lessonDetailView.findViewById(R.id.videoView);
        playButton = (ImageButton) lessonDetailView.findViewById(R.id.btn_play);
        descriptionText = (TextView) lessonDetailView.findViewById(R.id.lesson_edit);
        listOfComments = (ListView) lessonDetailView.findViewById(R.id.comment_list);
        commentEdit = (EditText) lessonDetailView.findViewById(R.id.comment_edit);
        sendComments = (Button) lessonDetailView.findViewById(R.id.sendCommentButton);
        DB = new DBHelper(getActivity());
        userData = DB.getUser();
        adapter = new CommentAdapter(getActivity(), lessonId);
        noComment = (TextView) lessonDetailView.findViewById(R.id.noc);
        noComment.setVisibility(View.GONE);
    }

    private void BackButtonPressed() {
        lessonDetailView.setFocusableInTouchMode(true);
        lessonDetailView.requestFocus();
        lessonDetailView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    getFragmentManager().beginTransaction()
                            .replace(R.id.content, new Profile())
                            .commit();
                    return true;
                } else {
                    return false;
                }
            }
        });
    }

}
