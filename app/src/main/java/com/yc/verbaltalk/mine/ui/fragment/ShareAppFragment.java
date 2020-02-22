package com.yc.verbaltalk.mine.ui.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.kk.share.UMShareImpl;
import com.kk.utils.ToastUtil;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXMiniProgramObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.yc.verbaltalk.R;
import com.yc.verbaltalk.chat.bean.ShareInfo;
import com.yc.verbaltalk.chat.bean.event.EventBusWxPayResult;
import com.yc.verbaltalk.model.constant.ConstantKey;
import com.yc.verbaltalk.base.engine.LoveEngine;
import com.yc.verbaltalk.model.single.YcSingle;
import com.yc.verbaltalk.base.view.LoadDialog;
import com.yc.verbaltalk.base.utils.ShareInfoHelper;

import org.greenrobot.eventbus.EventBus;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import androidx.core.content.ContextCompat;
import rx.Subscriber;

/**
 * Created by wanglin  on 2019/7/9 15:54.
 */
public class ShareAppFragment extends BottomSheetDialogFragment {


    protected Activity mContext;
    private BottomSheetDialog dialog;
    private View rootView;
    private BottomSheetBehavior<View> mBehavior;
    protected LoadDialog loadingView;

    private static final String TAG = "BaseBottomSheetDialogFr";

    private LinearLayout llWx;

    private LinearLayout llCircle;

    private TextView tvCancelShare;

    private ShareInfo mShareInfo;
    private LoveEngine loveEngin;
    private LinearLayout llMiniProgram;
    private IWXAPI api;

    public void setShareInfo(ShareInfo mShareInfo) {
        this.mShareInfo = mShareInfo;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = (Activity) context;
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();

        WindowManager.LayoutParams windowParams = window.getAttributes();
        //这里设置透明度
        windowParams.dimAmount = 0.5f;
//        windowParams.width = (int) (ScreenUtil.getWidth(mContext) * 0.98);
        window.setAttributes(windowParams);
        window.setWindowAnimations(R.style.share_anim);
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        dialog = new BottomSheetDialog(getContext(), getTheme());
        if (rootView == null) {
            //缓存下来的 View 当为空时才需要初始化 并缓存
            rootView = LayoutInflater.from(mContext).inflate(getLayoutId(), null);
            loadingView = new LoadDialog(mContext);
            llWx = rootView.findViewById(R.id.ll_wx);
            llCircle = rootView.findViewById(R.id.ll_circle);
            llMiniProgram = rootView.findViewById(R.id.ll_mini_program);
            tvCancelShare = rootView.findViewById(R.id.tv_cancel_share);
        }


        dialog.setContentView(rootView);

        mBehavior = BottomSheetBehavior.from((View) rootView.getParent());
        ((View) rootView.getParent()).setBackgroundColor(Color.TRANSPARENT);
        rootView.post(() -> {
            /**
             * PeekHeight 默认高度 256dp 会在该高度上悬浮
             * 设置等于 view 的高 就不会卡住
             */
            mBehavior.setPeekHeight(rootView.getHeight());
        });
        init();
        return dialog;
    }

    private void init() {
         api = WXAPIFactory.createWXAPI(mContext, ConstantKey.WX_APP_ID, false);
        loveEngin = new LoveEngine(mContext);
        tvCancelShare.setOnClickListener(v -> dismiss());
        List<View> shareItemViews = new ArrayList<>();
        shareItemViews.add(llWx);
        shareItemViews.add(llCircle);
        shareItemViews.add(llMiniProgram);

        for (int i = 0; i < shareItemViews.size(); i++) {
            View view = shareItemViews.get(i);
            view.setTag(i);
            final int finalI = i;
            view.setOnClickListener(v -> shareInfo(finalI));
        }


    }

    private void shareInfo(int tag) {
        String title = "表白神器——" + mContext.getString(R.string.app_name) + "！会聊才会撩，撩到心动的TA！";
        String url = "http://tic.upkao.com/apk/love.apk";
        String desc = "谈恋爱时，你是否因为不会聊天而苦恼过？\n" +
                "和女生聊天没有话题？遇见喜欢的人不敢搭讪？无法吸引TA的注意？猜不透女生的心意？恋爱话术宝，一款专门为您解决以上所有情感烦恼的APP。从搭讪开场到线下邀约互动对话，妹子的回复再也不用担心没话聊。只需搜一搜，N+精彩聊天回复任你选择，让您和女生聊天不再烦恼，全方面提高您的聊天能力。";

        mShareInfo = ShareInfoHelper.getShareInfo();
        if (mShareInfo != null) {
            if (!TextUtils.isEmpty(mShareInfo.getUrl())) {
                url = mShareInfo.getUrl();
            }
            if (!TextUtils.isEmpty(mShareInfo.getTitle())) {
                title = mShareInfo.getTitle();
            }
            if (!TextUtils.isEmpty(mShareInfo.getDesp())) {
                desc = mShareInfo.getDesp();
            }
        }

        dismiss();

//        if (bitmap != null) {
//            UMShareImpl.get().setCallback(mContext, umShareListener).shareImage("app", bitmap, bitmap, getShareMedia(tag.toString() + ""))
//        } else {
        if (tag == 2) {
            shareMiniProgram(title, desc, R.mipmap.ic_launcher);
//            UMShareImpl.get().setCallback(mContext, umShareListener).shareMiniParam(title, desc, R.mipmap.ic_launcher, "gh_d063497205cc");
        } else {
            UMShareImpl.get().setCallback(mContext, umShareListener).shareUrl(title, url, desc, R.mipmap.ic_launcher, getShareMedia(tag + ""));
        }
//        }
    }


