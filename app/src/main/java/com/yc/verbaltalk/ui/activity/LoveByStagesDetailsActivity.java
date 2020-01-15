package com.yc.verbaltalk.ui.activity;

import android.content.Context;
import android.content.Intent;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kk.utils.LogUtil;
import com.yc.verbaltalk.R;
import com.yc.verbaltalk.model.base.MySubscriber;
import com.yc.verbaltalk.model.bean.AResultInfo;
import com.yc.verbaltalk.model.bean.LoveByStagesDetailsBean;
import com.yc.verbaltalk.model.single.YcSingle;
import com.yc.verbaltalk.model.util.SizeUtils;
import com.yc.verbaltalk.ui.activity.base.BaseSameActivity;
import com.yc.verbaltalk.ui.view.NewsScrollView;
import com.yc.verbaltalk.utils.StatusBarUtil;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

public class LoveByStagesDetailsActivity extends BaseSameActivity {


    private int mId;
    private WebView webView;
    private String mPostTitle;
    private boolean mIsCollectArticle = false;
    private boolean mIsDigArticle;
    private int mCategoryId;
    private LinearLayout mClLikeCon;
    private ProgressBar mProgressBar;
    private String mUrl = "";
    private ImageView mIvLike;
    private LinearLayout llGetWeChat;
    private LinearLayout llCollect;
    private ImageView ivCollect;
    private TextView tvCollect;
    private View bottomView;
    private LinearLayout llLike;

    @Override
    protected void initIntentData() {
        Intent intent = getIntent();
        mId = intent.getIntExtra("love_id", -1);
        mPostTitle = intent.getStringExtra("post_title");
    }


    public static void startLoveByStagesDetailsActivity(Context context, int id, String postTitle) {
        Intent intent = new Intent(context, LoveByStagesDetailsActivity.class);
        intent.putExtra("love_id", id);
        intent.putExtra("post_title", postTitle);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_love_by_stages_details);

