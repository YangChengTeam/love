package com.yc.verbaltalk.index.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.verbaltalk.R;
import com.yc.verbaltalk.chat.bean.LoveHealDateBean;
import com.yc.verbaltalk.index.ui.activity.LoveHealDetailsActivity;

import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by mayn on 2019/6/12.
 * 首页话术适配器
 */

public class IndexVerbalAdapter extends BaseQuickAdapter<LoveHealDateBean, BaseViewHolder> {


    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public IndexVerbalAdapter(List<LoveHealDateBean> data) {
        super(R.layout.recycler_view_item_love_heal_new, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, LoveHealDateBean item) {
        if (item != null) {
            helper.setText(R.id.item_love_heal_title_tv_name, item.name);
            RecyclerView recyclerView = helper.getView(R.id.item_recyclerView);
            recyclerView.setLayoutManager(new GridLayoutManager(mContext, 3, GridLayoutManager.VERTICAL, false));
            final LoveIndexItemAdapter loveIndexItemAdapter = new LoveIndexItemAdapter(item.children);
            recyclerView.setAdapter(loveIndexItemAdapter);
            loveIndexItemAdapter.setOnItemClickListener((adapter, view, position) -> {
                LoveHealDateBean.ChildrenBean item1 = loveIndexItemAdapter.getItem(position);
                if (item1 != null) {
                    LoveHealDetailsActivity.startLoveHealDetailsActivity(mContext, item1.name, String.valueOf(item1.id));
                }
            });
//            int position = helper.getAdapterPosition();
//            if (position == mData.size() - 1) {
//                helper.setGone(R.id.divider, false);
//            } else {
//                helper.setGone(R.id.divider, true);
//            }


        }

    }


}