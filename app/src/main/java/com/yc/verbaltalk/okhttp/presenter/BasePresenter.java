package com.yc.verbaltalk.okhttp.presenter;

import com.yc.verbaltalk.okhttp.IOkHttpBiz;
import com.yc.verbaltalk.okhttp.OkHttpRequest;
import com.yc.verbaltalk.okhttp.view.IResponseToView;

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
