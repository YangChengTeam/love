package com.yc.love.ui.frament;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.yc.love.R;
import com.yc.love.adaper.rv.LoveByStagesAdapter;
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

//    private NoThingCanEmptyAdapter<LoveByStagesBean> mAdapter;

    private LoveByStagesAdapter mAdapter;

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
        mLoadingDialog = new LoadDialog(getActivity());
        initRecyclerView();
        initListener();
    }


    private void initRecyclerView() {
        mSwipeRefresh = rootView.findViewById(R.id.fragment_love_by_stages_swipe_refresh);
        mRecyclerView = rootView.findViewById(R.id.fragment_love_by_stages_rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mLoveByStagesActivity);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new LoveByStagesAdapter(null);
        mRecyclerView.setAdapter(mAdapter);

        mSwipeRefresh.setColorSchemeResources(R.color.red_crimson);
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
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
        if (PAGE_NUM == 1) {
            mLoadingDialog.showLoadingDialog();
        }
        mLoveEngin.listsArticle(String.valueOf(mCategoryId), String.valueOf(PAGE_NUM), String.valueOf(PAGE_SIZE), "Article/lists").subscribe(new MySubscriber<AResultInfo<List<LoveByStagesBean>>>(mLoadingDialog) {

            @Override
            protected void onNetNext(AResultInfo<List<LoveByStagesBean>> listAResultInfo) {
//                mLoveByStagesBeans = listAResultInfo.data;

                if (PAGE_NUM == 1 && mLoadingDialog != null) {
                    mLoadingDialog.dismissLoadingDialog();
                }
                if (listAResultInfo != null && listAResultInfo.code == HttpConfig.STATUS_OK && listAResultInfo.data != null)
                    createNewData(listAResultInfo.data);
            }

            @Override
            protected void onNetError(Throwable e) {
                if (mSwipeRefresh.isRefreshing()) mSwipeRefresh.setRefreshing(false);
                if (PAGE_NUM == 1 && mLoadingDialog != null) {
                    mLoadingDialog.dismissLoadingDialog();

                }
            }

            @Override
            protected void onNetCompleted() {
                if (PAGE_NUM == 1 && mLoadingDialog != null) {
                    mLoadingDialog.dismissLoadingDialog();

                }
                if (mSwipeRefresh.isRefreshing()) mSwipeRefresh.setRefreshing(false);
            }
        });
    }

    private void createNewData(List<LoveByStagesBean> loveByStagesBeans) {
        if (PAGE_NUM == 1) {
            mAdapter.setNewData(loveByStagesBeans);
        } else {
            mAdapter.addData(loveByStagesBeans);
        }
        if (PAGE_SIZE == loveByStagesBeans.size()) {
            mAdapter.loadMoreComplete();
            PAGE_NUM++;
        } else {
            mAdapter.loadMoreEnd();
        }
        if (mSwipeRefresh.isRefreshing()) mSwipeRefresh.setRefreshing(false);

    }

    private void initListener() {
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                mLoveByStagesActivity.startActivity(new Intent(mLoveByStagesActivity, TestWebViewActivity.class));
                LoveByStagesBean loveByStagesBean = mAdapter.getItem(position);
                if (loveByStagesBean != null)
                    LoveByStagesDetailsActivity.startLoveByStagesDetailsActivity(mLoveByStagesActivity, loveByStagesBean.id, loveByStagesBean.post_title);
            }
        });
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                netData();
            }
        }, mRecyclerView);
    }

}
