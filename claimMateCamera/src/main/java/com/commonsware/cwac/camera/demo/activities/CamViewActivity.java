package com.commonsware.cwac.camera.demo.activities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaScannerConnection;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.commonsware.cwac.camera.demo.other.MyApplication;
import com.commonsware.cwac.camera.demo.other.MyApplication.TrackerName;
import com.commonsware.cwac.camera.demo.db.ClaimSqlLiteDbHelper;
import com.example.claimmate.R;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.rampo.updatechecker.UpdateChecker;
import com.rampo.updatechecker.UpdateCheckerResult;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class CamViewActivity extends Activity implements SurfaceHolder.Callback, View.OnClickListener, UpdateCheckerResult ,SensorEventListener {

    PopupMenu popupMenu2;
    Camera camera;
    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;

    PictureCallback rawCallback;
    ShutterCallback shutterCallback;
    PictureCallback jpegCallback;


    Button btnrei,btndamagetype,btnabc,btnmaterial,btntype;
    ImageButton ibtnflash,imgbtnsetting,imgbtncam;
    ImageView  imgtop,imgleft,imgright,imgbottom;
    TextView txtcenter;

    File  savefile,mydir,subdir ;

    Button btnlastphoto;
    File[] allFiles ;

    Boolean opengallery = false;

    SharedPreferences lastpathpf ;
    SharedPreferences.Editor lastimageeditor ;

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
    SimpleDateFormat imgdateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    Button btnocb;

    String selectslope = "";

    SeekBar zoomseek;

    boolean hasFlash;

    AlertDialog flashalert;
    AlertDialog updateappalert;

    RelativeLayout rlsetting;


    EditText txtfoldername;
    Button btnok , btncancel;

    String appfoldername="";

    RelativeLayout rlcontrolview , rliteriortype;

    Button btniteriortype;

    String stralphabetname = "a";

    String strboc = "1";
    String strboctype = "Overview";

    String strreitype ="";

    CheckBox chkdate;

    int iteriortypeindex = 0;

//    String[] itrarr = new String[]{"Blank","Bed","Bed1","Bed2","Bed3","MstrBed","MstrBath","Hallway","Foyer","Liv Rm","Din Rm","Basement","Bath","Fmly Rm","Office","TV Rm","Storage","cstom txt"};

    ArrayList<String> itrarr;
    ArrayList<String> macrosarr;

    RelativeLayout rltorch;

    int photoindex = 1;
    int riskindex = 1;

    UpdateChecker checker;

    Tracker t;

    RelativeLayout rlmenuselection;
    RelativeLayout rlriskphoto ,rllayerphoto,rlshinglephoto,rlgutterphoto,rloverhangphoto,rlpitchphoto,rlcostm;
    RelativeLayout rlBack,rllayerBack,rlshingleBack,rlgutterBack,rlOverhangBack,rlpitchBack,rlcostmback;

    Button btnrisk, btnskip ,btnlayerskip, btnpitchskip,btnshingleskip,btngutterskip,btnoverhangskip,btncostmskip;


    SensorManager mSensorManager;
    Sensor accelerometer;
    Sensor magnetometer;

    float[] mGravity;
    float[] mGeomagnetic;
    private float[] mMagnetic;


    String addcostmphotoname = "zz";

    Button btnshinglemenu1,btnshinglemenu2;
    Button btnguttermenu1;
    Button btnLayersmenu;
    Button btnLayersmenu1,btnoverhangmenu1;

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

    String myPath ;
    ClaimSqlLiteDbHelper claimDbHelper;
    SQLiteDatabase DB;

    String SELECT_SQL;
    Cursor Cur;

    int value = 0;




    Button btnroofadd,btnaddelevations,btnaddinterior,btnadddamage,btnesubcatgry,btnaddclaimname,btnmacroadd;
    LinearLayout llline;

    RelativeLayout rlmain;


    Bitmap bmoverlay;


    String matrialcostmtext = "";
    String damagecostmtext = "";
    String strname = "";

    private boolean inPreview = false;



    Button btncat;
    EditText txtname;

    TextView txtalphaname;

    Button btncamsave;

    String strcid = "";



    private void exitappalert()
    {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }


    @Override
    protected void onStart()
    {
        // TODO Auto-generated method stub
        super.onStart();
    }


    @Override
    protected void onStop()
    {
        // TODO Auto-generated method stub

//        if (camera != null)
//        {
//            camera.stopPreview();
//            camera.release();
//            camera = null;
//        }

        super.onStop();
    }



    protected void onResume()
    {
        super.onResume();

        showclaimnameoption(false);

        mSensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_NORMAL);

//        if(camera == null)
//        {
//            camera = Camera.open();
//        }

        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_HARDWARE);



    }


    protected void onPause() {


//        if (camera != null)
//        {
//            camera.stopPreview();
//            camera.release();
//            camera = null;
//        }

        mSensorManager.unregisterListener(this);

        super.onPause();


    }


    private void opendatabase()
    {
        myPath = claimDbHelper.claimdb_PATH + claimDbHelper.claimdb_NAME;
        DB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
    }


    private void closedatabase()
    {
        DB.close();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camview);



        strcid = getIntent().getStringExtra("cid");

        lastpathpf = getSharedPreferences("claimmatecam", Context.MODE_PRIVATE);
        lastimageeditor = lastpathpf.edit();


        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);


        rlmain = (RelativeLayout)findViewById(R.id.rlmain);

        if (isNetworkAvailable(CamViewActivity.this))
        {
            UpdateChecker checker = new UpdateChecker(this, this);

            //   Google Analytics
            Tracker t = ((MyApplication) getApplication()).getTracker(TrackerName.APP_TRACKER);
            t.setScreenName("Home");
            t.send(new HitBuilders.AppViewBuilder().build());


        }



        lblpitchvalue = (TextView)findViewById(R.id.lblpitchvalue);



        hasFlash = getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);


        appfoldername = lastpathpf.getString("appfoldername", "ClaimMate");

        claimDbHelper = new ClaimSqlLiteDbHelper(this);


        String iscopydp = lastpathpf.getString("iscopy", "no");

        if(iscopydp.toString().equals("no"))
        {
            try
            {

                claimDbHelper.CopyDataBaseFromAsset();
                lastimageeditor.putString("iscopy", "yes");
                lastimageeditor.commit();
                Log.e("iscopy","no");

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else
        {
            Log.e("iscopy","yes");

        }
        photoindex = lastpathpf.getInt("photoindex", 1);
        riskindex = lastpathpf.getInt("riskindex", 1);

        mydir = new File(Environment.getExternalStorageDirectory(), appfoldername);
        if(!mydir.exists())
        {
            mydir.mkdir();
        }

        rlsetting = (RelativeLayout)findViewById(R.id.rlsetting);

        llline = (LinearLayout)findViewById(R.id.llline);

        btnroofadd = (Button)findViewById(R.id.btnroofadd);
        btnaddelevations = (Button)findViewById(R.id.btnaddelevations);
        btnaddinterior = (Button)findViewById(R.id.btnaddinterior);
        btnadddamage = (Button)findViewById(R.id.btnadddamage);
        btnesubcatgry = (Button)findViewById(R.id.btnesubcatgry);
        btnaddclaimname= (Button)findViewById(R.id.btnaddclaimname);
        btnmacroadd = (Button)findViewById(R.id.btnmacroadd);

        btncamsave= (Button)findViewById(R.id.btncamsave);


        txtname = (EditText)findViewById(R.id.txtname);

        btncat = (Button)findViewById(R.id.btncat);
        btncat.setText(appfoldername);

        txtfoldername = (EditText)findViewById(R.id.txtfoldername);
        btnok = (Button)findViewById(R.id.btnok);
        btncancel = (Button)findViewById(R.id.btncancel);

        btniteriortype =(Button)findViewById(R.id.btniteriortype);

        rlcontrolview = (RelativeLayout)findViewById(R.id.rlcontrolview);
        rliteriortype = (RelativeLayout)findViewById(R.id.rliteriortype);

        rlmenuselection =(RelativeLayout)findViewById(R.id.rlmenuselection);

        rlriskphoto =(RelativeLayout)findViewById(R.id.rlriskphoto);
        rllayerphoto =(RelativeLayout)findViewById(R.id.rllayerphoto);

        rlshinglephoto = (RelativeLayout)findViewById(R.id.rlshinglephoto);

        rlgutterphoto = (RelativeLayout)findViewById(R.id.rlgutterphoto);

        rloverhangphoto = (RelativeLayout)findViewById(R.id.rloverhangphoto);
        rlpitchphoto = (RelativeLayout)findViewById(R.id.rlpitchphoto);
        rlcostm= (RelativeLayout)findViewById(R.id.rlcostm);
        rltorch = (RelativeLayout)findViewById(R.id.rltorch);

        rlBack = (RelativeLayout)findViewById(R.id.rlBack);
        rllayerBack = (RelativeLayout)findViewById(R.id.rllayerBack);

        rlshingleBack = (RelativeLayout)findViewById(R.id.rlshingleBack);
        rlgutterBack= (RelativeLayout)findViewById(R.id.rlgutterBack);
        rlOverhangBack= (RelativeLayout)findViewById(R.id.rlOverhangBack);

        rlpitchBack= (RelativeLayout)findViewById(R.id.rlpitchBack);
        rlcostmback= (RelativeLayout)findViewById(R.id.rlcostmback);

        chkdate = (CheckBox)findViewById(R.id.chkdate);

        btnrei = (Button)findViewById(R.id.btnrei);
        btndamagetype = (Button)findViewById(R.id.btndamagetype);
        btnabc = (Button)findViewById(R.id.btnabc);
        btnmaterial = (Button)findViewById(R.id.btnmaterial);
        btntype = (Button)findViewById(R.id.btntype);

        btnocb =  (Button)findViewById(R.id.btnocb);

        //btnlastphoto = (Button)findViewById(R.id.btnlastphoto);

        ibtnflash = (ImageButton)findViewById(R.id.ibtnflash);
//        ibtnflash.setTag("1");

        imgbtnsetting = (ImageButton)findViewById(R.id.imgbtnsetting);
        imgbtncam = (ImageButton)findViewById(R.id.imgbtncam);

        imgtop = (ImageView)findViewById(R.id.imgtop);
        imgleft = (ImageView)findViewById(R.id.imgleft);
        imgright = (ImageView)findViewById(R.id.imgright);
        imgbottom = (ImageView)findViewById(R.id.imgbottom);


        btnrisk = (Button )findViewById(R.id.btnrisk);
        btnskip = (Button )findViewById(R.id.btnskip);

        btnlayerskip = (Button )findViewById(R.id.btnlayerskip);
        btnpitchskip = (Button )findViewById(R.id.btnpitchskip);
        btnshingleskip = (Button )findViewById(R.id.btnshingleskip);
        btngutterskip = (Button )findViewById(R.id.btngutterskip);
        btnoverhangskip = (Button )findViewById(R.id.btnoverhangskip);
        btncostmskip = (Button )findViewById(R.id.btncostmskip);

        btnshinglemenu1 =(Button)findViewById(R.id.btnshinglemenu1);
        btnshinglemenu2 =(Button)findViewById(R.id.btnshinglemenu2);

        btnguttermenu1  =(Button)findViewById(R.id.btnguttermenu1);
        btnguttermenu1.setText("5\" Gutter");

        btnLayersmenu =(Button)findViewById(R.id.btnLayersmenu);

        btnLayersmenu1 = (Button)findViewById(R.id.btnLayersmenu1);
        btnoverhangmenu1= (Button)findViewById(R.id.btnoverhangmenu1);

        txtcenter = (TextView)findViewById(R.id.imgcenter);

        //zoomseek = (SeekBar)findViewById(R.id.//zoomseek);

        btnmatrialsubmenu = (Button)findViewById(R.id.btnmatrialsubmenu);
        btnmatrialsubmenu.setVisibility(View.GONE);


        txtalphaname  = (TextView)findViewById(R.id.txtalphaname);
//        setInteriorvalue();

        btnrei.setOnClickListener(this);
        btndamagetype.setOnClickListener(this);
        btnabc.setOnClickListener(this);
        btnmaterial.setOnClickListener(this);
        //btnlastphoto.setOnClickListener(this);
        btntype.setOnClickListener(this);
        btnocb.setOnClickListener(this);

        imgbtnsetting.setOnClickListener(this);
        imgtop.setOnClickListener(this);
        imgbottom.setOnClickListener(this);
        imgleft.setOnClickListener(this);
        imgright.setOnClickListener(this);

        ibtnflash.setOnClickListener(this);

        imgbtncam.setOnClickListener(this);

        btniteriortype.setOnClickListener(this);
        rlsetting.setOnClickListener(this);
        btnok.setOnClickListener(this);

        btnroofadd.setOnClickListener(this);
        btnaddelevations.setOnClickListener(this);
        btnaddinterior.setOnClickListener(this);
        btnadddamage.setOnClickListener(this);
        btnesubcatgry.setOnClickListener(this);
        btnaddclaimname.setOnClickListener(this);
        btnmacroadd.setOnClickListener(this);
        btncamsave.setOnClickListener(this);

        btncancel.setOnClickListener(this);

        /*
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
            });*/

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

        rlshinglephoto.setOnClickListener(this);
        rlgutterphoto.setOnClickListener(this);
        rloverhangphoto.setOnClickListener(this);
        rlpitchphoto.setOnClickListener(this);
        rlcostm.setOnClickListener(this);
        btnshinglemenu1.setOnClickListener(this);
        btnshinglemenu2.setOnClickListener(this);


        btnguttermenu1.setOnClickListener(this);

        btnLayersmenu.setOnClickListener(this);

        btnLayersmenu1.setOnClickListener(this);
        btnoverhangmenu1.setOnClickListener(this);


        btnlayerskip.setOnClickListener(this);
        btnpitchskip.setOnClickListener(this);
        btnshingleskip.setOnClickListener(this);
        btngutterskip.setOnClickListener(this);
        btnoverhangskip.setOnClickListener(this);
        btncostmskip.setOnClickListener(this);
        btnmatrialsubmenu.setOnClickListener(this);
        btncat.setOnClickListener(this);

        getinteriordbvalue();

        accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);


        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);

