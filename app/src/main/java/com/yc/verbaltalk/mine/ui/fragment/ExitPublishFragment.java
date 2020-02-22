package com.yc.verbaltalk.mine.ui.fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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
 * Created by suns  on 2019/8/31 10:58.
 */
public class ExitPublishFragment extends DialogFragment {

    private Window window;
    private View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        window = getDialog().getWindow();


        if (rootView == null) {
            getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
            rootView = inflater.inflate(R.layout.frament_exit_publish, container, false);

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
        TextView tvCancel = (TextView) getView(R.id.tv_cancel);
        TextView tvConfirm = (TextView) getView(R.id.tv_confirm);

        tvCancel.setOnClickListener(v -> dismiss());

        tvConfirm.setOnClickListener(v -> {
            if (confirmListener != null) {
                confirmListener.onConfirm();
            }
            dismiss();
        });
    }

    protected float getWidth() {
        return 0.8f;
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

    private onConfirmListener confirmListener;

    public void setOnConfirmListener(onConfirmListener confirmListener) {
        this.confirmListener = confirmListener;
    }

    public interface onConfirmListener {
        void onConfirm();
    }

}
