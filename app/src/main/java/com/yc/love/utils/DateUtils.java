package com.yc.love.utils;

import android.text.TextUtils;

/**
 * Created by admin on 2018/2/12.
 */

public class DateUtils {
    public static String getFormatDateInSecond(String second) {
        if (!TextUtils.isEmpty(second)) {
            int seconds = Integer.parseInt(second);
            int temp = 0;
            StringBuffer sb = new StringBuffer();
            temp = seconds / 60;
            sb.append((temp < 10) ? "0" + temp + ":" : "" + temp + ":");

            temp = seconds % 60;
            sb.append((temp < 10) ? "0" + temp : "" + temp);

            return sb.toString();
        }
        return null;
    }
}
