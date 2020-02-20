package com.yc.verbaltalk.base.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.core.content.ContextCompat;

/**
 * Created by wanglin  on 2018/10/8 14:32.
 */
public class CommonWebView extends WebView {
    private static final String TAG = "CommonWebView";

    public CommonWebView(Context context) {
        super(context);
        init(context);
    }

    public CommonWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);

    }

    public CommonWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(final Context context) {
        final WebSettings webSettings = getSettings();

        setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));

        webSettings.setAllowUniversalAccessFromFileURLs(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);// 解决对某些标签的不支持出现白屏
        webSettings.setNeedInitialFocus(false);
        webSettings.setSupportZoom(false);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setAllowFileAccess(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setDatabaseEnabled(true);

        String appCaceDir = context.getDir("cache", Context.MODE_PRIVATE).getPath();
        webSettings.setAppCachePath(appCaceDir);


        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小

//        wvMain.addJavascriptInterface(new NewsDetailActivity.JavascriptInterface(), "HTML");

        //其他细节操作
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //关闭webview中缓存 //优先使用缓存:
        webSettings.setAllowFileAccess(true); //设置可以访问文件
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
        webSettings.setBlockNetworkImage(false);//设置是否加载网络图片 true 为不加载 false 为加载

//       removeJavascriptInterface("searchBoxJavaBridge_");
//        // 加载https网页包含http链接的资源，必须要加这句，否则图片资源加载不出来
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        setDownloadListener((url, userAgent, contentDisposition, mimetype, contentLength) -> {
            //在这里进行下载的处理。
            // 如果你没有进行处理，一般APP就不会开始下载行为，在这里可以自己开启一个线程来下载
            Log.i("download", "url: " + url);
            Log.i("download", "contentDisposition: " + contentDisposition);
            Log.i("download", "mimetype: " + mimetype);

            /**
             * 通过系统下载apk
             */
            if (url.endsWith(".apk") || contentDisposition.endsWith(".apk")) {
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                context.startActivity(intent);
            }
        });

        setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                if (keyCode == KeyEvent.KEYCODE_BACK && canGoBack()) {
                    goBack();
                    return true;
                }
            }
            return false;
        });
        setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                Log.e(TAG, "onReceivedTitle: " + title);
            }
        });

    }
}
