package com.music.player.lib.util;

/**
 * TinyHung@outlook.com
 * 2017/6/30 14:54
 */
public class DateUtil {

    public static String getTimeLengthString(int var0) {
        if(var0 > 0) {
            StringBuffer var1 = new StringBuffer();
            Integer var2 = Integer.valueOf(var0 / 60);
            Integer var4 = Integer.valueOf(var0 % 60);
            if(var2.intValue() > 60) {
                Integer var3;
                if((var3 = Integer.valueOf(var2.intValue() / 60)).intValue() < 10) {
                    var1.append("0" + var3);
                } else {
                    var1.append(var3);
                }

                var1.append(":");
                if((var2 = Integer.valueOf(var2.intValue() % 60)).intValue() < 10) {
                    var1.append("0" + var2);
                } else {
                    var1.append(var2);
                }
            } else if(var2.intValue() < 10) {
                var1.append("0" + var2);
            } else {
                var1.append(var2);
            }

            var1.append(":");
            if(var4.intValue() < 10) {
                var1.append("0" + var4);
            } else {
                var1.append(var4);
            }

            return var1.toString();
        } else {
            return "00:00";
        }
    }
}
