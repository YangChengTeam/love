package com.yc.love.adaper.rv.holder;

import android.content.Context;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.yc.love.R;
import com.yc.love.adaper.rv.base.RecyclerViewItemListener;
import com.yc.love.model.bean.ExampListsBean;


public class CollectExampleItemViewHolder extends BaseViewHolder<ExampListsBean> {

    private final Context context;

    public CollectExampleItemViewHolder(Context context, RecyclerViewItemListener listener, ViewGroup parent) {
        super(context, parent, R.layout.recycler_view_item_collect_example, listener);   //一个类对应一个布局文件
        this.context = context;
    }

    @Override
    public void bindData(ExampListsBean exampListsBean) {
        TextView tvName = itemView.findViewById(R.id.item_collect_example_tv_name);
        TextView tvDes = itemView.findViewById(R.id.item_collect_example_tv_des);
        ImageView ivIcon = itemView.findViewById(R.id.item_collect_example_iv_icon);
        tvName.setText(exampListsBean.post_title);
        tvDes.setText(String.valueOf(exampListsBean.feeluseful).concat("人觉得有用"));

        String image = exampListsBean.image;
        if (TextUtils.isEmpty(image)) {
            image = "image";
        }

        //设置图片圆角角度
        RoundedCorners roundedCorners = new RoundedCorners(6);
        //通过RequestOptions扩展功能,override:采样率,因为ImageView就这么大,可以压缩图片,降低内存消耗
        RequestOptions options = RequestOptions.bitmapTransform(roundedCorners).override(300, 300);
        Glide.with(context).asBitmap().load(image).apply(options.error(R.mipmap.main_bg_t3_placeholder).placeholder(R.mipmap.main_bg_t3_placeholder)).into(ivIcon);
    }
}