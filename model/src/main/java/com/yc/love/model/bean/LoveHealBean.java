package com.yc.love.model.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by mayn on 2019/4/30.
 */

public class LoveHealBean implements Serializable {
    public int type;
    public String _level;
    public int id;
    public String name;
    public int parent_id;

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
                ", id=" + id +
                ", name='" + name + '\'' +
                ", parent_id=" + parent_id +
                '}';
    }
}
