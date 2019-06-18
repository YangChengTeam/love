package com.yc.love.ui.frament.main;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.umeng.analytics.MobclickAgent;
import com.yc.love.R;
import com.yc.love.adaper.rv.MainT2MoreItemAdapter;
import com.yc.love.adaper.rv.base.RecyclerViewItemListener;
import com.yc.love.adaper.rv.holder.BaseViewHolder;
import com.yc.love.adaper.rv.holder.CaseTitleViewHolder;
import com.yc.love.adaper.rv.holder.EndEmptyViewHolder;
import com.yc.love.adaper.rv.holder.MainT2ToPayVipHolder;
import com.yc.love.adaper.rv.holder.MainT2ViewHolder;
import com.yc.love.adaper.rv.holder.ProgressBarViewHolder;
import com.yc.love.adaper.rv.holder.VipViewHolder;
import com.yc.love.cache.CacheWorker;
import com.yc.love.model.base.MySubscriber;
import com.yc.love.model.bean.AResultInfo;
import com.yc.love.model.bean.ExampDataBean;
import com.yc.love.model.bean.ExampListsBean;
import com.yc.love.model.bean.MainT2Bean;
import com.yc.love.model.constant.ConstantKey;
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
    private boolean mIsHandRefresh;  //是否手动刷新
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
    private LoadDialog mLoadingDialog;


    @Override
    protected int setContentView() {
        return R.layout.fragment_child_main_t2_t1;
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

        mSwipeRefresh.setColorSchemeResources(R.color.red_crimson);
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mIsHandRefresh = true;
                mIsAddToPayVipItem = false;
                loadMoreEnd = false;
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
        if (!mIsHandRefresh) {
            mMainT2Beans = (List<MainT2Bean>) mCacheWorker.getCache(mMainActivity, "main2_example_lists");
            if (mMainT2Beans != null && mMainT2Beans.size() != 0) {
                initRecyclerViewData();
            } else {
                mLoadingDialog = new LoadDialog(mMainActivity);
                mLoadingDialog.showLoadingDialog();
            }
        }
        mLoveEngin.exampLists(String.valueOf(YcSingle.getInstance().id), String.valueOf(PAGE_NUM), String.valueOf(PAGE_SIZE), "example/lists")
                .subscribe(new MySubscriber<AResultInfo<ExampDataBean>>(mLoadingDialog) {
                    @Override
                    protected void onNetNext(AResultInfo<ExampDataBean> exampDataBeanAResultInfo) {
                        mIsNetData = true;
                        ExampDataBean exampDataBean = exampDataBeanAResultInfo.data;
                        int isVip = exampDataBean.is_vip;
                        if (isVip > 0) {
                            mUserIsVip = true;
                        }
                        List<ExampListsBean> exampListsBeans = exampDataBean.lists;
                        mMainT2Beans = new ArrayList<>();
                        mMainT2Beans.add(new MainT2Bean("tit", 0));
                        if (exampListsBeans != null && exampListsBeans.size() != 0) {
                            for (int i = 0; i < exampListsBeans.size(); i++) {
                                ExampListsBean exampListsBean = exampListsBeans.get(i);
                                mMainT2Beans.add(new MainT2Bean(1, exampListsBean.create_time, exampListsBean.id, exampListsBean.image, exampListsBean.post_title));
                            }
                        }
                        if (!mUserIsVip && mMainT2Beans != null && mMainT2Beans.size() > 6) {
                            mMainT2Beans.add(6, new MainT2Bean("vip", 2));
//                            mMainT2Beans.add(new MainT2Bean("探探遇到的超顶级时尚美女，用浪漫话术成功拐回家", 3, mMainT2Beans.size()));
//                            mMainT2Beans.add(new MainT2Bean("探探遇到的超顶级时尚美女，用浪漫话术成功拐回家", 3, mMainT2Beans.size()));
//                            mMainT2Beans.add(new MainT2Bean("探探遇到的超顶级时尚美女，用浪漫话术成功拐回家", 3, mMainT2Beans.size()));
                        }


                        mCacheWorker.setCache("main2_example_lists", mMainT2Beans);
                        initRecyclerViewData();
                    }

                    @Override
                    protected void onNetError(Throwable e) {
                        mSwipeRefresh.setRefreshing(false);
                    }

                    @Override
                    protected void onNetCompleted() {
                        mSwipeRefresh.setRefreshing(false);
                    }
                });
    }

    private void initRecyclerViewData() {
        mSwipeRefresh.setRefreshing(false);
        mAdapter = new MainT2MoreItemAdapter(mMainT2Beans, mRecyclerView) {
            @Override
            public BaseViewHolder getTitleHolder(ViewGroup parent) {
                return new CaseTitleViewHolder(mMainActivity, null, parent);
            }

            @Override
            public BaseViewHolder getHolder(ViewGroup parent) {
                return new MainT2ViewHolder(mMainActivity, recyclerViewItemListener, parent);
            }

            @Override
            public BaseViewHolder getToPayVipHolder(ViewGroup parent) {
                return new MainT2ToPayVipHolder(mMainActivity, recyclerViewToVipListener, parent);
            }

            @Override
            protected RecyclerView.ViewHolder getVipHolder(ViewGroup parent) {
                VipViewHolder vipViewHolder = new VipViewHolder(mMainActivity, recyclerViewToVipListener, parent);
                return vipViewHolder;
            }

            @Override
            protected RecyclerView.ViewHolder getEndHolder(ViewGroup parent) {
                return new EndEmptyViewHolder(mMainActivity, null, parent);
            }


            @Override
            protected RecyclerView.ViewHolder getBarViewHolder(ViewGroup parent) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_test_item_footer, parent, false);
                ProgressBarViewHolder progressBarViewHolder = new ProgressBarViewHolder(view);
                return progressBarViewHolder;
            }


        };
        mRecyclerView.setAdapter(mAdapter);
