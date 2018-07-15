package com.example.lxx.hola;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lxx.hola.HttpUtil.HttpUtil2;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;


public class ImageActivity extends AppCompatActivity{
    ImageView image;
    ImageView icon;
    TextView name;
    TextView count_comment;
    TextView description;
    TextView time;
    EditText comment;
    ImageButton btn_comment;
    Button sendComment;

    private Bitmap loadIcon;
    private Bitmap photo;
    private String latitude;
    private String longitude;
    private String lat;
    private String lon;
    private String momentId;
    private String photoUrl;    //图片路径photo:
    private String iconUrl;   //头像路径avatar:
    private String username;  //用户名username:
    private int commentCount;  //评论数目commentCount:
    private String talk;  //说说内容text:
    private String published;      //发布时间published:
    private JSONArray comments;   //评论数组，传递给commentActivity; coments:[]

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("ImamgeActivity","onCreate.");
        setContentView(R.layout.activity_image);
        Intent intent = getIntent();
        latitude = intent.getStringExtra("latitude");
        longitude = intent.getStringExtra("longitude");
        momentId = intent.getStringExtra("momentId");
        photo = intent.getParcelableExtra("photoBitmap");
        ImageButton btn_return = (ImageButton) findViewById(R.id.btn_return);
        image = (ImageView) findViewById(R.id.image);

        //点击返回按钮
        btn_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ImageActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("hahahaha","onStart");

        //从服务器获取数据
        getDataFromServer();

