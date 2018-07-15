package com.example.lxx.hola;


import android.graphics.Bitmap;
import android.icu.text.IDNA;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;


import static org.litepal.LitePalApplication.getContext;


public class User  {
    public static String userId;
    public static String username;
    public static boolean isOK;
    public static String gender;
    public static String signature;
    public static File file = new File(getContext().getFilesDir(),"avatar.jpg");
    public static Bitmap avatar;
    public static JSONObject moments;
    public static JSONArray comments;
    public static int count;
    //刚注册完没有gender和signature,默认为空字符串,avatar默认为null
    public static String getUserId(){
        return userId;
    }
    public static void setUserId(String userId){
        User.userId = userId;
    }
    public static String getUsername(){
        return username;
    }
    public static void setUsername(String username){
        User.username = username;
    }
    public static boolean getIsOK(){
        return isOK;
    }
    public static void setIsOK(boolean isOK){
        User.isOK = isOK;
    }
    public static String getGender(){
        return gender;
    }
    public static void setGender(String gender){
        User.gender = gender;
    }
    public static String getSignature(){
        return signature;
    }
    public static void setSignature(String signature){
        User.signature = signature;
    }
    public static Bitmap getAvatar(){
        return avatar;
    }
    public static void setAvatar(Bitmap avatar){
        User.avatar = avatar;
    }
    public static JSONArray getComments(){
        return comments;
    }
    public static void setComments(JSONArray comments){
        User.comments = comments;
    }
    public static void setMoments(JSONObject moments){
        User.moments = moments;
    }
    public static JSONObject getMoments(){
        return moments;
    }

}
