package com.yc.verbaltalk.chat.ui.fragment;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;

import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTFeedAd;
import com.bytedance.sdk.openadsdk.TTNativeExpressAd;
import com.yc.verbaltalk.R;
import com.yc.verbaltalk.base.engine.LoveEngine;
import com.yc.verbaltalk.base.utils.UserInfoHelper;
import com.yc.verbaltalk.base.view.LoadDialog;
import com.yc.verbaltalk.base.view.imgs.Constant;
import com.yc.verbaltalk.chat.adapter.LoveByStagesAdapter;
import com.yc.verbaltalk.chat.bean.LoveByStagesBean;
import com.yc.verbaltalk.chat.ui.activity.LoveByStagesDetailsActivity;

import java.util.List;
import java.util.Map;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import io.reactivex.observers.DisposableObserver;
import yc.com.rthttplibrary.bean.ResultInfo;
import yc.com.rthttplibrary.config.HttpConfig;
import yc.com.toutiao_adv.OnAdvStateListener;
import yc.com.toutiao_adv.TTAdDispatchManager;
import yc.com.toutiao_adv.TTAdType;

/**
 * Created by sunshey on 2019/5/5.
 */

public class LoveByStagesFragment extends BaseLoveByStagesFragment implements OnAdvStateListener {

    private SwipeRefreshLayout mSwipeRefresh;
    private RecyclerView mRecyclerView;
    private int mCategoryId;
    private LoveEngine mLoveEngin;
    private int PAGE_SIZE = 10;
    private int PAGE_NUM = 1;
    private LoadDialog mLoadingDialog;

//    private NoThingCanEmptyAdapter<LoveByStagesBean> mAdapter;

    private LoveByStagesAdapter mAdapter;
    private LoveByStagesBean loveByStagesBean;

    @Override
    protected void initBundle() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            int position = arguments.getInt("position");
            mCategoryId = arguments.getInt("category_id", -1);
        }
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_love_by_stages;
    }

    @Override
    protected void initViews() {
        mLoveEngin = new LoveEngine(mLoveByStagesActivity);
//        LoadingDialog loadingDialog=mLoveByStagesActivity.mLoadingDialog;
        mLoadingDialog = new LoadDialog(getActivity());
        initRecyclerView();
        initListener();
    }


    private void initRecyclerView() {
        mSwipeRefresh = rootView.findViewById(R.id.fragment_love_by_stages_swipe_refresh);
        mRecyclerView = rootView.findViewById(R.id.fragment_love_by_stages_rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mLoveByStagesActivity);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new LoveByStagesAdapter(null);
        mRecyclerView.setAdapter(mAdapter);

        mSwipeRefresh.setColorSchemeResources(R.color.red_crimson);
        mSwipeRefresh.setOnRefreshListener(() -> {
            PAGE_NUM = 1;
            netData();
        });
    }


    @Override
    protected void lazyLoad() {
        netData();
    }


    private void netData() {
        if (PAGE_NUM == 1) {
            mLoadingDialog.showLoadingDialog();
        }
        mLoveEngin.listsArticle(String.valueOf(mCategoryId), String.valueOf(PAGE_NUM), String.valueOf(PAGE_SIZE))
                .subscribe(new DisposableObserver<ResultInfo<List<LoveByStagesBean>>>() {

                    @Override
                    public void onComplete() {
                        if (PAGE_NUM == 1 && mLoadingDialog != null) {
                            mLoadingDialog.dismissLoadingDialog();

                        }
                        if (mSwipeRefresh.isRefreshing()) mSwipeRefresh.setRefreshing(false);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ResultInfo<List<LoveByStagesBean>> listAResultInfo) {
                        if (PAGE_NUM == 1 && mLoadingDialog != null) {
                            mLoadingDialog.dismissLoadingDialog();
                        }
                        if (listAResultInfo != null && listAResultInfo.code == HttpConfig.STATUS_OK && listAResultInfo.data != null) {
                            createNewData(listAResultInfo.data);
                        }
                    }


                });
    }

    private void createNewData(List<LoveByStagesBean> loveByStagesBeans) {
        if (PAGE_NUM == 1) {
            mAdapter.setNewData(loveByStagesBeans);
        } else {
            mAdapter.addData(loveByStagesBeans);
        }
        if (PAGE_SIZE == loveByStagesBeans.size()) {
            mAdapter.loadMoreComplete();
            PAGE_NUM++;
        } else {
            mAdapter.loadMoreEnd();
        }
        if (mSwipeRefresh.isRefreshing()) mSwipeRefresh.setRefreshing(false);

    }

    private void initListener() {
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
//                mLoveByStagesActivity.startActivity(new Intent(mLoveByStagesActivity, TestWebViewActivity.class));
            loveByStagesBean = mAdapter.getItem(position);

            String brand = Build.BRAND.toLowerCase();
//            TextUtils.equals("huawei", brand) || TextUtils.equals("honor", brand) ||
            if (UserInfoHelper.isVip()) {
                toLoveDetail();
            } else {
                TTAdDispatchManager.getManager().init(mLoveByStagesActivity, TTAdType.REWARD_VIDEO, null, Constant.TOUTIAO_REWARD2_ID, 0, "学习聊天技能", 1, UserInfoHelper.getUid(), TTAdConstant.VERTICAL, LoveByStagesFragment.this);


            }
        });
        mAdapter.setOnLoadMoreListener(this::netData, mRecyclerView);
    }

    @Override
    public void loadSuccess() {

    }

    @Override
    public void loadFailed() {

    }

    @Override
    public void clickAD() {
//        if (!rewardComplete) {
//            AlertDialog.Builder builder = new AlertDialog.Builder(mLoveByStagesActivity);
//            builder.setMessage("关闭广告可能无法学习撩妹技能！！");
//            builder.setPositiveButton("确定", (dialog, which) ->
//                    toLoveDetail()).show();
//        } else {
//
//        }
        toLoveDetail();
    }

    @Override
    public void onTTNativeExpressed(List<TTNativeExpressAd> ads) {

    }


    @Override
    public void onDrawFeedAd(List<TTFeedAd> feedAdList) {

    }

    @Override
    public void removeNativeAd(TTNativeExpressAd ad, int position) {

    }

    boolean rewardComplete = false;

    @Override
    public void rewardComplete() {
        rewardComplete = true;
    }


    private void toLoveDetail() {
        if (loveByStagesBean != null) {
            LoveByStagesDetailsActivity.startLoveByStagesDetailsActivity(mLoveByStagesActivity, loveByStagesBean.id, loveByStagesBean.post_title);
        }
    }
}
