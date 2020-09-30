package com.yc.verbaltalk.chat.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.Serializable;

/**
 * Created by sunshey on 2019/4/30.
 */

public class LoveHealBean implements MultiItemEntity, Serializable {
    public int type;
    public String _level;
    public int id;
    public String name;
    public int parent_id;

    public static final int VIEW_ITEM = 0;
    public static final int LOVE_HEAL_TYPE_TITLE = 1;
    public static final int LOVE_HEAL_TYPE_ITEM = 2;
    public static final int LOVE_HEAL_TYPE_NO_ANY = 3;

    public LoveHealBean(int type, String _level, int id, String name, int parent_id) {
        this.type = type;
        this._level = _level;
        this.id = id;
        this.name = name;
        this.parent_id = parent_id;
    }

    public LoveHealBean(int type, String name) {
        this.type = type;
        this.name = name;
    }

    @Override
    public String toString() {
        return "LoveHealBean{" +
                "type=" + type +
                ", _level='" + _level + '\'' +
                ",id=" + id +
                ", name='" + name + '\'' +
                ", parent_id=" + parent_id +
                '}';
    }

    @Override
    public int getItemType() {
        return type;
    }
}
