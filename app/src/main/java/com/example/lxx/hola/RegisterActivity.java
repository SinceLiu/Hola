package com.example.lxx.hola;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity {

    private EditText mRegisterUsername;
    private EditText mRegisterPhone;
    private EditText mRegisterPassword;
    private Button mRegisterRegister;
    private String username;
    private String phone;
    private String password;
    private EditText mConfirmPassword;
    private String confirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mRegisterUsername = (EditText)findViewById(R.id.name);
        mRegisterPhone = (EditText) findViewById(R.id.phone);
        mRegisterPassword = (EditText) findViewById(R.id.password);
        mRegisterRegister = (Button) findViewById(R.id.register);
        mConfirmPassword = (EditText)findViewById(R.id.confirm_password);

        //注册页面的登录按钮
//        Button mRegisterLogin = (Button)findViewById(R.id.login);
//        mRegisterLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
//                startActivity(intent);
//            }
//        });

        mRegisterRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = mRegisterUsername.getText().toString();
                phone = mRegisterPhone.getText().toString();
                password = mRegisterPassword.getText().toString();
                confirmPassword = mConfirmPassword.getText().toString();
                Log.d("测试username", username);
                Log.d("测试userphone", phone);
                Log.d("测试userpassword", password);
                if (username.length()==0){
                    Toast.makeText(RegisterActivity.this,"用户名不能为空",Toast.LENGTH_SHORT).show();
                }else if (phone.length() != 11){
                    Toast.makeText(RegisterActivity.this,"请输入正确的手机号码",Toast.LENGTH_SHORT).show();
                }else if (password.length() == 0){
                    Toast.makeText(RegisterActivity.this,"密码不能为空",Toast.LENGTH_SHORT).show();
                }else if (password.length() < 6 || password.length() >16){
                    Toast.makeText(RegisterActivity.this,"请输入6-16位密码",Toast.LENGTH_SHORT).show();
                }else if(! password.equals(confirmPassword)){
                    Toast.makeText(RegisterActivity.this,"两次输入的密码不一致",Toast.LENGTH_SHORT).show();
                }
                else {
                    sendRequestWithOkHttp();
                }

//                Log.d("onclick测试user isok=",String.valueOf(User.isOK));
//                Log.d("onclick测试user username=", user.getUsername());
//                Log.d("onclick测试user userid=", user.getUserId());
//                Log.d("测试username", username);
//                Log.d("测试userphone", phone);
//                Log.d("测试userpassword", password);


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
                            .add("username",username)
                            .build();
                    Request request = new Request.Builder()
                            // 指定访问的服务器地址是电脑本机
                            // .url("http://10.0.2.2/get_data.json")
//                            .url("POSThttps://private-402ae-hola16.apiary-mock.com/register")
                            .url("http://112.74.125.217:3000/register")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    //parseJSONWithGSON(responseData);
                    Log.d("输出服务器返回的数据",responseData);
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
    //使用JSONOBJECT解析返回数据，初始化User
    private void parseJSONWithJSONObject(String jsonData) {
        try {
//            JSONArray jsonArray = new JSONArray(jsonData);
//            for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = new JSONObject(jsonData);
//                boolean isOK = Boolean.parseBoolean(jsonObject.getString("isOK"));
            boolean isOK = true;
            if(jsonObject.has("isOK")){
                isOK = false;
            }else{
                String userId = jsonObject.getString("userId");
                String username = jsonObject.getString("username");
                User.setUserId(userId);
                User.setUsername(username);
                User.setIsOK(isOK);
                Log.d("json是否初始化user,userid=",User.getUserId());
//                Log.d("MainActivity", "id is " + id);
//                Log.d("MainActivity", "name is " + name);
//                Log.d("MainActivity", "version is " + version);
            }

            //}
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //跳转主界面并销毁注册活动和登录活动
    private void toMain(){
        LoginActivity.instance.finish();
        Toast.makeText(RegisterActivity.this,"注册成功！",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
    //切换回主线程
    private void isToMain() {
        final boolean isOK = User.getIsOK();
        if (isOK) {
            final String userid = User.getUserId();
            final String username = User.getUsername();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d("切回主线程测试user userid=", userid);
                    Log.d("切回主线程测试user username=", username);

                    toMain();

                }

            });
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(RegisterActivity.this, "该手机号码已经被注册！", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}