package com.yc.verbaltalk.chat.bean;

/**
 * Created by sunshey on 2019/5/10.
 */

public class LoveByStagesBean {
    /**
     * create_time : 1543305363
     * feeluseful : 200
     * id : 159
     * post_title : 刚在一起没多久就要分隔两地，如何维持异地关系？
     */

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
