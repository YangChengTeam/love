package com.yc.love.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;

import com.yc.love.BuildConfig;

import java.io.File;

/**
 * Created by mayn on 2019/5/20.
 */

public class InstallApkUtlis {
    public static void toInstallApk(Context context,long cacheDownLoadId) {
        Intent install = new Intent(Intent.ACTION_VIEW);
        File apkFile = DownloadedApkUtlis.queryDownloadedApk(context,cacheDownLoadId);
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
    }
}