//        if (!mUserIsVip) {
//            return;
//        }
        if (mMainT2Beans.size() < PAGE_SIZE) {
            Log.d("ssss", "loadMoreData: data---end");
        } else {
            mAdapter.setOnMoreDataLoadListener(new MainT2MoreItemAdapter.OnLoadMoreDataListener() {
                @Override
                public void loadMoreData() {
                    if (loadDataEnd == false) {
                        return;
                    }
                    if (showProgressBar == false) {
                        //加入null值此时adapter会判断item的type
                        mMainT2Beans.add(null);
//                        mAdapter.notifyDataSetChanged();
                        mAdapter.notifyItemChanged(mMainT2Beans.size() - 1);
                        showProgressBar = true;
                    }
                    if (!loadMoreEnd) {
                        netLoadMore();
                    } else {
                        Log.d("mylog", "loadMoreData: loadMoreEnd end 已加载全部数据 ");
                        mMainT2Beans.remove(mMainT2Beans.size() - 1);
//                        mAdapter.notifyDataSetChanged();
                        mAdapter.notifyItemChanged(mMainT2Beans.size() - 1);
                    }
                }
            });
        }
        loadDataEnd = true;
    }

    private void netLoadMore() {
        MobclickAgent.onEvent(mMainActivity, ConstantKey.UM_PRACTICE_ID);
        mLoveEngin.exampLists(String.valueOf(YcSingle.getInstance().id), String.valueOf(++PAGE_NUM), String.valueOf(PAGE_SIZE), "example/lists").subscribe(new MySubscriber<AResultInfo<ExampDataBean>>(mMainActivity) {
            @Override
            protected void onNetNext(AResultInfo<ExampDataBean> exampDataBeanAResultInfo) {
                ExampDataBean exampDataBean = exampDataBeanAResultInfo.data;

                List<ExampListsBean> exampListsBeans = exampDataBean.lists;
                List<MainT2Bean> netLoadMoreData = new ArrayList<>();
                if (exampListsBeans != null && exampListsBeans.size() > 0) {
                    int type = exampDataBean.is_vip > 0 ? 1 : 3;
                    for (int i = 0; i < exampListsBeans.size(); i++) {
                        ExampListsBean exampListsBean = exampListsBeans.get(i);
                        netLoadMoreData.add(new MainT2Bean(type, exampListsBean.create_time, exampListsBean.id, exampListsBean.image, exampListsBean.post_title));
                    }
                }
                MainT2Bean mainT2Bean = mMainT2Beans.get(mMainT2Beans.size() - 1);
                if (mainT2Bean == null) {
                    showProgressBar = false;
                    mMainT2Beans.remove(mMainT2Beans.size() - 1);
//            mAdapter.notifyItemChanged(mMainT2Beans.size() - 1);
                    mAdapter.notifyDataSetChanged();
                }
                if (netLoadMoreData.size() < PAGE_SIZE) {
                    loadMoreEnd = true;
                }
                if (netLoadMoreData != null && netLoadMoreData.size() != 0) {
                    mMainT2Beans.addAll(netLoadMoreData);
                    mAdapter.notifyDataSetChanged();
                }
                mAdapter.setLoaded();
            }

            @Override
            protected void onNetError(Throwable e) {

            }

            @Override
            protected void onNetCompleted() {

            }
        });
    }


    RecyclerViewItemListener recyclerViewItemListener = new RecyclerViewItemListener() {
        @Override
        public void onItemClick(int position) {
            if (position < 0) {
                return;
            }
            MainT2Bean mainT2Bean = mMainT2Beans.get(position);
            ExampleDetailActivity.startExampleDetailActivity(mMainActivity, mainT2Bean.id, mainT2Bean.post_title);
        }

        @Override
        public void onItemLongClick(int position) {

        }
    };
    RecyclerViewItemListener recyclerViewToVipListener = new RecyclerViewItemListener() {
        @Override
        public void onItemClick(int position) {
            if (position < 0) {
                return;
            }
            startActivity(new Intent(mMainActivity, BecomeVipActivity.class));
        }

        @Override
        public void onItemLongClick(int position) {

        }
    };
}
