package com.yc.verbaltalk.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.kk.share.UMShareImpl;
import com.music.player.lib.manager.MusicPlayerManager;
import com.tencent.bugly.Bugly;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.UMShareAPI;
import com.yc.verbaltalk.R;
import com.yc.verbaltalk.base.config.URLConfig;
import com.yc.verbaltalk.base.engine.LoveEngine;
import com.yc.verbaltalk.base.utils.AssetsUtils;
import com.yc.verbaltalk.base.utils.ShareInfoHelper;
import com.yc.verbaltalk.base.utils.UIUtils;
import com.yc.verbaltalk.base.utils.UserInfoHelper;
import com.yc.verbaltalk.base.view.imgs.Constant;
import com.yc.verbaltalk.model.ModelApp;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.multidex.MultiDexApplication;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import yc.com.rthttplibrary.bean.ResultInfo;
import yc.com.rthttplibrary.config.GoagalInfo;
import yc.com.rthttplibrary.config.HttpConfig;
import yc.com.rthttplibrary.converter.FastJsonConverterFactory;
import yc.com.rthttplibrary.request.RetrofitHttpRequest;
import yc.com.rthttplibrary.util.FileUtil;
import yc.com.rthttplibrary.util.LogUtil;
import yc.com.toutiao_adv.BrandType;
import yc.com.toutiao_adv.TTAdDispatchManager;
import yc.com.toutiao_adv.TTAdManagerHolder;

/**
 * Created by sunshey on 2019/4/24.
 */

public class YcApplication extends MultiDexApplication {

    private static YcApplication ycApplication;

    public static YcApplication getInstance() {
        return ycApplication;
    }

    public List<Activity> activityIdCorList;


    private Handler handler = new Handler();

    public static String privacyPolicy;

    @SuppressLint("CheckResult")
    @Override
    public void onCreate() {
        super.onCreate();
        ycApplication = this;

        new RetrofitHttpRequest.Builder(URLConfig.baseUrlV1)
                .convert(FastJsonConverterFactory.create());
        Observable.just("").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(s -> init());

        ModelApp.init(this);
        MusicPlayerManager.getInstance().init(this);
        MusicPlayerManager.getInstance().setDebug(true);
        TTAdManagerHolder.init(this, Constant.TOUTIAO_AD_ID);
        registerActivityLifecycleCallbacks(callbacks);

    }


