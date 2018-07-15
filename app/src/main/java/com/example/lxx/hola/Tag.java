package com.example.lxx.hola.model;

import android.graphics.Bitmap;

import java.io.File;


public class Tag {
    public static String username;
    public static String tags;
    public static String text;
    public static String longitude;
    public static String latitude;
    public static String published;
    public static File file=new File("img.jpg");
    public static Bitmap img;
    public static String getUsername(){
        return username;
    }
    public static void setUsername(String username){
       Tag.username= username;
    }
    public static String getTag(){
        return tags;
    }
    public static void setTag(String tag){
        Tag.tags = tags;
    }
    public static String getText(){
        return text;
    }
    public static void setText(String text){
        Tag.text = text;
    }
    public static String getPublished(){
        return published;
    }
    public static void setPublished(String published){
        Tag.published = published;
    }
    public static String getLongitude(){
        return longitude;
    }
    public static void setLongitude(String longitude){
        Tag.longitude=longitude;
    }
    public static String  getLatitude(){
        return latitude;
    }
    public static void setLatitude( String  latitude){
        Tag.latitude=latitude;
    }
    public static Bitmap getImg(){
        return img;
    }
    public static void seImg(Bitmap img){
        Tag.img = img;
    }
}
