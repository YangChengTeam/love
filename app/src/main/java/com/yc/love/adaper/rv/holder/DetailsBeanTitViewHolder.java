package com.yc.love.adaper.rv.holder;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yc.love.R;
import com.yc.love.adaper.rv.base.RecyclerViewItemListener;
import com.yc.love.model.bean.LoveHealDetDetailsBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class DetailsBeanTitViewHolder extends BaseViewHolder<LoveHealDetDetailsBean> {

    private final Context context;

    public DetailsBeanTitViewHolder(Context context, RecyclerViewItemListener listener, ViewGroup parent) {
        super(context, parent, R.layout.recycler_view_item_details_bean_tit, listener);   //一个类对应一个布局文件
        this.context = context;
    }

    @Override
    public void bindData(final LoveHealDetDetailsBean detailsBean) {
        ImageView ivSex = itemView.findViewById(R.id.item_details_bean_titiv_sex);
//        TextView tvSex = itemView.findViewById(R.id.item_details_bean_titiv_tv_sex);
        TextView tvName = itemView.findViewById(R.id.item_details_bean_tittv_name);

        tvName.setText(detailsBean.content);
        String ansSex = detailsBean.ans_sex;
        if (!TextUtils.isEmpty(ansSex)) {
//            tvSex.setText("女:");
            ivSex.setImageDrawable(context.getResources().getDrawable(R.mipmap.icon_dialogue_women));

            /*if ("1".equals(ansSex)) { //1男2女0bi'a
                ivSex.setImageDrawable(context.getResources().getDrawable(R.mipmap.icon_dialogue_men));
            } else if ("2".equals(ansSex)) {
                ivSex.setImageDrawable(context.getResources().getDrawable(R.mipmap.icon_dialogue_women));
            } else {
                ivSex.setImageDrawable(context.getResources().getDrawable(R.mipmap.icon_dialogue_nothing));
            }*/
        }
    }


}