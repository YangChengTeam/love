package com.yc.love.ui.frament;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kk.share.UMShareImpl;
import com.kk.utils.ToastUtil;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.yc.love.R;
import com.yc.love.model.bean.ShareInfo;
import com.yc.love.ui.view.LoadingDialog;
import com.yc.love.utils.ShareInfoHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wanglin  on 2019/7/9 15:54.
 */
public class ShareAppFragment extends BottomSheetDialogFragment {


    protected Activity mContext;
    private BottomSheetDialog dialog;
    private View rootView;
    private BottomSheetBehavior<View> mBehavior;
    protected LoadingDialog loadingView;

    private static final String TAG = "BaseBottomSheetDialogFr";

    private LinearLayout llWx;

    private LinearLayout llCircle;

    private TextView tvCancelShare;

    private ShareInfo mShareInfo;

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
            loadingView = new LoadingDialog(mContext);
            llWx = rootView.findViewById(R.id.ll_wx);
            llCircle = rootView.findViewById(R.id.ll_circle);
            tvCancelShare = rootView.findViewById(R.id.tv_cancel_share);
        }


        dialog.setContentView(rootView);

        mBehavior = BottomSheetBehavior.from((View) rootView.getParent());
        ((View) rootView.getParent()).setBackgroundColor(Color.TRANSPARENT);
        rootView.post(new Runnable() {
            @Override
            public void run() {
                /**
                 * PeekHeight 默认高度 256dp 会在该高度上悬浮
                 * 设置等于 view 的高 就不会卡住
                 */
                mBehavior.setPeekHeight(rootView.getHeight());
            }
        });
        init();
        return dialog;
    }

    private void init() {
        tvCancelShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        List<View> shareItemViews = new ArrayList<View>();
        shareItemViews.add(llWx);
        shareItemViews.add(llCircle);

        for (int i = 0; i < shareItemViews.size(); i++) {
            View view = shareItemViews.get(i);
            view.setTag(i);
            final int finalI = i;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shareInfo(finalI);
                }
            });
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
        UMShareImpl.get().setCallback(mContext, umShareListener).shareUrl(title, url, desc, R.mipmap.ic_launcher, getShareMedia(tag + ""));
//        }
    }


    private SHARE_MEDIA getShareMedia(String tag) {
        if (tag.equals("0")) {
            return SHARE_MEDIA.WEIXIN;
        }

        if (tag.equals("1")) {
            return SHARE_MEDIA.WEIXIN_CIRCLE;
        }

        return SHARE_MEDIA.WEIXIN;

    }

    private UMShareListener umShareListener = new UMShareListener() {

        @Override
        public void onStart(SHARE_MEDIA share_media) {
            if (loadingView != null) {
                loadingView.setText("正在分享...");
                loadingView.showLoading();
            }
        }

        @Override
        public void onResult(SHARE_MEDIA share_media) {
            if (loadingView != null) {
                loadingView.dismissLoading();
            }
            ToastUtil.toast2(mContext, "分享成功");
            //            RxSPTool.putBoolean(mContext, SpConstant.SHARE_SUCCESS, true);

//            if (mShareInfo == null)
//                mShareInfo = ShareInfoHelper.shareInfo
//
//            mShareInfo ?.let {
//                if (mIsShareMoney) {
//                    //                    mPresenter.shareMoney();
//                } else {
////                    if (!TextUtils.isEmpty(mShareInfo?.task_id)) {
//                    mPresenter.share(mShareInfo ?.task_id)
////                    }
//                }
//            }
        }

        @Override
        public void onError(SHARE_MEDIA share_media, Throwable throwable) {
            if (loadingView != null) {
                loadingView.dismissLoading();
            }
            ToastUtil.toast2(mContext, "分享有误");
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media) {
            if (loadingView != null) {

                loadingView.dismissLoading();
            }
            ToastUtil.toast2(mContext, "取消发送");
        }
    };


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
