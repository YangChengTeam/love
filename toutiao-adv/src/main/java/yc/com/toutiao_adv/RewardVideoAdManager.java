package yc.com.toutiao_adv;

import android.app.Activity;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdManager;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTRewardVideoAd;

/**
 * Created by suns  on 2020/1/6 14:44.
 */
public class RewardVideoAdManager implements OnAdvManagerListener, WeakHandler.IHandler {

    private OnAdvStateListener stateListener;
    private int mRewardCount;
    private int mOrientation;
    private String mRewardName;
    private String mUserId;
    private String mAdId;
    private Activity mActivity;
    private TTAdNative mTTAdNative;
    private TTRewardVideoAd mttRewardVideoAd;
    private WeakHandler mHandler = new WeakHandler(this);

    private final int LOAD_SUCCESS = 100;

    RewardVideoAdManager(Activity activity, String adId, String rewardName, int rewardCount, String userId, int orientation, OnAdvStateListener listener) {

        this.mActivity = activity;
        this.mAdId = adId;
        this.mRewardName = rewardName;
        this.mUserId = userId;
        this.mOrientation = orientation;
        this.mRewardCount = rewardCount;
        this.stateListener = listener;
    }

    @Override
    public void showAD() {
        //step1:初始化sdk
        TTAdManager ttAdManager = TTAdManagerHolder.get();
        //step2:(可选，强烈建议在合适的时机调用):申请部分权限，如read_phone_state,防止获取不了imei时候，下载类广告没有填充的问题。
        TTAdManagerHolder.get().requestPermissionIfNecessary(mActivity);
        //step3:创建TTAdNative对象,用于调用广告请求接口
        mTTAdNative = ttAdManager.createAdNative(mActivity);

        loadAd(mAdId, mOrientation, mRewardName, mRewardCount, mUserId);
    }

    private void loadAd(String codeId, int orientation, String rewardName, int rewardCount, String userId) {
        //step4:创建广告请求参数AdSlot,具体参数含义参考文档
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(codeId)
                .setSupportDeepLink(true)
                .setImageAcceptedSize(1080, 1920)
                .setRewardName(rewardName) //奖励的名称 金币
                .setRewardAmount(rewardCount)  //奖励的数量
                .setUserID(userId)//用户id,必传参数
                .setMediaExtra("media_extra") //附加参数，可选
                .setOrientation(orientation) //必填参数，期望视频的播放方向：TTAdConstant.HORIZONTAL 或 TTAdConstant.VERTICAL
                .build();
        //step5:请求广告
        mTTAdNative.loadRewardVideoAd(adSlot, new TTAdNative.RewardVideoAdListener() {
            @Override
            public void onError(int code, String message) {

            }

            //视频广告加载后，视频资源缓存到本地的回调，在此回调后，播放本地视频，流畅不阻塞。
            @Override
            public void onRewardVideoCached() {
                mHandler.sendEmptyMessage(LOAD_SUCCESS);
            }

            //视频广告的素材加载完毕，比如视频url等，在此回调后，可以播放在线视频，网络不好可能出现加载缓冲，影响体验。
            @Override
            public void onRewardVideoAdLoad(TTRewardVideoAd ad) {

                mttRewardVideoAd = ad;
                mHandler.sendEmptyMessage(LOAD_SUCCESS);
                mttRewardVideoAd.setRewardAdInteractionListener(new TTRewardVideoAd.RewardAdInteractionListener() {

                    @Override
                    public void onAdShow() {
                        Log.e("rewardvideo", "onAdShow: " + "rewardVideoAd show");
                    }

                    @Override
                    public void onAdVideoBarClick() {
                        Log.e("rewardvideo", "rewardVideoAd bar click");
                    }

                    @Override
                    public void onAdClose() {
                        Log.e("rewardvideo", "rewardVideoAd close");
                        stateListener.clickAD();
                    }

                    //视频播放完成回调
                    @Override
                    public void onVideoComplete() {
                        Log.e("rewardvideo", "rewardVideoAd complete");
                    }

                    @Override
                    public void onVideoError() {
                        Log.e("rewardvideo", "rewardVideoAd error");
                    }

                    //视频播放完成后，奖励验证回调，rewardVerify：是否有效，rewardAmount：奖励梳理，rewardName：奖励名称
                    @Override
                    public void onRewardVerify(boolean rewardVerify, int rewardAmount, String rewardName) {
                        Log.e("rewardvideo", "verify:" + rewardVerify + " amount:" + rewardAmount +
                                " name:" + rewardName);
                    }

                    @Override
                    public void onSkippedVideo() {
                        Log.e("rewardvideo", "rewardVideoAd has onSkippedVideo");
                    }
                });

            }
        });

    }


    private void loadRewardAd() {
        if (mttRewardVideoAd != null) {
            //step6:在获取到广告后展示
            //该方法直接展示广告
//                    mttRewardVideoAd.showRewardVideoAd(RewardVideoActivity.this);

            //展示广告，并传入广告展示的场景
            mttRewardVideoAd.showRewardVideoAd(mActivity, TTAdConstant.RitScenes.CUSTOMIZE_SCENES, "scenes_test");
            mttRewardVideoAd = null;
            Log.e("TAG", "loadRewardAd: " + Thread.currentThread().getName());
        }
//        else {
//            Toast.makeText(mActivity, "请先加载广告", Toast.LENGTH_SHORT).show();
//        }
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

    @Override
    public void handleMsg(Message msg) {
        if (msg.what == LOAD_SUCCESS) {
            loadRewardAd();
        }
    }
}
