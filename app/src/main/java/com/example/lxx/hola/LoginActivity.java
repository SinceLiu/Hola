package com.example.lxx.hola;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.Call;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class LoginActivity extends AppCompatActivity {
    private Button mLogin;
    private Button mRegister;
    private EditText mPhone;
    private EditText mPassword;
    //获取手机号，密码
    private String phone;
    private String password;

    //用于注册成功时销毁登录活动
    public static LoginActivity instance = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        instance = this;
        mLogin = (Button) findViewById(R.id.login);
        mRegister = (Button) findViewById(R.id.register);
        mPhone = (EditText)findViewById(R.id.phone);
        mPassword = (EditText) findViewById(R.id.password);
        //注册
        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        //登录
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phone = mPhone.getText().toString();
                password = mPassword.getText().toString();
                Log.d("获取登录手机=",phone);
                Log.d("获取登录密码=",password);
                if(phone.length() == 0){
                    Toast.makeText(LoginActivity.this, "手机号码不能为空", Toast.LENGTH_SHORT).show();
                }else if(password.length() == 0){
                    Toast.makeText(LoginActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                }
                else {
                    sendRequestWithOkHttp();
                }
            }
        });
    }

    //使用OKhttp发送网络请求
    private void sendRequestWithOkHttp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("phone",phone)
                            .add("password",password)
                            .build();
                    Request request = new Request.Builder()
                            // 指定访问的服务器地址是电脑本机
                            // .url("http://10.0.2.2/get_data.json")
                            .url("http://112.74.125.217:3000/login")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    //parseJSONWithGSON(responseData);
                    parseJSONWithJSONObject(responseData);
                    isToMain();
//                    parseXMLWithSAX(responseData);
//                    parseXMLWithPull(responseData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    //使用JSONOBJECT解析返回数据，创建user类并初始化变量
    private void parseJSONWithJSONObject(String jsonData) {
        try {
//            JSONArray jsonArray = new JSONArray(jsonData);
//            for (int i = 0; i < jsonArray.length(); i++) {
//                JSONObject jsonObject = jsonArray.getJSONObject(i);
            System.out.println("jsonData is " + jsonData);
            JSONObject jsonObject=new JSONObject(jsonData);
            //验证失败返回isOK，成功返回userId和username
            boolean isOK = true;
            Log.d("输出服务器返回的数据=",jsonData);
            if(jsonObject.has("isOK")){
                isOK = false;
            }
            User.setIsOK(isOK);
            //不确定会不会返回的5个key,userId,username,gender,signature,avatar，需要添加判断
            String userId ;
            String username;
            String gender;
            String signature;
            Bitmap avatar;
            if(jsonObject.has("userId")){
                userId = jsonObject.getString("userId");
                User.setUserId(userId);
            }
            if(jsonObject.has("username")){
                username = jsonObject.getString("username");
                User.setUsername(username);
            }
            if (jsonObject.has("gender")){
                gender = jsonObject.getString("gender");
                User.setGender(gender);
            }
            if (jsonObject.has("signature")){
                signature = jsonObject.getString("signature");
                User.setSignature(signature);
                System.out.println("jsonObject.has(\"signature\"");
            }
            //from 林伟毅   9.6
            if (jsonObject.has("avatar")){
                System.out.println("jsonObject.has(\"avatar\"");
                try{
                    URL url = new URL("http://112.74.125.217:3000/" +
                            jsonObject.getString("avatar"));//Bitmap
                    avatar = getBitmapFromURL(url.toString());
                    User.setAvatar(avatar);

                    //测试url是否成功被解析并转化为bitmap
                    if (avatar == null){
                        System.out.println("User.avatar is null");
                    }else {
                        System.out.println("Avatar is setted." + "URL is" + url);
                    }
                }catch (Exception e){
                    System.out.println("错误的头像网址");
                }
            }
//                Log.d("测试user isok=",String.valueOf(user.getIsOK()));
//                Log.d("测试user username=", User.username);
//                Log.d("测试user userid=", User.userId);
//                Log.d("测试userphone", phone);
//                Log.d("测试userpassword", password);
//                Log.d("MainActivity", "id is " + id);
//                Log.d("MainActivity", "name is " + name);
//                Log.d("MainActivity", "version is " + version);
            //}
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

// 9.6 from 林伟毅
    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            if (myBitmap ==null)
            {
                System.out.println("myBitmap == null");
            }
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            System.out.println("IOException!!!");
            return null;
        }
    }


    //判断登陆是否成功,如登录成功跳转主界面，否则发送toast提示
    private void isLoginSucceed(){
        if (User.getIsOK()) {
            toMain();
        }else{
            Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
        }
    }
//登录成功，跳转到主界面的intent
    private void toMain(){
        Toast.makeText(LoginActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    //切回UI主线程
    private void isToMain(){
        final boolean isOK = User.getIsOK();
        Log.d("切回主线程测试isOK=", String.valueOf(isOK));
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                isLoginSucceed();
            }
        });
    }


}
