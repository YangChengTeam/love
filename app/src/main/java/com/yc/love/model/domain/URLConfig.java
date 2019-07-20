package com.yc.love.model.domain;

import java.io.File;

/**
 * Created by mayn on 2019/5/8.
 */

public class URLConfig {


    public static final boolean DEBUG = Config.DEBUG;

    //    private static String baseUrl = "http://en.wk2.com/api/";
    private static final String baseUrl = "http://love.bshu.com/";
    private static final String baseUrlV1 = baseUrl.concat("v1/");
    private static String baseUrlV2 = baseUrl.concat("v2/");
    private static String debugBaseUrl = "http://en.qqtn.com/api/";


    private final static String URL_SERVER_IP = "http://nz.qqtn.com/zbsq/index.php?";

    //表白
    public final static String CATEGORY_LIST_URL = URL_SERVER_IP + "m=Home&c=zbsq&a=getCateList";
    // 5、图片合成
    public final static String URL_IMAGE_CREATE = URL_SERVER_IP + "m=Home&c=Zbsq&a=start_zb";
    //音频分类
    public static final String AUDIO_DATA_LIST_URL = "http://love.bshu.com/v1/music/cats";
    public static final String SPA_ITEM_LIST_URL = "http://love.bshu.com/v1/music/lists";

    public static final String LOVE_INDEX_URL = "http://love.bshu.com/v1/Hotsearch/index";

    //音频收藏接口
    public static final String AUDIO_COLLECT_URL = "http://love.bshu.com/v1/music/collect";
    //音频收藏列表
    public static final String AUDIO_COLLECT_LIST_URL = "http://love.bshu.com/v1/music/collect_list";


    /**
     * spa随便听听
     */
    public static final String SPA_RANDOM_URL = "http://sleep.bshu.com/v1/spa/random";

    //音频播放次数
    public static final String AUDIO_PLAY_URL = "http://love.bshu.com/v1/music/play";


    //搜索次数统计
    public static final String SEARCH_COUNT_URL = baseUrlV1.concat("dialogue/searchlog");


    //默认值
    private static String sdPath = "/storage/emulated/0/Android/data/com.ant.flying/cache";
    private static String SD_DIR = sdPath;
    private static final String BASE_SD_DIR = SD_DIR + File.separator + "TNZBSQ";
    public static final String BASE_NORMAL_FILE_DIR = BASE_SD_DIR + File.separator + "files";


    public static String uploadPhotoUrl = baseUrlV1.concat("common/upload");


    public static final String ID_INFO_SMS = getBaseUrl() + "user/code";


    public static String getBaseUrl() {
        return (DEBUG ? debugBaseUrl : baseUrl);
    }

    public static String getUrl(String url) {
        return DEBUG ? debugBaseUrl : baseUrlV1.concat(url);
    }

    public static String getUrlV2(String url) {
        return DEBUG ? debugBaseUrl : baseUrlV2.concat(url);
    }

    // apk 下载地址
    public static String download_apk_url = "http://toppic-mszs.oss-cn-hangzhou.aliyuncs.com/xfzs.apk";

    public static String get_share_info = getBaseUrl() + "v1/Share/info";
//    public static String download_apk_url = "http://toppic-mszs.oss-cn-hangzhou.aliyuncs.com/";

}
