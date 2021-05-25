
package com.commonsware.cwac.camera.demo.activities;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;

import com.commonsware.cwac.camera.demo.HomeActivity;
import com.commonsware.cwac.camera.demo.other.Helper;
import com.commonsware.cwac.camera.demo.other.PrefManager;
import com.example.claimmate.R;


public class SplashScreenActivity extends Activity {

    private final int SPLASH_DISPLAY_LENGTH = 3000;
    private ProgressBar spinner;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.splashscreen);

        new Helper(this);
        new PrefManager(this);

        spinner = findViewById(R.id.progressBar);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (checkPermission()) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.WAKE_LOCK, Manifest.permission.READ_PHONE_STATE}, 101);
        } else {
            startHome();
        }
    }

    public boolean checkPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED  || ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 101 && (grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
            startHome();
        } else {
            showPermissionDialog();
        }
    }

    public void showPermissionDialog() {

        new androidx.appcompat.app.AlertDialog.Builder(SplashScreenActivity.this)
                .setMessage(
                        getString(R.string.error_permission))
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        goToSetting();
                    }
                }).setCancelable(false).show();
    }

    void goToSetting() {
        Intent loAppSettings = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS", Uri.parse("package:" + getPackageName()));
        loAppSettings.addCategory(Intent.CATEGORY_DEFAULT);
        loAppSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivityForResult(loAppSettings, 102);
    }

    private void startHome() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                spinner.setVisibility(View.GONE);

                String struid = PrefManager.getUserId();

                Log.d("User-Id===>", "===============>" + PrefManager.getUserId());

                /*if (Helper.isFirstTime()) {
                    Intent i = new Intent(SplashScreenActivity.this, OneTimeActivity.class);
                    startActivity(i);
                    finish();
                } else */
                if (struid.equals("")) {
                    Intent i = new Intent(SplashScreenActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                } else {
//                    Intent i = new Intent(SplashScreenActivity.this, SwipeUpActivity.class);
                    Intent i = new Intent(SplashScreenActivity.this, HomeActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
