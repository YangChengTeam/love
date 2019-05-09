package com.yc.love.adaper.rv.holder;

import android.content.Context;
import android.view.ViewGroup;

import com.yc.love.R;
import com.yc.love.adaper.rv.base.RecyclerViewItemListener;
import com.yc.love.model.bean.LoveHealDetBean;


public class LoveHealDetVipHolder extends BaseViewHolder<LoveHealDetBean> {

    public LoveHealDetVipHolder(Context context, RecyclerViewItemListener listener, ViewGroup parent) {
        super(context, parent, R.layout.recycler_view_item_love_heal_det_vip, listener);   //一个类对应一个布局文件
    }

    @Override
    public void bindData(LoveHealDetBean loveHealDetBean) {


//        TextView tvName = itemView.findViewById(R.id.item_string_bean_tv_name);
//        tvName.setText(mainT2Bean.name);

    }
}