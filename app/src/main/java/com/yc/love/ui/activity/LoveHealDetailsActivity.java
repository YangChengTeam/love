package com.yc.love.ui.activity;

import android.content.Context;
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
import com.yc.love.R;
import com.yc.love.adaper.rv.LoveHealDetailsAdapterNew;
import com.yc.love.model.base.MySubscriber;
import com.yc.love.model.bean.AResultInfo;
import com.yc.love.model.bean.LoveHealDetBean;
import com.yc.love.model.engin.LoveEngin;
import com.yc.love.model.single.YcSingle;
import com.yc.love.ui.activity.base.BaseSameActivity;

import java.util.List;

public class LoveHealDetailsActivity extends BaseSameActivity {

    private String mTitle;
    private String mCategoryId;
    private LoveEngin mLoveEngin;
    private LoveHealDetailsAdapterNew mAdapter;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefresh;
    private int PAGE_SIZE = 8;
    private int PAGE_NUM = 1;
    private List<LoveHealDetBean> mLoveHealDetBeans;


    @Override
    protected void initIntentData() {
        Intent intent = getIntent();
        mTitle = intent.getStringExtra("title");
        mCategoryId = intent.getStringExtra("category_id");
    }

    public static void startLoveHealDetailsActivity(Context context, String title, String categoryId) {
        Intent intent = new Intent(context, LoveHealDetailsActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("category_id", categoryId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_love_heal_details);
        mLoveEngin = new LoveEngin(this);
        initViews();
        initData();
        initListener();
    }

    private void initData() {
        netData();
    }


    private void initViews() {
        ImageView ivToWx = findViewById(R.id.love_heal_details_iv_to_wx);
        ivToWx.setOnClickListener(this);
        initRecyclerView();
    }

    private void initListener() {
        mSwipeRefresh.setColorSchemeResources(R.color.red_crimson);
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                obtainWalletData();
                /*if (mLoveHealDetBeans != null) {
                    mLoveHealDetBeans.clear();
                    mAdapter.notifyDataSetChanged();
                }*/

                PAGE_NUM = 1;
                netData();
            }
        });
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                netData();
            }
        }, mRecyclerView);

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                LoveHealDetBean loveHealDetBean = mAdapter.getItem(position);
                if (loveHealDetBean != null && LoveHealDetBean.VIEW_VIP == loveHealDetBean.type)
                    //TODO 购买VIP刷新数据
                    startActivity(new Intent(LoveHealDetailsActivity.this, BecomeVipActivity.class));
            }
        });
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                LoveHealDetBean loveHealDetBean = mAdapter.getItem(position);
                if (loveHealDetBean != null && LoveHealDetBean.VIEW_VIP == loveHealDetBean.type)
                    //TODO 购买VIP刷新数据
                    startActivity(new Intent(LoveHealDetailsActivity.this, BecomeVipActivity.class));
            }
        });

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.love_heal_details_iv_to_wx:
                showToWxServiceDialog();
                break;
        }
    }

    private void initRecyclerView() {

        mSwipeRefresh = findViewById(R.id.love_heal_details_swipe_refresh);
        mRecyclerView = findViewById(R.id.love_heal_details_rl);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        //设置增加或删除条目的动画
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new LoveHealDetailsAdapterNew(mLoveHealDetBeans,mTitle);
        mRecyclerView.setAdapter(mAdapter);


    }

    private void netData() {
        int id = YcSingle.getInstance().id;
        if (PAGE_NUM == 1)
            mLoadingDialog.showLoadingDialog();
        mLoveEngin.loveListCategory(String.valueOf(id), mCategoryId, String.valueOf(PAGE_NUM), String.valueOf(PAGE_SIZE), "Dialogue/lists").subscribe(new MySubscriber<AResultInfo<List<LoveHealDetBean>>>(mLoadingDialog) {


            @Override
            protected void onNetNext(AResultInfo<List<LoveHealDetBean>> listAResultInfo) {
                if (PAGE_NUM == 1 && mLoadingDialog != null) {
                    mLoadingDialog.dismissLoadingDialog();
                }
                if (mSwipeRefresh.isRefreshing())
                    mSwipeRefresh.setRefreshing(false);
                createNewData(listAResultInfo.data);
//                mLoveHealDetBeans = listAResultInfo.data;
//                Log.d("mylog", "onNetNext: loveHealDetBeans.size() " + mLoveHealDetBeans.size());
            }

            @Override
            protected void onNetError(Throwable e) {
                if (PAGE_NUM == 1 && mLoadingDialog != null) {
                    mLoadingDialog.dismissLoadingDialog();
                }
                if (mSwipeRefresh.isRefreshing())
                    mSwipeRefresh.setRefreshing(false);
            }

            @Override
            protected void onNetCompleted() {
                if (PAGE_NUM == 1 && mLoadingDialog != null) {
                    mLoadingDialog.dismissLoadingDialog();
                }
                if (mSwipeRefresh.isRefreshing())
                    mSwipeRefresh.setRefreshing(false);
            }
        });
    }

    private void createNewData(List<LoveHealDetBean> loveHealDetBeans) {

        if (loveHealDetBeans != null && loveHealDetBeans.size() > 0) {
            for (LoveHealDetBean loveHealDetBean : loveHealDetBeans) {
                if (loveHealDetBean.is_vip == 0) {
                    loveHealDetBean.type = LoveHealDetBean.VIEW_ITEM;
                } else {
                    loveHealDetBean.type = LoveHealDetBean.VIEW_VIP;
                }

            }
        }
        mLoveHealDetBeans = loveHealDetBeans;
        if (PAGE_NUM == 1) {
            mAdapter.setNewData(mLoveHealDetBeans);
        } else {
            mAdapter.addData(mLoveHealDetBeans);
        }
        if (loveHealDetBeans != null && loveHealDetBeans.size() == PAGE_SIZE) {
            mAdapter.loadMoreComplete();
            PAGE_NUM++;
        } else {
            mAdapter.loadMoreEnd();
        }


    }

    @Override
    protected String offerActivityTitle() {
        return mTitle;
    }

}
