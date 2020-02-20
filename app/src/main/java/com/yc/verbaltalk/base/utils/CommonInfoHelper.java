package com.yc.verbaltalk.base.utils;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.music.player.lib.util.Logger;

import java.lang.reflect.Type;

/**
 * Created by wanglin  on 2018/2/9 11:34.
 */

public class CommonInfoHelper {


    public static <T> void getO(Context context, String key, final Type type, final onParseListener<T> listener) {

        try {
            CacheUtils.readCache(context, key, new CacheUtils.SubmitRunable() {
                @Override
                public void run() {
                    final String json = this.getJson();
                    UIUtils.post(() -> {
                        T t = parseData(json, type);
                        if (listener != null) {
                            listener.onParse(t);
                        }
                    });

                }
            });

        } catch (Exception e) {
            Logger.e(CommonInfoHelper.class.getClass().getName(), "error:->>" + e.getMessage());
        }

    }

    public static <T> void setO(Context context, T t, String key) {
        try {
            CacheUtils.writeCache(context, key, JSON.toJSONString(t));
        } catch (Exception e) {
            Logger.e(CommonInfoHelper.class.getClass().getName(), "error:->>" + e.getMessage());
        }

    }


    private static <T> T parseData(String result, Type type) {

        if (type.toString().equals("java.lang.String")) {
            return (T) result;
        }
        T resultInfo;
        if (type != null) {
            resultInfo = JSON.parseObject(result, type);
        } else {
            resultInfo = JSON.parseObject(result, new TypeReference<T>() {
            }.getType());
        }

        return resultInfo;
    }


    public interface onParseListener<T> {
        void onParse(T o);

    }
}
