package com.yc.verbaltalk.skill.adapter;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.verbaltalk.R;
import com.yc.verbaltalk.chat.bean.MainT2Bean;

import java.util.List;


/**
 * Created by Administrator on 2017/9/12.
 *
 */

public class ChatSkillItemAdapter extends BaseMultiItemQuickAdapter<MainT2Bean, BaseViewHolder> {


    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public ChatSkillItemAdapter(List<MainT2Bean> data) {
        super(data);
        addItemType(MainT2Bean.VIEW_ITEM, R.layout.recycler_view_item_main_t2);
        addItemType(MainT2Bean.VIEW_TITLE, R.layout.recycler_view_item_title_t2_view);
        addItemType(MainT2Bean.VIEW_TO_PAY_VIP, R.layout.item_title_view_main_to_pay_vip);
        addItemType(MainT2Bean.VIEW_VIP, R.layout.item_title_view_vip);
    }

    @Override
    protected void convert(BaseViewHolder helper, MainT2Bean item) {
        if (item != null) {
            switch (item.type) {
                case MainT2Bean.VIEW_ITEM:
                    helper.setText(R.id.item_main_t2_tv_name, item.post_title);
                    break;
                case MainT2Bean.VIEW_TITLE:
                    helper.setImageResource(R.id.roundCornerImg_banner, item.imgId);
                    helper.addOnClickListener(R.id.roundCornerImg_banner);
//                    helper.setText()
                    break;
                case MainT2Bean.VIEW_TO_PAY_VIP:
                    helper.setText(R.id.item_main_to_pay_vip_tv_name, item.post_title);
                    break;

                case MainT2Bean.VIEW_VIP:

                    break;

            }
        }
    }


}