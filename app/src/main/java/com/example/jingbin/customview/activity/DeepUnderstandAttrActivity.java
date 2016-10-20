package com.example.jingbin.customview.activity;

import android.net.http.SslError;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.jingbin.customview.R;

/**
 * TypedArray其实是用来简化我们的工作的，比如上例，如果布局中的属性的值是引用类型（比如：@dimen/dp100），
 * 如果使用AttributeSet去获得最终的像素值，那么需要第一步拿到id，第二步再去解析id。
 * 而TypedArray正是帮我们简化了这个过程。
 */
public class DeepUnderstandAttrActivity extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_deep_understand_attr);
        setTitle("深入理解Android中的自定义属性");
        webView = (WebView) findViewById(R.id.activity_deep_understand_att);
        initWebView();
        webView.loadUrl("http://blog.csdn.net/lmj623565791/article/details/45022631");
    }

    private void initWebView() {
        WebSettings ws = webView.getSettings();
        ws.setLoadWithOverviewMode(true);// setUseWideViewPort方法设置webview推荐使用的窗口。setLoadWithOverviewMode方法是设置webview加载的页面的模式。
        ws.setSavePassword(true);
        ws.setSaveFormData(true);// 保存表单数据
        ws.setSupportZoom(true);
        // 双击缩放
        ws.setBuiltInZoomControls(true);
        ws.setDisplayZoomControls(false);

        //设置缓存模式
        ws.setAppCacheEnabled(true);
        ws.setCacheMode(WebSettings.LOAD_DEFAULT);//设置缓存模式
        // 设置此属性，可任意比例缩放。
        ws.setUseWideViewPort(true);
        //缩放比例 1
        webView.setInitialScale(1);
        ws.setJavaScriptEnabled(true);
        ws.setDomStorageEnabled(true);
        ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);// 排版适应屏幕
        ws.setSupportMultipleWindows(true);// 新加
//        ws.setUseWideViewPort(true); /**合作商页面适应屏幕*/
        webView.setWebViewClient(new MyWebViewClient());
    }

    public class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.startsWith("http://v.youku.com/")) {
                return true;
            } else {
                view.loadUrl(url);
            }
            return false;
        }


        @Override
        public void onPageFinished(WebView view, String url) {
            // html加载完成之后，添加监听图片的点击js函数
            //  stopProgressDialog();
            // mProgressBar.setVisibility(View.GONE);
//            addImageClickListener();
//            view.loadUrl("javascript:window.android.test();");
            super.onPageFinished(view, url);
        }

        //        webView默认是不处理https请求的，页面显示空白，需要进行如下设置：
//         onReceivedSslError为webView处理ssl证书设置
        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();
            super.onReceivedSslError(view, handler, error);
        }
    }
}
