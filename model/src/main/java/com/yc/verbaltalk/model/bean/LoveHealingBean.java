package com.yc.verbaltalk.model.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by mayn on 2019/4/25.
 */

public class LoveHealingBean implements MultiItemEntity, Serializable {

    /**
     * chat_count : 4
     * chat_name : 别让我看见你
     * id : 2
     * quiz_sex : 1
     * search_type : 1
     */

    public String chat_count;
    public String chat_name;
    public int id;
    public int is_collect;
    public String quiz_sex;
    public String search_type;
    public List<LoveHealingDetailBean> detail;
    public static final int VIEW_TITLE = 0;
    public static final int VIEW_ITEM_ITEM = 1;
    public static final int VIEW_ITEM = 2;
    public static final int VIEW_PROG = 3;

    //    public String name;
    public int type;


    public LoveHealingBean() {
    }

    public LoveHealingBean(int type, String chat_name) {
        this.type = type;
        this.chat_name = chat_name;
    }

    public LoveHealingBean(int type, String chat_count, String chat_name, int id, String quiz_sex, String search_type) {
        this.type = type;
        this.chat_count = chat_count;
        this.chat_name = chat_name;
        this.id = id;
        this.quiz_sex = quiz_sex;
        this.search_type = search_type;
    }

    @Override
    public String toString() {
        return "LoveHealingBean{" +
                "type=" + type +
                ", chat_count='" + chat_count + '\'' +
                ", chat_name='" + chat_name + '\'' +
                ", id=" + id +
                ", quiz_sex='" + quiz_sex + '\'' +
                ", search_type='" + search_type + '\'' +
                '}';
    }

    @Override
    public int getItemType() {
        return type;
    }
}
