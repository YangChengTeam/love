package com.yc.verbaltalk.skill.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.music.player.lib.util.ToastUtils;
import com.yc.verbaltalk.R;
import com.yc.verbaltalk.base.activity.BaseSameActivity;
import com.yc.verbaltalk.base.activity.MainActivity;
import com.yc.verbaltalk.base.utils.StatusBarUtil;
import com.yc.verbaltalk.base.utils.UserInfoHelper;
import com.yc.verbaltalk.chat.bean.LoveByStagesDetailsBean;
import com.yc.verbaltalk.model.util.SizeUtils;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import io.reactivex.observers.DisposableObserver;
import yc.com.rthttplibrary.bean.ResultInfo;
import yc.com.rthttplibrary.config.HttpConfig;

/**
 * 实例详情页 Maint2 click to me
 */

public class ExampleDetailActivity extends BaseSameActivity {


    private String mActivityTitle;

    private int mId;
    private WebView webView;
    private boolean mIsCollectLovewords = false;
    private int mDetailId;
    private ImageView mIvLike;
    private boolean mIsDigArticle;
    private String mUrl = "";
    private ProgressBar mProgressBar;
    private LinearLayout mClLikeCon;
    private LinearLayout llGetWeChat;
    private LinearLayout llCollect;
    private ImageView ivCollect;
    private TextView tvCollect;
    private View bottomView;
    private LinearLayout llLike;

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

        initViews();
        netData();

    }

    private void initViews() {

        mProgressBar = findViewById(R.id.example_detail_pb_progress);
        mClLikeCon = findViewById(R.id.example_detail_cl_like_con);
        mIvLike = findViewById(R.id.example_detail_iv_like);
        webView = findViewById(R.id.example_detail_webview);
        llGetWeChat = findViewById(R.id.ll_get_wechat);
        llCollect = findViewById(R.id.ll_collect);
        ivCollect = findViewById(R.id.iv_collect);
        tvCollect = findViewById(R.id.tv_collect_count);
        bottomView = findViewById(R.id.rlBottom);
        llLike = findViewById(R.id.ll_like);

        mBaseSameTvSub.setOnClickListener(this);
        mIvLike.setOnClickListener(this);
        llGetWeChat.setOnClickListener(this);
        llCollect.setOnClickListener(this);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        ConstraintLayout.MarginLayoutParams layoutParams = (ConstraintLayout.LayoutParams) bottomView.getLayoutParams();
        LinearLayout.MarginLayoutParams layoutParams1 = (LinearLayout.MarginLayoutParams) llLike.getLayoutParams();
        int bottom = 0;
        int bottom1 = 0;
        if (StatusBarUtil.isNavigationBarExist(this)) {
            bottom = StatusBarUtil.getNavigationBarHeight(this);
            bottom1 = SizeUtils.dp2px(this, 30);
        }

        layoutParams.bottomMargin = bottom;
        bottomView.setLayoutParams(layoutParams);

        layoutParams1.bottomMargin = bottom1;
        llLike.setLayoutParams(layoutParams1);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch (v.getId()) {
            case R.id.example_detail_iv_like:
                if (UserInfoHelper.isLogin(this))
                    netExample(mIsDigArticle);
                break;
            case R.id.activity_base_same_tv_sub:
            case R.id.ll_collect:

                if (UserInfoHelper.isLogin(this))
                    netCollect(mIsCollectLovewords, UserInfoHelper.getUid());
                break;
            case R.id.ll_get_wechat:
                showToWxServiceDialog("shizhan", null);
                break;

        }
    }

    private void netExample(boolean isDigArticle) {

        if (mDetailId <= 0) {

            return;
        }
        if (isDigArticle) {
            mUrl = "Example/undig";
        } else {
            mUrl = "Example/dig";
        }
        mLoadingDialog.showLoadingDialog();
        mLoveEngine.collectExample(UserInfoHelper.getUid(), String.valueOf(mDetailId), mUrl)
                .subscribe(new DisposableObserver<ResultInfo<String>>() {
                    @Override
                    public void onComplete() {
                        mLoadingDialog.dismissLoadingDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mLoadingDialog.dismissLoadingDialog();
                    }

                    @Override
                    public void onNext(ResultInfo<String> stringAResultInfo) {
                        mLoadingDialog.dismissLoadingDialog();
                        if (stringAResultInfo != null && stringAResultInfo.code == HttpConfig.STATUS_OK) {
                            String msg = stringAResultInfo.message;
                            ToastUtils.showCenterToast(msg);
                            mIsDigArticle = !mIsDigArticle;
                            changLikeStaty(mIsDigArticle);
                        }
                    }


                });
    }

    private void initWebView(String data) {
        webView.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent));

        WebSettings settings = webView.getSettings();


//        设置自适应屏幕，两者合用
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
//        settings.setTextZoom(200);//通过设置WebSettings，改变HTML中文字的大小
        settings.setJavaScriptCanOpenWindowsAutomatically(true);//支持通过JS打开新窗口
        //设置WebView属性，能够执行Javascript脚本
        settings.setJavaScriptEnabled(true);//设置js可用
        webView.setWebViewClient(new WebViewClient());
        webView.addJavascriptInterface(new AndroidJavaScript(), "android");//设置js接口
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//支持内容重新布局
        data = formatting(data);

        webView.loadDataWithBaseURL(null, data, "text/html", "utf-8", null);


        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
