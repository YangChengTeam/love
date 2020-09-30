package com.yc.verbaltalk.base.okhttp.view;

import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by sunshey on 2019/6/5.
 */

public interface IResponseToView {

//    void onSuccess(String jsonData);

    void onFailed(String errMsg);

    void onBefore(Request request, int id);
}
