package com.yc.love.ui.frament.main;

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
import com.yc.love.adaper.rv.MainT2MoreItemAdapter;
import com.yc.love.adaper.rv.base.RecyclerViewItemListener;
import com.yc.love.adaper.rv.holder.BaseViewHolder;
import com.yc.love.adaper.rv.holder.ProgressBarViewHolder;
import com.yc.love.adaper.rv.holder.MainT2ViewHolder;
import com.yc.love.adaper.rv.holder.MainT2TitleViewHolder;
import com.yc.love.adaper.rv.holder.VipViewHolder;
import com.yc.love.model.bean.MainT2Bean;
import com.yc.love.ui.frament.base.BaseMainFragment;
import com.yc.love.ui.view.LoadDialog;
import com.yc.love.ui.view.LoadingDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mayn on 2019/4/23.
 */

public class MainT2Fragment extends BaseMainFragment {
    private List<MainT2Bean> stringBeans;
    private int PAGE_NUM = 10;
    private boolean loadMoreEnd;
    private boolean loadDataEnd;
    private boolean showProgressBar = false;
    private MainT2MoreItemAdapter mAdapter;
    private int num = 10;
    private ProgressBarViewHolder progressBarViewHolder;


    @Override
    protected int setContentView() {
        return R.layout.fragment_main_t2;
    }

    @Override
    protected void initViews() {

        final RecyclerView recyclerView = rootView.findViewById(R.id.main_t2_rl);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mMainActivity);
        recyclerView.setLayoutManager(layoutManager);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        //设置增加或删除条目的动画
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        stringBeans = new ArrayList<>();
        stringBeans.add(new MainT2Bean("tit", 0));
        for (int i = 0; i < 10; i++) {
            stringBeans.add(new MainT2Bean("name " + i, 1));
            if (i == 4) {
                stringBeans.add(new MainT2Bean("vip", 2));
            }
        }


        mAdapter = new MainT2MoreItemAdapter<MainT2Bean>(stringBeans, recyclerView) {
            @Override
            public BaseViewHolder getHolder(ViewGroup parent) {
                return new MainT2ViewHolder(mMainActivity, recyclerViewItemListener, parent);
            }

            @Override
            public BaseViewHolder getTitleHolder(ViewGroup parent) {
                MainT2TitleViewHolder mainT2TitleViewHolder = new MainT2TitleViewHolder(mMainActivity, null, parent);
                mainT2TitleViewHolder.setOnClickShareListent(new MainT2TitleViewHolder.OnClickShareListent() {
                    @Override
                    public void clickShareListent() {
                        Toast.makeText(mMainActivity, "clickShare", Toast.LENGTH_SHORT).show();
                    }
                });
                return mainT2TitleViewHolder;
            }

            @Override
            protected RecyclerView.ViewHolder getVipHolder(ViewGroup parent) {
                VipViewHolder vipViewHolder = new VipViewHolder(mMainActivity, null, parent);
                return vipViewHolder;
            }


            @Override
            protected RecyclerView.ViewHolder getBarViewHolder(ViewGroup parent) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_test_item_footer, parent, false);
                progressBarViewHolder = new ProgressBarViewHolder(view);
                return progressBarViewHolder;
            }


        };
        recyclerView.setAdapter(mAdapter);
        if (stringBeans.size() < PAGE_NUM) {
            Log.d("ssss", "loadMoreData: data---end");
        } else {
            mAdapter.setOnMoreDataLoadListener(new MainT2MoreItemAdapter.OnLoadMoreDataListener() {
                @Override
                public void loadMoreData() {
                    if (loadDataEnd == false) {
                        return;
                    }
                    if (showProgressBar == false) {
                        //加入null值此时adapter会判断item的type
                        stringBeans.add(null);
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

                                List<MainT2Bean> netLoadMoreData = new ArrayList<>();
                                for (int i = 0; i < 10; i++) {
                                    netLoadMoreData.add(new MainT2Bean("name " + (i + num), 1));
                                }
                                num += 10;
                                showProgressBar = false;
                                stringBeans.remove(stringBeans.size() - 1);
                                mAdapter.notifyDataSetChanged();
                                if (netLoadMoreData.size() < PAGE_NUM) {
                                    loadMoreEnd = true;
                                }
                                stringBeans.addAll(netLoadMoreData);
                                mAdapter.notifyDataSetChanged();
                                mAdapter.setLoaded();
                            }
                        }, 1000);
                    } else {
                        Log.d("mylog", "loadMoreData: loadMoreEnd end 已加载全部数据 ");
                        stringBeans.remove(stringBeans.size() - 1);
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
            Toast.makeText(mMainActivity, "onItemClick " + position, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onItemLongClick(int position) {

        }
    };

    @Override
    protected void lazyLoad() {
        isCanLoadData();
    }

    private void isCanLoadData() {

       /* final LoadingDialog loadingView = new LoadingDialog(mMainActivity);
        loadingView.showLoading();
        tvName.postDelayed(new Runnable() {
            @Override
            public void run() {
                tvName.setText(getClass().getName());
                loadingView.dismissLoading();
            }
        }, 400);*/
    }
}
