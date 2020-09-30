package com.yc.verbaltalk.index.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import com.yc.verbaltalk.chat.bean.LoveHealDetBean;
import com.yc.verbaltalk.chat.bean.LoveHealDetBeanWrapper;

import java.util.List;

/**
 * Created by suns  on 2020/9/1 10:44.
 */
public class SmartChatItem implements MultiItemEntity {
    private int type;


    public static final int CHAT_ITEM_SELF = 0;
    public static final int CHAT_ITEM_VERBAL = 1;
    private String content;
    @JSONField(name = "ai")
    private List<AIItem> aiItems;

    private LoveHealDetBeanWrapper dialogue;


    public SmartChatItem() {
    }

    public SmartChatItem(int type, String content) {
        this.type = type;
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<AIItem> getAiItems() {
        return aiItems;
    }

    public void setAiItems(List<AIItem> aiItems) {
        this.aiItems = aiItems;
    }

    public LoveHealDetBeanWrapper getDialogue() {
        return dialogue;
    }

    public void setDialogue(LoveHealDetBeanWrapper dialogue) {
        this.dialogue = dialogue;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public int getItemType() {
        return type;
    }


}
