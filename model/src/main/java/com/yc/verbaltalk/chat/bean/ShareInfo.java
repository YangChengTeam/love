package com.yc.verbaltalk.chat.bean;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by wanglin  on 2019/7/9 16:09.
 */
public class ShareInfo {
    private String title;
    @JSONField(name = "content")
    private String desp;

    private String img;
    private String url;

    public String getDesp() {
        return desp;
    }

    public void setDesp(String desp) {
        this.desp = desp;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
