package com.yc.love.ui.activity;

import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.yc.love.R;
import com.yc.love.adaper.vp.MainPagerAdapter;
import com.yc.love.factory.MainFragmentFactory;
import com.yc.love.model.domain.URLConfig;
import com.yc.love.model.single.YcSingle;
import com.yc.love.model.util.DownloadedApkUtlis;
import com.yc.love.model.util.SPUtils;
import com.yc.love.receiver.NetWorkChangReceiver;
import com.yc.love.ui.activity.base.BaseActivity;
import com.yc.love.ui.view.ControlScrollViewPager;
import com.yc.love.utils.InstallApkUtlis;

//import cn.trinea.android.common.util.PreferencesUtils;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private ControlScrollViewPager mVpFragment;
    private TextView mTvTab1, mTvTab2, mTvTab3, mTvTab4;
    private boolean isRegistered = false;
    private NetWorkChangReceiver netWorkChangReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        invadeStatusBar(); //侵入状态栏
        setAndroidNativeLightStatusBar(); //状态栏字体颜色改变

        initView();

        checkUpdate();
    }


    private void initNetWorkChangReceiver() {
        //注册网络状态监听广播
        netWorkChangReceiver = new NetWorkChangReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(netWorkChangReceiver, filter);
        isRegistered = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //解绑
        if (isRegistered) {
            unregisterReceiver(netWorkChangReceiver);
        }
    }

    private void initView() {


        mVpFragment = findViewById(R.id.comp_main_vp_fragment);
        mTvTab1 = findViewById(R.id.comp_main_tv_tab_1);
        mTvTab2 = findViewById(R.id.comp_main_tv_tab_2);
        mTvTab3 = findViewById(R.id.comp_main_tv_tab_3);
        mTvTab4 = findViewById(R.id.comp_main_tv_tab_4);

        mTvTab1.setOnClickListener(this);
        mTvTab2.setOnClickListener(this);
        mTvTab3.setOnClickListener(this);
        mTvTab4.setOnClickListener(this);

        MainPagerAdapter mainPagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
        mVpFragment.setAdapter(mainPagerAdapter);


        mTvTab1.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d("mylog", "run: postDelayed postDelayed ");
                initNetWorkChangReceiver();
            }
        }, 200);
    }

    public void scroolToHomeFragment() {
        mVpFragment.setCurrentItem(MainFragmentFactory.MAIN_FRAGMENT_0, false);
        iconSelect(MainFragmentFactory.MAIN_FRAGMENT_0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.comp_main_tv_tab_1:
                mVpFragment.setCurrentItem(MainFragmentFactory.MAIN_FRAGMENT_0, false);
                iconSelect(MainFragmentFactory.MAIN_FRAGMENT_0);
                break;
            case R.id.comp_main_tv_tab_2:
                mVpFragment.setCurrentItem(MainFragmentFactory.MAIN_FRAGMENT_1, false);
                iconSelect(MainFragmentFactory.MAIN_FRAGMENT_1);
                break;
            case R.id.comp_main_tv_tab_3:
                mVpFragment.setCurrentItem(MainFragmentFactory.MAIN_FRAGMENT_2, false);
                iconSelect(MainFragmentFactory.MAIN_FRAGMENT_2);
                break;
            case R.id.comp_main_tv_tab_4:
                int id = YcSingle.getInstance().id;
                if (id <= 0) {
                    showToLoginDialog();
                    return;
                }
                mVpFragment.setCurrentItem(MainFragmentFactory.MAIN_FRAGMENT_3, false);
                iconSelect(MainFragmentFactory.MAIN_FRAGMENT_3);
                break;
        }
    }

    private void iconSelect(int current) {
        cleatIconState();
        switch (current) {
            case MainFragmentFactory.MAIN_FRAGMENT_0:
                setCompoundDrawablesTop(mTvTab1, R.mipmap.main_icon_tab_01_s);
                break;
            case MainFragmentFactory.MAIN_FRAGMENT_1:
                setCompoundDrawablesTop(mTvTab2, R.mipmap.main_icon_tab_02_s);
                break;
            case MainFragmentFactory.MAIN_FRAGMENT_2:
                setCompoundDrawablesTop(mTvTab3, R.mipmap.main_icon_tab_03_s);
                break;
            case MainFragmentFactory.MAIN_FRAGMENT_3:
                setCompoundDrawablesTop(mTvTab4, R.mipmap.main_icon_tab_04_s);
                break;
        }
    }

    private void cleatIconState() {
        setCompoundDrawablesTop(mTvTab1, R.mipmap.main_icon_tab_01);
        setCompoundDrawablesTop(mTvTab2, R.mipmap.main_icon_tab_02);
        setCompoundDrawablesTop(mTvTab3, R.mipmap.main_icon_tab_03);
        setCompoundDrawablesTop(mTvTab4, R.mipmap.main_icon_tab_04);

        mTvTab1.setTextColor(getResources().getColor(R.color.text_gray));
        mTvTab2.setTextColor(getResources().getColor(R.color.text_gray));
        mTvTab3.setTextColor(getResources().getColor(R.color.text_gray));
        mTvTab4.setTextColor(getResources().getColor(R.color.text_gray));
    }

    public void setCompoundDrawablesTop(TextView tv_icon, int id) {
        Drawable top22 = getResources().getDrawable(id);
        tv_icon.setCompoundDrawablesWithIntrinsicBounds(null, top22, null, null);
        tv_icon.setTextColor(getResources().getColor(R.color.black));
    }


    private void checkUpdate() {

        //TODO 如果版本号一致，清除缓存的 download apk的id
//        SPUtils.putLong(this, DownloadedApkUtlis.DOWNLOAD_ID, -1);

//        showUpdateDialog();
    }

    private void showUpdateDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setCancelable(false);
        alertDialog.setTitle("发现新版本");
        alertDialog.setMessage("为了您更好的体验，请更新最新版本");
        DialogInterface.OnClickListener listener = null;
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", listener);
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "立即下载", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                downLoadApk();
            }
        });
        alertDialog.show();
