package com.example.jingbin.customview.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.example.jingbin.customview.R;
import com.example.jingbin.customview.utils.CheckNetwork;
import com.example.jingbin.customview.utils.SPUtils;

/**
 * TypedArray其实是用来简化我们的工作的，比如上例，如果布局中的属性的值是引用类型（比如：@dimen/dp100），
 * 如果使用AttributeSet去获得最终的像素值，那么需要第一步拿到id，第二步再去解析id。
 * 而TypedArray正是帮我们简化了这个过程。
 * <p>
 * 网页可以处理:
 * 点击相应控件:拨打电话、发送短信、发送邮件、上传图片、播放视频
 * 进度条、返回网页上一层、显示网页标题
 *
 * 具体请参见: https://github.com/youlookwhat/WebViewStudy
 */
public class DeepUnderstandAttrActivity extends AppCompatActivity {

    private WebView webView;
    /*进度条*/
    private ProgressBar mProgressBar;
    /*进度条是否加载到90%*/
    private boolean progress90;
    /*网页是否加载完成*/
    private boolean pageFinsh;

    /*点击上传图片相关*/
    public ValueCallback<Uri> mUploadMessage;
    public ValueCallback<Uri[]> mUploadMessageForAndroid5;
    public final static int FILECHOOSER_RESULTCODE = 1;
    public final static int FILECHOOSER_RESULTCODE_FOR_ANDROID_5 = 2;
    /*加载视频相关*/
    private MyWebChromeClient xwebchromeclient;
    private FrameLayout video_fullView;// 全屏时视频加载view
    private View xCustomView;
    private WebChromeClient.CustomViewCallback xCustomViewCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_deep_understand_attr);
        setTitle("深入理解Android中的自定义属性");
        initView();
        initWebView();
        webView.loadUrl("http://blog.csdn.net/lmj623565791/article/details/45022631");
    }

    private void initView() {
        webView = (WebView) findViewById(R.id.activity_deep_understand_att);
        mProgressBar = (ProgressBar) findViewById(R.id.pb_progress);
        mProgressBar.setVisibility(View.VISIBLE);
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

        /** 设置字体默认缩放大小（默认总体为16sp，默认缩放比例为110）*/
        int defaultTextZoom = SPUtils.getInt("UserInfo", "default_textZoom", 110);
        ws.setTextZoom(defaultTextZoom);

//        ws.setUseWideViewPort(true); /**合作商页面适应屏幕*/
        xwebchromeclient = new MyWebChromeClient();
        webView.setWebChromeClient(xwebchromeclient);
//        webView.addJavascriptInterface(new VideoAndImageClickInterface(this), "injectedObject");

        webView.setWebViewClient(new MyWebViewClient());
    }

    public class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.startsWith("http://v.youku.com/")) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                intent.addCategory("android.intent.category.DEFAULT");
                intent.addCategory("android.intent.category.BROWSABLE");
                Uri content_url = Uri.parse(url);
                intent.setData(content_url);
                startActivity(intent);
                return true;
            } else if (url.startsWith(WebView.SCHEME_TEL) || url.startsWith("sms:") || url.startsWith(WebView.SCHEME_MAILTO)) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                } catch (android.content.ActivityNotFoundException ignored) {
                }
                return true;
            }
            startProgress90();
            view.loadUrl(url);
            return false;
        }


        @Override
        public void onPageFinished(WebView view, String url) {
            // html加载完成之后，添加监听图片的点击js函数
            //  stopProgressDialog();
            // mProgressBar.setVisibility(View.GONE);
            if (progress90) {
                mProgressBar.setVisibility(View.GONE);
            } else {
                pageFinsh = true;
            }
            if (!CheckNetwork.isNetworkConnected(DeepUnderstandAttrActivity.this)) {
                mProgressBar.setVisibility(View.GONE);
            }
//            DebugUtil.debug(tag, "onPageFinished:" + pageFinsh);
            addImageClickListener();
            super.onPageFinished(view, url);
        }
    }

    // 注入js函数监听
    private void addImageClickListener() {
//        Log.d(tag, "==addImageClickListener");
        // 这段js函数的功能就是，遍历所有的img几点，并添加onclick函数，函数的功能是在图片点击的时候调用本地java接口并传递url过去
        webView.loadUrl("javascript:(function(){" +
                "var objs = document.getElementsByTagName(\"img\");" +
                "for(var i=0;i<objs.length;i++)" +
                "{" +
                //  "objs[i].onclick=function(){alert(this.getAttribute(\"has_link\"));}" +
                "objs[i].onclick=function(){window.injectedObject.imageClick(this.getAttribute(\"src\"),this.getAttribute(\"has_link\"));}" +
                "}" +
                "})()");
    }

    /**
     * 进度条 假装加载到90%
     */
    public void startProgress90() {
        for (int i = 0; i < 900; i++) {
            final int progress = i + 1;
            mProgressBar.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mProgressBar.setProgress(progress);
                    if (progress == 900) {
                        progress90 = true;
                        if (pageFinsh) {
                            startProgress90to100();
                        }
                    }
                }
            }, (i + 1) * 2);
        }
    }

    /**
     * 进度条 加载到100%
     */
    public void startProgress90to100() {
        for (int i = 900; i <= 1000; i++) {
            final int progress = i + 1;
            mProgressBar.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mProgressBar.setProgress(progress);
                    if (progress == 1000) {
                        mProgressBar.setVisibility(View.GONE);
                    }
                }
            }, (i + 1) * 2);
        }
    }

    static class FullscreenHolder extends FrameLayout {

        public FullscreenHolder(Context ctx) {
            super(ctx);
            setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            return true;
        }

    }

    public class MyWebChromeClient extends WebChromeClient {
        private View xprogressvideo;


        // 播放网络视频时全屏会被调用的方法
        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            webView.setVisibility(View.INVISIBLE);
            // 如果一个视图已经存在，那么立刻终止并新建一个
            if (xCustomView != null) {
                callback.onCustomViewHidden();
                return;
            }

            FrameLayout decor = (FrameLayout) getWindow().getDecorView();
            video_fullView = new FullscreenHolder(DeepUnderstandAttrActivity.this);
            video_fullView.addView(view);
            decor.addView(video_fullView);

            xCustomView = view;
            xCustomViewCallback = callback;
            video_fullView.setVisibility(View.VISIBLE);
        }

        // 视频播放退出全屏会被调用的
        @Override
        public void onHideCustomView() {
            if (xCustomView == null)// 不是全屏播放状态
                return;

            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            xCustomView.setVisibility(View.GONE);
            video_fullView.removeView(xCustomView);
            xCustomView = null;
            video_fullView.setVisibility(View.GONE);
            xCustomViewCallback.onCustomViewHidden();
            webView.setVisibility(View.VISIBLE);
        }

        // 视频加载时进程loading
        @Override
        public View getVideoLoadingProgressView() {
            if (xprogressvideo == null) {
                LayoutInflater inflater = LayoutInflater.from(DeepUnderstandAttrActivity.this);
                xprogressvideo = inflater.inflate(R.layout.video_loading_progress, null);
            }
            return xprogressvideo;
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            // mProgressBar.setProgress(newProgress);
            if (progress90) {
                int progress = newProgress * 100;
//                DebugUtil.debug(tag, "progress:" + progress);
                if (progress > 900) {
                    mProgressBar.setProgress(progress);
                    if (progress == 1000) {
                        mProgressBar.setVisibility(View.GONE);
                    }
                }
            }
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            // 设置title
            setTitle(title);
        }

        //扩展浏览器上传文件
        //3.0++版本
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
            openFileChooserImpl(uploadMsg);
        }

        //3.0--版本
        public void openFileChooser(ValueCallback<Uri> uploadMsg) {
            openFileChooserImpl(uploadMsg);
        }

        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
            openFileChooserImpl(uploadMsg);
        }

        // For Android > 5.0
        @Override
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> uploadMsg, WebChromeClient.FileChooserParams fileChooserParams) {
            openFileChooserImplForAndroid5(uploadMsg);
            return true;
        }
    }

    private void openFileChooserImpl(ValueCallback<Uri> uploadMsg) {
        mUploadMessage = uploadMsg;
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("image/*");
        startActivityForResult(Intent.createChooser(i, "文件选择"), FILECHOOSER_RESULTCODE);
    }

    private void openFileChooserImplForAndroid5(ValueCallback<Uri[]> uploadMsg) {
        mUploadMessageForAndroid5 = uploadMsg;
        Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
        contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
        contentSelectionIntent.setType("image/*");

        Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
        chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
        chooserIntent.putExtra(Intent.EXTRA_TITLE, "图片选择");

        startActivityForResult(chooserIntent, FILECHOOSER_RESULTCODE_FOR_ANDROID_5);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage)
                return;
            Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;

        } else if (requestCode == FILECHOOSER_RESULTCODE_FOR_ANDROID_5) {
            if (null == mUploadMessageForAndroid5)
                return;
            Uri result = (intent == null || resultCode != RESULT_OK) ? null : intent.getData();
            if (result != null) {
                mUploadMessageForAndroid5.onReceiveValue(new Uri[]{result});
            } else {
                mUploadMessageForAndroid5.onReceiveValue(new Uri[]{});
            }
            mUploadMessageForAndroid5 = null;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webView.canGoBack()) {
                webView.goBack();
                return true;
            } else {
                webView.loadUrl("about:blank");
                finish();
            }
        }
        return false;
    }
}
