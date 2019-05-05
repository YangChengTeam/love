package com.yc.love.adaper.rv.holder;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yc.love.R;
import com.yc.love.adaper.rv.base.RecyclerViewItemListener;
import com.yc.love.model.bean.LoveHealBean;
import com.yc.love.model.bean.StringBean;




public class LoveHealItemViewHolder extends BaseViewHolder<LoveHealBean> {

    public LoveHealItemViewHolder(Context context, RecyclerViewItemListener listener, ViewGroup parent) {
        super(context, parent, R.layout.recycler_view_item_love_heal, listener);   //一个类对应一个布局文件
    }

    @Override
    public void bindData(LoveHealBean loveHealBean) {

        TextView tvName = itemView.findViewById(R.id.item_love_heal_tv_name);
        tvName.setText(loveHealBean.name);

    }
}