//        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.xiaofeng_orange));
//        alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.xiaofeng_orange));
    }


    private DownloadChangeObserver downloadObserver;
    private long lastDownloadId = 0;
    //"content://downloads/my_downloads"必须这样写不可更改
    public static final Uri CONTENT_URI = Uri.parse("content://downloads/my_downloads");
    private String netUrl = URLConfig.download_apk_url;
    //    private String netUrl = "http://toppic-mszs.oss-cn-hangzhou.aliyuncs.com/xiaofeng_android_alpha.apk";
//    ProgressDialog pd; // 进度条对话框

    private void downLoadApk() {

        //1.得到下载对象
        DownloadManager dowanloadmanager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);


        long spDownloadId = (long) SPUtils.get(this, DownloadedApkUtlis.DOWNLOAD_ID, (long)-1);
   /*
        下载管理器中有很多下载项，怎么知道一个资源已经下载过，避免重复下载呢？
        我的项目中的需求就是apk更新下载，用户点击更新确定按钮，第一次是直接下载，
        后面如果用户连续点击更新确定按钮，就不要重复下载了。
        可以看出来查询和操作数据库查询一样的
         */
        if (spDownloadId > 0) {
            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById(spDownloadId);
            Cursor cursor = dowanloadmanager.query(query);
            if (!cursor.moveToFirst()) {// 没有记录
                Log.d("mylog", "downLoadApk:  没有记录 ");
            } else {
                //有记录
                Log.d("mylog", "downLoadApk:  有记录 ");
                InstallApkUtlis.toInstallApk(this);  //有记录，直接安装
                return;
            }
        }
//        SPUtils.put(SelectCompOrStudentActivity.this, SPUtils.version, this.version);

        /*pd = new ProgressDialog(this);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setMessage("正在下载更新");
        pd.setMax(100);
        pd.show();
        pd.setCancelable(false);*/

        //2.创建下载请求对象，并且把下载的地址放进去
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(netUrl));
        //3.给下载的文件指定路径
        request.setDestinationInExternalFilesDir(this, Environment.DIRECTORY_DOWNLOADS, "xfzs.apk");
        //4.设置显示在文件下载Notification（通知栏）中显示的文字。6.0的手机Description不显示
        request.setTitle("YC");
        request.setDescription("正在下载");
        //5更改服务器返回的minetype为android包类型
        request.setMimeType("application/vnd.android.package-archive");
        //6.设置在什么连接状态下执行下载操作
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        //7. 设置为可被媒体扫描器找到
        request.allowScanningByMediaScanner();
        //8. 设置为可见和可管理
        request.setVisibleInDownloadsUi(true);
        lastDownloadId = dowanloadmanager.enqueue(request);
        //9.保存id到缓存
        SPUtils.putLong(this, DownloadedApkUtlis.DOWNLOAD_ID, lastDownloadId);
//        PreferencesUtils.putLong(this, DOWNLOAD_ID, lastDownloadId);
        //10.采用内容观察者模式实现进度
        downloadObserver = new DownloadChangeObserver(null);
        getContentResolver().registerContentObserver(CONTENT_URI, true, downloadObserver);
    }

    //用于显示下载进度
    class DownloadChangeObserver extends ContentObserver {

        public DownloadChangeObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById(lastDownloadId);
            DownloadManager dManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            final Cursor cursor = dManager.query(query);
            if (cursor != null && cursor.moveToFirst()) {
                final int totalColumn = cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES);
                final int currentColumn = cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR);
                int totalSize = cursor.getInt(totalColumn);
                int currentSize = cursor.getInt(currentColumn);
                float percent = (float) currentSize / (float) totalSize;
                int progress = Math.round(percent * 100);
               /* pd.setProgress(progress);
                if (progress == 100) {
                    pd.dismiss();
                }*/
            }
        }
    }

}
