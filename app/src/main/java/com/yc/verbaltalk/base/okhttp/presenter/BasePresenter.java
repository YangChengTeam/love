package com.yc.verbaltalk.base.okhttp.presenter;

import com.yc.verbaltalk.base.okhttp.IOkHttpBiz;
import com.yc.verbaltalk.base.okhttp.OkHttpRequest;
import com.yc.verbaltalk.base.okhttp.view.IResponseToView;

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
