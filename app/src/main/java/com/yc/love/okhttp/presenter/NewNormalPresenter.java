package com.yc.love.okhttp.presenter;

import com.yc.love.okhttp.IResultListener;
import com.yc.love.okhttp.view.INormalUiView;
import com.yc.love.okhttp.view.IResponseToView;

import java.util.Map;

import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by mayn on 2019/6/17.
 */

public class NewNormalPresenter extends NewBasePresenter {
    public NewNormalPresenter() {
        super();
    }

    public void netNormalData(Map<String, String> requestMap, String requestUrl, final INormalUiView iNormalUiView) {
        mIOkHttpBiz.connectNet(requestMap, requestUrl, new IResultListener() {
            @Override
            public void onSuccess(String jsonData) {
//                INormalUiView iNormalUiView = (INormalUiView) mIChangUiView;
                iNormalUiView.onSuccess(jsonData);
            }

            @Override
            public void onFailed(Call call, Exception e, int id) {
                iNormalUiView.onFailed(call, e, id);
            }

            @Override
            public void onBefore(Request request, int id) {
                iNormalUiView.onBefore(request, id);
            }
        });
    }

}
