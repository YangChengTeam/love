package com.yc.verbaltalk.index.ui.fragment;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.music.player.lib.util.ToastUtils;
import com.yc.verbaltalk.R;
import com.yc.verbaltalk.base.activity.BaseActivity;
import com.yc.verbaltalk.base.fragment.BaseDialogFragment;

/**
 * Created by suns  on 2020/4/15 20:16.
 */
public class IndexNoticeDialogFragment extends BaseDialogFragment {

    TextView tvNoticeContent;

    TextView tvKnow;


    public int getGravity() {
        return Gravity.CENTER;
    }


    protected float getWidth() {
        return 0.7f;
    }


    public int getAnimationId() {
        return R.style.share_anim;
    }


    public int getHeight() {
        return ViewGroup.LayoutParams.WRAP_CONTENT;
    }


    public int getLayoutId() {
        return R.layout.fragment_index_notice;
    }


    public void init() {
        tvNoticeContent = rootView.findViewById(R.id.tv_notice_content);
        tvKnow = rootView.findViewById(R.id.tv_know);
        tvNoticeContent.setText(Html.fromHtml("系统升级，更新用户登录注册后退出重启即可正常使用！如遇问题，请加客服微信<font color='#fe6225'>pai201807</font>"));
        tvKnow.setOnClickListener(v -> {
            if (getActivity() != null) {
                ClipboardManager myClipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData myClip = ClipData.newPlainText("text", "pai201807");
                myClipboard.setPrimaryClip(myClip);
                ToastUtils.showCenterToast("微信复制成功");
                if (getActivity() instanceof BaseActivity) {
                    ((BaseActivity) getActivity()).openWeiXin();
                }
                dismiss();
            }
        });
    }
}
