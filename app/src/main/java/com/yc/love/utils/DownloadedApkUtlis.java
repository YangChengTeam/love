package com.yc.love.utils;

import android.app.DownloadManager;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.yc.love.model.util.SPUtils;

import java.io.File;

/**
 * 查询指定apk文件
 * Created by mayn on 2019/5/20.
 */

public class DownloadedApkUtlis {
    //"content://downloads/my_downloads"必须这样写不可更改
    private static final Uri CONTENT_URI = Uri.parse("content://downloads/my_downloads");

    public static void downLoadApk(Context context, String downloadIdKey, String apkUrl) {
        downLoadApk(context, downloadIdKey, apkUrl, null);
    }

    public static void downLoadApk(Context context, String downloadIdKey, String apkUrl, ContentObserver contentObserver) {

        //1.得到下载对象
        DownloadManager dowanloadmanager = (DownloadManager) context.getSystemService(context.DOWNLOAD_SERVICE);
        long spDownloadId = (long) SPUtils.get(context, downloadIdKey, (long) -1);
        /*
        下载管理器中有很多下载项，怎么知道一个资源已经下载过，避免重复下载呢？
        我的项目中的需求就是apk更新下载，用户点击更新确定按钮，第一次是直接下载，
        后面如果用户连续点击更新确定按钮，就不要重复下载了。
        可以看出来查询和操作数据库查询一样的
         */
        if (spDownloadId > 0) {
            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById(spDownloadId);
            Cursor cursor = dowanloadmanager.query(query);
            if (!cursor.moveToFirst()) {// 没有记录
                Log.d("mylog", "downLoadApk:  没有记录 ");
            } else {
                //有记录
                Log.d("mylog", "downLoadApk:  有记录 ");
                InstallApkUtlis.toInstallApk(context,spDownloadId);  //有记录，直接安装
                return;
            }
        }
        //2.创建下载请求对象，并且把下载的地址放进去
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(apkUrl));
        //3.给下载的文件指定路径
        request.setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, "xfzs.apk");
        //4.设置显示在文件下载Notification（通知栏）中显示的文字。6.0的手机Description不显示
        request.setTitle("YC");
        request.setDescription("正在下载");
        //5更改服务器返回的minetype为android包类型
        request.setMimeType("application/vnd.android.package-archive");
        //6.设置在什么连接状态下执行下载操作
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        //7. 设置为可被媒体扫描器找到
        request.allowScanningByMediaScanner();
        //8. 设置为可见和可管理
        request.setVisibleInDownloadsUi(true);
        long lastDownloadId = dowanloadmanager.enqueue(request);
        //9.保存id到缓存
        SPUtils.putLong(context, downloadIdKey, lastDownloadId);
        //10.采用内容观察者模式实现进度
//        downloadObserver = new DownloadChangeObserver(null);
        if (contentObserver != null) {
            context.getContentResolver().registerContentObserver(CONTENT_URI, true, contentObserver);
        }
    }


//    public static final String DOWNLOAD_ID = "download_id";

    //通过downLoadId查询下载的apk，解决6.0以后安装的问题
    public static File queryDownloadedApk(Context context,long downLoadId) {
        File targetApkFile = null;
        DownloadManager downloader = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
//        long downloadId = (long) SPUtils.get(context, DOWNLOAD_ID, (long) -1);
//        long downloadId = PreferencesUtils.getLong(context, DOWNLOAD_ID);
        if (downLoadId != -1) {
            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById(downLoadId);
            query.setFilterByStatus(DownloadManager.STATUS_SUCCESSFUL);
            Cursor cur = downloader.query(query);
            if (cur != null) {
                if (cur.moveToFirst()) {
                    String uriString = cur.getString(cur.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                    if (!TextUtils.isEmpty(uriString)) {
                        targetApkFile = new File(Uri.parse(uriString).getPath());
                    }
                }
                cur.close();
            }
        }
        return targetApkFile;
    }
}
