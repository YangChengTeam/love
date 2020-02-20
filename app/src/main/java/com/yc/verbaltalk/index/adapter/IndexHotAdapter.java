package com.yc.verbaltalk.index.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.verbaltalk.R;
import com.yc.verbaltalk.chat.bean.IndexHotInfo;

import java.util.List;

import androidx.annotation.Nullable;

/**
 * Created by wanglin  on 2019/7/17 16:35.
 *
 */
public class IndexHotAdapter extends BaseQuickAdapter<IndexHotInfo, BaseViewHolder> {
    public IndexHotAdapter(@Nullable List<IndexHotInfo> data) {
        super(R.layout.recycler_view_item_index_hot, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, IndexHotInfo item) {
        helper.setText(R.id.item_love_heal_tv_name, item.getSearch());
    }
}
