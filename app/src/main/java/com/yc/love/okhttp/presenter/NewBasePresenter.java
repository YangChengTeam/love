package com.yc.love.okhttp.presenter;

import com.yc.love.okhttp.IOkHttpBiz;
import com.yc.love.okhttp.OkHttpRequest;
import com.yc.love.okhttp.view.IResponseToView;

/**
 * Created by mayn on 2019/6/5.
 */

public class NewBasePresenter {
    public IOkHttpBiz mIOkHttpBiz;

    public NewBasePresenter() {
        mIOkHttpBiz = new OkHttpRequest();
    }
}
