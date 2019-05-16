package com.yc.love.adaper.rv.holder;

import android.content.Context;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yc.love.R;
import com.yc.love.adaper.rv.base.RecyclerViewItemListener;
import com.yc.love.model.bean.LoveHealBean;
import com.yc.love.model.bean.MainT3Bean;
import com.yc.love.ui.view.CropSquareTransformation;


public class MainT3ItemViewHolder extends BaseViewHolder<MainT3Bean> {

    private final Context context;

    public MainT3ItemViewHolder(Context context, RecyclerViewItemListener listener, ViewGroup parent) {
        super(context, parent, R.layout.recycler_view_item_t3item, listener);   //一个类对应一个布局文件
        this.context=context;
    }

    @Override
    public void bindData(MainT3Bean mainT3Bean) {

        ImageView imageView = itemView.findViewById(R.id.item_t3item_iv);
        TextView tvTitle= itemView.findViewById(R.id.item_t3item_tv_title);
        TextView tvDes= itemView.findViewById(R.id.item_t3item_tv_des);
        tvTitle.setText(mainT3Bean.name);
        tvDes.setText(mainT3Bean.desp);
//        imageView.setImageResource(mainT3Bean.imgResId);
        String image=mainT3Bean.image;
        if(TextUtils.isEmpty(image)){
            image="image";
        }
        Picasso.with(context).load(image).transform(new CropSquareTransformation()).error(R.mipmap.main_bg_t3_placeholder).placeholder(R.mipmap.main_bg_t3_placeholder).into(imageView);

    }
}