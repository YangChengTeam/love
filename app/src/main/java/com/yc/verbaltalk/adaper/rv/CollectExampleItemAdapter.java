package com.yc.verbaltalk.adaper.rv;

import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.verbaltalk.R;
import com.yc.verbaltalk.model.bean.ExampListsBean;

import java.util.List;

import androidx.annotation.Nullable;


public class CollectExampleItemAdapter extends BaseQuickAdapter<ExampListsBean, BaseViewHolder> {


    public CollectExampleItemAdapter(@Nullable List<ExampListsBean> data) {
        super(R.layout.recycler_view_item_collect_example, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ExampListsBean item) {
        int like_num = item.feeluseful;
        if (like_num < 50) {
            like_num = 50;
        }
        helper.setText(R.id.item_collect_example_tv_name, item.post_title)
                .setText(R.id.item_collect_example_tv_des, String.valueOf(like_num).concat("人觉得有用"));


        String image = item.image;
        if (TextUtils.isEmpty(image)) {
            image = "image";
        }

//        //设置图片圆角角度
//        RoundedCorners roundedCorners = new RoundedCorners(6);
//        //通过RequestOptions扩展功能,override:采样率,因为ImageView就这么大,可以压缩图片,降低内存消耗
//        RequestOptions options = RequestOptions.bitmapTransform(roundedCorners).override(300, 300);
        Glide.with(mContext).asBitmap().load(image).apply(new RequestOptions().error(R.mipmap.main_bg_t3_placeholder)
                .placeholder(R.mipmap.main_bg_t3_placeholder)).into((ImageView) helper.getView(R.id.item_collect_example_iv_icon));
    }
}