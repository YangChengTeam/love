package com.yc.love.model.bean;

import java.util.List;

/**
 * Created by mayn on 2019/5/9.
 */

public class LoveHealDateBean {
   /* //
    *//**
     * code : 1
     * msg : Success
     * data : [{"id":1,"name":"开场","parent_id":0,"_level":"1","children":[{"id":2,"name":"搭讪开场","parent_id":1,"_level":"2"},{"id":3,"name":"话题重新开场","parent_id":1,"_level":"2"},{"id":4,"name":"表情话术","parent_id":1,"_level":"2"},{"id":5,"name":"趣味搭讪","parent_id":1,"_level":"2"},{"id":6,"name":"勾起好奇","parent_id":1,"_level":"2"}]},{"id":7,"name":"情绪","parent_id":0,"_level":"1","children":[{"id":8,"name":"幽默聊天","parent_id":7,"_level":"2"},{"id":9,"name":"共谋与赞美","parent_id":7,"_level":"2"},{"id":10,"name":"调侃类别","parent_id":7,"_level":"2"},{"id":11,"name":"自恋类别","parent_id":7,"_level":"2"},{"id":12,"name":"影视综艺话梗","parent_id":7,"_level":"2"},{"id":13,"name":"打情骂俏","parent_id":7,"_level":"2"}]},{"id":14,"name":"聊天","parent_id":0,"_level":"1","children":[{"id":15,"name":"初聊互动","parent_id":14,"_level":"2"},{"id":16,"name":"价值型聊天","parent_id":14,"_level":"2"},{"id":17,"name":"冷读术","parent_id":14,"_level":"2"},{"id":18,"name":"话块连情","parent_id":14,"_level":"2"},{"id":19,"name":"框架筛选","parent_id":14,"_level":"2"},{"id":20,"name":"高价值展示","parent_id":14,"_level":"2"},{"id":21,"name":"情感链接","parent_id":14,"_level":"2"},{"id":22,"name":"设立框架","parent_id":14,"_level":"2"},{"id":23,"name":"废物测试","parent_id":14,"_level":"2"},{"id":24,"name":"化解I0D","parent_id":14,"_level":"2"},{"id":25,"name":"联系感建立","parent_id":14,"_level":"2"},{"id":26,"name":"共鸣故事","parent_id":14,"_level":"2"},{"id":27,"name":"异议处理","parent_id":14,"_level":"2"}]},{"id":28,"name":"升级","parent_id":0,"_level":"1","children":[{"id":29,"name":"文字进挪","parent_id":28,"_level":"2"},{"id":30,"name":"框架与拉升","parent_id":28,"_level":"2"},{"id":31,"name":"恋爱调情","parent_id":28,"_level":"2"},{"id":32,"name":"暧昧套路话术","parent_id":28,"_level":"2"},{"id":33,"name":"关系升高","parent_id":28,"_level":"2"},{"id":34,"name":"表达兴趣","parent_id":28,"_level":"2"},{"id":35,"name":"合约恋人","parent_id":28,"_level":"2"},{"id":36,"name":"隐性诱惑","parent_id":28,"_level":"2"},{"id":37,"name":"情感波动","parent_id":28,"_level":"2"},{"id":38,"name":"框架推拉","parent_id":28,"_level":"2"},{"id":39,"name":"真心话大冒险","parent_id":28,"_level":"2"}]},{"id":40,"name":"邀约","parent_id":0,"_level":"1","children":[{"id":41,"name":"模糊邀约","parent_id":40,"_level":"2"},{"id":42,"name":"邀约话术","parent_id":40,"_level":"2"},{"id":43,"name":"速约模板","parent_id":40,"_level":"2"},{"id":44,"name":"预期建立","parent_id":40,"_level":"2"},{"id":45,"name":"促成邀约","parent_id":40,"_level":"2"}]},{"id":46,"name":"约会","parent_id":0,"_level":"1","children":[{"id":47,"name":"现场聊天交流","parent_id":46,"_level":"2"},{"id":48,"name":"互动与游戏","parent_id":46,"_level":"2"},{"id":49,"name":"进挪与升级","parent_id":46,"_level":"2"},{"id":50,"name":"聊天模板","parent_id":46,"_level":"2"},{"id":51,"name":"颜色星座手相","parent_id":46,"_level":"2"},{"id":52,"name":"约会套路","parent_id":46,"_level":"2"}]}]
     *//*

    public int code;
    public String msg;
    public List<DataBean> data;

    public static class DataBean {
        *//**
         * id : 1
         * name : 开场
         * parent_id : 0
         * _level : 1
         * children : [{"id":2,"name":"搭讪开场","parent_id":1,"_level":"2"},{"id":3,"name":"话题重新开场","parent_id":1,"_level":"2"},{"id":4,"name":"表情话术","parent_id":1,"_level":"2"},{"id":5,"name":"趣味搭讪","parent_id":1,"_level":"2"},{"id":6,"name":"勾起好奇","parent_id":1,"_level":"2"}]
         *//*

        public int id;
        public String name;
        public int parent_id;
        public String _level;
        public List<ChildrenBean> children;

        public static class ChildrenBean {
            *//**
             * id : 2
             * name : 搭讪开场
             * parent_id : 1
             * _level : 2
             *//*

            public int id;
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

    public static class ChildrenBean {
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
