package com.yc.verbaltalk.base.fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.umeng.analytics.MobclickAgent;
import com.yc.verbaltalk.base.activity.BaseActivity;
import com.yc.verbaltalk.base.view.LoadDialog;
import com.yc.verbaltalk.model.util.ScreenUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;



/**
 * Created by wanglin  on 2018/3/6 11:14.
 */

public abstract class BaseDialogFragment extends DialogFragment {


    protected View rootView;
    private LoadDialog loadingView;
    protected BaseActivity mContext;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Window window = getDialog().getWindow();

        if (rootView == null) {
            getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
            rootView = inflater.inflate(getLayoutId(), container, false);
            loadingView = new LoadDialog(getActivity());
//            window.setLayout((int) (RxDeviceTool.getScreenWidth(getActivity()) * getWidth()), getHeight());//这2行,和上面的一样,注意顺序就行;
            window.setWindowAnimations(getAnimationId());
        }
        getDialog().setCancelable(false);
        getDialog().setCanceledOnTouchOutside(false);

        init();


        return rootView;

    }

    protected abstract int getLayoutId();

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mContext = (BaseActivity) context;
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));//注意此处

            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.width = (int) (ScreenUtils.getScreenWidth(getActivity()) * getWidth());
            layoutParams.height = getHeight();
            window.setAttributes(layoutParams);
            window.setGravity(getGravity());
        }

    }

    protected abstract float getWidth();

    public abstract int getAnimationId();

    public abstract int getHeight();

    public abstract void init();

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

        Runtime.getRuntime().gc();
    }

    public int getGravity() {
        return Gravity.CENTER;
    }
}
