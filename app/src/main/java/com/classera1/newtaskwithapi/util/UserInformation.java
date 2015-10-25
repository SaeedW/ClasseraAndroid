package com.classera1.newtaskwithapi.util;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.telephony.TelephonyManager;

/**
 * Created by classera 1 on 19/10/2015.
 */
public class UserInformation {

    /*
       Classs to get the email also the device id for the user.
        */

    public static Account getAccount(AccountManager accountManager) {
        Account[] accounts = accountManager.getAccountsByType("com.google");
        Account account;
        if (accounts.length > 0) {
            account = accounts[0];
        } else {
            account = null;
        }
        return account;
    }

    public static String getEmail(Context context) {
        AccountManager accountManager = AccountManager.get(context);
        Account account = getAccount(accountManager);

        if (account == null) {
            return null;
        } else {
            return account.name;
        }
    }


    public static String getdeviceId(Context context) {
        TelephonyManager tManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        String uid = tManager.getDeviceId();
        return uid;
    }

    public static String getUniqueDeviceID(Context context)
    {
        ContentResolver contentResolver = context.getContentResolver();
        String id = android.provider.Settings.System.getString(contentResolver, android.provider.Settings.System.ANDROID_ID);
        return id;
    }



}
