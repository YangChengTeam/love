package com.yc.love.model.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.WindowManager;

/**
 * Created by mayn on 2019/5/6.
 */

public class LocalDisplay {

    public static int WIDTH_PIXEL;
    public static int HEIGHT_PIXEL;
    public static float DENSITY;
    public static int WIDTH_DP;
    public static int HEIGHT_DP;
    //default false volatile 保证同步刷新到内存即可见性
    private static volatile boolean sInitialized;

    public static void init(Context context) {
        if (sInitialized) {
            return;
        }
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        DENSITY = dm.density;
        boolean widthLessThanHeight = dm.widthPixels <= dm.heightPixels;
        WIDTH_PIXEL = widthLessThanHeight ? dm.widthPixels : dm.heightPixels;
        HEIGHT_PIXEL = widthLessThanHeight ? dm.heightPixels : dm.widthPixels;
        WIDTH_DP = (int) (WIDTH_PIXEL / DENSITY);
        HEIGHT_DP = (int) (HEIGHT_PIXEL / DENSITY);
        sInitialized = true;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dp2px(float dp) {
        checkInit();
        final float scale = DENSITY;
        return (int) (dp * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dp(float pxValue) {
        checkInit();
        final float scale = DENSITY;
        return (int) (pxValue / scale + 0.5f);
    }

    private static void checkInit() {
        if (!sInitialized)
            throw new IllegalStateException("LocalDisplay has not init");
    }

    /**
     * 单位转换
     */
    public static int convert(int unit, float value, Context context) {
        // TypedValue.COMPLEX_UNIT_SP
        return (int) TypedValue.applyDimension(unit, value, context.getResources()
                .getDisplayMetrics());
    }
}