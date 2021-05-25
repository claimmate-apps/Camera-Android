package com.commonsware.cwac.camera.demo.retrofit;

public interface ICallback {

    public enum RESULT {SUCCESS, FAILURE};
    public void onCompletion(RESULT result, Object resultParam);

}
