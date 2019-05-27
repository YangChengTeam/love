package com.yc.love.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;

import com.yc.love.R;
import com.yc.love.adaper.rv.NoThingAdapter;
import com.yc.love.adaper.rv.base.RecyclerViewItemListener;
import com.yc.love.adaper.rv.holder.BaseViewHolder;
import com.yc.love.adaper.rv.holder.LoveIntroHolder;
import com.yc.love.adaper.rv.holder.StringBeanViewHolder;
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
import com.yc.love.ui.activity.base.BaseSameActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 入门秘籍 提升列表
 */
public class LoveIntroductionActivity extends BaseSameActivity {

    private String mActivityTitle;
    private RecyclerView mRecyclerView;
    private NoThingAdapter<ExampListsBean> mAdapter;
//    private List<StringBean> mExampListsBeans;
    //    private int PAGE_NUM = 10;
    private int PAGE_SIZE = 10;
    private int PAGE_NUM = 1;
    private boolean loadMoreEnd;
    private boolean loadDataEnd;
    private boolean showProgressBar = false;
    private int num = 10;
    LoveEngin mLoveEngin;
    private List<ExampListsBean> mExampListsBeans;
    private String mId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_love_introduction);
        mLoveEngin = new LoveEngin(this);
        initViews();
        initDatas();
    }

    protected void initViews() {
        initRecyclerView();
    }

    private void initRecyclerView() {
        mRecyclerView = findViewById(R.id.love_introduction_rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
    }


    private void initDatas() {
        netData();
    }

    private void netData() {
        mLoadingDialog.showLoadingDialog();
        mLoveEngin.exampleTsList(mId, String.valueOf(PAGE_NUM), String.valueOf(PAGE_SIZE), "example/ts_lists").subscribe(new MySubscriber<AResultInfo<ExampDataBean>>(mLoadingDialog) {
            @Override
            protected void onNetNext(AResultInfo<ExampDataBean> exampDataBeanAResultInfo) {
                ExampDataBean exampDataBean = exampDataBeanAResultInfo.data;
                mExampListsBeans = exampDataBean.lists;

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
      /*  mExampListsBeans = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            StringBean stringBean = new StringBean("name " + i);
            mExampListsBeans.add(stringBean);
        }*/
        mAdapter = new NoThingAdapter<ExampListsBean>(mExampListsBeans, mRecyclerView) {
            @Override
            public BaseViewHolder getHolder(ViewGroup parent) {
                return new LoveIntroHolder(LoveIntroductionActivity.this, recyclerViewItemListener, parent);
            }
        };
        mRecyclerView.setAdapter(mAdapter);
        if (mExampListsBeans.size() < PAGE_SIZE) {
            Log.d("ssss", "loadMoreData: data---end");
        } else {
            mAdapter.setOnMoreDataLoadListener(new NoThingAdapter.OnLoadMoreDataListener() {
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
//        mLoveEngin.indexExample(String.valueOf(++PAGE_NUM), String.valueOf(PAGE_SIZE), "example/index").subscribe(new MySubscriber<AResultInfo<ExampDataBean>>(mMainActivity) {
        mLoveEngin.exampleTsList(mId, String.valueOf(++PAGE_NUM), String.valueOf(PAGE_SIZE), "example/ts_lists").subscribe(new MySubscriber<AResultInfo<ExampDataBean>>(this) {
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
    RecyclerViewItemListener recyclerViewItemListener = new RecyclerViewItemListener() {
        @Override
        public void onItemClick(int position) {
//            ExampListsBean exampListsBean = mExampListsBeans.get(position);
//            LoveByStagesDetailsActivity.startLoveByStagesDetailsActivity(mMainActivity, exampListsBean.id, exampListsBean.post_title);

            ExampListsBean exampListsBean = mExampListsBeans.get(position);
            ExampleDetailActivity.startExampleDetailActivity(LoveIntroductionActivity.this,exampListsBean.id,exampListsBean.post_title);
        }

        @Override
        public void onItemLongClick(int position) {

        }
    };

    @Override
    protected void initIntentData() {
        Intent intent = getIntent();
        mActivityTitle = intent.getStringExtra("title");
        mId = intent.getStringExtra("id");
    }

    public static void startLoveIntroductionActivity(Context context, String title, String tagId) {
        Intent intent = new Intent(context, LoveIntroductionActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("id", tagId);
        context.startActivity(intent);
    }

    @Override
    protected String offerActivityTitle() {
        return mActivityTitle;
    }
}
