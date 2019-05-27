package com.yc.love.ui.activity;
import android.app.Activity;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.yc.love.R;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by wanglin  on 2019/5/27 10:35.
 */
public class TestWebViewActivity extends Activity {

    private WebView webView;
    private String url = "http://u.5gchang.com/loanbox/index.html?agent_id=108";

    private List<String> urlList = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_web_view);

        webView = findViewById(R.id.webView);

        initWebView();
    }


    private void initWebView() {
        final WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
//        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR1)
//            webView.addJavascriptInterface(new JavascriptInterface(), "HTML");

        //其他细节操作
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //关闭webview中缓存 //优先使用缓存:
        webSettings.setAllowFileAccess(true); //设置可以访问本地文件
        webSettings.setJavaScriptCanOpenWindowsAutomatically(false); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
        webSettings.setBlockNetworkImage(true);//设置是否加载网络图片 true 为不加载 false 为加载


        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);


        webSettings.setSaveFormData(true);
        webSettings.setDomStorageEnabled(true);

        webSettings.setAppCacheEnabled(true);

        webSettings.setDatabaseEnabled(true);


        webView.setWebViewClient(new WebViewClient() {
           /* @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.e("TAG", "shouldOverrideUrlLoading: " + url);
                webView.loadUrl(url);
                return false;
            }*/

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.e("TAG", "onPageFinished: " + url);
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
//                Log.e("TAG", "onLoadResource: " + url);


                if (Pattern.compile(".*/\\w+.html.*").matcher(url).find() && !isBack) {
                    urlList.add(url);
                }
            }


            //            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
//                Uri uri = request.getUrl();
//
//                Log.e("TAG", "shouldOverrideUrlLoading: " + uri.getPath());
//
//                return false;
//            }
        });
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
//                Log.e("TAG", "newProgress: " + newProgress);
                if (newProgress == 100) {
                    webSettings.setBlockNetworkImage(false);
                }
            }

            @Override
            public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
                Log.e("TAG", "url: " + url);
                return super.onJsConfirm(view, url, message, result);
            }
        });
        webView.loadUrl(url);

//        webView.setOnKeyListener((v, keyCode, event) -> {
//            if (event.getAction() == KeyEvent.ACTION_DOWN) {
//                if (keyCode == KeyEvent.KEYCODE_BACK && urlList.size() > 0) {
//                    webView.goBack();
//                    return true;
//                }
//            }
//            return false;
//        });
    }

    private boolean isBack;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.e("TAG", "onKeyDown: " + event.getAction() + "  keyCode: " + keyCode + "  canBack: " + webView.canGoBack() + " list: " + urlList.size());
        for (String s : urlList) {
            Log.e("TAG", "onKeyDown: " + s);

        }

        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (keyCode == KeyEvent.KEYCODE_BACK && urlList.size() > 0) {
                webView.loadUrl(urlList.get(0));
                urlList.clear();
                isBack = true;
                return true;
            }
        }

        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        urlList.clear();
    }
}

