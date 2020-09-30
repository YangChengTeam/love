package com.yc.verbaltalk.index.adapter;

import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.verbaltalk.R;
import com.yc.verbaltalk.chat.bean.LoveHealDetDetailsBean;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import yc.com.rthttplibrary.util.LogUtil;

/**
 * Created by suns  on 2020/9/9 10:51.
 */
public class VerbalVbItemAdapter extends BaseQuickAdapter<LoveHealDetDetailsBean, BaseViewHolder> {
    public VerbalVbItemAdapter(@Nullable List<LoveHealDetDetailsBean> data) {
        super(R.layout.verbal_vb_item_view, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, LoveHealDetDetailsBean item) {
        helper.setText(R.id.item_details_bean_tv_name, item.content);

        String ansSex = item.ans_sex;

        if (!TextUtils.isEmpty(ansSex)) {

            switch (ansSex) {//1男2女0bi'a
                case "1":
                    helper.setImageDrawable(R.id.item_details_bean_iv_sex, ContextCompat.getDrawable(mContext, R.mipmap.icon_dialogue_men));
                    break;
                case "2":
                    helper.setImageDrawable(R.id.item_details_bean_iv_sex, ContextCompat.getDrawable(mContext, R.mipmap.icon_dialogue_women));
                    break;
                default: {
                    helper.setImageDrawable(R.id.item_details_bean_iv_sex, ContextCompat.getDrawable(mContext, R.mipmap.icon_dialogue_nothing));
                    break;
                }

            }
        }
    }
}
