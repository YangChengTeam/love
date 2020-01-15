package com.yc.verbaltalk.model.base;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

import com.yc.verbaltalk.R;
import com.yc.verbaltalk.model.bean.AResultInfo;
import com.yc.verbaltalk.ui.view.LoadDialog;

import androidx.appcompat.app.AlertDialog;
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
        if (t == null) {
            onNetError(null);
            return;
        } else {
            AResultInfo resultInfo = null;
            try {
                resultInfo = (AResultInfo) t;
            } catch (ClassCastException e) {
                Log.d("mylog", "MySubscriber onNext: ClassCastException " + e);
            }
            if (resultInfo == null) {
                onNetError(null);
                return;
            }
            int code = resultInfo.code;
            String message = resultInfo.msg;
            Log.d("mylog", "MySubscriber onNext: code " + code + " message " + message + " activityDealServiceCode1 " + activityDealServiceCode1);
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
                return;
            }
            onNetNext(t);
        }
        if (loadDialog != null) {
            loadDialog.dismissLoadingDialog();
        }
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
