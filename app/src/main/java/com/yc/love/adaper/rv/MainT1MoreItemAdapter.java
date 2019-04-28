package com.yc.love.adaper.rv;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.yc.love.adaper.rv.holder.BaseViewHolder;
import com.yc.love.adaper.rv.holder.ProgressBarViewHolder;
import com.yc.love.adaper.rv.holder.TitleT1ViewHolder;

import java.util.List;


/**
 * Created by Administrator on 2017/9/12.
 */

public abstract class MainT1MoreItemAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<T> mPersonList;
    private final ImageView ll_toolbar;
    private RecyclerView mRecyclerView;
    private static final int VIEW_ITEM = 0;
    private static final int VIEW_PROG = 1;
    private static final int VIEW_TITLE = 2;
    private boolean isLoading;
    private int totalItemCount;
    private int lastVisibleItemPosition;
    //当前滚动的position下面最小的items的临界值
//    private int visibleThreshold = 5;

    public MainT1MoreItemAdapter(List<T> personList, RecyclerView recyclerView, ImageView ll_toolbar) {
        this.mPersonList = personList;
        this.mRecyclerView = recyclerView;
        this.ll_toolbar = ll_toolbar;
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
        if (position == 0) {
            return VIEW_TITLE;
        }
        return mPersonList.get(position) != null ? VIEW_ITEM : VIEW_PROG;
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
            /*View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_test_item_footer, parent, false);
            holder = new ProgressBarViewHolder(view);*/
        }
        return holder;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TitleT1ViewHolder) {
            ((BaseViewHolder) holder).bindData(mPersonList.get(position));
        } else if (holder instanceof BaseViewHolder) {
            ((BaseViewHolder) holder).bindData(mPersonList.get(position - 1));
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

            int currentposition = linearLayoutManager.getPosition(linearLayoutManager.getChildAt(0));
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

    protected abstract RecyclerView.ViewHolder getBarViewHolder(ViewGroup parent);
}