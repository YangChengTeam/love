package yc.com.toutiao_adv;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTNativeExpressAd;

import java.util.List;

/**
 * Created by suns  on 2020/1/6 14:29.
 */
public class InteractionAdManager implements OnAdvManagerListener {

    private OnAdvStateListener stateListener;
    private String mAdId;
    private Activity mActivity;
    private TTAdNative mTTAdNative;
    private TTNativeExpressAd mTTAd;
    private long startTime;

    public InteractionAdManager(Activity activity, String adId, OnAdvStateListener listener) {
        this.mActivity = activity;
        this.mAdId = adId;
        this.stateListener = listener;
    }

    @Override
    public void showAD() {
//step2:创建TTAdNative对象，createAdNative(Context context) banner广告context需要传入Activity对象
        mTTAdNative = TTAdManagerHolder.get().createAdNative(mActivity);
        //step3:(可选，强烈建议在合适的时机调用):申请部分权限，如read_phone_state,防止获取不了imei时候，下载类广告没有填充的问题。
        TTAdManagerHolder.get().requestPermissionIfNecessary(mActivity);
        loadExpressAd(mAdId);
    }

    private void loadExpressAd(String codeId) {
        float expressViewWidth = 350;
        float expressViewHeight = 350;

        //step4:创建广告请求参数AdSlot,具体参数含义参考文档
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(codeId) //广告位id
                .setSupportDeepLink(true)
                .setAdCount(1) //请求广告数量为1到3条
                .setExpressViewAcceptedSize(expressViewWidth, expressViewHeight) //期望模板广告view的size,单位dp
                .setImageAcceptedSize(640, 320)//这个参数设置即可，不影响模板广告的size
                .build();
        //step5:请求广告，对请求回调的广告作渲染处理
        mTTAdNative.loadInteractionExpressAd(adSlot, new TTAdNative.NativeExpressAdListener() {
            @Override
            public void onError(int code, String message) {

            }

            @Override
            public void onNativeExpressAdLoad(List<TTNativeExpressAd> ads) {
                if (ads == null || ads.size() == 0) {
                    return;
                }
                mTTAd = ads.get(0);
                bindAdListener(mTTAd);
                startTime = System.currentTimeMillis();
                mTTAd.render();
            }
        });
    }

    private void bindAdListener(TTNativeExpressAd ad) {
        ad.setExpressInteractionListener(new TTNativeExpressAd.AdInteractionListener() {
            @Override
            public void onAdDismiss() {

            }

            @Override
            public void onAdClicked(View view, int type) {
                stateListener.clickAD();
            }

            @Override
            public void onAdShow(View view, int type) {

            }

            @Override
            public void onRenderFail(View view, String msg, int code) {
                Log.e("ExpressView", "render fail:" + (System.currentTimeMillis() - startTime));

            }

            @Override
            public void onRenderSuccess(View view, float width, float height) {
                Log.e("ExpressView", "render suc:" + (System.currentTimeMillis() - startTime));
                //返回view的宽高 单位 dp

                mTTAd.showInteractionExpressAd(mActivity);
                stateListener.loadSuccess();
            }
        });

        if (ad.getInteractionType() != TTAdConstant.INTERACTION_TYPE_DOWNLOAD) {
            return;
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
        if (mTTAd != null) {
            mTTAd.destroy();
        }
    }
}
