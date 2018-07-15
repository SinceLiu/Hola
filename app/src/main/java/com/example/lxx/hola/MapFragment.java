package com.example.lxx.hola;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.Circle;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.example.lxx.hola.HttpUtil.HttpUtil;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;




public class MapFragment extends Fragment implements LocationSource,AMapLocationListener,AMap.OnMarkerClickListener {
    private ImageButton refresh;

    private static MapFragment fragment = null;

    private ImageButton shaixuan;

    private ProgressDialog progressDialog;

    private List<RemarkerInfo> remarkerInfoList = new ArrayList<>();

    private List<Marker> markerList = new ArrayList<>();

    private MapView mapView;
    private AMap aMap;
    private View mapLayout;
    private MyLocationStyle myLocationStyle;
    private LocationSource.OnLocationChangedListener mListener;
    private AMapLocationClient locationClient;
    private AMapLocationClientOption clientOption;
    private UiSettings mUiSettings;
    public static LatLng myLatlng;//自己的位置的经纬度
    private Bitmap photoBitmap;
    private Bitmap iconBitmap;

    private final String address = "http://112.74.125.217:3000/moments";

    public static Fragment newInstance() {
        if (fragment == null) {
            synchronized (MapFragment.class) {
                if (fragment == null) {
                    fragment = new MapFragment();
                }
            }
        }
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mapLayout == null) {
            mapLayout = inflater.inflate(R.layout.map_layout,container,false);
            initView(savedInstanceState,mapLayout);
        } else {
            if (mapLayout.getParent() != null) {
                ((ViewGroup) mapLayout.getParent()).removeView(mapLayout);
            }
        }

        shaixuan = (ImageButton) mapLayout.findViewById(R.id.shaixuan);
        refresh = (ImageButton) mapLayout.findViewById(R.id.refresh);
        return mapLayout;
    }



    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        shaixuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),ShaiXuanDialogActivity.class);
                startActivityForResult(intent,1);
            }
        });

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                queryFromServer(address);
            }
        });
    }


    private void initView(Bundle savedInstanceState, View view) {
        mapView = (MapView) mapLayout.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        if (aMap == null) {
            aMap = mapView.getMap();
        }
        mUiSettings = aMap.getUiSettings();
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样类
        //隐藏自带的圆圈
        myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));// 设置圆形的边框颜色
        myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));// 设置圆形的填充颜色
        myLocationStyle.strokeWidth(0f);//设置精度圈边框宽度

        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);//定位一次，并将视角移到地图中心点
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的风格
        aMap.getUiSettings().setMyLocationButtonEnabled(true);//设置默认定位按钮是否显示
        aMap.setLocationSource(this);//设置定位监听
        aMap.setMyLocationEnabled(true);//true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位
        mUiSettings.setZoomControlsEnabled(true);//显示放大缩小控件
        mUiSettings.setScaleControlsEnabled(true);//显示比例尺控件
        CameraUpdate cameraUpdate = CameraUpdateFactory.zoomTo(15);//确定地图的缩放级别3-21
        aMap.moveCamera(cameraUpdate);



        //手势禁用
        mUiSettings.setZoomGesturesEnabled(true);//允许缩放
        mUiSettings.setScrollGesturesEnabled(true);//允许滑动

        mUiSettings.setTiltGesturesEnabled(false);//禁止倾斜
        mUiSettings.setRotateGesturesEnabled(false);//禁止旋转


//        remarkerInfoList.clear();

//        remarkerInfoList = RemarkerInfo.initdata();
        Log.d("A","初始化布局完成");

    }



    /**
     * 激活定位
     * @param listener
     */
    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        if (locationClient == null) {
            locationClient = new AMapLocationClient(getActivity());
            clientOption = new AMapLocationClientOption();
            locationClient.setLocationListener(this);
            clientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            clientOption.setOnceLocationLatest(true);//设置单次精确定位
            locationClient.setLocationOption(clientOption);
            locationClient.startLocation();
        }
    }

    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
        mListener = null;
        if (locationClient != null) {
            locationClient.stopLocation();;
            locationClient.onDestroy();
        }
        locationClient = null;
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {

        if (mListener != null && aMapLocation != null) {

            if (aMapLocation != null && aMapLocation.getErrorCode() == 0) {

                mListener.onLocationChanged(aMapLocation);//显示系统小蓝点
                myLatlng = new LatLng(aMapLocation.getLatitude(),aMapLocation.getLongitude());
                //添加一个范围为1000米的圆圈
                Circle circle = aMap.addCircle(new CircleOptions().
                        center(myLatlng)
                        .radius(1000)
//                        .fillColor(Color.parseColor("#1296db"))
                        .strokeColor(Color.parseColor("#1296db"))
                        .strokeWidth(10));
                queryFromServer(address);
//                if (remarkerInfoList == null) {
//                    RemarkerInfo remarker = new RemarkerInfo();
//                    remarker.setRemarkerId("reamrker12");
//                    remarker.setRemarkerLon(113.41);
//                    remarker.setRemarkerLat(23.0500);
//                    remarker.save();
//                    if (remarker.save()) {
//                        Log.d("添加初始化","数据成功");
//                    } else {
//                        Log.d("添加初始化","失败");
//                    }
//
//                }
//                List<RemarkerInfo> remarkerInfoList = DataSupport.findAll(RemarkerInfo.class);

                    Log.d("数据库","长度 = " + remarkerInfoList.size());
//                loadMarker(remarkerInfoList);
                aMap.setOnMarkerClickListener(this);
            } else {
                String errText = "定位失败，"+ aMapLocation.getErrorCode()+": "+aMapLocation.getErrorInfo();
                Log.e("AmapErr",errText);
            }
        }
    }

    private void setIconToOptions(MarkerOptions options) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.map_marker_layout,null);
        ImageView imageView = (ImageView) view.findViewById(R.id.iv_marker);
        imageView.setImageResource(R.drawable.remarker);//设置remarker的缩略图
        options.icon(BitmapDescriptorFactory.fromView(view));
    }

    /*
    查询所有的remarker，从服务器获取所有的remarker
     */
