package com.yc.verbaltalk.model.util;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.yc.verbaltalk.model.bean.MyAppInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mayn on 2019/5/21.
 */

public class ApkToolUtils {
    public static List<MyAppInfo> mLocalInstallApps = null;

    public static List<MyAppInfo> scanLocalInstallAppList(PackageManager packageManager) {
        List<MyAppInfo> myAppInfos = new ArrayList<MyAppInfo>();
        try {
            List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
            for (int i = 0; i < packageInfos.size(); i++) {
                PackageInfo packageInfo = packageInfos.get(i);

                long firstInstallTime = packageInfo.firstInstallTime;// 应用第一次安装的时间
                int versionCode = packageInfo.versionCode;// 应用现在的版本号
                String versionName = packageInfo.versionName;// 应用现在的版本名称
                long lastUpdateTime = packageInfo.lastUpdateTime;// 最后一次更新时间

                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                Log.e("Steven", "firstInstallTime=" + format.format(firstInstallTime) +
                                ",versionCode=" + versionCode + ",versionName=" + versionName + ",最后更新时间=" + format.format(lastUpdateTime));

                        //如下可获得更多信息
                        ApplicationInfo applicationInfo = packageInfo.applicationInfo;
                String name = applicationInfo.name;// Application类名
                String packageName = applicationInfo.packageName;// 包名

                String applicationName =
                        (String) packageManager.getApplicationLabel(applicationInfo);

                Log.e("Steven", "ApkTool.scanLocalInstallAppList：" + "应用名：" + applicationName + ",包名:" + packageName);

                //过滤掉系统app
                if ((ApplicationInfo.FLAG_SYSTEM & packageInfo.applicationInfo.flags) != 0) {
                    continue;
                }
                MyAppInfo myAppInfo = new MyAppInfo();
                myAppInfo.setAppName(packageInfo.packageName);
                if (packageInfo.applicationInfo.loadIcon(packageManager) == null) {
                    continue;
                }
//                packageInfo
                Drawable drawable = packageInfo.applicationInfo.loadIcon(packageManager);
                myAppInfo.setImage(drawable);
                myAppInfos.add(myAppInfo);


            }
        } catch (Exception e) {
            Log.e("Steven", "===============获取应用包信息失败");
        }
        return myAppInfos;
    }

}