//        calljpg();


    }


    private String calljpgsavevalue()
    {

        if(btnrisk.getText().toString().trim().equals("Pitch"))
        {
            screenshotHideview();
            bmoverlay = loadBitmapFromView(rlmain);
            screenshotShowview();
        }

        String strisNone = "";

        if(btnabc.getText().toString().equals("None"))
        {
            strisNone="";
        }
        else
        {
            strisNone = btnabc.getText().toString();
        }

        strname = "";


            String imgname = "";



            if(btnrisk.getText().toString().trim().equals("Aditional Photo"))
            {

                strreitype = "";

                if (btnrei.getText().toString().trim().equals("I")) {
                    strreitype = itrarr.get(iteriortypeindex) + "-";
                } else if (!selectslope.equals("")) {
                    if (btnrei.getText().toString().trim().equals("R")) {
                        strreitype = selectslope + "Slope-";

                    } else if (btnrei.getText().toString().trim().equals("E")) {
                        strreitype = selectslope + "Elev-";

                    }

                } else {

                }


                String strdamagetype = "";

                if (!btndamagetype.getText().toString().trim().equals("Blank"))
                {
                    if (!btntype.getTag().equals("1"))
                    {
                        if(btndamagetype.getText().toString().trim().equals("Custom text"))
                        {
                            strdamagetype = damagecostmtext + " - ";

                        }
                        else
                        {
                            strdamagetype = btndamagetype.getText().toString().trim() + " - ";

                        }
                    }

                }

                String strmaterial = "";

                if(btnmaterial.getText().toString().trim().equals("Custom Text"))
                {
                    strmaterial = matrialcostmtext;
                }
                else
                {
                    strmaterial = btnmaterial.getText().toString().trim();
                }

                strname = stralphabetname + strboc + " " + strreitype + "" + strboctype + " " + strdamagetype + strmaterial+" ";


                if (strname.contains("Blank -")) {
                    strname = strname.replace("Blank -", "");
                }

                if (strname.contains("- Blank")) {
                    strname = strname.replace("- Blank", "");
                }

                if (strname.contains(" Blank")) {
                    strname = strname.replace(" Blank", "");
                }

                if (strname.contains("-Blank")) {
                    strname = strname.replace("-Blank", "");
                }

                if (strname.contains(" Blank ")) {
                    strname = strname.replace(" Blank ", "");
                }


                if (btnrei.getText().toString().trim().equals("R"))
                {
                    if (btnmaterial.getText().toString().equals("test sq"))
                    {

                        strname= strname+" "+btnmatrialsubmenu.getText().toString().trim()+" ";
                    }
                }


                if(btnrei.getText().toString().equals("I"))
                {

                    imgname = "k "+strname + "_"+btniteriortype.getText().toString()+" "+strisNone+"_";
                }
                else
                {
                    imgname = strname+" "+strisNone+ "_";

                }

            }
            else if (btnrisk.getText().toString().trim().equals("Risk"))
            {
                imgname = "0 "+riskindex+" Risk Photo"+" "+strisNone;//dateFormat.format(new Date());
                riskindex++;
                lastimageeditor.putInt("riskindex", riskindex);
                lastimageeditor.commit();

            }
            else if (btnrisk.getText().toString().trim().equals("Layers"))
            {
                imgname = btnLayersmenu1.getText().toString()+" Layers of Roofing with " + btnLayersmenu.getText().toString()+" "+strisNone;//dateFormat.format(new Date());

            }
            else if (btnrisk.getText().toString().trim().equals("Pitch"))
            {
                imgname =lblpitchvalue.getText().toString().trim()+" Roof Pitch"+" "+strisNone;//dateFormat.format(new Date());

            }
            else if (btnrisk.getText().toString().trim().equals("Shingle"))
            {
                imgname = btnshinglemenu1.getText().toString()+" "+btnshinglemenu2.getText().toString()+" Shingle"+" "+strisNone;//dateFormat.format(new Date());

            }
            else if (btnrisk.getText().toString().trim().equals("Gutter"))
            {

                imgname = "Standard " + btnguttermenu1.getText().toString()+" on this dwelling"+" "+strisNone;//dateFormat.format(new Date());

            }
            else if (btnrisk.getText().toString().trim().equals("Overhang"))
            {
                imgname = btnoverhangmenu1.getText().toString()+" \" overhang"+" "+strisNone;//dateFormat.format(new Date());

            }
            else
            {
                imgname = txtname.getText().toString().trim()+" "+btnrisk.getText().toString()+" "+strisNone;//dateFormat.format(new Date());

            }
        return imgname;
    }


    private String getaplphaname() {

        String strisNone = "";

        if (btnabc.getText().toString().equals("None")) {
            strisNone = "";
        } else {
            strisNone = btnabc.getText().toString();
        }

        strname = "";

        String imgname = "";


        if (btnrisk.getText().toString().trim().equals("Aditional Photo")) {

            strreitype = "";

            if (btnrei.getText().toString().trim().equals("I")) {
                strreitype = itrarr.get(iteriortypeindex) + "-";
            } else if (!selectslope.equals("")) {
                if (btnrei.getText().toString().trim().equals("R")) {
                    strreitype = selectslope + "Slope-";

                } else if (btnrei.getText().toString().trim().equals("E")) {
                    strreitype = selectslope + "Elev-";

                }
                        /*
                        else if(btnrei.getText().toString().trim().equals("I"))
                        {
                            strreitype = selectslope+itrarr[index]+":";

                        }*/
            } else {

            }


            String strdamagetype = "";

            if (!btndamagetype.getText().toString().trim().equals("Blank")) {
                if (!btntype.getTag().equals("1")) {
                    if (btndamagetype.getText().toString().trim().equals("Custom text")) {
                        strdamagetype = damagecostmtext + " - ";

                    } else {
                        strdamagetype = btndamagetype.getText().toString().trim() + " - ";

                    }
                }

            }

            String strmaterial = "";

            if (btnmaterial.getText().toString().trim().equals("Custom Text")) {
                strmaterial = matrialcostmtext;
            } else {
                strmaterial = btnmaterial.getText().toString().trim();
            }

            strname =  strreitype + "" + strboctype + " " + strdamagetype + strmaterial + " ";


            if (strname.contains("Blank -")) {
                strname = strname.replace("Blank -", "");
            }

            if (strname.contains("- Blank")) {
                strname = strname.replace("- Blank", "");
            }

            if (strname.contains(" Blank")) {
                strname = strname.replace(" Blank", "");
            }

            if (strname.contains("-Blank")) {
                strname = strname.replace("-Blank", "");
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

                imgname = strname + "_" + btniteriortype.getText().toString() + " " + strisNone ;
            } else {
                imgname = strname + " " + strisNone;

            }

//                        imgname = strname + "_" + currentTimeStamp + ".jpg";


        } else if (btnrisk.getText().toString().trim().equals("Risk")) {
            imgname = "0 " + riskindex + " Risk Photo" + " " + strisNone ;//dateFormat.format(new Date());
            riskindex++;
            lastimageeditor.putInt("riskindex", riskindex);
            lastimageeditor.commit();

        } else if (btnrisk.getText().toString().trim().equals("Layers")) {
            imgname = btnLayersmenu1.getText().toString() + " Layers of Roofing with " + btnLayersmenu.getText().toString() + " " + strisNone ;//dateFormat.format(new Date());

        } else if (btnrisk.getText().toString().trim().equals("Pitch")) {
            imgname = lblpitchvalue.getText().toString().trim() + " Roof Pitch" + " " + strisNone ;//dateFormat.format(new Date());

        } else if (btnrisk.getText().toString().trim().equals("Shingle")) {
            imgname = btnshinglemenu1.getText().toString() + " " + btnshinglemenu2.getText().toString() + " Shingle" + " " + strisNone ;//dateFormat.format(new Date());

        } else if (btnrisk.getText().toString().trim().equals("Gutter")) {

            imgname = "Standard " + btnguttermenu1.getText().toString() + " on this dwelling" + " " + strisNone ;//dateFormat.format(new Date());

        } else if (btnrisk.getText().toString().trim().equals("Overhang")) {
            imgname = btnoverhangmenu1.getText().toString() + " \" overhang" + " " + strisNone ;//dateFormat.format(new Date());

        }
        else
        {
            imgname = txtname.getText().toString().trim() + " " + btnrisk.getText().toString() + " " + strisNone;//dateFormat.format(new Date());

        }

        return imgname.toString().trim();

    }

    private String getaplphaname(String TimeStamp , String currentTimeStamp) {

        String strisNone = "";

        if (btnabc.getText().toString().equals("None")) {
            strisNone = "";
        } else {
            strisNone = btnabc.getText().toString();
        }

        strname = "";

        String imgname = "";


        if (btnrisk.getText().toString().trim().equals("Aditional Photo")) {

            strreitype = "";

            if (btnrei.getText().toString().trim().equals("I")) {
                strreitype = itrarr.get(iteriortypeindex) + "-";
            } else if (!selectslope.equals("")) {
                if (btnrei.getText().toString().trim().equals("R")) {
                    strreitype = selectslope + "Slope-";

                } else if (btnrei.getText().toString().trim().equals("E")) {
                    strreitype = selectslope + "Elev-";

                }
                        /*
                        else if(btnrei.getText().toString().trim().equals("I"))
                        {
                            strreitype = selectslope+itrarr[index]+":";

                        }*/
            } else {

            }


            String strdamagetype = "";

            if (!btndamagetype.getText().toString().trim().equals("Blank")) {
                if (!btntype.getTag().equals("1")) {
                    if (btndamagetype.getText().toString().trim().equals("Custom text")) {
                        strdamagetype = damagecostmtext + " - ";

                    } else {
                        strdamagetype = btndamagetype.getText().toString().trim() + " - ";

                    }
                }

            }

            String strmaterial = "";

            if (btnmaterial.getText().toString().trim().equals("Custom Text")) {
                strmaterial = matrialcostmtext;
            } else {
                strmaterial = btnmaterial.getText().toString().trim();
            }

            strname = stralphabetname + strboc + " " + strreitype + "" + strboctype + " " + strdamagetype + strmaterial + " ";


            if (strname.contains("Blank -")) {
                strname = strname.replace("Blank -", "");
            }

            if (strname.contains("- Blank")) {
                strname = strname.replace("- Blank", "");
            }

            if (strname.contains(" Blank")) {
                strname = strname.replace(" Blank", "");
            }

            if (strname.contains("-Blank")) {
                strname = strname.replace("-Blank", "");
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

                imgname = strname + "_" + btniteriortype.getText().toString() + " " + strisNone ;
            } else {
                imgname = strname + " " + strisNone;

            }

//                        imgname = strname + "_" + currentTimeStamp + ".jpg";


        } else if (btnrisk.getText().toString().trim().equals("Risk")) {
            imgname = "0 " + riskindex + " Risk Photo" + " " + strisNone ;//dateFormat.format(new Date());
            riskindex++;
            lastimageeditor.putInt("riskindex", riskindex);
            lastimageeditor.commit();

        } else if (btnrisk.getText().toString().trim().equals("Layers")) {
            imgname = btnLayersmenu1.getText().toString() + " Layers of Roofing with " + btnLayersmenu.getText().toString() + " " + strisNone ;//dateFormat.format(new Date());

        } else if (btnrisk.getText().toString().trim().equals("Pitch")) {
            imgname = lblpitchvalue.getText().toString().trim() + " Roof Pitch" + " " + strisNone ;//dateFormat.format(new Date());

        } else if (btnrisk.getText().toString().trim().equals("Shingle")) {
            imgname = btnshinglemenu1.getText().toString() + " " + btnshinglemenu2.getText().toString() + " Shingle" + " " + strisNone ;//dateFormat.format(new Date());

        } else if (btnrisk.getText().toString().trim().equals("Gutter")) {

            imgname = "Standard " + btnguttermenu1.getText().toString() + " on this dwelling" + " " + strisNone ;//dateFormat.format(new Date());

        } else if (btnrisk.getText().toString().trim().equals("Overhang")) {
            imgname = btnoverhangmenu1.getText().toString() + " \" overhang" + " " + strisNone ;//dateFormat.format(new Date());

        }
        else
        {
            imgname = txtname.getText().toString().trim() + " " + btnrisk.getText().toString() + " " + strisNone;//dateFormat.format(new Date());

        }



        return imgname.toString().trim();


    }

    private void screenshotHideview() {

        rlpitchBack.setVisibility(View.INVISIBLE);
        ibtnflash.setVisibility(View.INVISIBLE);
        rltorch.setVisibility(View.INVISIBLE);
        imgbtncam.setVisibility(View.INVISIBLE);
        //zoomseek.setVisibility(View.INVISIBLE);
        btntype.setVisibility(View.INVISIBLE);
        btnrisk.setVisibility(View.INVISIBLE);
        btnpitchskip.setVisibility(View.INVISIBLE);
    }

    private void screenshotShowview() {

        rlpitchBack.setVisibility(View.VISIBLE);

        ibtnflash.setVisibility(View.VISIBLE);
        rltorch.setVisibility(View.VISIBLE);
        imgbtncam.setVisibility(View.VISIBLE);
        //zoomseek.setVisibility(View.VISIBLE);

        if (btndamagetype.getText().toString().equals("Blank")) {
            btntype.setVisibility(View.INVISIBLE);

        } else {
            btntype.setVisibility(View.VISIBLE);
        }

        btnrisk.setVisibility(View.VISIBLE);
        btnpitchskip.setVisibility(View.VISIBLE);
    }


    private void nextPhoto(String strphoto)
    {


        btnrisk.setText(strphoto);


        hideallview();
        if(strphoto.equals("Aditional Photo"))
        {
            btnrisk.setTag("1");
            showoption();
        }
        else if(strphoto.equals("Risk"))
        {
            btnrisk.setTag("2");
            btnskip.setVisibility(View.INVISIBLE);
            hideoption();
        }
        else if(strphoto.equals("Layers"))
        {
            btnrisk.setTag("1");
            btnLayersmenu.setText("Drip Edge");
            btnLayersmenu1.setText("1");
            hidelayeroption();
        }
        else if(strphoto.equals("Pitch"))
        {
            btnrisk.setText("Pitch");
            hidepitchoption();

        }
        else if(strphoto.equals("Shingle"))
        {
            btnshinglemenu1.setText("25 years");
            btnshinglemenu2.setText("3 Tab");

            hideShingleoption();
        }
        else if(strphoto.equals("Gutter"))
        {

            btnguttermenu1.setText("5\" Gutter");
            hideGutteroption();

        }
        else if(strphoto.equals("Overhang"))
        {
            btnoverhangmenu1.setText("1");
            hideoverhangoption();
        }
        else
        {
            hidecostmoption();
        }


    }

    private void getinteriordbvalue()
    {

        value = 0;
        itrarr = new ArrayList<>();

        opendatabase();

        SELECT_SQL = "SELECT * FROM tbl_interior";
        Cur = DB.rawQuery(SELECT_SQL, null);
        if(Cur != null && Cur.getCount() > 0)
        {
            Cur.moveToFirst();
            do
            {

                String strvalue = Cur.getString(Cur.getColumnIndex("value"));
                Log.e("Value==>", "" + strvalue);
                String strshortname = Cur.getString(Cur.getColumnIndex("shortname"));
                itrarr.add(strshortname);


                value++;
            }
            while(Cur.moveToNext());
        }
        Cur.close();
        DB.close();
    }


    private void setInteriorvalue()
    {
        value = 0;
        itrarr = new ArrayList<>();

        PopupMenu popupMenu2 = new PopupMenu(this,findViewById(R.id.btniteriortype));
     /*   popupMenu2.getMenu().add(Menu.NONE, 0, Menu.NONE, "Blank");
        popupMenu2.getMenu().add(Menu.NONE, 1, Menu.NONE, "Bedroom");
        popupMenu2.getMenu().add(Menu.NONE, 2, Menu.NONE, "Bedroom 1");
        popupMenu2.getMenu().add(Menu.NONE, 3, Menu.NONE, "Bedroom 2");
        popupMenu2.getMenu().add(Menu.NONE, 4, Menu.NONE, "Bedroom 3");
        popupMenu2.getMenu().add(Menu.NONE, 5, Menu.NONE, "Master Bedroom");
        popupMenu2.getMenu().add(Menu.NONE, 6, Menu.NONE, "Master Bath");
        popupMenu2.getMenu().add(Menu.NONE, 7, Menu.NONE, "Hallway");
        popupMenu2.getMenu().add(Menu.NONE, 8, Menu.NONE, "Foyer");
        popupMenu2.getMenu().add(Menu.NONE, 9, Menu.NONE, "Living room");
        popupMenu2.getMenu().add(Menu.NONE, 10, Menu.NONE, "Dining room");
        popupMenu2.getMenu().add(Menu.NONE, 11, Menu.NONE, "Basement");
        popupMenu2.getMenu().add(Menu.NONE, 12, Menu.NONE, "Bathroom");
        popupMenu2.getMenu().add(Menu.NONE, 13, Menu.NONE, "Family room");
        popupMenu2.getMenu().add(Menu.NONE, 14, Menu.NONE, "Office");
        popupMenu2.getMenu().add(Menu.NONE, 15, Menu.NONE, "TV Room");
        popupMenu2.getMenu().add(Menu.NONE, 16, Menu.NONE, "Storage");
        popupMenu2.getMenu().add(Menu.NONE, 17, Menu.NONE, "Custom Text");*/


        opendatabase();

        SELECT_SQL = "SELECT * FROM tbl_interior";
        Cur = DB.rawQuery(SELECT_SQL, null);
        if(Cur != null && Cur.getCount() > 0)
        {
            Cur.moveToFirst();
            do
            {

                String strvalue = Cur.getString(Cur.getColumnIndex("value"));
                Log.e("Value==>",""+strvalue);
                String strshortname = Cur.getString(Cur.getColumnIndex("shortname"));
                itrarr.add(strshortname);

                popupMenu2.getMenu().add(Menu.NONE, value, Menu.NONE, strvalue);

                value++;
            }
            while(Cur.moveToNext());
        }
        Cur.close();
        DB.close();


        popupMenu2.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem arg0) {

                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) arg0.getMenuInfo();
                iteriortypeindex = arg0.getItemId();


                btniteriortype.setText(arg0.getTitle());

