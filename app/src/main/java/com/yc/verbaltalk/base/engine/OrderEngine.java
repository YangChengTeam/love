package com.yc.verbaltalk.base.engine;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.yc.verbaltalk.base.config.URLConfig;
import com.yc.verbaltalk.chat.bean.AResultInfo;
import com.yc.verbaltalk.chat.bean.ExampleTsListBean;
import com.yc.verbaltalk.chat.bean.IndexDoodsBean;
import com.yc.verbaltalk.chat.bean.OrdersInitBean;
import com.yc.verbaltalk.chat.bean.OthersJoinNum;
import com.yc.verbaltalk.chat.bean.UserInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import yc.com.rthttplibrary.bean.ResultInfo;


/**
 * Created by sunshey on 2019/5/14.
 */

public class OrderEngine extends BaseEngine {
    public OrderEngine(Context context) {
        super(context);
    }

    public Observable<ResultInfo<List<IndexDoodsBean>>> indexGoods() {
//        Map<String, String> params = new HashMap<>();
////        params.put("page_size", pageSize);
//        requestParams(params);
//        HttpCoreEngin<AResultInfo<List<IndexDoodsBean>>> httpCoreEngin = HttpCoreEngin.get(mContext);
//        Observable<AResultInfo<List<IndexDoodsBean>>> rxpost = httpCoreEngin.rxpost(URLConfig.getUrl(url), new TypeReference<AResultInfo<List<IndexDoodsBean>>>() {
//                }.getType(),
//                params,
//                true,
//                true, true);
//        return rxpost;

        return request.indexGoods().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<ResultInfo<OthersJoinNum>> othersJoinNum() {
//        Map<String, String> params = new HashMap<>();
//        requestParams(params);
//        HttpCoreEngin<AResultInfo<OthersJoinNum>> httpCoreEngin = HttpCoreEngin.get(mContext);
//        Observable<AResultInfo<OthersJoinNum>> rxpost = httpCoreEngin.rxpost(URLConfig.getUrl(url), new TypeReference<AResultInfo<OthersJoinNum>>() {
//                }.getType(),
//                params,
//                true,
//                true, true);
//        return rxpost;
        return request.othersJoinNum().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<ResultInfo<OrdersInitBean>> initOrders(Map<String, String> params) {
//        requestParams(params);
//        HttpCoreEngin<AResultInfo<OrdersInitBean>> httpCoreEngin = HttpCoreEngin.get(mContext);
//        Observable<AResultInfo<OrdersInitBean>> rxpost = httpCoreEngin.rxpost(URLConfig.getUrl(url), new TypeReference<AResultInfo<OrdersInitBean>>() {
//                }.getType(),
//                params,
//                true,
//                true, true);
//        return rxpost;

        return request.initOrders(params).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }





}
