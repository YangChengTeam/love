package yc.com.toutiao_adv;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTNativeExpressAd;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by suns  on 2020/1/6 11:50.
 */
public class NativeAdManager implements OnAdvManagerListener {

    private OnAdvStateListener stateListener;
    private int mNativeCount;
    private String mNativeAd;
    private Activity mActivity;
    private TTAdNative mTTAdNative;

    private long startTime;
    private List<TTNativeExpressAd> mDatas;

    NativeAdManager(Activity activity, String nativeAd, int nativeCount, OnAdvStateListener listener) {
        this.mActivity = activity;

        this.mNativeAd = nativeAd;
        this.mNativeCount = nativeCount;
        this.stateListener = listener;
        mDatas = new ArrayList<>();
    }

    @Override
    public void showAD() {
//step2:创建TTAdNative对象，createAdNative(Context context) banner广告context需要传入Activity对象
        mTTAdNative = TTAdManagerHolder.get().createAdNative(mActivity);
        //step3:(可选，强烈建议在合适的时机调用):申请部分权限，如read_phone_state,防止获取不了imei时候，下载类广告没有填充的问题。
        TTAdManagerHolder.get().requestPermissionIfNecessary(mActivity);
        loadExpressAd(mNativeAd, mNativeCount);
    }

    private void loadExpressAd(String codeId, int nativeCount) {

        float expressViewWidth = 350;
        float expressViewHeight = 0;

        //step4:创建广告请求参数AdSlot,具体参数含义参考文档
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(codeId) //广告位id
                .setSupportDeepLink(true)
                .setAdCount(nativeCount) //请求广告数量为1到3条
                .setExpressViewAcceptedSize(expressViewWidth, expressViewHeight) //期望模板广告view的size,单位dp
                .setImageAcceptedSize(640, 320)//这个参数设置即可，不影响模板广告的size
                .build();
        //step5:请求广告，对请求回调的广告作渲染处理
        mTTAdNative.loadNativeExpressAd(adSlot, new TTAdNative.NativeExpressAdListener() {
            @Override
            public void onError(int code, String message) {

            }

            @Override
            public void onNativeExpressAdLoad(List<TTNativeExpressAd> ads) {
                if (ads == null || ads.size() == 0) {
                    return;
                }
//                mTTAd = ads.get(0);
                mDatas.clear();
                bindAdListener(ads);
                startTime = System.currentTimeMillis();
//                mTTAd.render();
            }
        });
    }

    private void bindAdListener(List<TTNativeExpressAd> ads) {

        for (final TTNativeExpressAd ad : ads) {

            ad.setExpressInteractionListener(new TTNativeExpressAd.ExpressAdInteractionListener() {
                @Override
                public void onAdClicked(View view, int type) {

                }

                @Override
                public void onAdShow(View view, int type) {
                    Log.e("ExpressView", "onAdShow: " + (System.currentTimeMillis() - startTime));
                }

                @Override
                public void onRenderFail(View view, String msg, int code) {
                    Log.e("ExpressView", "render fail:" + (System.currentTimeMillis() - startTime));

                }

                @Override
                public void onRenderSuccess(View view, float width, float height) {
                    Log.e("ExpressView", "render suc:" + (System.currentTimeMillis() - startTime));
                    //返回view的宽高 单位 dp
                    mDatas.add(ad);
//                    mContainer.removeAllViews();
//                    mContainer.addView(view);
                }
            });
            ad.render();
            //dislike设置
//        bindDislike(ad, false);
            if (ad.getInteractionType() != TTAdConstant.INTERACTION_TYPE_DOWNLOAD) {
                return;
            }
        }
        if (mDatas.size() > 0) {
            stateListener.onTTNativeExpressed(mDatas);
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
        if (mDatas != null) {
            for (TTNativeExpressAd ad : mDatas) {
                if (ad != null) {
                    ad.destroy();
                }
            }
        }

    }
}
