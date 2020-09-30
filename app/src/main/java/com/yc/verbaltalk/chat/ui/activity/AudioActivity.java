package com.yc.verbaltalk.chat.ui.activity;


import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.TypeReference;
import com.music.player.lib.bean.MusicInfo;
import com.music.player.lib.bean.MusicInfoWrapper;
import com.umeng.analytics.MobclickAgent;
import com.yc.verbaltalk.R;
import com.yc.verbaltalk.base.activity.BaseSameActivity;
import com.yc.verbaltalk.base.utils.CommonInfoHelper;
import com.yc.verbaltalk.base.utils.UserInfoHelper;
import com.yc.verbaltalk.base.view.AudioFilterPopwindow;
import com.yc.verbaltalk.base.view.LoadDialog;
import com.yc.verbaltalk.mine.adapter.AudioMainAdapter;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import io.reactivex.observers.DisposableObserver;
import yc.com.rthttplibrary.bean.ResultInfo;
import yc.com.rthttplibrary.config.HttpConfig;
import yc.com.rthttplibrary.util.ScreenUtil;


/**
 * Created by wanglin  on 2018/1/10 17:18.
 */

public class AudioActivity extends BaseSameActivity implements View.OnClickListener {


    private RecyclerView recyclerViewSleep;


    private int itemPage = 1;

    private int pageSize = 10;


    private AudioMainAdapter audioMainAdapter;

    private LoadDialog loadingDialog = null;
    private TextView tvSelectTime, tvSelectHot;
    private TextView tvSelectFilter;
    private RelativeLayout rlSelectContainer;
    private String cateId;
    private int order = 1;//按时间排序
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main_audio);
        initViews();
    }


    protected void initViews() {
        recyclerViewSleep = findViewById(R.id.recyclerView_sleep);

        tvSelectTime = findViewById(R.id.tv_select_time);
        tvSelectHot = findViewById(R.id.tv_select_hot);
        tvSelectFilter = findViewById(R.id.tv_select_filter);
        rlSelectContainer = findViewById(R.id.rl_select_container);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

        tvSelectTime.setSelected(true);
        init();

    }


    public void init() {


        audioMainAdapter = new AudioMainAdapter(null);
        recyclerViewSleep.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewSleep.setAdapter(audioMainAdapter);


//        recyclerViewSleep.addItemDecoration(new MyDecoration());
        initData();
        initListener();

    }

    private void initData() {


        CommonInfoHelper.getO(this, "audio_main_data", new TypeReference<List<MusicInfo>>() {
        }.getType(), (CommonInfoHelper.onParseListener<List<MusicInfo>>) datas -> {
            if (datas != null && itemPage == 1) {
                createNewData(datas, true);
            }
        });


        getData();

    }

    private void getData() {

        if (itemPage == 1) {
            loadingDialog = new LoadDialog(this);
            loadingDialog.showLoadingDialog();
        }


        mLoveEngine.getLoveItemList(UserInfoHelper.getUid(), cateId, itemPage, pageSize, order).subscribe(new DisposableObserver<ResultInfo<MusicInfoWrapper>>() {
            @Override
            public void onComplete() {

            }

            @Override
            public void onError(Throwable e) {
                if (loadingDialog != null) loadingDialog.dismissLoadingDialog();
                if (swipeRefreshLayout.isRefreshing()) swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onNext(ResultInfo<MusicInfoWrapper> listResultInfo) {
                if (loadingDialog != null) loadingDialog.dismissLoadingDialog();
                if (swipeRefreshLayout.isRefreshing()) swipeRefreshLayout.setRefreshing(false);
                if (listResultInfo != null && listResultInfo.code == HttpConfig.STATUS_OK && listResultInfo.data != null) {
                    createNewData(listResultInfo.data.getList(), false);
                }
            }
        });
    }


    private void createNewData(List<MusicInfo> datas, boolean isCache) {
        if (itemPage == 1) {
            audioMainAdapter.setNewData(datas);
//            mCacheWorker.setCache("audio_main_data", datas);
            CommonInfoHelper.setO(this, datas, "audio_main_data");
        } else {
            audioMainAdapter.addData(datas);
        }

        if (datas != null && datas.size() == pageSize && !isCache) {
            audioMainAdapter.loadMoreComplete();
            itemPage++;
        } else {
            audioMainAdapter.loadMoreEnd();
        }


    }


    private void initListener() {
        tvSelectTime.setOnClickListener(this);
        tvSelectHot.setOnClickListener(this);
        tvSelectFilter.setOnClickListener(this);
        audioMainAdapter.setOnLoadMoreListener(this::getData, recyclerViewSleep);

        audioMainAdapter.setOnItemClickListener((adapter, view, position) -> {
            MusicInfo item = audioMainAdapter.getItem(position);

            if (item != null) {
                MobclickAgent.onEvent(this, "audio_play_id", "音频播放");
//                Intent intent = new Intent(this, LoveAudioDetailActivity.class);
//
//                intent.putExtra("type_id", item.getId());
//                intent.putExtra("spa_id", item.getId());
//                startActivity(intent);

                Intent intent = new Intent(this, ChatRecommendDetailActivity.class);
                intent.putExtra("type_id", item.getId());

                startActivity(intent);

            }
        });

        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(this, R.color.red_crimson));
        swipeRefreshLayout.setOnRefreshListener(() -> {
            itemPage = 1;
            getData();
        });

    }

    @Override
    protected String offerActivityTitle() {
        return "撩爱音频";
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_select_time:
                resetState();
                tvSelectTime.setSelected(true);
                startAnimation(tvSelectTime);
                itemPage = 1;
                order = 1;
                getData();
                break;
            case R.id.tv_select_hot:
                resetState();
                tvSelectHot.setSelected(true);
                startAnimation(tvSelectHot);
                itemPage = 1;
                order = 2;
                getData();
                break;
            case R.id.tv_select_filter:
                AudioFilterPopwindow filterPopwindow = new AudioFilterPopwindow(this);
                filterPopwindow.showAsDropDown(rlSelectContainer);
                filterPopwindow.setOnItemClickListener(audioDataInfo -> {
                    if (audioDataInfo != null) {
                        cateId = audioDataInfo.getId();
                        itemPage = 1;
                        getData();
                    }
                });
                break;
        }
    }

    private void resetState() {
        tvSelectHot.setSelected(false);
        tvSelectTime.setSelected(false);
    }


    private class MyDecoration extends RecyclerView.ItemDecoration {
        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view,
                                   @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.set(0, 0, 0, ScreenUtil.dip2px(AudioActivity.this, 10f));
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        itemPage = 1;
    }

    private void startAnimation(TextView textView) {
        ScaleAnimation scaleAnimation = new ScaleAnimation(0f, 1f, 0f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0f, 1f);
        AnimationSet animationSet = new AnimationSet(false);
        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(alphaAnimation);
        animationSet.setDuration(100);
        textView.startAnimation(animationSet);

//
//        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1f);
//        valueAnimator.setTarget(textView);
//        valueAnimator.setDuration(300).start();
//        valueAnimator.addUpdateListener(animation -> {
//
//            textView.getBackground().setAlpha((Integer) animation.getAnimatedValue());
//        });


    }
}
