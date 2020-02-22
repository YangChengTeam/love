package com.yc.verbaltalk.base.config;

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
    public static final String AUDIO_DATA_LIST_URL = baseUrlV1.concat("music/cats");
    //音频列表
    public static final String AUDIO_ITEM_LIST_URL = baseUrlV1.concat("music/lists");

    public static final String LOVE_INDEX_URL = baseUrlV1.concat("Hotsearch/index");

    //音频收藏接口
    public static final String AUDIO_COLLECT_URL = baseUrlV1.concat("music/collect");
    //音频收藏列表
    public static final String AUDIO_COLLECT_LIST_URL = baseUrlV1.concat("music/collect_list");

    //首页搜索框下拉热词
    public static final String INDEX_DROP_URL = baseUrlV1.concat("hotsearch/dropdown");

    //音频详情
    public static final String AUDIO_DETAIL_URL = baseUrlV1.concat("music/info");

    //分享得会员
    public static final String SHARE_REWARD_URL = baseUrlV1.concat("share/reward");

    /**
     * spa随便听听
     */
    public static final String SPA_RANDOM_URL = baseUrlV1.concat("spa/random");

    //音频播放次数
    public static final String AUDIO_PLAY_URL = baseUrlV1.concat("music/play");


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

    public static final String WECHAT_INFO_URL = baseUrlV1.concat("config/index");

    //最新帖子
    public static final String COMMUNITY_NEWEST_LIST_URL = baseUrlV1.concat("topic/newlist");

    //帖子点赞
    public static final String TOPIC_LIKE_URL = baseUrlV1.concat("topic/dig");

    //帖子详情
    public static final String TOPIC_DETAIL_URL = baseUrlV1.concat("topic/detail");

    //创建评论
    public static final String CREATE_COMMENT_URL = baseUrlV1.concat("TopicComment/create");

    //评论点赞
    public static final String COMMENT_LIKE_URL = baseUrlV1.concat("TopicComment/dig");

    //评论标签
    public static final String COMMUNITY_TAG_URL = baseUrlV1.concat("TopicCat/all");

    //热门帖子
    public static final String COMMUNITY_HOT_LIST_URL = baseUrlV1.concat("topic/hotlist");
    //发帖
    public static final String PUBLISH_COMMUNITY_URL = baseUrlV1.concat("topic/post");

    //我的发帖
    public static final String COMMUNITY_MY_URL = baseUrlV1.concat("Topic/mylist");
    //顶部公告
    public static final String TOP_TOPIC_URL = baseUrlV1.concat("Topic/top");
    //热门标签列表
    public static final String COMMUNITY_TAG_LIST_URL = baseUrlV1.concat("Topic/catlist");

    //首页banner
    public static final String INDEX_BANNER_URL = baseUrlV1.concat("banner/index");

    //撩吧课程数据
    public static final String CHAT_LESSONS_URL= baseUrlV1.concat("Lesson/lists");

    //撩吧课程详情数据
    public static final String CHAT_LESSON_DETAIL_URL= baseUrlV1.concat("Lesson/detail");
//    public static String download_apk_url = "http://toppic-mszs.oss-cn-hangzhou.aliyuncs.com/";

}
