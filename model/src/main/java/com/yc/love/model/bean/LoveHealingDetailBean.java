package com.yc.love.model.bean;

import java.io.Serializable;

/**
 * Created by mayn on 2019/5/11.
 */

public class LoveHealingDetailBean implements Serializable {
    /**
     * ans_sex : 1
     * content : 别让我看见你
     * id : 4
     * lovewords_id : 2
     */

    public String ans_sex;
    public String content;
    public int id;
    public int lovewords_id;

    public LoveHealingDetailBean(String ans_sex) {
        this.ans_sex = ans_sex;
    }

    public LoveHealingDetailBean() {
    }

    @Override
    public String toString() {
        return "LoveHealingDetailBean{" +
                "ans_sex='" + ans_sex + '\'' +
                ", content='" + content + '\'' +
                ", id=" + id +
                ", lovewords_id=" + lovewords_id +
                '}';
    }
}
