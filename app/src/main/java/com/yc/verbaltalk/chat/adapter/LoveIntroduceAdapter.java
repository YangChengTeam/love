package com.yc.verbaltalk.chat.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.verbaltalk.R;
import com.yc.verbaltalk.chat.bean.ExampListsBean;

import java.util.List;

import androidx.annotation.Nullable;

/**
 * Created by wanglin  on 2019/7/1 09:06.
 */
public class LoveIntroduceAdapter extends BaseQuickAdapter<ExampListsBean, BaseViewHolder> {

    public LoveIntroduceAdapter(@Nullable List<ExampListsBean> data) {
        super(R.layout.recycler_view_item_love_intro, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ExampListsBean item) {

        ImageView ivIcon = helper.getView(R.id.love_intro_iv_icon);
        helper.setText(R.id.love_intro_tv_name, item.post_title);
        helper.setText(R.id.love_intro_tv_des, String.valueOf(item.feeluseful).concat("人觉得有用"));

        String image = item.image;

        //设置图片圆角角度
//        RoundedCorners roundedCorners = new RoundedCorners(6);
//        //通过RequestOptions扩展功能,override:采样率,因为ImageView就这么大,可以压缩图片,降低内存消耗
//        RequestOptions options = RequestOptions.bitmapTransform(roundedCorners).override(300, 300);
        Glide.with(mContext).asBitmap().load(image).apply(new RequestOptions().error(R.mipmap.main_bg_t3_placeholder).placeholder(R.mipmap.main_bg_t3_placeholder)
                .diskCacheStrategy(DiskCacheStrategy.DATA)).into(ivIcon);
    }
}