//                webView.loadUrl("javascript:window.android.resize(document.body.getBoundingClientRect().height)");
                webView.loadUrl("javascript:window.APP.resize(document.body.getScrollHeight())");
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                //首先我们的进度条是隐藏的
                mProgressBar.setVisibility(View.VISIBLE);//把网页加载的进度传给我们的进度条
                mProgressBar.setProgress(newProgress);

                if (newProgress >= 95) { //加载完毕让进度条消失
                    if (mProgressBar.getVisibility() != View.GONE) {
                        mProgressBar.setVisibility(View.GONE);
                    }
                    if (mClLikeCon.getVisibility() != View.VISIBLE) {
                        mClLikeCon.setVisibility(View.VISIBLE);
                    }
                }
                super.onProgressChanged(view, newProgress);
            }
        });

    }

    private String formatting(String data) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<html>");
        stringBuilder.append("<head><style>img{max-width: 100%!important;height:auto!important;}body{background:#fff;position: relative;line-height:1.6;font-family:Microsoft YaHei,Helvetica,Tahoma,Arial,\\5FAE\\8F6F\\96C5\\9ED1,sans-serif}</style></head>");
        stringBuilder.append("<body>");
        stringBuilder.append(data);
        stringBuilder.append("</body></html>");
        return stringBuilder.toString();
    }


    private class AndroidJavaScript {


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

        @JavascriptInterface
        public void resize(float height) {
//            Log.e("TAG", "resize: " + height);
            runOnUiThread(() -> {
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(getResources().getDisplayMetrics().widthPixels - SizeUtils.dp2px(ExampleDetailActivity.this, 10f), (int) (height * getResources().getDisplayMetrics().density));
                layoutParams.leftMargin = SizeUtils.dp2px(ExampleDetailActivity.this, 12f);
//                layoutParams.rightMargin = SizeUtils.dp2px(ExampleDetailActivity.this, 10f);
                webView.setLayoutParams(layoutParams);
            });
        }


    }


    private void netData() {
        mLoadingDialog.showLoadingDialog();
        mLoveEngine.detailExample(String.valueOf(mId), UserInfoHelper.getUid())
                .subscribe(new DisposableObserver<ResultInfo<LoveByStagesDetailsBean>>() {

                    @Override
                    public void onComplete() {
                        mLoadingDialog.dismissLoadingDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mLoadingDialog.dismissLoadingDialog();
                    }

                    @Override
                    public void onNext(ResultInfo<LoveByStagesDetailsBean> loveByStagesDetailsBeanAResultInfo) {
                        mLoadingDialog.dismissLoadingDialog();
                        if (loveByStagesDetailsBeanAResultInfo != null && loveByStagesDetailsBeanAResultInfo.code == HttpConfig.STATUS_OK && loveByStagesDetailsBeanAResultInfo.data != null) {
                            LoveByStagesDetailsBean loveByStagesDetailsBean = loveByStagesDetailsBeanAResultInfo.data;
                            String postContent = loveByStagesDetailsBean.post_content;
                            initWebView(postContent);
                            int collectNum = loveByStagesDetailsBean.collect_num;
//                String collEctResult = String.valueOf(collectNum);
//                if (collectNum > 99) {
//                    collEctResult = "99";
//                }


//                tvCollect.setText(collEctResult);
                            mDetailId = loveByStagesDetailsBean.id;

                            int isCollect = loveByStagesDetailsBean.is_collect;
                            if (isCollect > 0) { //是否收藏
                                mIsCollectLovewords = true;
                            }

                            changSubImg(collectNum, false);

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
                    }
                });
    }

    private void netCollect(boolean isCollect, String userId) {
        mLoadingDialog.showLoadingDialog();
        String url = "";
        if (isCollect) {
            url = "Example/uncollect";
        } else {
            url = "Example/collect";
        }
        mLoveEngine.collectExample(userId, String.valueOf(mDetailId), url)
                .subscribe(new DisposableObserver<ResultInfo<String>>() {
                    @Override
                    public void onComplete() {
                        mLoadingDialog.dismissLoadingDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mLoadingDialog.dismissLoadingDialog();
                    }

                    @Override
                    public void onNext(ResultInfo<String> stringAResultInfo) {
                        mLoadingDialog.dismissLoadingDialog();
                        if (stringAResultInfo != null && stringAResultInfo.code == HttpConfig.STATUS_OK) {
                            String msg = stringAResultInfo.message;
                            ToastUtils.showCenterToast(msg);
                            mIsCollectLovewords = !mIsCollectLovewords;
                            changSubImg(-1, true);
                        }
                    }


                });
    }

    private void changSubImg(int collEctResult, boolean isClick) {


        if (collEctResult < 0) {
            collEctResult = Integer.parseInt(tvCollect.getText().toString().trim());
        }

        if (mIsCollectLovewords) {
            mBaseSameTvSub.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.icon_star_s, 0);
            ivCollect.setImageResource(R.mipmap.search_knack_collected_icon);
            if (isClick) {
                collEctResult = collEctResult + 1;
            }

        } else {
            mBaseSameTvSub.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.icon_star_black, 0);
            ivCollect.setImageResource(R.mipmap.search_knack_collect_icon);
            if (isClick) {
                collEctResult = collEctResult - 1;
            }
        }

        String result = String.valueOf(collEctResult);
        if (collEctResult >= 99) {
            result = "99";
        } else if (collEctResult <= 1) {
            result = "1";
        }
        tvCollect.setText(result);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyWebView();
    }

    public void destroyWebView() {
        if (mProgressBar != null) {
            mProgressBar.clearAnimation();
        }
        if (webView != null) {
            webView.clearHistory();
            webView.clearCache(true);
            webView.loadUrl("about:blank"); // clearView() should be changed to loadUrl("about:blank"), since clearView() is deprecated now mWebView.freeMemory(); mWebView.pauseTimers(); mWebView = null; // Note that mWebView.destroy() and mWebView = null do the exact same thing } }
            webView.destroy();
            webView = null;
        }
    }
}
