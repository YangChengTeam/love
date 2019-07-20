package com.yc.love.adaper.rv;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.love.R;
import com.yc.love.model.bean.IndexHotInfo;

import java.util.List;

/**
 * Created by wanglin  on 2019/7/17 16:35.
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
