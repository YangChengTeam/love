package com.yc.verbaltalk.community.ui.fragment;

import android.util.Log;
import android.view.View;

import com.alibaba.fastjson.TypeReference;

import com.umeng.analytics.MobclickAgent;
import com.yc.verbaltalk.R;
import com.yc.verbaltalk.base.utils.UserInfoHelper;
import com.yc.verbaltalk.community.adapter.CommunityHotAdapter;
import com.yc.verbaltalk.chat.bean.CommunityInfo;
import com.yc.verbaltalk.chat.bean.CommunityInfoWrapper;
import com.yc.verbaltalk.chat.bean.CommunityTagInfo;
import com.yc.verbaltalk.chat.bean.CommunityTagInfoWrapper;
import com.yc.verbaltalk.base.engine.LoveEngine;
import com.yc.verbaltalk.community.ui.activity.CommunityDetailActivity;
import com.yc.verbaltalk.community.ui.activity.CommunityTagListActivity;
import com.yc.verbaltalk.base.fragment.BaseMainFragment;
import com.yc.verbaltalk.base.view.LoadDialog;
import com.yc.verbaltalk.base.utils.CommonInfoHelper;

import java.util.ArrayList;
import java.util.List;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import io.reactivex.observers.DisposableObserver;

import yc.com.rthttplibrary.bean.ResultInfo;
import yc.com.rthttplibrary.config.HttpConfig;

/**
 * Created by suns  on 2019/8/28 09:17.
 */
public class CommunityHotFragment extends BaseMainFragment implements View.OnClickListener {

    private static final String TAG = "CommunityHotFragment";
    private RecyclerView recyclerView;


    private LoveEngine loveEngin;
    private CommunityHotAdapter communityAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    private List<CommunityInfo> communityInfoList;
    private LoadDialog loadDialog;
    private int page = 1;
    private int PAGE_SIZE = 10;
    private CommunityTagInfoWrapper communityTagInfoWrapper;

    @Override
    protected int setContentView() {
        return R.layout.fragment_community_hot;
    }

    @Override
    protected void initViews() {


        recyclerView = rootView.findViewById(R.id.rv_hot_community);

        swipeRefreshLayout = rootView.findViewById(R.id.swipeRefreshLayout);

        loveEngin = new LoveEngine(mMainActivity);
        initRecyclerView();

    }

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new GridLayoutManager(mMainActivity, 3));
        communityAdapter = new CommunityHotAdapter(null);
        recyclerView.setAdapter(communityAdapter);

    }



    private void initListener() {
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(mMainActivity, R.color.red_crimson));
        swipeRefreshLayout.setOnRefreshListener(() -> {
            page = 1;
            getTagInfo();
        });

        communityAdapter.setOnItemClickListener((adapter, view, position) -> {
            CommunityInfo communityInfo = communityAdapter.getItem(position);
            if (communityInfo != null) {
                if (CommunityInfo.ITEM_TOP_ACTIVITY == communityInfo.itemType) {

                } else if (CommunityInfo.ITEM_TAG == communityInfo.itemType) {
                    // TODO: 2019/8/30 社区标签

                    CommunityTagListActivity.StartActivity(mMainActivity, communityInfo.tagInfo.getTitle(), communityInfo.tagInfo.getId());

                } else if (CommunityInfo.ITEM_CONTENT == communityInfo.itemType) {
                    MobclickAgent.onEvent(mMainActivity, "look_community", "查看发帖详情");
                    if (UserInfoHelper.isLogin(mMainActivity))
                        CommunityDetailActivity.StartActivity(mMainActivity, getString(R.string.community_detail), communityInfo.topicId);
                }
            }


        });
        communityAdapter.setOnLoadMoreListener(this::getData, recyclerView);
        communityAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            CommunityInfo communityInfo = communityAdapter.getItem(position);

            if (communityInfo != null) {
                if (CommunityInfo.ITEM_CONTENT == communityInfo.itemType) {
                    switch (view.getId()) {
                        case R.id.iv_like:
                        case R.id.ll_like:
                            if (UserInfoHelper.isLogin(getActivity()))
                                if (communityInfo.is_dig == 0) {//未点赞

                                    like(communityInfo, position);
                                }

                            break;
                    }
                } else if (CommunityInfo.ITEM_TOP_ACTIVITY == communityInfo.itemType) {
                    if (view.getId() == R.id.iv_hot_activity) {
                        mMainActivity.showToWxServiceDialog(null);
                    }
                }

            }
        });

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
                    int likeNum = communityInfo.like_num + 1;
                    communityInfo.like_num = likeNum;
                    communityAdapter.notifyItemChanged(position);
