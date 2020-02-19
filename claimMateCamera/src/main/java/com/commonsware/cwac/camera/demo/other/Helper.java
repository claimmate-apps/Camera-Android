package com.commonsware.cwac.camera.demo.other;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.misc.DiskLruCache;

public class Helper {

    static SharedPreferences sp;
    static SharedPreferences.Editor editor;

    public Helper(Context context) {
        sp  = context.getSharedPreferences("ClaimMate", Context.MODE_PRIVATE);
        editor = sp.edit();
    }

    public static void setToken(String token) {
        editor.putString("token", token);
        editor.commit();
    }

    public static String getToken() {
        return sp.getString("token", "null");
    }

    public static void setDeviceRegister(boolean b) {
        editor.putBoolean("register", b);
        editor.commit();
    }

    public static boolean isDeviceRegister() {
        return sp.getBoolean("register", false);
    }

    public static boolean isFirstTime() {
        return sp.getBoolean("isFirstTime", true);
    }

    public static void setFirstTime() {
        editor.putBoolean("isFirstTime", false);
        editor.commit();
    }
}
