package com.yc.love.okhttp.presenter;

import com.yc.love.okhttp.IResultListener;
import com.yc.love.okhttp.view.IMoreUiView;
import com.yc.love.okhttp.view.INormalUiView;
import com.yc.love.okhttp.view.IUpFileUiView;

import java.io.File;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by mayn on 2019/6/5.
 */

public class NormalPresenter extends BasePresenter {

    public NormalPresenter(INormalUiView iChangUiView) {
        super(iChangUiView);
    }

    public void netNormalData(Map<String, String> requestMap, String requestUrl) {
        mIOkHttpBiz.connectNet(requestMap, requestUrl, new IResultListener() {
            @Override
            public void onSuccess(String jsonData) {
                INormalUiView iNormalUiView = (INormalUiView) mIChangUiView;
                iNormalUiView.onSuccess(jsonData);
            }

            @Override
            public void onFailed(Call call, Exception e, int id) {
                mIChangUiView.onFailed(call, e, id);
            }

            @Override
            public void onBefore(Request request, int id) {
                mIChangUiView.onBefore(request, id);
            }
        });
    }

    public void netMoreData(Map<String, String> requestMap, String requestUrl) {
        mIOkHttpBiz.connectNet(requestMap, requestUrl, new IResultListener() {
            @Override
            public void onSuccess(String jsonData) {
                IMoreUiView iMoreUiView = (IMoreUiView) mIChangUiView;
                iMoreUiView.onMoreSuccess(jsonData);
            }

            @Override
            public void onFailed(Call call, Exception e, int id) {
                mIChangUiView.onFailed(call, e, id);
            }

            @Override
            public void onBefore(Request request, int id) {
                mIChangUiView.onBefore(request, id);
            }
        });
    }

    public void netUpFileNet(Map<String, String> requestMap, File upFile, String requestUrl){
        mIOkHttpBiz.connectUpFileNet(requestMap, upFile, requestUrl, new IResultListener() {
            @Override
            public void onSuccess(String jsonData) {
                IUpFileUiView iNormalUiView = (IUpFileUiView) mIChangUiView;
                iNormalUiView.onUpFileSuccess(jsonData);
            }

            @Override
            public void onFailed(Call call, Exception e, int id) {
                mIChangUiView.onFailed(call, e, id);
            }

            @Override
            public void onBefore(Request request, int id) {
                mIChangUiView.onBefore(request, id);
            }
        });
    }
}
