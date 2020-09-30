package com.yc.verbaltalk.index.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yc.verbaltalk.R;
import com.yc.verbaltalk.base.engine.LoveEngine;
import com.yc.verbaltalk.base.fragment.BaseLazyFragment;
import com.yc.verbaltalk.base.utils.UserInfoHelper;
import com.yc.verbaltalk.base.view.LoadDialog;
import com.yc.verbaltalk.chat.bean.LoveHealDetBean;
import com.yc.verbaltalk.chat.bean.LoveHealDetDetailsBean;
import com.yc.verbaltalk.chat.bean.SearchDialogueBean;
import com.yc.verbaltalk.index.adapter.LoveHealDetailsAdapter;
import com.yc.verbaltalk.index.ui.activity.SearchActivity;
import com.yc.verbaltalk.mine.ui.activity.BecomeVipActivity;

import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import io.reactivex.observers.DisposableObserver;
import yc.com.rthttplibrary.bean.ResultInfo;
import yc.com.rthttplibrary.config.HttpConfig;

/**
 * Created by sunshey on 2019/5/5.
 */

public class SearchFragment extends BaseLazyFragment {

    private SearchActivity mShareActivity;
    private RecyclerView mRecyclerView;
    private LoveEngine mLoveEngin;
    private int PAGE_SIZE = 10;
    private int PAGE_NUM = 1;
    private LoadDialog mLoadingDialog;

    private LoveHealDetailsAdapter mAdapter;

    private String keyword;
    private SwipeRefreshLayout mSwipeRefresh;


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
        Bundle bundle = getArguments();
        if (bundle != null) {
            keyword = bundle.getString("keyword");
        }

        mLoveEngin = new LoveEngine(mShareActivity);
        mSwipeRefresh = rootView.findViewById(R.id.base_share_swipe_refresh);
        mLoadingDialog = mShareActivity.mLoadingDialog;
        initRecyclerView();
        initListener();


    }

    private void initListener() {
        mSwipeRefresh.setColorSchemeResources(R.color.red_crimson);
        mSwipeRefresh.setOnRefreshListener(() -> {
//                obtainWalletData();
            if (!TextUtils.isEmpty(keyword)) {
                PAGE_NUM = 1;
                netData(keyword);
            }
        });
        mAdapter.setOnItemClickListener((adapter, view, position) -> {

        });
        mAdapter.setOnLoadMoreListener(() -> netData(keyword), mRecyclerView);
    }

    @Override
    protected void lazyLoad() {
        netData(keyword);
    }

    private void initRecyclerView() {
        mRecyclerView = rootView.findViewById(R.id.base_share_rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mShareActivity);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new LoveHealDetailsAdapter(null, getString(R.string.search));
        mRecyclerView.setAdapter(mAdapter);

    }

    /**
     * @param keyword
     */
    public void netData(String keyword) {
        this.keyword = keyword;

        if (PAGE_NUM == 1)
            mLoadingDialog.showLoadingDialog();
        mLoveEngin.searchDialogue2(UserInfoHelper.getUid(), keyword, String.valueOf(PAGE_NUM), String.valueOf(PAGE_SIZE))
                .subscribe(new DisposableObserver<ResultInfo<SearchDialogueBean>>() {


                    @Override
                    public void onComplete() {
                        if (mSwipeRefresh.isRefreshing())
                            mSwipeRefresh.setRefreshing(false);
                        if (PAGE_NUM == 1) mLoadingDialog.dismissLoadingDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (PAGE_NUM == 1) mLoadingDialog.dismissLoadingDialog();
                        if (mSwipeRefresh.isRefreshing())
                            mSwipeRefresh.setRefreshing(false);
                    }

                    @Override
                    public void onNext(ResultInfo<SearchDialogueBean> searchDialogueBeanAResultInfo) {
                        if (PAGE_NUM == 1) mLoadingDialog.dismissLoadingDialog();
                        if (searchDialogueBeanAResultInfo != null && searchDialogueBeanAResultInfo.code == HttpConfig.STATUS_OK && searchDialogueBeanAResultInfo.data != null) {
                            SearchDialogueBean searchDialogueBean = searchDialogueBeanAResultInfo.data;
                            int searchBuyVip = searchDialogueBean.search_buy_vip;
                            if (1 == searchBuyVip) { //1 弹窗 0不弹
                                if (UserInfoHelper.isLogin(mShareActivity)) {
                                    startActivity(new Intent(mShareActivity, BecomeVipActivity.class));
                                    mRecyclerView.postDelayed(() -> mShareActivity.childDisposeOnBack(), 1000);
                                }
//                    notCanShart();
                                return;
                            }
                            List<LoveHealDetBean> mLoveHealDetBeans = searchDialogueBean.list;

                            initRecyclerData(mLoveHealDetBeans);
                        }
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
        for (LoveHealDetBean loveHealDetBean : loveHealDetBeans) {
            List<LoveHealDetDetailsBean> details = loveHealDetBean.details;
            if (details == null || details.size() == 0) {
                details = loveHealDetBean.detail;
            }
            details.add(0, new LoveHealDetDetailsBean(0, loveHealDetBean.id, loveHealDetBean.chat_name, loveHealDetBean.ans_sex));
        }
        return loveHealDetBeans;
    }


}

