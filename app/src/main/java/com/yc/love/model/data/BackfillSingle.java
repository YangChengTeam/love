package com.yc.love.model.data;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.yc.love.model.bean.IdCorrelationLoginBean;
import com.yc.love.model.single.YcSingle;
import com.yc.love.model.util.SPUtils;
import com.yc.love.ui.activity.IdCorrelationSlidingActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mayn on 2019/5/8.
 */

public class BackfillSingle {

    public static void backfillLoginData(Context context, String stringBean) {
        String idInfoString = (String) SPUtils.get(context, SPUtils.ID_INFO_BEAN, "");
        IdCorrelationLoginBean idCorrelationLogin = new Gson().fromJson(idInfoString, IdCorrelationLoginBean.class);
        YcSingle ycSingle = YcSingle.getInstance();
        if (!TextUtils.isEmpty(stringBean)) {
            try {
                JSONObject jsonObject = new JSONObject(stringBean);
                if (jsonObject.has("face")) {
                    String face = jsonObject.getString("face");
                    if (!TextUtils.isEmpty(face)) {
                        ycSingle.face = face;
                        idCorrelationLogin.setFace(face);
                    }
                }
                if (jsonObject.has("nick_name")) {
                    String nick_name = jsonObject.getString("nick_name");
                    if (!TextUtils.isEmpty(nick_name)) {
                        ycSingle.nick_name = nick_name;
                        idCorrelationLogin.setNick_name(nick_name);
                    }
                }
                if (jsonObject.has("name")) {
                    String name = jsonObject.getString("name");
                    if (!TextUtils.isEmpty(name)) {
                        ycSingle.name = name;
                        idCorrelationLogin.setName(name);
                    }
                }
                if (jsonObject.has("mobile")) {
                    String mobile = jsonObject.getString("mobile");
                    if (!TextUtils.isEmpty(mobile)) {
                        ycSingle.mobile = mobile;
                        idCorrelationLogin.setMobile(mobile);
                    }
                }
                if (jsonObject.has("vip_end_time")) {
                    int vip_end_time = jsonObject.getInt("vip_end_time");
                    if (vip_end_time > 0) {
                        ycSingle.vip_end_time = vip_end_time;
                        idCorrelationLogin.setVip_end_time(vip_end_time);
                    }
                }
                if (jsonObject.has("id")) {
                    int id = jsonObject.getInt("id");
                    if (id > 0) {
                        ycSingle.id = id;
                        idCorrelationLogin.setId(id);
                    }
                }
                if (jsonObject.has("vip")) {
                    int vip = jsonObject.getInt("vip");
                    if (vip > 0) {
                        ycSingle.vip = vip;
                        idCorrelationLogin.setVip(vip);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        SPUtils.put(context, SPUtils.ID_INFO_BEAN, JSON.toJSONString(idCorrelationLogin));
    }
}
