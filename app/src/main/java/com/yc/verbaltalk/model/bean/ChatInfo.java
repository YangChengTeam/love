package com.yc.verbaltalk.model.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.music.player.lib.bean.MusicInfo;
import com.yc.verbaltalk.model.AudioItemInfo;

/**
 * Created by suns  on 2019/11/18 08:39.
 */
public class ChatInfo implements MultiItemEntity {

    public static final int ITEM_TYPE_RECOMMEND = 1;

    public static final int ITEM_TYPE_COURSE = 2;

    public static final int ITEM_TYPE_DIVIDER = 3;


    public int type;

    public String category_title;

    public MusicInfo musicInfo;

    public CourseInfo courseInfo;

    public boolean isShowCateTitle;

    public boolean isShowDivider = true;

    public boolean isShowCourseCategory;

    public ChatInfo(int type) {
        this.type = type;
    }

    public ChatInfo() {
    }

    @Override
    public int getItemType() {
        return type;
    }
}
