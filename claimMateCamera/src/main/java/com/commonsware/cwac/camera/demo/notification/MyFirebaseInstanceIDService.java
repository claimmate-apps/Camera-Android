package com.commonsware.cwac.camera.demo.notification;

import android.util.Log;

import com.commonsware.cwac.camera.demo.other.Helper;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private String TAG = this.getClass().getSimpleName();

    @Override
    public void onTokenRefresh() {

        Log.i(TAG, "token = " + FirebaseInstanceId.getInstance().getToken());
        new Helper(this);
        Helper.setToken(FirebaseInstanceId.getInstance().getToken());
    }
}
