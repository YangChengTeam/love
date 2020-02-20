package com.yc.verbaltalk.base.holder;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yc.verbaltalk.R;

import androidx.recyclerview.widget.RecyclerView;


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