package com.yc.verbaltalk.base.cache;

import android.content.Context;

/**
 * Created by mayn on 2019/5/30.
 */

public interface CacheStrategy {

    /**
     * 存储缓存
     *
     * @param key  文件名
     * @param obj  文件内容
     */
    void setCache(String key, Object obj);

    /**
     * 获取缓存
     *
     * @return 文件内容
     */
    Object getCache(Context context, String fileName);


}
