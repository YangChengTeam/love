package com.yc.love.adaper.rv.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yc.love.R;
import com.yc.love.adaper.rv.base.BaseEmptyViewHolder;
import com.yc.love.adaper.rv.base.RecyclerViewItemListener;
import com.yc.love.model.bean.ExampleTsListBean;


public class EmptyViewHolder extends BaseEmptyViewHolder {

    private String des;

    public EmptyViewHolder(Context context, ViewGroup root, String des) {
        super(context, root, R.layout.recycler_view_item_empty_view);
        this.des = des;

        initView();
    }

    protected void initView() {
        ImageView ivIcon = itemView.findViewById(R.id.empty_view_iv_icon);
        TextView tvDes = itemView.findViewById(R.id.empty_view_tv_des);

        if (!TextUtils.isEmpty(des)) {
            tvDes.setText(des);
        }
    }
}