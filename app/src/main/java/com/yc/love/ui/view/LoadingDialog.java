package com.yc.love.ui.view;

import android.content.Context;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yc.love.R;

/**
 * Created by mayn on 2019/4/24.
 */

public class LoadingDialog {
    private Context context;
    private ProgressBar progressBar;
    private TextView tv;
    private ImageView iv;
    //    private AlertDialog alertDialog;
    private AlertDialog alertDialog;

    public LoadingDialog(Context context) {
        this.context = context;
        instanceDialog();
    }

    private void instanceDialog() {
        alertDialog = new AlertDialog.Builder(context, R.style.LoadingDialogTheme).create();
        alertDialog.show();
        alertDialog.setCancelable(false);
        Window window = alertDialog.getWindow();
        window.setContentView(R.layout.loading_view);

        progressBar = window.findViewById(R.id.loading_view_progressbar);
        tv = window.findViewById(R.id.loading_view_tv);
        iv = window.findViewById(R.id.loading_view_iv);
    }

    /**
     * loading
     */
    public void showLoading() {
        iv.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    public void dismissLoading() {
        alertDialog.dismiss();
    }

    /**
     * 成功
     */
    /*public void showSuccess() {
        iv.setImageResource(R.mipmap.load_success);
        iv.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    *//**
     * 失败
     *//*
    public void showFail() {
        iv.setImageResource(R.mipmap.load_fail);
        iv.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }*/

    /**
     * 提示文字
     *
     * @param txt string
     */
    public void setText(String txt) {
        tv.setText(txt);
    }

    /**
     * 提示文字
     */
    public void setText(@StringRes int txtId) {
        tv.setText(txtId);
    }
}
