package com.yc.love.model.bean;

import java.util.List;

/**
 * Created by mayn on 2019/5/5.
 */

public class BecomeVipBean {
    public int type;
    public String name;
    public String subName;
    public int imgResId;
    public List<BecomeVipPayBean> payBeans;




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

    public BecomeVipBean(int type, List<BecomeVipPayBean> payBeans) {
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
}
