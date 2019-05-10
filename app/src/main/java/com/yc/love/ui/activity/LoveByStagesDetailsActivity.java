package com.yc.love.ui.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.yc.love.R;
import com.yc.love.model.base.MySubscriber;
import com.yc.love.model.bean.AResultInfo;
import com.yc.love.model.bean.LoveByStagesBean;
import com.yc.love.model.bean.LoveByStagesDetailsBean;
import com.yc.love.model.engin.LoveEngin;
import com.yc.love.ui.activity.base.BaseSameActivity;

import java.util.List;

public class LoveByStagesDetailsActivity extends BaseSameActivity {

    private LoveEngin mLoveEngin;
    private int mId;
    private WebView webView;

    @Override
    protected void initIntentData() {
        Intent intent = getIntent();
        mId = intent.getIntExtra("id", -1);
    }


    public static void startLoveByStagesDetailsActivity(Context context, int id) {
        Intent intent = new Intent(context, LoveByStagesDetailsActivity.class);
        intent.putExtra("id", id);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_love_by_stages_details);
        mLoveEngin = new LoveEngin(this);
        initViews();
        netData();
    }

    private void initViews() {
        webView = findViewById(R.id.love_by_stages_details_webview);
//        initWebView();


       /* webView = new WebView(getApplicationContext());
        *//*LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(AutoUtils.getPercentWidthSize(6), AutoUtils.getPercentHeightSize(6), AutoUtils.getPercentWidthSize(10), 0);
        mLlSuveryHolder.addView(webView, params);*//*
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setHorizontalScrollBarEnabled(false);//水平不显示
        webView.setVerticalScrollBarEnabled(false); //垂直不显示
        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);//硬件加速*/
 

    }

    /** * 设置WebView自适应屏幕 * @param data 目标字符串 */
    public void initWebView(String data){
        WebSettings settings = webView.getSettings();

        //settings.setUseWideViewPort(true);//调整到适合webview的大小，不过尽量不要用，有些手机有问题
        settings.setLoadWithOverviewMode(true);//设置WebView是否使用预览模式加载界面。
        webView.setVerticalScrollBarEnabled(false);//不能垂直滑动
        webView.setHorizontalScrollBarEnabled(false);//不能水平滑动
        settings.setTextSize(WebSettings.TextSize.LARGEST);//通过设置WebSettings，改变HTML中文字的大小
        settings.setJavaScriptCanOpenWindowsAutomatically(true);//支持通过JS打开新窗口
        //设置WebView属性，能够执行Javascript脚本
        webView.getSettings().setJavaScriptEnabled(true);//设置js可用
        webView.setWebViewClient(new WebViewClient());
        webView.addJavascriptInterface(new AndroidJavaScript(getApplication()), "android");//设置js接口
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//支持内容重新布局
        webView.loadDataWithBaseURL(null, data, "text/html", "utf-8", null);
    }
    /**
     * AndroidJavaScript
     * 本地与h5页面交互的js类，这里写成内部类了
     * returnAndroid方法上@JavascriptInterface一定不能漏了
     */
    private class AndroidJavaScript  {
        Context mContxt;

        public AndroidJavaScript (Context mContxt) {
            this.mContxt = mContxt;
        }

        @JavascriptInterface
        public void returnAndroid(String name) {//从网页跳回到APP，这个方法已经在上面的HTML中写上了
            if (name.isEmpty()||name.equals("")){
                return ;
            }
            showToastShort(name);
            //这里写你的操作///////////////////////
            //MainActivity就是一个空页面，不影响
            Intent intent = new Intent(LoveByStagesDetailsActivity.this, MainActivity.class);
            intent.putExtra("name",name);
            startActivity(intent);
        }
    }


   /* @SuppressLint("SetJavaScriptEnabled")
    private void initWebView() {
//        mProgressBar.setVisibility(View.VISIBLE);
        WebSettings ws = webView.getSettings();
        // 网页内容的宽度是否可大于WebView控件的宽度
        ws.setLoadWithOverviewMode(false);
        // 保存表单数据
        ws.setSaveFormData(true);
        // 是否应该支持使用其屏幕缩放控件和手势缩放
        ws.setSupportZoom(true);
        ws.setBuiltInZoomControls(true);
        ws.setDisplayZoomControls(false);
        // 启动应用缓存
        ws.setAppCacheEnabled(true);
        // 设置缓存模式
        ws.setCacheMode(WebSettings.LOAD_DEFAULT);
        // setDefaultZoom  api19被弃用
        // 设置此属性，可任意比例缩放。
        ws.setUseWideViewPort(true);
        // 不缩放
        webView.setInitialScale(100);
        // 告诉WebView启用JavaScript执行。默认的是false。
        ws.setJavaScriptEnabled(true);
        //  页面加载好以后，再放开图片
        ws.setBlockNetworkImage(false);
        // 使用localStorage则必须打开
        ws.setDomStorageEnabled(true);
        // 排版适应屏幕
        ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        // WebView是否新窗口打开(加了后可能打不开网页)
//        ws.setSupportMultipleWindows(true);

        // webview从5.0开始默认不允许混合模式,https中不能加载http资源,需要设置开启。
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ws.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        *//** 设置字体默认缩放大小(改变网页字体大小,setTextSize  api14被弃用)*//*
        ws.setTextZoom(100);

       *//* mWebChromeClient = new MyWebChromeClient(this);
        webView.setWebChromeClient(mWebChromeClient);
        // 与js交互
        webView.addJavascriptInterface(new ImageClickInterface(this), "injectedObject");
        webView.setWebViewClient(new MyWebViewClient(this));
        webView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return handleLongImage();
            }
        });*//*
    }*/

    private void netData() {
        mLoadingDialog.show();
        mLoveEngin.detailArticle(String.valueOf(String.valueOf(mId)), "Article/detail").subscribe(new MySubscriber<AResultInfo<LoveByStagesDetailsBean>>(mLoadingDialog) {

            @Override
            protected void onNetNext(AResultInfo<LoveByStagesDetailsBean> listAResultInfo) {
                LoveByStagesDetailsBean loveByStagesDetailsBean = listAResultInfo.data;
                Log.d("mylog", "onNetNext: loveByStagesDetailsBean " + loveByStagesDetailsBean.toString());

                initWebView(loveByStagesDetailsBean.post_content);
            }

            @Override
            protected void onNetError(Throwable e) {

            }

            @Override
            protected void onNetCompleted() {

            }
        });
    }

    @Override
    protected String offerActivityTitle() {
        return "问答";
    }
}
