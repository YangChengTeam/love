package com.yc.verbaltalk.chat.adapter;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.verbaltalk.R;
import com.yc.verbaltalk.chat.bean.LoveHealingBean;

import java.util.List;


/**
 * Created by Administrator on 2017/9/12.
 */

public class LoveHealingAdapter extends BaseMultiItemQuickAdapter<LoveHealingBean, BaseViewHolder> {


    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public LoveHealingAdapter(List<LoveHealingBean> data) {
        super(data);
        addItemType(LoveHealingBean.VIEW_TITLE, R.layout.recycler_view_item_title_love_healing);
        addItemType(LoveHealingBean.VIEW_ITEM_ITEM, R.layout.recycler_view_item_love_healing_item_title);
        addItemType(LoveHealingBean.VIEW_ITEM, R.layout.recycler_view_item_love_healing);
    }

    @Override
    protected void convert(BaseViewHolder helper, LoveHealingBean item) {
        if (item != null) {
            switch (item.type) {
                case LoveHealingBean.VIEW_TITLE:
                    break;
                case LoveHealingBean.VIEW_ITEM_ITEM:
                    helper.setText(R.id.item_love_healing_item_title_tv_name, item.chat_name);
                    break;
                case LoveHealingBean.VIEW_ITEM:
                    helper.setText(R.id.item_love_healing_tv_name, item.chat_name);
                    break;
            }
        }
    }


}