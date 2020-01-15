package com.yc.love.model.bean;

/**
 * Created by mayn on 2019/4/25.
 */

public class StringBean {
    public String name;

    public StringBean(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "StringBean{" +
                "name='" + name + '\'' +
                '}';
    }
}
