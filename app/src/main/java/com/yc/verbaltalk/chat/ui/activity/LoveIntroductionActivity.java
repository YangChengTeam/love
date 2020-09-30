package com.yc.verbaltalk.chat.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;

import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTFeedAd;
import com.bytedance.sdk.openadsdk.TTNativeExpressAd;
import com.yc.verbaltalk.R;
import com.yc.verbaltalk.base.activity.BaseSameActivity;
import com.yc.verbaltalk.base.engine.LoveEngine;
import com.yc.verbaltalk.base.utils.UserInfoHelper;
import com.yc.verbaltalk.base.view.imgs.Constant;
import com.yc.verbaltalk.chat.adapter.LoveIntroduceAdapter;
import com.yc.verbaltalk.chat.bean.ExampDataBean;
import com.yc.verbaltalk.chat.bean.ExampListsBean;
import com.yc.verbaltalk.skill.ui.activity.ExampleDetailActivity;

import java.util.List;
import java.util.Map;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import io.reactivex.observers.DisposableObserver;
import yc.com.rthttplibrary.bean.ResultInfo;
import yc.com.rthttplibrary.config.HttpConfig;
import yc.com.toutiao_adv.OnAdvStateListener;
import yc.com.toutiao_adv.TTAdDispatchManager;
import yc.com.toutiao_adv.TTAdType;

/**
 * 入门秘籍 提升列表
 */
public class LoveIntroductionActivity extends BaseSameActivity implements OnAdvStateListener {

    private String mActivityTitle;
    private RecyclerView mRecyclerView;

    private int PAGE_SIZE = 10;
    private int PAGE_NUM = 1;

    private LoveEngine mLoveEngin;
    private String mId;
    private LoveIntroduceAdapter loveIntroduceAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ExampListsBean exampListsBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_love_introduction);
        mLoveEngin = new LoveEngine(this);
        initViews();
        initDatas();
        initListener();
    }

    protected void initViews() {
        initRecyclerView();
    }

    private void initRecyclerView() {
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
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
        loveIntroduceAdapter.setOnItemClickListener((adapter, view, position) -> {
            exampListsBean = loveIntroduceAdapter.getItem(position);

            String brand = Build.BRAND.toLowerCase();
//            TextUtils.equals("huawei", brand) || TextUtils.equals("honor", brand) ||
            if (UserInfoHelper.isVip()) {
                toExampleDetail();
            } else {

                String uid = UserInfoHelper.getUid();
                if (TextUtils.isEmpty(uid)) {
                    uid = "111";
                }

                TTAdDispatchManager.getManager().init(LoveIntroductionActivity.this, TTAdType.REWARD_VIDEO, null, Constant.TOUTIAO_REWARD2_ID, 0, "学习聊天技能", 1, uid, TTAdConstant.VERTICAL, LoveIntroductionActivity.this);


            }

        });
        loveIntroduceAdapter.setOnLoadMoreListener(this::netData, mRecyclerView);

        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(this, R.color.red_crimson));
        swipeRefreshLayout.setOnRefreshListener(() -> {
            PAGE_NUM = 1;
            netData();
        });
    }

    private void netData() {
        if (PAGE_NUM == 1)
            mLoadingDialog.showLoadingDialog();
        mLoveEngin.exampleTsList(mId, String.valueOf(PAGE_NUM), String.valueOf(PAGE_SIZE)).subscribe(new DisposableObserver<ResultInfo<ExampDataBean>>() {
            @Override
            public void onComplete() {
                if (swipeRefreshLayout.isRefreshing()) swipeRefreshLayout.setRefreshing(false);
                if (PAGE_NUM == 1)
                    mLoadingDialog.dismissLoadingDialog();
            }

            @Override
            public void onError(Throwable e) {
                if (PAGE_NUM == 1)
                    mLoadingDialog.dismissLoadingDialog();
            }

            @Override
            public void onNext(ResultInfo<ExampDataBean> exampDataBeanAResultInfo) {
                if (PAGE_NUM == 1)
                    mLoadingDialog.dismissLoadingDialog();
                if (exampDataBeanAResultInfo != null && exampDataBeanAResultInfo.code == HttpConfig.STATUS_OK && exampDataBeanAResultInfo.data != null) {
                    ExampDataBean exampDataBean = exampDataBeanAResultInfo.data;
//                    mExampListsBeans = exampDataBean.lists;
                    createNewData(exampDataBean.lists);
                }
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

    @Override
    public void loadSuccess() {

    }

    @Override
    public void loadFailed() {

    }

    @Override
    public void clickAD() {

//        if (!rewardComplete) {
//            AlertDialog.Builder builder = new AlertDialog.Builder(LoveIntroductionActivity.this);
//            builder.setMessage("关闭广告可能无法学习撩妹技能！！");
//            builder.setPositiveButton("确定", (dialog, which) -> toExampleDetail()).show();
//        } else {
//
//        }
        toExampleDetail();
    }

    @Override
    public void onTTNativeExpressed(List<TTNativeExpressAd> ads) {

    }


    @Override
    public void onDrawFeedAd(List<TTFeedAd> feedAdList) {

    }

    @Override
    public void removeNativeAd(TTNativeExpressAd ad, int position) {

    }

    boolean rewardComplete = false;

    @Override
    public void rewardComplete() {
        rewardComplete = true;
    }

    private void toExampleDetail() {
        if (exampListsBean != null)
            ExampleDetailActivity.startExampleDetailActivity(LoveIntroductionActivity.this, exampListsBean.id, exampListsBean.post_title);
    }
}
