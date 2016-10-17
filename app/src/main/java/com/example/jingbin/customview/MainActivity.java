package com.example.jingbin.customview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.jingbin.customview.activity.CustomImageViewActivity;
import com.example.jingbin.customview.activity.CustomProgressBarActivity;
import com.example.jingbin.customview.activity.CustomTitleViewActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showCustomView01();
        showCustomView02();
        showCustomView03();
    }


    /**
     * 点击更换验证码(4位随机数字)
     */
    private void showCustomView01() {
        findViewById(R.id.bt_custom_view_01).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), CustomTitleViewActivity.class));
            }
        });
    }

    private void showCustomView02() {
        findViewById(R.id.bt_custom_view_02).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), CustomImageViewActivity.class));
            }
        });
    }

    private void showCustomView03() {
        findViewById(R.id.bt_custom_view_03).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), CustomProgressBarActivity.class));
            }
        });
    }
}
