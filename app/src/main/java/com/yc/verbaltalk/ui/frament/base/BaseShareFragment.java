package com.yc.verbaltalk.ui.frament.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yc.verbaltalk.R;
import com.yc.verbaltalk.adaper.rv.LoveHealDetailsAdapter;
import com.yc.verbaltalk.model.base.MySubscriber;
import com.yc.verbaltalk.model.bean.AResultInfo;
import com.yc.verbaltalk.model.bean.LoveHealDetBean;
import com.yc.verbaltalk.model.bean.LoveHealDetDetailsBean;
import com.yc.verbaltalk.model.engin.LoveEngine;
import com.yc.verbaltalk.model.single.YcSingle;
import com.yc.verbaltalk.ui.activity.SearchActivity;
import com.yc.verbaltalk.ui.view.LoadDialog;

import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by mayn on 2019/5/5.
 */

public class BaseShareFragment extends BaseLazyFragment {

    public SearchActivity mShareActivity;
    private RecyclerView mRecyclerView;
    private LoveEngine mLoveEngin;
    private int PAGE_SIZE = 10;
    private int PAGE_NUM = 1;
    private LoadDialog mLoadingDialog;

    private LoveHealDetailsAdapter mAdapter;
    private List<LoveHealDetBean> mLoveHealDetBeans;
    private String keyword;
    private String mSearchType;

    @Override
    protected View onFragmentCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mShareActivity = (SearchActivity) getActivity();
        return super.onFragmentCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_base_share;
    }

    @Override
    protected void initViews() {
        mLoveEngin = new LoveEngine(mShareActivity);
        mLoadingDialog = mShareActivity.mLoadingDialog;
        initRecyclerView();
        initListener();
    }

    private void initListener() {
        mAdapter.setOnLoadMoreListener(() -> netData(mSearchType, keyword), mRecyclerView);
    }

    @Override
    protected void lazyLoad() {

    }

    private void initRecyclerView() {
        mRecyclerView = rootView.findViewById(R.id.base_share_rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mShareActivity);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new LoveHealDetailsAdapter(null, getString(R.string.search));
        mRecyclerView.setAdapter(mAdapter);
    }

    /**
     * @param searchType 1模糊  2精准
     * @param keyword
     */
    public void netData(String searchType, String keyword) {
        this.mSearchType = searchType;
        this.keyword = keyword;
        int id = YcSingle.getInstance().id;
        if (id <= 0) {
            mShareActivity.showToLoginDialog();
            return;
        }
        mLoadingDialog.showLoadingDialog();
        mLoveEngin.searchDialogue(String.valueOf(id), searchType, keyword, String.valueOf(PAGE_NUM), String.valueOf(PAGE_SIZE), "Dialogue/search").subscribe(new MySubscriber<AResultInfo<List<LoveHealDetBean>>>(mLoadingDialog) {


            @Override
            protected void onNetNext(AResultInfo<List<LoveHealDetBean>> listAResultInfo) {
                mLoveHealDetBeans = listAResultInfo.data;
                initRecyclerData(mLoveHealDetBeans);
            }

            @Override
            protected void onNetError(Throwable e) {

            }

            @Override
            protected void onNetCompleted() {

            }
        });
    }

    private void initRecyclerData(List<LoveHealDetBean> loveHealDetBeans) {
        List<LoveHealDetBean> mLoveHealDetBeans = addTitle(loveHealDetBeans);
        if (PAGE_NUM == 1) {
            mAdapter.setNewData(mLoveHealDetBeans);
        } else {
            mAdapter.addData(mLoveHealDetBeans);
        }

        if (loveHealDetBeans.size() == PAGE_SIZE) {
            mAdapter.loadMoreComplete();
            PAGE_NUM++;
        } else {
            mAdapter.loadMoreEnd();
        }


    }

    private List<LoveHealDetBean> addTitle(List<LoveHealDetBean> loveHealDetBeans) {
        for (LoveHealDetBean loveHealDetBean : loveHealDetBeans
                ) {
            List<LoveHealDetDetailsBean> details = loveHealDetBean.details;
            if (details == null || details.size() == 0) {
                details = loveHealDetBean.detail;
            }
            details.add(0, new LoveHealDetDetailsBean(0, loveHealDetBean.id, loveHealDetBean.chat_name, loveHealDetBean.ans_sex));
        }
        return loveHealDetBeans;
    }


}

