package com.yc.love.adaper.rv.holder;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yc.love.R;
import com.yc.love.adaper.rv.base.RecyclerViewItemListener;
import com.yc.love.model.bean.MainT2Bean;


public class MainT2ViewHolder extends BaseViewHolder<MainT2Bean> {

    public MainT2ViewHolder(Context context, RecyclerViewItemListener listener, ViewGroup parent) {
        super(context, parent, R.layout.recycler_view_item_main_t2, listener);   //一个类对应一个布局文件
    }

    @Override
    public void bindData(MainT2Bean mainT2Bean) {

        TextView tvName = itemView.findViewById(R.id.item_main_t2_tv_name);
        tvName.setText(mainT2Bean.post_title);

    }
}