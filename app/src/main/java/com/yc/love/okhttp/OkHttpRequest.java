package com.yc.love.okhttp;

import android.util.Log;

import com.yc.love.model.domain.URLConfig;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by mayn on 2019/6/5.
 */

public class OkHttpRequest implements IOkHttpBiz {
    @Override
    public void connectNet(Map<String, String> requestMap, String requestUrl, final IResultListener iResultListener) {
        Log.d("securityhttp", "connectNet: requestUrl " + requestUrl);
        if (requestMap != null && requestMap.size() > 0) {
            for (String key : requestMap.keySet()
            ) {
                Log.d("securityhttp", "connectNet: request  key " + key + " value " + requestMap.get(key));
            }
        }
        OkHttpUtils.post().params(requestMap).url(requestUrl).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                //Logger.e("---data error---");
                iResultListener.onFailed(call, e, id);
                e.printStackTrace();
            }

            @Override
            public void onResponse(String response, int id) {
                Log.d("securityhttp", "onResponse: response " + response);
                //Logger.e("--- data success---" + response);
                if (iResultListener != null) {
                    iResultListener.onSuccess(response);
                }
            }

            @Override
            public void onBefore(Request request, int id) {
                iResultListener.onBefore(request, id);
            }
        });
    }

    @Override
    public void connectUpFileNet(Map<String, String> requestMap, File upFile, String requestUrl, final IResultListener iResultListener) {
        Log.d("securityhttp", "connectNet: requestUrl " + requestUrl);
        if (requestMap != null && requestMap.size() > 0) {
            for (String key : requestMap.keySet()
            ) {
                Log.d("securityhttp", "connectNet: request  key " + key + " value " + requestMap.get(key));
            }
        }
        Log.d("mylog", "aget: upFile " + upFile);
        String fileName = URLConfig.BASE_NORMAL_FILE_DIR + File.separator + System.currentTimeMillis() + (int) (Math.random() * 10000) + ".jpg";
        OkHttpUtils.post().addHeader("Cookie", "cookie_tnzbsq").params(requestMap).addFile("img", fileName, upFile).url(requestUrl).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                //Logger.e("---data error---");
                iResultListener.onFailed(call,e,id);
                e.printStackTrace();
            }

            @Override
            public void onResponse(String response, int id) {
                Log.d("mylog", "onResponse: response "+response);
                //Logger.e("--- data success---" + response);
                if (iResultListener != null) {
                    iResultListener.onSuccess(response);
                }
            }

            @Override
            public void onBefore(Request request, int id) {
                iResultListener.onBefore(request,id);
            }
        });
    }
}
