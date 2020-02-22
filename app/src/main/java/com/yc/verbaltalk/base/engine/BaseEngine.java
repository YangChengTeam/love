package com.yc.verbaltalk.base.engine;

import android.content.Context;
import android.util.Log;

import java.util.Map;

/**
 * Created by wanglin  on 2018/10/25 13:54.
 */
public class BaseEngine {

    protected Context mContext;

    public BaseEngine(Context context) {
        this.mContext = context;
    }

    public void requestParams(Map<String, String> params) {
        StringBuffer stringBuffer = new StringBuffer();
        for (String s : params.keySet()
                ) {
            stringBuffer.append(s).append(":").append(params.get(s)).append("   ");
        }
        Log.d("mylog", "requestParams: " + stringBuffer.toString());
    }

}
