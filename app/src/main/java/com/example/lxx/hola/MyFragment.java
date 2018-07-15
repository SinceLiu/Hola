package com.example.lxx.hola;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class MyFragment extends Fragment {
    private ImageView iv ;
    private ImageView back;
    private ImageView ok;
    private EditText name , gender , signature;

//    public MyFragment() throws FileNotFoundException {
//    }
    public MyFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View myLayout = inflater.inflate(R.layout.myinfo_layout,container,false);
        iv=(ImageView)myLayout.findViewById(R.id.imageicon);

        if (User.avatar != null){
            updateAvatar();
            System.out.println("User.avatar is updated2");
        }
        else {
            System.out.println("User.avatar == null");
        }

        back = (ImageView)myLayout.findViewById(R.id.btn_cancel);
        ok = (ImageView)myLayout.findViewById(R.id.btn_ok);
        name = (EditText) myLayout.findViewById(R.id.user_name);
        name.setText(User.username);
        gender = (EditText) myLayout.findViewById(R.id.user_gender);
        gender.setText(User.gender);
        signature = (EditText) myLayout.findViewById(R.id.user_signature);
        signature.setText(User.signature);
        return myLayout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        new Networks().execute();

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),ChangeInfoActivity.class);

                getActivity().startActivityForResult(intent,3);
                //startActivityForResult(intent,2);
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ok.setFocusable(true);
                ok.setFocusableInTouchMode(true);
                ok.requestFocus();
                ok.requestFocusFromTouch();
                User.username = name.getText().toString();
                User.gender = gender.getText().toString();
                User.signature = signature.getText().toString();
                new Networks().execute();
                Toast.makeText(getActivity(),"修改成功！",Toast.LENGTH_SHORT).show();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),FriendActivity.class);
                startActivity(intent);
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == requestCode){
            Bundle bundle = data.getExtras();
            //显示扫描到的内容
            //接收返回图片，并且设置到ImageView里面
            if (data.getParcelableExtra("bitmap") != null)
            {
            iv.setImageBitmap((Bitmap) data.getParcelableExtra("bitmap"));
                System.out.println("MyFragment");
                User.avatar = (Bitmap) data.getParcelableExtra("bitmap");
            }
        }
    }

    public void bitmap2jpg(Bitmap bitmap) throws IOException {
        OutputStream os = new BufferedOutputStream(new FileOutputStream(User.file));
        User.avatar.compress(Bitmap.CompressFormat.JPEG, 100, os);
        os.close();
    }

    public void updateAvatar(){
        iv.setImageBitmap(User.avatar);
        System.out.println("iv is setted");
    }

}
