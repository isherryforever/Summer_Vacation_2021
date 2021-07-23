package com.upc.smartfamily;

import android.content.Context;
import android.util.Log;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Api {


    static OkHttpClient client = new OkHttpClient();
    private final static String Url = "http://www.glp666.ltd/main.html";
    private static String requestUrl;
    private static HashMap<String, Object> mParams;
    public static Context AppContext;
    public static Api api = new Api();
    public static String  cookie;

    static RequestBody body;

    static {
        body = new FormBody.Builder()
                .add("username", "123")
                .add("password", "123456")
                .build();
    }

    public static void postRequest(final myCallback callback) {
        Request request = new Request.Builder()
                .url(Url)
                .post(body)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e("Failure", e.getMessage());
                Log.e("Failure", "COOKIE调用失败");
                callback.onFailure(e);
            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                cookie = response.header("Set-Cookie");
                callback.onSuccess(cookie==null?"":cookie);
                Log.e("Failure", cookie==null?"COOKIE空的":cookie);
            }
        });
    }



    public static void getRequest(final myCallback callback) {

        Request request = new Request.Builder()
//               .url(Url+"mychart")
                .url("http://www.glp666.ltd/mychart")
                //.addHeader("Cookie",cookie==null?"":cookie)
                .get()
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e("onFailure", e.getMessage());
                callback.onFailure(e);
            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String result = Objects.requireNonNull(response.body()).string();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                callback.onSuccess(result);
            }
        });
    }



}