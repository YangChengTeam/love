package yc.com.toutiao_adv;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.bytedance.sdk.openadsdk.TTAdConstant;

/**
 * Created by suns  on 2020/1/6 09:54.
 * 头条广告分发
 */
public class TTAdDispatchManager {

    private static TTAdDispatchManager manager;
    private Activity mActivity;

    private OnAdvManagerListener managerListener;
    private boolean isShowAd = true;

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

    public void init(Enum<BrandType>... branTypes) {
        if (branTypes.length > 0) {
            for (Enum<BrandType> branType : branTypes) {
                if (TextUtils.equals(Build.BRAND.toLowerCase(), branType.name())) {
                    isShowAd = false;
                }
            }
        } else {
            isShowAd = true;
        }

    }

    public void init(Activity activity,  TTAdType adType, ViewGroup container, String adId, int nativeCount,
                     String rewardName, int rewardCount, String userId, int orientation, OnAdvStateListener onAdvStateListener) {
        this.mActivity = activity;
//        if (!isVip && isShowAd) {
            if (adType == TTAdType.SPLASH) {
                managerListener = new SplashADManager(activity, container, adId, onAdvStateListener);
            } else if (adType == TTAdType.BANNER) {
                managerListener = new BannerADManager(activity, container, adId, onAdvStateListener);
            } else if (adType == TTAdType.NATIVE_EXPRESS) {
                managerListener = new NativeAdManager(activity, adId, nativeCount, onAdvStateListener);
            } else if (adType == TTAdType.INTERACTION_EXPRESS) {
                managerListener = new InteractionAdManager(activity, adId, onAdvStateListener);
            } else if (adType == TTAdType.REWARD_VIDEO) {
                managerListener = new RewardVideoAdManager(activity, adId, rewardName, rewardCount, userId, orientation, onAdvStateListener);
            } else if (adType == TTAdType.FULLSCREEN_VIDEO) {
                managerListener = new FullscreenVideoAdManager(activity, adId, orientation);
            }else if (adType==TTAdType.DRAW_FEED){
                managerListener= new DrawFeedAdManager(activity, adId,nativeCount,onAdvStateListener);
            }

                managerListener.showAD();

//        } else {
//
//            if (onHideAdvListener != null) {
//                onHideAdvListener.onHide();
//            }
//        }
    }

    private void showAdDialog(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("观看视频即可使用神器功能！！");
        builder.setPositiveButton("确定", (dialog, which) -> {
            Toast.makeText(context, "确定", Toast.LENGTH_SHORT).show();
        }).create().show();

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