    private void init() {
        //        Bugly.init(getApplicationContext(), "注册时申请的APPID", false);  //腾迅自动更新
        Bugly.init(getApplicationContext(), "dc88d75f55", false);  //腾迅自动更新

        TTAdDispatchManager.getManager().init(BrandType.HUAWEI, BrandType.HONOR);


        UMConfigure.init(getApplicationContext(), "5cec86d84ca35779b00010b8", "Umeng", UMConfigure.DEVICE_TYPE_PHONE, null);

        //初始化友盟SDK
        UMShareAPI.get(this);//初始化sdk

        //开启debug模式，方便定位错误，具体错误检查方式可以查看
        //http://dev.umeng.com/social/android/quick-integration的报错必看，正式发布，请关闭该模式
        UMConfigure.setLogEnabled(true);

        UMShareImpl.Builder builder = new UMShareImpl.Builder();

        builder.setWeixin("wx97a8dad615ab0283", "2c3424ec501402d1ecc663974117cdb5")
                .build(this);
//


        //全局信息初始化
        GoagalInfo.get().init(getApplicationContext());
        ApplicationInfo appinfo = getApplicationInfo();
        String sourceDir = appinfo.sourceDir;
        ZipFile zf = null;
        try {
            zf = new ZipFile(sourceDir);
            ZipEntry ze1 = zf.getEntry("META-INF/channelconfig.json");
            InputStream in1 = zf.getInputStream(ze1);
            String result1 = FileUtil.readString(in1);
            JSONObject jsonObject = new JSONObject(result1);
            setHttpDefaultParams(jsonObject);

            LogUtil.msg("渠道->" + result1);
        } catch (Exception e) {
            setHttpDefaultParams(null);
        }


        HttpConfig.setPublickey("-----BEGIN PUBLIC KEY-----\n" +
                "MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEA5KaI8l7xplShIEB0Pwgm\n" +
                "MRX/3uGG9BDLPN6wbMmkkO7H1mIOXWB/Jdcl4/IMEuUDvUQyv3P+erJwZ1rvNsto\n" +
                "hXdhp2G7IqOzH6d3bj3Z6vBvsXP1ee1SgqUNrjX2dn02hMJ2Swt4ry3n3wEWusaW\n" +
                "mev4CSteSKGHhBn5j2Z5B+CBOqPzKPp2Hh23jnIH8LSbXmW0q85a851BPwmgGEan\n" +
                "5HBPq04QUjo6SQsW/7dLaaAXfUTYETe0HnpLaimcHl741ftGyrQvpkmqF93WiZZX\n" +
                "wlcDHSprf8yW0L0KA5jIwq7qBeu/H/H5vm6yVD5zvUIsD7htX0tIcXeMVAmMXFLX\n" +
                "35duvYDpTYgO+DsMgk2Q666j6OcEDVWNBDqGHc+uPvYzVF6wb3w3qbsqTnD0qb/p\n" +
                "WxpEdgK2BMVz+IPwdP6hDsDRc67LVftYqHJLKAfQt5T6uRImDizGzhhfIfJwGQxI\n" +
                "7TeJq0xWIwB+KDUbFPfTcq0RkaJ2C5cKIx08c7lYhrsPXbW+J/W4M5ZErbwcdj12\n" +
                "hrfV8TPx/RgpJcq82otrNthI3f4QdG4POUhdgSx4TvoGMTk6CnrJwALqkGl8OTfP\n" +
                "KojOucENSxcA4ERtBw4It8/X39Mk0aqa8/YBDSDDjb+gCu/Em4yYvrattNebBC1z\n" +
                "ulK9uJIXxVPi5tNd7KlwLRMCAwEAAQ==\n" +
                "-----END PUBLIC KEY-----");


        activityIdCorList = new ArrayList<>();
        userActive();

        ShareInfoHelper.getNetShareInfo(this);
        UserInfoHelper.login(this);
        privacyPolicy = AssetsUtils.readAsset(this, "privacy_policy.txt");
//        netUserReg();
    }


