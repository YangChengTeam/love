package com.yc.love.adaper.rv.holder;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yc.love.R;
import com.yc.love.adaper.rv.base.RecyclerViewItemListener;
import com.yc.love.model.bean.MainT3Bean;


/**
 * Created by Administrator on 2017/9/12.
 */

public class MainT3ItemTitleViewHolder extends BaseViewHolder<MainT3Bean> {

    public MainT3ItemTitleViewHolder(Context context, RecyclerViewItemListener listener, ViewGroup parent) {
        super(context, parent, R.layout.recycler_view_item_t3item_title, listener);   //一个类对应一个布局文件
    }

    @Override
    public void bindData(MainT3Bean mainT3Bean) {

        TextView tvName = itemView.findViewById(R.id.item_t3item_title_tv_name);
        tvName.setText(mainT3Bean.name);

    }
}