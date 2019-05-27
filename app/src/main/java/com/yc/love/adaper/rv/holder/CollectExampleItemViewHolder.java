package com.yc.love.adaper.rv.holder;

import android.content.Context;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yc.love.R;
import com.yc.love.adaper.rv.base.RecyclerViewItemListener;
import com.yc.love.model.bean.ExampListsBean;
import com.yc.love.model.bean.LoveHealingBean;
import com.yc.love.ui.view.CropSquareTransformation;


public class CollectExampleItemViewHolder extends BaseViewHolder<ExampListsBean> {

    private final Context context;

    public CollectExampleItemViewHolder(Context context, RecyclerViewItemListener listener, ViewGroup parent) {
        super(context, parent, R.layout.recycler_view_item_collect_example, listener);   //一个类对应一个布局文件
        this.context=context;
    }

    @Override
    public void bindData(ExampListsBean exampListsBean) {
        TextView tvName = itemView.findViewById(R.id.item_collect_example_tv_name);
        TextView tvDes = itemView.findViewById(R.id.item_collect_example_tv_des);
        ImageView ivIcon  = itemView.findViewById(R.id.item_collect_example_iv_icon);
        tvName.setText(exampListsBean.post_title);
        tvDes.setText(String.valueOf(exampListsBean.feeluseful).concat("人觉得有用"));

        String image=exampListsBean.image;
        if(TextUtils.isEmpty(image)){
            image="image";
        }
        Picasso.with(context).load(image).transform(new CropSquareTransformation()).error(R.mipmap.main_bg_t3_placeholder).placeholder(R.mipmap.main_bg_t3_placeholder).into(ivIcon);

    }
}