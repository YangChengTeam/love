package com.yc.verbaltalk.base.utils;


import android.os.Handler;
import android.os.Looper;

/**
 * Created by wanglin  on 2018/2/5 17:37.
 */

public class UIUtils {
    private static Handler handler = new Handler(Looper.getMainLooper());

    public static void post(Runnable runnable) {
        handler.post(runnable);
    }

    public static void postDelay(Runnable runnable, long i) {
        handler.postDelayed(runnable, i);
    }






}
