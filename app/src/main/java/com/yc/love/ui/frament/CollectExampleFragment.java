package com.yc.love.ui.frament;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;

import com.yc.love.R;
import com.yc.love.adaper.rv.NoThingCanEmptyAdapter;
import com.yc.love.adaper.rv.base.RecyclerViewItemListener;
import com.yc.love.adaper.rv.holder.BaseViewHolder;
import com.yc.love.adaper.rv.holder.CollectExampleItemViewHolder;
import com.yc.love.adaper.rv.holder.EmptyViewHolder;
import com.yc.love.model.base.MySubscriber;
import com.yc.love.model.bean.AResultInfo;
import com.yc.love.model.bean.ExampListsBean;
import com.yc.love.model.engin.LoveEngin;
import com.yc.love.model.single.YcSingle;
import com.yc.love.ui.activity.ExampleDetailActivity;
import com.yc.love.ui.frament.base.BaseCollectFragment;
import com.yc.love.ui.view.LoadDialog;

import java.util.List;

/**
 * 收藏 实例（文章）
 * Created by mayn on 2019/5/5.
 */

public class CollectExampleFragment extends BaseCollectFragment {

    private RecyclerView mRecyclerView;
    //    private int mCategoryId;
    private LoveEngin mLoveEngin;
    private int PAGE_SIZE = 10;
    private int PAGE_NUM = 1;
    private LoadDialog mLoadingDialog;
    private boolean loadMoreEnd;
    private boolean loadDataEnd;
    private boolean showProgressBar = false;
    private NoThingCanEmptyAdapter<ExampListsBean> mAdapter;
    private boolean mUserIsVip = false;
    private List<ExampListsBean> mExampListsBeans;

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
        return R.layout.fragment_collect_article;
    }

    @Override
    protected void initViews() {
        mLoveEngin = new LoveEngin(mCollectActivity);
        mLoadingDialog = mCollectActivity.mLoadingDialog;
        initRecyclerView();
    }


    private void initRecyclerView() {
        mRecyclerView = rootView.findViewById(R.id.fragment_collect_article_rv);
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
        mLoveEngin.exampleCollectList(String.valueOf(YcSingle.getInstance().id), String.valueOf(PAGE_NUM), String.valueOf(PAGE_SIZE), "Example/collect_list").subscribe(new MySubscriber<AResultInfo<List<ExampListsBean>>>(mLoadingDialog) {


            @Override
            protected void onNetNext(AResultInfo<List<ExampListsBean>> listAResultInfo) {
                mExampListsBeans = listAResultInfo.data;
                initRecyclerViewData();
            }

            @Override
            protected void onNetError(Throwable e) {

            }

            @Override
            protected void onNetCompleted() {

            }
        });
    }

    private void initRecyclerViewData() {
        mAdapter = new NoThingCanEmptyAdapter<ExampListsBean>(mExampListsBeans, mRecyclerView) {
            @Override
            public BaseViewHolder getHolder(ViewGroup parent) {
                return new CollectExampleItemViewHolder(mCollectActivity, recyclerViewItemListener, parent);
            }

            @Override
            protected RecyclerView.ViewHolder getEmptyHolder(ViewGroup parent) {
                return new EmptyViewHolder(mCollectActivity, parent, "");
            }
        };
        mRecyclerView.setAdapter(mAdapter);
        if (mExampListsBeans.size() < PAGE_SIZE) {
            Log.d("ssss", "loadMoreData: data---end");
        } else {
            mAdapter.setOnMoreDataLoadListener(new NoThingCanEmptyAdapter.OnLoadMoreDataListener() {
                @Override
                public void loadMoreData() {
                    if (loadDataEnd == false) {
                        return;
                    }
                    if (showProgressBar == false) {
                        //加入null值此时adapter会判断item的type
                        mExampListsBeans.add(null);
                        mAdapter.notifyDataSetChanged();
                        showProgressBar = true;
                    }
                    if (!loadMoreEnd) {
                        netLoadMore();
                    } else {
                        Log.d("mylog", "loadMoreData: loadMoreEnd end 已加载全部数据 ");
                        mExampListsBeans.remove(mExampListsBeans.size() - 1);
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
//        mLoveEngin.listsCollectLovewords(String.valueOf(love_id), String.valueOf(PAGE_NUM), String.valueOf(PAGE_SIZE), "Lovewords/collect_list").subscribe(new MySubscriber<AResultInfo<List<LoveHealingBean>>>(mCollectActivity) {
        mLoveEngin.exampleCollectList(String.valueOf(YcSingle.getInstance().id), String.valueOf(++PAGE_NUM), String.valueOf(PAGE_SIZE), "Example/collect_list").subscribe(new MySubscriber<AResultInfo<List<ExampListsBean>>>(mCollectActivity) {
            @Override
            protected void onNetNext(AResultInfo<List<ExampListsBean>> listAResultInfo) {
                List<ExampListsBean> netLoadMoreData = listAResultInfo.data;
                showProgressBar = false;
                mExampListsBeans.remove(mExampListsBeans.size() - 1);
                mAdapter.notifyDataSetChanged();
                if (netLoadMoreData.size() < PAGE_SIZE) {
                    loadMoreEnd = true;
                }
                mExampListsBeans.addAll(netLoadMoreData);
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
            ExampListsBean exampListsBean = mExampListsBeans.get(position);
            ExampleDetailActivity.startExampleDetailActivity(mCollectActivity, exampListsBean.id, exampListsBean.post_title);

        }

        @Override
        public void onItemLongClick(int position) {

        }
    };

}
