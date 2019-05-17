package com.yc.love.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
    private boolean mIsCollectLovewords = false;
    private int mDetailId;
    private ImageView mIvLike;
    private boolean mIsDigArticle;
    private String mUrl = "";
    private ProgressBar mProgressBar;
    private ConstraintLayout mClLikeCon;

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
        mProgressBar = findViewById(R.id.example_detail_pb_progress);
        mClLikeCon = findViewById(R.id.example_detail_cl_like_con);
        mIvLike = findViewById(R.id.example_detail_iv_like);
        webView = findViewById(R.id.example_detail_webview);

        mBaseSameTvSub.setOnClickListener(this);
        mIvLike.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.example_detail_iv_like:
                netExample(mIsDigArticle);
                break;
            case R.id.activity_base_same_tv_sub:
                int id = YcSingle.getInstance().id;
                if (id <= 0) {
                    showToLoginDialog();
                    return;
                }
                netCollect(mIsCollectLovewords, id);
                break;
        }
    }

    private void netExample(boolean isDigArticle) {
        int id = YcSingle.getInstance().id;
        if (id <= 0) {
            showToLoginDialog();
            return;
        }
        if (mDetailId <= 0) {
            showToastShort("实例Id为 " + mDetailId);
            return;
        }
        if (isDigArticle) {
            mUrl = "Example/undig";
        } else {
            mUrl = "Example/dig";
        }
        mLoadingDialog.showLoadingDialog();
        mLoveEngin.collectExample(String.valueOf(id), String.valueOf(mDetailId), mUrl).subscribe(new MySubscriber<AResultInfo<String>>(mLoadingDialog) {
            @Override
            protected void onNetNext(AResultInfo<String> stringAResultInfo) {
                String msg = stringAResultInfo.msg;
                showToastShort(msg);
                mIsDigArticle = !mIsDigArticle;
                changLikeStaty(mIsDigArticle);
            }

            @Override
            protected void onNetError(Throwable e) {
            }

            @Override
            protected void onNetCompleted() {
            }
        });
    }

    private void initWebView(String data) {

        WebSettings settings = webView.getSettings();

        //设置自适应屏幕，两者合用
        settings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        settings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小

        /*SMALLEST(50%),
            SMALLER(75%),
            NORMAL(100%),
            LARGER(150%),
            LARGEST(200%); */
//        settings.setTextSize(WebSettings.TextSize.NORMAL);

        settings.setLoadWithOverviewMode(true);//设置WebView是否使用预览模式加载界面。
        webView.setVerticalScrollBarEnabled(false);//不能垂直滑动
        webView.setHorizontalScrollBarEnabled(false);//不能水平滑动
//        settings.setTextSize(WebSettings.TextSize.LARGEST);//通过设置WebSettings，改变HTML中文字的大小
        settings.setJavaScriptCanOpenWindowsAutomatically(true);//支持通过JS打开新窗口
        //设置WebView属性，能够执行Javascript脚本
        settings.setJavaScriptEnabled(true);//设置js可用
        webView.setWebViewClient(new WebViewClient());
        webView.addJavascriptInterface(new AndroidJavaScript(getApplication()), "android");//设置js接口
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//支持内容重新布局

        webView.loadDataWithBaseURL(null, data, "text/html", "utf-8", null);

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                //首先我们的进度条是隐藏的
                mProgressBar.setVisibility(View.VISIBLE);//把网页加载的进度传给我们的进度条
                mProgressBar.setProgress(newProgress);

                if(newProgress>=95){ //加载完毕让进度条消失
                    if(mProgressBar.getVisibility()!=View.GONE){
                        mProgressBar.setVisibility(View.GONE);
                    }
                    if(mClLikeCon.getVisibility()!=View.INVISIBLE){
                        mClLikeCon.setVisibility(View.VISIBLE);
                    }
                }
                super.onProgressChanged(view, newProgress);
            }
        });
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

                mDetailId = loveByStagesDetailsBean.id;

                int isCollect = loveByStagesDetailsBean.is_collect;
                Log.d("mylog", "onNetNext: isCollect " + isCollect + " loveHealingBean.chat_name " + loveByStagesDetailsBean.post_content);
                if (isCollect > 0) { //是否收藏
                    mIsCollectLovewords = true;
                }
                changSubImg();

                //点赞
                int isLike = loveByStagesDetailsBean.is_like;
                switch (isLike) {
                    case 0:
                        break;
                    case 1:
                        mIsDigArticle = true;
                        break;
                }
                changLikeStaty(mIsDigArticle);
            }

            @Override
            protected void onNetError(Throwable e) {

            }

            @Override
            protected void onNetCompleted() {

            }
        });
    }

    private void netCollect(boolean isCollect, int userId) {
        mLoadingDialog.showLoadingDialog();
        String url = "";
        if (isCollect) {
            url = "Example/uncollect";
        } else {
            url = "Example/collect";
        }
        mLoveEngin.collectExample(String.valueOf(userId), String.valueOf(mDetailId), url)
                .subscribe(new MySubscriber<AResultInfo<String>>(mLoadingDialog) {
                    @Override
                    protected void onNetNext(AResultInfo<String> listAResultInfo) {
                        String msg = listAResultInfo.msg;
                        showToastShort(msg);
                        mIsCollectLovewords = !mIsCollectLovewords;
                        changSubImg();
                    }

                    @Override
                    protected void onNetError(Throwable e) {

                    }

                    @Override
                    protected void onNetCompleted() {

                    }
                });
    }

    private void changSubImg() {
        if (mIsCollectLovewords) {
            mBaseSameTvSub.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.icon_star_s, 0);
        } else {
            mBaseSameTvSub.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.icon_star_black, 0);
        }
    }

    private void changLikeStaty(boolean isLikeArticle) {
        if (isLikeArticle) {
            mIvLike.setImageDrawable(getResources().getDrawable(R.mipmap.icon_like_s));
        } else {
            mIvLike.setImageDrawable(getResources().getDrawable(R.mipmap.icon_like_gray));
        }
    }

    @Override
    protected String offerActivityTitle() {
        return mActivityTitle;
    }
}
