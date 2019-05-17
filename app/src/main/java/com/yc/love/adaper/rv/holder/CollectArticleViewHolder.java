package com.yc.love.adaper.rv.holder;

import android.content.Context;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yc.love.R;
import com.yc.love.adaper.rv.base.RecyclerViewItemListener;
import com.yc.love.model.bean.ExampleTsListBean;
import com.yc.love.model.bean.LoveByStagesBean;
import com.yc.love.ui.view.CropSquareTransformation;


public class CollectArticleViewHolder extends BaseViewHolder<ExampleTsListBean> {

    private final Context context;

    public CollectArticleViewHolder(Context context, RecyclerViewItemListener listener, ViewGroup parent) {
        super(context, parent, R.layout.recycler_view_item_collect_article, listener);   //一个类对应一个布局文件
        this.context=context;
    }

    @Override
    public void bindData(ExampleTsListBean exampleTsListBean) {

        TextView tvName = itemView.findViewById(R.id.item_collect_article_tv_name);
        TextView tvDes = itemView.findViewById(R.id.item_collect_article_tv_des);
        tvName.setText(exampleTsListBean.post_title);
        tvDes.setText(String.valueOf(exampleTsListBean.feeluseful).concat("人觉得有用"));


    }
}