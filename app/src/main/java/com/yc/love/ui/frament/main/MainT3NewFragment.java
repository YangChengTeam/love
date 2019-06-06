package com.yc.love.ui.frament.main;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.yc.love.R;
import com.yc.love.adaper.rv.ConfessionAdapter;
import com.yc.love.adaper.rv.base.RecyclerViewItemListener;
import com.yc.love.adaper.rv.holder.BaseViewHolder;
import com.yc.love.adaper.rv.holder.ConfessionViewHolder;
import com.yc.love.adaper.rv.holder.DataOverViewHolder;
import com.yc.love.adaper.rv.holder.MainT2TitleViewHolder;
import com.yc.love.cache.CacheWorker;
import com.yc.love.model.bean.ExampListsBean;
import com.yc.love.model.bean.confession.ConfessionBean;
import com.yc.love.model.bean.confession.ConfessionDataBean;
import com.yc.love.model.domain.URLConfig;
import com.yc.love.okhttp.presenter.NormalPresenter;
import com.yc.love.okhttp.view.IMoreUiView;
import com.yc.love.okhttp.view.INormalUiView;
import com.yc.love.ui.activity.CreateBeforeActivity;
import com.yc.love.ui.frament.base.BaseMainFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by mayn on 2019/6/5.
 */

public class MainT3NewFragment extends BaseMainFragment implements INormalUiView, IMoreUiView {

    private RecyclerView mRecyclerView;
    private CacheWorker mCacheWorker;
    private List<ConfessionDataBean> mConfessionDataBeans;
    private int PAGE_SIZE = 20;
    private int PAGE_NUM = 1;
    private ConfessionAdapter mAdapter;
    private boolean loadMoreEnd;
    private boolean loadDataEnd;
    private boolean showProgressBar = false;
    private NormalPresenter mNormalPresenter;
    private Map<String, String> mRequestMap;
    private String mDataUrl;


    @Override
    protected int setContentView() {
        return R.layout.fragment_main_t3_new;
    }

    @Override
    protected void initViews() {
        initView();

    }

    private void initView() {
        mCacheWorker = new CacheWorker();

        View viewBar = rootView.findViewById(R.id.main_t3_new_view_bar);
        mMainActivity.setStateBarHeight(viewBar, 1);

        initRecyclerView();
    }

