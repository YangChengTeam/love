package com.yc.love.model.util;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by mayn on 2019/5/6.
 */

public class TimeUtils {
    public static String dateToYyMmDd(Date date) {
        if (date == null) {
            return "";
        }
        String format = "yyyy-MM-dd";
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(date);
    }

    public static long dateToStamp(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String format = simpleDateFormat.format(date);
        Date parse = null;
        try {
            parse = simpleDateFormat.parse(format);
        } catch (ParseException e) {
            Log.d("mylog", "dateToStamp: ParseException " + e);
        }
        return parse.getTime();
    }

}
