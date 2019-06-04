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
import android.widget.LinearLayout;

import com.umeng.analytics.MobclickAgent;
import com.yc.love.R;
import com.yc.love.adaper.rv.MainT2MoreItemAdapter;
import com.yc.love.adaper.rv.base.RecyclerViewItemListener;
import com.yc.love.adaper.rv.holder.BaseViewHolder;
import com.yc.love.adaper.rv.holder.MainT2ToPayVipHolder;
import com.yc.love.adaper.rv.holder.ProgressBarViewHolder;
import com.yc.love.adaper.rv.holder.MainT2ViewHolder;
import com.yc.love.adaper.rv.holder.MainT2TitleViewHolder;
import com.yc.love.adaper.rv.holder.VipViewHolder;
import com.yc.love.cache.CacheWorker;
import com.yc.love.cache.SDFileStrategy;
import com.yc.love.model.base.MySubscriber;
import com.yc.love.model.bean.AResultInfo;
import com.yc.love.model.bean.ExampDataBean;
import com.yc.love.model.bean.ExampListsBean;
import com.yc.love.model.bean.MainT2Bean;
import com.yc.love.model.bean.event.NetWorkChangT2Bean;
import com.yc.love.model.constant.ConstantKey;
import com.yc.love.model.engin.LoveEngin;
import com.yc.love.model.single.YcSingle;
import com.yc.love.ui.activity.BecomeVipActivity;
import com.yc.love.ui.activity.ExampleDetailActivity;
import com.yc.love.ui.frament.base.BaseMainFragment;
import com.yc.love.ui.view.LoadDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mayn on 2019/4/23.
 */

public class MainT2Fragment extends BaseMainFragment {
    private List<MainT2Bean> mMainT2Beans;
    private int PAGE_SIZE = 10;
    private int PAGE_NUM = 1;
    private boolean loadMoreEnd;
    private boolean loadDataEnd;
    private boolean showProgressBar = false;
    private MainT2MoreItemAdapter mAdapter;
    //    private int num = 10;
    private ProgressBarViewHolder progressBarViewHolder;
    private RecyclerView mRecyclerView;
    private boolean mUserIsVip = false;
    private boolean mIsAddToPayVipItem = false;
    private boolean mIsShowLogined = false;
    private SwipeRefreshLayout mSwipeRefresh;
    private boolean mIsNotNet;
    private LoadDialog mLoadDialog;
    private CacheWorker mCacheWorker;


    @Override
    protected int setContentView() {
        return R.layout.fragment_main_t2;
    }

    private LoveEngin mLoveEngin;
    private LinearLayout mLlNotNet;
    private boolean mIsNetData = false;
    //    private boolean mIsDataToCache;
    private boolean mIsHandRefresh;

    @Override
    protected void initViews() {

    }

    private void initView() {
        if(mLoveEngin==null||mLlNotNet==null){
            Log.d("mylog", "initView: MainT2Fragment 2222222222 initView ");
            MobclickAgent.onEvent(mMainActivity, ConstantKey.UM_INSTANCE_ID);
            mLoveEngin = new LoveEngin(mMainActivity);
            mCacheWorker = new CacheWorker();

            View viewBar = rootView.findViewById(R.id.main_t2_view_bar);
            mLlNotNet = rootView.findViewById(R.id.main_t2_not_net);
            mMainActivity.setStateBarHeight(viewBar, 1);

            initRecyclerView();
        }
    }

    private void initRecyclerView() {
        mSwipeRefresh = rootView.findViewById(R.id.main_t2_swipe_refresh);
        mRecyclerView = rootView.findViewById(R.id.main_t2_rl);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mMainActivity);
        mRecyclerView.setLayoutManager(layoutManager);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        //设置增加或删除条目的动画
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
//        mRecyclerView.addItemDecoration(new DividerItemDecoration(mMainActivity,DividerItemDecoration.VERTICAL));

