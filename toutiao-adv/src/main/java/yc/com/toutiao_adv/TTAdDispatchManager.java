package yc.com.toutiao_adv;

import android.app.Activity;
import android.view.ViewGroup;

/**
 * Created by suns  on 2020/1/6 09:54.
 * 头条广告分发
 */
public class TTAdDispatchManager {

    private static TTAdDispatchManager manager;
    private Activity mActivity;

    private OnAdvManagerListener managerListener;

    private TTAdDispatchManager() {
    }

    public static TTAdDispatchManager getManager() {
        if (manager == null) {
            synchronized (TTAdDispatchManager.class) {
                if (manager == null) {
                    manager = new TTAdDispatchManager();
                }
            }
        }
        return manager;
    }

    public void init(Activity activity, TTAdType adType, ViewGroup container, String adId, int nativeCount,
                     String rewardName, int rewardCount, String userId, int orientation, OnAdvStateListener onAdvStateListener) {
        this.mActivity = activity;
        if (adType == TTAdType.SPLASH) {
            managerListener = new SplashADManager(activity, container, adId, onAdvStateListener);
        } else if (adType == TTAdType.BANNER) {
            managerListener = new BannerADManager(activity, container, adId, onAdvStateListener);
        } else if (adType == TTAdType.NATIVE_EXPRESS) {
            managerListener = new NativeAdManager(activity, adId, nativeCount, onAdvStateListener);
        } else if (adType == TTAdType.INTERACTION_EXPRESS) {
            managerListener = new InteractionAdManager(activity, adId, onAdvStateListener);
        } else if (adType == TTAdType.REWARD_VIDEO) {
            managerListener = new RewardVideoAdManager(activity, adId, rewardName, rewardCount, userId, orientation,onAdvStateListener);
        } else if (adType == TTAdType.FULLSCREEN_VIDEO) {
            managerListener = new FullscreenVideoAdManager(activity, adId, orientation);
        }

        managerListener.showAD();
    }




    public void onResume() {
        if (managerListener != null) {
            managerListener.onResume();
        }
    }


    public void onStop() {
        if (managerListener != null) {
            managerListener.onStop();
        }
    }


    public void onDestroy() {
        if (managerListener != null) {
            managerListener.onDestroy();
        }
    }

}
