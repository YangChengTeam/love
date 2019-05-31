package com.yc.love.model.data;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.yc.love.model.bean.IdCorrelationLoginBean;
import com.yc.love.model.single.YcSingle;
import com.yc.love.model.util.SPUtils;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by mayn on 2019/5/8.
 */

public class BackfillSingle {

    /**
     * 更新单例数据
     * @param context
     * @param stringBean  服务器返回的新数据，传""表示没有新数据取本地SP的数据恢复
     */
    public static void backfillLoginData(Context context, String stringBean) {
        YcSingle ycSingle = YcSingle.getInstance();
        //本地数据
        String idInfoString = (String) SPUtils.get(context, SPUtils.ID_INFO_BEAN, "");
        IdCorrelationLoginBean idCorrelationLogin = new IdCorrelationLoginBean();

        if (!TextUtils.isEmpty(idInfoString) && !"null".equals(idInfoString)) {
            try {
                JSONObject jsonObject = new JSONObject(idInfoString);
                if (jsonObject.has("face")) {
                    String face = jsonObject.getString("face");
                    if (!TextUtils.isEmpty(face)) {
                        ycSingle.face = face;
                        idCorrelationLogin.face = (face);
                    }
                }
                if (jsonObject.has("nick_name")) {
                    String nick_name = jsonObject.getString("nick_name");
                    if (!TextUtils.isEmpty(nick_name)) {
                        ycSingle.nick_name = nick_name;
                        idCorrelationLogin.nick_name = (nick_name);
                    }
                }
                if (jsonObject.has("name")) {
                    String name = jsonObject.getString("name");
                    if (!TextUtils.isEmpty(name)) {
                        ycSingle.name = name;
                        idCorrelationLogin.name = (name);
                    }
                }
                if (jsonObject.has("mobile")) {
                    String mobile = jsonObject.getString("mobile");
                    if (!TextUtils.isEmpty(mobile)) {
                        ycSingle.mobile = mobile;
                        idCorrelationLogin.mobile = (mobile);
                    }
                }
                if (jsonObject.has("vip_end_time")) {
                    int vip_end_time = jsonObject.getInt("vip_end_time");
                    if (vip_end_time > 0) {
                        ycSingle.vip_end_time = vip_end_time;
                        idCorrelationLogin.vip_end_time = (vip_end_time);
                    }
                }
                if (jsonObject.has("id")) {
                    int id = jsonObject.getInt("id");
                    if (id > 0) {
                        ycSingle.id = id;
                        idCorrelationLogin.id = id;
                    }
                }
                if (jsonObject.has("vip")) {
                    int vip = jsonObject.getInt("vip");
                    if (vip > 0) {
                        ycSingle.vip = vip;
                        idCorrelationLogin.vip = vip;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.d("mylog", "backfillLoginData: " + e);
            }
        }

        Log.d("mylog", "backfillLoginData: idCorrelationLogin " + idCorrelationLogin.toString());

        //新取到的数据
        if (!TextUtils.isEmpty(stringBean) && !"null".equals(stringBean)) {
            IdCorrelationLoginBean idCorrelationLoginBean = new Gson().fromJson(stringBean, IdCorrelationLoginBean.class);

            String face = idCorrelationLoginBean.face;
            String nick_name = idCorrelationLoginBean.nick_name;
            String name = idCorrelationLoginBean.name;
            String mobile = idCorrelationLoginBean.mobile;
            int vip_end_time = idCorrelationLoginBean.vip_end_time;
            int id = idCorrelationLoginBean.id;
            int vip = idCorrelationLoginBean.vip;
            if (!TextUtils.isEmpty(face)) {
                ycSingle.face = face;
                idCorrelationLogin.face = face;
            }
            if (!TextUtils.isEmpty(nick_name)) {
                ycSingle.nick_name = nick_name;
                idCorrelationLogin.nick_name = nick_name;
            }
            if (!TextUtils.isEmpty(name)) {
                ycSingle.name = name;
                idCorrelationLogin.name = name;
            }
            if (!TextUtils.isEmpty(mobile)) {
                ycSingle.mobile = mobile;
                idCorrelationLogin.mobile = mobile;
            }
            if (vip_end_time > 0) {
                ycSingle.vip_end_time = vip_end_time;
                idCorrelationLogin.vip_end_time = vip_end_time;
            }
            if (id > 0) {
                ycSingle.id = id;
                idCorrelationLogin.id = id;
            }
            if (vip > 0) {
                ycSingle.vip = vip;
                idCorrelationLogin.vip = vip;
            }
//            Log.d("mylog", "backfillLoginData: idCorrelationLoginBean " + idCorrelationLoginBean.toString());
//            Log.d("mylog", "backfillLoginData: idCorrelationLogin " + idCorrelationLogin.toString());
        }
        String string = JSON.toJSONString(idCorrelationLogin);
        Log.d("mylog", "backfillLoginData: JSON.toJSONString(idCorrelationLogin " + string);
        SPUtils.put(context, SPUtils.ID_INFO_BEAN, string);
    }
}
