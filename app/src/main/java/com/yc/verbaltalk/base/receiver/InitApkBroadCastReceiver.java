package com.yc.verbaltalk.base.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by sunshey on 2019/5/23.
 */

public class InitApkBroadCastReceiver extends BroadcastReceiver {

    private static final int PACKAGE_NAME_START_INDEX = 8;
    @Override
    public void onReceive(Context context, Intent intent) {

        if (Intent.ACTION_PACKAGE_ADDED.equals(intent.getAction())) {
            System.out.println("监听到系统广播添加");
            String data = intent.getDataString();
            Log.d("mylog", "onReceive: 监听到系统广播添加 data "+data); // 监听到系统广播添加 data package:com.example.jingbin.cloudreader
            if (data == null || data.length() <= PACKAGE_NAME_START_INDEX) {
                return;
            }

            String packageName = data.substring(PACKAGE_NAME_START_INDEX);

           /* if (packageName.equals(AppActivity.PACKAGE_NAME)) { //todo

            }*/

        }

        if (Intent.ACTION_PACKAGE_REMOVED.equals(intent.getAction())) {
            System.out.println("监听到系统广播移除");
            Log.d("mylog", "onReceive: 监听到系统广播移除  ");
        }

        if (Intent.ACTION_PACKAGE_REPLACED.equals(intent.getAction())) {
            System.out.println("监听到系统广播替换");
            Log.d("mylog", "onReceive: 监听到系统广播替换  ");
        }
    }

}