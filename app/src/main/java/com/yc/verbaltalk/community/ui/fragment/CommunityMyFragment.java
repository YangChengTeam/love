package com.yc.verbaltalk.community.ui.fragment;

import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.TypeReference;
import com.yc.verbaltalk.R;
import com.yc.verbaltalk.base.engine.LoveEngine;
import com.yc.verbaltalk.base.fragment.BaseMainFragment;
import com.yc.verbaltalk.base.utils.CommonInfoHelper;
import com.yc.verbaltalk.base.utils.ItemDecorationHelper;
import com.yc.verbaltalk.base.utils.UserInfoHelper;
import com.yc.verbaltalk.base.view.LoadDialog;
import com.yc.verbaltalk.chat.bean.CommunityInfo;
import com.yc.verbaltalk.chat.bean.CommunityInfoWrapper;
import com.yc.verbaltalk.chat.bean.event.CommunityPublishSuccess;
import com.yc.verbaltalk.chat.bean.event.EventLoginState;
import com.yc.verbaltalk.community.adapter.CommunityAdapter;
import com.yc.verbaltalk.community.ui.activity.CommunityDetailActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import io.reactivex.observers.DisposableObserver;
import yc.com.rthttplibrary.bean.ResultInfo;
import yc.com.rthttplibrary.config.HttpConfig;

/**
 * Created by suns  on 2019/8/28 09:17.
 */
public class CommunityMyFragment extends BaseMainFragment implements View.OnClickListener {

    private RecyclerView recyclerView;


    private LoveEngine loveEngin;
    private CommunityAdapter communityAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    private int page = 1;
    private final int PAGE_SIZE = 10;
    private LoadDialog loadingDialog;
    private LinearLayout topEmptyView;
    private Handler mHandler;
    private TextView tvEmptyDes;

    @Override
    protected int setContentView() {
        return R.layout.fragment_community;
    }

