package com.yc.verbaltalk.index.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.verbaltalk.R;
import com.yc.verbaltalk.chat.bean.IndexHotInfo;

import java.util.List;

import androidx.annotation.Nullable;

/**
 * Created by wanglin  on 2019/7/10 15:06.
 * 首页搜索推荐适配器
 */
public class SearchTintAdapter extends BaseQuickAdapter<IndexHotInfo, BaseViewHolder> {
    public SearchTintAdapter(@Nullable List<IndexHotInfo> data) {
        super(R.layout.search_tint_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, IndexHotInfo item) {
        helper.setText(R.id.tv_title, item.getSearch());
        int position = helper.getAdapterPosition();
//        if (position == mData.size() - 1) {
//            helper.setGone(R.id.divider, false);
//        }
    }
}
