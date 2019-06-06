package com.yc.love.ui.frament.main;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.qw.soul.permission.SoulPermission;
import com.qw.soul.permission.bean.Permission;
import com.qw.soul.permission.callbcak.CheckRequestPermissionListener;
import com.umeng.analytics.MobclickAgent;
import com.yc.love.R;
import com.yc.love.model.base.MySubscriber;
import com.yc.love.model.bean.AResultInfo;
import com.yc.love.model.bean.MenuadvInfoBean;
import com.yc.love.model.constant.ConstantKey;
import com.yc.love.model.engin.LoveEngin;
import com.yc.love.model.util.SPUtils;
import com.yc.love.ui.activity.MainActivity;
import com.yc.love.ui.frament.base.BaseMainFragment;
import com.yc.love.ui.view.LoadDialog;
import com.yc.love.utils.DownloadedApkUtlis;

/**
 * Created by mayn on 2019/5/22.
 */

public class MainT4Fragment extends BaseMainFragment implements View.OnClickListener {

    private WebView mWebView;
    private ProgressBar mProgressBar;
    //    private String mHomeTitle;
//    private String mHomeUrl;
    //    private List<String> urlList = new ArrayList<>();
//    private boolean mIsCanToHome;
    private String homeUrl;
    private int mProgress;
    //    private boolean mIsHome;

    @Override
    protected int setContentView() {
        return R.layout.fragment_main_t4;
    }


    private String mDownloadIdKey;

    private LoveEngin mLoveEngin;
    private boolean isLoadUrl = false;

