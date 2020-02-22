package com.yc.verbaltalk.base.engine;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.engin.HttpCoreEngin;
import com.yc.verbaltalk.chat.bean.AResultInfo;
import com.yc.verbaltalk.chat.bean.IdCorrelationLoginBean;
import com.yc.verbaltalk.base.config.URLConfig;

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
