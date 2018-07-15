package com.example.lxx.hola.HttpUtil;

import android.text.TextUtils;
import android.util.Log;

import com.amap.api.maps.model.LatLng;
import com.example.lxx.hola.RemarkerInfo;
import com.example.lxx.hola.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;





public class HttpUtil {




    public static void sendOkHttpRequest(String address, LatLng latLng, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        double longitude = latLng.longitude;
        double latitude = latLng.latitude;
        String lon = Double.toString(longitude);
        String lat = Double.toString(latitude);
        RequestBody requestBody = new FormBody.Builder()
                .add("tags","食物,糗事,动物,人物,风景,体验")
                .add("range","1000")
                .add("offset","0")
                .add("limit","1000")
                .add("longitude",lon)
                .add("latitude",lat)
                .build();
        Request request = new Request.Builder().url(address).post(requestBody).build();
        client.newCall(request).enqueue(callback);
    }

    public static List<RemarkerInfo> handleRemarkerInfoResponse(String response) {
        if (!TextUtils.isEmpty(response)) {
            try {
                Log.d("开始","解析" + response);
                List<RemarkerInfo> remarkerInfoList = new ArrayList<>();

                JSONObject allRemarker = new JSONObject(response);
                User.setMoments(allRemarker);
                JSONArray array = allRemarker.getJSONArray("results");

                for (int i=0; i < array.length(); i++) {

                    JSONObject remarkerObject = array.getJSONObject(i);

                    RemarkerInfo remarker = new RemarkerInfo();

                    remarker.setMomentId(remarkerObject.getString("momentId"));
                    JSONObject location = remarkerObject.getJSONObject("location");
                    remarker.setRemarkerLon(location.getDouble("longitude"));
                    remarker.setRemarkerLat(location.getDouble("latitude"));
//                    RemarkerInfo.setImageId(remarkerObject.getString("photo"));
                    remarkerInfoList.add(remarker);

                    Log.d("获取解析的数据","momentId = " + remarker.getMomentId() + "longitude = " + remarker.getRemarkerLon() + "latitude = " + remarker.getRemarkerLat());

                }
                return remarkerInfoList;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Log.d("response","kong");
        return null;
    }
}
