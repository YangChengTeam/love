package com.yc.verbaltalk.utils;

import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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

    public static String formatTimeToStr(long time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM月dd日", Locale.getDefault());
        return simpleDateFormat.format(new Date(time * 1000));
    }
}
