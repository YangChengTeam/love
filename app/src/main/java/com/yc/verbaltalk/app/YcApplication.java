package com.yc.verbaltalk.app;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.text.TextUtils;

import com.kk.securityhttp.domain.GoagalInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.kk.share.UMShareImpl;
import com.kk.utils.FileUtil;
import com.kk.utils.LogUtil;
import com.music.player.lib.manager.MusicPlayerManager;
import com.tencent.bugly.Bugly;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.UMShareAPI;
import com.yc.verbaltalk.R;
import com.yc.verbaltalk.model.ModelApp;
import com.yc.verbaltalk.ui.view.imgs.Constant;
import com.yc.verbaltalk.utils.ShareInfoHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import androidx.multidex.MultiDexApplication;
import rx.Observable;
import rx.schedulers.Schedulers;
import yc.com.toutiao_adv.TTAdDispatchManager;
import yc.com.toutiao_adv.TTAdManagerHolder;

/**
 * Created by mayn on 2019/4/24.
 */

public class YcApplication extends MultiDexApplication {

    private static YcApplication ycApplication;

    public static YcApplication getInstance() {
        return ycApplication;
    }

    public List<Activity> activityIdCorList;


    @SuppressWarnings("unused")
    @Override
    public void onCreate() {
        super.onCreate();
        ycApplication = this;

        Observable.just("").subscribeOn(Schedulers.io()).subscribe(s -> init());

        ModelApp.init(this);
        MusicPlayerManager.getInstance().init(this);
        MusicPlayerManager.getInstance().setDebug(true);
        TTAdManagerHolder.init(this, Constant.TOUTIAO_AD_ID);

    }


    private void init() {
        //        Bugly.init(getApplicationContext(), "注册时申请的APPID", false);  //腾迅自动更新
        Bugly.init(getApplicationContext(), "dc88d75f55", false);  //腾迅自动更新

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

        ShareInfoHelper.getNetShareInfo(this);
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
//        View view;
//        view.setVisibility(g);
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

}
