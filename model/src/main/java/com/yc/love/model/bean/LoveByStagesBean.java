package com.yc.love.model.bean;

import java.util.List;

/**
 * Created by mayn on 2019/5/10.
 */

public class LoveByStagesBean {

    public int id;
    public int feeluseful;
    public int create_time;
    public String post_title;
    public String image;

    @Override
    public String toString() {
        return "LoveByStagesBean{" +
                "id=" + id +
                ", feeluseful=" + feeluseful +
                ", create_time=" + create_time +
                ", post_title='" + post_title + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
