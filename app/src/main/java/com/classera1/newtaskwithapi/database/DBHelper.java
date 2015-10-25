package com.classera1.newtaskwithapi.database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.classera1.newtaskwithapi.oop.Comments;
import com.classera1.newtaskwithapi.oop.Courses;
import com.classera1.newtaskwithapi.oop.Lessons;
import com.classera1.newtaskwithapi.oop.User;

import java.util.ArrayList;


/**
 * Created by Saeed on 1/10/2015.
 */
public class DBHelper extends SQLiteOpenHelper {


    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "classeraextwo";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    // Table Names
    private static final String TABLE_USER = "user";
    private static final String TABLE_COURSES = "courses";
    private static final String TABLE_LESSONS = "lessons";
    private static final String TABLE_LESSON_COMMENTS = "comments";

    // Common column names
    private static final String KEY_ID = "id";

    //user table
    private static final String CREATE_TABLE_USER = "CREATE TABLE "
            + TABLE_USER + "(" + KEY_ID + " TEXT," + "email" + " TEXT," + "name" + " TEXT," + "deviceid" + " TEXT" + ")";
    //courses table
    private static final String CREATE_TABLE_COURSES = "CREATE TABLE " + TABLE_COURSES
            + "(" + KEY_ID + " INTEGER ," + "name" + " TEXT" + ")";
    //table lessons
    private static final String CREATE_TABLE_LESSONS = "CREATE TABLE "
            + TABLE_LESSONS + "(" + KEY_ID + " INTEGER ,"
            + "cid" + " TEXT," + "desc" + " TEXT," + "videoUrl" + " TEXT," + "name" + " TEXT  )";
    //table lesson comments
    private static final String CREATE_TABLE_LESSON_COMMENT = "CREATE TABLE "
            + TABLE_LESSON_COMMENTS + "(" + KEY_ID + " TEXT ," + "" + "uid" + " TEXT ," + "lid" + " TEXT," + "comment" + " TEXT" + ")";

    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating required tables
        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_COURSES);
        db.execSQL(CREATE_TABLE_LESSONS);
        db.execSQL(CREATE_TABLE_LESSON_COMMENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_COURSES);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_LESSONS);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_LESSON_COMMENT);
        // create new tables
        onCreate(db);
    }

    ////////////////////////////////////////// Users Section\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    public ArrayList<User> getUser() {
        ArrayList<User> userData = new ArrayList<User>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM user", null);
        if (c.moveToFirst()) {
            do {
                User uData = new User();
                uData.setId(c.getString(0));
                uData.setEmail(c.getString(1));
                uData.setName(c.getString(2));
                uData.setDeviceid(c.getString(3));
                userData.add(uData);
            } while (c.moveToNext());
        }
        return userData;
    }

    public void inserUserloginedDone(String id,String email, String name, String deviceid) {
        //object of class database
        //get writable to write data on database
        SQLiteDatabase db = this.getWritableDatabase();
        //Content values
        ContentValues content = new ContentValues();
        content.put("id", id);
        content.put("email", email);
        content.put("name", name);
        content.put("deviceid", deviceid);
        db.insert(TABLE_USER, null, content);
    }

    public void deleteUsersTable() {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TABLE_USER, null, null);
        } catch (Exception e) {
            Log.d("DB", "ERROR IN DATABASE 'delete function'");
        }
    }

    public void updateUserData(String name,String email){
        SQLiteDatabase db = this.getWritableDatabase();
        //Content values
        ContentValues content = new ContentValues();
        content.put("email", email);
        content.put("name", name);
        db.update(TABLE_USER,content,null,null);
    }

    public void updateUserId(String uid){
        SQLiteDatabase db = this.getWritableDatabase();
        //Content values
        ContentValues content = new ContentValues();
        content.put("id", uid);
        db.update(TABLE_USER,content,null,null);
    }

    ////////////////////////////////////////// Users Section\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\


    ////////////////////////////////////////// COURSES Section\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

    public void inserCourses(String id, String name) {
        //object of class database
        //get writable to write data on database
        SQLiteDatabase db = this.getWritableDatabase();
        //Content values
        ContentValues content = new ContentValues();
        content.put("id", id);
        content.put("name", name);
        db.insert("courses", null, content);
    }

    public void deleteCoursesTable() {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete("courses", null, null);
        } catch (Exception e) {
            Log.d("DB", "ERROR IN DATABASE 'delete function'");
        }
    }

    public ArrayList<Courses> getCourses() {
        ArrayList<Courses> coursesData = new ArrayList<Courses>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM courses", null);
        if (c.moveToFirst()) {
            do {
                Courses cData = new Courses();
                cData.setId(c.getString(0));
                cData.setName(c.getString(1));
                coursesData.add(cData);
            } while (c.moveToNext());
        }
        return coursesData;
    }

    ////////////////////////////////////////// COURSES Section\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\


    ////////////////////////////////////////// LESSONS Section\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

    public void insertLessons(String id, String cid, String name, String desc, String videoUrl) {
        //object of class database
        //get writable to write data on database
        SQLiteDatabase db = this.getWritableDatabase();
        //Content values
        ContentValues content = new ContentValues();
        content.put("id", id);
        content.put("cid", cid);
        content.put("name", name);
        content.put("desc", desc);
        content.put("videoUrl", videoUrl);
        db.insert("lessons", null, content);
    }

    public void deleteLessonsTable() {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete("lessons", null, null);
        } catch (Exception e) {
            Log.d("DB", "ERROR IN DATABASE 'delete function'");
        }
    }

    public ArrayList<Lessons> getLessonsById(String id) {
        ArrayList<Lessons> LessonsData = new ArrayList<Lessons>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM lessons WHERE cid ='" + id + "'", null);
        if (c.moveToFirst()) {
            do {
                Lessons lData = new Lessons();
                lData.setId(c.getString(0));
                lData.setCid(c.getString(1));
                lData.setName(c.getString(2));
                lData.setDesc(c.getString(3));
                lData.setVideoUrl(c.getString(4));
                LessonsData.add(lData);
            } while (c.moveToNext());
        }
        return LessonsData;
    }

    public ArrayList<Lessons> getLessonDetailById(String id) {
        ArrayList<Lessons> LessonsData = new ArrayList<Lessons>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM lessons WHERE id ='" + id + "'", null);
        if (c.moveToFirst()) {
            do {
                Lessons lData = new Lessons();
                lData.setId(c.getString(0));
                lData.setCid(c.getString(1));
                lData.setDesc(c.getString(2));
                lData.setVideoUrl(c.getString(3));
                lData.setName(c.getString(4));
                LessonsData.add(lData);
            } while (c.moveToNext());
        }
        return LessonsData;
    }

    ////////////////////////////////LESSON COMMENTS\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    public void deleteLessonCommentTable() {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete("comments", null, null);
        } catch (Exception e) {
            Log.d("DB", "ERROR IN DATABASE 'delete function'");
        }
    }

    public void insertLessonComment(String id,String uid, String lid, String comment) {
        //object of class database
        //get writable to write data on database
        SQLiteDatabase db = this.getWritableDatabase();
        //Content values
        ContentValues content = new ContentValues();
        content.put("id", id);
        content.put("uid", uid);
        content.put("lid", lid);
        content.put("comment", comment);
        db.insert("comments", null, content);
    }

    public ArrayList<Comments> getLessonCommentsByLessonId() {
        ArrayList<Comments> commentData = new ArrayList<Comments>();
        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor c = db.rawQuery("SELECT * FROM comments WHERE lid ='" + id + "'", null);
        Cursor c = db.rawQuery("SELECT * FROM comments", null);
        if (c.moveToFirst()) {
            do {
                Comments cData = new Comments();
                cData.setId(c.getString(0));
                cData.setUid(c.getString(1));
                cData.setLid(c.getString(2));
                cData.setText(c.getString(3));
                commentData.add(cData);
            } while (c.moveToNext());
        }
        return commentData;
    }

    ////////////////////////////////LESSON COMMENTS\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

    ////////////////////////////////////////// LESSONS Section\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

}

