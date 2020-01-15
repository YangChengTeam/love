package yc.com.toutiao_adv;

/**
 * Created by suns  on 2020/1/6 10:29.
 * 头条广告类型
 * 1.开屏广告
 * 2.banner广告
 * 3.原生广告
 * 4.插屏广告
 * 5.激励视频广告
 * 6.全屏视频广告
 * 7.类似抖音的信息流广告
 */
public enum TTAdType {
    SPLASH(1), BANNER(2), NATIVE_EXPRESS(3), INTERACTION_EXPRESS(4), REWARD_VIDEO(5), FULLSCREEN_VIDEO(6), DRAW_FEED(7);
    public int type;

    TTAdType(int i) {
        this.type = i;
    }
}
