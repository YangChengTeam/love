package com.yc.love.ui.frament.main;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
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
import com.yc.love.ui.activity.BecomeVipActivity;
import com.yc.love.ui.activity.ExampleDetailActivity;
import com.yc.love.ui.frament.base.BaseMainT2ChildFragment;
import com.yc.love.ui.view.LoadDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mayn on 2019/6/18.
 */

//LoveCaseActivity
public class ChildMainT2T1Fragment extends BaseMainT2ChildFragment {

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefresh;
    private CacheWorker mCacheWorker;
    private LoveEngin mLoveEngin;

    private List<MainT2Bean> mMainT2Beans;

    private int PAGE_SIZE = 10;
    private int PAGE_NUM = 1;

    private MainT2MoreItemAdapter mAdapter;

    private boolean mUserIsVip = false;

    private LoadDialog mLoadingDialog;


    @Override
    protected int setContentView() {
        return R.layout.activity_practice_teach;
    }

    @Override
    protected void initViews() {
        mLoveEngin = new LoveEngin(mMainActivity);
        mCacheWorker = new CacheWorker();
        initRecyclerView();
    }

    private void initRecyclerView() {
        mSwipeRefresh = rootView.findViewById(R.id.child_main_t2_t1_swipe_refresh);
        mRecyclerView = rootView.findViewById(R.id.lchild_main_t2_t1_rl);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mMainActivity);
//        RecyclerViewNoBugLinearLayoutManager layoutManager = new RecyclerViewNoBugLinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        //设置增加或删除条目的动画
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new MainT2MoreItemAdapter(mMainT2Beans);
        mRecyclerView.setAdapter(mAdapter);

        initListener();

    }

    private void initListener() {
        mSwipeRefresh.setColorSchemeResources(R.color.red_crimson);
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                PAGE_NUM = 1;
                netData(true);
            }
        });
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                netData(false);
            }
        }, mRecyclerView);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                MainT2Bean mainT2Bean = mAdapter.getItem(position);
                if (mainT2Bean != null) {
                    if (MainT2Bean.VIEW_ITEM == mainT2Bean.type) {
                        ExampleDetailActivity.startExampleDetailActivity(mMainActivity, mainT2Bean.id, mainT2Bean.post_title);
                    } else if (MainT2Bean.VIEW_TO_PAY_VIP == mainT2Bean.type || MainT2Bean.VIEW_VIP == mainT2Bean.type) {
                        startActivity(new Intent(mMainActivity, BecomeVipActivity.class));
                    }
                }
            }
        });
    }


    @Override
    protected void lazyLoad() {
        netData(false);
    }


    private void netData(final boolean isRefresh) {

        mMainT2Beans = (List<MainT2Bean>) mCacheWorker.getCache(mMainActivity, "main2_example_lists");
        if (PAGE_NUM == 1 && mMainT2Beans != null && mMainT2Beans.size() > 0) {
            mAdapter.setNewData(mMainT2Beans);
        }

        if (PAGE_NUM == 1 && !isRefresh) {
            mLoadingDialog = new LoadDialog(mMainActivity);
            mLoadingDialog.showLoadingDialog();
        }

        mLoveEngin.exampLists(String.valueOf(YcSingle.getInstance().id), String.valueOf(PAGE_NUM), String.valueOf(PAGE_SIZE), "example/lists")
                .subscribe(new MySubscriber<AResultInfo<ExampDataBean>>(mLoadingDialog) {
                    @Override
                    protected void onNetNext(AResultInfo<ExampDataBean> exampDataBeanAResultInfo) {
                        if (PAGE_NUM == 1 && !isRefresh) {
                            mLoadingDialog.dismissLoadingDialog();
                        }
                        ExampDataBean exampDataBean = exampDataBeanAResultInfo.data;
                        createNewData(exampDataBean);
                    }

                    @Override
                    protected void onNetError(Throwable e) {
                        if (mSwipeRefresh.isRefreshing()) {
                            mSwipeRefresh.setRefreshing(false);
                        }
                        if (PAGE_NUM == 1 && !isRefresh) {
                            mLoadingDialog.dismissLoadingDialog();
                        }
                    }

                    @Override
                    protected void onNetCompleted() {
                        if (mSwipeRefresh.isRefreshing()) {
                            mSwipeRefresh.setRefreshing(false);
                        }
                        if (PAGE_NUM == 1 && !isRefresh) {
                            mLoadingDialog.dismissLoadingDialog();
                        }
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
        if (PAGE_NUM == 1) mMainT2Beans.add(new MainT2Bean("tit", MainT2Bean.VIEW_TITLE));
        if (exampListsBeans != null && exampListsBeans.size() > 0) {

            for (ExampListsBean exampListsBean : exampListsBeans) {
                int type = MainT2Bean.VIEW_ITEM;
                if (PAGE_NUM > 1) {
                    type = exampDataBean.is_vip > 0 ? MainT2Bean.VIEW_ITEM : MainT2Bean.VIEW_TO_PAY_VIP;
                }

                mMainT2Beans.add(new MainT2Bean(type, exampListsBean.create_time, exampListsBean.id, exampListsBean.image, exampListsBean.post_title));
            }

        }
        if (!mUserIsVip && mMainT2Beans != null && mMainT2Beans.size() > 6 && PAGE_NUM == 1) {
            mMainT2Beans.add(6, new MainT2Bean("vip", MainT2Bean.VIEW_VIP));
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
        if (mSwipeRefresh.isRefreshing()) {
            mSwipeRefresh.setRefreshing(false);
        }
    }


}
