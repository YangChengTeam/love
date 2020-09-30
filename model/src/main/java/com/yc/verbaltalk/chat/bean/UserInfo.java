package com.yc.verbaltalk.chat.bean;

import java.io.Serializable;


public class UserInfo implements Serializable {
    /**
     * id : 2
     * name :
     * nick_name : 小明
     * birthday : 20150514
     * sex : 1
     * mobile : 15927678095
     * face : https://avatar.csdn.net/A/1/4/3_lawsonjin.jpg
     * vip : 0
     * vip_end_time : 0
     */


    public String id;
    public int is_vip;
    public String name;
    public String nick_name;
    public String birthday;
    public int sex;
    public String mobile;
    public String face;
    public int vip;
    public int vip_end_time;
    /* "vip_tips": 1 已开通
         "vip_tips":  2     已过期
         "vip_tips": 0 未开通*/
    public int vip_tips;
    public String pwd;

    @Override
    public String toString() {
        return "IdCorrelationLoginBean{" +
                "id=" + id +
                ", is_vip=" + is_vip +
                ", name='" + name + '\'' +
                ", nick_name='" + nick_name + '\'' +
                ", birthday='" + birthday + '\'' +
                ", sex=" + sex +
                ", mobile='" + mobile + '\'' +
                ", face='" + face + '\'' +
                ", vip=" + vip +
                ", vip_end_time=" + vip_end_time +
                ", vip_tips=" + vip_tips +
                '}';
    }
}