//    private void queryRemarkerInfo() {
//        String ad2 = "https://private-b1ee4-hola16.apiary-mock.com/moments";
//
//        Log.d("更新数据库","成功");
//    }

    /*
    根据传入的地址和类型从服务器上查询数据
     */
    private void queryFromServer(String address) {

        showProgressDialog();
        HttpUtil.sendOkHttpRequest(address, myLatlng, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(getContext(),"加载失败", Toast.LENGTH_SHORT).show();
                        Log.d("加载失败","数据请求");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                remarkerInfoList = HttpUtil.handleRemarkerInfoResponse(responseText);
                if (remarkerInfoList != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                        }
                    });
                }
                loadMarker(remarkerInfoList);
            }
        });
    }

    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("正在加载...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    private void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    private void loadMarker(List<RemarkerInfo> remarkerInfoList) {
        double a = myLatlng.latitude;
        double b = myLatlng.longitude;
        Log.d("location","a = "+ a +" b = " + b);
        if (remarkerInfoList == null || remarkerInfoList.size() == 0) {
            Log.d("A","没有数据");
            return;
        }
        for (int i = 0; i < remarkerInfoList.size(); i++) {
            RemarkerInfo remarkerInfo = remarkerInfoList.get(i);
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.anchor(0.5f,1.0f);//设置marker覆盖物的锚点比例
            LatLng latLng = new LatLng(remarkerInfo.getRemarkerLat(), remarkerInfo.getRemarkerLon());

//            double distance = AMapUtils.calculateLineDistance(latLng,myLatlng);

            markerOptions.position(latLng);
            setIconToOptions(markerOptions);
            Marker marker = aMap.addMarker(markerOptions);
            marker.setTitle(remarkerInfo.getMomentId());
            marker.setInfoWindowEnable(false);
            markerList.add(marker);

            Log.d("添加","成功");
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        for (int i = 0;i < markerList.size();i++) {
            if (marker.equals(markerList.get(i))) {
                final Intent intent = new Intent(getActivity(), ImageActivity.class);
                intent.putExtra("latitude", Double.toString(marker.getPosition().latitude));
                intent.putExtra("longitude", Double.toString(marker.getPosition().longitude));
                intent.putExtra("momentId", marker.getTitle());
                //解析，下载图片生成bitmap，传递给ImageActivity
                try {
                    JSONArray jsonArray = User.getMoments().getJSONArray("results");
                    for (int j = 0; j < jsonArray.length(); j++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(j);
                        if (jsonObject.getString("momentId").equals(marker.getTitle())) {
                            String photoUrl = jsonObject.getString("photo");
                            setphotoBitmapFromURL(photoUrl);
                            TimerTask task = new TimerTask() {
                                @Override
                                public void run() {
                                    intent.putExtra("photoBitmap", photoBitmap);  //传递bitmap给ImageActivity
                                    startActivity(intent);
                                }
                            };
                            Timer timer = new Timer();
                            timer.schedule(task,500);  //0.5秒后执行TimeTask的run方法

                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //这里将remarker的经纬度传给查看活动，查看活动通过经纬度来匹配查询数据库获得这个remarker的信息
            }
        }
        return false;
    }

    //从服务器下载图片并设置成bitmap，by lxx
    public void setphotoBitmapFromURL(final String path){
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
                    photoBitmap = BitmapFactory.decodeStream(inputStream);
                }catch (MalformedURLException e){
                    e.printStackTrace();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {

                }
        }
    }
}
