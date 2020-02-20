package com.yc.verbaltalk.base.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bytedance.sdk.openadsdk.TTNativeExpressAd;
import com.qq.e.ads.nativ.NativeExpressADView;
import com.yc.verbaltalk.R;
import com.yc.verbaltalk.base.utils.BackfillSingle;
import com.yc.verbaltalk.model.util.CheckNetwork;
import com.yc.verbaltalk.base.view.imgs.Constant;
import com.yc.verbaltalk.base.utils.UIUtils;

import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import yc.com.tencent_adv.AdvDispatchManager;
import yc.com.tencent_adv.OnAdvStateListener;
import yc.com.toutiao_adv.TTAdDispatchManager;
import yc.com.toutiao_adv.TTAdType;

public class SpecializedActivity extends BaseActivity implements OnAdvStateListener, yc.com.toutiao_adv.OnAdvStateListener {

    private ImageView ivLighting;
    private FrameLayout splashContainer;
    private TextView tvSipView;


    private int seconds = 3;

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

        ivLighting = findViewById(R.id.specialized_iv_lightning);
        splashContainer = findViewById(R.id.splash_container);
        tvSipView = findViewById(R.id.skip_view);

//        mHandler.post(runnable);
//        AdvDispatchManager.getManager().init(this, AdvType.SPLASH, splashContainer, tvSipView, Constant.TENCENT_ADV_ID, Constant.SPLASH_ADV_ID, this);

        TTAdDispatchManager.getManager().init(this, TTAdType.SPLASH, splashContainer, Constant.TOUTIAO_SPLASH_ID, 0, null, 0, null, 0, this);

//        ivLighting.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showToWxServiceDialog(null);
//            }
//        });
    }


    private void startNextActivity() {
        startActivity(new Intent(SpecializedActivity.this, MainActivity.class));
        BackfillSingle.backfillLoginData(SpecializedActivity.this, ""); //初始化单例，回填ID数据
//        overridePendingTransition(R.anim.screen_zoom_in, R.anim.screen_zoom_out);  //开屏动画
        finish();
    }

    private void checkNetwork() {
        boolean networkConnected = CheckNetwork.isNetworkConnected(SpecializedActivity.this);
        boolean connected = CheckNetwork.isWifiConnected(SpecializedActivity.this);
        Log.d("mylog", "checkNetwork: networkConnected " + networkConnected);
        Log.d("mylog", "checkNetwork: connected " + connected);
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
    public void onShow() {
        ivLighting.setVisibility(View.GONE);
        tvSipView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDismiss(long delayTime) {
        switchMain(delayTime);
    }

    @Override
    public void onError() {
        startNextActivity();
    }

    @Override
    public void onNativeExpressDismiss(NativeExpressADView view) {
    }

    @Override
    public void onNativeExpressShow(Map<NativeExpressADView, Integer> mDatas) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        AdvDispatchManager.getManager().onRequestPermissionsResult(requestCode, permissions, grantResults);

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


//    private Runnable runnable = new Runnable() {
//        @Override
//        public void run() {
//            tvSipView.setVisibility(View.VISIBLE);
//            tvSipView.setText(seconds + "s");
//            seconds--;
//            if (seconds > 0) {
//                weakHandler.postDelayed(this, 1000);
//            } else {
//                switchMain(0);
//            }
//        }
//    };
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        weakHandler.removeCallbacks(runnable);
//    }


}
