package com.yc.love.model.domain;

/**
 * Created by mayn on 2019/5/8.
 */

public class URLConfig {

    public static final boolean DEBUG = Config.DEBUG;

//    private static String baseUrl = "http://en.wk2.com/api/";
    private static String baseUrl = "http://love.bshu.com/v1/";
    private static String debugBaseUrl = "http://en.qqtn.com/api/";

    public static String uploadPhotoUrl = baseUrl.concat("common/upload");



    public static final String ID_INFO_SMS = getBaseUrl() + "user/code";


    public static String getBaseUrl() {
        return (DEBUG ? debugBaseUrl : baseUrl);
    }

    public static String getUrl(String url) {
        return DEBUG ? debugBaseUrl : baseUrl.concat(url);
    }
}
