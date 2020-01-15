package com.yc.verbaltalk.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import com.umeng.analytics.MobclickAgent;
import com.yc.verbaltalk.BuildConfig;
import com.yc.verbaltalk.model.constant.ConstantKey;

import java.io.File;

import androidx.core.content.FileProvider;

/**
 * Created by mayn on 2019/5/20.
 */

public class InstallApkUtlis {
    public static boolean toInstallApk(Context context, long cacheDownLoadId) {
        Intent install = new Intent(Intent.ACTION_VIEW);
        File apkFile = DownloadedApkUtlis.queryDownloadedApk(context, cacheDownLoadId);
        if (apkFile != null && apkFile.exists()) {
            MobclickAgent.onEvent(context, ConstantKey.UM_INSTALL_OUT_ID);
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
            return true;
        }else {
            Log.d("mylog", "toInstallApk: !!!!!!!!!!!apkFile.exists");
            return false;
        }
    }
}
