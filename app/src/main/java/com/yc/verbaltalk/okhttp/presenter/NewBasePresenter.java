package com.yc.verbaltalk.okhttp.presenter;

import com.yc.verbaltalk.okhttp.IOkHttpBiz;
import com.yc.verbaltalk.okhttp.OkHttpRequest;
import com.yc.verbaltalk.okhttp.view.IResponseToView;

/**
 * Created by mayn on 2019/6/5.
 */

public class NewBasePresenter {
    public IOkHttpBiz mIOkHttpBiz;

    public NewBasePresenter() {
        mIOkHttpBiz = new OkHttpRequest();
    }
}
