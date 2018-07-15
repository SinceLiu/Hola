package com.example.lxx.hola.HttpUtil;


import okhttp3.OkHttpClient;
import okhttp3.Request;

    //Get
    //       Request request = new Request.Builder().url(address).build();
    //      HttpUtil.sendOkHttpRequest(request,new okhttp3.Callback(){
    //      @Override
    //      public void onResponse(Call call, Response response) throws IOException{
    //          //得到服务器返回的具体内容
    //        }
    //      @Override
    //      public void onFailure(Call call,IOException e){
    //        //在这里对异常情况进行处理
    //        }
    //        });


    //Post
    //        RequestBody requestBody = new FormBody.Builder()
    //                .add("username","admin")
    //                .add("password","123456")
    //                .build();
    //        Request request = new Request.Builder().url(address).post(requestBody).build();

public class HttpUtil2 {
    public static void sendOkHttpRequest(Request request,okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(callback);
    }
}
