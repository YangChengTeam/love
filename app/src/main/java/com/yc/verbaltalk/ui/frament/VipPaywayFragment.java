package com.yc.verbaltalk.ui.frament;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.umeng.analytics.MobclickAgent;
import com.yc.verbaltalk.R;
import com.yc.verbaltalk.ui.activity.base.BaseActivity;

/**
 * Created by suns  on 2019/11/18 18:04.
 */
public class VipPaywayFragment extends BottomSheetDialogFragment {

    protected BaseActivity mContext;
    private BottomSheetDialog dialog;
    private View rootView;
    private BottomSheetBehavior<View> mBehavior;

    private ImageView icClose;
    private ImageView ivAli;
    private ImageView ivWx;

    private RelativeLayout rlAliPay;
    private RelativeLayout rlWxiPay;

    private TextView tvPayBtn;
    private String payWayName = "alipay";

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
            rootView = LayoutInflater.from(mContext).inflate(R.layout.layout_vip_pay_way, null);

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
        initView();
        return dialog;
    }

    private void initView() {
        this.icClose = rootView.findViewById(R.id.iv_payway_close);

        this.rlAliPay = rootView.findViewById(R.id.rl_ali_pay);

        this.rlWxiPay = rootView.findViewById(R.id.rl_wx_pay);

        this.ivAli = rootView.findViewById(R.id.iv_ali_select);

        this.ivWx = rootView.findViewById(R.id.iv_wx_select);

        this.tvPayBtn = rootView.findViewById(R.id.tv_pay_btn);

        initListener();
    }

    private void initListener() {

        icClose.setOnClickListener(v -> dismiss());

        rlAliPay.setOnClickListener(v -> {
            payWayName = "alipay";
            ivAli.setImageResource(R.mipmap.icon_circle_sel);
            ivWx.setImageResource(R.mipmap.icon_circle_default);
        });

        rlWxiPay.setOnClickListener(v -> {
            payWayName = "wxpay";
            ivWx.setImageResource(R.mipmap.icon_circle_sel);
            ivAli.setImageResource(R.mipmap.icon_circle_default);
        });
        tvPayBtn.setOnClickListener(v -> {
            if (onPayWaySelectListener != null) {

                onPayWaySelectListener.onPayWaySelect(payWayName);
            }
            dismiss();
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
        Runtime.getRuntime().gc();
    }

    private OnPayWaySelectListener onPayWaySelectListener = null;

    public void setOnPayWaySelectListener(OnPayWaySelectListener listener) {
        this.onPayWaySelectListener = listener;
    }


    public interface OnPayWaySelectListener {
        void onPayWaySelect(String payway);
    }
}
