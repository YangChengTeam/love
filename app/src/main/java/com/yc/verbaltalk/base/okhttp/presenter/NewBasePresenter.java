package com.yc.verbaltalk.base.okhttp.presenter;

import com.yc.verbaltalk.base.okhttp.IOkHttpBiz;
import com.yc.verbaltalk.base.okhttp.OkHttpRequest;

/**
 * Created by mayn on 2019/6/5.
 */

public class NewBasePresenter {
    public IOkHttpBiz mIOkHttpBiz;

    public NewBasePresenter() {
        mIOkHttpBiz = new OkHttpRequest();
    }
}
