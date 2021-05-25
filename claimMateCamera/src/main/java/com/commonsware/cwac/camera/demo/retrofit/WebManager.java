package com.commonsware.cwac.camera.demo.retrofit;

import java.util.concurrent.TimeUnit;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;


public class WebManager {

    public enum METHOD{GET, POST};


    private static final int TIMEOUT = 60;     // seconds


    public static void call(String url, METHOD method, RequestBody body, final Callback callback) {

        url = ApiManager.API + url;

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(TIMEOUT, TimeUnit.SECONDS);
        builder.readTimeout(TIMEOUT, TimeUnit.SECONDS);
        builder.writeTimeout(TIMEOUT, TimeUnit.SECONDS);
        OkHttpClient client = builder.build();

        Request request = null;

        switch (method) {

            case GET:
                request = new Request.Builder().url(url).build();
                break;

            case POST:
                request = new Request.Builder().url(url).post(body).build();
                break;
        }


        client.newCall(request).enqueue(callback);

    }

    // GET
    public static void call(String url, final Callback callback) {
        call(url, METHOD.GET, null, callback);
    }

}
