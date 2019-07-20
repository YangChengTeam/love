package com.yc.love.adaper.rv.holder;

import android.content.Context;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yc.love.R;
import com.yc.love.adaper.rv.base.RecyclerViewItemListener;
import com.yc.love.model.bean.LoveHealBean;
import com.yc.love.model.bean.LoveHealingBean;


public class LoveHealingItemViewHolder extends BaseViewHolder<LoveHealingBean> {

    public LoveHealingItemViewHolder(Context context, RecyclerViewItemListener listener, ViewGroup parent) {
        super(context, parent, R.layout.recycler_view_item_love_healing, listener);   //一个类对应一个布局文件
    }

    @Override
    public void bindData(LoveHealingBean loveHealingBean) {
        TextView tvName = itemView.findViewById(R.id.item_love_healing_tv_name);
        tvName.setSingleLine(true);
        tvName.setEllipsize(TextUtils.TruncateAt.END);
        tvName.setText(loveHealingBean.chat_name);
    }
}