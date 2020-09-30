package com.yc.verbaltalk.base.activity;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.music.player.lib.manager.MusicPlayerManager;
import com.umeng.socialize.UMShareAPI;
import com.yc.verbaltalk.R;
import com.yc.verbaltalk.base.adapter.MainPagerAdapter;
import com.yc.verbaltalk.index.factory.MainFragmentFactory;
import com.yc.verbaltalk.base.receiver.NetWorkChangReceiver;
import com.yc.verbaltalk.base.view.ControlScrollViewPager;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

//import cn.trinea.android.common.util.PreferencesUtils;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private ControlScrollViewPager mVpFragment;
    private TextView mTvTab1, mTvTab2, mTvTab3, mTvTab4, mTvTab5;
    //    private boolean isRegistered = false;
    private NetWorkChangReceiver netWorkChangReceiver;
    private String mPackageVersionName;
    public String mDownloadIdKey = "mDownloadIdKey";
    private OnChildDisposeMainKeyDownListent onChildDisposeMainKeyDownListent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        invadeStatusBar(); //侵入状态栏
        setAndroidNativeLightStatusBar(); //状态栏字体颜色改变
//        checkPermission();
        try {
            // 判断当前的版本与服务器上的最版版本是否一致
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getApplication().getPackageName(), 0);
            mPackageVersionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            Log.d("mylog", "run: getPackageManager " + e.toString());
            mPackageVersionName = "1.0";
        }
        mDownloadIdKey = "download_id".concat(mPackageVersionName);
        Log.d("mylog", "onCreate: download_id mDownloadIdKey " + mDownloadIdKey);
        //初始化MusicService
        MusicPlayerManager.getInstance().bindService(this);
        initView();


    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            int currentItem = mVpFragment.getCurrentItem();
            if (MainFragmentFactory.MAIN_FRAGMENT_3 == currentItem && onChildDisposeMainKeyDownListent != null && onChildDisposeMainKeyDownListent.onChildDisposeMainKeyDown()) {
                Log.d("mylog", "onKeyDown: WebView goBack");
            } else {
                Log.d("mylog", "onKeyDown: 退出app");
                MusicPlayerManager.getInstance().unBindService(this);
//                Intent home = new Intent(Intent.ACTION_MAIN);
//                home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                home.addCategory(Intent.CATEGORY_HOME);
//                startActivity(home);
                finish();
                System.exit(0);
            }

            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    public interface OnChildDisposeMainKeyDownListent {
        boolean onChildDisposeMainKeyDown();
    }

    public void setOnChildDisposeMainKeyDownListent(OnChildDisposeMainKeyDownListent onChildDisposeMainKeyDownListent) {
        this.onChildDisposeMainKeyDownListent = onChildDisposeMainKeyDownListent;
    }


    private void initNetWorkChangReceiver() {
        //注册网络状态监听广播
        netWorkChangReceiver = new NetWorkChangReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(netWorkChangReceiver, filter);
//        isRegistered = true;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        //解绑
//        if (isRegistered) {
        if (netWorkChangReceiver != null) {
            unregisterReceiver(netWorkChangReceiver);
        }

//        }

    }

    private void initView() {


        mVpFragment = findViewById(R.id.comp_main_vp_fragment);
        mTvTab1 = findViewById(R.id.comp_main_tv_tab_1);
        mTvTab2 = findViewById(R.id.comp_main_tv_tab_2);
        mTvTab3 = findViewById(R.id.comp_main_tv_tab_3);
        mTvTab4 = findViewById(R.id.comp_main_tv_tab_4);
        mTvTab5 = findViewById(R.id.comp_main_tv_tab_5);

//        FloatingActionButton fabToWx = findViewById(R.love_id.comp_main_floating_action_button_to_wx);
        final ImageView ivToWx = findViewById(R.id.comp_main_iv_to_wx);

        LinearLayout llIcon = findViewById(R.id.ll_icon);
//        fabToWx.setOnClickListener(this);
        ivToWx.setOnClickListener(this);

        mTvTab1.setOnClickListener(this);
        mTvTab2.setOnClickListener(this);
        mTvTab3.setOnClickListener(this);
        mTvTab4.setOnClickListener(this);
        mTvTab5.setOnClickListener(this);

        MainPagerAdapter mainPagerAdapter = new MainPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        mVpFragment.setAdapter(mainPagerAdapter);
//        mVpFragment.setOffscreenPageLimit(3);

        mTvTab1.postDelayed(this::initNetWorkChangReceiver, 200);

        mVpFragment.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == MainFragmentFactory.MAIN_FRAGMENT_2) {
                    if (llIcon.getVisibility() != View.GONE) {
                        llIcon.setVisibility(View.GONE);
                    }
                } else {
                    if (llIcon.getVisibility() != View.VISIBLE) {
                        llIcon.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
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
                mVpFragment.setCurrentItem(MainFragmentFactory.MAIN_FRAGMENT_3, false);
                iconSelect(MainFragmentFactory.MAIN_FRAGMENT_3);
                break;
            case R.id.comp_main_tv_tab_5:
//                int id = YcSingle.getInstance().id;
//                if (id <= 0) {
//                    showToLoginDialog();
//                    return;
//                }
                mVpFragment.setCurrentItem(MainFragmentFactory.MAIN_FRAGMENT_4, false);
                iconSelect(MainFragmentFactory.MAIN_FRAGMENT_4);
                break;
            case R.id.comp_main_iv_to_wx:
                showToWxServiceDialog(null);
                break;
        }
    }


    private void iconSelect(int current) {
        cleatIconState();
        switch (current) {
            case MainFragmentFactory.MAIN_FRAGMENT_0:
                setCompoundDrawablesTop(mTvTab1, R.mipmap.home_sel);
                break;
            case MainFragmentFactory.MAIN_FRAGMENT_1:
                setCompoundDrawablesTop(mTvTab2, R.mipmap.home_love_sel);
                break;
            case MainFragmentFactory.MAIN_FRAGMENT_2:
                setCompoundDrawablesTop(mTvTab3, R.mipmap.home_community_sel);
                break;
            case MainFragmentFactory.MAIN_FRAGMENT_3:
                setCompoundDrawablesTop(mTvTab4, R.mipmap.home_tease_sel);
                break;
            case MainFragmentFactory.MAIN_FRAGMENT_4:
                setCompoundDrawablesTop(mTvTab5, R.mipmap.home_personal_sel);
                break;
        }
    }

    private void cleatIconState() {
        setCompoundDrawablesTop(mTvTab1, R.mipmap.home_default);
        setCompoundDrawablesTop(mTvTab2, R.mipmap.home_love_default);
        setCompoundDrawablesTop(mTvTab3, R.mipmap.home_community_default);
        setCompoundDrawablesTop(mTvTab4, R.mipmap.home_tease_default);
        setCompoundDrawablesTop(mTvTab5, R.mipmap.home_personal_default);

        mTvTab1.setTextColor(getResources().getColor(R.color.text_gray));
        mTvTab2.setTextColor(getResources().getColor(R.color.text_gray));
        mTvTab3.setTextColor(getResources().getColor(R.color.text_gray));
        mTvTab4.setTextColor(getResources().getColor(R.color.text_gray));
        mTvTab5.setTextColor(getResources().getColor(R.color.text_gray));
    }

    public void setCompoundDrawablesTop(TextView tv_icon, int id) {
        Drawable top22 = getResources().getDrawable(id);
        tv_icon.setCompoundDrawablesWithIntrinsicBounds(null, top22, null, null);
        tv_icon.setTextColor(getResources().getColor(R.color.black));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        Log.e("TAG", "onBackPressed: " );
        finish();
//        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
        super.onBackPressed();
    }


}
