package com.yc.love.adaper.rv;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yc.love.R;
import com.yc.love.adaper.rv.holder.BaseViewHolder;
import com.yc.love.adaper.rv.holder.ProgressBarViewHolder;
import com.yc.love.adaper.rv.holder.UpDownMenHolder;
import com.yc.love.adaper.rv.holder.UpDownWomenHolder;
import com.yc.love.model.bean.LoveHealingDetailBean;

import java.util.List;

/**
 * Created by mayn on 2019/5/5.
 */

public abstract class LoveUpDownPhotoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final List<LoveHealingDetailBean> mPersonList;
    private RecyclerView mRecyclerView;
    private static final int VIEW_ITEM_MEN = 0;
    private static final int VIEW_ITEM_WOMEN = 2;
    private static final int VIEW_PROG = 1;
    private boolean isLoading;
    private int totalItemCount;
    private int lastVisibleItemPosition;

    public LoveUpDownPhotoAdapter(List<LoveHealingDetailBean> personList, RecyclerView recyclerView) {
        this.mPersonList = personList;
        this.mRecyclerView = recyclerView;
//        addOnScrollListenerPacked();
    }

    @Override
    public int getItemCount() {
        int size = 0;
        if (mPersonList != null) {
            size = mPersonList.size();
        }
        Log.d("mylog", "getItemCount: mPersonList.size() " + size);
        return size;
    }

    //根据不同的数据返回不同的viewType
    @Override
    public int getItemViewType(int position) {
        LoveHealingDetailBean loveHealingDetailBean = mPersonList.get(position);
        if (loveHealingDetailBean == null) {
            return VIEW_PROG;
        }
        String ansSex = loveHealingDetailBean.ans_sex;
        if ("1".equals(ansSex)) { //1男2女
            return VIEW_ITEM_MEN;
        } else {
            return VIEW_ITEM_WOMEN;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder;
        if (viewType == VIEW_ITEM_MEN) {
            holder = getMenHolder(parent);
        } else if (viewType == VIEW_ITEM_WOMEN) {
            holder = getWomenHolder(parent);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_test_item_footer, parent, false);
            holder = new ProgressBarViewHolder(view);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof UpDownMenHolder) {
            ((BaseViewHolder) holder).bindData(mPersonList.get(position));
        } else if (holder instanceof UpDownWomenHolder) {
            ((BaseViewHolder) holder).bindData(mPersonList.get(position));
        } else if (holder instanceof ProgressBarViewHolder) {
            ProgressBarViewHolder viewHolder = (ProgressBarViewHolder) holder;
            viewHolder.pb.setIndeterminate(true);
        }
    }

    private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
            totalItemCount = linearLayoutManager.getItemCount();
            lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();

            Log.d("ssss", "onScrolled: " + "lastVisibleItemPosition 可见的最后一条  222 =" + lastVisibleItemPosition);
            if (totalItemCount == lastVisibleItemPosition + 1) {
                if (totalItemCount == 0) {
                    return;
                }
            }

//            if (!isLoading && totalItemCount <= (lastVisibleItemPosition + visibleThreshold)) {
            if (!isLoading && totalItemCount == lastVisibleItemPosition + 1) {
                //此时是刷新状态
                if (mMoreDataListener != null) {
                    if (totalItemCount == 0) {
                        return;
                    }
                    isLoading = true;
                    mMoreDataListener.loadMoreData();
                    Log.d("ssss", "onScrolled: 触发加载数据 --------");
                }
//                isLoading = true;
            }
        }
    };

    public void removeOnScrollListenerPacked() {
        mRecyclerView.removeOnScrollListener(onScrollListener);
    }

    public void addOnScrollListenerPacked() {
        mRecyclerView.addOnScrollListener(onScrollListener);
    }


    public void setLoaded() {
        isLoading = false;
    }

    private OnLoadMoreDataListener mMoreDataListener;

    //加载更多监听方法
    public void setOnMoreDataLoadListener(OnLoadMoreDataListener onMoreDataLoadListener) {
        mMoreDataListener = onMoreDataLoadListener;
    }

    public void removeOnMoreDataLoadListener() {
        mMoreDataListener = null;
    }

    public interface OnLoadMoreDataListener {
        void loadMoreData();
    }


    public abstract BaseViewHolder getMenHolder(ViewGroup parent);

    public abstract BaseViewHolder getWomenHolder(ViewGroup parent);
}