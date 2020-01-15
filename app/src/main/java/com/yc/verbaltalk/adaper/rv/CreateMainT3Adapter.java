package com.yc.verbaltalk.adaper.rv;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.verbaltalk.R;
import com.yc.verbaltalk.model.bean.MainT3Bean;
import com.yc.verbaltalk.ui.frament.main.AudioActivity;
import com.yc.verbaltalk.utils.GlideImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by mayn on 2019/4/30.
 */

public class CreateMainT3Adapter extends BaseMultiItemQuickAdapter<MainT3Bean, BaseViewHolder> {

    private List<Integer> images;

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public CreateMainT3Adapter(List<MainT3Bean> data) {
        super(data);
        images = Arrays.asList(R.mipmap.main_bg_t3_title, R.mipmap.main_bg_t3_title_audio);

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


                    Banner banner = helper.getView(R.id.banner);
                    //设置banner样式
                    banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
                    //设置图片加载器
                    banner.setImageLoader(new GlideImageLoader(true));
                    //设置图片集合
                    banner.setImages(images);
                    //设置banner动画效果
                    banner.setBannerAnimation(Transformer.Accordion);

                    //设置自动轮播，默认为true
                    banner.isAutoPlay(true);
                    //设置轮播时间
                    banner.setDelayTime(1500);
                    //设置指示器位置（当banner模式中有指示器时）
                    banner.setIndicatorGravity(BannerConfig.CENTER);
                    //banner设置方法全部调用完毕时最后调用
                    banner.setOnBannerListener(position -> {
                        if (position == 0) {
//                            mContext.startActivity(new Intent(mContext, ExpressFragment.class));

                        } else if (position == 1) {
                            mContext.startActivity(new Intent(mContext, AudioActivity.class));
                        }
                    });
                    banner.start();

                    helper.addOnClickListener(R.id.item_t3title_iv_title)
                            .addOnClickListener(R.id.item_t3title_tv_icon_01)
                            .addOnClickListener(R.id.item_t3title_tv_icon_02)
                            .addOnClickListener(R.id.item_t3title_tv_icon_03)
                            .addOnClickListener(R.id.item_t3title_tv_icon_04)
                            .addOnClickListener(R.id.item_t3title_tv_icon_05);
//                            .addOnClickListener(R.id.iv_practice_teach)
//                            .addOnClickListener(R.id.iv_practice_love);

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
