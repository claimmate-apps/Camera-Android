package com.commonsware.cwac.camera.demo.retrofit;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface APIInterface {

    @FormUrlEncoded
    @POST("NewUser/AppSignup")
    Call<String> registerUser(@Field("name") String name, @Field("email") String email, @Field("mobile") String mobile, @Field("password") String password);

    @FormUrlEncoded
    @POST("Login")
    Call<String> login(@Field("email") String email, @Field("password") String password, @Field("device_token") String device_token, @Field("app_type") String app_type);

    @FormUrlEncoded
    @POST("Forget_password/Check_email")
    Call<String> forgotPassword(@Field("email") String email);

    @FormUrlEncoded
    @POST("Login/check_status")
    Call<String> checkStatus(@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("Logout")
    Call<String> logout(@Field("user_id") String user_id, @Field("app_type") String app_type);

    @GET("RegisterDevice.php")
    Call<String> registerDevice(@Query("ifsc") String ifsc, @Query("token") String token);

    @FormUrlEncoded
    @POST("Claim/Get_claim")
    Call<String> getClaimList(@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("Claim/add_claim")
    Call<String> addClaim(@Field("user_id") String user_id, @Field("claim_name") String claim_name);

    @FormUrlEncoded
    @POST("Claim/edit_claim")
    Call<String> updateClaim(@Field("id") String user_id, @Field("claim_name") String claim_name);

    @FormUrlEncoded
    @POST("Claim/delete")
    Call<String> deleteClaim(@Field("id") String user_id);

    @FormUrlEncoded
    @POST("Claim/Check_data")
    Call<String> checkClaimDes(@Field("user_id") String user_id, @Field("claim_id") String claim_id);

    @FormUrlEncoded
    @POST("Claim/add_claim_description")
    Call<String> addClaimDescription(@Field("user_id") String user_id, @Field("claim_id") String claim_id, @Field("user_name") String user_name, @Field("mr") String mr, @Field("claimant_name") String claimant_name, @Field("causesOfLoss") String causesOfLoss, @Field("dateLoss") String dateLoss, @Field("LaborMin") String LaborMin, @Field("LaborMinAdded") String LaborMinAdded, @Field("LaborMinRemoved") String LaborMinRemoved, @Field("AllCustom") String AllCustom, @Field("dateInspected") String dateInspected, @Field("timeInspected") String timeInspected);

    @FormUrlEncoded
    @POST("Claim_details")
    Call<String> addClaimReport(@Field("user_id") String user_id, @Field("claim_id") String claim_id, @Field("name") String name, @Field("cause_of_loss") String cause_of_loss, @Field("date_of_loss") String date_of_loss, @Field("mortgage") String mortgage, @Field("labor_min") String labor_min, @Field("company") String company, @Field("stories") String stories, @Field("type_of_construction") String type_of_construction, @Field("rci") String rci, @Field("single_family") String single_family, @Field("garages") String garages, @Field("exterior_siding") String exterior_siding, @Field("insured_person") String insured_person, @Field("inspection_date") String inspection_date, @Field("op") String op, @Field("depreciation") String depreciation, @Field("contents") String contents, @Field("salvage") String salvage, @Field("subrogation") String subrogation);

    @FormUrlEncoded
    @POST("Claim_details/update")
    Call<String> updateClaimReport(@Field("id") String id, @Field("name") String name, @Field("cause_of_loss") String cause_of_loss, @Field("date_of_loss") String date_of_loss, @Field("mortgage") String mortgage, @Field("labor_min") String labor_min, @Field("company") String company, @Field("stories") String stories, @Field("type_of_construction") String type_of_construction, @Field("rci") String rci, @Field("single_family") String single_family, @Field("garages") String garages, @Field("exterior_siding") String exterior_siding, @Field("insured_person") String insured_person, @Field("inspection_date") String inspection_date, @Field("op") String op, @Field("depreciation") String depreciation, @Field("contents") String contents, @Field("salvage") String salvage, @Field("subrogation") String subrogation);

    @FormUrlEncoded
    @POST("Claim_details/Get_claim_details")
    Call<String> getClaimReport(@Field("user_id") String user_id, @Field("claim_id") String claim_id);

    @FormUrlEncoded
    @POST("report-app/Risk_macro")
    Call<String> addRiskMacroDes(@Field("user_id") String user_id, @Field("claim_id") String claim_id, @Field("story") String story, @Field("dwl_first") String dwl_first, @Field("dwl_first_custom") String dwl_first_custom, @Field("dwl_second") String dwl_second, @Field("dwl_second_custom") String dwl_second_custom, @Field("dwl_third") String dwl_third, @Field("dwl_third_custom") String dwl_third_custom, @Field("dwl_fourth") String dwl_fourth, @Field("dwl_fourth_custom") String dwl_fourth_custom, @Field("dwl_fifth") String dwl_fifth, @Field("dwl_fifth_custom") String dwl_fifth_custom);

    @FormUrlEncoded
    @POST("report-app/Roof/Add_Roof_Layer")
    Call<String> addRoofLayer(@Field("user_id") String user_id, @Field("claim_id") String claim_id, @Field("no_layer") String no_layer, @Field("layer_type") String layer_type);

    @FormUrlEncoded
    @POST("report-app/Roof/Add_Roof_Pitch")
    Call<String> addRoofPitch(@Field("user_id") String user_id, @Field("claim_id") String claim_id, @Field("pitch") String pitch, @Field("slope") String slope);

    @FormUrlEncoded
    @POST("report-app/Roof/Add_Roof_Single")
    Call<String> addRoofShingle(@Field("user_id") String user_id, @Field("claim_id") String claim_id, @Field("year") String year, @Field("tab") String tab);

    @FormUrlEncoded
    @POST("scope-sheet/Interior")
    Call<String> addInteriorArea(@Field("user_id") String user_id, @Field("claim_id") String claim_id, @Field("areatype") String areaType, @Field("matarial") String matarial, @Field("insulation") String insulation, @Field("quantity") String quantity);

    @FormUrlEncoded
    @POST("Roommacro")
    Call<String> addRoomMacro(@FieldMap Map<String, String> param);

    @FormUrlEncoded
    @POST("scope-sheet/Roof_main")
    Call<String> addRoof(@Field("user_id") String user_id, @Field("claim_id") String claim_id, @Field("matarial") String matarial, @Field("quantity") String quantity, @Field("slope") String slope, @Field("damage") String damage);

    @FormUrlEncoded
    @POST("scope-sheet/Elevation_main")
    Call<String> addElevation(@Field("user_id") String user_id, @Field("claim_id") String claim_id, @Field("matarial") String matarial, @Field("quantity") String quantity, @Field("elevation") String elevation, @Field("damage") String damage);

    @FormUrlEncoded
    @POST("Synchronization")
    Call<String> synchronization(@Field("parm") String parm);
}
