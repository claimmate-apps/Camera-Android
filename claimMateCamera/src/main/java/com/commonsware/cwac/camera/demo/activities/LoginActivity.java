package com.commonsware.cwac.camera.demo.activities;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends Activity implements View.OnClickListener {

    private final String TAG = "LoginActivity";
    private Context mContext;

    EditText tvemail, tvpassword;
    Button btnlogin, btnregister;
    TextView txtForgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        mContext = LoginActivity.this;
        new PrefManager(mContext);

        tvemail = findViewById(R.id.tvemail);
        tvpassword = findViewById(R.id.tvpassword);
        btnlogin = findViewById(R.id.btnlogin);
        btnregister = findViewById(R.id.btnregister);
        txtForgotPassword = findViewById(R.id.txtForgotPassword);

        btnregister.setOnClickListener(this);
        btnlogin.setOnClickListener(this);
        txtForgotPassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == btnlogin.getId()) {
            if (Utility.haveInternet(mContext, true)) {
                if (tvemail.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Enter Email", Toast.LENGTH_LONG).show();
                } else if (tvpassword.getText().toString().trim().equals("")) {
                    Toast.makeText(getApplicationContext(), "Enter Password", Toast.LENGTH_LONG).show();
                } else {
                    LoginUser();
                }
            }
        } else if (view.getId() == btnregister.getId()) {
            Intent register_act = new Intent(getApplicationContext(), RegisterActivity.class);
            startActivity(register_act);
        } else if (view.getId() == txtForgotPassword.getId()) {
            forgotPassword();
        }
    }

    private void LoginUser() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 101);
            return;
        }
        //final String IMEI = ((TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
        final String IMEI = "thisistempimei";

        Utility.showProgress(mContext);
        ApiClient.getClient().create(APIInterface.class).login(tvemail.getText().toString(), tvpassword.getText().toString(), IMEI, "cam").enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                Utility.dismissProgress();
                Log.i(TAG, "loginRes = "+response.body());

                if (response.body() == null) {
                    Utility.errorDialog(mContext, getString(R.string.error_data_not_found));
                    return;
                }

                try {
                    JSONObject jsonObject = new JSONObject(response.body());

                    if (jsonObject.getString("success").equals("success")) {
                        JSONObject user_data = jsonObject.getJSONObject("user_data");

                        PrefManager.setUserId(user_data.getString("id"));
                        PrefManager.setFullName(user_data.getString("fname"));

                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                        LoginActivity.this.finish();
                    } else {
                        Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Utility.dismissProgress();
                Log.i(TAG, "loginError = "+t.toString());
            }
        });

        // Login old api
        /*final String stremail = tvemail.getText().toString().trim();
        final String strpsw = tvpassword.getText().toString().trim();

        String loginurl = "http://adjuster.claimmate.com/api/LoginAuth";

        RequestQueue mRequestQueue = Volley.newRequestQueue(getApplicationContext());

        Utility.showProgress(mContext);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, loginurl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Utility.dismissProgress();
                Log.e("app_res", "res = "+response);
                try {
                    JSONObject jobj = new JSONObject(response);

                    if (jobj.getString("success").equals("success")) {
                        editor = pref.edit();
                        editor.putString("user_id", "" + jobj.getString("user_id"));
                        editor.commit();

                        Intent homescreen = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(homescreen);
                        LoginActivity.this.finish();
                    } else {
                        Toast.makeText(getApplicationContext(), jobj.getString("message"), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utility.dismissProgress();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", stremail);
                params.put("password", strpsw);
                return params;
            }
        };
        mRequestQueue.add(stringRequest);*/
    }

    private void forgotPassword() {
        final Dialog dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.popup_forgot_password);

        final EditText edtEmail = dialog.findViewById(R.id.edtEmail);

        dialog.findViewById(R.id.btnSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(edtEmail.getText())) {
                    edtEmail.setError("Enter Email.");
                    edtEmail.requestFocus();
                } else {
                    Utility.showProgress(mContext);
                    ApiClient.getClient().create(APIInterface.class).forgotPassword(edtEmail.getText().toString()).enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            Utility.dismissProgress();
                            Log.i(TAG, "forgotPasswordRes = "+response.body());
                            if (response.body() == null) {
                                Utility.errorDialog(mContext, getString(R.string.error_data_not_found));
                                return;
                            }

                            try {
                                JSONObject jsonObject = new JSONObject(response.body());

                                if (jsonObject.getString("success").equalsIgnoreCase("success")) {
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
                            Log.i(TAG, "forgotPasswordError = "+t.toString());
                        }
                    });
                }
            }
        });

        dialog.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
