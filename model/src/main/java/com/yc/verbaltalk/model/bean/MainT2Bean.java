package com.yc.verbaltalk.model.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.Serializable;

/**
 * Created by mayn on 2019/4/25.
 */

public class MainT2Bean implements Serializable, MultiItemEntity {
    public String name;
    public int type;

    /**
     * create_time : 1509927506
     * love_id : 263
     * image :
     * post_title : 约超萌超可爱美女出来看电影，当晚就拿下
     */

    public int create_time;
    public int canShouCount;
    public int id;
    public String image;
    public String post_title;
    public int imgId;

    public static final int VIEW_TITLE = 0;
    public static final int VIEW_ITEM = 1;
    public static final int VIEW_VIP = 2;
    public static final int VIEW_PROG = 4;
    public static final int VIEW_TO_PAY_VIP = 3;


    public MainT2Bean() {
    }

    public MainT2Bean(String name, int type) {
        this.name = name;
        this.type = type;
    }

    public MainT2Bean(String name, int type, int canShouCount) {
        this.name = name;
        this.type = type;
        this.canShouCount = canShouCount;
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
                ", love_id=" + id +
                ", image='" + image + '\'' +
                ", post_title='" + post_title + '\'' +
                '}';
    }

    @Override
    public int getItemType() {
        return type;
    }
}
