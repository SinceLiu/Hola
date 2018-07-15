package com.example.lxx.hola;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ToggleButton;


/*
待添加修改，隐身、一次性和标签没有意义。应该会修改为时间上的筛选
 */
public class ShaiXuanDialogActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {


    private ImageButton close;

    private CheckBox zhiwu,dongwu,qiushi,fengjing,tiyan,shiwu;//对应六个标签

    private Button OK,Cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_shaixuandialog);

        WindowManager manager = getWindowManager();
        Display display = manager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.height = (int) ((size.y)* 0.4);
        params.width = (int) ((size.x)* 0.8);
        params.alpha = 1.0f;
        params.dimAmount = 0.0f;
        getWindow().setAttributes(params);
        getWindow().setGravity(Gravity.CENTER);


        zhiwu = (CheckBox) findViewById(R.id.zhiwu);
        dongwu = (CheckBox) findViewById(R.id.dongwu);
        qiushi = (CheckBox) findViewById(R.id.qiushi);
        fengjing = (CheckBox) findViewById(R.id.fengjing);
        tiyan = (CheckBox) findViewById(R.id.tiyan);
        shiwu = (CheckBox) findViewById(R.id.shiwu);

        zhiwu.setOnCheckedChangeListener(this);
        dongwu.setOnCheckedChangeListener(this);
        qiushi.setOnCheckedChangeListener(this);
        fengjing.setOnCheckedChangeListener(this);
        tiyan.setOnCheckedChangeListener(this);
        shiwu.setOnCheckedChangeListener(this);

        OK = (Button) findViewById(R.id.OK);
        Cancel = (Button) findViewById(R.id.Cancel);
        close = (ImageButton) findViewById(R.id.shaixuan_close);

        OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //添加逻辑，返回筛选掉的结果
                Intent intent = new Intent();

                setResult(RESULT_OK,intent);
                finish();
            }
        });

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()) {
            case R.id.zhiwu:
                //添加逻辑
                break;
            case R.id.dongwu:
                //逻辑
                break;
            case R.id.qiushi:
                //逻辑
                break;
            case R.id.tiyan:
                //逻辑
                break;
            case R.id.fengjing:
                //逻辑
                break;
            case R.id.shiwu:
                //逻辑
                break;
            default:
                break;
        }
    }
}
