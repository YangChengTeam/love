package com.yc.love.adaper.rv.holder;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yc.love.R;
import com.yc.love.adaper.rv.base.RecyclerViewItemListener;
import com.yc.love.model.bean.BecomeVipBean;
import com.yc.love.model.bean.LoveHealingBean;



public class BecomeVipTailViewHolder extends BaseViewHolder<BecomeVipBean> {

    public BecomeVipTailViewHolder(Context context, RecyclerViewItemListener listener, ViewGroup parent) {
        super(context, parent, R.layout.recycler_view_item_become_vip_tail, listener);   //一个类对应一个布局文件
    }

    @Override
    public void bindData(BecomeVipBean becomeVipBean) {

//        TextView tvName = itemView.findViewById(R.id.item_love_healing_item_title_tv_name);
//        tvName.setText(loveHealingBean.name);

    }
}