package com.yc.verbaltalk.mine.ui.fragment;

import android.os.Bundle;
import android.view.View;

import com.yc.verbaltalk.R;
import com.yc.verbaltalk.base.engine.LoveEngine;
import com.yc.verbaltalk.base.utils.UserInfoHelper;
import com.yc.verbaltalk.base.view.LoadDialog;
import com.yc.verbaltalk.chat.bean.ExampListsBean;
import com.yc.verbaltalk.mine.adapter.CollectExampleItemAdapter;
import com.yc.verbaltalk.skill.ui.activity.ExampleDetailActivity;

import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.observers.DisposableObserver;
import yc.com.rthttplibrary.bean.ResultInfo;
import yc.com.rthttplibrary.config.HttpConfig;

/**
 * 收藏 实例（文章）
 * Created by sunshey on 2019/5/5.
 */

public class CollectExampleFragment extends BaseCollectFragment {

    private RecyclerView mRecyclerView;
    //    private int mCategoryId;
    private LoveEngine mLoveEngin;
    private int PAGE_SIZE = 10;
    private int PAGE_NUM = 1;
    private LoadDialog mLoadingDialog;

    private CollectExampleItemAdapter mAdapter;


    private View emptyView;

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
        mLoveEngin = new LoveEngine(mCollectActivity);
        mLoadingDialog = mCollectActivity.mLoadingDialog;
        emptyView = rootView.findViewById(R.id.top_empty_view);
        initRecyclerView();
        initListener();
    }

    private void initListener() {
        mAdapter.setOnLoadMoreListener(this::netData, mRecyclerView);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            ExampListsBean exampListsBean = mAdapter.getItem(position);
            if (exampListsBean != null)
                ExampleDetailActivity.startExampleDetailActivity(mCollectActivity, exampListsBean.id, exampListsBean.post_title);
        });
    }


    private void initRecyclerView() {
        mRecyclerView = rootView.findViewById(R.id.fragment_collect_love_healing_rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mCollectActivity);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new CollectExampleItemAdapter(null);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void lazyLoad() {
        netData();
    }


    private void netData() {

        if (PAGE_NUM == 1)
            mLoadingDialog.showLoadingDialog();
        mLoveEngin.exampleCollectList(UserInfoHelper.getUid(), String.valueOf(PAGE_NUM), String.valueOf(PAGE_SIZE))
                .subscribe(new DisposableObserver<ResultInfo<List<ExampListsBean>>>() {


                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (PAGE_NUM == 1) mLoadingDialog.dismissLoadingDialog();

                    }

                    @Override
                    public void onNext(ResultInfo<List<ExampListsBean>> listAResultInfo) {
                        if (PAGE_NUM == 1) mLoadingDialog.dismissLoadingDialog();

                        if (listAResultInfo != null && listAResultInfo.code == HttpConfig.STATUS_OK) {
                            List<ExampListsBean> mExampListsBeans = listAResultInfo.data;
                            if (mExampListsBeans != null && mExampListsBeans.size() > 0) {
                                initRecyclerViewData(mExampListsBeans);
                            } else {
                                if (PAGE_NUM == 1) {
                                    emptyView.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    }


                });
    }

    private void initRecyclerViewData(List<ExampListsBean> mExampListsBeans) {

        if (PAGE_NUM == 1) {
            mAdapter.setNewData(mExampListsBeans);
        } else {
            mAdapter.addData(mExampListsBeans);
        }
        if (mExampListsBeans != null && mExampListsBeans.size() == PAGE_SIZE) {
            mAdapter.loadMoreComplete();
            PAGE_NUM++;
        } else {
            mAdapter.loadMoreEnd();
        }

    }


}
