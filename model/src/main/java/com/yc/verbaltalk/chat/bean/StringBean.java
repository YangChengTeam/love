package com.yc.verbaltalk.chat.bean;

/**
 * Created by sunshey on 2019/4/25.
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
