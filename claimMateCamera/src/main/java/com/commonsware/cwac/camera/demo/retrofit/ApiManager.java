package com.commonsware.cwac.camera.demo.retrofit;

import android.util.Log;

import com.commonsware.cwac.camera.demo.common.Commons;
import com.commonsware.cwac.camera.demo.model.ClaimModel;
import com.commonsware.cwac.camera.demo.model.UserModel;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;


public class ApiManager {

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static final MediaType JPEG = MediaType.parse("image/jpeg");
    public static final MediaType PNG = MediaType.parse("image/png");
    public static final MediaType VIDEO = MediaType.parse("video/mp4");

    public static final String HOST = "http://ladderassist.pro/";      // production
    public static final String API = HOST + "api/";
    public static final String AUTH = "auth/";
    public static final String CLAIM = "claim/";

    private static final String SIGNUP = "signup_inspector";
    private static final String LOGIN = "login_inspector";
    private static final String FORGOT = "forgot_inspector";
    private static final String RESET_PASSWORD = "reset_password_inspector";

    private static final String UPDATE_PROFILE = "update_profile_inspector";
    private static final String GET_CLAIM = "get_claim";
    private static final String UPDATE_CLAIM = "update_claim";
    private static final String ADD_CLAIM = "add_claim_by_inspector";

    private static final int RESULT_OK = 200;


