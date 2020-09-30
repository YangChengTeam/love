package com.yc.verbaltalk.skill.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.verbaltalk.R;
import com.yc.verbaltalk.chat.bean.confession.ConfessionDataBean;

import java.util.List;

/**
 * Created by sunshey on 2019/5/5.
 */

public class ConfessionAdapter extends BaseMultiItemQuickAdapter<ConfessionDataBean, BaseViewHolder> {

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public ConfessionAdapter(List<ConfessionDataBean> data) {
        super(data);

        addItemType(ConfessionDataBean.VIEW_TITLE, R.layout.recycler_view_item_title_t2_view);
        addItemType(ConfessionDataBean.VIEW_ITEM, R.layout.recycler_view_item_confession);
    }

    @Override
    protected void convert(BaseViewHolder helper, ConfessionDataBean item) {
        if (item != null) {
            switch (item.itemType) {
                case ConfessionDataBean.VIEW_TITLE:
                    break;

                case ConfessionDataBean.VIEW_ITEM:


                    ImageView ivIcon = helper.getView(R.id.iv_zb_thumb);

                    helper.setText(R.id.tv_zb_title, item.title);

                    helper.setText(R.id.tv_zb_des, item.desp);
                    helper.setText(R.id.tv_use_count, item.build_num);

                    String ivSrc = item.small_img;
                    Glide.with(mContext).asBitmap().load(ivSrc).apply(new RequestOptions().error(R.mipmap.acts_default).placeholder(R.mipmap.acts_default)).into(ivIcon);
                    break;
            }
        }
    }

}