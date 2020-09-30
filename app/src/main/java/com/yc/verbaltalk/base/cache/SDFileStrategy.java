package com.yc.verbaltalk.base.cache;

import android.os.Environment;

/**
 * Created by sunshey on 2019/5/31.
 */

public class SDFileStrategy extends BaseFileStrategy  {
    /**
     * 构造函数
     */
    public SDFileStrategy() {
        super(Environment.getExternalStorageDirectory() +"/cacheData");
    }
}