    private void shareMiniProgram(String title, String desc, int drawableId) {

//        api.registerApp(ConstantKey.WX_APP_ID);
        WXMiniProgramObject miniProgramObj = new WXMiniProgramObject();
        miniProgramObj.webpageUrl = "https://sj.qq.com/myapp/detail.htm?apkName=com.yc.verbaltalk"; // 兼容低版本的网页链接
        miniProgramObj.miniprogramType = WXMiniProgramObject.MINIPTOGRAM_TYPE_RELEASE;// 正式版:0，测试版:1，体验版:2
        miniProgramObj.userName = ConstantKey.WX_MINI_ORIGIN_ID;     // 小程序原始id
        miniProgramObj.path = "pages/index/index";            //小程序页面路径；对于小游戏，可以只传入 query 部分，来实现传参效果，如：传入 "?foo=bar"
        WXMediaMessage msg = new WXMediaMessage(miniProgramObj);
        msg.title = title;                    // 小程序消息title
        msg.description = desc;               // 小程序消息desc
        msg.thumbData = getThumb(drawableId);                      // 小程序消息封面图片，小于128k

        SendMessageToWX.Req req = new SendMessageToWX.Req();
//        req.transaction = buildTransaction("miniProgram");
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneSession;  // 目前只支持会话
        api.sendReq(req);
    }

    private byte[] getThumb(int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(mContext, drawableId);
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(),
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                        : Bitmap.Config.RGB_565);


        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight());
        drawable.draw(canvas);

        int size = bitmap.getWidth() * bitmap.getHeight();
        // 创建一个字节数组输出流,流的大小为size
        ByteArrayOutputStream baos = new ByteArrayOutputStream(size);
        // 设置位图的压缩格式，质量为100%，并放入字节数组输出流中
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        // 将字节数组输出流转化为字节数组byte[]
        return baos.toByteArray();

    }


    private SHARE_MEDIA getShareMedia(String tag) {
        if (tag.equals("0")) {
            MobclickAgent.onEvent(mContext, "share_with_wechat", "分享到微信");
            return SHARE_MEDIA.WEIXIN;
        }

        if (tag.equals("1")) {
            MobclickAgent.onEvent(mContext, "sharing_circle_id", "分享到朋友圈");
            return SHARE_MEDIA.WEIXIN_CIRCLE;
        }
        if (tag.equals("2")) {
            return SHARE_MEDIA.WEIXIN;
        }

        return SHARE_MEDIA.WEIXIN;

    }

    private boolean mIsShareMoney;

    public void setIsShareMoney(boolean isShareMoney) {
        this.mIsShareMoney = isShareMoney;
    }

    private UMShareListener umShareListener = new UMShareListener() {

        @Override
        public void onStart(SHARE_MEDIA share_media) {
            if (loadingView != null) {
                loadingView.setText("正在分享...");
                loadingView.showLoadingDialog();
            }
        }

        @Override
        public void onResult(SHARE_MEDIA share_media) {
            if (loadingView != null) {
                loadingView.dismissLoadingDialog();
            }
            ToastUtil.toast2(mContext, "分享成功");
            //            RxSPTool.putBoolean(mContext, SpConstant.SHARE_SUCCESS, true);

//            if (mShareInfo == null)
//                mShareInfo = ShareInfoHelper.shareInfo
//
//            mShareInfo ?.let {
            if (mIsShareMoney) {
                shareReward();
            }
//                else {
////                    if (!TextUtils.isEmpty(mShareInfo?.task_id)) {
//                    mPresenter.share(mShareInfo ?.task_id)
////                    }
//                }
//            }
        }

        @Override
        public void onError(SHARE_MEDIA share_media, Throwable throwable) {
            if (loadingView != null) {
                loadingView.dismissLoadingDialog();
            }
            ToastUtil.toast2(mContext, "分享有误");
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media) {
            if (loadingView != null) {

                loadingView.dismissLoadingDialog();
            }
            ToastUtil.toast2(mContext, "取消发送");
        }
    };

    private void shareReward() {
        int id = YcSingle.getInstance().id;
        if (id < 0) {
            return;
        }
        loveEngin.shareReward(id + "").subscribe(new Subscriber<ResultInfo<String>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResultInfo<String> stringResultInfo) {
                if (stringResultInfo != null && stringResultInfo.code == HttpConfig.STATUS_OK) {
                    //分享成功
                    EventBus.getDefault().post(new EventBusWxPayResult(0, "分享成功"));
                }
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(this.getClass().getSimpleName());
    }


    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(this.getClass().getSimpleName());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        //解除缓存 View 和当前 ViewGroup 的关联
        ((ViewGroup) (rootView.getParent())).removeView(rootView);
//        Runtime.getRuntime().gc();
    }

    public int getLayoutId() {
        return R.layout.fragment_share;
    }
}
