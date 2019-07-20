package com.yc.love.adaper.rv;

import android.support.annotation.Nullable;
import android.util.Log;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.love.R;

import java.util.List;

/**
 * Created by wanglin  on 2019/7/10 15:06.
 */
public class SearchTintAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public SearchTintAdapter(@Nullable List<String> data) {
        super(R.layout.search_tint_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.tv_title, item);
        int position = helper.getAdapterPosition();
        if (position == mData.size() - 1) {
            helper.setGone(R.id.divider, false);
        }
    }
}
