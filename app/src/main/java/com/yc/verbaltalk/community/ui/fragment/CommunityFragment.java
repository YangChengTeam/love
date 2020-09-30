package com.yc.verbaltalk.community.ui.fragment;

import android.util.Log;
import android.view.View;

import com.alibaba.fastjson.TypeReference;
import com.umeng.analytics.MobclickAgent;
import com.yc.verbaltalk.R;
import com.yc.verbaltalk.base.engine.LoveEngine;
import com.yc.verbaltalk.base.fragment.BaseMainFragment;
import com.yc.verbaltalk.base.utils.CommonInfoHelper;
import com.yc.verbaltalk.base.utils.ItemDecorationHelper;
import com.yc.verbaltalk.base.utils.UserInfoHelper;
import com.yc.verbaltalk.base.view.LoadDialog;
import com.yc.verbaltalk.chat.bean.CommunityInfo;
import com.yc.verbaltalk.chat.bean.CommunityInfoWrapper;
import com.yc.verbaltalk.community.adapter.CommunityAdapter;
import com.yc.verbaltalk.community.ui.activity.CommunityDetailActivity;

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
public class CommunityFragment extends BaseMainFragment implements View.OnClickListener {

    private RecyclerView recyclerView;


    private LoveEngine loveEngin;
    private CommunityAdapter communityAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    private int page = 1;
    private final int PAGE_SIZE = 10;
    private LoadDialog loadingDialog;


    @Override
    protected int setContentView() {
        return R.layout.fragment_community;
    }

    @Override
    protected void initViews() {


        recyclerView = rootView.findViewById(R.id.rv_community);

        swipeRefreshLayout = rootView.findViewById(R.id.swipeRefreshLayout);
        loveEngin = new LoveEngine(mMainActivity);
        initRecyclerView();
    }

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(mMainActivity));
        communityAdapter = new CommunityAdapter(null);
        recyclerView.setAdapter(communityAdapter);
        recyclerView.addItemDecoration(new ItemDecorationHelper(mMainActivity, 10));

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
                if (null != communityInfo) {

                    CommunityDetailActivity.StartActivity(mMainActivity, getString(R.string.community_detail), communityInfo.topicId);
                    MobclickAgent.onEvent(mMainActivity, "look_community", "查看发帖详情");
                }
        });


        communityAdapter.setOnLoadMoreListener(this::getData, recyclerView);

        communityAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            CommunityInfo communityInfo = communityAdapter.getItem(position);
            if (communityInfo != null) {

                switch (view.getId()) {
                    case R.id.iv_like:
                    case R.id.ll_like:
//                        ImageView iv = view.findViewById(R.id.iv_like);
                        if (UserInfoHelper.isLogin(getActivity()))
                            if (communityInfo.is_dig == 0) {//未点赞
//                            TextView textView = communityAdapter.getView(position);
                                like(communityInfo, position);
                            }

                        break;
                }
            }
        });


    }

    private void initData() {
        CommonInfoHelper.getO(mMainActivity, "community_newst_data", new TypeReference<List<CommunityInfo>>() {
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
        initListener();
    }

    public void getData() {

        if (page == 1) {
            loadingDialog = new LoadDialog(mMainActivity);
            loadingDialog.showLoadingDialog();
        }

        loveEngin.getCommunityNewstInfos(UserInfoHelper.getUid(), page, PAGE_SIZE).subscribe(new DisposableObserver<ResultInfo<CommunityInfoWrapper>>() {
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
                if (communityInfoWrapperResultInfo != null && communityInfoWrapperResultInfo.code == HttpConfig.STATUS_OK
                        && communityInfoWrapperResultInfo.data != null) {
                    List<CommunityInfo> list = communityInfoWrapperResultInfo.data.list;
                    createNewData(list);

                }


            }
        });

    }

    private void createNewData(List<CommunityInfo> list) {

        if (page == 1) {
            CommonInfoHelper.setO(mMainActivity, list, "community_newst_data");
            communityAdapter.setNewData(list);
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
