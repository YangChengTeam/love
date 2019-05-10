package com.yc.love.adaper.rv.holder;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yc.love.R;
import com.yc.love.adaper.rv.base.RecyclerViewItemListener;
import com.yc.love.model.bean.LoveByStagesBean;
import com.yc.love.model.bean.StringBean;


public class LoveByStagesViewHolder extends BaseViewHolder<LoveByStagesBean> {

    public LoveByStagesViewHolder(Context context, RecyclerViewItemListener listener, ViewGroup parent) {
        super(context, parent, R.layout.recycler_view_item_love_by_stages, listener);   //一个类对应一个布局文件
    }

    @Override
    public void bindData(LoveByStagesBean loveByStagesBean) {

        TextView tvName = itemView.findViewById(R.id.item_love_by_stages_tv_name);
        TextView tvDes = itemView.findViewById(R.id.item_love_by_stages_tv_des);
        tvName.setText(loveByStagesBean.post_title);
        tvDes.setText(String.valueOf(loveByStagesBean.feeluseful).concat("人觉得有用"));

    }
}