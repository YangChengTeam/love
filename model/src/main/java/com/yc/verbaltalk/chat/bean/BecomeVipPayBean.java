package com.yc.verbaltalk.chat.bean;

/**
 * Created by mayn on 2019/5/5.
 */

public class BecomeVipPayBean {

    /**
     * payDes : 最低0.5月/天
     * payName : 购买全年vip
     * payMoney : 198
     */

    public String payName;
    public int payMoney;
    public String payDes;

    public BecomeVipPayBean(String payName, int payMoney, String payDes) {
        this.payName = payName;
        this.payMoney = payMoney;
        this.payDes = payDes;
    }

    @Override
    public String toString() {
        return "BecomeVipPayBean{" +
                "payName='" + payName + '\'' +
                ", payMoney=" + payMoney +
                ", payDes='" + payDes + '\'' +
                '}';
    }
}