        mSwipeRefresh.setColorSchemeResources(R.color.red_crimson);
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                obtainWalletData();
                /*if (mMainT2Beans != null) {
                    mMainT2Beans.clear();
                    mAdapter.notifyDataSetChanged();
                }*/
                mIsHandRefresh = true;
                mIsAddToPayVipItem = false;
                loadMoreEnd = false;
                PAGE_NUM = 1;
                netData();
            }
        });
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
    public void onMessageEvent(NetWorkChangT2Bean netWorkChangBean) {
        if(mLlNotNet!=null){
        List<String> connectionTypeList = netWorkChangBean.connectionTypeList;
        if (connectionTypeList == null || connectionTypeList.size() == 0) {
            mIsNotNet = true;
            if (mLlNotNet.getVisibility() != View.VISIBLE) {
                mLlNotNet.setVisibility(View.VISIBLE);
            }
        } else {
            mIsNotNet = false;
            if (mLlNotNet.getVisibility() != View.GONE) {
                mLlNotNet.setVisibility(View.GONE);
                lazyLoad();
            }
        }
        }
    }


    @Override
    protected void lazyLoad() {
        /*if(mIsDataToCache){
            mIsNetData=false;
        }*/

        initView();

        if (!mIsNetData) {
            netData();
        }
    }

    private void netData() {
        if (!mIsHandRefresh) {
            mMainT2Beans = (List<MainT2Bean>) mCacheWorker.getCache(mMainActivity, "main2_example_lists");
            if (mMainT2Beans != null && mMainT2Beans.size() != 0) {
                initRecyclerViewData();
            } else {
                mLoadDialog = new LoadDialog(mMainActivity);
                mLoadDialog.showLoadingDialog();
            }
        }
        mLoveEngin.exampLists(String.valueOf(YcSingle.getInstance().id), String.valueOf(PAGE_NUM), String.valueOf(PAGE_SIZE), "example/lists")
                .subscribe(new MySubscriber<AResultInfo<ExampDataBean>>(mLoadDialog) {
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
                        }


                        mCacheWorker.setCache("main2_example_lists", mMainT2Beans);

//                SerializableFileUtli.checkPermissionWriteData(mMainT2Beans, "main2_example_lists");
//                CacheUtils.cacheBeanData(mMainActivity, "main2_example_lists", mMainT2Beans);
                        initRecyclerViewData();
                    }

                    @Override
                    protected void onNetError(Throwable e) {
                        mSwipeRefresh.setRefreshing(false);
                /*String data = (String) SPUtils.get(mMainActivity, "main2_example_lists", "");
                mMainT2Beans = new Gson().fromJson(data, new TypeToken<ArrayList<MainT2Bean>>() {
                }.getType());
                if (mMainT2Beans != null && mMainT2Beans.size() != 0) {
                    mIsDataToCache = true;
                    mIsNetData = true;
                    initRecyclerViewData();
                }*/
                    }

                    @Override
                    protected void onNetCompleted() {
                        mSwipeRefresh.setRefreshing(false);
                    }
                });
    }

    private void initRecyclerViewData() {
        mAdapter = new MainT2MoreItemAdapter(mMainT2Beans, mRecyclerView) {


            @Override
            public BaseViewHolder getTitleHolder(ViewGroup parent) {
                return new MainT2TitleViewHolder(mMainActivity, null, parent);
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
            protected RecyclerView.ViewHolder getBarViewHolder(ViewGroup parent) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_test_item_footer, parent, false);
                progressBarViewHolder = new ProgressBarViewHolder(view);
                return progressBarViewHolder;
            }


        };
        mRecyclerView.setAdapter(mAdapter);
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
                        mAdapter.notifyDataSetChanged();
                        showProgressBar = true;
                    }
                    if (!loadMoreEnd) {
                        netLoadMore();
                        //  判断是否是VIP
                       /* if (PAGE_NUM >= 3 && !mUserIsVip) {
                            addToPayVipData();
                        } else {
                            netLoadMore();
                        }*/
                    } else {
                        Log.d("mylog", "loadMoreData: loadMoreEnd end 已加载全部数据 ");
                        mMainT2Beans.remove(mMainT2Beans.size() - 1);
                        mAdapter.notifyDataSetChanged();
                    }
                }
            });
        }
        loadDataEnd = true;
    }


    private void netLoadMore() {
        mLoveEngin.exampLists(String.valueOf(YcSingle.getInstance().id), String.valueOf(++PAGE_NUM), String.valueOf(PAGE_SIZE), "example/lists").subscribe(new MySubscriber<AResultInfo<ExampDataBean>>(mMainActivity) {
            @Override
            protected void onNetNext(AResultInfo<ExampDataBean> exampDataBeanAResultInfo) {
                ExampDataBean exampDataBean = exampDataBeanAResultInfo.data;
                List<ExampListsBean> exampListsBeans = exampDataBean.lists;
                List<MainT2Bean> netLoadMoreData = new ArrayList<>();
                if (exampListsBeans != null && exampListsBeans.size() > 0) {
                    for (int i = 0; i < exampListsBeans.size(); i++) {
                        ExampListsBean exampListsBean = exampListsBeans.get(i);
                        netLoadMoreData.add(new MainT2Bean(1, exampListsBean.create_time, exampListsBean.id, exampListsBean.image, exampListsBean.post_title));
                    }
                }
                changLoadMoreView(netLoadMoreData);
              /*  showProgressBar = false;
                mMainT2Beans.remove(mMainT2Beans.size() - 1);
                mAdapter.notifyDataSetChanged();
                if (netLoadMoreData.size() < PAGE_SIZE) {
                    loadMoreEnd = true;
                }
                mMainT2Beans.addAll(netLoadMoreData);
                mAdapter.notifyDataSetChanged();
                mAdapter.setLoaded();*/
            }

            @Override
            protected void onNetError(Throwable e) {

                changLoadMoreView(null);
            }

            @Override
            protected void onNetCompleted() {

            }
        });
    }

    private void changLoadMoreView(List<MainT2Bean> netLoadMoreData) {
        MainT2Bean mainT2Bean = mMainT2Beans.get(mMainT2Beans.size() - 1);
        if (mainT2Bean == null) {
            showProgressBar = false;
            mMainT2Beans.remove(mMainT2Beans.size() - 1);
            mAdapter.notifyDataSetChanged();
        }
        if (netLoadMoreData != null && netLoadMoreData.size() != 0) {
            mMainT2Beans.addAll(netLoadMoreData);
            mAdapter.notifyDataSetChanged();
        } else {
            dataEmptyCheck();
        }
        mAdapter.setLoaded();
    }

    private void dataEmptyCheck() {
        if (PAGE_NUM != 1) {  //如果购买成功，依然请求这一页
            PAGE_NUM--;
        }
        int id = YcSingle.getInstance().id;
        /*if (id <= 0) {   //数据为空
            Log.d("mylog", "dataEmptyCheck: PAGE_NUM "+PAGE_NUM);
            if (PAGE_NUM <= 2) {
                addToPayVipData(); //数据为空 不是VIP
            }
            *//*if (mIsShowLogined) {
                addToPayVipData(); //数据为空 不是VIP
                return;
            }
            if(!mIsNotNet){
                mMainActivity.showToLoginDialog();
                mIsShowLogined = true;
            }*//*
        }*/
        if (PAGE_NUM <= 2) {
            if (!mIsAddToPayVipItem) {
                addToPayVipData(); //数据为空 不是VIP
            }
        }
    }

    private void addToPayVipData() {
        if (mUserIsVip) {  //已经是VIP 真的没有数据了
            return;
        }
        /*if (mIsAddToPayVipItem) {  //只添加一条即可
            return;
        }
        mIsAddToPayVipItem = true;*/
        mIsAddToPayVipItem = true;
        mRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mMainT2Beans.add(new MainT2Bean("toPayVip", 3, mMainT2Beans.size()));
                mAdapter.notifyDataSetChanged();
//                mRecyclerView.scrollToPosition(mMainT2Beans.size()-1);
            }
        }, 600);
    }


    RecyclerViewItemListener recyclerViewItemListener = new RecyclerViewItemListener() {
        @Override
        public void onItemClick(int position) {
            if (position < 0) {
                return;
            }
//            Toast.makeText(mMainActivity, "onItemClick " + position, Toast.LENGTH_SHORT).show();
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
