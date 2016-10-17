package com.example.jingbin.customview.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.jingbin.customview.R;
import com.example.jingbin.customview.view.CustomProgressBar;

public class CustomProgressBarActivity extends AppCompatActivity {

    private CustomProgressBar custom_progress_bar_01;
    private CustomProgressBar custom_progress_bar_02;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_progress_bar);

        initView();
    }

    private void initView() {
        custom_progress_bar_01 = (CustomProgressBar) findViewById(R.id.custom_progress_bar_01);
        custom_progress_bar_02 = (CustomProgressBar) findViewById(R.id.custom_progress_bar_02);

    }


    @Override
    protected void onStop() {
        super.onStop();
        custom_progress_bar_01.setContinue(false);
        custom_progress_bar_02.setContinue(false);
    }
}
