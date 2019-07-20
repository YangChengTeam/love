package com.yc.love.ui.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.umeng.analytics.MobclickAgent;
import com.yc.love.R;
import com.yc.love.adaper.rv.LoveHealingAdapterNew;
import com.yc.love.cache.CacheWorker;
import com.yc.love.model.base.MySubscriber;
import com.yc.love.model.bean.AResultInfo;
import com.yc.love.model.bean.LoveHealingBean;
import com.yc.love.model.constant.ConstantKey;
import com.yc.love.model.engin.LoveEngin;
import com.yc.love.model.single.YcSingle;
import com.yc.love.ui.activity.base.BaseSameActivity;
import com.yc.love.ui.view.LoadDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mayn on 2019/6/18.
 */

//LoveHealingActivity
public class PracticeLoveActivity extends BaseSameActivity {

    private List<LoveHealingBean> loveHealingBeans;
    private LoveHealingAdapterNew mAdapter;
    private int PAGE_SIZE = 15;
    private int PAGE_NUM = 1;

    //    private MainT2MoreItemAdapter mAdapter;

    private LoveEngin mLoveEngin;
    private CacheWorker mCacheWorker;
    private RecyclerView mRecyclerView;
    private LoadDialog mLoadingDialog;

    private SwipeRefreshLayout mSwipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice_love);
        initViews();
    }


    protected void initViews() {
        mLoveEngin = new LoveEngin(this);
        mCacheWorker = new CacheWorker();

        initRecyclerView();
        lazyLoad();

    }

    private void initRecyclerView() {
        mRecyclerView = findViewById(R.id.child_main_t2_t2_rv);
        mSwipe = findViewById(R.id.swipeRefreshLayout);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        mAdapter = new LoveHealingAdapterNew(loveHealingBeans);
        mRecyclerView.setAdapter(mAdapter);
        //设置增加或删除条目的动画
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        initListener();
    }

    private void initListener() {
        mSwipe.setColorSchemeResources(R.color.red_crimson);
        mSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
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
                LoveHealingBean item = mAdapter.getItem(position);
                if (item != null) {
                    if (LoveHealingBean.VIEW_ITEM == item.type) {
                        LoveUpDownPhotoActivity.startLoveUpDownPhotoActivity(PracticeLoveActivity.this, position - 2, "lovewords/recommend");
                    }
                }
            }
        });

    }


    protected void lazyLoad() {
        MobclickAgent.onEvent(this, ConstantKey.UM_HONEYEDWORDS_ID);
        netData(false);
    }

    private void netData(final boolean isRefesh) {
        loveHealingBeans = (List<LoveHealingBean>) mCacheWorker.getCache(this, "maint2_t2_lovewords_recommend");
        if (PAGE_NUM == 1 && loveHealingBeans != null && loveHealingBeans.size() > 0) {

            mAdapter.setNewData(loveHealingBeans);
        }
        if (PAGE_NUM == 1 && !isRefesh) {
            mLoadingDialog = new LoadDialog(this);
            mLoadingDialog.showLoadingDialog();
        }
        mLoveEngin.recommendLovewords(String.valueOf(YcSingle.getInstance().id), String.valueOf(PAGE_NUM), String.valueOf(PAGE_SIZE), "lovewords/recommend")
                .subscribe(new MySubscriber<AResultInfo<List<LoveHealingBean>>>(mLoadingDialog) {
                    @Override
                    protected void onNetNext(AResultInfo<List<LoveHealingBean>> listAResultInfo) {
                        if (PAGE_NUM == 1 && !isRefesh) {
                            mLoadingDialog.dismissLoadingDialog();
                        }
                        if (mSwipe.isRefreshing()) {
                            mSwipe.setRefreshing(false);
                        }

                        List<LoveHealingBean> loveHealingBeanList = listAResultInfo.data;
                        createNewData(loveHealingBeanList);
                    }

                    @Override
                    protected void onNetError(Throwable e) {
                        if (PAGE_NUM == 1 && !isRefesh) {
                            mLoadingDialog.dismissLoadingDialog();
                        }
                        if (mSwipe.isRefreshing()) {
                            mSwipe.setRefreshing(false);
                        }
                    }

                    @Override
                    protected void onNetCompleted() {
                        if (PAGE_NUM == 1 && !isRefesh) {
                            mLoadingDialog.dismissLoadingDialog();
                        }
                        if (mSwipe.isRefreshing()) {
                            mSwipe.setRefreshing(false);
                        }
                    }
                });
    }

    private void createNewData(List<LoveHealingBean> loveHealingBeanList) {
        loveHealingBeans = new ArrayList<>();
        if (PAGE_NUM == 1) {
            loveHealingBeans.add(new LoveHealingBean(LoveHealingBean.VIEW_TITLE, "title_img"));
            loveHealingBeans.add(new LoveHealingBean(LoveHealingBean.VIEW_ITEM_ITEM, "为你推荐 "));
        }
        if (loveHealingBeanList != null) {
            for (int i = 0; i < loveHealingBeanList.size(); i++) {
                LoveHealingBean loveHealingBean = loveHealingBeanList.get(i);
                loveHealingBeans.add(new LoveHealingBean(LoveHealingBean.VIEW_ITEM, loveHealingBean.chat_count, loveHealingBean.chat_name, loveHealingBean.id, loveHealingBean.quiz_sex, loveHealingBean.search_type));
            }
        }
        if (PAGE_NUM == 1)
            mCacheWorker.setCache("maint2_t2_lovewords_recommend", loveHealingBeans);
        if (PAGE_NUM == 1) {
            mAdapter.setNewData(loveHealingBeans);
        } else {
            mAdapter.addData(loveHealingBeans);
        }
        if (loveHealingBeanList != null && loveHealingBeanList.size() > 0) {
            mAdapter.loadMoreComplete();
            PAGE_NUM++;
        } else {
            mAdapter.loadMoreEnd();
        }

    }


    @Override
    protected String offerActivityTitle() {
        return "实战情话";
    }
}