    private void setHttpDefaultParams(JSONObject jsonObject) {
        //设置http默认参数
        String appName = getString(R.string.app_name);
        String agent_id = "1";
        if (TextUtils.equals("恋爱话术宝", appName) || TextUtils.equals("恋爱话术", appName)) {
            agent_id = "1";
        } else if (TextUtils.equals("西门恋爱话术库", appName)) {
            agent_id = "2";
        }

        Map<String, String> params = new HashMap<>();
        if (GoagalInfo.get().channelInfo != null && GoagalInfo.get().channelInfo.agent_id != null) {
            params.put("from_id", GoagalInfo.get().channelInfo.from_id + "");
            params.put("author", GoagalInfo.get().channelInfo.author + "");
            agent_id = GoagalInfo.get().channelInfo.agent_id;
        }
        params.put("agent_id", agent_id);
        params.put("ts", System.currentTimeMillis() + "");
        params.put("device_type", "2");
        params.put("app_id", "5");//8


        String uid = GoagalInfo.get().uuid;
        if (TextUtils.isEmpty(uid)) uid = getPesudoUniqueID();
        params.put("imeil", uid);
        try {
            if (jsonObject != null) {
                params.put("site_id", jsonObject.getString("site_id"));
                params.put("soft_id", jsonObject.getString("soft_id"));
                params.put("app_name", getString(R.string.app_name));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String sv = android.os.Build.MODEL.contains(android.os.Build.BRAND) ? android.os.Build.MODEL + " " + android
                .os.Build.VERSION.RELEASE : Build.BRAND + " " + android
                .os.Build.MODEL + " " + android.os.Build.VERSION.RELEASE;
        params.put("sys_version", sv);
        if (GoagalInfo.get().packageInfo != null) {
            params.put("app_version", GoagalInfo.get().packageInfo.versionCode + "");
        }
        HttpConfig.setDefaultParams(params);


    }

    /**
     * 用户激活数据统计
     */
    private void userActive() {
        LoveEngine loveEngine = new LoveEngine(this);
        loveEngine.userActive().subscribe(new DisposableObserver<ResultInfo<String>>() {
            @Override
            public void onComplete() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResultInfo<String> stringResultInfo) {

            }
        });
    }


    public String getPesudoUniqueID() {
        return "8f" + //we make this look like a valid IMEI
                Build.BOARD.length() % 10 +
                Build.BRAND.length() % 10 +
                Build.CPU_ABI.length() % 10 +
                Build.DEVICE.length() % 10 +
                Build.DISPLAY.length() % 10 +
                Build.HOST.length() % 10 +
                Build.ID.length() % 10 +
                Build.MANUFACTURER.length() % 10 +
                Build.MODEL.length() % 10 +
                Build.PRODUCT.length() % 10 +
                Build.TAGS.length() % 10 +
                Build.TYPE.length() % 10 +
                Build.USER.length() % 10;
    }

    private ActivityLifecycleCallbacks callbacks = new ActivityLifecycleCallbacks() {
        @Override
        public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
//            Log.e(TAG, "onActivityCreated: " + activity.getClass().getName());
        }

        @Override
        public void onActivityStarted(@NonNull Activity activity) {
//            Log.e(TAG, "onActivityStarted: " + activity.getClass().getName());
        }

        @Override
        public void onActivityResumed(@NonNull Activity activity) {
            Log.e("TAG", "onActivityResumed: " + activity.getClass().getName());
            String className = activity.getClass().getName();
            if (TextUtils.equals("com.bytedance.sdk.openadsdk.activity.base.TTRewardExpressVideoActivity", className)) {
                handler.postDelayed(() -> {
                    View view = activity.findViewById(UIUtils.getIdentifier(activity, "tt_video_ad_close_layout"));
                    View close = activity.findViewById(UIUtils.getIdentifier(activity, "tt_video_ad_close"));
                    view.setVisibility(View.VISIBLE);
                    close.setVisibility(View.VISIBLE);

//                    view.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
//                            builder.setMessage("关闭广告可能无法使用功能！！");
//                            builder.setPositiveButton("确定", (dialog, which) -> {
//                                try {
//                                    Class clazz = Class.forName("com.bytedance.sdk.openadsdk.activity.base.TTRewardExpressVideoActivity");
//
//
//                                    Method method = clazz.getSuperclass().getMethod("finish");
//                                    method.setAccessible(true);
//                                    method.invoke(clazz.getSuperclass());
//
//                                } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
//                                    e.printStackTrace();
//                                }
//                            })
//                                    .setNegativeButton("取消", null).create().show();
//                            ToastUtil.toast(ycApplication, "点击了");
//                        }
//                    });
//                    Log.e(TAG, "onActivityResumed: " + view.getClass().getName());
//                    Log.e(TAG, "onActivityResumed: " + close.getClass().getName());
                }, 1500 * 10);

            }
        }

        @Override
        public void onActivityPaused(@NonNull Activity activity) {
//            Log.e(TAG, "onActivityPaused: " + activity.getClass().getName());
        }

        @Override
        public void onActivityStopped(@NonNull Activity activity) {
//            Log.e(TAG, "onActivityStopped: " + activity.getClass().getName());
        }

        @Override
        public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
//            Log.e(TAG, "onActivitySaveInstanceState: " + activity.getClass().getName());
        }

        @Override
        public void onActivityDestroyed(@NonNull Activity activity) {
//            Log.e(TAG, "onActivityDestroyed: " + activity.getClass().getName());
        }
    };


}
