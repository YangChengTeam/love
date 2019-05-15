package com.yc.love.model.bean;

/**
 * Created by mayn on 2019/4/25.
 */

public class MainT2Bean {
    public String name;
    public int type;

    /**
     * create_time : 1509927506
     * id : 263
     * image :
     * post_title : 约超萌超可爱美女出来看电影，当晚就拿下
     */

    public int create_time;
    public int id;
    public String image;
    public String post_title;

    public MainT2Bean() {
    }

    public MainT2Bean(String name, int type) {
        this.name = name;
        this.type = type;
    }

    public MainT2Bean(int type, int create_time, int id, String image, String post_title) {
        this.type = type;
        this.create_time = create_time;
        this.id = id;
        this.image = image;
        this.post_title = post_title;
    }

    @Override
    public String toString() {
        return "MainT2Bean{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", create_time=" + create_time +
                ", id=" + id +
                ", image='" + image + '\'' +
                ", post_title='" + post_title + '\'' +
                '}';
    }
}
