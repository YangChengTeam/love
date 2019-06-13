package com.yc.love.adaper.rv.holder;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yc.love.R;
import com.yc.love.adaper.rv.base.RecyclerViewItemListener;
import com.yc.love.model.bean.MainT3Bean;
import com.yc.love.ui.view.CropSquareTransformation;


public class MainT3ItemLocalityViewHolder extends BaseViewHolder<MainT3Bean> {

    private final Context context;

    public MainT3ItemLocalityViewHolder(Context context, RecyclerViewItemListener listener, ViewGroup parent) {
        super(context, parent, R.layout.recycler_view_item_t3item, listener);   //一个类对应一个布局文件
        this.context = context;
    }

    @Override
    public void bindData(MainT3Bean mainT3Bean) {

        ImageView imageView = itemView.findViewById(R.id.item_t3item_iv);
        TextView tvTitle = itemView.findViewById(R.id.item_t3item_tv_title);
        TextView tvDes = itemView.findViewById(R.id.item_t3item_tv_des);
        tvTitle.setText(mainT3Bean.name);
        tvDes.setText(mainT3Bean.desp);
//        imageView.setImageResource(mainT3Bean.imgResId);
//        Drawable imageResourceldId = mainT3Bean.imageResourceldId;

//        Log.d("mylog", "bindData: imageResourceldId " + imageResourceldId);
        int imageResourceld = mainT3Bean.imageResourceld;
        if (imageResourceld > 0) {
            Drawable imageResourceldId = null;
            try {
                imageResourceldId = context.getResources().getDrawable(imageResourceld);
            } catch (Exception e) {
            }
            if (imageResourceldId != null) {
                imageView.setImageDrawable(imageResourceldId);
            }
        }
    }
}