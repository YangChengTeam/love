package com.yc.verbaltalk.mine.ui.fragment;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.umeng.analytics.MobclickAgent;
import com.yc.verbaltalk.R;
import com.yc.verbaltalk.chat.bean.AResultInfo;
import com.yc.verbaltalk.chat.bean.MenuadvInfoBean;
import com.yc.verbaltalk.model.constant.ConstantKey;
import com.yc.verbaltalk.base.engine.LoveEngine;
import com.yc.verbaltalk.base.activity.MainActivity;
import com.yc.verbaltalk.base.fragment.BaseMainFragment;
import com.yc.verbaltalk.base.view.LoadDialog;
import com.yc.verbaltalk.base.utils.CommonInfoHelper;

import androidx.appcompat.app.AlertDialog;
import rx.Subscriber;

/**
 * Created by mayn on 2019/5/22.
 * 福利
 */

public class WealFragment extends BaseMainFragment {

    private WebView mWebView;
    private ProgressBar mProgressBar;

    private String homeUrl;

    private String mWechat;

    private LoadDialog mLoadDialog;


    private LoveEngine mLoveEngin;

    @Override
    protected int setContentView() {
        return R.layout.fragment_main_t4;
    }


    @Override
    protected void initViews() {


        View viewBar = rootView.findViewById(R.id.main_t4_view_bar);
        mMainActivity.setStateBarHeight(viewBar, 1);
        mLoveEngin = new LoveEngine(mMainActivity);
        mProgressBar = rootView.findViewById(R.id.main_t4_pb_progress);
        mWebView = rootView.findViewById(R.id.main_t4_webview);
//        initWebView();
    }


    private void initWebView(String url) {


        mWebView.setClickable(true);
        WebSettings settings = mWebView.getSettings();
        settings.setUseWideViewPort(true);

        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setJavaScriptEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
//        settings.setDefaultTextEncodingName("gb2312");
        settings.setSaveFormData(true);
        settings.setDomStorageEnabled(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setAppCacheEnabled(true);
        settings.setLoadWithOverviewMode(true);
        settings.setDatabaseEnabled(true);
        mWebView.addJavascriptInterface(new JsInterface(), "android");
        settings.setLoadsImagesAutomatically(true);

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
                    mWebView.postDelayed(() -> mIsCanBack = false, 2500);

                    return true;
                }
            }
        });
        mWebView.loadUrl(url);

        /**
         * VOVO 8.0手机莫名闪退
         */
        mWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        mWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
    }


    @Override
    protected void lazyLoad() {
        MobclickAgent.onEvent(mMainActivity, ConstantKey.UM_WELFEAR_ID);
        netData();
    }


    private void getCache() {
        CommonInfoHelper.getO(mMainActivity, "main4_menuadv_info", new TypeReference<MenuadvInfoBean>() {
        }.getType(), (CommonInfoHelper.onParseListener<MenuadvInfoBean>) o -> {

            if (o != null) {
                loadWebViewData(o);
                mLoadDialog.dismissLoadingDialog();
            }
        });
    }

    private void netData() {

        mLoadDialog = new LoadDialog(mMainActivity);
        mLoadDialog.showLoadingDialog();

        mLoveEngin.menuadvInfo("menuadv/info").subscribe(new Subscriber<AResultInfo<MenuadvInfoBean>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
//                getCache();
                if (mLoadDialog != null) mLoadDialog.dismissLoadingDialog();
            }

            @Override
            public void onNext(AResultInfo<MenuadvInfoBean> menuadvInfoBeanAResultInfo) {
                if (mLoadDialog != null) mLoadDialog.dismissLoadingDialog();
                if (menuadvInfoBeanAResultInfo != null && menuadvInfoBeanAResultInfo.code == HttpConfig.STATUS_OK && menuadvInfoBeanAResultInfo.data != null) {
                    MenuadvInfoBean menuadvInfoBean = menuadvInfoBeanAResultInfo.data;

                    //不需要重复加载
                    loadWebViewData(menuadvInfoBean);
//                    mCacheWorker.setCache("main4_menuadv_info", menuadvInfoBean);
                    CommonInfoHelper.setO(mMainActivity, menuadvInfoBean, "main4_menuadv_info");
                }
            }

        });
    }

    private void loadWebViewData(MenuadvInfoBean menuadvInfoBean) {
        String url = menuadvInfoBean.url;
        mWechat = menuadvInfoBean.wechat;
//                url = "http://en.upkao.com";
        //        String url = "https://fir.im/cloudreader";
        Log.d("mylog", "onNetNext: url " + url);
//        mWebView.loadUrl(url);
        initWebView(url);
        WealFragment.this.homeUrl = url;
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

    public class JsInterface {


        //JS交互
        @JavascriptInterface
        public void toNext() {
            MobclickAgent.onEvent(mMainActivity, ConstantKey.UM_WECHAT_ID);
            Log.d("mylog", "toNext: ---------------");
//            mMainActivity.showToastShort("123456");
            if (!TextUtils.isEmpty(mWechat)) {
                ClipboardManager myClipboard = (ClipboardManager) mMainActivity.getSystemService(mMainActivity.CLIPBOARD_SERVICE);
                ClipData myClip = ClipData.newPlainText("text", mWechat);
                myClipboard.setPrimaryClip(myClip);
//                mMainActivity.showToastShort("微信号已复制到剪切板");
                AlertDialog alertDialog = new AlertDialog.Builder(mMainActivity).create();
                alertDialog.setMessage("微信号已复制到剪切板,去添加好友吧");
                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", (dialogInterface, i) -> openWeiXin());
                DialogInterface.OnClickListener listent = null;
                alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", listent);
                alertDialog.show();
            }
        }

        private void openWeiXin() {

            try {
                Intent intent = new Intent();
                ComponentName cmp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI");
                intent.setAction(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setComponent(cmp);
                mMainActivity.startActivity(intent);
            } catch (Exception e) {
                mMainActivity.showToastShort("未安装微信");
            }


        }
    }


}
