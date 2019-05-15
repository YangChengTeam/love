package com.yc.love.adaper.rv.holder;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.yc.love.R;
import com.yc.love.adaper.rv.base.RecyclerViewItemListener;
import com.yc.love.model.bean.MainT2Bean;
import com.yc.love.model.bean.StringBean;

/**
 * Created by mayn on 2019/4/26.
 */

public class VipViewHolder extends BaseViewHolder<MainT2Bean> {

    private final Context context;

    public VipViewHolder(Context context, RecyclerViewItemListener listener, ViewGroup parent) {
        super(context, parent, R.layout.item_title_view_vip, listener);   //一个类对应一个布局文件
        this.context = context;
    }

    @Override
    public void bindData(MainT2Bean mainT2Bean) {

    }
}
