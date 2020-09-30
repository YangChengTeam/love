package com.yc.verbaltalk.pay.ui.fragment;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.music.player.lib.util.ToastUtils;
import com.yc.verbaltalk.R;
import com.yc.verbaltalk.base.activity.BaseActivity;
import com.yc.verbaltalk.base.fragment.BaseDialogFragment;

/**
 * Created by suns  on 2020/7/1 08:41.
 */
public class PaySuccessFragment extends BaseDialogFragment {

    private String mWx = "pai201807";

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_pay_success;
    }

    @Override
    protected float getWidth() {
        return 1.0f;
    }

    @Override
    public int getAnimationId() {
        return R.style.share_anim;
    }

    @Override
    public int getHeight() {
        return ViewGroup.LayoutParams.WRAP_CONTENT;
    }

    @Override
    public void init() {
        initViews();
    }


    public final void initViews() {
        if (getActivity() instanceof BaseActivity) {


            ((BaseActivity) getActivity()).showToWxServiceDialog("audio", (BaseActivity.onExtraListener) (wx -> PaySuccessFragment.this.mWx = wx));
        }


        ImageView var2 = (ImageView) rootView.findViewById(R.id.iv_cancel);
        if (var2 != null) {
            var2.setOnClickListener((View.OnClickListener) (it -> dismiss()));
        }


        var2 = (ImageView) rootView.findViewById(R.id.iv_copy_wx);
        if (var2 != null) {
            var2.setOnClickListener((View.OnClickListener) ((View.OnClickListener) it -> {

                if (getActivity() != null) {
                    ClipboardManager myClipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData myClip = ClipData.newPlainText("text", this.mWx);
                    myClipboard.setPrimaryClip(myClip);
                    ToastUtils.showCenterToast("微信复制成功");
                    if (getActivity() instanceof BaseActivity) {
                        ((BaseActivity) getActivity()).openWeiXin();
                    }

                    dismiss();
                }
            }));
        }


    }
}
