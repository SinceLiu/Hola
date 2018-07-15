package com.example.lxx.hola;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lxx.hola.model.Tag;

import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class AddTagActivity extends Activity {
	private Button send;
	private EditText tagEditText;
	private TextView text_send;
	private ImageView imageView;
	private List<CheckBox> checkBoxList = new ArrayList<CheckBox>();
	private String published;
	private Bitmap srcBitmap;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_add_tag);
		//获取系统时间
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日    HH:mm:ss     ");
		Date curDate = new Date(System.currentTimeMillis());//获取当前时间
		published = formatter.format(curDate);

		//控件获取实例
		imageView = (ImageView) findViewById(R.id.photo_send);
		Intent intent = getIntent();
		Uri imgUri = Uri.parse(intent.getStringExtra("imageUri"));
		Tag.published=published;
		Tag.longitude=Double.toString(MapFragment.myLatlng.longitude);
		Tag.latitude= Double.toString(MapFragment.myLatlng.latitude);
		Tag.username=User.getUsername();

//		Tag.longitude= Uri.parse(intent.getStringExtra("imageUri"));
//		Tag.latitude= Uri.parse(intent.getStringExtra("latitude"));
//		Tag.username= Uri.parse(intent.getStringExtra("username"));
		try {
			Bitmap srcBitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imgUri));
			imageView.setImageBitmap(srcBitmap); //显示拍照图片
//			Tag.img=srcBitmap;;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		send=(Button) findViewById(R.id.send);
		text_send = (EditText) findViewById(R.id.text_send);

		send.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
//				Intent intent=getIntent();
//				final String filePath = intent.getStringExtra("imageUri");
//				final File pictureFile = new File(filePath); //通过路径获取文件;
//				List tag = new ArrayList<String>();

				Tag.text=text_send.getText().toString();
				Tag.tags="食物,糗事,动物,人物,风景,体验";
				new AddTag().execute();
				Toast.makeText(AddTagActivity.this, "发布成功",Toast.LENGTH_LONG).show();
				Intent intent = new Intent(AddTagActivity.this, MainActivity.class);
				startActivity(intent);
				AddTagActivity.this.finish();
			}
		});
	}


}

	//使用OKhttp发送请求
