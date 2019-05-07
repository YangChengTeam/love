package com.yc.love.cont.gank;

import android.util.Log;

import com.yc.love.cont.http.HttpClient;
import com.yc.love.cont.http.RequestImpl;
import com.yc.love.model.bean.FrontpageBean;
import com.yc.love.model.bean.IdCorrelationSmsBean;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by mayn on 2019/5/7.
 */

public class IdCorrelationGank {
    public void showSmsData(final RequestImpl listener) {
        Log.d("mylog", "getLoveServer showSmsData: ");
        Disposable subscribe = HttpClient.Builder.getLoveServer().idCorrelationSms()
                .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                .subscribe(new Consumer<IdCorrelationSmsBean>() {
                    @Override
                    public void accept(IdCorrelationSmsBean idCorrelationSmsBean) throws Exception {
                        Log.d("mylog", "getLoveServer accept: ");
                        if (listener != null) {
                            listener.loadSuccess(idCorrelationSmsBean);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d("mylog", "getLoveServer loadFailed: ");
                        if (listener != null) {
                            listener.loadFailed(throwable);
                        }
                    }
                });
        if (listener != null) {
            Log.d("mylog", "getLoveServer addSubscription: ");
            listener.addSubscription(subscribe);
        }
    }
}
