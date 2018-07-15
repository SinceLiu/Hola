package com.example.lxx.hola;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;


import com.example.lxx.hola.HttpUtil.HttpUtil2;
import com.example.lxx.hola.adapter.CommentAdapter;
import com.example.lxx.hola.model.Comment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CommentActivity extends AppCompatActivity {
    private EditText editComment;
    private Button sendComment;
    private ListView comment_list;

    private CommentAdapter commentAdapter;
    private String momentId;
    private List<Comment> data;
    private JSONArray comments;  //评论数组


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        momentId = getIntent().getStringExtra("momentId");
        initView();
    }

    private void initView(){
        comment_list = (ListView) findViewById(R.id.comment_list);
        data = new ArrayList<>();
        commentAdapter = new CommentAdapter(getApplicationContext(),data);
        comment_list.setAdapter(commentAdapter);
        comment_list.setBackgroundResource(R.drawable.bg_comment);
        //获取评论数据
        comments = User.getComments();

        //解析JSONArray,初始化评论数据，显示在界面上
        try{
            for(int i=0;i<comments.length();i++){
                JSONObject jsonObject = comments.getJSONObject(i);
                Log.i("comments:",jsonObject.toString());
                //生成评论数据
                Comment comment = new Comment();
                comment.setName(jsonObject.getString("userId")+":");
                comment.setContent(jsonObject.getString("comment"));
                comment.setTime(jsonObject.getString("published"));
                commentAdapter.addComment(comment);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        editComment = (EditText) findViewById(R.id.editComment);
        sendComment = (Button) findViewById(R.id.sendComment);

        sendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editComment.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),"评论不能为空!",Toast.LENGTH_SHORT).show();
                }else{
                    //生成评论数据
                    Comment comment = new Comment();
                    comment.setName(User.getUserId()+":   ");  //显示userId
                    comment.setContent(editComment.getText().toString());
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    String time =simpleDateFormat.format(new java.util.Date());
                    comment.setTime(time);
                    commentAdapter.addComment(comment);
                    User.count = User.count+1;
                    Toast.makeText(getApplicationContext(), "评论成功！", Toast.LENGTH_SHORT).show();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("userId",User.getUsername())
                            .add("comment",editComment.getText().toString())
                            .add("published",time)
                            .build();
                    Request request = new Request.Builder()
                            .url("http://112.74.125.217:3000/moments/"+momentId+"/comments")
                            .post(requestBody)
                            .build();
                    HttpUtil2.sendOkHttpRequest(request,new okhttp3.Callback(){
                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String responseData = response.body().string();
                            Log.i("新的评论列表：",responseData);

                            //清空输入框
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    editComment.setText("");
                                }
                            });
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
}