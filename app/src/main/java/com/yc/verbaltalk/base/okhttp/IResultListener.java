package com.yc.verbaltalk.base.okhttp;

import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by mayn on 2019/6/5.
 */

public interface IResultListener {

    void onSuccess(String jsonData);

    void onFailed(Call call, Exception e,int id);

    void onBefore(Request request, int id);

}
