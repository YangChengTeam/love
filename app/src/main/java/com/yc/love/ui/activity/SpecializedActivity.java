package com.yc.love.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.umeng.commonsdk.statistics.common.DeviceConfig;
import com.yc.love.R;
import com.yc.love.model.bean.event.EventLoginState;
import com.yc.love.model.data.BackfillSingle;
import com.yc.love.model.single.YcSingle;
import com.yc.love.model.util.CheckNetwork;
import com.yc.love.model.util.SPUtils;
import com.yc.love.ui.activity.base.BaseActivity;

import org.greenrobot.eventbus.EventBus;

public class SpecializedActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specialized);

        // 后台返回时可能启动这个页面 http://blog.csdn.net/jianiuqi/article/details/54091181
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                checkNetwork();
                startNextActivity();
            }
        }, 600);


       /* String[] testDeviceInfo = getTestDeviceInfo(this);
        for (int i = 0; i < testDeviceInfo.length; i++) {
            Log.d("mylog", "onCreate: i " + i + "  " + testDeviceInfo[i]);
//            {"device_id":"7c7cce7780fb8044","mac":"1c:15:1f:b4:a9:fd"}
        }*/
    }

    private void startNextActivity() {
        BackfillSingle.backfillLoginData(SpecializedActivity.this, ""); //初始化单例，回填ID数据
        startActivity(new Intent(SpecializedActivity.this, MainActivity.class));
        overridePendingTransition(R.anim.screen_zoom_in, R.anim.screen_zoom_out);
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

    private void showNotNetworkDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setMessage("网络连接失败，请重试");
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "重试", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                checkNetwork();
            }
        });
        alertDialog.show();
    }

   /* public static String[] getTestDeviceInfo(Context context) {
        String[] deviceInfo = new String[2];
        try {
            if (context != null) {
                deviceInfo[0] = DeviceConfig.getDeviceIdForGeneral(context);
                deviceInfo[1] = DeviceConfig.getMac(context);
            }
        } catch (Exception e) {
        }
        return deviceInfo;
    }*/
}
