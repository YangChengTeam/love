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

        return request.indexGoods().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<ResultInfo<OthersJoinNum>> othersJoinNum() {

        return request.othersJoinNum().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<ResultInfo<OrdersInitBean>> initOrders(Map<String, String> params) {


        return request.initOrders(params).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }





}
