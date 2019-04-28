package com.yc.love.model.bean;

/**
 * Created by mayn on 2019/4/25.
 */

public class MainT2Bean {
    public String name;
    public int type;



    public MainT2Bean(String name, int type) {
        this.name = name;
        this.type = type;
    }

    @Override
    public String toString() {
        return "MainT2Bean{" +
                "name='" + name + '\'' +
                ", type=" + type +
                '}';
    }
}
