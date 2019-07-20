package com.music.player.lib.bean;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * TinyHung@Outlook.com
 * 2018/1/18.
 */


public class MusicInfo {


    private String id;// 音乐ID
    private String type_id;// 分类ID
    private String title;// 音乐名称
    private String img;// 图片
    @JSONField(name = "url")
    private String file;// 音频文件
    @JSONField(name = "listen_times")
    private String play_num;// 播放次数
    @JSONField(name = "duration")
    private String time;// 时长（秒）
    private long add_time;// 添加时间
    private long add_date;// 添加日期
    //
    @JSONField(name = "is_collect")
    private int is_favorite;// 是否收藏 0 未收藏 1已收藏

    private int plauStatus;//0：未播放 1：准备中 2：正在播放 3：暂停播放, 4：停止播放

    @JSONField(name = "author_id")
    private String author;// 作者ID

    private String desp;// 简介

    @JSONField(name = "author_name")
    private String author_title;// 作者名称

    // extend
    private String author_desp;// 作者简介
    private String author_img;// 作者图片

    private List<MusicInfo> lists;

    private int type;//1.音乐 2.spa


    public int getPlauStatus() {
        return plauStatus;
    }

    public void setPlauStatus(int plauStatus) {
        this.plauStatus = plauStatus;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType_id() {
        return type_id;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
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

    public long getAdd_time() {
        return add_time;
    }

    public void setAdd_time(long add_time) {
        this.add_time = add_time;
    }

    public long getAdd_date() {
        return add_date;
    }

    public void setAdd_date(long add_date) {
        this.add_date = add_date;
    }

    public int getIs_favorite() {
        return is_favorite;
    }

    public void setIs_favorite(int is_favorite) {
        this.is_favorite = is_favorite;
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

    public String getAuthor_title() {
        return author_title;
    }

    public void setAuthor_title(String author_title) {
        this.author_title = author_title;
    }

    public String getAuthor_desp() {
        return author_desp;
    }

    public void setAuthor_desp(String author_desp) {
        this.author_desp = author_desp;
    }

    public String getAuthor_img() {
        return author_img;
    }

    public void setAuthor_img(String author_img) {
        this.author_img = author_img;
    }

    public List<MusicInfo> getLists() {
        return lists;
    }

    public void setLists(List<MusicInfo> lists) {
        this.lists = lists;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
