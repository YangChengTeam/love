package com.yc.love.adaper.rv;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;

import com.yc.love.adaper.rv.holder.BaseViewHolder;
import com.yc.love.adaper.rv.holder.MainT2ViewHolder;
import com.yc.love.adaper.rv.holder.ProgressBarViewHolder;
import com.yc.love.adaper.rv.holder.MainT2TitleViewHolder;
import com.yc.love.adaper.rv.holder.VipViewHolder;
import com.yc.love.model.bean.MainT2Bean;

import java.util.List;


/**
 * Created by Administrator on 2017/9/12.
 */

public abstract class MainT2MoreItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<MainT2Bean> mPersonList;
    private RecyclerView mRecyclerView;
    private static final int VIEW_TITLE = 0;
    private static final int VIEW_ITEM = 1;
    private static final int VIEW_VIP = 2;
    private static final int VIEW_PROG = 4;
    private static final int VIEW_TO_PAY_VIP = 3;
    private boolean isLoading;
    private int totalItemCount;
    private int lastVisibleItemPosition;

    public MainT2MoreItemAdapter(List<MainT2Bean> personList, RecyclerView recyclerView) {
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
//        int size = mPersonList.size();
        MainT2Bean mainT2Bean = mPersonList.get(position);
        if (mainT2Bean == null) {
            return VIEW_PROG;
        }
        int type = mainT2Bean.type;
        switch (type) {
            case VIEW_TITLE:
                return VIEW_TITLE;
            case VIEW_VIP:
                return VIEW_VIP;
            case VIEW_TO_PAY_VIP:
                return VIEW_TO_PAY_VIP;
            case VIEW_ITEM:
                return VIEW_ITEM;
        }
        return VIEW_PROG;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder;
        if (viewType == VIEW_TO_PAY_VIP) {
            holder = getToPayVipHolder(parent);
        } else if (viewType == VIEW_TITLE) {
            holder = getTitleHolder(parent);
        } else if (viewType == VIEW_VIP) {
            holder = getVipHolder(parent);
        } else if (viewType == VIEW_ITEM) {
            holder = getHolder(parent);
        } else {
            holder = getBarViewHolder(parent);
        }
        return holder;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MainT2TitleViewHolder) {
            ((BaseViewHolder) holder).bindData(mPersonList.get(position));
        } else if (holder instanceof VipViewHolder) {
            ((BaseViewHolder) holder).bindData(mPersonList.get(position));
        } else if (holder instanceof MainT2ViewHolder) {
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

//            int currentposition = linearLayoutManager.getPosition(linearLayoutManager.getChildAt(0));
            int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
           /* if (firstVisibleItemPosition >= 1) {
                ll_toolbar.setVisibility(View.VISIBLE);
            } else {
                ll_toolbar.setVisibility(View.GONE);
            }*/


            totalItemCount = linearLayoutManager.getItemCount();
            lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();
            Log.d("ssss", "onScrolled totalItemCount =  全部数据条数 " + totalItemCount + "-----");
//            Log.d("ssss", "onScrolled: visibleThreshold  伐值 :" + visibleThreshold);

//            int firstCompletelyVisibleItemPosition = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
//            int lastCompletelyVisibleItemPosition = linearLayoutManager.findLastCompletelyVisibleItemPosition();


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

    public abstract BaseViewHolder getToPayVipHolder(ViewGroup parent);

    protected abstract RecyclerView.ViewHolder getBarViewHolder(ViewGroup parent);

    protected abstract RecyclerView.ViewHolder getVipHolder(ViewGroup parent);
}