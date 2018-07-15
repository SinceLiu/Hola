package com.example.lxx.hola;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by echo on 2017/9/3.
 */

public class FriendInfoActivity extends AppCompatActivity {
            private TextView textView;
            private ImageView head;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);

        textView = (TextView)findViewById(R.id.fri_name);
        textView.setText(getIntent().getStringExtra("friend_name"));

        textView = (TextView)findViewById(R.id.fri_sign);
        textView.setText(getIntent().getStringExtra("friend_sign"));

        head = (ImageView)findViewById(R.id.fri_head);
        head.setBackgroundResource(getIntent().getIntExtra("friend_head",R.drawable.head2));

        ImageView btn_back = (ImageView)findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FriendInfoActivity.this,FriendActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
