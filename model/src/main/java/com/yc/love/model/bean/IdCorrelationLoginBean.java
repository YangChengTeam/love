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

    private String face;
    private String nick_name;
    private String name;
    private String mobile;
    private int vip_end_time;
    private int id;
    private int vip;

    public String getFace() {
        return face;
    }

    public void setFace(String face) {
        this.face = face;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getVip_end_time() {
        return vip_end_time;
    }

    public void setVip_end_time(int vip_end_time) {
        this.vip_end_time = vip_end_time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVip() {
        return vip;
    }

    public void setVip(int vip) {
        this.vip = vip;
    }

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
