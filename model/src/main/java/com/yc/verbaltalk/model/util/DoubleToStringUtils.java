package com.yc.verbaltalk.model.util;

/**
 * Created by mayn on 2019/5/17.
 */

public class DoubleToStringUtils {
    public static String doubleToString(double dou){
        if(dou>0){
            String s = String.valueOf(dou);
            if(s.contains(".00")){
                s = s.replace(".00", "");
            }
            return s;
        }
        return String.valueOf(0);
    }
    public static String doubleStringToString(String s){
//            String s = String.valueOf(dou);
            if(s.contains(".00")){
                s = s.replace(".00", "");
            }
            return s;
    }
}
