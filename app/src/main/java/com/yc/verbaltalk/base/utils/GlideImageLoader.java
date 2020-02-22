package com.yc.verbaltalk.base.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yc.verbaltalk.base.view.RoundCornerImg;
import com.youth.banner.loader.ImageLoader;

/**
 * Created by suns  on 2019/9/4 15:08.
 */
public class GlideImageLoader extends ImageLoader {
    private boolean mIsCorner;

    public GlideImageLoader(boolean isRoundCorner) {
        this.mIsCorner = isRoundCorner;
    }

    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        //Glide 加载图片简单用法
        //设置图片圆角角度
//        RoundedCorners roundedCorners = new RoundedCorners(30);
//        //通过RequestOptions扩展功能,override:采样率,因为ImageView就这么大,可以压缩图片,降低内存消耗
//        RequestOptions options = RequestOptions.bitmapTransform(roundedCorners);
        Glide.with(context).load(path).into(imageView);
    }

    @Override
    public ImageView createImageView(Context context) {
        RoundCornerImg roundCornerImg = new RoundCornerImg(context, null);
        if (mIsCorner) {

            roundCornerImg.setCorner(20);
        } else {
            roundCornerImg.setCorner(0);
        }
        return roundCornerImg;
    }
}
