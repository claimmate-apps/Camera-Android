package com.commonsware.cwac.camera.demo;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Paint;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.speech.RecognizerIntent;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.hardware.Camera.Size;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.misc.AsyncTask;
import com.commonsware.cwac.camera.demo.activities.AddClaimNameActivity;
import com.commonsware.cwac.camera.demo.activities.AddValueActivity;
import com.commonsware.cwac.camera.demo.activities.LiveStreamingActivity;
import com.commonsware.cwac.camera.demo.activities.LoginActivity;
import com.commonsware.cwac.camera.demo.activities.ReportActivity;
import com.commonsware.cwac.camera.demo.activities.addclaimname;
import com.commonsware.cwac.camera.demo.adpt.SlopListAdapter;
import com.commonsware.cwac.camera.demo.db.ClaimSqlLiteDbHelper;
import com.commonsware.cwac.camera.demo.model.ClaimModel;
import com.commonsware.cwac.camera.demo.model.QueModel;
import com.commonsware.cwac.camera.demo.model.SlopCount;
import com.commonsware.cwac.camera.demo.other.Constants;
import com.commonsware.cwac.camera.demo.other.FocusMarkerLayout;
import com.commonsware.cwac.camera.demo.other.Helper;
import com.commonsware.cwac.camera.demo.other.MultipartUtility;
import com.commonsware.cwac.camera.demo.other.MyApplication;
import com.commonsware.cwac.camera.demo.other.MyCamPara;
import com.commonsware.cwac.camera.demo.other.PrefManager;
import com.commonsware.cwac.camera.demo.other.SimpleGestureFilter;
import com.commonsware.cwac.camera.demo.other.Utility;
import com.commonsware.cwac.camera.demo.other.customitemclicklistener;
import com.commonsware.cwac.camera.demo.retrofit.APIInterface;
import com.commonsware.cwac.camera.demo.retrofit.ApiClient;
import com.example.claimmate.R;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.iid.FirebaseInstanceId;
import com.rampo.updatechecker.UpdateChecker;

import com.commonsware.cwac.camera.demo.other.MyApplication.TrackerName;
import com.rampo.updatechecker.UpdateCheckerResult;
//import com.scanlibrary.ScanActivity;
//import com.scanlibrary.ScanConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends Activity implements SimpleGestureFilter.SimpleGestureListener, SurfaceHolder.Callback, View.OnClickListener, UpdateCheckerResult, SensorEventListener, CompoundButton.OnCheckedChangeListener, View.OnLongClickListener {

    private String TAG = "HomeActivity";
    private Context mContext;

    List<String> response;
    PopupMenu popupMenu2, popupsubcat, popuphailmenu;
    Camera camera;
    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;

    PictureCallback jpegCallback;

    Button btnrei, btndamagetype, btnabc, btnmaterial, btntype;
    ImageButton ibtnflash, imgbtnsetting, imgbtncam, ibtnLive;
    ImageView imgtop, imgleft, imgright, imgbottom, imageViewTorch;
    TextView txtcenter, txtBNm;

    final Context context = this;
    Bitmap b1;
    int i = 0;
    File appdir,savefile1, savefile, mydir, subdir, reidir,maindir;

    String strrei = "R";
    Button btnlastphoto;
    File[] allFiles;
    String ocb1 = "";
    Boolean opengallery = false;

    SharedPreferences lastpathpf;
    SharedPreferences.Editor lastimageeditor;

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
    SimpleDateFormat folderdateFormat = new SimpleDateFormat("yyyy-MM-dd");

    SimpleDateFormat imgdateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    String strselectarea = "";
    String strselectsubarea = "";

    Button btnocb;

    String selectslope = "";
    String pitchvaluestorevalue;

    SeekBar zoomseek;

    boolean hasFlash;
    boolean btemp = false;

    AlertDialog flashalert;
    AlertDialog updateappalert;

    RelativeLayout rlsetting;

    EditText txtfoldername;
    Button btnok, btncancel;

    String appfoldername = "";

    RelativeLayout rlcontrolview, rliteriortype;

    Button btniteriortype;
    Button txt_slopeno;

    String strboc = "1";
    String strboctype = "Overview";
    String strhaildamage = "";
    String strhailmaterialdamage = "";

    String strreitype = "";

    CheckBox chkdate, chkApi, mchko, mchkc;

    int iteriortypeindex = 1;

    int layerphoto = 0;

    String[] hail_fs = new String[]{"Overview", "test sq Overview", "test sq", "hail damage close up on shingles",};

    String[] hail_fs_imgname = new String[]{
            "Front Slope Overview",
            "Front Slope test sq Overview",
            "Front Slope test sq",
            "Front Slope hail damage close up",
    };

    String[] hail_rs_imgname = new String[]{
            "Right Slope Overview",
            "Right Slope test sq Overview",
            "Right Slope test sq",
            "Right Slope hail damage close up",
    };

    String[] hail_ls_imgname = new String[]{
            "Left Slope Overview",
            "Left Slope test sq Overview",
            "Left Slope test sq",
            "Left Slope hail damage close up",
    };

    String[] hail_rears_imgname = new String[]{
            "Rear Slope Overview",
            "Rear Slope test sq Overview",
            "Rear Slope test sq",
            "Rear Slope hail damage close up",
    };

    ArrayList<String> itrarr;
    ArrayList<String> c_macrosidarr;
    ArrayList<String> c_macroname;

    ArrayList<String> macrosidarr;
    ArrayList<String> macroname;

    RelativeLayout rltorch;

    int photoindex = 1;
    int riskindex = 1;

    UpdateChecker checker;
    private SimpleGestureFilter detector;


    //declare boolean
    boolean clickedc = false;
    boolean clickedo = false;
    boolean clickedonew = false;
    boolean clickedonewj = false;


    RelativeLayout rlmenuselection;
    RelativeLayout rlriskphoto, rllayerphoto, rlshinglephoto, rlgutterphoto, rloverhangphoto, rlpitchphoto, rlcostm, rlfeo, rlhail;
    RelativeLayout rlBack, rllayerBack, rlshingleBack, rlgutterBack, rlOverhangBack, rlpitchBack, rlcostmback, rlfeoback, rlhailback, rlblankback;
    LinearLayout llRiskDes;
    Button btnTypeOfConstruction, btnRCI, btnSingleFamily, btnTypeOfExteriorSiding;
    TextView txtStoryDec, txtStory, txtStoryInc, txtCarDec, txtCar, txtCarInc;
    CheckBox chkAttached;

    Button btnrisk, btnskip, btnlayerskip, btnpitchskip, btnshingleskip, btngutterskip, btnoverhangskip, btncostmskip, btnfeoskip, btnhailskip;

    SensorManager mSensorManager;
    Sensor accelerometer;
    Sensor magnetometer;

    String addcostmphotoname = "zz";

    Button btnshinglemenu1, btnshinglemenu2;
    Button btnguttermenu1;
    Button btnLayersmenu;
    Button btnLayersmenu1, btnoverhangmenu1;

    Button btnnodamagetype;

    TextView lblpitchvalue;

    DecimalFormat decimalFormat = new DecimalFormat("#.#");

    private float[] mLastAccelerometer = new float[3];
    private float[] mLastMagnetometer = new float[3];
    private boolean mLastAccelerometerSet = false;
    private boolean mLastMagnetometerSet = false;

    private float[] mR = new float[9];
    private float[] mOrientation = new float[3];

    float pitch;

    Button btnmatrialsubmenu;

    String myPath;
    ClaimSqlLiteDbHelper claimDbHelper;
    SQLiteDatabase DB;

    String SELECT_SQL;
    Cursor Cur;

    int value = 0;

    Button btntype2, btnnodamages, btno, btnnc,btnhailnodamages,btnhailmaterialdamages;

    Button btnroofadd, btnaddelevations, btnaddinterior, btnadddamage, btnesubcatgry, btnaddclaimname, btnmacroadd, btnAddDocument, btnLogout, btnSynchronize;
    LinearLayout llline;

    RelativeLayout rlmain;

    Bitmap bmoverlay;

    String matrialcostmtext = "";
    String damagecostmtext = "";
    String strname = "";

    Button btncat;
    EditText txtname;

    TextView txtalphaname, txtalphaname2;

    public static int cid_marcos = 1;

    ArrayList<Map<String, String>> macrocostamdetail;
    ArrayList<String> macrosubmenu;

    Button btnimgmacro;
    Button btnsubmenu;

    RelativeLayout rladdtionalback;

    Boolean isdbmenu = true;
    Button btnimgmacrosub;
    Button btnhailmenu1, btnhailtype;

    RelativeLayout rlhailoptionview, rlhailoptionback;
    CheckBox rbtn_frontslope, rbtn_rightslope, rbtn_rearslope, rbtn_leftslope;

    Button txt_frontslope, txt_rightslope, txt_rearslope, txt_leftslope;
    String no_frontslope = "0";
    String no_rightslope = "0";
    String no_rearslope = "0";
    String no_leftslope = "0";

    Button btnhailgo;

    int no_slopeimg = 0;

    int no_frontslopeimg = 0;
    int no_rightslopeimg = 0;
    int no_rearslopeimg = 0;
    int no_leftslopeimg = 0;

    Boolean chkfrontslopeimg = true;
    Boolean chkrightslopeimg = false;
    Boolean chkrearslopeimg = false;
    Boolean chkleftslopeimg = false;

    int frontslopeimgindex = 1;
    int rightslopeimgindex = 1;
    int rearslopeimgindex = 1;
    int leftslopeimgindex = 1;

    int hailimgcount = 0;

    TextView tvonrakes;

    Button btnarea, btnInsulation, btnQty;

    Button btndamagetype1,btnmaterial1;

    RelativeLayout rlmainview;

    Button btnpitchmenu;

    String strfencegame = "";

    RelativeLayout rlbottomview, rlBottomView2;
    Button btnsave, btnBcancel;

    Button btnlength, btnwidth, btnheight;
    Button btnareatogal;
    boolean isBottomShow = false, isFlashOn = false, isCameraOn = false;
    static Camera.Parameters parameters;

    public static RadioButton rbDamageMark, rbDamageUnmark, rbDamageLeave;
    public static RadioButton rbAreaMark, rbAreaUnmark, rbAreaLeave;
    public static RadioButton rbAreaSubMark, rbAreaSubUnmark, rbAreaSubLeave;
    public static RadioButton rbCeiling, rbWall, rbFloor;
    public static RadioButton rbMaterialMark, rbMaterialUnmark, rbMaterialLeave;
    public static RadioButton rbOverview, rbCloseUp, rbBlank;

    Button btnInteriorMacro, btnInteriorMenu, btnInteriorSkip, btnSubAreaTogal, btnQue, btnMic, btnReport;
    TextView txtQueCount;
    RelativeLayout rlInteriorPhoto, rlInteriorBack, rlTypesOfSidingPoto, rlTypesOfSidingBack;
    Button btnTypesOfSidingMenu2, btnTypesOfSidingMenu1, btnTypesOfSidingSkip;

    // Interior New Macro
    RelativeLayout rlInteriorNewPhoto, rlInteriorNewBack;
    Button btnInteriorNewMacro, btnInteriorNewNext, btnIntNewDamage, btnIntNewMaterial, btnIntNewDamageRed, btnIntNewMaterialRed, btnIntNewInsulation, btnIntNewQty;

    // New Macro
    RelativeLayout rlNewMacro, rlNewMacroBack;
    Button btnNewMacroSkip, btnNewMacroDamage;

    // Room Macro
    RelativeLayout rlRoomMacroPhoto, rlRoomMacroBack;
    Button btnRoomMacro, btnRoomMacroDamageRed, btnRoomMacroDamage, btnRoomMacroDamageAmount, btnRoomMacroMaterialRed, btnRoomMacroMaterial, btnRoomMacroMaterialFinish, btnRoomMacroMaterialFinishRed, btnRoomMacroNext, btnRoomMacroPlus;
    ListView listViewLWH;
    CheckBox chkRoomMacroAffects;

    Button btnCeiling, btnWall, btnFloor;
    LinearLayout llArea;

    ArrayList<QueModel> arrayListQue;
    ArrayList<ClaimModel> arrayListClaim;

    Button btnr,btne;

    SlopListAdapter slopListAdapter;
    RecyclerView rv_slopno;
    GridLayoutManager gridLayoutManager;
    String selectslop_cur = "1";

    List<SlopCount> data;

    AppCompatButton btn_replace_front,btn_replace_right,btn_replace_rear,btn_replace_left;
    AppCompatButton btn_repair_front,btn_repair_right,btn_repair_rear,btn_repair_left;
    AppCompatEditText edtrepair,edtreplace;
    String repairno = "1";
    String replaceno = "1";

    LinearLayout ll_default_button_submenu;
    RelativeLayout rl_defaultmenu;

    int repairvalue = 5;


    @Override
    public void onBackPressed() {
        if (rlsetting.getVisibility() == View.VISIBLE)
            rlsetting.setVisibility(View.GONE);
        else
            exitappalert();
    }

    private void exitappalert() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void setToken() {
        new Helper(this);
        if (!Helper.isDeviceRegister() && !Helper.getToken().equals("null")) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 101);
            } else {
                /*ApiClient.getClient().create(APIInterface.class).registerDevice(((TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId(), Helper.getToken()).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                        if (response != null) {
                            Log.i(TAG, "registerDeviceRes = " + response.body());
                            try {
                                JSONObject jsonObject = new JSONObject(response.body());
                                if (!jsonObject.getBoolean("error")) {
                                    Helper.setDeviceRegister(true);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        t.printStackTrace();
                        Log.i(TAG, "registerDeviceError = " + t.toString());
                    }
                });*/
            }
        }
    }

    protected void onResume() {
        super.onResume();

        getClaimList();

//        showclaimnameoption(false);

        mSensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_NORMAL);

        int numCams = Camera.getNumberOfCameras();
        if (numCams > 0) {
            try {
                if (!isCameraOn) {
                    camera = Camera.open(0);
                    camera.startPreview();
                    isCameraOn = true;
                }
                parameters = camera.getParameters();
            } catch (RuntimeException ex) {
                ex.printStackTrace();
                Toast.makeText(HomeActivity.this, "Camera not found", Toast.LENGTH_LONG).show();
            }
        }

        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_HARDWARE);

        if (btnsubmenu.getText().toString().trim().equals("")) {
            getcostammenu(false);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.WAKE_LOCK}, 101);
        }
        refreshCamera();

        setToken();


        if(!Constants.addclaimname.trim().equalsIgnoreCase(""))
        {
            AddClaimName(Constants.addclaimname.trim());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 101 && (grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
            HomeActivity.this.finish();
            startActivity(getIntent());
        }
    }

    private View.OnClickListener onTouchRiskMacro() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == txtStoryDec.getId()) {

                    clickonView("txtStoryDec");


                    try {
                        int cnt = Integer.parseInt(txtStory.getText().toString());
                        if (cnt != 0)
                            txtStory.setText(--cnt + "");
                    } catch (Exception e) {
                        txtStory.setText("0");
                    }
                } else if (view.getId() == txtStoryInc.getId()) {
                    clickonView("txtStoryInc");

                    try {
                        int cnt = Integer.parseInt(txtStory.getText().toString());
                        txtStory.setText(++cnt + "");
                    } catch (Exception e) {
                        txtStory.setText("0");
                    }
                } else if (view.getId() == txtCarDec.getId()) {

                    clickonView("txtCarDec");

                    try {
                        int cnt = Integer.parseInt(txtCar.getText().toString());
                        if (cnt != 0)
                            txtCar.setText(--cnt + "");
                    } catch (Exception e) {
                        txtCar.setText("0");
                    }
                } else if (view.getId() == txtCarInc.getId()) {
                    clickonView("txtCarInc");


                    try {
                        int cnt = Integer.parseInt(txtCar.getText().toString());
                        txtCar.setText(++cnt + "");
                    } catch (Exception e) {
                        txtCar.setText("0");
                    }
                }
            }
        };
    }

    public void onClickRiskMacroDes(final View v)
    {
        PopupMenu popupMenu = new PopupMenu(mContext, v);
        if (v.getId() == btnTypeOfConstruction.getId()) {
            clickonView("btnTypeOfConstruction");


            popupMenu.getMenu().add("Conventional wood framed");
            popupMenu.getMenu().add("Brick");
            popupMenu.getMenu().add("Insert custom data");
            clickonView("btnTypeOfConstruction");
        } else if (v.getId() == btnRCI.getId()) {
            clickonView("btnRCI");

            popupMenu.getMenu().add("Residential");
            popupMenu.getMenu().add("Commercial");
            popupMenu.getMenu().add("Industrial");
            popupMenu.getMenu().add("Blank");
            popupMenu.getMenu().add("Insert custom data");
            clickonView("btnRCI");

        } else if (v.getId() == btnSingleFamily.getId()) {
            clickonView("btnSingleFamily");


            if (btnRCI.getText().toString().equalsIgnoreCase("Residential")) {
                popupMenu.getMenu().add("Single Family");
                popupMenu.getMenu().add("Multi-Family");
                popupMenu.getMenu().add("Mixed Use");
            } else if (btnRCI.getText().toString().equalsIgnoreCase("Commercial")) {
                popupMenu.getMenu().add("Retail");
                popupMenu.getMenu().add("Mixed Use");
                popupMenu.getMenu().add("Blank");
            } else {
                popupMenu.getMenu().add("Single Family Dwelling");
                popupMenu.getMenu().add("Condo");
                popupMenu.getMenu().add("Blank");
            }
            popupMenu.getMenu().add("Insert custom data");
        } else if (v.getId() == btnTypeOfExteriorSiding.getId()) {
            clickonView("btnTypeOfExteriorSiding");

            popupMenu.getMenu().add("Brick veneer and vinyl siding");
            popupMenu.getMenu().add("Brick veneer and aluminum siding");
            popupMenu.getMenu().add("All brick exterior");
            popupMenu.getMenu().add("Vinyl siding exterior");
            popupMenu.getMenu().add("Aluminum siding exterior");
            popupMenu.getMenu().add("Insert custom data");
        }

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                String title = item.getTitle().toString();
                if (title.equals("Insert custom data")) {
                    addRiskCustomTextPopup((Button) v);
                } else {
                    ((Button) v).setText(title);
                    if (v.getId() == btnRCI.getId()) {
                        clickonView("btnRCI");

                        if (btnRCI.getText().toString().equalsIgnoreCase("Residential")) {
                            btnSingleFamily.setText("Single Family");
                        } else if (btnRCI.getText().toString().equalsIgnoreCase("Commercial")) {
                            btnSingleFamily.setText("Retail");
                        } else {
                            btnSingleFamily.setText("Single Family Dwelling");
                        }
                    }
                }
                v.setTag(title);

                return false;
            }
        });
        popupMenu.show();
    }

    private void clickonView(String clickviewname)
    {
        Log.e("clickviewname",""+clickviewname);
    }

    private void addRiskCustomTextPopup(final Button btn) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setCancelable(false);
        alert.setTitle("Add custom text"); //Set Alert dialog title here

        // Set an EditText view to get user input
        final EditText input = new EditText(this);
        input.setText("");
        input.setSelection(input.getText().toString().length());
        alert.setView(input);

        alert.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                if (input.getText().toString().trim().equals("")) {
                    input.setError("Enter custom text.");
                    input.requestFocus();
                } else {
                    btn.setText(input.getText().toString());
                }
            } // End of onClick(DialogInterface dialog, int whichButton)
        }); //End of alert.setPositiveButton
        alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = alert.create();
        alertDialog.show();
    }

    private void addRiskMacroDes() {
        final Dialog dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.popup_add_risk_macro);

        final EditText edtTypeOfConstruction, edtRCI, edtSingleFamily, edtGarageAttached, edtTypeOfExteriorSiding;
        final Spinner spStory, spTypeOfConstruction, spRCI, spSingleFamily, spGarageAttached, spTypeOfExteriorSiding;

        edtTypeOfConstruction = dialog.findViewById(R.id.edtTypeOfConstruction);
        edtRCI = dialog.findViewById(R.id.edtRCI);
        edtSingleFamily = dialog.findViewById(R.id.edtSingleFamily);
        edtGarageAttached = dialog.findViewById(R.id.edtGarageAttached);
        edtTypeOfExteriorSiding = dialog.findViewById(R.id.edtTypeOfExteriorSiding);

        spStory = dialog.findViewById(R.id.spStory);
        spTypeOfConstruction = dialog.findViewById(R.id.spTypeOfConstruction);
        spRCI = dialog.findViewById(R.id.spRCI);
        spSingleFamily = dialog.findViewById(R.id.spSingleFamily);
        spGarageAttached = dialog.findViewById(R.id.spGarageAttached);
        spTypeOfExteriorSiding = dialog.findViewById(R.id.spTypeOfExteriorSiding);

        AdapterView.OnItemSelectedListener onItemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (adapterView.getId() == spTypeOfConstruction.getId()) {
                    clickonView("spTypeOfConstruction");

                    if (spTypeOfConstruction.getSelectedItem().toString().equals("Insert custom data")) {
                        edtTypeOfConstruction.setVisibility(View.VISIBLE);
                        edtTypeOfConstruction.setText("");
                        edtTypeOfConstruction.requestFocus();
                    } else {
                        edtTypeOfConstruction.setVisibility(View.GONE);
                    }
                } else if (adapterView.getId() == spRCI.getId()) {
                    clickonView("spRCI");

                    if (spRCI.getSelectedItem().toString().equals("Insert custom data")) {
                        edtRCI.setVisibility(View.VISIBLE);
                        edtRCI.setText("");
                        edtRCI.requestFocus();
                    } else {
                        edtRCI.setVisibility(View.GONE);
                    }
                } else if (adapterView.getId() == spSingleFamily.getId()) {
                    clickonView("spSingleFamily");

                    if (spSingleFamily.getSelectedItem().toString().equals("Insert custom data")) {
                        edtSingleFamily.setVisibility(View.VISIBLE);
                        edtSingleFamily.setText("");
                        edtSingleFamily.requestFocus();
                    } else {
                        edtSingleFamily.setVisibility(View.GONE);
                    }
                } else if (adapterView.getId() == spGarageAttached.getId()) {
                    clickonView("spGarageAttached");

                    if (spGarageAttached.getSelectedItem().toString().equals("Insert custom data")) {
                        edtGarageAttached.setVisibility(View.VISIBLE);
                        edtGarageAttached.setText("");
                        edtGarageAttached.requestFocus();
                    } else {
                        edtGarageAttached.setVisibility(View.GONE);
                    }
                } else if (adapterView.getId() == spTypeOfExteriorSiding.getId()) {

                    clickonView("spTypeOfExteriorSiding");

                    if (spTypeOfExteriorSiding.getSelectedItem().toString().equals("Insert custom data")) {
                        edtTypeOfExteriorSiding.setVisibility(View.VISIBLE);
                        edtTypeOfExteriorSiding.setText("");
                        edtTypeOfExteriorSiding.requestFocus();
                    } else {
                        edtTypeOfExteriorSiding.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        };

        spTypeOfConstruction.setOnItemSelectedListener(onItemSelectedListener);
        spRCI.setOnItemSelectedListener(onItemSelectedListener);
        spSingleFamily.setOnItemSelectedListener(onItemSelectedListener);
        spGarageAttached.setOnItemSelectedListener(onItemSelectedListener);
        spTypeOfExteriorSiding.setOnItemSelectedListener(onItemSelectedListener);

        dialog.findViewById(R.id.imgClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.findViewById(R.id.btnSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

                String userId = PrefManager.getUserId(), claimId = PrefManager.getClaimId(), story = spStory.getSelectedItem().toString(), typeOfConstruction = spTypeOfConstruction.getSelectedItem().toString(), rci = spRCI.getSelectedItem().toString(), singleFamily = spSingleFamily.getSelectedItem().toString(), garageAttached = spGarageAttached.getSelectedItem().toString(), typeOfExteriorSiding = spTypeOfExteriorSiding.getSelectedItem().toString();

                if (edtTypeOfConstruction.getVisibility() == View.VISIBLE)
                    typeOfConstruction = edtTypeOfConstruction.getText().toString();
                if (edtRCI.getVisibility() == View.VISIBLE)
                    rci = edtRCI.getText().toString();
                if (edtSingleFamily.getVisibility() == View.VISIBLE)
                    singleFamily = edtSingleFamily.getText().toString();
                if (edtGarageAttached.getVisibility() == View.VISIBLE)
                    garageAttached = edtGarageAttached.getText().toString();
                if (edtTypeOfExteriorSiding.getVisibility() == View.VISIBLE)
                    typeOfExteriorSiding = edtTypeOfExteriorSiding.getText().toString();

                if (!Utility.haveInternet(mContext, false)) {
                    claimDbHelper.addRiskMacroDes(userId, claimId, story, typeOfConstruction, rci, singleFamily, garageAttached, typeOfExteriorSiding);
                    return;
                }

                Utility.showProgress(mContext);
                /*ApiClient.getClient().create(APIInterface.class).addRiskMacroDes(userId, claimId, story, typeOfConstruction, rci, singleFamily, garageAttached, typeOfExteriorSiding).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                        Utility.dismissProgress();
                        Log.i(TAG, "addRiskMacroDesRes = " + response.body());

                        if (response.body() == null) {
                            Utility.errorDialog(mContext, getString(R.string.error_data_not_found));
                            return;
                        }

                        try {
                            JSONObject jsonObject = new JSONObject(response.body());
                            if (jsonObject.getString("success").equals("success")) {
                                Toast.makeText(mContext, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            } else {
                                Utility.errorDialog(mContext, jsonObject.getString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Utility.dismissProgress();
                        Log.i(TAG, "addRiskMacroDesError = " + t.toString());
                    }
                });*/
            }
        });

        dialog.show();
    }

    private void showalertinfo() {

        String msg = "Is this for " + btncat.getText().toString().trim() + " or would you like to select a different claim";

        AlertDialog.Builder builder1 = new AlertDialog.Builder(HomeActivity.this);
        builder1.setTitle("ClaimMate Camera");
        builder1.setMessage(msg);
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        btnimgmacrosub.setVisibility(View.VISIBLE);
                        btnimgmacrosub.setText("Risk");

                        nextPhoto("Risk");
//                        addRiskMacroDes();
                        dialog.dismiss();
                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        //                    rladdtionalback.setVisibility(View.VISIBLE);
                        btnrisk.setText("Aditional Photo");
                        btnimgmacrosub.setVisibility(View.GONE);
                        btnimgmacro.setVisibility(View.VISIBLE);

                        btntype.setVisibility(View.VISIBLE);
                        btnnodamages.setVisibility(View.VISIBLE);

                        btnnc.setVisibility(View.VISIBLE);//Dmakchange
                        btno.setVisibility(View.VISIBLE);//dmakchange
                        btntype2.setVisibility(View.VISIBLE);


                        btnimgmacro.setText("Macro");
                        nextPhoto("Aditional Photo");
                        getalphaname();

                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    protected void onPause() {

        mSensorManager.unregisterListener(this);

        super.onPause();
    }

    private void opendatabase()  throws SQLException {

    /*    claimDbHelper = new ClaimSqlLiteDbHelper(HomeActivity.this, claimDbHelper.claimdb_NAME);
        claimDbHelper.openDataBase();
*/
        myPath = claimDbHelper.claimdb_PATH + claimDbHelper.claimdb_NAME;
        DB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
    }




    private void closedatabase() {
        DB.close();
    }

    Tracker t;
    FocusMarkerLayout focusMarkerLayout;

    private static GoogleAnalytics sAnalytics;
    private static Tracker sTracker;
    Button  btnscope;
    synchronized public Tracker getDefaultTracker() {
        // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
        if (sTracker == null) {
            sTracker = sAnalytics.newTracker(R.xml.global_tracker);
        }
        return sTracker;
    }

    FirebaseAnalytics firebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.homeactivity);

        mContext = HomeActivity.this;

        new PrefManager(mContext);

        Log.i(TAG, "token = " + FirebaseInstanceId.getInstance().getToken());

        arrayListQue = new ArrayList<>();

        firebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Home");
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

        detector = new SimpleGestureFilter(this, this);

        imageViewTorch = findViewById(R.id.imageViewTorch);
        rlmainview = findViewById(R.id.rlmainview);

        lastpathpf = getSharedPreferences("claimmatecam", Context.MODE_PRIVATE);
        lastimageeditor = lastpathpf.edit();

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        rlmain = findViewById(R.id.rlmain);
        btnscope = findViewById(R.id.btnscope);

        if (isNetworkAvailable(HomeActivity.this)) {
//            UpdateChecker checker = new UpdateChecker(this, this);

            //   Google Analytics
            t = ((MyApplication) getApplication()).getTracker(TrackerName.APP_TRACKER);
            t.setScreenName("Home");
            t.send(new HitBuilders.AppViewBuilder().build());
        }

        lblpitchvalue = findViewById(R.id.lblpitchvalue);


        hasFlash = getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);


        appfoldername = lastpathpf.getString("appfoldername", "ClaimMate");


        claimDbHelper = new ClaimSqlLiteDbHelper(this);


        String iscopydp = lastpathpf.getString("iscopy", "no");

        if (iscopydp.toString().equals("no")) {
            try {

                claimDbHelper.CopyDataBaseFromAsset();
                lastimageeditor.putString("iscopy", "yes");
                lastimageeditor.commit();
                Log.e("iscopy", "no");

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("iscopy", "yes");

        }
        photoindex = lastpathpf.getInt("photoindex", 1);

        riskindex = lastpathpf.getInt("riskindex", 1);

        frontslopeimgindex = lastpathpf.getInt("frontslopeimgindex", 1);
        rightslopeimgindex = lastpathpf.getInt("rightslopeimgindex", 1);
        rearslopeimgindex = lastpathpf.getInt("rearslopeimgindex", 1);
        leftslopeimgindex = lastpathpf.getInt("leftslopeimgindex", 1);


        appdir = new File(Environment.getExternalStorageDirectory(), getResources().getString(R.string.app_name));
        if(!appdir.exists())
        {
            appdir.mkdir();
        }

        mydir = new File(appdir, appfoldername);

        if(!mydir.exists())
        {
            mydir.mkdir();
        }

//        mydir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), appfoldername);

        rlsetting = findViewById(R.id.rlsetting);

        llline = findViewById(R.id.llline);


        txtBNm = findViewById(R.id.txtBNm);

        rlbottomview = findViewById(R.id.rlbottomview);
        rlBottomView2 = findViewById(R.id.rlBottomView2);
        btnsave = findViewById(R.id.btnsave);
        btnBcancel = findViewById(R.id.btnBcancel);


        ll_default_button_submenu = findViewById(R.id.ll_default_button_submenu);
        rl_defaultmenu = findViewById(R.id.rl_defaultmenu);





        btnroofadd = findViewById(R.id.btnroofadd);
        btnaddelevations = findViewById(R.id.btnaddelevations);
        btnaddinterior = findViewById(R.id.btnaddinterior);
        btnadddamage = findViewById(R.id.btnadddamage);
        btnAddDocument = findViewById(R.id.btnAddDocument);
        btnSynchronize = findViewById(R.id.btnSynchronize);
        btnLogout = findViewById(R.id.btnLogout);
        btnesubcatgry = findViewById(R.id.btnesubcatgry);
        btnaddclaimname = findViewById(R.id.btnaddclaimname);
        btnmacroadd = findViewById(R.id.btnmacroadd);

        btnInteriorMacro = findViewById(R.id.btnInteriorMacro);
        rlInteriorPhoto = findViewById(R.id.rlInteriorPhoto);
        rlInteriorBack = findViewById(R.id.rlInteriorBack);
        btnInteriorMenu = findViewById(R.id.btnInteriorMenu);
        btnInteriorSkip = findViewById(R.id.btnInteriorSkip);

        btnInteriorMacro.setOnClickListener(this);
        rlInteriorBack.setOnClickListener(this);
        btnInteriorMenu.setOnClickListener(this);
        btnInteriorSkip.setOnClickListener(this);

        btnscope.setOnClickListener(this);
        rlInteriorNewPhoto = findViewById(R.id.rlInteriorNewPhoto);
        rlInteriorNewBack = findViewById(R.id.rlInteriorNewBack);
        btnInteriorNewMacro = findViewById(R.id.btnInteriorNewMacro);
        btnInteriorNewNext = findViewById(R.id.btnInteriorNewNext);
        btnIntNewDamage = findViewById(R.id.btnIntNewDamage);
        btnIntNewMaterial = findViewById(R.id.btnIntNewMaterial);
        btnIntNewDamageRed = findViewById(R.id.btnIntNewDamageRed);
        btnIntNewMaterialRed = findViewById(R.id.btnIntNewMaterialRed);
        btnIntNewQty = findViewById(R.id.btnIntNewQty);
        btnIntNewInsulation = findViewById(R.id.btnIntNewInsulation);

        btnInteriorNewMacro.setOnClickListener(onClickInteriorNewMacro());
        rlInteriorNewBack.setOnClickListener(onClickInteriorNewMacro());
        btnInteriorNewNext.setOnClickListener(onClickInteriorNewMacro());
        btnIntNewDamage.setOnClickListener(onClickInteriorNewMacro());
        btnIntNewMaterial.setOnClickListener(onClickInteriorNewMacro());
        btnIntNewDamageRed.setOnClickListener(onClickInteriorNewMacro());
        btnIntNewMaterialRed.setOnClickListener(onClickInteriorNewMacro());
        btnIntNewQty.setOnClickListener(onClickInteriorNewMacro());
        btnIntNewInsulation.setOnClickListener(onClickInteriorNewMacro());

        rlRoomMacroPhoto = findViewById(R.id.rlRoomMacroPhoto);
        rlRoomMacroBack = findViewById(R.id.rlRoomMacroBack);
        btnRoomMacro = findViewById(R.id.btnRoomMacro);
        btnRoomMacroDamage = findViewById(R.id.btnRoomMacroDamage);
        btnRoomMacroDamageRed = findViewById(R.id.btnRoomMacroDamageRed);
        btnRoomMacroDamageAmount = findViewById(R.id.btnRoomMacroDamageAmount);
        btnRoomMacroMaterialRed = findViewById(R.id.btnRoomMacroMaterialRed);
        btnRoomMacroMaterial = findViewById(R.id.btnRoomMacroMaterial);
        btnRoomMacroMaterialFinish = findViewById(R.id.btnRoomMacroMaterialFinish);
        btnRoomMacroMaterialFinishRed = findViewById(R.id.btnRoomMacroMaterialFinishRed);
        btnRoomMacroNext = findViewById(R.id.btnRoomMacroNext);
        btnRoomMacroPlus = findViewById(R.id.btnRoomMacroPlus);
        listViewLWH = findViewById(R.id.listViewLWH);
        chkRoomMacroAffects = findViewById(R.id.chkRoomMacroAffects);

        rlRoomMacroBack.setOnClickListener(onClickRoomMacro());
        btnRoomMacro.setOnClickListener(onClickRoomMacro());
        btnRoomMacroDamage.setOnClickListener(onClickRoomMacro());
        btnRoomMacroDamageRed.setOnClickListener(onClickRoomMacro());
        btnRoomMacroDamageAmount.setOnClickListener(onClickRoomMacro());
        btnRoomMacroMaterial.setOnClickListener(onClickRoomMacro());
        btnRoomMacroMaterialRed.setOnClickListener(onClickRoomMacro());
        btnRoomMacroMaterialFinish.setOnClickListener(onClickRoomMacro());
        btnRoomMacroMaterialFinishRed.setOnClickListener(onClickRoomMacro());
        btnRoomMacroNext.setOnClickListener(onClickRoomMacro());
        btnRoomMacroPlus.setOnClickListener(onClickRoomMacro());

        rlNewMacro = findViewById(R.id.rlNewMacro);
        rlNewMacroBack = findViewById(R.id.rlNewMacroBack);
        btnNewMacroSkip = findViewById(R.id.btnNewMacroSkip);
        btnNewMacroDamage = findViewById(R.id.btnNewMacroDamage);

        rlNewMacroBack.setOnClickListener(this);
        btnNewMacroSkip.setOnClickListener(this);
        btnNewMacroDamage.setOnClickListener(this);

        rlTypesOfSidingPoto = findViewById(R.id.rlTypesOfSidingPoto);
        rlTypesOfSidingBack = findViewById(R.id.rlTypesOfSidingBack);
        btnTypesOfSidingMenu2 = findViewById(R.id.btnTypesOfSidingMenu2);
        btnTypesOfSidingMenu1 = findViewById(R.id.btnTypesOfSidingMenu1);
        btnTypesOfSidingSkip = findViewById(R.id.btnTypesOfSidingSkip);

        rlTypesOfSidingBack.setOnClickListener(this);
        btnTypesOfSidingMenu1.setOnClickListener(this);
        btnTypesOfSidingMenu2.setOnClickListener(this);
        btnTypesOfSidingSkip.setOnClickListener(this);

        btnarea = findViewById(R.id.btnarea);
        btnInsulation = findViewById(R.id.btnInsulation);
        btnQty = findViewById(R.id.btnQty);

        btnimgmacro = findViewById(R.id.btnimgmacro);
        btnimgmacrosub = findViewById(R.id.btnimgmacrosub);

        btnhailmenu1 = findViewById(R.id.btnhailmenu1);
        btnhailtype = findViewById(R.id.btnhailtype);

        txtname = findViewById(R.id.txtname);

        btncat = findViewById(R.id.btncat);
        btncat.setText(appfoldername);

        txtfoldername = findViewById(R.id.txtfoldername);
        btnok = findViewById(R.id.btnok);
        btncancel = findViewById(R.id.btncancel);

        btniteriortype = findViewById(R.id.btniteriortype);

        txt_slopeno  = findViewById(R.id.txt_slopeno);

        rlcontrolview = findViewById(R.id.rlcontrolview);
        rliteriortype = findViewById(R.id.rliteriortype);

        rlmenuselection = findViewById(R.id.rlmenuselection);

        rlriskphoto = findViewById(R.id.rlriskphoto);
        rllayerphoto = findViewById(R.id.rllayerphoto);

        rlshinglephoto = findViewById(R.id.rlshinglephoto);

        rlgutterphoto = findViewById(R.id.rlgutterphoto);

        rloverhangphoto = findViewById(R.id.rloverhangphoto);
        rlpitchphoto = findViewById(R.id.rlpitchphoto);
        rlcostm = findViewById(R.id.rlcostm);
        rlfeo = findViewById(R.id.rlfeo);
        rlhail = findViewById(R.id.rlhail);

        rltorch = findViewById(R.id.rltorch);

        rladdtionalback = findViewById(R.id.rladdtionalback);

        rlBack = findViewById(R.id.rlBack);
        llRiskDes = findViewById(R.id.llRiskDes);
        btnTypeOfConstruction = findViewById(R.id.btnTypeOfConstruction);
        btnRCI = findViewById(R.id.btnRCI);
        btnSingleFamily = findViewById(R.id.btnSingleFamily);
        btnTypeOfExteriorSiding = findViewById(R.id.btnTypeOfExteriorSiding);
        txtStoryDec = findViewById(R.id.txtStoryDec);
        txtStory = findViewById(R.id.txtStory);
        txtStoryInc = findViewById(R.id.txtStoryInc);
        txtCarDec = findViewById(R.id.txtCarDec);
        txtCar = findViewById(R.id.txtCar);
        txtCarInc = findViewById(R.id.txtCarInc);
        chkAttached = findViewById(R.id.chkAttached);

        txtStoryDec.setOnClickListener(onTouchRiskMacro());
        txtStoryInc.setOnClickListener(onTouchRiskMacro());
        txtCarDec.setOnClickListener(onTouchRiskMacro());
        txtCarInc.setOnClickListener(onTouchRiskMacro());

        rllayerBack = findViewById(R.id.rllayerBack);

        rlshingleBack = findViewById(R.id.rlshingleBack);
        rlgutterBack = findViewById(R.id.rlgutterBack);
        rlOverhangBack = findViewById(R.id.rlOverhangBack);

        rlpitchBack = findViewById(R.id.rlpitchBack);
        rlcostmback = findViewById(R.id.rlcostmback);
        rlfeoback = findViewById(R.id.rlfeoback);
        rlhailback = findViewById(R.id.rlhailback);

        rlblankback = findViewById(R.id.rlblankback);

        chkdate = findViewById(R.id.chkdate);
        mchko = findViewById(R.id.mchko);
        mchkc = findViewById(R.id.mchkc);
        chkApi = findViewById(R.id.chkApi);


        btnr = findViewById(R.id.btnr);
        btnr.setTag("1");
        btne = findViewById(R.id.btne);
        btne.setTag("1");

       btn_replace_front= findViewById(R.id.btn_replace_front);
       btn_replace_right= findViewById(R.id.btn_replace_right);
       btn_replace_rear= findViewById(R.id.btn_replace_rear);
       btn_replace_left= findViewById(R.id.btn_replace_left);


       btn_repair_front= findViewById(R.id.btn_repair_front);
       btn_repair_right= findViewById(R.id.btn_repair_right);
       btn_repair_rear= findViewById(R.id.btn_repair_rear);
       btn_repair_left= findViewById(R.id.btn_repair_left);

        edtrepair= findViewById(R.id.edtrepair);
        edtreplace = findViewById(R.id.edtreplace);

        repairno = lastpathpf.getString("repairno", "1");
        replaceno = lastpathpf.getString("replaceno", "1");


        rv_slopno = findViewById(R.id.rv_slopno);
        gridLayoutManager = new GridLayoutManager(this,3);
        gridLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rv_slopno.setLayoutManager(gridLayoutManager);


        SetSlopNumberDate("1");

//        mchko.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
////            @Override
////            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
////
////                btnocb.setText("O");
//////                mchkc.setChecked(false);
//////                mchko.setChecked(true);
//////                btnocb.setText("O");
////
//////                if (mchkc.isChecked()) {
//////                    mchkc.setChecked(false);
//////                }
////                if (b) {
////                    btnocb.setText("O");
////                } else {
////                    mchkc.setChecked(true);
////                    btnocb.setText("C");
////                }
////
////                if (mchko.isChecked() && mchkc.isChecked()) {
////                    btnocb.setText("B");
////                    //calluncheckdata();
////                }
////            }
////        });
////
////        mchkc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
////            @Override
////            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
////
////                btnocb.setText("C");
//////                mchko.setChecked(false);
//////                mchkc.setChecked(true);
//////                btnocb.setText("C");
////                if (b) {
////                    btnocb.setText("C");
////                } else {
////                    mchko.setChecked(true);
////                    btnocb.setText("O");
////                }
////                if (mchko.isChecked() && mchkc.isChecked()) {
////                    btnocb.setText("B");
////                    // calluncheckdata();
////                }
////            }
////        });

        //calluncheckdata();
        rbDamageMark = findViewById(R.id.rbDamageMark);
        rbDamageUnmark = findViewById(R.id.rbDamageUnmark);
        rbDamageLeave = findViewById(R.id.rbDamageLeave);

        rbAreaMark = findViewById(R.id.rbAreaMark);
        rbAreaUnmark = findViewById(R.id.rbAreaUnmark);
        rbAreaLeave = findViewById(R.id.rbAreaLeave);

        rbAreaSubMark = findViewById(R.id.rbAreaSubMark);
        rbAreaSubUnmark = findViewById(R.id.rbAreaSubUnmark);
        rbAreaSubLeave = findViewById(R.id.rbAreaSubLeave);

        rbCeiling = findViewById(R.id.rbCeiling);
        rbWall = findViewById(R.id.rbWall);
        rbFloor = findViewById(R.id.rbFloor);

        rbMaterialMark = findViewById(R.id.rbMaterialMark);
        rbMaterialUnmark = findViewById(R.id.rbMaterialUnmark);
        rbMaterialLeave = findViewById(R.id.rbMaterialLeave);

        rbOverview = findViewById(R.id.rbOverview);
        rbCloseUp = findViewById(R.id.rbCloseUp);
        rbBlank = findViewById(R.id.rbBlank);

        btnrei = findViewById(R.id.btnrei);
        btnQue = findViewById(R.id.btnQue);
        txtQueCount = findViewById(R.id.txtQueCount);
        setQueCount();

        btnMic = findViewById(R.id.btnMic);
        btnReport = findViewById(R.id.btnReport);

        btndamagetype = findViewById(R.id.btndamagetype);
        btndamagetype1 = findViewById(R.id.btndamagetype1);
        btnmaterial1 = findViewById(R.id.btnmaterial1);
        btnabc = findViewById(R.id.btnabc);
        btnmaterial = findViewById(R.id.btnmaterial);
        btntype = findViewById(R.id.btntype);
        btntype2 = findViewById(R.id.btntype2);
        btnnodamages = findViewById(R.id.btnnodamages);
        btnhailnodamages = findViewById(R.id.btnhailnodamages);
        btnhailmaterialdamages = findViewById(R.id.btnhailmaterialdamages);


        btno = findViewById(R.id.btnoverview);
        btnnc = findViewById(R.id.btncloseup);

        btnpitchmenu = findViewById(R.id.btnpitchmenu);

        btnocb = findViewById(R.id.btnocb);

        btnlastphoto = findViewById(R.id.btnlastphoto);

        ibtnflash = findViewById(R.id.ibtnflash);
        ibtnLive = findViewById(R.id.ibtnLive);

        imgbtnsetting = findViewById(R.id.imgbtnsetting);
        imgbtncam = findViewById(R.id.imgbtncam);

        imgtop = findViewById(R.id.imgtop);
        imgleft = findViewById(R.id.imgleft);
        imgright = findViewById(R.id.imgright);
        imgbottom = findViewById(R.id.imgbottom);

        btnrisk = findViewById(R.id.btnrisk);
        btnskip = findViewById(R.id.btnskip);

        btnlayerskip = findViewById(R.id.btnlayerskip);
        btnpitchskip = findViewById(R.id.btnpitchskip);
        btnshingleskip = findViewById(R.id.btnshingleskip);
        btngutterskip = findViewById(R.id.btngutterskip);
        btnoverhangskip = findViewById(R.id.btnoverhangskip);
        btncostmskip = findViewById(R.id.btncostmskip);
        btnfeoskip = findViewById(R.id.btnfeoskip);
        btnhailskip = findViewById(R.id.btnhailskip);

        btnshinglemenu1 = findViewById(R.id.btnshinglemenu1);
        btnshinglemenu2 = findViewById(R.id.btnshinglemenu2);

        btnguttermenu1 = findViewById(R.id.btnguttermenu1);
        btnguttermenu1.setText("5 inch Gutter");

        btnLayersmenu = findViewById(R.id.btnLayersmenu);

        btnLayersmenu1 = findViewById(R.id.btnLayersmenu1);
        btnoverhangmenu1 = findViewById(R.id.btnoverhangmenu1);

        btnsubmenu = findViewById(R.id.btnsubmenu);

        txtcenter = findViewById(R.id.imgcenter);

        zoomseek = findViewById(R.id.zoomseek);

        btnmatrialsubmenu = findViewById(R.id.btnmatrialsubmenu);
        btnmatrialsubmenu.setVisibility(View.GONE);

        btnnodamagetype = findViewById(R.id.btnnodamagetype);

        txtalphaname = findViewById(R.id.txtalphaname);
        txtalphaname2 = findViewById(R.id.txtalphaname2);
//        setInteriorvalue();

        tvonrakes = findViewById(R.id.tvonrakes);

        rlhailoptionview = findViewById(R.id.rlhailoptionview);
        rlhailoptionback = findViewById(R.id.rlhailoptionback);

        rbtn_frontslope = findViewById(R.id.rbtn_frontslope);
        rbtn_rightslope = findViewById(R.id.rbtn_rightslope);
        rbtn_rearslope = findViewById(R.id.rbtn_rearslope);
        rbtn_leftslope = findViewById(R.id.rbtn_leftslope);

        txt_frontslope = findViewById(R.id.txt_frontslope);
        txt_rightslope = findViewById(R.id.txt_rightslope);
        txt_rearslope = findViewById(R.id.txt_rearslope);
        txt_leftslope = findViewById(R.id.txt_leftslope);

        btnhailgo = findViewById(R.id.btnhailgo);

        btnlength = findViewById(R.id.btnlength);
        btnwidth = findViewById(R.id.btnwidth);
        btnheight = findViewById(R.id.btnheight);

        btnareatogal = findViewById(R.id.btnareatogal);
        btnSubAreaTogal = findViewById(R.id.btnSubAreaTogal);

        llArea = findViewById(R.id.llArea);
        btnCeiling = findViewById(R.id.btnCeiling);
        btnWall = findViewById(R.id.btnWall);
        btnFloor = findViewById(R.id.btnFloor);

        btnCeiling.setOnClickListener(this);
        btnWall.setOnClickListener(this);
        btnFloor.setOnClickListener(this);

        btnSubAreaTogal.setOnClickListener(this);

        rlhailoptionview.setOnClickListener(this);
        rlhailoptionback.setOnClickListener(this);

        btnlength.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showlengthoption();
            }
        });


        btnwidth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showwidthoption();

            }
        });

        btnheight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showheightoption();

            }
        });

        btnQue.setOnLongClickListener(this);
        btnQue.setOnClickListener(this);
        btnMic.setOnClickListener(this);
        btnReport.setOnClickListener(this);
        btnrei.setOnClickListener(this);
        btndamagetype.setOnClickListener(this);
        btndamagetype1.setOnClickListener(this);
        btnmaterial1.setOnClickListener(this);
        btnabc.setOnClickListener(this);
        btnmaterial.setOnClickListener(this);
        btnlastphoto.setOnClickListener(this);
        btntype.setOnClickListener(this);
        btntype2.setOnClickListener(this);
        btnnodamages.setOnClickListener(this);
        btnhailnodamages.setOnClickListener(this);

        btnhailmaterialdamages.setOnClickListener(this);
        btnnc.setOnClickListener(this);
        btno.setOnClickListener(this);
        btnocb.setOnClickListener(this);

        imgbtnsetting.setOnClickListener(this);
        imgtop.setOnClickListener(this);
        imgbottom.setOnClickListener(this);
        imgleft.setOnClickListener(this);
        imgright.setOnClickListener(this);

        ibtnflash.setOnClickListener(this);
        ibtnLive.setOnClickListener(this);

        imgbtncam.setOnClickListener(this);

        btniteriortype.setOnClickListener(this);
        rlsetting.setOnClickListener(this);
        btnok.setOnClickListener(this);

        btnroofadd.setOnClickListener(this);
        btnaddelevations.setOnClickListener(this);
        btnaddinterior.setOnClickListener(this);
        btnadddamage.setOnClickListener(this);
        btnAddDocument.setOnClickListener(this);
        btnSynchronize.setOnClickListener(this);
        btnLogout.setOnClickListener(this);
        btnesubcatgry.setOnClickListener(this);
        btnaddclaimname.setOnClickListener(this);
        btnmacroadd.setOnClickListener(this);
        rladdtionalback.setOnClickListener(this);


        txt_frontslope.setOnClickListener(this);
        txt_rightslope.setOnClickListener(this);
        txt_rearslope.setOnClickListener(this);
        txt_leftslope.setOnClickListener(this);

        txt_slopeno.setOnClickListener(this);

        btnarea.setOnClickListener(this);
        btnInsulation.setOnClickListener(this);
        btnQty.setOnClickListener(this);
        btnareatogal.setOnClickListener(this);
        btnnodamagetype.setOnClickListener(this);
        rlmain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rlsetting.setVisibility(View.GONE);
            }
        });

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    isBottomShow = true;
                    captureImage();
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            }
        });

        btnBcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation slide = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                        Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                        0.0f, Animation.RELATIVE_TO_SELF, 1.0f);

                slide.setDuration(400);
                slide.setFillAfter(true);
                slide.setFillEnabled(true);
                rlBottomView2.startAnimation(slide);
                slide.setAnimationListener(new Animation.AnimationListener() {

                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                        rlBottomView2.clearAnimation();

                    /*RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                            rlBottomView2.getWidth(), rlBottomView2.getHeight());
                    lp.setMargins(0, rlBottomView2.getWidth(), 0, 0);
                    lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    rlBottomView2.setLayoutParams(lp);*/

                    }
                });
                rlBottomView2.setVisibility(View.GONE);
            }
        });

        btncancel.setOnClickListener(this);

//        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
//            camera = Camera.open();
//            Camera.Parameters p = camera.getParameters();
//            p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
//            camera.setParameters(p);
//        }


        zoomseek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Camera.Parameters param = camera.getParameters();
                param.setZoom(progress);
                camera.setParameters(param);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        rltorch.setOnClickListener(this);
        btnrisk.setOnClickListener(this);
        rlriskphoto.setOnClickListener(this);
        rllayerphoto.setOnClickListener(this);

        btnskip.setOnClickListener(this);

        rlBack.setOnClickListener(this);

        rllayerBack.setOnClickListener(this);
        rlshingleBack.setOnClickListener(this);
        rlgutterBack.setOnClickListener(this);
        rlOverhangBack.setOnClickListener(this);
        rlpitchBack.setOnClickListener(this);
        rlcostmback.setOnClickListener(this);
        rlfeoback.setOnClickListener(this);
        rlhailback.setOnClickListener(this);
        rlblankback.setOnClickListener(this);

        rlshinglephoto.setOnClickListener(this);
        rlgutterphoto.setOnClickListener(this);
        rloverhangphoto.setOnClickListener(this);
        rlpitchphoto.setOnClickListener(this);
        rlcostm.setOnClickListener(this);
        rlfeo.setOnClickListener(this);
        rlhail.setOnClickListener(this);

        btnshinglemenu1.setOnClickListener(this);
        btnshinglemenu2.setOnClickListener(this);

        btnguttermenu1.setOnClickListener(this);

        btnLayersmenu.setOnClickListener(this);

        btnLayersmenu1.setOnClickListener(this);
        btnoverhangmenu1.setOnClickListener(this);

        btnimgmacro.setOnClickListener(this);
        btnimgmacrosub.setOnClickListener(this);
        btnhailmenu1.setOnClickListener(this);
        btnhailtype.setOnClickListener(this);

        btnlayerskip.setOnClickListener(this);
        btnpitchskip.setOnClickListener(this);
        btnshingleskip.setOnClickListener(this);
        btngutterskip.setOnClickListener(this);
        btnoverhangskip.setOnClickListener(this);
        btncostmskip.setOnClickListener(this);
        btnfeoskip.setOnClickListener(this);
        btnhailskip.setOnClickListener(this);

        btnmatrialsubmenu.setOnClickListener(this);
        btncat.setOnClickListener(this);
        btnsubmenu.setOnClickListener(this);

        btnhailgo.setOnClickListener(this);

        getinteriordbvalue();

        accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        surfaceView = findViewById(R.id.surfaceView2);

        calljpg();
        getAllMenu();
        getalphaname();

        rbtn_frontslope.setOnClickListener(this);
        rbtn_rightslope.setOnClickListener(this);
        rbtn_rearslope.setOnClickListener(this);
        rbtn_leftslope.setOnClickListener(this);
        btnpitchmenu.setOnClickListener(this);

        btnr.setOnClickListener(this);
        btne.setOnClickListener(this);

        rl_defaultmenu.setOnClickListener(this);

        setDamageSelect();

        focusMarkerLayout = new FocusMarkerLayout(HomeActivity.this);
        focusMarkerLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        rlmainview.addView(focusMarkerLayout);

        rlmain.setOnTouchListener(onTouchListener());
        rlriskphoto.setOnTouchListener(onTouchListener());
        rllayerphoto.setOnTouchListener(onTouchListener());
        rlpitchphoto.setOnTouchListener(onTouchListener());
        rlshinglephoto.setOnTouchListener(onTouchListener());
        rlgutterphoto.setOnTouchListener(onTouchListener());
        rloverhangphoto.setOnTouchListener(onTouchListener());
        rlcostm.setOnTouchListener(onTouchListener());
        rlfeo.setOnTouchListener(onTouchListener());
        rlhail.setOnTouchListener(onTouchListener());
        rlInteriorPhoto.setOnTouchListener(onTouchListener());
        rlNewMacro.setOnTouchListener(onTouchListener());


        btnrei.setTag("3");
        onclick_R_E_I();

        if(btnrei.getText().toString().equals("E") || btnrei.getText().toString().equals("R"))
        {
            serdefaultvalue();
        }

        btn_replace_front.setOnClickListener(this);
        btn_replace_right.setOnClickListener(this);
        btn_replace_rear.setOnClickListener(this);
        btn_replace_left.setOnClickListener(this);

        btn_repair_front.setOnClickListener(this);
        btn_repair_right.setOnClickListener(this);
        btn_repair_rear.setOnClickListener(this);
        btn_repair_left.setOnClickListener(this);
    }

    private void Select_Replace_Repair(AppCompatButton btn_Select_Replace_Repair, String slopno)
    {
        if(slopno.equalsIgnoreCase("1"))
        {
            btn_replace_front.setBackgroundColor(Color.parseColor("#645B5B"));
            btn_repair_front.setBackgroundColor(Color.parseColor("#645B5B"));
        }
        else if(slopno.equalsIgnoreCase("2"))
        {
            btn_replace_right.setBackgroundColor(Color.parseColor("#645B5B"));
            btn_repair_right.setBackgroundColor(Color.parseColor("#645B5B"));
        }
        else if(slopno.equalsIgnoreCase("3"))
        {
            btn_replace_rear.setBackgroundColor(Color.parseColor("#645B5B"));
            btn_repair_rear.setBackgroundColor(Color.parseColor("#645B5B"));
        }
        else if(slopno.equalsIgnoreCase("4"))
        {
            btn_replace_left.setBackgroundColor(Color.parseColor("#645B5B"));
            btn_repair_left.setBackgroundColor(Color.parseColor("#645B5B"));
        }
        btn_Select_Replace_Repair.setBackgroundColor(Color.parseColor("#FF0000"));
    }

    private void SetSlopNumberDate(String selectno)
    {
        data = new ArrayList<>();

        SlopCount slopCountdata = new SlopCount();
        slopCountdata.setIscheck(false);
        slopCountdata.setNo(0);
        data.add(slopCountdata);

        slopCountdata = new SlopCount();
        slopCountdata.setIscheck(false);
        slopCountdata.setNo(1);
        data.add(slopCountdata);

        slopCountdata = new SlopCount();
        slopCountdata.setIscheck(false);
        slopCountdata.setNo(2);
        data.add(slopCountdata);

        slopCountdata = new SlopCount();
        slopCountdata.setIscheck(false);
        slopCountdata.setNo(3);
        data.add(slopCountdata);

        slopCountdata = new SlopCount();
        slopCountdata.setIscheck(false);
        slopCountdata.setNo(4);
        data.add(slopCountdata);

        slopCountdata = new SlopCount();
        slopCountdata.setIscheck(false);
        slopCountdata.setNo(5);
        data.add(slopCountdata);

        slopCountdata = new SlopCount();
        slopCountdata.setIscheck(false);
        slopCountdata.setNo(6);
        data.add(slopCountdata);

        slopCountdata = new SlopCount();
        slopCountdata.setIscheck(false);
        slopCountdata.setNo(7);
        data.add(slopCountdata);

        slopCountdata = new SlopCount();
        slopCountdata.setIscheck(false);
        slopCountdata.setNo(8);
        data.add(slopCountdata);

        slopCountdata = new SlopCount();
        slopCountdata.setIscheck(false);
        slopCountdata.setNo(9);
        data.add(slopCountdata);

        slopCountdata = new SlopCount();
        slopCountdata.setIscheck(false);
        slopCountdata.setNo(10);
        data.add(slopCountdata);

        int select = Integer.parseInt(selectno);
        for (int i=0;i<data.size();i++)
        {
                if(i==select)
                {
                    data.get(i).setIscheck(true);
                }
        }

        slopListAdapter = new SlopListAdapter(HomeActivity.this, data, new customitemclicklistener() {
            @Override
            public void onItemClick(View v, int position)
            {
                if (selectslop_cur.equals("1")) {
                    no_frontslope = ""+data.get(position).getNo();
                    txt_frontslope.setText(no_frontslope);
                    if(data.get(position).getNo()<repairvalue)
                    {
                        btn_repair_front.performClick();
                    }
                    else
                    {
                        btn_replace_front.performClick();
                    }
                } else if (selectslop_cur.equals("2")) {
                    no_rightslope = ""+data.get(position).getNo();
                    txt_rightslope.setText(no_rightslope);
                    if(data.get(position).getNo()<repairvalue)
                    {
                        btn_repair_right.performClick();
                    }
                    else
                    {
                        btn_replace_right.performClick();
                    }
                } else if (selectslop_cur.equals("3")) {
                    no_rearslope = ""+data.get(position).getNo();
                    txt_rearslope.setText(no_rearslope);

                    if(data.get(position).getNo()<repairvalue)
                    {
                        btn_repair_rear.performClick();
                    }
                    else
                    {
                        btn_replace_rear.performClick();
                    }
                } else if (selectslop_cur.equals("4")) {
                    no_leftslope = ""+data.get(position).getNo();
                    txt_leftslope.setText(no_leftslope);
                    if(data.get(position).getNo()<repairvalue)
                    {
                        btn_repair_left.performClick();
                    }
                    else
                    {
                        btn_replace_left.performClick();
                    }
                }
            }
        });
        rv_slopno.setAdapter(slopListAdapter);
    }

    private void serdefaultvalue()
    {
        appfoldername = lastpathpf.getString("appfoldername", "ClaimMate");
        Log.e("calldefault","==>serdefaultvalue");
//        imgtop.performClick();
        showdamagetypeoption(false);
        showmaterialoption(false);

        btnocb.setTag("3");
        selectocb();
        settopslopselect();

        imgtop.performClick();
//        btnocb.performClick();
//        btnocb.performClick();
//        btnocb.performClick();
//        showmaterialoption(false);
    }

    private void settopslopselect() {

        btntype2.setBackgroundResource(R.drawable.button_background);
        btntype2.setTag("1");

        if (btnrei.getText().toString().equals("R")) {
            selectslope = " Front ";
        } else if (btnrei.getText().toString().equals("E")) {
            selectslope = "Front ";
        } else if (btnrei.getText().toString().equals("I")) {
            selectslope = "Front ";
        }

        setarrowicon(imgtop, R.drawable.rticon);
        btnocb.setTag("3");
        selectocb();
        strboc = "1";
        strboctype = "Overview";
        strfencegame = "Front Run";
    }

    private void calluncheckdata() {
        if (mchko.isChecked() && mchkc.isChecked()) {

        } else {
            btnocb.setText("B");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkAuth();
    }

    private View.OnTouchListener onTouchListener() {
        return new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getPointerCount() > 1) {
                    if (event.getAction() == MotionEvent.ACTION_POINTER_DOWN) {
                        mDist = getFingerSpacing(event);
                    } else if (event.getAction() == MotionEvent.ACTION_MOVE && camera.getParameters().isZoomSupported()) {
                        camera.cancelAutoFocus();
                        handleZoom(event, camera.getParameters());
                    }
                } else {
                    if (event.getActionMasked() == MotionEvent.ACTION_UP) {
                        focusOnTouch(event);
                    }
                }
                return true;
            }
        };
    }

    private void handleZoom(MotionEvent event, Camera.Parameters params) {
        int maxZoom = params.getMaxZoom();
        int zoom = params.getZoom();
        float newDist = getFingerSpacing(event);
        if (newDist > mDist) {
            //zoom in
            if (zoom < maxZoom)
                zoom++;
        } else if (newDist < mDist) {
            //zoom out
            if (zoom > 0)
                zoom--;
        }
        zoomseek.setProgress(zoom);
        mDist = newDist;
//        params.setZoom(zoom);
//        camera.setParameters(params);
    }

    float mDist;

    private float getFingerSpacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    private static final int FOCUS_AREA_SIZE = 300;

    private void focusOnTouch(MotionEvent event) {
        if (camera != null) {
            Camera.Parameters parameters = camera.getParameters();
            if (parameters.getMaxNumMeteringAreas() > 0) {
                focusMarkerLayout.focus(event.getX(), event.getY());
                Rect rect = calculateFocusArea(event.getX(), event.getY());
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
                List<Camera.Area> meteringAreas = new ArrayList<>();
                meteringAreas.add(new Camera.Area(rect, 800));
                parameters.setFocusAreas(meteringAreas);
                try {
                    camera.setParameters(parameters);
                    camera.autoFocus(mAutoFocusTakePictureCallback);
                } catch (Exception e) {
                }
            } else {
                camera.autoFocus(mAutoFocusTakePictureCallback);
            }
        }
    }

    private Rect calculateFocusArea(float x, float y) {
        int left = clamp(Float.valueOf((x / surfaceView.getWidth()) * 2000 - 1000).intValue(), FOCUS_AREA_SIZE);
        int top = clamp(Float.valueOf((y / surfaceView.getHeight()) * 2000 - 1000).intValue(), FOCUS_AREA_SIZE);
        return new Rect(left, top, left + FOCUS_AREA_SIZE, top + FOCUS_AREA_SIZE);
    }

    private int clamp(int touchCoordinateInCameraReper, int focusAreaSize) {
        int result;
        if (Math.abs(touchCoordinateInCameraReper) + focusAreaSize / 2 > 1000) {
            if (touchCoordinateInCameraReper > 0) {
                result = 1000 - focusAreaSize / 2;
            } else {
                result = -1000 + focusAreaSize / 2;
            }
        } else {
            result = touchCoordinateInCameraReper - focusAreaSize / 2;
        }
        return result;
    }

    private Camera.AutoFocusCallback mAutoFocusTakePictureCallback = new Camera.AutoFocusCallback() {
        @Override
        public void onAutoFocus(boolean success, Camera camera) {
            if (success) {
                // do something...
                Log.e("app_res", "success!");
            } else {
                // do something...
                Log.e("app_res", "fail!");
            }
        }
    };

    private void showlengthoption() {
        popupMenu2 = new PopupMenu(this, findViewById(R.id.btnlength));
        for (int i = 0; i < 50; i++) {
            popupMenu2.getMenu().add(Menu.NONE, 0, Menu.NONE, "" + (i + 1));
        }
        popupMenu2.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem arg0) {
                btnlength.setText(arg0.getTitle().toString());
                return false;
            }
        });
        popupMenu2.show();
    }

    private void showwidthoption() {
        popupMenu2 = new PopupMenu(this, findViewById(R.id.btnwidth));
        for (int i = 0; i < 50; i++) {
            popupMenu2.getMenu().add(Menu.NONE, 0, Menu.NONE, "" + (i + 1));
        }
        popupMenu2.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem arg0) {
                btnwidth.setText(arg0.getTitle().toString());
                return false;
            }
        });
        popupMenu2.show();
    }

    private void showheightoption() {
        popupMenu2 = new PopupMenu(this, findViewById(R.id.btnheight));
        for (int i = 0; i < 50; i++) {
            popupMenu2.getMenu().add(Menu.NONE, 0, Menu.NONE, "" + (i + 1));
        }
        popupMenu2.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem arg0) {
                btnheight.setText(arg0.getTitle().toString());
                return false;
            }
        });
        popupMenu2.show();
    }

    private void setDamageSelect() {
        if (btndamagetype.getText().toString().equals("Damage")) {
            btndamagetype.setText("Damage");
            btntype.setVisibility(View.INVISIBLE);
        } else {
            btntype.setVisibility(View.VISIBLE);
        }
        if (rbDamageMark.isChecked()) {
            btntype.setBackgroundResource(R.drawable.red_button_background);
            btntype.setTag("2");
        } else if (rbDamageUnmark.isChecked()) {
            btntype.setBackgroundResource(R.drawable.button_background);
            btntype.setTag("1");
        }
        btnnodamages.setTag("1");
        btnnodamages.setBackgroundResource(R.drawable.button_background);

        btnnc.setTag("1");
        btnnc.setBackgroundResource(R.drawable.button_background);
        btno.setTag("1");
        btno.setBackgroundResource(R.drawable.red_button_background);
        if (btntype.getText().toString().equalsIgnoreCase("BLANK")) {
            btntype.setVisibility(View.INVISIBLE);
        }
        getalphaname();
    }

    private void checkAuth() {
        if (Utility.haveInternet(mContext, false)) {
            ApiClient.getClient().create(APIInterface.class).checkStatus(PrefManager.getUserId()).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    Log.i(TAG, "checkStatusRes = " + response.body());
                    if (response.body() == null) {
                        return;
                    }
                    try {
                        JSONObject jsonObject = new JSONObject(response.body());
                        if (jsonObject.getString("message").equalsIgnoreCase("User blocked!")) {
                            logout();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.i(TAG, "checkStatusError = " + t.toString());
                }
            });
        }
        /*final String struid = PrefManager.getUserId();
        String loginurl = "http://adjuster.claimmate.com/api/LoginAuth/CheckUserStatus";
        RequestQueue mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, loginurl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jobj = new JSONObject(response);
                    if (jobj.getString("success").equals("success")) {
                        if (jobj.getString("response").equals("1")) {
                            rlmainview.setVisibility(View.VISIBLE);
                        } else {
                            rlmainview.setVisibility(View.GONE);
                        }
                    } else {
                        rlmainview.setVisibility(View.GONE);

                        String msg = jobj.getString("message");
                        showalert(msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", struid);
                return params;
            }
        };
        mRequestQueue.add(stringRequest);*/
    }

    private void showalert(String msg) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(HomeActivity.this);
        dialog.setCancelable(false);
        dialog.setTitle("ClaimMate");
        dialog.setMessage(msg);
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent i = new Intent(HomeActivity.this,
                        LoginActivity.class);
                startActivity(i);
                finish();
                //Action for "Delete".
            }
        });

        final AlertDialog alert = dialog.create();
        alert.show();
    }

    private void getAllMenu() {
        getsumcat();
    }

    private void getsumcat() {
        value = 0;
        popupsubcat = new PopupMenu(this, findViewById(R.id.btnabc));
        opendatabase();
        SELECT_SQL = "SELECT * FROM tblsubcat";
        Cur = DB.rawQuery(SELECT_SQL, null);
        if (Cur != null && Cur.getCount() > 0) {
            Cur.moveToFirst();
            do {
                String strname = Cur.getString(Cur.getColumnIndex("name"));
                popupsubcat.getMenu().add(Menu.NONE, value, Menu.NONE, strname);
                value++;
            }
            while (Cur.moveToNext());
        }
        Cur.close();
        DB.close();
        popupsubcat.getMenu().add(Menu.NONE, value, Menu.NONE, "Custom");
//        popupsubcat.getMenu().add(Menu.NONE, 0, Menu.NONE, "None");
//        popupsubcat.getMenu().add(Menu.NONE, 1, Menu.NONE, "Garage");
//        popupsubcat.getMenu().add(Menu.NONE, 2, Menu.NONE, "Shed");
//        popupsubcat.getMenu().add(Menu.NONE, 3, Menu.NONE, "Fence");
//        popupsubcat.getMenu().add(Menu.NONE, 4, Menu.NONE, "Custom");
        popupsubcat.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem arg0) {
                btnmatrialsubmenu.setText("0");
                if (arg0.getTitle().toString().equals("Custom")) {
                    subcatalert();
                } else {
                    btnabc.setText(arg0.getTitle());
                    if (arg0.getTitle().toString().equals("Fence")) {
                        btndamagetype.setVisibility(View.VISIBLE);
                        btndamagetype.setText("Damage");
                        hidefenceallmenu();
                    } else {
                        btndamagetype.setVisibility(View.INVISIBLE);
                        showsubmenuview();
                    }
                    if (btnabc.getText().toString().equalsIgnoreCase("none")) {
                        txtalphaname2.setVisibility(View.INVISIBLE);
                    } else {
                        txtalphaname2.setVisibility(View.VISIBLE);
                    }
                    txtalphaname2.setText(arg0.getTitle());
                    getalphaname();
                }
                return false;
            }
        });
    }

    private void showsubmenuview() {
        btnrei.setVisibility(View.INVISIBLE);
        btnmaterial.setVisibility(View.VISIBLE);
        btndamagetype.setVisibility(View.VISIBLE);
        btnmatrialsubmenu.setVisibility(View.VISIBLE);
        btnarea.setVisibility(View.INVISIBLE);
        rlcontrolview.setVisibility(View.GONE);
        btniteriortype.setVisibility(View.VISIBLE);
        btntype.setVisibility(View.VISIBLE);
        btntype2.setVisibility(View.VISIBLE);
        setbackmenu();
        nextPhoto("Aditional Photo");
        rladdtionalback.setVisibility(View.GONE);
        btnimgmacro.setText("Macro");
        if (btnrei.getText().toString().equals("I")) {
            rliteriortype.setVisibility(View.VISIBLE);
            if (!btnarea.getText().equals("Area")) {
                btnInsulation.setVisibility(View.VISIBLE);
                btnQty.setVisibility(View.VISIBLE);
                btnareatogal.setVisibility(View.INVISIBLE);
                btnSubAreaTogal.setVisibility(View.VISIBLE);
            }
        } else {
            rliteriortype.setVisibility(View.GONE);
            btnQty.setVisibility(View.VISIBLE);
        }
        getalphaname();
        btnimgmacrosub.setVisibility(View.GONE);
        btnimgmacro.setVisibility(View.VISIBLE);
    }

    private void addRoofDataApi() {
        String userId = PrefManager.getUserId();
        String claimId = PrefManager.getClaimId();
        Callback<String> callback = new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.i(TAG, "addRoof" + btnrisk.getText().toString() + "Res = " + response.body());
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.i(TAG, "addRoof" + btnrisk.getText().toString() + "Error = " + t.toString());
            }
        };
        if (btnrisk.getText().toString().trim().equals("Layers")) { // 1987
            String noLayer = btnLayersmenu1.getText().toString();
            String layerType = btnLayersmenu.getText().toString();
            if (!Utility.haveInternet(mContext, false)) {
                claimDbHelper.addRoofLayer(userId, claimId, noLayer, layerType);
                return;
            }
            ApiClient.getClient().create(APIInterface.class).addRoofLayer(userId, claimId, noLayer, layerType).enqueue(callback);
            System.out.println("layerType:-" + layerType);
            //System.out.println("pitchvalue id:-" + pitchvalue);
        } else if (btnrisk.getText().toString().trim().equals("Pitch")) { //2008
            String pitchvalue = lblpitchvalue.getText().toString().trim(); //.replace("/12", "") + "/12";
            System.out.println("pitch value 1:-" + pitchvalue);
            System.out.println("pitchvalue id:-" + pitchvalue);
            String slope = btnpitchmenu.getText().toString().trim();
            System.out.println("pitch value slope1:-" + pitchvalue);
            pitchvaluestorevalue = pitchvalue.replace("/12", "");
            System.out.println("pitch value pitchvaluestorevalue:-" + pitchvaluestorevalue);
            if (!Utility.haveInternet(mContext, false)) {
                claimDbHelper.addRoofPitch(userId, claimId, pitchvalue, slope);
                return;
            }
            ApiClient.getClient().create(APIInterface.class).addRoofPitch(userId, claimId, pitchvaluestorevalue, slope).enqueue(callback);
//            System.out.println("user id:-" + userId);
//            System.out.println("claimId id:-" + claimId);
            System.out.println("pitchvalue id:-" + pitchvalue);
//            System.out.println("slope id:-" + slope);
            //ApiClient.getClient().create(APIInterface.class).addRoofPitch(userId, claimId, pitchvalue, slope).enqueue(callback);

        } else if (btnrisk.getText().toString().trim().equals("Shingle")) { //2030
            String years = btnshinglemenu1.getText().toString();
            String tab = btnshinglemenu2.getText().toString();
            if (!Utility.haveInternet(mContext, false)) {
                claimDbHelper.addRoofShingle(userId, claimId, years, tab);
                return;
            }
            ApiClient.getClient().create(APIInterface.class).addRoofShingle(userId, claimId, years, tab).enqueue(callback);
            System.out.println("user id" + userId);
            System.out.println("claimId id" + claimId);
            System.out.println("years id" + years);
            System.out.println("tab id" + tab);
        }
    }

    private void addRiskMacroDataApi() {
        String userId = PrefManager.getUserId();
        String claimId = PrefManager.getClaimId();
        String story = txtStory.getText().toString();
        String dwl_first = "";
        String dwl_first_custom = "";
        String dwl_second = "";
        String dwl_second_custom= "";
        String dwl_third="";
        String dwl_third_custom="";
        String dwl_fourth="";
        String  dwl_fourth_custom = "";
        String dwl_fifth="";
        String dwl_fifth_custom="";

        if (btnTypeOfConstruction.getTag().toString().equalsIgnoreCase("Insert custom data")) {
            dwl_first = btnTypeOfConstruction.getTag().toString();
            dwl_first_custom = btnTypeOfConstruction.getText().toString();
        } else {
            dwl_first = btnTypeOfConstruction.getText().toString();
            dwl_first_custom = "";
        }
        if (btnRCI.getTag().toString().equalsIgnoreCase("Insert custom data")) {
            dwl_second = btnRCI.getTag().toString();
            dwl_second_custom = btnRCI.getText().toString();
        } else {
            dwl_second = btnRCI.getText().toString();
            dwl_second_custom = "";
        }
        if (btnSingleFamily.getTag().toString().equalsIgnoreCase("Insert custom data") || btnRCI.getText().toString().equalsIgnoreCase("Residential") || btnRCI.getText().toString().equalsIgnoreCase("Commercial")) {
            dwl_third = "Insert custom data";
            dwl_third_custom = btnSingleFamily.getText().toString();
        } else {
            dwl_third = btnSingleFamily.getText().toString();
            dwl_third_custom = "";
        }
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("1", txtCar.getText().toString());
            jsonObject.put("2", chkAttached.isChecked() ? "attached" : "detached");
            jsonArray.put(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        dwl_fourth = jsonArray.toString();
        /*if (btnGarageAttached.getTag().toString().equalsIgnoreCase("Insert custom data")) {
            dwl_fourth = btnGarageAttached.getTag().toString();
            dwl_fourth_custom = btnGarageAttached.getText().toString();
        } else {
            dwl_fourth = btnGarageAttached.getText().toString();
            dwl_fourth_custom = "";
        }*/
        if (btnTypeOfExteriorSiding.getTag().toString().equalsIgnoreCase("Insert custom data")) {
            dwl_fifth = btnTypeOfExteriorSiding.getTag().toString();
            dwl_fifth_custom = btnTypeOfExteriorSiding.getText().toString();
        } else {
            dwl_fifth = btnTypeOfExteriorSiding.getText().toString();
            dwl_fifth_custom = "";
        }
        if (!Utility.haveInternet(mContext, false)) {
//            claimDbHelper.addRiskMacroDes(userId, claimId, story, typeOfConstruction, rci, singleFamily, garageAttached, typeOfExteriorSiding);
            return;
        }
        ApiClient.getClient().create(APIInterface.class).addRiskMacroDes(userId, claimId, story, dwl_first, dwl_first_custom, dwl_second, dwl_second_custom, dwl_third, dwl_third_custom, dwl_fourth, dwl_fourth_custom, dwl_fifth, dwl_fifth_custom).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.i(TAG, "addRiskMacroDesRes = " + response.body());
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.i(TAG, "addRiskMacroDesError = " + t.toString());
            }
        });
    }

    private void addRoofElevationInteriorDataApi() {
        String userId = PrefManager.getUserId(), claimId = PrefManager.getClaimId(), material = btnmaterial.getText().toString(), qty = btnQty.getText().toString().equalsIgnoreCase("Quantity") ? "N/A" : btnQty.getText().toString(), damage = "N/A";
        if (btnnodamages.getTag().equals("2")) {
            damage = "N/D";
        } else {
            if (!btndamagetype.getTag().toString().trim().equals("0")) {
                if (!btntype.getTag().equals("1")) {
                    if (btndamagetype.getText().toString().trim().equals("Custom text")) {
                        damage = "N/A";
                    } else {
                        damage = btndamagetype.getText().toString().trim();
                    }
                }
            }
        }

        if (btnrei.getText().toString().equalsIgnoreCase("R")) {
            String slope = (selectslope.equals("") ? "N/A" : selectslope).trim();
            material = material.equalsIgnoreCase("Material") ? "N/A" : material;
            Log.i(TAG, "addRoofParam material = " + material + ", quantity = " + qty + ", slope = " + slope + ", damage = " + damage);
            if (!Utility.haveInternet(mContext, false)) {
                claimDbHelper.addRoof(userId, claimId, material, qty, slope, damage);
                return;
            }
            ApiClient.getClient().create(APIInterface.class).addRoof(userId, claimId, material, qty, slope, damage).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    Log.i(TAG, "addRoofRes = " + response.body());
                }
                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.i(TAG, "addRoofError = " + t.toString());
                }
            });
        } else if (btnrei.getText().toString().equalsIgnoreCase("e")) {
            String elevation = (selectslope.equals("") ? "N/A" : selectslope).trim();
            material = material.equalsIgnoreCase("Material") ? "N/A" : material;
            Log.i(TAG, "addElevationParam material = " + material + ", quantity = " + qty + ", elevation = " + elevation + ", damage = " + damage);
            if (!Utility.haveInternet(mContext, false)) {
                claimDbHelper.addElevation(userId, claimId, material, qty, elevation, damage);
                return;
            }
            ApiClient.getClient().create(APIInterface.class).addElevation(userId, claimId, material, qty, elevation, damage).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    Log.i(TAG, "addElevationRes = " + response.body());
                }
                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.i(TAG, "addElevationError = " + t.toString());
                }
            });
        } else if (!btnarea.getText().toString().equalsIgnoreCase("area")) {
            String areaType = btnarea.getText().toString(), insulation = btnInsulation.getText().toString().equals("Insulation") ? "N/A" : btnInsulation.getText().toString();
            Log.i(TAG, "addInteriorDataApiParam  areaType = " + areaType + ", material = " + material + ", insulation = " + insulation + ", qty = " + qty);
            System.out.println("areaType =:-" + areaType);
            System.out.println("material =:-" + material);
            System.out.println("insulation =:-" + insulation);
            System.out.println("qty =:-" + qty);
            if (!Utility.haveInternet(mContext, false)) {
                claimDbHelper.addInteriorArea(userId, claimId, areaType, material, insulation, qty);
                return;
            }
            ApiClient.getClient().create(APIInterface.class).addInteriorArea(userId, claimId, areaType, material, insulation, qty).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    Log.i(TAG, "addInteriorAreaRes = " + response.body());
                    System.out.println("addInteriorArea response:-" + response.body());
                }
                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.i(TAG, "addInteriorAreaError = " + t.toString());
                }
            });
        }
    }

    private void addInteriorRoomMacroApi() {
        String userId = PrefManager.getUserId(), claimId = PrefManager.getClaimId();
        String areaType = btnInteriorNewMacro.getText().toString(), material = btnIntNewMaterialRed.getTag().toString().equals("1") ? btnIntNewMaterial.getText().toString() : "Blank", insulation = btnIntNewInsulation.getText().toString().equals("Insulation") ? "N/A" : btnIntNewInsulation.getText().toString(), qty = btnIntNewQty.getText().toString().equalsIgnoreCase("Quantity") ? "N/A" : btnIntNewQty.getText().toString();
        ApiClient.getClient().create(APIInterface.class).addInteriorArea(userId, claimId, areaType, material, insulation, qty).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.i(TAG, "addInteriorAreaRes = " + response.body());
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.i(TAG, "addInteriorAreaError = " + t.toString());
            }
        });
    }

    public void addRoomMacroApi() {
        String roomName = intRoomFullName, ocb = "", area = btnRoomMacro.getText().toString(), affectsWall = chkRoomMacroAffects.isChecked() + "", damage = btnRoomMacroDamage.getText().toString(), material1 = btnRoomMacroMaterial.getText().toString(), material2 = btnRoomMacroMaterialFinish.getText().toString(), addItem = btnRoomMacroPlus.getText().toString(), LWH = "", damageAmount = btnRoomMacroDamageAmount.getText().toString();
        if (btnocb.getText().toString().equalsIgnoreCase("O"))
            ocb = "Overview";
        else if (btnocb.getText().toString().equalsIgnoreCase("C"))
            ocb = "Close up";
        JSONArray jsonArray = new JSONArray();
        for (HashMap<String, String> hm : arrayListLWHRoomMacro) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("l", hm.get("l"));
                jsonObject.put("w", hm.get("w"));
                jsonObject.put("h", hm.get("h"));
                jsonArray.put(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        LWH = jsonArray.toString();
        HashMap<String, String> hmParam = new HashMap<>();
        hmParam.put("user_id", PrefManager.getUserId());
        hmParam.put("claim_id", PrefManager.getClaimId());
        hmParam.put("room_name", roomName);
        hmParam.put("ocb", ocb);
        hmParam.put("area", area);
        hmParam.put("affectsWall", affectsWall);
        hmParam.put("damage", damage);
        hmParam.put("material1", material1);
        hmParam.put("material2", material2);
        hmParam.put("addItem", addItem);
        hmParam.put("LWH", LWH);
        hmParam.put("damageAmount", damageAmount);
        Log.i(TAG, "addRoomMacro Parm = " + hmParam.toString());
        ApiClient.getClient().create(APIInterface.class).addRoomMacro(hmParam).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.i(TAG, "addRoomMacro res = " + response.body());
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.i(TAG, "addRoomMacro error = " + t.toString());
            }
        });
    }

    private void calljpg() {
        jpegCallback = new PictureCallback() {
            public void onPictureTaken(byte[] data, Camera camera) {
//
//                btno.setVisibility(View.VISIBLE);
//                btnnc.setVisibility(View.VISIBLE );
                lastimageeditor = lastpathpf.edit();
                if(btnrisk.getVisibility() == View.VISIBLE)
                {
                    btnrisk.setVisibility(View.INVISIBLE);
                }
                if(btnimgmacrosub.getVisibility() == View.VISIBLE)
                {
                    btnimgmacrosub.setVisibility(View.INVISIBLE);
                }
                String feo = "";
                String TimeStamp = dateFormat.format(new Date());
                String FolderTimeStamp = folderdateFormat.format(new Date());
                String currentTimeStamp = "" + photoindex;//dateFormat.format(new Date());
                Log.e("currentTimeStamp","" + currentTimeStamp);
                getalphaname();
                if (btnrisk.getText().toString().trim().equals("Pitch")) {
                    screenshotHideview();
                    bmoverlay = loadBitmapFromView(rlmain);
                    screenshotShowview();
                }
                if (btnrisk.getText().toString().trim().equals("New Aditional photo")) {
                    screenshotHideviewAditional();
                    bmoverlay = loadBitmapFromView(rlmain);
                    screenshotShowviewAditional();
                }
                mydir = new File(appdir, appfoldername);
                if(!mydir.exists())
                {
                    mydir.mkdir();
                }
                String mydirpath = mydir.getAbsolutePath();
                Log.e("GetPath", "==>" + mydirpath);
                Log.e("GetPathdate", "==>" + FolderTimeStamp);
                File fileTimeStamp = new File(mydir,FolderTimeStamp);
                if(!fileTimeStamp.exists())
                {
                    fileTimeStamp.mkdir();
                }
                /*if (!mydirpath.contains(FolderTimeStamp.toString()))
                {
                    String[] arr = mydirpath.split("/");
                    String fname = arr[arr.length - 1];
                    String Save_file_name = fname + " " + FolderTimeStamp;
                    mydirpath = mydirpath.replace(fname, Save_file_name);
                    mydir = new File(mydirpath);
                }*/
           /*     if (!mydir.exists())
                    mydir.mkdirs();
                else
                    Log.d("error", "dir. already exists");
*/
                if (isBottomShow)
                {
                    subdir = new File(fileTimeStamp, "Screenshots");
                    if (!subdir.exists())
                        subdir.mkdirs();
                    else
                        Log.d("error", "dir. already exists");
                    savefile1 = subdir;
                } else if (btnabc.getText().toString().equals("None")) {
                    savefile1 = fileTimeStamp;
                } else {
                    subdir = new File(fileTimeStamp, btncat.getText().toString() + " " + btnabc.getText().toString());
                    if (!subdir.exists())
                        subdir.mkdirs();
                    else
                        Log.d("error", "dir. already exists");
                    savefile1 = subdir;
                }
                reidir = savefile1;
                String strisNone = "";
                if (btnabc.getText().toString().equals("None")) {
                    savefile = reidir;
                    strisNone = "";
                } else {
                    strisNone = btnabc.getText().toString();
                    savefile = reidir;
                }
                strname = "";
                FileOutputStream outStream = null;
                try {
                    String imgname = "";
                    String space = "                                ";
                    if (btnimgmacro.getText().toString().equals("Hail")) {
                        frontslopeimgindex = lastpathpf.getInt("frontslopeimgindex", 1);
                        rightslopeimgindex = lastpathpf.getInt("rightslopeimgindex", 1);
                        rearslopeimgindex = lastpathpf.getInt("rearslopeimgindex", 1);
                        leftslopeimgindex = lastpathpf.getInt("leftslopeimgindex", 1);
                        String strgethailimagename = gethailimgname();
                        if (btnrei.getText().toString().trim().equals("R")) {
                            if (strgethailimagename.contains(" Overview")) {
                                strgethailimagename = strgethailimagename.replace("Overview", " " + strboctype);
                                if(strgethailimagename.contains(" Close up"))
                                {
                                    strgethailimagename = strgethailimagename.replace(" Close up", "" + strboctype);
                                }
                            } else if (strgethailimagename.contains("Overview")) {
                                strgethailimagename = strgethailimagename.replace("Overview", " " + strboctype);
                                if(strgethailimagename.contains("Close up"))
                                {
                                    strgethailimagename = strgethailimagename.replace(" Close up", "" + strboctype);
                                }
                            }
                            if (strgethailimagename.contains(" test sq")) {
                                strgethailimagename = strgethailimagename.replace("test sq", "test sq");
                            } else if (strgethailimagename.contains("test sq")) {
                                strgethailimagename = strgethailimagename.replace("test sq", "test sq");
                            }
                            if (strgethailimagename.contains(" test sq  Overview")) {
                                strgethailimagename = strgethailimagename.replace("test sq  Overview", "test sq  " + strboctype);
                            } else if (strgethailimagename.contains("test sq Overview")) {
                                strgethailimagename = strgethailimagename.replace("test sq Overview", "test sq  " + strboctype);
                            }
                        }
                        String afternum = "";
                        if (btnimgmacrosub.getText().toString().trim().equals("test sq Overview")) {
                            afternum = "hits ";
                        } else if (btnimgmacrosub.getText().toString().trim().equals("test sq")) {
                            afternum = "hits per sq";
                        }
                        if (btnimgmacrosub.getText().toString().trim().equals("hail damage close up on shingles")) {
                            String strhaildamage1 = "";

                            if (!strhaildamage.trim().equals("Blank")) {
                                strhaildamage1 = strhaildamage.trim();
                            }

                            if (strboctype.equals("Blank")) {
                                strboctype = "";
                            }

                            strgethailimagename = btnhailtype.getText().toString() + " " + strhaildamage1 + " " + strboctype + " on " + strhailmaterialdamage + "";

                            if (strgethailimagename.contains(" Overview")) {
                                strgethailimagename = strgethailimagename.replace("Overview", " " + strboctype);
                                //  strgethailimagename="Front Slop "+ strboctype;
                            } else if (strgethailimagename.contains("Overview")) {
                                strgethailimagename = strgethailimagename.replace("Overview", "  " + strboctype);
                                //strgethailimagename="Front Slop "+ strboctype;
                            }

                            if (strgethailimagename.contains(" Close up")) {
                                strgethailimagename = strgethailimagename.replace(strboctype, "" + strboctype);
                                //strgethailimagename=strboctype;
                            } else if (strgethailimagename.contains("Close up")) {
                                strgethailimagename = strgethailimagename.replace(strboctype, "" + strboctype);
                                //strgethailimagename=strboctype;
                            }else if (strgethailimagename.contains("  Close up")) {
                                strgethailimagename = strgethailimagename.replace("  "+strboctype, " " + strboctype);
                                //strgethailimagename=strboctype;
                            }

                        } else {
                            strgethailimagename = strgethailimagename + " " + btnhailmenu1.getText().toString().trim() + " " + afternum;
                        }

                        Log.e("strgethailimagename2", "" + strgethailimagename);


                        String indeximage = "";
                        if (btnhailtype.getText().equals("Front Slope")) {
                            imgname = strgethailimagename + indeximage + " " + strisNone + space + TimeStamp + ".jpg";//dateFormat.format(new Date());
                            frontslopeimgindex++;
                            lastimageeditor.putInt("frontslopeimgindex", frontslopeimgindex);
                            lastimageeditor.commit();

                        } else if (btnhailtype.getText().equals("Right Slope")) {
                            imgname = strgethailimagename + indeximage + " " + strisNone + space + TimeStamp + ".jpg";//dateFormat.format(new Date());
                            rightslopeimgindex++;
                            lastimageeditor.putInt("rightslopeimgindex", rightslopeimgindex);
                            lastimageeditor.commit();

                        } else if (btnhailtype.getText().equals("Rear Slope")) {

                            imgname = strgethailimagename + indeximage + " " + strisNone + space + TimeStamp + ".jpg";//dateFormat.format(new Date());
                            rearslopeimgindex++;
                            lastimageeditor.putInt("rearslopeimgindex", rearslopeimgindex);
                            lastimageeditor.commit();

                        } else if (btnhailtype.getText().equals("Left Slope")) {
                            imgname = strgethailimagename + indeximage + " " + strisNone + space + TimeStamp + ".jpg";//dateFormat.format(new Date());
                            leftslopeimgindex++;
                            lastimageeditor.putInt("leftslopeimgindex", leftslopeimgindex);
                            lastimageeditor.commit();

                        }
                        Log.e("strgethailimagename3", "" + imgname);


                        imgname = "  " + imgname;

//                        if(imgname.contains("Slope  Overview on Shingles"))
//                        {
//                            imgname = imgname.replace("Slope  Overview on Shingles","  Overview on Shingles");
//                        }

//                        Toast.makeText(mContext, strgethailimagename, Toast.LENGTH_LONG).show();

                        adddbimg(strgethailimagename);
                    } else if (btnInteriorMacro.getVisibility() == View.VISIBLE) {
                        imgname = txtalphaname.getText().toString() + space + currentTimeStamp + ".jpg";
                    } else if (btnInteriorNewMacro.getVisibility() == View.VISIBLE) {
                        imgname = txtalphaname.getText().toString() + space + currentTimeStamp + ".jpg";
                        imgname = imgname.replace(intRoomName, intRoomFullName);
                        addInteriorRoomMacroApi();
                    } else if (btnRoomMacro.getVisibility() == View.VISIBLE) {
                        imgname = txtalphaname.getText().toString() + space + currentTimeStamp + ".jpg";
                        imgname = imgname.replace(intRoomName, intRoomFullName);
                        addRoomMacroApi();
                    } else if (rlNewMacro.getVisibility() == View.VISIBLE) {
                        imgname = txtalphaname.getText().toString() + space + currentTimeStamp + ".jpg";
                    } else if (btnrisk.getText().toString().trim().equals("Aditional Photo")) {

                        strreitype = "";

                        if (btnrei.getText().toString().trim().equals("I")) {
//                            strreitype = itrarr.get(iteriortypeindex) + "-";
                            strreitype = btniteriortype.getText().toString();
                            if (!btnmatrialsubmenu.getText().toString().equals("0"))
                                strreitype += " " + btnmatrialsubmenu.getText().toString();
                        } else if (!selectslope.equals("")) {
                            if (btnrei.getText().toString().trim().equals("R")) {
                                strreitype = selectslope;//+ "Slope-"
                            } else if (btnrei.getText().toString().trim().equals("E")) {
                                strreitype = selectslope;//+ "Elev-"
                            }
                        }

                        String strdamagetype = "";
                        String strmaterial = "";
                        String strnodamage = "";
                        String areaname = "";

                        if (btnnodamages.getTag().equals("2")) {
                            strnodamage = " No Damage";
                        } else {

                            if (!btndamagetype.getTag().toString().trim().equals("0")) {
                                if (!btntype.getTag().equals("1")) {
                                    if (btndamagetype.getText().toString().trim().equals("Custom text")) {
                                        strdamagetype = " " + damagecostmtext;
                                    } else {

                                        if (btndamagetype.getText().toString().equals("Damage")) {
                                            strdamagetype = "";
                                        } else {
                                            strdamagetype = " " + btndamagetype.getText().toString().trim();
                                        }

                                    }
                                }
                            }

                            if (btnrei.getText().toString().trim().equals("I")) {
                                if (btnarea.getText().toString().trim().equals("Area")) {
                                    areaname = "";

                                } else {
                                    areaname = " " + btnarea.getText().toString().trim();
                                }
                            } else {
                                areaname = "";
                            }
                        }

                     /*   if (!btnmaterial.getTag().toString().trim().equals("0")) {
                            if (!btntype2.getTag().equals("1")) {
                                if (btnmaterial.getText().toString().trim().equals("Custom text")) {
                                    strmaterial = " " + matrialcostmtext;

                                }
                                else {
                                    if(btnmaterial.getText().toString().equals("Material"))
                                    {
                                        strmaterial = "";
                                    }else {
                                        strmaterial = " "+btnmaterial.getText().toString().trim();
                                    }
                                }
                            }
                        }
                        else
                        {
                            strmaterial = "Dmak";
                        }*/


                        if(!btntype2.getTag().equals("1")|| btnrei.getText().toString().equalsIgnoreCase("i"))
                        {if(btnrei.getText().toString().equalsIgnoreCase("i"))
                        {
                            if (btnmaterial.getText().toString().trim().equalsIgnoreCase("Custom text") )
                            {
                                strmaterial = matrialcostmtext + "   ";
                            }
                            else
                            {
                                if(btnmaterial.getText().toString().equals("Material"))
                                {
                                    strmaterial = "";
                                }else {


                                    if(btnSubAreaTogal.getVisibility() == View.VISIBLE && btnSubAreaTogal.getTag().toString().equalsIgnoreCase("2"))
                                    {
                                        strmaterial = ""+btnmaterial.getText().toString().trim() + "   ";
                                    }
                                    else
                                    {
                                        strmaterial = "";
                                    }
                                }

                            }

                        }
                        else
                        {
                            if (btnmaterial.getText().toString().trim().equalsIgnoreCase("Custom text") )
                            {
                                strmaterial = matrialcostmtext + "   ";
                            }
                            else
                            {
                                if(btnmaterial.getText().toString().equals("Material"))
                                {
                                    strmaterial = "";
                                }else {

                                    strmaterial = ""+btnmaterial.getText().toString().trim() + "   ";
                                }

                            }
                        }

                            Log.e("strmaterial_if",""+strmaterial);

                        }
                        else
                        {
                            Log.e("strmaterial_else",""+strmaterial);

                        }

                        strmaterial = strmaterial+" ";
                        Log.e("strmaterial->","==>"+strmaterial);
                        if (btnrei.getText().toString().endsWith("I")) {
                            strname = strreitype + ((areaname.equalsIgnoreCase(" Ceiling") || areaname.equalsIgnoreCase(" wall")) ? " " : "") + areaname + strdamagetype + strnodamage + strmaterial +/*(btnocb.getTag().toString().equals("1")?"  ":" ") +*/" " + strboctype.trim() + " ";
                        } else {
                            strname = strreitype + (btnrei.getText().toString().trim().equals("R") ? "Roof" : "Elevation") + strmaterial + strdamagetype + strnodamage + " " + (btnocb.getTag().equals("1") ? "Overview" : (btnocb.getTag().equals("2") ? "Close up" : "")) + " ";
                        }

                        Log.e("getstrname", "==>" + strname);

                        if (strname.contains("Blank  ")) {
                            strname = strname.replace("Blank  ", "");
                        }

                        if (strname.contains("  Blank")) {
                            strname = strname.replace("  Blank", "");
                        }

                        if (strname.contains(" Blank")) {
                            strname = strname.replace(" Blank", "");
                        }

                        if (strname.contains(" Blank")) {
                            strname = strname.replace(" Blank", "");
                        }

                        if (strname.contains(" Blank ")) {
                            strname = strname.replace(" Blank ", "");
                        }

                        if (btnrei.getText().toString().trim().equals("R")) {
                            if (btnmaterial.getText().toString().equals("test sq")) {
                                strname = strname + " " + btnmatrialsubmenu.getText().toString().trim() + " ";
                            }
                        }

                        if (btnrei.getText().toString().equals("I")) {
                            imgname = strname + "" + strisNone + space + currentTimeStamp + ".jpg";
                        } else {
                            if (strname.equalsIgnoreCase("Slope Overview "))
                                strname = "Roof Overview ";
                            imgname = strname + "" + strisNone + space + currentTimeStamp + ".jpg";
                        }

                        if (btnrei.getText().toString().equalsIgnoreCase("r"))
                            imgname = "  " + imgname.trim();
                        else if (btnrei.getText().toString().equalsIgnoreCase("e"))
                            imgname = " " + imgname.trim();
                        else
                            imgname = imgname.trim();

                        // call api
                        addRoofElevationInteriorDataApi();

                    } else if (btnrisk.getText().toString().trim().equals("Risk")) {

                        imgname = "Risk Photo" + " " + strisNone + space + currentTimeStamp + ".jpg";//dateFormat.format(new Date());
                        imgname = "      " + imgname.trim();
                        riskindex++;
                        lastimageeditor.putInt("riskindex", riskindex);
                        lastimageeditor.commit();

                        photoindex++;
                        lastimageeditor.putInt("photoindex", photoindex);
                        lastimageeditor.commit();

                        // api call
                        addRiskMacroDataApi();
                    } else if (btnrisk.getText().toString().trim().equals("Risk2")) {

                        imgname = "Risk Photo" + " " + strisNone + space + currentTimeStamp + ".jpg";
                        imgname = "      " + imgname.trim();
                        riskindex++;
                        lastimageeditor.putInt("riskindex", riskindex);
                        lastimageeditor.commit();

                        photoindex++;
                        lastimageeditor.putInt("photoindex", photoindex);
                        lastimageeditor.commit();
                    } else if (btnrisk.getText().toString().trim().equals("Front Elevation Overview")) {
                        feo = " ";
                        imgname = "Front Elevation Overview" + space + currentTimeStamp + ".jpg";//dateFormat.format(new Date());
                        photoindex++;
                        lastimageeditor.putInt("photoindex", photoindex);
                        lastimageeditor.commit();
                    } else if (btnrisk.getText().toString().trim().equals("Roof Overview")) {
                        imgname = "    Roof Overview" + space + TimeStamp + ".jpg";//dateFormat.format(new Date());
                    } else if (btnrisk.getText().toString().trim().equals("Blank")) {
                        imgname = space + TimeStamp + ".jpg";
                    } else if (btnrisk.getText().toString().trim().equals("Layers")) {

                        if (layerphoto == 0)
                            imgname = btnLayersmenu1.getText().toString() + " Layer with " + btnLayersmenu.getText().toString() + " " + strisNone + space + TimeStamp + ".jpg";//dateFormat.format(new Date());
                        else
                            imgname = btnLayersmenu1.getText().toString() + " Layer with " + btnLayersmenu.getText().toString() + " on rakes " + strisNone + space + TimeStamp + ".jpg";//dateFormat.format(new Date());

                        imgname = "     " + imgname.trim();

                        addRoofDataApi();
                    } else if (btnrisk.getText().toString().trim().equals("Pitch")) {
                        btnpitchmenu.setVisibility(View.VISIBLE);
                        String pitchvalue = lblpitchvalue.getText().toString().trim();
                        pitchvaluestorevalue = pitchvalue;
                        pitchvalue = pitchvalue.replace("/12", "");
                        System.out.println("pitch value slope2:-" + pitchvalue);
                        if (btnpitchmenu.getText().toString().trim().equals("Blank")) {
                            imgname = pitchvalue + " roof pitch " + strisNone + space + TimeStamp + ".jpg";

                        } else {
                            imgname = pitchvalue + " roof pitch " + strisNone + "" + btnpitchmenu.getText().toString().trim() + space + TimeStamp + ".jpg";
                        }

                        imgname = "     " + imgname.trim();
                        Log.i(TAG, "imgname = " + imgname);

                        addRoofDataApi();
                    } else if (btnrisk.getText().toString().trim().equals("New Aditional photo")) {
                        btnpitchmenu.setVisibility(View.VISIBLE);
                        txtalphaname.setText(addcostmphotoname);
                        String pitchvalue = txtalphaname.getText().toString().trim();
                        System.out.println("txtalphaname:-" + pitchvalue);
                        if (btnrisk.getText().toString().trim().equals("New Aditional photo")) {
                            imgname = pitchvalue + ".jpg";

                        } else {
                            imgname = txtalphaname.getText().toString().trim() + ".jpg";
                        }

                        imgname = imgname.trim();
                        Log.i(TAG, "imgname = " + imgname);


                    } else if (btnrisk.getText().toString().trim().equals("Shingle")) {
                        btnpitchmenu.setVisibility(View.VISIBLE);

                        /*if (btnrei.getText().toString().trim().equals("R")) {
                            imgname = "  " + btnshinglemenu1.getText().toString() + " " + btnshinglemenu2.getText().toString() + " Shingle" + " " + strisNone + "                                        " + TimeStamp + ".jpg";//dateFormat.format(new Date());
                        } else {
                            imgname = btnshinglemenu1.getText().toString() + " " + btnshinglemenu2.getText().toString() + " Shingle" + " " + strisNone + "                                        " + TimeStamp + ".jpg";//dateFormat.format(new Date());
                        }*/
                        imgname = btnshinglemenu1.getText().toString() + " " + btnshinglemenu2.getText().toString() + " Shingle" + " " + strisNone + space + TimeStamp + ".jpg";//dateFormat.format(new Date());
                        imgname = "     " + imgname.trim();

                        addRoofDataApi();
                    } else if (btnrisk.getText().toString().trim().equals("Gutter")) {

                        /*if (btnrei.getText().toString().trim().equals("R")) {
                            imgname = "  " + btnguttermenu1.getText().toString() + " on this dwelling" + " " + strisNone + "                                        " + TimeStamp + ".jpg";//dateFormat.format(new Date());
                        } else {
                            imgname = btnguttermenu1.getText().toString() + " on this dwelling" + " " + strisNone + "                                        " + TimeStamp + ".jpg";//dateFormat.format(new Date());
                        }*/
                        imgname = btnguttermenu1.getText().toString() + " " + strisNone + space + TimeStamp + ".jpg";//dateFormat.format(new Date());
                        imgname = "     " + imgname.trim();
                    } else if (btnrisk.getText().toString().trim().equals("Overhang")) {

                        if (btnrei.getText().toString().trim().equals("R")) {
                            imgname = "  " + btnoverhangmenu1.getText().toString() + " inch overhang" + " " + strisNone + space + TimeStamp + ".jpg";//dateFormat.format(new Date());

                        } else {
                            imgname = btnoverhangmenu1.getText().toString() + " inch overhang" + " " + strisNone + space + TimeStamp + ".jpg";//dateFormat.format(new Date());
                        }
                        imgname = "     " + imgname.trim();
                    } else if (btnrisk.getText().toString().trim().equals("Type of siding")) {
                        imgname = txtalphaname.getText().toString() + space + TimeStamp + ".jpg";
                        imgname = " " + imgname.trim();
                    } else {
                        imgname = getcoustomname(btnsubmenu, macrosubmenu) + space + TimeStamp + ".jpg";//dateFormat.format(new Date());
                    }

                    Log.e("claimpath", "==>" + savefile.getAbsolutePath());
                    Log.e("claimpathfilename", "==>" + imgname);

                    if (imgname.contains("Overview")) {
                        if (btnrei.getText().toString().endsWith("I"))
                            imgname = imgname.replace("Overview", " Overview");
                        else
                            imgname = imgname.replace("Overview", "Overview");
                        Log.e("imgName", "over = " + imgname);
                    }


//                    if(imgname.contains("Front Slope Hail Damage Slope Overview on Shingles"))
//                    {
//
//                        imgname = imgname.replace("Front Slope Hail Damage Slope Overview on Shingles", "Front Slope Hail Damage Overview on Shingles");
//
//                    }



                    Log.e("imgName", "over after = " + imgname);

                    /*if (imgname.contains("Risk")) {
                        imgname = "      " + imgname.trim();
                    }*/

                    if (imgname.contains("test sq")) {
                        imgname = imgname.replace("test sq", " test sq");
                    }

                    /*if (btnInteriorMacro.getVisibility() == View.VISIBLE) {
                        imgname = imgname.trim();
                    } else if (btnimgmacro.getText().toString().trim().equals("Macro")) {
                        if (btnrei.getText().toString().equals("E")) {
                            imgname = "__" + imgname;
                        } else if (btnrei.getText().toString().equals("I")) {
                            imgname = imgname.toString().trim();
                            imgname = "_" + imgname;
                        }
                    }*/

                    if (imgname.contains("_")) {
                        String first = "";
                        String second = "";

                        String[] stroverviewname = imgname.split("_");
                        first = stroverviewname[0];
                        Log.e("firstname", "==>" + first);

                        if (first.trim().equals("")) {
                            first = stroverviewname[0];
                            second = stroverviewname[1];

                        } else {
                            first = stroverviewname[0];
                            second = stroverviewname[1];
                        }

                        if (first.contains(" ")) {
                            first = first.replace(" ", "");

                            if (first.trim().equals("Overview")) {
                                if (btnrei.getText().toString().equals("R")) {
                                    imgname = "Roof " + first.toString().trim() + " " + second;
                                    Log.e("imgname", "" + imgname);

                                } else if (btnrei.getText().toString().equals("E")) {
                                    imgname = "Elevations " + first.toString().trim() + " " + second;
                                    Log.e("imgname", "" + imgname);

                                } else if (btnrei.getText().toString().equals("I")) {
                                    imgname = "Interior " + first.toString().trim() + " " + second;
                                    Log.e("imgname", "" + imgname);
                                }
                            }
                        } else if (first.contains("_")) {
                            first = first.replace("_", "");

                            if (first.trim().equals("Overview")) {
                                switch (btnrei.getText().toString()) {
                                    case "R":
                                        imgname = "Roof " + first.trim() + " " + second;
                                        break;
                                    case "E":
                                        imgname = "Elevations " + first.trim() + " " + second;
                                        break;
                                    case "I":
                                        imgname = "Interior " + first.trim() + " " + second;
                                        break;
                                }
                            }
                        } else {

                            if (first.trim().equals("Overview")) {

                                if (btnrei.getText().toString().equals("R")) {
                                    imgname = "Roof " + first.trim() + " " + second;
                                    Log.e("imgname", "" + imgname);

                                } else if (btnrei.getText().toString().equals("E")) {
                                    imgname = "Elevations " + first.trim() + " " + second;
                                    Log.e("imgname", "" + imgname);

                                } else if (btnrei.getText().toString().equals("I")) {
                                    imgname = "Interior " + first.trim() + " " + second;
                                    Log.e("imgname", "" + imgname);
                                }
                            }
                        }
                    }

                    if (btnabc.getText().toString().equals("Fence")) {
                        String strf_fdamage = "";

                        if (!btndamagetype.getTag().toString().trim().equals("0")) {
                            strf_fdamage = btndamagetype.getText().toString();
                        }

                        imgname = "Fence " + strfencegame + " " + strf_fdamage + space + currentTimeStamp + ".jpg";
                    }
                    imgname = feo + imgname;


                    if(!imgname.contains("  Overview"))
                    {
                        if(imgname.contains(" Overview"))
                        {
                            imgname = imgname.replace(" Overview","  Overview");
                        }
                    }
                    else
                    {
                        if(imgname.contains(" Overview"))
                        {
                            imgname = imgname.replace(" Overview","  Overview");
                        }
                    }




                    if (isBottomShow)
                        imgname = "zz_" + imgname;

                    if (!imgMicName.equals("")) {
                        imgname = imgMicName + space + currentTimeStamp + ".jpg";
                        savefile = new File(savefile.getAbsolutePath() + "/VoicePhotos");
                        if (!savefile.exists())
                            savefile.mkdir();
                    }
                    //imgname=

                    if (btnrisk.getText().toString().trim().equals("New Aditional photo")) {
                        imgname = txtalphaname.getText().toString().trim() + ".jpg";

                        savefile = new File(savefile.getAbsolutePath() + "/New Aditional photo");
                        if (!savefile.exists())
                            savefile.mkdir();
                    }

//                    ClaimModel m = new ClaimModel();
//                    if (btncat.getText().toString() != m.getName()) {
//                        imgname = txtalphaname.getText().toString().trim() + ".jpg";
//
//                        savefile = new File(savefile.getAbsolutePath() + "/claimmate ");
//                        if (!savefile.exists())
//                            savefile.mkdir();
//                    }





                    /*if (imgname.contains(currentTimeStamp)) {
                        imgname = imgname.split(currentTimeStamp)[0]+"                              "+currentTimeStamp+".jpg";
                    }*/
                    String savefilepath = savefile.getAbsolutePath() + "/" + imgname;// String.format(mydir.getAbsolutePath()+"/"+imgname+"_"+currentTimeStamp+".jpg", System.currentTimeMillis());
                    Log.e("savefilepath", "==>" +savefilepath);

                    lastimageeditor.putString("lastimgpath", savefilepath);
                    lastimageeditor.putString("lastimgname", imgname);
                    lastimageeditor.commit();

                    outStream = new FileOutputStream(savefilepath);
                    outStream.write(data);
                    outStream.close();
                    Log.d("Log", "onPictureTaken - wrote bytes: " + data.length);


                    File refereshfile = new File(savefilepath);

                    String mCurrentPhotoPath = "file:" + savefile.getAbsolutePath(); // image is the created file image

                    opengallery = false;





                    if (chkdate.isChecked()) {
                        Bitmap workingBitmap = BitmapFactory.decodeFile(refereshfile.getAbsolutePath());

                        Bitmap bitmap = workingBitmap.copy(Bitmap.Config.ARGB_8888, true);
                        FileOutputStream fileOutputStream = new FileOutputStream(refereshfile);

                        Canvas canvas = new Canvas(bitmap);
                        Paint paint = new Paint();
                        paint.setAntiAlias(true);
                        paint.setColor(Color.WHITE);
                        paint.setStyle(Paint.Style.FILL);
                        paint.setTextSize(75);
                        paint.setShadowLayer(2.0f, 1.0f, 1.0f, Color.BLACK);

                        String imgcurrentTimeStamp = imgdateFormat.format(new Date());


                        canvas.drawText(imgcurrentTimeStamp, ((bitmap.getWidth() / 2) - (paint.measureText(imgcurrentTimeStamp) / 2)), bitmap.getHeight() - 150, paint);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 40, fileOutputStream);

                        fileOutputStream.flush();
                        fileOutputStream.close();

                    }

                    if (btnrisk.getText().toString().trim().equals("Pitch")) {

                        Bitmap workingBitmap = BitmapFactory.decodeFile(refereshfile.getAbsolutePath());

                        Bitmap bitmap = workingBitmap.copy(Bitmap.Config.ARGB_8888, true);

                        FileOutputStream fileOutputStream = new FileOutputStream(refereshfile);

                        Canvas canvas = new Canvas(bitmap);

                        bmoverlay = Bitmap.createScaledBitmap(bmoverlay, bitmap.getWidth(), bitmap.getHeight(), false);
                        canvas.drawBitmap(bmoverlay, 0, 0, null);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 40, fileOutputStream);
                        fileOutputStream.flush();
                        fileOutputStream.close();

                    } else if (isBottomShow) {
                        bmoverlay = loadBitmapFromView(findViewById(R.id.rlMain2));
                        Bitmap workingBitmap = BitmapFactory.decodeFile(refereshfile.getAbsolutePath());

                        Bitmap bitmap = workingBitmap.copy(Bitmap.Config.ARGB_8888, true);

                        FileOutputStream fileOutputStream = new FileOutputStream(refereshfile);

                        Canvas canvas = new Canvas(bitmap);

                        bmoverlay = Bitmap.createScaledBitmap(bmoverlay, bitmap.getWidth(), bitmap.getHeight(), false);
                        canvas.drawBitmap(bmoverlay, 0, 0, null);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 40, fileOutputStream);
                        fileOutputStream.flush();
                        fileOutputStream.close();
//                        rlbottomview.setVisibility(View.GONE);
                        rlBottomView2.setVisibility(View.GONE);
                        isBottomShow = false;
                    }
//                    uploadMedia(refereshfile.getAbsolutePath(),imgname);
                    if (btnrisk.getText().toString().trim().equals("New Aditional photo")) {

                        Bitmap workingBitmap = BitmapFactory.decodeFile(refereshfile.getAbsolutePath());

                        Bitmap bitmap = workingBitmap.copy(Bitmap.Config.ARGB_8888, true);

                        FileOutputStream fileOutputStream = new FileOutputStream(refereshfile);

                        Canvas canvas = new Canvas(bitmap);

                        bmoverlay = Bitmap.createScaledBitmap(bmoverlay, bitmap.getWidth(), bitmap.getHeight(), false);
                        canvas.drawBitmap(bmoverlay, 0, 0, null);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 40, fileOutputStream);
                        fileOutputStream.flush();
                        fileOutputStream.close();

                    } else if (isBottomShow) {
                        bmoverlay = loadBitmapFromView(findViewById(R.id.rlMain2));
                        Bitmap workingBitmap = BitmapFactory.decodeFile(refereshfile.getAbsolutePath());

                        Bitmap bitmap = workingBitmap.copy(Bitmap.Config.ARGB_8888, true);

                        FileOutputStream fileOutputStream = new FileOutputStream(refereshfile);

                        Canvas canvas = new Canvas(bitmap);

                        bmoverlay = Bitmap.createScaledBitmap(bmoverlay, bitmap.getWidth(), bitmap.getHeight(), false);
                        canvas.drawBitmap(bmoverlay, 0, 0, null);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 40, fileOutputStream);
                        fileOutputStream.flush();
                        fileOutputStream.close();
//                        rlbottomview.setVisibility(View.GONE);
                        rlBottomView2.setVisibility(View.GONE);
                        isBottomShow = false;
                    }

                    try {
                        ExifInterface exifInterface = new ExifInterface(refereshfile.getAbsolutePath());
                        if (rlriskphoto.getVisibility() == View.VISIBLE) {
                            exifInterface.setAttribute(ExifInterface.TAG_USER_COMMENT, "1");
                        } else if (rllayerphoto.getVisibility() == View.VISIBLE) {
                            exifInterface.setAttribute(ExifInterface.TAG_USER_COMMENT, "2");
                        } else if (btnrei.getText().toString().equalsIgnoreCase("r")) {
                            exifInterface.setAttribute(ExifInterface.TAG_USER_COMMENT, "3");
                        } else if (btnrei.getText().toString().equalsIgnoreCase("e")) {
                            exifInterface.setAttribute(ExifInterface.TAG_USER_COMMENT, "4");
                        } else if (btnrei.getText().toString().equalsIgnoreCase("i")) {
                            exifInterface.setAttribute(ExifInterface.TAG_USER_COMMENT, "5");
                        }
                        exifInterface.saveAttributes();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    new SingleMediaScanner(HomeActivity.this, refereshfile);

                    if (chkApi.isChecked())
                        SavePic(refereshfile.getAbsolutePath());

                    refereshgallery();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                }

//                showphotonamealert(strname);

                if (!btnQue.getTag().toString().equals("0")) {
                    new ClaimSqlLiteDbHelper(HomeActivity.this).removeQue(btnQue.getTag().toString());
                    btnQue.setTag("0");
                    setQueCount();
//                    nextQue();
                }

                if (btnrisk.getText().toString().trim().equals("Aditional Photo")) {
//                    if(!btnrei.getText().toString().equals("E"))
//                    {
                    photoindex++;
                    lastimageeditor.putInt("photoindex", photoindex);
                    lastimageeditor.commit();
//                    }
                }
                if (btnrisk.getText().toString().trim().equals("New Aditional photo")) {
//                    if(!btnrei.getText().toString().equals("E"))
//                    {
                    photoindex++;
                    lastimageeditor.putInt("photoindex", photoindex);
                    lastimageeditor.commit();
//                    }
                }

                Toast.makeText(getApplicationContext(), "save your picture in " + savefile.getAbsolutePath(), Toast.LENGTH_LONG).show();
                refreshCamera();


                Log.e("so", "-->" + btnrisk.getText());

                if (btnimgmacro.getText().toString().trim().equals("Aditional Photo")) {
                    nextPhoto("Layers");
                } else if (btnrisk.getText().toString().trim().equals("Risk")) {
                    if (btnrisk.getTag().equals("2")) {

                        if (btnskip.getVisibility() == View.INVISIBLE) {
                            btnskip.setVisibility(View.VISIBLE);

                        } else if (btnskip.getVisibility() == View.VISIBLE) {
                            btnskip.setVisibility(View.INVISIBLE);
                            btnrisk.setTag("1");
//                            showoption();
//                            btnrisk.setText("Aditional Photo");

                            nextPhoto("Front Elevation Overview");
                        }
                    }
                } else if (btnrisk.getText().toString().trim().equals("Front Elevation Overview")) {
                    btnskip.setVisibility(View.VISIBLE);

                    nextPhoto("Risk2");
                } else if (btnrisk.getText().toString().trim().equals("Risk2")) {

                    btnimgmacrosub.setVisibility(View.GONE);
                    btnimgmacro.setText("Macro");
                    btnimgmacro.setVisibility(View.VISIBLE);
                    nextPhoto("Aditional Photo");
                    getalphaname();

                    if (btnrei.getTag().toString().equals("3")) {
                        onClick(btnrei);
                    }
                    if (btnrei.getTag().toString().equals("1")) {
                        onClick(btnrei);
                    }

                } else if (btnrisk.getText().toString().trim().equals("Roof Overview")) {

                    btnimgmacrosub.setVisibility(View.GONE);
                    btnimgmacro.setText("Macro");
                    btnimgmacro.setVisibility(View.VISIBLE);
                    nextPhoto("Aditional Photo");
                    getalphaname();

                    if (btnrei.getTag().toString().equals("2")) {
                        onClick(btnrei);
                    }
                    if (btnrei.getTag().toString().equals("3")) {
                        onClick(btnrei);
                    }

                    // SetHail();
                } else if (btnrisk.getText().toString().trim().equals("Layers")) {
//                    showlayreoption();
//                    btnrisk.setText("Aditional Photo");

                    if (layerphoto == 0) {
                        nextPhoto("Layers");
                        getalphaname();
                        layerphoto = 1;
                        tvonrakes.setVisibility(View.VISIBLE);

                    } else {
                        btnpitchmenu.setText("Blank");
                        nextPhoto("Pitch");
                        layerphoto = 0;
                        tvonrakes.setVisibility(View.GONE);
                    }
                } else if (btnrisk.getText().toString().trim().equals("Pitch")) {
//                    hideallview();
//                    btnrisk.setText("Aditional Photo");
                    nextPhoto("Shingle");

                } else if (btnrisk.getText().toString().trim().equals("Shingle")) {
//                    hideallview();
//                    btnrisk.setText("Aditional Photo");
                    nextPhoto("Gutter");


                } else if (btnrisk.getText().toString().trim().equals("Gutter")) {
//                    hideallview();
//                    btnrisk.setText("Aditional Photo");

                    nextPhoto("Overhang");

                } else if (btnrisk.getText().toString().trim().equals("Overhang")) {
                    nextPhoto("Type of siding");

                } else if (btnrisk.getText().toString().trim().equals("Type of siding")) {
                    nextPhoto("Roof Overview");

                } else if (btnimgmacro.getText().toString().equals("Hail")) {
                    hailimgcount++;
                    btno.setVisibility(View.VISIBLE);//dmakchange
                    btnocb.setVisibility(View.INVISIBLE);

                    Log.e("no_slopeimg", "" + no_slopeimg);

                    if (hailimgcount == no_slopeimg) {
                        btnhailskip.setText("Next Slope");
                        btno.setVisibility(View.VISIBLE);//dmakchange
                        btnnc.setVisibility(View.VISIBLE);//Dmakchange
                        btnocb.setVisibility(View.INVISIBLE);
                    }


                    if (!btnhailskip.getText().toString().trim().equals("Next Slope")) {


                        if (hailimgcount == no_slopeimg) {
                            btno.setVisibility(View.VISIBLE);//dmakchange
                            btnnc.setVisibility(View.VISIBLE);//Dmakchange
                            btnocb.setVisibility(View.INVISIBLE);

                            if (btnhailtype.getText().equals("Front Slope")) {
                                if (chkrightslopeimg) {
                                    btnhailtype.setText("Right Slope");
                                    hailimgcount = 0;
                                    no_slopeimg = no_rightslopeimg;
                                    btnhailmenu1.setVisibility(View.INVISIBLE);

                                    setnexthailslopvalue();

                                } else if (chkrearslopeimg) {
                                    btnhailtype.setText("Rear Slope");
                                    hailimgcount = 0;
                                    no_slopeimg = no_rearslopeimg;
                                    btnhailmenu1.setVisibility(View.INVISIBLE);

                                    setnexthailslopvalue();

                                } else if (chkleftslopeimg) {
                                    btnhailtype.setText("Left Slope");
                                    hailimgcount = 0;
                                    no_slopeimg = no_leftslopeimg;
                                    btnhailmenu1.setVisibility(View.INVISIBLE);

                                    setnexthailslopvalue();

                                } else {
                                    hailSloptoHome();
                                }
                            } else if (btnhailtype.getText().equals("Right Slope")) {

                                if (chkrearslopeimg) {
                                    btnhailtype.setText("Rear Slope");
                                    btnhailmenu1.setVisibility(View.INVISIBLE);

                                    hailimgcount = 0;
                                    no_slopeimg = no_rearslopeimg;
                                    setnexthailslopvalue();

                                } else if (chkleftslopeimg) {
                                    btnhailtype.setText("Left Slope");
                                    btnhailmenu1.setVisibility(View.INVISIBLE);

                                    hailimgcount = 0;
                                    no_slopeimg = no_leftslopeimg;
                                    setnexthailslopvalue();
                                } else {
                                    hailSloptoHome();
                                }
                            } else if (btnhailtype.getText().equals("Rear Slope")) {

                                if (chkleftslopeimg) {
                                    btnhailtype.setText("Left Slope");
                                    btnhailmenu1.setVisibility(View.INVISIBLE);

                                    hailimgcount = 0;
                                    no_slopeimg = no_leftslopeimg;
                                    setnexthailslopvalue();
                                } else {
                                    hailSloptoHome();
                                }
                            } else if (btnhailtype.getText().equals("Left Slope")) {
                                btnhailmenu1.setVisibility(View.INVISIBLE);

                                hailSloptoHome();
                            }
                        } else if (btnimgmacro.getText().toString().equals("Hail") && btnimgmacrosub.getText().toString().trim().equals("hail damage close up on 2")) {
                            btnhailmenu1.setText("");
                            btnhailmenu1.setVisibility(View.GONE);
                            btnimgmacrosub.setVisibility(View.GONE);
                            btnimgmacro.setText("Macro");
                            btnimgmacro.setVisibility(View.VISIBLE);
                            nextPhoto("Aditional Photo");
                            getalphaname();
                        } else if (btnimgmacro.getText().toString().equals("Hail")) {
                            nextPhoto("hail frontslopenext");
                            getalphaname();
                        }
                    }
                } else if (btnInteriorMacro.getVisibility() == View.VISIBLE) {
                    skipInterior();
                    btno.setVisibility(View.VISIBLE);//dmakchange
                    btnocb.setVisibility(View.INVISIBLE);
                } else if (rlNewMacro.getVisibility() == View.VISIBLE) {
                    skipNewMacro();
                    btno.setVisibility(View.VISIBLE);//dmakchange
                    btnocb.setVisibility(View.INVISIBLE);
                } else if (btnInteriorNewMacro.getVisibility() == View.VISIBLE) {
                    interiorNewMacroNext();
                    btno.setVisibility(View.VISIBLE);//dmakchange
                    btnocb.setVisibility(View.INVISIBLE);
                } else if (btnRoomMacro.getVisibility() == View.VISIBLE) {
                    roomMacroNext();
                    btno.setVisibility(View.VISIBLE);//dmakchange
                    btnocb.setVisibility(View.INVISIBLE);
                }


                btno.setVisibility(View.VISIBLE);
                btnnc.setVisibility(View.VISIBLE);
                btnocb.setVisibility(View.INVISIBLE);
//                btnnodamagetype.setVisibility(View.VISIBLE);



                if(btnrisk.getVisibility() == View.INVISIBLE)
                {
                    btnrisk.setVisibility(View.VISIBLE);
                }

                if(btnimgmacrosub.getVisibility() == View.INVISIBLE)
                {
                    btnimgmacrosub.setVisibility(View.VISIBLE);
                }


                Save_CameraData();
            }
        };
    }

    private void Save_CameraData() {
        if(strrei.equalsIgnoreCase("I"))
        {
            String buildingType = "";

            if(btnabc.getText().toString().equalsIgnoreCase("None"))
            {
                buildingType = "Main Building";
            }
            else
            {
                buildingType = btnabc.getText().toString().trim();
            }

            if(btnCeiling.getTag().equals("2"))
            {
                String areaname = "";
                String subareaname = "";
                String damagename = "";
                String roomtype= "";
                String no = "";

                areaname = "Ceiling";//btnCeiling.getText().toString();
                no =  btnmatrialsubmenu.getText().toString();

                if(!btniteriortype.getText().toString().equalsIgnoreCase("Room"))
                {
                    roomtype = btniteriortype.getText().toString();
                }

                Log.e("btntype2","==>"+btntype2.getText().toString());
                if(!btndamagetype.getText().toString().equalsIgnoreCase("Damage"))
                {
                    damagename = btndamagetype.getText().toString();

                  /*  if(btndamagetype.getTag().toString().equalsIgnoreCase("2"))
                    {
                    }*/
                }


                if(btnSubAreaTogal.getTag().toString().equalsIgnoreCase("2"))
                {
                    subareaname = btnSubAreaTogal.getText().toString();

                }
                else
                {
                    subareaname = "";
                }

                Log.e("SaveCameraDataname",""+areaname+","+subareaname+","+damagename+","+roomtype+","+no);
                addInteriorData(areaname,subareaname,damagename,roomtype,no,buildingType);
            }


            if(btnWall.getTag().equals("2"))
            {
                String areaname = "";
                String subareaname = "";
                String damagename = "";
                String roomtype= "";
                String no = "";

                areaname = "Wall";//btnWall.getText().toString();
                no =  btnmatrialsubmenu.getText().toString();

                if(!btniteriortype.getText().toString().equalsIgnoreCase("Room"))
                {
                    roomtype = btniteriortype.getText().toString();
                }

                Log.e("btntype2","==>"+btntype2.getText().toString());
                if(!btndamagetype.getText().toString().equalsIgnoreCase("Damage"))
                {
                    damagename = btndamagetype.getText().toString();

                  /*  if(btndamagetype.getTag().toString().equalsIgnoreCase("2"))
                    {
                    }*/
                }


                if(btnSubAreaTogal.getTag().toString().equalsIgnoreCase("2"))
                {
                    subareaname = btnSubAreaTogal.getText().toString();

                }
                else
                {
                    subareaname = "";
                }

                Log.e("SaveCameraDataname",""+areaname+","+subareaname+","+damagename+","+roomtype+","+no);
                addInteriorData(areaname,subareaname,damagename,roomtype,no,buildingType);
            }


            if(btnFloor.getTag().equals("2"))
            {
                String areaname = "";
                String subareaname = "";
                String damagename = "";
                String roomtype= "";
                String no = "";

                areaname = "Floor";//btnWall.getText().toString();
                no =  btnmatrialsubmenu.getText().toString();

                if(!btniteriortype.getText().toString().equalsIgnoreCase("Room"))
                {
                    roomtype = btniteriortype.getText().toString();
                }

                Log.e("btntype2","==>"+btntype2.getText().toString());
                if(!btndamagetype.getText().toString().equalsIgnoreCase("Damage"))
                {
                    damagename = btndamagetype.getText().toString();

                  /*  if(btndamagetype.getTag().toString().equalsIgnoreCase("2"))
                    {
                    }*/
                }


                if(btnSubAreaTogal.getTag().toString().equalsIgnoreCase("2"))
                {
                    subareaname = btnSubAreaTogal.getText().toString();

                }
                else
                {
                    subareaname = "";
                }

                Log.e("SaveCameraDataname",""+areaname+","+subareaname+","+damagename+","+roomtype+","+no);
                addInteriorData(areaname,subareaname,damagename,roomtype,no,buildingType);
            }

        }
        else if(strrei.equalsIgnoreCase("R"))
        {


            String buildingType = "";
            String quantity="";
            String damageType="";
            String material = "";
            String slopeType = "";


            if(btnabc.getText().toString().equalsIgnoreCase("None"))
            {
                buildingType = "Main Building";
            }
            else
            {
                buildingType = btnabc.getText().toString().trim();
            }

            quantity = btnQty.getText().toString().trim();

            if(quantity.toString().trim().equalsIgnoreCase("Quantity"))
            {
                quantity = "0";
            }


            if(btntype.getVisibility() == View.VISIBLE)
            {
                if(btntype.getTag().equals("2"))
                {
                    damageType = btntype.getText().toString().trim();
                }
                else
                {
                    damageType = "No Damage";
                }

            }
            else
            {
                damageType = "No Damage";
            }


            material = btntype2.getText().toString().trim();

/*
            if(btntype2.getTag().equals("2"))
            {
                material = btntype2.getText().toString().trim();
            }
            else
            {
                material = "";//btntype2.getText().toString().trim();
            }
*/


            if(selectslope.toString().trim().equalsIgnoreCase(""))
            {
                slopeType = "Slope";
            }
            else
            {
                slopeType  = selectslope.toString().trim();
            }


            Log.e("SaveRoofCameraData",""+slopeType+","+damageType+","+material+","+quantity+","+buildingType);
            addRoofData(slopeType,material,damageType,quantity,buildingType);



        }
        else if(strrei.equalsIgnoreCase("E"))
        {


            String buildingType = "";
            String quantity="";
            String damageType="";
            String material = "";
            String slopeType = "";


            if(btnabc.getText().toString().equalsIgnoreCase("None"))
            {
                buildingType = "Main Building";
            }
            else
            {
                buildingType = btnabc.getText().toString().trim();
            }

            quantity = btnQty.getText().toString().trim();

            if(quantity.toString().trim().equalsIgnoreCase("Quantity"))
            {
                quantity = "0";
            }


            if(btntype.getVisibility() == View.VISIBLE)
            {
                if(btntype.getTag().equals("2"))
                {
                    damageType = btntype.getText().toString().trim();
                }
                else
                {
                    damageType = "No Damage";
                }

            }
            else
            {
                damageType = "No Damage";
            }

            material = btntype2.getText().toString().trim();

/*

            if(btntype2.getTag().equals("2"))
            {
                material = btntype2.getText().toString().trim();
            }
            else
            {
                material = "";//btntype2.getText().toString().trim();
            }

*/

            if(selectslope.toString().trim().equalsIgnoreCase(""))
            {
                slopeType = "Slope";
            }
            else
            {
                slopeType  = selectslope.toString().trim();
            }


            Log.e("SavElevationCameraData",""+slopeType+","+damageType+","+material+","+quantity+","+buildingType);
            addElevationData(slopeType,material,damageType,quantity,buildingType);


        }
    }

    private void addElevationData(String slopeType, String material, String damageType, String quantity, String buildingType) {

        String claimId = PrefManager.getClaimId();
        opendatabase();
        DB.execSQL("insert into tbl_elevation_savedata ('slopetype','material','damagetype','quantity','buildingtype','claim_id')" + "values('" + slopeType + "','" + material + "','" + damageType + "','" + quantity + "','" + buildingType + "','"+claimId+"') ;");
        DB.close();

    }

    private void addRoofData(String slopeType, String material, String damageType, String quantity, String buildingType) {


        String claimId = PrefManager.getClaimId();
        opendatabase();
        DB.execSQL("insert into tbl_roof_savedata ('slopetype','material','damagetype','quantity','buildingtype','claim_id')" + "values('" + slopeType + "','" + material + "','" + damageType + "','" + quantity + "','" + buildingType + "','"+claimId+"') ;");
        DB.close();
//        getInteriorSaveData();
    }

    private void screenshotShowviewAditional() {

        txtalphaname.setVisibility(View.VISIBLE);
        txtalphaname.setText(addcostmphotoname);
        btnareatogal.setVisibility(View.GONE);

        btnSubAreaTogal.setVisibility(View.GONE);

        rlblankback.setVisibility(View.VISIBLE);


        btnrisk.setVisibility(View.VISIBLE);
        btnimgmacro.setVisibility(View.VISIBLE);

        imgbtncam.setVisibility(View.VISIBLE);
        rlriskphoto.setVisibility(View.GONE);//check
        rllayerphoto.setVisibility(View.GONE);//check
        rlshinglephoto.setVisibility(View.GONE);//check
        rlgutterphoto.setVisibility(View.GONE);//check
        rloverhangphoto.setVisibility(View.GONE);//check
        rlpitchphoto.setVisibility(View.GONE);//check
        llline.setVisibility(View.GONE);//check
        rlcostm.setVisibility(View.GONE);//check
        rlfeo.setVisibility(View.GONE);//check
        rlhail.setVisibility(View.GONE);//front slope

        rlmenuselection.setVisibility(View.GONE);//all options is like a test claim,damage,material,quantity
        llArea.setVisibility(View.GONE);//ceiling,wall,floor
        btnMic.setVisibility(View.GONE);//mic
        btnReport.setVisibility(View.GONE);
        btnQue.setVisibility(View.GONE);
        txtQueCount.setVisibility(View.GONE);
        rltorch.setVisibility(View.GONE);
        ibtnflash.setVisibility(View.GONE);
        ibtnLive.setVisibility(View.GONE);
        imgbtnsetting.setVisibility(View.GONE);
        zoomseek.setVisibility(View.GONE);
        rlcontrolview.setVisibility(View.GONE);
        btnocb.setVisibility(View.INVISIBLE);//dmak
        btnlastphoto.setVisibility(View.GONE);


    }

    private void screenshotHideviewAditional() {

        // txtalphaname.setText(addcostmphotoname);
        btnareatogal.setVisibility(View.INVISIBLE);
        txtalphaname.setVisibility(View.INVISIBLE);
        btnSubAreaTogal.setVisibility(View.INVISIBLE);
        rlblankback.setVisibility(View.INVISIBLE);
        rlriskphoto.setVisibility(View.INVISIBLE);//check
        rllayerphoto.setVisibility(View.INVISIBLE);//check
        rlshinglephoto.setVisibility(View.INVISIBLE);//check
        rlgutterphoto.setVisibility(View.INVISIBLE);//check
        rloverhangphoto.setVisibility(View.INVISIBLE);//check
        rlpitchphoto.setVisibility(View.INVISIBLE);//check
        llline.setVisibility(View.INVISIBLE);//check
        rlcostm.setVisibility(View.INVISIBLE);//check
        rlfeo.setVisibility(View.INVISIBLE);//check
        rlhail.setVisibility(View.INVISIBLE);//front slope

        btnrisk.setVisibility(View.INVISIBLE);
        btnimgmacro.setVisibility(View.INVISIBLE);

        rlmenuselection.setVisibility(View.INVISIBLE);//all options is like a test claim,damage,material,quantity
        llArea.setVisibility(View.INVISIBLE);//ceiling,wall,floor
        btnMic.setVisibility(View.INVISIBLE);//mic
        btnReport.setVisibility(View.INVISIBLE);
        btnQue.setVisibility(View.INVISIBLE);
        txtQueCount.setVisibility(View.INVISIBLE);
        rltorch.setVisibility(View.INVISIBLE);
        ibtnflash.setVisibility(View.INVISIBLE);
        ibtnLive.setVisibility(View.INVISIBLE);
        imgbtnsetting.setVisibility(View.INVISIBLE);
        zoomseek.setVisibility(View.INVISIBLE);
        rlcontrolview.setVisibility(View.INVISIBLE);
        btnocb.setVisibility(View.INVISIBLE);
        btnlastphoto.setVisibility(View.INVISIBLE);
        imgbtncam.setVisibility(View.GONE);

    }

    private void SavePic(String Path) {

        if (isInternetOn()) {
            if (btnrei.getText().toString().trim().equals("R")) {
                new Upload_R_Pic().execute(Path);

            } else if (btnrei.getText().toString().trim().equals("E")) {
                new Upload_E_Pic().execute(Path);

            } else if (btnrei.getText().toString().trim().equals("I")) {
                new Upload_I_Pic().execute(Path);
            }
        } else {
            Toast.makeText(getApplicationContext(), "Please Check Internet Connection", Toast.LENGTH_LONG).show();
        }
    }

    private void adddbimg(String imgname) {
        opendatabase();
        DB.execSQL("insert into tbl_hailimage (imgname)" + "values('" + imgname + "') ;");
        DB.close();
    }

    private void hailSloptoHome() {

        onClick(rlhailback);

        /*btnhailmenu1.setText("");
        btnhailmenu1.setVisibility(View.GONE);
        btnimgmacrosub.setVisibility(View.GONE);
        btnimgmacro.setText("Macro");
        btnimgmacro.setVisibility(View.VISIBLE);
        nextPhoto("Aditional Photo");
        getalphaname();*/
    }

    private String getcoustomname(Button btnsubmenu, ArrayList<String> menu) {

        for (int i = 0; i < menu.size(); i++) {
            if (btnsubmenu.getText().toString().trim().equals(menu.get(i).toString().trim())) {
                return macrocostamdetail.get(i).get("value").toString();
            }
        }

        return "";
    }

    private void getalphaname() {

        btnMic.setTag("0");

        String strisNone = "";

        strname = "";

        String imgname = "";
        String imgpitchname = "";


        //change slop
        if (btnrei.getText().toString().trim().equals("R")) {

            String selectslopname = "";


            if(!selectslope.equalsIgnoreCase(""))
            {
                selectslopname = selectslopname+ "Slope ";
            }
            else
            {
                selectslopname = "Roof ";
            }

            if(btnimgmacro.getText().toString().equals("Hail"))
            {
                strboctype = "" + (btnocb.getTag().toString().equalsIgnoreCase("3") ? "" : (btnocb.getTag().toString().equals("1") ? "Overview" : "Close up"));
            }
            else
            {
                strboctype = selectslopname + (btnocb.getTag().toString().equalsIgnoreCase("3") ? "" : (btnocb.getTag().toString().equals("1") ? "Overview" : "Close up"));
            }

        } else if (btnrei.getText().toString().trim().equals("E")) {
            strboctype = "Elevation " + (btnocb.getTag().toString().equalsIgnoreCase("3") ? "" : (btnocb.getTag().toString().equals("1") ? "Overview" : "Close up"));
        }


        if (btnimgmacro.getText().toString().equals("Hail")) {

            frontslopeimgindex = lastpathpf.getInt("frontslopeimgindex", 1);
            rightslopeimgindex = lastpathpf.getInt("rightslopeimgindex", 1);
            rearslopeimgindex = lastpathpf.getInt("rearslopeimgindex", 1);
            leftslopeimgindex = lastpathpf.getInt("leftslopeimgindex", 1);

            String strgethailimagename = gethailimgname();

            Log.e("strgethailimagename1", "" + strgethailimagename);



            if (btnrei.getText().toString().trim().equals("R")) {


                if (strgethailimagename.equals("Front Slope test sq")) {
                    btnocb.setVisibility(View.INVISIBLE);
                }else if(strgethailimagename.equals("Right Slope test sq")) {
                    btnocb.setVisibility(View.INVISIBLE);//dmak
                }
                else if(strgethailimagename.equals("Left Slope test sq")) {
                    btnocb.setVisibility(View.INVISIBLE);//dmak
                }
                else if(strgethailimagename.equals("Rear Slope test sq")) {
                    btnocb.setVisibility(View.INVISIBLE);//dmak
                }
                if (strgethailimagename.equals("Front Slope Overview")) {
                    btnhailmenu1.setText("");

                  //  strhailmaterialdamage = "";





                }else if(strgethailimagename.equals("Right Slope Overview")) {
                    btnhailmenu1.setText("");
                    //strhailmaterialdamage = "";
                }
                else if(strgethailimagename.equals("Left Slope Overview")) {
                    btnhailmenu1.setText("");
                   // strhailmaterialdamage = "";
                }
                else if(strgethailimagename.equals("Rear Slope Overview")) {
                    btnhailmenu1.setText("");
                   // strhailmaterialdamage = "";
                }

                if (strgethailimagename.contains(" Overview")) {

                    btnhailmenu1.setVisibility(View.GONE);
                    // btnhailmenu1.setText("");

                    strgethailimagename = strgethailimagename.replace("Overview", " " + strboctype);
                    //  strgethailimagename="Front Slop "+ strboctype;
                } else if (strgethailimagename.contains("Overview")) {

                    btnhailmenu1.setVisibility(View.GONE);
                    // btnhailmenu1.setText("");

                    strgethailimagename = strgethailimagename.replace("Overview", "  " + strboctype);
                    //strgethailimagename="Front Slop "+ strboctype;
                }

                if (strgethailimagename.contains(" Close up")) {
                    btnhailmenu1.setVisibility(View.GONE);
                    // btnhailmenu1.setText("");

                    strgethailimagename = strgethailimagename.replace(strboctype, " " + strboctype);
                    //strgethailimagename=strboctype;
                } else if (strgethailimagename.contains("Close up")) {

                    btnhailmenu1.setVisibility(View.GONE);
                    // btnhailmenu1.setText("");

                    strgethailimagename = strgethailimagename.replace(strboctype, "  " + strboctype);
                    //strgethailimagename=strboctype;
                }

                if (strgethailimagename.contains(" test sq")) {
                    //  btnocb.setVisibility(View.INVISIBLE);//dmak
                    strgethailimagename = strgethailimagename.replace("test sq", " test sq");
                } else if (strgethailimagename.contains("test sq")) {
                    // btnocb.setVisibility(View.INVISIBLE);//dmak
                    strgethailimagename = strgethailimagename.replace("test sq", "  test sq");
                }


            }


            String afternum = "";
            if (btnimgmacrosub.getText().toString().trim().equalsIgnoreCase("test sq Overview")) {

                afternum = " hits ";

            } else if (btnimgmacrosub.getText().toString().trim().equalsIgnoreCase("test sq")) {


                afternum = "hits per sq";

            }

            if (btnimgmacrosub.getText().toString().trim().equals("hail damage close up on shingles")) {



                String strhaildamage1 = "";

                if (!strhaildamage.trim().equals("Blank")) {
                    strhaildamage1 = strhaildamage.trim();
                }

                if (strboctype.equals("Blank")) {
                    strboctype = "";
                }

                if (btnocb.getTag().equals("1")) {
                    ocb1 = " Overview";
                    btnocb.setTag("1");
                    strboc = "1";
                    strboctype = "Overview";
                }

                else if (btnocb.getTag().equals("2")) {
                    ocb1 = " Close up";
                    btnocb.setTag("2");
                    strboc = "2";
                    strboctype = "Close up";
                }else if (btnocb.getTag().equals("3")) {
                    ocb1 = "";
                    btnocb.setTag("3");
                    strboc = "";
                    strboctype = "";


                }


              /*  if (btno.getTag().equals("1"))
                    ocb1 = " Overview";

                else if (btnocb.getTag().equals("2"))
                    ocb1 = " Close up";*/

                strgethailimagename = btnhailtype.getText().toString() + " " + strhaildamage1 + ocb1 + " on " + strhailmaterialdamage + "";

            } else {


                if(txt_slopeno.getVisibility() == View.VISIBLE)
                {
                    strgethailimagename = strgethailimagename + " " + txt_slopeno.getText().toString().trim() + " " + afternum;

                }
                else
                {
                    strgethailimagename = strgethailimagename + " " + afternum;

                }

            }




            String indeximage = "";
            if (btnhailtype.getText().equals("Front Slope")) {


                imgname = strgethailimagename + indeximage + " " + strisNone;
                frontslopeimgindex++;
                lastimageeditor.putInt("frontslopeimgindex", frontslopeimgindex);
                System.out.println("front slope");
                System.out.println("front slope frontslopeimgindex:-" + frontslopeimgindex);
                lastimageeditor.commit();


            } else if (btnhailtype.getText().equals("Right Slope")) {

                imgname = strgethailimagename + indeximage + " " + strisNone;
                rightslopeimgindex++;
                lastimageeditor.putInt("rightslopeimgindex", rightslopeimgindex);
                System.out.println("front slope");
                System.out.println("front sloperightslopeimgindex :-" + rightslopeimgindex);
                lastimageeditor.commit();

            } else if (btnhailtype.getText().equals("Rear Slope")) {
                imgname = strgethailimagename + indeximage + " " + strisNone;
                rearslopeimgindex++;
                lastimageeditor.putInt("rearslopeimgindex", rearslopeimgindex);
                System.out.println("front slope");
                System.out.println("front slope rearslopeimgindex:-" + rearslopeimgindex);
                lastimageeditor.commit();

            } else if (btnhailtype.getText().equals("Left Slope")) {
                imgname = strgethailimagename + indeximage + " " + strisNone;
                leftslopeimgindex++;
                lastimageeditor.putInt("leftslopeimgindex", leftslopeimgindex);
                System.out.println("front slope");
                System.out.println("front leftslopeimgindex:-" + leftslopeimgindex);
                lastimageeditor.commit();
            }





        } else if (btnInteriorMacro.getVisibility() == View.VISIBLE) {
            if (btnocb.getTag().equals("1")) {
                imgname = "Overview ";
            } else if (btnocb.getTag().equals("2")) {
                imgname = "Close up ";
            }

            if (!btnInteriorMenu.getText().toString().equalsIgnoreCase("blank")) {
                if (btnInteriorMacro.getTag().toString().equals("1"))
                    imgname = itrarr.get(interiorMacroRoomSelect) + " " + imgname.trim();

                else
                    imgname += btnInteriorMenu.getText().toString();
            }
//            if (!btnInteriorMenu.getText().toString().equalsIgnoreCase("New Aditional photo")) {
//                if (btnInteriorMacro.getTag().toString().equals("1"))
//                    imgname = itrarr.get(interiorMacroRoomSelect) + " " + imgname.trim();
//
//                else
//                    imgname += btnInteriorMenu.getText().toString();
//            }
//
//
//            txtalphaname.setText(Html.fromHtml(imgname.trim()));
        } else if (btnInteriorNewMacro.getVisibility() == View.VISIBLE) {
            String ocb = "", dmg = "", material = "";
            if (!strboctype.equals("Blank")) {
                ocb = (btnocb.getTag().toString().equalsIgnoreCase("3") ? "" : (btnocb.getTag().toString().equals("1") ? "Overview" : "Close up")) + " ";
            }
            if (!btnIntNewDamage.getTag().equals("0"))
                if (btnIntNewDamageRed.getTag().toString().equals("1"))
                    dmg = btnIntNewDamage.getText().toString() + " ";
            if (btnIntNewMaterialRed.getTag().toString().equals("1"))
                material = btnIntNewMaterial.getText().toString();
            if (btnInteriorNewMacro.getTag().toString().equals("Overview"))
                imgname = intRoomName + " " + btnInteriorNewMacro.getTag().toString();
            else
                imgname = intRoomName + " " + ocb + dmg + btnInteriorNewMacro.getTag().toString() + " " + material;
        } else if (btnRoomMacro.getVisibility() == View.VISIBLE) {
            String ocb = "", dmg = "", material = "";
            if (!strboctype.equals("Blank")) {
                ocb = (btnocb.getTag().toString().equalsIgnoreCase("3") ? "" : (btnocb.getTag().toString().equals("1") ? "Overview" : "Close up")) + " ";
            }
            if (!btnRoomMacroDamage.getTag().equals("0"))
                if (btnRoomMacroDamageRed.getTag().toString().equals("1"))
                    dmg = btnRoomMacroDamage.getText().toString() + " ";
            if (btnRoomMacroMaterialRed.getTag().toString().equals("1"))
                material = btnRoomMacroMaterial.getText().toString();
            if (btnRoomMacro.getTag().toString().equals("Overview"))
                imgname = intRoomName + " " + btnRoomMacro.getTag().toString();
            else
                imgname = intRoomName + " " + ocb + dmg + btnRoomMacro.getTag().toString() + " " + material;
        } else if (rlNewMacro.getVisibility() == View.VISIBLE) {
            imgname = arrayListNewMacro.get(selectNewMacroIndex);
            if (!btnNewMacroDamage.getTag().toString().equalsIgnoreCase("Blank"))
                imgname += " " + btnNewMacroDamage.getText().toString();
            txtalphaname.setText(Html.fromHtml(imgname.trim()));
        } else if (btnrisk.getText().toString().trim().equals("Aditional Photo")) {

            strreitype = "";

            if (btnrei.getText().toString().trim().equals("I"))
            {
                if (!btniteriortype.getTag().toString().equals("0")) {
                    strreitype = itrarr.get(iteriortypeindex) + " ";
                    if (!btnmatrialsubmenu.getText().toString().equals("0"))
                        strreitype += btnmatrialsubmenu.getText().toString() + " ";
                }
            }
            else if (!selectslope.equals(""))
            {
                if (btnrei.getText().toString().trim().equals("R")) {
                    strreitype = selectslope; //+ "Slope-";

                } else if (btnrei.getText().toString().trim().equals("E")) {
                    strreitype = selectslope;// + "Elev-";
                }
            }


            String strdamagetype = "";

            String strnodamage = "";

            String strmaterial = "";

            String areaname = "";

            if (btnnodamages.getTag().equals("2")) {
                strnodamage = "No Damage ";
            } else {
                if (!btndamagetype.getTag().toString().trim().equals("0")) {
                    if (!btntype.getTag().equals("1")) {
                        if (btndamagetype.getText().toString().trim().equals("Custom text")) {
                            strdamagetype = damagecostmtext + "   ";

                        } else {

                            if (btndamagetype.getText().toString().equals("Damage")) {
                                strdamagetype = "";
                            } else {
                                strdamagetype = btndamagetype.getText().toString().trim() + "   ";
                            }

                        }
                    }
                }
            }


            if (btnrei.getText().toString().trim().equals("I")) {

                if (btnarea.getText().toString().trim().equals("Area")) {
                    areaname = " ";

                } else {
                    areaname = btnarea.getText().toString().trim() + " ";

                }
            } else {
                areaname = "";
            }

          /*  if (!btnmaterial.getTag().toString().trim().equals("0")) {
                if (!btntype2.getTag().equals("1") || btnrei.getText().toString().equalsIgnoreCase("i")) {
                    if (btnmaterial.getText().toString().trim().equals("Custom text")) {
                        strmaterial = matrialcostmtext + "   ";
                    } else {



                        if(btnmaterial.getText().toString().equals("Material"))
                        {
                            strmaterial = "";
                        }else {
                            strmaterial = btnmaterial.getText().toString().trim() + "   ";
                        }

                    }
                }
            }*/

            if(!btntype2.getTag().equals("1")|| btnrei.getText().toString().equalsIgnoreCase("i"))
            {if(btnrei.getText().toString().equalsIgnoreCase("i"))
            {
                if (btnmaterial.getText().toString().trim().equalsIgnoreCase("Custom text") )
                {
                    strmaterial = matrialcostmtext + "   ";
                }
                else
                {
                    if(btnmaterial.getText().toString().equals("Material"))
                    {
                        strmaterial = "";
                    }else {


                        if(btnSubAreaTogal.getVisibility() == View.VISIBLE && btnSubAreaTogal.getTag().toString().equalsIgnoreCase("2"))
                        {
                            strmaterial = ""+btnmaterial.getText().toString().trim() + "   ";
                        }
                        else
                        {
                            strmaterial = "";
                        }
                    }

                }

            }
            else
            {
                if (btnmaterial.getText().toString().trim().equalsIgnoreCase("Custom text") )
                {
                    strmaterial = matrialcostmtext + "   ";
                }
                else
                {
                    if(btnmaterial.getText().toString().equals("Material"))
                    {
                        strmaterial = "";
                    }else {

                        strmaterial = ""+btnmaterial.getText().toString().trim() + "   ";
                    }

                }
            }

                Log.e("strmaterial_if",""+strmaterial);

            }
            else
            {
                Log.e("strmaterial_else",""+strmaterial);

            }


            if (strboctype.equals("Blank"))
            {
                strname = strreitype + " " + strdamagetype + strnodamage + areaname + strmaterial + " ";

            } else {

                strname = strreitype + "" +strboctype + " " + strdamagetype + strnodamage + areaname + strmaterial + " ";

            }


            if (strname.contains("Blank  ")) {
                strname = strname.replace("Blank  ", "");
            }

            if (strname.contains("  Blank")) {
                strname = strname.replace("  Blank", "");
            }

            if (strname.contains(" Blank")) {
                strname = strname.replace(" Blank", "");
            }

            if (strname.contains(" Blank")) {
                strname = strname.replace(" Blank", "");
            }

            if (strname.contains(" Blank ")) {
                strname = strname.replace(" Blank ", "");
            }


            if (btnrei.getText().toString().trim().equals("R")) {
                if (btnmaterial.getText().toString().equals("test sq")) {

                    strname = strname + " " + btnmatrialsubmenu.getText().toString().trim() + " ";
                }
            }

            if (btnrei.getText().toString().equals("I")) {
                imgname = strname + " " + strisNone;
            } else {

                imgname = strname + " " + strisNone;
                if (imgname.contains("Left Slope Overview")) {
                    //strname = "Roof Overview";
                    strname = imgname.replace("Left Slope Overview","Left Slope  Overview");

                    imgname = strname;
                }else if (imgname.contains("Front Slope Overview")) {
                    //strname = "Roof Overview";
                    strname = imgname.replace("Front Slope Overview","Front Slope  Overview");

                    imgname = strname;
                }
                else if (imgname.contains("Right Slope Overview")) {
                    //strname = "Roof Overview";
                    strname = imgname.replace("Right Slope Overview","Right Slope  Overview");

                    imgname = strname;
                }
                else if (imgname.contains("Rear Slope Overview")) {
                    //strname = "Roof Overview";
                    strname = imgname.replace("Rear Slope Overview","Rear Slope  Overview");

                    imgname = strname;
                }
               else if (imgname.contains("Slope Overview")) {
                    //strname = "Roof Overview";
                    strname = imgname.replace("Slope Overview","Roof  Overview");

                    imgname = strname;
                }
                else if (imgname.trim().equalsIgnoreCase("Slope Close up")) {
                    //strname = "Roof Overview";
                    strname = imgname.replace("Slope Close up","Roof  Close up");

                    imgname = strname;
                }

            }
        } else if (btnrisk.getText().toString().trim().equals("Risk")) {
            imgname = "0 " + riskindex + " Risk Photo" + " " + strisNone;//dateFormat.format(new Date());
        } else if (btnrisk.getText().toString().trim().equals("Layers")) {

            if (layerphoto == 0) {
                imgname = btnLayersmenu1.getText().toString() + " Layers of Roofing with " + btnLayersmenu.getText().toString() + " " + strisNone;//dateFormat.format(new Date());
            } else {
                imgname = btnLayersmenu1.getText().toString() + " Layers of Roofing with " + btnLayersmenu.getText().toString() + " on rakes " + strisNone;//dateFormat.format(new Date());
            }
        } else if (btnrisk.getText().toString().trim().equals("Pitch")) {

            String pitchvalue = lblpitchvalue.getText().toString().trim();
            pitchvalue = pitchvalue.replace("/12", "");

            String slope = "";
            if (!btnpitchmenu.getText().toString().equalsIgnoreCase("Blank"))
                slope = btnpitchmenu.getText().toString() + " ";

            imgname = pitchvalue + " Roof Pitch" + " " + strisNone;//dateFormat.format(new Date());

            imgpitchname = lblpitchvalue.getText().toString().trim() + " Roof Pitch" + " " + slope + strisNone;//dateFormat.format(new Date());

        } else if (btnrisk.getText().toString().trim().equals("New Aditional photo")) {
            // btnpitchmenu.setVisibility(View.VISIBLE);
            txtalphaname.setVisibility(View.VISIBLE);
            // txtalphaname.setText(addcostmphotoname);
            //txtalphaname.setText(addcostmphotoname);
            String pitchvalue = txtalphaname.getText().toString().trim();
            System.out.println("txtalphaname:-" + pitchvalue);
            if (btnrisk.getText().toString().trim().equals("New Aditional photo")) {
                imgname = ".jpg";

            } else {
                imgname = txtalphaname.getText().toString().trim() + ".jpg";
            }

            imgname = imgname.trim();
            Log.i(TAG, "imgname = " + imgname);

        } else if (btnrisk.getText().toString().trim().equals("Shingle")) {
            imgname = btnshinglemenu1.getText().toString() + " " + btnshinglemenu2.getText().toString() + " Shingle" + " " + strisNone;//dateFormat.format(new Date());

        } else if (btnrisk.getText().toString().trim().equals("Gutter")) {

            imgname = "Standard " + btnguttermenu1.getText().toString() + " on this dwelling" + " " + strisNone;//dateFormat.format(new Date());

        } else if (btnrisk.getText().toString().trim().equals("Overhang")) {
            imgname = btnoverhangmenu1.getText().toString() + " inch overhang" + " " + strisNone;//dateFormat.format(new Date());
        } else if (btnrisk.getText().toString().trim().equals("Type of siding")) {
            imgname = "Elevation " + btnTypesOfSidingMenu2.getText().toString() + (btnTypesOfSidingMenu1.getText().toString().equals("Blank") ? "" : " " + btnTypesOfSidingMenu1.getText().toString() + " Siding");
        } else if (btnrisk.getText().toString().trim().equals("Front Elevation Overview")) {
            imgname = "Front Elevation Overview";//dateFormat.format(new Date());

        } else if (btnrisk.getText().toString().trim().equals("Front Elevation Overview")) {
            imgname = "Front Elevation Overview";//dateFormat.format(new Date());

        } else if (btnimgmacro.getText().toString().equals("Hail")) {
            imgname = btnimgmacrosub.getText().toString();//dateFormat.format(new Date());

        } else if (btnrisk.getText().toString().trim().equals("Risk2")) {
            imgname = "   0 " + riskindex + " Risk Photo" + " " + strisNone;//dateFormat.format(new Date());
        } else if (btnrisk.getText().toString().trim().equals("Roof Overview")) {
            imgname = "Roof Overview";
        } else {
            if (btnsubmenu.getText().toString().trim().equals("")) {
                getcostammenu(false);
            }
            imgname = btnsubmenu.getText().toString().trim();//dateFormat.format(new Date());
        }


        strname = imgname.replace("Left Slope Overview","Left Slope  Overview");


        if (btnrisk.getText().toString().trim().equals("New Aditional photo")) {


            txtalphaname.setText(Html.fromHtml(imgpitchname.trim()));
        } else {




            txtalphaname.setText(Html.fromHtml(imgname.trim()));
        }


        if (btnabc.getText().toString().equals("Fence")) {
            String strf_fdamage = "";

            if (!btndamagetype.getTag().toString().trim().equals("0")) {
                if (!btntype.getTag().equals("1")) {
                    if (btndamagetype.getText().toString().trim().equals("Custom text")) {
                        strf_fdamage = damagecostmtext;

                    } else {
                        strf_fdamage = btndamagetype.getText().toString().trim();
                    }
                }
            }

            String strfencename = "<font color='#FFFF33'>" + "Fence" + "</font>" + " " + strfencegame + " " + strf_fdamage;
            txtalphaname.setText(Html.fromHtml(strfencename));
        }

        String newimagename = txtalphaname.getText().toString();

        if(!newimagename.contains("  Overview"))
        {
            if(newimagename.contains(" Overview"))
            {
                newimagename = newimagename.replace(" Overview","  Overview");
            }
        }
        else
        {
            if(newimagename.contains(" Overview"))
            {
                newimagename = newimagename.replace(" Overview","  Overview");
            }
        }


        txtalphaname.setText(Html.fromHtml(newimagename));


    }


    private void GetPhotoName() {

        btnMic.setTag("0");

        String strisNone = "";

        strname = "";

        String imgname = "";
        String imgpitchname = "";


        //change slop
        if (btnrei.getText().toString().trim().equals("R")) {


            String selectslopname = "";
            if(!selectslope.equalsIgnoreCase(""))
            {
                selectslopname =  "Slope ";
            }

            if(btnimgmacro.getText().toString().equals("Hail"))
            {
                strboctype = "" + (btnocb.getTag().toString().equalsIgnoreCase("3") ? "" : (btnocb.getTag().toString().equals("1") ? "Overview" : "Close up"));
            }
            else
            {
                strboctype = selectslopname + (btnocb.getTag().toString().equalsIgnoreCase("3") ? "" : (btnocb.getTag().toString().equals("1") ? "Overview" : "Close up"));
            }

        } else if (btnrei.getText().toString().trim().equals("E")) {
            strboctype = "Elevation " + (btnocb.getTag().toString().equalsIgnoreCase("3") ? "" : (btnocb.getTag().toString().equals("1") ? "Overview" : "Close up"));
        }


        if (btnimgmacro.getText().toString().equals("Hail")) {

            frontslopeimgindex = lastpathpf.getInt("frontslopeimgindex", 1);
            rightslopeimgindex = lastpathpf.getInt("rightslopeimgindex", 1);
            rearslopeimgindex = lastpathpf.getInt("rearslopeimgindex", 1);
            leftslopeimgindex = lastpathpf.getInt("leftslopeimgindex", 1);

            String strgethailimagename = gethailimgname();

            Log.e("strgethailimagename1", "" + strgethailimagename);



            if (btnrei.getText().toString().trim().equals("R")) {


                if (strgethailimagename.equals("Front Slope test sq")) {
                    btnocb.setVisibility(View.INVISIBLE);
                }else if(strgethailimagename.equals("Right Slope test sq")) {
                    btnocb.setVisibility(View.INVISIBLE);//dmak
                }
                else if(strgethailimagename.equals("Left Slope test sq")) {
                    btnocb.setVisibility(View.INVISIBLE);//dmak
                }
                else if(strgethailimagename.equals("Rear Slope test sq")) {
                    btnocb.setVisibility(View.INVISIBLE);//dmak
                }
                if (strgethailimagename.equals("Front Slope Overview")) {
                    btnhailmenu1.setText("");

                    //  strhailmaterialdamage = "";





                }else if(strgethailimagename.equals("Right Slope Overview")) {
                    btnhailmenu1.setText("");
                    //strhailmaterialdamage = "";
                }
                else if(strgethailimagename.equals("Left Slope Overview")) {
                    btnhailmenu1.setText("");
                    // strhailmaterialdamage = "";
                }
                else if(strgethailimagename.equals("Rear Slope Overview")) {
                    btnhailmenu1.setText("");
                    // strhailmaterialdamage = "";
                }

                if (strgethailimagename.contains(" Overview")) {

                    btnhailmenu1.setVisibility(View.GONE);
                    // btnhailmenu1.setText("");

                    strgethailimagename = strgethailimagename.replace("Overview", " " + strboctype);
                    //  strgethailimagename="Front Slop "+ strboctype;
                } else if (strgethailimagename.contains("Overview")) {

                    btnhailmenu1.setVisibility(View.GONE);
                    // btnhailmenu1.setText("");

                    strgethailimagename = strgethailimagename.replace("Overview", "  " + strboctype);
                    //strgethailimagename="Front Slop "+ strboctype;
                }

                if (strgethailimagename.contains(" Close up")) {
                    btnhailmenu1.setVisibility(View.GONE);
                    // btnhailmenu1.setText("");

                    strgethailimagename = strgethailimagename.replace(strboctype, " " + strboctype);
                    //strgethailimagename=strboctype;
                } else if (strgethailimagename.contains("Close up")) {

                    btnhailmenu1.setVisibility(View.GONE);
                    // btnhailmenu1.setText("");

                    strgethailimagename = strgethailimagename.replace(strboctype, "  " + strboctype);
                    //strgethailimagename=strboctype;
                }

                if (strgethailimagename.contains(" test sq")) {
                    //  btnocb.setVisibility(View.INVISIBLE);//dmak
                    strgethailimagename = strgethailimagename.replace("test sq", " test sq");
                } else if (strgethailimagename.contains("test sq")) {
                    // btnocb.setVisibility(View.INVISIBLE);//dmak
                    strgethailimagename = strgethailimagename.replace("test sq", "  test sq");
                }


            }


            String afternum = "";
            if (btnimgmacrosub.getText().toString().trim().equalsIgnoreCase("test sq Overview")) {

                afternum = " hits ";

            } else if (btnimgmacrosub.getText().toString().trim().equalsIgnoreCase("test sq")) {


                afternum = "hits per sq";

            }

            if (btnimgmacrosub.getText().toString().trim().equals("hail damage close up on shingles")) {



                String strhaildamage1 = "";

                if (!strhaildamage.trim().equals("Blank")) {
                    strhaildamage1 = strhaildamage.trim();
                }

                if (strboctype.equals("Blank")) {
                    strboctype = "";
                }

                if (btnocb.getTag().equals("1")) {
                    ocb1 = " Overview";
                    btnocb.setTag("1");
                    strboc = "1";
                    strboctype = "Overview";
                }

                else if (btnocb.getTag().equals("2")) {
                    ocb1 = " Close up";
                    btnocb.setTag("2");
                    strboc = "2";
                    strboctype = "Close up";
                }else if (btnocb.getTag().equals("3")) {
                    ocb1 = "";
                    btnocb.setTag("3");
                    strboc = "";
                    strboctype = "";


                }


              /*  if (btno.getTag().equals("1"))
                    ocb1 = " Overview";

                else if (btnocb.getTag().equals("2"))
                    ocb1 = " Close up";*/

                strgethailimagename = btnhailtype.getText().toString() + " " + strhaildamage1 + ocb1 + " on " + strhailmaterialdamage + "";

            } else {


                if(txt_slopeno.getVisibility() == View.VISIBLE)
                {
                    strgethailimagename = strgethailimagename + " " + txt_slopeno.getText().toString().trim() + " " + afternum;

                }
                else
                {
                    strgethailimagename = strgethailimagename + " " + afternum;

                }

            }




            String indeximage = "";
            if (btnhailtype.getText().equals("Front Slope")) {


                imgname = strgethailimagename + indeximage + " " + strisNone;
                frontslopeimgindex++;
                lastimageeditor.putInt("frontslopeimgindex", frontslopeimgindex);
                System.out.println("front slope");
                System.out.println("front slope frontslopeimgindex:-" + frontslopeimgindex);
                lastimageeditor.commit();


            } else if (btnhailtype.getText().equals("Right Slope")) {

                imgname = strgethailimagename + indeximage + " " + strisNone;
                rightslopeimgindex++;
                lastimageeditor.putInt("rightslopeimgindex", rightslopeimgindex);
                System.out.println("front slope");
                System.out.println("front sloperightslopeimgindex :-" + rightslopeimgindex);
                lastimageeditor.commit();

            } else if (btnhailtype.getText().equals("Rear Slope")) {
                imgname = strgethailimagename + indeximage + " " + strisNone;
                rearslopeimgindex++;
                lastimageeditor.putInt("rearslopeimgindex", rearslopeimgindex);
                System.out.println("front slope");
                System.out.println("front slope rearslopeimgindex:-" + rearslopeimgindex);
                lastimageeditor.commit();

            } else if (btnhailtype.getText().equals("Left Slope")) {
                imgname = strgethailimagename + indeximage + " " + strisNone;
                leftslopeimgindex++;
                lastimageeditor.putInt("leftslopeimgindex", leftslopeimgindex);
                System.out.println("front slope");
                System.out.println("front leftslopeimgindex:-" + leftslopeimgindex);
                lastimageeditor.commit();
            }





        } else if (btnInteriorMacro.getVisibility() == View.VISIBLE) {
            if (btnocb.getTag().equals("1")) {
                imgname = "Overview ";
            } else if (btnocb.getTag().equals("2")) {
                imgname = "Close up ";
            }

            if (!btnInteriorMenu.getText().toString().equalsIgnoreCase("blank")) {
                if (btnInteriorMacro.getTag().toString().equals("1"))
                    imgname = itrarr.get(interiorMacroRoomSelect) + " " + imgname.trim();

                else
                    imgname += btnInteriorMenu.getText().toString();
            }
//            if (!btnInteriorMenu.getText().toString().equalsIgnoreCase("New Aditional photo")) {
//                if (btnInteriorMacro.getTag().toString().equals("1"))
//                    imgname = itrarr.get(interiorMacroRoomSelect) + " " + imgname.trim();
//
//                else
//                    imgname += btnInteriorMenu.getText().toString();
//            }
//
//
//            txtalphaname.setText(Html.fromHtml(imgname.trim()));
        } else if (btnInteriorNewMacro.getVisibility() == View.VISIBLE) {
            String ocb = "", dmg = "", material = "";
            if (!strboctype.equals("Blank")) {
                ocb = (btnocb.getTag().toString().equalsIgnoreCase("3") ? "" : (btnocb.getTag().toString().equals("1") ? "Overview" : "Close up")) + " ";
            }
            if (!btnIntNewDamage.getTag().equals("0"))
                if (btnIntNewDamageRed.getTag().toString().equals("1"))
                    dmg = btnIntNewDamage.getText().toString() + " ";
            if (btnIntNewMaterialRed.getTag().toString().equals("1"))
                material = btnIntNewMaterial.getText().toString();
            if (btnInteriorNewMacro.getTag().toString().equals("Overview"))
                imgname = intRoomName + " " + btnInteriorNewMacro.getTag().toString();
            else
                imgname = intRoomName + " " + ocb + dmg + btnInteriorNewMacro.getTag().toString() + " " + material;
        } else if (btnRoomMacro.getVisibility() == View.VISIBLE) {
            String ocb = "", dmg = "", material = "";
            if (!strboctype.equals("Blank")) {
                ocb = (btnocb.getTag().toString().equalsIgnoreCase("3") ? "" : (btnocb.getTag().toString().equals("1") ? "Overview" : "Close up")) + " ";
            }
            if (!btnRoomMacroDamage.getTag().equals("0"))
                if (btnRoomMacroDamageRed.getTag().toString().equals("1"))
                    dmg = btnRoomMacroDamage.getText().toString() + " ";
            if (btnRoomMacroMaterialRed.getTag().toString().equals("1"))
                material = btnRoomMacroMaterial.getText().toString();
            if (btnRoomMacro.getTag().toString().equals("Overview"))
                imgname = intRoomName + " " + btnRoomMacro.getTag().toString();
            else
                imgname = intRoomName + " " + ocb + dmg + btnRoomMacro.getTag().toString() + " " + material;
        } else if (rlNewMacro.getVisibility() == View.VISIBLE) {
            imgname = arrayListNewMacro.get(selectNewMacroIndex);
            if (!btnNewMacroDamage.getTag().toString().equalsIgnoreCase("Blank"))
                imgname += " " + btnNewMacroDamage.getText().toString();
            txtalphaname.setText(Html.fromHtml(imgname.trim()));
        } else if (btnrisk.getText().toString().trim().equals("Aditional Photo")) {

            strreitype = "";

            if (btnrei.getText().toString().trim().equals("I"))
            {
                if (!btniteriortype.getTag().toString().equals("0")) {
                    strreitype = itrarr.get(iteriortypeindex) + " ";
                    if (!btnmatrialsubmenu.getText().toString().equals("0"))
                        strreitype += btnmatrialsubmenu.getText().toString() + " ";
                }
            }
            else if (!selectslope.equals(""))
            {
                if (btnrei.getText().toString().trim().equals("R")) {
                    strreitype = selectslope; //+ "Slope-";

                } else if (btnrei.getText().toString().trim().equals("E")) {
                    strreitype = selectslope;// + "Elev-";
                }
            }


            String strdamagetype = "";

            String strnodamage = "";

            String strmaterial = "";

            String areaname = "";

            if (btnnodamages.getTag().equals("2")) {
                strnodamage = "No Damage ";
            } else {
                if (!btndamagetype.getTag().toString().trim().equals("0")) {
                    if (!btntype.getTag().equals("1")) {
                        if (btndamagetype.getText().toString().trim().equals("Custom text")) {
                            strdamagetype = damagecostmtext + "   ";

                        } else {

                            if (btndamagetype.getText().toString().equals("Damage")) {
                                strdamagetype = "";
                            } else {
                                strdamagetype = btndamagetype.getText().toString().trim() + "   ";
                            }

                        }
                    }
                }
            }


            if (btnrei.getText().toString().trim().equals("I")) {

                if (btnarea.getText().toString().trim().equals("Area")) {
                    areaname = " ";

                } else {
                    areaname = btnarea.getText().toString().trim() + " ";

                }
            } else {
                areaname = "";
            }

          /*  if (!btnmaterial.getTag().toString().trim().equals("0")) {
                if (!btntype2.getTag().equals("1") || btnrei.getText().toString().equalsIgnoreCase("i")) {
                    if (btnmaterial.getText().toString().trim().equals("Custom text")) {
                        strmaterial = matrialcostmtext + "   ";
                    } else {



                        if(btnmaterial.getText().toString().equals("Material"))
                        {
                            strmaterial = "";
                        }else {
                            strmaterial = btnmaterial.getText().toString().trim() + "   ";
                        }

                    }
                }
            }*/

            if(!btntype2.getTag().equals("1")|| btnrei.getText().toString().equalsIgnoreCase("i"))
            {if(btnrei.getText().toString().equalsIgnoreCase("i"))
            {
                if (btnmaterial.getText().toString().trim().equalsIgnoreCase("Custom text") )
                {
                    strmaterial = matrialcostmtext + "   ";
                }
                else
                {
                    if(btnmaterial.getText().toString().equals("Material"))
                    {
                        strmaterial = "";
                    }else {


                        if(btnSubAreaTogal.getVisibility() == View.VISIBLE && btnSubAreaTogal.getTag().toString().equalsIgnoreCase("2"))
                        {
                            strmaterial = ""+btnmaterial.getText().toString().trim() + "   ";
                        }
                        else
                        {
                            strmaterial = "";
                        }
                    }

                }

            }
            else
            {
                if (btnmaterial.getText().toString().trim().equalsIgnoreCase("Custom text") )
                {
                    strmaterial = matrialcostmtext + "   ";
                }
                else
                {
                    if(btnmaterial.getText().toString().equals("Material"))
                    {
                        strmaterial = "";
                    }else {

                        strmaterial = ""+btnmaterial.getText().toString().trim() + "   ";
                    }

                }
            }

                Log.e("strmaterial_if",""+strmaterial);

            }
            else
            {
                Log.e("strmaterial_else",""+strmaterial);

            }


            if (strboctype.equals("Blank")) {

                strname = strreitype + " " + strdamagetype + strnodamage + areaname + strmaterial + " ";

            } else {

                strname = strreitype + "" +strboctype + " " + strdamagetype + strnodamage + areaname + strmaterial + " ";

            }


            if (strname.contains("Blank  ")) {
                strname = strname.replace("Blank  ", "");
            }

            if (strname.contains("  Blank")) {
                strname = strname.replace("  Blank", "");
            }

            if (strname.contains(" Blank")) {
                strname = strname.replace(" Blank", "");
            }

            if (strname.contains(" Blank")) {
                strname = strname.replace(" Blank", "");
            }

            if (strname.contains(" Blank ")) {
                strname = strname.replace(" Blank ", "");
            }


            if (btnrei.getText().toString().trim().equals("R")) {
                if (btnmaterial.getText().toString().equals("test sq")) {

                    strname = strname + " " + btnmatrialsubmenu.getText().toString().trim() + " ";
                }
            }

            if (btnrei.getText().toString().equals("I")) {
                imgname = strname + " " + strisNone;
            } else {

                imgname = strname + " " + strisNone;
                if (imgname.contains("Left Slope Overview")) {
                    //strname = "Roof Overview";
                    strname = imgname.replace("Left Slope Overview","Left Slope  Overview");

                    imgname = strname;
                }else if (imgname.contains("Front Slope Overview")) {
                    //strname = "Roof Overview";
                    strname = imgname.replace("Front Slope Overview","Front Slope  Overview");

                    imgname = strname;
                }
                else if (imgname.contains("Right Slope Overview")) {
                    //strname = "Roof Overview";
                    strname = imgname.replace("Right Slope Overview","Right Slope  Overview");

                    imgname = strname;
                }
                else if (imgname.contains("Rear Slope Overview")) {
                    //strname = "Roof Overview";
                    strname = imgname.replace("Rear Slope Overview","Rear Slope  Overview");

                    imgname = strname;
                }
                else if (imgname.contains("Slope Overview")) {
                    //strname = "Roof Overview";
                    strname = imgname.replace("Slope Overview","Roof  Overview");

                    imgname = strname;
                }
                else if (imgname.trim().equalsIgnoreCase("Slope Close up")) {
                    //strname = "Roof Overview";
                    strname = imgname.replace("Slope Close up","Roof  Close up");

                    imgname = strname;
                }
            }
        } else if (btnrisk.getText().toString().trim().equals("Risk")) {
            imgname = "0 " + riskindex + " Risk Photo" + " " + strisNone;//dateFormat.format(new Date());
        } else if (btnrisk.getText().toString().trim().equals("Layers")) {

            if (layerphoto == 0) {
                imgname = btnLayersmenu1.getText().toString() + " Layers of Roofing with " + btnLayersmenu.getText().toString() + " " + strisNone;//dateFormat.format(new Date());
            } else {
                imgname = btnLayersmenu1.getText().toString() + " Layers of Roofing with " + btnLayersmenu.getText().toString() + " on rakes " + strisNone;//dateFormat.format(new Date());
            }
        } else if (btnrisk.getText().toString().trim().equals("Pitch")) {

            String pitchvalue = lblpitchvalue.getText().toString().trim();
            pitchvalue = pitchvalue.replace("/12", "");

            String slope = "";
            if (!btnpitchmenu.getText().toString().equalsIgnoreCase("Blank"))
                slope = btnpitchmenu.getText().toString() + " ";

            imgname = pitchvalue + " Roof Pitch" + " " + strisNone;//dateFormat.format(new Date());

            imgpitchname = lblpitchvalue.getText().toString().trim() + " Roof Pitch" + " " + slope + strisNone;//dateFormat.format(new Date());

        } else if (btnrisk.getText().toString().trim().equals("New Aditional photo")) {
            // btnpitchmenu.setVisibility(View.VISIBLE);
            txtalphaname.setVisibility(View.VISIBLE);
            // txtalphaname.setText(addcostmphotoname);
            //txtalphaname.setText(addcostmphotoname);
            String pitchvalue = txtalphaname.getText().toString().trim();
            System.out.println("txtalphaname:-" + pitchvalue);
            if (btnrisk.getText().toString().trim().equals("New Aditional photo")) {
                imgname = ".jpg";

            } else {
                imgname = txtalphaname.getText().toString().trim() + ".jpg";
            }

            imgname = imgname.trim();
            Log.i(TAG, "imgname = " + imgname);

        } else if (btnrisk.getText().toString().trim().equals("Shingle")) {
            imgname = btnshinglemenu1.getText().toString() + " " + btnshinglemenu2.getText().toString() + " Shingle" + " " + strisNone;//dateFormat.format(new Date());

        } else if (btnrisk.getText().toString().trim().equals("Gutter")) {

            imgname = "Standard " + btnguttermenu1.getText().toString() + " on this dwelling" + " " + strisNone;//dateFormat.format(new Date());

        } else if (btnrisk.getText().toString().trim().equals("Overhang")) {
            imgname = btnoverhangmenu1.getText().toString() + " inch overhang" + " " + strisNone;//dateFormat.format(new Date());
        } else if (btnrisk.getText().toString().trim().equals("Type of siding")) {
            imgname = "Elevation " + btnTypesOfSidingMenu2.getText().toString() + (btnTypesOfSidingMenu1.getText().toString().equals("Blank") ? "" : " " + btnTypesOfSidingMenu1.getText().toString() + " Siding");
        } else if (btnrisk.getText().toString().trim().equals("Front Elevation Overview")) {
            imgname = "Front Elevation Overview";//dateFormat.format(new Date());

        } else if (btnrisk.getText().toString().trim().equals("Front Elevation Overview")) {
            imgname = "Front Elevation Overview";//dateFormat.format(new Date());

        } else if (btnimgmacro.getText().toString().equals("Hail")) {
            imgname = btnimgmacrosub.getText().toString();//dateFormat.format(new Date());

        } else if (btnrisk.getText().toString().trim().equals("Risk2")) {
            imgname = "   0 " + riskindex + " Risk Photo" + " " + strisNone;//dateFormat.format(new Date());
        } else if (btnrisk.getText().toString().trim().equals("Roof Overview")) {
            imgname = "Roof Overview";
        } else {
            if (btnsubmenu.getText().toString().trim().equals("")) {
                getcostammenu(false);
            }
            imgname = btnsubmenu.getText().toString().trim();//dateFormat.format(new Date());
        }


        strname = imgname.replace("Left Slope Overview","Left Slope  Overview");


        if (btnrisk.getText().toString().trim().equals("New Aditional photo")) {


            txtalphaname.setText(Html.fromHtml(imgpitchname.trim()));
        } else {




            txtalphaname.setText(Html.fromHtml(imgname.trim()));
        }


        if (btnabc.getText().toString().equals("Fence")) {
            String strf_fdamage = "";

            if (!btndamagetype.getTag().toString().trim().equals("0")) {
                if (!btntype.getTag().equals("1")) {
                    if (btndamagetype.getText().toString().trim().equals("Custom text")) {
                        strf_fdamage = damagecostmtext;

                    } else {
                        strf_fdamage = btndamagetype.getText().toString().trim();
                    }
                }
            }

            String strfencename = "<font color='#FFFF33'>" + "Fence" + "</font>" + " " + strfencegame + " " + strf_fdamage;
            txtalphaname.setText(Html.fromHtml(strfencename));
        }

        String newimagename = txtalphaname.getText().toString();

        if(!newimagename.contains("  Overview"))
        {
            if(newimagename.contains(" Overview"))
            {
                newimagename = newimagename.replace(" Overview","  Overview");
            }
        }
        else
        {
            if(newimagename.contains(" Overview"))
            {
                newimagename = newimagename.replace(" Overview","  Overview");
            }
        }


        txtalphaname.setText(Html.fromHtml(newimagename));


    }


    private void screenshotHideview() {

        btnQue.setVisibility(View.INVISIBLE);
        txtQueCount.setVisibility(View.INVISIBLE);

        rlpitchBack.setVisibility(View.INVISIBLE);
        ibtnflash.setVisibility(View.INVISIBLE);
        ibtnLive.setVisibility(View.INVISIBLE);
        rltorch.setVisibility(View.INVISIBLE);
        imgbtncam.setVisibility(View.INVISIBLE);
        zoomseek.setVisibility(View.INVISIBLE);
        btntype.setVisibility(View.INVISIBLE);
        btntype2.setVisibility(View.INVISIBLE);
        btnrisk.setVisibility(View.INVISIBLE);
        btnpitchskip.setVisibility(View.INVISIBLE);
        btnpitchmenu.setVisibility(View.INVISIBLE);
    }

    private void screenshotShowview() {

        btnQue.setVisibility(View.VISIBLE);
        setQueCount();

        rlpitchBack.setVisibility(View.VISIBLE);

        ibtnflash.setVisibility(View.VISIBLE);
        ibtnLive.setVisibility(View.VISIBLE);
        rltorch.setVisibility(View.VISIBLE);
        imgbtncam.setVisibility(View.VISIBLE);
        zoomseek.setVisibility(View.VISIBLE);

        if (btndamagetype.getTag().toString().equals("0")) {
            btntype.setVisibility(View.INVISIBLE);

        } else {
            btntype.setVisibility(View.VISIBLE);
        }

        if (btndamagetype.getText().toString().equals("Damage")) {
            btndamagetype.setText("Damage");
            btntype.setVisibility(View.INVISIBLE);
        } else {
            btntype.setVisibility(View.VISIBLE);
        }

        if (btnmaterial.getTag().toString().equals("0")) {
            btntype2.setVisibility(View.INVISIBLE);

        } else {
            btntype2.setVisibility(View.VISIBLE);
        }

        btnpitchmenu.setVisibility(View.VISIBLE);
        btnrisk.setVisibility(View.VISIBLE);
        btnpitchskip.setVisibility(View.VISIBLE);
    }

    private void nextPhoto(String strphoto) {
        btnnc.setVisibility(View.VISIBLE);//Dmakchange
        btnsubmenu.setVisibility(View.GONE);
        btnsubmenu.setText("");

        btnrisk.setText(strphoto);
        btno.setVisibility(View.VISIBLE);//dmakchange

        hideallview();
        if (strphoto.equalsIgnoreCase("Aditional Photo")) {
            btnrisk.setTag("1");
            showoption();
        } else if (strphoto.equalsIgnoreCase("Risk")) {
            llRiskDes.setVisibility(View.VISIBLE);
            btnimgmacrosub.setText("Risk");

            btnrisk.setTag("2");
            btnskip.setVisibility(View.VISIBLE);
            hideoption();
        } else if (strphoto.equalsIgnoreCase("Layers")) {

            btnrisk.setTag("1");
            btnimgmacrosub.setText("Layers");

            btnLayersmenu.setText("Drip Edge");
            btnLayersmenu1.setText("1");

            //btnInteriorMenu.setText("Blank");

            hidelayeroption();
            System.out.println("hidelayeroption Blank 2");
            //btnInteriorMenu.setText("Blank");


        } else if (strphoto.equalsIgnoreCase("Front Elevation Overview")) {
            btnimgmacrosub.setText("Front Elevation Overview");
            btnrisk.setText("Front Elevation Overview");
            hidefeooption();

        } else if (strphoto.equalsIgnoreCase("Roof Overview")) {
            btnimgmacrosub.setText("Roof Overview");
            btnrisk.setText("Roof Overview");
            hidefeooption();

        } else if (strphoto.equalsIgnoreCase("Risk2")) {
            btnskip.setVisibility(View.VISIBLE);
            llRiskDes.setVisibility(View.GONE);
            btnimgmacrosub.setText("Risk2");
            btnrisk.setText("Risk2");
            hideoption();
        } else if (strphoto.equalsIgnoreCase("Pitch")) {
            btnrisk.setText("Pitch");
            btnimgmacrosub.setText("Pitch");

            hidepitchoption();


        } else if (strphoto.equalsIgnoreCase("Shingle")) {
            btnimgmacrosub.setText("Shingle");

            btnshinglemenu1.setText("25 years");
            btnshinglemenu2.setText("3 Tab");

            hideShingleoption();
        } else if (strphoto.equalsIgnoreCase("Gutter")) {
            btnimgmacrosub.setText("Gutter");

            btnguttermenu1.setText("5 inch Gutter");
            hideGutteroption();

        } else if (strphoto.equalsIgnoreCase("Overhang")) {
            btnimgmacrosub.setText("Overhang");

            btnoverhangmenu1.setText("1");
            hideoverhangoption();
        } else if (strphoto.equalsIgnoreCase("Type of siding")) {
            btnimgmacrosub.setText("Type of siding");
            btnTypesOfSidingMenu1.setText("8");
            btnTypesOfSidingMenu2.setText("Vinyl Single ");

            hideTypesOfSidingOption();
        } else if (strphoto.equalsIgnoreCase("hail frontslopenext")) {
            btnimgmacrosub.setText(gethailname());
            btnhailnodamages.setVisibility(View.GONE);
            btnhailmaterialdamages.setVisibility(View.GONE);
            if (btnimgmacrosub.getText().toString().trim().equals("Overview")) {
                btnhailmenu1.setVisibility(View.INVISIBLE);
                btnhailmenu1.setText("");
            } else if (btnimgmacrosub.getText().toString().trim().equals("test sq Overview")) {
                btnhailmenu1.setVisibility(View.GONE);
                btnmaterial1.setVisibility(View.GONE);
                btnocb.setText("O");
                btnocb.setTag("1");
                strboc = "1";
                strboctype = "Overview";

                if (btnhailtype.getText().toString().equals("Front Slope")) {
                    btnhailmenu1.setText(no_frontslope);
                } else if (btnhailtype.getText().toString().equals("Right Slope")) {
                    btnhailmenu1.setText(no_rightslope);
                } else if (btnhailtype.getText().toString().equals("Left Slope")) {
                    btnhailmenu1.setText(no_leftslope);
                } else if (btnhailtype.getText().toString().equals("Rear Slope")) {
                    btnhailmenu1.setText(no_rearslope);
                    System.out.println("data user 1");
                }
            } else if (btnimgmacrosub.getText().toString().trim().equals("test sq")) {


                //chnage
                btnhailmenu1.setVisibility(View.GONE);
                btnmaterial1.setVisibility(View.GONE);
                if (btnhailtype.getText().toString().equals("Front Slope")) {
                    btnhailmenu1.setText(no_frontslope);
                } else if (btnhailtype.getText().toString().equals("Right Slope")) {
                    btnhailmenu1.setText(no_rightslope);
                } else if (btnhailtype.getText().toString().equals("Left Slope")) {
                    btnhailmenu1.setText(no_leftslope);
                } else if (btnhailtype.getText().toString().equals("Rear Slope")) {
                    btnhailmenu1.setText(no_rearslope);
                    System.out.println("data user 2");
                }
            } else if (btnimgmacrosub.getText().toString().trim().equals("hail damage close up on shingles") ||
                    btnimgmacrosub.getText().toString().trim().equals("hail damage close up on shingles 2") ||
                    btnimgmacrosub.getText().toString().trim().equals("hail damage close up on shingles 3")
            ) {

                btnocb.setVisibility(View.INVISIBLE);
                btnhailskip.setText("Next Slope");
                btndamagetype1.setVisibility(View.VISIBLE);
                btnmaterial1.setVisibility(View.GONE);
                btndamagetype1.setText("Hail Damage");

                btnhailnodamages.setVisibility(View.VISIBLE);
                btnhailmaterialdamages.setVisibility(View.VISIBLE);

                strhaildamage=btndamagetype1.getText().toString();

                btnhailnodamages.setTag("2");
                btnhailnodamages.setBackgroundResource(R.drawable.red_button_background);
                btnhailnodamages.setText(btndamagetype1.getText().toString());





                //change
                btnhailmenu1.setVisibility(View.VISIBLE);
                //btnhailmenu1.setText("");
                btnhailmenu1.setText("Shingles");

                strhailmaterialdamage=btnhailmenu1.getText().toString();

                btnhailmaterialdamages.setTag("2");
                btnhailmaterialdamages.setBackgroundResource(R.drawable.red_button_background);
                btnhailmaterialdamages.setText(btnhailmenu1.getText().toString());

                btnocb.setText("C");
                btnocb.setTag("2");
                strboc = "2";
                strboctype = "Close up";

            } else if (btnimgmacrosub.getText().toString().trim().equals("hail damage close up on shingles 4") ||
                    btnimgmacrosub.getText().toString().trim().equals("hail damage close up on 1")) {
                //change
                btnhailmenu1.setVisibility(View.VISIBLE);
                btnhailmenu1.setText("Shingles");
                // btnhailmenu1.setText("");
            } else if (btnimgmacrosub.getText().toString().trim().equals("hail damage close up on 2")) {
                //change

                // btnhailmenu1.setText("");

                btnhailmenu1.setVisibility(View.VISIBLE);
                btnhailmenu1.setText("Shingles");
            }

            hidehailoption();
        } else {
            btnsubmenu.setVisibility(View.VISIBLE);
            getcostammenu(false);

            hidecostmoption();
        }
        getalphaname();
    }

    private String gethailname() {
        /*
                for (int i=0;i<hail_fs.length;i++)
                {
                    if(btnimgmacrosub.getText().toString().trim().equals(hail_fs[i].toString().trim()))
                    {
                        return hail_fs[i+1];
                    }
                }*/

        txt_slopeno.setVisibility(View.GONE);



        if (btnimgmacrosub.getText().toString().trim().equals("Overview")) {

            txt_slopeno.setVisibility(View.VISIBLE);
            return "test sq Overview";
        } else if (btnimgmacrosub.getText().toString().trim().equals("test sq Overview")) {
            txt_slopeno.setVisibility(View.VISIBLE);
            return "test sq";

        } else if (btnimgmacrosub.getText().toString().trim().equals("test sq")) {
            return "hail damage close up on shingles";

        } else if (btnimgmacrosub.getText().toString().trim().equals("hail damage close up on shingles")) {
            return "hail damage close up on shingles";
        }

        return "";
    }

    private String gethailimgname() {
        for (int i = 0; i < hail_fs.length; i++) {
            if (btnimgmacrosub.getText().toString().trim().equals(hail_fs[i].toString().trim())) {

                if (btnhailtype.getText().toString().equals("Front Slope")) {
                    return hail_fs_imgname[i];
                } else if (btnhailtype.getText().toString().equals("Right Slope")) {
                    return hail_rs_imgname[i];
                } else if (btnhailtype.getText().toString().equals("Left Slope")) {
                    return hail_ls_imgname[i];
                } else if (btnhailtype.getText().toString().equals("Rear Slope")) {
                    return hail_rears_imgname[i];
                }


                break;
            }
        }

        return "";
    }


    private void getcostammenu(Boolean isShowing) {

        Log.e("cid-->", "" + cid_marcos);

        PopupMenu popupMenu2 = new PopupMenu(this, findViewById(R.id.btnsubmenu));

        int indexrow = 0;

        macrocostamdetail = new ArrayList<>();
        macrosubmenu = new ArrayList<>();
        opendatabase();

        SELECT_SQL = "SELECT * from tbl_macrossubcat where cid =" + cid_marcos;
        Cur = DB.rawQuery(SELECT_SQL, null);
        if (Cur != null && Cur.getCount() > 0) {
            Cur.moveToFirst();
            do {

                Map<String, String> Data = new HashMap<>();

//                String id = ""+Cur.getInt(Cur.getColumnIndex("id"));

                String name = Cur.getString(Cur.getColumnIndex("name"));
                String value = Cur.getString(Cur.getColumnIndex("value"));

                popupMenu2.getMenu().add(Menu.NONE, indexrow, Menu.NONE, "" + name);
                indexrow++;

                macrosubmenu.add(name);

                Data.put("name", name);
                Data.put("value", value);
                macrocostamdetail.add(Data);

            }
            while (Cur.moveToNext());
        }
        Cur.close();
        DB.close();


        if (macrosubmenu.size() != 0) {
            btnsubmenu.setVisibility(View.VISIBLE);
            btnsubmenu.setText(macrosubmenu.get(0));
        } else {
            btnsubmenu.setVisibility(View.GONE);
        }

        popupMenu2.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem arg0) {

                btnsubmenu.setText(arg0.getTitle());
                getalphaname();
                return false;
            }
        });

        if (isShowing)
            popupMenu2.show();

    }

    private void getinteriordbvalue() {

        value = 0;
        itrarr = new ArrayList<>();

        opendatabase();

        SELECT_SQL = "SELECT * FROM tbl_interior ORDER BY value ASC";
        Cur = DB.rawQuery(SELECT_SQL, null);
        if (Cur != null && Cur.getCount() > 0) {
            Cur.moveToFirst();
            do {

                String strvalue = Cur.getString(Cur.getColumnIndex("value"));
                Log.e("Value==>", "" + strvalue);
                String strshortname = Cur.getString(Cur.getColumnIndex("value"));
                itrarr.add(strshortname);


                value++;
            }
            while (Cur.moveToNext());
        }
        Cur.close();
        DB.close();
    }


    private void setInteriorvalue() {
        value = 0;
        itrarr = new ArrayList<>();

        PopupMenu popupMenu2 = new PopupMenu(this, findViewById(R.id.btniteriortype));

        opendatabase();

        SELECT_SQL = "SELECT * FROM tbl_interior ORDER BY value ASC";
        Cur = DB.rawQuery(SELECT_SQL, null);
        if (Cur != null && Cur.getCount() > 0) {
            Cur.moveToFirst();
            do {

                String strvalue = Cur.getString(Cur.getColumnIndex("value"));
                if (strvalue.equalsIgnoreCase("Custom Text"))
                    continue;
                Log.e("Value==>", "" + strvalue);

                if(strvalue.equals("Blank"))
                {
                }
                else {
                    String strshortname = Cur.getString(Cur.getColumnIndex("shortname"));
                    itrarr.add(strshortname);

                    popupMenu2.getMenu().add(Menu.NONE, value, Menu.NONE, strvalue);

                    value++;
                }
            }
            while (Cur.moveToNext());
            /*popupMenu2.getMenu().add(Menu.NONE, value, Menu.NONE, "Office 2");
            itrarr.add("Office 2");
            value++;*/
            popupMenu2.getMenu().add(Menu.NONE, value, Menu.NONE, "Room");
            itrarr.add("");
            value++;
            popupMenu2.getMenu().add(Menu.NONE, value, Menu.NONE, "Custom Text");
        }
        Cur.close();
        DB.close();

        popupMenu2.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem arg0) {


                btnmatrialsubmenu.setText("0");

                if(btnnodamages.getTag().equals("2"))
                {
                    btnnodamages.performClick();
                }

                btniteriortype.setTag(arg0.getItemId() + "");
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) arg0.getMenuInfo();


                if (arg0.getTitle().toString().equalsIgnoreCase("Custom Text")) {
                    showcostmtextalert("3");
                } else {
                    iteriortypeindex = arg0.getItemId();

                    if (arg0.getTitle().toString().equalsIgnoreCase("Room")) {
                        btniteriortype.setText("Room");
                        btniteriortype.setTag("0");
                    } else {
                        btniteriortype.setText(arg0.getTitle());
                    }

                    btnocb.setTag("3");
                    selectocb();
                    strboc = "1";
                    strboctype = "Overview";


                    btnSubAreaTogal.setTag("2");
                    btnSubAreaTogal.performClick();

                    btntype.setTag("2");
                    btntype.performClick();

                    getalphaname();
                }




                return false;
            }
        });
        popupMenu2.show();
    }

    private void refereshgallery() {

        allFiles = savefile.listFiles();

        for (int i = 0; i < allFiles.length; i++) {
            new SingleMediaScanner(HomeActivity.this, allFiles[i]);
        }
    }

    public void captureImage() throws IOException {
        btno.setVisibility(View.GONE);
        btnnc.setVisibility(View.GONE);
        btnocb.setVisibility(View.GONE);
//        btnnodamagetype.setVisibility(View.GONE);
        camera.takePicture(null, null, jpegCallback);
    }

    public void refreshCamera() {
        if (surfaceHolder.getSurface() == null) {
            // preview surface does not exist
            return;
        }

        // stop preview before making changes
        try {
            if (isCameraOn) {
                camera.stopPreview();
                isCameraOn = false;
            }
        } catch (Exception e) {
            // ignore: tried to stop a non-existent preview
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here
        // start preview with new settings
        try {
            camera.setPreviewDisplay(surfaceHolder);
            if (!isCameraOn) {
                camera.startPreview();
                isCameraOn = true;
            }
        } catch (Exception e) {

        }
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // Now that the size is known, set up the camera parameters and begin
        // the preview.

//        refreshCamera();
    }

    public void surfaceCreated(SurfaceHolder holder) {

        try {
            if (!isCameraOn) {
                camera = Camera.open(0);
                isCameraOn = true;
            }
        } catch (RuntimeException e) {
            System.err.println(e);
            return;
        }

        createCamera();

        try {
            camera.setPreviewDisplay(surfaceHolder);
            if (!isCameraOn) {
                camera.startPreview();
                isCameraOn = true;
            }
        } catch (Exception e) {
            System.err.println(e);
            return;
        }
    }

    private void createCamera() {
        Camera.Parameters params = camera.getParameters();

        if (hasFlash) {
            params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        }
        if (params.isZoomSupported()) {
            zoomseek.setMax(params.getMaxZoom());
        } else {
            zoomseek.setVisibility(View.GONE);
        }

        Size pictureSize = MyCamPara.getInstance().getPictureSize(params.getSupportedPictureSizes(), 1000);

        Size previewSize = MyCamPara.getInstance().getPreviewSize(params.getSupportedPreviewSizes(), pictureSize.height);

        if (previewSize != null)
            params.setPreviewSize(previewSize.width, previewSize.height);
        if (pictureSize != null)
            params.setPictureSize(pictureSize.width, pictureSize.height);

        params.setPictureFormat(ImageFormat.JPEG);
        params.setJpegQuality(100);
        camera.setParameters(params);
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // stop preview and release camera

        if (camera != null) {
            camera.stopPreview();
            camera.release();
            camera = null;
            isCameraOn = false;
        }

    }

    private void showcostmnamealert() {
        addcostmphotoname = "zz";
        /* Alert Dialog Code Start*/
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setCancelable(false);
        alert.setTitle("Add custom  name"); //Set Alert dialog title here

        // Set an EditText view to get user input
        final EditText input = new EditText(this);

        input.setSelection(input.getText().toString().length());
        alert.setView(input);

        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //You will get as string input data in this variable.
                // here we convert the input to a string and show in a toast.
//						String srt = input.getEditableText().toString();

                if (input.getText().toString().trim().equals("")) {
                    Toast.makeText(getApplicationContext(), "Enter  name", Toast.LENGTH_LONG).show();

                } else {
                    addcostmphotoname = input.getText().toString().trim();
                    System.out.println("data of user input is:-" + addcostmphotoname);
                    txtalphaname.setText(addcostmphotoname);

                }
            } // End of onClick(DialogInterface dialog, int whichButton)
        }); //End of alert.setPositiveButton
        alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
                dialog.cancel();
            }
        }); //End of alert.setNegativeButton
        AlertDialog alertDialog = alert.create();
        alertDialog.show();

    }

    private void addmenuvalue(String tname, String strtitle) {

        rlsetting.setVisibility(View.GONE);
        Intent addnewvalue = new Intent(getApplicationContext(), AddValueActivity.class);
        addnewvalue.putExtra("tablename", tname);
        addnewvalue.putExtra("title", strtitle);
        startActivity(addnewvalue);
    }

    private View.OnClickListener onClickInteriorNewMacro() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == btnInteriorNewMacro.getId()) {

                    clickonView("btnInteriorNewMacro");


                    PopupMenu popupMenu = new PopupMenu(mContext, btnInteriorNewMacro);
                    popupMenu.getMenu().add("Overview");
                    popupMenu.getMenu().add("Ceiling");
                    popupMenu.getMenu().add("Wall");
                    popupMenu.getMenu().add("Floor");

                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            btnInteriorNewMacro.setText(item.getTitle().toString());
                            btnInteriorNewMacro.setTag(item.getTitle().toString());

                            if (btnInteriorNewMacro.getTag().toString().equals("Floor"))
                                btnIntNewMaterial.setText("Carpet");
                            else
                                btnIntNewMaterial.setText("Drywall");

                            if (btnInteriorNewMacro.getTag().toString().equals("Overview")) {
                                btnIntNewDamage.setVisibility(View.INVISIBLE);
                                btnIntNewMaterial.setVisibility(View.INVISIBLE);
                                btnIntNewMaterialRed.setVisibility(View.INVISIBLE);
                                btnIntNewInsulation.setVisibility(View.INVISIBLE);
                                btnIntNewQty.setVisibility(View.INVISIBLE);
                            } else {
                                btnIntNewDamage.setVisibility(View.VISIBLE);
                                btnIntNewMaterial.setVisibility(View.VISIBLE);
                                btnIntNewMaterialRed.setVisibility(View.VISIBLE);
                                btnIntNewInsulation.setVisibility(View.VISIBLE);
                                btnIntNewQty.setVisibility(View.VISIBLE);
                            }

                            btnIntNewMaterial.setTag("Blank");

                            btnocb.setTag("3");
        selectocb();
                            strboc = "1";
                            strboctype = "Overview";

                            btnIntNewDamage.setText("Damage");
                            btnIntNewDamage.setTag("0");

                            btnIntNewDamageRed.setVisibility(View.INVISIBLE);
                            btnIntNewMaterialRed.setBackgroundResource(R.drawable.button_background);
                            btnIntNewMaterialRed.setTag("0");
                            btnIntNewMaterialRed.setText(btnIntNewMaterial.getText());

                            btnIntNewInsulation.setText("Insulation");
                            btnIntNewQty.setText("Quantity");

                            getalphaname();
                            return false;
                        }
                    });

                    popupMenu.show();
                } else if (view.getId() == rlInteriorNewBack.getId()) {

                    clickonView("rlInteriorNewBack");

                    showoption();
                    rlInteriorNewPhoto.setVisibility(View.GONE);
                    btnInteriorNewMacro.setVisibility(View.GONE);
                    btnnodamages.setVisibility(View.VISIBLE);

                    btnnc.setVisibility(View.VISIBLE);//Dmakchange
                    btno.setVisibility(View.VISIBLE);//dmakchange
                    btnocb.setVisibility(View.INVISIBLE);
                    btnimgmacro.setVisibility(View.VISIBLE);
                    btnimgmacro.setText("Macro");

                    btnQue.setVisibility(View.VISIBLE);
                    setQueCount();

                    getalphaname();
                    if (btnrei.getText().toString().equals("I")) {
                        rliteriortype.setVisibility(View.VISIBLE);
                    } else {
                        rliteriortype.setVisibility(View.GONE);
                    }
                } else if (view.getId() == btnInteriorNewNext.getId()) {

                    clickonView("btnInteriorNewNext");


                    if (btnInteriorNewMacro.getTag().toString().equals("Floor")) {
                        onClick(rlInteriorNewBack);
                    } else
                        interiorNewMacroNext();
                } else if (view.getId() == btnIntNewDamage.getId()) {

                    clickonView("btnIntNewDamage");

                    value = 0;

                    PopupMenu popupMenu2 = new PopupMenu(mContext, btnIntNewDamage);

                    opendatabase();

                    SELECT_SQL = "SELECT * FROM tbldamagetype";
                    Cur = DB.rawQuery(SELECT_SQL, null);
                    if (Cur != null && Cur.getCount() > 0) {
                        Cur.moveToFirst();
                        do {

                            String strvalue = Cur.getString(Cur.getColumnIndex("value"));
                            if (strvalue.equals("Custom text"))
                                continue;
                            Log.e("Value==>", "" + strvalue);
                            popupMenu2.getMenu().add(Menu.NONE, value, Menu.NONE, strvalue);

                            value++;
                        }
                        while (Cur.moveToNext());
//                        popupMenu2.getMenu().add(Menu.NONE, value, Menu.NONE, "Custom text");
                    }
                    Cur.close();
                    DB.close();

                    popupMenu2.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                        @Override
                        public boolean onMenuItemClick(MenuItem arg0) {
                            btnIntNewDamage.setTag(arg0.getItemId() + "");
                            btnIntNewDamage.setText(arg0.getTitle());

                            btnIntNewDamageRed.setBackgroundResource(R.drawable.red_button_background);
                            btnIntNewDamageRed.setText(btnIntNewDamage.getText().toString());
                            btnIntNewDamageRed.setTag("1");
                            btnIntNewDamageRed.setVisibility(View.VISIBLE);

                            btnIntNewQty.setText("Quantity");

                            if (arg0.getTitle().equals("Blank")) {
                                btnIntNewDamage.setText("Damage");
                                btnIntNewDamageRed.setVisibility(View.INVISIBLE);
                                btnIntNewDamageRed.setTag("0");
                            }

                            getalphaname();

                            return false;
                        }
                    });
                    popupMenu2.show();
                } else if (view.getId() == btnIntNewMaterial.getId()) {

                    clickonView("btnIntNewMaterial");

                    PopupMenu popupMenu2 = new PopupMenu(mContext, btnIntNewMaterial);

                    if (btnInteriorNewMacro.getTag().toString().equalsIgnoreCase("Ceiling")) {

                        popupMenu2.getMenu().add(Menu.NONE, 0, Menu.NONE, "Blank");
                        popupMenu2.getMenu().add(Menu.NONE, 1, Menu.NONE, "Drywall");
                        popupMenu2.getMenu().add(Menu.NONE, 2, Menu.NONE, "Plaster");
                        popupMenu2.getMenu().add(Menu.NONE, 3, Menu.NONE, "tile");
                        popupMenu2.getMenu().add(Menu.NONE, 4, Menu.NONE, "Paint");
                        popupMenu2.getMenu().add(Menu.NONE, 5, Menu.NONE, "Crown molding");
                        popupMenu2.getMenu().add(Menu.NONE, 6, Menu.NONE, "Light");
                        popupMenu2.getMenu().add(Menu.NONE, 7, Menu.NONE, "Ceiling fan");
                        popupMenu2.getMenu().add(Menu.NONE, 8, Menu.NONE, "Wall paper");
                        popupMenu2.getMenu().add(Menu.NONE, 9, Menu.NONE, "Texture");
                        popupMenu2.getMenu().add(Menu.NONE, 10, Menu.NONE, "Trim");
                    } else if (btnInteriorNewMacro.getTag().toString().equalsIgnoreCase("Wall")) {

                        popupMenu2.getMenu().add(Menu.NONE, 0, Menu.NONE, "Blank");
                        popupMenu2.getMenu().add(Menu.NONE, 1, Menu.NONE, "Drywall");
                        popupMenu2.getMenu().add(Menu.NONE, 2, Menu.NONE, "Plaster");
                        popupMenu2.getMenu().add(Menu.NONE, 3, Menu.NONE, "tile");
                        popupMenu2.getMenu().add(Menu.NONE, 4, Menu.NONE, "Paint");
                        popupMenu2.getMenu().add(Menu.NONE, 5, Menu.NONE, "Crown molding");
                        popupMenu2.getMenu().add(Menu.NONE, 6, Menu.NONE, "Light");
                        popupMenu2.getMenu().add(Menu.NONE, 7, Menu.NONE, "Wall paper");
                        popupMenu2.getMenu().add(Menu.NONE, 8, Menu.NONE, "Texture");
                        popupMenu2.getMenu().add(Menu.NONE, 9, Menu.NONE, "Casing");
                        popupMenu2.getMenu().add(Menu.NONE, 10, Menu.NONE, "Baseboards");
                        popupMenu2.getMenu().add(Menu.NONE, 11, Menu.NONE, "Door");
                        popupMenu2.getMenu().add(Menu.NONE, 12, Menu.NONE, "Window");
                        popupMenu2.getMenu().add(Menu.NONE, 13, Menu.NONE, "Cabinet");
                        popupMenu2.getMenu().add(Menu.NONE, 14, Menu.NONE, "Countertop");
                        popupMenu2.getMenu().add(Menu.NONE, 15, Menu.NONE, "Tile");
                        popupMenu2.getMenu().add(Menu.NONE, 16, Menu.NONE, "Insulation");

                    } else if (btnInteriorNewMacro.getTag().toString().equals("Floor")) {

                        popupMenu2.getMenu().add(Menu.NONE, 0, Menu.NONE, "Blank");
                        popupMenu2.getMenu().add(Menu.NONE, 1, Menu.NONE, "Tile");
                        popupMenu2.getMenu().add(Menu.NONE, 2, Menu.NONE, "Vinyl");
                        popupMenu2.getMenu().add(Menu.NONE, 3, Menu.NONE, "Wood");
                        popupMenu2.getMenu().add(Menu.NONE, 4, Menu.NONE, "Laminate");
                        popupMenu2.getMenu().add(Menu.NONE, 5, Menu.NONE, "Subfloor");
                        popupMenu2.getMenu().add(Menu.NONE, 6, Menu.NONE, "Carpet");
                        popupMenu2.getMenu().add(Menu.NONE, 7, Menu.NONE, "Pad");

                    }

                    popupMenu2.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            btnIntNewMaterial.setTag(item.getTitle() + "");
                            btnIntNewMaterial.setText(item.getTitle());

                            btnIntNewMaterialRed.setTag("1");
                            btnIntNewMaterialRed.setBackgroundResource(R.drawable.red_button_background);

                            if (btnIntNewMaterial.getText().toString().equalsIgnoreCase("Blank")) {
                                if (btnInteriorNewMacro.getText().toString().equalsIgnoreCase("floor"))
                                    btnIntNewMaterial.setText("Carpet");
                                else
                                    btnIntNewMaterial.setText("Drywall");
                                btnIntNewMaterialRed.setTag("0");
                                btnIntNewMaterialRed.setBackgroundResource(R.drawable.button_background);
                            }
                            btnIntNewMaterialRed.setText(btnIntNewMaterial.getText());

                            getalphaname();
                            return false;
                        }
                    });
                    popupMenu2.show();
                } else if (view.getId() == btnIntNewDamageRed.getId()) {

                    clickonView("btnIntNewDamageRed");


                    if (btnIntNewDamageRed.getTag().toString().equals("0")) {
                        btnIntNewDamageRed.setBackgroundResource(R.drawable.red_button_background);
                        btnIntNewDamageRed.setTag("1");
                    } else {
                        btnIntNewDamageRed.setBackgroundResource(R.drawable.button_background);
                        btnIntNewDamageRed.setTag("0");
                    }
                } else if (view.getId() == btnIntNewMaterialRed.getId()) {

                    clickonView("btnIntNewMaterialRed");


                    if (btnIntNewMaterialRed.getTag().toString().equals("0")) {
                        btnIntNewMaterialRed.setTag("1");
                        btnIntNewMaterialRed.setBackgroundResource(R.drawable.red_button_background);
                    } else {
                        btnIntNewMaterialRed.setTag("0");
                        btnIntNewMaterialRed.setBackgroundResource(R.drawable.button_background);
                    }
                } else if (view.getId() == btnIntNewInsulation.getId()) {

                    clickonView("btnIntNewInsulation");


                    PopupMenu popupMenu = new PopupMenu(mContext, btnIntNewInsulation);

                    popupMenu.getMenu().add("Blank");
                    popupMenu.getMenu().add("I");
                    popupMenu.getMenu().add("No");
                    popupMenu.getMenu().add(".25I");
                    popupMenu.getMenu().add(".5I");
                    popupMenu.getMenu().add(".75I");

                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            if (item.getTitle().toString().equals("Blank"))
                                btnIntNewInsulation.setText("Insulation");
                            else
                                btnIntNewInsulation.setText(item.getTitle().toString());
                            return false;
                        }
                    });

                    popupMenu.show();
                } else if (view.getId() == btnIntNewQty.getId()) {

                    clickonView("btnIntNewQty");


                    PopupMenu popupMenu = new PopupMenu(mContext, btnIntNewQty);

                    popupMenu.getMenu().add("Blank");
                    for (int i = 1; i <= 99; i++) {
                        popupMenu.getMenu().add(i + "");
                    }

                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            btnIntNewQty.setText(item.getTitle());
                            if (item.getTitle().toString().equalsIgnoreCase("blank"))
                                btnIntNewQty.setText("Quantity");
                            return false;
                        }
                    });

                    popupMenu.show();
                }
                getalphaname();
            }
        };
    }

    private void interiorNewMacroNext() {

        btno.setVisibility(View.VISIBLE);//dmakchange
        btnocb.setVisibility(View.INVISIBLE);
        if (btnInteriorNewMacro.getTag().toString().equalsIgnoreCase("Overview")) {
            btnInteriorNewMacro.setText("Ceiling");
            btnInteriorNewMacro.setTag("Ceiling");

            btnIntNewDamage.setVisibility(View.VISIBLE);
            btnIntNewMaterial.setVisibility(View.VISIBLE);
            btnIntNewMaterialRed.setVisibility(View.VISIBLE);
            btnIntNewInsulation.setVisibility(View.VISIBLE);
            btnIntNewQty.setVisibility(View.VISIBLE);

        } else if (btnInteriorNewMacro.getTag().toString().equalsIgnoreCase("Ceiling")) {
            btnInteriorNewMacro.setText("Wall");
            btnInteriorNewMacro.setTag("Wall");

            btnIntNewMaterial.setText("Drywall");
        } else if (btnInteriorNewMacro.getTag().toString().equalsIgnoreCase("Wall")) {
            btnInteriorNewMacro.setText("Floor");
            btnInteriorNewMacro.setTag("Floor");

            btnIntNewMaterial.setText("Carpet");
        }

        btnIntNewMaterial.setTag("Blank");

        btnocb.setTag("3");
        selectocb();
        strboc = "1";
        strboctype = "Overview";

        btnIntNewDamage.setText("Damage");
        btnIntNewDamage.setTag("0");

        btnIntNewDamageRed.setVisibility(View.INVISIBLE);
        btnIntNewMaterialRed.setBackgroundResource(R.drawable.button_background);
        btnIntNewMaterialRed.setTag("0");
        btnIntNewMaterialRed.setText(btnIntNewMaterial.getText());

        btnIntNewInsulation.setText("Insulation");
        btnIntNewQty.setText("Quantity");

        getalphaname();
    }

    private String intRoomName, intRoomFullName;

    private void selectInteriorRoom(final String from) {
        btno.setVisibility(View.VISIBLE);//dmakchange
        btnocb.setVisibility(View.INVISIBLE);
        intRoomName = "";
        intRoomFullName = "";
        final Dialog dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.popup_select_room_interior);

        final Button btnIntRoom = dialog.findViewById(R.id.btnIntRoom);
        Button btnSubmit = dialog.findViewById(R.id.btnSubmit);
        final TextView txtRoomDec = dialog.findViewById(R.id.txtRoomDec);
        final TextView txtRoom = dialog.findViewById(R.id.txtRoom);
        final TextView txtRoomInc = dialog.findViewById(R.id.txtRoomInc);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == txtRoomDec.getId()) {

                    clickonView("txtRoomDec");


                    try {
                        int cnt = Integer.parseInt(txtRoom.getText().toString());
                        if (cnt != 0)
                            txtRoom.setText(--cnt + "");
                    } catch (Exception e) {
                        txtRoom.setText("0");
                    }
                } else if (view.getId() == txtRoomInc.getId()) {

                    clickonView("txtRoomInc");

                    try {
                        int cnt = Integer.parseInt(txtRoom.getText().toString());
                        txtRoom.setText(++cnt + "");
                    } catch (Exception e) {
                        txtRoom.setText("0");
                    }
                }
            }
        };

        txtRoomDec.setOnClickListener(onClickListener);
        txtRoomInc.setOnClickListener(onClickListener);

        btnIntRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                value = 0;
                itrarr = new ArrayList<>();

                PopupMenu popupMenu2 = new PopupMenu(mContext, btnIntRoom);

                opendatabase();

                SELECT_SQL = "SELECT * FROM tbl_interior ORDER BY value ASC";
                Cur = DB.rawQuery(SELECT_SQL, null);
                if (Cur != null && Cur.getCount() > 0) {
                    Cur.moveToFirst();
                    do {

                        String strvalue = Cur.getString(Cur.getColumnIndex("value"));
                        if (strvalue.equalsIgnoreCase("Custom Text"))
                            continue;
                        Log.e("Value==>", "" + strvalue);
                        String strshortname = Cur.getString(Cur.getColumnIndex("shortname"));
                        itrarr.add(strshortname);

                        popupMenu2.getMenu().add(Menu.NONE, value, Menu.NONE, strvalue);

                        value++;
                    }
                    while (Cur.moveToNext());
//                    popupMenu2.getMenu().add(Menu.NONE, value, Menu.NONE, "Custom Text");
                }
                Cur.close();
                DB.close();

                popupMenu2.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem arg0) {

                        btnIntRoom.setTag(arg0.getItemId() + "");

                        if (arg0.getTitle().toString().equalsIgnoreCase("Custom Text")) {
                            showcostmtextalert("3");
                        } else {
                            iteriortypeindex = arg0.getItemId();

                            if (arg0.getTitle().toString().equalsIgnoreCase("blank")) {
                                btnIntRoom.setText("Room");
                                btnIntRoom.setTag("0");
                                intRoomName = "";
                                intRoomFullName = "";
                            } else {
                                btnIntRoom.setText(arg0.getTitle());
                                intRoomName = itrarr.get(iteriortypeindex);
                                intRoomFullName = arg0.getTitle().toString();
                            }
                        }
                        return false;
                    }
                });
                popupMenu2.show();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!btnIntRoom.getTag().toString().equals("0")) {
                    intRoomName += " " + (txtRoom.getText().toString().equals("0") ? "" : txtRoom.getText().toString());
                    intRoomFullName += " " + (txtRoom.getText().toString().equals("0") ? "" : txtRoom.getText().toString());
                }
                dialog.dismiss();
                if (from.equals("InteriorRoom"))
                    showInteriorNewView();
                else if (from.equals("RoomMacro"))
                    showRooMacroView();
                getalphaname();
            }
        });
        dialog.show();
    }

    private void showInteriorNewView() {
        btnocb.setVisibility(View.INVISIBLE);

        btnimgmacro.setVisibility(View.INVISIBLE);
        rlInteriorNewPhoto.setVisibility(View.VISIBLE);
        btnInteriorNewMacro.setVisibility(View.VISIBLE);

        btnInteriorNewMacro.setText("Overview");
        btnInteriorNewMacro.setTag("Overview");

        btnIntNewDamage.setVisibility(View.INVISIBLE);
        btnIntNewMaterial.setVisibility(View.INVISIBLE);
        btnIntNewMaterialRed.setVisibility(View.INVISIBLE);
        btnIntNewInsulation.setVisibility(View.INVISIBLE);
        btnIntNewQty.setVisibility(View.INVISIBLE);

//        btnInteriorNewMacro.setText("Ceiling");
//        btnInteriorNewMacro.setTag("Ceiling");

        btnIntNewDamage.setText("Damage");
        btnIntNewDamage.setTag("0");

        btnIntNewMaterial.setText("Drywall");
        btnIntNewMaterial.setTag("Blank");

        btnIntNewDamageRed.setVisibility(View.INVISIBLE);

        btnIntNewMaterialRed.setBackgroundResource(R.drawable.button_background);
        btnIntNewMaterialRed.setTag("0");
        btnIntNewMaterialRed.setText(btnIntNewMaterial.getText());

        btnIntNewInsulation.setText("Insulation");
        btnIntNewQty.setText("Quantity");

        btnocb.setTag("1");
        btnocb.setText("B");
        strboc = "1";
        strboctype = "Overview";

        btnQue.setVisibility(View.GONE);
        txtQueCount.setVisibility(View.GONE);

        btnareatogal.setVisibility(View.GONE);
        btnSubAreaTogal.setVisibility(View.GONE);

        rlriskphoto.setVisibility(View.GONE);
        rllayerphoto.setVisibility(View.GONE);
        rlshinglephoto.setVisibility(View.GONE);
        rlgutterphoto.setVisibility(View.GONE);
        rloverhangphoto.setVisibility(View.GONE);
        rlpitchphoto.setVisibility(View.GONE);
        llline.setVisibility(View.GONE);
        rlcostm.setVisibility(View.GONE);
        rlfeo.setVisibility(View.GONE);
        rlhail.setVisibility(View.GONE);

        rlmenuselection.setVisibility(View.GONE);
        llArea.setVisibility(View.GONE);
        btnMic.setVisibility(View.GONE);
        btnReport.setVisibility(View.GONE);
        rltorch.setVisibility(View.VISIBLE);
        ibtnflash.setVisibility(View.VISIBLE);
        ibtnLive.setVisibility(View.INVISIBLE);
        imgbtnsetting.setVisibility(View.INVISIBLE);
        rlcontrolview.setVisibility(View.INVISIBLE);
        btnocb.setVisibility(View.INVISIBLE);
        btnlastphoto.setVisibility(View.INVISIBLE);

        btntype.setVisibility(View.INVISIBLE);

        btntype2.setVisibility(View.GONE);

        rlcontrolview.setVisibility(View.INVISIBLE);
        rliteriortype.setVisibility(View.INVISIBLE);

        rliteriortype.setVisibility(View.INVISIBLE);
    }

    private void showInteriorView() {
        btnocb.setText("B");
        btno.setVisibility(View.VISIBLE);//dmakchange
        btnocb.setVisibility(View.INVISIBLE);
        setInteriorDropdownTitle();

        btno.setVisibility(View.VISIBLE);//dmakchange
        btnocb.setVisibility(View.INVISIBLE);
        btnimgmacro.setVisibility(View.INVISIBLE);
        rlInteriorPhoto.setVisibility(View.VISIBLE);
        btnInteriorMacro.setVisibility(View.VISIBLE);

        btnInteriorMacro.setText("Room Overview");
        btnInteriorMacro.setTag("1");

        btnareatogal.setVisibility(View.GONE);
        btnSubAreaTogal.setVisibility(View.GONE);

        rlriskphoto.setVisibility(View.GONE);
        rllayerphoto.setVisibility(View.GONE);
        rlshinglephoto.setVisibility(View.GONE);
        rlgutterphoto.setVisibility(View.GONE);
        rloverhangphoto.setVisibility(View.GONE);
        rlpitchphoto.setVisibility(View.GONE);
        llline.setVisibility(View.GONE);
        rlcostm.setVisibility(View.GONE);
        rlfeo.setVisibility(View.GONE);
        rlhail.setVisibility(View.GONE);

        rlmenuselection.setVisibility(View.GONE);
        llArea.setVisibility(View.GONE);
        btnMic.setVisibility(View.GONE);
        btnReport.setVisibility(View.GONE);
        rltorch.setVisibility(View.VISIBLE);
        ibtnflash.setVisibility(View.VISIBLE);
        ibtnLive.setVisibility(View.INVISIBLE);
        imgbtnsetting.setVisibility(View.INVISIBLE);
        rlcontrolview.setVisibility(View.INVISIBLE);
        btnocb.setVisibility(View.INVISIBLE);
        btnlastphoto.setVisibility(View.INVISIBLE);

        btntype.setVisibility(View.INVISIBLE);

        btntype2.setVisibility(View.GONE);

        rlcontrolview.setVisibility(View.INVISIBLE);
        rliteriortype.setVisibility(View.INVISIBLE);

        rliteriortype.setVisibility(View.INVISIBLE);
    }

    private ArrayList<HashMap<String, String>> arrayListLWHRoomMacro;
    private LWHRoomMacroAdpt lwhRoomMacroAdpt;

    private void showRooMacroView() {
        btnimgmacro.setVisibility(View.INVISIBLE);
        rlRoomMacroPhoto.setVisibility(View.VISIBLE);
        btnRoomMacro.setVisibility(View.VISIBLE);

        chkRoomMacroAffects.setText("Affects Wall");

        lwhRoomMacroAdpt = new LWHRoomMacroAdpt();
        arrayListLWHRoomMacro = new ArrayList<>();
        HashMap<String, String> hm = new HashMap<>();
        hm.put("l", "12");
        hm.put("w", "12");
        hm.put("h", "8");
        arrayListLWHRoomMacro.add(hm);
        listViewLWH.setAdapter(lwhRoomMacroAdpt);
        Utility.setListViewHeightBasedOnChildren(listViewLWH);

        btnRoomMacro.setText("Ceiling");
        btnRoomMacro.setTag("Ceiling");

        btnRoomMacroDamage.setText("Damage");
        btnRoomMacroDamage.setTag("0");

        btnRoomMacroDamageAmount.setVisibility(View.INVISIBLE);

        btnRoomMacroMaterial.setText("Drywall");
        btnRoomMacroMaterial.setTag("Blank");

        btnRoomMacroMaterialFinish.setText("Drywall");
        btnRoomMacroMaterialFinish.setTag("Blank");

        btnRoomMacroDamageRed.setVisibility(View.INVISIBLE);
        btnRoomMacroMaterialRed.setBackgroundResource(R.drawable.button_background);
        btnRoomMacroMaterialRed.setTag("0");
        btnRoomMacroMaterialRed.setText(btnRoomMacroMaterial.getText());
        btnRoomMacroMaterialFinishRed.setBackgroundResource(R.drawable.button_background);
        btnRoomMacroMaterialFinishRed.setTag("0");
        btnRoomMacroMaterialFinishRed.setText(btnRoomMacroMaterialFinish.getText());

        btnocb.setTag("1");
        btnocb.setText("B");
        strboc = "1";
        strboctype = "Overview";

        btnQue.setVisibility(View.GONE);
        txtQueCount.setVisibility(View.GONE);

        btnareatogal.setVisibility(View.GONE);
        btnSubAreaTogal.setVisibility(View.GONE);

        rlriskphoto.setVisibility(View.GONE);
        rllayerphoto.setVisibility(View.GONE);
        rlshinglephoto.setVisibility(View.GONE);
        rlgutterphoto.setVisibility(View.GONE);
        rloverhangphoto.setVisibility(View.GONE);
        rlpitchphoto.setVisibility(View.GONE);
        llline.setVisibility(View.GONE);
        rlcostm.setVisibility(View.GONE);
        rlfeo.setVisibility(View.GONE);
        rlhail.setVisibility(View.GONE);

        rlmenuselection.setVisibility(View.GONE);
        llArea.setVisibility(View.GONE);
        btnMic.setVisibility(View.GONE);
        btnReport.setVisibility(View.GONE);
        rltorch.setVisibility(View.VISIBLE);
        ibtnflash.setVisibility(View.VISIBLE);
        ibtnLive.setVisibility(View.INVISIBLE);
        imgbtnsetting.setVisibility(View.INVISIBLE);
        rlcontrolview.setVisibility(View.INVISIBLE);
        btnocb.setVisibility(View.INVISIBLE);
        btnlastphoto.setVisibility(View.INVISIBLE);

        btntype.setVisibility(View.INVISIBLE);

        btntype2.setVisibility(View.GONE);

        rlcontrolview.setVisibility(View.INVISIBLE);
        rliteriortype.setVisibility(View.INVISIBLE);

        rliteriortype.setVisibility(View.INVISIBLE);
    }

    private class LWHRoomMacroAdpt extends BaseAdapter {

        @Override
        public int getCount() {
            return arrayListLWHRoomMacro.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(final int pos, View view, ViewGroup viewGroup) {

            view = LayoutInflater.from(mContext).inflate(R.layout.item_lwh_room_macro, null);

            final Button btnAdd = view.findViewById(R.id.btnAdd);
            final TextView txtLengthDec = view.findViewById(R.id.txtLengthDec);
            final TextView txtLength = view.findViewById(R.id.txtLength);
            final TextView txtLengthInc = view.findViewById(R.id.txtLengthInc);
            final TextView txtWidthDec = view.findViewById(R.id.txtWidthDec);
            final TextView txtWidth = view.findViewById(R.id.txtWidth);
            final TextView txtWidthInc = view.findViewById(R.id.txtWidthInc);
            final TextView txtHeightDec = view.findViewById(R.id.txtHeightDec);
            final TextView txtHeight = view.findViewById(R.id.txtHeight);
            final TextView txtHeightInc = view.findViewById(R.id.txtHeightInc);

            if (pos != 0)
                btnAdd.setText("-");

            final HashMap<String, String> hmItem = arrayListLWHRoomMacro.get(pos);
            txtLength.setText(hmItem.get("l"));
            txtWidth.setText(hmItem.get("w"));
            txtHeight.setText(hmItem.get("h"));

            View.OnClickListener onClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (view.getId() == btnAdd.getId()) {

                        clickonView("btnAdd");


                        if (pos == 0) {
                            if (arrayListLWHRoomMacro.size() < 3) {
                                HashMap<String, String> hm = new HashMap<>();
                                hm.put("l", "12");
                                hm.put("w", "12");
                                hm.put("h", "8");
                                arrayListLWHRoomMacro.add(hm);
                                lwhRoomMacroAdpt.notifyDataSetChanged();
                            }
                        } else {
                            arrayListLWHRoomMacro.remove(pos);
                        }
                        Utility.setListViewHeightBasedOnChildren(listViewLWH);
                    } else {
                        if (view.getId() == txtLengthDec.getId()) {

                            clickonView("txtLengthDec");


                            try {
                                int cnt = Integer.parseInt(txtLength.getText().toString());
                                if (cnt != 0)
                                    txtLength.setText(--cnt + "");
                            } catch (Exception e) {
                                txtLength.setText("0");
                            }
                        } else if (view.getId() == txtLengthInc.getId()) {


                            clickonView("txtLengthInc");


                            try {
                                int cnt = Integer.parseInt(txtLength.getText().toString());
                                txtLength.setText(++cnt + "");
                            } catch (Exception e) {
                                txtLength.setText("0");
                            }
                        } else if (view.getId() == txtWidthDec.getId()) {

                            clickonView("txtWidthDec");


                            try {
                                int cnt = Integer.parseInt(txtWidth.getText().toString());
                                if (cnt != 0)
                                    txtWidth.setText(--cnt + "");
                            } catch (Exception e) {
                                txtWidth.setText("0");
                            }
                        } else if (view.getId() == txtWidthInc.getId()) {

                            clickonView("txtWidthInc");

                            try {
                                int cnt = Integer.parseInt(txtWidth.getText().toString());
                                txtWidth.setText(++cnt + "");
                            } catch (Exception e) {
                                txtWidth.setText("0");
                            }
                        } else if (view.getId() == txtHeightDec.getId()) {

                            clickonView("txtHeightDec");


                            try {
                                int cnt = Integer.parseInt(txtHeight.getText().toString());
                                if (cnt != 0)
                                    txtHeight.setText(--cnt + "");
                            } catch (Exception e) {
                                txtHeight.setText("0");
                            }
                        } else if (view.getId() == txtHeightInc.getId()) {


                            clickonView("txtHeightInc");

                            try {
                                int cnt = Integer.parseInt(txtHeight.getText().toString());
                                txtHeight.setText(++cnt + "");
                            } catch (Exception e) {
                                txtHeight.setText("0");
                            }
                        }

                        hmItem.put("l", txtLength.getText().toString());
                        hmItem.put("w", txtWidth.getText().toString());
                        hmItem.put("h", txtHeight.getText().toString());
                        arrayListLWHRoomMacro.set(pos, hmItem);
                    }
                }
            };

            btnAdd.setOnClickListener(onClickListener);
            txtLengthDec.setOnClickListener(onClickListener);
            txtLengthInc.setOnClickListener(onClickListener);
            txtWidthDec.setOnClickListener(onClickListener);
            txtWidthInc.setOnClickListener(onClickListener);
            txtHeightDec.setOnClickListener(onClickListener);
            txtHeightInc.setOnClickListener(onClickListener);

            return view;
        }
    }

    private View.OnClickListener onClickRoomMacro() {
        return new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if (view.getId() == btnRoomMacro.getId()) {

                    clickonView("btnRoomMacro");


                    PopupMenu popupMenu = new PopupMenu(mContext, btnRoomMacro);
                    popupMenu.getMenu().add("Ceiling");
                    popupMenu.getMenu().add("Wall");
                    popupMenu.getMenu().add("Floor");
                    btno.setVisibility(View.VISIBLE);//dmakchange
                    btnocb.setVisibility(View.INVISIBLE);
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            btnRoomMacro.setText(item.getTitle().toString());
                            btnRoomMacro.setTag(item.getTitle().toString());

                            chkRoomMacroAffects.setVisibility(View.GONE);
                            if (btnRoomMacro.getTag().toString().equals("Floor")) {
                                btnRoomMacroMaterial.setText("Carpet");
                                btnRoomMacroMaterialFinish.setText("Carpet");
                            } else {
                                btnRoomMacroMaterial.setText("Drywall");
                                btnRoomMacroMaterialFinish.setText("Drywall");

                                chkRoomMacroAffects.setVisibility(View.VISIBLE);
                                if (btnRoomMacro.getTag().toString().equals("Ceiling"))
                                    chkRoomMacroAffects.setText("Affects Wall");
                                else
                                    chkRoomMacroAffects.setText("Affects Ceiling");
                            }

                            btnRoomMacroMaterial.setTag("Blank");
                            btnRoomMacroMaterialFinish.setTag("Blank");

                            btnocb.setTag("3");
        selectocb();
                            strboc = "1";
                            strboctype = "Overview";

                            btnRoomMacroDamage.setText("Damage");
                            btnRoomMacroDamage.setTag("0");

                            btnRoomMacroDamageAmount.setVisibility(View.INVISIBLE);

                            btnRoomMacroDamageRed.setVisibility(View.INVISIBLE);
                            btnRoomMacroMaterialRed.setBackgroundResource(R.drawable.button_background);
                            btnRoomMacroMaterialRed.setTag("0");
                            btnRoomMacroMaterialRed.setText(btnRoomMacroMaterial.getText());
                            btnRoomMacroMaterialFinishRed.setBackgroundResource(R.drawable.button_background);
                            btnRoomMacroMaterialFinishRed.setTag("0");
                            btnRoomMacroMaterialFinishRed.setText(btnRoomMacroMaterialFinish.getText());

//        btnIntNewInsulation.setText("Insulation");
//        btnIntNewQty.setText("Quantity");

                            getalphaname();
                            return false;
                        }
                    });

                    popupMenu.show();
                } else if (view.getId() == rlRoomMacroBack.getId()) {

                    clickonView("rlRoomMacroBack");


                    showoption();
                    rlRoomMacroPhoto.setVisibility(View.GONE);
                    btnRoomMacro.setVisibility(View.GONE);
                    btnnodamages.setVisibility(View.VISIBLE);
                    btno.setVisibility(View.VISIBLE);//dmakchange
                    btnocb.setVisibility(View.INVISIBLE);
                    btnimgmacro.setVisibility(View.VISIBLE);
                    btnimgmacro.setText("Macro");

                    btnQue.setVisibility(View.VISIBLE);
                    setQueCount();

                    getalphaname();
                    if (btnrei.getText().toString().equals("I")) {
                        rliteriortype.setVisibility(View.VISIBLE);
                    } else {
                        rliteriortype.setVisibility(View.GONE);
                    }
                } else if (view.getId() == btnRoomMacroNext.getId()) {

                    clickonView("btnRoomMacroNext");


                    if (btnRoomMacro.getTag().toString().equals("Floor")) {
                        onClick(rlRoomMacroBack);
                    } else
                        roomMacroNext();
                } else if (view.getId() == btnRoomMacroDamageAmount.getId()) {

                    clickonView("btnRoomMacroDamageAmount");


                    AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
                    alert.setTitle("Damage Amount");
                    final EditText edtAmt = new EditText(mContext);
                    edtAmt.setInputType(InputType.TYPE_CLASS_TEXT);
                    edtAmt.setHint("Enter Damage Amount");
                    edtAmt.setSingleLine(true);
                    alert.setView(edtAmt);
                    alert.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (edtAmt.getText().toString().equals(""))
                                btnRoomMacroDamageAmount.setText("Damage Amount");
                            else
                                btnRoomMacroDamageAmount.setText(edtAmt.getText().toString());
                        }
                    });
                    alert.show();
                } else if (view.getId() == btnRoomMacroDamage.getId()) {

                    clickonView("btnRoomMacroDamage");


                    value = 0;

                    PopupMenu popupMenu2 = new PopupMenu(mContext, btnRoomMacroDamage);

                    opendatabase();

                    SELECT_SQL = "SELECT * FROM tbldamagetype";
                    Cur = DB.rawQuery(SELECT_SQL, null);
                    if (Cur != null && Cur.getCount() > 0) {
                        Cur.moveToFirst();
                        do {

                            String strvalue = Cur.getString(Cur.getColumnIndex("value"));
                            if (strvalue.equals("Custom text"))
                                continue;
                            popupMenu2.getMenu().add(Menu.NONE, value, Menu.NONE, strvalue);

                            value++;
                        }
                        while (Cur.moveToNext());
//                        popupMenu2.getMenu().add(Menu.NONE, value, Menu.NONE, "Custom text");
                    }
                    Cur.close();
                    DB.close();

                    popupMenu2.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                        @Override
                        public boolean onMenuItemClick(MenuItem arg0) {
                            btnRoomMacroDamage.setTag(arg0.getItemId() + "");
                            btnRoomMacroDamage.setText(arg0.getTitle());

                            btnRoomMacroDamageRed.setBackgroundResource(R.drawable.red_button_background);
                            btnRoomMacroDamageRed.setText(btnRoomMacroDamage.getText().toString());
                            btnRoomMacroDamageRed.setTag("1");
                            btnRoomMacroDamageRed.setVisibility(View.VISIBLE);

//                            btnIntNewQty.setText("Quantity");
                            btnRoomMacroDamageAmount.setVisibility(View.VISIBLE);
                            if (arg0.getTitle().equals("Blank")) {
                                btnRoomMacroDamage.setText("Damage");
                                btnRoomMacroDamageRed.setVisibility(View.INVISIBLE);
                                btnRoomMacroDamageRed.setTag("0");

                                btnRoomMacroDamageAmount.setVisibility(View.INVISIBLE);
                            }

                            getalphaname();

                            return false;
                        }
                    });
                    popupMenu2.show();
                } else if (view.getId() == btnRoomMacroDamageRed.getId()) {

                    clickonView("btnRoomMacroDamageRed");


                    if (btnRoomMacroDamageRed.getTag().toString().equals("0")) {
                        btnRoomMacroDamageRed.setBackgroundResource(R.drawable.red_button_background);
                        btnRoomMacroDamageRed.setTag("1");

                        btnRoomMacroDamageAmount.setVisibility(View.VISIBLE);
                    } else {
                        btnRoomMacroDamageRed.setBackgroundResource(R.drawable.button_background);
                        btnRoomMacroDamageRed.setTag("0");

                        btnRoomMacroDamageAmount.setVisibility(View.INVISIBLE);
                    }
                } else if (view.getId() == btnRoomMacroMaterial.getId() || view.getId() == btnRoomMacroMaterialFinish.getId()) {


                    clickonView("btnRoomMacroMaterial,btnRoomMacroMaterialFinish,btnRoomMacroMaterialFinish");


                    PopupMenu popupMenu2 = new PopupMenu(mContext, btnRoomMacroMaterial);
                    if (view.getId() == btnRoomMacroMaterialFinish.getId())
                        popupMenu2 = new PopupMenu(mContext, btnRoomMacroMaterialFinish);

                    if (btnRoomMacro.getTag().toString().equalsIgnoreCase("Ceiling")) {

                        popupMenu2.getMenu().add(Menu.NONE, 0, Menu.NONE, "Blank");
                        popupMenu2.getMenu().add(Menu.NONE, 1, Menu.NONE, "Drywall");
                        popupMenu2.getMenu().add(Menu.NONE, 2, Menu.NONE, "Plaster");
                        popupMenu2.getMenu().add(Menu.NONE, 3, Menu.NONE, "tile");
                        popupMenu2.getMenu().add(Menu.NONE, 4, Menu.NONE, "Paint");
                        popupMenu2.getMenu().add(Menu.NONE, 5, Menu.NONE, "Crown molding");
                        popupMenu2.getMenu().add(Menu.NONE, 6, Menu.NONE, "Light");
                        popupMenu2.getMenu().add(Menu.NONE, 7, Menu.NONE, "Ceiling fan");
                        popupMenu2.getMenu().add(Menu.NONE, 8, Menu.NONE, "Wall paper");
                        popupMenu2.getMenu().add(Menu.NONE, 9, Menu.NONE, "Texture");
                        popupMenu2.getMenu().add(Menu.NONE, 10, Menu.NONE, "Trim");
                    } else if (btnRoomMacro.getTag().toString().equalsIgnoreCase("Wall")) {

                        popupMenu2.getMenu().add(Menu.NONE, 0, Menu.NONE, "Blank");
                        popupMenu2.getMenu().add(Menu.NONE, 1, Menu.NONE, "Drywall");
                        popupMenu2.getMenu().add(Menu.NONE, 2, Menu.NONE, "Plaster");
                        popupMenu2.getMenu().add(Menu.NONE, 3, Menu.NONE, "tile");
                        popupMenu2.getMenu().add(Menu.NONE, 4, Menu.NONE, "Paint");
                        popupMenu2.getMenu().add(Menu.NONE, 5, Menu.NONE, "Crown molding");
                        popupMenu2.getMenu().add(Menu.NONE, 6, Menu.NONE, "Light");
                        popupMenu2.getMenu().add(Menu.NONE, 7, Menu.NONE, "Wall paper");
                        popupMenu2.getMenu().add(Menu.NONE, 8, Menu.NONE, "Texture");
                        popupMenu2.getMenu().add(Menu.NONE, 9, Menu.NONE, "Casing");
                        popupMenu2.getMenu().add(Menu.NONE, 10, Menu.NONE, "Baseboards");
                        popupMenu2.getMenu().add(Menu.NONE, 11, Menu.NONE, "Door");
                        popupMenu2.getMenu().add(Menu.NONE, 12, Menu.NONE, "Window");
                        popupMenu2.getMenu().add(Menu.NONE, 13, Menu.NONE, "Cabinet");
                        popupMenu2.getMenu().add(Menu.NONE, 14, Menu.NONE, "Countertop");
                        popupMenu2.getMenu().add(Menu.NONE, 15, Menu.NONE, "Tile");
                        popupMenu2.getMenu().add(Menu.NONE, 16, Menu.NONE, "Insulation");

                    } else if (btnRoomMacro.getTag().toString().equals("Floor")) {

                        popupMenu2.getMenu().add(Menu.NONE, 0, Menu.NONE, "Blank");
                        popupMenu2.getMenu().add(Menu.NONE, 1, Menu.NONE, "Tile");
                        popupMenu2.getMenu().add(Menu.NONE, 2, Menu.NONE, "Vinyl");
                        popupMenu2.getMenu().add(Menu.NONE, 3, Menu.NONE, "Wood");
                        popupMenu2.getMenu().add(Menu.NONE, 4, Menu.NONE, "Laminate");
                        popupMenu2.getMenu().add(Menu.NONE, 5, Menu.NONE, "Subfloor");
                        popupMenu2.getMenu().add(Menu.NONE, 6, Menu.NONE, "Carpet");
                        popupMenu2.getMenu().add(Menu.NONE, 7, Menu.NONE, "Pad");

                    }

                    popupMenu2.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            if (view.getId() == btnRoomMacroMaterial.getId()) {

                                clickonView("btnRoomMacroMaterial");



                                btnRoomMacroMaterial.setTag(item.getTitle() + "");
                                btnRoomMacroMaterial.setText(item.getTitle());

                                btnRoomMacroMaterialRed.setTag("1");
                                btnRoomMacroMaterialRed.setBackgroundResource(R.drawable.red_button_background);

                                if (btnRoomMacroMaterial.getText().toString().equalsIgnoreCase("Blank")) {
                                    if (btnRoomMacro.getText().toString().equalsIgnoreCase("floor"))
                                        btnRoomMacroMaterial.setText("Carpet");
                                    else
                                        btnRoomMacroMaterial.setText("Drywall");
                                    btnRoomMacroMaterialRed.setTag("0");
                                    btnRoomMacroMaterialRed.setBackgroundResource(R.drawable.button_background);
                                }
                                btnRoomMacroMaterialRed.setText(btnRoomMacroMaterial.getText());
                            } else {
                                btnRoomMacroMaterialFinish.setTag(item.getTitle() + "");
                                btnRoomMacroMaterialFinish.setText(item.getTitle());

                                btnRoomMacroMaterialFinishRed.setTag("1");
                                btnRoomMacroMaterialFinishRed.setBackgroundResource(R.drawable.red_button_background);

                                if (btnRoomMacroMaterialFinish.getText().toString().equalsIgnoreCase("Blank")) {
                                    if (btnRoomMacro.getText().toString().equalsIgnoreCase("floor"))
                                        btnRoomMacroMaterialFinish.setText("Carpet");
                                    else
                                        btnRoomMacroMaterialFinish.setText("Drywall");
                                    btnRoomMacroMaterialFinishRed.setTag("0");
                                    btnRoomMacroMaterialFinishRed.setBackgroundResource(R.drawable.button_background);
                                }
                                btnRoomMacroMaterialFinishRed.setText(btnRoomMacroMaterialFinish.getText());
                            }
                            getalphaname();
                            return false;
                        }
                    });
                    popupMenu2.show();
                } else if (view.getId() == btnRoomMacroMaterialRed.getId() || view.getId() == btnRoomMacroMaterialFinishRed.getId()) {

                    clickonView("btnRoomMacroMaterialRed.btnRoomMacroMaterialFinishRed");

                    if (view.getId() == btnRoomMacroMaterialRed.getId()) {
                        if (btnRoomMacroMaterialRed.getTag().toString().equals("0")) {
                            btnRoomMacroMaterialRed.setTag("1");
                            btnRoomMacroMaterialRed.setBackgroundResource(R.drawable.red_button_background);
                        } else {
                            btnRoomMacroMaterialRed.setTag("0");
                            btnRoomMacroMaterialRed.setBackgroundResource(R.drawable.button_background);
                        }
                    } else {
                        if (btnRoomMacroMaterialFinishRed.getTag().toString().equals("0")) {
                            btnRoomMacroMaterialFinishRed.setTag("1");
                            btnRoomMacroMaterialFinishRed.setBackgroundResource(R.drawable.red_button_background);
                        } else {
                            btnRoomMacroMaterialFinishRed.setTag("0");
                            btnRoomMacroMaterialFinishRed.setBackgroundResource(R.drawable.button_background);
                        }
                    }
                } else if (view.getId() == btnRoomMacroPlus.getId()) {

                    clickonView("btnRoomMacroPlus");


                    PopupMenu popupMenu = new PopupMenu(mContext, btnRoomMacroPlus);
                    popupMenu.getMenu().add("+");
                    popupMenu.getMenu().add("-");
                    popupMenu.getMenu().add("&");
                    popupMenu.getMenu().add("M");
                    popupMenu.getMenu().add("I");

                    final ArrayList<String> arrayListPlus = new ArrayList<>();
                    arrayListPlus.add("Fan");
                    arrayListPlus.add("Fan/Light");
                    arrayListPlus.add("Light");
                    arrayListPlus.add("Crown");
                    arrayListPlus.add("Can Lights");
                    arrayListPlus.add("Light Bar");
                    arrayListPlus.add("Skylight");
                    arrayListPlus.add("Vent Cover");

                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(final MenuItem item) {

                            AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
                            alert.setTitle("Select Items");
                            final String items[] = new String[arrayListPlus.size()];
                            for (int i = 0; i < arrayListPlus.size(); i++)
                                items[i] = arrayListPlus.get(i);

                            alert.setMultiChoiceItems(items, null, new DialogInterface.OnMultiChoiceClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                                    arrayListPlus.set(i, b + "");
                                }
                            });

                            alert.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int j) {
                                    String sItem = item.getTitle().toString();
                                    for (int i = 0; i < arrayListPlus.size(); i++)
                                        if (arrayListPlus.get(i).equals("true"))
                                            sItem += " " + items[i];
                                    btnRoomMacroPlus.setText(sItem);
                                }
                            });
                            alert.show();

                            return false;
                        }
                    });

                    popupMenu.show();
                }
                getalphaname();
            }
        };
    }

    private void roomMacroNext() {

        btno.setVisibility(View.VISIBLE);//dmakchange
        btnocb.setVisibility(View.INVISIBLE);
        chkRoomMacroAffects.setVisibility(View.GONE);
        if (btnRoomMacro.getTag().toString().equalsIgnoreCase("Overview")) {
            btnRoomMacro.setText("Ceiling");
            btnRoomMacro.setTag("Ceiling");

            btnRoomMacroDamage.setVisibility(View.VISIBLE);
            btnRoomMacroMaterial.setVisibility(View.VISIBLE);
            btnRoomMacroMaterialRed.setVisibility(View.VISIBLE);
            btnRoomMacroMaterialFinish.setVisibility(View.VISIBLE);
            btnRoomMacroMaterialFinishRed.setVisibility(View.VISIBLE);
//            btnIntNewInsulation.setVisibility(View.VISIBLE);
//            btnIntNewQty.setVisibility(View.VISIBLE);

            chkRoomMacroAffects.setText("Affects Wall");
            chkRoomMacroAffects.setVisibility(View.VISIBLE);
        } else if (btnRoomMacro.getTag().toString().equalsIgnoreCase("Ceiling")) {
            btnRoomMacro.setText("Wall");
            btnRoomMacro.setTag("Wall");

            btnRoomMacroMaterial.setText("Drywall");
            btnRoomMacroMaterialFinish.setText("Drywall");

            chkRoomMacroAffects.setText("Affects Ceiling");
            chkRoomMacroAffects.setVisibility(View.VISIBLE);
        } else if (btnRoomMacro.getTag().toString().equalsIgnoreCase("Wall")) {
            btnRoomMacro.setText("Floor");
            btnRoomMacro.setTag("Floor");

            btnRoomMacroMaterial.setText("Carpet");
            btnRoomMacroMaterialFinish.setText("Carpet");
        }

        btnRoomMacroMaterial.setTag("Blank");
        btnRoomMacroMaterialFinish.setTag("Blank");

        btnocb.setTag("3");
        selectocb();
        strboc = "1";
        strboctype = "Overview";

        btnRoomMacroDamage.setText("Damage");
        btnRoomMacroDamage.setTag("0");
        btnRoomMacroDamageAmount.setVisibility(View.INVISIBLE);

        btnRoomMacroDamageRed.setVisibility(View.INVISIBLE);
        btnRoomMacroMaterialRed.setBackgroundResource(R.drawable.button_background);
        btnRoomMacroMaterialRed.setTag("0");
        btnRoomMacroMaterialRed.setText(btnRoomMacroMaterial.getText());
        btnRoomMacroMaterialFinishRed.setBackgroundResource(R.drawable.button_background);
        btnRoomMacroMaterialFinishRed.setTag("0");
        btnRoomMacroMaterialFinishRed.setText(btnRoomMacroMaterialFinish.getText());
//        btnIntNewInsulation.setText("Insulation");
//        btnIntNewQty.setText("Quantity");

        getalphaname();
    }

    private void hideallview() {

        btnareatogal.setVisibility(View.GONE);
        btnSubAreaTogal.setVisibility(View.GONE);

        rlriskphoto.setVisibility(View.GONE);
        rllayerphoto.setVisibility(View.GONE);
        rlshinglephoto.setVisibility(View.GONE);
        rlgutterphoto.setVisibility(View.GONE);
        rloverhangphoto.setVisibility(View.GONE);
        rlpitchphoto.setVisibility(View.GONE);
        llline.setVisibility(View.GONE);
        rlcostm.setVisibility(View.GONE);
        rlfeo.setVisibility(View.GONE);
        rlhail.setVisibility(View.GONE);
        rlTypesOfSidingPoto.setVisibility(View.GONE);

        rlmenuselection.setVisibility(View.VISIBLE);
        btnQue.setVisibility(View.VISIBLE);
        setQueCount();

        //  btnMic.setVisibility(View.VISIBLE);
        btnReport.setVisibility(View.VISIBLE);
        rltorch.setVisibility(View.VISIBLE);
        ibtnflash.setVisibility(View.VISIBLE);
        ibtnLive.setVisibility(View.VISIBLE);
        imgbtnsetting.setVisibility(View.VISIBLE);
        rlcontrolview.setVisibility(View.VISIBLE);
        btnocb.setVisibility(View.INVISIBLE);
        btnlastphoto.setVisibility(View.VISIBLE);

        if (btndamagetype.getTag().toString().equals("0")) {
            btntype.setVisibility(View.INVISIBLE);
        } else {
            btntype.setVisibility(View.VISIBLE);
        }
        if (btndamagetype.getText().toString().equals("Damage")) {
            btndamagetype.setText("Damage");
            btntype.setVisibility(View.INVISIBLE);
        } else {
            btntype.setVisibility(View.VISIBLE);
        }

        if (btnmaterial.getTag().toString().equals("0")) {
            btntype2.setVisibility(View.INVISIBLE);

        } else {
            btntype2.setVisibility(View.VISIBLE);
        }

        if (btnrei.getText().toString().trim().equals("I")) {
            llArea.setVisibility(View.VISIBLE);

            rlcontrolview.setVisibility(View.GONE);
            rliteriortype.setVisibility(View.VISIBLE);

            if (!btnarea.getText().toString().equalsIgnoreCase("area")) {
                btnInsulation.setVisibility(View.VISIBLE);
                btnQty.setVisibility(View.VISIBLE);
            }
        } else {
            rlcontrolview.setVisibility(View.VISIBLE);
            rliteriortype.setVisibility(View.GONE);
        }

        if (btnimgmacro.getText().toString().trim().equals("Macro")) {
            rliteriortype.setVisibility(View.GONE);

        } else {
            rliteriortype.setVisibility(View.GONE);
        }
    }

    private void showoption() {
        btno.setVisibility(View.VISIBLE);//dmakchange
        btnocb.setText("B");
        rlriskphoto.setVisibility(View.GONE);
        rlmenuselection.setVisibility(View.VISIBLE);
        btnQue.setVisibility(View.VISIBLE);
        setQueCount();

        //  btnMic.setVisibility(View.VISIBLE);
        btnReport.setVisibility(View.VISIBLE);
        imgbtnsetting.setVisibility(View.VISIBLE);
        rlcontrolview.setVisibility(View.VISIBLE);
        btnocb.setVisibility(View.INVISIBLE);
        btnlastphoto.setVisibility(View.VISIBLE);
        ibtnLive.setVisibility(View.VISIBLE);

        if (btndamagetype.getTag().toString().equals("0")) {
            btntype.setVisibility(View.INVISIBLE);
        } else {
            btntype.setVisibility(View.VISIBLE);
        }
        if (btndamagetype.getText().toString().equals("Damage")) {
            btndamagetype.setText("Damage");
            btntype.setVisibility(View.INVISIBLE);
        } else {
            btntype.setVisibility(View.VISIBLE);
        }

        if (btnmaterial.getTag().toString().equals("0")) {
            btntype2.setVisibility(View.INVISIBLE);

        } else {
            btntype2.setVisibility(View.VISIBLE);
        }

        btnnodamages.setVisibility(View.VISIBLE);
        btno.setVisibility(View.VISIBLE);//dmakchange
        btnocb.setVisibility(View.INVISIBLE);
        if (btnrei.getText().toString().trim().equals("I")) {
            llArea.setVisibility(View.VISIBLE);

            rlcontrolview.setVisibility(View.GONE);

            btntype2.setVisibility(View.GONE);
            btnareatogal.setVisibility(View.INVISIBLE);
            btnSubAreaTogal.setVisibility(View.INVISIBLE);
        } else {
            rliteriortype.setVisibility(View.GONE);
            rlcontrolview.setVisibility(View.VISIBLE);
        }

        if (btnarea.getText().toString().equalsIgnoreCase("ceiling") || btnarea.getText().toString().equalsIgnoreCase("wall") || btnarea.getText().toString().equalsIgnoreCase("floor")) {
            btnareatogal.setVisibility(View.INVISIBLE);
            if (!btnmaterial.getTag().toString().equalsIgnoreCase("0"))
                btnSubAreaTogal.setVisibility(View.VISIBLE);
        }
    }

    private void hidepitchoption() {

        rlpitchphoto.setVisibility(View.VISIBLE);
        llline.setVisibility(View.VISIBLE);
        btno.setVisibility(View.VISIBLE);//dmakchange

        rlmenuselection.setVisibility(View.INVISIBLE);
        llArea.setVisibility(View.INVISIBLE);
//        btnQue.setVisibility(View.INVISIBLE);
        btnMic.setVisibility(View.GONE);
        btnReport.setVisibility(View.GONE);
        imgbtnsetting.setVisibility(View.INVISIBLE);
        rlcontrolview.setVisibility(View.INVISIBLE);
        btnocb.setVisibility(View.INVISIBLE);
        btntype.setVisibility(View.INVISIBLE);
        btntype2.setVisibility(View.INVISIBLE);

        btnlastphoto.setVisibility(View.INVISIBLE);


    }

    private void hidecostmoption() {

        rlcostm.setVisibility(View.VISIBLE);
        btnocb.setVisibility(View.INVISIBLE);
        rlmenuselection.setVisibility(View.INVISIBLE);
        llArea.setVisibility(View.INVISIBLE);
        btnMic.setVisibility(View.GONE);
        btnReport.setVisibility(View.GONE);
        btnQue.setVisibility(View.INVISIBLE);
        txtQueCount.setVisibility(View.INVISIBLE);
        rlcontrolview.setVisibility(View.INVISIBLE);
        btnocb.setVisibility(View.INVISIBLE);
        btntype.setVisibility(View.INVISIBLE);
        btntype2.setVisibility(View.INVISIBLE);
        btnlastphoto.setVisibility(View.INVISIBLE);
    }

    private void hidefeooption() {
        btno.setVisibility(View.VISIBLE);//dmakchange
        btnocb.setVisibility(View.INVISIBLE);
        rlfeo.setVisibility(View.VISIBLE);
        rlTypesOfSidingPoto.setVisibility(View.GONE);
        rlmenuselection.setVisibility(View.INVISIBLE);
        llArea.setVisibility(View.INVISIBLE);
        btnMic.setVisibility(View.GONE);
        btnReport.setVisibility(View.GONE);
        rlcontrolview.setVisibility(View.INVISIBLE);
        btnocb.setVisibility(View.INVISIBLE);//dmak
        btntype.setVisibility(View.INVISIBLE);
        btntype2.setVisibility(View.INVISIBLE);
        btnlastphoto.setVisibility(View.INVISIBLE);
    }

    private void hidehailoption() {
        btnocb.setVisibility(View.INVISIBLE);

        //change

        btnareatogal.setVisibility(View.GONE);
        btnSubAreaTogal.setVisibility(View.GONE);
        btno.setVisibility(View.VISIBLE);//dmakchange
        rlhail.setVisibility(View.VISIBLE);
        rlmenuselection.setVisibility(View.INVISIBLE);
        llArea.setVisibility(View.INVISIBLE);
        btnMic.setVisibility(View.GONE);
        btnReport.setVisibility(View.GONE);
//        btnQue.setVisibility(View.INVISIBLE);
        rlcontrolview.setVisibility(View.INVISIBLE);
        btnocb.setVisibility(View.INVISIBLE);
        btntype.setVisibility(View.INVISIBLE);
        btntype2.setVisibility(View.INVISIBLE);
        btndamagetype1.setVisibility(View.GONE);
        btnhailnodamages.setVisibility(View.GONE);
        btnhailmaterialdamages.setVisibility(View.GONE);
        btnlastphoto.setVisibility(View.INVISIBLE);

        btnnodamagetype.setVisibility(View.GONE);



        if (btnimgmacrosub.getText().toString().trim().equals("hail damage close up on shingles")) {

            btnnodamagetype.setVisibility(View.VISIBLE);
            btnocb.setVisibility(View.INVISIBLE);
            btndamagetype1.setVisibility(View.VISIBLE);
            btnhailnodamages.setVisibility(View.VISIBLE);
            btnhailmaterialdamages.setVisibility(View.VISIBLE);
            btndamagetype1.setText("Hail Damage");
            strhaildamage=btndamagetype1.getText().toString();




            btnhailnodamages.setTag("2");
            btnhailnodamages.setBackgroundResource(R.drawable.red_button_background);
            btnhailnodamages.setText(btndamagetype1.getText().toString());

            btnmaterial1.setVisibility(View.GONE);


            btnhailmenu1.setVisibility(View.VISIBLE);
            //btnhailmenu1.setText("");
            btnhailmenu1.setText("Shingles");

            strhailmaterialdamage=btnhailmenu1.getText().toString();

            btnhailmaterialdamages.setTag("2");
            btnhailmaterialdamages.setBackgroundResource(R.drawable.red_button_background);
            btnhailmaterialdamages.setText(btnhailmenu1.getText().toString());



            btnhailskip.setText("Next Slope");



        }
       /* if(btnimgmacrosub.getText().toString().trim().equals("test sq Overview"))
        {
            btnmaterial1.setVisibility(View.VISIBLE);
            btnhailskip.setText("Next Slope");
        }
         if(btnimgmacrosub.getText().toString().trim().equals("test sq"))
        {
            btnmaterial1.setVisibility(View.VISIBLE);
            btnhailskip.setText("Next Slope");
        }*/
    }

    private void hideoption() {
        btno.setVisibility(View.VISIBLE);//dmakchange
        btnocb.setVisibility(View.INVISIBLE);
        rlriskphoto.setVisibility(View.VISIBLE);
        rlmenuselection.setVisibility(View.INVISIBLE);
        llArea.setVisibility(View.INVISIBLE);
        btnMic.setVisibility(View.GONE);
        btnReport.setVisibility(View.GONE);
//        btnQue.setVisibility(View.INVISIBLE);
//        rltorch.setVisibility(View.INVISIBLE);
//        ibtnflash.setVisibility(View.INVISIBLE);
        imgbtnsetting.setVisibility(View.INVISIBLE);
//        zoomseek.setVisibility(View.INVISIBLE);
        rlcontrolview.setVisibility(View.INVISIBLE);
        btnocb.setVisibility(View.INVISIBLE);
        btntype.setVisibility(View.INVISIBLE);
        btntype2.setVisibility(View.INVISIBLE);

        btnlastphoto.setVisibility(View.INVISIBLE);
        rlblankback.setVisibility(View.GONE);
    }

    private void hidelayeroption() {


        //   System.out.println("hidelayeroption Blank");
        rllayerphoto.setVisibility(View.VISIBLE);
        rlmenuselection.setVisibility(View.INVISIBLE);
        llArea.setVisibility(View.INVISIBLE);
        btnMic.setVisibility(View.GONE);
        btnReport.setVisibility(View.GONE);

        btno.setVisibility(View.VISIBLE);//dmakchange

//        btnQue.setVisibility(View.INVISIBLE);
//        rltorch.setVisibility(View.INVISIBLE);
//        ibtnflash.setVisibility(View.INVISIBLE);
        imgbtnsetting.setVisibility(View.INVISIBLE);
//        zoomseek.setVisibility(View.INVISIBLE);
        rlcontrolview.setVisibility(View.INVISIBLE);
        btnocb.setVisibility(View.INVISIBLE);
        btntype.setVisibility(View.INVISIBLE);
        btntype2.setVisibility(View.INVISIBLE);
        btnlastphoto.setVisibility(View.INVISIBLE);
        // System.out.println("data of pitch");
//        btnInteriorMenu.setText("Blank");
    }

    private void hideShingleoption() {
        btno.setVisibility(View.VISIBLE);//dmakchange
        rlshinglephoto.setVisibility(View.VISIBLE);
        rlmenuselection.setVisibility(View.INVISIBLE);
        llArea.setVisibility(View.INVISIBLE);
        btnMic.setVisibility(View.GONE);
        btnReport.setVisibility(View.GONE);
//        btnQue.setVisibility(View.INVISIBLE);
//        rltorch.setVisibility(View.INVISIBLE);
//        ibtnflash.setVisibility(View.INVISIBLE);
        imgbtnsetting.setVisibility(View.INVISIBLE);
//        zoomseek.setVisibility(View.INVISIBLE);
        rlcontrolview.setVisibility(View.INVISIBLE);
        btnocb.setVisibility(View.INVISIBLE);
        btntype.setVisibility(View.INVISIBLE);
        btntype2.setVisibility(View.INVISIBLE);

        btnlastphoto.setVisibility(View.INVISIBLE);

    }

    private void hideGutteroption() {
        btno.setVisibility(View.VISIBLE);//dmakchange
        rlgutterphoto.setVisibility(View.VISIBLE);
        rlmenuselection.setVisibility(View.INVISIBLE);
        llArea.setVisibility(View.INVISIBLE);
        btnMic.setVisibility(View.GONE);
        btnReport.setVisibility(View.GONE);
//        btnQue.setVisibility(View.INVISIBLE);
//        rltorch.setVisibility(View.INVISIBLE);
//        ibtnflash.setVisibility(View.INVISIBLE);
        imgbtnsetting.setVisibility(View.INVISIBLE);
//        zoomseek.setVisibility(View.INVISIBLE);
        rlcontrolview.setVisibility(View.INVISIBLE);
        btnocb.setVisibility(View.INVISIBLE);
        btntype.setVisibility(View.INVISIBLE);
        btntype2.setVisibility(View.INVISIBLE);

        btnlastphoto.setVisibility(View.INVISIBLE);

    }

    private void hideoverhangoption() {
        btno.setVisibility(View.VISIBLE);//dmakchange
        rloverhangphoto.setVisibility(View.VISIBLE);
        rlmenuselection.setVisibility(View.INVISIBLE);
        llArea.setVisibility(View.INVISIBLE);
        btnMic.setVisibility(View.GONE);
        btnReport.setVisibility(View.GONE);
//        btnQue.setVisibility(View.INVISIBLE);
//        rltorch.setVisibility(View.INVISIBLE);
//        ibtnflash.setVisibility(View.INVISIBLE);
        imgbtnsetting.setVisibility(View.INVISIBLE);
//        zoomseek.setVisibility(View.INVISIBLE);
        rlcontrolview.setVisibility(View.INVISIBLE);
        btnocb.setVisibility(View.INVISIBLE);
        btntype.setVisibility(View.INVISIBLE);
        btntype2.setVisibility(View.INVISIBLE);

        btnlastphoto.setVisibility(View.INVISIBLE);

    }

    private void hideTypesOfSidingOption() {

        rlTypesOfSidingPoto.setVisibility(View.VISIBLE);
        btno.setVisibility(View.VISIBLE);//dmakchange
        rloverhangphoto.setVisibility(View.INVISIBLE);
        rlmenuselection.setVisibility(View.INVISIBLE);
        llArea.setVisibility(View.INVISIBLE);
        btnMic.setVisibility(View.GONE);
        btnReport.setVisibility(View.GONE);
//        btnQue.setVisibility(View.INVISIBLE);
//        rltorch.setVisibility(View.INVISIBLE);
//        ibtnflash.setVisibility(View.INVISIBLE);
        imgbtnsetting.setVisibility(View.INVISIBLE);
//        zoomseek.setVisibility(View.INVISIBLE);
        rlcontrolview.setVisibility(View.INVISIBLE);
        btnocb.setVisibility(View.INVISIBLE);
        btntype.setVisibility(View.INVISIBLE);
        btntype2.setVisibility(View.INVISIBLE);

        btnlastphoto.setVisibility(View.INVISIBLE);

    }

    private void showTypeOfSidingMenu1Option() {
        PopupMenu popupMenu = new PopupMenu(HomeActivity.this, btnTypesOfSidingMenu1);
        for (int i = 1; i <= 12; i++)
            popupMenu.getMenu().add("" + i);
        popupMenu.getMenu().add("Blank");

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                btnTypesOfSidingMenu1.setText(item.getTitle().toString());
                getalphaname();
                return false;
            }
        });
        popupMenu.show();
    }

    private void showTypeOfSidingMenu2Option() {
        PopupMenu popupMenu = new PopupMenu(this, btnTypesOfSidingMenu2);
        popupMenu.getMenu().add("Aluminum");
        popupMenu.getMenu().add("Vinyl Double");
        popupMenu.getMenu().add("Vinyl Single");
        popupMenu.getMenu().add("Wood");
        popupMenu.getMenu().add("Vertical Vinyl");
        popupMenu.getMenu().add("Vertical Aluminum");
        popupMenu.getMenu().add("T1-11");
        popupMenu.getMenu().add("Brick");
        popupMenu.getMenu().add("Stucco");

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                btnTypesOfSidingMenu2.setText(item.getTitle().toString());
                getalphaname();
                return false;
            }
        });

        popupMenu.show();
    }

    private void showflashalert() {

        flashalert = new AlertDialog.Builder(HomeActivity.this)
                .setTitle("ClaimMate")
                .setMessage("Sorry, your device doesn't support flash light!")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void showupdatealert() {

        updateappalert = new AlertDialog.Builder(HomeActivity.this)
                .setCancelable(false)
                .setTitle("ClaimMate")
                .setMessage("Updated your app!")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }


    private void showphotonamealert(String strname) {

        new AlertDialog.Builder(HomeActivity.this)
                .setTitle("Save Picture Name")
                .setMessage(strname)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                        // continue with delete
                    }
                })
//                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        // do nothing
//                    }
//                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }

    private void setArrowBlank() {
        imgtop.setBackgroundResource(R.drawable.wticon);
        imgbottom.setBackgroundResource(R.drawable.wbicon);
        imgleft.setBackgroundResource(R.drawable.wlicon);
        imgright.setBackgroundResource(R.drawable.wricon);

        btnocb.setTag("3");
        selectocb();
        strboc = "1";
        strboctype = "Overview";
        selectslope = "";

        strfencegame = "";
    }

    private void setarrowicon(ImageView imgarrow, int iconid) {

        if(btnnodamages.getTag().equals("2"))
        {
            btnnodamages.performClick();
        }
        imgtop.setBackgroundResource(R.drawable.wticon);
        imgbottom.setBackgroundResource(R.drawable.wbicon);
        imgleft.setBackgroundResource(R.drawable.wlicon);
        imgright.setBackgroundResource(R.drawable.wricon);

        imgarrow.setBackgroundResource(iconid);
    }

    private void showmaterialoption(Boolean showalert)
    {

        if (btnrei.getText().toString().trim().equals("I")) {

//            popupMenu2.getMenu().add(Menu.NONE, 0, Menu.NONE, "Area");
//            popupMenu2.getMenu().add(Menu.NONE, 1, Menu.NONE, "Ceiling");
//            popupMenu2.getMenu().add(Menu.NONE, 2, Menu.NONE, "Wall");
//            popupMenu2.getMenu().add(Menu.NONE, 3, Menu.NONE, "Floor");

            PopupMenu popupMenu2 = new PopupMenu(this, findViewById(R.id.btnmaterial));

            if (btnarea.getTag().toString().equalsIgnoreCase("Area")) {

                //popupMenu2.getMenu().add(Menu.NONE, 0, Menu.NONE, "Blank");

            } else if (btnarea.getTag().toString().equalsIgnoreCase("Ceiling")) {

//                popupMenu2.getMenu().add(Menu.NONE, 0, Menu.NONE, "Blank");
//                popupMenu2.getMenu().add(Menu.NONE, 1, Menu.NONE, "Drywall");
//                popupMenu2.getMenu().add(Menu.NONE, 2, Menu.NONE, "Plaster");
//                popupMenu2.getMenu().add(Menu.NONE, 3, Menu.NONE, "tile");
//                popupMenu2.getMenu().add(Menu.NONE, 4, Menu.NONE, "Paint");
//                popupMenu2.getMenu().add(Menu.NONE, 5, Menu.NONE, "Crown molding");
//                popupMenu2.getMenu().add(Menu.NONE, 6, Menu.NONE, "Light");
//                popupMenu2.getMenu().add(Menu.NONE, 7, Menu.NONE, "Ceiling fan");
//                popupMenu2.getMenu().add(Menu.NONE, 8, Menu.NONE, "Wall paper");
//                popupMenu2.getMenu().add(Menu.NONE, 9, Menu.NONE, "Texture");
//                popupMenu2.getMenu().add(Menu.NONE, 10, Menu.NONE, "Trim");


//                popupMenu2.getMenu().add(Menu.NONE, 0, Menu.NONE, "Blank");
//                popupMenu2.getMenu().add(Menu.NONE, 1, Menu.NONE, "Ceiling fan");
//                popupMenu2.getMenu().add(Menu.NONE, 2, Menu.NONE, "Crown molding");
//                popupMenu2.getMenu().add(Menu.NONE, 3, Menu.NONE, "Drywall");
//                popupMenu2.getMenu().add(Menu.NONE, 4, Menu.NONE, "Light");
//                popupMenu2.getMenu().add(Menu.NONE, 5, Menu.NONE, "Paint");
//                popupMenu2.getMenu().add(Menu.NONE, 6, Menu.NONE, "Plaster");
//                popupMenu2.getMenu().add(Menu.NONE, 7, Menu.NONE, "Texture");
//                popupMenu2.getMenu().add(Menu.NONE, 8, Menu.NONE, "tile");
//                popupMenu2.getMenu().add(Menu.NONE, 9, Menu.NONE, "Trim");
//                popupMenu2.getMenu().add(Menu.NONE, 10, Menu.NONE, "Wall paper");
                //  popupMenu2.getMenu().add(Menu.NONE, 11, Menu.NONE, "Custom Text");

                value = 0;

                if (btnarea.getTag().toString().equals("Ceiling")) {
                    opendatabase();

                    SELECT_SQL = "SELECT * FROM tbl_w ORDER BY value ASC";
                    Cur = DB.rawQuery(SELECT_SQL, null);
                    if (Cur != null && Cur.getCount() > 0) {
                        Cur.moveToFirst();
                        do {

                            String strvalue = Cur.getString(Cur.getColumnIndex("value"));
                            if (strvalue.equalsIgnoreCase("Custom Text"))
                                continue;
                            Log.e("Value==>", "" + strvalue);


                            if(strvalue.trim().equals("Blank"))
                            {

                            }else {
                                popupMenu2.getMenu().add(Menu.NONE, value, Menu.NONE, strvalue);

                                value++;
                            }
                        }
                        while (Cur.moveToNext());

                        popupMenu2.getMenu().add(Menu.NONE, value, Menu.NONE, "Custom Text");
                    }
                    Cur.close();
                    DB.close();

                    /*
                    popupMenu2.getMenu().add(Menu.NONE, 0, Menu.NONE, "Blank");
                    popupMenu2.getMenu().add(Menu.NONE, 1, Menu.NONE, "Shingles");
                    popupMenu2.getMenu().add(Menu.NONE, 2, Menu.NONE, "Vent");
                    popupMenu2.getMenu().add(Menu.NONE, 3, Menu.NONE, "Furnace cap");
                    popupMenu2.getMenu().add(Menu.NONE, 4, Menu.NONE, "Drip edge");
                    popupMenu2.getMenu().add(Menu.NONE, 5, Menu.NONE, "Metal");
                    popupMenu2.getMenu().add(Menu.NONE, 6, Menu.NONE, "test sq");
                    popupMenu2.getMenu().add(Menu.NONE, 7, Menu.NONE, "Custom Text");*/

                }


            } else if (btnarea.getTag().toString().equalsIgnoreCase("Wall")) {


//                popupMenu2.getMenu().add(Menu.NONE, 0, Menu.NONE, "Blank");
//                popupMenu2.getMenu().add(Menu.NONE, 1, Menu.NONE, "Drywall");
//                popupMenu2.getMenu().add(Menu.NONE, 2, Menu.NONE, "Plaster");
//                popupMenu2.getMenu().add(Menu.NONE, 3, Menu.NONE, "tile");
//                popupMenu2.getMenu().add(Menu.NONE, 4, Menu.NONE, "Paint");
//                popupMenu2.getMenu().add(Menu.NONE, 5, Menu.NONE, "Crown molding");
//                popupMenu2.getMenu().add(Menu.NONE, 6, Menu.NONE, "Light");
//                popupMenu2.getMenu().add(Menu.NONE, 7, Menu.NONE, "Wall paper");
//                popupMenu2.getMenu().add(Menu.NONE, 8, Menu.NONE, "Texture");
//                popupMenu2.getMenu().add(Menu.NONE, 9, Menu.NONE, "Casing");
//                popupMenu2.getMenu().add(Menu.NONE, 10, Menu.NONE, "Baseboards");
//                popupMenu2.getMenu().add(Menu.NONE, 11, Menu.NONE, "Door");
//                popupMenu2.getMenu().add(Menu.NONE, 12, Menu.NONE, "Window");
//                popupMenu2.getMenu().add(Menu.NONE, 13, Menu.NONE, "Cabinet");
//                popupMenu2.getMenu().add(Menu.NONE, 14, Menu.NONE, "Countertop");
//                popupMenu2.getMenu().add(Menu.NONE, 15, Menu.NONE, "Tile");
//                popupMenu2.getMenu().add(Menu.NONE, 16, Menu.NONE, "Insulation");


//                popupMenu2.getMenu().add(Menu.NONE, 0, Menu.NONE, "Blank");
//                popupMenu2.getMenu().add(Menu.NONE, 1, Menu.NONE, "Baseboards");
//                popupMenu2.getMenu().add(Menu.NONE, 2, Menu.NONE, "Casing");
//                popupMenu2.getMenu().add(Menu.NONE, 3, Menu.NONE, "Cabinet");
//                popupMenu2.getMenu().add(Menu.NONE, 4, Menu.NONE, "Countertop");
//                popupMenu2.getMenu().add(Menu.NONE, 5, Menu.NONE, "Crown molding");
//                popupMenu2.getMenu().add(Menu.NONE, 6, Menu.NONE, "Drywall");
//                popupMenu2.getMenu().add(Menu.NONE, 7, Menu.NONE, "Door");
//                popupMenu2.getMenu().add(Menu.NONE, 8, Menu.NONE, "Insulation");
//                popupMenu2.getMenu().add(Menu.NONE, 9, Menu.NONE, "Light");
//                popupMenu2.getMenu().add(Menu.NONE, 10, Menu.NONE, "Paint");
//                popupMenu2.getMenu().add(Menu.NONE, 11, Menu.NONE, "Plaster");
//                popupMenu2.getMenu().add(Menu.NONE, 12, Menu.NONE, "Texture");
//                popupMenu2.getMenu().add(Menu.NONE, 13, Menu.NONE, "Tile");
//                popupMenu2.getMenu().add(Menu.NONE, 14, Menu.NONE, "Tile");
//                popupMenu2.getMenu().add(Menu.NONE, 15, Menu.NONE, "Wall paper");
//                popupMenu2.getMenu().add(Menu.NONE, 16, Menu.NONE, "Window");
//                popupMenu2.getMenu().add(Menu.NONE, 17, Menu.NONE, "Custom Text");


                value = 0;

                if (btnarea.getTag().toString().equals("Wall")) {
                    opendatabase();

                    SELECT_SQL = "SELECT * FROM tbl_c ORDER BY value ASC";
                    Cur = DB.rawQuery(SELECT_SQL, null);
                    if (Cur != null && Cur.getCount() > 0) {
                        Cur.moveToFirst();
                        do {

                            String strvalue = Cur.getString(Cur.getColumnIndex("value"));
                            if (strvalue.equalsIgnoreCase("Custom Text"))
                                continue;
                            Log.e("Value==>", "" + strvalue);
                            if(strvalue.trim().equals("Blank"))
                            {

                            }else {

                                popupMenu2.getMenu().add(Menu.NONE, value, Menu.NONE, strvalue);

                                value++;
                            }
                        }
                        while (Cur.moveToNext());

                        popupMenu2.getMenu().add(Menu.NONE, value, Menu.NONE, "Custom Text");
                    }
                    Cur.close();
                    DB.close();

                    /*
                    popupMenu2.getMenu().add(Menu.NONE, 0, Menu.NONE, "Blank");
                    popupMenu2.getMenu().add(Menu.NONE, 1, Menu.NONE, "Shingles");
                    popupMenu2.getMenu().add(Menu.NONE, 2, Menu.NONE, "Vent");
                    popupMenu2.getMenu().add(Menu.NONE, 3, Menu.NONE, "Furnace cap");
                    popupMenu2.getMenu().add(Menu.NONE, 4, Menu.NONE, "Drip edge");
                    popupMenu2.getMenu().add(Menu.NONE, 5, Menu.NONE, "Metal");
                    popupMenu2.getMenu().add(Menu.NONE, 6, Menu.NONE, "test sq");
                    popupMenu2.getMenu().add(Menu.NONE, 7, Menu.NONE, "Custom Text");*/

                }


            } else if (btnarea.getTag().toString().equals("Floor")) {

//                popupMenu2.getMenu().add(Menu.NONE, 0, Menu.NONE, "Blank");
//                popupMenu2.getMenu().add(Menu.NONE, 1, Menu.NONE, "Tile");
//                popupMenu2.getMenu().add(Menu.NONE, 2, Menu.NONE, "Vinyl");
//                popupMenu2.getMenu().add(Menu.NONE, 3, Menu.NONE, "Wood");
//                popupMenu2.getMenu().add(Menu.NONE, 4, Menu.NONE, "Laminate");
//                popupMenu2.getMenu().add(Menu.NONE, 5, Menu.NONE, "Subfloor");
//                popupMenu2.getMenu().add(Menu.NONE, 6, Menu.NONE, "Carpet");
//                popupMenu2.getMenu().add(Menu.NONE, 7, Menu.NONE, "Pad");

//                popupMenu2.getMenu().add(Menu.NONE, 0, Menu.NONE, "Blank");
//                popupMenu2.getMenu().add(Menu.NONE, 1, Menu.NONE, "Carpet");
//                popupMenu2.getMenu().add(Menu.NONE, 2, Menu.NONE, "Laminate");
//                popupMenu2.getMenu().add(Menu.NONE, 3, Menu.NONE, "Pad");
//                popupMenu2.getMenu().add(Menu.NONE, 4, Menu.NONE, "Subfloor");
//                popupMenu2.getMenu().add(Menu.NONE, 5, Menu.NONE, "Tile");
//                popupMenu2.getMenu().add(Menu.NONE, 6, Menu.NONE, "Vinyl");
//                popupMenu2.getMenu().add(Menu.NONE, 7, Menu.NONE, "Wood");
//                popupMenu2.getMenu().add(Menu.NONE, 8, Menu.NONE, "Custom Text");
                value = 0;

                if (btnarea.getTag().toString().equals("Floor")) {
                    opendatabase();

                    SELECT_SQL = "SELECT * FROM tbl_f ORDER BY value ASC";
                    Cur = DB.rawQuery(SELECT_SQL, null);
                    if (Cur != null && Cur.getCount() > 0) {
                        Cur.moveToFirst();
                        do {

                            String strvalue = Cur.getString(Cur.getColumnIndex("value"));
                            if (strvalue.equalsIgnoreCase("Custom Text"))
                                continue;
                            Log.e("Value==>", "" + strvalue);
                            if(strvalue.trim().equals("Blank"))
                            {

                            }else {

                                popupMenu2.getMenu().add(Menu.NONE, value, Menu.NONE, strvalue);

                                value++;
                            }
                        }
                        while (Cur.moveToNext());

                        popupMenu2.getMenu().add(Menu.NONE, value, Menu.NONE, "Custom Text");
                    }
                    Cur.close();
                    DB.close();

                    /*
                    popupMenu2.getMenu().add(Menu.NONE, 0, Menu.NONE, "Blank");
                    popupMenu2.getMenu().add(Menu.NONE, 1, Menu.NONE, "Shingles");
                    popupMenu2.getMenu().add(Menu.NONE, 2, Menu.NONE, "Vent");
                    popupMenu2.getMenu().add(Menu.NONE, 3, Menu.NONE, "Furnace cap");
                    popupMenu2.getMenu().add(Menu.NONE, 4, Menu.NONE, "Drip edge");
                    popupMenu2.getMenu().add(Menu.NONE, 5, Menu.NONE, "Metal");
                    popupMenu2.getMenu().add(Menu.NONE, 6, Menu.NONE, "test sq");
                    popupMenu2.getMenu().add(Menu.NONE, 7, Menu.NONE, "Custom Text");*/

                }

            }

            popupMenu2.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {

                    selectmaterialmenu_I(item,false);
                    return false;
                }
            });

            if (btnrei.getText().toString().equalsIgnoreCase("i") && btnareatogal.getTag().toString().equals("2"))
            {
                if(showalert)
                {
                    popupMenu2.show();

                }
                else
                {

                    for (int i=0;i<popupMenu2.getMenu().size();i++)
                    {

                        if(btnrei.getText().equals("E"))
                        {
                            if(popupMenu2.getMenu().getItem(i).getTitle().equals("Siding"))
                            {
                                selectmaterialmenu_I(popupMenu2.getMenu().getItem(i),false);

                                break;
                            }
                        }
                        else if(btnrei.getText().equals("R"))
                        {
                            if(popupMenu2.getMenu().getItem(i).getTitle().equals("Shingles"))
                            {
                                selectmaterialmenu_I(popupMenu2.getMenu().getItem(i),false);

                                break;
                            }
                        }

                    }

                }
            }




        } else {
            value = 0;

            PopupMenu popupMenu2 = new PopupMenu(this, findViewById(R.id.btnmaterial));


            if (btnrei.getText().toString().trim().equals("R")) {
                opendatabase();

                SELECT_SQL = "SELECT * FROM tbl_r ORDER BY value ASC";
                Cur = DB.rawQuery(SELECT_SQL, null);
                if (Cur != null && Cur.getCount() > 0) {
                    Cur.moveToFirst();
                    do {

                        String strvalue = Cur.getString(Cur.getColumnIndex("value"));
//                        if (strvalue.equalsIgnoreCase("Custom Text"))
//                            continue;
                        Log.e("Valueelse==>", "" + strvalue);
                        if(strvalue.equalsIgnoreCase("Blank") || strvalue.equalsIgnoreCase("Custom Text") )
                        {

                        }else {

                            popupMenu2.getMenu().add(Menu.NONE, value, Menu.NONE, strvalue);

                            value++;
                        }
                    }
                    while (Cur.moveToNext());
                    popupMenu2.getMenu().add(Menu.NONE, value, Menu.NONE, "Flat Roof");
                    value++;
                    popupMenu2.getMenu().add(Menu.NONE, value, Menu.NONE, "Material");
                    value++;
                    popupMenu2.getMenu().add(Menu.NONE, value, Menu.NONE, "Custom Text");
                }
                Cur.close();
                DB.close();

                    /*
                    popupMenu2.getMenu().add(Menu.NONE, 0, Menu.NONE, "Blank");
                    popupMenu2.getMenu().add(Menu.NONE, 1, Menu.NONE, "Shingles");
                    popupMenu2.getMenu().add(Menu.NONE, 2, Menu.NONE, "Vent");
                    popupMenu2.getMenu().add(Menu.NONE, 3, Menu.NONE, "Furnace cap");
                    popupMenu2.getMenu().add(Menu.NONE, 4, Menu.NONE, "Drip edge");
                    popupMenu2.getMenu().add(Menu.NONE, 5, Menu.NONE, "Metal");
                    popupMenu2.getMenu().add(Menu.NONE, 6, Menu.NONE, "test sq");
                    popupMenu2.getMenu().add(Menu.NONE, 7, Menu.NONE, "Custom Text");*/

            } else if (btnrei.getText().toString().trim().equals("E")) {

                opendatabase();

                SELECT_SQL = "SELECT * FROM tbl_e ORDER BY value ASC";
                Cur = DB.rawQuery(SELECT_SQL, null);
                if (Cur != null && Cur.getCount() > 0) {
                    Cur.moveToFirst();
                    do {

                        String strvalue = Cur.getString(Cur.getColumnIndex("value"));
                        if (strvalue.equalsIgnoreCase("Custom Text"))
                            continue;
                        Log.e("Value==>", "" + strvalue);
                        if(strvalue.equalsIgnoreCase("Blank"))
                        {

                        }else {

                            popupMenu2.getMenu().add(Menu.NONE, value, Menu.NONE, strvalue);

                            value++;
                        }
                    }
                    while (Cur.moveToNext());
                    popupMenu2.getMenu().add(Menu.NONE, value, Menu.NONE, "Material");
                    value++;
                    popupMenu2.getMenu().add(Menu.NONE, value, Menu.NONE, "Custom Text");
                }
                Cur.close();
                DB.close();

                    /*
                    popupMenu2.getMenu().add(Menu.NONE, 0, Menu.NONE, "Blank");
                    popupMenu2.getMenu().add(Menu.NONE, 1, Menu.NONE, "Siding");
                    popupMenu2.getMenu().add(Menu.NONE, 2, Menu.NONE, "Soffit");
                    popupMenu2.getMenu().add(Menu.NONE, 3, Menu.NONE, "Fascia");
                    popupMenu2.getMenu().add(Menu.NONE, 4, Menu.NONE, "Window");
                    popupMenu2.getMenu().add(Menu.NONE, 5, Menu.NONE, "Window Wrap");
                    popupMenu2.getMenu().add(Menu.NONE, 6, Menu.NONE, "AC Unit");
                    popupMenu2.getMenu().add(Menu.NONE, 7, Menu.NONE, "Paint");
                    popupMenu2.getMenu().add(Menu.NONE, 8, Menu.NONE, "Gutter");
                    popupMenu2.getMenu().add(Menu.NONE, 9, Menu.NONE, "DownSpout");
                    popupMenu2.getMenu().add(Menu.NONE, 10, Menu.NONE, "Custom Text");*/


            } else if (btnrei.getText().toString().trim().equals("I")) {

                opendatabase();

                SELECT_SQL = "SELECT * FROM tbl_i ORDER BY value ASC";
                Cur = DB.rawQuery(SELECT_SQL, null);
                if (Cur != null && Cur.getCount() > 0) {
                    Cur.moveToFirst();
                    do {

                        String strvalue = Cur.getString(Cur.getColumnIndex("value"));
                        if (strvalue.equalsIgnoreCase("Custom Text"))
                            continue;
                        Log.e("Value==>", "" + strvalue);
                        if(strvalue.equalsIgnoreCase("Blank"))
                        {

                        }else {

                            popupMenu2.getMenu().add(Menu.NONE, value, Menu.NONE, strvalue);

                            value++;
                        }
                    }
                    while (Cur.moveToNext());
                    popupMenu2.getMenu().add(Menu.NONE, value, Menu.NONE, "Material");
                    value++;
                    popupMenu2.getMenu().add(Menu.NONE, value, Menu.NONE, "Custom Text");
                }
                Cur.close();
                DB.close();

                    /*
                    popupMenu2.getMenu().add(Menu.NONE, 0, Menu.NONE, "Blank");
                    popupMenu2.getMenu().add(Menu.NONE, 1, Menu.NONE, "Drywall Ceiling");
                    popupMenu2.getMenu().add(Menu.NONE, 2, Menu.NONE, "Drywall Wall");
                    popupMenu2.getMenu().add(Menu.NONE, 3, Menu.NONE, "Paint");
                    popupMenu2.getMenu().add(Menu.NONE, 4, Menu.NONE, "Trim");
                    popupMenu2.getMenu().add(Menu.NONE, 5, Menu.NONE, "Window");
                    popupMenu2.getMenu().add(Menu.NONE, 6, Menu.NONE, "Cabinet");
                    popupMenu2.getMenu().add(Menu.NONE, 7, Menu.NONE, "Countertop");
                    popupMenu2.getMenu().add(Menu.NONE, 8, Menu.NONE, "Carpet");
                    popupMenu2.getMenu().add(Menu.NONE, 9, Menu.NONE, "Flooring");
                    popupMenu2.getMenu().add(Menu.NONE, 10, Menu.NONE, "Tile");
                    popupMenu2.getMenu().add(Menu.NONE, 11, Menu.NONE, "Vinyl");
                    popupMenu2.getMenu().add(Menu.NONE, 12, Menu.NONE, "Custom Text");*/

            }

            popupMenu2.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                @Override
                public boolean onMenuItemClick(MenuItem arg0) {

                    selectmaterialmenu_Else(arg0,false);
                    return false;
                }
            });

                if(showalert)
                {
                    popupMenu2.show();

                }
                else
                {
                    for (int i=0;i<popupMenu2.getMenu().size();i++)
                    {
//                        if(popupMenu2.getMenu().getItem(i).getTitle().equals("Shingles"))
//                        {
//                            selectmaterialmenu_Else(popupMenu2.getMenu().getItem(i),true);
//
//                            break;
//                        }


                        if(btnrei.getText().equals("E"))
                        {
                            if(popupMenu2.getMenu().getItem(i).getTitle().equals("Siding"))
                            {
                                selectmaterialmenu_Else(popupMenu2.getMenu().getItem(i),false);

                                break;
                            }
                        }
                        else if(btnrei.getText().equals("R"))
                        {
                            if(popupMenu2.getMenu().getItem(i).getTitle().equals("Shingles"))
                            {
                                selectmaterialmenu_Else(popupMenu2.getMenu().getItem(i),false);

                                break;
                            }
                        }

                    }

                }



        }
    }

    private void selectmaterialmenu_Else(MenuItem arg0, boolean isdefault) {



        btnmaterial.setTag(arg0.getItemId() + "");
        btnmaterial.setText(arg0.getTitle());

        btnnodamages.setTag("1");
        btnnodamages.setBackgroundResource(R.drawable.button_background);

//        btno.setTag("1");
//        btno.setBackgroundResource(R.drawable.button_background);


        if (btnrei.getText().toString().trim().equals("R") && arg0.getTitle().toString().equals("Custom Text")) {

            showcostmtextalert("2");

        }
        if (btnrei.getText().toString().trim().equals("E") && arg0.getTitle().toString().equals("Custom Text")) {

            showcostmtextalert("2");

        }
        if (btnrei.getText().toString().trim().equals("I") && arg0.getTitle().toString().equals("Custom Text")) {

            showcostmtextalert("2");

        } else {
            if (btnrei.getText().toString().trim().equals("R")) {
                btntype2.setText(arg0.getTitle());

                if (btnmaterial.getText().toString().trim().equals("test sq")) {
                    btntype.setVisibility(View.INVISIBLE);
                    btntype2.setVisibility(View.INVISIBLE);

                    btnmatrialsubmenu.setVisibility(View.VISIBLE);
                } else {

                    btnmatrialsubmenu.setVisibility(View.GONE);

                    if (btndamagetype.getTag().toString().equals("0")) {
                        btntype.setVisibility(View.INVISIBLE);

                    } else {
                        btntype.setVisibility(View.VISIBLE);
                    }

                    if (btndamagetype.getText().toString().equals("Damage")) {
                        btndamagetype.setText("Damage");
                        btntype.setVisibility(View.INVISIBLE);
                    } else {
                        btntype.setVisibility(View.VISIBLE);
                    }

                    if(btnmaterial.getText().toString().equals("Material"))
                    {
                        btnmaterial.setText("Blank");
                    }
                    if (btnmaterial.getText().toString().equals("Blank")) {
                        btnmaterial.setText("Material");
                        btntype2.setVisibility(View.INVISIBLE);

                    } else {
                        btntype2.setVisibility(View.VISIBLE);
                    }

                }

            }

            if (btnrei.getText().toString().trim().equals("E")) {
                btntype2.setText(arg0.getTitle());

                if (btnmaterial.getText().toString().trim().equals("test sq")) {
                    btntype.setVisibility(View.INVISIBLE);
                    btntype2.setVisibility(View.INVISIBLE);

                    btnmatrialsubmenu.setVisibility(View.VISIBLE);
                } else {

                    btnmatrialsubmenu.setVisibility(View.GONE);

                    if (btndamagetype.getTag().toString().equals("0")) {
                        btntype.setVisibility(View.INVISIBLE);

                    } else {
                        btntype.setVisibility(View.VISIBLE);
                    }

                    if (btndamagetype.getText().toString().equals("Damage")) {
                        btndamagetype.setText("Damage");
                        btntype.setVisibility(View.INVISIBLE);
                    } else {
                        btntype.setVisibility(View.VISIBLE);
                    }

                    if(btnmaterial.getText().toString().equals("Material"))
                    {
                        btnmaterial.setText("Blank");
                    }

                    if (btnmaterial.getText().toString().equals("Blank")) {
                        btnmaterial.setText("Material");
                        btntype2.setVisibility(View.INVISIBLE);

                    } else {
                        btntype2.setVisibility(View.VISIBLE);
                    }
                }
            }

            if (btnrei.getText().toString().trim().equals("I")) {
                btntype2.setText(arg0.getTitle());

                if (btnmaterial.getText().toString().trim().equals("test sq")) {
                    btntype.setVisibility(View.INVISIBLE);
                    btntype2.setVisibility(View.INVISIBLE);

                    btnmatrialsubmenu.setVisibility(View.VISIBLE);
                } else {

                    btnmatrialsubmenu.setVisibility(View.GONE);

                    if (btndamagetype.getTag().toString().equals("0")) {
                        btntype.setVisibility(View.INVISIBLE);

                    } else {
                        btntype.setVisibility(View.VISIBLE);
                    }

                    if (btndamagetype.getText().toString().equals("Damage")) {
                        btndamagetype.setText("Damage");
                        btntype.setVisibility(View.INVISIBLE);
                    } else {
                        btntype.setVisibility(View.VISIBLE);
                    }

                    if(btnmaterial.getText().toString().equals("Material"))
                    {
                        btnmaterial.setText("Blank");
                    }
                    if (btnmaterial.getText().toString().equals("Blank")) {
                        btnmaterial.setText("Material");
                        btntype2.setVisibility(View.INVISIBLE);

                    } else {
                        btntype2.setVisibility(View.VISIBLE);
                    }
                }
            }
        }


        Log.e("selectgettitle", "==>" + arg0.getTitle());


//        getalphaname();

        if(isdefault)
        {
            btntype2.setTag("1");
            btntype2.setBackgroundResource(R.drawable.button_background);

        }
        else
        {
            btntype2.setTag("2");
            btntype2.setBackgroundResource(R.drawable.red_button_background);

        }

        getalphaname();
    }

    private void selectmaterialmenu_I(MenuItem item, Boolean isdefault)
    {


//                    if (item.getTitle().toString().equalsIgnoreCase("Custom Text")) {
//                        showcostmtextalert("4");
//                    }
        if (btnarea.getTag().toString().equals("Floor") && item.getTitle().toString().equals("Custom Text")) {

            showcostmtextalert("2");
        }
        if (btnarea.getTag().toString().equals("Wall") && item.getTitle().toString().equals("Custom Text")) {

            showcostmtextalert("2");
        }
        if (btnarea.getTag().toString().equals("Ceiling") && item.getTitle().toString().equals("Custom Text")) {

            showcostmtextalert("2");
        }


        btnmaterial.setTag(item.getItemId() + "");
        btnmaterial.setText(item.getTitle());
        if(item.getTitle().equals("Material"))
        {
            btnmaterial.setText("Blank");
        }

        if (btnmaterial.getText().toString().equalsIgnoreCase("Blank")) {
            if (btnarea.getText().toString().equalsIgnoreCase("floor"))
                btnmaterial.setText("Carpet");
            else
                btnmaterial.setText("Drywall");
            btnSubAreaTogal.setVisibility(View.INVISIBLE);
        } else {
            btnSubAreaTogal.setVisibility(View.VISIBLE);
            btnSubAreaTogal.setBackgroundResource(R.drawable.red_button_background);
            btnSubAreaTogal.setText(btnmaterial.getText().toString());
            btnSubAreaTogal.setTag("2");
        }

        getalphaname();
    }


    private void showmaterialoption1() {

        if (btnrei.getText().toString().trim().equals("I")) {

//            popupMenu2.getMenu().add(Menu.NONE, 0, Menu.NONE, "Area");
//            popupMenu2.getMenu().add(Menu.NONE, 1, Menu.NONE, "Ceiling");
//            popupMenu2.getMenu().add(Menu.NONE, 2, Menu.NONE, "Wall");
//            popupMenu2.getMenu().add(Menu.NONE, 3, Menu.NONE, "Floor");

            PopupMenu popupMenu2 = new PopupMenu(this, findViewById(R.id.btnmaterial1));

            if (btnarea.getTag().toString().equalsIgnoreCase("Area")) {

                //popupMenu2.getMenu().add(Menu.NONE, 0, Menu.NONE, "Blank");

            } else if (btnarea.getTag().toString().equalsIgnoreCase("Ceiling")) {

//                popupMenu2.getMenu().add(Menu.NONE, 0, Menu.NONE, "Blank");
//                popupMenu2.getMenu().add(Menu.NONE, 1, Menu.NONE, "Drywall");
//                popupMenu2.getMenu().add(Menu.NONE, 2, Menu.NONE, "Plaster");
//                popupMenu2.getMenu().add(Menu.NONE, 3, Menu.NONE, "tile");
//                popupMenu2.getMenu().add(Menu.NONE, 4, Menu.NONE, "Paint");
//                popupMenu2.getMenu().add(Menu.NONE, 5, Menu.NONE, "Crown molding");
//                popupMenu2.getMenu().add(Menu.NONE, 6, Menu.NONE, "Light");
//                popupMenu2.getMenu().add(Menu.NONE, 7, Menu.NONE, "Ceiling fan");
//                popupMenu2.getMenu().add(Menu.NONE, 8, Menu.NONE, "Wall paper");
//                popupMenu2.getMenu().add(Menu.NONE, 9, Menu.NONE, "Texture");
//                popupMenu2.getMenu().add(Menu.NONE, 10, Menu.NONE, "Trim");


//                popupMenu2.getMenu().add(Menu.NONE, 0, Menu.NONE, "Blank");
//                popupMenu2.getMenu().add(Menu.NONE, 1, Menu.NONE, "Ceiling fan");
//                popupMenu2.getMenu().add(Menu.NONE, 2, Menu.NONE, "Crown molding");
//                popupMenu2.getMenu().add(Menu.NONE, 3, Menu.NONE, "Drywall");
//                popupMenu2.getMenu().add(Menu.NONE, 4, Menu.NONE, "Light");
//                popupMenu2.getMenu().add(Menu.NONE, 5, Menu.NONE, "Paint");
//                popupMenu2.getMenu().add(Menu.NONE, 6, Menu.NONE, "Plaster");
//                popupMenu2.getMenu().add(Menu.NONE, 7, Menu.NONE, "Texture");
//                popupMenu2.getMenu().add(Menu.NONE, 8, Menu.NONE, "tile");
//                popupMenu2.getMenu().add(Menu.NONE, 9, Menu.NONE, "Trim");
//                popupMenu2.getMenu().add(Menu.NONE, 10, Menu.NONE, "Wall paper");
                //  popupMenu2.getMenu().add(Menu.NONE, 11, Menu.NONE, "Custom Text");

                value = 0;

                if (btnarea.getTag().toString().equals("Ceiling")) {
                    opendatabase();

                    SELECT_SQL = "SELECT * FROM tbl_w ORDER BY value ASC";
                    Cur = DB.rawQuery(SELECT_SQL, null);
                    if (Cur != null && Cur.getCount() > 0) {
                        Cur.moveToFirst();
                        do {

                            String strvalue = Cur.getString(Cur.getColumnIndex("value"));
                            if (strvalue.equalsIgnoreCase("Custom Text"))
                                continue;
                            Log.e("Value==>", "" + strvalue);


                            if(strvalue.trim().equals("Blank"))
                            {

                            }else {
                                popupMenu2.getMenu().add(Menu.NONE, value, Menu.NONE, strvalue);

                                value++;
                            }
                        }
                        while (Cur.moveToNext());

                        popupMenu2.getMenu().add(Menu.NONE, value, Menu.NONE, "Custom Text");
                    }
                    Cur.close();
                    DB.close();

                    /*
                    popupMenu2.getMenu().add(Menu.NONE, 0, Menu.NONE, "Blank");
                    popupMenu2.getMenu().add(Menu.NONE, 1, Menu.NONE, "Shingles");
                    popupMenu2.getMenu().add(Menu.NONE, 2, Menu.NONE, "Vent");
                    popupMenu2.getMenu().add(Menu.NONE, 3, Menu.NONE, "Furnace cap");
                    popupMenu2.getMenu().add(Menu.NONE, 4, Menu.NONE, "Drip edge");
                    popupMenu2.getMenu().add(Menu.NONE, 5, Menu.NONE, "Metal");
                    popupMenu2.getMenu().add(Menu.NONE, 6, Menu.NONE, "test sq");
                    popupMenu2.getMenu().add(Menu.NONE, 7, Menu.NONE, "Custom Text");*/

                }


            } else if (btnarea.getTag().toString().equalsIgnoreCase("Wall")) {


//                popupMenu2.getMenu().add(Menu.NONE, 0, Menu.NONE, "Blank");
//                popupMenu2.getMenu().add(Menu.NONE, 1, Menu.NONE, "Drywall");
//                popupMenu2.getMenu().add(Menu.NONE, 2, Menu.NONE, "Plaster");
//                popupMenu2.getMenu().add(Menu.NONE, 3, Menu.NONE, "tile");
//                popupMenu2.getMenu().add(Menu.NONE, 4, Menu.NONE, "Paint");
//                popupMenu2.getMenu().add(Menu.NONE, 5, Menu.NONE, "Crown molding");
//                popupMenu2.getMenu().add(Menu.NONE, 6, Menu.NONE, "Light");
//                popupMenu2.getMenu().add(Menu.NONE, 7, Menu.NONE, "Wall paper");
//                popupMenu2.getMenu().add(Menu.NONE, 8, Menu.NONE, "Texture");
//                popupMenu2.getMenu().add(Menu.NONE, 9, Menu.NONE, "Casing");
//                popupMenu2.getMenu().add(Menu.NONE, 10, Menu.NONE, "Baseboards");
//                popupMenu2.getMenu().add(Menu.NONE, 11, Menu.NONE, "Door");
//                popupMenu2.getMenu().add(Menu.NONE, 12, Menu.NONE, "Window");
//                popupMenu2.getMenu().add(Menu.NONE, 13, Menu.NONE, "Cabinet");
//                popupMenu2.getMenu().add(Menu.NONE, 14, Menu.NONE, "Countertop");
//                popupMenu2.getMenu().add(Menu.NONE, 15, Menu.NONE, "Tile");
//                popupMenu2.getMenu().add(Menu.NONE, 16, Menu.NONE, "Insulation");


//                popupMenu2.getMenu().add(Menu.NONE, 0, Menu.NONE, "Blank");
//                popupMenu2.getMenu().add(Menu.NONE, 1, Menu.NONE, "Baseboards");
//                popupMenu2.getMenu().add(Menu.NONE, 2, Menu.NONE, "Casing");
//                popupMenu2.getMenu().add(Menu.NONE, 3, Menu.NONE, "Cabinet");
//                popupMenu2.getMenu().add(Menu.NONE, 4, Menu.NONE, "Countertop");
//                popupMenu2.getMenu().add(Menu.NONE, 5, Menu.NONE, "Crown molding");
//                popupMenu2.getMenu().add(Menu.NONE, 6, Menu.NONE, "Drywall");
//                popupMenu2.getMenu().add(Menu.NONE, 7, Menu.NONE, "Door");
//                popupMenu2.getMenu().add(Menu.NONE, 8, Menu.NONE, "Insulation");
//                popupMenu2.getMenu().add(Menu.NONE, 9, Menu.NONE, "Light");
//                popupMenu2.getMenu().add(Menu.NONE, 10, Menu.NONE, "Paint");
//                popupMenu2.getMenu().add(Menu.NONE, 11, Menu.NONE, "Plaster");
//                popupMenu2.getMenu().add(Menu.NONE, 12, Menu.NONE, "Texture");
//                popupMenu2.getMenu().add(Menu.NONE, 13, Menu.NONE, "Tile");
//                popupMenu2.getMenu().add(Menu.NONE, 14, Menu.NONE, "Tile");
//                popupMenu2.getMenu().add(Menu.NONE, 15, Menu.NONE, "Wall paper");
//                popupMenu2.getMenu().add(Menu.NONE, 16, Menu.NONE, "Window");
//                popupMenu2.getMenu().add(Menu.NONE, 17, Menu.NONE, "Custom Text");


                value = 0;

                if (btnarea.getTag().toString().equals("Wall")) {
                    opendatabase();

                    SELECT_SQL = "SELECT * FROM tbl_c ORDER BY value ASC";
                    Cur = DB.rawQuery(SELECT_SQL, null);
                    if (Cur != null && Cur.getCount() > 0) {
                        Cur.moveToFirst();
                        do {

                            String strvalue = Cur.getString(Cur.getColumnIndex("value"));
                            if (strvalue.equalsIgnoreCase("Custom Text"))
                                continue;
                            Log.e("Value==>", "" + strvalue);
                            if(strvalue.trim().equals("Blank"))
                            {

                            }else {

                                popupMenu2.getMenu().add(Menu.NONE, value, Menu.NONE, strvalue);

                                value++;
                            }
                        }
                        while (Cur.moveToNext());

                        popupMenu2.getMenu().add(Menu.NONE, value, Menu.NONE, "Custom Text");
                    }
                    Cur.close();
                    DB.close();

                    /*
                    popupMenu2.getMenu().add(Menu.NONE, 0, Menu.NONE, "Blank");
                    popupMenu2.getMenu().add(Menu.NONE, 1, Menu.NONE, "Shingles");
                    popupMenu2.getMenu().add(Menu.NONE, 2, Menu.NONE, "Vent");
                    popupMenu2.getMenu().add(Menu.NONE, 3, Menu.NONE, "Furnace cap");
                    popupMenu2.getMenu().add(Menu.NONE, 4, Menu.NONE, "Drip edge");
                    popupMenu2.getMenu().add(Menu.NONE, 5, Menu.NONE, "Metal");
                    popupMenu2.getMenu().add(Menu.NONE, 6, Menu.NONE, "test sq");
                    popupMenu2.getMenu().add(Menu.NONE, 7, Menu.NONE, "Custom Text");*/

                }


            } else if (btnarea.getTag().toString().equals("Floor")) {

//                popupMenu2.getMenu().add(Menu.NONE, 0, Menu.NONE, "Blank");
//                popupMenu2.getMenu().add(Menu.NONE, 1, Menu.NONE, "Tile");
//                popupMenu2.getMenu().add(Menu.NONE, 2, Menu.NONE, "Vinyl");
//                popupMenu2.getMenu().add(Menu.NONE, 3, Menu.NONE, "Wood");
//                popupMenu2.getMenu().add(Menu.NONE, 4, Menu.NONE, "Laminate");
//                popupMenu2.getMenu().add(Menu.NONE, 5, Menu.NONE, "Subfloor");
//                popupMenu2.getMenu().add(Menu.NONE, 6, Menu.NONE, "Carpet");
//                popupMenu2.getMenu().add(Menu.NONE, 7, Menu.NONE, "Pad");

//                popupMenu2.getMenu().add(Menu.NONE, 0, Menu.NONE, "Blank");
//                popupMenu2.getMenu().add(Menu.NONE, 1, Menu.NONE, "Carpet");
//                popupMenu2.getMenu().add(Menu.NONE, 2, Menu.NONE, "Laminate");
//                popupMenu2.getMenu().add(Menu.NONE, 3, Menu.NONE, "Pad");
//                popupMenu2.getMenu().add(Menu.NONE, 4, Menu.NONE, "Subfloor");
//                popupMenu2.getMenu().add(Menu.NONE, 5, Menu.NONE, "Tile");
//                popupMenu2.getMenu().add(Menu.NONE, 6, Menu.NONE, "Vinyl");
//                popupMenu2.getMenu().add(Menu.NONE, 7, Menu.NONE, "Wood");
//                popupMenu2.getMenu().add(Menu.NONE, 8, Menu.NONE, "Custom Text");
                value = 0;

                if (btnarea.getTag().toString().equals("Floor")) {
                    opendatabase();

                    SELECT_SQL = "SELECT * FROM tbl_f ORDER BY value ASC";
                    Cur = DB.rawQuery(SELECT_SQL, null);
                    if (Cur != null && Cur.getCount() > 0) {
                        Cur.moveToFirst();
                        do {

                            String strvalue = Cur.getString(Cur.getColumnIndex("value"));
                            if (strvalue.equalsIgnoreCase("Custom Text"))
                                continue;
                            Log.e("Value==>", "" + strvalue);
                            if(strvalue.trim().equals("Blank"))
                            {

                            }else {

                                popupMenu2.getMenu().add(Menu.NONE, value, Menu.NONE, strvalue);

                                value++;
                            }
                        }
                        while (Cur.moveToNext());

                        popupMenu2.getMenu().add(Menu.NONE, value, Menu.NONE, "Custom Text");
                    }
                    Cur.close();
                    DB.close();

                    /*
                    popupMenu2.getMenu().add(Menu.NONE, 0, Menu.NONE, "Blank");
                    popupMenu2.getMenu().add(Menu.NONE, 1, Menu.NONE, "Shingles");
                    popupMenu2.getMenu().add(Menu.NONE, 2, Menu.NONE, "Vent");
                    popupMenu2.getMenu().add(Menu.NONE, 3, Menu.NONE, "Furnace cap");
                    popupMenu2.getMenu().add(Menu.NONE, 4, Menu.NONE, "Drip edge");
                    popupMenu2.getMenu().add(Menu.NONE, 5, Menu.NONE, "Metal");
                    popupMenu2.getMenu().add(Menu.NONE, 6, Menu.NONE, "test sq");
                    popupMenu2.getMenu().add(Menu.NONE, 7, Menu.NONE, "Custom Text");*/

                }

            }

            popupMenu2.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {


//                    if (item.getTitle().toString().equalsIgnoreCase("Custom Text")) {
//                        showcostmtextalert("4");
//                    }
                    if (btnarea.getTag().toString().equals("Floor") && item.getTitle().toString().equals("Custom Text")) {

                        showcostmtextalert("2");
                    }
                    if (btnarea.getTag().toString().equals("Wall") && item.getTitle().toString().equals("Custom Text")) {

                        showcostmtextalert("2");
                    }
                    if (btnarea.getTag().toString().equals("Ceiling") && item.getTitle().toString().equals("Custom Text")) {

                        showcostmtextalert("2");
                    }


                    btnmaterial.setTag(item.getItemId() + "");
                    btnmaterial.setText(item.getTitle());


                    if (btnmaterial.getText().toString().equalsIgnoreCase("Blank")) {
                        if (btnarea.getText().toString().equalsIgnoreCase("floor"))
                            btnmaterial.setText("Carpet");
                        else
                            btnmaterial.setText("Drywall");
                        btnSubAreaTogal.setVisibility(View.INVISIBLE);
                    } else {
                        btnSubAreaTogal.setVisibility(View.VISIBLE);
                        btnSubAreaTogal.setBackgroundResource(R.drawable.red_button_background);
                        btnSubAreaTogal.setText(btnmaterial.getText().toString());
                        btnSubAreaTogal.setTag("2");
                    }
                    getalphaname();
                    return false;
                }
            });

            if (btnrei.getText().toString().equalsIgnoreCase("i") && btnareatogal.getTag().toString().equals("2"))
                popupMenu2.show();


        } else {
            value = 0;

            PopupMenu popupMenu2 = new PopupMenu(this, findViewById(R.id.btnmaterial1));


            if (btnrei.getText().toString().trim().equals("R")) {
                opendatabase();

                SELECT_SQL = "SELECT * FROM tbl_r ORDER BY value ASC";
                Cur = DB.rawQuery(SELECT_SQL, null);
                if (Cur != null && Cur.getCount() > 0) {
                    Cur.moveToFirst();
                    do {

                        String strvalue = Cur.getString(Cur.getColumnIndex("value"));
                        if (strvalue.equalsIgnoreCase("Custom Text"))
                            continue;
                        Log.e("Value==>", "" + strvalue);
                        if(strvalue.equalsIgnoreCase("Blank"))
                        {

                        }else {

                            popupMenu2.getMenu().add(Menu.NONE, value, Menu.NONE, strvalue);

                            value++;
                        }
                    }
                    while (Cur.moveToNext());
                    popupMenu2.getMenu().add(Menu.NONE, value, Menu.NONE, "Flat Roof");
                    value++;
                    popupMenu2.getMenu().add(Menu.NONE, value, Menu.NONE, "Material");
                    value++;
                    popupMenu2.getMenu().add(Menu.NONE, value, Menu.NONE, "Custom Text");
                }
                Cur.close();
                DB.close();

                    /*
                    popupMenu2.getMenu().add(Menu.NONE, 0, Menu.NONE, "Blank");
                    popupMenu2.getMenu().add(Menu.NONE, 1, Menu.NONE, "Shingles");
                    popupMenu2.getMenu().add(Menu.NONE, 2, Menu.NONE, "Vent");
                    popupMenu2.getMenu().add(Menu.NONE, 3, Menu.NONE, "Furnace cap");
                    popupMenu2.getMenu().add(Menu.NONE, 4, Menu.NONE, "Drip edge");
                    popupMenu2.getMenu().add(Menu.NONE, 5, Menu.NONE, "Metal");
                    popupMenu2.getMenu().add(Menu.NONE, 6, Menu.NONE, "test sq");
                    popupMenu2.getMenu().add(Menu.NONE, 7, Menu.NONE, "Custom Text");*/

            } else if (btnrei.getText().toString().trim().equals("E")) {

                opendatabase();

                SELECT_SQL = "SELECT * FROM tbl_e ORDER BY value ASC";
                Cur = DB.rawQuery(SELECT_SQL, null);
                if (Cur != null && Cur.getCount() > 0) {
                    Cur.moveToFirst();
                    do {

                        String strvalue = Cur.getString(Cur.getColumnIndex("value"));
                        if (strvalue.equalsIgnoreCase("Custom Text"))
                            continue;
                        Log.e("Value==>", "" + strvalue);
                        if(strvalue.equalsIgnoreCase("Blank"))
                        {

                        }else {

                            popupMenu2.getMenu().add(Menu.NONE, value, Menu.NONE, strvalue);

                            value++;
                        }
                    }
                    while (Cur.moveToNext());
                    popupMenu2.getMenu().add(Menu.NONE, value, Menu.NONE, "Material");
                    value++;
                    popupMenu2.getMenu().add(Menu.NONE, value, Menu.NONE, "Custom Text");
                }
                Cur.close();
                DB.close();

                    /*
                    popupMenu2.getMenu().add(Menu.NONE, 0, Menu.NONE, "Blank");
                    popupMenu2.getMenu().add(Menu.NONE, 1, Menu.NONE, "Siding");
                    popupMenu2.getMenu().add(Menu.NONE, 2, Menu.NONE, "Soffit");
                    popupMenu2.getMenu().add(Menu.NONE, 3, Menu.NONE, "Fascia");
                    popupMenu2.getMenu().add(Menu.NONE, 4, Menu.NONE, "Window");
                    popupMenu2.getMenu().add(Menu.NONE, 5, Menu.NONE, "Window Wrap");
                    popupMenu2.getMenu().add(Menu.NONE, 6, Menu.NONE, "AC Unit");
                    popupMenu2.getMenu().add(Menu.NONE, 7, Menu.NONE, "Paint");
                    popupMenu2.getMenu().add(Menu.NONE, 8, Menu.NONE, "Gutter");
                    popupMenu2.getMenu().add(Menu.NONE, 9, Menu.NONE, "DownSpout");
                    popupMenu2.getMenu().add(Menu.NONE, 10, Menu.NONE, "Custom Text");*/


            } else if (btnrei.getText().toString().trim().equals("I")) {

                opendatabase();

                SELECT_SQL = "SELECT * FROM tbl_i ORDER BY value ASC";
                Cur = DB.rawQuery(SELECT_SQL, null);
                if (Cur != null && Cur.getCount() > 0) {
                    Cur.moveToFirst();
                    do {

                        String strvalue = Cur.getString(Cur.getColumnIndex("value"));
                        if (strvalue.equalsIgnoreCase("Custom Text"))
                            continue;
                        Log.e("Value==>", "" + strvalue);
                        if(strvalue.equalsIgnoreCase("Blank"))
                        {

                        }else {

                            popupMenu2.getMenu().add(Menu.NONE, value, Menu.NONE, strvalue);

                            value++;
                        }
                    }
                    while (Cur.moveToNext());
                    popupMenu2.getMenu().add(Menu.NONE, value, Menu.NONE, "Material");
                    value++;
                    popupMenu2.getMenu().add(Menu.NONE, value, Menu.NONE, "Custom Text");
                }
                Cur.close();
                DB.close();

                    /*
                    popupMenu2.getMenu().add(Menu.NONE, 0, Menu.NONE, "Blank");
                    popupMenu2.getMenu().add(Menu.NONE, 1, Menu.NONE, "Drywall Ceiling");
                    popupMenu2.getMenu().add(Menu.NONE, 2, Menu.NONE, "Drywall Wall");
                    popupMenu2.getMenu().add(Menu.NONE, 3, Menu.NONE, "Paint");
                    popupMenu2.getMenu().add(Menu.NONE, 4, Menu.NONE, "Trim");
                    popupMenu2.getMenu().add(Menu.NONE, 5, Menu.NONE, "Window");
                    popupMenu2.getMenu().add(Menu.NONE, 6, Menu.NONE, "Cabinet");
                    popupMenu2.getMenu().add(Menu.NONE, 7, Menu.NONE, "Countertop");
                    popupMenu2.getMenu().add(Menu.NONE, 8, Menu.NONE, "Carpet");
                    popupMenu2.getMenu().add(Menu.NONE, 9, Menu.NONE, "Flooring");
                    popupMenu2.getMenu().add(Menu.NONE, 10, Menu.NONE, "Tile");
                    popupMenu2.getMenu().add(Menu.NONE, 11, Menu.NONE, "Vinyl");
                    popupMenu2.getMenu().add(Menu.NONE, 12, Menu.NONE, "Custom Text");*/

            }

            popupMenu2.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                @Override
                public boolean onMenuItemClick(MenuItem arg0) {
                    btnmaterial1.setTag(arg0.getItemId() + "");
                    btnmaterial1.setText(arg0.getTitle());

                    btnnodamages.setTag("1");
                    btnnodamages.setBackgroundResource(R.drawable.button_background);

                    btno.setTag("1");
                    btno.setBackgroundResource(R.drawable.button_background);


                    if (btnrei.getText().toString().trim().equals("R") && arg0.getTitle().toString().equals("Custom Text")) {

                        showcostmtextalert("2");

                    }
                    if (btnrei.getText().toString().trim().equals("E") && arg0.getTitle().toString().equals("Custom Text")) {

                        showcostmtextalert("2");

                    }
                    if (btnrei.getText().toString().trim().equals("I") && arg0.getTitle().toString().equals("Custom Text")) {

                        showcostmtextalert("2");

                    } else {
                        if (btnrei.getText().toString().trim().equals("R")) {
                            btntype2.setText(arg0.getTitle());

                            if (btnmaterial1.getText().toString().trim().equals("test sq")) {
                                btntype.setVisibility(View.INVISIBLE);
                                btntype2.setVisibility(View.INVISIBLE);

                                btnmatrialsubmenu.setVisibility(View.VISIBLE);
                            } else {

                                btnmatrialsubmenu.setVisibility(View.GONE);

                                if (btndamagetype.getTag().toString().equals("0")) {
                                    btntype.setVisibility(View.INVISIBLE);

                                } else {
                                    btntype.setVisibility(View.VISIBLE);
                                }

                                if (btndamagetype.getText().toString().equals("Damage")) {
                                    btndamagetype.setText("Damage");
                                    btntype.setVisibility(View.INVISIBLE);
                                } else {
                                    btntype.setVisibility(View.VISIBLE);
                                }
                                if(btnmaterial1.getText().toString().equals("Material"))
                                {
                                    btnmaterial1.setText("Blank");
                                }
                                if (btnmaterial1.getText().toString().equals("Blank")) {
                                    btnmaterial1.setText("Material");
                                    btntype2.setVisibility(View.INVISIBLE);

                                } else {
                                    btntype2.setVisibility(View.VISIBLE);
                                }

                            }

                        }

                        if (btnrei.getText().toString().trim().equals("E")) {
                            btntype2.setText(arg0.getTitle());

                            if (btnmaterial1.getText().toString().trim().equals("test sq")) {
                                btntype.setVisibility(View.INVISIBLE);
                                btntype2.setVisibility(View.INVISIBLE);

                                btnmatrialsubmenu.setVisibility(View.VISIBLE);
                            } else {

                                btnmatrialsubmenu.setVisibility(View.GONE);

                                if (btndamagetype.getTag().toString().equals("0")) {
                                    btntype.setVisibility(View.INVISIBLE);

                                } else {
                                    btntype.setVisibility(View.VISIBLE);
                                }


                                if (btndamagetype.getText().toString().equals("Damage")) {
                                    btndamagetype.setText("Damage");
                                    btntype.setVisibility(View.INVISIBLE);
                                } else {
                                    btntype.setVisibility(View.VISIBLE);
                                }

                                if (btnmaterial1.getText().toString().equals("Blank")) {
                                    btnmaterial.setText("Material");
                                    btntype2.setVisibility(View.INVISIBLE);

                                } else {
                                    btntype2.setVisibility(View.VISIBLE);
                                }
                            }
                        }

                        if (btnrei.getText().toString().trim().equals("I")) {
                            btntype2.setText(arg0.getTitle());

                            if (btnmaterial1.getText().toString().trim().equals("test sq")) {
                                btntype.setVisibility(View.INVISIBLE);
                                btntype2.setVisibility(View.INVISIBLE);

                                btnmatrialsubmenu.setVisibility(View.VISIBLE);
                            } else {

                                btnmatrialsubmenu.setVisibility(View.GONE);

                                if (btndamagetype.getTag().toString().equals("0")) {
                                    btntype.setVisibility(View.INVISIBLE);

                                } else {
                                    btntype.setVisibility(View.VISIBLE);
                                }


                                if (btndamagetype.getText().toString().equals("Damage")) {
                                    btndamagetype.setText("Damage");
                                    btntype.setVisibility(View.INVISIBLE);
                                } else {
                                    btntype.setVisibility(View.VISIBLE);
                                }


                                if (btnmaterial1.getText().toString().equals("Blank")) {
                                    btnmaterial1.setText("Material");
                                    btntype2.setVisibility(View.INVISIBLE);

                                } else {
                                    btntype2.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    }

                    getalphaname();

                    btntype2.setTag("2");
                    btntype2.setBackgroundResource(R.drawable.red_button_background);

                    getalphaname();
                    return false;
                }
            });
            popupMenu2.show();
        }
    }


    private void showcostmtextalert(final String strtext) {
        /* Alert Dialog Code Start*/
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setCancelable(false);
        alert.setTitle("Add custom text"); //Set Alert dialog title here

        // Set an EditText view to get user input
        final EditText input = new EditText(this);
        input.setText("");
        input.setSelection(input.getText().toString().length());
        alert.setView(input);

        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                if (input.getText().toString().trim().equals("")) {
                    Toast.makeText(getApplicationContext(), "Enter custom text", Toast.LENGTH_LONG).show();

                } else {
                    if (strtext.equals("1")) {
                        opendatabase();
                        String strqry = "Insert into " + "tbldamagetype" + " ('value') Values('" + input.getText().toString().trim() + "')";
                        DB.execSQL(strqry);
                        DB.close();

                        btndamagetype.setText(input.getText().toString());
                        btntype.setText(input.getText().toString());
                        btnnodamages.setTag("1");
                        btnnodamages.setBackgroundResource(R.drawable.button_background);
                        btno.setTag("1");
                        btno.setBackgroundResource(R.drawable.button_background);

                        damagecostmtext = input.getText().toString().trim();
                    } else if (strtext.equals("2")) {
                        opendatabase();
                        if (btnrei.getText().toString().trim().equals("E")) {
                            String strqry = "Insert into " + "tbl_e" + " ('value') Values('" + input.getText().toString().trim() + "')";
                            DB.execSQL(strqry);
                        } else if (btnrei.getText().toString().trim().equals("R")) {
                            String strqry = "Insert into " + "tbl_r" + " ('value') Values('" + input.getText().toString().trim() + "')";
                            DB.execSQL(strqry);
                        } else if (btnarea.getTag().toString().equals("Floor")) {
                            String strqry = "Insert into " + "tbl_f" + " ('value') Values('" + input.getText().toString().trim() + "')";
                            DB.execSQL(strqry);
                        } else if (btnarea.getTag().toString().equals("Wall")) {
                            String strqry = "Insert into " + "tbl_c" + " ('value') Values('" + input.getText().toString().trim() + "')";
                            DB.execSQL(strqry);
                        } else if (btnarea.getTag().toString().equals("Ceiling")) {
                            String strqry = "Insert into " + "tbl_w" + " ('value') Values('" + input.getText().toString().trim() + "')";
                            DB.execSQL(strqry);
                        }

                        DB.close();

                        btnmaterial.setText(input.getText().toString());
                        btntype2.setText(input.getText().toString());

                        btnnodamages.setTag("1");
                        btnnodamages.setBackgroundResource(R.drawable.button_background);
                        btno.setTag("1");
                        btno.setBackgroundResource(R.drawable.button_background);

                        matrialcostmtext = input.getText().toString().trim();

                    } else if (strtext.equals("3")) {
                        opendatabase();
                        String strqry = "Insert into " + "tbl_interior" + " ('value','shortname') Values('" + input.getText().toString().trim() + "','" + input.getText().toString().trim() + "')";
                        DB.execSQL(strqry);
                        DB.close();

                        btniteriortype.setText(input.getText().toString());

                        btnocb.setTag("3");
        selectocb();
                        strboc = "1";
                        strboctype = "Overview";
                    }
                    getalphaname();
                }
            } // End of onClick(DialogInterface dialog, int whichButton)
        }); //End of alert.setPositiveButton
        alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
                dialog.cancel();
            }
        }); //End of alert.setNegativeButton
        AlertDialog alertDialog = alert.create();
        alertDialog.show();
    }

    private void showabcoption() {

        popupsubcat.show();
    }

    int claimSelectPos = 0;

    private void showclaimnameoption(Boolean isshowalert) {

        value = 0;

        PopupMenu popupMenu2 = new PopupMenu(this, findViewById(R.id.btncat));

        if (arrayListClaim == null)
            return;

        for (int i = 0; i < arrayListClaim.size(); i++) {
            popupMenu2.getMenu().add(Menu.NONE, i, Menu.NONE, arrayListClaim.get(i).getName());
            System.out.println("data of value" + arrayListClaim.get(i).getName());
        }

        popupMenu2.getMenu().add(Menu.NONE, arrayListClaim.size(), Menu.NONE, "Add New Claim");

        popupMenu2.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if (item.getTitle().toString().equals("Add New Claim")) {
                    addClaim();
                }
                else
                {
//                    claimSelectPos = item.getItemId();

                    setclaimname(item.getTitle());
//                    checkClaimDescription(arrayListClaim.get(claimSelectPos).getId());
                }
                return false;
            }
        });
        popupMenu2.show();

        /*opendatabase();

        SELECT_SQL = "SELECT * FROM tbl_claimname";
        Cur = DB.rawQuery(SELECT_SQL, null);
        if (Cur != null && Cur.getCount() > 0) {
            Cur.moveToFirst();
            do {
                String strvalue = Cur.getString(Cur.getColumnIndex("name"));
                popupMenu2.getMenu().add(Menu.NONE, value, Menu.NONE, strvalue);

                value++;
            }
            while (Cur.moveToNext());
        } else {
            btncat.setText("ClaimMate");
            lastimageeditor.putString("appfoldername", "ClaimMate");
            lastimageeditor.commit();
            appfoldername = "ClaimMate";
            mydir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), appfoldername);
            btnabc.setText("None");
            txtalphaname2.setVisibility(View.INVISIBLE);
        }
        Cur.close();
        DB.close();

        popupMenu2.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem arg0) {

                btncat.setText(arg0.getTitle());
                lastimageeditor.putString("appfoldername", arg0.getTitle().toString());
                lastimageeditor.commit();
                appfoldername = arg0.getTitle().toString();
                mydir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), appfoldername);
                btnabc.setText("None");

//                PrefManager.setClaimId(arg0.getTitle().toString());
//                addClaimDescription();

                getalphaname();

                return false;
            }
        });

        if (isshowalert) {
            popupMenu2.show();
        }*/
    }

    private void setclaimname(CharSequence title)
    {
        if(arrayListClaim!=null)
        {
            for (int i=0;i<arrayListClaim.size();i++)
            {
                if(title.equals(arrayListClaim.get(i).getName()))
                {
                    claimSelectPos = i;
                    getselectclaimname(i);
                    break;
                }

            }
        }






    }

    private void getselectclaimname(int i) {

        PrefManager.setClaimId(arrayListClaim.get(i).getId());
        btncat.setText(arrayListClaim.get(i).getName());
        btncat.setTag(claimSelectPos);

        lastimageeditor = lastpathpf.edit();
        lastimageeditor.putString("appfoldername", arrayListClaim.get(i).getName());
        lastimageeditor.commit();
        appfoldername = arrayListClaim.get(i).getName();
        mydir = new File(appdir, appfoldername);
        btnabc.setText("None");
        txtalphaname2.setVisibility(View.INVISIBLE);
        getalphaname();
    }

    private void getClaimList()
    {
        Log.e("btncattage","--->"+Integer.parseInt(btncat.getTag().toString()));

        if (Utility.haveInternet(mContext, true))
        {
//            Utility.showProgress(mContext);
            ApiClient.getClient().create(APIInterface.class).getClaimList(PrefManager.getUserId()).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
//                    Utility.dismissProgress();
                    Log.i(TAG, "getClaimListRes = " + response.body());
                    //  System.out.println("getClaimListRes :-" + response.body());
                    if (response.body() == null) {
                        Utility.errorDialog(mContext, getString(R.string.error_data_not_found));
                        return;
                    }

                    try {
                        JSONObject jsonObject = new JSONObject(response.body());
                        if (jsonObject.getString("success").equals("success")) {
                            JSONArray datas = jsonObject.getJSONArray("Data");
                            arrayListClaim = new ArrayList<>();
                            for (int i = 0; i < datas.length(); i++) {
                                JSONObject data = datas.getJSONObject(i);
                                ClaimModel claimModel = new ClaimModel();
                                claimModel.setId(data.getString("id"));
                                claimModel.setName(data.getString("claim_name"));
                                //claimModel.setShortName(data.getString("short_name"));
                                arrayListClaim.add(claimModel);
                            }


                            setclaimname(appfoldername);

//                            claimSelectPos = Integer.parseInt(btncat.getTag().toString());
//                            btncat.setText(arrayListClaim.get(Integer.parseInt(btncat.getTag().toString())).getName());
//                            checkClaimDescription(arrayListClaim.get(Integer.parseInt(btncat.getTag().toString())).getId());
                        } else {
                            addClaim();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
//                    Utility.dismissProgress();
                    Log.i(TAG, "getClaimListError = " + t.toString());
                }
            });
        }
    }

    private void checkClaimDescription(final String claimId) {
        if (Utility.haveInternet(mContext, false)) {
            Utility.showProgress(mContext);
            ApiClient.getClient().create(APIInterface.class).checkClaimDes(PrefManager.getUserId(), claimId).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    Utility.dismissProgress();
                    Log.i(TAG, "checkClaimDesRes = " + response.body());

                    if (response.body() == null) {
                        Utility.errorDialog(mContext, getString(R.string.error_data_not_found));
                        return;
                    }

                    try {
                        JSONObject jsonObject = new JSONObject(response.body());
                        if (jsonObject.getString("message").equalsIgnoreCase("False")) {
                            addClaimDescription();
                        } else {
                            ClaimModel claimModel = arrayListClaim.get(claimSelectPos);

                            PrefManager.setClaimId(claimId);
                            btncat.setText(claimModel.getName());
                            btncat.setTag(claimSelectPos);
                            lastimageeditor.putString("appfoldername", claimModel.getName());
                            lastimageeditor.commit();
                            appfoldername = claimModel.getName();
                            mydir = new File(appdir, appfoldername);
                            btnabc.setText("None");
                            txtalphaname2.setVisibility(View.INVISIBLE);

                            getalphaname();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Utility.dismissProgress();
                    Log.i(TAG, "checkClaimDesError = " + t.toString());
                }
            });
        }
    }

    private void addClaim() {

        Constants.addclaimname = "";
        Intent addclaimname_act = new Intent(getApplicationContext(), addclaimname.class);
        startActivity(addclaimname_act);

        /*final Dialog dialog = new Dialog(mContext);
        if (arrayListClaim == null || arrayListClaim.size() == 0)
            dialog.setCancelable(false);
        dialog.setContentView(R.layout.popup_add_claim);

        final EditText edtClaimName = dialog.findViewById(R.id.edtClaimName);
        // final EditText edtShortName = dialog.findViewById(R.id.edtShortName);

        dialog.findViewById(R.id.btnSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

                AddClaimName(edtClaimName.getText().toString());
            }
        });
        dialog.show();*/
    }

    private void AddClaimName(final String claimname) {


        if (Utility.haveInternet(mContext, true))
        {
//                final String claimname = edtClaimName.getText().toString();
                Utility.showProgress(mContext);
                ApiClient.getClient().create(APIInterface.class).addClaim(PrefManager.getUserId(), claimname).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        Utility.dismissProgress();
                        Log.i(TAG, "addClaimRes = " + response.body());

                        if (response.body() == null) {
                            Utility.errorDialog(mContext, getString(R.string.error_data_not_found));
                            return;
                        }

                        try {
                            JSONObject jsonObject = new JSONObject(response.body());
                            Toast.makeText(mContext, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            if (jsonObject.getString("success").equals("success")) {
                                appfoldername =  claimname;
                                getClaimList();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Utility.dismissProgress();
                        Log.i(TAG, "addClaimError = " + t.toString());
                    }
                });

        }
    }

    private void addClaimDescription() {
        final Dialog dialog = new Dialog(this);
//        dialog.setCancelable(false);
        dialog.setContentView(R.layout.popup_claim_description);

        final TextView txtClaim = dialog.findViewById(R.id.txtClaim);
        final Spinner spName = dialog.findViewById(R.id.spName);
        final EditText edtName = dialog.findViewById(R.id.edtName);
        final EditText edtCauseOfLoss = dialog.findViewById(R.id.edtCauseOfLoss);
        final TextView txtDateOfLoss = dialog.findViewById(R.id.txtDateOfLoss);
        final CheckBox chkLaborMin = dialog.findViewById(R.id.chkLaborMin);
        final LinearLayout llLaborMin = dialog.findViewById(R.id.llLaborMin);
        final EditText edtAdded = dialog.findViewById(R.id.edtAdded);
        final EditText edtRemoved = dialog.findViewById(R.id.edtRemoved);
        final EditText edtCompany = dialog.findViewById(R.id.edtCompany);
        final TextView txtInspectionDate = dialog.findViewById(R.id.txtInspectionDate);
        final TextView txtInspectionTime = dialog.findViewById(R.id.txtInspectionTime);

        // txtClaim.setText("Claim : " + arrayListClaim.get(claimSelectPos).getName());
        edtName.setText(arrayListClaim.get(claimSelectPos).getName());
        Calendar calendar = Calendar.getInstance();
        txtDateOfLoss.setText(calendar.get(Calendar.DAY_OF_MONTH) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.YEAR));
        txtInspectionDate.setText(calendar.get(Calendar.DAY_OF_MONTH) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.YEAR));
        txtInspectionTime.setText((calendar.get(Calendar.HOUR) == 0 ? "12" : calendar.get(Calendar.HOUR)) + ":" + calendar.get(Calendar.MINUTE) + " " + (calendar.get(Calendar.AM_PM) == 0 ? "AM" : "PM"));
//        txtClaim.setText("Claim : TestClaim");

        chkLaborMin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    llLaborMin.setVisibility(View.VISIBLE);
                    edtAdded.setText("No");
                    edtRemoved.setText("No");
                    edtAdded.requestFocus();
                } else {
                    llLaborMin.setVisibility(View.GONE);
                }

            }
        });

        dialog.findViewById(R.id.imgDateOfLoss).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(HomeActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        txtDateOfLoss.setText(i2 + "-" + (i1 + 1) + "-" + i);
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        dialog.findViewById(R.id.imgInspectionDate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(HomeActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        txtInspectionDate.setText(i2 + "-" + (i1 + 1) + "-" + i);
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        dialog.findViewById(R.id.imgInspectionTime).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                TimePickerDialog timePickerDialog = new TimePickerDialog(mContext, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), i, i1);
                        txtInspectionTime.setText((calendar.get(Calendar.HOUR) == 0 ? "12" : calendar.get(Calendar.HOUR)) + ":" + calendar.get(Calendar.MINUTE) + " " + (calendar.get(Calendar.AM_PM) == 0 ? "AM" : "PM"));
                    }
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);
                timePickerDialog.show();
            }
        });

        dialog.findViewById(R.id.btnSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "Submit Success.", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.btnSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utility.haveInternet(mContext, true)) {
                    if (TextUtils.isEmpty(edtName.getText())) {
                        edtName.setError("Please enter name");
                        edtName.requestFocus();
                    } else if (TextUtils.isEmpty(edtCauseOfLoss.getText())) {
                        edtCauseOfLoss.setError("Please enter cause of loss");
                        edtCauseOfLoss.requestFocus();
                    } else if (TextUtils.isEmpty(txtDateOfLoss.getText())) {
                        Toast.makeText(HomeActivity.this, "Please select date of loss.", Toast.LENGTH_SHORT).show();
                    } else if (chkLaborMin.isChecked() && TextUtils.isEmpty(edtAdded.getText())) {
                        edtAdded.setError("Please enter labor minimum added");
                        edtAdded.requestFocus();
                    } else if (chkLaborMin.isChecked() && TextUtils.isEmpty(edtRemoved.getText())) {
                        edtRemoved.setError("Please enter labor minimum removed");
                        edtRemoved.requestFocus();
                    } else if (TextUtils.isEmpty(edtCompany.getText())) {
                        edtCompany.setError("Please enter company");
                        edtCompany.requestFocus();
                    } else if (TextUtils.isEmpty(txtInspectionDate.getText())) {
                        Toast.makeText(HomeActivity.this, "Please select inspection date.", Toast.LENGTH_SHORT).show();
                    } else {
                        Utility.showProgress(mContext);
                        String mr = spName.getSelectedItem().toString().equals("None") ? "" : spName.getSelectedItem().toString(), LaborMin = chkLaborMin.isChecked() ? "1" : "0";
                        ApiClient.getClient().create(APIInterface.class).addClaimDescription(PrefManager.getUserId(), arrayListClaim.get(claimSelectPos).getId(), mr + " " + edtName.getText().toString(), mr, edtName.getText().toString(), edtCauseOfLoss.getText().toString(), txtDateOfLoss.getText().toString(), LaborMin, edtAdded.getText().toString(), edtRemoved.getText().toString(), edtCompany.getText().toString(), txtInspectionDate.getText().toString(), txtInspectionTime.getText().toString()).enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                Utility.dismissProgress();
                                Log.i(TAG, "addClaimDescriptionRes = " + response.body());

                                if (response.body() == null) {
                                    Utility.errorDialog(mContext, getString(R.string.error_data_not_found));
                                    return;
                                }

                                try {
                                    JSONObject jsonObject = new JSONObject(response.body());
                                    Toast.makeText(mContext, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                    if (jsonObject.getString("success").equalsIgnoreCase("success")) {
                                        dialog.dismiss();
                                        getClaimList();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                                Utility.dismissProgress();
                                Log.i(TAG, "addClaimDescriptionError = " + t.toString());
                            }
                        });
                    }
                }
            }
        });

        dialog.findViewById(R.id.imgClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void showpitchmenu(final Button btnview) {

        PopupMenu popupMenu2 = new PopupMenu(this, btnview);
        popupMenu2.getMenu().add(Menu.NONE, 0, Menu.NONE, "Blank");
        popupMenu2.getMenu().add(Menu.NONE, 1, Menu.NONE, "Front Slope");
        popupMenu2.getMenu().add(Menu.NONE, 2, Menu.NONE, "Right Slope");
        popupMenu2.getMenu().add(Menu.NONE, 3, Menu.NONE, "Rear Slope");
        popupMenu2.getMenu().add(Menu.NONE, 4, Menu.NONE, "Left Slope");
        popupMenu2.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem arg0) {
                btnview.setText(arg0.getTitle());
                getalphaname();
                return false;
            }
        });
        popupMenu2.show();
    }

    private void showmyhail(final Button btnhailopttionview, final String slopselect)
    {

        selectslop_cur = slopselect;


        txt_frontslope.setBackgroundResource(R.drawable.roundgray_select);
        txt_rightslope.setBackgroundResource(R.drawable.roundgray_select);
        txt_rearslope.setBackgroundResource(R.drawable.roundgray_select);
        txt_leftslope.setBackgroundResource(R.drawable.roundgray_select);


        if(txt_frontslope == btnhailopttionview)
        {
            txt_frontslope.setBackgroundResource(R.drawable.roundred_select);
        }
        else if(txt_rightslope == btnhailopttionview)
        {
            txt_rightslope.setBackgroundResource(R.drawable.roundred_select);
        }
        else if(txt_rearslope == btnhailopttionview)
        {
            txt_rearslope.setBackgroundResource(R.drawable.roundred_select);
        }
        else if(txt_leftslope == btnhailopttionview)
        {
            txt_leftslope.setBackgroundResource(R.drawable.roundred_select);
        }



        /*PopupMenu popupMenu2 = new PopupMenu(this, btnhailopttionview);

        popupMenu2.getMenu().add(Menu.NONE, 0, Menu.NONE, "0");
        popupMenu2.getMenu().add(Menu.NONE, 1, Menu.NONE, "1");
        popupMenu2.getMenu().add(Menu.NONE, 2, Menu.NONE, "2");
        popupMenu2.getMenu().add(Menu.NONE, 3, Menu.NONE, "3");
        popupMenu2.getMenu().add(Menu.NONE, 4, Menu.NONE, "4");
        popupMenu2.getMenu().add(Menu.NONE, 5, Menu.NONE, "5");
        popupMenu2.getMenu().add(Menu.NONE, 6, Menu.NONE, "6");
        popupMenu2.getMenu().add(Menu.NONE, 7, Menu.NONE, "7");
        popupMenu2.getMenu().add(Menu.NONE, 8, Menu.NONE, "8");
        popupMenu2.getMenu().add(Menu.NONE, 9, Menu.NONE, "9");
        popupMenu2.getMenu().add(Menu.NONE, 10, Menu.NONE, "10");

        popupMenu2.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem arg0) {

                btnhailopttionview.setText(arg0.getTitle());

                if (slopselect.equals("1")) {
                    no_frontslope = arg0.getTitle().toString();
                } else if (slopselect.equals("2")) {
                    no_rightslope = arg0.getTitle().toString();
                } else if (slopselect.equals("3")) {
                    no_rearslope = arg0.getTitle().toString();
                } else if (slopselect.equals("4")) {
                    no_leftslope = arg0.getTitle().toString();
                }
                return false;
            }
        });
        popupMenu2.show();*/

        SetSlopNumberDate(btnhailopttionview.getText().toString().trim());

    }


    private void selectslopno(final Button btnsloptotalno) {

        PopupMenu popupMenu2 = new PopupMenu(this, btnsloptotalno);

        popupMenu2.getMenu().add(Menu.NONE, 0, Menu.NONE, "0");
        popupMenu2.getMenu().add(Menu.NONE, 1, Menu.NONE, "1");
        popupMenu2.getMenu().add(Menu.NONE, 2, Menu.NONE, "2");
        popupMenu2.getMenu().add(Menu.NONE, 3, Menu.NONE, "3");
        popupMenu2.getMenu().add(Menu.NONE, 4, Menu.NONE, "4");
        popupMenu2.getMenu().add(Menu.NONE, 5, Menu.NONE, "5");
        popupMenu2.getMenu().add(Menu.NONE, 6, Menu.NONE, "6");
        popupMenu2.getMenu().add(Menu.NONE, 7, Menu.NONE, "7");
        popupMenu2.getMenu().add(Menu.NONE, 8, Menu.NONE, "8");
        popupMenu2.getMenu().add(Menu.NONE, 9, Menu.NONE, "9");
        popupMenu2.getMenu().add(Menu.NONE, 10, Menu.NONE, "10");

        popupMenu2.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem arg0) {

                btnsloptotalno.setText(arg0.getTitle().toString());
                getalphaname();
                return false;
            }
        });
        popupMenu2.show();
    }




    private void showInsulationOption() {
        PopupMenu popupMenu = new PopupMenu(mContext, btnInsulation);

        popupMenu.getMenu().add("Insulation");
        popupMenu.getMenu().add("I");
        popupMenu.getMenu().add("No");
        popupMenu.getMenu().add(".25I");
        popupMenu.getMenu().add(".5I");
        popupMenu.getMenu().add(".75I");

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getTitle().toString().equals("Insulation"))
                    btnInsulation.setText("Insulation");
                else
                    btnInsulation.setText(item.getTitle().toString());
                return false;
            }
        });

        popupMenu.show();
    }

    private void showQuantityOption() {
        PopupMenu popupMenu = new PopupMenu(mContext, btnQty);

        popupMenu.getMenu().add("0");
        for (int i = 1; i <= 99; i++) {
            popupMenu.getMenu().add(i + "");
        }
        /*if (btnrei.getText().toString().equalsIgnoreCase("R")) {
            popupMenu.getMenu().add("SQ");
            popupMenu.getMenu().add("F");
            popupMenu.getMenu().add("R");
            popupMenu.getMenu().add("B");
            popupMenu.getMenu().add("L");
            popupMenu.getMenu().add("D");
            popupMenu.getMenu().add("1");
            popupMenu.getMenu().add("2");
            popupMenu.getMenu().add("3");
            popupMenu.getMenu().add("all");
            popupMenu.getMenu().add("the");
            popupMenu.getMenu().add("way");
            popupMenu.getMenu().add("to");
            popupMenu.getMenu().add("99");
        } else if (btnrei.getText().toString().equalsIgnoreCase("E")) {
            popupMenu.getMenu().add(".25W");
            popupMenu.getMenu().add(".5W");
            popupMenu.getMenu().add(".75W");
            popupMenu.getMenu().add("W");
            popupMenu.getMenu().add("D");
            popupMenu.getMenu().add("1");
            popupMenu.getMenu().add("2");
            popupMenu.getMenu().add("3");
            popupMenu.getMenu().add("4");
            popupMenu.getMenu().add("all");
            popupMenu.getMenu().add("the");
            popupMenu.getMenu().add("way");
            popupMenu.getMenu().add("to");
            popupMenu.getMenu().add("999");
        } else {
            if (btnarea.getText().toString().equalsIgnoreCase("Ceiling")) {
                popupMenu.getMenu().add("C");
                popupMenu.getMenu().add(".5C");
                popupMenu.getMenu().add("D");
                popupMenu.getMenu().add("1");
                popupMenu.getMenu().add("2");
                popupMenu.getMenu().add("3");
                popupMenu.getMenu().add("4");
                popupMenu.getMenu().add("all");
                popupMenu.getMenu().add("the");
                popupMenu.getMenu().add("way");
                popupMenu.getMenu().add("to");
                popupMenu.getMenu().add("999");
            } else if (btnarea.getText().toString().equalsIgnoreCase("Wall")) {
                popupMenu.getMenu().add(".25W");
                popupMenu.getMenu().add(".5W");
                popupMenu.getMenu().add(".75W");
                popupMenu.getMenu().add("W");
                popupMenu.getMenu().add("D");
                popupMenu.getMenu().add("1");
                popupMenu.getMenu().add("2");
                popupMenu.getMenu().add("3");
                popupMenu.getMenu().add("4");
                popupMenu.getMenu().add("all");
                popupMenu.getMenu().add("the");
                popupMenu.getMenu().add("way");
                popupMenu.getMenu().add("to");
                popupMenu.getMenu().add("999");
            } else {
                popupMenu.getMenu().add("Cln");
                popupMenu.getMenu().add("Sand");
                popupMenu.getMenu().add("Replace");
            }
        }*/

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getTitle().toString().equals("0"))
                    btnQty.setText("Quantity");
                else
                    btnQty.setText(item.getTitle().toString());
                return false;
            }
        });
        popupMenu.show();
    }

    private void showAreaoption() {

        PopupMenu popupMenu2 = new PopupMenu(this, findViewById(R.id.btnarea));
        popupMenu2.getMenu().add(Menu.NONE, 0, Menu.NONE, "Area");
        popupMenu2.getMenu().add(Menu.NONE, 1, Menu.NONE, "Ceiling");
        popupMenu2.getMenu().add(Menu.NONE, 2, Menu.NONE, "Wall");
        popupMenu2.getMenu().add(Menu.NONE, 3, Menu.NONE, "Floor");

        popupMenu2.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem arg0) {

                btnarea.setText(arg0.getTitle());
                btnarea.setTag(arg0.getTitle());

                setAreaOption();

                return false;
            }
        });
        popupMenu2.show();
    }

    private void setAreaOption() {
        btnareatogal.setText(btnarea.getText().toString());
        btnareatogal.setBackgroundResource(R.drawable.red_button_background);
        btnareatogal.setTag("2");

        btnmaterial.setTag("0");
        if (btnarea.getText().toString().equalsIgnoreCase("floor"))
            btnmaterial.setText("Carpet");
        else
            btnmaterial.setText("Drywall");

        btnSubAreaTogal.setVisibility(View.VISIBLE);
        btnSubAreaTogal.setText(btnmaterial.getText().toString());
        btnSubAreaTogal.setTag("1");
        btnSubAreaTogal.setBackgroundResource(R.drawable.button_background);

        strselectarea = btnareatogal.getText().toString();
        if (btnarea.getText().toString().equals("Area")) {
            btnmaterial.setVisibility(View.INVISIBLE);
            btnareatogal.setVisibility(View.INVISIBLE);
            btnareatogal.setTag("0");

            btnSubAreaTogal.setVisibility(View.INVISIBLE);

            btnInsulation.setVisibility(View.GONE);
            btnQty.setVisibility(View.GONE);
        } else {
            btnmaterial.setVisibility(View.VISIBLE);
            btnareatogal.setVisibility(View.INVISIBLE);

            btnInsulation.setText("Insulation");
            btnQty.setText("Quantity");
            btnInsulation.setVisibility(View.VISIBLE);
            btnQty.setVisibility(View.VISIBLE);
        }
        getalphaname();
    }

    private void showdamagetypeoption1() {

        value = 0;

        PopupMenu popupMenu2 = new PopupMenu(this, findViewById(R.id.btndamagetype1));

        opendatabase();

        SELECT_SQL = "SELECT * FROM tbldamagetype";
        Cur = DB.rawQuery(SELECT_SQL, null);
        if (Cur != null && Cur.getCount() > 0) {
            Cur.moveToFirst();
            do {

                String strvalue = Cur.getString(Cur.getColumnIndex("value"));
                Log.e("Value==>", "" + strvalue);
                if(strvalue.equals("Blank"))
                {

                }else {
                    popupMenu2.getMenu().add(Menu.NONE, value, Menu.NONE, strvalue);

                    value++;
                }

            }
            while (Cur.moveToNext());
        }
        Cur.close();
        DB.close();

        popupMenu2.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem arg0) {

                btndamagetype1.setTag(arg0.getItemId() + "");
                btndamagetype1.setText(arg0.getTitle());
                //   btntype.setText(arg0.getTitle());

                btnhailnodamages.setTag("2");
                btnhailnodamages.setBackgroundResource(R.drawable.red_button_background);
                btnhailnodamages.setText(arg0.getTitle());
                strhaildamage=arg0.getTitle().toString();
                //  btno.setTag("1");
                //  btno.setBackgroundResource(R.drawable.button_background);

                if (arg0.getTitle().equals("Blank")) {
                    btndamagetype1.setText("Hail Damage");
                    //btntype.setVisibility(View.INVISIBLE);
                } else {
                    // btntype.setVisibility(View.VISIBLE);

                }

                // btntype.setTag("2");
                // btntype.setBackgroundResource(R.drawable.red_button_background);

                if (arg0.getTitle().equals("Custom text")) {
                    showcostmtextalert("1");
                }

/*

                btnmaterial1.setTag("0");
                if (btnarea.getText().toString().equalsIgnoreCase("floor"))
                    btnmaterial1.setText("Carpet");
                else
                    btnmaterial1.setText("Drywall");

                btnSubAreaTogal.setVisibility(View.VISIBLE);
                btnSubAreaTogal.setText(btnmaterial1.getText().toString());
                btnSubAreaTogal.setTag("1");
                btnSubAreaTogal.setBackgroundResource(R.drawable.button_background);

                strselectarea = btnareatogal.getText().toString();

                btnmaterial1.setVisibility(View.VISIBLE);
*/
                btndamagetype1.setText(arg0.getTitle());
                getalphaname();

                return false;
            }
        });
        popupMenu2.show();
    }

    private void showdamagetypeoption(Boolean showalert)
    {

        Log.e("","call defaultfucation");
        value = 0;

        PopupMenu popupMenu2 = new PopupMenu(this, findViewById(R.id.btndamagetype));

        opendatabase();

        SELECT_SQL = "SELECT * FROM tbldamagetype";
        Cur = DB.rawQuery(SELECT_SQL, null);
        if (Cur != null && Cur.getCount() > 0) {
            Cur.moveToFirst();
            do {

                String strvalue = Cur.getString(Cur.getColumnIndex("value"));
                if (strvalue.equals("Custom text"))
                    continue;
                Log.e("Value==>", "" + strvalue);
                if(strvalue.equals("Blank"))
                {

                }else {
                    popupMenu2.getMenu().add(Menu.NONE, value, Menu.NONE, strvalue);

                    value++;
                }
            }
            while (Cur.moveToNext());
            popupMenu2.getMenu().add(Menu.NONE, value, Menu.NONE, "Custom text");
        }
        Cur.close();
        DB.close();

        popupMenu2.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem arg0) {

                btnmatrialsubmenu.setText("0");
                damagetypeoptionselect(arg0,false);

//                GetPhotoName();
                return false;
            }
        });

        if(showalert)
        {
            popupMenu2.show();
        }
        else
        {

            damagetypeoptionselect(popupMenu2.getMenu().getItem(2),true);
        }
    }

    private void damagetypeoptionselect(MenuItem arg0,Boolean isdefault) {

        btndamagetype.setTag(arg0.getItemId() + "");
        btndamagetype.setText(arg0.getTitle());
        btntype.setText(arg0.getTitle());

        btnnodamages.setTag("1");
        btnnodamages.setBackgroundResource(R.drawable.button_background);
//        btno.setTag("1");
//        btno.setBackgroundResource(R.drawable.button_background);


        if(arg0.getTitle().equals("Damage"))
        {
            btndamagetype.setText("Blank");
        }

        if (btndamagetype.getText().toString().equals("Blank")) {
            btndamagetype.setText("Damage");
            btntype.setVisibility(View.INVISIBLE);
        } else {
            btntype.setVisibility(View.VISIBLE);
        }


        if(isdefault)
        {
            btntype.setTag("1");
            btntype.setBackgroundResource(R.drawable.button_background);

        }
        else
        {
            btntype.setTag("2");
            btntype.setBackgroundResource(R.drawable.red_button_background);

        }

        if (arg0.getTitle().equals("Custom text")) {
            showcostmtextalert("1");
        }

        getalphaname();

    }

    @Override
    public void foundUpdateAndShowIt(String versionDonwloadable) {
        showupdatealert();
    }

    @Override
    public void foundUpdateAndDontShowIt(String versionDonwloadable) {

    }

    @Override
    public void returnUpToDate(String versionDonwloadable) {

    }

    @Override
    public void returnMultipleApksPublished() {

    }

    @Override
    public void returnNetworkError() {

    }

    @Override
    public void returnAppUnpublished() {
    }

    @Override
    public void returnStoreError() {
    }

    @Override
    public void onSensorChanged(final SensorEvent event) {
        if (btnrisk.getText().toString().trim().equals("Pitch")) {

            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                System.arraycopy(event.values, 0, mLastAccelerometer, 0, event.values.length);
                mLastAccelerometerSet = true;
            } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                System.arraycopy(event.values, 0, mLastMagnetometer, 0, event.values.length);
                mLastMagnetometerSet = true;
            }
            if (mLastAccelerometerSet && mLastMagnetometerSet) {
                SensorManager.getRotationMatrix(mR, null, mLastAccelerometer, mLastMagnetometer);
                SensorManager.getOrientation(mR, mOrientation);

                Double angle = Math.toDegrees(mOrientation[1]);

                double rad = angle * Math.PI / 180;
                double y = Math.tan(rad);
                double pitch = y * 12;


                lblpitchvalue.setText("" + decimalFormat.format(Math.abs(Math.round(pitch))) + "/12");
                getalphaname();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }

    private void selecthailoption(CheckBox rbtn_hail, Button txt_hail, String type) {

        if (rbtn_hail.isChecked()) {
//            txt_hail.setText("1");
//            showmyhail(txt_hail, "1");

//            txt_hail.setVisibility(View.VISIBLE);
        } else {
//            txt_hail.setVisibility(View.INVISIBLE);

            txt_hail.setText("1");
            showmyhail(txt_hail, type);
        }
    }

    private void addQue() {
        ClaimSqlLiteDbHelper db = new ClaimSqlLiteDbHelper(this);
        QueModel queModel = new QueModel();
        queModel.setName(txtalphaname.getText().toString());

        if (btnimgmacro.getVisibility() != View.VISIBLE) {
            queModel.setType(btnimgmacro.getTag().toString());
            if (queModel.getType().equalsIgnoreCase("Interior")) {
                queModel.setMacroName(btnInteriorMacro.getText().toString());
                queModel.setM1(btnInteriorMenu.getText().toString());
            } else if (queModel.getType().equalsIgnoreCase("Initial")) {
                queModel.setMacroName(btnimgmacrosub.getText().toString());
            } else if (queModel.getType().equalsIgnoreCase("Hail")) {
                queModel.setMacroName(btnimgmacrosub.getText().toString());
                queModel.setM1(btnhailmenu1.getText().toString());
                queModel.setM2(btndamagetype1.getText().toString());
            } else if (queModel.getType().equalsIgnoreCase("Roof")) {
                queModel.setMacroName(btnimgmacrosub.getText().toString());
                if (queModel.getMacroName().equalsIgnoreCase("layers")) {
                    queModel.setM1(btnLayersmenu.getText().toString());
                    queModel.setM2(btnLayersmenu1.getText().toString());
                } else if (queModel.getMacroName().equalsIgnoreCase("pitch")) {
                    queModel.setM1(lblpitchvalue.getText().toString().trim());
                } else if (queModel.getMacroName().equalsIgnoreCase("Shingle")) {
                    queModel.setM1(btnshinglemenu1.getText().toString());
                    queModel.setM2(btnshinglemenu2.getText().toString());
                } else if (queModel.getMacroName().equalsIgnoreCase("Gutter")) {
                    queModel.setM1(btnguttermenu1.getText().toString());
                } else if (queModel.getMacroName().equalsIgnoreCase("Overhang")) {
                    queModel.setM1(btnoverhangmenu1.getText().toString());
                } else if (queModel.getMacroName().equalsIgnoreCase("Type of siding")) {
                    queModel.setM1(btnTypesOfSidingMenu1.getText().toString());
                    queModel.setM2(btnTypesOfSidingMenu2.getText().toString());
                }
            }
        } else {
            queModel.setType("none");
            queModel.setFolderName(btncat.getText().toString());
            queModel.setFolderSubName(btnabc.getText().toString());
            queModel.setRei(btnrei.getText().toString());
            queModel.setDamage(btndamagetype.getTag().toString().equals("0") ? "Blank" : btndamagetype.getText().toString());
            queModel.setMaterial(btnmaterial.getText().toString());
            queModel.setOcb(btnocb.getText().toString());
            if (btnrei.getText().toString().equalsIgnoreCase("i")) {
                queModel.setArea(btnarea.getText().toString());
                queModel.setSlop(btniteriortype.getText().toString());
            } else {
                queModel.setSlop(strfencegame);
                queModel.setArea("");
            }
        }
        db.addQue(queModel);
        setQueCount();
        Toast.makeText(this, "Que add success", Toast.LENGTH_SHORT).show();
    }

    private void setQueCount() {
        ClaimSqlLiteDbHelper db = new ClaimSqlLiteDbHelper(this);
        int cnt = db.getQue().size();
        txtQueCount.setText(cnt + "");
        if (cnt > 0)
            txtQueCount.setVisibility(View.VISIBLE);
        else
            txtQueCount.setVisibility(View.INVISIBLE);
        db.close();
    }

    private void nextQue() {
        if (++queSelectedPos < arrayListQue.size())
            applyQue(arrayListQue.get(queSelectedPos));
    }

    private int queSelectedPos = 0;

    private void getQue() {
        final ClaimSqlLiteDbHelper db = new ClaimSqlLiteDbHelper(this);
        arrayListQue = db.getQue();
        PopupMenu popupMenu = new PopupMenu(this, btnQue);
        for (int i = 0; i < arrayListQue.size(); i++) {
            popupMenu.getMenu().add(Menu.NONE, i, Menu.NONE, arrayListQue.get(i).getName());
        }

        if (arrayListQue.size() > 0)
            popupMenu.getMenu().add(Menu.NONE, arrayListQue.size(), Menu.NONE, "Clear Que");

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getTitle().equals("Clear Que")) {
                    db.removeAllQue();
                    setQueCount();
                    Toast.makeText(mContext, "Que Cleared.", Toast.LENGTH_SHORT).show();
                } else {
                    queSelectedPos = item.getItemId();

                    applyQue(arrayListQue.get(item.getItemId()));
                }
                return true;
            }
        });

        setQueCount();

        if (arrayListQue.size() > 0) {
            popupMenu.show();
        } else {
            Toast.makeText(this, "Que not found.", Toast.LENGTH_SHORT).show();
        }
    }

    private void applyQue(QueModel queModel) {
        btnQue.setTag(queModel.getId());
        setQueCount();
        if (rlblankback.getVisibility() == View.VISIBLE) {
            onClick(rlblankback);
        }
        if (rlriskphoto.getVisibility() == View.VISIBLE) {
            onClick(rlBack);
        }
        if (rllayerphoto.getVisibility() == View.VISIBLE) {
            onClick(rllayerBack);
        }
        if (rlpitchphoto.getVisibility() == View.VISIBLE)
            onClick(rlpitchBack);
        if (rlshinglephoto.getVisibility() == View.VISIBLE)
            onClick(rlshingleBack);
        if (rlgutterphoto.getVisibility() == View.VISIBLE)
            onClick(rlgutterBack);
        if (rloverhangphoto.getVisibility() == View.VISIBLE)
            onClick(rlOverhangBack);
        if (rlcostm.getVisibility() == View.VISIBLE)
            onClick(rlcostmback);
        if (rlfeo.getVisibility() == View.VISIBLE)
            onClick(rlfeoback);
        if (rlhail.getVisibility() == View.VISIBLE)
            onClick(rlhailback);
        if (rlInteriorPhoto.getVisibility() == View.VISIBLE)
            onClick(rlInteriorBack);
        if (rlhailoptionview.getVisibility() == View.VISIBLE)
            onClick(rlhailoptionback);

        if (!queModel.getType().equalsIgnoreCase("none")) {
            btnimgmacro.setTag(queModel.getType());

            txtalphaname.setVisibility(View.VISIBLE);
                    /*if (!btnabc.getText().toString().equalsIgnoreCase("none"))
                        txtalphaname2.setVisibility(View.VISIBLE);*/

            btntype.setVisibility(View.INVISIBLE);
            btnnodamages.setVisibility(View.INVISIBLE);
            btno.setVisibility(View.INVISIBLE);
            btnocb.setVisibility(View.INVISIBLE);
            btntype2.setVisibility(View.INVISIBLE);

            if (queModel.getType().equalsIgnoreCase("Interior")) {
                showInteriorView();
                btnocb.setText("B");
                btnInteriorMacro.setTag("2");

                if (queModel.getMacroName().equalsIgnoreCase("Room Overview"))
                    btnInteriorMacro.setTag("1");
                btnInteriorMacro.setText(queModel.getMacroName());
                btnInteriorMenu.setText(queModel.getM1());
            } else if (queModel.getType().equalsIgnoreCase("Initial")) {
                nextPhoto(queModel.getMacroName());
                rliteriortype.setVisibility(View.GONE);
                btnimgmacrosub.setText(queModel.getMacroName());
                btnimgmacrosub.setVisibility(View.VISIBLE);
                btnimgmacro.setVisibility(View.INVISIBLE);
            } else if (queModel.getType().equalsIgnoreCase("Hail")) {
                btnimgmacro.setText(queModel.getType());
                btnimgmacro.setVisibility(View.INVISIBLE);
                btnimgmacrosub.setText(queModel.getMacroName().trim());
                btnimgmacrosub.setVisibility(View.VISIBLE);
                setHailItem();
                btnhailmenu1.setText(queModel.getM1());
                btndamagetype1.setText(queModel.getM2());
            } else if (queModel.getType().equalsIgnoreCase("Roof")) {
                btnimgmacro.setText(queModel.getType());
                btnimgmacro.setVisibility(View.INVISIBLE);
                btnimgmacrosub.setText(queModel.getMacroName());
                btnimgmacrosub.setVisibility(View.VISIBLE);

                nextPhoto(queModel.getMacroName());

                if (queModel.getMacroName().equalsIgnoreCase("layers")) {
                    btnLayersmenu.setText(queModel.getM1());
                    btnLayersmenu1.setText(queModel.getM2());
                } else if (queModel.getMacroName().equalsIgnoreCase("pitch")) {
                    lblpitchvalue.setText(queModel.getM1());
                } else if (queModel.getMacroName().equalsIgnoreCase("Shingle")) {
                    btnshinglemenu1.setText(queModel.getM1());
                    btnshinglemenu2.setText(queModel.getM2());
                } else if (queModel.getMacroName().equalsIgnoreCase("Gutter")) {
                    btnguttermenu1.setText(queModel.getM1());
                } else if (queModel.getMacroName().equalsIgnoreCase("Overhang")) {
                    btnoverhangmenu1.setText(queModel.getM1());
                } else if (queModel.getMacroName().equalsIgnoreCase("Type of siding")) {
                    btnTypesOfSidingMenu1.setText(queModel.getM1());
                    btnTypesOfSidingMenu2.setText(queModel.getM2());
                }
            }
        } else {
            btncat.setText(queModel.getFolderName());
            btnabc.setText(queModel.getFolderSubName());
            btnrei.setText(queModel.getRei());

            if (queModel.getRei().equalsIgnoreCase("r"))
                btnrei.setTag("3");
            else if (queModel.getRei().equalsIgnoreCase("e"))
                btnrei.setTag("1");
            else
                btnrei.setTag("2");

            if (btnrei.getText().toString().equalsIgnoreCase("i")) {
                btnarea.setText(queModel.getArea());
                btnareatogal.setText(queModel.getArea());
                onClick(btnrei);

                btniteriortype.setText(queModel.getSlop());
                if (btniteriortype.getText().toString().equalsIgnoreCase("room"))
                    btniteriortype.setTag("0");
                else
                    btniteriortype.setTag("1");
            } else {
                onClick(btnrei);
                strfencegame = queModel.getSlop();
                if (strfencegame.equals("Front Run"))
                    onClick(imgtop);
                else if (strfencegame.equals("Rear Run"))
                    onClick(imgbottom);
                else if (strfencegame.equals("Left Run"))
                    onClick(imgleft);
                else if (strfencegame.equals("Right Run"))
                    onClick(imgright);
            }

            btnmaterial.setTag("0");
            btnmaterial.setText(queModel.getMaterial());
            if (!btnmaterial.getText().toString().equalsIgnoreCase("Material")) {
                btnmaterial.setTag("1");
                btntype2.setVisibility(View.VISIBLE);
                btntype2.setText(btnmaterial.getText().toString());
                btntype2.setBackgroundResource(R.drawable.red_button_background);
                btntype2.setTag("2");
            }

            btnocb.setText(queModel.getOcb());
            if (btnocb.getText().equals("C")) {
                btnocb.setTag("2");
                strboc = "2";
                strboctype = "Close up";
            } else if (btnocb.getText().equals("B")) {
                btnocb.setTag("3");
                strboc = "";
                strboctype = "Blank";
            } else if (btnocb.getText().equals("O")) {
                btnocb.setTag("1");
                strboc = "1";
                strboctype = "Overview";
            }

            btndamagetype.setText(queModel.getDamage().equals("Blank") ? "Damage" : queModel.getDamage());
            btndamagetype.setTag(queModel.getDamage().equals("Blank") ? "0" : "1");
            btntype.setVisibility(View.INVISIBLE);

            if (!queModel.getDamage().equalsIgnoreCase("blank")) {
                btntype.setText(queModel.getDamage());
                btntype.setVisibility(View.VISIBLE);
                btntype.setBackgroundResource(R.drawable.red_button_background);
                btntype.setTag("2");

                btnnodamages.setBackgroundResource(R.drawable.button_background);
                btnnodamages.setTag("1");
                btno.setBackgroundResource(R.drawable.button_background);
                btno.setTag("1");
            }
        }
        getalphaname();
    }

    @Override
    public boolean onLongClick(View view) {
        if (view.getId() == btnQue.getId()) {
            addQue();
        }
        return true;
    }

    public class SingleMediaScanner implements MediaScannerConnection.MediaScannerConnectionClient {

        private MediaScannerConnection mMs;
        private File mFile;

        public SingleMediaScanner(Context context, File f) {
            mFile = f;
            mMs = new MediaScannerConnection(context, this);
            mMs.connect();
        }

        public void onMediaScannerConnected() {
            mMs.scanFile(mFile.getAbsolutePath(), null);
        }

        public void onScanCompleted(String path, Uri uri) {

            if (opengallery) {

                /*
               String lastpath = lastpathpf.getString("lastimgpath","");

                File imgfile = new File(lastpath);

                if(!lastpath.equals(""))
                {
                    if(imgfile.exists())
                    {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setDataAndType(Uri.parse("file://"+lastpath), "image/*");
                        startActivity(intent);
                    }
                    else
                    {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(uri);
                        startActivity(intent);
                    }
                }
                else
                {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(uri);
                    startActivity(intent);

                }*/


                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(uri);
                startActivity(intent);
                mMs.disconnect();
                opengallery = false;
            }
        }
    }

    public boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    public Bitmap loadBitmapFromView(View v) {
        Bitmap bitmap;
        v.setDrawingCacheEnabled(true);
        bitmap = Bitmap.createBitmap(v.getDrawingCache());
        v.setDrawingCacheEnabled(false);
        return bitmap;
    }

    public void turnOnFlash() {
        if (!isFlashOn) {
            parameters = camera.getParameters();
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            camera.setParameters(parameters);
            if (!isCameraOn) {
                camera.startPreview();
                isCameraOn = true;
            }
            isFlashOn = true;
        }
    }

    public void turnOffFlash() {
        if (isFlashOn) {
            parameters = camera.getParameters();
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            camera.setParameters(parameters);
            if (!isCameraOn) {
                camera.startPreview();
                isCameraOn = true;
            }
            isFlashOn = false;
        }
    }

    private void synchronizeServer() {
        rlsetting.setVisibility(View.GONE);
        if (!Utility.haveInternet(mContext, true))
            return;

        try {
            JSONObject jsonObjectMain = new JSONObject();

            JSONArray jsonArray = new JSONArray();
            for (HashMap<String, String> hashMap : claimDbHelper.getRoof()) {
                JSONObject obj = new JSONObject();
                obj.put("userId", hashMap.get("userId"));
                obj.put("claimId", hashMap.get("claimId"));
                obj.put("material", hashMap.get("material"));
                obj.put("qty", hashMap.get("qty"));
                obj.put("slope", hashMap.get("slope"));
                obj.put("damage", hashMap.get("damage"));
                jsonArray.put(obj);
            }
            jsonObjectMain.put("Roof", jsonArray);

            jsonArray = new JSONArray();
            for (HashMap<String, String> hashMap : claimDbHelper.getElevation()) {
                JSONObject obj = new JSONObject();
                obj.put("userId", hashMap.get("userId"));
                obj.put("claimId", hashMap.get("claimId"));
                obj.put("material", hashMap.get("material"));
                obj.put("qty", hashMap.get("qty"));
                obj.put("elevation", hashMap.get("elevation"));
                obj.put("damage", hashMap.get("damage"));
                jsonArray.put(obj);
            }
            jsonObjectMain.put("Elevation", jsonArray);

            jsonArray = new JSONArray();
            for (HashMap<String, String> hashMap : claimDbHelper.getInteriorArea()) {
                JSONObject obj = new JSONObject();
                obj.put("userId", hashMap.get("userId"));
                obj.put("claimId", hashMap.get("claimId"));
                obj.put("areaType", hashMap.get("areaType"));
                obj.put("material", hashMap.get("material"));
                obj.put("insulation", hashMap.get("insulation"));
                obj.put("qty", hashMap.get("qty"));

                jsonArray.put(obj);
            }
            jsonObjectMain.put("InteriorArea", jsonArray);

            jsonArray = new JSONArray();
            for (HashMap<String, String> hashMap : claimDbHelper.getRoofLayer()) {
                JSONObject obj = new JSONObject();
                obj.put("userId", hashMap.get("userId"));
                obj.put("claimId", hashMap.get("claimId"));
                obj.put("noLayer", hashMap.get("noLayer"));
                obj.put("layerType", hashMap.get("layerType"));
                jsonArray.put(obj);
            }
            jsonObjectMain.put("RoofLayer", jsonArray);

            jsonArray = new JSONArray();
            for (HashMap<String, String> hashMap : claimDbHelper.getRoofPitch()) {
                JSONObject obj = new JSONObject();
                obj.put("userId", hashMap.get("userId"));
                obj.put("claimId", hashMap.get("claimId"));
                obj.put("pitchValue", hashMap.get("pitchValue"));
                obj.put("slope", hashMap.get("slope"));
                jsonArray.put(obj);
            }
            jsonObjectMain.put("RoofPitch", jsonArray);

            jsonArray = new JSONArray();
            for (HashMap<String, String> hashMap : claimDbHelper.getRoofShingle()) {
                JSONObject obj = new JSONObject();
                obj.put("userId", hashMap.get("userId"));
                obj.put("claimId", hashMap.get("claimId"));
                obj.put("years", hashMap.get("years"));
                obj.put("tab", hashMap.get("tab"));
                jsonArray.put(obj);
            }
            jsonObjectMain.put("RoofShingle", jsonArray);

            jsonArray = new JSONArray();
            for (HashMap<String, String> hashMap : claimDbHelper.getRiskMacroDes()) {
                JSONObject obj = new JSONObject();
                obj.put("userId", hashMap.get("userId"));
                obj.put("claimId", hashMap.get("claimId"));
                obj.put("story", hashMap.get("story"));
                obj.put("typeOfConstruction", hashMap.get("typeOfConstruction"));
                obj.put("rci", hashMap.get("rci"));
                obj.put("singleFamily", hashMap.get("singleFamily"));
                obj.put("garageAttached", hashMap.get("garageAttached"));
                obj.put("typeOfExteriorSiding", hashMap.get("typeOfExteriorSiding"));
                jsonArray.put(obj);
            }
            jsonObjectMain.put("RiskMacroDes", jsonArray);

            String parm = jsonObjectMain.toString();
            Log.i(TAG, "SynchronizeParam = " + parm);

            Utility.showProgress(mContext);
            ApiClient.getClient().create(APIInterface.class).synchronization(parm).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    Utility.dismissProgress();
                    Log.i(TAG, "synchronizationRes = " + response.body());
                    Toast.makeText(mContext, "Synchronize Success.", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Utility.dismissProgress();
                    Log.i(TAG, "synchronizationError = " + t.toString());
                }
            });
        } catch (Exception e) {
        }
    }

    private void logout() {

        if (Utility.haveInternet(mContext, true)) {
            Utility.showProgress(mContext);
            ApiClient.getClient().create(APIInterface.class).logout(PrefManager.getUserId(), "cam").enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    Utility.dismissProgress();
                    Log.i(TAG, "logoutRes = " + response.body());

                    if (response.body() == null) {
                        Utility.errorDialog(mContext, getString(R.string.error_data_not_found));
                        return;
                    }

                    try {
                        JSONObject jsonObject = new JSONObject(response.body());
                        Toast.makeText(mContext, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        if (jsonObject.getString("success").equals("success")) {
                            PrefManager.logout();
                            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                            HomeActivity.this.finish();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Utility.dismissProgress();
                    Log.i(TAG, "logoutError = " + t.toString());
                }
            });
        }
    }

    public boolean checkPermission() {
        return (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) || ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED;
    }

    private void addDocument() {
        rlsetting.setVisibility(View.GONE);
        final CharSequence[] options = {"Take Photo", "Choose From Gallery", "Cancel"};
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle("Select Option");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    dialog.dismiss();

                    if (checkPermission()) {
                        ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 101);
                    } else {
//                        Intent intent = new Intent(HomeActivity.this, ScanActivity.class);
//                        intent.putExtra(ScanConstants.OPEN_INTENT_PREFERENCE, ScanConstants.OPEN_CAMERA);
//                        startActivityForResult(intent, 151);
                    }
                } else if (options[item].equals("Choose From Gallery")) {
                    dialog.dismiss();

                    if (checkPermission()) {
                        ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 101);
                    } else {
//                        Intent intent = new Intent(HomeActivity.this, ScanActivity.class);
//                        intent.putExtra(ScanConstants.OPEN_INTENT_PREFERENCE, ScanConstants.OPEN_MEDIA);
//                        startActivityForResult(intent, 151);
                    }
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_CODE_SPEECH_INPUT) {
            if (resultCode == RESULT_OK && null != data) {
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                txtalphaname.setText(result.get(0));
                btnMic.setTag("1");
            }
        }
//        } else if (requestCode == 151 && resultCode == Activity.RESULT_OK) {
//            Uri uri = data.getExtras().getParcelable(ScanConstants.SCANNED_RESULT);
//            Bitmap bitmap = null;
//            try {
//                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
//                getContentResolver().delete(uri, null, null);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            String FolderTimeStamp = folderdateFormat.format(new Date());
//            String mydirpath = mydir.getAbsolutePath().toString();
//            if (!mydirpath.contains(FolderTimeStamp.toString())) {
//                String[] arr = mydirpath.split("/");
//
//                String fname = arr[arr.length - 1];
//                String Save_file_name = fname + " " + FolderTimeStamp;
//
//                mydirpath = mydirpath.replace(fname, Save_file_name);
//
//                mydir = new File(mydirpath);
//            }
//
//            if (!mydir.exists())
//                mydir.mkdirs();
//
//            File f = mydir;
//            if (btnabc.getText().toString().equals("None")) {
//                savefile1 = f;
//            } else {
//                f = new File(f, btncat.getText().toString() + " " + btnabc.getText().toString());
//
//                if (!f.exists())
//                    f.mkdirs();
//                else
//                    Log.d("error", "dir. already exists");
//
//            }
//
//            f = new File(f, "Documents");
//            if (!f.exists()) {
//                f.mkdir();
//            }
//
//            f = new File(f, "doc_" + Calendar.getInstance().getTimeInMillis() + ".jpg");
//            if (!f.exists()) {
//                try {
//                    f.createNewFile();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//            savefile = f;
//            FileOutputStream outStream = null;
//            try {
//                outStream = new FileOutputStream(savefile.getAbsolutePath());
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 40, outStream);
//                outStream.close();
//                Toast.makeText(this, "Document save " + savefile.getAbsolutePath(), Toast.LENGTH_LONG).show();
//                new SingleMediaScanner(HomeActivity.this, savefile);
////                refereshgallery();
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//                Log.e("_claim", "error = " + e.toString());
//            } catch (IOException e) {
//                e.printStackTrace();
//                Log.e("_claim", "error = " + e.toString());
//            }
//        }
    }

    String areaMaterial = "Material", roofMaterial = "Material", elevationMaterial = "Material";
    String imgMicName = "";

    public void onClick(View v) {

        int vid = v.getId();
        if (vid == btnrei.getId()) {
            clickonView("btnrei");

            onclick_R_E_I();

        }
        else if (vid == btnr.getId())
        {
            clickonView("btnr");

            if(btnr.getTag().equals("1"))
            {
                btnrei.setTag("3");
                onclick_R_E_I();
            }
            else
            {
                btnrei.setTag("2");
                onclick_R_E_I();
            }

        }
        else if (vid == rl_defaultmenu.getId())
        {
//            ll_default_button_submenu

                if(ll_default_button_submenu.getVisibility() == View.VISIBLE)
                {
                    ll_default_button_submenu.setVisibility(View.GONE);
                }
                else
                {
                    ll_default_button_submenu.setVisibility(View.VISIBLE);
                }
        }
        else if (vid == btne.getId()) {

            clickonView("btne");

            if(btne.getTag().equals("1"))
            {

                btnrei.setTag("1");
                onclick_R_E_I();

            }
            else
            {
                btnrei.setTag("2");
                onclick_R_E_I();
            }


        }

        else if (vid == btnQue.getId()) {
            clickonView("btnQue");


            getQue();
        } else if (vid == btncat.getId()) {
            clickonView("btncat");

            showclaimnameoption(true);
        } else if (vid == btnabc.getId()) {
            clickonView("btnabc");

            showabcoption();
        } else if (vid == btnsubmenu.getId()) {
            clickonView("btnsubmenu");
            getcostammenu(true);
        } else if (vid == btndamagetype.getId()) {
            clickonView("btndamagetype");

            showdamagetypeoption(true);


        }
        else if (vid == btnnodamagetype.getId()) {
            clickonView("btnnodamagetype");

            btnhailnodamages.performClick();
        }
        else if (vid == btndamagetype1.getId()) {
            clickonView("btndamagetype1");

            showdamagetypeoption1();
        }     else if (vid == btnmaterial1.getId()) {
            clickonView("btnmaterial1");

            showmaterialoption1();
        }
        else if (vid == btnareatogal.getId()) {


            clickonView("btnareatogal");


            if (btnareatogal.getTag().equals("1")) {
                btnareatogal.setBackgroundResource(R.drawable.red_button_background);
                btnareatogal.setTag("2");
                btnarea.setText(strselectarea);
                btnmaterial.setTag("1");
                btnmaterial.setText(strselectsubarea);
                btnmaterial.setVisibility(View.VISIBLE);

                /*if (!btnmaterial.getTag().toString().equalsIgnoreCase("0")) {
//                    btnSubAreaTogal.setVisibility(View.VISIBLE);
                    btnmaterial.setTag("1");
                    btnmaterial.setText(btnSubAreaTogal.getText().toString());
                    btnSubAreaTogal.setTag("2");
                    btnSubAreaTogal.setBackgroundResource(R.drawable.red_button_background);
                }*/
//                btnnodamages.setBackgroundResource(R.drawable.button_background);
//                btnnodamages.setTag("1");
            } else if (btnareatogal.getTag().equals("2")) {
                btnareatogal.setBackgroundResource(R.drawable.button_background);
                btnareatogal.setTag("1");

                strselectarea = btnarea.getText().toString();
                strselectsubarea = btnmaterial.getText().toString();
                btnarea.setText("Area");
//                btnmaterial.setTag("0");

                /*if (btnarea.getText().toString().equalsIgnoreCase("floor"))
                    btnmaterial.setText("Carpet");
                else
                    btnmaterial.setText("Drywall");*/

//                btnSubAreaTogal.setTag("1");
//                btnSubAreaTogal.setBackgroundResource(R.drawable.button_background);
            }
        } else if (vid == btnSubAreaTogal.getId()) {

            clickonView("btnSubAreaTogal");

            /*if (btnareatogal.getTag().equals("1")) {
                return;
            }*/
//            if (btnmaterial.getTag().equals("0")) {
////                return;
////            }
            if (btnSubAreaTogal.getTag().toString().equals("1")) {
                btnmaterial.setTag("1");
                btnmaterial.setText(btnSubAreaTogal.getText().toString());
                btnSubAreaTogal.setTag("2");
                btnSubAreaTogal.setBackgroundResource(R.drawable.red_button_background);
            } else {
                btnmaterial.setTag("0");

                if (btnarea.getText().toString().equalsIgnoreCase("floor"))
                    btnmaterial.setText("Carpet");
                else
                    btnmaterial.setText("Drywall");
                btnSubAreaTogal.setTag("1");
                btnSubAreaTogal.setBackgroundResource(R.drawable.button_background);
            }
            getalphaname();
        } else if (vid == btnarea.getId()) {
            clickonView("btnarea");

            showAreaoption();
        } else if (vid == btnInsulation.getId()) {
            clickonView("btnInsulation");

            showInsulationOption();
        } else if (vid == btnQty.getId()) {
            clickonView("btnQty");

            showQuantityOption();
        } else if (vid == btnCeiling.getId() || vid == btnWall.getId() || vid == btnFloor.getId()) {

            if (vid == btnCeiling.getId()) {



                clickonView("btnCeiling");

                btnarea.setText("Ceiling");
                btnarea.setTag("Ceiling");
                btnWall.setBackgroundResource(R.drawable.button_background);
                btnFloor.setBackgroundResource(R.drawable.button_background);
                //  btnCeiling.setBackgroundResource(R.drawable.red_button_background);
                btnWall.setTag("1");
                btnFloor.setTag("1");
//                btnCeiling.setTag("1");

                if (btnCeiling.getTag().equals("1")) {

                    btnCeiling.setBackgroundResource(R.drawable.red_button_background);
                    btnCeiling.setTag("2");
                    btnmatrialsubmenu.setText("0");



                } else if (btnCeiling.getTag().equals("2"))  {

                    btnCeiling.setBackgroundResource(R.drawable.button_background);
                    btnCeiling.setTag("1");
                    strselectarea = btnarea.getText().toString();
                    strselectsubarea = btnmaterial.getText().toString();
                    btnarea.setText("Area");


                }

            } else if (vid == btnWall.getId())
            {
                clickonView("btnWall");

                btnarea.setText("Wall");
                btnarea.setTag("Wall");
                // btnWall.setBackgroundResource(R.drawable.red_button_background);
                btnCeiling.setBackgroundResource(R.drawable.button_background);
                btnFloor.setBackgroundResource(R.drawable.button_background);
                btnCeiling.setTag("1");
                btnFloor.setTag("1");
//                btnWall.setTag("1");

                if (btnWall.getTag().equals("1")) {
                    btnWall.setBackgroundResource(R.drawable.red_button_background);
                    btnWall.setTag("2");
                    btnmatrialsubmenu.setText("0");




                }  else if (btnWall.getTag().equals("2"))  {

                    btnWall.setBackgroundResource(R.drawable.button_background);
                    btnWall.setTag("1");
                    strselectarea = btnarea.getText().toString();
                    strselectsubarea = btnmaterial.getText().toString();
                    btnarea.setText("Area");


                }
            } else {


                clickonView("btnFloor");


                btnarea.setText("Floor");
                btnarea.setTag("Floor");
                //    btnFloor.setBackgroundResource(R.drawable.red_button_background);
                btnCeiling.setBackgroundResource(R.drawable.button_background);
                btnWall.setBackgroundResource(R.drawable.button_background);
                btnCeiling.setTag("1");
                btnWall.setTag("1");
//                btnFloor.setTag("1");

                if (btnFloor.getTag().equals("1")) {
                    btnFloor.setBackgroundResource(R.drawable.red_button_background);
                    btnFloor.setTag("2");
                    btnmatrialsubmenu.setText("0");



                }  else if (btnFloor.getTag().equals("2"))  {

                    btnFloor.setBackgroundResource(R.drawable.button_background);
                    btnFloor.setTag("1");
                    strselectarea = btnarea.getText().toString();
                    strselectsubarea = btnmaterial.getText().toString();
                    btnarea.setText("Area");


                }
            }

            btnareatogal.setText(btnarea.getText().toString());
            btnareatogal.setBackgroundResource(R.drawable.red_button_background);
            btnareatogal.setTag("2");

            btnmaterial.setTag("0");
            if (btnarea.getText().toString().equalsIgnoreCase("floor"))
                btnmaterial.setText("Carpet");
            else
                btnmaterial.setText("Drywall");

            btnSubAreaTogal.setVisibility(View.VISIBLE);
            btnSubAreaTogal.setText(btnmaterial.getText().toString());
            btnSubAreaTogal.setTag("1");
            btnSubAreaTogal.setBackgroundResource(R.drawable.button_background);

            strselectarea = btnareatogal.getText().toString();

            btnmaterial.setVisibility(View.VISIBLE);
            btnareatogal.setVisibility(View.GONE);

            getalphaname();

            btnSubAreaTogal.setTag("1");
            btnSubAreaTogal.performClick();

        } else if (vid == btnhailgo.getId()) {

            clickonView("btnhailgo");

            no_slopeimg = 0;
            hailimgcount = 0;

            no_frontslopeimg = 0;
            no_rightslopeimg = 0;
            no_rearslopeimg = 0;
            no_leftslopeimg = 0;

            chkfrontslopeimg = false;
            chkrightslopeimg = false;
            chkrearslopeimg = false;
            chkleftslopeimg = false;

            btnhailskip.setText("Skip");


            if (rbtn_frontslope.isChecked()) {
                no_frontslopeimg = Integer.parseInt(txt_frontslope.getText().toString().toString());
                chkfrontslopeimg = true;

            } else {
                chkfrontslopeimg = false;
            }

            if (rbtn_rightslope.isChecked()) {
                no_rightslopeimg = Integer.parseInt(txt_rightslope.getText().toString().toString());
                chkrightslopeimg = true;

            } else {
                chkrightslopeimg = false;
            }

            if (rbtn_rearslope.isChecked()) {
                no_rearslopeimg = Integer.parseInt(txt_rearslope.getText().toString().toString());
                chkrearslopeimg = true;

            } else {
                chkrearslopeimg = false;
            }

            if (rbtn_leftslope.isChecked()) {
                no_leftslopeimg = Integer.parseInt(txt_leftslope.getText().toString().toString());
                chkleftslopeimg = true;

            } else {
                chkleftslopeimg = false;
            }


            no_frontslopeimg = 4;// no_frontslopeimg + 2;
            no_rightslopeimg = 4;//no_rightslopeimg + 2;
            no_rearslopeimg = 4;//no_rearslopeimg + 2;
            no_leftslopeimg = 4;//no_leftslopeimg + 2;


//            no_frontslopeimg =  no_frontslopeimg + 3;
//            no_rightslopeimg = no_rightslopeimg + 3;
//            no_rearslopeimg = no_rearslopeimg + 3;
//            no_leftslopeimg = no_leftslopeimg + 3;


            if (rbtn_frontslope.isChecked()) {
                btnhailtype.setText("Front Slope");
                no_slopeimg = no_frontslopeimg;

            } else if (rbtn_rightslope.isChecked()) {
                btnhailtype.setText("Right Slope");
                no_slopeimg = no_rightslopeimg;


            } else if (rbtn_rearslope.isChecked()) {
                btnhailtype.setText("Rear Slope");
                no_slopeimg = no_rearslopeimg;


            } else if (rbtn_leftslope.isChecked()) {
                btnhailtype.setText("Left Slope");
                no_slopeimg = no_leftslopeimg;

            }

            no_slopeimg = 6;


//            if(no_slopeimg > 6)
//            {
//                no_slopeimg = 6;
//            }

//            btnhailtype.setVisibility(View.INVISIBLE);
            rlhailoptionview.setVisibility(View.GONE);
            btnimgmacrosub.setVisibility(View.VISIBLE);
            btnimgmacrosub.setText("Overview");
            hidehailoption();
        } else if (vid == btnpitchmenu.getId()) {
            clickonView("btnpitchmenu");

            showpitchmenu(btnpitchmenu);

        } else if (vid == btnocb.getId())
        {
            clickonView("btnocb");


          selectocb();

        } else if (vid == btnmaterial.getId()) {
            clickonView("btnmaterial");

            showmaterialoption(true);
        } else if (vid == btnrisk.getId()) {
            clickonView("btnrisk");

//            getMacrosname();


        } else if (vid == btnInteriorMacro.getId()) {

            clickonView("btnInteriorMacro");

            getMacroInterior();
            btno.setVisibility(View.VISIBLE);//dmakchange
            btnocb.setVisibility(View.INVISIBLE);
        } else if (vid == btnInteriorMenu.getId()) {
            clickonView("btnInteriorMenu");


            setInteriorDropdown();
        } else if (vid == rlInteriorBack.getId()) {

            clickonView("rlInteriorBack");




            showoption();
            rlInteriorPhoto.setVisibility(View.GONE);
            btnInteriorMacro.setVisibility(View.GONE);
            btnnodamages.setVisibility(View.VISIBLE);
            btno.setVisibility(View.VISIBLE);//dmakchange
            btnocb.setVisibility(View.INVISIBLE);
            btnimgmacro.setVisibility(View.VISIBLE);
            btnimgmacro.setText("Macro");

            getalphaname();
            if (btnrei.getText().toString().equals("I")) {
                rliteriortype.setVisibility(View.VISIBLE);
            } else {
                rliteriortype.setVisibility(View.GONE);
            }
        } else if (vid == btnInteriorSkip.getId()) {
            clickonView("btnInteriorSkip");

            skipInterior();

        }
        else if(vid == btnscope.getId())
        {
            clickonView("btnscope");

            rlsetting.setVisibility(View.GONE);
            Intent act_ScopeName = new Intent(HomeActivity.this,ScopeName.class);
            startActivity(act_ScopeName);
        }
        else if (vid == rlNewMacroBack.getId()) {
            clickonView("rlNewMacroBack");

            showoption();
            rlNewMacro.setVisibility(View.GONE);
            btnnodamages.setVisibility(View.VISIBLE);
            btno.setVisibility(View.VISIBLE);//dmakchange
            btnocb.setVisibility(View.INVISIBLE);
            btnimgmacro.setVisibility(View.VISIBLE);
            btnimgmacro.setText("Macro");

            getalphaname();
            if (btnrei.getText().toString().equals("I")) {
                rliteriortype.setVisibility(View.VISIBLE);
            } else {
                rliteriortype.setVisibility(View.GONE);
            }
        } else if (vid == btnNewMacroSkip.getId()) {
            clickonView("btnNewMacroSkip");

            skipNewMacro();
        } else if (vid == btnNewMacroDamage.getId()) {
            clickonView("btnNewMacroDamage");

            newMacroDamage();
        } else if (vid == btnTypesOfSidingMenu1.getId()) {
            clickonView("btnTypesOfSidingMenu1");

            showTypeOfSidingMenu1Option();
        } else if (vid == btnTypesOfSidingMenu2.getId()) {
            clickonView("btnTypesOfSidingMenu2");

            showTypeOfSidingMenu2Option();
        } else if (vid == rlTypesOfSidingBack.getId()) {
            clickonView("rlTypesOfSidingBack");

            rlTypesOfSidingPoto.setVisibility(View.GONE);
            onClick(rllayerBack);
        } else if (vid == btnTypesOfSidingSkip.getId()) {
            clickonView("btnTypesOfSidingSkip");

            nextPhoto("Roof Overview");
        } else if (vid == btnimgmacro.getId()) {
            clickonView("btnimgmacro");

            getMacrosname();
        } else if (vid == btnimgmacrosub.getId()) {
            clickonView("btnimgmacrosub");

            getMacrossubname();
        } else if (vid == btnhailmenu1.getId()) {

            clickonView("btnhailmenu1");


            if (btnimgmacrosub.getText().toString().trim().equals("Overview")) {
                btnhailmenu1.setVisibility(View.GONE);
                btnhailmenu1.setText("");
            } else if (btnimgmacrosub.getText().toString().trim().equals("test sq Overview")) {

                //change
                btnhailmenu1.setVisibility(View.GONE);
                if (btnhailtype.getText().toString().equals("Front Slope")) {
                    btnhailmenu1.setText(no_frontslope);
                } else if (btnhailtype.getText().toString().equals("Right Slope")) {
                    btnhailmenu1.setText(no_rightslope);
                } else if (btnhailtype.getText().toString().equals("Left Slope")) {
                    btnhailmenu1.setText(no_leftslope);
                } else if (btnhailtype.getText().toString().equals("Rear Slope")) {
                    btnhailmenu1.setText(no_rearslope);
                }
                showhailmenu("num");
            } else if (btnimgmacrosub.getText().toString().trim().equals("test sq")) {
                //change
                btnhailmenu1.setVisibility(View.GONE);
                if (btnhailtype.getText().toString().equals("Front Slope")) {
                    btnhailmenu1.setText(no_frontslope);
                } else if (btnhailtype.getText().toString().equals("Right Slope")) {
                    btnhailmenu1.setText(no_rightslope);
                } else if (btnhailtype.getText().toString().equals("Left Slope")) {
                    btnhailmenu1.setText(no_leftslope);
                } else if (btnhailtype.getText().toString().equals("Rear Slope")) {
                    btnhailmenu1.setText(no_rearslope);
                }
                showhailmenu("num");
            } else if (btnimgmacrosub.getText().toString().trim().equals("hail damage close up on shingles") ||
                    btnimgmacrosub.getText().toString().trim().equals("hail damage close up on shingles 2") ||
                    btnimgmacrosub.getText().toString().trim().equals("hail damage close up on shingles 3")
            ) {
                btnocb.setVisibility(View.INVISIBLE);
                btnocb.setVisibility(View.INVISIBLE);
                btndamagetype1.setVisibility(View.VISIBLE);
                btnmaterial1.setVisibility(View.GONE);

                //change
                btnhailmenu1.setVisibility(View.VISIBLE);
                btnhailmenu1.setText("Shingles");
                btnhailskip.setText("Next Slope");

                showhailmenu("Material");

            } else if (btnimgmacrosub.getText().toString().trim().equals("hail damage close up on shingles 4") ||
                    btnimgmacrosub.getText().toString().trim().equals("hail damage close up on 1")) {
                //change
                btnhailmenu1.setVisibility(View.VISIBLE);
                btnhailmenu1.setText("Vent");

                showhailmenu("Material");

            } else if (btnimgmacrosub.getText().toString().trim().equals("hail damage close up on 2")) {
                //change
                btnhailmenu1.setVisibility(View.VISIBLE);
                btnhailmenu1.setText("Furnace Cap");

                showhailmenu("Material");

            }





        }
        else if (vid == btn_replace_front.getId()) {

            Select_Replace_Repair(btn_replace_front,"1");
        }
        else if (vid == btn_replace_right.getId()) {
            Select_Replace_Repair(btn_replace_right,"2");

        }
        else if (vid == btn_replace_rear.getId()) {
            Select_Replace_Repair(btn_replace_rear,"3");

        }
        else if (vid == btn_replace_left.getId()) {
            Select_Replace_Repair(btn_replace_left,"4");

        }
        else if (vid == btn_repair_front.getId()) {
            Select_Replace_Repair(btn_repair_front,"1");

        }
        else if (vid == btn_repair_right.getId()) {
            Select_Replace_Repair(btn_repair_right,"2");

        }
        else if (vid == btn_repair_rear.getId()) {
            Select_Replace_Repair(btn_repair_rear,"3");

        }
        else if (vid == btn_repair_left.getId()) {
            Select_Replace_Repair(btn_repair_left,"4");
        }


        else if (vid == btnhailtype.getId()) {

            clickonView("btnhailtype");

            /*
            popuphailtype = new PopupMenu(this, findViewById(R.id.btnhailtype));
            popuphailtype.getMenu().add(Menu.NONE, 0, Menu.NONE, "Front Slope");
            popuphailtype.getMenu().add(Menu.NONE, 1, Menu.NONE, "Right Slope");
            popuphailtype.getMenu().add(Menu.NONE, 2, Menu.NONE, "Rear Slope");
            popuphailtype.getMenu().add(Menu.NONE, 2, Menu.NONE, "Left Slope");


            popuphailtype.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener()
            {
                @Override
                public boolean onMenuItemClick(MenuItem arg0)
                {
                    btnhailtype.setText(arg0.getTitle());
                    btnhailmenu1.setVisibility(View.GONE);
                    btnimgmacrosub.setText("Overview");

//                    getalphaname();
                    return false;
                }
            });

            popuphailtype.show();*/


        } else if (vid == btnshinglemenu1.getId()) {
            clickonView("btnshinglemenu1");

            value = 0;
            popupMenu2 = new PopupMenu(this, findViewById(R.id.btnshinglemenu1));
            opendatabase();
            SELECT_SQL = "SELECT * FROM tbl_shinglemenu1";
            Cur = DB.rawQuery(SELECT_SQL, null);
            if (Cur != null || Cur.getCount() <= 0) {
                Cur.moveToFirst();
                do {
                    String strvalue = Cur.getString(Cur.getColumnIndex("value"));
                    popupMenu2.getMenu().add(Menu.NONE, value, Menu.NONE, strvalue);
                    value++;
                } while (Cur.moveToNext());
            }
            Cur.close();
            DB.close();

            popupMenu2.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem arg0) {
                    btnshinglemenu1.setText(arg0.getTitle());
                    if (arg0.getTitle().toString().equalsIgnoreCase("25 Years")) {
                        btnshinglemenu2.setText("3 Tab");
                    } else {
                        btnshinglemenu2.setText("Dimensional");
                    }
                    getalphaname();

                    return false;
                }
            });

            popupMenu2.show();

        } else if (vid == btnLayersmenu1.getId()) {
            clickonView("btnLayersmenu1");

            popupMenu2 = new PopupMenu(this, findViewById(R.id.btnLayersmenu1));
            for (int i = 0; i < 9; i++) {
                popupMenu2.getMenu().add(Menu.NONE, i, Menu.NONE, "" + (i + 1));
            }
            popupMenu2.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem arg0) {
                    btnLayersmenu1.setText(arg0.getTitle());
                    getalphaname();

                    return false;
                }
            });
            popupMenu2.show();
        } else if (vid == btnoverhangmenu1.getId()) {
            clickonView("btnoverhangmenu1");

            popupMenu2 = new PopupMenu(this, findViewById(R.id.btnoverhangmenu1));
            for (int i = 0; i < 99; i++) {
                popupMenu2.getMenu().add(Menu.NONE, i, Menu.NONE, "" + (i + 1));
            }
            popupMenu2.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem arg0) {
                    btnoverhangmenu1.setText(arg0.getTitle());
                    getalphaname();

                    return false;
                }
            });
            popupMenu2.show();
        } else if (vid == btnshinglemenu2.getId()) {
            clickonView("btnshinglemenu2");

            value = 0;
            popupMenu2 = new PopupMenu(this, findViewById(R.id.btnshinglemenu2));
            opendatabase();
            SELECT_SQL = "SELECT * FROM tbl_shinglemenu2";
            Cur = DB.rawQuery(SELECT_SQL, null);
            if (Cur != null || Cur.getCount() <= 0) {
                Cur.moveToFirst();
                do {
                    String strvalue = Cur.getString(Cur.getColumnIndex("value"));
                    Log.e("Value==>", "" + strvalue);
                    popupMenu2.getMenu().add(Menu.NONE, value, Menu.NONE, strvalue);
                    value++;
                } while (Cur.moveToNext());
            }
            Cur.close();
            DB.close();
            popupMenu2.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem arg0) {
                    btnshinglemenu2.setText(arg0.getTitle());
                    getalphaname();

                    return false;
                }
            });
            popupMenu2.show();


        } else if (vid == btnmatrialsubmenu.getId()) {

            clickonView("btnmatrialsubmenu");

            value = 0;
            popupMenu2 = new PopupMenu(this, findViewById(R.id.btnmatrialsubmenu));
            opendatabase();
            SELECT_SQL = "SELECT * FROM tbl_matrialsubmenu";
            Cur = DB.rawQuery(SELECT_SQL, null);
            if (Cur != null || Cur.getCount() <= 0) {

                Cur.moveToFirst();
                do {
                    String strvalue = Cur.getString(Cur.getColumnIndex("value"));
                    Log.e("Value==>", "" + strvalue);
                    popupMenu2.getMenu().add(Menu.NONE, value, Menu.NONE, strvalue);
                    value++;
                } while (Cur.moveToNext());
            }
            Cur.close();
            DB.close();
            popupMenu2.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem arg0) {
                    btnmatrialsubmenu.setText(arg0.getTitle());
                    getalphaname();

                    return false;
                }
            });
            popupMenu2.show();
        } else if (vid == btnguttermenu1.getId()) {
            clickonView("btnguttermenu1");

            value = 0;
            popupMenu2 = new PopupMenu(this, findViewById(R.id.btnguttermenu1));
            opendatabase();
            SELECT_SQL = "SELECT * FROM tbl_gutter";
            Cur = DB.rawQuery(SELECT_SQL, null);
            if (Cur != null || Cur.getCount() <= 0) {

                Cur.moveToFirst();
                do {
                    String strvalue = Cur.getString(Cur.getColumnIndex("value"));
//                Log.e("Value==>", BuildConfig.FLAVOR + strvalue);
                    popupMenu2.getMenu().add(Menu.NONE, value, Menu.NONE, strvalue);
                    value++;
                } while (Cur.moveToNext());
            }
            Cur.close();
            DB.close();
            popupMenu2.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem arg0) {
                    btnguttermenu1.setText(arg0.getTitle());
                    getalphaname();

                    return false;
                }
            });
            popupMenu2.show();
        } else if (vid == btnLayersmenu.getId()) {
            clickonView("btnLayersmenu");

            value = 0;
            popupMenu2 = new PopupMenu(this, findViewById(R.id.btnLayersmenu));
            opendatabase();
            SELECT_SQL = "SELECT * FROM tbl_layermenu";
            Cur = DB.rawQuery(SELECT_SQL, null);
            if (Cur != null || Cur.getCount() <= 0) {

                Cur.moveToFirst();
                do {
                    String strvalue = Cur.getString(Cur.getColumnIndex("value"));
                    popupMenu2.getMenu().add(Menu.NONE, value, Menu.NONE, strvalue);
                    value++;
                } while (Cur.moveToNext());
            }
            Cur.close();
            DB.close();
            popupMenu2.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem arg0) {
                    btnLayersmenu.setText(arg0.getTitle());
                    getalphaname();

                    return false;
                }
            });
            popupMenu2.show();
        } else if (vid == btnskip.getId()) {
            clickonView("btnskip");

            if (btnrisk.getText().toString().trim().equals("Risk")) {
                nextPhoto("Front Elevation Overview");

            } else if (btnrisk.getText().toString().trim().equals("Risk2")) {

                btnimgmacrosub.setVisibility(View.GONE);
                btnimgmacro.setText("Macro");

                btnimgmacro.setVisibility(View.VISIBLE);
                nextPhoto("Aditional Photo");
                getalphaname();

                if (btnrei.getTag().toString().equals("3")) {
                    onClick(btnrei);
                }
                if (btnrei.getTag().toString().equals("1")) {
                    onClick(btnrei);
                }
            }
        } else if (vid == btnlayerskip.getId()) {
            clickonView("btnlayerskip");

            if (layerphoto == 0) {
                nextPhoto("Layers");
                getalphaname();
                layerphoto = 1;
                tvonrakes.setVisibility(View.VISIBLE);

            } else {
                btnpitchmenu.setVisibility(View.VISIBLE);
                btnpitchmenu.setText("Blank");
                nextPhoto("Pitch");

                layerphoto = 0;
                tvonrakes.setVisibility(View.GONE);
            }
        } else if (vid == btnpitchskip.getId()) {
            clickonView("btnpitchskip");

            btnpitchmenu.setVisibility(View.GONE);
            nextPhoto("Shingle");
        } else if (vid == btnshingleskip.getId()) {
            clickonView("btnshingleskip");

            nextPhoto("Gutter");
        } else if (vid == btngutterskip.getId()) {
            clickonView("btngutterskip");

            nextPhoto("Overhang");
        } else if (vid == btnoverhangskip.getId()) {
            clickonView("btnoverhangskip");

            nextPhoto("Type of siding");
        } else if (vid == btncostmskip.getId()) {
            clickonView("btncostmskip");

//            nextPhoto("Aditional Photo");
        } else if (vid == btnfeoskip.getId()) {
            clickonView("btnfeoskip");

            if (btnrisk.getText().toString().trim().equals("Front Elevation Overview")) {
                nextPhoto("Risk2");
                getalphaname();
            } else {
                boolean isRoof = false;
                if (btnrisk.getText().toString().equalsIgnoreCase("Roof Overview"))
                    isRoof = true;

                btnimgmacrosub.setVisibility(View.GONE);
                btnimgmacro.setText("Macro");
                btnimgmacro.setVisibility(View.VISIBLE);
                nextPhoto("Aditional Photo");
                getalphaname();

                if (isRoof) {
                    if (btnrei.getTag().toString().equals("2")) {
                        onClick(btnrei);
                    }
                    if (btnrei.getTag().toString().equals("3")) {
                        onClick(btnrei);
                    }
                }
            }
        } else if (vid == btnhailskip.getId())  {
            clickonView("btnhailskip");


            Log.e("btnhailskip===>","click");


            if (btnimgmacro.getText().toString().equals("Hail")) {
                hailimgcount++;
            }





            if (btnhailskip.getText().toString().trim().equals("Next Slope")) {


                btnocb.setTag("3");
        selectocb();
                strboc = "1";
                strboctype = "Overview";

                btnhailskip.setText("Skip");

                if (btnhailtype.getText().equals("Front Slope")) {



                    if (chkrightslopeimg) {
                        btnhailmenu1.setVisibility(View.INVISIBLE);

                        btnhailtype.setText("Right Slope");
                        hailimgcount = 0;
                        no_slopeimg = no_rightslopeimg;

                        setnexthailslopvalue();

                    } else if (chkrearslopeimg) {
                        btnhailmenu1.setVisibility(View.INVISIBLE);

                        btnhailtype.setText("Rear Slope");
                        hailimgcount = 0;
                        no_slopeimg = no_rearslopeimg;

                        setnexthailslopvalue();

                    } else if (chkleftslopeimg) {
                        btnhailmenu1.setVisibility(View.INVISIBLE);

                        btnhailtype.setText("Left Slope");
                        hailimgcount = 0;
                        no_slopeimg = no_leftslopeimg;

                        setnexthailslopvalue();


                    } else {
                        btnhailmenu1.setVisibility(View.INVISIBLE);

                        hailSloptoHome();
                    }


                } else if (btnhailtype.getText().equals("Right Slope")) {

                    if (chkrearslopeimg) {
                        btnhailmenu1.setVisibility(View.INVISIBLE);

                        btnhailtype.setText("Rear Slope");
                        hailimgcount = 0;
                        no_slopeimg = no_rearslopeimg;

                        setnexthailslopvalue();

                    } else if (chkleftslopeimg) {
                        btnhailmenu1.setVisibility(View.INVISIBLE);

                        btnhailtype.setText("Left Slope");
                        hailimgcount = 0;
                        no_slopeimg = no_leftslopeimg;

                        setnexthailslopvalue();


                    } else {
                        hailSloptoHome();
                    }


                } else if (btnhailtype.getText().equals("Rear Slope")) {

                    if (chkleftslopeimg) {
                        btnhailmenu1.setVisibility(View.INVISIBLE);

                        btnhailtype.setText("Left Slope");
                        hailimgcount = 0;
                        no_slopeimg = no_leftslopeimg;

                        setnexthailslopvalue();
                    } else {
                        hailSloptoHome();
                    }

                } else if (btnhailtype.getText().equals("Left Slope")) {
                    hailSloptoHome();

                }
            } else {

                if (hailimgcount == no_slopeimg) {

                    if (btnhailtype.getText().equals("Front Slope")) {
                        if (chkrightslopeimg) {
                            btnhailmenu1.setVisibility(View.INVISIBLE);

                            btnhailtype.setText("Right Slope");
                            hailimgcount = 0;
                            no_slopeimg = no_rightslopeimg;

                            setnexthailslopvalue();

                        } else if (chkrearslopeimg) {
                            btnhailmenu1.setVisibility(View.INVISIBLE);

                            btnhailtype.setText("Rear Slope");
                            hailimgcount = 0;
                            no_slopeimg = no_rearslopeimg;

                            setnexthailslopvalue();

                        } else if (chkleftslopeimg) {
                            btnhailmenu1.setVisibility(View.INVISIBLE);

                            btnhailtype.setText("Left Slope");
                            hailimgcount = 0;
                            no_slopeimg = no_leftslopeimg;

                            setnexthailslopvalue();
                        } else {
                            btnhailmenu1.setVisibility(View.INVISIBLE);

                            hailSloptoHome();
                        }

                    } else if (btnhailtype.getText().equals("Right Slope")) {

                        if (chkrearslopeimg) {
                            btnhailmenu1.setVisibility(View.INVISIBLE);

                            btnhailtype.setText("Rear Slope");
                            hailimgcount = 0;
                            no_slopeimg = no_rearslopeimg;

                            setnexthailslopvalue();

                        } else if (chkleftslopeimg) {
                            btnhailmenu1.setVisibility(View.INVISIBLE);

                            btnhailtype.setText("Left Slope");
                            hailimgcount = 0;
                            no_slopeimg = no_leftslopeimg;

                            setnexthailslopvalue();
                        } else {
                            hailSloptoHome();
                        }
                    } else if (btnhailtype.getText().equals("Rear Slope")) {

                        if (chkleftslopeimg) {
                            btnhailmenu1.setVisibility(View.INVISIBLE);

                            btnhailtype.setText("Left Slope");
                            hailimgcount = 0;
                            no_slopeimg = no_leftslopeimg;

                            setnexthailslopvalue();
                        } else {
                            hailSloptoHome();
                        }

                    } else if (btnhailtype.getText().equals("Left Slope")) {
                        hailSloptoHome();

                    }

                } else if (btnimgmacro.getText().toString().equals("Hail") && btnimgmacrosub.getText().toString().trim().equals("hail damage close up on 2")) {
                    btnhailmenu1.setText("");
                    btnhailmenu1.setVisibility(View.GONE);
                    btnimgmacrosub.setVisibility(View.GONE);
                    btnimgmacro.setText("Macro");
                    btnimgmacro.setVisibility(View.VISIBLE);
                    nextPhoto("Aditional Photo");
                    getalphaname();
                } else if (btnimgmacro.getText().toString().equals("Hail")) {
                    nextPhoto("hail frontslopenext");
                    getalphaname();

                }
            }



            Log.e("btnRoomMacrotext",""+btnimgmacrosub.getText());

            btno.setVisibility(View.VISIBLE);
            btnnc.setVisibility(View.VISIBLE);
            if(btnimgmacrosub.getText().toString().equalsIgnoreCase("test sq"))
            {


                btno.setVisibility(View.INVISIBLE);
                btnnc.setVisibility(View.INVISIBLE);

            }
            else if(btnimgmacrosub.getText().toString().equalsIgnoreCase("hail damage close up on shingles"))
            {
                btnocb.setTag("1");
                selectocb();
            }
            else
            {
                btnocb.setTag("3");
                selectocb();
            }



            if (btnhailtype.getText().equals("Front Slope"))
            {
                txt_slopeno.setText(no_frontslope);
            } else if (btnhailtype.getText().equals("Right Slope"))
            {
                txt_slopeno.setText(no_rightslope);

            } else if (btnhailtype.getText().equals("Rear Slope"))
            {
                txt_slopeno.setText(no_rearslope);
            } else if (btnhailtype.getText().equals("Left Slope")) {
                txt_slopeno.setText(no_leftslope);

            }

        } else if (vid == rlBack.getId()) {
            clickonView("rlBack");

            boolean isRisk2 = false;
            if (btnimgmacrosub.getText().toString().equalsIgnoreCase("risk2")) {
                isRisk2 = true;
            }

            btnrisk.setTag("1");
            showoption();
            btnrisk.setText("Aditional Photo");

            hideallview();
            btnrisk.setText("Aditional Photo");
            btnimgmacrosub.setVisibility(View.GONE);
            btnnodamages.setVisibility(View.VISIBLE);
            btno.setVisibility(View.VISIBLE);//dmakchange
            btnocb.setVisibility(View.INVISIBLE);
            btnimgmacro.setVisibility(View.VISIBLE);
            btnimgmacro.setText("Macro");
            getalphaname();

            if (btnrei.getText().toString().equals("I")) {
                rliteriortype.setVisibility(View.VISIBLE);

                btntype2.setVisibility(View.GONE);
                btnareatogal.setVisibility(View.INVISIBLE);
                btnSubAreaTogal.setVisibility(View.INVISIBLE);
            } else {
                rliteriortype.setVisibility(View.GONE);
            }

            if (btnarea.getText().toString().equalsIgnoreCase("ceiling") || btnarea.getText().toString().equalsIgnoreCase("wall") || btnarea.getText().toString().equalsIgnoreCase("floor")) {
                btnareatogal.setVisibility(View.INVISIBLE);
                if (!btnmaterial.getTag().toString().equalsIgnoreCase("0"))
                    btnSubAreaTogal.setVisibility(View.VISIBLE);
            }

            if (isRisk2) {
                if (btnrei.getTag().toString().equals("3")) {
                    onClick(btnrei);
                }
                if (btnrei.getTag().toString().equals("1")) {
                    onClick(btnrei);
                }
            }
        } else if (vid == rladdtionalback.getId()) {

            clickonView("rladdtionalback");

            if (btnabc.getText().toString().equals("Fence")) {

                btnabc.setText("None");
                txtalphaname2.setVisibility(View.INVISIBLE);
                btnrei.setVisibility(View.INVISIBLE);//Dmak
                btnmaterial.setVisibility(View.VISIBLE);
                btndamagetype.setVisibility(View.VISIBLE);
                btnmatrialsubmenu.setVisibility(View.VISIBLE);
                btnarea.setVisibility(View.INVISIBLE);
                rlcontrolview.setVisibility(View.GONE);
                btniteriortype.setVisibility(View.VISIBLE);
                btntype.setVisibility(View.VISIBLE);
                btntype2.setVisibility(View.VISIBLE);

                setbackmenu();

                nextPhoto("Aditional Photo");
                getalphaname();

            }

            rladdtionalback.setVisibility(View.GONE);

            btnimgmacro.setText("Macro");

            if (btnrei.getText().toString().equals("I")) {
                rliteriortype.setVisibility(View.VISIBLE);

                if (!btnarea.getText().equals("Area")) {
                    btnInsulation.setVisibility(View.VISIBLE);
                    btnQty.setVisibility(View.VISIBLE);
                    btnareatogal.setVisibility(View.INVISIBLE);
                    btnSubAreaTogal.setVisibility(View.VISIBLE);
                }
            } else {
                rliteriortype.setVisibility(View.GONE);
                btnQty.setVisibility(View.VISIBLE);
            }

            getalphaname();
            btnimgmacrosub.setVisibility(View.GONE);
            btnimgmacro.setVisibility(View.VISIBLE);
            btnnodamages.setVisibility(View.VISIBLE);
            btno.setVisibility(View.VISIBLE);//dmakchange
            btnocb.setVisibility(View.INVISIBLE);

        } else if (vid == txt_frontslope.getId())
        {
            clickonView("txt_frontslope");
            showmyhail(txt_frontslope, "1");

        } else if (vid == txt_rightslope.getId()) {
            clickonView("txt_rightslope");

            showmyhail(txt_rightslope, "2");

        } else if (vid == txt_rearslope.getId()) {
            clickonView("txt_rearslope");

            showmyhail(txt_rearslope, "3");

        } else if (vid == txt_leftslope.getId()) {
            clickonView("txt_leftslope");

            showmyhail(txt_leftslope, "4");

        }
        else if(vid == txt_slopeno.getId())
        {
            clickonView("txt_slopeno");

            selectslopno(txt_slopeno);
        }

        else if (vid == rllayerBack.getId()) {

            clickonView("rllayerBack");

            btnrisk.setTag("1");
            hideallview();
            btnimgmacro.setText("Macro");
            btnrisk.setText("Aditional Photo");
            btnimgmacrosub.setVisibility(View.GONE);
            btnimgmacro.setVisibility(View.VISIBLE);
            btnnodamages.setVisibility(View.VISIBLE);
            btno.setVisibility(View.VISIBLE);//dmakchange
            btnocb.setVisibility(View.INVISIBLE);

            if (btnrei.getText().toString().equals("I")) {
                rliteriortype.setVisibility(View.VISIBLE);

                btntype2.setVisibility(View.GONE);
                btnareatogal.setVisibility(View.INVISIBLE);
                btnSubAreaTogal.setVisibility(View.INVISIBLE);

            } else {
                rliteriortype.setVisibility(View.GONE);

            }

            getalphaname();

            if (btnarea.getText().toString().equalsIgnoreCase("ceiling") || btnarea.getText().toString().equalsIgnoreCase("wall") || btnarea.getText().toString().equalsIgnoreCase("floor")) {
                btnareatogal.setVisibility(View.INVISIBLE);
                if (!btnmaterial.getTag().toString().equalsIgnoreCase("0"))
                    btnSubAreaTogal.setVisibility(View.VISIBLE);
            }

        } else if (vid == rlshingleBack.getId()) {

            clickonView("rlshingleBack");

            btnrisk.setTag("1");
            hideallview();
            btnrisk.setText("Aditional Photo");
            btnimgmacro.setText("Macro");

            if (btnrei.getText().toString().equals("I")) {
                rliteriortype.setVisibility(View.VISIBLE);
            } else {
                rliteriortype.setVisibility(View.GONE);
            }

            getalphaname();
            btnimgmacrosub.setVisibility(View.GONE);
            btnimgmacro.setVisibility(View.VISIBLE);
            btnnodamages.setVisibility(View.VISIBLE);
            btno.setVisibility(View.VISIBLE);//dmakchange
            btnocb.setVisibility(View.INVISIBLE);

            if (btnarea.getText().toString().equalsIgnoreCase("ceiling") || btnarea.getText().toString().equalsIgnoreCase("wall") || btnarea.getText().toString().equalsIgnoreCase("floor")) {
                btnareatogal.setVisibility(View.INVISIBLE);
                if (!btnmaterial.getTag().toString().equalsIgnoreCase("0"))
                    btnSubAreaTogal.setVisibility(View.VISIBLE);
            }

        } else if (vid == rlgutterBack.getId()) {

            clickonView("rlgutterBack");


            btnrisk.setTag("1");
            hideallview();
            btnrisk.setText("Aditional Photo");
            btnimgmacro.setText("Macro");

            if (btnrei.getText().toString().equals("I")) {
                rliteriortype.setVisibility(View.VISIBLE);

                btntype2.setVisibility(View.GONE);
                btnareatogal.setVisibility(View.INVISIBLE);
                btnSubAreaTogal.setVisibility(View.INVISIBLE);
            } else {
                rliteriortype.setVisibility(View.GONE);

            }

            getalphaname();
            btnimgmacrosub.setVisibility(View.GONE);
            btnimgmacro.setVisibility(View.VISIBLE);
            btnnodamages.setVisibility(View.VISIBLE);
            btno.setVisibility(View.VISIBLE);//dmakchange
            btnocb.setVisibility(View.INVISIBLE);

//            btnimgmacro.setText("Macro");

            if (btnarea.getText().toString().equalsIgnoreCase("ceiling") || btnarea.getText().toString().equalsIgnoreCase("wall") || btnarea.getText().toString().equalsIgnoreCase("floor")) {
                btnareatogal.setVisibility(View.INVISIBLE);
                if (!btnmaterial.getTag().toString().equalsIgnoreCase("0"))
                    btnSubAreaTogal.setVisibility(View.VISIBLE);
            }

        } else if (vid == rlOverhangBack.getId()) {

            clickonView("rlOverhangBack");


            btnrisk.setTag("1");
            hideallview();
            btnrisk.setText("Aditional Photo");
            btnimgmacro.setText("Macro");
            btnimgmacrosub.setVisibility(View.GONE);
            btnimgmacro.setVisibility(View.VISIBLE);
            btnnodamages.setVisibility(View.VISIBLE);
            btno.setVisibility(View.VISIBLE);//dmakchange
            btnocb.setVisibility(View.INVISIBLE);

            if (btnrei.getText().toString().equals("I")) {
                rliteriortype.setVisibility(View.VISIBLE);

                btntype2.setVisibility(View.GONE);
                btnareatogal.setVisibility(View.INVISIBLE);
                btnSubAreaTogal.setVisibility(View.INVISIBLE);
            } else {
                rliteriortype.setVisibility(View.GONE);

            }


            getalphaname();

            if (btnarea.getText().toString().equalsIgnoreCase("ceiling") || btnarea.getText().toString().equalsIgnoreCase("wall") || btnarea.getText().toString().equalsIgnoreCase("floor")) {
                btnareatogal.setVisibility(View.INVISIBLE);
                if (!btnmaterial.getTag().toString().equalsIgnoreCase("0"))
                    btnSubAreaTogal.setVisibility(View.VISIBLE);
            }
//            btnimgmacro.setText("Macro");

        } else if (vid == rlBack.getId()) {

            clickonView("rlBack");


            btnrisk.setTag("1");
            hideallview();
            btnrisk.setText("Aditional Photo");
            btnimgmacrosub.setVisibility(View.GONE);
            btnimgmacro.setVisibility(View.VISIBLE);
            btnimgmacro.setText("Macro");
            btnnodamages.setVisibility(View.VISIBLE);
            btno.setVisibility(View.VISIBLE);//dmakchange
            btnocb.setVisibility(View.INVISIBLE);

            if (btnrei.getText().toString().equals("I")) {
                rliteriortype.setVisibility(View.VISIBLE);

                btntype2.setVisibility(View.GONE);
                btnareatogal.setVisibility(View.INVISIBLE);
                btnSubAreaTogal.setVisibility(View.INVISIBLE);
            } else {
                rliteriortype.setVisibility(View.GONE);

            }


            getalphaname();

            if (btnarea.getText().toString().equalsIgnoreCase("ceiling") || btnarea.getText().toString().equalsIgnoreCase("wall") || btnarea.getText().toString().equalsIgnoreCase("floor")) {
                btnareatogal.setVisibility(View.INVISIBLE);
                if (!btnmaterial.getText().toString().equalsIgnoreCase("blank"))
                    btnSubAreaTogal.setVisibility(View.VISIBLE);
            }
//            btnimgmacro.setText("Macro");

        } else if (vid == rlpitchBack.getId()) {

            clickonView("rlpitchBack");



            btnrisk.setTag("1");
            hideallview();
            btnrisk.setText("Aditional Photo");
            btnimgmacrosub.setVisibility(View.GONE);
            btnimgmacro.setVisibility(View.VISIBLE);
            btnimgmacro.setText("Macro");
            btnnodamages.setVisibility(View.VISIBLE);
            btno.setVisibility(View.VISIBLE);//dmakchange
            btnocb.setVisibility(View.INVISIBLE);
            if (btnrei.getText().toString().equals("I")) {
                rliteriortype.setVisibility(View.VISIBLE);

                btntype2.setVisibility(View.GONE);
                btnareatogal.setVisibility(View.INVISIBLE);
                btnSubAreaTogal.setVisibility(View.INVISIBLE);
            } else {
                rliteriortype.setVisibility(View.GONE);

            }


            getalphaname();

            if (btnarea.getText().toString().equalsIgnoreCase("ceiling") || btnarea.getText().toString().equalsIgnoreCase("wall") || btnarea.getText().toString().equalsIgnoreCase("floor")) {
                btnareatogal.setVisibility(View.INVISIBLE);
                if (!btnmaterial.getTag().toString().equalsIgnoreCase("0"))
                    btnSubAreaTogal.setVisibility(View.VISIBLE);
            }

//            btnimgmacro.setText("Macro");

        } else if (vid == rlcostmback.getId()) {

            clickonView("rlcostmback");


            btnimgmacro.setText("Macro");
            btnnodamages.setVisibility(View.VISIBLE);
            btno.setVisibility(View.VISIBLE);//dmakchange
            btnocb.setVisibility(View.INVISIBLE);
            btnimgmacrosub.setVisibility(View.GONE);
            btnimgmacro.setVisibility(View.VISIBLE);

            txtalphaname.setVisibility(View.VISIBLE);
            if (!btnabc.getText().toString().equalsIgnoreCase("none"))
                txtalphaname2.setVisibility(View.VISIBLE);

            btnrisk.setTag("1");
            hideallview();
            btnrisk.setText("Aditional Photo");
            btnimgmacrosub.setVisibility(View.GONE);
            btnimgmacro.setVisibility(View.VISIBLE);
            btnimgmacro.setText("Macro");

            if (btnrei.getText().toString().equals("I")) {
                rliteriortype.setVisibility(View.VISIBLE);

                btntype2.setVisibility(View.GONE);
                btnareatogal.setVisibility(View.INVISIBLE);
                btnSubAreaTogal.setVisibility(View.INVISIBLE);
            } else {
                rliteriortype.setVisibility(View.GONE);

            }


            getalphaname();
//            btnimgmacro.setText("Macro");

            if (btnarea.getText().toString().equalsIgnoreCase("ceiling") || btnarea.getText().toString().equalsIgnoreCase("wall") || btnarea.getText().toString().equalsIgnoreCase("floor")) {
                btnareatogal.setVisibility(View.INVISIBLE);
                if (!btnmaterial.getTag().toString().equalsIgnoreCase("0"))
                    btnSubAreaTogal.setVisibility(View.VISIBLE);
            }

        } else if (vid == rlhailback.getId()) {

            clickonView("rlhailback");

            btnocb.setTag("3");
            selectocb();
            btno.setVisibility(View.VISIBLE);
            btnnc.setVisibility(View.VISIBLE);


            btnocb.setText("O");
            btnocb.setTag("1");
            strboc = "1";
            strboctype = "Overview";

            no_slopeimg = 0;
            hailimgcount = 0;

            btnimgmacro.setText("Macro");
            btnimgmacrosub.setVisibility(View.GONE);
            btnimgmacro.setVisibility(View.VISIBLE);
            btnimgmacro.setText("Macro");

            txtalphaname.setVisibility(View.VISIBLE);
            if (!btnabc.getText().toString().equalsIgnoreCase("none"))
                txtalphaname2.setVisibility(View.VISIBLE);

            btnrisk.setTag("1");
            hideallview();
            btnrisk.setText("Aditional Photo");
            btnimgmacrosub.setVisibility(View.GONE);
            btnnodamages.setVisibility(View.VISIBLE);
            btno.setVisibility(View.VISIBLE);//dmakchange
            btnocb.setVisibility(View.INVISIBLE);
            btnimgmacro.setVisibility(View.VISIBLE);
            btnimgmacro.setText("Macro");

            if (btnrei.getText().toString().equals("I")) {
                rliteriortype.setVisibility(View.VISIBLE);

                btntype2.setVisibility(View.GONE);
                btnareatogal.setVisibility(View.INVISIBLE);
                btnSubAreaTogal.setVisibility(View.INVISIBLE);

                if (btnarea.getText().toString().equalsIgnoreCase("ceiling") || btnarea.getText().toString().equalsIgnoreCase("wall") || btnarea.getText().toString().equalsIgnoreCase("floor")) {
                    btnareatogal.setVisibility(View.INVISIBLE);
                    if (!btnmaterial.getTag().toString().equalsIgnoreCase("0"))
                        btnSubAreaTogal.setVisibility(View.VISIBLE);
                }
            } else {
                rliteriortype.setVisibility(View.GONE);

            }

            getalphaname();
//            btnimgmacro.setText("Macro");

        } else if (vid == rlblankback.getId()) {

            clickonView("rlblankback");



            rlblankback.setVisibility(View.GONE);

            zoomseek.setVisibility(View.VISIBLE);

            btnrisk.setText("Aditional Photo");
            btnimgmacrosub.setVisibility(View.GONE);
            btnnodamages.setVisibility(View.VISIBLE);
            btno.setVisibility(View.VISIBLE);//dmakchange
            btnocb.setVisibility(View.INVISIBLE);
            btnimgmacro.setVisibility(View.VISIBLE);

            btnimgmacro.setText("Macro");
            nextPhoto("Aditional Photo");

            if (btnrei.getText().toString().equals("I")) {
                rliteriortype.setVisibility(View.VISIBLE);

                btntype2.setVisibility(View.GONE);
                btnareatogal.setVisibility(View.INVISIBLE);
                btnSubAreaTogal.setVisibility(View.INVISIBLE);
            } else {
                rliteriortype.setVisibility(View.GONE);

            }

            getalphaname();

            if (btnarea.getText().toString().equalsIgnoreCase("ceiling") || btnarea.getText().toString().equalsIgnoreCase("wall") || btnarea.getText().toString().equalsIgnoreCase("floor")) {
                btnareatogal.setVisibility(View.INVISIBLE);
                if (!btnmaterial.getTag().toString().equalsIgnoreCase("0"))
                    btnSubAreaTogal.setVisibility(View.VISIBLE);
            }


        } else if (vid == rlfeoback.getId()) {
            clickonView("rlfeoback");

            boolean isRoof = false;
            if (btnrisk.getText().toString().equalsIgnoreCase("Roof Overview")) {
                isRoof = true;
            }

            btnimgmacro.setText("Macro");
            btnimgmacrosub.setVisibility(View.GONE);
            btnnodamages.setVisibility(View.VISIBLE);
            btno.setVisibility(View.VISIBLE);//dmakchange
            btnocb.setVisibility(View.INVISIBLE);
            btnimgmacro.setVisibility(View.VISIBLE);
            btnimgmacro.setText("Macro");

            txtalphaname.setVisibility(View.VISIBLE);
            if (!btnabc.getText().toString().equalsIgnoreCase("none"))
                txtalphaname2.setVisibility(View.VISIBLE);

            btnrisk.setTag("1");
            hideallview();
            btnrisk.setText("Aditional Photo");
            btnimgmacrosub.setVisibility(View.GONE);
            btnimgmacro.setVisibility(View.VISIBLE);
            btnimgmacro.setText("Macro");

            if (btnrei.getText().toString().equals("I")) {
                rliteriortype.setVisibility(View.VISIBLE);

                btntype2.setVisibility(View.GONE);
                btnareatogal.setVisibility(View.INVISIBLE);
                btnSubAreaTogal.setVisibility(View.INVISIBLE);
            } else {
                rliteriortype.setVisibility(View.GONE);

            }

            getalphaname();
//            btnimgmacro.setText("Macro");

            if (btnarea.getText().toString().equalsIgnoreCase("ceiling") || btnarea.getText().toString().equalsIgnoreCase("wall") || btnarea.getText().toString().equalsIgnoreCase("floor")) {
                btnareatogal.setVisibility(View.INVISIBLE);
                if (!btnmaterial.getTag().toString().equalsIgnoreCase("0"))
                    btnSubAreaTogal.setVisibility(View.VISIBLE);
            }

            if (isRoof) {
                if (btnrei.getTag().toString().equals("2")) {
                    onClick(btnrei);
                }
                if (btnrei.getTag().toString().equals("3")) {
                    onClick(btnrei);
                }
            }

        } else if (vid == rlhailoptionback.getId()) {
            clickonView("rlhailoptionback");

            rlhailoptionview.setVisibility(View.GONE);

            btnimgmacro.setText("Macro");
            btnimgmacrosub.setVisibility(View.GONE);
            btnnodamages.setVisibility(View.VISIBLE);
            btno.setVisibility(View.VISIBLE);//dmakchange
            btnocb.setVisibility(View.INVISIBLE);
            btnimgmacro.setVisibility(View.VISIBLE);
            btnimgmacro.setText("Macro");

            txtalphaname.setVisibility(View.VISIBLE);
            if (!btnabc.getText().toString().equalsIgnoreCase("none"))
                txtalphaname2.setVisibility(View.VISIBLE);

            btnrisk.setTag("1");
            hideallview();
            btnrisk.setText("Aditional Photo");
            btnimgmacrosub.setVisibility(View.GONE);
            btnimgmacro.setVisibility(View.VISIBLE);
            btnimgmacro.setText("Macro");

            if (btnrei.getText().toString().equals("I")) {
                rliteriortype.setVisibility(View.VISIBLE);

                btntype2.setVisibility(View.GONE);
                btnareatogal.setVisibility(View.INVISIBLE);
                btnSubAreaTogal.setVisibility(View.INVISIBLE);

                if (btnarea.getText().toString().equalsIgnoreCase("ceiling") || btnarea.getText().toString().equalsIgnoreCase("wall") || btnarea.getText().toString().equalsIgnoreCase("floor")) {
                    btnareatogal.setVisibility(View.INVISIBLE);
                    if (!btnmaterial.getTag().toString().equalsIgnoreCase("0"))
                        btnSubAreaTogal.setVisibility(View.VISIBLE);
                }
            } else {
                rliteriortype.setVisibility(View.GONE);
            }

            getalphaname();

        } else if (vid == rbtn_frontslope.getId()) {
            clickonView("rbtn_frontslope");

            selecthailoption(rbtn_frontslope, txt_frontslope,"1");
        } else if (vid == rbtn_rightslope.getId()) {
            clickonView("rbtn_rightslope");

            no_rightslope="1";
            selecthailoption(rbtn_rightslope, txt_rightslope,"2");
        } else if (vid == rbtn_rearslope.getId()) {
            clickonView("rbtn_rearslope");

            no_rearslope="1";
            selecthailoption(rbtn_rearslope, txt_rearslope,"3");
        } else if (vid == rbtn_leftslope.getId()) {
            clickonView("rbtn_leftslope");

            no_leftslope="1";
            selecthailoption(rbtn_leftslope, txt_leftslope,"4");
        } else if (vid == btntype.getId()) {

            clickonView("btntype");

            if (btntype.getTag().equals("1")) {
                btntype.setBackgroundResource(R.drawable.red_button_background);
                btntype.setTag("2");

                btnnodamages.setBackgroundResource(R.drawable.button_background);
                btnnodamages.setTag("1");
              /*  btno.setBackgroundResource(R.drawable.button_background);
                btno.setTag("1");*/

            } else if (btntype.getTag().equals("2")) {

                btntype.setBackgroundResource(R.drawable.button_background);
                btntype.setTag("1");
            }
        } else if (vid == btntype2.getId()) {

            clickonView("btntype2");

            if (btntype2.getTag().equals("1")) {
                btntype2.setBackgroundResource(R.drawable.red_button_background);
                btntype2.setTag("2");
            } else if (btntype2.getTag().equals("2")) {
                btntype2.setBackgroundResource(R.drawable.button_background);
                btntype2.setTag("1");
            }

            getalphaname();
        } else if (vid == btnnodamages.getId()) {

            clickonView("btnnodamages");


            if (btnnodamages.getTag().equals("1")) {
                btnnodamages.setBackgroundResource(R.drawable.red_button_background);
                btnnodamages.setTag("2");

                btntype.setTag("2");

                btntype.callOnClick();
            } else if (btnnodamages.getTag().equals("2")) {
                btnnodamages.setBackgroundResource(R.drawable.button_background);
                btnnodamages.setTag("1");
            }

        } else if (vid == btnhailnodamages.getId()) {

            clickonView("btnhailnodamages");


            if (btnhailnodamages.getTag().equals("1")) {
                btnnodamagetype.setBackgroundResource(R.drawable.button_background);
                btnhailnodamages.setBackgroundResource(R.drawable.red_button_background);
                btnhailnodamages.setTag("2");
                //btnhailnodamages.setText(btndamagetype1.getText());
                strhaildamage=btndamagetype1.getText().toString();
                // btntype.setTag("2");

                // btntype.callOnClick();
                getalphaname();

            } else if (btnhailnodamages.getTag().equals("2")) {
                btnnodamagetype.setBackgroundResource(R.drawable.red_button_background);

                btnhailnodamages.setBackgroundResource(R.drawable.button_background);
                btnhailnodamages.setTag("1");
                strhaildamage="";
                //    btnhailnodamages.setText("");
                getalphaname();
            }

        }
        else if (vid == btnhailmaterialdamages.getId()) {


            clickonView("btnhailmaterialdamages");


            if (btnhailmaterialdamages.getTag().equals("1")) {
                btnhailmaterialdamages.setBackgroundResource(R.drawable.red_button_background);
                btnhailmaterialdamages.setTag("2");
                //btnhailnodamages.setText(btndamagetype1.getText());
                strhailmaterialdamage=btnhailmenu1.getText().toString();
                // btntype.setTag("2");

                // btntype.callOnClick();
                getalphaname();

            } else if (btnhailmaterialdamages.getTag().equals("2")) {
                btnhailmaterialdamages.setBackgroundResource(R.drawable.button_background);
                btnhailmaterialdamages.setTag("1");
                strhailmaterialdamage="";
                //    btnhailnodamages.setText("");
                getalphaname();
            }

        }
        else if (vid == btno.getId()) {

            clickonView("btno");

            if(!btnocb.getText().toString().equalsIgnoreCase("O"))
            {
                btnocb.setTag("3");
                btnocb.performClick();
            }
            else
            {
                btnocb.setTag("2");
                btnocb.performClick();

            }

/*
//            if (btno.getText().equals("O") && btnnc.getText().equals("C")) {
//
//                btno.setBackgroundResource(R.drawable.button_background);
//                btno.setTag("1");
//                btnocb.setText("C");
//                btno.setText("O");
//
//                btnnc.setBackgroundResource(R.drawable.button_background);
////                if (i == 2) {
////                    btno.setBackgroundResource(R.drawable.button_background);
////                    btno.setTag("1");
////                    btnocb.setText("C");
////                    btno.setText("O");
////
////                    btnnc.setBackgroundResource(R.drawable.button_background);
////                    Log.e("i==2 btnnc.getId()", "i==2 btnnc.getId()");
////                }
//            }
            if (btno.getText().equals("O")) {

                btno.setBackgroundResource(R.drawable.red_button_background);
                btno.setTag("1");
                btnocb.setText("C");
                btno.setText("O");
                btnnc.setBackgroundResource(R.drawable.button_background);
                Log.e("called red22", "called red22");
//                if(btemp)
//                {
//                    btno.setBackgroundResource(R.drawable.button_background);
//                    btno.setTag("1");
//                    btnocb.setText("C");
//                    btno.setText("O");
//                    btnnc.setBackgroundResource(R.drawable.red_button_background);
//                }

//                if (i == 2) {
//                    btno.setBackgroundResource(R.drawable.button_background);
//                    btno.setTag("1");
//                    btnocb.setText("C");
//                    btno.setText("O");
//                    btnnc.setBackgroundResource(R.drawable.button_background);
//                    Log.e("i==2 btno.getId()", "i==2 btno.getId()");
//                }
            }


            if (btno.getTag().equals("1")) {

                btno.setBackgroundResource(R.drawable.red_button_background);
                btno.setTag("2");
                btntype.setTag("2");
                btnocb.setText("O");
                btno.setText("O");
                btntype.callOnClick();
                Log.e("called red2", "called red2");

//                if(btemp)
//                {
//                    btno.setBackgroundResource(R.drawable.button_background);
//                    btno.setTag("1");
//                    btnocb.setText("C");
//                    btno.setText("O");
//                    btnnc.setBackgroundResource(R.drawable.red_button_background);
//                }


            } else if (btno.getTag().equals("2")) {

                btno.setBackgroundResource(R.drawable.button_background);
                btno.setTag("1");
                btnocb.setText("O");
                btno.setText("O");

                clickedo = true;

                if (clickedo) {
                    btno.setBackgroundResource(R.drawable.button_background);
                    btno.setTag("1");
                    btnocb.setText("B");
                    btno.setText("O");
                }
            }

            if (btnnc.getTag().equals("2")) {
                btnnc.setBackgroundResource(R.drawable.button_background);
                btnnc.setTag("1");
                btnocb.setText("O");
                btnnc.setText("C");
            }


//            if (btnnc.getTag().equals("3")) {
//                btno.setBackgroundResource(R.drawable.button_background);
//                btnnc.setTag("1");
//                btnocb.setText("O");
//
//                btnnc.setText("C");
//                Log.e("called red1 exc ","called red1 exc");
//            }
*/

        } else if (vid == btnnc.getId()) {

            clickonView("btnnc");


            if(!btnocb.getText().toString().equalsIgnoreCase("C"))
            {
                btnocb.setTag("1");
                btnocb.performClick();
            }
            else
            {
                btnocb.setTag("2");
                btnocb.performClick();

            }


        /*    if (btnnc.getText().equals("C")) {

                btno.setBackgroundResource(R.drawable.button_background);
                btno.setTag("1");
                btnocb.setText("C");
                btno.setText("O");

                btnnc.setBackgroundResource(R.drawable.red_button_background);
                Log.e("called red11", "called red11");
                //  btemp=true;
                btnnc.setTag("3");
//                if (i == 2) {
//                    btno.setBackgroundResource(R.drawable.button_background);
//                    btno.setTag("1");
//                    btnocb.setText("C");
//                    btno.setText("O");
//
//                    btnnc.setBackgroundResource(R.drawable.button_background);
//                    Log.e("i==2 btnnc.getId()", "i==2 btnnc.getId()");
//                }
            }
//            if (btnnc.getText().equals("C") && btno.getText().equals("O")) {
//
//                btno.setBackgroundResource(R.drawable.button_background);
//                btno.setTag("1");
//                btnocb.setText("C");
//                btno.setText("O");
//
//                btnnc.setBackgroundResource(R.drawable.button_background);
////                if (i == 2) {
////                    btno.setBackgroundResource(R.drawable.button_background);
////                    btno.setTag("1");
////                    btnocb.setText("C");
////                    btno.setText("O");
////
////                    btnnc.setBackgroundResource(R.drawable.button_background);
////                    Log.e("i==2 btnnc.getId()", "i==2 btnnc.getId()");
////                }
//            }


            if (btnnc.getTag().equals("1")) {

                btnnc.setBackgroundResource(R.drawable.red_button_background);
                btnnc.setTag("2");
                btntype.setTag("2");
                btnocb.setText("C");

                btnnc.setText("C");
                btntype.callOnClick();
                Log.e("called red1", "called red1");
                btnnc.setTag("3");

            } else if (btnnc.getTag().equals("2")) {

                btnnc.setBackgroundResource(R.drawable.button_background);
                btnnc.setTag("1");
                btnocb.setText("C");
                btnnc.setText("C");

                clickedc = true;

                if (clickedc) {
                    btnnc.setBackgroundResource(R.drawable.button_background);
                    btnnc.setTag("1");
                    btnocb.setText("B");
                    btnnc.setText("C");
                }
            }

            if (btno.getTag().equals("2")) {

                btno.setBackgroundResource(R.drawable.button_background);
                btno.setTag("1");
                btnocb.setText("C");
                btno.setText("O");
            }
            if (btno.getText().equals("O") && btno.getTag().equals("1")) {
                Log.e("called the last", "called the last");
                btno.setBackgroundResource(R.drawable.button_background);
                btno.setTag("1");
                btnocb.setText("C");
                btno.setText("O");
                btnnc.setBackgroundResource(R.drawable.red_button_background);
            }*/


        } else if (vid == imgtop.getId()) {


            clickonView("imgtop");



            btntype.setBackgroundResource(R.drawable.button_background);
            btntype.setTag("1");


            btntype2.setBackgroundResource(R.drawable.button_background);
            btntype2.setTag("1");


            if (selectslope.trim().equals("Front")) {
                setArrowBlank();
            } else {


                if (btnrei.getText().toString().equals("R")) {
                    selectslope = " Front ";
                } else if (btnrei.getText().toString().equals("E")) {
                    selectslope = "Front ";
                } else if (btnrei.getText().toString().equals("I")) {
                    selectslope = "Front ";
                }

                setarrowicon(imgtop, R.drawable.rticon);
                btnocb.setTag("3");
        selectocb();
                strboc = "1";
                strboctype = "Overview";
                strfencegame = "Front Run";

            }

        } else if (vid == imgbottom.getId()) {


            clickonView("imgbottom");


            btntype.setBackgroundResource(R.drawable.button_background);
            btntype.setTag("1");

            btntype2.setBackgroundResource(R.drawable.button_background);
            btntype2.setTag("1");

            if (selectslope.trim().equals("Rear")) {
                setArrowBlank();
            } else {

//                btntype.setBackgroundResource(R.drawable.button_background);
//                btntype.setTag("1");



                if (btnrei.getText().toString().equals("R")) {
                    selectslope = " Rear ";
                } else if (btnrei.getText().toString().equals("E")) {
                    selectslope = "Rear ";
                } else if (btnrei.getText().toString().equals("I")) {
                    selectslope = "Rear ";
                }
                setarrowicon(imgbottom, R.drawable.rbicon);
                btnocb.setTag("3");
        selectocb();
                strboc = "1";
                strboctype = "Overview";
                strfencegame = "Rear Run";
            }

        } else if (vid == imgleft.getId()) {


            clickonView("imgleft");


            btntype.setBackgroundResource(R.drawable.button_background);
            btntype.setTag("1");


            btntype2.setBackgroundResource(R.drawable.button_background);
            btntype2.setTag("1");


            if (selectslope.trim().equals("Left")) {
                setArrowBlank();
            } else {
//
//                btntype.setBackgroundResource(R.drawable.button_background);
//                btntype.setTag("1");


                setarrowicon(imgleft, R.drawable.rlicon);
                if (btnrei.getText().toString().equals("R")) {
                    selectslope = " Left ";
                } else if (btnrei.getText().toString().equals("E")) {
                    selectslope = "Left ";
                } else if (btnrei.getText().toString().equals("I")) {
                    selectslope = "Left ";
                }
                btnocb.setTag("3");
        selectocb();
                strboc = "1";
                strboctype = "Overview";
                strfencegame = "Left Run";
            }

        } else if (vid == imgright.getId()) {



            clickonView("imgright");


            btntype.setBackgroundResource(R.drawable.button_background);
            btntype.setTag("1");

            btntype2.setBackgroundResource(R.drawable.button_background);
            btntype2.setTag("1");

            if (selectslope.trim().equals("Right")) {
                setArrowBlank();
            } else {
                setarrowicon(imgright, R.drawable.rricon);
                if (btnrei.getText().toString().equals("R")) {
                    selectslope = " Right ";
                } else if (btnrei.getText().toString().equals("E")) {
                    selectslope = "Right ";
                } else if (btnrei.getText().toString().equals("I")) {
                    selectslope = "Right ";
                }
                btnocb.setTag("3");
        selectocb();
                strboc = "1";
                strboctype = "Overview";
                strfencegame = "Right Run";
            }
        } else if (vid == rlsetting.getId()) {

            clickonView("rlsetting");


        } else {
            if (vid == rltorch.getId()) {
                clickonView("rltorch");


                if (!getApplicationContext().getPackageManager()
                        .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
                    AlertDialog alert = new AlertDialog.Builder(HomeActivity.this)
                            .create();
                    alert.setTitle("Error");
                    alert.setMessage("Sorry, your device doesn't support flash light!");
                    alert.setButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
                    alert.show();
                    return;
                } else {
                    if (isFlashOn) {
                        imageViewTorch.setBackgroundResource(R.drawable.flashlight_icon);
                        turnOffFlash();
                    } else {
                        imageViewTorch.setBackgroundResource(R.drawable.flashlight_off_icon);
                        turnOnFlash();
                    }
                }
            } else if (vid == btnok.getId()) {
                clickonView("btnok");


                if (!txtfoldername.getText().toString().trim().equals("")) {
                    btncat.setText(txtfoldername.getText().toString().trim());
                    lastimageeditor.putString("appfoldername", txtfoldername.getText().toString().trim());
                    lastimageeditor.commit();
                    appfoldername = txtfoldername.getText().toString().trim();
                    mydir = new File(appdir, appfoldername);

                }

                              rlsetting.setVisibility(View.GONE);
                btnabc.setText("None");
            } else if (vid == btnroofadd.getId()) {
                clickonView("btnroofadd");


                addmenuvalue("tbl_r", "Roof");
            } else if (vid == btnaddelevations.getId()) {
                clickonView("btnaddelevations");

                addmenuvalue("tbl_e", "Elevations");
            } else if (vid == btnaddinterior.getId()) {
                clickonView("btnaddinterior");

                addmenuvalue("tbl_i", "Interior");
            } else if (vid == btnadddamage.getId()) {
                clickonView("btnadddamage");

                addmenuvalue("tbldamagetype", "Damage");
            } else if (vid == btnesubcatgry.getId()) {
                clickonView("btnesubcatgry");

                addmenuvalue("tbl_interior", "Interior Subcategory");
            } else if (vid == btnAddDocument.getId()) {
                clickonView("btnAddDocument");

                addDocument();
            } else if (vid == btnSynchronize.getId()) {
                clickonView("btnSynchronize");

                synchronizeServer();
            } else if (vid == btnLogout.getId()) {
                clickonView("btnLogout");

                logout();
            } else if (vid == btnaddclaimname.getId()) {
                clickonView("btnaddclaimname");

                rlsetting.setVisibility(View.GONE);
                Intent addnclaimname = new Intent(getApplicationContext(), AddClaimNameActivity.class);
                startActivity(addnclaimname);
            } else if (vid == btnmacroadd.getId()) {
                clickonView("btnmacroadd");

                addmenuvalue("tbl_macros", "Add Macros");
            } else if (vid == btncancel.getId()) {
                clickonView("btncancel");

                rlsetting.setVisibility(View.GONE);
            } else if (vid == btniteriortype.getId()) {
                clickonView("btniteriortype");

                setInteriorvalue();
            } else if (vid == btnlastphoto.getId()) {
                clickonView("btnlastphoto");

                File latimagefile = new File(lastpathpf.getString("lastimgpath", ""));
                Log.e("getlatimagefile","==>"+latimagefile.getAbsolutePath());


                PopupMenu popupMenu = new PopupMenu(this, btnlastphoto);
                popupMenu.getMenu().add("View last photo");
                popupMenu.getMenu().add("Delete last photo");

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        try {
                            File latimagefile = new File(lastpathpf.getString("lastimgpath", ""));


                            if (!latimagefile.exists()) {
                                Toast.makeText(HomeActivity.this, "Last photo not found.", Toast.LENGTH_SHORT).show();
                                return false;
                            }

                            if (item.getTitle().equals("View last photo")) {
                                try {
                                    opengallery = Boolean.valueOf(true);
                                    allFiles = mydir.listFiles();
                                    if (allFiles.length == 0) {
                                        return false;
                                    }
                                    if (latimagefile.exists()) {
                                        new SingleMediaScanner(HomeActivity.this, latimagefile);
                                    } else {
                                        new SingleMediaScanner(HomeActivity.this, allFiles[0]);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
//                                    Toast.makeText(HomeActivity.this, "Last photo not found.", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent();
                                    intent.setAction(Intent.ACTION_VIEW);
                                    intent.setDataAndType(Uri.fromFile(latimagefile), "image/*");
                                    startActivity(intent);
                                }
                            } else {
                                Toast.makeText(HomeActivity.this, "Delete last photo " + (latimagefile.delete() ? "success" : "failed") + ".", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {

                            Toast.makeText(HomeActivity.this, "Last photo not found.", Toast.LENGTH_SHORT).show();
                        }
                        return true;
                    }
                });
                popupMenu.show();
            } else if (vid == ibtnflash.getId()) {

                clickonView("ibtnflash");

                if (hasFlash) {
                    Camera.Parameters cameraparameters = camera.getParameters();
                    if (ibtnflash.getTag().equals("1")) {
                        ibtnflash.setBackgroundResource(R.drawable.flashautoicon);
                        cameraparameters.setFlashMode("auto");
                        ibtnflash.setTag("2");
                    } else if (ibtnflash.getTag().equals("2")) {
                        ibtnflash.setBackgroundResource(R.drawable.flashofficon);
                        cameraparameters.setFlashMode("off");
                        ibtnflash.setTag("3");
                    } else if (ibtnflash.getTag().equals("3")) {
                        ibtnflash.setBackgroundResource(R.drawable.flashonicon);
                        cameraparameters.setFlashMode("on");
                        ibtnflash.setTag("1");
                    }
                    camera.setParameters(cameraparameters);
                    return;
                }
                showflashalert();
            } else if (vid == ibtnLive.getId()) {
                clickonView("ibtnLive");

                startActivity(new Intent(this, LiveStreamingActivity.class));
                overridePendingTransition(R.anim.right_out, R.anim.left_out);
            } else if (vid == imgbtncam.getId()) {

                clickonView("imgbtncam");

                imgMicName = "";

            /*    if (btnarea.getText().toString().equalsIgnoreCase("ceiling")) {
                    String userId = PrefManager.getUserId();
                    String claimId = PrefManager.getClaimId();
                    String material = btnmaterial.getText().toString().equalsIgnoreCase("Material") ? "N/A" : btnmaterial.getText().toString();
                    String Room = btniteriortype.getText().toString().equalsIgnoreCase("Room") ? "N/A" : btniteriortype.getText().toString();
                    String qty = btnQty.getText().toString().equalsIgnoreCase("Quantity") ? "N/A" : btnQty.getText().toString();
                    String damage = btndamagetype.getText().toString();
                    String insulation = btnInsulation.getText().toString();
                    String areaType = btnarea.getText().toString();

                    System.out.println("ceiling userId first:-" + userId);
                    System.out.println("ceiling claimId first:-" + claimId);
                    System.out.println("ceiling material first:-" + material);
                    System.out.println("ceiling Room first:- " + Room);
                    System.out.println("ceiling qty first:- " + qty);

                    System.out.println("ceiling insulation first:- " + insulation);
                    System.out.println("ceiling areaType first:-" + areaType);

                    System.out.println("ceiling damage first:-" + damage);

                    claimDbHelper.adddataCeiling(userId, claimId, Room, material, insulation, areaType, qty, damage);
                    opendatabase();

                    SELECT_SQL = "SELECT * FROM TBLCeiling";
                    Cur = DB.rawQuery(SELECT_SQL, null);
                    if (Cur != null && Cur.getCount() > 0) {
                        Cur.moveToFirst();
                        do {

                            String strname = Cur.getString(Cur.getColumnIndex("Insulation"));
                            String strRoom = Cur.getString(Cur.getColumnIndex("Room"));
                            String strAreaType = Cur.getString(Cur.getColumnIndex("AreaType"));
                            String strQty = Cur.getString(Cur.getColumnIndex("Qty"));
                            String strDamage = Cur.getString(Cur.getColumnIndex("Damage"));
                            String strMaterial = Cur.getString(Cur.getColumnIndex("Material"));
                            // popupsubcat.getMenu().add(Menu.NONE, value, Menu.NONE, strname);
                            System.out.println("ceiling Insulation new:-" + strname);
                            System.out.println("ceiling Room new:-" + strRoom);
                            System.out.println("ceiling AreaType new:-" + strAreaType);
                            System.out.println("ceiling Qty new:-" + strQty);
                            System.out.println("ceiling Damage new:-" + strDamage);
                            System.out.println("ceiling Material new:-" + strMaterial);
                            //value++;
                        }
                        while (Cur.moveToNext());
                    }
                    Cur.close();
                    DB.close();


                }*/

                if (btnarea.getText().toString().equalsIgnoreCase("wall")) {
                    String userId = PrefManager.getUserId();
                    String claimId = PrefManager.getClaimId();
                    String material = btnmaterial.getText().toString().equalsIgnoreCase("Material") ? "N/A" : btnmaterial.getText().toString();
                    String Room = btniteriortype.getText().toString().equalsIgnoreCase("Room") ? "N/A" : btniteriortype.getText().toString();
                    String qty = btnQty.getText().toString().equalsIgnoreCase("Quantity") ? "N/A" : btnQty.getText().toString();
                    String damage = btndamagetype.getText().toString();
                    String insulation = btnInsulation.getText().toString();
                    String areaType = btnarea.getText().toString();

                    System.out.println("wall userId first:-" + userId);
                    System.out.println("wall claimId first:-" + claimId);
                    System.out.println("wall material first:-" + material);
                    System.out.println("wall Room first:- " + Room);
                    System.out.println("wall qty first:- " + qty);

                    System.out.println("wall insulation first:- " + insulation);
                    System.out.println("wall areaType first:-" + areaType);

                    System.out.println("wall damage first:-" + damage);

                  /*  claimDbHelper.adddataWall(userId, claimId, Room, material, insulation, areaType, qty, damage);
                    opendatabase();

                    SELECT_SQL = "SELECT * FROM TBLWall";
                    Cur = DB.rawQuery(SELECT_SQL, null);
                    if (Cur != null && Cur.getCount() > 0) {
                        Cur.moveToFirst();
                        do {

                            String strname = Cur.getString(Cur.getColumnIndex("Insulation"));
                            String strRoom = Cur.getString(Cur.getColumnIndex("Room"));
                            String strAreaType = Cur.getString(Cur.getColumnIndex("AreaType"));
                            String strQty = Cur.getString(Cur.getColumnIndex("Qty"));
                            String strDamage = Cur.getString(Cur.getColumnIndex("Damage"));
                            String strMaterial = Cur.getString(Cur.getColumnIndex("Material"));
                            // popupsubcat.getMenu().add(Menu.NONE, value, Menu.NONE, strname);
                            System.out.println("wall Insulation new:-" + strname);
                            System.out.println("wall Room new:-" + strRoom);
                            System.out.println("wall AreaType new:-" + strAreaType);
                            System.out.println("wall Qty new:-" + strQty);
                            System.out.println("wall Damage new:-" + strDamage);
                            System.out.println("wall Material new:-" + strMaterial);
                            //value++;
                        }
                        while (Cur.moveToNext());
                    }
                    Cur.close();
                    DB.close();*/
                }


                if (btnarea.getText().toString().equalsIgnoreCase("floor")) {
                    String userId = PrefManager.getUserId();
                    String claimId = PrefManager.getClaimId();
                    String material = btnmaterial.getText().toString().equalsIgnoreCase("Material") ? "N/A" : btnmaterial.getText().toString();
                    String Room = btniteriortype.getText().toString().equalsIgnoreCase("Room") ? "N/A" : btniteriortype.getText().toString();
                    String qty = btnQty.getText().toString().equalsIgnoreCase("Quantity") ? "N/A" : btnQty.getText().toString();
                    String damage = btndamagetype.getText().toString();
                    String insulation = btnInsulation.getText().toString();
                    String areaType = btnarea.getText().toString();

                    System.out.println("floor userId first:-" + userId);
                    System.out.println("floor claimId first:-" + claimId);
                    System.out.println("floor material first:-" + material);
                    System.out.println("floor Room first:- " + Room);
                    System.out.println("floor qty first:- " + qty);

                    System.out.println("floor insulation first:- " + insulation);
                    System.out.println("floor areaType first:-" + areaType);

                    System.out.println("floor damage first:-" + damage);

//                    claimDbHelper.adddataFloor(userId, claimId, Room, material, insulation, areaType, qty, damage);
//                    opendatabase();

//                    SELECT_SQL = "SELECT * FROM TBLFloor";
//                    Cur = DB.rawQuery(SELECT_SQL, null);
//                    if (Cur != null && Cur.getCount() > 0) {
//                        Cur.moveToFirst();
//                        do {
//
//                            String strname = Cur.getString(Cur.getColumnIndex("Insulation"));
//                            String strRoom = Cur.getString(Cur.getColumnIndex("Room"));
//                            String strAreaType = Cur.getString(Cur.getColumnIndex("AreaType"));
//                            String strQty = Cur.getString(Cur.getColumnIndex("Qty"));
//                            String strDamage = Cur.getString(Cur.getColumnIndex("Damage"));
//                            String strMaterial = Cur.getString(Cur.getColumnIndex("Material"));
//                            // popupsubcat.getMenu().add(Menu.NONE, value, Menu.NONE, strname);
//                            System.out.println("floor Insulation new:-" + strname);
//                            System.out.println("floor Room new:-" + strRoom);
//                            System.out.println("floor AreaType new:-" + strAreaType);
//                            System.out.println("floor Qty new:-" + strQty);
//                            System.out.println("floor Damage new:-" + strDamage);
//                            System.out.println("floor Material new:-" + strMaterial);
//                            //value++;
//                        }
//                        while (Cur.moveToNext());
//                    }
//                    Cur.close();
//                    DB.close();


                }
                if (btnMic.getTag().toString().equals("1"))
                    imgMicName = txtalphaname.getText().toString();

                if (btnsubmenu.getVisibility() == View.VISIBLE) {
                    if (btnsubmenu.getText().toString().trim().equals("")) {
                        Toast.makeText(getApplicationContext(), "Please Add subcategory of " + btnrisk.getText(), Toast.LENGTH_LONG).show();
                    } else {
                        try {
                            captureImage();
                        } catch (IOException e2) {
                            e2.printStackTrace();
                        }
                    }
                } else {
                    try {
                        captureImage();
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                }
            } else if (vid == imgbtnsetting.getId()) {
                clickonView("imgbtnsetting");

//                startActivity(new Intent(mContext, SettingActivity.class));
                txtfoldername.setText(appfoldername);
                if (rlsetting.getVisibility() == View.VISIBLE) {
                    rlsetting.setVisibility(View.GONE);
                } else {
                    rlsetting.setVisibility(View.VISIBLE);
                }
            }
        }
        getalphaname();
        if (vid == btnMic.getId()) {
            clickonView("btnMic");

            startVoiceInput();
        } else if (vid == btnReport.getId()) {
            clickonView("btnReport");

            Intent intent = new Intent(HomeActivity.this, ReportActivity.class);
//            intent.putExtra("uId", pref.getString("user_id", "0"));
            intent.putExtra("claimId", arrayListClaim.get(claimSelectPos).getId());
            startActivity(intent);
        }
    }

    private void onclick_R_E_I() {


        btntype.setVisibility(View.INVISIBLE);

        btntype2.setVisibility(View.INVISIBLE);

        strfencegame = "";
        imgtop.setBackgroundResource(R.drawable.wticon);
        imgbottom.setBackgroundResource(R.drawable.wbicon);
        imgleft.setBackgroundResource(R.drawable.wlicon);
        imgright.setBackgroundResource(R.drawable.wricon);

        llArea.setVisibility(View.GONE);
        btnInsulation.setVisibility(View.GONE);
        btnQty.setVisibility(View.VISIBLE);

        btnInsulation.setText("Insulation");
        btnQty.setText("Quantity");

        ibtnLive.setVisibility(View.VISIBLE);
        ibtnflash.setVisibility(View.VISIBLE);
        imageViewTorch.setVisibility(View.VISIBLE);

        if (btnrei.getTag().equals("1")) {
            strrei = "E";
            btnrei.setTag("2");
            btnrei.setText("E");

            roofMaterial = btnmaterial.getText().toString();

            btnarea.setVisibility(View.GONE);
            btnSubAreaTogal.setVisibility(View.GONE);
            btnareatogal.setVisibility(View.GONE);

            rlcontrolview.setVisibility(View.VISIBLE);
            rliteriortype.setVisibility(View.GONE);

            btnmatrialsubmenu.setVisibility(View.GONE);

            btnmaterial.setVisibility(View.VISIBLE);

            btnmaterial.setText(elevationMaterial);
            if (!btnmaterial.getText().toString().equalsIgnoreCase("Material")) {
                btnmaterial.setTag("1");
                btntype2.setText(btnmaterial.getText().toString());
                btntype2.setVisibility(View.VISIBLE);
                if (rbMaterialMark.isChecked()) {
                    btntype2.setBackgroundResource(R.drawable.red_button_background);
                    btntype2.setTag("2");
                } else if (rbMaterialUnmark.isChecked()) {
                    btntype2.setBackgroundResource(R.drawable.button_background);
                    btntype2.setTag("1");
                }
            } else
                btnmaterial.setTag("0");
        } else if (btnrei.getTag().equals("2")) {
            btnrei.setTag("3");
            btnrei.setText("I");
            strrei = "I";

            btnarea.setVisibility(View.INVISIBLE);

            btntype2.setVisibility(View.GONE);
            btnareatogal.setVisibility(View.INVISIBLE);
            btnSubAreaTogal.setVisibility(View.INVISIBLE);

            elevationMaterial = btnmaterial.getText().toString();

            rlcontrolview.setVisibility(View.GONE);
            rliteriortype.setVisibility(View.VISIBLE);
            btnmatrialsubmenu.setVisibility(View.VISIBLE);

            llArea.setVisibility(View.VISIBLE);

            if (rbCeiling.isChecked()) {
                btnarea.setText("Ceiling");
                btnarea.setTag("Ceiling");
                setAreaOption();
            } else if (rbWall.isChecked()) {
                btnarea.setText("Wall");
                btnarea.setTag("Wall");
                setAreaOption();
            } else if (rbFloor.isChecked()) {
                btnarea.setText("Floor");
                btnarea.setTag("Floor");
                setAreaOption();
            }

            if (!btnarea.getText().toString().equalsIgnoreCase("area") || !btnareatogal.getTag().toString().equals("0")) {
                btnarea.setText(btnareatogal.getText().toString());
                btnareatogal.setVisibility(View.INVISIBLE);

                btnmaterial.setTag("1");
                btnmaterial.setText(areaMaterial);

//                    if (!areaMaterial.equalsIgnoreCase("blank"))
                btnSubAreaTogal.setVisibility(View.VISIBLE);

                if (btnSubAreaTogal.getTag().toString().equals("1")) {
                    btnmaterial.setTag("0");
                    if (btnarea.getText().toString().equalsIgnoreCase("floor"))
                        btnmaterial.setText("Carpet");
                    else
                        btnmaterial.setText("Drywall");
                }

                if (rbAreaMark.isChecked()) {
                    btnareatogal.setBackgroundResource(R.drawable.red_button_background);
                    btnareatogal.setTag("2");
                } else if (rbAreaUnmark.isChecked()) {
//                        btnmaterial.setVisibility(View.INVISIBLE);
//                        btnSubAreaTogal.setVisibility(View.GONE);
//                        btnSubAreaTogal.setTag("1");
                    btnareatogal.setBackgroundResource(R.drawable.button_background);
                    btnareatogal.setTag("1");
                }

                if (btnareatogal.getTag().toString().equals("1")) {
                    btnarea.setText("Area");

//                        btnmaterial.setTag("0");
//                        btnmaterial.setText("Material");
//                        btnSubAreaTogal.setTag("1");
//                        btnSubAreaTogal.setBackgroundResource(R.drawable.button_background);
//                        btnSubAreaTogal.setVisibility(View.INVISIBLE);
//                        btnmaterial.setVisibility(View.INVISIBLE);
                }

//                    if (/*!rbAreaUnmark.isChecked() &&*/ !areaMaterial.equalsIgnoreCase("blank")) {
                if (rbAreaSubMark.isChecked()) {
                    btnSubAreaTogal.setBackgroundResource(R.drawable.red_button_background);
                    btnSubAreaTogal.setTag("2");
                    btnmaterial.setTag("1");
                    if (!areaMaterial.equalsIgnoreCase("blank"))
                        btnmaterial.setText(areaMaterial);
                } else if (rbAreaSubUnmark.isChecked()) {
                    btnSubAreaTogal.setBackgroundResource(R.drawable.button_background);
                    btnSubAreaTogal.setTag("1");

                    btnmaterial.setTag("0");
                    if (btnarea.getText().toString().equalsIgnoreCase("floor"))
                        btnmaterial.setText("Carpet");
                    else
                        btnmaterial.setText("Drywall");
                }
//                    }
            } else {

                btnmaterial.setTag("0");
                if (btnarea.getText().toString().equalsIgnoreCase("floor"))
                    btnmaterial.setText("Carpet");
                else
                    btnmaterial.setText("Drywall");
                btnmaterial.setVisibility(View.INVISIBLE);
            }

            if (!btnarea.getText().toString().equalsIgnoreCase("area")) {
                btnInsulation.setVisibility(View.VISIBLE);
                btnQty.setVisibility(View.VISIBLE);
            } else {
                btnInsulation.setVisibility(View.GONE);
                btnQty.setVisibility(View.GONE);
            }
        } else if (btnrei.getTag().equals("3")) {

            btnrei.setTag("1");
            btnrei.setText("R");
            strrei = "R";

            btnarea.setVisibility(View.GONE);
            btnSubAreaTogal.setVisibility(View.GONE);
            btnareatogal.setVisibility(View.GONE);
            if (!btnmaterial.getTag().toString().equalsIgnoreCase("0")/* && btnSubAreaTogal.getVisibility() != View.VISIBLE*/)
                areaMaterial = btnmaterial.getText().toString();
            else
                areaMaterial = "blank";
//                    areaMaterial = btnSubAreaTogal.getText().toString();

            rlcontrolview.setVisibility(View.VISIBLE);
            rliteriortype.setVisibility(View.GONE);
            btnmatrialsubmenu.setVisibility(View.GONE);

            btnmaterial.setVisibility(View.VISIBLE);
            btnmaterial.setText(roofMaterial);
            if (!btnmaterial.getText().toString().equalsIgnoreCase("Material")) {
                btnmaterial.setTag("1");
                btntype2.setText(btnmaterial.getText().toString());
                btntype2.setVisibility(View.VISIBLE);
                if (rbMaterialMark.isChecked()) {
                    btntype2.setBackgroundResource(R.drawable.red_button_background);
                    btntype2.setTag("2");
                } else if (rbMaterialUnmark.isChecked()) {
                    btntype2.setBackgroundResource(R.drawable.button_background);
                    btntype2.setTag("1");
                }
            } else
                btnmaterial.setTag("0");
        }

        if (rbOverview.isChecked()) {
            btnocb.setTag("3");
            selectocb();
            strboc = "1";
            strboctype = "Overview";
            selectocb();
        } else if (rbCloseUp.isChecked()) {
            btnocb.setTag("2");
            btnocb.setText("C");
            strboc = "2";
            strboctype = "Close up";
            selectocb();

        } else if (rbBlank.isChecked()) {
            btnocb.setTag("3");
            btnocb.setText("B");
            strboc = "3";
            strboctype = "Blank";
            selectocb();
        }

        btniteriortype.setTag("0");
        btniteriortype.setText("Room");


        selectslope = "";

        setDamageSelect();


        if(btnrei.getText().equals("E") || btnrei.getText().equals("R"))
        {
            serdefaultvalue();
        }



        btnr.setTag("1");
        btnr.setBackgroundResource(R.drawable.button_background);

        btne.setTag("1");
        btne.setBackgroundResource(R.drawable.button_background);


        if(btnrei.getText().toString().equalsIgnoreCase("r"))
        {
            btnr.setTag("2");
            btnr.setBackgroundResource(R.drawable.red_button_background);

        }
        else if(btnrei.getText().toString().equalsIgnoreCase("e"))
        {
            btne.setTag("2");
            btne.setBackgroundResource(R.drawable.red_button_background);
        }
        else if(btnrei.getText().toString().equalsIgnoreCase("i"))
        {

        }

    }

    private void selectocb() {

        Log.e("btnocbtag", "-->click");


        if (btnocb.getTag().equals("1")) {

            Log.e("called close up", "called close up");
            btnocb.setTag("2");
            btnocb.setText("C");
            strboc = "2";
            strboctype = "Close up";
            ocb1= "Close up";
            btnnc.setBackgroundResource(R.drawable.red_button_background);
            btno.setBackgroundResource(R.drawable.button_background);

                /*
                i = 2;

                if (clickedonewj) {
                    btnocb.setTag("1");
                    btnocb.setText("C");
                    Log.e("close up new set 1", "");
                    strboc = "1";
                    strboctype = "Overview";
                    i = 1;
                    btno.setBackgroundResource(R.drawable.button_background);
                    clickedonewj = false;
                    Log.e("close up new set 2", "");
                    //   Log.e("Close overview 1", "close up overview  22:-" + clickedonewj);
                }
                Log.e("Close up  closeup 3", "close up close up 3:-" + clickedonewj);*/

//                if(clickedonewj==false)
//                {
//                    btnocb.setTag("1");
//                    btnocb.setText("O");
//
//                    strboc = "1";
//                    strboctype = "Overview";
//                    btno.setBackgroundResource(R.drawable.button_background);
//                }

        } else if (btnocb.getTag().equals("2")) {

            Log.e("Called blank", "Called blank");
            btnocb.setTag("3");
            btnocb.setText("B");
            btno.setText("O");
            strboc = "";
            strboctype = "Blank";
            i = 1;
            btno.setBackgroundResource(R.drawable.button_background);
            btnnc.setBackgroundResource(R.drawable.button_background);
            ocb1= "Blank";
            Log.e("Close up blank 3", "close blank 3:-" + clickedonewj);

        } else if (btnocb.getTag().equals("3")) {

            Log.e("called overview", "");
            btnocb.setTag("1");
            btnocb.setText("O");
            i = 1;
            strboc = "1";
            strboctype = "Overview";
            ocb1= "Overview";
            btno.setBackgroundResource(R.drawable.red_button_background);
            btnnc.setBackgroundResource(R.drawable.button_background);
               /* if (btnocb.getTag().equals("1")) {
                    clickedonew = true;
                    Log.e("Called Overview new ", "Callled Overview New ");
                    if (clickedonew) {
                        clickedonewj = true;
                        Log.e("Called Overview", "Called Overview");
                        i = 2;
                        btno.setBackgroundResource(R.drawable.red_button_background);
                        btnnc.setBackgroundResource(R.drawable.button_background);
                        Log.e("Close overview 1", "close up overview  1:-" + clickedonewj);
                    }
                    Log.e("Close overview 1", "close up overview  2:-" + clickedonewj);
                }*/
        }


        Log.e("btnocbtag", "-->" + btnocb.getTag());


    }

    private void saveimages() {
        rllayerphoto.setDrawingCacheEnabled(true);
        rllayerphoto.buildDrawingCache(true);
        //Bitmap drawingCache = rllayerphoto.getDrawingCache();

        Bitmap bitmap = rllayerphoto.getDrawingCache();
        rllayerphoto.setDrawingCacheEnabled(false); // clear drawing cache
        final String root = Environment.getExternalStorageDirectory()
                .getAbsolutePath();
        File newDir = new File(root + "/New Aditional photo");

        newDir.mkdirs();
        Random gen = new Random();

        int n = 10000;
        n = gen.nextInt(n);
        String fotoname = "Image" + n + ".jpg";
        File file = new File(newDir, fotoname);
        String s = file.getAbsolutePath();
        Log.i("Path of saved image.", s);
        System.err.print("Path of saved image." + s);

        try {

            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();

            Toast.makeText(getApplicationContext(),
                    "Image saved to SD Card", Toast.LENGTH_SHORT)
                    .show();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                Intent mediaScanIntent = new Intent(
                        Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                File f = new File(newDir, fotoname); // OR File f = new
                // File(YourCurrentPhotoPath);
                Uri contentUri = Uri.fromFile(f);
                mediaScanIntent.setData(contentUri);
                context.sendBroadcast(mediaScanIntent);
            } else {
                context.sendBroadcast(new Intent(
                        Intent.ACTION_MEDIA_MOUNTED,
                        Uri.parse("file://"
                                + Environment
                                .getExternalStorageDirectory()
                                + "/" + "FOLDER_TO_REFRESH")));
            }
            out.close();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Photo Saved",
                    Toast.LENGTH_SHORT).show();
            Log.e("Exception", e.toString());
        }
    }

    private static final int REQ_CODE_SPEECH_INPUT = 100;

    private void startVoiceInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
//        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hello, How can I help you?");
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {

        }
    }

    private void setbackmenu() {
        btntype.setVisibility(View.INVISIBLE);

        btnmaterial.setTag("0");
        btnmaterial.setText("Material");
        btntype2.setVisibility(View.INVISIBLE);

        imgtop.setBackgroundResource(R.drawable.wticon);
        imgbottom.setBackgroundResource(R.drawable.wbicon);
        imgleft.setBackgroundResource(R.drawable.wlicon);
        imgright.setBackgroundResource(R.drawable.wricon);

        if (btnrei.getText().toString().equals("E")) {
            strrei = "E";
            btnrei.setTag("2");
            btnrei.setText("E");

            btnmaterial.setTag("0");
            btnmaterial.setText("Material");
            btnarea.setVisibility(View.GONE);

            rlcontrolview.setVisibility(View.VISIBLE);
            rliteriortype.setVisibility(View.GONE);

            btnmatrialsubmenu.setVisibility(View.GONE);

            btnmaterial.setVisibility(View.VISIBLE);
        } else if (btnrei.getText().toString().equals("I")) {
            btnrei.setTag("3");
            btnrei.setText("I");
            strrei = "I";
            btnarea.setVisibility(View.INVISIBLE);

            btnarea.setText("Area");

            btnmaterial.setTag("0");
            btnmaterial.setText("Material");
            rlcontrolview.setVisibility(View.GONE);
            rliteriortype.setVisibility(View.VISIBLE);
            btnmatrialsubmenu.setVisibility(View.VISIBLE);

            btnmaterial.setVisibility(View.INVISIBLE);
        } else if (btnrei.getText().toString().equals("R")) {
            btnrei.setTag("1");
            btnrei.setText("R");
            strrei = "R";

            btnarea.setVisibility(View.GONE);

            btnmaterial.setTag("0");
            btnmaterial.setText("Material");
            rlcontrolview.setVisibility(View.VISIBLE);
            rliteriortype.setVisibility(View.GONE);
            btnmatrialsubmenu.setVisibility(View.GONE);

            btnmaterial.setVisibility(View.VISIBLE);
        }

        btniteriortype.setTag("0");
        btniteriortype.setText("Room");

        btnocb.setTag("3");
        selectocb();
        strboc = "1";
        strboctype = "Overview";
        selectslope = "";

        setDamageSelect();
    }

    private void setnexthailslopvalue() {



        if (btnhailtype.getText().equals("Front Slope")) {

            Log.e("slopselect===>","Front Slope");
        } else if (btnhailtype.getText().equals("Right Slope")) {
            Log.e("slopselect===>","Right Slope");


        } else if (btnhailtype.getText().equals("Rear Slope")) {
            Log.e("slopselect===>","Rear Slope");


        } else if (btnhailtype.getText().equals("Left Slope")) {
            Log.e("slopselect===>","Left Slope");

        }

        btno.setVisibility(View.VISIBLE);//dmakchange
        btnnc.setVisibility(View.VISIBLE);//Dmakchange
        btnocb.setVisibility(View.INVISIBLE);

        rlhailoptionview.setVisibility(View.GONE);
        btnimgmacrosub.setVisibility(View.VISIBLE);
        btnimgmacrosub.setText("Overview");
        hidehailoption();
    }

    private void showhailmenu(String type) {
        popuphailmenu = new PopupMenu(this, findViewById(R.id.btnhailmenu1));

        if (type.toString().trim().equals("Material")) {
            popuphailmenu.getMenu().add(Menu.NONE, 0, Menu.NONE, "Shingles");
            popuphailmenu.getMenu().add(Menu.NONE, 1, Menu.NONE, "Vent");
            popuphailmenu.getMenu().add(Menu.NONE, 2, Menu.NONE, "Furnace Cap");
            popuphailmenu.getMenu().add(Menu.NONE, 3, Menu.NONE, "Drip Edge");
            popuphailmenu.getMenu().add(Menu.NONE, 4, Menu.NONE, "Metal");
            popuphailmenu.getMenu().add(Menu.NONE, 5, Menu.NONE, "Test Sq");
//            popuphailmenu.getMenu().add(Menu.NONE, 6, Menu.NONE, "Custom Text");
        } else {
            popuphailmenu.getMenu().add(Menu.NONE, 0, Menu.NONE, "0");
            popuphailmenu.getMenu().add(Menu.NONE, 1, Menu.NONE, "1");
            popuphailmenu.getMenu().add(Menu.NONE, 2, Menu.NONE, "2");
            popuphailmenu.getMenu().add(Menu.NONE, 3, Menu.NONE, "3");
            popuphailmenu.getMenu().add(Menu.NONE, 4, Menu.NONE, "4");
            popuphailmenu.getMenu().add(Menu.NONE, 5, Menu.NONE, "5");
            popuphailmenu.getMenu().add(Menu.NONE, 6, Menu.NONE, "6");
            popuphailmenu.getMenu().add(Menu.NONE, 7, Menu.NONE, "7");
            popuphailmenu.getMenu().add(Menu.NONE, 8, Menu.NONE, "8");
            popuphailmenu.getMenu().add(Menu.NONE, 9, Menu.NONE, "9");
            popuphailmenu.getMenu().add(Menu.NONE, 10, Menu.NONE, "10");
            popuphailmenu.getMenu().add(Menu.NONE, 11, Menu.NONE, "11");
            popuphailmenu.getMenu().add(Menu.NONE, 12, Menu.NONE, "12");
        }

        popuphailmenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem arg0) {
                btnhailmenu1.setText(arg0.getTitle());


                btnhailmaterialdamages.setTag("2");
                btnhailmaterialdamages.setBackgroundResource(R.drawable.red_button_background);
                btnhailmaterialdamages.setText(arg0.getTitle());
                strhailmaterialdamage=arg0.getTitle().toString();




                getalphaname();
                return false;
            }
        });

        popuphailmenu.show();
    }

    private int interiorMacroRoomSelect = 0;

    private void setInteriorDropdown() {

        PopupMenu popupMenu2 = new PopupMenu(this, findViewById(R.id.btnInteriorMenu));
        value = 0;
        if (btnInteriorMacro.getTag().toString().equals("1")) {
            opendatabase();
            itrarr = new ArrayList<>();
            SELECT_SQL = "SELECT * FROM tbl_interior ORDER BY value ASC";
            Cur = DB.rawQuery(SELECT_SQL, null);
            if (Cur != null && Cur.getCount() > 0) {
                Cur.moveToFirst();
                do {
                    String strvalue = Cur.getString(Cur.getColumnIndex("value"));
                    if (strvalue.equalsIgnoreCase("Custom Text"))
                        continue;

                    String strshortname = Cur.getString(Cur.getColumnIndex("shortname"));
                    itrarr.add(strshortname);

                    popupMenu2.getMenu().add(Menu.NONE, value, Menu.NONE, strvalue);
                    value++;
                }
                while (Cur.moveToNext());
            }
            Cur.close();
            DB.close();
        } else if (btnInteriorMacro.getTag().toString().equals("2")) {

            popupMenu2.getMenu().add(Menu.NONE, 0, Menu.NONE, "Blank");
            popupMenu2.getMenu().add(Menu.NONE, 1, Menu.NONE, "Drywall");
            popupMenu2.getMenu().add(Menu.NONE, 2, Menu.NONE, "Plaster");
            popupMenu2.getMenu().add(Menu.NONE, 3, Menu.NONE, "tile");
            popupMenu2.getMenu().add(Menu.NONE, 4, Menu.NONE, "Paint");
            popupMenu2.getMenu().add(Menu.NONE, 5, Menu.NONE, "Crown molding");
            popupMenu2.getMenu().add(Menu.NONE, 6, Menu.NONE, "Light");
            popupMenu2.getMenu().add(Menu.NONE, 7, Menu.NONE, "Ceiling fan");
            popupMenu2.getMenu().add(Menu.NONE, 8, Menu.NONE, "Wall paper");
            popupMenu2.getMenu().add(Menu.NONE, 9, Menu.NONE, "Texture");
            popupMenu2.getMenu().add(Menu.NONE, 10, Menu.NONE, "Trim");
        } else if (btnInteriorMacro.getTag().toString().equals("3") || btnInteriorMacro.getTag().toString().equals("4") || btnInteriorMacro.getTag().toString().equals("6") || btnInteriorMacro.getTag().toString().equals("7")) {
            opendatabase();

            SELECT_SQL = "SELECT * FROM tbldamagetype";
            Cur = DB.rawQuery(SELECT_SQL, null);
            if (Cur != null && Cur.getCount() > 0) {
                Cur.moveToFirst();
                do {

                    String strvalue = Cur.getString(Cur.getColumnIndex("value"));
                    if (strvalue.equalsIgnoreCase("Custom text"))
                        continue;

                    popupMenu2.getMenu().add(Menu.NONE, value, Menu.NONE, strvalue);

                    value++;
                }
                while (Cur.moveToNext());
            }
            Cur.close();
            DB.close();
        } else if (btnInteriorMacro.getTag().toString().equals("5")) {
            popupMenu2.getMenu().add(Menu.NONE, 0, Menu.NONE, "Blank");
            popupMenu2.getMenu().add(Menu.NONE, 1, Menu.NONE, "Drywall");
            popupMenu2.getMenu().add(Menu.NONE, 2, Menu.NONE, "Plaster");
            popupMenu2.getMenu().add(Menu.NONE, 3, Menu.NONE, "tile");
            popupMenu2.getMenu().add(Menu.NONE, 4, Menu.NONE, "Paint");
            popupMenu2.getMenu().add(Menu.NONE, 5, Menu.NONE, "Crown molding");
            popupMenu2.getMenu().add(Menu.NONE, 6, Menu.NONE, "Light");
            popupMenu2.getMenu().add(Menu.NONE, 7, Menu.NONE, "Wall paper");
            popupMenu2.getMenu().add(Menu.NONE, 8, Menu.NONE, "Texture");
            popupMenu2.getMenu().add(Menu.NONE, 9, Menu.NONE, "Casing");
            popupMenu2.getMenu().add(Menu.NONE, 10, Menu.NONE, "Baseboards");
            popupMenu2.getMenu().add(Menu.NONE, 11, Menu.NONE, "Door");
            popupMenu2.getMenu().add(Menu.NONE, 12, Menu.NONE, "Window");
            popupMenu2.getMenu().add(Menu.NONE, 13, Menu.NONE, "Cabinet");
            popupMenu2.getMenu().add(Menu.NONE, 14, Menu.NONE, "Countertop");
            popupMenu2.getMenu().add(Menu.NONE, 15, Menu.NONE, "Tile");
            popupMenu2.getMenu().add(Menu.NONE, 16, Menu.NONE, "Insulation");
        } else if (btnInteriorMacro.getTag().toString().equals("8")) {
            popupMenu2.getMenu().add(Menu.NONE, 0, Menu.NONE, "Blank");
            popupMenu2.getMenu().add(Menu.NONE, 1, Menu.NONE, "Tile");
            popupMenu2.getMenu().add(Menu.NONE, 2, Menu.NONE, "Vinyl");
            popupMenu2.getMenu().add(Menu.NONE, 3, Menu.NONE, "Wood");
            popupMenu2.getMenu().add(Menu.NONE, 4, Menu.NONE, "Laminate");
            popupMenu2.getMenu().add(Menu.NONE, 5, Menu.NONE, "Subfloor");
            popupMenu2.getMenu().add(Menu.NONE, 6, Menu.NONE, "Carpet");
            popupMenu2.getMenu().add(Menu.NONE, 7, Menu.NONE, "Pad");
        }

        popupMenu2.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (btnInteriorMacro.getTag().toString().equals("1"))
                    interiorMacroRoomSelect = item.getItemId();
                btnInteriorMenu.setText(item.getTitle().toString());
                getalphaname();
                return false;
            }
        });
        popupMenu2.show();
    }

    private void setInteriorDropdownTitle() {
        /*if (btnInteriorMacro.getTag().toString().equals("1")) {
            btnInteriorMenu.setText("Room");
        } else if (btnInteriorMacro.getTag().toString().equals("2")) {
            btnInteriorMenu.setText("Ceiling");
        } else if (btnInteriorMacro.getTag().toString().equals("3")) {
            btnInteriorMenu.setText("Damage");
        } else if (btnInteriorMacro.getTag().toString().equals("4")) {
            btnInteriorMenu.setText("Damage");
        } else if (btnInteriorMacro.getTag().toString().equals("5")) {
            btnInteriorMenu.setText("Wall/Area");
        } else if (btnInteriorMacro.getTag().toString().equals("6")) {
            btnInteriorMenu.setText("Damage");
        } else if (btnInteriorMacro.getTag().toString().equals("7")) {
            btnInteriorMenu.setText("Damage");
        } else if (btnInteriorMacro.getTag().toString().equals("8")) {
            btnInteriorMenu.setText("Floor");
        }*/

        // btnInteriorMenu.setText("Blank");

        if (btnInteriorMacro.getTag().toString().equals("8")) {
            btnocb.setVisibility(View.INVISIBLE);
        } else {
            btnocb.setVisibility(View.INVISIBLE);
        }

        if (btnInteriorMacro.getTag().toString().equals("4") || btnInteriorMacro.getTag().toString().equals("7")) {
            btnocb.setText("C");
            btnocb.setTag("2");
            strboc = "2";
            strboctype = "Close up";
        } else {
            btnocb.setText("B");
            btnocb.setTag("1");
            strboc = "1";
            strboctype = "Overview";
        }
        getalphaname();
    }

    private void skipInterior() {


        btnocb.setText("B");
        if (btnInteriorMacro.getTag().toString().equals("1")) {
            btnInteriorMacro.setText("Ceiling Overview");
            btnocb.setText("B");
            btnInteriorMacro.setTag("2");

        } else if (btnInteriorMacro.getTag().toString().equals("2")) {
            btnInteriorMacro.setText("Damage Overview");
            btnocb.setText("B");
            btnInteriorMacro.setTag("3");
            btnocb.setText("B");
        } else if (btnInteriorMacro.getTag().toString().equals("3")) {
            btnInteriorMacro.setText("Damage CloseUp");
            btnocb.setText("B");
            btnInteriorMacro.setTag("4");
            btnocb.setText("B");
        } else if (btnInteriorMacro.getTag().toString().equals("4")) {
            btnInteriorMacro.setText("Wall Overview");
            btnocb.setText("B");
            btnInteriorMacro.setTag("5");
            btnocb.setText("B");
        } else if (btnInteriorMacro.getTag().toString().equals("5")) {
            btnInteriorMacro.setText("Damage Overview");
            btnocb.setText("B");
            btnInteriorMacro.setTag("6");
            btnocb.setText("B");
        } else if (btnInteriorMacro.getTag().toString().equals("6")) {
            btnInteriorMacro.setText("Damage CloseUp");
            btnocb.setText("B");
            btnInteriorMacro.setTag("7");
            btnocb.setText("B");
        } else if (btnInteriorMacro.getTag().toString().equals("7")) {
            btnInteriorMacro.setText("Floor Overview");
            btnocb.setText("B");
            btnInteriorMacro.setTag("8");
            btnocb.setText("B");
        } else if (btnInteriorMacro.getTag().toString().equals("8")) {
            showoption();
            btno.setVisibility(View.VISIBLE);//dmakchange
            btnocb.setVisibility(View.INVISIBLE);
            rlInteriorPhoto.setVisibility(View.GONE);
            btnInteriorMacro.setVisibility(View.GONE);
            btnnodamages.setVisibility(View.VISIBLE);
            btno.setVisibility(View.VISIBLE);//dmakchange
            btnimgmacro.setVisibility(View.VISIBLE);
            btnimgmacro.setText("Macro");
            btnocb.setText("B");

            getalphaname();
            if (btnrei.getText().toString().equals("I")) {
                rliteriortype.setVisibility(View.VISIBLE);
            } else {
                rliteriortype.setVisibility(View.GONE);
            }
        }
        setInteriorDropdownTitle();
    }

    private void getMacroInterior() {
        popupMenu2 = new PopupMenu(this, btnInteriorMacro);

        popupMenu2.getMenu().add(Menu.NONE, 1, Menu.NONE, "Room Overview");
        popupMenu2.getMenu().add(Menu.NONE, 2, Menu.NONE, "Ceiling Overview");
        popupMenu2.getMenu().add(Menu.NONE, 3, Menu.NONE, "Damage Overview");
        popupMenu2.getMenu().add(Menu.NONE, 4, Menu.NONE, "Damage CloseUp");
        popupMenu2.getMenu().add(Menu.NONE, 5, Menu.NONE, "Wall Overview");
        popupMenu2.getMenu().add(Menu.NONE, 6, Menu.NONE, "Damage Overview");
        popupMenu2.getMenu().add(Menu.NONE, 7, Menu.NONE, "Damage CloseUp");
        popupMenu2.getMenu().add(Menu.NONE, 8, Menu.NONE, "Floor Overview");

        popupMenu2.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                btnInteriorMacro.setText(item.getTitle().toString());
                btnInteriorMacro.setTag(item.getItemId() + "");
                setInteriorDropdownTitle();
                return false;
            }
        });
        popupMenu2.show();
    }

    private void getMacrossubname() {

        popupMenu2 = new PopupMenu(this, findViewById(R.id.btnimgmacrosub));

        if (btnimgmacro.getText().toString().trim().equals("Initial")) {
            popupMenu2.getMenu().add(Menu.NONE, 0, Menu.NONE, "Risk");
            popupMenu2.getMenu().add(Menu.NONE, 1, Menu.NONE, "Front Elevation Overview");
            popupMenu2.getMenu().add(Menu.NONE, 2, Menu.NONE, "Risk2");
        } else if (btnimgmacro.getText().toString().trim().equals("Roof")) {
            popupMenu2.getMenu().add(Menu.NONE, 0, Menu.NONE, "Layers");
            popupMenu2.getMenu().add(Menu.NONE, 1, Menu.NONE, "Pitch");
            popupMenu2.getMenu().add(Menu.NONE, 2, Menu.NONE, "Shingle");
            popupMenu2.getMenu().add(Menu.NONE, 3, Menu.NONE, "Gutter");
            popupMenu2.getMenu().add(Menu.NONE, 4, Menu.NONE, "Overhang");
            popupMenu2.getMenu().add(Menu.NONE, 5, Menu.NONE, "Type of siding");
            popupMenu2.getMenu().add(Menu.NONE, 6, Menu.NONE, "Roof Overview");

        } else if (btnimgmacro.getText().toString().trim().equals("Hail")) {
            popupMenu2.getMenu().add(Menu.NONE, 0, Menu.NONE, "Overview");
            popupMenu2.getMenu().add(Menu.NONE, 1, Menu.NONE, "test sq Overview");
            popupMenu2.getMenu().add(Menu.NONE, 2, Menu.NONE, "test sq");
            popupMenu2.getMenu().add(Menu.NONE, 3, Menu.NONE, "hail damage close up on shingles");
//            popupMenu2.getMenu().add(Menu.NONE, 4, Menu.NONE, "hail damage close up on shingles 2");
//            popupMenu2.getMenu().add(Menu.NONE, 5, Menu.NONE, "hail damage close up on shingles 3");
//            popupMenu2.getMenu().add(Menu.NONE, 6, Menu.NONE, "hail damage close up on shingles 4");
//            popupMenu2.getMenu().add(Menu.NONE, 7, Menu.NONE, "hail damage close up on 1");
//            popupMenu2.getMenu().add(Menu.NONE, 8, Menu.NONE, "hail damage close up on 2");
        }

        popupMenu2.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem arg0) {

                btnimgmacrosub.setText(arg0.getTitle().toString());

                if (btnimgmacro.getText().toString().trim().equals("Hail")) {
                    setHailItem();
                } else {
                    nextPhoto(arg0.getTitle().toString());
                }

                getalphaname();

                return false;
            }
        });
        popupMenu2.show();
    }

    private void setHailItem() {


//        btnocb.setText("C");
//        btnocb.setTag("1");
//        strboc = "1";
//        strboctype = "Overview";
        btnocb.setTag("3");
        selectocb();
        hidehailoption();

        if (btnimgmacrosub.getText().toString().trim().equalsIgnoreCase("Overview") || btnimgmacrosub.getText().toString().trim().equalsIgnoreCase("test sq Overview")) {
            btnhailmenu1.setVisibility(View.GONE);
            btnhailmenu1.setText("");
        } else if (btnimgmacrosub.getText().toString().trim().equalsIgnoreCase("test sq")) {
            //change
            btnhailmenu1.setVisibility(View.GONE);

            if (btnhailtype.getText().toString().equals("Front Slope")) {
                btnhailmenu1.setText(no_frontslope);

                System.out.println("front slope" + no_frontslope);
            } else if (btnhailtype.getText().toString().equals("Right Slope")) {
                btnhailmenu1.setText(no_rightslope);
            } else if (btnhailtype.getText().toString().equals("Left Slope")) {
                btnhailmenu1.setText(no_leftslope);
            } else if (btnhailtype.getText().toString().equals("Rear Slope")) {
                btnhailmenu1.setText(no_rearslope);
            }
        } else if (btnimgmacrosub.getText().toString().trim().equalsIgnoreCase("hail damage close up on shingles") ||
                btnimgmacrosub.getText().toString().trim().equalsIgnoreCase("hail damage close up on shingles 2") ||
                btnimgmacrosub.getText().toString().trim().equalsIgnoreCase("hail damage close up on shingles 3")) {
            btnocb.setVisibility(View.INVISIBLE);
            btndamagetype1.setVisibility(View.VISIBLE);
            btnmaterial1.setVisibility(View.GONE);
            //change
            btnhailmenu1.setVisibility(View.VISIBLE);
            btnhailmenu1.setText("Shingles");
            btnhailskip.setText("Next Slope");


        } else if (btnimgmacrosub.getText().toString().trim().equals("hail damage close up on shingles 4") ||
                btnimgmacrosub.getText().toString().trim().equals("hail damage close up on 1")) {
            //change
            btnhailmenu1.setVisibility(View.VISIBLE);
            btnhailmenu1.setText("Vent");
        } else if (btnimgmacrosub.getText().toString().trim().equals("hail damage close up on 2")) {
            //change
            btnhailmenu1.setVisibility(View.VISIBLE);
            btnhailmenu1.setText("Furnace Cap");
        }
        getalphaname();
    }

    private void getMacrosname() {

        value = 0;
        macrosidarr = new ArrayList<>();
        macroname = new ArrayList<>();

        c_macrosidarr = new ArrayList<>();
        c_macroname = new ArrayList<>();


        popupMenu2 = new PopupMenu(this, findViewById(R.id.btnimgmacro));

        isdbmenu = true;

        opendatabase();
        SELECT_SQL = "SELECT * FROM tbl_macros ";
        Cur = DB.rawQuery(SELECT_SQL, null);
        if (Cur != null && Cur.getCount() > 0) {
            Cur.moveToFirst();
            do {
                macrosidarr.add("" + Cur.getInt(Cur.getColumnIndex("id")));
                macroname.add(Cur.getString(Cur.getColumnIndex("name")));


                if (Cur.getString(Cur.getColumnIndex("iscoustom")).equals("0")) {
                    c_macrosidarr.add("" + Cur.getInt(Cur.getColumnIndex("id")));
                    c_macroname.add(Cur.getString(Cur.getColumnIndex("name")));
                }
                if (Cur.getString(Cur.getColumnIndex("name")).equalsIgnoreCase("Initial"))
                    popupMenu2.getMenu().add(Menu.NONE, value, Menu.NONE, "Risk");
                else
                    popupMenu2.getMenu().add(Menu.NONE, value, Menu.NONE, Cur.getString(Cur.getColumnIndex("name")));
                value++;
            }
            while (Cur.moveToNext());
        }
        Cur.close();
        DB.close();

        popupMenu2.getMenu().add(Menu.NONE, value, Menu.NONE, "Interior");
        value++;
        popupMenu2.getMenu().add(Menu.NONE, value, Menu.NONE, "Interior New");
        value++;
        popupMenu2.getMenu().add(Menu.NONE, value, Menu.NONE, "New Macro");
        value++;
        popupMenu2.getMenu().add(Menu.NONE, value, Menu.NONE, "Room Macro");
        value++;
        popupMenu2.getMenu().add(Menu.NONE, value, Menu.NONE, "Macro");
        value++;
        popupMenu2.getMenu().add(Menu.NONE, value, Menu.NONE, "Blank");
        value++;
        popupMenu2.getMenu().add(Menu.NONE, value, Menu.NONE, "New Aditional photo");
        value++;
        popupMenu2.getMenu().add(Menu.NONE, value, Menu.NONE, "Add Custom Photo");

        popupMenu2.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem arg0) {

                btnimgmacro.setTag(arg0.getTitle().toString());

                txtalphaname.setVisibility(View.VISIBLE);
                if (!btnabc.getText().toString().equalsIgnoreCase("none"))
                    txtalphaname2.setVisibility(View.VISIBLE);

//                btniteriortype.setVisibility(View.GONE);
                btntype.setVisibility(View.INVISIBLE);
                btnnodamages.setVisibility(View.INVISIBLE);
                btno.setVisibility(View.INVISIBLE);

                btntype2.setVisibility(View.INVISIBLE);

                int pos = 0;

                if (isdbmenu) {
                    if (!arg0.getTitle().toString().equals("Add Custom Photo")) {
                        pos = getmacropos(macroname, arg0.getTitle());
                        cid_marcos = Integer.parseInt(macrosidarr.get(pos));
                    }
                }

                if (arg0.getTitle().toString().equals("Add Custom Photo")) {
                    macrosalert();
                } else if (arg0.getTitle().toString().equals("Blank")) {
                    hideallmenu();
                    rlblankback.setVisibility(View.VISIBLE);
                    btnrisk.setText("Blank");
                    btnimgmacro.setText("Blank");

                    getalphaname();

                } else if (arg0.getTitle().toString().equals("New Aditional photo")) {

//                    txtalphaname.setVisibility(View.VISIBLE);
//                    txtalphaname.setText("add name");

                    //txtalphaname.setText(addcostmphotoname);
                    hideallmenuadd();
//                    btnInteriorMacro.setText("add name");
//                    txtalphaname.setText("add name");
                    rlblankback.setVisibility(View.VISIBLE);
                    txtalphaname.setVisibility(View.VISIBLE);
                    btnrisk.setText("New Aditional photo");
                    btnimgmacro.setText("New Aditional photo");
                    getalphaname();
                    showcostmnamealert();
                    //txtalphaname.setText("add name");

                    txtalphaname.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //                showcostmnamealert();
                        }
                    });

                } else if (arg0.getTitle().toString().equals("Macro")) {

//                    rliteriortype.setVisibility(View.VISIBLE);
//                    rladdtionalback.setVisibility(View.VISIBLE);
                    btnrisk.setText("Aditional Photo");
                    btnimgmacrosub.setVisibility(View.GONE);
                    btnimgmacro.setVisibility(View.VISIBLE);

                    btntype.setVisibility(View.VISIBLE);
                    btnnodamages.setVisibility(View.VISIBLE);
                    btno.setVisibility(View.VISIBLE);//dmakchange
                    btnocb.setVisibility(View.INVISIBLE);
                    btntype2.setVisibility(View.VISIBLE);

                    btnimgmacro.setText("Macro");
                    nextPhoto("Aditional Photo");
                    getalphaname();
                } else if (arg0.getTitle().toString().equals("Interior")) {
                    showInteriorView();
                    btnocb.setText("B");
                } else if (arg0.getTitle().toString().equals("Interior New")) {
                    selectInteriorRoom("InteriorRoom");
                } else if (arg0.getTitle().toString().equals("New Macro")) {
                    showNewMacroPopup();
                } else if (arg0.getTitle().toString().equals("Room Macro")) {
                    selectInteriorRoom("RoomMacro");
//                    showRooMacroView();
                } else {
                    btnimgmacro.setVisibility(View.INVISIBLE);
                    btnimgmacro.setText(arg0.getTitle().toString());

                    if (btnimgmacro.getText().toString().equals("Risk")) {
                        showalertinfo();

                    } else if (btnimgmacro.getText().toString().equals("Roof")) {
                        btnimgmacrosub.setVisibility(View.VISIBLE);
                        btnimgmacrosub.setText("Layers");
                        nextPhoto("Layers");
                        btnpitchmenu.setText("Blank");
                    } else if (btnimgmacro.getText().toString().equals("Hail"))
                    {


                        //HailDefault
                        rbtn_frontslope.setChecked(true);
                        rbtn_rightslope.setChecked(true);
                        rbtn_rearslope.setChecked(true);
                        rbtn_leftslope.setChecked(true);

                        txt_frontslope.setText("1");
                        txt_rightslope.setText("1");
                        txt_rearslope.setText("1");
                        txt_leftslope.setText("1");

                        no_frontslope="1";

                        txt_frontslope.setVisibility(View.VISIBLE);
                        txt_rightslope.setVisibility(View.VISIBLE);
                        txt_rearslope.setVisibility(View.VISIBLE);
                        txt_leftslope.setVisibility(View.VISIBLE);

                        rlhailoptionview.setVisibility(View.VISIBLE);
                        selecthailoption(rbtn_frontslope, txt_frontslope,"1");



                        setHailItem();


                        txt_frontslope.performClick();

                    } else {
                        btnimgmacrosub.setVisibility(View.GONE);
                        btnimgmacro.setVisibility(View.VISIBLE);
                        nextPhoto(arg0.getTitle().toString());
                    }
                }

                getalphaname();


                return false;
            }
        });
        popupMenu2.show();

    }

    private void hideallmenuadd() {

        btnareatogal.setVisibility(View.GONE);
        btnSubAreaTogal.setVisibility(View.GONE);
        //   btnInteriorMacro.setVisibility(View.VISIBLE);
        rlriskphoto.setVisibility(View.GONE);//check
        rllayerphoto.setVisibility(View.GONE);//check
        rlshinglephoto.setVisibility(View.GONE);//check
        rlgutterphoto.setVisibility(View.GONE);//check
        rloverhangphoto.setVisibility(View.GONE);//check
        rlpitchphoto.setVisibility(View.GONE);//check
        llline.setVisibility(View.GONE);//check
        rlcostm.setVisibility(View.GONE);//check
        rlfeo.setVisibility(View.GONE);//check
        rlhail.setVisibility(View.GONE);//front slope

        rlmenuselection.setVisibility(View.GONE);//all options is like a test claim,damage,material,quantity
        llArea.setVisibility(View.GONE);//ceiling,wall,floor
        btnMic.setVisibility(View.GONE);//mic
        btnReport.setVisibility(View.GONE);
        btnQue.setVisibility(View.GONE);
        txtQueCount.setVisibility(View.GONE);
        rltorch.setVisibility(View.GONE);
        ibtnflash.setVisibility(View.GONE);
        ibtnLive.setVisibility(View.GONE);
        imgbtnsetting.setVisibility(View.GONE);
        zoomseek.setVisibility(View.GONE);
        rlcontrolview.setVisibility(View.GONE);
        btnocb.setVisibility(View.INVISIBLE);
        btnlastphoto.setVisibility(View.GONE);

    }


    private Button btnRoomName, btnRCnt, btnCMaterial1, btnCMaterial2, btnCMaterial3, btnWMaterial1, btnWMaterial2, btnWMaterial3, btnFMaterial1, btnSave;
    private EditText edtCOption1, edtCOption2, edtCOption3,
            edtWOption1, edtWOption2, edtWOption3, edtCWidth1, edtCWidth2, edtCWidth3,
            edtWWidth1, edtWWidth2, edtWWidth3, edtFWidth1, edtCHeight1, edtCHeight2, edtCHeight3,
            edtWHeight1, edtWHeight2, edtWHeight3, edtFHeight1;
    private ArrayList<String> arrayListNewMacro;
    private int selectNewMacroIndex = 0;
    private Dialog dialogNewMacro;

    private void showNewMacroPopup() {
        btno.setVisibility(View.VISIBLE);//dmakchange
        btnocb.setVisibility(View.INVISIBLE);
        dialogNewMacro = new Dialog(mContext);
        dialogNewMacro.setContentView(R.layout.new_macro_popup);

        btnRoomName = dialogNewMacro.findViewById(R.id.btnRoomName);
        btnRCnt = dialogNewMacro.findViewById(R.id.btnRCnt);
        btnCMaterial1 = dialogNewMacro.findViewById(R.id.btnCMaterial1);
        btnCMaterial2 = dialogNewMacro.findViewById(R.id.btnCMaterial2);
        btnCMaterial3 = dialogNewMacro.findViewById(R.id.btnCMaterial3);
        btnWMaterial1 = dialogNewMacro.findViewById(R.id.btnWMaterial1);
        btnWMaterial2 = dialogNewMacro.findViewById(R.id.btnWMaterial2);
        btnWMaterial3 = dialogNewMacro.findViewById(R.id.btnWMaterial3);
        btnFMaterial1 = dialogNewMacro.findViewById(R.id.btnFMaterial1);
        btnSave = dialogNewMacro.findViewById(R.id.btnSave);

        edtCOption1 = dialogNewMacro.findViewById(R.id.edtCOption1);
        edtCOption2 = dialogNewMacro.findViewById(R.id.edtCOption2);
        edtCOption3 = dialogNewMacro.findViewById(R.id.edtCOption3);
        edtWOption1 = dialogNewMacro.findViewById(R.id.edtWOption1);
        edtWOption2 = dialogNewMacro.findViewById(R.id.edtWOption2);
        edtWOption3 = dialogNewMacro.findViewById(R.id.edtWOption3);
        edtCWidth1 = dialogNewMacro.findViewById(R.id.edtCWidth1);
        edtCWidth2 = dialogNewMacro.findViewById(R.id.edtCWidth2);
        edtCWidth3 = dialogNewMacro.findViewById(R.id.edtCWidth3);
        edtWWidth1 = dialogNewMacro.findViewById(R.id.edtWWidth1);
        edtWWidth2 = dialogNewMacro.findViewById(R.id.edtWWidth2);
        edtWWidth3 = dialogNewMacro.findViewById(R.id.edtWWidth3);
        edtFWidth1 = dialogNewMacro.findViewById(R.id.edtFWidth1);
        edtCHeight1 = dialogNewMacro.findViewById(R.id.edtCHeight1);
        edtCHeight2 = dialogNewMacro.findViewById(R.id.edtCHeight2);
        edtCHeight3 = dialogNewMacro.findViewById(R.id.edtCHeight3);
        edtWHeight1 = dialogNewMacro.findViewById(R.id.edtWHeight1);
        edtWHeight2 = dialogNewMacro.findViewById(R.id.edtWHeight2);
        edtWHeight3 = dialogNewMacro.findViewById(R.id.edtWHeight3);
        edtFHeight1 = dialogNewMacro.findViewById(R.id.edtFHeight1);

        btnRoomName.setOnClickListener(onClickNewMacro());
        btnRCnt.setOnClickListener(onClickNewMacro());
        btnCMaterial1.setOnClickListener(onClickNewMacro());
        btnCMaterial2.setOnClickListener(onClickNewMacro());
        btnCMaterial3.setOnClickListener(onClickNewMacro());
        btnWMaterial1.setOnClickListener(onClickNewMacro());
        btnWMaterial2.setOnClickListener(onClickNewMacro());
        btnWMaterial3.setOnClickListener(onClickNewMacro());
        btnFMaterial1.setOnClickListener(onClickNewMacro());
        btnSave.setOnClickListener(onClickNewMacro());

        dialogNewMacro.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
            }
        });

        dialogNewMacro.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                onClick(rlNewMacroBack);
            }
        });

        dialogNewMacro.show();
    }

    private View.OnClickListener onClickNewMacro() {
        return new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if (view.getId() == btnRoomName.getId()) {
                    clickonView("btnRoomName");

                    value = 0;
                    itrarr = new ArrayList<>();

                    PopupMenu popupMenu2 = new PopupMenu(mContext, btnRoomName);

                    opendatabase();

                    SELECT_SQL = "SELECT * FROM tbl_interior ORDER BY value ASC";
                    Cur = DB.rawQuery(SELECT_SQL, null);
                    if (Cur != null && Cur.getCount() > 0) {
                        Cur.moveToFirst();
                        do {

                            String strvalue = Cur.getString(Cur.getColumnIndex("value"));
                            if (strvalue.equalsIgnoreCase("Custom Text"))
                                continue;
                            popupMenu2.getMenu().add(Menu.NONE, value, Menu.NONE, strvalue);

                            value++;
                        }
                        while (Cur.moveToNext());
                    }
                    Cur.close();
                    DB.close();

                    popupMenu2.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                        @Override
                        public boolean onMenuItemClick(MenuItem arg0) {

                            btnRoomName.setText(arg0.getTitle().toString());
                            return false;
                        }
                    });
                    popupMenu2.show();
                } else if (view.getId() == btnRCnt.getId()) {
                    clickonView("btnRCnt");

                    value = 0;
                    popupMenu2 = new PopupMenu(mContext, btnRCnt);
                    opendatabase();
                    SELECT_SQL = "SELECT * FROM tbl_matrialsubmenu";
                    Cur = DB.rawQuery(SELECT_SQL, null);
                    if (Cur != null || Cur.getCount() <= 0) {

                        Cur.moveToFirst();
                        do {
                            String strvalue = Cur.getString(Cur.getColumnIndex("value"));
                            popupMenu2.getMenu().add(Menu.NONE, value, Menu.NONE, strvalue);
                            value++;
                        } while (Cur.moveToNext());
                    }
                    Cur.close();
                    DB.close();
                    popupMenu2.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem arg0) {
                            btnRCnt.setText(arg0.getTitle());
                            return false;
                        }
                    });
                    popupMenu2.show();
                } else if (view.getId() == btnCMaterial1.getId() || view.getId() == btnCMaterial2.getId() || view.getId() == btnCMaterial3.getId()) {
                    clickonView("btnCMaterial1,btnCMaterial2,btnCMaterial3");

                    PopupMenu popupMenu;
                    if (view.getId() == btnCMaterial1.getId())
                        popupMenu = new PopupMenu(mContext, btnCMaterial1);
                    else if (view.getId() == btnCMaterial2.getId())
                        popupMenu = new PopupMenu(mContext, btnCMaterial2);
                    else
                        popupMenu = new PopupMenu(mContext, btnCMaterial3);

                    popupMenu.getMenu().add(Menu.NONE, 0, Menu.NONE, "Blank");
                    popupMenu.getMenu().add(Menu.NONE, 1, Menu.NONE, "Drywall");
                    popupMenu.getMenu().add(Menu.NONE, 2, Menu.NONE, "Plaster");
                    popupMenu.getMenu().add(Menu.NONE, 3, Menu.NONE, "tile");
                    popupMenu.getMenu().add(Menu.NONE, 4, Menu.NONE, "Paint");
                    popupMenu.getMenu().add(Menu.NONE, 5, Menu.NONE, "Crown molding");
                    popupMenu.getMenu().add(Menu.NONE, 6, Menu.NONE, "Light");
                    popupMenu.getMenu().add(Menu.NONE, 7, Menu.NONE, "Ceiling fan");
                    popupMenu.getMenu().add(Menu.NONE, 8, Menu.NONE, "Wall paper");
                    popupMenu.getMenu().add(Menu.NONE, 9, Menu.NONE, "Texture");
                    popupMenu.getMenu().add(Menu.NONE, 10, Menu.NONE, "Trim");

                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            if (view.getId() == btnCMaterial1.getId())
                                btnCMaterial1.setText(item.getTitle().toString());
                            else if (view.getId() == btnCMaterial2.getId())
                                btnCMaterial2.setText(item.getTitle().toString());
                            else
                                btnCMaterial3.setText(item.getTitle().toString());
                            return false;
                        }
                    });
                    popupMenu.show();
                } else if (view.getId() == btnWMaterial1.getId() || view.getId() == btnWMaterial2.getId() || view.getId() == btnWMaterial3.getId()) {

                    clickonView("btnWMaterial1,btnWMaterial2,btnWMaterial3");

                    PopupMenu popupMenu;
                    if (view.getId() == btnWMaterial1.getId())
                        popupMenu = new PopupMenu(mContext, btnWMaterial1);
                    else if (view.getId() == btnWMaterial2.getId())
                        popupMenu = new PopupMenu(mContext, btnWMaterial2);
                    else
                        popupMenu = new PopupMenu(mContext, btnWMaterial3);

                    popupMenu.getMenu().add(Menu.NONE, 0, Menu.NONE, "Blank");
                    popupMenu.getMenu().add(Menu.NONE, 1, Menu.NONE, "Drywall");
                    popupMenu.getMenu().add(Menu.NONE, 2, Menu.NONE, "Plaster");
                    popupMenu.getMenu().add(Menu.NONE, 3, Menu.NONE, "tile");
                    popupMenu.getMenu().add(Menu.NONE, 4, Menu.NONE, "Paint");
                    popupMenu.getMenu().add(Menu.NONE, 5, Menu.NONE, "Crown molding");
                    popupMenu.getMenu().add(Menu.NONE, 6, Menu.NONE, "Light");
                    popupMenu.getMenu().add(Menu.NONE, 7, Menu.NONE, "Wall paper");
                    popupMenu.getMenu().add(Menu.NONE, 8, Menu.NONE, "Texture");
                    popupMenu.getMenu().add(Menu.NONE, 9, Menu.NONE, "Casing");
                    popupMenu.getMenu().add(Menu.NONE, 10, Menu.NONE, "Baseboards");
                    popupMenu.getMenu().add(Menu.NONE, 11, Menu.NONE, "Door");
                    popupMenu.getMenu().add(Menu.NONE, 12, Menu.NONE, "Window");
                    popupMenu.getMenu().add(Menu.NONE, 13, Menu.NONE, "Cabinet");
                    popupMenu.getMenu().add(Menu.NONE, 14, Menu.NONE, "Countertop");
                    popupMenu.getMenu().add(Menu.NONE, 15, Menu.NONE, "Tile");
                    popupMenu.getMenu().add(Menu.NONE, 16, Menu.NONE, "Insulation");

                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            if (view.getId() == btnWMaterial1.getId())
                                btnWMaterial1.setText(item.getTitle().toString());
                            else if (view.getId() == btnWMaterial2.getId())
                                btnWMaterial2.setText(item.getTitle().toString());
                            else
                                btnWMaterial3.setText(item.getTitle().toString());
                            return false;
                        }
                    });
                    popupMenu.show();
                } else if (view.getId() == btnFMaterial1.getId()) {
                    PopupMenu popupMenu = new PopupMenu(mContext, btnFMaterial1);

                    popupMenu.getMenu().add(Menu.NONE, 0, Menu.NONE, "Blank");
                    popupMenu.getMenu().add(Menu.NONE, 1, Menu.NONE, "Tile");
                    popupMenu.getMenu().add(Menu.NONE, 2, Menu.NONE, "Vinyl");
                    popupMenu.getMenu().add(Menu.NONE, 3, Menu.NONE, "Wood");
                    popupMenu.getMenu().add(Menu.NONE, 4, Menu.NONE, "Laminate");
                    popupMenu.getMenu().add(Menu.NONE, 5, Menu.NONE, "Subfloor");
                    popupMenu.getMenu().add(Menu.NONE, 6, Menu.NONE, "Carpet");
                    popupMenu.getMenu().add(Menu.NONE, 7, Menu.NONE, "Pad");

                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            btnFMaterial1.setText(item.getTitle().toString());
                            return false;
                        }
                    });

                    popupMenu.show();
                } else if (view.getId() == btnSave.getId()) {

                    clickonView("btnSave");

                    selectNewMacroIndex = 0;
                    arrayListNewMacro = new ArrayList<>();
                    String rName = btnRoomName.getText().toString() + (btnRCnt.getText().toString().equals("0") ? "" : " " + btnRCnt.getText().toString()) + " ";

                    arrayListNewMacro.add(rName + "Ceiling "/*+edtCOption1.getText().toString()+" "*/ + btnCMaterial1.getText().toString()/*+" "+edtCWidth1.getText().toString()+" "+edtCHeight1.getText().toString()*/);
                    arrayListNewMacro.add(rName + "Ceiling "/*+edtCOption2.getText().toString()+" "*/ + btnCMaterial2.getText().toString()/*+" "+edtCWidth2.getText().toString()+" "+edtCHeight2.getText().toString()*/);
                    arrayListNewMacro.add(rName + "Ceiling "/*+edtCOption3.getText().toString()+" "*/ + btnCMaterial3.getText().toString()/*+" "+edtCWidth3.getText().toString()+" "+edtCHeight3.getText().toString()*/);

                    arrayListNewMacro.add(rName + "Wall "/*+edtWOption1.getText().toString()+" "*/ + btnWMaterial1.getText().toString()/*+" "+edtWWidth1.getText().toString()+" "+edtWHeight1.getText().toString()*/);
                    arrayListNewMacro.add(rName + "Wall "/*+edtWOption2.getText().toString()+" "*/ + btnWMaterial2.getText().toString()/*+" "+edtWWidth2.getText().toString()+" "+edtWHeight2.getText().toString()*/);
                    arrayListNewMacro.add(rName + "Wall "/*+edtWOption3.getText().toString()+" "*/ + btnWMaterial3.getText().toString()/*+" "+edtWWidth3.getText().toString()+" "+edtWHeight3.getText().toString()*/);

                    arrayListNewMacro.add(rName + "Floor " + btnFMaterial1.getText().toString()/*+" "+edtFWidth1.getText().toString()+" "+edtFHeight1.getText().toString()*/);

                    dialogNewMacro.dismiss();
                    showNewMacro();
                }
            }
        };
    }

    private void skipNewMacro() {
        btno.setVisibility(View.VISIBLE);//dmakchange
        btnocb.setVisibility(View.INVISIBLE);
        selectNewMacroIndex++;
        btnNewMacroDamage.setText("Damage");
        btnNewMacroDamage.setTag("Blank");
        if (selectNewMacroIndex == arrayListNewMacro.size()) {
            onClick(rlNewMacroBack);
        }
        getalphaname();
    }

    private void showNewMacro() {
        btnimgmacro.setVisibility(View.INVISIBLE);
        rlNewMacro.setVisibility(View.VISIBLE);
        btnInteriorMacro.setVisibility(View.VISIBLE);
        btnInteriorMacro.setText("Room Overview");
        btnInteriorMacro.setTag("1");

        btnareatogal.setVisibility(View.GONE);
        btnSubAreaTogal.setVisibility(View.GONE);

        rlriskphoto.setVisibility(View.GONE);
        rllayerphoto.setVisibility(View.GONE);
        rlshinglephoto.setVisibility(View.GONE);
        rlgutterphoto.setVisibility(View.GONE);
        rloverhangphoto.setVisibility(View.GONE);
        rlpitchphoto.setVisibility(View.GONE);
        llline.setVisibility(View.GONE);
        rlcostm.setVisibility(View.GONE);
        rlfeo.setVisibility(View.GONE);
        rlhail.setVisibility(View.GONE);

        rlmenuselection.setVisibility(View.GONE);
        llArea.setVisibility(View.GONE);
        btnMic.setVisibility(View.GONE);
        btnReport.setVisibility(View.GONE);
        rltorch.setVisibility(View.VISIBLE);
        ibtnflash.setVisibility(View.VISIBLE);
        ibtnLive.setVisibility(View.INVISIBLE);
        imgbtnsetting.setVisibility(View.INVISIBLE);
        rlcontrolview.setVisibility(View.INVISIBLE);
        btnocb.setVisibility(View.INVISIBLE);
        btnlastphoto.setVisibility(View.INVISIBLE);

        btntype.setVisibility(View.INVISIBLE);

        btntype2.setVisibility(View.GONE);

        rlcontrolview.setVisibility(View.INVISIBLE);
        rliteriortype.setVisibility(View.INVISIBLE);

        rliteriortype.setVisibility(View.INVISIBLE);

        getalphaname();
    }

    private void newMacroDamage() {
        value = 0;

        PopupMenu popupMenu2 = new PopupMenu(this, btnNewMacroDamage);

        opendatabase();

        SELECT_SQL = "SELECT * FROM tbldamagetype";
        Cur = DB.rawQuery(SELECT_SQL, null);
        if (Cur != null && Cur.getCount() > 0) {
            Cur.moveToFirst();
            do {

                String strvalue = Cur.getString(Cur.getColumnIndex("value"));
                if (strvalue.equals("Custom text"))
                    continue;
                popupMenu2.getMenu().add(Menu.NONE, value, Menu.NONE, strvalue);
                value++;
            }
            while (Cur.moveToNext());
        }
        Cur.close();
        DB.close();

        popupMenu2.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem arg0) {
                btnNewMacroDamage.setText(arg0.getTitle().toString());
                btnNewMacroDamage.setTag(arg0.getTitle().toString());
                if (arg0.getTitle().toString().equalsIgnoreCase("Blank"))
                    btnNewMacroDamage.setText("Damage");

//                arrayListNewMacro.set(selectNewMacroIndex, arrayListNewMacro.get(selectNewMacroIndex)+" ")
                getalphaname();

                return false;
            }
        });
        popupMenu2.show();
    }

    private void hidefenceallmenu() {

        btnQty.setVisibility(View.INVISIBLE);
        btnInsulation.setVisibility(View.INVISIBLE);
        btnareatogal.setVisibility(View.INVISIBLE);
        btnSubAreaTogal.setVisibility(View.INVISIBLE);

        rlriskphoto.setVisibility(View.GONE);
        rllayerphoto.setVisibility(View.GONE);
        rlshinglephoto.setVisibility(View.GONE);
        rlgutterphoto.setVisibility(View.GONE);
        rloverhangphoto.setVisibility(View.GONE);
        rlpitchphoto.setVisibility(View.GONE);
        llline.setVisibility(View.GONE);
        rlcostm.setVisibility(View.GONE);
        rlfeo.setVisibility(View.GONE);
        rlhail.setVisibility(View.GONE);

        rlmenuselection.setVisibility(View.VISIBLE);
        //    btnMic.setVisibility(View.VISIBLE);
        btnReport.setVisibility(View.VISIBLE);
        btnQue.setVisibility(View.VISIBLE);
        llArea.setVisibility(View.GONE);
        setQueCount();

        rladdtionalback.setVisibility(View.VISIBLE);

        rltorch.setVisibility(View.VISIBLE);
        ibtnflash.setVisibility(View.VISIBLE);
        ibtnLive.setVisibility(View.VISIBLE);
        imgbtnsetting.setVisibility(View.VISIBLE);
        zoomseek.setVisibility(View.VISIBLE);
        rlcontrolview.setVisibility(View.VISIBLE);
        btnocb.setVisibility(View.INVISIBLE);
        btnlastphoto.setVisibility(View.VISIBLE);
        btncat.setVisibility(View.VISIBLE);
        btnabc.setVisibility(View.VISIBLE);
        btnnodamages.setVisibility(View.INVISIBLE);
        btno.setVisibility(View.INVISIBLE);
        btnimgmacro.setVisibility(View.INVISIBLE);
        btnrisk.setVisibility(View.INVISIBLE);

        btnmaterial.setVisibility(View.INVISIBLE);
        btnarea.setVisibility(View.INVISIBLE);
        btnrei.setVisibility(View.INVISIBLE);

        btnmatrialsubmenu.setVisibility(View.INVISIBLE);
        btniteriortype.setVisibility(View.GONE);
        btntype.setVisibility(View.INVISIBLE);
        btntype2.setVisibility(View.INVISIBLE);
    }


    private void hideallmenu() {

        btnareatogal.setVisibility(View.GONE);
        btnSubAreaTogal.setVisibility(View.GONE);
        btnnc.setVisibility(View.VISIBLE);//Dmakchange
        rlriskphoto.setVisibility(View.GONE);
        rllayerphoto.setVisibility(View.GONE);
        rlshinglephoto.setVisibility(View.GONE);
        rlgutterphoto.setVisibility(View.GONE);
        rloverhangphoto.setVisibility(View.GONE);
        rlpitchphoto.setVisibility(View.GONE);
        llline.setVisibility(View.GONE);
        rlcostm.setVisibility(View.GONE);
        rlfeo.setVisibility(View.GONE);
        rlhail.setVisibility(View.GONE);

        rlmenuselection.setVisibility(View.GONE);
        llArea.setVisibility(View.GONE);
        btnMic.setVisibility(View.GONE);
        btnReport.setVisibility(View.GONE);
        btnQue.setVisibility(View.GONE);
        txtQueCount.setVisibility(View.GONE);
        rltorch.setVisibility(View.GONE);
        ibtnflash.setVisibility(View.GONE);
        ibtnLive.setVisibility(View.GONE);
        imgbtnsetting.setVisibility(View.GONE);
        zoomseek.setVisibility(View.GONE);
        rlcontrolview.setVisibility(View.GONE);
        btnocb.setVisibility(View.INVISIBLE);
        btnlastphoto.setVisibility(View.GONE);
    }

    private int getmacropos(ArrayList<String> macroname, CharSequence title) {

        for (int i = 0; i < macroname.size(); i++) {
            if (title.equals(macroname.get(i).toString())) {
                return i;
            }
        }
        return 0;
    }

    private void macrosalert() {

        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setCancelable(false);
        alert.setTitle("Add Macros");
        final EditText input = new EditText(this);
        input.setHint("Enter Macros Name");
        alert.setView(input);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                String value = input.getText().toString().trim();

                if (value.equals("")) {
                    Toast.makeText(getApplicationContext(), "please enter macros name", Toast.LENGTH_SHORT).show();

                } else if (isexists("tbl_macros", value)) {
                    Toast.makeText(getApplicationContext(), "Name is already Exists", Toast.LENGTH_SHORT).show();


                } else {
//                    btnrisk.setText(value);
                    dialog.cancel();

                    addmacros(value);
                }

            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
            }
        });
        alert.show();
    }

    private boolean isexists(String tbl, String value) {

        opendatabase();
        Cursor cursor = DB.rawQuery("SELECT  * FROM " + tbl + " where name = '" + value + "' ", null);
        int count = cursor.getCount();

        if (count == 0) {
            return false;
        } else {
            return true;
        }

    }

    private void addmacros(String name) {

        opendatabase();
        DB.execSQL("insert into tbl_macros (name)" + "values('" + name + "') ;");
//        hidecostmoption();

    }

    private void addsubcat(String name) {

        opendatabase();
        DB.execSQL("insert into tblsubcat (name)" + "values('" + name + "') ;");
        btnabc.setText(name);

        getsumcat();
    }


    private void addInteriorData(String areaname,String subareaname,String damagename,String roomtype,String no, String buildingtype) {

        String claimId = PrefManager.getClaimId();

        opendatabase();
        DB.execSQL("insert into tbl_interior_savedata ('areaname','subareaname','damagename','roomtype','no','claim_id','buildingtype')" + "values('" + areaname + "','" + subareaname + "','" + damagename + "','" + roomtype + "','" + no + "','"+claimId+"','"+buildingtype+"') ;");
        DB.close();
//        getInteriorSaveData();


    }

//    private void getInteriorSaveData() {
//
//
//        opendatabase();
//
//        SELECT_SQL = "SELECT * FROM tbl_interior_savedata";
//        Cur = DB.rawQuery(SELECT_SQL, null);
//        if (Cur != null && Cur.getCount() > 0) {
//            Cur.moveToFirst();
//            do {
//
//                String areaname = Cur.getString(Cur.getColumnIndex("areaname"));
//                String subareaname = Cur.getString(Cur.getColumnIndex("subareaname"));
//
//                Log.e("savedataareaname-->",""+areaname);
//                Log.e("savedatasubareaname-->",""+subareaname);
//
//
//            }
//            while (Cur.moveToNext());
//        }
//        Cur.close();
//        DB.close();
//
//    }


    private void subcatalert() {

        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setCancelable(false);
        alert.setTitle("Add Name");
        final EditText input = new EditText(this);
        input.setHint("Enter Name");
        alert.setView(input);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                String value = input.getText().toString().trim();

                if (value.equals("")) {
                    Toast.makeText(getApplicationContext(), "please enter name", Toast.LENGTH_SHORT).show();

                } else if (isexists("tblsubcat", value)) {
                    Toast.makeText(getApplicationContext(), "Name is already Exists", Toast.LENGTH_SHORT).show();


                } else {
//                    btnrisk.setText(value);
                    dialog.cancel();

                    addsubcat(value);
                }}
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
            }
        });
        alert.show();
    }


    private void uploadMedia(String imgpath, String imgname) {
        try {

            Log.e("saveimgpath", "===>" + imgpath);



            /*
            String charset = "UTF-8";
            File uploadFile1 = new File(imgpath);
            String requestURL = "";

            MultipartUtility multipart = new MultipartUtility(requestURL, charset);

//            multipart.addHeaderField("User-Agent", "CodeJava");
//            multipart.addHeaderField("Test-Header", "Header-Value");

            multipart.addFormField("userid", "Cool Pictures");
            multipart.addFormField("filepath", imgpath);
            multipart.addFilePart("file", uploadFile1);
            List<String> response = multipart.finish();
            Log.v("rht", "SERVER REPLIED:");
            for (String line : response)
            {
                Log.v("rht", "Line : "+line);
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent me) {
        // Call onTouchEvent of SimpleGestureFilter class
        this.detector.onTouchEvent(me);
        return super.dispatchTouchEvent(me);
    }

    @Override
    public void onSwipe(int direction) {
        String str = "";

       /* switch (direction) {
//
//            case SimpleGestureFilter.SWIPE_RIGHT : str = "Swipe Right";
//                break;
//            case SimpleGestureFilter.SWIPE_LEFT :  str = "Swipe Left";
//                break;
//            case SimpleGestureFilter.SWIPE_DOWN :  str = "Swipe Down";
//                break;
            case SimpleGestureFilter.SWIPE_UP:
                Intent intent = new Intent(HomeActivity.this, SwipeUpActivity.class);
                intent.putExtra("select", btnrei.getText().toString().equalsIgnoreCase("r") ? "Roof" : btnrei.getText().toString().equalsIgnoreCase("e") ? "Elevations" : btnrei.getText().toString().equalsIgnoreCase("i") ? "Interior" : "");
                startActivity(intent);
                overridePendingTransition(R.anim.right_out, R.anim.left_out);

                *//*txtBNm.setText(btnrei.getText().toString().equalsIgnoreCase("r") ? "Roof" : btnrei.getText().toString().equalsIgnoreCase("e") ? "Elevations" : btnrei.getText().toString().equalsIgnoreCase("i") ? "Interior" : "");

                if (rlBottomView2.getVisibility() == View.GONE) {
                    rlBottomView2.setVisibility(View.VISIBLE);
                    Animation slide = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.01f,
                            Animation.RELATIVE_TO_SELF, 0.00f, Animation.RELATIVE_TO_SELF,
                            1.0f, Animation.RELATIVE_TO_SELF, 0.0f);

                    slide.setDuration(400);
                    slide.setFillAfter(true);
                    slide.setFillEnabled(true);
                    rlBottomView2.startAnimation(slide);
                    slide.setAnimationListener(new Animation.AnimationListener() {

                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {

                            rlBottomView2.clearAnimation();

                        }

                    });
                }*//*
                break;
            case SimpleGestureFilter.SWIPE_DOWN:
                Animation slide = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                        Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                        0.0f, Animation.RELATIVE_TO_SELF, 1.0f);

                slide.setDuration(400);
                slide.setFillAfter(true);
                slide.setFillEnabled(true);
                rlBottomView2.startAnimation(slide);
                slide.setAnimationListener(new Animation.AnimationListener() {

                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                        rlBottomView2.clearAnimation();

                    *//*RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                            rlBottomView2.getWidth(), rlBottomView2.getHeight());
                    lp.setMargins(0, rlBottomView2.getWidth(), 0, 0);
                    lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    rlBottomView2.setLayoutParams(lp);*//*

                    }
                });
                rlBottomView2.setVisibility(View.GONE);
                break;

        }*/
    }

    @Override
    public void onDoubleTap() {
//        Toast.makeText(this, "Double Tap", Toast.LENGTH_SHORT).show();
    }

    private class Upload_R_Pic extends AsyncTask<String, String, String> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(HomeActivity.this);
            pDialog.setMessage("Upload...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();

        }

        @Override
        protected String doInBackground(String... args) {
            try {

                String imgpathurl = args[0];

                String charset = "UTF-8";
                File uploadFile = new File(imgpathurl);
                String requestURL = "https://www.mylogisticsgroup.com/map/api/photo-server/AddPhoto/addroof";

                MultipartUtility multipart = new MultipartUtility(requestURL, charset);

//            multipart.addHeaderField("User-Agent", "CodeJava");
//            multipart.addHeaderField("Test-Header", "Header-Value");

                multipart.addFormField("claim_id", "13");
                multipart.addFormField("rei", "r");
                multipart.addFormField("sub_folder", "1");
                multipart.addFormField("sub_folder_value", btnabc.getText().toString().equalsIgnoreCase("none") ? "" : btnabc.getText().toString());
                multipart.addFormField("damage_type", btndamagetype.getText().toString());
                multipart.addFormField("material", btnmaterial.getText().toString());
                multipart.addFormField("overview", strboc);
                multipart.addFormField("adjust", selectslope.trim());
                multipart.addFormField("claim_folder", btncat.getText().toString());

                multipart.addFilePart("photo", uploadFile);

                response = multipart.finish();

                Log.i(TAG, "SERVER REPLIED:");

                for (String line : response) {
                    Log.i(TAG, "Line : " + line);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response.toString();
        }

        @Override
        protected void onPostExecute(String response) {
            pDialog.dismiss();
            Log.e("Uploadimage", "==>" + response);

            try {
                JSONArray jarr = new JSONArray(response);

                JSONObject jobj = jarr.getJSONObject(0);

                String success = jobj.getString("success");

                if (success.equals("1")) {
                    String msg = jobj.getString("message");
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class Upload_E_Pic extends AsyncTask<String, String, String> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(HomeActivity.this);
            pDialog.setMessage("Upload...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();

        }

        @Override
        protected String doInBackground(String... args) {
            try {

                String imgpathurl = args[0];

                String charset = "UTF-8";
                File uploadFile = new File(imgpathurl);
                String requestURL = "https://www.mylogisticsgroup.com/map/api/photo-server/AddPhoto/addelevation";

                MultipartUtility multipart = new MultipartUtility(requestURL, charset);

                multipart.addFormField("claim_id", "13");
                multipart.addFormField("rei", "e");
                multipart.addFormField("sub_folder", "1");
                multipart.addFormField("sub_folder_value", btnabc.getText().toString().equalsIgnoreCase("none") ? "" : btnabc.getText().toString());
                multipart.addFormField("damage_type", btndamagetype.getText().toString());
                multipart.addFormField("material", btnmaterial.getText().toString());
                multipart.addFormField("overview", strboc);
                multipart.addFormField("adjust", selectslope.toString().trim());
                multipart.addFormField("claim_folder", btncat.getText().toString());

                multipart.addFilePart("photo", uploadFile);

                response = multipart.finish();

                Log.v("rht", "SERVER REPLIED:");

                for (String line : response) {
                    Log.v("rht", "Line : " + line);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response.toString();
        }

        @Override
        protected void onPostExecute(String response) {
            pDialog.dismiss();
            Log.e("Uploadimage", "==>" + response);

            try {
                JSONArray jarr = new JSONArray(response);

                JSONObject jobj = jarr.getJSONObject(0);

                String success = jobj.getString("success");

                if (success.equals("1")) {
                    String msg = jobj.getString("message");
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class Upload_I_Pic extends AsyncTask<String, String, String> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(HomeActivity.this);
            pDialog.setMessage("Upload...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();

        }

        @Override
        protected String doInBackground(String... args) {
            try {
                String imgpathurl = args[0];

                String charset = "UTF-8";
                File uploadFile = new File(imgpathurl);
                String requestURL = "https://www.mylogisticsgroup.com/map/api/photo-server/AddPhoto/addinterior";

                MultipartUtility multipart = new MultipartUtility(requestURL, charset);

//            multipart.addHeaderField("User-Agent", "CodeJava");
//            multipart.addHeaderField("Test-Header", "Header-Value");

                multipart.addFormField("claim_id", "13");
                multipart.addFormField("rei", "i");
                multipart.addFormField("sub_folder", "1");
                multipart.addFormField("sub_folder_value", btnabc.getText().toString().equalsIgnoreCase("none") ? "" : btnabc.getText().toString());
                multipart.addFormField("damage_type", btndamagetype.getText().toString());
                multipart.addFormField("overview", strboc);
                multipart.addFormField("adjust", "");
                multipart.addFormField("area", btnarea.getText().toString());
                multipart.addFormField("sub_area", btnmaterial.getText().toString());
                multipart.addFormField("room", btniteriortype.getText().toString());
                multipart.addFormField("damage_value", btnmatrialsubmenu.getText().toString());

                multipart.addFormField("claim_folder", btncat.getText().toString());

                multipart.addFilePart("photo", uploadFile);

                response = multipart.finish();

                Log.v("rht", "SERVER REPLIED:");

                for (String line : response) {
                    Log.v("rht", "Line : " + line);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response.toString();
        }

        @Override
        protected void onPostExecute(String response) {
            pDialog.dismiss();
            Log.e("Uploadimage", "==>" + response);

            try {
                JSONArray jarr = new JSONArray(response);

                JSONObject jobj = jarr.getJSONObject(0);

                String success = jobj.getString("success");

                if (success.equals("1")) {
                    String msg = jobj.getString("message");
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public Bitmap takeScreenshot() {

        rllayerphoto.setDrawingCacheEnabled(true);
        rllayerphoto.buildDrawingCache(true);
        b1 = Bitmap.createBitmap(rllayerphoto.getDrawingCache(true));
        rllayerphoto.setDrawingCacheEnabled(false); // clear drawing cache
        return b1;
    }

//    public void saveBitmap(Bitmap bitmap) {
//        if (bitmap == null) {
//            return;
//        }
//        final String root = Environment.getExternalStorageDirectory()
//                .getAbsolutePath();
//        File newDir = new File(root + "/New Aditional photo");
//
//        newDir.mkdirs();
//        Random gen = new Random();
//
//        int n = 10000;
//        n = gen.nextInt(n);
//        String fotoname = "Image" + n + ".jpg";
//        File imagePath1 = new File(newDir, fotoname);
//        FileOutputStream fos;
//        try {
//            fos = new FileOutputStream(imagePath1);
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
//            fos.flush();
//            fos.close();
//        } catch (IOException e) {
//            Log.e("GREC", e.getMessage(), e);
//        }
//    }

    public final boolean isInternetOn() {
        // get Connectivity Manager object to check connection
        ConnectivityManager connec =
                (ConnectivityManager) getSystemService(getBaseContext().CONNECTIVITY_SERVICE);
        // Check for network connections
        if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {
            // if connected with internet
            Toast.makeText(this, " Connected ", Toast.LENGTH_LONG).show();
            return true;
        } else if (
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {
            Toast.makeText(this, " Not Connected ", Toast.LENGTH_LONG).show();
            return false;
        }
        return false;
    }
}
