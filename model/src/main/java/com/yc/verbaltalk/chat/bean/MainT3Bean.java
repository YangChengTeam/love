package com.yc.verbaltalk.chat.bean;

import android.graphics.drawable.Drawable;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.Serializable;

/**
 * Created by sunshey on 2019/4/30.
 */

public class MainT3Bean implements MultiItemEntity, Serializable {

    public int type;
    public String titleName;

    //    public int imgResId;
//    public String des;


    /**
     * _level : 2
     * desp : 形象决定气质，改变从现在开始
     * love_id : 12
     * image : http://v.bjyzbx.com/upload/image/20181129/5bff5e8745ce3.jpg
     * name : 形象提升
     * parent_id : 19
     */

    public static final int LOVE_HEAL_TYPE_TITLE = 1;
    public static final int LOVE_HEAL_TYPE_ITEM_TITLE = 2;
    public static final int LOVE_HEAL_TYPE_ITEM = 3;
    public static final int LOVE_HEAL_TYPE_ITEM_LOCALITY = 4;

    public String _level;
    public String desp;
    public int id;
    public String image;
    public transient Drawable imageResourceldId;
    public int imageResourceld;
    public String name;
    public int parent_id;

    public MainT3Bean() {
    }

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

    public MainT3Bean(int type, String _level, String desp, int id, int imageResourceld, String name, int parent_id) {
        this.type = type;
        this._level = _level;
        this.desp = desp;
        this.id = id;
        this.imageResourceld = imageResourceld;
        this.name = name;
        this.parent_id = parent_id;
    }

    @Override
    public String toString() {
        return "MainT3Bean{" +
                "type=" + type +
                ", titleName='" + titleName + '\'' +
                ", _level='" + _level + '\'' +
                ", desp='" + desp + '\'' +
                ", love_id=" + id +
                ", image='" + image + '\'' +
                ", name='" + name + '\'' +
                ", parent_id=" + parent_id +
                '}';
    }

    @Override
    public int getItemType() {
        return type;
    }
}
