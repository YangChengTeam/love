package com.yc.verbaltalk.receiver;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.yc.verbaltalk.utils.InstallApkUtlis;


/**
 * Created by mayn on 2019/5/20.
 */

public class UpdataBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        long downLoadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
        Log.d("mylog", "onReceive: downLoadId "+downLoadId);
        if (downLoadId > 0 ) {
            InstallApkUtlis.toInstallApk(context, downLoadId);  //安装apk
        }
    }


    /*@SuppressLint("NewApi")
    public void onReceive(Context context, Intent intent) {
        Log.d("mylog", "onReceive: 1111111111111111111111111");
        long downLoadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
        long cacheDownLoadId = (long) SPUtils.get(context, SPUtils.DOWNLOAD_OUT_ID, "");

        Log.d("mylog", "onReceive: downLoadId " + downLoadId + " cacheDownLoadId " + cacheDownLoadId);

        if (downLoadId > 0 && downLoadId == cacheDownLoadId) {
            InstallApkUtlis.toInstallApk(context, downLoadId);  //安装apk
        }
    }*/

}
