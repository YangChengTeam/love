package com.yc.verbaltalk.chat.bean;

import java.io.Serializable;
import java.util.List;



public class LoveHealDateBean implements Serializable {



    public String _level;
    public int id;
    public String name;
    public int parent_id;
    public List<ChildrenBean> children;


    public class ChildrenBean implements Serializable {
        public String _level;
        public int id;
        public String name;
        public int parent_id;

        @Override
        public String toString() {
            return "ChildrenBean{" +
                    "_level='" + _level + '\'' +
                    ", id=" + id +
                    ", name='" + name + '\'' +
                    ", parent_id=" + parent_id +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "LoveHealDateBean{" +
                "_level='" + _level + '\'' +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", parent_id=" + parent_id +
                ", children=" + children +
                '}';
    }
}
