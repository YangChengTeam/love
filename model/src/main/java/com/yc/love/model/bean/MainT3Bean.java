package com.yc.love.model.bean;

/**
 * Created by mayn on 2019/4/30.
 */

public class MainT3Bean {

    public int type;
    public String titleName;

    //    public int imgResId;
//    public String des;


    /**
     * _level : 2
     * desp : 形象决定气质，改变从现在开始
     * id : 12
     * image : http://v.bjyzbx.com/upload/image/20181129/5bff5e8745ce3.jpg
     * name : 形象提升
     * parent_id : 19
     */

    public String _level;
    public String desp;
    public int id;
    public String image;
    public String name;
    public int parent_id;

    public MainT3Bean(int type) {
        this.type = type;
    }

    public MainT3Bean(int type, String titleName) {
        this.type = type;
        this.titleName = titleName;
    }

    public MainT3Bean(int type, String _level, String desp, int id, String image, String name, int parent_id) {
        this.type = type;
        this._level = _level;
        this.desp = desp;
        this.id = id;
        this.image = image;
        this.name = name;
        this.parent_id = parent_id;
    }
}
