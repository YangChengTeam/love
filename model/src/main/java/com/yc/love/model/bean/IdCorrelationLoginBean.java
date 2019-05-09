package com.yc.love.model.bean;

/**
 * Created by mayn on 2019/5/7.
 */

public class IdCorrelationLoginBean {
    /**
     * face : 1
     * vip_end_time : 0
     * nick_name : 1
     * name : 1
     * mobile : 15927678095
     * id : 2
     * vip : 0
     */

    public String face;
    public String nick_name;
    public String name;
    public String mobile;
    public int vip_end_time;
    public int id;
    public int vip;

    @Override
    public String toString() {
        return "IdCorrelationLoginBean{" +
                "face='" + face + '\'' +
                ", vip_end_time=" + vip_end_time +
                ", nick_name='" + nick_name + '\'' +
                ", name='" + name + '\'' +
                ", mobile='" + mobile + '\'' +
                ", id=" + id +
                ", vip=" + vip +
                '}';
    }
}
