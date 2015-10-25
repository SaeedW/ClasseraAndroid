package com.classera1.newtaskwithapi.serviceside.requests;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.classera1.newtaskwithapi.database.DBHelper;
import com.classera1.newtaskwithapi.oop.Courses;
import com.classera1.newtaskwithapi.serviceside.api.Links;
import com.classera1.newtaskwithapi.serviceside.connection.serverrequest.Networking;
import com.classera1.newtaskwithapi.serviceside.parser.Parser;

import java.util.ArrayList;

/**
 * Created by classera 1 on 20/10/2015.
 */
public class GetCoursesAndLessonsAsync extends AsyncTask<Void,Void,String> {

    Context context;
    DBHelper DB;
    ArrayList<Courses> coursesData;
    ProgressBar dialog;

    public GetCoursesAndLessonsAsync(Context context,ProgressBar dialog){
        this.context = context;
        this.DB = new DBHelper(context);
        this.dialog = dialog;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog.setVisibility(View.VISIBLE);
    }

    @Override
    protected String doInBackground(Void... params) {

        // get the link -> // make the request -> // get the parsing result -> // return the value
        try {
            String result = new Parser(context).parseCourse(Networking.getStringFromURL(Links.COURSES_URL+Links.ENDOFURL));
            if(result.equals("DONE")){
                this.coursesData = DB.getCourses();
                for(int i=0;i<coursesData.size();i++){
                    String id = coursesData.get(i).getId();
                    getTheLessonsByCoursesId(id);
                }
                return "DONE";
            }else{
                return "ERROR";
            }
        }catch(Exception e){
            Log.d("ERROR", "ERROR IN REQUEST  " + e.getMessage());
            return "ERROR";
        }

    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        asyncDone.onFinish(s);
        dialog.setVisibility(View.GONE);
    }

    private void getTheLessonsByCoursesId(String id) {
    new Parser(context).parseLessons(Networking.getStringFromURL(Links.LESSON_URL+id+Links.ENDOFURL));
    }

    public asyncDoneLoading asyncDone;

    public void asyncDone(asyncDoneLoading asyncDoneLoading) {
        asyncDone = asyncDoneLoading;
    }

    public interface asyncDoneLoading {
        public void onFinish(String s);
    }
}
