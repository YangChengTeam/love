package com.yc.love.adaper.rv;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

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
    private RecyclerView mRecyclerView;
    private static final int VIEW_TITLE = 1;
    private static final int VIEW_ITEM = 2;
    private static final int VIEW_TAIL = 3;

    public BecomeVipAdapter(List<BecomeVipBean> personList, RecyclerView recyclerView) {
        this.mDatas = personList;
        this.mRecyclerView = recyclerView;
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
            ((BaseViewHolder) holder).bindData(mDatas.get(position ));
        } else if (holder instanceof BecomeVipTitleViewHolder) {
            ((BaseViewHolder) holder).bindData(mDatas.get(position ));
        }
    }

    private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

        }
    };

    public void addOnScrollListenerPacked() {
        mRecyclerView.addOnScrollListener(onScrollListener);
    }

    public abstract BaseViewHolder getHolder(ViewGroup parent);

    public abstract BaseViewHolder getTitleHolder(ViewGroup parent);

    protected abstract RecyclerView.ViewHolder getTailViewHolder(ViewGroup parent);
}