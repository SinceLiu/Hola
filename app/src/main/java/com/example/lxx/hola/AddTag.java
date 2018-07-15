package com.example.lxx.hola;

import android.os.AsyncTask;

import com.example.lxx.hola.model.Tag;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by hasee on 2017/9/4.
        */
public class AddTag extends AsyncTask {
    @Override
    protected Object doInBackground(Object[] params) {
        String url = "http://112.74.125.217:3000/users/"+User.getUserId()+"/moments";
        OkHttpClient mOkHttpClient = new OkHttpClient();
//        File sdcache = getExternalCacheDir();
//        OkHttpClient.Builder builder = new OkHttpClient.Builder()
//                .connectTimeout(150, TimeUnit.SECONDS)
//                .writeTimeout(200, TimeUnit.SECONDS)
//                .readTimeout(200, TimeUnit.SECONDS);
//        OkHttpClient mOkHttpClient=builder.build();
        MediaType type = MediaType.parse("application/json;charset=utf-8");

        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("username", Tag.username)
                .addFormDataPart("tags", Tag.tags)
                .addFormDataPart("text", Tag.text)
                .addFormDataPart("longitude", Tag.longitude)
                .addFormDataPart("latitude",Tag.latitude)
                .addFormDataPart("published",Tag.published)
//                .addFormDataPart("tag", Tag.tag)
                .addFormDataPart("photo", "image",
                        RequestBody.create(MediaType.parse("image/jpg"),
                                Tag.file))
                .build();
        RequestBody.create(type, body.toString());
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        Call call = mOkHttpClient.newCall(request);
        try {
            Response response = call.execute();
            System.out.println(body.toString());
            System.out.println(request.toString() + "------");
            System.out.println(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}