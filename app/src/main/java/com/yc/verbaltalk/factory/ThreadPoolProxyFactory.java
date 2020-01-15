package com.yc.verbaltalk.factory;


import com.yc.verbaltalk.proxy.ThreadPoolProxy;

/**
 * 描述	      ThreadPoolProxy工厂类,封装对ThreadPoolProxy创建
 */

public class ThreadPoolProxyFactory {
    //普通类型的线程池代理
    static ThreadPoolProxy mNormalThreadPoolProxy;
    //下载类型的线程池代理
    static ThreadPoolProxy mDownLoadThreadPoolProxy;

    /**
     * 得到普通类型的线程池代理
     */
    public static ThreadPoolProxy getNormalThreadPoolProxy() {
        if (mNormalThreadPoolProxy == null) {
            synchronized (ThreadPoolProxyFactory.class) {
                if (mNormalThreadPoolProxy == null) {
                    mNormalThreadPoolProxy = new ThreadPoolProxy(5, 5);
                }
            }
        }
        return mNormalThreadPoolProxy;
    }

}
