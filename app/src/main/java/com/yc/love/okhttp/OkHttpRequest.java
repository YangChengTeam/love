package com.yc.love.okhttp;

import android.util.Log;

import com.yc.love.model.domain.URLConfig;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.util.Map;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by mayn on 2019/6/5.
 */

public class OkHttpRequest implements IOkHttpBiz {
    @Override
    public void connectNet(Map<String, String> requestMap, String requestUrl, final IResultListener iResultListener) {
        Log.d("securityhttp", "connectNet: 111 requestUrl " + requestUrl);
        if (requestMap != null && requestMap.size() > 0) {
            for (String key : requestMap.keySet()) {
                Log.d("securityhttp", "connectNet: 111 request  key " + key + " value " + requestMap.get(key));
            }
        }
        OkHttpUtils.post().params(requestMap).url(requestUrl).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.d("mylog", "onError: 111 call-- " + call + " e-- " + e + " love_id-- " + id);
                Log.d("securityhttp", "onError: 111 call-- " + call + " e-- " + e + " love_id-- " + id);
                //Logger.e("---data error---");
                iResultListener.onFailed(call, e, id);
                e.printStackTrace();
            }

            @Override
            public void onResponse(String response, int id) {
                Log.d("securityhttp", "onResponse: 111 response " + response);
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
        Log.d("securityhttp", "connectNet: 222 requestUrl " + requestUrl);
        if (requestMap != null && requestMap.size() > 0) {
            for (String key : requestMap.keySet()
            ) {
                Log.d("securityhttp", "connectNet: 222  request  key " + key + " value " + requestMap.get(key));
            }
        }
        Log.d("securityhttp", "aget: upFile " + upFile);
        String fileName = URLConfig.BASE_NORMAL_FILE_DIR + File.separator + System.currentTimeMillis() + (int) (Math.random() * 10000) + ".jpg";
//        OkHttpUtils.post().addHeader("Cookie", "cookie_tnzbsq").params(requestMap).addFile("img", fileName, upFile).url(requestUrl).build().execute(new StringCallback() {
        OkHttpUtils.post().params(requestMap).addFile("img", fileName, upFile).url(requestUrl).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.d("mylog", "onError: 222  call-- " + call + " e-- " + e + " love_id-- " + id);
                Log.d("securityhttp", "onError: 222  call-- " + call + " e-- " + e + " love_id-- " + id);
                //Logger.e("---data error---");
                iResultListener.onFailed(call, e, id);
                e.printStackTrace();
            }

            @Override
            public void onResponse(String response, int id) {
                Log.d("securityhttp", "onResponse: 222  response " + response);
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
}
