package com.yc.love.model.bean;

import java.io.Serializable;

/**
 * Created by mayn on 2019/5/7.
 */

public class IdCorrelationLoginBean implements Serializable {
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


    public int id;
    public int is_vip;
    public String name;
    public String nick_name;
    public String birthday;
    public int sex;
    public String mobile;
    public String face;
    public int vip;
    public int vip_end_time;


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
                '}';
    }
}