//                    imageView.setImageResource(R.mipmap.community_like_selected);
//
//                    textView.setText(String.valueOf(likeNum));
                }
            }
        });
    }

    @Override
    protected void lazyLoad() {
//        getTagInfo();
        initData();
        initListener();
    }

    private void initData() {
        CommonInfoHelper.getO(mMainActivity, "community_hot_info", new TypeReference<List<CommunityInfo>>() {
        }.getType(), (CommonInfoHelper.onParseListener<List<CommunityInfo>>) datas -> {
            if (datas != null && datas.size() > 0) {
                createNewData(datas);
            }
        });
        getTagInfo();
    }


    public void getTagInfo() {

        if (page == 1) {
            loadDialog = new LoadDialog(mMainActivity);
            loadDialog.showLoadingDialog();
        }

        loveEngin.getCommunityTagInfos().subscribe(new DisposableObserver<ResultInfo<CommunityTagInfoWrapper>>() {
            @Override
            public void onComplete() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResultInfo<CommunityTagInfoWrapper> communityTagInfoWrapperResultInfo) {
                if (communityTagInfoWrapperResultInfo != null && communityTagInfoWrapperResultInfo.code == HttpConfig.STATUS_OK
                        && communityTagInfoWrapperResultInfo.data != null) {
                    communityTagInfoWrapper = communityTagInfoWrapperResultInfo.data;
                    getData();
                }

            }
        });


    }

    public void getData() {


        loveEngin.getCommunityHotList(UserInfoHelper.getUid(), page, PAGE_SIZE).subscribe(new DisposableObserver<ResultInfo<CommunityInfoWrapper>>() {
            @Override
            public void onComplete() {
                if (loadDialog != null) loadDialog.dismissLoadingDialog();
                if (swipeRefreshLayout.isRefreshing()) swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onError(Throwable e) {
                if (loadDialog != null) loadDialog.dismissLoadingDialog();
                if (swipeRefreshLayout.isRefreshing()) swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onNext(ResultInfo<CommunityInfoWrapper> communityInfoWrapperResultInfo) {
                if (loadDialog != null) loadDialog.dismissLoadingDialog();
                if (swipeRefreshLayout.isRefreshing()) swipeRefreshLayout.setRefreshing(false);
                if (communityInfoWrapperResultInfo != null && communityInfoWrapperResultInfo.code == HttpConfig.STATUS_OK) {
                    CommunityInfoWrapper communityInfoWrapper = communityInfoWrapperResultInfo.data;
                    if (communityInfoWrapper != null && communityInfoWrapper.list != null) {
                        List<CommunityInfo> communityInfos = communityInfoWrapper.list;
                        createNewData(communityInfos);
                    }

                }
            }
        });


    }

    private void createNewData(List<CommunityInfo> communityInfos) {
        communityInfoList = new ArrayList<>();
        if (page == 1) {
            if (null != communityTagInfoWrapper) {
                CommunityInfo communityInfo = new CommunityInfo(CommunityInfo.ITEM_TOP_ACTIVITY);
                communityInfo.pic = communityTagInfoWrapper.getImage();
                communityInfoList.add(communityInfo);
                List<CommunityTagInfo> communityTagInfos = communityTagInfoWrapper.getList();
                if (null != communityTagInfos && communityTagInfos.size() > 0) {
                    for (CommunityTagInfo datum : communityTagInfos) {
                        communityInfoList.add(new CommunityInfo(CommunityInfo.ITEM_TAG, datum));
                    }
                    communityInfoList.add(new CommunityInfo(CommunityInfo.ITEM_DIVIDER));
                }
            }

        }
        if (null != communityInfos) {
            for (CommunityInfo info : communityInfos) {
                info.itemType = CommunityInfo.ITEM_CONTENT;

                communityInfoList.add(info);
            }

            if (page == 1) {
                CommonInfoHelper.setO(mMainActivity, communityInfoList, "community_hot_info");
                communityAdapter.setNewData(communityInfoList);
            } else {
                communityAdapter.addData(communityInfoList);
            }

            if (communityInfos.size() == PAGE_SIZE) {
                communityAdapter.loadMoreComplete();
                page++;
                communityAdapter.isEnd(false);
            } else {
                communityAdapter.loadMoreEnd();
                communityAdapter.isEnd(true);
            }
        }

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
