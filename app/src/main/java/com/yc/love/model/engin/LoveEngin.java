package com.yc.love.model.engin;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.engin.HttpCoreEngin;
import com.yc.love.model.base.BaseEngine;
import com.yc.love.model.bean.AResultInfo;
import com.yc.love.model.bean.LoveHealDateBean;
import com.yc.love.model.bean.LoveHealDetBean;
import com.yc.love.model.domain.URLConfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * Created by mayn on 2019/5/9.
 */

public class LoveEngin extends BaseEngine {

    public LoveEngin(Context context) {
        super(context);
    }


    public Observable<AResultInfo<List<LoveHealDateBean>>> loveCategory(String url) {
        Map<String, String> params = new HashMap<>();
//        params.put("password", password);
        requestParams(params);
        HttpCoreEngin<AResultInfo<List<LoveHealDateBean>>> httpCoreEngin = HttpCoreEngin.get(mContext);
        Observable<AResultInfo<List<LoveHealDateBean>>> rxpost = httpCoreEngin.rxpost(URLConfig.getUrl(url), new TypeReference<AResultInfo<List<LoveHealDateBean>>>() {
                }.getType(),
                params,
                true,
                true, true);
        return rxpost;
    }
    public Observable<AResultInfo<List<LoveHealDetBean>>> loveListCategory(String category_id, String page, String page_size, String url) {
        Map<String, String> params = new HashMap<>();
        params.put("category_id", category_id);
        params.put("page", page);
        params.put("page_size", page_size);
        requestParams(params);
        HttpCoreEngin<AResultInfo<List<LoveHealDetBean>>> httpCoreEngin = HttpCoreEngin.get(mContext);
        Observable<AResultInfo<List<LoveHealDetBean>>> rxpost = httpCoreEngin.rxpost(URLConfig.getUrl(url), new TypeReference<AResultInfo<List<LoveHealDetBean>>>() {
                }.getType(),
                params,
                true,
                true, true);
        return rxpost;
    }


}
