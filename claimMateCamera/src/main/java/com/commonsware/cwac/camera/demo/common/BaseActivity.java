package com.commonsware.cwac.camera.demo.common;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.dovar.dtoast.DToast;
import com.example.claimmate.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.pixplicity.easyprefs.library.Prefs;

import java.io.File;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;

public class BaseActivity extends AppCompatActivity implements Handler.Callback {

    ///////*************** Date and Time ***********************//
    public enum Days implements Serializable {
        NO, SUN, MON, TUE, WED, THU, FRI, SAT
    }
    ///// ------------ String => Timestamp
    public static long getCurrentTimeStamp(){
        Timestamp timestamp = new Timestamp(new Date().getTime());
        try {
            long currentTime = timestamp.getTime();
            return currentTime;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    public static long getTimeTimestamp(String timeString){
        long timestamp = 0;
        //SimpleDateFormat format = new SimpleDateFormat("h:mm aa");
        SimpleDateFormat format = new SimpleDateFormat("h aa");
        //SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-d");
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MMM-d");
        try {
            Date date = format.parse(timeString);
            timestamp = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timestamp;
    }
    public static long getTimestamp(String timeString){
        long timestamp = 0;
        //SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        //SimpleDateFormat format = new SimpleDateFormat("MMM/dd/yyyy h:mm aa");
        SimpleDateFormat format = new SimpleDateFormat("MMM/dd/yyyy h aa");
        try {
            Date date = format.parse(timeString);
            timestamp = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timestamp;
    }
    public static long getTimestampMin(String timeString){
        long timestamp = 0;
        //SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyy hh:mm aa");
        try {
            Date date = format.parse(timeString);
            timestamp = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timestamp;
    }
    ///// ------------ Timestamp => String
    public static String getTimebyTimeZone(String timezoneID){
        SimpleDateFormat sdf = new SimpleDateFormat("MMM/dd\nh:mm aa");
        sdf.setTimeZone(TimeZone.getTimeZone(timezoneID));
        Date df = new Date(getCurrentTimeStamp());
        String timeString = sdf.format(df);
        return timeString;
    }
    public static int getDayOfWeek(long timeStamp){
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(timeStamp);
        int dayNum = c.get(Calendar.DAY_OF_WEEK);  // 1:Sunday ~ 7:saturday
        Log.d("dayNum==========>", String.valueOf(dayNum));
        Days day = Days.values()[dayNum];
        Log.d("dayNum==========>", String.valueOf(day));
        //return String.valueOf(day);
        return dayNum;
    }
    public static String getDateTimeString(long timeStamp){
        // long millis = new Date().getTime();
        Date df = new Date(timeStamp);
        String timeStampString = new SimpleDateFormat("dd MMM yyyy HH:mm").format(df);
        //String timeStampString = new SimpleDateFormat("HH:mm").format(df);
        return timeStampString;
    }
    public static String getDateString(long timeStamp){
        Date df = new Date(timeStamp);
        String timeStampString = new SimpleDateFormat("dd MMM yyyy").format(df);
        return timeStampString;
    }
    public static String getTimeString(long timeStamp){
        Date df = new Date(timeStamp);
        String timeStampString = new SimpleDateFormat("h aa").format(df);
        return timeStampString;
    }
    public static String getTimeMinString(long timeStamp){
        Date df = new Date(timeStamp);
        String timeStampString = new SimpleDateFormat("h:mm aa").format(df);
        return timeStampString;
    }
    public static String getMonthString(long timeStamp){
        Date df = new Date(timeStamp);
        String timeStampString = new SimpleDateFormat("MM/yyyy").format(df);
        return timeStampString;
    }
    public static String getDayString(long timeStamp){
        Date df = new Date(timeStamp);
        String timeStampString = new SimpleDateFormat("dd/MM/yyyy").format(df);
        return timeStampString;
    }
    public static String getYearString(long timeStamp){
        Date df = new Date(timeStamp);
        //String timeStampString = new SimpleDateFormat("MM dd, yyyy hh:mma").format(df);
        String timeStampString = new SimpleDateFormat("yyyy").format(df);
        return timeStampString;
    }
    public static String getNextYearString(){
        Date df = new Date(getCurrentTimeStamp());
        Date date = null;
        String timeStampString = new SimpleDateFormat("yyyy").format(df);
        timeStampString = String.valueOf(Integer.parseInt(timeStampString) + 1);
        SimpleDateFormat format = new SimpleDateFormat("yyyy");
        try {
            date = format.parse(timeStampString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return getYearString(date.getTime());
    }
    ///// ------------ Timestamp Compare


    //*********** Email & Phone Validate ***********************//
    public static boolean validateEmail(String email){
        String emailPattern = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        if (email.matches(emailPattern)) {
            return true;
        }
        else {
            return false;
        }
    }
    public static boolean isValidMobile(String phone) {
        boolean check = false;
        if (!Pattern.matches("[a-zA-Z]", phone)) {
            if (phone.length() < 9 || phone.length() > 15) {
                // if(phone.length() != 10) {
                check = false;
                // txtPhone.setError("Not Valid Number");
            } else {
                check = android.util.Patterns.PHONE.matcher(phone).matches();
            }
        } else {
            check = false;
        }
        return check;
    }


    //********************* Permission setting *********************//
    ////// permission check  //////
    private static final String[] PERMISSIONS = {
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
    };
    public void checkAllPermission() {

        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        if (hasPermissions(getApplicationContext(), PERMISSIONS)){

        }else {
            ActivityCompat.requestPermissions(this, PERMISSIONS, 101);
        }
    }
    public static boolean hasPermissions(Context context, String... permissions) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {

            for (String permission : permissions) {

                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
    //**************** UI setting ***********************//

    public void showToast1(final String message) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                DToast.make(BaseActivity.this)
                        .setText(R.id.tv_content_default, message)
                        .setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 120)
                        .show();
            }
        });


    }


    public void showToast(String content){

        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast, (ViewGroup) findViewById(R.id.toast_layout_root));
        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setText(content);

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 50);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }
    public void setupUI(View view, final Activity activity) {
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    try {
                        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                        if (imm.isAcceptingText()) {
                            hideSoftKeyboard(activity);
                        }
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    return false;
                }
            });
        }
        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView, activity);
            }
        }
    }
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        assert inputMethodManager != null;
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }
    public static void showSoftKeyboard(View view, Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

    //**************** 00:00 Formatting ***********************//
    public static String format2LenStr(int num) {

        return (num < 10) ? "0" + num : String.valueOf(num);
    }
    //**************** 00:00:00 Formatting Time to Seconds ***********************//
    public static int getTimeFromString(String time) {
        int timeValue = 0;
        String[] str = time.split(":");
        if (str.length == 2){
            timeValue += Float.parseFloat(str[1]);
            timeValue += Integer.parseInt(str[0]) * 60;
        } else if (str.length == 3){
            timeValue += Float.parseFloat(str[2]);
            timeValue += Integer.parseInt(str[1]) * 60;
            timeValue += Integer.parseInt(str[0]) * 60 * 60;
        }
        return timeValue;
    }


    void onFcmUploadToken(){
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("onFcmUploadToken:", "getInstanceId failed", task.getException());
                            return;
                        }
                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        Log.d("Token===!!!====>", token);
                    }
                });
    }

    public static void removeFile (String path) {
        File file = new File(path);
        if (file.exists()) {
            if (file.delete()) {
                Log.d("RemoveFile===>", "SUCCESS");
            } else {
                Log.d("RemoveFile===>", "FAIL");
            }
        } else {
            Log.d("RemoveFile===>", "FileNonExist: " + file.getPath());
        }
    }

    public static void removeAllFiles(File dir){
        Log.d("RemoveAllFile", "ROOT: " + dir.getPath());
        if (dir.isDirectory())
        {
            String[] children = dir.list();
            if (children != null){
                for (String child : children) {
                    File temp = new File(dir, child);
                    if (temp.isDirectory()) {
                        Log.d("RemoveAllFile", "Recursive Call: " + temp.getPath());
                        removeAllFiles(temp);
                    } else {
                        Log.d("RemoveAllFile", "SUCCESS: " + temp.getPath());
                        boolean b = temp.delete();
                        if (!b) {
                            Log.d("RemoveAllFile", "FAIL");
                        }
                    }
                }
            }
        }
        dir.delete();
    }



    public String getAddress(double ltt, double lgt)
    {

        try {
            Geocoder geo = new Geocoder(this.getApplicationContext(), Locale.getDefault());
            List<Address> addresses = geo.getFromLocation(ltt, lgt, 1);
            if (addresses.isEmpty()) {
            }
            else {
                if (addresses.size() > 0) {
                    String temp = addresses.get(0).getAddressLine(0);
                    /*country = addresses.get(0).getCountryName();
                    if (addresses.get(0).getLocality() == null)
                    {
                        city = addresses.get(0).getAdminArea();
                    } else {
                        city = addresses.get(0).getLocality();
                    }*/
                    return temp;
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace(); // getFromLocation() may sometimes fail
        }
        return "Undefined";
    }

    KProgressHUD hud = null;
    public Context _context = null;
    public boolean _isEndFlag;    // double click back button to finish the app
    public Handler _handler = null;

    @Override
    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);

        _context = this;
        _handler = new Handler(this);
        // Obtain the FirebaseAnalytics instance.

        // Initialize the Prefs class
        new Prefs.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(getPackageName())
                .setUseDefaultSharedPreference(true)
                .build();

    }

    public void showToast(int id) {
        showToast(getString(id));
    }


    public void showHUD(String label, String detail) {

        if (hud != null)
            hideHUD();

        hud = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel(label)
                .setDetailsLabel(detail)
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();
    }

    public void showHUD(String label) {

        if (hud != null)
            hideHUD();

        hud = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel(label)
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();
    }

    public void showHUD() {

        if (hud != null)
            hideHUD();

        hud = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();
    }

    public void hideHUD() {

        if (hud != null)
            hud.dismiss();

        hud = null;
    }

    public void showAlertDialog(String msg) {

        AlertDialog alertDialog = new AlertDialog.Builder(_context).create();

        alertDialog.setTitle(getString(R.string.app_name));
        alertDialog.setMessage(msg);

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, _context.getString(R.string.ok),

                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        //alertDialog.setIcon(R.drawable.banner);
        alertDialog.show();

    }

    public void printHash(){

        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);

            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("MY KEY HASH:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public static final int BACK_TWO_CLICK_DELAY_TIME = 3000 ; //ms
    public void onExit() {

        if (_isEndFlag == false) {

            Toast.makeText(this, getString(R.string.str_back_one_more_end),
                    Toast.LENGTH_SHORT).show();
            _isEndFlag = true;

            _handler.postDelayed(_exitRunner, BACK_TWO_CLICK_DELAY_TIME);

        } else if (_isEndFlag == true) {

            finish();
        }
    }

    public Runnable _exitRunner = new Runnable() {
        @Override
        public void run() {
            _isEndFlag = false ;
        }
    };

    @Override
    public boolean handleMessage(@NonNull Message msg) {
        switch (msg.what){
            default:
                break;
        }
        return false;
    }

    public static Integer min(Integer... vals) {

        Integer ret = null;
        for (Integer val : vals) {
            if (ret == null || (val != null && val < ret)) {
                ret = val;
            }
        }
        return ret;
    }

    public static Integer max(Integer... vals) {
        Integer ret = null;
        for (Integer val : vals) {
            if (ret == null || (val != null && val > ret)) {
                ret = val;
            }
        }
        return ret;
    }

    public int getWidth(){

        //return dp
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowmanager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        windowmanager.getDefaultDisplay().getMetrics(displayMetrics);

        int height = Math.round(displayMetrics.heightPixels / displayMetrics.density);

        int width = Math.round(displayMetrics.widthPixels / displayMetrics.density);

        return width;
    }

}
