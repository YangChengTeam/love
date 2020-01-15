package com.yc.verbaltalk.ui.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yc.verbaltalk.R;

/**
 * Created by mayn on 2019/6/6.
 */

public class ShareShowImgDialog extends android.app.AlertDialog implements View.OnClickListener {

    //    private final Activity activity;
    private final String imgUrl;
    private final Context context;
    //    private AlertDialog alertDialog;
    private OnClickShareItemListent onClickShareItemListent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instanceDialog(context, imgUrl);
    }

    public ShareShowImgDialog(Context context, String imgUrl) {
        super(context);
//        this.activity = activity;
        this.context = context;
        this.imgUrl = imgUrl;
    }


    private void instanceDialog(Context context, String imgUrl) {

        setContentView(R.layout.dialog_share_show_img);
        Window window = getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }


       /* alertDialog = new AlertDialog.Builder(activity, R.style.Dialog_FS).create();
        alertDialog.show();
        alertDialog.setCancelable(true);
        Window window = alertDialog.getWindow();
        window.setContentView(R.layout.dialog_share_show_img);
        window.setWindowAnimations(R.style.DialogBottom); // 添加动画
//        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.x = 0;*/

        ImageView ivImg = window.findViewById(R.id.dialog_share_show_img_iv_img);
        Glide.with(context).load(imgUrl).into(ivImg);

        TextView ivShareQq = window.findViewById(R.id.dialog_share_show_img_tv_share_qq);
        TextView ivShareWx = window.findViewById(R.id.dialog_share_show_img_tv_share_wx);
        TextView ivShareSave = window.findViewById(R.id.dialog_share_show_img_tv_share_save);

        ivShareQq.setOnClickListener(this);
        ivShareWx.setOnClickListener(this);
        ivShareSave.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_share_show_img_tv_share_qq:
//                shareToQQ(activity, tencent, title, summary, url, imageUrl, iUiListener);
                onClickShareItemListent.oClickShareItem(0);
//                sharePhotoToQQ(activity, tencent, iUiListener);
                dismiss();
                break;
            case R.id.dialog_share_show_img_tv_share_wx:
                onClickShareItemListent.oClickShareItem(1);
                dismiss();
                break;
            case R.id.dialog_share_show_img_tv_share_save:
                onClickShareItemListent.oClickShareItem(2);
                dismiss();
                break;
        }
    }

    public interface OnClickShareItemListent {
        void oClickShareItem(int postion);
    }

    public void setOnClickShareItemListent(OnClickShareItemListent onClickShareItemListent) {
        this.onClickShareItemListent = onClickShareItemListent;
    }
}
