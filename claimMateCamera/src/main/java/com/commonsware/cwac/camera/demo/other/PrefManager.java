package com.commonsware.cwac.camera.demo.other;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PrefManager {

    static SharedPreferences sharedPreferences;
    static SharedPreferences.Editor editor;

    public PrefManager(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = sharedPreferences.edit();
    }

    public static void setUserId(String userId) {
        editor.putString("user_id", userId);
        editor.commit();
    }

    public static String getUserId() {
        return sharedPreferences.getString("user_id", "");
    }

    public static void setFullName(String fullName) {
        editor.putString("fullName", fullName);
        editor.commit();
    }

    public static String getFullName() {
        return sharedPreferences.getString("fullName", "");
    }

    public static String getClaimId() {
        return sharedPreferences.getString("claimId", "0");
    }

    public static void setClaimId(String claimId) {
        editor.putString("claimId", claimId);
        editor.commit();
    }

    public static void logout() {
        editor.clear();
        editor.commit();
    }
}