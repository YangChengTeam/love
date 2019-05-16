package com.yc.love.ui.frament.main;

import android.content.Intent;
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
import com.yc.love.adaper.rv.MainT1MoreItemAdapter;
import com.yc.love.adaper.rv.base.RecyclerViewItemListener;
import com.yc.love.adaper.rv.holder.BaseViewHolder;
import com.yc.love.adaper.rv.holder.MainT1ItemHolder;
import com.yc.love.adaper.rv.holder.ProgressBarViewHolder;
import com.yc.love.adaper.rv.holder.StringBeanViewHolder;
import com.yc.love.adaper.rv.holder.TitleT1ViewHolder;
import com.yc.love.model.base.MySubscriber;
import com.yc.love.model.bean.AResultInfo;
import com.yc.love.model.bean.ExampDataBean;
import com.yc.love.model.bean.ExampListsBean;
import com.yc.love.model.bean.ExampleTsBean;
import com.yc.love.model.bean.ExampleTsListBean;
import com.yc.love.model.bean.MainT2Bean;
import com.yc.love.model.bean.MainT3Bean;
import com.yc.love.model.bean.StringBean;
import com.yc.love.model.engin.LoveEngin;
import com.yc.love.model.single.YcSingle;
import com.yc.love.ui.activity.LoveByStagesDetailsActivity;
import com.yc.love.ui.activity.LoveHealActivity;
import com.yc.love.ui.activity.LoveHealingActivity;
import com.yc.love.ui.frament.base.BaseMainFragment;
import com.yc.love.ui.view.LoadDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mayn on 2019/4/23.
 */

public class MainT1Fragment extends BaseMainFragment {
    //    private List<StringBean> mExampListsBeans;
    private int PAGE_SIZE = 10;
    private int PAGE_NUM = 1;
    private boolean loadMoreEnd;
    private boolean loadDataEnd;
    private boolean showProgressBar = false;
    private int num = 10;
    private MainT1MoreItemAdapter mAdapter;
    private ProgressBarViewHolder progressBarViewHolder;
    private LoveEngin mLoveEngin;
    private List<ExampListsBean> mExampListsBeans;
    private RecyclerView mRecyclerView;

    //    private TextView tvName;

    @Override
    protected int setContentView() {
        return R.layout.fragment_main_t1;
    }


    @Override
    protected void initViews() {
        mLoveEngin = new LoveEngin(mMainActivity);
//        tvName = rootView.findViewById(R.id.main_t1_tv_name);

        mRecyclerView = rootView.findViewById(R.id.main_t1_rl);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mMainActivity);
        mRecyclerView.setLayoutManager(layoutManager);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        //设置增加或删除条目的动画
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    RecyclerViewItemListener recyclerViewItemListener = new RecyclerViewItemListener() {
        @Override
        public void onItemClick(int position) {
//            Toast.makeText(mMainActivity, "onItemClick " + position, Toast.LENGTH_SHORT).show();
            ExampListsBean exampListsBean = mExampListsBeans.get(position);
            LoveByStagesDetailsActivity.startLoveByStagesDetailsActivity(mMainActivity, exampListsBean.id, exampListsBean.post_title);

        }

        @Override
        public void onItemLongClick(int position) {

        }
    };


    @Override
    protected void lazyLoad() {

        netData();
    }

    private void netData() {
        LoadDialog loadDialog = new LoadDialog(mMainActivity);
        loadDialog.show();
        mLoveEngin.indexExample(String.valueOf(PAGE_NUM), String.valueOf(PAGE_SIZE), "example/index").subscribe(new MySubscriber<AResultInfo<ExampDataBean>>(loadDialog) {
            @Override
            protected void onNetNext(AResultInfo<ExampDataBean> exampDataBeanAResultInfo) {
                ExampDataBean exampDataBean = exampDataBeanAResultInfo.data;
                if (exampDataBean != null) {
                    mExampListsBeans = exampDataBean.lists;
                    if (mExampListsBeans == null) {
                        mExampListsBeans = new ArrayList<>();
                    }
                }
                mExampListsBeans.add(0, new ExampListsBean(0, "title"));

                initRecyclerViewData();
            }

            @Override
            protected void onNetError(Throwable e) {

            }

            @Override
            protected void onNetCompleted() {

            }
        });
    }

