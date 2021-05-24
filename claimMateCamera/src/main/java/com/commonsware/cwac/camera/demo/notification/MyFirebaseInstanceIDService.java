package com.commonsware.cwac.camera.demo.notification;

import android.util.Log;

import androidx.annotation.NonNull;

import com.commonsware.cwac.camera.demo.other.Helper;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;

public class MyFirebaseInstanceIDService extends FirebaseMessagingService {

    private String TAG = this.getClass().getSimpleName();

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Log.i(TAG, "token = " + s);
        new Helper(this);
        Helper.setToken(s);
    }

//    @Override
//    public void onTokenRefresh() {
//        Log.i(TAG, "token = " + FirebaseInstanceId.getInstance().getToken());
//        new Helper(this);
//        Helper.setToken(FirebaseInstanceId.getInstance().getToken());
//    }
}
