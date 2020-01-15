package yc.com.toutiao_adv;

import android.content.Context;

import com.bytedance.sdk.openadsdk.TTAdConfig;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdManager;
import com.bytedance.sdk.openadsdk.TTAdSdk;

/**
 * 可以用一个单例来保存TTAdManager实例，在需要初始化sdk的时候调用
 * 一般放在application中初始化
 */
public class TTAdManagerHolder {

    private static boolean sInit;

    private static TTAdConfig.Builder builder;

    public static TTAdManager get() {
        if (!sInit) {
            throw new RuntimeException("TTAdSdk is not init, please check.");
        }
        return TTAdSdk.getAdManager();
    }

    public static void init(Context context, String tt_ad_id) {
        doInit(context, tt_ad_id);
    }

    //step1:接入网盟广告sdk的初始化操作，详情见接入文档和穿山甲平台说明
    private static void doInit(Context context, String tt_ad_id) {
        if (!sInit) {
            TTAdSdk.init(context, buildConfig(context, tt_ad_id).build());
            sInit = true;
        }
    }

    public static TTAdConfig.Builder buildConfig(Context context, String tt_ad_id) {
        if (builder == null) {
            builder = new TTAdConfig.Builder()
                    .appId(tt_ad_id)
                    .paid(true)// 可选参数，设置是否为计费用户：true计费用户、false非计费用户。默认为false非计费用户
                    .useTextureView(true) //使用TextureView控件播放视频,默认为SurfaceView,当有SurfaceView冲突的场景，可以使用TextureView
                    .appName(context.getString(R.string.app_name))
                    .titleBarTheme(TTAdConstant.TITLE_BAR_THEME_DARK)
                    .allowShowNotify(true) //是否允许sdk展示通知栏提示
                    .allowShowPageWhenScreenLock(true) //是否在锁屏场景支持展示广告落地页
                    .debug(true) //测试阶段打开，可以通过日志排查问题，上线时去除该调用
                    .directDownloadNetworkType(TTAdConstant.NETWORK_STATE_WIFI, TTAdConstant.NETWORK_STATE_3G) //允许直接下载的网络状态集合
                    .supportMultiProcess(false);//是否支持多进程
            //.httpStack(new MyOkStack3())//自定义网络库，demo中给出了okhttp3版本的样例，其余请自行开发或者咨询工作人员。;
        }


        return builder;
    }
}
