package com.yc.love.model.bean;

/**
 * Created by mayn on 2019/5/7.
 */

public class IdCorrelationLoginBean {
    /**
     * id : 2
     * name :
     * nick_name : 12345
     * mobile : 15927678095
     * face : https://avatar.csdn.net/A/1/4/3_lawsonjin.jpg
     * vip : 0
     * vip_end_time : 0
     */

    public int id;
    public String name;
    public String nick_name;
    public String mobile;
    public String face;
    public int vip;
    public int vip_end_time;

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
