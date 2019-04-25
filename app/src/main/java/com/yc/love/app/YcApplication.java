package com.yc.love.app;

import android.support.multidex.MultiDexApplication;

import com.yc.love.http.HttpUtils;

/**
 * Created by mayn on 2019/4/24.
 */

public class YcApplication  extends MultiDexApplication {

    private static YcApplication ycApplication;

    public static YcApplication getInstance() {
        return ycApplication;
    }

    @SuppressWarnings("unused")
    @Override
    public void onCreate() {
        super.onCreate();
        ycApplication = this;
//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            return;
//        }
//        LeakCanary.install(this);
        HttpUtils.getInstance().init(this);
//        CrashReport.initCrashReport(getApplicationContext(), "3977b2d86f", DebugUtil.DEBUG);

    }

}
