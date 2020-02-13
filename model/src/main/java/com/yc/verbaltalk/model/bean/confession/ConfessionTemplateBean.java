package com.yc.verbaltalk.model.bean.confession;

import java.io.Serializable;

/**
 * Created by mayn on 2019/6/5.
 */

public class ConfessionTemplateBean implements Serializable {
    /**
     * delay : 60
     * height : 671
     * love_id : 2323
     * img : http://nz.qqtn.com/zbsq/Upload/Model/5cdb8a2aa2311.png
     * model_id : 2361
     * pid : 0
     * sort : 0
     * title :
     * width : 671
     */

    public String delay;
    public String height;
    public String id;
    public String img;
    public String model_id;
    public String pid;
    public String sort;
    public String title;
    public String width;

    @Override
    public String toString() {
        return "ConfessionTemplateBean{" +
                "delay='" + delay + '\'' +
                ", height='" + height + '\'' +
                ", love_id='" + id + '\'' +
                ", img='" + img + '\'' +
                ", model_id='" + model_id + '\'' +
                ", pid='" + pid + '\'' +
                ", sort='" + sort + '\'' +
                ", title='" + title + '\'' +
                ", width='" + width + '\'' +
                '}';
    }
}
