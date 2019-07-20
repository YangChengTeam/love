package com.yc.love.utils;


import android.os.Handler;
import android.os.Looper;

/**
 * Created by wanglin  on 2018/2/5 17:37.
 */

public class UIUtils {
    private static Handler handler = new Handler(Looper.getMainLooper()) ;
    public static void post(Runnable runnable){
        handler.post(runnable);
    }
}
