package com.classera1.newtaskwithapi;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.classera1.newtaskwithapi.database.DBHelper;
import com.classera1.newtaskwithapi.serviceside.connection.connectivity.Connection;
import com.classera1.newtaskwithapi.serviceside.requests.GetCoursesAndLessonsAsync;

public class SplashScreen extends AppCompatActivity {


    ProgressBar dialog;
    TextView errorText;
    RelativeLayout layout;
    GetCoursesAndLessonsAsync async;
    DBHelper DB;
    Button signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        //decalre the views
        declare();

        //get the data
        getData();
    }

    public void declare() {
        dialog = (ProgressBar) findViewById(R.id.dialog);
        errorText = (TextView) findViewById(R.id.text_error);
        layout = (RelativeLayout) findViewById(R.id.main_layout_splash);
        DB = new DBHelper(SplashScreen.this);
        signUp = (Button)findViewById(R.id.signup);
    }

    public void showSnack(String text) {
        Snackbar.make(layout, text, Snackbar.LENGTH_INDEFINITE)
                .setAction("Retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getData();
                        errorText.setVisibility(View.VISIBLE);
                        dialog.setVisibility(View.VISIBLE);
                    }
                })
                .show();
    }

    public void getData() {

        Intent i = getIntent();
        String state = i.getStringExtra("ERROR");
        if (!state.equals("DONE")) {
            errorText.setText(state);
            dialog.setVisibility(View.GONE);
            signUp.setVisibility(View.VISIBLE);
            signUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(SplashScreen.this,MainActivity.class).putExtra("notvalid5lyysjlm3lm","ss"));
                }
            });

        } else {
            signUp.setVisibility(View.GONE);
            if (new Connection(SplashScreen.this).isOnline()) {
                //clean ur db
                DB.deleteCoursesTable();
                DB.deleteLessonsTable();
                //get new data
                async = new GetCoursesAndLessonsAsync(SplashScreen.this, dialog);
                async.asyncDone(new GetCoursesAndLessonsAsync.asyncDoneLoading() {
                    @Override
                    public void onFinish(String s) {
                        if (s.equals("DONE")) {
                            // start the new activity
                            errorText.setVisibility(View.GONE);
                            startActivity(new Intent(SplashScreen.this, Home.class));
                        } else {
                            errorText.setVisibility(View.GONE);
                            dialog.setVisibility(View.GONE);
                            showSnack("Server Error");
                        }
                    }
                });
                async.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else {
                showSnack("Check Your Connection");
                errorText.setVisibility(View.GONE);
                dialog.setVisibility(View.GONE);
            }

        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent x= new Intent(Intent.ACTION_MAIN);
        x.addCategory(Intent.CATEGORY_HOME);
        x.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        x.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(x);
    }
}
