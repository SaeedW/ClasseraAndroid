package com.classera1.newtaskwithapi.activites;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import com.classera1.newtaskwithapi.R;

/**
 Full Screen Activity to play the streaming video
 */
public class FullVideoActivity extends AppCompatActivity {

    VideoView videoView;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);

        //get the name of the lesson
        // from previous fragment

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("");// name of lesseon
        }

        //get the url
        Intent i  = getIntent();
        url  = i.getStringExtra("url");

        //play the fulll screen video
        videoView = (VideoView) this.findViewById(R.id.videoViewfull);
        MediaController mc = new MediaController(this);
        videoView.setMediaController(mc);
        videoView.setVideoURI(Uri.parse(url));//get the video from db
        videoView.requestFocus();
        videoView.start();

    }
}