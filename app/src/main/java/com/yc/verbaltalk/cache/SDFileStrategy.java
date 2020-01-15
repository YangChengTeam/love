package com.yc.verbaltalk.cache;

import android.os.Environment;

/**
 * Created by mayn on 2019/5/31.
 */

public class SDFileStrategy extends BaseFileStrategy  {
    /**
     * 构造函数
     */
    public SDFileStrategy() {
        super(Environment.getExternalStorageDirectory() +"/cacheData");
    }
}
