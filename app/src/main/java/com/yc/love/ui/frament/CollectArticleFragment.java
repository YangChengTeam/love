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
import com.yc.love.adaper.rv.holder.CollectArticleViewHolder;
import com.yc.love.adaper.rv.holder.LoveByStagesViewHolder;
import com.yc.love.model.base.MySubscriber;
import com.yc.love.model.bean.AResultInfo;
import com.yc.love.model.bean.ExampleTsListBean;
import com.yc.love.model.engin.LoveEngin;
import com.yc.love.model.single.YcSingle;
import com.yc.love.ui.activity.LoveByStagesDetailsActivity;
import com.yc.love.ui.frament.base.BaseCollectFragment;
import com.yc.love.ui.view.LoadDialog;

import java.util.List;

/**
 *
 * Created by mayn on 2019/5/5.
 */

public class CollectArticleFragment extends BaseCollectFragment {

    private RecyclerView mRecyclerView;
//    private int mCategoryId;
    private LoveEngin mLoveEngin;
    private int PAGE_SIZE = 10;
    private int PAGE_NUM = 1;
    private LoadDialog mLoadingDialog;
    private List<ExampleTsListBean> mExampleTsListBeans;
    private boolean loadMoreEnd;
    private boolean loadDataEnd;
    private boolean showProgressBar = false;
    private NoThingAdapter<ExampleTsListBean> mAdapter;

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
        return R.layout.fragment_love_by_stages;
    }

    @Override
    protected void initViews() {
        mLoveEngin = new LoveEngin(mCollectActivity);
//        LoadingDialog loadingDialog=mCollectActivity.mLoadingDialog;
        mLoadingDialog = mCollectActivity.mLoadingDialog;
        initRecyclerView();
    }


    private void initRecyclerView() {
        mRecyclerView = rootView.findViewById(R.id.fragment_love_by_stages_rv);
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
        mLoveEngin.collectListsArticle(String.valueOf(id), String.valueOf(PAGE_NUM), String.valueOf(PAGE_SIZE), "Article/collect_list").subscribe(new MySubscriber<AResultInfo<List<ExampleTsListBean>>>(mLoadingDialog) {


            @Override
            protected void onNetNext(AResultInfo<List<ExampleTsListBean>> listAResultInfo) {
                mExampleTsListBeans = listAResultInfo.data;
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
        mAdapter = new NoThingAdapter<ExampleTsListBean>(mExampleTsListBeans, mRecyclerView) {
            @Override
            public BaseViewHolder getHolder(ViewGroup parent) {
                return new CollectArticleViewHolder(mCollectActivity, recyclerViewItemListener, parent);
            }
        };
        mRecyclerView.setAdapter(mAdapter);
        if (mExampleTsListBeans.size() < PAGE_SIZE) {
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
                        mExampleTsListBeans.add(null);
                        mAdapter.notifyDataSetChanged();
                        showProgressBar = true;
                    }
                    if (!loadMoreEnd) {
                        netLoadMore();
                    } else {
                        Log.d("mylog", "loadMoreData: loadMoreEnd end 已加载全部数据 ");
                        mExampleTsListBeans.remove(mExampleTsListBeans.size() - 1);
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
        mLoveEngin.collectListsArticle(String.valueOf(id), String.valueOf(PAGE_NUM++), String.valueOf(PAGE_SIZE), "Article/collect_list").subscribe(new MySubscriber<AResultInfo<List<ExampleTsListBean>>>(mCollectActivity) {

            @Override
            protected void onNetNext(AResultInfo<List<ExampleTsListBean>> listAResultInfo) {
                List<ExampleTsListBean> netLoadMoreData = listAResultInfo.data;
                showProgressBar = false;
                mExampleTsListBeans.remove(mExampleTsListBeans.size() - 1);
                mAdapter.notifyDataSetChanged();
                if (netLoadMoreData.size() < PAGE_SIZE) {
                    loadMoreEnd = true;
                }
                mExampleTsListBeans.addAll(netLoadMoreData);
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
            ExampleTsListBean ExampleTsListBean = mExampleTsListBeans.get(position);
            LoveByStagesDetailsActivity.startLoveByStagesDetailsActivity(mCollectActivity,ExampleTsListBean.id,ExampleTsListBean.post_title);
        }

        @Override
        public void onItemLongClick(int position) {

        }
    };
}