//                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) arg0.getMenuInfo();
//
//                 index = info.position;


                btnocb.setTag("1");
                btnocb.setText("O");
                strboc = "1";
                strboctype = "Overview";

                return false;
            }
        });
        popupMenu2.show();

    }

    private void refereshgallery() {

        allFiles = savefile.listFiles();

        for (int i=0;i<allFiles.length;i++)
        {
            new SingleMediaScanner(CamViewActivity.this, allFiles[i]);

        }
    }


    public void captureImage() throws IOException
    {
        //take the picture
        camera.takePicture(null, null, jpegCallback);
    }

    public void refreshCamera() {
        if (surfaceHolder.getSurface() == null) {
            // preview surface does not exist
            return;
        }

        // stop preview before making changes
        try {
            camera.stopPreview();
        } catch (Exception e) {
            // ignore: tried to stop a non-existent preview
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here
        // start preview with new settings
        try {
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
            inPreview = true;
        } catch (Exception e) {

        }
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // Now that the size is known, set up the camera parameters and begin
        // the preview.
        refreshCamera();
    }

    public void surfaceCreated(SurfaceHolder holder)
    {
        try {
            // open the camera
            camera = Camera.open();
        } catch (RuntimeException e) {
            // check for exceptions
            System.err.println(e);
            return;
        }
        Camera.Parameters param;
        param = camera.getParameters();


        if(hasFlash)
        {
            param.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        }



        /*
        if(param.isZoomSupported())
        {
            //zoomseek.setMax(param.getMaxZoom());
        }
        else
        {
            //zoomseek.setVisibility(View.GONE);
        }*/
        // modify parameter
//        param.setPreviewSize(352, 288);
        camera.setParameters(param);
        try {
            // The Surface has been created, now tell the camera where to draw
            // the preview.
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
            inPreview = true;
        } catch (Exception e) {
            // check for exceptions
            System.err.println(e);
            return;
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder)
    {
        // stop preview and release camera

        if (camera != null)
        {
            camera.stopPreview();
            camera.release();
            camera = null;
        }

    }


    private void showcostmnamealert() {
        addcostmphotoname = "zz";
        		/* Alert Dialog Code Start*/
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setCancelable(false);
        alert.setTitle("Add custom photo name"); //Set Alert dialog title here

        // Set an EditText view to get user input
        final EditText input = new EditText(this);
        input.setText("zz");
        input.setSelection(input.getText().toString().length());
        alert.setView(input);

        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //You will get as string input data in this variable.
                // here we convert the input to a string and show in a toast.
//						String srt = input.getEditableText().toString();

                if (input.getText().toString().trim().equals("")) {
                    Toast.makeText(getApplicationContext(), "Enter photo name", Toast.LENGTH_LONG).show();

                } else {
                    addcostmphotoname = input.getText().toString().trim();
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
        Intent addnewvalue = new Intent(getApplicationContext(),AddValueActivity.class);
        addnewvalue.putExtra("tablename", tname);
        addnewvalue.putExtra("title", strtitle);
        startActivity(addnewvalue);
    }

    private void hideallview()
    {

        rlriskphoto.setVisibility(View.GONE);
        rllayerphoto.setVisibility(View.GONE);
        rlshinglephoto.setVisibility(View.GONE);
        rlgutterphoto.setVisibility(View.GONE);
        rloverhangphoto.setVisibility(View.GONE);
        rlpitchphoto.setVisibility(View.GONE);
        llline.setVisibility(View.GONE);
        rlcostm.setVisibility(View.GONE);

        rlmenuselection.setVisibility(View.VISIBLE);
//        rltorch.setVisibility(View.VISIBLE);
//        ibtnflash.setVisibility(View.VISIBLE);
        imgbtnsetting.setVisibility(View.VISIBLE);
//        zoomseek.setVisibility(View.VISIBLE);
        rlcontrolview.setVisibility(View.VISIBLE);
        btnocb.setVisibility(View.VISIBLE);
        //btnlastphoto.setVisibility(View.VISIBLE);


        if(btndamagetype.getText().toString().equals("Blank"))
        {
            btntype.setVisibility(View.INVISIBLE);

        }
        else
        {
            btntype.setVisibility(View.VISIBLE);
        }

        if(btnrei.getText().toString().trim().equals("I"))
        {
            rlcontrolview.setVisibility(View.GONE);
            rliteriortype.setVisibility(View.VISIBLE);
        }
        else
        {
            rlcontrolview.setVisibility(View.VISIBLE);
            rliteriortype.setVisibility(View.GONE);
        }

    }

    private void showoption()
    {
        rlriskphoto.setVisibility(View.GONE);
        rlmenuselection.setVisibility(View.VISIBLE);
//        rltorch.setVisibility(View.VISIBLE);
//        ibtnflash.setVisibility(View.VISIBLE);
        imgbtnsetting.setVisibility(View.VISIBLE);
//        zoomseek.setVisibility(View.VISIBLE);
        rlcontrolview.setVisibility(View.VISIBLE);
        btnocb.setVisibility(View.VISIBLE);
        //btnlastphoto.setVisibility(View.VISIBLE);

        if (btndamagetype.getText().toString().equals("Blank")) {
            btntype.setVisibility(View.INVISIBLE);

        } else {
            btntype.setVisibility(View.VISIBLE);
        }



    }

    private void hidepitchoption()
    {

        rlpitchphoto.setVisibility(View.VISIBLE);
        llline.setVisibility(View.VISIBLE);

        rlmenuselection.setVisibility(View.INVISIBLE);
//        rltorch.setVisibility(View.INVISIBLE);
//        ibtnflash.setVisibility(View.INVISIBLE);
        imgbtnsetting.setVisibility(View.INVISIBLE);
//        zoomseek.setVisibility(View.INVISIBLE);
        rlcontrolview.setVisibility(View.INVISIBLE);
        btnocb.setVisibility(View.INVISIBLE);
        btntype.setVisibility(View.INVISIBLE);
        //btnlastphoto.setVisibility(View.INVISIBLE);


    }

    private void hidecostmoption()
    {

        rlcostm.setVisibility(View.VISIBLE);

        rlmenuselection.setVisibility(View.INVISIBLE);
//        rltorch.setVisibility(View.INVISIBLE);
//        ibtnflash.setVisibility(View.INVISIBLE);
        imgbtnsetting.setVisibility(View.INVISIBLE);
//        zoomseek.setVisibility(View.INVISIBLE);
        rlcontrolview.setVisibility(View.INVISIBLE);
        btnocb.setVisibility(View.INVISIBLE);
        btntype.setVisibility(View.INVISIBLE);
        //btnlastphoto.setVisibility(View.INVISIBLE);


    }



    private void hideoption()
    {

        rlriskphoto.setVisibility(View.VISIBLE);
        rlmenuselection.setVisibility(View.INVISIBLE);
//        rltorch.setVisibility(View.INVISIBLE);
//        ibtnflash.setVisibility(View.INVISIBLE);
        imgbtnsetting.setVisibility(View.INVISIBLE);
//        zoomseek.setVisibility(View.INVISIBLE);
        rlcontrolview.setVisibility(View.INVISIBLE);
        btnocb.setVisibility(View.INVISIBLE);
        btntype.setVisibility(View.INVISIBLE);
        //btnlastphoto.setVisibility(View.INVISIBLE);

    }


    private void showlayreoption()
    {
        rlriskphoto.setVisibility(View.GONE);
        rllayerphoto.setVisibility(View.GONE);


        rlmenuselection.setVisibility(View.VISIBLE);
//        rltorch.setVisibility(View.VISIBLE);
//        ibtnflash.setVisibility(View.VISIBLE);
        imgbtnsetting.setVisibility(View.VISIBLE);
//        zoomseek.setVisibility(View.VISIBLE);
        rlcontrolview.setVisibility(View.VISIBLE);
        btnocb.setVisibility(View.VISIBLE);
        //btnlastphoto.setVisibility(View.VISIBLE);
        btntype.setVisibility(View.VISIBLE);


    }

    private void hidelayeroption()
    {

        rllayerphoto.setVisibility(View.VISIBLE);
        rlmenuselection.setVisibility(View.INVISIBLE);
//        rltorch.setVisibility(View.INVISIBLE);
//        ibtnflash.setVisibility(View.INVISIBLE);
        imgbtnsetting.setVisibility(View.INVISIBLE);
//        zoomseek.setVisibility(View.INVISIBLE);
        rlcontrolview.setVisibility(View.INVISIBLE);
        btnocb.setVisibility(View.INVISIBLE);
        btntype.setVisibility(View.INVISIBLE);
        //btnlastphoto.setVisibility(View.INVISIBLE);

    }

    private void showShingleoption()
    {
        rlshinglephoto.setVisibility(View.GONE);
        rlmenuselection.setVisibility(View.VISIBLE);
//        rltorch.setVisibility(View.VISIBLE);
//        ibtnflash.setVisibility(View.VISIBLE);
        imgbtnsetting.setVisibility(View.VISIBLE);
//        zoomseek.setVisibility(View.VISIBLE);
        rlcontrolview.setVisibility(View.VISIBLE);
        btnocb.setVisibility(View.VISIBLE);
        //btnlastphoto.setVisibility(View.VISIBLE);
        btntype.setVisibility(View.VISIBLE);


    }

    private void hideShingleoption()
    {

        rlshinglephoto.setVisibility(View.VISIBLE);
        rlmenuselection.setVisibility(View.INVISIBLE);
//        rltorch.setVisibility(View.INVISIBLE);
//        ibtnflash.setVisibility(View.INVISIBLE);
        imgbtnsetting.setVisibility(View.INVISIBLE);
//        zoomseek.setVisibility(View.INVISIBLE);
        rlcontrolview.setVisibility(View.INVISIBLE);
        btnocb.setVisibility(View.INVISIBLE);
        btntype.setVisibility(View.INVISIBLE);
        //btnlastphoto.setVisibility(View.INVISIBLE);

    }

    private void hideGutteroption()
    {

        rlgutterphoto.setVisibility(View.VISIBLE);
        rlmenuselection.setVisibility(View.INVISIBLE);
//        rltorch.setVisibility(View.INVISIBLE);
//        ibtnflash.setVisibility(View.INVISIBLE);
        imgbtnsetting.setVisibility(View.INVISIBLE);
//        zoomseek.setVisibility(View.INVISIBLE);
        rlcontrolview.setVisibility(View.INVISIBLE);
        btnocb.setVisibility(View.INVISIBLE);
        btntype.setVisibility(View.INVISIBLE);
        //btnlastphoto.setVisibility(View.INVISIBLE);

    }

    private void hideoverhangoption()
    {

        rloverhangphoto.setVisibility(View.VISIBLE);
        rlmenuselection.setVisibility(View.INVISIBLE);
//        rltorch.setVisibility(View.INVISIBLE);
//        ibtnflash.setVisibility(View.INVISIBLE);
        imgbtnsetting.setVisibility(View.INVISIBLE);
//        zoomseek.setVisibility(View.INVISIBLE);
        rlcontrolview.setVisibility(View.INVISIBLE);
        btnocb.setVisibility(View.INVISIBLE);
        btntype.setVisibility(View.INVISIBLE);
        //btnlastphoto.setVisibility(View.INVISIBLE);

    }

    private void showflashalert() {

        flashalert = new AlertDialog.Builder(CamViewActivity.this)
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

    private void showupdatealert()
    {

        updateappalert = new AlertDialog.Builder(CamViewActivity.this)
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



    private void showphotonamealert(String strname)
    {

        new AlertDialog.Builder(CamViewActivity.this)
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

    private void setarrowicon(ImageView imgarrow, int iconid)
    {
        imgtop.setBackgroundResource(R.drawable.wticon);
        imgbottom.setBackgroundResource(R.drawable.wbicon);
        imgleft.setBackgroundResource(R.drawable.wlicon);
        imgright.setBackgroundResource(R.drawable.wricon);

        imgarrow.setBackgroundResource(iconid);
    }

    private void showmaterialoption()
    {
        value = 0;

        PopupMenu popupMenu2 = new PopupMenu(this, findViewById(R.id.btnmaterial));


        if(btnrei.getText().toString().trim().equals("R"))
                {
                    opendatabase();

                    SELECT_SQL = "SELECT * FROM tbl_r";
                    Cur = DB.rawQuery(SELECT_SQL, null);
                    if(Cur != null && Cur.getCount() > 0)
                    {
                        Cur.moveToFirst();
                        do
                        {

                            String strvalue = Cur.getString(Cur.getColumnIndex("value"));
                            Log.e("Value==>",""+strvalue);
                            popupMenu2.getMenu().add(Menu.NONE, value, Menu.NONE, strvalue);

                            value++;
                        }
                        while(Cur.moveToNext());
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
                else if(btnrei.getText().toString().trim().equals("E"))
                {

                    opendatabase();

                    SELECT_SQL = "SELECT * FROM tbl_e";
                    Cur = DB.rawQuery(SELECT_SQL, null);
                    if(Cur != null && Cur.getCount() > 0)
                    {
                        Cur.moveToFirst();
                        do
                        {

                            String strvalue = Cur.getString(Cur.getColumnIndex("value"));
                            Log.e("Value==>",""+strvalue);
                            popupMenu2.getMenu().add(Menu.NONE, value, Menu.NONE, strvalue);

                            value++;
                        }
                        while(Cur.moveToNext());
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


                }
                else if(btnrei.getText().toString().trim().equals("I"))
                {

                    opendatabase();

                    SELECT_SQL = "SELECT * FROM tbl_i";
                    Cur = DB.rawQuery(SELECT_SQL, null);
                    if(Cur != null && Cur.getCount() > 0)
                    {
                        Cur.moveToFirst();
                        do
                        {

                            String strvalue = Cur.getString(Cur.getColumnIndex("value"));
                            Log.e("Value==>",""+strvalue);
                            popupMenu2.getMenu().add(Menu.NONE, value, Menu.NONE, strvalue);

                            value++;
                        }
                        while(Cur.moveToNext());
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
                btnmaterial.setText(arg0.getTitle());


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
                        if (btnmaterial.getText().toString().trim().equals("test sq")) {
                            btntype.setVisibility(View.INVISIBLE);
                            btnmatrialsubmenu.setVisibility(View.VISIBLE);
                        } else {

                            btnmatrialsubmenu.setVisibility(View.GONE);

                            if (btndamagetype.getText().toString().equals("Blank")) {
                                btntype.setVisibility(View.INVISIBLE);

                            } else {
                                btntype.setVisibility(View.VISIBLE);
                            }
                        }

                    }
                }


                return false;
            }
        });
        popupMenu2.show();

    }


    private void showcostmtextalert(final String strtext)
    {
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
                //You will get as string input data in this variable.
                // here we convert the input to a string and show in a toast.
//						String srt = input.getEditableText().toString();

                if (input.getText().toString().trim().equals("")) {
                    Toast.makeText(getApplicationContext(), "Enter custom text", Toast.LENGTH_LONG).show();

                } else {
                    if (strtext.equals("1")) {
                        damagecostmtext = input.getText().toString().trim();

                    } else if (strtext.equals("2")) {
                        matrialcostmtext = input.getText().toString().trim();

                    }

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

        PopupMenu popupMenu2 = new PopupMenu(this,
                findViewById(R.id.btnabc));
        popupMenu2.getMenu().add(Menu.NONE, 0, Menu.NONE, "None");
        popupMenu2.getMenu().add(Menu.NONE, 1, Menu.NONE, "Garage");
        popupMenu2.getMenu().add(Menu.NONE, 2, Menu.NONE, "Shed");
        popupMenu2.getMenu().add(Menu.NONE, 2, Menu.NONE, "Fence");
        popupMenu2.getMenu().add(Menu.NONE, 2, Menu.NONE, "Custom");

        popupMenu2.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem arg0) {

                btnabc.setText(arg0.getTitle());


                return false;
            }
        });
        popupMenu2.show();

    }

    private void showclaimnameoption(Boolean isshowalert)
    {

        value = 0;

        PopupMenu popupMenu2 = new PopupMenu(this,findViewById(R.id.btncat));

        opendatabase();

        SELECT_SQL = "SELECT * FROM tbl_claimname";
        Cur = DB.rawQuery(SELECT_SQL, null);
        if(Cur != null && Cur.getCount() > 0)
        {
            Cur.moveToFirst();
            do
            {
                String strvalue = Cur.getString(Cur.getColumnIndex("name"));
                popupMenu2.getMenu().add(Menu.NONE, value, Menu.NONE, strvalue);

                value++;
            }
            while(Cur.moveToNext());
        }
        else
        {
            btncat.setText("ClaimMate");
            lastimageeditor.putString("appfoldername", "ClaimMate");
            lastimageeditor.commit();
            appfoldername = "ClaimMate";
            mydir = new File(Environment.getExternalStorageDirectory(), appfoldername);
            btnabc.setText("None");
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
                mydir = new File(Environment.getExternalStorageDirectory(), appfoldername);
                btnabc.setText("None");

                return false;
            }
        });

        if(isshowalert)
        {
            popupMenu2.show();
        }
    }



    private void showdamagetypeoption()
    {


        value = 0;

        PopupMenu popupMenu2 = new PopupMenu(this,findViewById(R.id.btndamagetype));

        opendatabase();

        SELECT_SQL = "SELECT * FROM tbldamagetype";
        Cur = DB.rawQuery(SELECT_SQL, null);
        if(Cur != null && Cur.getCount() > 0)
        {
            Cur.moveToFirst();
            do
            {

                String strvalue = Cur.getString(Cur.getColumnIndex("value"));
                Log.e("Value==>",""+strvalue);
                popupMenu2.getMenu().add(Menu.NONE, value, Menu.NONE, strvalue);

                value++;
            }
            while(Cur.moveToNext());
        }
        Cur.close();
        DB.close();


        /*
        popupMenu2.getMenu().add(Menu.NONE, 0, Menu.NONE, "Blank");
        popupMenu2.getMenu().add(Menu.NONE, 1, Menu.NONE, "No Damage");
        popupMenu2.getMenu().add(Menu.NONE, 3, Menu.NONE, "Damage");
        popupMenu2.getMenu().add(Menu.NONE, 4, Menu.NONE, "Hail Damage");
        popupMenu2.getMenu().add(Menu.NONE, 5, Menu.NONE, "Wind Damage");
        popupMenu2.getMenu().add(Menu.NONE, 6, Menu.NONE, "Water Damage");
        popupMenu2.getMenu().add(Menu.NONE, 7, Menu.NONE, "Tree Damage");
        popupMenu2.getMenu().add(Menu.NONE, 8, Menu.NONE, "Fire Damage");
        popupMenu2.getMenu().add(Menu.NONE, 9, Menu.NONE, "Smoke Damage");
        popupMenu2.getMenu().add(Menu.NONE, 10, Menu.NONE, "Custom text");*/


        popupMenu2.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener()
        {

            @Override
            public boolean onMenuItemClick(MenuItem arg0)
            {

                btndamagetype.setText(arg0.getTitle());
                btntype.setText(arg0.getTitle());

                if(arg0.getTitle().equals("Blank"))
                {
                    btntype.setVisibility(View.INVISIBLE);
                }
                else
                {
                    btntype.setVisibility(View.VISIBLE);
                }

                btntype.setTag("2");
                btntype.setBackgroundColor(Color.RED);

                if(arg0.getTitle().equals("Custom text"))
                {
                    showcostmtextalert("1");
                }
                return false;
            }
        });
        popupMenu2.show();
    }


    private void showreioption()
    {

        PopupMenu popupMenu2 = new PopupMenu(this,
                findViewById(R.id.btnrei));
        popupMenu2.getMenu().add(Menu.NONE, 0, Menu.NONE, "Roof");
        popupMenu2.getMenu().add(Menu.NONE, 1, Menu.NONE, "Elevations");
        popupMenu2.getMenu().add(Menu.NONE, 2, Menu.NONE, "Interior");


        popupMenu2.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem arg0) {

                btnrei.setText(arg0.getTitle());

                if(arg0.getTitle().equals("Roof"))
                {
                    txtcenter.setText("R");
                }
                else if (arg0.getTitle().equals("Elevations"))
                {
                    txtcenter.setText("E");

                }
                else if (arg0.getTitle().equals("Interior"))
                {
                    txtcenter.setText("I");
                }

                return false;
            }
        });
        popupMenu2.show();
    }

    @Override
    public void foundUpdateAndShowIt(String versionDonwloadable)
    {
        showupdatealert();
    }

    @Override
    public void foundUpdateAndDontShowIt(String versionDonwloadable)
    {

    }

    @Override
    public void returnUpToDate(String versionDonwloadable)
    {

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
        public void onSensorChanged(final SensorEvent event)
    {
        if(btnrisk.getText().toString().trim().equals("Pitch"))
        {

            /*
            if (event.sensor.getType() == 1)
            {


                f7466z = (event.values[0] * f7440a) + (f7466z * (1.0f - f7440a));

                f7442A = (event.values[1] * f7440a) + (f7442A * (1.0f - f7440a));
                f7464x = (float) Math.atan2((double) (-f7442A), (double) (-f7466z));

                float tan = (float) (Math.tan((double) m10986a(f7464x)) * 12.0d);

                if (2!= f7461u) {
                    tan = (float) (((double) tan) - 0.6d);
                }



                if (Math.abs(tan) <= 40.0f) {
                    String format = String.format("%.1f", new Object[]{Float.valueOf((float) Math.toDegrees((double) ((float) Math.atan((double) (tan / 12.0f)))))});
                    Log.e("Pitchvalue",""+String.valueOf(format.replace(",", ".")));
                }

            }*/

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


//                Double pitch = Math.toDegrees(mOrientation[1]);

                Double angle = Math.toDegrees(mOrientation[1]);

//            double rad = 38 * Math.PI / 180;
//            double y = Math.tan(rad);
//            y*12


                double rad = angle * Math.PI / 180;
                double y = Math.tan(rad);
                double pitch = y * 12;


                lblpitchvalue.setText("" + decimalFormat.format(Math.abs(Math.round(pitch))));

//                Log.e("OrientationTestActivity", String.format("Orientation: %f", pitch));
            }
        }

//        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
//        {
//            if(btnrisk.getText().toString().trim().equals("Pitch"))
//             {
//
//
//
////                 int sensor = Sensor.TYPE_ACCELEROMETER;
//                 float[] values = event.values;
//                 int i;
//                 StringBuffer str=new StringBuffer();
//                 // do something with the sensor data
//                 float[] R = new float[9]; // rotation matrix
//                 float[] magnetic = new float[3];
//                 float[] orientation = new float[3];
//
//                 magnetic[0]=0;
//                 magnetic[1]=1;
//                 magnetic[2]=0;
//
//                 str.append("From Sensor :\n");
//                 for(i=0;i<values.length; i++) {
//                     str.append(values[i]);
//                     str.append(", ");
//                 }
//
//                 SensorManager.getRotationMatrix(R, null, values, magnetic);
//                 SensorManager.getOrientation(R, orientation);
//
//
//                 /*
//                 str.append("\n\nGives :\n");
//                 for(i=0;i<orientation.length; i++) {
//                     str.append(orientation[i]);
//                     str.append(", ");
//                 }*/
//
//                 float pitch = orientation[1];
//                 Double pitchvalue = pitch* 180/Math.PI;
//
//                 Log.e("GetAngleChange", "==>" + pitchvalue);
//                 lblpitchvalue.setText("" + decimalFormat.format(Math.abs(pitchvalue)) + '°');
//
//
//
//                 /*
//                Double pitch = getDirection(event);
//                */
//
//            }
//
//
//        }
        /*
        switch(event.sensor.getType())
        {
            case Sensor.TYPE_ACCELEROMETER:
                mGravity = event.values.clone();
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                mMagnetic = event.values.clone();
                break;
            default:
                return;
        }
        if(mGravity != null && mMagnetic != null)
        {
            getDirection(event);
        }*/



    }

    private Double getDirection(final SensorEvent event)
    {
         double pitch = 0 ;

//        new Handler().postDelayed(new Runnable()
//        {
//            public void run() {


                    float X_Axis = event.values[0];
                    float Y_Axis = event.values[1];

                    Double angle = Math.atan2(X_Axis, Y_Axis) * (180 / Math.PI);
                    Log.e("Angle==>", "" + angle);

                    double rad = angle * Math.PI / 180;
                    double y = Math.tan(rad);
                     pitch = y * 12;



//            }
//        }, 5000);

        return pitch;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {

    }

    public class SingleMediaScanner implements MediaScannerConnection.MediaScannerConnectionClient
    {

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

        public void onScanCompleted(String path, Uri uri)
        {

            if(opengallery)
            {

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
            else
            {

            }

        }

    }


    public boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }


    public  Bitmap loadBitmapFromView(View v) {
        Bitmap bitmap;
        v.setDrawingCacheEnabled(true);
        bitmap = Bitmap.createBitmap(v.getDrawingCache());
        v.setDrawingCacheEnabled(false);
        return bitmap;
    }


    public void saveBitmap(Bitmap bitmap) {
        String filePath = Environment.getExternalStorageDirectory()
                + File.separator + "Pictures/screenshot.png";
        File imagePath = new File(filePath);
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(imagePath);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
//            sendMail(filePath);
        } catch (FileNotFoundException e) {
            Log.e("GREC", e.getMessage(), e);
        } catch (IOException e) {
            Log.e("GREC", e.getMessage(), e);
        }
    }



    public void onClick(View v) {
        int vid = v.getId();
        if (vid == btnrei.getId()) {

            btndamagetype.setText("Blank");
            btntype.setText("Blank");
            btntype.setVisibility(View.INVISIBLE);

            imgtop.setBackgroundResource(R.drawable.wticon);
            imgbottom.setBackgroundResource(R.drawable.wbicon);
            imgleft.setBackgroundResource(R.drawable.wlicon);
            imgright.setBackgroundResource(R.drawable.wricon);

            if (btnrei.getTag().equals("1"))
            {
                btnrei.setTag("2");
                btnrei.setText("E");
                btnmaterial.setText("Blank");
                rlcontrolview.setVisibility(View.VISIBLE);
                rliteriortype.setVisibility(View.GONE);
                stralphabetname = "f";
                btnmatrialsubmenu.setVisibility(View.GONE);
            } else if (btnrei.getTag().equals("2")) {
                btnrei.setTag("3");
                btnrei.setText("I");
                btnmaterial.setText("Blank");
                rlcontrolview.setVisibility(View.GONE);
                rliteriortype.setVisibility(View.VISIBLE);
                stralphabetname = "k";
                btnmatrialsubmenu.setVisibility(View.GONE);
            } else if (btnrei.getTag().equals("3")) {
                btnrei.setTag("1");
                btnrei.setText("R");
                stralphabetname = "a";
                btnmaterial.setText("Blank");
                rlcontrolview.setVisibility(View.VISIBLE);
                rliteriortype.setVisibility(View.GONE);
                btnmatrialsubmenu.setVisibility(View.GONE);
            }
            btniteriortype.setText("Bedroom");
            btnocb.setTag("1");
            btnocb.setText("O");
            strboc = "1";
            strboctype = "Overview";
            selectslope = "" ;
            stralphabetname = "";

        } else if (vid == btndamagetype.getId()) {
            showdamagetypeoption();
        } else if (vid == btnabc.getId()) {


            showabcoption();
           /* if (btnabc.getTag().equals("1")) {
                btnabc.setTag("2");
                btnabc.setText("B");
            } else if (btnabc.getTag().equals("2")) {
                btnabc.setTag("3");
                btnabc.setText("C");
            } else if (btnabc.getTag().equals("3")) {
                btnabc.setTag("1");
                btnabc.setText("A");
            }*/
        }
        else if (vid == btncat.getId()) {


            showclaimnameoption(true);
           /* if (btnabc.getTag().equals("1")) {
                btnabc.setTag("2");
                btnabc.setText("B");
            } else if (btnabc.getTag().equals("2")) {
                btnabc.setTag("3");
                btnabc.setText("C");
            } else if (btnabc.getTag().equals("3")) {
                btnabc.setTag("1");
                btnabc.setText("A");
            }*/
        }
        else if (vid == btnocb.getId()) {
            if (btnocb.getTag().equals("1")) {
                btnocb.setTag("2");
                btnocb.setText("C");
                strboc = "2";
                strboctype = "Close up";
            } else if (btnocb.getTag().equals("2")) {
                btnocb.setTag("3");
                btnocb.setText("B");
                strboc = "";
                strboctype = "Blank";
            } else if (btnocb.getTag().equals("3")) {
                btnocb.setTag("1");
                btnocb.setText("O");
                strboc = "1";
                strboctype = "Overview";
            }
        } else if (vid == btnmaterial.getId()) {
            showmaterialoption();
        }
        else if (vid == btnrisk.getId())
        {

            getMacrosname();



        } else if (vid == btnshinglemenu1.getId()) {

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
                    return false;
                }
            });

            popupMenu2.show();

        } else if (vid == btnLayersmenu1.getId()) {
            popupMenu2 = new PopupMenu(this, findViewById(R.id.btnLayersmenu1));
            for (int i = 0; i < 9; i++) {
                popupMenu2.getMenu().add(Menu.NONE, i, Menu.NONE, "" + (i + 1));
            }
                popupMenu2.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem arg0) {
                        btnLayersmenu1.setText(arg0.getTitle());
                        return false;
                    }
                });
            popupMenu2.show();
        } else if (vid == btnoverhangmenu1.getId()) {
            popupMenu2 = new PopupMenu(this, findViewById(R.id.btnoverhangmenu1));
            for (int i = 0; i < 99; i++) {
                popupMenu2.getMenu().add(Menu.NONE, i, Menu.NONE, "" + (i + 1));
            }
                popupMenu2.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem arg0) {
                        btnoverhangmenu1.setText(arg0.getTitle());
                        return false;
                    }
                });
                popupMenu2.show();
        }
        else if (vid == btnshinglemenu2.getId())
        {
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
                    return false;
                }
            });
            popupMenu2.show();


        } else if (vid == btnmatrialsubmenu.getId()) {
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
                    return false;
                }
            });
            popupMenu2.show();
        } else if (vid == btnguttermenu1.getId()) {
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
                    return false;
                }
            });
            popupMenu2.show();
        } else if (vid == btnLayersmenu.getId()) {
            value = 0;
            popupMenu2 = new PopupMenu(this, findViewById(R.id.btnLayersmenu));
            opendatabase();
            SELECT_SQL = "SELECT * FROM tbl_layermenu";
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
                    btnLayersmenu.setText(arg0.getTitle());
                    return false;
                }
            });
            popupMenu2.show();
        } else if (vid == btnskip.getId()) {
            nextPhoto("Layers");
        } else if (vid == btnlayerskip.getId()) {
            nextPhoto("Pitch");
        } else if (vid == btnpitchskip.getId()) {
            nextPhoto("Shingle");
        } else if (vid == btnshingleskip.getId()) {
            nextPhoto("Gutter");
        } else if (vid == btngutterskip.getId()) {
            nextPhoto("Overhang");
        } else if (vid == btnoverhangskip.getId()) {
            nextPhoto("Aditional Photo");
        } else if (vid == btncostmskip.getId()) {
//            nextPhoto("Aditional Photo");
        } else if (vid == rlBack.getId()) {
            btnrisk.setTag("1");
            showoption();
            btnrisk.setText("Aditional Photo");
        } else if (vid == rllayerBack.getId()) {
            btnrisk.setTag("1");
            hideallview();
            btnrisk.setText("Aditional Photo");
        } else if (vid == rlshingleBack.getId()) {
            btnrisk.setTag("1");
            hideallview();
            btnrisk.setText("Aditional Photo");
        } else if (vid == rlgutterBack.getId()) {
            btnrisk.setTag("1");
            hideallview();
            btnrisk.setText("Aditional Photo");
        } else if (vid == rlOverhangBack.getId()) {
            btnrisk.setTag("1");
            hideallview();
            btnrisk.setText("Aditional Photo");
        } else if (vid == rlBack.getId()) {
            btnrisk.setTag("1");
            hideallview();
            btnrisk.setText("Aditional Photo");
        } else if (vid == rlpitchBack.getId()) {
            btnrisk.setTag("1");
            hideallview();
            btnrisk.setText("Aditional Photo");
        } else if (vid == rlcostmback.getId()) {
            btnrisk.setTag("1");
            hideallview();
            btnrisk.setText("Aditional Photo");
        } else if (vid == btntype.getId()) {
            if (btntype.getTag().equals("1")) {
                btntype.setBackgroundColor(Color.parseColor("#FF0000"));
                btntype.setTag("2");
            } else if (btntype.getTag().equals("2")) {
                btntype.setBackgroundResource(R.drawable.button_background);
                btntype.setTag("1");
            }
        } else if (vid == imgtop.getId()) {
            btndamagetype.setText("Blank");

            if (btnrei.getText().toString().equals("R")) {
                selectslope = "F ";
                stralphabetname = "b";
            } else if (btnrei.getText().toString().equals("E")) {
                selectslope = "F ";
                stralphabetname = "g";
            } else if (btnrei.getText().toString().equals("I")) {
                selectslope = "F ";
                stralphabetname = "";
            }
            setarrowicon(imgtop, R.drawable.rticon);
            btnocb.setTag("1");
            btnocb.setText("O");
            strboc = "1";
            strboctype = "Overview";
        } else if (vid == imgbottom.getId()) {
            btndamagetype.setText("Blank");

            if (btnrei.getText().toString().equals("R")) {
                selectslope = "B ";
                stralphabetname = "d";
            } else if (btnrei.getText().toString().equals("E")) {
                selectslope = "B ";
                stralphabetname = "i";
            } else if (btnrei.getText().toString().equals("I")) {
                selectslope = "B ";
                stralphabetname = "";
            }
            setarrowicon(imgbottom, R.drawable.rbicon);
            btnocb.setTag("1");
            btnocb.setText("O");
            strboc = "1";
            strboctype = "Overview";
        } else if (vid == imgleft.getId()) {
            btndamagetype.setText("Blank");

            setarrowicon(imgleft, R.drawable.rlicon);
            if (btnrei.getText().toString().equals("R")) {
                selectslope = "L ";
                stralphabetname = "e";
            } else if (btnrei.getText().toString().equals("E")) {
                selectslope = "L ";
                stralphabetname = "j";
            } else if (btnrei.getText().toString().equals("I")) {
                selectslope = "L ";
                stralphabetname = "";
            }
            btnocb.setTag("1");
            btnocb.setText("O");
            strboc = "1";
            strboctype = "Overview";
        } else if (vid == imgright.getId()) {
            btndamagetype.setText("Blank");

            setarrowicon(imgright, R.drawable.rricon);
            if (btnrei.getText().toString().equals("R")) {
                selectslope = "R ";
                stralphabetname = "c";
            } else if (btnrei.getText().toString().equals("E")) {
                selectslope = "R ";
                stralphabetname = "h";
            } else if (btnrei.getText().toString().equals("I")) {
                selectslope = "R ";
                stralphabetname = "";
            }
            btnocb.setTag("1");
            btnocb.setText("O");
            strboc = "1";
            strboctype = "Overview";
        } else if (vid == rlsetting.getId()) {
        } else {
            if (vid == rltorch.getId()) {
                try {
                    if (getPackageManager().hasSystemFeature("android.hardware.camera.flash")) {
                        camera.stopPreview();
                        camera.release();
                        camera = null;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                startActivity(new Intent(getApplicationContext(), TorchActivity.class));
                finish();
            } else if (vid == btnok.getId()) {
                if (txtfoldername.getText().toString().trim().equals("")) {
                    Toast.makeText(getApplicationContext(), "Please enter name", Toast.LENGTH_LONG).show();
                    return;
                }

                btncat.setText(txtfoldername.getText().toString().trim());
                lastimageeditor.putString("appfoldername", txtfoldername.getText().toString().trim());
                lastimageeditor.commit();
                appfoldername = txtfoldername.getText().toString().trim();
                mydir = new File(Environment.getExternalStorageDirectory(), appfoldername);
                rlsetting.setVisibility(View.GONE);
                btnabc.setText("None");
            } else if (vid == btnroofadd.getId()) {
                addmenuvalue("tbl_r", "Roof");
            } else if (vid == btnaddelevations.getId()) {
                addmenuvalue("tbl_e", "Elevations");
            } else if (vid == btnaddinterior.getId()) {
                addmenuvalue("tbl_i", "Interior");
            } else if (vid == btnadddamage.getId()) {
                addmenuvalue("tbldamagetype", "Damage");
            } else if (vid == btnesubcatgry.getId()) {
                addmenuvalue("tbl_interior", "Interior Subcategory");
            } else if (vid == btnaddclaimname.getId()){
                rlsetting.setVisibility(View.GONE);
                Intent addnclaimname = new Intent(getApplicationContext(),AddClaimNameActivity.class);
                startActivity(addnclaimname);
           }
            else if (vid == btnmacroadd.getId())
            {
                addmenuvalue("tbl_macros", "Add Macros");

            }
            else if (vid == btncamsave.getId())
            {

                saveimagename(getaplphaname(),calljpgsavevalue());
            }

            else if (vid == btncancel.getId()) {
                rlsetting.setVisibility(View.GONE);
            } else if (vid == btniteriortype.getId()) {
                setInteriorvalue();
            } else if (vid == btnlastphoto.getId()) {

                /*
                String lastimagename = lastpathpf.getString("lastimgname", "");
                File latimagefile = new File(lastpathpf.getString("lastimgpath", ""));
                opengallery = Boolean.valueOf(true);
                allFiles = mydir.listFiles();
                if (allFiles.length == 0) {
                    return;
                }
                SingleMediaScanner singleMediaScanner;
                if (latimagefile.exists()) {
                    singleMediaScanner = new SingleMediaScanner(this, latimagefile);
                } else {
                    singleMediaScanner = new SingleMediaScanner(this, allFiles[0]);
                }*/
            } else if (vid == ibtnflash.getId()) {
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
            } else if (vid == imgbtncam.getId())
            {
                /*try {
                    captureImage();
                } catch (IOException e2) {
                    e2.printStackTrace();
                }*/
            } else if (vid == imgbtnsetting.getId()) {
                txtfoldername.setText(appfoldername);
                if (rlsetting.getVisibility() == View.VISIBLE) {
                    rlsetting.setVisibility(View.GONE);
                } else {
                    rlsetting.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    private void saveimagename(final String name, final String value)
    {

        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setTitle("Save Name");
        builder1.setMessage(name);
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Save",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {


                        if(isexists(name))
                        {
                            Toast.makeText(getApplicationContext(), "Name is already Exists", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            dialog.cancel();
                            addcamviewname(name,value);

                        }


                    }
                });


        builder1.setNegativeButton(
                "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();

    }


    private boolean isexists(String value) {

        opendatabase();
        Cursor cursor = DB.rawQuery("SELECT  * FROM tbl_macrossubcat where name = '"+value+"' ",null);
        int count = cursor.getCount();

        if(count == 0)
        {
            return  false;
        }
        else
        {
            return  true;
        }

    }

    private void addcamviewname(String name, String value)
    {
            opendatabase();

            String strqry = "Insert into tbl_macrossubcat ('cid','name','value') Values('"+strcid+"','"+name+"','"+value+"')";
            DB.execSQL(strqry);
            DB.close();

            finish();
    }



    private void getMacrosname()
    {

        value = 0;
//        macrosarr = new ArrayList<>();

        popupMenu2 = new PopupMenu(this, findViewById(R.id.btnrisk));


        opendatabase();


        SELECT_SQL = "SELECT * FROM tbl_macros";
        Cur = DB.rawQuery(SELECT_SQL, null);
        if(Cur != null && Cur.getCount() > 0)
        {
            Cur.moveToFirst();
            do
            {

                popupMenu2.getMenu().add(Menu.NONE, 0, Menu.NONE, Cur.getString(Cur.getColumnIndex("name")));
                value++;
            }
            while(Cur.moveToNext());
        }
        Cur.close();
        DB.close();

        popupMenu2.getMenu().add(Menu.NONE, value, Menu.NONE, "Add Custom Photo");

        /*
        popupMenu2.getMenu().add(Menu.NONE, 0, Menu.NONE, "Aditional Photo");
        popupMenu2.getMenu().add(Menu.NONE, 1, Menu.NONE, "Risk");
        popupMenu2.getMenu().add(Menu.NONE, 2, Menu.NONE, "Layers");
        popupMenu2.getMenu().add(Menu.NONE, 3, Menu.NONE, "Pitch");
        popupMenu2.getMenu().add(Menu.NONE, 4, Menu.NONE, "Shingle");
        popupMenu2.getMenu().add(Menu.NONE, 5, Menu.NONE, "Gutter");
        popupMenu2.getMenu().add(Menu.NONE, 6, Menu.NONE, "Overhang");
        */

        popupMenu2.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener()
        {
            @Override
            public boolean onMenuItemClick(MenuItem arg0)
            {

                if(arg0.getTitle().toString().equals("Add Custom Photo"))
                {
                    macrosalert();
                }
                else
                {
                    nextPhoto(arg0.getTitle().toString());
                }

                return false;
            }
        });
        popupMenu2.show();

    }

    private void macrosalert()
    {

        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Add Macros");
        final EditText input = new EditText(this);
        input.setHint("Enter Macros Name");
        alert.setView(input);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                String value = input.getText().toString().trim();

                if (value.equals("")) {
                    Toast.makeText(getApplicationContext(), "please enter macros name", Toast.LENGTH_SHORT).show();

                }
                else
                {
                    btnrisk.setText(value);
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

    private void addmacros(String name)
    {

        opendatabase();
        DB.execSQL("insert into tbl_macros (name)" + "values('" + name + "') ;");
        hidecostmoption();

    }


}
