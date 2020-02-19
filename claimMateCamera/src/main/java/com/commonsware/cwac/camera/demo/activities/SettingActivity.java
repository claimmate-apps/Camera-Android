package com.commonsware.cwac.camera.demo.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.commonsware.cwac.camera.demo.HomeActivity;
import com.commonsware.cwac.camera.demo.other.PrefManager;
import com.commonsware.cwac.camera.demo.other.Utility;
import com.commonsware.cwac.camera.demo.retrofit.APIInterface;
import com.commonsware.cwac.camera.demo.retrofit.ApiClient;
import com.example.claimmate.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import retrofit2.Call;
import retrofit2.Callback;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = "SettingActivity";
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        init();
    }

    private RelativeLayout rlBack;
    EditText edtFolderName;
    RadioButton rbDamageMark, rbDamageUnmark, rbDamageLeave;
    RadioButton rbAreaMark, rbAreaUnmark, rbAreaLeave;
    RadioButton rbAreaSubMark, rbAreaSubUnmark, rbAreaSubLeave;
    RadioButton rbCeiling, rbWall, rbFloor;
    RadioButton rbMaterialMark, rbMaterialUnmark, rbMaterialLeave;
    RadioButton rbOverview, rbCloseUp, rbBlank;
    private Button btnaddclaimname, btnmacroadd, btnroofadd, btnaddelevations, btnaddinterior, btnesubcatgry, btnadddamage, btnAddDocument, btnLogout, btnOk, btnCancel;

    private void init() {
        rlBack = findViewById(R.id.rlBack);

        edtFolderName = findViewById(R.id.edtFolderName);

        rbDamageMark = findViewById(R.id.rbDamageMark);
        rbDamageUnmark = findViewById(R.id.rbDamageUnmark);
        rbDamageLeave = findViewById(R.id.rbDamageLeave);

        btnaddclaimname = findViewById(R.id.btnaddclaimname);
        btnmacroadd = findViewById(R.id.btnmacroadd);
        btnroofadd = findViewById(R.id.btnroofadd);
        btnaddelevations = findViewById(R.id.btnaddelevations);
        btnaddinterior = findViewById(R.id.btnaddinterior);
        btnesubcatgry = findViewById(R.id.btnesubcatgry);
        btnadddamage = findViewById(R.id.btnadddamage);
        btnAddDocument = findViewById(R.id.btnAddDocument);
        btnLogout = findViewById(R.id.btnLogout);
        btnOk = findViewById(R.id.btnOk);
        btnCancel = findViewById(R.id.btnCancel);

        btnaddclaimname.setOnClickListener(this);
        btnmacroadd.setOnClickListener(this);
        btnroofadd.setOnClickListener(this);
        btnaddelevations.setOnClickListener(this);
        btnaddinterior.setOnClickListener(this);
        btnesubcatgry.setOnClickListener(this);
        btnadddamage.setOnClickListener(this);
        btnAddDocument.setOnClickListener(this);
        btnLogout.setOnClickListener(this);
        btnOk.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        rlBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == rlBack.getId()) {
            onBackPressed();
        } else if (view.getId() == btnaddclaimname.getId()) {
            startActivity(new Intent(getApplicationContext(), AddClaimNameActivity.class));
        } else if (view.getId() == btnmacroadd.getId()) {
            addMenuValue("tbl_macros", "Add Macros");
        } else if (view.getId() == btnroofadd.getId()) {
            addMenuValue("tbl_r", "Roof");
        } else if (view.getId() == btnaddelevations.getId()) {
            addMenuValue("tbl_e", "Elevations");
        } else if (view.getId() == btnaddinterior.getId()) {
            addMenuValue("tbl_i", "Interior");
        } else if (view.getId() == btnesubcatgry.getId()) {
            addMenuValue("tbl_interior", "Interior Subcategory");
        } else if (view.getId() == btnadddamage.getId()) {
            addMenuValue("tbldamagetype", "Damage");
        } else if (view.getId() == btnAddDocument.getId()) {

        } else if (view.getId() == btnLogout.getId()) {
            logout();
        } else if (view.getId() == btnOk.getId()) {
//            if (txtfoldername.getText().toString().trim().equals("")) {
//                Toast.makeText(getApplicationContext(), "Please enter name", Toast.LENGTH_LONG).show();
//                return;
//            }
//
//            btncat.setText(txtfoldername.getText().toString().trim());
//            lastimageeditor.putString("appfoldername", txtfoldername.getText().toString().trim());
//            lastimageeditor.commit();
//            appfoldername = txtfoldername.getText().toString().trim();
//            mydir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), appfoldername);
//            rlsetting.setVisibility(View.GONE);
//            btnabc.setText("None");
        } else if (view.getId() == btnCancel.getId()) {
            onBackPressed();
        }
    }

    private void addMenuValue(String tname, String strtitle) {

        Intent addnewvalue = new Intent(getApplicationContext(), AddValueActivity.class);
        addnewvalue.putExtra("tablename", tname);
        addnewvalue.putExtra("title", strtitle);
        startActivity(addnewvalue);
    }

    private void logout() {

        if (Utility.haveInternet(mContext, true)) {
            Utility.showProgress(mContext);
            ApiClient.getClient().create(APIInterface.class).logout(PrefManager.getUserId(), "cam").enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, retrofit2.Response<String> response) {
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
                            Intent intent = new Intent(mContext, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            SettingActivity.this.finish();
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

    @Override
    public void onBackPressed() {
        HomeActivity.rbDamageMark.setSelected(rbDamageMark.isChecked());
        HomeActivity.rbDamageUnmark.setSelected(rbDamageUnmark.isChecked());
        HomeActivity.rbDamageLeave.setSelected(rbDamageLeave.isChecked());
        super.onBackPressed();
    }
}
