package yc.com.toutiao_adv;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdManager;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTFullScreenVideoAd;

/**
 * Created by suns  on 2020/1/6 15:19.
 */
public class FullscreenVideoAdManager implements OnAdvManagerListener {

    private String mAdId;
    private int mOrentation;
    private Activity mActiviy;
    private TTAdNative mTTAdNative;
    private TTFullScreenVideoAd mttFullVideoAd;

    public FullscreenVideoAdManager(Activity activity, String adId, int orientation) {

        this.mActiviy = activity;
        this.mAdId = adId;
        this.mOrentation = orientation;
    }

    @Override
    public void showAD() {
//step1:初始化sdk
        TTAdManager ttAdManager = TTAdManagerHolder.get();
        //step2:(可选，强烈建议在合适的时机调用):申请部分权限，如read_phone_state,防止获取不了imei时候，下载类广告没有填充的问题。
        TTAdManagerHolder.get().requestPermissionIfNecessary(mActiviy);
        //step3:创建TTAdNative对象,用于调用广告请求接口
        mTTAdNative = ttAdManager.createAdNative(mActiviy);

        loadAd(mAdId, mOrentation);
    }


    private void loadAd(String codeId, int orientation) {
        //step4:创建广告请求参数AdSlot,具体参数含义参考文档
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(codeId)
                .setSupportDeepLink(true)
                .setImageAcceptedSize(1080, 1920)
                .setOrientation(orientation)//必填参数，期望视频的播放方向：TTAdConstant.HORIZONTAL 或 TTAdConstant.VERTICAL
                .build();
        //step5:请求广告
        mTTAdNative.loadFullScreenVideoAd(adSlot, new TTAdNative.FullScreenVideoAdListener() {
            @Override
            public void onError(int code, String message) {

            }

            @Override
            public void onFullScreenVideoAdLoad(TTFullScreenVideoAd ad) {

                mttFullVideoAd = ad;
                mttFullVideoAd.setFullScreenVideoAdInteractionListener(new TTFullScreenVideoAd.FullScreenVideoAdInteractionListener() {

                    @Override
                    public void onAdShow() {
                        Log.e("fullscreenvideo", "onAdShow: " + "FullVideoAd show");
                    }

                    @Override
                    public void onAdVideoBarClick() {
                        Log.e("fullscreenvideo", "FullVideoAd bar click");
                    }

                    @Override
                    public void onAdClose() {
                        Log.e("fullscreenvideo", "FullVideoAd close");
                    }

                    @Override
                    public void onVideoComplete() {
                        Log.e("fullscreenvideo", "FullVideoAd complete");
                    }

                    @Override
                    public void onSkippedVideo() {
                        Log.e("fullscreenvideo", "FullVideoAd skipped");

                    }

                });
            }

            @Override
            public void onFullScreenVideoCached() {
                Log.e("fullscreenvideo", "FullVideoAd video cached");
            }
        });
        if (mttFullVideoAd != null) {
            //step6:在获取到广告后展示
            //该方法直接展示广告
            //mttFullVideoAd.showFullScreenVideoAd(FullScreenVideoActivity.this);

            //展示广告，并传入广告展示的场景
            mttFullVideoAd.showFullScreenVideoAd(mActiviy, TTAdConstant.RitScenes.GAME_GIFT_BONUS, null);
            mttFullVideoAd = null;
        } else {
            Toast.makeText(mActiviy, "请先加载广告", Toast.LENGTH_SHORT).show();
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