    private void initRecyclerView() {
        mRecyclerView = rootView.findViewById(R.id.main_t3_new_rl);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mMainActivity);
        mRecyclerView.setLayoutManager(layoutManager);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        //设置增加或删除条目的动画
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }


    @Override
    protected void lazyLoad() {
        mNormalPresenter = new NormalPresenter(this);
        mRequestMap = new HashMap<>();
        mDataUrl = URLConfig.CATEGORY_LIST_URL;
        mRequestMap.put("id", "1");
        mRequestMap.put("page", String.valueOf(PAGE_NUM));
        mNormalPresenter.netNormalData(mRequestMap, mDataUrl);
    }

    @Override
    public void onSuccess(String jsonData) {
        Log.d("mylog", "onSuccess: jsonData " + jsonData);
        ConfessionBean confessionBean = new Gson().fromJson(jsonData, ConfessionBean.class);
        mConfessionDataBeans = confessionBean.data;

        /*for (ConfessionDataBean confessionDataBean : mConfessionDataBeans
        ) {
            Log.d("mylog", "onSuccess: confessionDataBean " + confessionDataBean);
        }*/
        if (mConfessionDataBeans == null) {
            mConfessionDataBeans = new ArrayList<>();
        }
        mConfessionDataBeans.add(0, new ConfessionDataBean(0, "data_title"));
        initRecyclerViewData();
    }

    @Override
    public void onFailed(Call call, Exception e, int id) {

    }

    @Override
    public void onBefore(Request request, int id) {

    }


    private void initRecyclerViewData() {
        mAdapter = new ConfessionAdapter(mConfessionDataBeans, mRecyclerView) {


            @Override
            protected RecyclerView.ViewHolder getTitleHolder(ViewGroup parent) {
                return new MainT2TitleViewHolder(mMainActivity, null, parent);
            }

            @Override
            public BaseViewHolder getHolder(ViewGroup parent) {
                return new ConfessionViewHolder(mMainActivity, recyclerViewItemListener, parent);
            }

            @Override
            protected RecyclerView.ViewHolder getDaTaOverHolder(ViewGroup parent) {
                return new DataOverViewHolder(mMainActivity, parent, "");
            }
        };
        mRecyclerView.setAdapter(mAdapter);
        if (mConfessionDataBeans.size() < PAGE_SIZE) {
            Log.d("ssss", "loadMoreData: data---end");
        } else {
            mAdapter.setOnMoreDataLoadListener(new ConfessionAdapter.OnLoadMoreDataListener() {
                @Override
                public void loadMoreData() {
                    if (loadDataEnd == false) {
                        return;
                    }
                    if (showProgressBar == false) {
                        //加入null值此时adapter会判断item的type
                        mConfessionDataBeans.add(null);
                        mAdapter.notifyItemChanged(mConfessionDataBeans.size() - 1);
                        showProgressBar = true;
                    }
                    if (!loadMoreEnd) {
                        netLoadMore();
                    } else {
                        Log.d("mylog", "loadMoreData: loadMoreEnd end 已加载全部数据 ");
                        mConfessionDataBeans.remove(mConfessionDataBeans.size() - 1);
                        mAdapter.notifyDataSetChanged();
                    }
                }
            });
        }
        loadDataEnd = true;
    }

    RecyclerViewItemListener recyclerViewItemListener = new RecyclerViewItemListener() {
        @Override
        public void onItemClick(int position) {
            if (position < 0) {
                return;
            }
//            ExampListsBean exampListsBean = mExampListsBeans.get(position);
//            ExampleDetailActivity.startExampleDetailActivity(mMainActivity, exampListsBean.id, exampListsBean.post_title);

            ConfessionDataBean confessionDataBean = mConfessionDataBeans.get(position);
//            String title=confessionDataBean.title;

            CreateBeforeActivity.startCreateBeforeActivity(mMainActivity,confessionDataBean);
        }

        @Override
        public void onItemLongClick(int position) {

        }

    };


    private void netLoadMore() {
        mRequestMap.put("page", String.valueOf(++PAGE_NUM));
        mNormalPresenter.netMoreData(mRequestMap, mDataUrl);
    }

    @Override
    public void onMoreSuccess(String jsonData) {
        ConfessionBean confessionBean = new Gson().fromJson(jsonData, ConfessionBean.class);
        List<ConfessionDataBean> netLoadMoreData = confessionBean.data;
        Log.d("mylog", "onMoreSuccess: " + netLoadMoreData.size());

        if (PAGE_NUM == 4) {
            netLoadMoreData.remove(10);
            netLoadMoreData.remove(10);
            netLoadMoreData.remove(10);
        }

        if (netLoadMoreData != null) {

            showProgressBar = false;
            if (mConfessionDataBeans.get(mConfessionDataBeans.size() - 1) == null) {
                mConfessionDataBeans.remove(mConfessionDataBeans.size() - 1);
                mAdapter.notifyItemChanged(mConfessionDataBeans.size() - 1);
            }


            if (netLoadMoreData.size() < PAGE_SIZE) {
                loadMoreEnd = true;
                netLoadMoreData.add(new ConfessionDataBean(3, "data_over"));
            }
            mConfessionDataBeans.addAll(netLoadMoreData);
            mAdapter.notifyDataSetChanged();
//            mAdapter.notifyItemRangeChanged(mConfessionDataBeans.size() - netLoadMoreData.size(), netLoadMoreData.size());
        }
        mAdapter.setLoaded();
    }
}
