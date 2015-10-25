package com.classera1.newtaskwithapi.util;

import android.text.TextUtils;
import android.util.Patterns;
import android.widget.EditText;

/**
 * Created by Saeed on 9/28/2015.
 * Validating form
 */
public class Validation {

    public Validation(){}

    public static boolean isValidateText(EditText inputName) {
        if (inputName.getText().toString().trim().isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    public boolean validateEmail(EditText inputEmail) {
        String email = inputEmail.getText().toString().trim();
        if (email.isEmpty() || !isValidEmail(email)) {
            return false;
        } else {
            return true;
        }
    }

    public boolean validatePassword(EditText inputPassword) {
        if (inputPassword.getText().toString().trim().isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


}
