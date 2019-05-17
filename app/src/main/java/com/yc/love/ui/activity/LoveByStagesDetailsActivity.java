package com.yc.love.ui.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kk.utils.LogUtil;
import com.yc.love.R;
import com.yc.love.model.base.MySubscriber;
import com.yc.love.model.bean.AResultInfo;
import com.yc.love.model.bean.LoveByStagesBean;
import com.yc.love.model.bean.LoveByStagesDetailsBean;
import com.yc.love.model.engin.LoveEngin;
import com.yc.love.model.single.YcSingle;
import com.yc.love.ui.activity.base.BaseSameActivity;
import com.yc.love.ui.view.NewsScrollView;

import java.util.List;

public class LoveByStagesDetailsActivity extends BaseSameActivity {

    private LoveEngin mLoveEngin;
    private int mId;
    private WebView webView;
    private String mPostTitle;
    private boolean mIsCollectArticle = false;
    private boolean mIsDigArticle;
    private int mCategoryId;
    private ConstraintLayout mClLikeCon;
    private ProgressBar mProgressBar;
    private String mUrl = "";
    private ImageView mIvLike;

    @Override
    protected void initIntentData() {
        Intent intent = getIntent();
        mId = intent.getIntExtra("id", -1);
        mPostTitle = intent.getStringExtra("post_title");
    }


    public static void startLoveByStagesDetailsActivity(Context context, int id, String postTitle) {
        Intent intent = new Intent(context, LoveByStagesDetailsActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("post_title", postTitle);
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
        mLoveEngin.collectArticle(String.valueOf(id), String.valueOf(mCategoryId), mUrl).subscribe(new MySubscriber<AResultInfo<String>>(mLoadingDialog) {
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
        mLoveEngin.collectArticle(String.valueOf(id), String.valueOf(mCategoryId), mUrl).subscribe(new MySubscriber<AResultInfo<String>>(mLoadingDialog) {

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
        textView.setText(mPostTitle);
        mIvLike.setOnClickListener(this);


        newsScrollView.setOnScrollChangeListener(new NewsScrollView.onScrollChangeListener() {
            @Override
            public void onScrollChange(int l, int t, int oldl, int oldt) {
                if (t > llTitCon.getMeasuredHeight()) {
                    setBarTitle(mPostTitle);
//                    mToolbar.setTitle(title);
                } else {
                    setBarTitle("问答");
//                    mToolbar.setTitle("");
                }
            }
        });
    }


    private void initWebView(final String data) {

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

        Log.d("mylog", "initWebView: data "+data);
//        String body = data.getInfo().getBody();
        webView.loadDataWithBaseURL(null, data, "text/html", "utf-8", null);


        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
            }
        });
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
                    if(mClLikeCon.getVisibility()!=View.VISIBLE){
                        mClLikeCon.setVisibility(View.VISIBLE);
                    }
                }
                super.onProgressChanged(view, newProgress);
            }
        });


        webView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
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
            }
        });
    }


    private void netData() {
        mLoadingDialog.showLoadingDialog();
        mLoveEngin.detailArticle(String.valueOf(String.valueOf(mId)), String.valueOf(YcSingle.getInstance().id), "Article/detail").subscribe(new MySubscriber<AResultInfo<LoveByStagesDetailsBean>>(mLoadingDialog) {

            @Override
            protected void onNetNext(AResultInfo<LoveByStagesDetailsBean> listAResultInfo) {
                LoveByStagesDetailsBean loveByStagesDetailsBean = listAResultInfo.data;
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
        if (isCollectArticle) {
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
        return "问答";
    }
}
