package com.yc.love.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kk.utils.LogUtil;
import com.yc.love.R;
import com.yc.love.model.base.MySubscriber;
import com.yc.love.model.bean.AResultInfo;
import com.yc.love.model.bean.LoveByStagesDetailsBean;
import com.yc.love.model.engin.LoveEngin;
import com.yc.love.model.single.YcSingle;
import com.yc.love.ui.activity.base.BaseSameActivity;
import com.yc.love.ui.view.NewsScrollView;

/**
 * 实例详情页 Maint2 click to me
 */

public class ExampleDetailActivity extends BaseSameActivity {


    private String mActivityTitle;
    private LoveEngin mLoveEngin;
    private int mId;
    private WebView webView;

    @Override
    protected void initIntentData() {
        Intent intent = getIntent();
        mId = intent.getIntExtra("id", -1);
        mActivityTitle = intent.getStringExtra("title");
    }


    public static void startExampleDetailActivity(Context context, int id, String postTitle) {
        Intent intent = new Intent(context, ExampleDetailActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("title", postTitle);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example_detail);
        mLoveEngin = new LoveEngin(this);
        initViews();
        netData();

    }

    private void initViews() {
        webView = findViewById(R.id.example_detail_webview);
    }

    private void initWebView(String data) {
        WebSettings settings = webView.getSettings();

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

    private class AndroidJavaScript {
        Context mContxt;

        public AndroidJavaScript(Context mContxt) {
            this.mContxt = mContxt;
        }

        @JavascriptInterface
        public void returnAndroid(String name) {//从网页跳回到APP，这个方法已经在上面的HTML中写上了
            if (name.isEmpty() || name.equals("")) {
                return;
            }
            Toast.makeText(getApplication(), name, Toast.LENGTH_SHORT).show();
            //这里写你的操作///////////////////////
            //MainActivity就是一个空页面，不影响
            Intent intent = new Intent(ExampleDetailActivity.this, MainActivity.class);
            intent.putExtra("name", name);
            startActivity(intent);
        }
    }


    private void netData() {
        mLoadingDialog.showLoadingDialog();
        mLoveEngin.detailExample(String.valueOf(String.valueOf(mId)), String.valueOf(YcSingle.getInstance().id), "example/detail").subscribe(new MySubscriber<AResultInfo<LoveByStagesDetailsBean>>(mLoadingDialog) {

            @Override
            protected void onNetNext(AResultInfo<LoveByStagesDetailsBean> listAResultInfo) {
                LoveByStagesDetailsBean loveByStagesDetailsBean = listAResultInfo.data;
                String postContent = loveByStagesDetailsBean.post_content;
                initWebView(postContent);
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
        return mActivityTitle;
    }
}
