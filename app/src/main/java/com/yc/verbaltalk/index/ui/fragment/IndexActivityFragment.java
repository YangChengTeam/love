package com.yc.verbaltalk.index.ui.fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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
import com.yc.verbaltalk.base.view.RoundCornerImg;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

/**
 * Created by suns  on 2019/9/26 17:48.
 */
public class IndexActivityFragment extends DialogFragment {

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
            rootView = inflater.inflate(R.layout.fragment_index_activity, container, false);

//            window.setLayout((int) (RxDeviceTool.getScreenWidth(getActivity()) * getWidth()), getHeight());//这2行,和上面的一样,注意顺序就行;
            window.setWindowAnimations(getAnimationId());
        }
        getDialog().setCancelable(true);
        getDialog().setCanceledOnTouchOutside(true);

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

        RoundCornerImg roundCornerImg = (RoundCornerImg) getView(R.id.roundCornerImg);

        roundCornerImg.setCorner(20);

        rootView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onToWx();
            }
        });

        ImageView ivClose = (ImageView) getView(R.id.iv_close);
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
