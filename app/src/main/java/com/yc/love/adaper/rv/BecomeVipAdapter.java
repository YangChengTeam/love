package com.yc.love.adaper.rv;

import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.yc.love.adaper.rv.holder.BaseViewHolder;
import com.yc.love.adaper.rv.holder.BecomeVipItemViewHolder;
import com.yc.love.adaper.rv.holder.BecomeVipTailViewHolder;
import com.yc.love.adaper.rv.holder.BecomeVipTitleViewHolder;
import com.yc.love.adaper.rv.holder.ProgressBarViewHolder;
import com.yc.love.adaper.rv.holder.TitleT1ViewHolder;
import com.yc.love.model.bean.BecomeVipBean;
import com.yc.love.model.bean.LoveHealBean;

import java.util.List;


public abstract class BecomeVipAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<com.yc.love.model.bean.BecomeVipBean> mDatas;
    private final LinearLayout mLlTitleCon;
    private RecyclerView mRecyclerView;
    private static final int VIEW_TITLE = 1;
    private static final int VIEW_ITEM = 2;
    private static final int VIEW_TAIL = 3;

    public BecomeVipAdapter(List<BecomeVipBean> personList, RecyclerView recyclerView, LinearLayout llTitleCon) {
        this.mDatas = personList;
        this.mRecyclerView = recyclerView;
        this.mLlTitleCon = llTitleCon;
        addOnScrollListenerPacked();
    }

    @Override
    public int getItemCount() {
        if (mDatas != null) {
            return mDatas.size();
        }
        return 0;
    }

    //根据不同的数据返回不同的viewType
    @Override
    public int getItemViewType(int position) {
        BecomeVipBean becomeVipBean = mDatas.get(position);
        switch (becomeVipBean.type) {
            case VIEW_TITLE:
                return VIEW_TITLE;
            case VIEW_ITEM:
                return VIEW_ITEM;
            default:
                return VIEW_TAIL;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder;
        if (viewType == VIEW_TITLE) {
            holder = getTitleHolder(parent);
        } else if (viewType == VIEW_ITEM) {
            holder = getHolder(parent);
        } else {
            holder = getTailViewHolder(parent);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof BecomeVipItemViewHolder) {
            ((BaseViewHolder) holder).bindData(mDatas.get(position));
        } else if (holder instanceof BecomeVipTailViewHolder) {
            ((BaseViewHolder) holder).bindData(mDatas.get(position));
        } else if (holder instanceof BecomeVipTitleViewHolder) {
            ((BaseViewHolder) holder).bindData(mDatas.get(position));
        }
    }

    private int height = 35;// 滑动开始变色的高,真实项目中此高度是由广告轮播或其他首页view高度决定
    private int overallXScroll = 0;

    private int endAlpha = 255;
//    private int endAlpha = 180;

    private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
            int firstCompletelyVisibleItemPosition = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
            if (firstCompletelyVisibleItemPosition == 0) {
                overallXScroll = 1;
            }
            overallXScroll = overallXScroll + dy;// 累加y值 解决滑动一半y值为0
            if (overallXScroll <= 10) {   //设置标题的背景颜色
//                mLlTitleCon.setBackgroundColor(Color.argb((int) 0, 255, 130, 0));
                mLlTitleCon.setBackgroundColor(Color.argb((int) 0, 208, 180, 130));
            } else if (overallXScroll > 10 && overallXScroll <= height) { //滑动距离小于banner图的高度时，设置背景和字体颜色颜色透明度渐变
                float scale = (float) overallXScroll / height;
                float alpha = (endAlpha * scale);
//                mLlTitleCon.setBackgroundColor(Color.argb((int) alpha, 255, 130, 0));
                mLlTitleCon.setBackgroundColor(Color.argb((int) alpha, 208, 180, 130));
            } else {
//                mLlTitleCon.setBackgroundColor(Color.argb((int) 255, 255, 130, 0));
                mLlTitleCon.setBackgroundColor(Color.argb((int) endAlpha, 208, 180, 130));
            }
        }
    };

    public void addOnScrollListenerPacked() {
        mRecyclerView.addOnScrollListener(onScrollListener);
    }

    public abstract BaseViewHolder getHolder(ViewGroup parent);

    public abstract BaseViewHolder getTitleHolder(ViewGroup parent);

    protected abstract RecyclerView.ViewHolder getTailViewHolder(ViewGroup parent);
}