        initViews();
        netData();

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.activity_base_same_tv_sub:
                netCollectArticle(mIsCollectArticle);
                break;
            case R.id.love_by_stages_details_iv_like:
                netDigArticle(mIsDigArticle);
                break;
            case R.id.ll_get_wechat:
                showToWxServiceDialog(null);
                break;
            case R.id.ll_collect:
                netCollectArticle(mIsCollectArticle);
                break;
        }
    }

    private void netDigArticle(boolean isDigArticle) {
        int id = YcSingle.getInstance().id;
        if (id <= 0) {
            showToLoginDialog();
            return;
        }
        if (mCategoryId <= 0) {
            showToastShort("文章Id为 " + mCategoryId);
            return;
        }
        if (isDigArticle) {
            mUrl = "Article/undig";
        } else {
            mUrl = "Article/dig";
        }
        mLoadingDialog.showLoadingDialog();
        mLoveEngine.collectArticle(String.valueOf(id), String.valueOf(mCategoryId), mUrl).subscribe(new MySubscriber<AResultInfo<String>>(mLoadingDialog) {
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

    private void netCollectArticle(boolean isCollectArticle) {
        int id = YcSingle.getInstance().id;
        if (id <= 0) {
            showToLoginDialog();
            return;
        }
        if (mCategoryId <= 0) {
            showToastShort("文章Id为 " + mCategoryId);
            return;
        }
        if (!isCollectArticle) {
            mUrl = "Article/collect";
        } else {
            mUrl = "Article/uncollect";
        }
        mLoadingDialog.showLoadingDialog();
        mLoveEngine.collectArticle(String.valueOf(id), String.valueOf(mCategoryId), mUrl).subscribe(new MySubscriber<AResultInfo<String>>(mLoadingDialog) {

            @Override
            protected void onNetNext(AResultInfo<String> stringAResultInfo) {
                String msg = stringAResultInfo.msg;
                showToastShort(msg);
                mIsCollectArticle = !mIsCollectArticle;
                changCollectStaty(mIsCollectArticle);
            }

            @Override
            protected void onNetError(Throwable e) {

            }

            @Override
            protected void onNetCompleted() {

            }
        });
    }

    private void initViews() {
        mProgressBar = findViewById(R.id.love_by_stages_details_pb_progress);
        mClLikeCon = findViewById(R.id.love_by_stages_details_cl_like_con);
        mIvLike = findViewById(R.id.love_by_stages_details_iv_like);

        webView = findViewById(R.id.love_by_stages_details_webview);
        TextView textView = findViewById(R.id.love_by_stages_details_tv_name);
        final LinearLayout llTitCon = findViewById(R.id.love_by_stages_details_ll_title_con);
        NewsScrollView newsScrollView = findViewById(R.id.love_by_stages_details_scroll_view);
        llGetWeChat = findViewById(R.id.ll_get_wechat);
        llCollect = findViewById(R.id.ll_collect);
        ivCollect = findViewById(R.id.iv_collect);
        tvCollect = findViewById(R.id.tv_collect_count);
        bottomView = findViewById(R.id.rlBottom);
        llLike = findViewById(R.id.ll_like);


        textView.setText(mPostTitle);
        mIvLike.setOnClickListener(this);

        llGetWeChat.setOnClickListener(this);
        llCollect.setOnClickListener(this);


        newsScrollView.setOnScrollChangeListener((l, t, oldl, oldt) -> {
            if (t > llTitCon.getMeasuredHeight()) {
                setBarTitle(mPostTitle);
//                    mToolbar.setTitle(title);
            } else {
                setBarTitle("问答");
//                    mToolbar.setTitle("");
            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        ConstraintLayout.MarginLayoutParams layoutParams = (ConstraintLayout.LayoutParams) bottomView.getLayoutParams();
        LinearLayout.MarginLayoutParams layoutParams1 = (LinearLayout.MarginLayoutParams) llLike.getLayoutParams();
        int bottom = 0;
        int bottom1=0;
        if (StatusBarUtil.isNavigationBarExist(this)) {
            bottom = StatusBarUtil.getNavigationBarHeight(this);
            bottom1 = SizeUtils.dp2px(this, 30);
        }


        layoutParams1.bottomMargin = bottom1;
        llLike.setLayoutParams(layoutParams1);

        layoutParams.bottomMargin = bottom;
        bottomView.setLayoutParams(layoutParams);
    }

    private void initWebView(String data) {

        webView.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent));
        final WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
       /* if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR1)
            webView.addJavascriptInterface(new JavascriptInterface(), "HTML");*/

        //其他细节操作
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //关闭webview中缓存 //优先使用缓存:
        webSettings.setAllowFileAccess(true); //设置可以访问本地文件
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
//        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
        webSettings.setBlockNetworkImage(true);//设置是否加载网络图片 true 为不加载 false 为加载


        Log.d("mylog", "initWebView: data " + data);
        data = formatting(data);
        webView.addJavascriptInterface(new MyJavaScript(), "android");
        webView.loadDataWithBaseURL(null, data, "text/html", "utf-8", null);


        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

//                javascript:window.android.resize(document.body.getBoundingClientRect().height)
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


        webView.setOnLongClickListener(v -> {
            // 长按事件监听（注意：需要实现LongClickCallBack接口并传入对象）

            final WebView.HitTestResult htr = webView.getHitTestResult();//获取所点击的内容
            if (htr.getType() == WebView.HitTestResult.IMAGE_TYPE
                    || htr.getType() == WebView.HitTestResult.IMAGE_ANCHOR_TYPE
                    || htr.getType() == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {
                //判断被点击的类型为图片

//                    showQRCodeDialog(htr.getExtra());

            }

            LogUtil.msg("url: " + htr.getExtra());

            return false;
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

    private void netData() {
        mLoadingDialog.showLoadingDialog();
        mLoveEngine.detailArticle(String.valueOf(String.valueOf(mId)), String.valueOf(YcSingle.getInstance().id), "Article/detail").subscribe(new MySubscriber<AResultInfo<LoveByStagesDetailsBean>>(mLoadingDialog) {

            @Override
            protected void onNetNext(AResultInfo<LoveByStagesDetailsBean> listAResultInfo) {
                LoveByStagesDetailsBean loveByStagesDetailsBean = listAResultInfo.data;

                int collectNum = loveByStagesDetailsBean.collect_num;
                String collEctResult = String.valueOf(loveByStagesDetailsBean.collect_num);
                if (collectNum == 0) {
                    collEctResult = "50";
                } else if (collectNum > 99) {
                    collEctResult = "99+";
                }

                tvCollect.setText(collEctResult);

                //收藏
                int isCollect = loveByStagesDetailsBean.is_collect;
                switch (isCollect) {
                    case 0:
                        break;
                    case 1:
                        mIsCollectArticle = true;
                        break;
                }
                changCollectStaty(mIsCollectArticle);
                mBaseSameTvSub.setOnClickListener(LoveByStagesDetailsActivity.this);

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
//                mClLikeCon.setVisibility(View.VISIBLE);

                mCategoryId = loveByStagesDetailsBean.id;

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

    private void changCollectStaty(boolean isCollectArticle) {
        String count = tvCollect.getText().toString().trim();
        if (isCollectArticle) {
            mBaseSameTvSub.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.icon_star_s, 0);
            ivCollect.setImageResource(R.mipmap.search_knack_collected_icon);
            int countInt = Integer.parseInt(count);
            tvCollect.setText(String.valueOf(++countInt));
        } else {
            mBaseSameTvSub.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.icon_star_black, 0);
            ivCollect.setImageResource(R.mipmap.search_knack_collect_icon);
            int countInt = Integer.parseInt(count);
            tvCollect.setText(String.valueOf(--countInt));
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
        return "问答";
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


    public class MyJavaScript {
        @JavascriptInterface
        public void resize(float height) {
//            Log.e("TAG", "resize: " + height);
            runOnUiThread(() -> {
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(getResources().getDisplayMetrics().widthPixels - SizeUtils.dp2px(LoveByStagesDetailsActivity.this, 12f), (int) (height * getResources().getDisplayMetrics().density));
                layoutParams.leftMargin = SizeUtils.dp2px(LoveByStagesDetailsActivity.this, 12f);

                webView.setLayoutParams(layoutParams);
            });
        }
    }
}
