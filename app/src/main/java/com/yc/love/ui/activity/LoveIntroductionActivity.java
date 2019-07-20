package com.yc.love.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.yc.love.R;
import com.yc.love.adaper.rv.LoveIntroduceAdapter;
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

    private int PAGE_SIZE = 10;
    private int PAGE_NUM = 1;

    private LoveEngin mLoveEngin;
    private String mId;
    private LoveIntroduceAdapter loveIntroduceAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_love_introduction);
        mLoveEngin = new LoveEngin(this);
        initViews();
        initDatas();
        initListener();
    }

    protected void initViews() {
        initRecyclerView();
    }

    private void initRecyclerView() {
        mRecyclerView = findViewById(R.id.love_introduction_rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        loveIntroduceAdapter = new LoveIntroduceAdapter(null);
        mRecyclerView.setAdapter(loveIntroduceAdapter);


    }


    private void initDatas() {
        netData();
    }

    private void initListener() {
        loveIntroduceAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ExampListsBean exampListsBean = loveIntroduceAdapter.getItem(position);
                if (exampListsBean != null)
                    ExampleDetailActivity.startExampleDetailActivity(LoveIntroductionActivity.this, exampListsBean.id, exampListsBean.post_title);
            }
        });
        loveIntroduceAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                netData();
            }
        }, mRecyclerView);
    }

    private void netData() {
        if (PAGE_NUM == 1)
            mLoadingDialog.showLoadingDialog();
        mLoveEngin.exampleTsList(mId, String.valueOf(PAGE_NUM), String.valueOf(PAGE_SIZE), "example/ts_lists").subscribe(new MySubscriber<AResultInfo<ExampDataBean>>(mLoadingDialog) {
            @Override
            protected void onNetNext(AResultInfo<ExampDataBean> exampDataBeanAResultInfo) {
                if (exampDataBeanAResultInfo != null && exampDataBeanAResultInfo.code == HttpConfig.STATUS_OK && exampDataBeanAResultInfo.data != null) {
                    ExampDataBean exampDataBean = exampDataBeanAResultInfo.data;
//                    mExampListsBeans = exampDataBean.lists;
                    createNewData(exampDataBean.lists);
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

    private void createNewData(List<ExampListsBean> exampListsBeans) {
        if (PAGE_NUM == 1) {
            loveIntroduceAdapter.setNewData(exampListsBeans);
        } else {
            loveIntroduceAdapter.addData(exampListsBeans);
        }
        if (exampListsBeans.size() == PAGE_SIZE) {
            loveIntroduceAdapter.loadMoreComplete();
            PAGE_NUM++;
        } else {
            loveIntroduceAdapter.loadMoreEnd();
        }


    }


    @Override
    protected void initIntentData() {
        Intent intent = getIntent();
        mActivityTitle = intent.getStringExtra("title");
        mId = intent.getStringExtra("love_id");
    }

    public static void startLoveIntroductionActivity(Context context, String title, String tagId) {
        Intent intent = new Intent(context, LoveIntroductionActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("love_id", tagId);
        context.startActivity(intent);
    }

    @Override
    protected String offerActivityTitle() {
        return mActivityTitle;
    }
}
