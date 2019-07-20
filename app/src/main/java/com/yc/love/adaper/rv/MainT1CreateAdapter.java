package com.yc.love.adaper.rv;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.love.R;
import com.yc.love.model.bean.LoveHealDateBean;
import com.yc.love.ui.activity.LoveHealDetailsActivity;

import java.util.List;

/**
 * Created by mayn on 2019/6/12.
 */

public class MainT1CreateAdapter extends BaseQuickAdapter<LoveHealDateBean, BaseViewHolder> {


    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public MainT1CreateAdapter(List<LoveHealDateBean> data) {
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
            loveIndexItemAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    LoveHealDateBean.ChildrenBean item = loveIndexItemAdapter.getItem(position);
                    if (item != null) {
                        LoveHealDetailsActivity.startLoveHealDetailsActivity(mContext, item.name, String.valueOf(item.id));
                    }
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