package com.yc.verbaltalk.chat.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.yc.verbaltalk.model.AudioItemInfo;

import java.util.List;

/**
 * Created by admin on 2018/1/25.
 * 分类信息
 */

public class AudioDataInfo {

    private String id;
    @JSONField(name = "cat_name")
    private String title;
    @JSONField(name = "cat_img")
    private String img;

    private String playicon;

    @JSONField(name = "detail")
    private List<AudioItemInfo> first;

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

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public List<AudioItemInfo> getFirst() {
        return first;
    }

    public void setFirst(List<AudioItemInfo> first) {
        this.first = first;
    }

    public String getPlayicon() {
        return playicon;
    }

    public void setPlayicon(String playicon) {
        this.playicon = playicon;
    }
}
