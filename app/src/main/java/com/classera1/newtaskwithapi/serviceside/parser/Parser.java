package com.classera1.newtaskwithapi.serviceside.parser;

import android.content.Context;

import com.classera1.newtaskwithapi.database.DBHelper;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by classera 1 on 19/10/2015.
 */
public class Parser {

    private static DBHelper DB;
    private static Context context;
    static String message;

    public Parser(Context context) {
        this.context = context;
        DB = new DBHelper(context);
    }

    public static String parseLessons(String response) {
        if (response != null) {
            try {
                // start parse the objects
                JSONObject data = new JSONObject(response);
                JSONArray arrayData = data.getJSONArray("subjects_Json");
                for (int i = 0; i < arrayData.length(); i++) {
                    JSONObject obj = arrayData.getJSONObject(i);
                    String id = obj.getString("id");
                    String cid = obj.getString("course_id");
                    String name = obj.getString("title");
                    String desc = obj.getString("description");
                    String videoUrl = obj.getString("video_url");
                    DB.insertLessons(id, cid, name, desc, videoUrl);
                }
                return "DONE";
            } catch (Exception e) {
                return "ERROR";
            }

        } else {
            return "ERROR";
        }
    }

    public static String parseCourse(String response) {
        if (response != null) {
            try {
                // start parse the objects
                JSONObject data = new JSONObject(response);
                JSONArray arrayData = data.getJSONArray("Courses_Json");
                for (int i = 0; i < arrayData.length(); i++) {
                    JSONObject obj = arrayData.getJSONObject(i);
                    String id = obj.getString("id");
                    String name = obj.getString("name");
                    DB.inserCourses(id, name);
                }
                return "DONE";
            } catch (Exception e) {
                return "ERROR";
            }

        } else {
            return "ERROR";
        }
    }

    public static String Parseers(String response) {
        try {
            JSONObject obj = new JSONObject(response);
            String status = obj.getString("status");
            String message = obj.getString("message");
            if (status.equals("true")) {
                return "DONE";
            } else {
                return "ERROR";
            }
        } catch (Exception e) {
            return e.getMessage().toString();
        }
    }


    public static String loginUser(String response){
        try {
            JSONObject obj = new JSONObject(response);
            String status = obj.getString("status");
            if (status.equals("true")) {
                String uid = obj.getString("user_id");
                DB.updateUserId(uid);
                return "DONE";
            } else {
                return "ERROR";
            }
        } catch (Exception e) {
            return e.getMessage().toString();
        }
    }

    public static String getCommetnsParser(String response) {
        try {
        JSONObject obj = new JSONObject(response);
            String status = obj.getString("status");
            if(status.equals("true")){
                JSONArray array = obj.getJSONArray("comments");
                for(int i = 0 ; i < array.length() ; i++){
                    JSONObject obj2 = array.getJSONObject(i);
                    String id = obj2.getString("id");
                    String uid = obj2.getString("user_id");
                    String lid = obj2.getString("subject_id");
                    String body = obj2.getString("body");
                    DB.insertLessonComment(id,uid,lid,body);
                }
                return "DONE";
            }else{
                return "ERROR";
            }
        } catch (Exception e) {
            return "ERROR";
        }


    }
}