    private void initRecyclerViewData() {


        mAdapter = new MainT1MoreItemAdapter(mExampListsBeans, mRecyclerView) {
            @Override
            public BaseViewHolder getHolder(ViewGroup parent) {
                return new MainT1ItemHolder(mMainActivity, recyclerViewItemListener, parent);
            }

            @Override
            public BaseViewHolder getTitleHolder(ViewGroup parent) {
                TitleT1ViewHolder titleT1ViewHolder = new TitleT1ViewHolder(mMainActivity, null, parent);
                titleT1ViewHolder.setOnClickShareListent(new TitleT1ViewHolder.OnClickMainT1TitleListent() {
                    @Override
                    public void clickShareListent() {
                        Toast.makeText(mMainActivity, "clickShare", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void clickIvModule02Listent() {
                        startActivity(new Intent(mMainActivity, LoveHealActivity.class));

                    }

                    @Override
                    public void clickIvModule03Listent() {
                        startActivity(new Intent(mMainActivity, LoveHealingActivity.class));
//                        Toast.makeText(mMainActivity, "clickIvModule03", Toast.LENGTH_SHORT).show();
                    }
                });
                return titleT1ViewHolder;
            }

            @Override
            protected RecyclerView.ViewHolder getBarViewHolder(ViewGroup parent) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_test_item_footer, parent, false);
                progressBarViewHolder = new ProgressBarViewHolder(view);
                return progressBarViewHolder;
            }
        };
        mRecyclerView.setAdapter(mAdapter);
        if (mExampListsBeans.size() < PAGE_SIZE) {
            Log.d("ssss", "loadMoreData: data---end");
        } else {
            mAdapter.setOnMoreDataLoadListener(new MainT1MoreItemAdapter.OnLoadMoreDataListener() {
                @Override
                public void loadMoreData() {
                    if (loadDataEnd == false) {
                        return;
                    }
                    if (showProgressBar == false) {
                        //加入null值此时adapter会判断item的type
                        mExampListsBeans.add(null);
                        mAdapter.notifyDataSetChanged();
                        showProgressBar = true;
                    }
                    if (!loadMoreEnd) {
                        netLoadMore();
                    } else {
                        Log.d("mylog", "loadMoreData: loadMoreEnd end 已加载全部数据 ");
                        mExampListsBeans.remove(mExampListsBeans.size() - 1);
                        mAdapter.notifyDataSetChanged();
                    }
                }
            });
        }
        loadDataEnd = true;
    }

    private void netLoadMore() {
        mLoveEngin.indexExample(String.valueOf(++PAGE_NUM), String.valueOf(PAGE_SIZE), "example/index").subscribe(new MySubscriber<AResultInfo<ExampDataBean>>(mMainActivity) {
            @Override
            protected void onNetNext(AResultInfo<ExampDataBean> exampDataBeanAResultInfo) {

                ExampDataBean exampDataBean = exampDataBeanAResultInfo.data;
                if (exampDataBean != null) {
                    List<ExampListsBean> netLoadMoreData = exampDataBean.lists;
                    showProgressBar = false;
                    mExampListsBeans.remove(mExampListsBeans.size() - 1);
                    mAdapter.notifyDataSetChanged();
                    if (netLoadMoreData.size() < PAGE_SIZE) {
                        loadMoreEnd = true;
                    }
                    mExampListsBeans.addAll(netLoadMoreData);
                    mAdapter.notifyDataSetChanged();
                    mAdapter.setLoaded();
                }
            }

            @Override
            protected void onNetError(Throwable e) {

            }

            @Override
            protected void onNetCompleted() {

            }
        });
    }
}
