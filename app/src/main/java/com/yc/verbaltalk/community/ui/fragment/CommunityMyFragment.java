package com.yc.verbaltalk.community.ui.fragment;

import android.os.Handler;
import android.view.View;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.yc.verbaltalk.R;
import com.yc.verbaltalk.community.adapter.CommunityAdapter;
import com.yc.verbaltalk.chat.bean.CommunityInfo;
import com.yc.verbaltalk.chat.bean.CommunityInfoWrapper;
import com.yc.verbaltalk.chat.bean.event.CommunityPublishSuccess;
import com.yc.verbaltalk.base.engine.LoveEngine;
import com.yc.verbaltalk.model.single.YcSingle;
import com.yc.verbaltalk.community.ui.activity.CommunityDetailActivity;
import com.yc.verbaltalk.base.fragment.BaseMainFragment;
import com.yc.verbaltalk.base.view.LoadDialog;
import com.yc.verbaltalk.base.utils.CommonInfoHelper;
import com.yc.verbaltalk.base.utils.ItemDecorationHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import rx.Subscriber;

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
    private View topEmptyView;
    private Handler mHandler;

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
        initData();
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
        int userId = YcSingle.getInstance().id;
        if (page == 1) {
            loadingDialog = new LoadDialog(mMainActivity);
            loadingDialog.showLoadingDialog();
        }

        loveEngin.getMyCommunityInfos(String.valueOf(userId), page, PAGE_SIZE).subscribe(new Subscriber<ResultInfo<CommunityInfoWrapper>>() {
            @Override
            public void onCompleted() {
                if (loadingDialog != null) loadingDialog.dismissLoadingDialog();
                if (swipeRefreshLayout.isRefreshing()) swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResultInfo<CommunityInfoWrapper> communityInfoWrapperResultInfo) {
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
        int userId = YcSingle.getInstance().id;
        loveEngin.likeTopic(String.valueOf(userId), communityInfo.topicId).subscribe(new Subscriber<ResultInfo<String>>() {
            @Override
            public void onCompleted() {

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
