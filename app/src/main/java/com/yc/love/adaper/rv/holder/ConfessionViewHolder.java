package com.yc.love.adaper.rv.holder;

import android.content.Context;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yc.love.R;
import com.yc.love.adaper.rv.base.RecyclerViewItemListener;
import com.yc.love.model.bean.MainT2Bean;
import com.yc.love.model.bean.confession.ConfessionDataBean;


public class ConfessionViewHolder extends BaseViewHolder<ConfessionDataBean> {

    private final Context context;

    public ConfessionViewHolder(Context context, RecyclerViewItemListener listener, ViewGroup parent) {
        super(context, parent, R.layout.recycler_view_item_confession, listener);   //一个类对应一个布局文件
        this.context = context;
    }

    @Override
    public void bindData(ConfessionDataBean confessionDataBean) {

//        TextView tvName = itemView.findViewById(R.id.item_main_t2_tv_name);
//        tvName.setText(mainT2Bean.post_title);

        TextView tvTitle = itemView.findViewById(R.id.tv_zb_title);
//        TextView tvName = itemView.findViewById(R.id.tv_zb_type);
        TextView tvDesp = itemView.findViewById(R.id.tv_zb_des);
        TextView tvUseCount = itemView.findViewById(R.id.tv_use_count);
        ImageView ivIcon = itemView.findViewById(R.id.iv_zb_thumb);

        tvTitle.setText(confessionDataBean.title);
//        tvName.setText(confessionDataBean.name);
        tvDesp.setText(confessionDataBean.desp);
        tvUseCount.setText(confessionDataBean.build_num);

        String ivSrc = confessionDataBean.small_img;
        if (TextUtils.isEmpty(ivSrc)) {
            ivSrc = "ivSrc";
        }
        Picasso.with(context).load(ivSrc).error(R.mipmap.acts_default).placeholder(R.mipmap.acts_default).into(ivIcon);

    }
}