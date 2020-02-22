package com.yc.verbaltalk.chat.bean;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by suns  on 2019/8/30 15:20.
 */
public class CommunityTagInfo {

    /**
     * id : 11
     * title : 导师心得
     * is_hot : 1
     */

    @JSONField(name = "cat_id")
    private String id;
    @JSONField(name = "cat_name")
    private String title;
    private String is_hot;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIs_hot() {
        return is_hot;
    }

    public void setIs_hot(String is_hot) {
        this.is_hot = is_hot;
    }

    @Override
    public String toString() {
        return "CommunityTagInfo{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", is_hot='" + is_hot + '\'' +
                '}';
    }
}
