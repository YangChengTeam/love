package com.yc.verbaltalk.okhttp;

import java.io.File;
import java.util.Map;

/**
 * Created by mayn on 2019/6/5.
 */

public interface IOkHttpBiz {
    void connectNet( Map<String, String> requestMap, String requestUrl,IResultListener iResultListener);

    void connectUpFileNet(Map<String, String> requestMap, File upFile, String requestUrl, IResultListener iResultListener);

}
