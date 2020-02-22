package com.yc.verbaltalk.base.engine;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.engin.HttpCoreEngin;
import com.yc.verbaltalk.chat.bean.AResultInfo;
import com.yc.verbaltalk.chat.bean.ExampleTsListBean;
import com.yc.verbaltalk.chat.bean.IdCorrelationLoginBean;
import com.yc.verbaltalk.chat.bean.IndexDoodsBean;
import com.yc.verbaltalk.chat.bean.OrdersInitBean;
import com.yc.verbaltalk.chat.bean.OthersJoinNum;
import com.yc.verbaltalk.base.config.URLConfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * Created by mayn on 2019/5/14.
 */

public class OrderEngine extends BaseEngine {
    public OrderEngine(Context context) {
        super(context);
    }

    public Observable<AResultInfo<List<IndexDoodsBean>>> indexDoods( String url) {
        Map<String, String> params = new HashMap<>();
//        params.put("page_size", pageSize);
        requestParams(params);
        HttpCoreEngin<AResultInfo<List<IndexDoodsBean>>> httpCoreEngin = HttpCoreEngin.get(mContext);
        Observable<AResultInfo<List<IndexDoodsBean>>> rxpost = httpCoreEngin.rxpost(URLConfig.getUrl(url), new TypeReference<AResultInfo<List<IndexDoodsBean>>>() {
                }.getType(),
                params,
                true,
                true, true);
        return rxpost;
    }
    public Observable<AResultInfo<OthersJoinNum>> othersJoinNum(String url) {
        Map<String, String> params = new HashMap<>();
        requestParams(params);
        HttpCoreEngin<AResultInfo<OthersJoinNum>> httpCoreEngin = HttpCoreEngin.get(mContext);
        Observable<AResultInfo<OthersJoinNum>> rxpost = httpCoreEngin.rxpost(URLConfig.getUrl(url), new TypeReference<AResultInfo<OthersJoinNum>>() {
                }.getType(),
                params,
                true,
                true, true);
        return rxpost;
    }
    public Observable<AResultInfo<IdCorrelationLoginBean>> userInfo(String userId, String url) {
        Map<String, String> params = new HashMap<>();
        params.put("user_id", userId);
        requestParams(params);
        HttpCoreEngin<AResultInfo<IdCorrelationLoginBean>> httpCoreEngin = HttpCoreEngin.get(mContext);
        Observable<AResultInfo<IdCorrelationLoginBean>> rxpost = httpCoreEngin.rxpost(URLConfig.getUrl(url), new TypeReference<AResultInfo<IdCorrelationLoginBean>>() {
                }.getType(),
                params,
                true,
                true, true);
        return rxpost;
    }

    public Observable<AResultInfo<OrdersInitBean>> initOrders(Map<String, String> params, String url) {
        requestParams(params);
        HttpCoreEngin<AResultInfo<OrdersInitBean>> httpCoreEngin = HttpCoreEngin.get(mContext);
        Observable<AResultInfo<OrdersInitBean>> rxpost = httpCoreEngin.rxpost(URLConfig.getUrl(url), new TypeReference<AResultInfo<OrdersInitBean>>() {
                }.getType(),
                params,
                true,
                true, true);
        return rxpost;
    }

    public Observable<AResultInfo<List<ExampleTsListBean>>> collectListsArticle(String userId, String page, String pageSize, String url) {
        Map<String, String> params = new HashMap<>();
        params.put("user_id", userId);
        params.put("page", page);
        params.put("page_size", pageSize);
        requestParams(params);
        HttpCoreEngin<AResultInfo<List<ExampleTsListBean>>> httpCoreEngin = HttpCoreEngin.get(mContext);
        Observable<AResultInfo<List<ExampleTsListBean>>> rxpost = httpCoreEngin.rxpost(URLConfig.getUrl(url), new TypeReference<AResultInfo<List<ExampleTsListBean>>>() {
                }.getType(),
                params,
                true,
                true, true);
        return rxpost;
    }

    public Observable<AResultInfo<String>> collectLovewords(String userId, String lovewordsId, String url) {
        Map<String, String> params = new HashMap<>();
        params.put("user_id", userId);
        params.put("lovewords_id", lovewordsId);
        requestParams(params);
        HttpCoreEngin<AResultInfo<String>> httpCoreEngin = HttpCoreEngin.get(mContext);
        Observable<AResultInfo<String>> rxpost = httpCoreEngin.rxpost(URLConfig.getUrl(url), new TypeReference<AResultInfo<String>>() {
                }.getType(),
                params,
                true,
                true, true);
        return rxpost;
    }


}
