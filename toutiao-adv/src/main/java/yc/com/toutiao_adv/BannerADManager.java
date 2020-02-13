package yc.com.toutiao_adv;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdDislike;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTBannerAd;
import com.bytedance.sdk.openadsdk.TTNativeAd;
import com.bytedance.sdk.openadsdk.TTNativeExpressAd;

import java.util.List;

/**
 * Created by suns  on 2020/1/6 11:38.
 */
public class BannerADManager implements OnAdvManagerListener {
    private OnAdvStateListener mStateListener;
    private ViewGroup mContainer;
    private String mBannerId;
    private Activity mActivity;
    private TTAdNative mTTAdNative;
    private TTNativeExpressAd mTTAd;
    private long startTime;

    BannerADManager(Activity activity, ViewGroup container, String bannerId, OnAdvStateListener stateListener) {
        this.mActivity = activity;
        this.mBannerId = bannerId;
        this.mContainer = container;
        this.mStateListener = stateListener;
    }

    @Override
    public void showAD() {
        mTTAdNative = TTAdManagerHolder.get().createAdNative(mActivity);
        //step3:(可选，强烈建议在合适的时机调用):申请部分权限，如read_phone_state,防止获取不了imei时候，下载类广告没有填充的问题。
        TTAdManagerHolder.get().requestPermissionIfNecessary(mActivity);

        loadBannerAd(mBannerId);
    }


    private void loadBannerAd(String codeId) {
        //step4:创建广告请求参数AdSlot,具体参数含义参考文档
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(codeId) //广告位id
                .setSupportDeepLink(true)
                .setExpressViewAcceptedSize(360, 0)
                .setImageAcceptedSize(600, 257)
                .setAdCount(1) //请求广告数量为1到3条
                .build();
        //step5:请求广告，对请求回调的广告作渲染处理
        mTTAdNative.loadBannerExpressAd(adSlot, new TTAdNative.NativeExpressAdListener() {
            @Override
            public void onError(int i, String message) {
                Log.e("TAG", "onError: " + message);
                mContainer.removeAllViews();
            }

            @Override
            public void onNativeExpressAdLoad(List<TTNativeExpressAd> ads) {
                if (ads == null || ads.size() == 0) {
                    return;
                }
                mTTAd = ads.get(0);
//                mTTAd.setSlideIntervalTime(30*1000);
                bindAdListener(mTTAd);
                startTime = System.currentTimeMillis();
                mTTAd.render();
            }

        });

    }

    private void bindAdListener(TTNativeExpressAd ad) {
        ad.setExpressInteractionListener(new TTNativeExpressAd.ExpressAdInteractionListener() {
            @Override
            public void onAdClicked(View view, int type) {
                mStateListener.clickAD();
            }

            @Override
            public void onAdShow(View view, int type) {
                mStateListener.loadSuccess();
            }

            @Override
            public void onRenderFail(View view, String msg, int code) {
                Log.e("ExpressView", "render fail:" + (System.currentTimeMillis() - startTime));

            }

            @Override
            public void onRenderSuccess(View view, float width, float height) {
                Log.e("ExpressView", "render suc:" + (System.currentTimeMillis() - startTime));
                //返回view的宽高 单位 dp

                mContainer.removeAllViews();
                mContainer.addView(view);
            }
        });
        //dislike设置
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
