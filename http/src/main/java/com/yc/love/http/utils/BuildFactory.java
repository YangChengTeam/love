package com.yc.love.http.utils;

import com.yc.love.http.HttpUtils;

import static com.yc.love.http.HttpUtils.API_TING;
/**
 * Created by mayn on 2019/4/24.
 */

public class BuildFactory {

    private static BuildFactory instance;
    private Object dongtingHttps;
    private Object gankHttps;
    public static BuildFactory getInstance() {
        if (instance == null) {
            synchronized (BuildFactory.class) {
                if (instance == null) {
                    instance = new BuildFactory();
                }
            }
        }
        return instance;
    }

    public <T> T create(Class<T> a, String type) {

        switch (type) {
            case API_TING:
                if (dongtingHttps == null) {
                    synchronized (BuildFactory.class) {
                        if (dongtingHttps == null) {
                            dongtingHttps = HttpUtils.getInstance().getBuilder(type).build().create(a);
                        }
                    }
                }
                return (T) dongtingHttps;
            default:
                if (gankHttps == null) {
                    synchronized (BuildFactory.class) {
                        if (gankHttps == null) {
                            gankHttps = HttpUtils.getInstance().getBuilder(type).build().create(a);
                        }
                    }
                }
                return (T) gankHttps;
        }
    }
}
