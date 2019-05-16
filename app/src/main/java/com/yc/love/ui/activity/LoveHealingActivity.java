package com.yc.love.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yc.love.R;
import com.yc.love.adaper.rv.LoveHealingAdapter;
import com.yc.love.adaper.rv.base.RecyclerViewItemListener;
import com.yc.love.adaper.rv.holder.BaseViewHolder;
import com.yc.love.adaper.rv.holder.LoveHealingItemTitleViewHolder;
import com.yc.love.adaper.rv.holder.LoveHealingItemViewHolder;
import com.yc.love.adaper.rv.holder.ProgressBarViewHolder;
import com.yc.love.adaper.rv.holder.LoveHealingTitleViewHolder;
import com.yc.love.model.base.MySubscriber;
import com.yc.love.model.bean.AResultInfo;
import com.yc.love.model.bean.LoveHealingBean;
import com.yc.love.model.bean.event.EventLoginState;
import com.yc.love.model.bean.event.EventLoveUpDownCollectChangBean;
import com.yc.love.model.engin.LoveEngin;
import com.yc.love.model.single.YcSingle;
import com.yc.love.ui.activity.base.BaseSameActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;


/**
 * 治愈情话
 */

public class LoveHealingActivity extends BaseSameActivity {
    private List<LoveHealingBean> loveHealingBeans = new ArrayList<>();
    private LoveHealingAdapter mAdapter;
    private int PAGE_SIZE = 10;
    private int PAGE_NUM = 1;
    private boolean loadMoreEnd;
    private boolean loadDataEnd;
    private boolean showProgressBar = false;
    //    private MainT2MoreItemAdapter mAdapter;
    private ProgressBarViewHolder progressBarViewHolder;
    private LoveEngin mLoveEngin;
    private RecyclerView mRecyclerView;
//    private int clickToPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_love_healing);
        mLoveEngin = new LoveEngin(this);
        initViews();
        netData();

    }

    protected void initViews() {
        mRecyclerView = findViewById(R.id.love_healing_rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(LoveHealingActivity.this);
        mRecyclerView.setLayoutManager(layoutManager);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        //设置增加或删除条目的动画
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private void netData() {
        mLoadingDialog.showLoadingDialog();
        mLoveEngin.recommendLovewords(String.valueOf(YcSingle.getInstance().id),String.valueOf(PAGE_NUM), String.valueOf(PAGE_SIZE), "lovewords/recommend")
                .subscribe(new MySubscriber<AResultInfo<List<LoveHealingBean>>>(mLoadingDialog) {
                    @Override
                    protected void onNetNext(AResultInfo<List<LoveHealingBean>> listAResultInfo) {
                        List<LoveHealingBean> loveHealingBeanList = listAResultInfo.data;
                        loveHealingBeans.add(new LoveHealingBean(0, "title_img"));
                        loveHealingBeans.add(new LoveHealingBean(1, "为你推荐 "));
                        for (int i = 0; i < loveHealingBeanList.size(); i++) {
                            LoveHealingBean loveHealingBean = loveHealingBeanList.get(i);
                            loveHealingBeans.add(new LoveHealingBean(2, loveHealingBean.chat_count, loveHealingBean.chat_name, loveHealingBean.id, loveHealingBean.quiz_sex, loveHealingBean.search_type));
                        }
                        initRecyclerData();
                    }

                    @Override
                    protected void onNetError(Throwable e) {

                    }

                    @Override
                    protected void onNetCompleted() {

                    }
                });
    }

    private void initRecyclerData() {
        mAdapter = new LoveHealingAdapter(loveHealingBeans, mRecyclerView) {
            @Override
            public BaseViewHolder getHolder(ViewGroup parent) {
                return new LoveHealingItemViewHolder(LoveHealingActivity.this, recyclerViewItemListener, parent);
            }

            @Override
            public BaseViewHolder getTitleHolder(ViewGroup parent) {
                return new LoveHealingTitleViewHolder(LoveHealingActivity.this, null, parent);
            }

            @Override
            protected RecyclerView.ViewHolder getItemTitleHolder(ViewGroup parent) {
                return new LoveHealingItemTitleViewHolder(LoveHealingActivity.this, null, parent);
            }

            @Override
            protected RecyclerView.ViewHolder getBarViewHolder(ViewGroup parent) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_test_item_footer, parent, false);
                progressBarViewHolder = new ProgressBarViewHolder(view);
                return progressBarViewHolder;
            }


        };
        mRecyclerView.setAdapter(mAdapter);
        if (loveHealingBeans.size() < PAGE_SIZE) {
            Log.d("mylog", "loadMoreData: data---end");
        } else {
            mAdapter.setOnMoreDataLoadListener(new LoveHealingAdapter.OnLoadMoreDataListener() {
                @Override
                public void loadMoreData() {
                    if (loadDataEnd == false) {
                        return;
                    }
                    if (showProgressBar == false) {
                        //加入null值此时adapter会判断item的type
                        loveHealingBeans.add(null);
//                        loveHealingBeans.add(new LoveHealingBean("name ", 3));
                        mAdapter.notifyDataSetChanged();
                        showProgressBar = true;
                    }
                    if (!loadMoreEnd) {
                        netLoadMoreData();
                    } else {
                        Log.d("mylog", "loadMoreData: loadMoreEnd end 已加载全部数据 ");
                        loveHealingBeans.remove(loveHealingBeans.size() - 1);
                        mAdapter.notifyDataSetChanged();
                    }
                }
            });
        }
        loadDataEnd = true;
    }

    private void netLoadMoreData() {
        mLoveEngin.recommendLovewords(String.valueOf(YcSingle.getInstance().id),String.valueOf(++PAGE_NUM), String.valueOf(PAGE_SIZE), "lovewords/recommend")
                .subscribe(new MySubscriber<AResultInfo<List<LoveHealingBean>>>(mLoadingDialog) {
                    @Override
                    protected void onNetNext(AResultInfo<List<LoveHealingBean>> listAResultInfo) {
                        List<LoveHealingBean> netLoadMoreData = listAResultInfo.data;
                        showProgressBar = false;
                        loveHealingBeans.remove(loveHealingBeans.size() - 1);
                        mAdapter.notifyDataSetChanged();
                        if (netLoadMoreData.size() < PAGE_SIZE) {
                            loadMoreEnd = true;
                        }
                        for (int i = 0; i < netLoadMoreData.size(); i++) {
                            LoveHealingBean loveHealingBean = netLoadMoreData.get(i);
                            loveHealingBeans.add(new LoveHealingBean(2, loveHealingBean.chat_count, loveHealingBean.chat_name, loveHealingBean.id, loveHealingBean.quiz_sex, loveHealingBean.search_type));
                        }
//                        loveHealingBeans.addAll(netLoadMoreData);
                        mAdapter.notifyDataSetChanged();
                        mAdapter.setLoaded();

                    }

                    @Override
                    protected void onNetError(Throwable e) {

                    }

                    @Override
                    protected void onNetCompleted() {

                    }
                });
    }

    RecyclerViewItemListener recyclerViewItemListener = new RecyclerViewItemListener() {
        @Override
        public void onItemClick(int position) {
            if (position >= 2) {
                LoveUpDownPhotoActivity.startLoveUpDownPhotoActivity(LoveHealingActivity.this, position - 1);
            }
//            LoveHealingActivity.this.clickToPosition = position;
        }

        @Override
        public void onItemLongClick(int position) {

        }
    };

   /* @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventLoveUpDownCollectChangBean event) {
        boolean isCollectLovewords = event.isCollectLovewords;
        LoveHealingBean loveHealingBean = loveHealingBeans.get(clickToPosition);
        Log.d("mylog", "onMessageEvent: isCollectLovewords  " + isCollectLovewords + " clickToPosition " + clickToPosition);
        if (isCollectLovewords) {
            loveHealingBean.is_collect = 1;
        } else {
            loveHealingBean.is_collect = 0;
        }
        mAdapter.notifyItemChanged(clickToPosition);
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
    }*/

    @Override
    protected String offerActivityTitle() {
        return "治愈情话";
    }
}
