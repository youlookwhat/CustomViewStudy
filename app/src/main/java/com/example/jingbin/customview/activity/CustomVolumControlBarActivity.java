package com.example.jingbin.customview.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.jingbin.customview.R;

public class CustomVolumControlBarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_volum_control_bar);
        setTitle("自定义View (四) 视频音量调控");
    }
}
