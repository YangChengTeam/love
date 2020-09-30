package com.yc.verbaltalk.index.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import androidx.annotation.Nullable;


/**
 * Created by suns  on 2020/9/1 17:18.
 */
public class AIItem implements MultiItemEntity {

    @JSONField(
            name = "value")
    @Nullable
    private String content;
    private int type;
    private int is_collect;
    private int is_favour;
    @Nullable
    private String id;
    public static final int TYPE_FROM_ME = 1;
    public static final int TYPE_FROM_OTHER = 2;


    @Nullable
    public final String getContent() {
        return this.content;
    }

    public final void setContent(@Nullable String var1) {
        this.content = var1;
    }

    public final int is_collect() {
        return this.is_collect;
    }

    public final void set_collect(int var1) {
        this.is_collect = var1;
    }

    public final int is_favour() {
        return this.is_favour;
    }

    public final void set_favour(int var1) {
        this.is_favour = var1;
    }

    @Nullable
    public final String getId() {
        return this.id;
    }

    public final void setId(@Nullable String var1) {
        this.id = var1;
    }

    public int getItemType() {
        return this.type;
    }

    public AIItem() {
    }

    public AIItem(@Nullable String content, int type) {
        this.type = type;
        this.content = content;
    }
}
