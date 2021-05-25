package com.commonsware.cwac.camera.demo.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatSpinner;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.commonsware.cwac.camera.demo.common.BaseActivity;
import com.commonsware.cwac.camera.demo.common.Commons;
import com.commonsware.cwac.camera.demo.model.UserModel;
import com.commonsware.cwac.camera.demo.retrofit.ApiManager;
import com.commonsware.cwac.camera.demo.retrofit.ICallback;
import com.example.claimmate.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.commonsware.cwac.camera.demo.common.Commons.GET_COMPANIES;

public class SignUpActivity extends BaseActivity {

    @BindView(R.id.edt_first_name)
    EditText edt_first_name;
    @BindView(R.id.edt_last_name)
    EditText edt_last_name;

    @BindView(R.id.edt_email)
    EditText edt_email;
    @BindView(R.id.edt_password)
    EditText edt_password;

    @BindView(R.id.spinner_companies)
    AppCompatSpinner spinner_companies;

    @BindView(R.id.txv_terms)
    TextView txv_terms;
    @BindView(R.id.txv_policy) TextView txv_policy;

    @BindView(R.id.checkbox_terms)
    CheckBox checkbox_terms;

    String _firstname = "", _lastname = "", _email = "", _password = "", _company = "0";
    ArrayList<UserModel> companies = new ArrayList<>();
    ArrayList<String> companyList = new ArrayList<String>();

    private static final int RESULT_OK = 200;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setupUI(findViewById(R.id.lyt_container), this);

        AndroidNetworking.initialize(getApplicationContext());

        ButterKnife.bind(this);

        getCompanies();
    }

    private void getCompanies(){
        companies.clear();

        AndroidNetworking.get(GET_COMPANIES)
                .setTag(this)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("company_list====>", response.toString());

                            int resultCode = response.getInt(ApiManager.PARAMS.RESULT_CODE);
                            if (resultCode == RESULT_OK) {
                                JSONArray jsonArray = response.getJSONArray("company_list");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    UserModel userModel = new UserModel();
                                    int id = jsonObject.getInt("id");  userModel.setId(id);
                                    String name = jsonObject.getString("name");  userModel.setFirstName(name);
                                    String email = jsonObject.getString("email");  userModel.setEmail(email);
                                    String mobile_no = jsonObject.getString("mobile_no");  userModel.setWorkDays(mobile_no);

                                    companies.add(userModel);
                                    //company_names[i] = name;
                                    companyList.add(name);
                                }
                            }

                            ArrayAdapter<String> companyAdapter = new ArrayAdapter<String>(SignUpActivity.this, android.R.layout.simple_spinner_item, companyList);
                            companyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner_companies.setAdapter(companyAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("SignupActivity====>", "Network issue Catch!");
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                    }
                });
    }

    private void processSignUp(){

        showHUD();

        ApiManager.signUp(_firstname, _lastname, _email, _password, _company, new ICallback() {
            @Override
            public void onCompletion(RESULT result, Object resultParam) {
                hideHUD();
                switch (result){

                    case FAILURE:

                        if (resultParam == null)
                            showToast1(getString(R.string.error));

                        else {
                            int resultCode = (Integer)resultParam;
                            if (resultCode == 201) {
                                showToast1("Email already existed");
                            }
                        }
                        break;

                    case SUCCESS:
                        gotoLogin();
                        break;
                }
            }
        });
    }

    @OnClick({R.id.txv_terms, R.id.txv_policy}) void checkTerms(){

        checkbox_terms.setChecked(true);
    }

    @OnClick(R.id.txv_signIn) void gotoBack(){
        finish();
    }

    @OnClick(R.id.btn_continue) void continueLogin(){

        Log.d("Selected Position===>", String.valueOf(spinner_companies.getSelectedItemPosition()));

        _firstname = edt_first_name.getText().toString();
        _lastname = edt_last_name.getText().toString();
        _email = edt_email.getText().toString();
        _password = edt_password.getText().toString();
        if (companies.size() > 0){
            _company = String.valueOf(companies.get(spinner_companies.getSelectedItemPosition()).getId());
        }
        Log.d("Selected Company===>", _company);


        if (checkedValid())
            processSignUp();
    }

    void gotoLogin(){

        Intent intent = new Intent();
        intent.putExtra(Commons.PREFKEY_USEREMAIL, _email);
        intent.putExtra(Commons.PREFKEY_USERPWD, _password);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    boolean checkedValid(){

        if (_firstname.isEmpty()){
            showToast("Enter first name");
            return false;
        }

        if (_lastname.isEmpty()){
            showToast("Enter last name");
            return false;
        }

        if (_email.isEmpty()){
            showToast(getString(R.string.enter_email));
            return false;
        }

        if (_password.isEmpty()){
            showToast("Enter password");
            return false;
        }
        if (!checkbox_terms.isChecked()){

            showToast("Check Terms of Service and Privacy Policy");
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        gotoBack();
    }
}