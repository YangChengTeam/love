package com.yc.love.ui.frament;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;

import com.yc.love.R;
import com.yc.love.adaper.rv.NoThingCanEmptyAdapter;
import com.yc.love.adaper.rv.base.RecyclerViewItemListener;
import com.yc.love.adaper.rv.holder.BaseViewHolder;
import com.yc.love.adaper.rv.holder.EmptyViewHolder;
import com.yc.love.adaper.rv.holder.LoveByStagesViewHolder;
import com.yc.love.model.base.MySubscriber;
import com.yc.love.model.bean.AResultInfo;
import com.yc.love.model.bean.LoveByStagesBean;
import com.yc.love.model.engin.LoveEngin;
import com.yc.love.ui.activity.LoveByStagesDetailsActivity;
import com.yc.love.ui.activity.TestWebViewActivity;
import com.yc.love.ui.frament.base.BaseLoveByStagesFragment;
import com.yc.love.ui.view.LoadDialog;

import java.util.List;

/**
 * Created by mayn on 2019/5/5.
 */

public class LoveByStagesFragment extends BaseLoveByStagesFragment {

    private SwipeRefreshLayout mSwipeRefresh;
    private RecyclerView mRecyclerView;
    private int mCategoryId;
    private LoveEngin mLoveEngin;
    private int PAGE_SIZE = 10;
    private int PAGE_NUM = 1;
    private LoadDialog mLoadingDialog;
    private List<LoveByStagesBean> mLoveByStagesBeans;
    private boolean loadMoreEnd;
    private boolean loadDataEnd;
    private boolean showProgressBar = false;
    private NoThingCanEmptyAdapter<LoveByStagesBean> mAdapter;

    @Override
    protected void initBundle() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            int position = arguments.getInt("position");
            mCategoryId = arguments.getInt("category_id", -1);
        }
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_love_by_stages;
    }

    @Override
    protected void initViews() {
        mLoveEngin = new LoveEngin(mLoveByStagesActivity);
//        LoadingDialog loadingDialog=mLoveByStagesActivity.mLoadingDialog;
        mLoadingDialog = mLoveByStagesActivity.mLoadingDialog;
        initRecyclerView();
    }


    private void initRecyclerView() {
        mSwipeRefresh = rootView.findViewById(R.id.fragment_love_by_stages_swipe_refresh);
        mRecyclerView = rootView.findViewById(R.id.fragment_love_by_stages_rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mLoveByStagesActivity);
        mRecyclerView.setLayoutManager(layoutManager);


        mSwipeRefresh.setColorSchemeResources(R.color.red_crimson);
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadMoreEnd = false;
                PAGE_NUM = 1;
                netData();
            }
        });
    }

    @Override
    protected void lazyLoad() {
        netData();
    }


    private void netData() {
        mLoadingDialog.showLoadingDialog();
        mLoveEngin.listsArticle(String.valueOf(mCategoryId), String.valueOf(PAGE_NUM), String.valueOf(PAGE_SIZE), "Article/lists").subscribe(new MySubscriber<AResultInfo<List<LoveByStagesBean>>>(mLoadingDialog) {

            @Override
            protected void onNetNext(AResultInfo<List<LoveByStagesBean>> listAResultInfo) {
                mLoveByStagesBeans = listAResultInfo.data;
                initRecyclerData();
            }

            @Override
            protected void onNetError(Throwable e) {
                mSwipeRefresh.setRefreshing(false);
            }

            @Override
            protected void onNetCompleted() {
                mSwipeRefresh.setRefreshing(false);
            }
        });
    }

    private void initRecyclerData() {
        mAdapter = new NoThingCanEmptyAdapter<LoveByStagesBean>(mLoveByStagesBeans, mRecyclerView) {
            @Override
            public BaseViewHolder getHolder(ViewGroup parent) {
                return new LoveByStagesViewHolder(mLoveByStagesActivity, recyclerViewItemListener, parent);
            }

            @Override
            protected RecyclerView.ViewHolder getEmptyHolder(ViewGroup parent) {
                return new EmptyViewHolder(mLoveByStagesActivity, parent, "");
            }
        };
        mRecyclerView.setAdapter(mAdapter);
        if (mLoveByStagesBeans.size() < PAGE_SIZE) {
            Log.d("ssss", "loadMoreData: data---end");
        } else {
            mAdapter.setOnMoreDataLoadListener(new NoThingCanEmptyAdapter.OnLoadMoreDataListener() {
                @Override
                public void loadMoreData() {
                    if (loadDataEnd == false) {
                        return;
                    }
                    if (showProgressBar == false) {
                        //加入null值此时adapter会判断item的type
                        mLoveByStagesBeans.add(null);
                        mAdapter.notifyDataSetChanged();
                        showProgressBar = true;
                    }
                    if (!loadMoreEnd) {
                        netLoadMore();
                    } else {
                        Log.d("mylog", "loadMoreData: loadMoreEnd end 已加载全部数据 ");
                        mLoveByStagesBeans.remove(mLoveByStagesBeans.size() - 1);
                        mAdapter.notifyDataSetChanged();
                    }
                }
            });
        }
        loadDataEnd = true;
    }

    private void netLoadMore() {
        mLoveEngin.listsArticle(String.valueOf(mCategoryId), String.valueOf(++PAGE_NUM), String.valueOf(PAGE_SIZE), "Article/lists").subscribe(new MySubscriber<AResultInfo<List<LoveByStagesBean>>>(mLoveByStagesActivity) {

            @Override
            protected void onNetNext(AResultInfo<List<LoveByStagesBean>> listAResultInfo) {
                List<LoveByStagesBean> netLoadMoreData = listAResultInfo.data;
                showProgressBar = false;
                mLoveByStagesBeans.remove(mLoveByStagesBeans.size() - 1);
                mAdapter.notifyDataSetChanged();
                if (netLoadMoreData.size() < PAGE_SIZE) {
                    loadMoreEnd = true;
                }
                mLoveByStagesBeans.addAll(netLoadMoreData);
                mAdapter.notifyDataSetChanged();
                mAdapter.setLoaded();
            }

            @Override
            protected void onNetError(Throwable e) {

            }

            @Override
            protected void onNetCompleted() {

            }
        });
    }

    RecyclerViewItemListener recyclerViewItemListener = new RecyclerViewItemListener() {
        @Override
        public void onItemClick(int position) {

//            mLoveByStagesActivity.startActivity(new Intent(mLoveByStagesActivity, TestWebViewActivity.class));
            if (position < 0) {
                return;
            }
            LoveByStagesBean loveByStagesBean = mLoveByStagesBeans.get(position);
            LoveByStagesDetailsActivity.startLoveByStagesDetailsActivity(mLoveByStagesActivity, loveByStagesBean.id, loveByStagesBean.post_title);
        }

        @Override
        public void onItemLongClick(int position) {

        }
    };
}