    @Override
    protected void initViews() {


        View viewBar = rootView.findViewById(R.id.main_t4_view_bar);
        mMainActivity.setStateBarHeight(viewBar, 1);
        mLoveEngin = new LoveEngin(mMainActivity);

        TextView tvBen = rootView.findViewById(R.id.main_t4_tv_btn);
        tvBen.setOnClickListener(this);


//        initWebView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_t4_tv_btn:

                SoulPermission.getInstance().checkAndRequestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        //if you want do noting or no need all the callbacks you may use SimplePermissionAdapter instead
                        new CheckRequestPermissionListener() {
                            @Override
                            public void onPermissionOk(Permission permission) {
                                String downloadUrl = "http://sp.5gchang.com/Uploads/apk/2/ssyy.apk";
                                long deadline = System.currentTimeMillis();
                                DownloadedApkUtlis.downLoadApk(mMainActivity, downloadUrl, contentObserver,String.valueOf(deadline));
                                MobclickAgent.onEvent(mMainActivity, ConstantKey.UM_DOWNLOAD_OUT_ID);
                                showDownloadDialog();
                            }

                            @Override
                            public void onPermissionDenied(Permission permission) {
//                                Activity activity = SoulPermission.getInstance().getTopActivity();
                                /*if (null == activity) {
                                    return;
                                }*/
                                //绿色框中的流程
                                //用户第一次拒绝了权限、并且没有勾选"不再提示"这个值为true，此时告诉用户为什么需要这个权限。
                                if (permission.shouldRationale()) {
                                    mMainActivity.showToastShort("未获取到存储权限");
                                } else {
                                    //此时请求权限会直接报未授予，需要用户手动去权限设置页，所以弹框引导用户跳转去设置页
                                    String permissionDesc = permission.getPermissionNameDesc();
                                    new AlertDialog.Builder(mMainActivity)
                                            .setTitle("提示")
                                            .setMessage(permissionDesc + "异常，请前往设置－>权限管理，打开" + permissionDesc + "。")
                                            .setPositiveButton("去设置", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    //去设置页
                                                    SoulPermission.getInstance().goPermissionSettings();
                                                }
                                            }).create().show();
                                }
                            }
                        });


                break;
        }
    }

    private ContentObserver contentObserver = new ContentObserver(null) {
        @Override
        public void onChange(boolean selfChange) {
            DownloadManager.Query query = new DownloadManager.Query();
            long spDownloadId = (long) SPUtils.get(mMainActivity, SPUtils.DOWNLOAD_OUT_ID, (long) -1);
            query.setFilterById(spDownloadId);
            DownloadManager dManager = (DownloadManager) mMainActivity.getSystemService(Context.DOWNLOAD_SERVICE);
            final Cursor cursor = dManager.query(query);
            if (cursor != null && cursor.moveToFirst()) {
                final int totalColumn = cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES);
                final int currentColumn = cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR);
                int totalSize = cursor.getInt(totalColumn);
                int currentSize = cursor.getInt(currentColumn);
                float percent = (float) currentSize / (float) totalSize;
                mProgress = Math.round(percent * 100);
                Log.d("mylog", "onChange: progress " + mProgress);
                if (pd != null) {
                    pd.setProgress(mProgress);
                    if (mProgress == 100) {
                        pd.dismiss();
                    }
                }
            }
        }
    };

    private ProgressDialog pd; // 进度条对话框

    private void showDownloadDialog() {
        pd = new ProgressDialog(mMainActivity);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setMessage("文件正在下载");
        pd.setMax(100);
        pd.show();
        pd.setCancelable(false);

        /*AlertDialog alertDialog = new AlertDialog.Builder(mMainActivity).create();
        alertDialog.setTitle("文件正在下载");
//        alertDialog.setMessage("文件正在下载");
        alertDialog.set
        DialogInterface.OnClickListener listener = null;
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", listener);
        alertDialog.show();*/
    }

    private void initWebView() {
        mProgressBar = rootView.findViewById(R.id.main_t4_pb_progress);
        mWebView = rootView.findViewById(R.id.main_t4_webview);

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
            /*@Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.d("mylog", "shouldOverrideUrlLoading: url---------  " + url);
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    view.loadUrl(request.getUrl().toString());
                } else {
                    view.loadUrl(request.toString());
                }
                return true;
            }*/

            @Override
            public void onLoadResource(WebView view, String url) {
//                mIsCanToHome = false;
//                Log.d("mylog", "onLoadResource: url " + url);
                super.onLoadResource(view, url);
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

            /*@Override
            public void onReceivedIcon(WebView view, Bitmap icon) {
                Log.d("mylog", "onReceivedIcon: icon " + icon);
                super.onReceivedIcon(view, icon);
            }*/
        });

        mMainActivity.setOnChildDisposeMainKeyDownListent(new MainActivity.OnChildDisposeMainKeyDownListent() {

            private boolean mIsCanBack;

            @Override
            public boolean onChildDisposeMainKeyDown() {
                Log.d("mylog", "onChildDisposeMainKeyDown: mWebView.canGoBack " + mWebView.canGoBack());
                if (mWebView.canGoBack()) {
                    mWebView.goBack();
                    return true;
                } else {
                    Log.d("mylog", "onChildDisposeMainKeyDown: mIsCanBack " + mIsCanBack);
                    if (mIsCanBack) {
                        return false;
                    }
                    mWebView.loadUrl(homeUrl);
                    mIsCanBack = true;
                    mWebView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mIsCanBack = false;
                        }
                    }, 2500);
//                    mWebView.
                    return true;
                }
            }
        });

        /**
         * VOVO 8.0手机莫名闪退
         */
        mWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        mWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
    }


    @Override
    protected void lazyLoad() {
        MobclickAgent.onEvent(mMainActivity, ConstantKey.UM_WELFEAR_ID);
//        netData();
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
                MainT4Fragment.this.homeUrl = url;
//                urlList.add(url);
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
    public void onDestroy() {
        super.onDestroy();
        destroyWebView();
    }

    public void destroyWebView() {
        if (mProgressBar != null) {
            mProgressBar.clearAnimation();
        }
        if (mWebView != null) {
            mWebView.clearHistory();
            mWebView.clearCache(true);
            mWebView.loadUrl("about:blank"); // clearView() should be changed to loadUrl("about:blank"), since clearView() is deprecated now mWebView.freeMemory(); mWebView.pauseTimers(); mWebView = null; // Note that mWebView.destroy() and mWebView = null do the exact same thing } }
            mWebView.destroy();
            mWebView = null;
        }

    }


}
