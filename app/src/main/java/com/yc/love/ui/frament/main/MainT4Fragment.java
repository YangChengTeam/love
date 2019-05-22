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
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.yc.love.R;
import com.yc.love.model.util.SPUtils;
import com.yc.love.ui.activity.MainActivity;
import com.yc.love.ui.frament.base.BaseMainFragment;
import com.yc.love.ui.view.imgs.ImgSelFragment;
import com.yc.love.utils.DownloadedApkUtlis;

/**
 * Created by mayn on 2019/5/22.
 */

public class MainT4Fragment extends BaseMainFragment {
    @Override
    protected int setContentView() {
        return R.layout.fragment_main_t4;
    }

    private String mDownloadIdKey;

    @Override
    protected void initViews() {
        View viewBar = rootView.findViewById(R.id.main_t4_view_bar);
        final WebView webView = rootView.findViewById(R.id.main_t4_webview);

        mMainActivity.setStateBarHeight(viewBar, 1);


        webView.setClickable(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.getSettings().setDefaultTextEncodingName("gb2312");
        webView.getSettings().setSaveFormData(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setDatabaseEnabled(true);

        String url = "http://en.upkao.com";
        webView.loadUrl(url);

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.d("mylog", "shouldOverrideUrlLoading: url---------  " + url);
                mDownloadIdKey = "download_id_".concat(url);
                DownloadedApkUtlis.downLoadApk(mMainActivity, mDownloadIdKey, url, contentObserver);
                return super.shouldOverrideUrlLoading(view, url);
            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String url1 = request.getUrl().toString();
                Log.d("mylog", "shouldOverrideUrlLoading: url " + url1);
                mDownloadIdKey = "download_id_".concat(url1);
                DownloadedApkUtlis.downLoadApk(mMainActivity, mDownloadIdKey, url1, contentObserver);
                return super.shouldOverrideUrlLoading(view, request);
            }
        });

       /* webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(final String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                mMainActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //使用前先判断是否有读取、写入内存卡权限
                        if (ContextCompat.checkSelfPermission(mMainActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(mMainActivity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_REQUEST_CODE);
                        } else {


                            downloadAPK.downloadAPK(url, "***.apk");//DownLoader 需要在oncreate 中初始化
                        }
                    }
                });


            }
        });*/
    }

    /*@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case STORAGE_REQUEST_CODE:
                if (grantResults.length >= 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .add(R.id.fmImageList, ImgSelFragment.instance(), null)
                            .commitAllowingStateLoss();
                } else {
                    mMainActivity.showToastShort("无法访问存储卡");
                }
                break;
            default:
                break;
        }
    }*/

    private static final int STORAGE_REQUEST_CODE = 1;

    @Override
    protected void lazyLoad() {

    }

    private ContentObserver contentObserver = new ContentObserver(null) {
        @Override
        public void onChange(boolean selfChange) {
            DownloadManager.Query query = new DownloadManager.Query();
            long spDownloadId = (long) SPUtils.get(mMainActivity, mDownloadIdKey, (long) -1);
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
