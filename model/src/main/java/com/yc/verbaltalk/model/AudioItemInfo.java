package com.yc.verbaltalk.model;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by admin on 2018/1/25.
 * 分类下item
 */

public class AudioItemInfo {

    private String id;
    private String title;
    private String type_id;
    private String img;
    @JSONField(name = "author_id")
    private String author;
    private String desp;
    @JSONField(name = "url")
    private String file;
    @JSONField(name = "listen_times")
    private String play_num;
    @JSONField(name = "duration")
    private String time;
    private String add_time;
    private String add_date;

    private int is_favorite;
    @JSONField(name = "author_name")
    private String author_title;


    private int groupPos;//当前数据在page页的位置

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

    public String getType_id() {
        return type_id;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDesp() {
        return desp;
    }

    public void setDesp(String desp) {
        this.desp = desp;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getPlay_num() {
        return play_num;
    }

    public void setPlay_num(String play_num) {
        this.play_num = play_num;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public String getAdd_date() {
        return add_date;
    }

    public void setAdd_date(String add_date) {
        this.add_date = add_date;
    }

    public int getIs_favorite() {
        return is_favorite;
    }

    public void setIs_favorite(int is_favorite) {
        this.is_favorite = is_favorite;
    }

    public String getAuthor_title() {
        return author_title;
    }

    public void setAuthor_title(String author_title) {
        this.author_title = author_title;
    }

    public int getGroupPos() {
        return groupPos;
    }

    public void setGroupPos(int groupPos) {
        this.groupPos = groupPos;
    }

    @Override
    public String toString() {
        return "AudioItemInfo{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", type_id='" + type_id + '\'' +
                ", img='" + img + '\'' +
                ", author='" + author + '\'' +
                ", desp='" + desp + '\'' +
                ", file='" + file + '\'' +
                ", play_num='" + play_num + '\'' +
                ", time='" + time + '\'' +
                ", add_time='" + add_time + '\'' +
                ", add_date='" + add_date + '\'' +
                ", is_favorite=" + is_favorite +
                ", author_title='" + author_title + '\'' +
                ", groupPos=" + groupPos +
                '}';
    }
}
