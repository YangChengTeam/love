package com.yc.love.adaper.rv.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yc.love.R;
import com.yc.love.adaper.rv.base.RecyclerViewItemListener;
import com.yc.love.adaper.rv.base.RecyclerViewTimeoutListener;
import com.yc.love.model.bean.ExampListsBean;
import com.yc.love.ui.view.CropSquareTransformation;


public class TimeoutItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public RecyclerViewTimeoutListener myAdapterListener;
    private String des;

    public TimeoutItemHolder(Context context, ViewGroup root, String des, RecyclerViewTimeoutListener listener) {
        super(LayoutInflater.from(context).inflate(R.layout.recycler_view_item_timeout, root, false));//super(View view)
        this.myAdapterListener =listener;
        itemView.setOnClickListener(this);
        this.des = des;
        initView();
    }

    private void initView() {
        ImageView ivIcon = itemView.findViewById(R.id.item_timeout_iv_icon);
        TextView tvDes = itemView.findViewById(R.id.item_timeout_tv_des);

        if (!TextUtils.isEmpty(des)) {
            tvDes.setText(des);
        }
    }


    @Override
    public void onClick(View v) {
        if(myAdapterListener !=null){
            myAdapterListener.onItemClick(getAdapterPosition());
        }
    }
}