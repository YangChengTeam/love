package com.yc.love.adaper.rv;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.yc.love.adaper.rv.holder.BaseViewHolder;
import com.yc.love.adaper.rv.holder.MainT1CategoryViewHolder;
import com.yc.love.adaper.rv.holder.MainT1ItemHolder;
import com.yc.love.adaper.rv.holder.ProgressBarViewHolder;
import com.yc.love.adaper.rv.holder.TitleT1ViewHolder;
import com.yc.love.model.bean.ExampListsBean;

import java.util.List;


/**
 * Created by Administrator on 2017/9/12.
 */

public abstract class MainT1MoreItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<ExampListsBean> mPersonList;
    private RecyclerView mRecyclerView;
    private static final int VIEW_ITEM = 0;
    private static final int VIEW_PROG = 1;
    private static final int VIEW_TITLE = 2;
    private static final int VIEW_CATEGORY = 3;
    private boolean isLoading;
    private int totalItemCount;
    private int lastVisibleItemPosition;
    //当前滚动的position下面最小的items的临界值
//    private int visibleThreshold = 5;

    public MainT1MoreItemAdapter(List<ExampListsBean> personList, RecyclerView recyclerView) {
        this.mPersonList = personList;
        this.mRecyclerView = recyclerView;
        addOnScrollListenerPacked();
    }

    @Override
    public int getItemCount() {
        if (mPersonList != null) {
            return mPersonList.size();
        }
        return 0;
    }

    //根据不同的数据返回不同的viewType
    @Override
    public int getItemViewType(int position) {
        ExampListsBean exampListsBean = mPersonList.get(position);
        if (exampListsBean == null) {
            return VIEW_PROG;
        }
        switch (exampListsBean.type) {
            case 0:
                return VIEW_TITLE;
            case 1:
                return VIEW_ITEM;
            case 3:
                return VIEW_CATEGORY;
        }
        return VIEW_PROG;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder;
        if (viewType == VIEW_TITLE) {
            holder = getTitleHolder(parent);
        } else if (viewType == VIEW_ITEM) {
            holder = getHolder(parent);
        } else {
            holder = getBarViewHolder(parent);
        }
        return holder;
    }




    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TitleT1ViewHolder) {
            ((BaseViewHolder) holder).bindData(mPersonList.get(position));
        } else if (holder instanceof MainT1ItemHolder) {
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
                    // 加载数据
                    mMoreDataListener.loadMoreData();
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

    public abstract BaseViewHolder getHolder(ViewGroup parent);

    public abstract BaseViewHolder getTitleHolder(ViewGroup parent);

    protected abstract RecyclerView.ViewHolder getBarViewHolder(ViewGroup parent);
}