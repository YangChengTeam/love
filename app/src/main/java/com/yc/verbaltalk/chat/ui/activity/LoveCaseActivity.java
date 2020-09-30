package com.yc.verbaltalk.chat.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.alibaba.fastjson.TypeReference;
import com.yc.verbaltalk.R;
import com.yc.verbaltalk.base.activity.BaseSameActivity;
import com.yc.verbaltalk.base.engine.LoveEngine;
import com.yc.verbaltalk.base.utils.CommonInfoHelper;
import com.yc.verbaltalk.base.utils.UserInfoHelper;
import com.yc.verbaltalk.chat.bean.ExampDataBean;
import com.yc.verbaltalk.chat.bean.ExampListsBean;
import com.yc.verbaltalk.skill.model.bean.ChatCheatsInfo;
import com.yc.verbaltalk.mine.ui.activity.BecomeVipActivity;
import com.yc.verbaltalk.skill.adapter.ChatSkillItemAdapter;
import com.yc.verbaltalk.skill.ui.activity.ExampleDetailActivity;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import io.reactivex.observers.DisposableObserver;
import yc.com.rthttplibrary.bean.ResultInfo;
import yc.com.rthttplibrary.config.HttpConfig;

public class LoveCaseActivity extends BaseSameActivity {


    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefresh;

    private LoveEngine mLoveEngin;

    private List<ChatCheatsInfo> mMainT2Beans;

    private int PAGE_SIZE = 10;
    private int PAGE_NUM = 1;

    private ChatSkillItemAdapter mAdapter;

    private boolean mUserIsVip = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_love_case);

        mLoveEngin = new LoveEngine(this);

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
                showToWxServiceDialog(null);
                break;
        }
    }

    private void initRecyclerView() {
        mSwipeRefresh = findViewById(R.id.love_case_swipe_refresh);
        mRecyclerView = findViewById(R.id.love_case_rl);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//        RecyclerViewNoBugLinearLayoutManager layoutManager = new RecyclerViewNoBugLinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        //设置增加或删除条目的动画
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new ChatSkillItemAdapter(null);
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
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            ChatCheatsInfo mainT2Bean = mAdapter.getItem(position);
            if (mainT2Bean != null) {
                if (ChatCheatsInfo.VIEW_ITEM == mainT2Bean.type) {
                    ExampleDetailActivity.startExampleDetailActivity(LoveCaseActivity.this, mainT2Bean.id, mainT2Bean.post_title);
                } else if (ChatCheatsInfo.VIEW_TO_PAY_VIP == mainT2Bean.type || ChatCheatsInfo.VIEW_VIP == mainT2Bean.type) {
                    if (UserInfoHelper.isLogin(LoveCaseActivity.this))
                        startActivity(new Intent(LoveCaseActivity.this, BecomeVipActivity.class));
                }
            }
        });

        mAdapter.setOnLoadMoreListener(this::netData, mRecyclerView);


    }

    private void netData() {

//        mMainT2Beans = (List<MainT2Bean>) mCacheWorker.getCache(this, "main2_example_lists");
        mLoadingDialog.showLoadingDialog();
        if (PAGE_NUM == 1) {
            CommonInfoHelper.getO(this, "main2_example_lists", new TypeReference<List<ChatCheatsInfo>>() {
            }.getType(), new CommonInfoHelper.onParseListener<List<ChatCheatsInfo>>() {
                @Override
                public void onParse(List<ChatCheatsInfo> o) {
                    mMainT2Beans = o;
                    if (mMainT2Beans != null && mMainT2Beans.size() > 0) {
//                initRecyclerViewData();
                        mAdapter.setNewData(mMainT2Beans);
                    }
                }
            });
        }
        mLoveEngin.exampLists(UserInfoHelper.getUid(), String.valueOf(PAGE_NUM), String.valueOf(PAGE_SIZE))
                .subscribe(new DisposableObserver<ResultInfo<ExampDataBean>>() {
                    @Override
                    public void onComplete() {
                        mLoadingDialog.dismissLoadingDialog();
                        if (mSwipeRefresh.isRefreshing()) mSwipeRefresh.setRefreshing(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mLoadingDialog.dismissLoadingDialog();
                        if (mSwipeRefresh.isRefreshing()) mSwipeRefresh.setRefreshing(false);
                    }

                    @Override
                    public void onNext(ResultInfo<ExampDataBean> exampDataBeanAResultInfo) {
                        mLoadingDialog.dismissLoadingDialog();
                        if (exampDataBeanAResultInfo != null && exampDataBeanAResultInfo.code == HttpConfig.STATUS_OK && exampDataBeanAResultInfo.data != null) {
                            ExampDataBean exampDataBean = exampDataBeanAResultInfo.data;
                            createNewData(exampDataBean);
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
        if (PAGE_NUM == 1)
            mMainT2Beans.add(new ChatCheatsInfo("tit", ChatCheatsInfo.VIEW_TITLE));
        if (exampListsBeans != null && exampListsBeans.size() != 0) {
            for (int i = 0; i < exampListsBeans.size(); i++) {
                ExampListsBean exampListsBean = exampListsBeans.get(i);
                mMainT2Beans.add(new ChatCheatsInfo(ChatCheatsInfo.VIEW_ITEM, exampListsBean.create_time, exampListsBean.id, exampListsBean.image, exampListsBean.post_title));
            }
        }
        if (!mUserIsVip && mMainT2Beans != null && mMainT2Beans.size() > 6 && PAGE_NUM == 1) {
            mMainT2Beans.add(6, new ChatCheatsInfo("vip", ChatCheatsInfo.VIEW_VIP));
            mMainT2Beans.add(new ChatCheatsInfo("toPayVip", ChatCheatsInfo.VIEW_TO_PAY_VIP, mMainT2Beans.size()));
        }


//        if (PAGE_NUM == 1)
//            mCacheWorker.setCache("main2_example_lists", mMainT2Beans);

        if (PAGE_NUM == 1) {
            mAdapter.setNewData(mMainT2Beans);
            CommonInfoHelper.setO(this, mMainT2Beans, "main2_example_lists");
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
