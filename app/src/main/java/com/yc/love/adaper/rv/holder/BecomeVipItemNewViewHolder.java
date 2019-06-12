package com.yc.love.adaper.rv.holder;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yc.love.R;
import com.yc.love.adaper.rv.base.RecyclerViewItemListener;
import com.yc.love.model.bean.BecomeVipBean;


public class BecomeVipItemNewViewHolder extends BaseViewHolder<BecomeVipBean> {

    public BecomeVipItemNewViewHolder(Context context, RecyclerViewItemListener listener, ViewGroup parent) {
        super(context, parent, R.layout.recycler_view_item_become_vip_new, listener);   //一个类对应一个布局文件
    }

    @Override
    public void bindData(BecomeVipBean becomeVipBean) {
        ImageView ivIcon = itemView.findViewById(R.id.item_become_vip_iv_icon);
        TextView tvName = itemView.findViewById(R.id.item_become_vip_tv_name);
        TextView tvDes = itemView.findViewById(R.id.item_become_vip_tv_des);
        ivIcon.setImageResource(becomeVipBean.imgResId);
        tvName.setText(becomeVipBean.name);
        tvDes.setText(becomeVipBean.subName);
    }
}