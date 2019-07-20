package com.yc.love.model.engin;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.engin.HttpCoreEngin;
import com.yc.love.model.base.BaseEngine;
import com.yc.love.model.bean.AResultInfo;
import com.yc.love.model.bean.IdCorrelationLoginBean;
import com.yc.love.model.domain.URLConfig;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;

/**
 * Created by mayn on 2019/6/12.
 */

public class LoveEnginV2  extends BaseEngine {

    public LoveEnginV2(Context context) {
        super(context);
    }

    public Observable<AResultInfo<IdCorrelationLoginBean>> userReg(String url) {
        Map<String, String> params = new HashMap<>();
        requestParams(params);
        HttpCoreEngin<AResultInfo<IdCorrelationLoginBean>> httpCoreEngin = HttpCoreEngin.get(mContext);
        Observable<AResultInfo<IdCorrelationLoginBean>> rxpost = httpCoreEngin.rxpost(URLConfig.getUrlV2(url), new TypeReference<AResultInfo<IdCorrelationLoginBean>>() {
                }.getType(),
                params,
                true,
                true, true);
        return rxpost;
    }
}
