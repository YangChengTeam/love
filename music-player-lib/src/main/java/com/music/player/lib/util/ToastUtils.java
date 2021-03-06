package com.music.player.lib.util;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.music.player.lib.R;
import com.music.player.lib.manager.MusicPlayerManager;

/**
 * TinyHung@outlook.com
 * 2017/3/17 16:56
 */
public class ToastUtils {

    private static TextView sMTv_text;
    private static Toast centerToast;
    private static ImageView ivOk;

    public static void showCenterToast(String text, boolean showImg) {
        if (null == centerToast) {
            centerToast = new Toast(MusicPlayerManager.getInstance().getContext());
            centerToast.setDuration(Toast.LENGTH_LONG);
            centerToast.setGravity(Gravity.NO_GRAVITY, 0, 0);
            View view = View.inflate(MusicPlayerManager.getInstance().getContext(), R.layout.toast_center_layout, null);
            sMTv_text = view.findViewById(R.id.tv_text);
            ivOk = view.findViewById(R.id.iv_ok);


            ivOk.setVisibility(showImg ? View.VISIBLE : View.GONE);
            sMTv_text.setText(TextUtils.isEmpty(text) ? "null" : text);
            centerToast.setView(view);
        } else {
            sMTv_text.setText(TextUtils.isEmpty(text) ? "null" : text);
            ivOk.setVisibility(showImg ? View.VISIBLE : View.GONE);
        }
        centerToast.show();
    }

    public static void showCenterToast(String text) {
        showCenterToast(text, false);
    }
}
