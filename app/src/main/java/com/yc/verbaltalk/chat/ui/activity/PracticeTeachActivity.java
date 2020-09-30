package com.yc.verbaltalk.chat.ui.activity;

import android.content.Intent;
import android.os.Bundle;

import com.alibaba.fastjson.TypeReference;

import com.yc.verbaltalk.R;
import com.yc.verbaltalk.base.utils.UserInfoHelper;
import com.yc.verbaltalk.mine.ui.activity.BecomeVipActivity;
import com.yc.verbaltalk.skill.adapter.ChatSkillItemAdapter;
import com.yc.verbaltalk.chat.bean.ExampDataBean;
import com.yc.verbaltalk.chat.bean.ExampListsBean;
import com.yc.verbaltalk.skill.model.bean.ChatCheatsInfo;
import com.yc.verbaltalk.chat.bean.event.EventPayVipSuccess;
import com.yc.verbaltalk.base.activity.BaseSameActivity;
import com.yc.verbaltalk.skill.ui.activity.ExampleDetailActivity;
import com.yc.verbaltalk.base.view.LoadDialog;
import com.yc.verbaltalk.base.utils.CommonInfoHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import io.reactivex.observers.DisposableObserver;

import yc.com.rthttplibrary.bean.ResultInfo;
import yc.com.rthttplibrary.config.HttpConfig;

/**
 * Created by sunshey on 2019/6/18.
 */

//LoveCaseActivity
public class PracticeTeachActivity extends BaseSameActivity {

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefresh;

//    private LoveEngine mLoveEngine;

    private List<ChatCheatsInfo> mMainT2Beans;

    private int PAGE_SIZE = 10;
    private int PAGE_NUM = 1;

    private ChatSkillItemAdapter mAdapter;

    private boolean mUserIsVip = false;

    private LoadDialog mLoadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice_teach);
        initView();

    }


    protected void initView() {
//        mLoveEngine = new LoveEngine(this);

        initRecyclerView();
        initData();
    }

    private void initRecyclerView() {
        mSwipeRefresh = findViewById(R.id.child_main_t2_t1_swipe_refresh);
        mRecyclerView = findViewById(R.id.lchild_main_t2_t1_rl);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//        RecyclerViewNoBugLinearLayoutManager layoutManager = new RecyclerViewNoBugLinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        //设置增加或删除条目的动画
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new ChatSkillItemAdapter(mMainT2Beans);
        mRecyclerView.setAdapter(mAdapter);

        initListener();

    }

    private void initListener() {
        mSwipeRefresh.setColorSchemeResources(R.color.red_crimson);
        mSwipeRefresh.setOnRefreshListener(() -> {
            PAGE_NUM = 1;
            netData(true);
        });
        mAdapter.setOnLoadMoreListener(() -> netData(false), mRecyclerView);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            ChatCheatsInfo mainT2Bean = mAdapter.getItem(position);
            if (mainT2Bean != null) {
                if (ChatCheatsInfo.VIEW_ITEM == mainT2Bean.type) {
                    ExampleDetailActivity.startExampleDetailActivity(PracticeTeachActivity.this, mainT2Bean.id, mainT2Bean.post_title);
                } else if (ChatCheatsInfo.VIEW_TO_PAY_VIP == mainT2Bean.type || ChatCheatsInfo.VIEW_VIP == mainT2Bean.type) {
                    if (UserInfoHelper.isLogin(PracticeTeachActivity.this))
                        startActivity(new Intent(PracticeTeachActivity.this, BecomeVipActivity.class));
                }
            }
        });
    }


    private void initData() {
        CommonInfoHelper.getO(this, "main2_example_lists", new TypeReference<List<ChatCheatsInfo>>() {
        }.getType(), (CommonInfoHelper.onParseListener<List<ChatCheatsInfo>>) o -> {
            if (o != null && o.size() > 0) {
                mAdapter.setNewData(o);
            }

        });
        netData(false);
    }


    private void netData(final boolean isRefresh) {


        if (PAGE_NUM == 1 && !isRefresh) {
            mLoadingDialog = new LoadDialog(this);
            mLoadingDialog.showLoadingDialog();
        }

        mLoveEngine.exampLists(UserInfoHelper.getUid(), String.valueOf(PAGE_NUM), String.valueOf(PAGE_SIZE))
                .subscribe(new DisposableObserver<ResultInfo<ExampDataBean>>() {
                    @Override
                    public void onComplete() {
                        if (mSwipeRefresh.isRefreshing()) {
                            mSwipeRefresh.setRefreshing(false);
                        }
                        if (PAGE_NUM == 1 && !isRefresh) {
                            mLoadingDialog.dismissLoadingDialog();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (mSwipeRefresh.isRefreshing()) {
                            mSwipeRefresh.setRefreshing(false);
                        }
                        if (PAGE_NUM == 1 && !isRefresh) {
                            mLoadingDialog.dismissLoadingDialog();
                        }
                    }

                    @Override
                    public void onNext(ResultInfo<ExampDataBean> exampDataBeanAResultInfo) {
                        if (PAGE_NUM == 1 && !isRefresh) {
                            mLoadingDialog.dismissLoadingDialog();
                        }
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
        if (PAGE_NUM == 1) mMainT2Beans.add(new ChatCheatsInfo("tit", ChatCheatsInfo.VIEW_TITLE));
        if (exampListsBeans != null && exampListsBeans.size() > 0) {

            for (ExampListsBean exampListsBean : exampListsBeans) {
                int type = ChatCheatsInfo.VIEW_ITEM;
                if (PAGE_NUM > 1) {
                    type = exampDataBean.is_vip > 0 ? ChatCheatsInfo.VIEW_ITEM : ChatCheatsInfo.VIEW_TO_PAY_VIP;
                }

                mMainT2Beans.add(new ChatCheatsInfo(type, exampListsBean.create_time, exampListsBean.id, exampListsBean.image, exampListsBean.post_title));
            }

        }
        if (!mUserIsVip && mMainT2Beans != null && mMainT2Beans.size() > 6 && PAGE_NUM == 1) {
            mMainT2Beans.add(6, new ChatCheatsInfo("vip", ChatCheatsInfo.VIEW_VIP));
        }


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
        if (mSwipeRefresh.isRefreshing()) {
            mSwipeRefresh.setRefreshing(false);
        }
    }


    @Override
    protected String offerActivityTitle() {
        return "实战学习";
    }


    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPaySuccess(EventPayVipSuccess eventPayVipSuccess) {
        PAGE_NUM = 1;
        netData(false);
    }
}
