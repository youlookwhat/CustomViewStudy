package com.example.jingbin.customview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.jingbin.customview.activity.CustomImageViewActivity;
import com.example.jingbin.customview.activity.CustomImgContainerActivity;
import com.example.jingbin.customview.activity.CustomProgressBarActivity;
import com.example.jingbin.customview.activity.CustomTitleViewActivity;
import com.example.jingbin.customview.activity.CustomVolumControlBarActivity;
import com.example.jingbin.customview.activity.DeepUnderstandAttrActivity;
import com.example.jingbin.customview.activity.VDHDeepLayoutActivity;

/**
 * Created by jingbin on 16/9/29.
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        findViewById(R.id.bt_custom_view_01).setOnClickListener(this);
        findViewById(R.id.bt_custom_view_02).setOnClickListener(this);
        findViewById(R.id.bt_custom_view_03).setOnClickListener(this);
        findViewById(R.id.bt_custom_view_04).setOnClickListener(this);
        findViewById(R.id.bt_custom_view_05).setOnClickListener(this);
        findViewById(R.id.bt_custom_view_06).setOnClickListener(this);
        findViewById(R.id.bt_custom_view_07).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_custom_view_01:// 点击更换验证码(4位随机数字)
                startActivity(new Intent(v.getContext(), CustomTitleViewActivity.class));
                break;
            case R.id.bt_custom_view_02:// 图文搭配(上图下文)
                startActivity(new Intent(v.getContext(), CustomImageViewActivity.class));
                break;
            case R.id.bt_custom_view_03:// 自定义圆形进度条
                startActivity(new Intent(v.getContext(), CustomProgressBarActivity.class));
                break;
            case R.id.bt_custom_view_04:// 视频音量调控
                startActivity(new Intent(v.getContext(), CustomVolumControlBarActivity.class));
                break;
            case R.id.bt_custom_view_05:// 左上右下的图片显示(ViewGroup)
                startActivity(new Intent(v.getContext(), CustomImgContainerActivity.class));
                break;
            case R.id.bt_custom_view_06:// 深入理解Android中的自定义属性
                startActivity(new Intent(v.getContext(), DeepUnderstandAttrActivity.class));
                break;
            case R.id.bt_custom_view_07:// ViewDragHelper完全解析 自定义ViewGroup神器
//                startActivity(new Intent(v.getContext(), VDHLayoutActivity.class));
                startActivity(new Intent(v.getContext(), VDHDeepLayoutActivity.class));
                break;
            default:
                break;
        }
    }
}
