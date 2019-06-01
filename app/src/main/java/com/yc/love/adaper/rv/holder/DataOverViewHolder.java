package com.yc.love.adaper.rv.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yc.love.R;
import com.yc.love.adaper.rv.base.BaseEmptyViewHolder;


public class DataOverViewHolder extends BaseEmptyViewHolder {

    private String des;

    public DataOverViewHolder(Context context, ViewGroup root, String des) {
        super(context, root, R.layout.recycler_view_item_data_over);
        this.des = des;

        initView();
    }

    protected void initView() {
        TextView tvDes = itemView.findViewById(R.id.item_data_over_tv_des);

        if (!TextUtils.isEmpty(des)) {
            tvDes.setText(des);
        }
    }

}