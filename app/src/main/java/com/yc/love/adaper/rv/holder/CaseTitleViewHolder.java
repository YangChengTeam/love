package com.yc.love.adaper.rv.holder;

import android.content.Context;
import android.view.ViewGroup;

import com.yc.love.R;
import com.yc.love.adaper.rv.base.RecyclerViewItemListener;
import com.yc.love.model.bean.MainT2Bean;

/**
 * Created by mayn on 2019/4/26.
 */

public class CaseTitleViewHolder extends BaseViewHolder<MainT2Bean> {

    private final Context context;

    public CaseTitleViewHolder(Context context, RecyclerViewItemListener listener, ViewGroup parent) {
        super(context, parent, R.layout.recycler_view_item_title_case, listener);   //一个类对应一个布局文件
        this.context = context;
    }

    @Override
    public void bindData(MainT2Bean mainT2Bean) {

    }
}
