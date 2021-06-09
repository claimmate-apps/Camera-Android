package com.commonsware.cwac.camera.demo.common;

import com.commonsware.cwac.camera.demo.model.UserModel;

public class Commons {

    public static UserModel thisUser = new UserModel();

    public static final String BASE_URL = "http://ladderassist.pro/";
    public static final String GET_COMPANIES = BASE_URL + "api/company/get_all";
    public static final String GET_CLAIM_ID = BASE_URL + "api/claim/get_claim_id";



    public static final String KEY_LOGOUT = "logout";
    public static final String PREFKEY_USEREMAIL = "user_email";
    public static final String PREFKEY_USERPWD = "user_pwd";

}
