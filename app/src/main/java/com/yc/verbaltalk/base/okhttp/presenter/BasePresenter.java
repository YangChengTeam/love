package com.yc.verbaltalk.base.okhttp.presenter;

import android.content.Context;

import com.yc.verbaltalk.base.engine.LoveEngine;
import com.yc.verbaltalk.base.okhttp.view.IResponseToView;

/**
 * Created by sunshey on 2019/6/5.
 */

public class BasePresenter {
    public IResponseToView mIChangUiView;
    protected LoveEngine loveEngine;

    public BasePresenter(Context context, IResponseToView iChangUiView) {
        mIChangUiView = iChangUiView;

        loveEngine = new LoveEngine(context);
    }
}
