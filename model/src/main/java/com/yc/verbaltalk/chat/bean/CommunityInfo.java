package com.yc.verbaltalk.chat.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by suns  on 2019/8/28 15:48.
 */
public class CommunityInfo implements MultiItemEntity {

    @JSONField(name = "topic_id")
    public String topicId;
    @JSONField(name = "face")
    public String pic;
    @JSONField(name = "nick_name")
    public String name;
    public String date;
    public String content;

    @JSONField(name = "cat_name")
    public String tag;
    @JSONField(name = "digcount")
    public int like_num;

    @JSONField(name = "comcount")
    public int comment_num;

    public long create_time;

    public int is_dig;//是否点赞

    public CommunityTagInfo tagInfo;

    public String comment_id;

    public CommunityInfo detail;



    public static final int ITEM_TOP_ACTIVITY = 0;
    public static final int ITEM_TAG = 1;

    public static final int ITEM_CONTENT = 2;
    public static final int ITEM_DIVIDER = 3;
    public int itemType = ITEM_CONTENT;

    public CommunityInfo(int itemType) {
        this.itemType = itemType;
    }

    public CommunityInfo(int itemType, CommunityTagInfo tagInfo) {
        this.tagInfo = tagInfo;
        this.itemType = itemType;
    }

    public CommunityInfo(String name, long create_time, String content, String tag, int like_num, int comment_num) {
        this.name = name;
        this.create_time = create_time;
        this.content = content;
        this.tag = tag;
        this.like_num = like_num;
        this.comment_num = comment_num;
    }

    public CommunityInfo() {
    }

    @Override
    public String toString() {
        return "CommunityInfo{" +
                "pic='" + pic + '\'' +
                ", name='" + name + '\'' +
                ", date='" + date + '\'' +
                ", content='" + content + '\'' +
                ", tag='" + tag + '\'' +
                ", like_num=" + like_num +
                ", comment_num=" + comment_num +
                '}';
    }

    @Override
    public int getItemType() {
        return itemType;
    }
}
