package com.yc.love.model.bean;

/**
 * Created by mayn on 2019/4/25.
 */

public class LoveHealingBean {
    public String name;
    public int type;



    public LoveHealingBean(String name, int type) {
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