//	private void sendRequestWithOkHttp() {
////				开启一个新线程，向服务器上传数据
//		new Thread() {
//			public void run() {
//				try {
//					//创建okHttpClient对象
//					OkHttpClient mOkHttpClient = new OkHttpClient();
//					MediaType type = MediaType.parse("application/json;charset=utf-8");
//					//测试上传
//					RequestBody body = new MultipartBody.Builder()
//							.setType(MultipartBody.FORM)
//							.addFormDataPart("username", "trk")
//							.addFormDataPart("tag", testTag)
//							.addFormDataPart("image", "image",
//									RequestBody.create(MediaType.parse("image/jpg"),
//											String.valueOf(User.avatar)
//									))
//							.build();
//
////					RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
////									.addFormDataPart("file", pictureFile.getName(), RequestBody.create(MediaType.parse("image/png"), pictureFile))
////									.addFormDataPart("files", null, new MultipartBody.Builder("BbC04y")
////											.addPart(Headers.of("Content-Disposition", "form-data; filename=\"img.png\""),
////													RequestBody.create(MediaType.parse("image/png"), new File(filePath)))
////											.build())
////							.addPart(Headers.of(
////											"Content-Disposition", "form-data; name=\"username\""),
////									RequestBody.create(null, "Trk"))
////							.addPart(Headers.of(
////											"Content-Disposition", "form-data; name=\"tag\""),
////									RequestBody.create(null, testTag))
////							.addPart(Headers.of(
////											"Content-Disposition", "form-data; name=\"text\""),
////									RequestBody.create(null, tagEditText.getText().toString()))
////									.addPart(Headers.of(
////											"Content-Disposition", "form-data; name=\"latitude\""),
////									RequestBody.create(null, latitude))
////							.addPart(Headers.of(
////											"Content-Disposition", "form-data; name=\"longitude\""),
////									RequestBody.create(null, longitude))
////							.addPart(Headers.of(
////											"Content-Disposition", "form-data; name=\"published\""),
////									RequestBody.create(null, published))
////							.build();
//					RequestBody.create(type, body.toString());
//					Request request = new Request.Builder()
//							.url("http://112.74.125.217:3000/users/userId/moments")
//							.post(body)
//							.build();
////					Call call = mOkHttpClient.newCall(request);
////					call.enqueue(new Callback() {
////						@Override
////						public void onFailure(Call call, IOException e) {
////							e.printStackTrace();
////						}
////						@Override
////						public void onResponse(Call call, Response response) throws IOException {
////							System.out.println("上传成功");
////						}
////					});
//
//					Response response = mOkHttpClient.newCall(request).execute();
//					String responseData = response.body().string();
//
//					//parseJSONWithGSON(responseData);
//
////						if (!response.isSuccessful()) {
////							throw new IOException("eror: " + response);
////							}
////							else {
////								Intent intent=new Intent(AddTagActivity.this,MainActivity.class);
////								startActivity(intent);
////								finish();
////							}
//
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		}.start();
//	}
//
//}
//	private void parseJSONWithJSONObject(String jsonData) {
//		try {
//			parseJSONWithJSONObject(responseData);
////            JSONArray jsonArray = new JSONArray(jsonData);
////            for (int i = 0; i < jsonArray.length(); i++) {
////                JSONObject jsonObject = jsonArray.getJSONObject(i);
//			JSONObject jsonObject=new JSONObject(jsonData);
//			//一定会返回isOK
//			Log.d("输出服务器返回的数据=",jsonData);
//			isOK = Boolean.parseBoolean(jsonObject.getString("isOK"));
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//	//跳转到主界面，并销毁标签界面
//	private void toMain(){
//		Intent intent = new Intent(AddTagActivity.this,MainActivity.class);
//		startActivity(intent);
//		finish();
//	}
//	//判断发布是否成功,如发布成功跳转主界面，否则发送toast提示
//	private void isPostSucceed(){
//		if (isOK) {
//			toMain();
//		}else{
//			Toast.makeText(AddTagActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
//		}
//	}
//	//切回UI主线程
//	private void isToMain(){
//		Log.d("切回主线程测试isOK=", String.valueOf(isOK));
//		runOnUiThread(new Runnable() {
//			@Override
//			public void run() {
//				isPostSucceed();
//			}
//		});
//	}
//}

//				Intent intent1 = new Intent(AddTagActivity.this, MainActivity.class);
//				startActivity(intent1);

//				Intent intent = getIntent();
//				String filePath = intent.getStringExtra("imageUri");
//				final File pictureFile = new File(filePath); //通过路径获取文件;
////				List tag = new ArrayList<String>();
//				String testTag="糗事,动物";
////				for (CheckBox checkbox : checkBoxList) {
////					if (checkbox.isChecked()) {
////						tag.add(  checkbox.getText().toString());
////					}
////				}
//				paramMap.put("username",testUsername);
//				paramMap.put("tag", testTag);
//				paramMap.put("text", tagEditText.getText().toString());
//				paramMap.put("longitude", longitude);
//				paramMap.put("latitude", latitude);
//				paramMap.put("published", published);


//				final String content = String.valueOf(js);
//
//				try {
//					url = new URL(urlPath);
//					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//					conn.setConnectTimeout(5000);
//					conn.setDoOutput(true);// 设置允许输出
//					conn.setRequestMethod("POST");
//					conn.setRequestProperty("Content-Type",
//							"application/json; charset=UTF-8"); // 内容类型
//					OutputStream os = conn.getOutputStream();
//					os.write(content.getBytes());
//					os.close();
//					if (conn.getResponseCode() == 200) {
//						handler.sendEmptyMessage(0x123);
//					} else {
//						handler.sendEmptyMessage(0x122);
//					}
//
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//
//		}.start();












