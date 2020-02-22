package com.yc.verbaltalk.base.cache;

import android.content.Context;

/**
 * Created by mayn on 2019/5/31.
 */

public class CacheWorker {
    /**
     * 缓存策略
     */
    private CacheStrategy mCacheStrategy;

    public CacheWorker(CacheStrategy cacheStrategy) {
        mCacheStrategy = cacheStrategy;
    }

    public CacheWorker() {
        setStrategy();
    }

    private void setStrategy() {
        mCacheStrategy = new SDFileStrategy();
    }

    /**
     * 存储缓存
     *
     * @param key 文件名
     * @param obj 文件内容
     */
    public void setCache(String key, Object obj) {
        mCacheStrategy.setCache(key, obj);
    }

    /**
     * 获取缓存
     *
     * @param context
     * @param fileName
     * @return
     */
    public Object getCache(Context context, String fileName) {
        return mCacheStrategy.getCache(context, fileName);
    }


}
