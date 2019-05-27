package com.yc.love.utils;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yc.love.factory.ThreadPoolProxyFactory;
import com.yc.love.model.util.SPUtils;
import com.yc.love.proxy.ThreadPoolProxy;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mayn on 2019/5/26.
 */

public class CacheUtils<T> {

    public static void cacheBeanData(final Context context, final String key, final Object data) {
        if (TextUtils.isEmpty(key) || data == null) {
            return;
        }
        ThreadPoolProxy normalThreadPoolProxy = ThreadPoolProxyFactory.getNormalThreadPoolProxy();
        normalThreadPoolProxy.execute(new Runnable() {
            @Override
            public void run() {
                String str = JSON.toJSONString(data);
                if (!TextUtils.isEmpty(str)) {
                    SPUtils.put(context, key, str);
                }
            }
        });
    }

}
