package com.yc.verbaltalk.chat.ui.fragment;

import android.content.Intent;

import com.alibaba.fastjson.TypeReference;


import com.music.player.lib.bean.MusicInfo;
import com.music.player.lib.bean.MusicInfoWrapper;
import com.yc.verbaltalk.R;
import com.yc.verbaltalk.base.utils.UserInfoHelper;
import com.yc.verbaltalk.chat.adapter.ChatMainAdapter;
import com.yc.verbaltalk.chat.bean.ChatInfo;
import com.yc.verbaltalk.chat.bean.CourseInfo;
import com.yc.verbaltalk.base.engine.LoveEngine;
import com.yc.verbaltalk.chat.ui.activity.ChatCourseDetailActivity;
import com.yc.verbaltalk.chat.ui.activity.ChatRecommendDetailActivity;
import com.yc.verbaltalk.base.fragment.BaseMainFragment;
import com.yc.verbaltalk.chat.ui.activity.AudioActivity;
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
 * Created by suns  on 2019/11/16 12:11.
 * 撩吧
 */
public class ChatFragment extends BaseMainFragment {

    private RecyclerView chatRecyclerview;
    private ChatMainAdapter chatMainAdapter;

    private int page = 1;
    private int PAGE_SIZE = 10;

    private LoadDialog loadingDialog;

    private LoveEngine mLoveEngine;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<MusicInfo> musicInfoList;

    @Override
    protected int setContentView() {
        return R.layout.fragment_main_chat;
    }

    @Override
    protected void initViews() {
        mLoveEngine = new LoveEngine(mMainActivity);
        chatRecyclerview = rootView.findViewById(R.id.chat_recyclerView);
        swipeRefreshLayout = rootView.findViewById(R.id.swipeRefreshLayout);

        chatRecyclerview.setLayoutManager(new GridLayoutManager(mMainActivity, 2));

        chatMainAdapter = new ChatMainAdapter(null);
        chatRecyclerview.setAdapter(chatMainAdapter);
        initListener();

    }

    private void initData() {


        CommonInfoHelper.getO(mMainActivity, "main_chat_infos", new TypeReference<List<ChatInfo>>() {
        }.getType(), (CommonInfoHelper.onParseListener<List<ChatInfo>>) datas -> {
            if (datas != null && datas.size() > 0 && page == 1) {
                chatMainAdapter.setNewData(datas);
            }
        });


        getData();

    }

    private void initListener() {

        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(mMainActivity, R.color.red_crimson));
        swipeRefreshLayout.setOnRefreshListener(() -> {
            page = 1;
            getData();
        });

        chatMainAdapter.setOnItemClickListener((adapter, view, position) -> {
            ChatInfo chatInfo = chatMainAdapter.getItem(position);
            if (chatInfo != null) {
                switch (chatInfo.type) {
                    case ChatInfo.ITEM_TYPE_RECOMMEND:

                        Intent intent = new Intent(mMainActivity, ChatRecommendDetailActivity.class);
                        intent.putExtra("type_id", chatInfo.musicInfo.getId());

                        startActivity(intent);
                        break;
                    case ChatInfo.ITEM_TYPE_COURSE:
                        Intent intent1 = new Intent(mMainActivity, ChatCourseDetailActivity.class);

                        intent1.putExtra("id", chatInfo.courseInfo.getId());
                        startActivity(intent1);

                        break;
                }
            }
        });
        chatMainAdapter.setOnLoadMoreListener(this::getCourseData, chatRecyclerview);

        chatMainAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            ChatInfo chatInfo = chatMainAdapter.getItem(position);
            if (chatInfo != null) {
                int itemType = chatInfo.getItemType();
                if (itemType == ChatInfo.ITEM_TYPE_RECOMMEND) {
                    if (view.getId() == R.id.tv_more_audio) {
                        startActivity(new Intent(mMainActivity, AudioActivity.class));
                    }
                }
            }
        });
    }

    @Override
    protected void lazyLoad() {
        initData();
    }


    private void getData() {


        if (page == 1) {
            loadingDialog = new LoadDialog(mMainActivity);
            loadingDialog.showLoadingDialog();
        }

        mLoveEngine.getLoveItemList(UserInfoHelper.getUid(), null, page, 3, 1).subscribe(new DisposableObserver<ResultInfo<MusicInfoWrapper>>() {
            @Override
            public void onComplete() {

            }

            @Override
            public void onError(Throwable e) {


            }

            @Override
            public void onNext(ResultInfo<MusicInfoWrapper> listResultInfo) {


                if (listResultInfo != null && listResultInfo.code == HttpConfig.STATUS_OK && listResultInfo.data != null) {

                    musicInfoList = listResultInfo.data.getList();
                    getCourseData();

                }
            }
        });


    }

    private void getCourseData() {
        mLoveEngine.getChatCourseInfos(page, PAGE_SIZE).subscribe(new DisposableObserver<ResultInfo<List<CourseInfo>>>() {
            @Override
            public void onComplete() {

            }

            @Override
            public void onError(Throwable e) {
                if (loadingDialog != null) loadingDialog.dismissLoadingDialog();
                if (swipeRefreshLayout.isRefreshing()) swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onNext(ResultInfo<List<CourseInfo>> listResultInfo) {
                if (loadingDialog != null) loadingDialog.dismissLoadingDialog();
                if (swipeRefreshLayout.isRefreshing()) swipeRefreshLayout.setRefreshing(false);
                if (listResultInfo != null && listResultInfo.code == HttpConfig.STATUS_OK && listResultInfo.data != null) {
                    createNewData(listResultInfo.data);
                }

            }
        });
    }


    private void createNewData(List<CourseInfo> data) {
        List<ChatInfo> chatInfos = new ArrayList<>();

        if (page == 1) {
            if (musicInfoList != null && musicInfoList.size() > 0) {
                for (int i = 0; i < musicInfoList.size(); i++) {
                    ChatInfo chatInfo = new ChatInfo(ChatInfo.ITEM_TYPE_RECOMMEND);
                    chatInfo.musicInfo = musicInfoList.get(i);
                    if (i == 0) {
                        chatInfo.isShowCateTitle = true;
                    }
                    if (i == 2) {
                        chatInfo.isShowDivider = false;
                    }
                    chatInfos.add(chatInfo);

                }
            }
            ChatInfo chatInfoDivder = new ChatInfo(ChatInfo.ITEM_TYPE_DIVIDER);
            chatInfos.add(chatInfoDivder);
        }
        if (data != null && data.size() > 0) {
            for (int i = 0; i < data.size(); i++) {
                ChatInfo chatInfo = new ChatInfo(ChatInfo.ITEM_TYPE_COURSE);
                chatInfo.courseInfo = data.get(i);
                if (i == 0) {
                    chatInfo.isShowCateTitle = true;
                }
                if (i == 1) {
                    chatInfo.isShowCourseCategory = true;
                }

                chatInfos.add(chatInfo);
            }
        }

        if (page == 1) {
            chatMainAdapter.setNewData(chatInfos);
            CommonInfoHelper.setO(mMainActivity, chatInfos, "main_chat_infos");
        } else {
            chatMainAdapter.addData(chatInfos);
        }

        if (data != null && data.size() == PAGE_SIZE) {
            chatMainAdapter.loadMoreComplete();
            page++;
        } else {
            chatMainAdapter.loadMoreEnd();
        }


    }


}
