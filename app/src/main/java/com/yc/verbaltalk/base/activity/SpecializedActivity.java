package com.yc.verbaltalk.base.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bytedance.sdk.openadsdk.TTFeedAd;
import com.bytedance.sdk.openadsdk.TTNativeExpressAd;
import com.yc.verbaltalk.R;
import com.yc.verbaltalk.base.fragment.PrivacyPolicyFragment;
import com.yc.verbaltalk.base.utils.UIUtils;
import com.yc.verbaltalk.base.view.imgs.Constant;
import com.yc.verbaltalk.model.util.CheckNetwork;
import com.yc.verbaltalk.model.util.SPUtils;

import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import yc.com.toutiao_adv.TTAdDispatchManager;
import yc.com.toutiao_adv.TTAdType;


public class SpecializedActivity extends BaseActivity implements yc.com.toutiao_adv.OnAdvStateListener {

    private ImageView ivLighting;
    private FrameLayout splashContainer;
    private TextView tvSipView;


    private int delay = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specialized);


        invadeStatusBar();
        // 后台返回时可能启动这个页面 http://blog.csdn.net/jianiuqi/article/details/54091181
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }
        boolean isFirstOpen = (boolean) SPUtils.get(this, SPUtils.FIRST_OPEN, true);

        ivLighting = findViewById(R.id.specialized_iv_lightning);
        splashContainer = findViewById(R.id.splash_container);
        tvSipView = findViewById(R.id.skip_view);

//        mHandler.post(runnable);
//        AdvDispatchManager.getManager().init(this, AdvType.SPLASH, splashContainer, tvSipView, Constant.TENCENT_ADV_ID, Constant.SPLASH_ADV_ID, this);

        String brand = Build.BRAND.toLowerCase();

        if (isFirstOpen) {
            mHandler.postDelayed(() -> {
                PrivacyPolicyFragment privacyPolicyFragment = new PrivacyPolicyFragment();
                privacyPolicyFragment.show(getSupportFragmentManager(), "");
                privacyPolicyFragment.setOnClickBtnListener(() -> {
                    if (!TTAdDispatchManager.getManager().init(SpecializedActivity.this, TTAdType.SPLASH, splashContainer, Constant.TOUTIAO_SPLASH_ID, 0, null, 0, null, 0, SpecializedActivity.this)) {
                        switchMain(delay);
                    }

                    SPUtils.put(SpecializedActivity.this, SPUtils.FIRST_OPEN, false);
                });
            }, 100);
        } else {
            if (!TTAdDispatchManager.getManager().init(SpecializedActivity.this, TTAdType.SPLASH, splashContainer, Constant.TOUTIAO_SPLASH_ID, 0, null, 0, null, 0, SpecializedActivity.this)) {
                switchMain(delay);
            }
        }

    }


    private void startNextActivity() {
        startActivity(new Intent(SpecializedActivity.this, MainActivity.class));
//        overridePendingTransition(R.anim.screen_zoom_in, R.anim.screen_zoom_out);  //开屏动画
        finish();
    }


    private void checkNetwork() {
        boolean networkConnected = CheckNetwork.isNetworkConnected(SpecializedActivity.this);
//        boolean connected = CheckNetwork.isWifiConnected(SpecializedActivity.this);
//        Log.d("mylog", "checkNetwork: networkConnected " + networkConnected);
//        Log.d("mylog", "checkNetwork: connected " + connected);
        if (!networkConnected) {
            showNotNetworkDialog();
        } else {
            startNextActivity();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

//        AdvDispatchManager.getManager().onResume();

        TTAdDispatchManager.getManager().onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();

//        AdvDispatchManager.getManager().onPause();
        TTAdDispatchManager.getManager().onStop();
    }

    private void showNotNetworkDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setMessage("网络连接失败，请重试");
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "重试", (dialogInterface, i) -> checkNetwork());
        alertDialog.show();
    }

    private void switchMain(long delay) {

        UIUtils.postDelay(this::startNextActivity, delay);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

//        AdvDispatchManager.getManager().onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    //防止用户返回键退出 APP
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                || keyCode == KeyEvent.KEYCODE_HOME) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void loadSuccess() {
        switchMain(0);
    }

    @Override
    public void loadFailed() {
        switchMain(0);
    }

    @Override
    public void clickAD() {
        switchMain(0);
    }

    @Override
    public void onTTNativeExpressed(List<TTNativeExpressAd> ads) {

    }


    @Override
    public void onDrawFeedAd(List<TTFeedAd> feedAdList) {

    }

    @Override
    public void removeNativeAd(TTNativeExpressAd ad, int position) {

    }

    @Override
    public void rewardComplete() {

    }


}
