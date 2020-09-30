package com.yc.verbaltalk.skill.ui.fragment;

import com.alibaba.fastjson.TypeReference;
import com.yc.verbaltalk.R;
import com.yc.verbaltalk.base.engine.LoveEngine;
import com.yc.verbaltalk.base.utils.CommonInfoHelper;
import com.yc.verbaltalk.base.view.LoadDialog;
import com.yc.verbaltalk.chat.bean.confession.ConfessionBean;
import com.yc.verbaltalk.chat.bean.confession.ConfessionDataBean;
import com.yc.verbaltalk.skill.adapter.ConfessionAdapter;
import com.yc.verbaltalk.skill.ui.activity.CreateBeforeActivity;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import io.reactivex.observers.DisposableObserver;


//表白
public class ExpressFragment extends BaseMainT2ChildFragment {

    private RecyclerView mRecyclerView;
    private List<ConfessionDataBean> mConfessionDataBeans;
    private int PAGE_SIZE = 20;
    private int PAGE_NUM = 1;
    private ConfessionAdapter mAdapter;

    private LoadDialog mLoadDialog;
    private LoveEngine loveEngin;
    private SwipeRefreshLayout mSipe;


    @Override
    protected int setContentView() {
        return R.layout.activity_express;
    }

    @Override
    protected void initViews() {
        loveEngin = new LoveEngine(mMainActivity);
        initView();

    }

    @Override
    protected void lazyLoad() {
        initData();
    }


    private void initView() {
        initRecyclerView();
        initListener();
    }

    private void initRecyclerView() {
        mRecyclerView = rootView.findViewById(R.id.express_rl);
        mSipe = rootView.findViewById(R.id.swipeRefreshLayout);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mMainActivity);
        mRecyclerView.setLayoutManager(layoutManager);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        //设置增加或删除条目的动画
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new ConfessionAdapter(null);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initListener() {
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            ConfessionDataBean confessionDataBean = mAdapter.getItem(position);
            if (confessionDataBean != null && ConfessionDataBean.VIEW_ITEM == confessionDataBean.itemType)
                CreateBeforeActivity.startCreateBeforeActivity(mMainActivity, confessionDataBean);
        });
        mAdapter.setOnLoadMoreListener(this::initData, mRecyclerView);
        mSipe.setColorSchemeResources(R.color.red_crimson);
        mSipe.setOnRefreshListener(() -> {
            PAGE_NUM = 1;
            initData();
        });
    }

    private void initData() {
        if (PAGE_NUM == 1) {

            CommonInfoHelper.getO(mMainActivity, "main3_new", new TypeReference<List<ConfessionDataBean>>() {
            }.getType(), (CommonInfoHelper.onParseListener<List<ConfessionDataBean>>) o -> {
                mConfessionDataBeans = o;
                if (mConfessionDataBeans != null && mConfessionDataBeans.size() > 0) {
                    mAdapter.setNewData(mConfessionDataBeans);
                }
            });

            mLoadDialog = new LoadDialog(mMainActivity);
            mLoadDialog.showLoadingDialog();

        }

        loveEngin.getExpressData(PAGE_NUM).subscribe(new DisposableObserver<ConfessionBean>() {
            @Override
            public void onComplete() {
                if (mLoadDialog != null) mLoadDialog.dismissLoadingDialog();
                if (mSipe.isRefreshing()) mSipe.setRefreshing(false);
            }

            @Override
            public void onError(Throwable e) {
                if (mLoadDialog != null) mLoadDialog.dismissLoadingDialog();
                if (mSipe.isRefreshing()) mSipe.setRefreshing(false);

            }

            @Override
            public void onNext(ConfessionBean confessionBean) {
                if (confessionBean != null && confessionBean.status) {
                    List<ConfessionDataBean> confessionDataBeans = confessionBean.data;
                    createNewData(confessionDataBeans);
                }
                if (mLoadDialog != null) mLoadDialog.dismissLoadingDialog();
                if (mSipe.isRefreshing()) mSipe.setRefreshing(false);
            }
        });

    }

    private void createNewData(List<ConfessionDataBean> confessionDataBeans) {
        mConfessionDataBeans = new ArrayList<>();

        if (confessionDataBeans != null && confessionDataBeans.size() > 0) {
            for (ConfessionDataBean confessionDataBean : confessionDataBeans) {
                confessionDataBean.itemType = ConfessionDataBean.VIEW_ITEM;
                mConfessionDataBeans.add(confessionDataBean);
            }
        }

        if (PAGE_NUM == 1)
            mConfessionDataBeans.add(0, new ConfessionDataBean(ConfessionDataBean.VIEW_TITLE, "data_title"));

        if (PAGE_NUM == 1) {
            mAdapter.setNewData(mConfessionDataBeans);
            CommonInfoHelper.setO(mMainActivity, mConfessionDataBeans, "main3_new");

        } else {
            mAdapter.addData(mConfessionDataBeans);
        }
        if (confessionDataBeans != null && confessionDataBeans.size() == PAGE_SIZE) {
            mAdapter.loadMoreComplete();
            PAGE_NUM++;
        } else {
            mAdapter.loadMoreEnd();
        }

    }


}
