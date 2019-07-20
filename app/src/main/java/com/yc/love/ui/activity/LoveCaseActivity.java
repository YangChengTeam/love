package com.yc.love.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.yc.love.R;
import com.yc.love.adaper.rv.MainT2MoreItemAdapter;
import com.yc.love.cache.CacheWorker;
import com.yc.love.model.base.MySubscriber;
import com.yc.love.model.bean.AResultInfo;
import com.yc.love.model.bean.ExampDataBean;
import com.yc.love.model.bean.ExampListsBean;
import com.yc.love.model.bean.MainT2Bean;
import com.yc.love.model.engin.LoveEngin;
import com.yc.love.model.single.YcSingle;
import com.yc.love.ui.activity.base.BaseSameActivity;

import java.util.ArrayList;
import java.util.List;

public class LoveCaseActivity extends BaseSameActivity {


    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefresh;
    private CacheWorker mCacheWorker;
    private LoveEngin mLoveEngin;
    private boolean mIsHandRefresh;
    private List<MainT2Bean> mMainT2Beans;

    private int PAGE_SIZE = 10;
    private int PAGE_NUM = 1;
    private boolean mIsAddToPayVipItem = false;
    private boolean loadMoreEnd;
    private MainT2MoreItemAdapter mAdapter;
    private boolean mIsNetData = false;
    private boolean mUserIsVip = false;
    private boolean loadDataEnd;
    private boolean showProgressBar = false;
    private int mRvLastPosition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_love_case);

        mLoveEngin = new LoveEngin(this);
        mCacheWorker = new CacheWorker();
        initViews();
        netData();

    }

    private void initViews() {
        ImageView ivToWx = findViewById(R.id.love_case_iv_to_wx);
        ivToWx.setOnClickListener(this);
        initRecyclerView();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.love_case_iv_to_wx:
                showToWxServiceDialog();
                break;
        }
    }

    private void initRecyclerView() {
        mSwipeRefresh = findViewById(R.id.love_case_swipe_refresh);
        mRecyclerView = findViewById(R.id.love_case_rl);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//        RecyclerViewNoBugLinearLayoutManager layoutManager = new RecyclerViewNoBugLinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        //设置增加或删除条目的动画
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new MainT2MoreItemAdapter(null);
        mRecyclerView.setAdapter(mAdapter);


        mSwipeRefresh.setColorSchemeResources(R.color.red_crimson);
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                PAGE_NUM = 1;
                netData();
            }
        });
        initListener();
    }

    private void initListener() {
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                MainT2Bean mainT2Bean = mAdapter.getItem(position);
                if (mainT2Bean != null) {
                    if (MainT2Bean.VIEW_ITEM == mainT2Bean.type) {
                        ExampleDetailActivity.startExampleDetailActivity(LoveCaseActivity.this, mainT2Bean.id, mainT2Bean.post_title);
                    } else if (MainT2Bean.VIEW_TO_PAY_VIP == mainT2Bean.type || MainT2Bean.VIEW_VIP == mainT2Bean.type) {
                        startActivity(new Intent(LoveCaseActivity.this, BecomeVipActivity.class));
                    }
                }
            }
        });

        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                netData();
            }
        }, mRecyclerView);


    }

    private void netData() {

        mMainT2Beans = (List<MainT2Bean>) mCacheWorker.getCache(this, "main2_example_lists");
        if (mMainT2Beans != null && mMainT2Beans.size() > 0 && PAGE_NUM == 1) {
//                initRecyclerViewData();
            mAdapter.setNewData(mMainT2Beans);
        } else {
            mLoadingDialog.showLoadingDialog();
        }
        mLoveEngin.exampLists(String.valueOf(YcSingle.getInstance().id), String.valueOf(PAGE_NUM), String.valueOf(PAGE_SIZE), "example/lists")
                .subscribe(new MySubscriber<AResultInfo<ExampDataBean>>(mLoadingDialog) {
                    @Override
                    protected void onNetNext(AResultInfo<ExampDataBean> exampDataBeanAResultInfo) {
                        mIsNetData = true;
                        if (exampDataBeanAResultInfo != null && exampDataBeanAResultInfo.code == HttpConfig.STATUS_OK && exampDataBeanAResultInfo.data != null) {
                            ExampDataBean exampDataBean = exampDataBeanAResultInfo.data;
                            createNewData(exampDataBean);
                        }

                    }

                    @Override
                    protected void onNetError(Throwable e) {
                        if (mSwipeRefresh.isRefreshing()) mSwipeRefresh.setRefreshing(false);
                    }

                    @Override
                    protected void onNetCompleted() {
                        if (mSwipeRefresh.isRefreshing()) mSwipeRefresh.setRefreshing(false);
                    }
                });
    }

    private void createNewData(ExampDataBean exampDataBean) {
        int isVip = exampDataBean.is_vip;
        if (isVip > 0) {
            mUserIsVip = true;
        }
        List<ExampListsBean> exampListsBeans = exampDataBean.lists;
        mMainT2Beans = new ArrayList<>();
        if (PAGE_NUM == 1)
            mMainT2Beans.add(new MainT2Bean("tit", MainT2Bean.VIEW_TITLE));
        if (exampListsBeans != null && exampListsBeans.size() != 0) {
            for (int i = 0; i < exampListsBeans.size(); i++) {
                ExampListsBean exampListsBean = exampListsBeans.get(i);
                mMainT2Beans.add(new MainT2Bean(MainT2Bean.VIEW_ITEM, exampListsBean.create_time, exampListsBean.id, exampListsBean.image, exampListsBean.post_title));
            }
        }
        if (!mUserIsVip && mMainT2Beans != null && mMainT2Beans.size() > 6 && PAGE_NUM == 1) {
            mMainT2Beans.add(6, new MainT2Bean("vip", MainT2Bean.VIEW_VIP));
            mMainT2Beans.add(new MainT2Bean("toPayVip", MainT2Bean.VIEW_TO_PAY_VIP, mMainT2Beans.size()));
        }


        if (PAGE_NUM == 1)
            mCacheWorker.setCache("main2_example_lists", mMainT2Beans);

        if (PAGE_NUM == 1) {
            mAdapter.setNewData(mMainT2Beans);
        } else {
            mAdapter.addData(mMainT2Beans);
        }
        if (exampListsBeans != null && exampListsBeans.size() == PAGE_SIZE) {
            mAdapter.loadMoreComplete();
            PAGE_NUM++;
        } else {
            mAdapter.loadMoreEnd();
        }
        if (mSwipeRefresh.isRefreshing()) mSwipeRefresh.setRefreshing(false);


    }

    @Override
    protected String offerActivityTitle() {
        return "实战学习";
    }
}
