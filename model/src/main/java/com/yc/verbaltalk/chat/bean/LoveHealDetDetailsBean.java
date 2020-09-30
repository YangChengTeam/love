package com.yc.verbaltalk.chat.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by sunshey on 2019/5/10.
 */

@Entity
public class LoveHealDetDetailsBean implements MultiItemEntity {
    @Id(autoincrement = true)
    public Long id;
    @Transient
    public int type = 1;

    @Index
    @JSONField(name = "id")
    public int love_id;
    @Transient
    public int size;
    public int dialogue_id;

    public String content;

    public String ans_sex;

    public String s_value;
    public static final int VIEW_TITLE = 0;
    public static final int VIEW_ITEM = 1;
    public static final int VIEW_PROG = 2;
    /**
     * 保存时间
     */
    public long saveTime;
    
    public String title;


    public LoveHealDetDetailsBean() {
    }

    public LoveHealDetDetailsBean(int type, int id, String content, String ans_sex) {
        this.type = type;
        this.love_id = id;
        this.content = content;
        this.ans_sex = ans_sex;
    }

    @Generated(hash = 1612580505)
    public LoveHealDetDetailsBean(Long id, int love_id, int dialogue_id, String content,
            String ans_sex, String s_value, long saveTime, String title) {
        this.id = id;
        this.love_id = love_id;
        this.dialogue_id = dialogue_id;
        this.content = content;
        this.ans_sex = ans_sex;
        this.s_value = s_value;
        this.saveTime = saveTime;
        this.title = title;
    }

    @Override
    public String toString() {
        return "DetailsBean{" +
                "love_id=" + love_id +
                ", dialogue_id=" + dialogue_id +
                ", content='" + content + '\'' +
                ", ans_sex='" + ans_sex + '\'' +
                '}';
    }

    @Override
    public int getItemType() {
        return type;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getLove_id() {
        return this.love_id;
    }

    public void setLove_id(int love_id) {
        this.love_id = love_id;
    }

    public int getDialogue_id() {
        return this.dialogue_id;
    }

    public void setDialogue_id(int dialogue_id) {
        this.dialogue_id = dialogue_id;
    }

    public String getAns_sex() {
        return this.ans_sex;
    }

    public void setAns_sex(String ans_sex) {
        this.ans_sex = ans_sex;
    }

    public long getSaveTime() {
        return this.saveTime;
    }

    public void setSaveTime(long saveTime) {
        this.saveTime = saveTime;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getS_value() {
        return this.s_value;
    }

    public void setS_value(String s_value) {
        this.s_value = s_value;
    }
}
