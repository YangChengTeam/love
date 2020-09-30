package com.yc.verbaltalk.base.okhttp.presenter;

import android.content.Context;

import com.yc.verbaltalk.base.okhttp.view.INormalUiView;
import com.yc.verbaltalk.base.okhttp.view.IUpFileUiView;

import java.io.File;
import java.util.Map;

import io.reactivex.observers.DisposableObserver;

/**
 * Created by sunshey on 2019/6/5.
 */

public class NormalPresenter extends BasePresenter {

    public NormalPresenter(Context context, INormalUiView iChangUiView) {
        super(context, iChangUiView);
    }

    public void netNormalData(Map<String, String> requestMap) {

        loveEngine.connectNet(requestMap).subscribe(new DisposableObserver<String>() {
            @Override
            public void onNext(String jsonData) {
                INormalUiView iNormalUiView = (INormalUiView) mIChangUiView;
                iNormalUiView.onSuccess(jsonData);
            }

            @Override
            public void onError(Throwable e) {
                mIChangUiView.onFailed(e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        });


    }


    public void netUpFileNet(Map<String, String> requestMap, File upFile) {

        loveEngine.connectUpFileNet(requestMap, upFile).subscribe(new DisposableObserver<String>() {
            @Override
            public void onNext(String jsonData) {
                IUpFileUiView iNormalUiView = (IUpFileUiView) mIChangUiView;
                iNormalUiView.onUpFileSuccess(jsonData);
            }

            @Override
            public void onError(Throwable e) {
                mIChangUiView.onFailed(e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        });
    }
}
