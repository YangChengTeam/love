package com.yc.verbaltalk.ui.activity;

import android.content.Intent;
import android.os.Bundle;

import com.alibaba.fastjson.TypeReference;
import com.yc.verbaltalk.R;
import com.yc.verbaltalk.adaper.rv.MainT2MoreItemAdapter;
import com.yc.verbaltalk.model.base.MySubscriber;
import com.yc.verbaltalk.model.bean.AResultInfo;
import com.yc.verbaltalk.model.bean.ExampDataBean;
import com.yc.verbaltalk.model.bean.ExampListsBean;
import com.yc.verbaltalk.model.bean.MainT2Bean;
import com.yc.verbaltalk.model.bean.event.EventPayVipSuccess;
import com.yc.verbaltalk.model.single.YcSingle;
import com.yc.verbaltalk.ui.activity.base.BaseSameActivity;
import com.yc.verbaltalk.ui.view.LoadDialog;
import com.yc.verbaltalk.utils.CommonInfoHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

/**
 * Created by mayn on 2019/6/18.
 */

//LoveCaseActivity
public class PracticeTeachActivity extends BaseSameActivity {

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefresh;

//    private LoveEngine mLoveEngine;

    private List<MainT2Bean> mMainT2Beans;

    private int PAGE_SIZE = 10;
    private int PAGE_NUM = 1;

    private MainT2MoreItemAdapter mAdapter;

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
        mAdapter = new MainT2MoreItemAdapter(mMainT2Beans);
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
            MainT2Bean mainT2Bean = mAdapter.getItem(position);
            if (mainT2Bean != null) {
                if (MainT2Bean.VIEW_ITEM == mainT2Bean.type) {
                    ExampleDetailActivity.startExampleDetailActivity(PracticeTeachActivity.this, mainT2Bean.id, mainT2Bean.post_title);
                } else if (MainT2Bean.VIEW_TO_PAY_VIP == mainT2Bean.type || MainT2Bean.VIEW_VIP == mainT2Bean.type) {
                    startActivity(new Intent(PracticeTeachActivity.this, BecomeVipActivity.class));
                }
            }
        });
    }


    private void initData() {
        CommonInfoHelper.getO(this, "main2_example_lists", new TypeReference<List<MainT2Bean>>() {
        }.getType(), (CommonInfoHelper.onParseListener<List<MainT2Bean>>) o -> {
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

        mLoveEngine.exampLists(String.valueOf(YcSingle.getInstance().id), String.valueOf(PAGE_NUM), String.valueOf(PAGE_SIZE), "example/lists")
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
