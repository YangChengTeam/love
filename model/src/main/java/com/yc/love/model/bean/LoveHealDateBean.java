package com.yc.love.model.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by mayn on 2019/5/9.
 */

public class LoveHealDateBean implements Serializable {
    /* //
     *//**
     * code : 1
     * msg : Success
     *//*

    public int code;
    public String msg;
    public List<DataBean> data;

    public static class DataBean {
        *//**
     * love_id : 1
     * name : 开场
     * parent_id : 0
     * _level : 1
     *//*

        public int love_id;
        public String name;
        public int parent_id;
        public String _level;
        public List<ChildrenBean> children;

        public static class ChildrenBean {
            */
    /**
     * id : 2
     * name : 搭讪开场
     * parent_id : 1
     * _level : 2
     *//*

            public int love_id;
            public String name;
            public int parent_id;
            public String _level;
        }
    }*/



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
                    ", love_id=" + id +
                    ", name='" + name + '\'' +
                    ", parent_id=" + parent_id +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "LoveHealDateBean{" +
                "_level='" + _level + '\'' +
                ", love_id=" + id +
                ", name='" + name + '\'' +
                ", parent_id=" + parent_id +
                ", children=" + children +
                '}';
    }
}
