package com.yc.love.model.util;

import android.text.TextUtils;
import android.util.Log;

import java.text.ParseException;
import java.text.ParsePosition;
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
        String format = "yyyyMMdd";
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(date);
    }

    public static String dateToYyMmDdDivide(Date date) {
        if (date == null) {
            return "";
        }
        String format = "yyyy-MM-dd";
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(date);
    }

    public static int[] formattingAddDivide(String string) {
        if (!TextUtils.isEmpty(string) && string.length() >= 8) {
            String y = string.substring(0, 4);
            String m = string.substring(4, 6);
            String d = string.substring(6, 8);
            int[] dtatInt = new int[]{Integer.parseInt(y), Integer.parseInt(m), Integer.parseInt(d)};
            return dtatInt;
        }
        return new int[]{};
    }

    public static long dateToStamp(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String format = simpleDateFormat.format(date);
//        format="2019-05-18";
        Date parse = null;
        try {
            parse = simpleDateFormat.parse(format);
        } catch (ParseException e) {
            Log.d("mylog", "dateToStamp: ParseException " + e);
        }
        return parse.getTime();
    }
    public static long dateToStamp(String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        String format = simpleDateFormat.format(date);
//        format="2019-05-18";
        Date parse = null;
        try {
            parse = simpleDateFormat.parse(format);
        } catch (ParseException e) {
            Log.d("mylog", "dateToStamp: ParseException " + e);
        }
        return parse.getTime();
    }

   /* public static Date dateStringToDate(String dateString) {
//        Date date = new Date(dateLong);
//        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//        String dateString = formatter.format(date);
//        ParsePosition pos = new ParsePosition(8);
//        Date currentTime_2 = formatter.parse(dateString, pos);
        Date  date=new Date();
        try {
            date = formatter.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }*/

}
