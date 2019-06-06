package com.yc.love.model.domain;

import java.io.File;

/**
 * Created by mayn on 2019/5/8.
 */

public class URLConfig {

    private final static String URL_SERVER_IP = "http://nz.qqtn.com/zbsq/index.php?";
    //表白
    public final static String CATEGORY_LIST_URL = URL_SERVER_IP + "m=Home&c=zbsq&a=getCateList";
    // 5、图片合成
    public final static String URL_IMAGE_CREATE = URL_SERVER_IP + "m=Home&c=Zbsq&a=start_zb";

    //默认值
    private static String sdPath = "/storage/emulated/0/Android/data/com.ant.flying/cache";
    private static String SD_DIR = sdPath;
    private static final String BASE_SD_DIR = SD_DIR + File.separator + "TNZBSQ";
    public static final String BASE_NORMAL_FILE_DIR = BASE_SD_DIR + File.separator + "files";

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

    // apk 下载地址
    public static String download_apk_url = "http://toppic-mszs.oss-cn-hangzhou.aliyuncs.com/xfzs.apk";
//    public static String download_apk_url = "http://toppic-mszs.oss-cn-hangzhou.aliyuncs.com/";

}
