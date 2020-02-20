package com.yc.verbaltalk.base.fragment;

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
import android.widget.ImageView;
import android.widget.TextView;

import com.kk.utils.ScreenUtil;
import com.yc.verbaltalk.R;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

/**
 * Created by suns  on 2019/9/6 16:41.
 */
public class AddWxFragment extends DialogFragment {

    private Window window;
    private View rootView;
    private String mWx = "";
    private TextView tvWx;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        window = getDialog().getWindow();


        if (rootView == null) {
            getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
            rootView = inflater.inflate(R.layout.frament_add_wx, container, false);

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


    protected void initView() {
        tvWx = (TextView) getView(R.id.tv_wx);
        TextView tvCopyWx = (TextView) getView(R.id.tv_copy_wx);
        ImageView ivClose = (ImageView) getView(R.id.iv_close);
        if (!TextUtils.isEmpty(mWx))
            tvWx.setText(mWx);
        tvCopyWx.setOnClickListener(v -> {
            if (listener != null) {
                listener.onToWx();
            }
        });
        ivClose.setOnClickListener(v -> dismiss());


    }


    protected float getWidth() {
        return 0.7f;
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

    public void setWX(String wx) {
        this.mWx = wx;

    }

    private onToWxListener listener;

    public void setListener(onToWxListener listener) {
        this.listener = listener;
    }

    public interface onToWxListener {
        void onToWx();
    }


}
