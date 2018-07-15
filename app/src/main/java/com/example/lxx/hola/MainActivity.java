package com.example.lxx.hola;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lxx.hola.model.Tag;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * 用于展示地图的Fragment
     */
    private MapFragment mapFragment;

    /**
     * 用于开启相机的Fragment
     */
    private CameraFragment cameraFragment;

//    /**
//     * 用于展示好友的Fragment
//     */
//    private FriendFragment friendFragment;

    /**
     * 用于展示我的的Fragment
     */
    private MyFragment myFragment;

    /**
     * 地图界面布局
     */
    private View mapLayout;
    /**
     * 相机界面布局
     */
    private View cameraLayout;

//    /**
//     * 好友界面布局
//     */
//    private View friendLayout;

    /**
     * 我的的界面布局
     */
    private View myLayout;

    /**
     * 地图图标控件
     */
    private ImageView mapImage;

    /**
     * 相机图标控件
     */
    private ImageView cameraImage;

//    /**
//     * 好友图标控件
//     */
//    private ImageView friendImage;

    /**
     * 我的图标控件
     */
    private ImageView myImage;

    /**
     * 地图标题控件
     */
    private TextView mapText;

    /**
     * 相机标题控件
     */
    private TextView cameraText;

//    /**
//     * 好友标题控件
//     */
//    private TextView friendText;

    /**
     * 我的标题控件
     */
    private TextView myText;

    private static final int GET_AVATAR = 3;
    /**
     * 用于对Fragment进行管理
     */
    private FragmentManager fragmentManager;

    //切换到相机fragment时的所需参数
    public static final int TAKE_PHOTO = 1;
    private Uri imageUri;
    private Bitmap head;// 头像Bitmap
    private static String path = "/sdcard/img/";// sd路径


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        //初始化布局元素
        initViews();
        fragmentManager = getSupportFragmentManager();
        //第一次启动选择第0个Tab，即地图界面
        setTabSelection(0);
    }

    /**
     * 获取每个需要用到的控件实例，并给它们设置好必要的点击事件
     */
    private void initViews() {
        mapLayout = findViewById(R.id.map_layout);
        cameraLayout = findViewById(R.id.camera_layout);
        myLayout = findViewById(R.id.my_layout);
        mapImage = (ImageView) findViewById(R.id.map_image);
        cameraImage = (ImageView) findViewById(R.id.camera_image);
        myImage = (ImageView) findViewById(R.id.my_image);
        mapText = (TextView) findViewById(R.id.map_text);
        cameraText = (TextView) findViewById(R.id.camera_text);
        myText = (TextView) findViewById(R.id.my_text);
        mapLayout.setOnClickListener(this);
        cameraLayout.setOnClickListener(this);
        myLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.map_layout:
                setTabSelection(0);
                break;
            case R.id.camera_layout:
                setTabSelection(1);
                Tag.file = new File(MainActivity.this.getExternalCacheDir(), "outputImage.jpg");
                try {
                    if (Tag.file.exists()) {
                        Tag.file.delete();
                    }
                    Tag.file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (Build.VERSION.SDK_INT < 24) {
                    imageUri = Uri.fromFile(Tag.file);
                } else {
                    imageUri = FileProvider.getUriForFile(MainActivity.this, "com.example.trycamera.fileprovider", Tag.file);
                }
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, TAKE_PHOTO);
                setTabSelection(0);
                break;
            case R.id.my_layout:
                setTabSelection(2);
                break;
            default:
                break;
        }
    }

    /**
     * 根据传入的index来设置选中的Tab页
     *
     * @param index 每个tab页对应的下标，0表示地图，1表示相机，2表示好友，3表示我的
     */
    private void setTabSelection(int index) {
        //每次选中之前清除掉上次的选中状态
        clearSelection();
        //开启一个Fragment事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        //先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        hideFragments(transaction);
        switch (index) {
            case 0:
                //点击地图tab是，改变控件的图片和文字颜色
                mapImage.setImageResource(R.drawable.map_click);
                mapText.setTextColor(Color.parseColor("#1296db"));
                if (mapFragment == null) {
                    //如果mapFragment为空，则创建一个并添加到界面上
                    mapFragment = new MapFragment();
                    transaction.add(R.id.content, mapFragment);
                } else {
                    //如果mapFragment不为空，则直接将它显示出来
                    transaction.show(mapFragment);
                }
                break;
            case 1:
                cameraImage.setImageResource(R.drawable.camera_click);
                cameraText.setTextColor(Color.parseColor("#1296db"));
                if (mapFragment == null) {
                    //如果mapFragment为空，则创建一个并添加到界面上
                    mapFragment = new MapFragment();
                    transaction.add(R.id.content, mapFragment);
                } else {
                    //如果mapFragment不为空，则直接将它显示出来
                    transaction.show(mapFragment);
                }
//                if (cameraFragment == null) {
//                    cameraFragment = new CameraFragment();
//                    transaction.add(R.id.content, cameraFragment);
//                } else {
//                    transaction.show(cameraFragment);
//                }
                break;
            case 2:
                myImage.setImageResource(R.drawable.my_click);
                myText.setTextColor(Color.parseColor("#1296db"));
                if (myFragment == null) {
                    myFragment = new MyFragment();
                    transaction.add(R.id.content, myFragment);
                } else {
                    transaction.show(myFragment);
                }
                break;
        }
        transaction.commit();
    }

    /**
     * 清楚掉所有的选中状态
     */
    private void clearSelection() {
        mapImage.setImageResource(R.drawable.map);
        mapText.setTextColor(Color.parseColor("#82858b"));
        cameraImage.setImageResource(R.drawable.camera);
        cameraText.setTextColor(Color.parseColor("#82858b"));
        myImage.setImageResource(R.drawable.my);
        myText.setTextColor(Color.parseColor("#82858b"));
    }

    /**
     * 将所有的Fragment都置为隐藏状态
     *
     * @param transaction 用于对Fragment执行操作的事务
     */
    private void hideFragments(FragmentTransaction transaction) {
        if (mapFragment != null) {
            transaction.hide(mapFragment);
        }
        if (cameraFragment != null) {
            transaction.hide(cameraFragment);
        }
        if (myFragment != null) {
            transaction.hide(myFragment);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PHOTO: {
                if (resultCode == RESULT_OK) {
                    cropPhoto(Uri.fromFile(Tag.file));// 裁剪图片
                } else {
                    Intent intent2 = new Intent(MainActivity.this, MainActivity.class);
                    startActivity(intent2);
                    MainActivity.this.finish();
                }
            }
                break;
            case 4:
                if(data!=null)
                {
//                    getDropedPic(data);
                    Intent intent = new Intent(MainActivity.this, AddTagActivity.class);
                    intent.putExtra("imageUri", imageUri.toString());
                    startActivity(intent);}
                break;
            case GET_AVATAR:
                {
                    System.out.println("onActivityResult is launched.");
                    if(resultCode == requestCode){
                        Bundle bundle = data.getExtras();
                        //显示扫描到的内容
                        //接收返回图片，并且设置到ImageView里面
                        if (data.getParcelableExtra("bitmap") != null)
                        {
                            User.avatar = (Bitmap) data.getParcelableExtra("bitmap");
                            myFragment.updateAvatar();
                            try {
                                myFragment.bitmap2jpg(User.avatar);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            System.out.println("User.avatar is updated");
                        }
                    }
                }
            default:
                break;


//                // 将拍摄的照片地址传输给标签活动；
////                    Bitmap bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(imageUri));
//                Intent intent = new Intent(MainActivity.this, AddTagActivity.class);
//                intent.putExtra("imageUri", imageUri.toString());
//                startActivity(intent);
        }

    }


    /**
     //     * 调用系统的裁剪功能
     //     *
     //     * @param uri
     //     */
    public void cropPhoto(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(Tag.file));//// 输出文件
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());// 输出格式
        intent.putExtra("crop", true);
        intent.putExtra("scale", true);// 缩放
        intent.putExtra("scaleUpIfNeeded", true);// 如果小于要求输出大小，就放大
        intent.putExtra("return-data", false);// 不返回缩略图
        intent.putExtra("noFaceDetection", true);// 关闭人脸识别
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 4);
    }
//    private void getDropedPic(Intent data) {
//        Bundle extras = data.getExtras();
//        if (extras != null) {
//            Bitmap bitmap = extras.getParcelable("data");
//            if (bitmap != null) {
//                try {
//                    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(Tag.file));
//                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
//                    bos.flush();
//                    bos.close();
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            } else {
//                Toast.makeText(MainActivity.this, "drop failed", Toast.LENGTH_LONG).show();
//            }
//        }
//    }
}
