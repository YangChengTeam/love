package com.yc.love.cont.gank;

import com.yc.love.cont.http.HttpClient;
import com.yc.love.cont.http.RequestImpl;
import com.yc.love.model.bean.FrontpageBean;
import com.yc.love.model.bean.JobBean;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by mayn on 2019/4/24.
 */

public class MainT1FragGank {
    public void showData(final RequestImpl listener) {
        Disposable subscribe = HttpClient.Builder.getTingServer().getFrontpage()
                .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                .subscribe(new Consumer<FrontpageBean>() {
                    @Override
                    public void accept(FrontpageBean frontpageBean) throws Exception {
                        if (listener != null) {
                            listener.loadSuccess(frontpageBean);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (listener != null) {
                            listener.loadFailed(throwable);
                        }
                    }
                });
        if (listener != null) {
            listener.addSubscription(subscribe);
        }
    }

    public void showJobData(final RequestImpl listener) {
        Disposable subscribe = HttpClient.Builder.getJobServer().collectUrl("android9", "358240051111110"
                , "android", "1556091888223"
                , "MzFmNWY0Nzk1OTY4NDg4YmE1MTBhOWNkNzg2ZDBlZmJfMTU1NjAwODk3OTAwNg", "2375e828ce94b788069356a305afae9f")
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<JobBean>() {
                    @Override
                    public void accept(JobBean jobBean) throws Exception {
                        if (listener != null) {
                            listener.loadSuccess(jobBean);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (listener != null) {
                            listener.loadFailed(throwable);
                        }
                    }
                });
        if (listener != null) {
            listener.addSubscription(subscribe);
        }
    }

}
