package com.yc.verbaltalk.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.yc.verbaltalk.model.bean.event.NetWorkChangT1Bean;
import com.yc.verbaltalk.model.bean.event.NetWorkChangT2Bean;
import com.yc.verbaltalk.model.bean.event.NetWorkChangT3Bean;
import com.yc.verbaltalk.model.single.YcSingle;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * 监听网络状态变化
 * Created by mayn on 2019/5/18.
 */

public class NetWorkChangReceiver extends BroadcastReceiver {
    private static final String TAG = "mylog";
    private List<String> mConnectionTypeList;

    /**
     * 获取连接类型
     *
     * @param type
     * @return
     */
    private String getConnectionType(int type) {
        String connType = "";
        if (type == ConnectivityManager.TYPE_MOBILE) {
            connType = "移动网络数据";
        } else if (type == ConnectivityManager.TYPE_WIFI) {
            connType = "WIFI网络";
        }
        return connType;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (mConnectionTypeList == null) {
            mConnectionTypeList = new ArrayList<>();
        }

        if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intent.getAction())) {// 监听wifi的打开与关闭，与wifi的连接无关
            int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
            Log.d(TAG, "NetWorkChangReceiver wifiState:" + wifiState);
            switch (wifiState) {
                case WifiManager.WIFI_STATE_DISABLED:
                    postNetState();
                    break;
                case WifiManager.WIFI_STATE_ENABLED:
                    break;
            }
        }
        // 监听网络连接，包括wifi和移动数据的打开和关闭,以及连接上可用的连接都会接到监听
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
            //获取联网状态的NetworkInfo对象
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (null != cm) {
                NetworkInfo info = cm.getActiveNetworkInfo();

//            NetworkInfo info = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
                if (info != null) {
                    //如果当前的网络连接成功并且网络连接可用
                    if (NetworkInfo.State.CONNECTED == info.getState() && info.isAvailable()) {

                        if (info.getType() == ConnectivityManager.TYPE_WIFI || info.getType() == ConnectivityManager.TYPE_MOBILE) {
                            Log.d(TAG, " NetWorkChangReceiver " + getConnectionType(info.getType()) + "连上");
                            mConnectionTypeList.add(String.valueOf(info.getType()));
                        }
                    } else {
                        Log.d(TAG, " NetWorkChangReceiver " + getConnectionType(info.getType()) + "断开");
                        mConnectionTypeList.remove(String.valueOf(info.getType()));
                    }
                    YcSingle.getInstance().connectionTypeList = mConnectionTypeList;
//                EventBus.getDefault().post(new NetWorkChangBean(mConnectionTypeList));
                    postNetState();
                }
            }

        }
    }

    private void postNetState() {
        EventBus.getDefault().post(new NetWorkChangT1Bean(mConnectionTypeList));
        EventBus.getDefault().post(new NetWorkChangT2Bean(mConnectionTypeList));
        EventBus.getDefault().post(new NetWorkChangT3Bean(mConnectionTypeList));
//        JSON.parseObject()
    }
}
