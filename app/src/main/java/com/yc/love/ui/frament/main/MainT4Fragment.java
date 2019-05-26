package com.yc.love.ui.frament.main;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.yc.love.R;
import com.yc.love.model.base.MySubscriber;
import com.yc.love.model.bean.AResultInfo;
import com.yc.love.model.bean.MenuadvInfoBean;
import com.yc.love.model.engin.LoveEngin;
import com.yc.love.model.util.SPUtils;
import com.yc.love.ui.activity.MainActivity;
import com.yc.love.ui.frament.base.BaseMainFragment;
import com.yc.love.ui.view.LoadDialog;
import com.yc.love.ui.view.imgs.ImgSelFragment;
import com.yc.love.utils.DownloadedApkUtlis;
import com.yc.love.utils.StatusBarUtil;

/**
 * Created by mayn on 2019/5/22.
 */

public class MainT4Fragment extends BaseMainFragment {

    private WebView mWebView;
    private ProgressBar mProgressBar;

    @Override
    protected int setContentView() {
        return R.layout.fragment_main_t4;
    }

    private String mDownloadIdKey;

    private LoveEngin mLoveEngin;

    @Override
    protected void initViews() {

        mLoveEngin = new LoveEngin(mMainActivity);
        mProgressBar = rootView.findViewById(R.id.main_t4_pb_progress);
        View viewBar = rootView.findViewById(R.id.main_t4_view_bar);
        mWebView = rootView.findViewById(R.id.main_t4_webview);
        mMainActivity.setStateBarHeight(viewBar, 1);
        mWebView.setClickable(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebView.getSettings().setDefaultTextEncodingName("gb2312");
        mWebView.getSettings().setSaveFormData(true);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWebView.getSettings().setAppCacheEnabled(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setDatabaseEnabled(true);


        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.d("mylog", "shouldOverrideUrlLoading: url---------  " + url);
//                mDownloadIdKey = "download_id_".concat("123");
//                Log.d("mylog", "shouldOverrideUrlLoading: mDownloadIdKey " + mDownloadIdKey);
//                DownloadedApkUtlis.downLoadApk(mMainActivity, mDownloadIdKey, url, contentObserver);
                return super.shouldOverrideUrlLoading(view, url);
            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Uri url = request.getUrl();
                Log.d("mylog", "shouldOverrideUrlLoading: url++++++++  " + url);
                return super.shouldOverrideUrlLoading(view, request);
            }
        });
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                //首先我们的进度条是隐藏的
                mProgressBar.setVisibility(View.VISIBLE);//把网页加载的进度传给我们的进度条
                mProgressBar.setProgress(newProgress);
                if (newProgress >= 95) { //加载完毕让进度条消失
                    if (mProgressBar.getVisibility() != View.GONE) {
                        mProgressBar.setVisibility(View.GONE);
                    }
                }
                super.onProgressChanged(view, newProgress);
            }
        });

        mMainActivity.setOnChildDisposeMainKeyDownListent(new MainActivity.OnChildDisposeMainKeyDownListent() {
            @Override
            public boolean onChildDisposeMainKeyDown() {
                Log.d("mylog", "onKeyDown: mWebView.canGoBack() "+mWebView.canGoBack());
                if (mWebView.canGoBack()) {
                    mWebView.goBack();
                    return true;
                }else {
                    mWebView.reload();
                }
                return false;
            }
        });
    }


    @Override
    protected void lazyLoad() {
        netData();
    }

    private void netData() {
        LoadDialog loadDialog = new LoadDialog(mMainActivity);
        loadDialog.show();
        mLoveEngin.menuadvInfo("menuadv/info").subscribe(new MySubscriber<AResultInfo<MenuadvInfoBean>>(loadDialog) {
            @Override
            protected void onNetNext(AResultInfo<MenuadvInfoBean> menuadvInfoBeanAResultInfo) {
                MenuadvInfoBean menuadvInfoBean = menuadvInfoBeanAResultInfo.data;
                String url = menuadvInfoBean.url;

//                url = "http://en.upkao.com";
                //        String url = "https://fir.im/cloudreader";
                Log.d("mylog", "onNetNext: url " + url);
                mWebView.loadUrl(url);
            }

            @Override
            protected void onNetError(Throwable e) {

            }

            @Override
            protected void onNetCompleted() {

            }
        });
    }

    private ContentObserver contentObserver = new ContentObserver(null) {
        @Override
        public void onChange(boolean selfChange) {
            DownloadManager.Query query = new DownloadManager.Query();
            long spDownloadId = (long) SPUtils.get(mMainActivity, mMainActivity.mDownloadIdKey, (long) -1);
            query.setFilterById(spDownloadId);
            DownloadManager dManager = (DownloadManager) mMainActivity.getSystemService(Context.DOWNLOAD_SERVICE);
            final Cursor cursor = dManager.query(query);
            if (cursor != null && cursor.moveToFirst()) {
                final int totalColumn = cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES);
                final int currentColumn = cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR);
                int totalSize = cursor.getInt(totalColumn);
                int currentSize = cursor.getInt(currentColumn);
                float percent = (float) currentSize / (float) totalSize;
                int progress = Math.round(percent * 100);
                Log.d("mylog", "onChange: progress " + progress);
               /* pd.setProgress(progress);
                if (progress == 100) {
                    pd.dismiss();
                }*/
            }
        }
    };


}
