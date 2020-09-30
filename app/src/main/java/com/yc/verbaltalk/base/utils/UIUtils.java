package com.yc.verbaltalk.base.utils;


import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Looper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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


    /**
     * 判断是否是常用11位数手机号
     *
     * @param phoneNumber
     * @return
     */
    public static boolean isPhoneNumber(String phoneNumber) {
        Pattern p = Pattern.compile("^((13[0-9])|(14[0-9])|(15[0-9])|(16[0-9])|(17[0-9])|(18[0-9])|(19[0-9]))\\d{8}$");
        Matcher m = p.matcher(phoneNumber);
        return m.matches();
    }

    /**
     * 是否是6或者4位数字验证码
     *
     * @param phoneNumber
     * @param type        6 或 4
     * @return
     */
    public static boolean isNumberCode(String phoneNumber, int type) {
        String rex = "^\\d{4}$";
        if (type == 6) {
            rex = "^\\d{6}$";
        }
        Pattern p = Pattern.compile(rex);
        Matcher m = p.matcher(phoneNumber);
        return m.matches();
    }

    private static Resources mResources = null;
    private static String mPackageName = null;


    public static int getIdentifier(Context context, String name) {
        return getIdentifier(context, name, "id");
    }


    private static int getIdentifier(Context context, String name, String defType) {
        if (mResources == null) {
            mResources = context.getResources();
        }
        return mResources.getIdentifier(name, defType, getPackageName(context));
    }


    private static String getPackageName(Context context) {
        if (mPackageName == null) {
            mPackageName = context.getPackageName();
        }
        return mPackageName;
    }


    public static void closeActivity(String className) {
        try {
            Class clazz = Class.forName(className);


            Method method = clazz.getMethod("finish");
            method.setAccessible(true);
            method.invoke(clazz);

        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public static String getAppName(Context context) {
        if (context == null) {
            return "";
        } else {
            String appName = "";
            try {
                PackageManager packageManager = context.getPackageManager();
                ApplicationInfo applicationInfo = null;
                try {
                    applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                if (null != applicationInfo) {
                    appName = packageManager.getApplicationLabel(applicationInfo).toString();
                } else {
                    appName = context.getResources().getString(context.getApplicationInfo().labelRes);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return appName;
        }

    }
}