    @Override
    protected void initViews() {
        mHandler = new Handler();

        recyclerView = rootView.findViewById(R.id.rv_community);

        swipeRefreshLayout = rootView.findViewById(R.id.swipeRefreshLayout);
        topEmptyView = rootView.findViewById(R.id.top_empty_view);
        tvEmptyDes = rootView.findViewById(R.id.empty_view_tv_des);
//        if (topEmptyView.getChildCount() >= 2 && topEmptyView.getChildAt(1) instanceof TextView)
//            tvEmptyDes = (TextView) topEmptyView.getChildAt(1);

        loveEngin = new LoveEngine(mMainActivity);
        initRecyclerView();
    }


    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(mMainActivity));
        communityAdapter = new CommunityAdapter(null);
        recyclerView.setAdapter(communityAdapter);
        recyclerView.addItemDecoration(new ItemDecorationHelper(mMainActivity, 10));
        initListener();
    }

    private void initListener() {
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(mMainActivity, R.color.red_crimson));
        swipeRefreshLayout.setOnRefreshListener(() -> {
            page = 1;
            getData();
        });


        communityAdapter.setOnItemClickListener((adapter, view, position) -> {

            CommunityInfo communityInfo = communityAdapter.getItem(position);
            if (UserInfoHelper.isLogin(mMainActivity))
                if (null != communityInfo)
                    CommunityDetailActivity.StartActivity(mMainActivity, getString(R.string.community_detail), communityInfo.topicId);
        });


        communityAdapter.setOnLoadMoreListener(this::getData, recyclerView);

        communityAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            CommunityInfo communityInfo = communityAdapter.getItem(position);
            if (communityInfo != null) {

                switch (view.getId()) {
                    case R.id.iv_like:
                    case R.id.ll_like:
                        if (UserInfoHelper.isLogin(getActivity()))
                            if (communityInfo.is_dig == 0) {//未点赞
                                like(communityInfo, position);
                            }

                        break;
                }
            }
        });


    }

    private void initData() {
        CommonInfoHelper.getO(mMainActivity, "community_my_info", new TypeReference<List<CommunityInfo>>() {
        }.getType(), (CommonInfoHelper.onParseListener<List<CommunityInfo>>) datas -> {
            if (datas != null && datas.size() > 0) {
                createNewData(datas);
            }
        });
        getData();
    }

    @Override
    protected void lazyLoad() {
        if (TextUtils.isEmpty(UserInfoHelper.getUid())) {//没有登录
            topEmptyView.setVisibility(View.VISIBLE);
            if (null != tvEmptyDes) tvEmptyDes.setText("未登录？请登录");
            topEmptyView.setOnClickListener(v -> {
                UserInfoHelper.isLogin(mMainActivity);

            });

            return;
        }
        initData();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void login(EventLoginState event) {
        switch (event.state) {
            case EventLoginState.STATE_LOGINED:
                topEmptyView.setVisibility(View.GONE);
                getData();
                break;
            case EventLoginState.STATE_EXIT:
                topEmptyView.setVisibility(View.VISIBLE);
                break;
        }
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
    public void publishSuccess(CommunityPublishSuccess success) {
        page = 1;
        getData();
        SuccessFragment successFragment = new SuccessFragment();
        successFragment.setTint("发布成功");
        successFragment.show(getChildFragmentManager(), "");
        mHandler.postDelayed(successFragment::dismiss, 1500);
    }

    public void getData() {

        if (page == 1) {
            loadingDialog = new LoadDialog(mMainActivity);
            loadingDialog.showLoadingDialog();
        }

        loveEngin.getMyCommunityInfos(UserInfoHelper.getUid(), page, PAGE_SIZE).subscribe(new DisposableObserver<ResultInfo<CommunityInfoWrapper>>() {
            @Override
            public void onComplete() {
                if (loadingDialog != null) loadingDialog.dismissLoadingDialog();
                if (swipeRefreshLayout.isRefreshing()) swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onError(Throwable e) {
                if (loadingDialog != null) loadingDialog.dismissLoadingDialog();
                if (swipeRefreshLayout.isRefreshing()) swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onNext(ResultInfo<CommunityInfoWrapper> communityInfoWrapperResultInfo) {
                if (loadingDialog != null) loadingDialog.dismissLoadingDialog();
                if (swipeRefreshLayout.isRefreshing()) swipeRefreshLayout.setRefreshing(false);
                if (communityInfoWrapperResultInfo != null && communityInfoWrapperResultInfo.code == HttpConfig.STATUS_OK) {
                    if (communityInfoWrapperResultInfo.data != null && communityInfoWrapperResultInfo.data.list != null
                            && communityInfoWrapperResultInfo.data.list.size() > 0) {
                        List<CommunityInfo> list = communityInfoWrapperResultInfo.data.list;
                        createNewData(list);
                        topEmptyView.setVisibility(View.GONE);
                    } else {
                        if (page == 1)
                            topEmptyView.setVisibility(View.VISIBLE);
                    }
                }

            }
        });

    }

    private void createNewData(List<CommunityInfo> list) {

        if (page == 1) {
            communityAdapter.setNewData(list);
            CommonInfoHelper.setO(mMainActivity, list, "community_my_info");
        } else {
            communityAdapter.addData(list);

        }

        if (list != null && list.size() == PAGE_SIZE) {
            communityAdapter.loadMoreComplete();
            page++;
        } else {
            communityAdapter.loadMoreEnd();
        }
    }


    private void like(CommunityInfo communityInfo, int position) {

        loveEngin.likeTopic(UserInfoHelper.getUid(), communityInfo.topicId).subscribe(new DisposableObserver<ResultInfo<String>>() {
            @Override
            public void onComplete() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResultInfo<String> stringResultInfo) {
                if (stringResultInfo != null && stringResultInfo.code == HttpConfig.STATUS_OK) {
                    communityInfo.is_dig = 1;
                    communityInfo.like_num = communityInfo.like_num + 1;
                    communityAdapter.notifyItemChanged(position);
//                    imageView.setImageResource(R.mipmap.community_like_selected);
//
//                    textView.setText(String.valueOf(likeNum));
                }
            }
        });
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        page = 1;
    }
}
