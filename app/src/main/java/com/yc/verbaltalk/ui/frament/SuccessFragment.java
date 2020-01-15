package com.yc.verbaltalk.ui.frament;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.kk.utils.ScreenUtil;
import com.yc.verbaltalk.R;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

/**
 * Created by suns  on 2019/9/4 17:01.
 */
public class SuccessFragment extends DialogFragment {

    private Window window;
    private View rootView;
    private String mTilte;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        window = getDialog().getWindow();


        if (rootView == null) {
            getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
            rootView = inflater.inflate(R.layout.frament_success, container, false);

//            window.setLayout((int) (RxDeviceTool.getScreenWidth(getActivity()) * getWidth()), getHeight());//这2行,和上面的一样,注意顺序就行;
            window.setWindowAnimations(getAnimationId());
        }
        getDialog().setCancelable(false);
        getDialog().setCanceledOnTouchOutside(false);

        initView();

        return rootView;

    }


    public View getView(int resId) {
        return rootView.findViewById(resId);
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));//注意此处

            WindowManager.LayoutParams layoutParams = window.getAttributes();

            layoutParams.width = (int) (ScreenUtil.getWidth(getActivity()) * getWidth());
            layoutParams.height = getHeight();
            window.setAttributes(layoutParams);
        }

    }

    public void setTint(String title) {
        this.mTilte = title;
    }

    protected void initView() {
        TextView tv = (TextView) getView(R.id.tv_success);

        if (!TextUtils.isEmpty(mTilte))
            tv.setText(mTilte);
    }

    protected float getWidth() {
        return 0.4f;
    }


    protected int getAnimationId() {
        return R.style.share_anim;
    }

    public int getHeight() {
        return ViewGroup.LayoutParams.WRAP_CONTENT;
    }

    protected int getGravity() {
        return Gravity.CENTER;
    }

}
