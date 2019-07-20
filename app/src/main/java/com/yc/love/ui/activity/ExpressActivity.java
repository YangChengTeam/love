package com.yc.love.ui.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yc.love.R;
import com.yc.love.adaper.rv.ConfessionAdapter;
import com.yc.love.cache.CacheWorker;
import com.yc.love.model.bean.confession.ConfessionBean;
import com.yc.love.model.bean.confession.ConfessionDataBean;
import com.yc.love.model.engin.LoveEngin;
import com.yc.love.ui.activity.base.BaseSameActivity;
import com.yc.love.ui.view.LoadDialog;
import com.yc.love.ui.view.LoadingDialog;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

//表白
public class ExpressActivity extends BaseSameActivity {

    private RecyclerView mRecyclerView;
    private CacheWorker mCacheWorker;
    private List<ConfessionDataBean> mConfessionDataBeans;
    private int PAGE_SIZE = 20;
    private int PAGE_NUM = 1;
    private ConfessionAdapter mAdapter;

    private LoadingDialog mLoadDialog;
    private LoveEngin loveEngin;
    private SwipeRefreshLayout mSipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_express);

        loveEngin = new LoveEngin(this);
        initView();
        initData();
    }

    private void initView() {
        mCacheWorker = new CacheWorker();
        initRecyclerView();
        initListener();
    }

    private void initRecyclerView() {
        mRecyclerView = findViewById(R.id.express_rl);
        mSipe = findViewById(R.id.swipeRefreshLayout);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        //设置增加或删除条目的动画
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new ConfessionAdapter(null);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initListener() {
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ConfessionDataBean confessionDataBean = mAdapter.getItem(position);
                if (confessionDataBean != null && ConfessionDataBean.VIEW_ITEM == confessionDataBean.itemType)
                    CreateBeforeActivity.startCreateBeforeActivity(ExpressActivity.this, confessionDataBean);
            }
        });
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                initData();
            }
        }, mRecyclerView);
        mSipe.setColorSchemeResources(R.color.red_crimson);
        mSipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                PAGE_NUM = 1;
                initData();
            }
        });
    }

    private void initData() {
        if (PAGE_NUM == 1) {
            mConfessionDataBeans = (List<ConfessionDataBean>) mCacheWorker.getCache(ExpressActivity.this, "main3_new");
            if (mConfessionDataBeans != null && mConfessionDataBeans.size() > 0) {
                mAdapter.setNewData(mConfessionDataBeans);
            }

            mLoadDialog = new LoadingDialog(ExpressActivity.this);
            mLoadDialog.showLoading();

        }

        loveEngin.geteExpressData(PAGE_NUM).subscribe(new Subscriber<ConfessionBean>() {
            @Override
            public void onCompleted() {
                if (mLoadDialog != null) mLoadDialog.dismissLoading();
                if (mSipe.isRefreshing()) mSipe.setRefreshing(false);
            }

            @Override
            public void onError(Throwable e) {
                if (mLoadDialog != null) mLoadDialog.dismissLoading();
                if (mSipe.isRefreshing()) mSipe.setRefreshing(false);

            }

            @Override
            public void onNext(ConfessionBean confessionBean) {
                if (confessionBean != null && confessionBean.status) {
                    List<ConfessionDataBean> confessionDataBeans = confessionBean.data;
                    createNewData(confessionDataBeans);
                }
                if (mLoadDialog != null) mLoadDialog.dismissLoading();
                if (mSipe.isRefreshing()) mSipe.setRefreshing(false);
            }
        });

    }

    private void createNewData(List<ConfessionDataBean> confessionDataBeans) {
        mConfessionDataBeans = new ArrayList<>();

        if (confessionDataBeans != null && confessionDataBeans.size() > 0) {
            for (ConfessionDataBean confessionDataBean : confessionDataBeans) {
                confessionDataBean.itemType = ConfessionDataBean.VIEW_ITEM;
                mConfessionDataBeans.add(confessionDataBean);
            }
        }

        if (PAGE_NUM == 1)
            mConfessionDataBeans.add(0, new ConfessionDataBean(ConfessionDataBean.VIEW_TITLE, "data_title"));

        if (PAGE_NUM == 1) {
            mCacheWorker.setCache("main3_new", mConfessionDataBeans);
            mAdapter.setNewData(mConfessionDataBeans);
        } else {
            mAdapter.addData(mConfessionDataBeans);
        }
        if (confessionDataBeans != null && confessionDataBeans.size() == PAGE_SIZE) {
            mAdapter.loadMoreComplete();
            PAGE_NUM++;
        } else {
            mAdapter.loadMoreEnd();
        }

    }


    @Override
    protected String offerActivityTitle() {
        return "表白神器";
    }
}
