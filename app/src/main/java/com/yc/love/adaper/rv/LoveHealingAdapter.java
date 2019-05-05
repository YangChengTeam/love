package com.yc.love.adaper.rv;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;

import com.yc.love.adaper.rv.holder.BaseViewHolder;
import com.yc.love.adaper.rv.holder.LoveHealingItemTitleViewHolder;
import com.yc.love.adaper.rv.holder.LoveHealingItemViewHolder;
import com.yc.love.adaper.rv.holder.MainT3ItemTitleViewHolder;
import com.yc.love.adaper.rv.holder.ProgressBarViewHolder;
import com.yc.love.model.bean.LoveHealingBean;

import java.util.List;


/**
 * Created by Administrator on 2017/9/12.
 */

public abstract class LoveHealingAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<T> mPersonList;
    private RecyclerView mRecyclerView;
    private static final int VIEW_TITLE = 0;
    private static final int VIEW_ITEM_ITEM = 1;
    private static final int VIEW_ITEM = 2;
    private static final int VIEW_PROG = 3;
    private boolean isLoading;
    private int totalItemCount;
    private int lastVisibleItemPosition;

    public LoveHealingAdapter(List<T> personList, RecyclerView recyclerView) {
        this.mPersonList = personList;
        this.mRecyclerView = recyclerView;
        addOnScrollListenerPacked();
    }

    @Override
    public int getItemCount() {
        if (mPersonList != null) {
            return mPersonList.size() ;
        }
        return 0;
    }

    //根据不同的数据返回不同的viewType
    @Override
    public int getItemViewType(int position) {
        int size = mPersonList.size();
        LoveHealingBean loveHealingBean = null;
        try {
            loveHealingBean = (LoveHealingBean) mPersonList.get(position);
        } catch (IndexOutOfBoundsException e) {
            Log.d("mylog", "getItemViewType: e " + e);
        }
        if (loveHealingBean != null) {
            int type = loveHealingBean.type;
            switch (type) {
                case VIEW_ITEM:
                    return VIEW_ITEM;
                case VIEW_ITEM_ITEM:
                    return VIEW_ITEM_ITEM;
                case VIEW_TITLE:
                    return VIEW_TITLE;
                default:
                    return VIEW_PROG;
            }
        }
        return mPersonList.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder holder = null;
        switch (viewType) {
            case VIEW_TITLE:
                holder = getTitleHolder(viewGroup);
                break;
            case VIEW_ITEM:
                holder = getHolder(viewGroup);
                break;
            case VIEW_ITEM_ITEM:
                holder = getItemTitleHolder(viewGroup);
                break;
            default:
                holder = getBarViewHolder(viewGroup);
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof LoveHealingItemViewHolder) {
            ((BaseViewHolder) holder).bindData(mPersonList.get(position));
        } else if (holder instanceof LoveHealingItemTitleViewHolder) {
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
            Log.d("mylog", "onScrolled totalItemCount =  全部数据条数 " + totalItemCount + "-----");
            Log.d("mylog", "onScrolled: " + "lastVisibleItemPosition 可见的最后一条  222 =" + lastVisibleItemPosition);

            if (totalItemCount == lastVisibleItemPosition + 1) {
                if (totalItemCount == 0) {
                    return;
                }
            }

            if (!isLoading && totalItemCount == lastVisibleItemPosition + 1) {
                //此时是刷新状态
                if (mMoreDataListener != null) {
                    if (totalItemCount == 0) {
                        return;
                    }
                    isLoading = true;
                    // 加载数据
                    Log.d("mylog", "onScrolled: mMoreDataListener.loadMoreData");
                    mMoreDataListener.loadMoreData();
                }
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


    public abstract BaseViewHolder getHolder(ViewGroup parent);

    public abstract BaseViewHolder getTitleHolder(ViewGroup parent);

    protected abstract RecyclerView.ViewHolder getBarViewHolder(ViewGroup parent);

    protected abstract RecyclerView.ViewHolder getItemTitleHolder(ViewGroup parent);
}