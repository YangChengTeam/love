package com.yc.verbaltalk.index.adapter;

import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.verbaltalk.R;
import com.yc.verbaltalk.chat.bean.LoveHealDetDetailsBean;

import java.util.List;

import androidx.annotation.NonNull;

/**
 * Created by sunshey on 2019/5/5.
 * 话术详情适配器
 */

public class IndexVerbalDetAdapter extends BaseMultiItemQuickAdapter<LoveHealDetDetailsBean, BaseViewHolder> {


    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public IndexVerbalDetAdapter(List<LoveHealDetDetailsBean> data) {
        super(data);
        addItemType(LoveHealDetDetailsBean.VIEW_TITLE, R.layout.recycler_view_item_details_bean_tit);
        addItemType(LoveHealDetDetailsBean.VIEW_ITEM, R.layout.recycler_view_item_details_bean);
    }


    @Override
    protected void convert(@NonNull BaseViewHolder helper, LoveHealDetDetailsBean item) {
        if (item != null) {
            String ansSex = item.ans_sex;

            switch (item.type) {
                case LoveHealDetDetailsBean.VIEW_TITLE:
                    helper.setText(R.id.item_details_bean_tittv_name, item.content);

                    if (!TextUtils.isEmpty(ansSex)) {
                        helper.setImageDrawable(R.id.item_details_bean_titiv_sex, mContext.getResources().getDrawable(R.mipmap.icon_dialogue_women));
                    }

                    break;
                case LoveHealDetDetailsBean.VIEW_ITEM:
                    if (TextUtils.isEmpty(item.content)) {
                        helper.itemView.setVisibility(View.GONE);
                    } else {
                        helper.itemView.setVisibility(View.VISIBLE);
                    }
                    helper.setText(R.id.item_details_bean_tv_name, item.content);
                    if (!TextUtils.isEmpty(ansSex)) {

                        if ("1".equals(ansSex)) { //1男2女0bi'a
                            helper.setImageDrawable(R.id.item_details_bean_iv_sex, mContext.getResources().getDrawable(R.mipmap.icon_dialogue_men));
                        } else if ("2".equals(ansSex)) {
                            helper.setImageDrawable(R.id.item_details_bean_iv_sex, mContext.getResources().getDrawable(R.mipmap.icon_dialogue_women));
                        } else {
                            helper.setImageDrawable(R.id.item_details_bean_iv_sex, mContext.getResources().getDrawable(R.mipmap.icon_dialogue_nothing));

                        }
                    }
                    helper.addOnClickListener(R.id.item_details_bean_iv_copy);

                    break;
            }
        }
    }

}