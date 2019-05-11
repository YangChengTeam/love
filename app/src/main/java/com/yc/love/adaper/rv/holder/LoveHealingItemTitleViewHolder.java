package com.yc.love.adaper.rv.holder;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yc.love.R;
import com.yc.love.adaper.rv.base.RecyclerViewItemListener;
import com.yc.love.model.bean.LoveHealingBean;
import com.yc.love.model.bean.MainT3Bean;




public class LoveHealingItemTitleViewHolder extends BaseViewHolder<LoveHealingBean> {

    public LoveHealingItemTitleViewHolder(Context context, RecyclerViewItemListener listener, ViewGroup parent) {
        super(context, parent, R.layout.recycler_view_item_love_healing_item_title, listener);   //一个类对应一个布局文件
    }

    @Override
    public void bindData(LoveHealingBean loveHealingBean) {

        TextView tvName = itemView.findViewById(R.id.item_love_healing_item_title_tv_name);
        tvName.setText(loveHealingBean.chat_name);

    }
}