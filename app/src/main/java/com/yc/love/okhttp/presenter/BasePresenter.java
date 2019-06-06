package com.yc.love.okhttp.presenter;

import com.yc.love.okhttp.IOkHttpBiz;
import com.yc.love.okhttp.OkHttpRequest;
import com.yc.love.okhttp.view.INormalUiView;
import com.yc.love.okhttp.view.IResponseToView;

/**
 * Created by mayn on 2019/6/5.
 */

public class BasePresenter {
    public IResponseToView mIChangUiView;
    public IOkHttpBiz mIOkHttpBiz;

    public BasePresenter(IResponseToView iChangUiView) {
        mIChangUiView = iChangUiView;
        mIOkHttpBiz = new OkHttpRequest();
    }
}
