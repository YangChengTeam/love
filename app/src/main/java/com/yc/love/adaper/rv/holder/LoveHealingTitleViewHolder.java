package com.yc.love.adaper.rv.holder;

import android.content.Context;
import android.util.Log;
import android.view.ViewGroup;

import com.yc.love.R;
import com.yc.love.adaper.rv.base.RecyclerViewItemListener;
import com.yc.love.model.bean.LoveHealingBean;
import com.yc.love.model.bean.MainT2Bean;

/**
 * Created by mayn on 2019/4/26.
 */

public class LoveHealingTitleViewHolder extends BaseViewHolder<LoveHealingBean> {

    private final Context context;

    public LoveHealingTitleViewHolder(Context context, RecyclerViewItemListener listener, ViewGroup parent) {
        super(context, parent, R.layout.recycler_view_item_title_love_healing, listener);   //一个类对应一个布局文件
        this.context = context;
    }

    @Override
    public void bindData(LoveHealingBean loveHealingBean) {

    }



}
