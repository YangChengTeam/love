package com.yc.verbaltalk.chat.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;



public class LoveHealDetBean implements MultiItemEntity {

//    public int type=0;
    /**
     * ans_sex : 1
     * chat_count : 2
     * chat_name : 李晨
     * love_id : 907
     * quiz_sex : 2
     * is_open : 1
     * search_type : 1
     * details : [{"love_id":3013,"dialogue_id":907,"content":"李晨","ans_sex":"2"},{"love_id":3014,"dialogue_id":907,"content":"我们这样的人都有一个通病，就是太相信自己的嘴，而不相信自己的耳朵。","ans_sex":"1"}]
     */

    public String ans_sex;
    public String chat_count;
    public String chat_name;
    public int id;
    public int is_vip = 1; // 0 显示  1 不显示
    public String quiz_sex;
    public String is_open;
    public String search_type;
    public List<LoveHealDetDetailsBean> details;
    public List<LoveHealDetDetailsBean> detail;
    public static final int VIEW_VIP = 1;  // 1 显示VIP  0 不显示VIP,显示ITEM
    public static final int VIEW_ITEM = 0;
    public int type;

    /*  public static class DetailsBean {
     */

    /**
     * love_id : 3013
     * dialogue_id : 907
     * content : 李晨
     * ans_sex : 2
     *//*

        public int love_id;
        public int size;
        public int dialogue_id;
        public String content;
        public String ans_sex;

        @Override
        public String toString() {
            return "DetailsBean{" +
                    "love_id=" + love_id +
                    ", dialogue_id=" + dialogue_id +
                    ", content='" + content + '\'' +
                    ", ans_sex='" + ans_sex + '\'' +
                    '}';
        }
    }*/
    @Override
    public String toString() {
        return "LoveHealDetBean{" +
                ", ans_sex='" + ans_sex + '\'' +
                ", chat_count='" + chat_count + '\'' +
                ", chat_name='" + chat_name + '\'' +
                ", id=" + id +
                ", quiz_sex='" + quiz_sex + '\'' +
                ", is_open='" + is_open + '\'' +
                ", search_type='" + search_type + '\'' +
                ", details=" + details +
                '}';
    }

    @Override
    public int getItemType() {
        return type;
    }
}
