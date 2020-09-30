package com.yc.verbaltalk.base.fragment;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.umeng.analytics.MobclickAgent;
import com.yc.verbaltalk.R;
import com.yc.verbaltalk.base.activity.BaseActivity;

/**
 * Created by suns  on 2020/6/22 09:47.
 */
public abstract class BaseBottomSheetDialogFragment extends BottomSheetDialogFragment {

    private BottomSheetDialog dialog;
    protected View rootView;
    private BottomSheetBehavior<View> mBehavior;
    protected BaseActivity mContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = (BaseActivity) context;
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
        initViews();
        return dialog;
    }

    protected abstract int getLayoutId();

    public abstract void initViews();


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
        Runtime.getRuntime().gc();
    }
}
