package com.yc.verbaltalk.ui.frament.main;

import com.alibaba.fastjson.TypeReference;
import com.umeng.analytics.MobclickAgent;
import com.yc.verbaltalk.R;
import com.yc.verbaltalk.adaper.rv.LoveHealingAdapter;
import com.yc.verbaltalk.model.base.MySubscriber;
import com.yc.verbaltalk.model.bean.AResultInfo;
import com.yc.verbaltalk.model.bean.LoveHealingBean;
import com.yc.verbaltalk.model.constant.ConstantKey;
import com.yc.verbaltalk.model.engin.LoveEngine;
import com.yc.verbaltalk.model.single.YcSingle;
import com.yc.verbaltalk.ui.activity.LoveUpDownPhotoActivity;
import com.yc.verbaltalk.ui.frament.base.BaseMainT2ChildFragment;
import com.yc.verbaltalk.ui.view.LoadDialog;
import com.yc.verbaltalk.utils.CommonInfoHelper;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

/**
 * Created by mayn on 2019/6/18.
 */

//LoveHealingActivity
public class ChildMainT2T2Fragment extends BaseMainT2ChildFragment {

    private List<LoveHealingBean> loveHealingBeans;
    private LoveHealingAdapter mAdapter;
    private int PAGE_SIZE = 10;
    private int PAGE_NUM = 1;

    //    private MainT2MoreItemAdapter mAdapter;

    private LoveEngine mLoveEngin;
    private RecyclerView mRecyclerView;
    private LoadDialog mLoadingDialog;

    private SwipeRefreshLayout mSwipe;

    @Override
    protected int setContentView() {
        return R.layout.activity_practice_love;
    }

    @Override
    protected void initViews() {
        mLoveEngin = new LoveEngine(mMainActivity);

        initRecyclerView();
    }

    private void initRecyclerView() {
        mRecyclerView = rootView.findViewById(R.id.child_main_t2_t2_rv);
        mSwipe = rootView.findViewById(R.id.swipeRefreshLayout);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mMainActivity);
        mRecyclerView.setLayoutManager(layoutManager);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        mAdapter = new LoveHealingAdapter(loveHealingBeans);
        mRecyclerView.setAdapter(mAdapter);
        //设置增加或删除条目的动画
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        initListener();
    }

    private void initListener() {
        mSwipe.setColorSchemeResources(R.color.red_crimson);
        mSwipe.setOnRefreshListener(() -> {
            PAGE_NUM = 1;
            netData(true);
        });

        mAdapter.setOnLoadMoreListener(() -> netData(false), mRecyclerView);

        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            LoveHealingBean item = mAdapter.getItem(position);
            if (item != null) {
                if (LoveHealingBean.VIEW_ITEM == item.type) {
                    LoveUpDownPhotoActivity.startLoveUpDownPhotoActivity(mMainActivity, position - 2, "lovewords/recommend");
                }
            }
        });

    }


    @Override
    protected void lazyLoad() {
        MobclickAgent.onEvent(mMainActivity, ConstantKey.UM_HONEYEDWORDS_ID);
        netData(false);
    }

    private void netData(final boolean isRefesh) {
//        loveHealingBeans = (List<LoveHealingBean>) mCacheWorker.getCache(mMainActivity, "maint2_t2_lovewords_recommend");

        if (PAGE_NUM == 1) {

            CommonInfoHelper.getO(mMainActivity, "maint2_t2_lovewords_recommend", new TypeReference<List<LoveHealingBean>>() {
            }.getType(), (CommonInfoHelper.onParseListener<List<LoveHealingBean>>) o -> {
                loveHealingBeans = o;
                if (PAGE_NUM == 1 && loveHealingBeans != null && loveHealingBeans.size() > 0) {
                    mAdapter.setNewData(loveHealingBeans);
                }
            });
        }


        if (PAGE_NUM == 1 && !isRefesh) {
            mLoadingDialog = new LoadDialog(mMainActivity);
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

        if (PAGE_NUM == 1) {
            mAdapter.setNewData(loveHealingBeans);
            CommonInfoHelper.setO(mMainActivity, loveHealingBeans, "maint2_t2_lovewords_recommend");
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


}
