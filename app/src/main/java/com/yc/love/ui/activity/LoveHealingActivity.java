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
import android.widget.Toast;

import com.yc.love.R;
import com.yc.love.adaper.rv.LoveHealingAdapter;
import com.yc.love.adaper.rv.base.RecyclerViewItemListener;
import com.yc.love.adaper.rv.holder.BaseViewHolder;
import com.yc.love.adaper.rv.holder.LoveHealingItemTitleViewHolder;
import com.yc.love.adaper.rv.holder.LoveHealingItemViewHolder;
import com.yc.love.adaper.rv.holder.MainT3ItemTitleViewHolder;
import com.yc.love.adaper.rv.holder.ProgressBarViewHolder;
import com.yc.love.adaper.rv.holder.LoveHealingTitleViewHolder;
import com.yc.love.model.bean.LoveHealingBean;
import com.yc.love.ui.activity.base.BaseSameActivity;

import java.util.ArrayList;
import java.util.List;


/**
 * 治愈情话
 */

public class LoveHealingActivity extends BaseSameActivity {
    private List<LoveHealingBean> loveHealingBeans;
    private LoveHealingAdapter mAdapter;
    private int PAGE_NUM = 10;
    private boolean loadMoreEnd;
    private boolean loadDataEnd;
    private boolean showProgressBar = false;
    //    private MainT2MoreItemAdapter mAdapter;
    private int num = 10;
    private ProgressBarViewHolder progressBarViewHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_love_healing);

        initViews();
    }

    protected void initViews() {
//        tvName = rootView.findViewById(R.id.main_t1_tv_name);

        final RecyclerView recyclerView = findViewById(R.id.love_healing_rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(LoveHealingActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        //设置增加或删除条目的动画
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        loveHealingBeans = new ArrayList<>();
        loveHealingBeans.add(new LoveHealingBean("name ", 0));
        loveHealingBeans.add(new LoveHealingBean("为你推荐 ", 1));
        for (int i = 0; i < 10; i++) {
            loveHealingBeans.add(new LoveHealingBean("name " + i, 2));
        }


        mAdapter = new LoveHealingAdapter<LoveHealingBean>(loveHealingBeans, recyclerView) {
            @Override
            public BaseViewHolder getHolder(ViewGroup parent) {
                return new LoveHealingItemViewHolder(LoveHealingActivity.this, recyclerViewItemListener, parent);
            }

            @Override
            public BaseViewHolder getTitleHolder(ViewGroup parent) {
                return new LoveHealingTitleViewHolder(LoveHealingActivity.this, null, parent);
            }

            @Override
            protected RecyclerView.ViewHolder getBarViewHolder(ViewGroup parent) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_test_item_footer, parent, false);
                progressBarViewHolder = new ProgressBarViewHolder(view);
                return progressBarViewHolder;
            }

            @Override
            protected RecyclerView.ViewHolder getItemTitleHolder(ViewGroup parent) {
                return new LoveHealingItemTitleViewHolder(LoveHealingActivity.this, null, parent);
            }
        };
        recyclerView.setAdapter(mAdapter);
        if (loveHealingBeans.size() < PAGE_NUM) {
            Log.d("mylog", "loadMoreData: data---end");
        } else {
            Log.d("mylog", "initViews: loveHealingBeans -------------------");
            mAdapter.setOnMoreDataLoadListener(new LoveHealingAdapter.OnLoadMoreDataListener() {
                @Override
                public void loadMoreData() {
                    if (loadDataEnd == false) {
                        return;
                    }
                    if (showProgressBar == false) {
                        Log.d("mylog", "loadMoreData: loveHealingBeans " + loveHealingBeans.size());
                        //加入null值此时adapter会判断item的type
                        loveHealingBeans.add(null);
//                        loveHealingBeans.add(new LoveHealingBean("name ", 3));
                        mAdapter.notifyDataSetChanged();
                        showProgressBar = true;
                    }
                    if (!loadMoreEnd) {
                        recyclerView.postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                if (num >= 41) {
                                    progressBarViewHolder.removePbChangDes("已加载全部数据");
                                    return;
                                }

                                List<LoveHealingBean> netLoadMoreData = new ArrayList<>();
                                for (int i = 0; i < 10; i++) {
                                    netLoadMoreData.add(new LoveHealingBean("name " + (i + num), 2));
                                }
                                num += 10;
                                showProgressBar = false;
                                loveHealingBeans.remove(loveHealingBeans.size() - 1);
                                mAdapter.notifyDataSetChanged();
                                if (netLoadMoreData.size() < PAGE_NUM) {
                                    loadMoreEnd = true;
                                }
                                loveHealingBeans.addAll(netLoadMoreData);
                                mAdapter.notifyDataSetChanged();
                                mAdapter.setLoaded();
                            }
                        }, 1000);
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

    RecyclerViewItemListener recyclerViewItemListener = new RecyclerViewItemListener() {
        @Override
        public void onItemClick(int position) {
//            Toast.makeText(LoveHealingActivity.this, "onItemClick " + position, Toast.LENGTH_SHORT).show();
            startActivity(new Intent(LoveHealingActivity.this,LoveUpDownPhotoActivity.class));
        }

        @Override
        public void onItemLongClick(int position) {

        }
    };

    @Override
    protected String offerActivityTitle() {
        return "治愈情话";
    }
}
