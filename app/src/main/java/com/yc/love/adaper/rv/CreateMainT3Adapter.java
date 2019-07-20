package com.yc.love.adaper.rv;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.love.R;
import com.yc.love.model.bean.MainT3Bean;

import java.util.List;

/**
 * Created by mayn on 2019/4/30.
 */

public class CreateMainT3Adapter extends BaseMultiItemQuickAdapter<MainT3Bean, BaseViewHolder> {

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public CreateMainT3Adapter(List<MainT3Bean> data) {
        super(data);
        addItemType(MainT3Bean.LOVE_HEAL_TYPE_TITLE, R.layout.recycler_view_item_t3title);
        addItemType(MainT3Bean.LOVE_HEAL_TYPE_ITEM_TITLE, R.layout.recycler_view_item_t3item_title);
        addItemType(MainT3Bean.LOVE_HEAL_TYPE_ITEM, R.layout.recycler_view_item_t3item);
        addItemType(MainT3Bean.LOVE_HEAL_TYPE_ITEM_LOCALITY, R.layout.recycler_view_item_t3item);
    }

    @Override
    protected void convert(BaseViewHolder helper, MainT3Bean item) {
        if (item != null) {
            switch (item.type) {
                case MainT3Bean.LOVE_HEAL_TYPE_TITLE:

                    helper.addOnClickListener(R.id.item_t3title_iv_title)
                            .addOnClickListener(R.id.item_t3title_tv_icon_01)
                            .addOnClickListener(R.id.item_t3title_tv_icon_02)
                            .addOnClickListener(R.id.item_t3title_tv_icon_03)
                            .addOnClickListener(R.id.item_t3title_tv_icon_04)
                            .addOnClickListener(R.id.item_t3title_tv_icon_05)
                            .addOnClickListener(R.id.iv_practice_teach)
                            .addOnClickListener(R.id.iv_practice_love);

                    break;
                case MainT3Bean.LOVE_HEAL_TYPE_ITEM_TITLE:

                    helper.setText(R.id.item_t3item_title_tv_name, item.titleName);

                    break;
                case MainT3Bean.LOVE_HEAL_TYPE_ITEM:

                    ImageView imageView = helper.getView(R.id.item_t3item_iv);

                    helper.setText(R.id.item_t3item_tv_title, item.name)
                            .setText(R.id.item_t3item_tv_des, item.desp);

                    String image = item.image;

                    //设置图片圆角角度
                    RoundedCorners roundedCorners = new RoundedCorners(6);
                    //通过RequestOptions扩展功能,override:采样率,因为ImageView就这么大,可以压缩图片,降低内存消耗
                    RequestOptions options = RequestOptions.bitmapTransform(roundedCorners).override(300, 300);

                    Glide.with(mContext).asBitmap().load(image).apply(options.diskCacheStrategy(DiskCacheStrategy.DATA).placeholder(R.mipmap.main_bg_t3_placeholder)
                            .error(R.mipmap.main_bg_t3_placeholder).dontAnimate()).thumbnail(0.1f).into(imageView);

                    break;
                case MainT3Bean.LOVE_HEAL_TYPE_ITEM_LOCALITY:


                    helper.setText(R.id.item_t3item_tv_title, item.name)
                            .setText(R.id.item_t3item_tv_des, item.desp);
                    int imageResourceld = item.imageResourceld;
                    if (imageResourceld > 0) {
                        Drawable imageResourceldId = null;
                        try {
                            imageResourceldId = mContext.getResources().getDrawable(imageResourceld);
                        } catch (Exception e) {
                        }
                        if (imageResourceldId != null) {
                            helper.setImageDrawable(R.id.item_t3item_iv, imageResourceldId);
                        }
                    }
                    break;
            }
        }
    }


    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {

        GridLayoutManager gridLayoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                MainT3Bean mainT3Bean = mData.get(position);
                int spansize = 1;
                switch (mainT3Bean.type) {
                    case MainT3Bean.LOVE_HEAL_TYPE_TITLE:
                        spansize = 2;  //占据2列
                        break;
                    case MainT3Bean.LOVE_HEAL_TYPE_ITEM_TITLE:
                        spansize = 2;  //占据2列
                        break;
                }
                return spansize;
            }
        });
    }


}
