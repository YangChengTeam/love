package com.yc.verbaltalk.chat.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by mayn on 2019/5/10.
 */

public class CategoryArticleBean implements Serializable {
        public String _level;
        public int id;
        public String name;
        public int parent_id;
        public ArrayList<CategoryArticleChildrenBean> children;

    @Override
    public String toString() {
        return "CategoryArticleBean{" +
                "_level='" + _level + '\'' +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", parent_id=" + parent_id +
                ", children=" + children +
                '}';
    }
}
