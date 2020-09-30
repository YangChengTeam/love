package yc.com.toutiao_adv;

import android.app.Activity;
import android.util.Log;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdDislike;
import com.bytedance.sdk.openadsdk.TTAdManager;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTFeedAd;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by suns  on 2020/9/16 14:55.
 */
public class DrawFeedAdManager implements OnAdvManagerListener {
    private int mCount;//加载信息流广告的数量
    private String mAdId;
    private Activity mActivity;
    private TTAdNative mTTAdNative;
    private List<TTFeedAd> feedAdList;
    private OnAdvStateListener onAdvStateListener;

    public DrawFeedAdManager(Activity activity, String adId, int count, OnAdvStateListener stateListener) {
        this.mActivity = activity;
        this.mAdId = adId;
        this.mCount = count;
        this.onAdvStateListener = stateListener;
        feedAdList = new ArrayList<>();
    }

    @Override
    public void showAD() {
        //step1:初始化sdk
        TTAdManager ttAdManager = TTAdManagerHolder.get();
        //step2:(可选，强烈建议在合适的时机调用):申请部分权限，如read_phone_state,防止获取不了imei时候，下载类广告没有填充的问题。
        TTAdManagerHolder.get().requestPermissionIfNecessary(mActivity);
        //step3:创建TTAdNative对象,用于调用广告请求接口
        mTTAdNative = ttAdManager.createAdNative(mActivity);

        loadAd(mAdId, mCount);
    }

    private void loadAd(String mAdId, int count) {
        //feed广告请求类型参数
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(mAdId)
                .setSupportDeepLink(true)
                .setImageAcceptedSize(640, 320)
                .setAdCount(count)

                .build();
        //调用feed广告异步请求接口
        mTTAdNative.loadFeedAd(adSlot, new TTAdNative.FeedAdListener() {
            @Override
            public void onError(int code, String message) {
                Log.e("TAG", "onError: " + code);
            }

            @Override
            public void onFeedAdLoad(List<TTFeedAd> ads) {

                if (ads == null || ads.isEmpty()) {
                    Log.e("TAG", "onFeedAdLoad: ad is null!");
                    return;
                }


                int count = feedAdList.size();
                for (TTFeedAd ad : ads) {
                    bindAdListener(ad);
                    feedAdList.add(ad);
                }
                onAdvStateListener.onDrawFeedAd(feedAdList);
            }
        });
    }

    private void bindAdListener(TTFeedAd ttFeedAd) {
        TTAdDislike ttAdDislike = ttFeedAd.getDislikeDialog(mActivity);
        if (ttAdDislike != null) {
            ttAdDislike.setDislikeInteractionCallback(new TTAdDislike.DislikeInteractionCallback() {
                @Override
                public void onSelected(int position, String value) {
                    //用户选择了第position条理由，该条理由的内容为value
                    Log.e("TAG", "onSelected: position=" + position + "  value=" + value);
                }

                @Override
                public void onCancel() {

                }

                @Override
                public void onRefuse() {

                }
            });

        }
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onDestroy() {

    }
}
