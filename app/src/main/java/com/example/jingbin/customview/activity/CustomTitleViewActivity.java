package com.example.jingbin.customview.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.jingbin.customview.R;

public class CustomTitleViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_title_view);
        setTitle("自定义View (一)");
    }
}
