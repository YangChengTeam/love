package com.yc.love.model.bean;

/**
 * Created by mayn on 2019/4/30.
 */

public class MainT3Bean {

    public int type;
    public String name;
    public String des;
    public int imgResId;

    public MainT3Bean(int type) {
        this.type = type;
    }

    public MainT3Bean(int type, String name) {
        this.type = type;
        this.name = name;
    }

    public MainT3Bean(int type, String name, String des, int imgResId) {
        this.type = type;
        this.name = name;
        this.des = des;
        this.imgResId = imgResId;
    }
}
