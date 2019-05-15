package com.yc.love.model.bean;

/**
 * Created by mayn on 2019/4/30.
 */

public class MainT3Bean {

    public int type;
    public String titleName;

    //    public int imgResId;
//    public String des;


    public int id;
    public String post_title;
    public String image;
    public String tag;
    public int tag_id;
    public String category_name;

    public MainT3Bean(int type) {
        this.type = type;
    }

    public MainT3Bean(int type, String titleName) {
        this.type = type;
        this.titleName = titleName;
    }

    public MainT3Bean(int type, int id, String post_title, String image, String tag, int tag_id, String category_name) {
        this.type = type;
        this.id = id;
        this.post_title = post_title;
        this.image = image;
        this.tag = tag;
        this.tag_id = tag_id;
        this.category_name = category_name;
    }

/*public MainT3Bean(int type, String name, String des, int imgResId) {
        this.type = type;
        this.name = name;
        this.des = des;
        this.imgResId = imgResId;
    }*/
}
