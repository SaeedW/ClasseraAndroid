package com.classera1.newtaskwithapi;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.classera1.newtaskwithapi.database.DBHelper;
import com.classera1.newtaskwithapi.oop.User;
import com.classera1.newtaskwithapi.serviceside.connection.connectivity.Connection;
import com.classera1.newtaskwithapi.serviceside.requests.CheckUserAsync;
import com.classera1.newtaskwithapi.serviceside.requests.CheckValidUser;
import com.classera1.newtaskwithapi.util.UserInformation;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText userNickName;
    Button loginButton;
    DBHelper DB;
    String deviceid, email;
    ArrayList<User> userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //declare the views
        declare();

        // listeners
        listener();

//        String deviceId = UserInformation.getUniqueDeviceID(MainActivity.this); // get unique one
//        String in2 = UserInformation.getdeviceId(MainActivity.this); // must try the sim card and then test
    }

    private void listener() {
        // check if it valid user
        userData = DB.getUser();
        try {
            Intent x = getIntent();
            String r = x.getStringExtra("notvalid5lyysjlm3lm");
            if (!r.equals("ss")) {

                if(userData.size()>0){
                    checkValidateUser();
                }

            }
        }catch(Exception e){
            if(userData.size()>0){
            checkValidateUser();
            }
        }

        // login button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateNickname(userNickName)) {
                    DB.deleteUsersTable();
                    //get the email
                    email = UserInformation.getEmail(MainActivity.this);
                    //get the device id
                    deviceid = UserInformation.getUniqueDeviceID(MainActivity.this);
                    if (new Connection(MainActivity.this).isOnline()) {
                        //start the async
//                        CheckUserAsync userChecking = new CheckUserAsync(MainActivity.this, email, userNickName.getText().toString(), deviceid);
                        DB.inserUserloginedDone("1","xxx@yahoo.com",userNickName.getText().toString(),deviceid);
                        CheckUserAsync userChecking = new CheckUserAsync(MainActivity.this, "xxx@yahoo.com", userNickName.getText().toString(), deviceid);
                        userChecking.asyncDone(new CheckUserAsync.asyncDoneLoading() {
                            @Override
                            public void onFinish(String s) {
                                if (s.equals("DONE")) {
                                    // go to the main activity
                                    //check the validate User
                                    startActivity(new Intent(MainActivity.this, SplashScreen.class).putExtra("ERROR", s));
                                } else if(s.equals("ERRORN")){
                                    Toast.makeText(MainActivity.this,"ERROR",Toast.LENGTH_LONG).show();
                                }
                                else {
                                    //something error
                                    //send the error to the splashscreen
//                                    DB.inserUserloginedDone("1", "ssss", "x", "x");
                                    startActivity(new Intent(MainActivity.this, SplashScreen.class).putExtra("ERROR", s));
                                }
                            }
                        });
                        userChecking.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    } else {
                        Snackbar.make(v, "Check Your Connection!", Snackbar.LENGTH_LONG)
                                .show();
                    }
                } else {
                    Snackbar.make(v, "Nick Name Cannot Be Empty!", Snackbar.LENGTH_LONG)
                            .show();
                }
            }
        });
    }

    private void checkValidateUser() {
        if (new Connection(MainActivity.this).isOnline()) {
            String _deviceId = UserInformation.getUniqueDeviceID(MainActivity.this);
//           CheckValidUser validUser = new CheckValidUser(MainActivity.this, email, userNickName.getText().toString(), _deviceId);
            CheckValidUser validUser = new CheckValidUser(MainActivity.this, "xx@yahoo.com", userNickName.getText().toString(), _deviceId);
            validUser.asyncDone(new CheckValidUser.asyncDoneLoading() {
                @Override
                public void onFinish(String s) {
                    if (s.equals("DONE")) {
                        // go to the main activity
                        //check the validate User
                        startActivity(new Intent(MainActivity.this, SplashScreen.class).putExtra("ERROR", s));
                    }
                    else if(s.equals("ERRORN")){
                        Toast.makeText(MainActivity.this,"ERROR IN SERVER", Toast.LENGTH_LONG).show();
                    }
                    else {
                        //something error
                        //send the error to the splashscreen
                        startActivity(new Intent(MainActivity.this, SplashScreen.class).putExtra("ERROR", "User is NOT Licensed"));
                    }
                }
            });
            validUser.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }

    private void declare() {
        userNickName = (EditText) findViewById(R.id.nicknameedit);
        loginButton = (Button) findViewById(R.id.login_btn);
        DB = new DBHelper(MainActivity.this);
    }

    public boolean validateNickname(EditText name) {
        String nickName = name.getText().toString();
        return !(nickName.isEmpty());
    }
}
