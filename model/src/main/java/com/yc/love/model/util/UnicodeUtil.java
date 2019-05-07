package com.yc.love.model.util;

/**
 * Created by mayn on 2019/5/7.
 */

public class UnicodeUtil {
    /**
     * 将utf-8的汉字转换成unicode格式汉字码
     *
     * @param string
     * @return
     */
    public static String stringToUnicode(String string) {
        StringBuffer unicode = new StringBuffer();
        for (int i = 0; i < string.length(); i++) {
            char c = string.charAt(i);
            unicode.append("\\u" + Integer.toHexString(c));
        }
        String str = unicode.toString();
        return str.replaceAll("\\\\", "0x");
    }

    /**
     * 将unicode的汉字码转换成utf-8格式的汉字
     *
     * @param unicode
     * @return
     */
    public static String unicodeToString(String unicode) {
        String str = unicode.replace("0x", "\\");
        StringBuffer string = new StringBuffer();
        String[] hex = str.split("\\\\u");
        for (int i = 1; i < hex.length; i++) {
            int data = Integer.parseInt(hex[i], 16);
            string.append((char) data);
        }
        return string.toString();
    }

/*    public static void main(String[] args) {
        String str = "你好吗？ How are you";
        String unicode = stringToUnicode(str);
        String string = unicodeToString(unicode);
        System.out.println("转换成unicode格式的:\n" + unicode);
        System.out.println("转换成汉字UTF-8格式的:\n" + string);
    }*/
}
