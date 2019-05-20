package com.yc.love.model.bean;

/**
 * Created by mayn on 2019/5/10.
 */

public class LoveHealDetDetailsBean {
    public int type=1;
    public int id;
    public int size;
    public int dialogue_id;
    public String content;
    public String ans_sex;

    public LoveHealDetDetailsBean() {
    }

    public LoveHealDetDetailsBean(int type,int id, String content, String ans_sex) {
        this.type = type;
        this.id = id;
        this.content = content;
        this.ans_sex = ans_sex;
    }

    @Override
    public String toString() {
        return "DetailsBean{" +
                "id=" + id +
                ", dialogue_id=" + dialogue_id +
                ", content='" + content + '\'' +
                ", ans_sex='" + ans_sex + '\'' +
                '}';
    }
}
