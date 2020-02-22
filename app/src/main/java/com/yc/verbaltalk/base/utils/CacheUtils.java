package com.yc.verbaltalk.base.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.text.TextUtils;

import com.qw.soul.permission.SoulPermission;
import com.qw.soul.permission.bean.Permission;
import com.qw.soul.permission.callbcak.CheckRequestPermissionListener;

import java.io.File;

import androidx.core.content.ContextCompat;

/**
 * Created by wanglin  on 2018/2/9 11:10.
 */

public class CacheUtils {

    public static void writeCache(final Context context, final String key, final String json) {
        new ThreadPoolUtils(ThreadPoolUtils.CachedThread, 5).execute(new Runnable() {
            @Override
            public void run() {
                SoulPermission.getInstance().checkAndRequestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, new CheckRequestPermissionListener() {
                    @Override
                    public void onPermissionOk(Permission permission) {
                        String path = FileUtils.createDir(makeBaseDir(context) + "/cache");
                        FileIOUtils.writeFileFromString(path + "/" + key, json);
                    }

                    @Override
                    public void onPermissionDenied(Permission permission) {

                    }
                });

            }
        });

    }

    public abstract static class SubmitRunable implements Runnable {

        private String json;

        public String getJson() {
            return json;
        }

        public void setJson(String json) {
            this.json = json;
        }
    }


    public static void readCache(final Context context, final String key, final SubmitRunable runable) {

        new ThreadPoolUtils(ThreadPoolUtils.FixedThread, 5).execute(() -> {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                String path = FileUtils.createDir(makeBaseDir(context) + "/cache");
                String json = FileIOUtils.readFile2String(path + "/" + key);
                if (!TextUtils.isEmpty(json)) {
                    if (runable != null) {
                        runable.setJson(json);
                        runable.run();
                    }
                }
            }
        });

    }

    private static String makeBaseDir(Context context) {
        File dir = new File(Environment.getExternalStorageDirectory() + "/" + context.getPackageName());
        if (!dir.exists()) {
            dir.mkdir();
        }
        return dir.getAbsolutePath();
    }
}
