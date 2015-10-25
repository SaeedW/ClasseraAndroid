package com.classera1.newtaskwithapi.serviceside.api;

/**
 * Created by classera 1 on 19/10/2015.
 */
public class Links {

    public static final String IP = "http://192.168.88.227";
    public static final String LESSON_URL = IP + "/cakeApi/Subjects/ListSubjects/";
    public static final String ENDOFURL = ".json";
    public static final String COURSES_URL = IP + "/cakeApi/Courses.json";
    public static final String CHECK_USER_VALID_URL = IP + "/cakeApi/Users/add";
    public static final String SEND_COMMENT_URL = "http://192.168.88.228/course/comments/addComment/";
    public static final String CHECK_USER_LISENSE =  IP +"/ClassAndAPI/Licenses/check";
    public static final String GET_COMMENTS = "http://192.168.88.228/course/comments/getComments/";
    public static final String DELETE_COMMENT = "http://192.168.88.228/course/comments/deleteComment/.json";
}
