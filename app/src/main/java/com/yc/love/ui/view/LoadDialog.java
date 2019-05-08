package com.yc.love.ui.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.yc.love.R;


public class LoadDialog extends AlertDialog {


    private final Context context;

    public LoadDialog(Context context) {
        super(context, R.style.LoadingDialogTheme);//设置样式
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_view);
        setCancelable(false);
        initView();
    }

    private void initView() {

    }

    public void showLoadingDialog() {
        try {
            Activity activity = (Activity) context;
            if (!activity.isFinishing()) {
                dismiss();
                if (!isShowing()) {
                    show();
                }
            }
        } catch (Exception e) {
            Log.d("mylog", "LoadDialog --showLoadingDialog: " + e.toString());
        }
    }

    public void dismissLoadingDialog() {
        try {
            Activity activity = (Activity) context;
            if (!activity.isFinishing()) {
                dismiss();
                if (isShowing()) {
                    hide();
                }
            }
        } catch (Exception e) {
            Log.d("mylog", "LoadDialog --dismissDialog: " + e.toString());
        }
    }

}
