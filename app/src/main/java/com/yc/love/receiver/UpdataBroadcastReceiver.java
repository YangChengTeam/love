package com.yc.love.receiver;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.yc.love.utils.InstallApkUtlis;


/**
 * Created by mayn on 2019/5/20.
 */

public class UpdataBroadcastReceiver extends BroadcastReceiver {

    private final long cacheDownLoadId;

    public UpdataBroadcastReceiver(long cacheDownLoadId) {
        this.cacheDownLoadId=cacheDownLoadId;
    }

    @SuppressLint("NewApi")
    public void onReceive(Context context, Intent intent) {
        long downLoadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
//        long cacheDownLoadId = (long) SPUtils.get(context,DownloadedApkUtlis.DOWNLOAD_ID,(long)-1);
//        long cacheDownLoadId = PreferencesUtils.getLong(context, DownloadedApkUtlis.DOWNLOAD_ID);
        if (cacheDownLoadId == downLoadId) {
            InstallApkUtlis.toInstallApk(context,cacheDownLoadId);  //安装apk
        }
    }

   /* private void toInstallApk(Context context) {
        Intent install = new Intent(Intent.ACTION_VIEW);
        File apkFile = DownloadedApkUtlis.queryDownloadedApk(context);
        //判断是否是AndroidN以及更高的版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            install.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileProvider", apkFile);
            install.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            install.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
//                install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(install);
    }*/


}