    public static void signUp(String firstname, String lastname, String email, String password, String company, ICallback callback) {

        RequestBody body = new FormBody.Builder()
                .add(PARAMS.FIRST_NAME, firstname)
                .add(PARAMS.LAST_NAME, lastname)
                .add(PARAMS.EMAIL, email)
                .add(PARAMS.PASSWORD, password)
                .add(PARAMS.COMPANY_ID, company)
                .build();

        WebManager.call(AUTH + SIGNUP, WebManager.METHOD.POST, body, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

                callback.onCompletion(ICallback.RESULT.FAILURE, null);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try {
                    JsonObject json = new JsonParser().parse(response.body().string()).getAsJsonObject();
                    int resultCode = json.get(PARAMS.RESULT_CODE).getAsInt();

                    if (resultCode == RESULT_OK) {

                        int id = json.get(PARAMS.USER_ID).getAsInt();

                        callback.onCompletion(ICallback.RESULT.SUCCESS, id);

                    } else {
                        callback.onCompletion(ICallback.RESULT.FAILURE, resultCode);
                    }
                }
                catch (Exception ex) {
                    callback.onCompletion(ICallback.RESULT.FAILURE, null);
                }

            }
        });

    }


    public static void login(String email, String password, final ICallback callback){

        RequestBody body = new FormBody.Builder()
                .add(PARAMS.EMAIL, email)
                .add(PARAMS.PASSWORD, password)
                .build();

        WebManager.call(AUTH + LOGIN, WebManager.METHOD.POST, body, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                callback.onCompletion(ICallback.RESULT.FAILURE, null);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                try {

                    JsonObject json = new JsonParser().parse(response.body().string()).getAsJsonObject();

                    int resultCode = json.get(PARAMS.RESULT_CODE).getAsInt();

                    if (resultCode == RESULT_OK){

                        callback.onCompletion(ICallback.RESULT.SUCCESS, json);
                    }
                    else {
                        callback.onCompletion(ICallback.RESULT.FAILURE, resultCode);
                    }
                } catch (Exception e) {
                    callback.onCompletion(ICallback.RESULT.FAILURE, null);
                }
            }
        });
    }

    public static void forgotPassword(String email, final ICallback callback){

        RequestBody body = new FormBody.Builder()
                .add(PARAMS.EMAIL, email)
                .build();

        WebManager.call(AUTH + FORGOT, WebManager.METHOD.POST, body, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                callback.onCompletion(ICallback.RESULT.FAILURE, null);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                try {

                    JsonObject json = new JsonParser().parse(response.body().string()).getAsJsonObject();

                    int resultCode = json.get(PARAMS.RESULT_CODE).getAsInt();

                    if (resultCode == RESULT_OK){

                        String pincode = json.get(PARAMS.PINCODE).getAsString();
                        int user_id = json.get(PARAMS.USER_ID).getAsInt();
                        UserModel user = new UserModel();
                        user.setId(user_id);
                        Commons.thisUser = user;
                        callback.onCompletion(ICallback.RESULT.SUCCESS, pincode);
                    }
                    else {
                        callback.onCompletion(ICallback.RESULT.FAILURE, resultCode);
                    }
                } catch (Exception e) {
                    callback.onCompletion(ICallback.RESULT.FAILURE, null);
                }
            }
        });
    }

    public static void resetPassword(int user_id, String password, final ICallback callback){

        RequestBody body = new FormBody.Builder()
                .add(PARAMS.USER_ID, String.valueOf(user_id))
                .add(PARAMS.PASSWORD, password)
                .build();

        WebManager.call(AUTH + RESET_PASSWORD, WebManager.METHOD.POST, body, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                callback.onCompletion(ICallback.RESULT.FAILURE, null);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                try {

                    JsonObject json = new JsonParser().parse(response.body().string()).getAsJsonObject();

                    int resultCode = json.get(PARAMS.RESULT_CODE).getAsInt();

                    if (resultCode == RESULT_OK){

                        callback.onCompletion(ICallback.RESULT.SUCCESS, resultCode);
                    }
                    else {
                        callback.onCompletion(ICallback.RESULT.FAILURE, resultCode);
                    }
                } catch (Exception e) {
                    callback.onCompletion(ICallback.RESULT.FAILURE, null);
                }
            }
        });
    }

    public static void getRequest(String inspector_id, ICallback callback){

        RequestBody body = new FormBody.Builder()
                .add(PARAMS.INSPECTOR_ID, String.valueOf(inspector_id))
                .build();

        WebManager.call(CLAIM + GET_CLAIM, WebManager.METHOD.POST, body, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                callback.onCompletion(ICallback.RESULT.FAILURE, null);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                try {

                    JsonObject json = new JsonParser().parse(response.body().string()).getAsJsonObject();

                    int resultCode = json.get(PARAMS.RESULT_CODE).getAsInt();

                    if (resultCode == RESULT_OK){

                        ArrayList<ClaimModel> claimArray = new ArrayList<>();
                        JsonArray jsonClaimArray = json.get(PARAMS.CLAIM_LIST).getAsJsonArray();

                        Log.d("Claim_list===>", jsonClaimArray.toString());

                        for (int i = 0; i < jsonClaimArray.size(); i++){

                            JsonObject jsonClaim = (JsonObject) jsonClaimArray.get(i).getAsJsonObject();

                            ClaimModel claim = new ClaimModel();

                            claim.setId(jsonClaim.get(PARAMS.ID).getAsString());
                            claim.setName(jsonClaim.get(PARAMS.INSURED_INFO).getAsString());

                            String status = jsonClaim.get(PARAMS.STATUS).getAsString();

                            if (status.equals("0") || status.equals("1") || status.equals("3"))
                                claimArray.add(claim);
                        }

                        callback.onCompletion(ICallback.RESULT.SUCCESS, claimArray);
                    }
                    else {
                        callback.onCompletion(ICallback.RESULT.FAILURE, resultCode);
                    }
                } catch (Exception e) {
                    callback.onCompletion(ICallback.RESULT.FAILURE, null);
                }
            }
        });
    }

    /*public static void updateProfile(int user_id, String first_name, String last_name, String phone, String work_days, String inspector_id, String description,
                                     File photo, ICallback callback){


        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart(PARAMS.USER_ID, String.valueOf(user_id))
                .addFormDataPart(PARAMS.FIRST_NAME, first_name)
                .addFormDataPart(PARAMS.LAST_NAME, last_name)
                .addFormDataPart(PARAMS.PHONE, phone)
                .addFormDataPart(PARAMS.WORK_DAYS, work_days)
                .addFormDataPart(PARAMS.INSPECTOR_ID, inspector_id)
                .addFormDataPart(PARAMS.DESCRIPTION, description)
                .addFormDataPart(PARAMS.PHOTO, photo.getName(), RequestBody.create(photo, MediaType.parse("image/*")))
                .build();

        WebManager.call(AUTH + UPDATE_PROFILE, WebManager.METHOD.POST, body, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                callback.onCompletion(ICallback.RESULT.FAILURE, null);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                try {

                    JsonObject json = new JsonParser().parse(response.body().string()).getAsJsonObject();
                    int resultCode = json.get(PARAMS.RESULT_CODE).getAsInt();

                    if (resultCode == RESULT_OK){

                        String photoURL = json.get(PARAMS.PHOTO_URL).getAsString();

                        callback.onCompletion(ICallback.RESULT.SUCCESS, photoURL);
                    }
                }
                catch (Exception e){
                    callback.onCompletion(ICallback.RESULT.FAILURE, null);
                }
            }
        });
    }




    */

    public static class PARAMS {

        private static final String TOKEN = "token";
        public static final String PHONE_NO = "phone_no";
        public static final String PASSWORD = "password";
        public static final String USER_DATA = "user_data";
        public static final String ID = "id";
        public static final String USER_ID = "user_id";
        public static final String NAME = "name";
        public static final String FIRST_NAME = "first_name";
        public static final String LAST_NAME = "last_name";
        public static final String EMAIL = "email";
        public static final String ADDRESS = "address";
        public static final String PHOTO_URL = "photo_url";
        public static final String PHOTO = "photo";
        public static final String CLAIM_LIST = "claim_list";
        public static final String CLAIM_ID = "claim_id";
        public static final String ADJUSTER_ID = "adjuster_id";
        public static final String STATE = "state";
        public static final String CITY = "city";
        public static final String ZIPCODE = "zip_code";
        public static final String COUNTRY = "country";
        public static final String PHONE = "phone";
        public static final String CLAIM_NUMBER = "claim_number";
        public static final String INSURANCE_COMPANY = "insurance_company";
        public static final String INSURED_INFO = "insured_info";
        public static final String DATE = "date";
        public static final String TIME = "time";
        public static final String SERVICE_TYPE = "service_type";
        public static final String DAMAGE_TYPE = "damage_type";
        public static final String DAMAGE_REPORT = "damage_report";
        public static final String PITCH = "pitch";
        public static final String HEIGHT = "height";
        public static final String NOTE = "note";
        public static final String FIRM_ONLY = "firm_only";
        public static final String FIRM_FIRST = "firm_first";
        public static final String FIRM_LAST = "firm_last";
        public static final String FIRM_DNI = "firm_dni";
        public static final String FIRM_DND = "firm_dni";
        public static final String RESULT_PHOTO = "result_photo";
        public static final String RESULT_MEASUREMENT = "result_measurement";
        public static final String RESULT_SKETCH = "result_sketch";
        public static final String RESULT_REPORT = "result_report";
        public static final String CODE = "code";
        public static final String RESULT_CODE = "result_code";
        public static final String LATITUDE = "latitude";
        public static final String LONGITUDE = "longitude";
        public static final String WORK_DAYS = "work_days";
        public static final String DESCRIPTION = "description";
        public static final String INSPECTOR_ID = "inspector_id";
        public static final String COMPANY_ID = "company_id";
        public static final String COMPLETE_INSURANCE = "complete_insurance";
        public static final String COMPLETE_COMPANY = "complete_company";
        public static final String COMPLETE_ADJUSTER = "complete_adjuster";
        public static final String PINCODE = "pincode";
        public static final String STATUS = "accept_status";

    }
}


