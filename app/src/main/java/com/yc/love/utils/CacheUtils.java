package com.yc.love.utils;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;

/**
 * Created by wanglin  on 2018/2/9 11:10.
 */

public class CacheUtils {

    public static void writeCache(final Context context, final String key, final String json) {
        new ThreadPoolUtils(ThreadPoolUtils.SingleThread, 5).execute(new Runnable() {
            @Override
            public void run() {

                String path = FileUtils.createDir(makeBaseDir(context) + "/cache");
                FileIOUtils.writeFileFromString(path + "/" + key, json);
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

        new ThreadPoolUtils(ThreadPoolUtils.SingleThread, 5).execute(new Runnable() {
            @Override
            public void run() {

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
