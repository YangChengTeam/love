package com.yc.love.model.base;

import android.util.Log;

import com.yc.love.ui.view.LoadDialog;

import rx.Subscriber;

/**
 * Created by mayn on 2019/5/8.
 */

public abstract class BaseMySubscriber<T> extends Subscriber<T> {

    private final LoadDialog loadDialog;

    public BaseMySubscriber(LoadDialog loadDialog) {
        this.loadDialog = loadDialog;
    }


    @Override
    public void onCompleted() {
        loadDialog.dismissLoadingDialog();
        onNetCompleted();
    }


    @Override
    public void onError(Throwable e) {
        Log.d("mylog", "BaseMySubscriber onError: " + e);
        loadDialog.dismissLoadingDialog();
        onNetError(e);
    }

    protected abstract void onNetError(Throwable e);

    protected abstract void onNetCompleted();

}
