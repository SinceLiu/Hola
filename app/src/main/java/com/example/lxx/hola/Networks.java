package com.example.lxx.hola;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.Normalizer;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by echo on 2017/9/4.
 */


public class Networks extends AsyncTask {
    @Override
    protected Object doInBackground(Object[] params) {
        String url = "http://112.74.125.217:3000/users/"+User.userId;
        OkHttpClient okHttpClient = new OkHttpClient();
        MediaType type = MediaType.parse("application/json;charset=utf-8");

        RequestBody body = null;

        if (User.username != null && User.signature != null && User.gender != null) {
            body = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("username", User.username)
                    .addFormDataPart("gender", User.gender)
                    .addFormDataPart("signature", User.signature)
                    .addFormDataPart("avatar", "avatar.jpg",
                            RequestBody.create(MediaType.parse("image/jpg"),
                                    User.file))
                    .build();
        }
        else{
            body = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("username", "Crustal Welch")
                    .addFormDataPart("gender", "women")
                    .addFormDataPart("signature", "what a pity")
                    .addFormDataPart("avatar", "avatar.jpg",
                            RequestBody.create(MediaType.parse("image/jpg"),
                                    User.file))
                    .build();
        }

        RequestBody.create(type,body.toString());
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        Call call = okHttpClient.newCall(request);
        try {
            Response response = call.execute();
            String jsonString = response.body().string();
            System.out.println(body.toString());
            System.out.println(request.toString()+"------");
            System.out.println(jsonString);
            JSONObject json = new JSONObject(jsonString);
            if (json.has("avatar")){
                URL Url = new URL("http://112.74.125.217:3000/users/"+
                        json.getString("avatar"));
                System.out.println("URL is " + Url);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }



}
