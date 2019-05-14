package com.yc.love.ui.frament;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;

import com.yc.love.R;
import com.yc.love.adaper.rv.NoThingAdapter;
import com.yc.love.adaper.rv.base.RecyclerViewItemListener;
import com.yc.love.adaper.rv.holder.BaseViewHolder;
import com.yc.love.adaper.rv.holder.LoveHealingItemTitleViewHolder;
import com.yc.love.adaper.rv.holder.LoveHealingItemViewHolder;
import com.yc.love.model.base.MySubscriber;
import com.yc.love.model.bean.AResultInfo;
import com.yc.love.model.bean.LoveHealingBean;
import com.yc.love.model.engin.LoveEngin;
import com.yc.love.model.single.YcSingle;
import com.yc.love.ui.activity.LoveByStagesDetailsActivity;
import com.yc.love.ui.frament.base.BaseCollectFragment;
import com.yc.love.ui.view.LoadDialog;

import java.util.List;

/**
 * Created by mayn on 2019/5/5.
 */

public class CollectLoveHealingFragment extends BaseCollectFragment {

    private RecyclerView mRecyclerView;
    //    private int mCategoryId;
    private LoveEngin mLoveEngin;
    private int PAGE_SIZE = 10;
    private int PAGE_NUM = 1;
    private LoadDialog mLoadingDialog;
    //    private List<LoveHealingBean> mLoveHealingBeans;
    private boolean loadMoreEnd;
    private boolean loadDataEnd;
    private boolean showProgressBar = false;
    private NoThingAdapter<LoveHealingBean> mAdapter;
    private List<LoveHealingBean> mLoveHealingBeans;

    @Override
    protected void initBundle() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            int position = arguments.getInt("position");
//            mCategoryId = arguments.getInt("category_id", -1);
        }
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_collect_love_healing;
    }

    @Override
    protected void initViews() {
        mLoveEngin = new LoveEngin(mCollectActivity);
//        LoadingDialog loadingDialog=mCollectActivity.mLoadingDialog;
        mLoadingDialog = mCollectActivity.mLoadingDialog;
        initRecyclerView();
    }


    private void initRecyclerView() {
        mRecyclerView = rootView.findViewById(R.id.fragment_collect_love_healing_rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mCollectActivity);
        mRecyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void lazyLoad() {
        netData();
    }


    private void netData() {
        int id = YcSingle.getInstance().id;
        if (id <= 0) {
            mCollectActivity.showToLoginDialog();
            return;
        }
        mLoadingDialog.showLoadingDialog();
        mLoveEngin.listsCollectLovewords(String.valueOf(id), String.valueOf(PAGE_NUM), String.valueOf(PAGE_SIZE), "Lovewords/collect_list").subscribe(new MySubscriber<AResultInfo<List<LoveHealingBean>>>(mLoadingDialog) {

            @Override
            protected void onNetNext(AResultInfo<List<LoveHealingBean>> listAResultInfo) {
                mLoveHealingBeans = listAResultInfo.data;
                initRecyclerData();
            }

            @Override
            protected void onNetError(Throwable e) {

            }

            @Override
            protected void onNetCompleted() {

            }
        });
    }

    private void initRecyclerData() {
        mAdapter = new NoThingAdapter<LoveHealingBean>(mLoveHealingBeans, mRecyclerView) {
            @Override
            public BaseViewHolder getHolder(ViewGroup parent) {
//                return new CollectLoveHealingViewHolder(mCollectActivity, recyclerViewItemListener, parent);
                return new LoveHealingItemViewHolder(mCollectActivity, recyclerViewItemListener, parent);
            }
        };
        mRecyclerView.setAdapter(mAdapter);
        if (mLoveHealingBeans.size() < PAGE_SIZE) {
            Log.d("ssss", "loadMoreData: data---end");
        } else {
            mAdapter.setOnMoreDataLoadListener(new NoThingAdapter.OnLoadMoreDataListener() {
                @Override
                public void loadMoreData() {
                    if (loadDataEnd == false) {
                        return;
                    }
                    if (showProgressBar == false) {
                        //加入null值此时adapter会判断item的type
                        mLoveHealingBeans.add(null);
                        mAdapter.notifyDataSetChanged();
                        showProgressBar = true;
                    }
                    if (!loadMoreEnd) {
                        netLoadMore();
                    } else {
                        Log.d("mylog", "loadMoreData: loadMoreEnd end 已加载全部数据 ");
                        mLoveHealingBeans.remove(mLoveHealingBeans.size() - 1);
                        mAdapter.notifyDataSetChanged();
                    }
                }
            });
        }
        loadDataEnd = true;
    }

    private void netLoadMore() {
        int id = YcSingle.getInstance().id;
        if (id <= 0) {
            mCollectActivity.showToLoginDialog();
            return;
        }
        mLoveEngin.listsCollectLovewords(String.valueOf(id), String.valueOf(PAGE_NUM), String.valueOf(PAGE_SIZE), "Lovewords/collect_list").subscribe(new MySubscriber<AResultInfo<List<LoveHealingBean>>>(mCollectActivity) {


            @Override
            protected void onNetNext(AResultInfo<List<LoveHealingBean>> listAResultInfo) {
                List<LoveHealingBean> netLoadMoreData = listAResultInfo.data;
                showProgressBar = false;
                mLoveHealingBeans.remove(mLoveHealingBeans.size() - 1);
                mAdapter.notifyDataSetChanged();
                if (netLoadMoreData.size() < PAGE_SIZE) {
                    loadMoreEnd = true;
                }
                mLoveHealingBeans.addAll(netLoadMoreData);
                mAdapter.notifyDataSetChanged();
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
            LoveHealingBean LoveHealingBean = mLoveHealingBeans.get(position);
            LoveByStagesDetailsActivity.startLoveByStagesDetailsActivity(mCollectActivity, LoveHealingBean.id, LoveHealingBean.chat_name);
        }

        @Override
        public void onItemLongClick(int position) {

        }
    };
}
