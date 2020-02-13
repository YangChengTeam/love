package com.yc.verbaltalk.model.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by mayn on 2019/5/5.
 */

public class BecomeVipBean  implements MultiItemEntity, Serializable {


    public int type;
    public String subName;
    public String name;
    public int imgResId;
//    public List<BecomeVipPayBean> payBeans;
    public List<IndexDoodsBean> payBeans;

    public static final int VIEW_TITLE = 1;
    public static final int VIEW_ITEM = 2;
    public static final int VIEW_TAIL = 3;
    public static final int VIEW_VIP_TAG = 4;

    public BecomeVipBean() {
    }

    public BecomeVipBean(int type, String name) {
        this.type = type;
        this.name = name;
    }

    public BecomeVipBean(int type, String name, String subName, int imgResId) {
        this.type = type;
        this.name = name;
        this.subName = subName;
        this.imgResId = imgResId;
    }

    public BecomeVipBean(int type, List<IndexDoodsBean> payBeans) {
        this.type = type;
        this.payBeans = payBeans;
    }

    @Override
    public String toString() {
        return "BecomeVipBean{" +
                "type=" + type +
                ", name='" + name + '\'' +
                ", subName='" + subName + '\'' +
                ", imgResId=" + imgResId +
                ", payBeans=" + payBeans +
                '}';
    }

    @Override
    public int getItemType() {
        return type;
    }
}
