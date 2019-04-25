package com.yc.love.cont.http;

import io.reactivex.disposables.Disposable;

/**
 * Created by mayn on 2019/4/24.
 * 用于数据请求的回调
 */

public interface RequestImpl {
    void loadSuccess(Object object);

    void loadFailed(Throwable throwable);

    void addSubscription(Disposable subscription);
}
