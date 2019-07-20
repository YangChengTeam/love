package com.yc.love.adaper.rv.holder;

import android.content.Context;
import android.view.ViewGroup;

import com.yc.love.R;
import com.yc.love.adaper.rv.base.RecyclerViewItemListener;
import com.yc.love.model.bean.BecomeVipBean;


public class EndEmptyViewHolder extends BaseViewHolder<BecomeVipBean> {

    public EndEmptyViewHolder(Context context, RecyclerViewItemListener listener, ViewGroup parent) {
        super(context, parent, R.layout.recycler_view_item_end_empty, listener);   //一个类对应一个布局文件
    }

    @Override
    public void bindData(BecomeVipBean becomeVipBean) {

//        TextView tvName = itemView.findViewById(R.love_id.item_become_vip_title_tv_name);
//        tvName.setText(becomeVipBean.name);

    }
}