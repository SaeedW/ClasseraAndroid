package com.classera1.newtaskwithapi.util;

/**
 * Created by classera 1 on 21/10/2015.
 */
public enum ErrorCodes {

     NOTFOUND(442);

    private int value;

    private ErrorCodes(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
