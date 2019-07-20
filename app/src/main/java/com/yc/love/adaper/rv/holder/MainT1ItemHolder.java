package com.yc.love.adaper.rv.holder;

import android.content.Context;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.yc.love.R;
import com.yc.love.adaper.rv.base.RecyclerViewItemListener;
import com.yc.love.model.bean.ExampListsBean;


public class MainT1ItemHolder extends BaseViewHolder<ExampListsBean> {

    private final Context context;

    public MainT1ItemHolder(Context context, RecyclerViewItemListener listener, ViewGroup parent) {
        super(context, parent, R.layout.recycler_view_item_main_t1, listener);   //一个类对应一个布局文件
        this.context = context;
    }

    @Override
    public void bindData(ExampListsBean exampListsBean) {
        TextView tvName = itemView.findViewById(R.id.item_main_t1_tv_name);
        TextView tvDes = itemView.findViewById(R.id.item_main_t1_tv_des);
        TextView tvLabel = itemView.findViewById(R.id.item_main_t1_tv_label);
        ImageView ivIcon = itemView.findViewById(R.id.item_main_t1_iv_icon);
        tvName.setText(exampListsBean.post_title);
        tvLabel.setText(exampListsBean.category_name);
        tvDes.setText(String.valueOf(exampListsBean.feeluseful).concat("人觉得有用"));

        String image = exampListsBean.image;
        if (TextUtils.isEmpty(image)) {
            image = "image";
        }

        Glide.with(context).asBitmap().load(image).apply(RequestOptions.circleCropTransform()
                .error(R.mipmap.main_bg_t3_placeholder).placeholder(R.mipmap.main_bg_t3_placeholder)).into(ivIcon);


    }
}