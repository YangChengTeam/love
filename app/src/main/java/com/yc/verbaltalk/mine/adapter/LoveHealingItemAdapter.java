package com.yc.verbaltalk.mine.adapter;

import android.text.TextUtils;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.verbaltalk.R;
import com.yc.verbaltalk.chat.bean.LoveHealingBean;

import java.util.List;

import androidx.annotation.Nullable;


public class LoveHealingItemAdapter extends BaseQuickAdapter<LoveHealingBean, BaseViewHolder> {
    public LoveHealingItemAdapter(@Nullable List<LoveHealingBean> data) {
        super(R.layout.recycler_view_item_love_healing, data);
    }



    @Override
    protected void convert(BaseViewHolder helper, LoveHealingBean item) {
        TextView tvName = helper.getView(R.id.item_love_healing_tv_name);
        tvName.setSingleLine(true);
        tvName.setEllipsize(TextUtils.TruncateAt.END);
        tvName.setText(item.chat_name);
    }
}