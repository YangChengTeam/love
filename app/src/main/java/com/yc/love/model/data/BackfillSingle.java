package com.yc.love.model.data;

import com.google.gson.Gson;
import com.yc.love.model.bean.IdCorrelationLoginBean;
import com.yc.love.model.single.YcSingle;

/**
 * Created by mayn on 2019/5/8.
 */

public class BackfillSingle {

    public static void backfillLoginData(String stringBean) {
        IdCorrelationLoginBean idCorrelationLoginBean = new Gson().fromJson(stringBean, IdCorrelationLoginBean.class);
        YcSingle ycSingle = YcSingle.getInstance();
        ycSingle.vip_end_time = idCorrelationLoginBean.vip_end_time;
        ycSingle.id = idCorrelationLoginBean.id;
        ycSingle.vip = idCorrelationLoginBean.vip;
        ycSingle.id = idCorrelationLoginBean.id;
        ycSingle.face = idCorrelationLoginBean.face;
        ycSingle.nick_name = idCorrelationLoginBean.nick_name;
        ycSingle.name = idCorrelationLoginBean.name;
        ycSingle.mobile = idCorrelationLoginBean.mobile;
    }

}
