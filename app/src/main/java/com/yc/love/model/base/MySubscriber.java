package com.yc.love.model.base;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;

import com.yc.love.R;
import com.yc.love.model.bean.AResultInfo;
import com.yc.love.ui.activity.IdCorrelationSlidingActivity;
import com.yc.love.ui.view.LoadDialog;

import rx.Subscriber;

/**
 * Created by mayn on 2019/5/8.
 */

public abstract class MySubscriber<T> extends Subscriber<T> {

    private LoadDialog loadDialog;
    private Context context;
    private boolean activityDealServiceCode1 = false;

    public MySubscriber(LoadDialog loadDialog) {
        this.loadDialog = loadDialog;
    }

    public MySubscriber(Context context) {
        this.context = context;
    }


    /**
     * @param loadDialog               loading图标
     * @param activityDealServiceCode1 当服务器code返回1时 true表示不希望弹出AlertDialog 默认是false弹窗
     */
    public MySubscriber(LoadDialog loadDialog, boolean activityDealServiceCode1) {
        this.loadDialog = loadDialog;
        this.activityDealServiceCode1 = activityDealServiceCode1;
    }


    @Override
    public void onNext(T t) {
        try {
            AResultInfo resultInfo = (AResultInfo) t;
            int code = resultInfo.code;
            String message = resultInfo.msg;
            if (1 != code && !activityDealServiceCode1) {
                if (loadDialog != null) {
                    context = loadDialog.getContext();
                }
                AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.MyAlertDialogStyle).create();
//                    alertDialog.setTitle("提示");
                alertDialog.setMessage(message);
                DialogInterface.OnClickListener listent = null;
                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", listent);
                alertDialog.show();
            }
            Log.d("mylog", "MySubscriber onNext: message " + message + " code " + code);
        } catch (ClassCastException e) {
            Log.d("mylog", "MySubscriber onNext: ClassCastException " + e);
        }
        if (loadDialog != null) {
            loadDialog.dismissLoadingDialog();
        }
        onNetNext(t);
    }

    protected abstract void onNetNext(T t);

    @Override
    public void onCompleted() {
        if (loadDialog != null) {
            loadDialog.dismissLoadingDialog();
        }
        onNetCompleted();
    }


    @Override
    public void onError(Throwable e) {
        Log.d("mylog", "MySubscriber onError: " + e);
        if (loadDialog != null) {
            loadDialog.dismissLoadingDialog();
        }
        onNetError(e);
    }

    protected abstract void onNetError(Throwable e);

    protected abstract void onNetCompleted();

}
