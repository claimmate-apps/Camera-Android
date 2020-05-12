package com.commonsware.cwac.camera.demo.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatImageView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.commonsware.cwac.camera.demo.other.Utility;
import com.commonsware.cwac.camera.demo.retrofit.APIInterface;
import com.commonsware.cwac.camera.demo.retrofit.ApiClient;
import com.example.claimmate.R;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;


public class RegisterActivity extends Activity implements View.OnClickListener {

    private final String TAG = "RegisterActivity";
    private Context mContext;
    ProgressDialog progressDialog;
    /*EditText tvfname,tvlname,tvphone,tvsphone,tvemail,tvaddress1,tvaddress2;
    EditText tvcity,tvstate,tvzipcode,tvpasswprd,tvcpasswprd;
    Button btnregister;
    ProgressDialog pd;*/

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    WebView wv_register;
    AppCompatImageView img_back;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        init();

        /*pd = new ProgressDialog(RegisterActivity.this);
        pd.setMessage("Wait...");


        tvfname = (EditText)findViewById(R.id.tvfname);
        tvlname = (EditText)findViewById(R.id.tvlname);
        tvphone = (EditText)findViewById(R.id.tvphone);
        tvsphone = (EditText)findViewById(R.id.tvsphone);
        tvemail = (EditText)findViewById(R.id.tvemail);
        tvaddress1 = (EditText)findViewById(R.id.tvaddress1);
        tvaddress2 = (EditText)findViewById(R.id.tvaddress2);
        tvcity = (EditText)findViewById(R.id.tvcity);
        tvstate = (EditText)findViewById(R.id.tvstate);
        tvzipcode = (EditText)findViewById(R.id.tvzipcode);
        tvpasswprd = (EditText)findViewById(R.id.tvpasswprd);
        tvcpasswprd = (EditText)findViewById(R.id.tvcpasswprd);

        btnregister = (Button) findViewById(R.id.btnregister);


        btnregister.setOnClickListener(this);*/
    }

    private EditText edtName, edtMobile, edtEmail, edtPassword, edtConfirmPassword;
    private Button btnRegister;

    @SuppressLint("SetJavaScriptEnabled")
    private void init() {
        mContext = RegisterActivity.this;


        this.wv_register = (WebView) findViewById(R.id.wv_register);

        wv_register.setWebViewClient(new myWebClient());
        WebSettings webSettings = wv_register.getSettings();
        webSettings.setJavaScriptEnabled(true);
        wv_register.getSettings().setBuiltInZoomControls(false);
        wv_register.getSettings().setSupportZoom(true);
        wv_register.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        wv_register.getSettings().setBuiltInZoomControls(true);
//      webview.getSettings().setCacheMode( WebSettings.LOAD_CACHE_ELSE_NETWORK );
        wv_register.getSettings().setDisplayZoomControls(false);

        startProDialog();

        wv_register.loadUrl("https://www.claimmate.com/signup");



        img_back = findViewById(R.id.img_back);
        edtName = findViewById(R.id.edtName);
        edtMobile = findViewById(R.id.edtMobile);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);

        btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(this);
        img_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == btnRegister.getId()) {
            if (Utility.haveInternet(mContext, true)) {
                if (validate()) {
                    register();
                }
            }
        }
        else if(view == img_back)
        {
            finish();
        }
    }

    private void register() {
        Utility.showProgress(mContext);
        ApiClient.getClient().create(APIInterface.class).registerUser(edtName.getText().toString(), edtEmail.getText().toString(), edtMobile.getText().toString(), edtPassword.getText().toString()).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                Utility.dismissProgress();
                Log.i(TAG, "registerUserRes = "+response.body());

                if (response.body() == null) {
                    Utility.errorDialog(mContext, getString(R.string.error_data_not_found));
                    return;
                }

                try {
                    JSONObject jsonObject = new JSONObject(response.body());
                    Toast.makeText(mContext, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                    if (jsonObject.getString("success").equals("success")) {
                        onBackPressed();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Utility.dismissProgress();
                Log.i(TAG, "registerUserError = "+t.toString());
            }
        });
    }

    private boolean validate() {
        boolean isValid = true;

        if (TextUtils.isEmpty(edtName.getText())) {
            edtName.setError(getString(R.string.error_field_required));
            edtName.requestFocus();
            isValid = false;
        } else if (TextUtils.isEmpty(edtMobile.getText())) {
            edtMobile.setError(getString(R.string.error_field_required));
            edtMobile.requestFocus();
            isValid = false;
        } else if (TextUtils.isEmpty(edtEmail.getText())) {
            edtEmail.setError(getString(R.string.error_field_required));
            edtEmail.requestFocus();
            isValid = false;
        } else if (!edtEmail.getText().toString().matches(emailPattern)) {
            edtEmail.setError("Enter Valid Email Address.");
            edtEmail.requestFocus();
            isValid = false;
        } else if (TextUtils.isEmpty(edtPassword.getText())) {
            edtPassword.setError(getString(R.string.error_field_required));
            edtPassword.requestFocus();
            isValid = false;
        } else if (TextUtils.isEmpty(edtConfirmPassword.getText())) {
            edtConfirmPassword.setError(getString(R.string.error_field_required));
            edtConfirmPassword.requestFocus();
            isValid = false;
        } else if (!edtPassword.getText().toString().equals(edtConfirmPassword.getText().toString())) {
            Toast.makeText(getApplicationContext(),"Both password must be same.",Toast.LENGTH_LONG).show();
            isValid = false;
        }
        return isValid;
    }

    /*@Override
    public void onClick(View v) {


        if(v == btnregister)
        {


            if(tvfname.getText().toString().trim().equals(""))
            {
                Toast.makeText(getApplicationContext(),"Enter First Name",Toast.LENGTH_LONG).show();
            }
            else if(tvlname.getText().toString().trim().equals(""))
            {
                Toast.makeText(getApplicationContext(),"Enter Last Name",Toast.LENGTH_LONG).show();
            }
            else if(tvphone.getText().toString().trim().equals("") && tvsphone.getText().toString().trim().equals(""))
            {
                Toast.makeText(getApplicationContext(),"Enter atleast One Number",Toast.LENGTH_LONG).show();
            }
            else if(tvemail.getText().toString().trim().equals(""))
            {
                Toast.makeText(getApplicationContext(),"Enter Email Address",Toast.LENGTH_LONG).show();
            }
            else if(!tvemail.getText().toString().trim().matches(emailPattern))
            {
                Toast.makeText(getApplicationContext(),"Enter Valid Email Address",Toast.LENGTH_LONG).show();
            }
            else if(tvcity.getText().toString().trim().equals(""))
            {
                Toast.makeText(getApplicationContext(),"Enter City",Toast.LENGTH_LONG).show();
            }
            else if(tvstate.getText().toString().trim().equals(""))
            {
                Toast.makeText(getApplicationContext(),"Enter State",Toast.LENGTH_LONG).show();
            }
            else if(tvzipcode.getText().toString().trim().equals(""))
            {
                Toast.makeText(getApplicationContext(),"Enter Zip Code",Toast.LENGTH_LONG).show();
            }
            else if(tvpasswprd.getText().toString().trim().equals(""))
            {
                Toast.makeText(getApplicationContext(),"Enter Password",Toast.LENGTH_LONG).show();
            }
            else if(tvcpasswprd.getText().toString().trim().equals(""))
            {
                Toast.makeText(getApplicationContext(),"Enter Confirm Password",Toast.LENGTH_LONG).show();
            }
            else if(!tvpasswprd.getText().toString().trim().equals(tvcpasswprd.getText().toString().trim()))
            {
                Toast.makeText(getApplicationContext(),"Both password must be same",Toast.LENGTH_LONG).show();
            }
            else
            {

                    String registerurl = "http://adjuster.claimmate.com/api/UserRegistration/RegisterMe";

                pd.show();

                RequestQueue mRequestQueue = Volley.newRequestQueue(getApplicationContext());

                StringRequest stringRequest = new StringRequest(Request.Method.POST, registerurl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response)
                    {
                        pd.dismiss();

                        try {
                            JSONObject jobj = new JSONObject(response);

                            if(jobj.getString("success").equals("1"))
                            {

                                Toast.makeText(getApplicationContext(),jobj.getString("message"),Toast.LENGTH_LONG).show();
                                Intent homescreen = new Intent(RegisterActivity.this,LoginActivity.class);
                                startActivity(homescreen);
                                finish();
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),jobj.getString("message"),Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                })
                {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError
                    {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("first_name", tvfname.getText().toString());
                        params.put("last_name", tvlname.getText().toString());
                        params.put("cell_phone", tvphone.getText().toString());
                        params.put("alt_phone", tvsphone.getText().toString());
                        params.put("email", tvemail.getText().toString());
                        params.put("address_1", tvaddress1.getText().toString());
                        params.put("address_2", tvaddress2.getText().toString());
                        params.put("city", tvcity.getText().toString());
                        params.put("state", tvstate.getText().toString());
                        params.put("zip_code", tvzipcode.getText().toString());
                        params.put("password", tvpasswprd.getText().toString());
                        params.put("confirm_password", tvcpasswprd.getText().toString());

                        return params;
                    }
                };

                mRequestQueue.add(stringRequest);
            }

        }
    }*/



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && this.wv_register.canGoBack()) {
            this.wv_register.goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }



    public void startProDialog()
    {

        if(progressDialog == null)
        {
            progressDialog = new ProgressDialog(RegisterActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Loading... please wait");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
        }
        else
        {
            progressDialog.show();
        }

    }

    public void stopProDialog()
    {
        if(progressDialog != null )
        {
            if(progressDialog.isShowing())
            {
                progressDialog.dismiss();
            }
        }
    }


    public  class myWebClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            startProDialog();
            view.loadUrl(url);
            return true;

        }

        @Override
        public void onPageFinished(WebView view, String url) {

            super.onPageFinished(view, url);
            stopProDialog();
        }

    }


}
