package com.yc.love.adaper.rv.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.yc.love.R;


/**
 * Created by Administrator on 2017/9/12.
 */

public class ProgressBarViewHolder extends RecyclerView.ViewHolder {

    public final ProgressBar pb;

    public ProgressBarViewHolder(View itemView) {
        super(itemView);
        pb =  itemView.findViewById(R.id.rv_pb_load_more);
    }

}