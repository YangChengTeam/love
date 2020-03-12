package com.yc.verbaltalk.base.utils;

import com.alibaba.fastjson.JSON;
import com.kk.utils.LogUtil;
import com.yc.verbaltalk.base.YcApplication;
import com.yc.verbaltalk.chat.bean.IdCorrelationLoginBean;
import com.yc.verbaltalk.model.util.SPUtils;

public class UserInfoHelper {

    private static IdCorrelationLoginBean loginBean;

    public static IdCorrelationLoginBean getUserInfo() {
        if (loginBean != null) return loginBean;
        try {

            String json = (String) SPUtils.get(YcApplication.getInstance(), SPUtils.USER_INFO, "");
            loginBean = JSON.parseObject(json, IdCorrelationLoginBean.class);
        } catch (Exception e) {
            LogUtil.msg("json解析失败");
        }

        return loginBean;
    }

    public static void saveUserInfo(IdCorrelationLoginBean loginBean) {
        UserInfoHelper.loginBean = loginBean;
        try {
            String jsonStr = JSON.toJSONString(loginBean);
            SPUtils.put(YcApplication.getInstance(), SPUtils.USER_INFO, jsonStr);
        } catch (Exception e) {
            LogUtil.msg("json序列化失败");
        }

    }

    public static boolean isVip() {
        IdCorrelationLoginBean infoBean = getUserInfo();
        if (infoBean != null) {
            int vipTips = infoBean.vip_tips;
            int isVip = infoBean.is_vip;
            int vip = infoBean.vip;
            return vipTips == 1 || isVip == 1 || vip == 1;
        }
        return false;
    }
}