        //初始化图片
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                image.setImageBitmap(photo);
            }
        });


        image.setOnClickListener(new View.OnClickListener() {//点击图片弹出评论弹窗
            @Override
            public void onClick(View v) {
                //构建一个popupWindow的布局
                View popupView = ImageActivity.this.getLayoutInflater().inflate(R.layout.ppw_comment, null);
                //创建PopupWindow对象，指定宽度和高度
                PopupWindow window = new PopupWindow(popupView, MATCH_PARENT, WRAP_CONTENT);
                window.setFocusable(true);
                window.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3299CC"))); //设置背景颜色
                window.setOutsideTouchable(true); //设置可触摸弹窗以外的区域
                //设置弹窗会随软键盘出现而改变位置
                window.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
                window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                window.showAtLocation(image, Gravity.BOTTOM, 0, 0);

                //获取头像图片
                icon = (ImageView)popupView.findViewById(R.id.icon);
                //获取名字
                name =(TextView)popupView.findViewById(R.id.name);
                //获取说说
                description=(TextView)popupView.findViewById(R.id.description);
                //获取发布时间
                time = (TextView)popupView.findViewById(R.id.published);
                //获取评论数目
                count_comment = (TextView)popupView.findViewById(R.id.count_comment);

                //初始化评论弹窗
                initPopupWindow();

                //点击评论
                btn_comment = (ImageButton)popupView.findViewById(R.id.btn_comment);
                btn_comment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //设置评论数组
                        User.setComments(comments);
                        Intent intent = new Intent(ImageActivity.this, CommentActivity.class);
                        intent.putExtra("momentId",momentId);
                        startActivityForResult(intent,1);

                    }
                });

                //输入评论内容
                comment=(EditText)popupView.findViewById(R.id.comment);
                //点击发送评论
                sendComment=(Button)popupView.findViewById(R.id.sendComment);

                sendComment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(comment.getText().toString().equals("")){
                            Toast.makeText(ImageActivity.this,"评论不能为空!",Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(ImageActivity.this,"评论成功！",Toast.LENGTH_SHORT).show();
                            //评论计数增加
                            commentCount=commentCount+1;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    count_comment.setText(commentCount+"");
                                }
                            });
                            //post数据
                            //与后台交互,上传评论
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                            RequestBody requestBody = new FormBody.Builder()
                                    .add("userId",User.getUserId())   //当前登录用户ID
                                    .add("comment",comment.getText().toString())
                                    .add("published",simpleDateFormat.format(new java.util.Date()))
                                    .build();
                            Request request = new Request.Builder()
                                    .url("http://112.74.125.217:3000/moments/"+momentId+"/comments")
                                    .post(requestBody)
                                    .build();
                         HttpUtil2.sendOkHttpRequest(request,new okhttp3.Callback(){
                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String responseData = response.body().string();
                            Log.i("新的评论：",responseData);
                            //清空输入框
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    comment.setText("");
                                }
                            });
                            getDataFromServer();

                        }
                        @Override
                        public void onFailure(Call call,IOException e){
                            //在这里对异常情况进行处理
                            e.printStackTrace();
                        }
                    });
                        }
                    }
                });
            }
        });

    }

    //从CommentActivity返回后更新评论弹窗
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        switch (requestCode){
            case 1:
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        count_comment.setText(User.count+"");
                    }
                });

        }
    }



    private void getDataFromServer(){
        RequestBody requestBody = new FormBody.Builder()
                .add("tags","食物,糗事,动物,人物,风景,体验")
                .add("range","1000")
                .add("offset","0")
                .add("limit","1000")
                .add("longitude",longitude)
                .add("latitude",latitude)
                .build();
        Request request = new Request.Builder().url("http://112.74.125.217:3000/moments")
                .post(requestBody).build();
        HttpUtil2.sendOkHttpRequest(request,new okhttp3.Callback(){
            @Override
            public void onResponse(Call call, Response response) throws IOException{
                //得到服务器返回的具体内容
                String responseData = response.body().string();
                Log.i("全部数据：",responseData);
                try{
                    JSONObject jsonObject = new JSONObject(responseData);
                    // 解析JSON
                    parseJSONWithJSONObject(jsonObject);

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call call,IOException e){
                //在这里对异常情况进行处理
                e.printStackTrace();
            }
        });
    }
    private void parseJSONWithJSONObject(JSONObject moments) {
        try {
            JSONArray jsonArray = moments.getJSONArray("results");
            for (int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if(jsonObject.getString("momentId").equals(momentId)){
                    username = jsonObject.getString("username");
                    Log.i("用户名：",username+"啊啊啊");
                    commentCount = jsonObject.getInt("commentCount");
                    User.count = commentCount;
                    Log.i("评论数目：",commentCount+"!!!!!!!!!!!!!!!!");
                    talk = jsonObject.getString("text");
                    Log.i("评论内容：",talk);
                    published = jsonObject.getString("published");
                    comments = jsonObject.getJSONArray("comments");
                    iconUrl = jsonObject.getString("avatar");
                    Log.i("头像地址：",iconUrl+"!!!!!!!!!!!!!!!!");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void initPopupWindow(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                name.setText(username);
                count_comment.setText(commentCount+"");
                description.setText(talk);
                time.setText(published);
                setIconFromURL(iconUrl);
                Log.i("ImamgeActivity","设置界面数据");
            }
        });

    }

//    private void setPhotoFromURL(final String path){
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                URL url;
//                try{
//                    url = new URL("http://112.74.125.217:3000/" + path);
//                    HttpURLConnection connection = (HttpURLConnection)url.openConnection();
//                    connection.setRequestMethod("GET");
//                    connection.setConnectTimeout(5000);
//                    InputStream inputStream = connection.getInputStream();
//                    final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            image.setImageBitmap(bitmap);
//                        }
//                    });
//
//                }catch (MalformedURLException e){
//                    e.printStackTrace();
//                }catch (IOException e){
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//    }

    private void setIconFromURL(final String path){
        new Thread(new Runnable() {
            @Override
            public void run() {
                URL url;
                try{
                    url = new URL("http://112.74.125.217:3000/" + path);
                    HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(5000);
                    InputStream inputStream = connection.getInputStream();
                    final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    TimerTask task = new TimerTask() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    icon.setImageBitmap(bitmap);
                                }
                            });
                        }
                    };
                    Timer timer = new Timer();
                    timer.schedule(task,500);  //0.5秒后执行TimeTask的run方法

                }catch (MalformedURLException e){
                    e.printStackTrace();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }).start();
    }
}

