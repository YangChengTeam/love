package com.yc.love.adaper.rv.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;


public abstract class BaseEmptyViewHolder extends RecyclerView.ViewHolder {

    /*public EmptyViewHolder(View itemView) {
        super(itemView);
    }*/
    public BaseEmptyViewHolder(Context context, ViewGroup root, int layoutRes) {
        super(LayoutInflater.from(context).inflate(layoutRes, root, false));
    }

}