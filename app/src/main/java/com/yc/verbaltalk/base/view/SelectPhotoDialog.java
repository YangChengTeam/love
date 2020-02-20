package com.yc.verbaltalk.base.view;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.yc.verbaltalk.R;

import androidx.appcompat.app.AlertDialog;


/**
 * Created by Administrator on 2017/9/22.
 */

public abstract class SelectPhotoDialog {

//    private TextView tv_look;
    public AlertDialog alertDialog;


    public void instanceDialog(final Context context) {
        alertDialog = new AlertDialog.Builder(context, R.style.Dialog_FS).create();
        alertDialog.show();
        alertDialog.setCancelable(true);
        Window window = alertDialog.getWindow();
        window.setContentView(R.layout.dialog_select_photo);
        window.setWindowAnimations(R.style.DialogBottom); // 添加动画
        window.setGravity(Gravity.BOTTOM);
        TextView tv_album = window.findViewById(R.id.dialog_select_photo_tv_album);
        TextView tv_camera = window.findViewById(R.id.dialog_select_photo_tv_camera);
        TextView tv_cancel = window.findViewById(R.id.dialog_select_photo_tv_cancel);


        tv_album.setOnClickListener(clickAlbum);
        tv_camera.setOnClickListener(clickCamera);
        tv_cancel.setOnClickListener(clickCancel);
    }


    private View.OnClickListener clickAlbum = view -> clickAlbum();
    private View.OnClickListener clickCamera = view -> clickCamera();


    public View.OnClickListener clickCancel = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            clickCancelOther();
            alertDialog.dismiss();
        }
    };

    protected void clickCancelOther() {

    }


    protected abstract void clickAlbum();

    protected abstract void clickCamera();


    public void dialogDismiss() {
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
    }

}
