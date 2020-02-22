package com.yc.verbaltalk.index.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.verbaltalk.R;
import com.yc.verbaltalk.chat.bean.LoveHealDateBean;

import java.util.List;

import androidx.annotation.Nullable;

/**
 * Created by wanglin  on 2019/6/27 15:14.
 */
public class LoveIndexItemAdapter extends BaseQuickAdapter<LoveHealDateBean.ChildrenBean,BaseViewHolder>{
    public LoveIndexItemAdapter(@Nullable List<LoveHealDateBean.ChildrenBean> data) {
        super(R.layout.recycler_view_item_love_heal,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, LoveHealDateBean.ChildrenBean item) {
        helper.setText(R.id.item_love_heal_tv_name, item.name);
    }
}
