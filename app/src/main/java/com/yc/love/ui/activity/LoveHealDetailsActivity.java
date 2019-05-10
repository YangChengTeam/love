package com.yc.love.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
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
import com.yc.love.adaper.rv.LoveHealDetailsAdapter;
import com.yc.love.adaper.rv.base.RecyclerViewItemListener;
import com.yc.love.adaper.rv.holder.BaseViewHolder;
import com.yc.love.adaper.rv.holder.LoveHealDetItemHolder;
import com.yc.love.adaper.rv.holder.LoveHealDetVipHolder;
import com.yc.love.adaper.rv.holder.ProgressBarViewHolder;
import com.yc.love.model.base.MySubscriber;
import com.yc.love.model.bean.AResultInfo;
import com.yc.love.model.bean.LoveHealDetBean;
import com.yc.love.model.bean.LoveHealDetDetailsBean;
import com.yc.love.model.engin.LoveEngin;
import com.yc.love.ui.activity.base.BaseSameActivity;

import java.util.List;

public class LoveHealDetailsActivity extends BaseSameActivity {

    private String mTitle;
    private String mCategoryId;
    private LoveEngin mLoveEngin;
    private LoveHealDetailsAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private int PAGE_SIZE = 8;
    private int PAGE_NUM = 1;
    private List<LoveHealDetBean> mLoveHealDetBeans;
    private boolean loadMoreEnd;
    private boolean loadDataEnd;
    private boolean showProgressBar = false;

    @Override
    protected void initIntentData() {
        Intent intent = getIntent();
        mTitle = intent.getStringExtra("title");
        mCategoryId = intent.getStringExtra("category_id");
    }

    public static void startLoveHealDetailsActivity(Context context, String title, String categoryId) {
        Intent intent = new Intent(context, LoveHealDetailsActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("category_id", categoryId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_love_heal_details);
        mLoveEngin = new LoveEngin(this);
        initViews();
        initData();
    }

    private void initData() {
        netData();
    }


    private void initViews() {
        initRecyclerView();
    }

    private void initRecyclerView() {
        mRecyclerView = findViewById(R.id.love_heal_details_rl);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        //设置增加或删除条目的动画
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

    }

    private void netData() {
        mLoadingDialog.show();
        mLoveEngin.loveListCategory(mCategoryId, String.valueOf(PAGE_NUM), String.valueOf(PAGE_SIZE), "Dialogue/lists").subscribe(new MySubscriber<AResultInfo<List<LoveHealDetBean>>>(mLoadingDialog) {


            @Override
            protected void onNetNext(AResultInfo<List<LoveHealDetBean>> listAResultInfo) {
                mLoveHealDetBeans = listAResultInfo.data;
                Log.d("mylog", "onNetNext: loveHealDetBeans.size() " + mLoveHealDetBeans.size());
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
        mAdapter = new LoveHealDetailsAdapter(mLoveHealDetBeans, mRecyclerView) {
            @Override
            public BaseViewHolder getHolder(ViewGroup parent) {
                LoveHealDetItemHolder loveHealDetItemHolder = new LoveHealDetItemHolder(LoveHealDetailsActivity.this, null, parent);
                loveHealDetItemHolder.setOnClickCopyListent(new LoveHealDetItemHolder.OnClickCopyListent() {
                    @Override
                    public void onClickCopy(LoveHealDetDetailsBean detailsBean) {
                        Log.d("mylog", "onClickCopy: detailsBean " + detailsBean.toString());
                        LoveHealDetailsActivity.this.showToastShort(detailsBean.content);
                    }
                });
                return loveHealDetItemHolder;
            }

            @Override
            protected RecyclerView.ViewHolder getPayVipHolder(ViewGroup parent) {
                return new LoveHealDetVipHolder(LoveHealDetailsActivity.this, recyclerViewItemListener, parent);
            }
            @Override
            protected RecyclerView.ViewHolder getBarViewHolder(ViewGroup parent) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_test_item_footer, parent, false);
                ProgressBarViewHolder progressBarViewHolder = new ProgressBarViewHolder(view);
                return progressBarViewHolder;
            }
        };
        mRecyclerView.setAdapter(mAdapter);
        if (mLoveHealDetBeans.size() < PAGE_SIZE) {
            Log.d("ssss", "loadMoreData: data---end");
        } else {
            mAdapter.setOnMoreDataLoadListener(new LoveHealDetailsAdapter.OnLoadMoreDataListener() {
                @Override
                public void loadMoreData() {
                    if (loadDataEnd == false) {
                        return;
                    }
                    if (showProgressBar == false) {
                        //加入null值此时adapter会判断item的type
                        mLoveHealDetBeans.add(null);
                        mAdapter.notifyDataSetChanged();
                        showProgressBar = true;
                    }
                    if (!loadMoreEnd) {
                        netLoadMore();
                    } else {
                        Log.d("mylog", "loadMoreData: loadMoreEnd end 已加载全部数据 ");
                        mLoveHealDetBeans.remove(mLoveHealDetBeans.size() - 1);
                        mAdapter.notifyDataSetChanged();
                    }
                }
            });
        }
        loadDataEnd = true;
    }

    private void netLoadMore() {
        mLoveEngin.loveListCategory(mCategoryId, String.valueOf(PAGE_NUM++), String.valueOf(PAGE_SIZE), "Dialogue/lists").subscribe(new MySubscriber<AResultInfo<List<LoveHealDetBean>>>(mLoadingDialog) {


            @Override
            protected void onNetNext(AResultInfo<List<LoveHealDetBean>> listAResultInfo) {
                List<LoveHealDetBean> netLoadMoreData = listAResultInfo.data;
                Log.d("mylog", "onNetNext: loveHealDetBeans.size() " + mLoveHealDetBeans.size());
//                initRecyclerData();
//                List<LoveHealDetBean> netLoadMoreData = new ArrayList<>();
                showProgressBar = false;
                mLoveHealDetBeans.remove(mLoveHealDetBeans.size() - 1);
                mAdapter.notifyDataSetChanged();
                if (netLoadMoreData.size() < PAGE_SIZE) {
                    loadMoreEnd = true;
                }
                mLoveHealDetBeans.addAll(netLoadMoreData);
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

            //TODO 购买VIP刷新数据
            startActivity(new Intent(LoveHealDetailsActivity.this, BecomeVipActivity.class));
        }

        @Override
        public void onItemLongClick(int position) {

        }
    };


    @Override
    protected String offerActivityTitle() {
        return mTitle;
    }

}
