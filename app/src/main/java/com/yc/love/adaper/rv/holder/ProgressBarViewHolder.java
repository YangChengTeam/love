package com.yc.love.adaper.rv.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yc.love.R;




public class ProgressBarViewHolder extends RecyclerView.ViewHolder {

    public final ProgressBar pb;
    private final TextView tvDes;

    public ProgressBarViewHolder(View itemView) {
        super(itemView);
        pb = itemView.findViewById(R.id.item_holder_pb_pb);
        tvDes = itemView.findViewById(R.id.item_holder_pb_tv);
    }

    public void removePbChangDes(String des){
        tvDes.setText(des);
        pb.setVisibility(View.GONE);
    }

}