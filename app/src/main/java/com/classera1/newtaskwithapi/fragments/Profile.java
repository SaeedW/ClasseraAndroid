package com.classera1.newtaskwithapi.fragments;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Toast;

import com.classera1.newtaskwithapi.R;
import com.classera1.newtaskwithapi.database.DBHelper;
import com.classera1.newtaskwithapi.oop.User;
import com.classera1.newtaskwithapi.serviceside.requests.UpdateUserInformationAsync;

import java.util.ArrayList;

/**
 * Created by classera 1 on 19/10/2015.
 */
public class Profile extends Fragment {

    View profile_view;
    EditText nickName, uEmail, uStatus, uHomeTown, uLanguage;
    RadioButton rMale, rFemale;
    ImageButton uImage;
    Button saveButton;
    ArrayList<User> userData;
    DBHelper DB;
    // Image loading result to pass to startActivityForResult method.
    private static int LOAD_IMAGE_RESULTS = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        profile_view = inflater.inflate(R.layout.activity_profile, container, false);

        //Declare the views
        declare();

        //set the values
        setValues();

        //listeners
        listener();


        return profile_view;
    }

    private void listener() {

        uImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create the Intent for Image Gallery.
                Intent i = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                // Start new activity with the LOAD_IMAGE_RESULTS to handle back the
                // results when image is picked from the Image Gallery.
                startActivityForResult(i, LOAD_IMAGE_RESULTS);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatingInfo();
            }
        });
    }

    private void updatingInfo() {
        // get the new values

        // run the update async task

        // for now just update the local database
        if (nickName.getText().toString().isEmpty()) {
            nickName.setError("Required Feild");
        } else if (uEmail.getText().toString().isEmpty()) {
            uEmail.setError("Required Feild");
        } else {
            UpdateUserInformationAsync updateInfo = new UpdateUserInformationAsync(getActivity(), nickName.getText().toString(), uEmail.getText().toString());
            updateInfo.asyncDone(new UpdateUserInformationAsync.asyncDoneLoading() {
                @Override
                public void onFinish(String s) {
                    if (s.equals("DONE")) {
                        Toast.makeText(getActivity(), "Saved", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            updateInfo.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        }
    }

    private void setValues() {
        nickName.setText(userData.get(0).getName());
        uEmail.setText(userData.get(0).getEmail());
    }

    private void declare() {
        //Database
        DB = new DBHelper(getActivity());
        userData = DB.getUser();
        //Fileds
        nickName = (EditText) profile_view.findViewById(R.id.username);
        uEmail = (EditText) profile_view.findViewById(R.id.email);
        uStatus = (EditText) profile_view.findViewById(R.id.status);
        uHomeTown = (EditText) profile_view.findViewById(R.id.hometown);
        uLanguage = (EditText) profile_view.findViewById(R.id.userlanguage);
        //gender
        rMale = (RadioButton) profile_view.findViewById(R.id.r1);
        rFemale = (RadioButton) profile_view.findViewById(R.id.r2);
        //user image
        uImage = (ImageButton) profile_view.findViewById(R.id.upic);
        //save data
        saveButton = (Button) profile_view.findViewById(R.id.save_p_data);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Here we need to check if the activity that was triggers was the Image
        // Gallery.
        // If it is the requestCode will match the LOAD_IMAGE_RESULTS value.
        // If the resultCode is RESULT_OK and there is some data we know that an
        // image was picked.
        if (requestCode == LOAD_IMAGE_RESULTS && resultCode == Activity.RESULT_OK // result ok constant in activity class so must type the name of class in fragment
                && data != null) {
            // Let's read picked image data - its URI
            Uri pickedImage = data.getData();
            // Let's read picked image path using content resolver
            String[] filePath = {MediaStore.Images.Media.DATA};
            Cursor cursor = getActivity().getContentResolver().query(pickedImage, filePath,
                    null, null, null);
            cursor.moveToFirst();
            String imagePath = cursor.getString(cursor
                    .getColumnIndex(filePath[0]));

            // Now we need to set the GUI ImageView data with data read from the
            // picked file.
            uImage.setImageBitmap(BitmapFactory.decodeFile(imagePath));

            // At the end remember to close the cursor or you will end with the
            // RuntimeException!
            cursor.close();
        }
    }
}
