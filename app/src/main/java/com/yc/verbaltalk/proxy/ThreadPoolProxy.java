package com.yc.verbaltalk.proxy;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 描述	      线程池代理类,替线程池完成相关操作
 * 描述  1.代理模式就是多一个代理类出来，`替原对象进行一些操作`
 * 描述  2.只需提供使用原对象时候真正关心的方法(提交任务,执行任务,移除任务)
 * 描述  3.可以对原对象方法进行增强
 */
public class ThreadPoolProxy {
    ThreadPoolExecutor mExecutor;
    private int mCorePoolSize;//核心池的大小
    private int mMaximumPoolSize;//线程最大线程数

    public ThreadPoolProxy(int maximumPoolSize, int corePoolSize) {
        mMaximumPoolSize = maximumPoolSize;
        mCorePoolSize = corePoolSize;
    }

    public void initThreadPoolExecutor() {//双重检查加锁:只有第一次实例化的时候才启用同步机制,提高了性能
        if (mExecutor == null || mExecutor.isShutdown() || mExecutor.isTerminated()) {
            synchronized (ThreadPoolProxy.class) {
                if (mExecutor == null || mExecutor.isShutdown() || mExecutor.isTerminated()) {
                    long keepAliveTime = 0;
                    TimeUnit unit = TimeUnit.MILLISECONDS;
                    BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue();
                    ThreadFactory threadFactory = Executors.defaultThreadFactory();
                    RejectedExecutionHandler handler = new ThreadPoolExecutor.DiscardPolicy();
                    mExecutor = new ThreadPoolExecutor(mCorePoolSize, mMaximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
                }
            }
        }
    }

    /*
    提交任务和执行任务的区别?
    有无返回值的区别?
        submit-->有返回值
        execute-->没有返回值
    Future对象是干嘛的?
        1.可以得到异步任务执行完成之后的结果
        2.校验任务是否执行完成,等待完成,接收结果
        3.其中get方法可以接收结果,get方法还是一个阻塞方法,还可以得到任务执行过程中抛出的异常信息
     */

    /**
     * 提交任务
     */
    public Future submit(Runnable task) {
        initThreadPoolExecutor();
        Future<?> future = mExecutor.submit(task);
        return future;
    }

    /**
     * 执行任务
     */
    public void execute(Runnable task) {
        initThreadPoolExecutor();
        mExecutor.execute(task);
    }

    /**
     * 移除任务
     */
    public void remove(Runnable task) {
        initThreadPoolExecutor();
        mExecutor.remove(task);
    }
}

