package com.commonsware.cwac.camera.demo.activities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.commonsware.cwac.camera.demo.model.ClaimModel;
import com.commonsware.cwac.camera.demo.other.PrefManager;
import com.commonsware.cwac.camera.demo.other.Utility;
import com.commonsware.cwac.camera.demo.retrofit.APIInterface;
import com.commonsware.cwac.camera.demo.retrofit.ApiClient;
import com.example.claimmate.R;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = "ReportActivity";
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        init();
    }

    @Override
    protected void onStart() {
        super.onStart();

        getClaimReport();
    }

    private RelativeLayout rlback;
    private EditText edtName, edtCauseOfLoss, edtMortgage, edtLaborMin, edtCompany, edtOfStories, edtTypeOfConstruction, edtRCI, edtSingleFamily, edtGaragesDetached, edtTypeOfExteriorSiding, edtInsuredPersonPresent, edtOP, edtDepreciation, edtContents, edtSalvage, edtSubrogation;
    private TextView txtDateOfLoss, txtInspectionDate;
    private ImageButton imgDateOfLoss, imgInspectionDate;
    private Button btnSave;

    private ClaimModel claimModel;

    private void init() {
        mContext = ReportActivity.this;

        rlback = findViewById(R.id.rlback);

        edtName = findViewById(R.id.edtName);
        edtCauseOfLoss = findViewById(R.id.edtCauseOfLoss);
        edtMortgage = findViewById(R.id.edtMortgage);
        edtLaborMin = findViewById(R.id.edtLaborMin);
        edtCompany = findViewById(R.id.edtCompany);
        edtOfStories = findViewById(R.id.edtOfStories);
        edtTypeOfConstruction = findViewById(R.id.edtTypeOfConstruction);
        edtRCI = findViewById(R.id.edtRCI);
        edtSingleFamily = findViewById(R.id.edtSingleFamily);
        edtGaragesDetached = findViewById(R.id.edtGaragesDetached);
        edtTypeOfExteriorSiding = findViewById(R.id.edtTypeOfExteriorSiding);
        edtInsuredPersonPresent = findViewById(R.id.edtInsuredPersonPresent);
        edtOP = findViewById(R.id.edtOP);
        edtDepreciation = findViewById(R.id.edtDepreciation);
        edtContents = findViewById(R.id.edtContents);
        edtSalvage = findViewById(R.id.edtSalvage);
        edtSubrogation = findViewById(R.id.edtSubrogation);

        txtDateOfLoss = findViewById(R.id.txtDateOfLoss);
        txtInspectionDate = findViewById(R.id.txtInspectionDate);

        imgDateOfLoss = findViewById(R.id.imgDateOfLoss);
        imgInspectionDate = findViewById(R.id.imgInspectionDate);

        btnSave = findViewById(R.id.btnSave);

        rlback.setOnClickListener(this);
        imgDateOfLoss.setOnClickListener(this);
        imgInspectionDate.setOnClickListener(this);
        btnSave.setOnClickListener(this);
    }

    private void setClaimReportData() {

        edtName.setText(claimModel.getName());
        edtCauseOfLoss.setText(claimModel.getCause_of_loss());
        edtMortgage.setText(claimModel.getMortgage());
        edtLaborMin.setText(claimModel.getLabor_min());
        edtCompany.setText(claimModel.getCompany());
        edtOfStories.setText(claimModel.getStories());
        edtTypeOfConstruction.setText(claimModel.getType_of_construction());
        edtRCI.setText(claimModel.getRci());
        edtSingleFamily.setText(claimModel.getSingle_family());
        edtGaragesDetached.setText(claimModel.getGarages());
        edtTypeOfExteriorSiding.setText(claimModel.getExterior_siding());
        edtInsuredPersonPresent.setText(claimModel.getInsured_person());
        edtOP.setText(claimModel.getOp());
        edtDepreciation.setText(claimModel.getDepreciation());
        edtContents.setText(claimModel.getContents());
        edtSalvage.setText(claimModel.getSalvage());
        edtSubrogation.setText(claimModel.getSubrogation());

        txtDateOfLoss.setText(claimModel.getDate_of_loss());
        txtInspectionDate.setText(claimModel.getInspection_date());

        /*edtName.setEnabled(false);
        edtCauseOfLoss.setEnabled(false);
        edtMortgage.setEnabled(false);
        edtLaborMin.setEnabled(false);
        edtCompany.setEnabled(false);
        edtOfStories.setEnabled(false);
        edtTypeOfConstruction.setEnabled(false);
        edtRCI.setEnabled(false);
        edtSingleFamily.setEnabled(false);
        edtGaragesDetached.setEnabled(false);
        edtTypeOfExteriorSiding.setEnabled(false);
        edtInsuredPersonPresent.setEnabled(false);
        edtOP.setEnabled(false);
        edtDepreciation.setEnabled(false);
        edtContents.setEnabled(false);
        edtSalvage.setEnabled(false);
        edtSubrogation.setEnabled(false);

        imgDateOfLoss.setEnabled(false);
        imgInspectionDate.setEnabled(false);*/

        btnSave.setText("Update");
//        btnSave.setVisibility(View.GONE);
    }

    private void getClaimReport() {
        Utility.showProgress(mContext);
        ApiClient.getClient().create(APIInterface.class).getClaimReport(PrefManager.getUserId(), getIntent().getStringExtra("claimId")).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Utility.dismissProgress();
                Log.i(TAG, "getClaimReportRes = "+response.body());

                if (response.body() == null) {
                    Utility.errorDialog(mContext, getString(R.string.error_data_not_found));
                    return;
                }

                try {
                    JSONObject jsonObject = new JSONObject(response.body());
                    if (jsonObject.getString("success").equals("success")) {
                        Gson gson = new Gson();
                        claimModel = gson.fromJson(jsonObject.getJSONObject("claim_details").toString(), ClaimModel.class);
                        setClaimReportData();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Utility.dismissProgress();
                Log.i(TAG, "getClaimReportError = "+t.toString());
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == rlback.getId()) {
            onBackPressed();
        } else if (view.getId() == imgDateOfLoss.getId()) {
            Calendar calendar = Calendar.getInstance();
            DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                    txtDateOfLoss.setText(i2 + "-" + (i1 + 1) + "-" + i);
                }
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        } else if (view.getId() == imgInspectionDate.getId()) {
            Calendar calendar = Calendar.getInstance();
            DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                    txtInspectionDate.setText(i2 + "-" + (i1 + 1) + "-" + i);
                }
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        } else if (view.getId() == btnSave.getId()) {
            if (Utility.haveInternet(mContext, true)) {
                if (isValid()) {
                    if (btnSave.getText().toString().equals("Save"))
                        addReport();
                    else
                        updateReport();
                }
            }
        }
    }

    private void addReport() {
        Utility.showProgress(mContext);
        ApiClient.getClient().create(APIInterface.class).addClaimReport(PrefManager.getUserId(), getIntent().getStringExtra("claimId"), edtName.getText().toString(), edtCauseOfLoss.getText().toString(), txtDateOfLoss.getText().toString(), edtMortgage.getText().toString(), edtLaborMin.getText().toString(), edtCompany.getText().toString(), edtOfStories.getText().toString(), edtTypeOfConstruction.getText().toString(), edtRCI.getText().toString(), edtSingleFamily.getText().toString(), edtGaragesDetached.getText().toString(), edtTypeOfExteriorSiding.getText().toString(), edtInsuredPersonPresent.getText().toString(), txtInspectionDate.getText().toString(), edtOP.getText().toString(), edtDepreciation.getText().toString(), edtContents.getText().toString(), edtSalvage.getText().toString(), edtSubrogation.getText().toString()).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Utility.dismissProgress();
                Log.i(TAG, "addClaimReportRes = "+response.body());

                if (response.body() == null) {
                    Utility.errorDialog(mContext, getString(R.string.error_data_not_found));
                    return;
                }

                try {
                    JSONObject jsonObject = new JSONObject(response.body());
//                    Utility.errorDialog(mContext, jsonObject.getString("message"));
                    Toast.makeText(mContext, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                    if (jsonObject.getString("success").equals("success")) {
                        onBackPressed();
//                        getClaimReport();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Utility.dismissProgress();
                Log.i(TAG, "addClaimReportError = "+t.toString());
            }
        });
    }

    private void updateReport() {
        Utility.showProgress(mContext);
        ApiClient.getClient().create(APIInterface.class).updateClaimReport(claimModel.getId(), edtName.getText().toString(), edtCauseOfLoss.getText().toString(), txtDateOfLoss.getText().toString(), edtMortgage.getText().toString(), edtLaborMin.getText().toString(), edtCompany.getText().toString(), edtOfStories.getText().toString(), edtTypeOfConstruction.getText().toString(), edtRCI.getText().toString(), edtSingleFamily.getText().toString(), edtGaragesDetached.getText().toString(), edtTypeOfExteriorSiding.getText().toString(), edtInsuredPersonPresent.getText().toString(), txtInspectionDate.getText().toString(), edtOP.getText().toString(), edtDepreciation.getText().toString(), edtContents.getText().toString(), edtSalvage.getText().toString(), edtSubrogation.getText().toString()).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Utility.dismissProgress();
                Log.i(TAG, "updateClaimReportRes = "+response.body());

                if (response.body() == null) {
                    Utility.errorDialog(mContext, getString(R.string.error_data_not_found));
                    return;
                }

                try {
                    JSONObject jsonObject = new JSONObject(response.body());
//                    Utility.errorDialog(mContext, jsonObject.getString("message"));
                    Toast.makeText(mContext, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                    if (jsonObject.getString("success").equals("success")) {
                        onBackPressed();
//                        getClaimReport();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Utility.dismissProgress();
                Log.i(TAG, "updateClaimReportError = "+t.toString());
            }
        });
    }

    private boolean isValid() {
        boolean isValid = true;

        if (TextUtils.isEmpty(edtName.getText())) {
            edtName.setError(getString(R.string.error_field_required));
            edtName.requestFocus();
            isValid = false;
        } else if (TextUtils.isEmpty(edtCauseOfLoss.getText())) {
            edtCauseOfLoss.setError(getString(R.string.error_field_required));
            edtCauseOfLoss.requestFocus();
            isValid = false;
        } else if (TextUtils.isEmpty(txtDateOfLoss.getText())) {
            Toast.makeText(mContext, "Please select date of loss.", Toast.LENGTH_SHORT).show();
            isValid = false;
        } else if (TextUtils.isEmpty(edtMortgage.getText())) {
            edtMortgage.setError(getString(R.string.error_field_required));
            edtMortgage.requestFocus();
            isValid = false;
        } else if (TextUtils.isEmpty(edtLaborMin.getText())) {
            edtLaborMin.setError(getString(R.string.error_field_required));
            edtLaborMin.requestFocus();
            isValid = false;
        } else if (TextUtils.isEmpty(edtCompany.getText())) {
            edtCompany.setError(getString(R.string.error_field_required));
            edtCompany.requestFocus();
            isValid = false;
        } else if (TextUtils.isEmpty(edtOfStories.getText())) {
            edtOfStories.setError(getString(R.string.error_field_required));
            edtOfStories.requestFocus();
            isValid = false;
        } else if (TextUtils.isEmpty(edtTypeOfConstruction.getText())) {
            edtTypeOfConstruction.setError(getString(R.string.error_field_required));
            edtTypeOfConstruction.requestFocus();
            isValid = false;
        } else if (TextUtils.isEmpty(edtRCI.getText())) {
            edtRCI.setError(getString(R.string.error_field_required));
            edtRCI.requestFocus();
            isValid = false;
        } else if (TextUtils.isEmpty(edtSingleFamily.getText())) {
            edtSingleFamily.setError(getString(R.string.error_field_required));
            edtSingleFamily.requestFocus();
            isValid = false;
        } else if (TextUtils.isEmpty(edtGaragesDetached.getText())) {
            edtGaragesDetached.setError(getString(R.string.error_field_required));
            edtGaragesDetached.requestFocus();
            isValid = false;
        } else if (TextUtils.isEmpty(edtTypeOfExteriorSiding.getText())) {
            edtTypeOfExteriorSiding.setError(getString(R.string.error_field_required));
            edtTypeOfExteriorSiding.requestFocus();
            isValid = false;
        } else if (TextUtils.isEmpty(edtInsuredPersonPresent.getText())) {
            edtInsuredPersonPresent.setError(getString(R.string.error_field_required));
            edtInsuredPersonPresent.requestFocus();
            isValid = false;
        } else if (TextUtils.isEmpty(txtInspectionDate.getText())) {
            Toast.makeText(mContext, "Please select inspection date.", Toast.LENGTH_SHORT).show();
            isValid = false;
        } else if (TextUtils.isEmpty(edtOP.getText())) {
            edtOP.setError(getString(R.string.error_field_required));
            edtOP.requestFocus();
            isValid = false;
        } else if (TextUtils.isEmpty(edtDepreciation.getText())) {
            edtDepreciation.setError(getString(R.string.error_field_required));
            edtDepreciation.requestFocus();
            isValid = false;
        } else if (TextUtils.isEmpty(edtContents.getText())) {
            edtContents.setError(getString(R.string.error_field_required));
            edtContents.requestFocus();
            isValid = false;
        } else if (TextUtils.isEmpty(edtSalvage.getText())) {
            edtSalvage.setError(getString(R.string.error_field_required));
            edtSalvage.requestFocus();
            isValid = false;
        } else if (TextUtils.isEmpty(edtSubrogation.getText())) {
            edtSubrogation.setError(getString(R.string.error_field_required));
            edtSubrogation.requestFocus();
            isValid = false;
        }
        return isValid;
    }
}
