package com.yc.verbaltalk.base.utils;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;

import com.yc.verbaltalk.base.YcApplication;
import com.yc.verbaltalk.base.engine.UserInfoEngine;
import com.yc.verbaltalk.chat.bean.UserInfo;
import com.yc.verbaltalk.mine.ui.activity.LoginRegisterActivity;
import com.yc.verbaltalk.model.util.SPUtils;

import io.reactivex.observers.DisposableObserver;
import yc.com.rthttplibrary.bean.ResultInfo;
import yc.com.rthttplibrary.config.HttpConfig;
import yc.com.rthttplibrary.util.LogUtil;

public class UserInfoHelper {

    private static UserInfo userInfo;

    public static UserInfo getUserInfo() {
        if (userInfo != null) return userInfo;
        try {

            String json = (String) SPUtils.get(YcApplication.getInstance(), SPUtils.USER_INFO, "");
            userInfo = JSON.parseObject(json, UserInfo.class);
        } catch (Exception e) {
            LogUtil.msg("json解析失败");
        }

        return userInfo;
    }

    public static void saveUserInfo(UserInfo loginBean) {
        UserInfoHelper.userInfo = loginBean;
        try {
            String jsonStr = JSON.toJSONString(loginBean);
            SPUtils.put(YcApplication.getInstance(), SPUtils.USER_INFO, jsonStr);
        } catch (Exception e) {
            LogUtil.msg("json序列化失败");
        }

    }

    public static boolean isVip() {
        UserInfo infoBean = getUserInfo();
        if (infoBean != null) {
            int vipTips = infoBean.vip_tips;
            int isVip = infoBean.is_vip;
            int vip = infoBean.vip;
            return vipTips == 1 || isVip == 1;//|| vip == 1;
        }
        return false;
    }

    public static String getUid() {
        String user_id = "";
        if (getUserInfo() != null) {
            user_id = getUserInfo().id;
        }
        return user_id;
    }

    public static boolean isLogin(Context context) {
        boolean isLogin = false;
        if (!TextUtils.isEmpty(getUid())) {
            isLogin = true;
        }

        if (!isLogin) {
            Intent intent = new Intent(context, LoginRegisterActivity.class);
            context.startActivity(intent);
//            WxLoginFragment wxLoginFragment = new WxLoginFragment();
//            if (context instanceof AppCompatActivity) {
//                AppCompatActivity activity = (AppCompatActivity) context;
//                wxLoginFragment.show(activity.getSupportFragmentManager(), "");
//            }

        }

        return isLogin;
    }

    public static void logout() {
        UserInfo userInfo = new UserInfo();

        saveUserInfo(userInfo);

    }

    private static UserInfoEngine correlationEngine;

    public static void login(Context context) {
        if (correlationEngine == null) {
            correlationEngine = new UserInfoEngine(context);
        }

        UserInfo userInfo = getUserInfo();

        if (null != userInfo) {
            String mobile = userInfo.mobile;
            String pwd = userInfo.pwd;
            if (!TextUtils.isEmpty(mobile) && !TextUtils.isEmpty(pwd)) {
                correlationEngine.login(mobile, pwd).subscribe(new DisposableObserver<ResultInfo<UserInfo>>() {
                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ResultInfo<UserInfo> userInfoResultInfo) {
                        if (userInfoResultInfo != null) {
                            if (userInfoResultInfo.code == HttpConfig.STATUS_OK && userInfoResultInfo.data != null) {
                                UserInfo data = userInfoResultInfo.data;
                                data.pwd = pwd;
                                saveUserInfo(data);
                            }
                        }
                    }
                });
            }
        }
    }

}
